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
	private RegValue[] valuesList;
	private final HashMap<String, RegValue> values;

	private RegKeyRoot(HKEY root) {
		this.hKey = root;
		subKeysList = new RegSubKey[0];
		subKeys = new HashMap<>();
		valuesList = new RegValue[0];
		values = new HashMap<>();
	}

	@Override
	public HKEY getHKey() {
		return hKey;
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

	@Override
	public void refresh() {
		attempted = true;

		//Load sub-keys
		String[] subKeyNames = Advapi32Util.registryGetKeys(hKey);
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
		
		//Load values.
		String[] valueNames = RegKey.getValueNames(hKey);
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
}
