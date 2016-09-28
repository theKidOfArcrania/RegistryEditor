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
import com.sun.jna.platform.win32.WinReg.HKEYByReference;

/**
 *
 * @author henry.wang.1
 */
public class RegSubKey implements RegKey {

	private static final Advapi32 REGS = Advapi32.INSTANCE;

	private final HKEYByReference parent;
	private final String name;
	private final HKEYByReference hKey;
	private RegSubKey[] subKeys;

	private boolean opened;
	private boolean writable;

	public RegSubKey(HKEYByReference parent, String name) {
		this.parent = parent;
		this.name = name;
		this.hKey = new HKEYByReference();
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
		if (subKeys != null) {
			return subKeys;
		}

		subKeys = new RegSubKey[0];

		open(false);
		String[] subKeyNames = Advapi32Util.registryGetKeys(hKey.getValue());

		subKeys = new RegSubKey[subKeyNames.length];
		for (int i = 0; i < subKeys.length; i++) {
			subKeys[i] = new RegSubKey(hKey, subKeyNames[i]);
		}
		return subKeys;
	}

	public String getName() {
		return name;
	}

	public void close() {
		if (opened) {
			Advapi32Util.registryCloseKey(hKey.getValue());
		}
		subKeys = null;
		opened = writable = false;
	}

	@Override
	public String toString() {
		return name;
	}
}
