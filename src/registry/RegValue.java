/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.ptr.IntByReference;

/**
 *
 * @author henry.wang.1
 */
public class RegValue 
{
	private static final Advapi32 REGS = Advapi32.INSTANCE;
	
	private final RegSubKey key;
	private final String name;
	private String type;
	private Object value;
	
	public RegValue(RegSubKey key, String name) {
		this.key = key;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void refresh()
	{
		key.open(false);
		IntByReference lpType = new IntByReference();
		REGS.RegQueryValueEx(key.getHKey(), name, 0, lpType, null, null);
		
	}
}
