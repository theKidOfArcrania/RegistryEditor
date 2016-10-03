/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author henry.wang.1
 */
public interface RegKey {
	public static final Advapi32 REGS = Advapi32.INSTANCE;
	
	public static String[] getValueNames(HKEY hKey)
	{
		Advapi32Util.InfoKey info = Advapi32Util.registryQueryInfoKey(hKey, 0);
		String[] valueNames = new String[info.lpcValues.getValue()];
		
		char[] lpValueName = new char[info.lpcMaxValueNameLen.getValue()];
		IntByReference lpcchValueName = new IntByReference(0);
		for (int i = 0; i < valueNames.length; i++)
		{
			int status = REGS.RegEnumValue(hKey, i, lpValueName, lpcchValueName, 
					null, null, null, null);
			if (status != WinError.ERROR_MORE_DATA && 
					status != WinError.ERROR_SUCCESS &&
					status != WinError.ERROR_NO_MORE_ITEMS)
				throw new Win32Exception(status);
			
			valueNames[i] = new String(lpValueName, 0, 
					lpcchValueName.getValue());
		}
		return valueNames;
	}
	
	RegSubKey[] getSubKeys();

	RegValue[] getValues();

	void refresh();

	HKEY getHKey();
}
