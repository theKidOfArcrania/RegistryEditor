/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry.ui;

import java.awt.Window;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import registry.RegValue.RegValueType;

/**
 *
 * @author henry.wang.1
 */
public class RegIntegerEditor extends JPanel implements RegValueEditor
{
	
	private enum NumberBase
	{
		Decimal(10), Hexadecimal(16);
		
		private final int base;

		private NumberBase(int base) {
			this.base = base;
		}
	}
	
	private boolean canceled = false;
	private RegValueType type;
	private NumberBase base = NumberBase.Decimal;
	private Object value;
	
	public class IntFilter extends DocumentFilter
	{
		
		@Override
		public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
			Document doc = fb.getDocument();
			String result = doc.getText(0, offset) + text
					+ doc.getText(offset, doc.getLength() - 
							offset);
			
			if (changeValue(result))
				fb.insertString(offset, text, attrs);
		}

		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			Document doc = fb.getDocument();
			String result = doc.getText(0, offset)
					+ doc.getText(offset + length, doc.getLength() - 
							offset - length);
			if (changeValue(result))
				fb.remove(offset, length);
		}

		
		
		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			Document doc = fb.getDocument();
			String result = doc.getText(0, offset) + text
					+ doc.getText(offset + length, doc.getLength() - 
							offset - length);
			if (changeValue(result))
				fb.replace(offset, length, text, attrs);
		}
		
		private boolean changeValue(String num)
		{
			try
			{
				switch (type)
				{
				case REG_DWORD:
					if (num.isEmpty())
						value = 0;
					else
						value = Integer.parseInt(num, base.base);
					break;
				case REG_QWORD:
					if (num.isEmpty())
						value = 0L;
					else
						value = Long.parseLong(num, base.base);
					break;
				default:
					throw new InternalError();
				}
				return true;
			}
			catch (NumberFormatException e)
			{
				return false;
			}
		}
	}
	
	@Override
	public Object showEditor(Window owner, String name, RegValueType type, Object val) {
		this.type = type;
		txtValueName.setText(name);
		txtValueData.setText(val.toString());
		
		canceled = true;

		//SHOW
		
		if (canceled)
			return null;
		else
			return value;
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btngpBase = new javax.swing.ButtonGroup();
        lblValueName = new javax.swing.JLabel();
        txtValueName = new javax.swing.JTextField();
        lblValueData = new javax.swing.JLabel();
        txtValueData = new javax.swing.JTextField();
        pnlBase = new javax.swing.JPanel();
        rbnDeci = new javax.swing.JRadioButton();
        rbnHex = new javax.swing.JRadioButton();
        pnlButtons = new javax.swing.JPanel();
        btnOkay = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblValueName.setText("Value Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        getContentPane().add(lblValueName, gridBagConstraints);

        txtValueName.setEditable(false);
        txtValueName.setText("<NAME>");
        txtValueName.setMinimumSize(new java.awt.Dimension(300, 20));
        txtValueName.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 10);
        getContentPane().add(txtValueName, gridBagConstraints);

        lblValueData.setText("Value Data:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        getContentPane().add(lblValueData, gridBagConstraints);

        txtValueData.setText("0");
        txtValueData.setMinimumSize(new java.awt.Dimension(100, 20));
        txtValueData.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 10);
        getContentPane().add(txtValueData, gridBagConstraints);

        pnlBase.setBorder(javax.swing.BorderFactory.createTitledBorder("Base"));
        pnlBase.setMinimumSize(new java.awt.Dimension(150, 69));
        pnlBase.setPreferredSize(new java.awt.Dimension(150, 69));
        pnlBase.setLayout(new java.awt.GridLayout(2, 0));

        btngpBase.add(rbnDeci);
        rbnDeci.setSelected(true);
        rbnDeci.setText("Decimal");
        pnlBase.add(rbnDeci);

        btngpBase.add(rbnHex);
        rbnHex.setText("Hexadecimal");
        pnlBase.add(rbnHex);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 10);
        getContentPane().add(pnlBase, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        btnOkay.setText("Okay");
        btnOkay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkayActionPerformed(evt);
            }
        });
        pnlButtons.add(btnOkay);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        pnlButtons.add(btnCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 10);
        getContentPane().add(pnlButtons, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        canceled = true;
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkayActionPerformed
        canceled = false;
    }//GEN-LAST:event_btnOkayActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(RegIntegerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(RegIntegerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(RegIntegerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(RegIntegerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				RegIntegerEditor dialog = new RegIntegerEditor(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOkay;
    private javax.swing.ButtonGroup btngpBase;
    private javax.swing.JLabel lblValueData;
    private javax.swing.JLabel lblValueName;
    private javax.swing.JPanel pnlBase;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JRadioButton rbnDeci;
    private javax.swing.JRadioButton rbnHex;
    private javax.swing.JTextField txtValueData;
    private javax.swing.JTextField txtValueName;
    // End of variables declaration//GEN-END:variables

}