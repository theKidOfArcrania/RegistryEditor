/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registryviewer;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;

/**
 *
 * @author henry.wang.1
 */
public enum RegKeyRoot implements RegKey {

	HKEY_CLASSES_ROOT(WinReg.HKEY_CLASSES_ROOT),
	HKEY_CURRENT_USER(WinReg.HKEY_CURRENT_USER),
	HKEY_LOCAL_MACHINE(WinReg.HKEY_LOCAL_MACHINE),
	HKEY_USERS(WinReg.HKEY_USERS),
	HKEY_CURRENT_CONFIG(WinReg.HKEY_CURRENT_CONFIG);

	private final HKEYByReference hKey;
	private RegSubKey[] subKeys;

	private RegKeyRoot(HKEY root) {
		this.hKey = new HKEYByReference(root);
	}

	@Override
	public RegSubKey[] getSubKeys() {
		if (subKeys != null) {
			return subKeys;
		}

		subKeys = new RegSubKey[0];

		String[] subKeyNames = Advapi32Util.registryGetKeys(hKey.getValue());
		subKeys = new RegSubKey[subKeyNames.length];
		for (int i = 0; i < subKeys.length; i++) {
			subKeys[i] = new RegSubKey(hKey, subKeyNames[i]);
		}
		return subKeys;
	}
}
