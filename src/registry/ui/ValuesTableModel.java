/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry.ui;

import com.sun.jna.platform.win32.Win32Exception;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import registry.RegKey;
import registry.RegValue;

/**
 *
 * @author Henry
 */
public class ValuesTableModel extends AbstractTableModel 
{

	private static final Logger LOG = Logger.getLogger(ValuesTableModel.class.getName());

	private static String[] COLUMN_NAMES = {
		"Name", "Type", "Data"
	};

	private RegKey key;
	private RegValue[] values = null;

	public ValuesTableModel(RegKey key) {
		this.key = key;
		if (key != null)
			updateValues();
	}

	@Override
	public String getColumnName(int column) {
		return COLUMN_NAMES[column];
	}

	@Override
	public int getRowCount() {
		return values == null ? 0 : values.length;
	}

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (values == null)
			throw new ArrayIndexOutOfBoundsException();
		
		RegValue val = values[rowIndex];
		switch (columnIndex)
		{
		case 0: //Value name
			return val.getValueName();
		case 1: //Value type
			return val.getValueType();
		case 2: //Value
			return val.getValue();
		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	public void updateValues() {
		updateValues(key);
	}
	public void updateValues(RegKey newKey) {
		try {
			int oldSize = values.length;
			RegValue[] loaded = key.getValues();
			key = newKey;
			
			values = new RegValue[0];
			fireTableRowsDeleted(0, oldSize - 1);
			
			values = loaded;
			fireTableRowsInserted(0, values.length - 1);
		} catch (Win32Exception e) {
			LOG.log(Level.SEVERE, "Error " + (e.getHR().intValue() & 0xFFFF)
				+ " (" + e.getMessage() + ")", e);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
			System.exit(1);
		}
	}
}
