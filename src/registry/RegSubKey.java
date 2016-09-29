/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;
import java.util.HashMap;

/**
 *
 * @author henry.wang.1
 */
public class RegSubKey implements RegKey {

	private static final Advapi32 REGS = Advapi32.INSTANCE;

	private HKEYByReference parent;
	private final String name;
	private final HKEYByReference hKey;
	private final HashMap<String, RegSubKey> subKeys;
	private RegSubKey[] subKeysList;

	private boolean attempted;
	private boolean opened;
	private boolean writable;

	public RegSubKey(HKEYByReference parent, String name) {
		this.parent = parent;
		this.name = name;
		this.hKey = new HKEYByReference();
		subKeys = new HashMap<>();
	}

	public void open(boolean write) throws Win32Exception {
		if (opened && !write || opened && write && writable) {
			return;
		}
		int rc = REGS.RegOpenKeyEx(parent.getValue(), name, 0, (write
			? WinNT.KEY_ALL_ACCESS : WinNT.KEY_READ), hKey);
		if (rc != W32Errors.ERROR_SUCCESS) {
			throw new Win32Exception(rc);
		}
		opened = true;
		writable = write;
	}

	@Override
	public RegSubKey[] getSubKeys() {
		if (!attempted) 
			refresh();
		return subKeysList;
	}

	public String getName() {
		return name;
	}

	public void close() {
		attempted = false;
		if (opened) {
			Win32Exception err = null;
			try
			{
				Advapi32Util.registryCloseKey(hKey.getValue());
			}
			catch (Win32Exception e)
			{
				err = e;
			}
			
			
			for (RegSubKey sub : subKeys.values())
			{
				try
				{
					sub.close();
				}
				catch (Win32Exception e)
				{
					//Don't care about closing errors
					e.printStackTrace();
				}
			}
			
			subKeys.clear();
			subKeysList = null;
			opened = writable = false;
			if (err != null)
				throw err;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public RegValue[] getValues() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void refresh() {
		refresh(null);
	}
	
	@Override
	public void refresh(HKEY newHKey) {
		attempted = true;
		subKeysList = new RegSubKey[0];
		
		open(false);
		String[] subKeyNames = Advapi32Util.registryGetKeys(hKey.getValue());
		
		subKeysList = new RegSubKey[subKeyNames.length];
		for (int i = 0; i < subKeysList.length; i++) {
			if (subKeys.containsKey(subKeyNames[i]))
			{
				subKeysList[i] = subKeys.get(subKeyNames[i]);
				subKeysList[i].refresh();
			}
			else
			{
				subKeysList[i] = new RegSubKey(hKey, subKeyNames[i]);
				subKeys.put(subKeyNames[i], subKeysList[i]);
			}
		}
	}
}
