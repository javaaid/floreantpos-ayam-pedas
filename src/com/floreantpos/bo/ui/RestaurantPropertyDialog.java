/*
 * RestaurantPropertyDialog.java
 *
 * Created on March 10, 2007, 2:05 PM
 */

package com.floreantpos.bo.ui;

import com.floreantpos.POSConstants;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

/**
 *
 * @author  mshahriar
 */
public class RestaurantPropertyDialog extends POSDialog {
	RestaurantDAO dao;
	Restaurant restaurant;

	/** Creates new form RestaurantPropertyDialog */
	public RestaurantPropertyDialog() {
		super(BackOfficeWindow.getInstance(), true);
		
		setTitle(com.floreantpos.POSConstants.CONFIGURE);
		
		initComponents();

		dao = new RestaurantDAO();
		restaurant = dao.get(1);

		tfName.setText(restaurant.getName());
		tfAddrLine1.setText(restaurant.getAddressLine1());
		tfAddrLine2.setText(restaurant.getAddressLine2());
		tfCapacity.setText(String.valueOf(restaurant.getCapacity()));
		tfTables.setText(String.valueOf(restaurant.getTables()));
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		tfName = new javax.swing.JTextField();
		tfAddrLine1 = new javax.swing.JTextField();
		tfAddrLine2 = new javax.swing.JTextField();
		tfCapacity = new javax.swing.JTextField();
		tfTables = new javax.swing.JTextField();
		btnCancel = new javax.swing.JButton();
		btnOk = new javax.swing.JButton();
		jSeparator1 = new javax.swing.JSeparator();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		jLabel1.setText(com.floreantpos.POSConstants.RESTAURANT_NAME + POSConstants.COLON);

		jLabel2.setText(com.floreantpos.POSConstants.ADDRESS_LINE1 + POSConstants.COLON);

		jLabel3.setText(com.floreantpos.POSConstants.ADDRESS_LINE2 + POSConstants.COLON);

		jLabel4.setText(com.floreantpos.POSConstants.CAPACITY + POSConstants.COLON);

		jLabel5.setText(com.floreantpos.POSConstants.TABLES + POSConstants.COLON);

		btnCancel.setText(com.floreantpos.POSConstants.CLOSE);
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doClose(evt);
			}
		});

		btnOk.setText(com.floreantpos.POSConstants.SAVE);
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doSave(evt);
			}
		});

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING,
				layout.createSequentialGroup().addContainerGap().add(
						layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).add(
								org.jdesktop.layout.GroupLayout.LEADING,
								layout.createSequentialGroup()
										.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1).add(jLabel2).add(jLabel3).add(jLabel4).add(jLabel5)).addPreferredGap(
												org.jdesktop.layout.LayoutStyle.RELATED).add(
												layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(tfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
														.add(tfAddrLine1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE).add(tfAddrLine2,
																org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE).add(
																layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, tfTables).add(
																		org.jdesktop.layout.GroupLayout.LEADING, tfCapacity, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))))
								.add(layout.createSequentialGroup().add(btnOk).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(btnCancel))).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup().addContainerGap().add(
						layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(tfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
						layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(tfAddrLine1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
						layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(tfAddrLine2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
						layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(tfCapacity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
						layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(tfTables, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 115,
						Short.MAX_VALUE).add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(
						org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(btnCancel).add(btnOk)).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void doClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doClose
		setCanceled(true);
		dispose();
	}//GEN-LAST:event_doClose

	private void doSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doSave
		try {
			String name = null;
			String addr1 = null;
			String addr2 = null;
			int capacity = 299;
			int tables = 74;

			name = tfName.getText();
			addr1 = tfAddrLine1.getText();
			addr2 = tfAddrLine2.getText();

			try {
				capacity = Integer.parseInt(tfCapacity.getText());
			} catch (Exception e) {
				POSMessageDialog.showError(this, com.floreantpos.POSConstants.CAPACITY_IS_NOT_VALID_);
				return;
			}

			try {
				tables = Integer.parseInt(tfTables.getText());
			} catch (Exception e) {
				POSMessageDialog.showError(this, com.floreantpos.POSConstants.NUMBER_OF_TABLES_IS_NOT_VALID);
				return;
			}

			restaurant.setName(name);
			restaurant.setAddressLine1(addr1);
			restaurant.setAddressLine2(addr2);
			restaurant.setCapacity(capacity);
			restaurant.setTables(tables);

			dao.saveOrUpdate(restaurant);
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}//GEN-LAST:event_doSave

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnOk;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JTextField tfAddrLine1;
	private javax.swing.JTextField tfAddrLine2;
	private javax.swing.JTextField tfCapacity;
	private javax.swing.JTextField tfName;
	private javax.swing.JTextField tfTables;
	// End of variables declaration//GEN-END:variables

}
