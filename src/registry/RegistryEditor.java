/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RegistryEditor.java
 *
 * Created on Sep 26, 2016, 10:05:20 AM
 */
package registry;

import com.sun.jna.platform.win32.Win32Exception;
import java.util.function.Supplier;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author henry.wang.1
 */
public class RegistryEditor extends javax.swing.JFrame {
	
	private class ValuesTableModel extends AbstractTableModel
	{
		
		
		@Override
		public int getRowCount() {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public int getColumnCount() {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
		
		public void updateValues()
		{
			
		}
	}
	
	private static final RegKeyRoot[] ROOT_KEYS = RegKeyRoot.values();
	private static Object ROOT = new Object();

	private <R> R tryAction(Supplier<R> action) {
		return tryAction(action, null);
	}

	private <R> R tryAction(Supplier<R> action, R defaultVal) {
		try {
			return action.get();
		} catch (Win32Exception e) {
			JOptionPane.showMessageDialog(this, "Error " + e.getHR().intValue()
				+ "(" + e.getMessage() + ")", getTitle(),
				JOptionPane.ERROR_MESSAGE);
			return defaultVal;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Unexpected error: "
				+ e.getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
			return defaultVal;
		}
	}

	private class RegKeyModel implements TreeModel {

		@Override
		public Object getRoot() {
			return ROOT;
		}

		@Override
		public Object getChild(Object o, int i) {
			if (o == ROOT) {
				return ROOT_KEYS[i];
			} else {
				return tryAction(() -> ((RegKey) o).getSubKeys()[i]);
			}
		}

		@Override
		public int getChildCount(Object o) {
			if (o == ROOT) {
				return ROOT_KEYS.length;
			} else {
				return tryAction(() -> ((RegKey) o).getSubKeys().length, 0);
			}
		}

		@Override
		public boolean isLeaf(Object o) {
			return false;
		}

		@Override
		public void valueForPathChanged(TreePath tp, Object o) {

		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			if (parent == ROOT) {
				return ((RegKeyRoot) child).ordinal();
			} else {
				RegKey[] subKeys = tryAction(() -> ((RegKey) parent).getSubKeys());
				for (int i = 0; i < subKeys.length; i++) {
					if (subKeys[i] == child) {
						return i;
					}
				}
				return -1;
			}
		}

		@Override
		public void addTreeModelListener(TreeModelListener tl) {
			//Does nothing yet
		}

		@Override
		public void removeTreeModelListener(TreeModelListener tl) {
			//Does nothing yet
		}

	}

	/**
	 * Creates new form RegistryViewer
	 */
	public RegistryEditor() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING:
	 * Do NOT modify this code. The content of this method is always regenerated by the
	 * Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splRegEditing = new javax.swing.JSplitPane();
        srpRegKeys = new javax.swing.JScrollPane();
        treRegKeys = new javax.swing.JTree();
        srpRegValues = new javax.swing.JScrollPane();
        tblRegValues = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Registry Editor UnBlocked");
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        srpRegKeys.setMinimumSize(new java.awt.Dimension(200, 200));
        srpRegKeys.setPreferredSize(new java.awt.Dimension(200, 200));

        treRegKeys.setModel(new RegKeyModel());
        treRegKeys.setRootVisible(false);
        treRegKeys.setShowsRootHandles(true);
        srpRegKeys.setViewportView(treRegKeys);

        splRegEditing.setLeftComponent(srpRegKeys);

        srpRegValues.setBackground(new java.awt.Color(255, 255, 255));

        tblRegValues.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Name", "Type", "Data"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRegValues.setGridColor(new java.awt.Color(204, 204, 204));
        tblRegValues.setSelectionBackground(java.awt.SystemColor.activeCaption);
        tblRegValues.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblRegValues.setShowHorizontalLines(false);
        srpRegValues.setViewportView(tblRegValues);

        splRegEditing.setRightComponent(srpRegValues);

        getContentPane().add(splRegEditing);

        setBounds(0, 0, 826, 458);
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/*
		 * Set the Nimbus look and feel
		 */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(RegistryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(RegistryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(RegistryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(RegistryEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		//</editor-fold>

		/*
		 * Create and display the form
		 */
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				new RegistryEditor().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane splRegEditing;
    private javax.swing.JScrollPane srpRegKeys;
    private javax.swing.JScrollPane srpRegValues;
    private javax.swing.JTable tblRegValues;
    private javax.swing.JTree treRegKeys;
    // End of variables declaration//GEN-END:variables
}
