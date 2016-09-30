/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.WinReg.HKEY;
import java.util.HashMap;

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

	private boolean attempted = false;
	private final HKEY hKey;
	private RegSubKey[] subKeysList;
	private final HashMap<String, RegSubKey> subKeys;

	private RegKeyRoot(HKEY root) {
		this.hKey = root;
		subKeys = new HashMap<>();
	}

	@Override
	public HKEY getHKey() {
		return hKey;
	}

	@Override
	public RegSubKey[] getSubKeys() {
		attempted = false;
		if (!attempted) {
			refresh();
		}
		return subKeysList;
	}

	@Override
	public RegValue[] getValues() {
		//TO DO: Implement this method.
		return null;
	}

	@Override
	public void refresh() {
		subKeysList = new RegSubKey[0];

		String[] subKeyNames = Advapi32Util.registryGetKeys(hKey);
		subKeysList = new RegSubKey[subKeyNames.length];
		for (int i = 0; i < subKeysList.length; i++) {
			if (subKeys.containsKey(subKeyNames[i])) {
				subKeysList[i] = subKeys.get(subKeyNames[i]);
				subKeysList[i].refresh();
			} else {
				subKeysList[i] = new RegSubKey(this, subKeyNames[i]);
				subKeys.put(subKeyNames[i], subKeysList[i]);
			}
		}
	}

}
