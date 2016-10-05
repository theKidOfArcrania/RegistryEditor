/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

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

	private boolean attempted;
	private final HKEYByReference hKey;
	private final String name;
	private boolean opened;
	private RegKey parent;
	
	private boolean writable;

	private RegSubKey[] subKeysList;
	private final HashMap<String, RegSubKey> subKeys;
	private RegValue[] valuesList;
	private final HashMap<String, RegValue> values;
	
	RegSubKey(RegKey parent, String name) {
		this.parent = parent;
		this.name = name;
		this.hKey = new HKEYByReference();
		subKeys = new HashMap<>();
		subKeysList = new RegSubKey[0];
		valuesList = new RegValue[0];
		values = new HashMap<>();
	}

	public void close() {
		attempted = false;
		if (opened) {
			Win32Exception err = null;
			try {
				Advapi32Util.registryCloseKey(hKey.getValue());
			} catch (Win32Exception e) {
				err = e;
			}

			for (RegSubKey sub : subKeys.values()) {
				try {
					sub.close();
				} catch (Win32Exception e) {
					//Don't care about closing errors
					e.printStackTrace();
				}
			}

			subKeys.clear();
			subKeysList = null;
			opened = writable = false;
			if (err != null) {
				throw err;
			}
		}
	}

	@Override
	public HKEY getHKey() {
		if (!attempted)
			refresh();
		return hKey.getValue();
	}

	public String getName() {
		return name;
	}

	@Override
	public RegSubKey[] getSubKeys() {
		if (!attempted) {
			refresh();
		}
		return subKeysList;
	}

	@Override
	public RegValue[] getValues() {
		if (!attempted)
			refresh();
		return valuesList;
	}

	public void open(boolean write) throws Win32Exception {
		if (opened && !write || opened && write && writable) {
			return;
		}
		int rc = REGS.RegOpenKeyEx(parent.getHKey(), name, 0, (write
			? WinNT.KEY_ALL_ACCESS : WinNT.KEY_READ), hKey);
		if (rc != W32Errors.ERROR_SUCCESS) {
			throw new Win32Exception(rc);
		}
		opened = true;
		writable = write;
	}

	@Override
	public void refresh() {
		attempted = true;
		open(false);
		
		//Load sub keys
		String[] subKeyNames = Advapi32Util.registryGetKeys(hKey.getValue());
		RegSubKey[] keysLoading = new RegSubKey[subKeyNames.length];
		for (int i = 0; i < keysLoading.length; i++) {
			if (subKeys.containsKey(subKeyNames[i])) {
				keysLoading[i] = subKeys.get(subKeyNames[i]);
				keysLoading[i].refresh();
			} else {
				keysLoading[i] = new RegSubKey(this, subKeyNames[i]);
				subKeys.put(subKeyNames[i], keysLoading[i]);
			}
		}
		subKeysList = keysLoading;
		
		//Load values
		String[] valueNames = RegKey.getValueNames(hKey.getValue());
		RegValue[] valuesLoading = new RegValue[valueNames.length];
		for (int i = 0; i < valuesLoading.length; i++) {
			if (values.containsKey(valueNames[i])) {
				valuesLoading[i] = values.get(valueNames[i]);
				valuesLoading[i].refresh();
			} else {
				valuesLoading[i] = new RegValue(this, valueNames[i]);
				values.put(valueNames[i], valuesLoading[i]);
			}
		}
		valuesList = valuesLoading;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}
}
