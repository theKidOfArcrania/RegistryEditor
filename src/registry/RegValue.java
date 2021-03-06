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
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author henry.wang.1
 */
public class RegValue
{

	public enum RegValueType
	{

		REG_BINARY("Binary"), REG_DWORD("DWORD (32-bit)"), REG_EXPAND_SZ("String"),
		REG_MULTI_SZ("Strings"), REG_QWORD("QWORD (64-bit)"), REG_SZ("String");

		private final String shortName;
		private final ImageIcon icon;
		private RegValueType(String shortName)
		{
			this.shortName = shortName;
			URL imgURL = ClassLoader.getSystemResource("registry/res/" + name() + "_ICON.png");
			if (imgURL != null)
				icon = new ImageIcon(imgURL);
			else
				icon = null;
		}

		public Icon getIcon()
		{
			return icon;
		}
		
		public String getShortName()
		{
			return shortName;
		}
		
	}
	private static final Advapi32 REGS = Advapi32.INSTANCE;

	private boolean attempted = false;
	private final RegKey key;
	private final String name;
	private RegValueType type;
	private Object value;

	RegValue(RegKey key, String name)
	{
		this.key = key;
		this.name = name;
	}

	public String getValueName()
	{
		return name;
	}

	public RegValueType getValueType()
	{
		if (!attempted)
			refresh();
		return type;
	}

	public Object getValue()
	{
		if (!attempted)
			refresh();
		return value;
	}

	public void refresh()
	{
		IntByReference lpType = new IntByReference();
		REGS.RegQueryValueEx(key.getHKey(), name, 0, lpType, (Pointer) null, null);
		switch (lpType.getValue())
		{
			case WinNT.REG_BINARY:
				type = RegValueType.REG_BINARY;
				value = Advapi32Util.registryGetBinaryValue(key.getHKey(), name);
				break;
			case WinNT.REG_DWORD:
				type = RegValueType.REG_DWORD;
				value = Advapi32Util.registryGetIntValue(key.getHKey(), name);
				break;
			case WinNT.REG_EXPAND_SZ:
				type = RegValueType.REG_EXPAND_SZ;
				value = Advapi32Util.registryGetStringValue(key.getHKey(), name);
				break;
			case WinNT.REG_MULTI_SZ:
				type = RegValueType.REG_MULTI_SZ;
				value = Advapi32Util.registryGetStringArray(key.getHKey(), name);
				break;
			case WinNT.REG_NONE:
				type = RegValueType.REG_SZ;
				value = null;
				break;
			case WinNT.REG_QWORD:
				type = RegValueType.REG_QWORD;
				value = Advapi32Util.registryGetLongValue(key.getHKey(), name);
				break;
			case WinNT.REG_SZ:
				type = RegValueType.REG_SZ;
				value = Advapi32Util.registryGetStringValue(key.getHKey(), name);
		}
	}
}
