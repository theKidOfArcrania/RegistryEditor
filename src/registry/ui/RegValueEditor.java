/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry.ui;

import java.awt.Window;
import registry.RegValue.RegValueType;

/**
 *
 * @author henry.wang.1
 */
public interface RegValueEditor {
	/** 
	 * Shows the editor for this registry value.
	 * @param owner the owner that the dialog will be to.
	 * @param name the value name
	 * @param type the value type
	 * @param val the value before
	 * @return the value if the the user entered valid input, <code>null</code>
	 * if user canceled action
	 */
	public Object showEditor(Window owner, String name, RegValueType type, Object val);
	
}
