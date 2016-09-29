/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry;

/**
 *
 * @author henry.wang.1
 */
public interface RegKey {

	RegSubKey[] getSubKeys();

	RegValue[] getValues();
	
	void refresh();
}
