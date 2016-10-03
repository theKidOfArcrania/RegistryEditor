/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author henry.wang.1
 */
public class RegValue 
{
	public enum RegType
	{
		REG_BINARY, REG_DWORD, REG_EXPAND_SZ, REG_MULTI_SZ, REG_NONE, REG_QWORD, REG_SZ;
	}
	private static final Advapi32 REGS = Advapi32.INSTANCE;
	
	private final RegKey key;
	private final String name;
	private RegType type;
	private Object value;
	
	RegValue(RegKey key, String name) {
		this.key = key;
		this.name = name;
	}

	public String getValueName() {
		return name;
	}
	
	public RegType getValueType()
	{
		return type;
	}
	
	public Object getValue()
	{
		return value;
	}
	
	public void refresh()
	{
		IntByReference lpType = new IntByReference();
		REGS.RegQueryValueEx(key.getHKey(), name, 0, lpType, (Pointer)null, null);
		switch (lpType.getValue())
		{
			case WinNT.REG_BINARY:
				type = RegType.REG_BINARY;
				value = Advapi32Util.registryGetBinaryValue(key.getHKey(), name);
				break;
			case WinNT.REG_DWORD:
				type = RegType.REG_DWORD;
				value = Advapi32Util.registryGetIntValue(key.getHKey(), name);
				break;
			case WinNT.REG_EXPAND_SZ:
				type = RegType.REG_EXPAND_SZ;
				value = Advapi32Util.registryGetStringValue(key.getHKey(), name);
				break;
			case WinNT.REG_MULTI_SZ:
				type = RegType.REG_MULTI_SZ;
				value = Advapi32Util.registryGetStringArray(key.getHKey(), name);
				break;
			case WinNT.REG_NONE:
				type = RegType.REG_NONE;
				value = null;
				break;
			case WinNT.REG_QWORD:
				type = RegType.REG_SZ;
				value = Advapi32Util.registryGetLongValue(key.getHKey(), name);
				break;
			case WinNT.REG_SZ:
				type = RegType.REG_SZ;
				value = Advapi32Util.registryGetStringValue(key.getHKey(), name);
		}
	}
}
