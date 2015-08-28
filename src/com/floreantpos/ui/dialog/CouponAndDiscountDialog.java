/*
 * CouponAndDiscountDialog.java
 *
 * Created on September 23, 2007, 1:59 PM
 */

package com.floreantpos.ui.dialog;

import com.floreantpos.PosException;
import com.floreantpos.main.Application;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.dao.CouponAndDiscountDAO;
import com.floreantpos.util.NumberUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author  mahbub.shahriar
 */
public class CouponAndDiscountDialog extends POSDialog implements ActionListener, ListSelectionListener{
	private List<CouponAndDiscount> couponList;
	private TicketCouponAndDiscount ticketCoupon;
	private Ticket ticket;
    
    /** Creates new form CouponAndDiscountDialog */
    public CouponAndDiscountDialog() {
    	super(Application.getPosWindow(), true, false);

    	initComponents();
        
        tfValue.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				try {
					double totalDiscount = Double.parseDouble(tfValue.getText());
					lblTotalDiscount.setText(NumberUtil.formatToCurrency(totalDiscount));
				} catch (Exception x) {
				}
			}

			public void removeUpdate(DocumentEvent e) {
				try {
					double totalDiscount = Double.parseDouble(tfValue.getText());
					lblTotalDiscount.setText(NumberUtil.formatToCurrency(totalDiscount));
				} catch (Exception x) {
				}
			}

			public void changedUpdate(DocumentEvent e) {
				try {
					double totalDiscount = Double.parseDouble(tfValue.getText());
					lblTotalDiscount.setText(NumberUtil.formatToCurrency(totalDiscount));
				} catch (Exception x) {
				}
			}
			
		});
        lblTotalDiscount.setText("");
        btnEditValue.setEnabled(false);
        
        btnUp.setActionCommand("scrollUP");
		btnDown.setActionCommand("scrollDown");
		btnUp.addActionListener(this);
		btnDown.addActionListener(this);
		listCoupons.addListSelectionListener(this);

		listCoupons.setCellRenderer(new CouponListRenderer());

		ticketCoupon = new TicketCouponAndDiscount();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titlePanel1 = new com.floreantpos.ui.TitlePanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listCoupons = new javax.swing.JList();
        btnCancel = new com.floreantpos.swing.PosButton();
        btnOk = new com.floreantpos.swing.PosButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnUp = new com.floreantpos.swing.PosButton();
        btnDown = new com.floreantpos.swing.PosButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        tfNumber = new javax.swing.JTextField();
        tfType = new javax.swing.JTextField();
        tfValue = new javax.swing.JTextField();
        btnEditValue = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        lblTotalDiscount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        titlePanel1.setTitle("Select Coupon/Discount");

        jScrollPane1.setViewportView(listCoupons);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel_32.png"))); // NOI18N
        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doCancel(evt);
            }
        });

        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/finish_32.png"))); // NOI18N
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doOk(evt);
            }
        });

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/up_32.png"))); // NOI18N

        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/down_32.png"))); // NOI18N

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setText("Coupon/Discount Name" + ":");

        jLabel2.setText("Coupon/Discount Number" + ":");

        jLabel3.setText("Coupon/Discount Type" + ":");

        jLabel4.setText("Coupon/Discount Value" + ":");

        tfName.setEditable(false);

        tfNumber.setEditable(false);

        tfType.setEditable(false);

        tfValue.setEditable(false);

        btnEditValue.setText("Enter Value");
        btnEditValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doEnterValue(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("TOTAL DISKON");

        lblTotalDiscount.setFont(new java.awt.Font("Tahoma", 1, 18));
        lblTotalDiscount.setForeground(new java.awt.Color(204, 51, 0));
        lblTotalDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalDiscount.setText("jLabel6");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                    .add(titlePanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(btnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 114, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .add(17, 17, 17)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel4)
                                    .add(jLabel3)
                                    .add(jLabel2)
                                    .add(jLabel1))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(layout.createSequentialGroup()
                                        .add(tfValue, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(btnEditValue))
                                    .add(tfType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                    .add(tfNumber, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                    .add(tfName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)))
                            .add(layout.createSequentialGroup()
                                .add(18, 18, 18)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                                    .add(jSeparator3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                                    .add(lblTotalDiscount, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(titlePanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(tfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(tfNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(tfType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(btnEditValue)
                            .add(tfValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSeparator3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(5, 5, 5)
                                .add(btnUp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnDown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblTotalDiscount)))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public TicketCouponAndDiscount getSelectedCoupon() {
		try {
			double parseDouble = Double.parseDouble(tfValue.getText());
			ticketCoupon.setValue(parseDouble);
		}catch(Exception x) {
			throw new PosException("CouponAndDiscount amount is not valid");
		}
	return ticketCoupon;
}

    private void doEnterValue(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doEnterValue
        NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
        dialog.setFloatingPoint(true);
        dialog.setTitle("Enter value");
        dialog.pack();
        dialog.open();
        
        if(!dialog.isCanceled()) {
        	double value = dialog.getValue();
        	tfValue.setText(NumberUtil.formatNumber(value));
        }
    }//GEN-LAST:event_doEnterValue

    private void doOk(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doOk
    	try {
			TicketCouponAndDiscount selectedCoupon = getSelectedCoupon();
			if (selectedCoupon == null) {
				POSMessageDialog.showError(this, "Please select a coupon/discount");
				return;
			}
			setCanceled(false);
			dispose();
		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getMessage());
		}
    }//GEN-LAST:event_doOk

    private void doCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doCancel
        setCanceled(true);
        dispose();
    }//GEN-LAST:event_doCancel
    
    
    public void initData() throws Exception {
		CouponAndDiscountDAO dao = new CouponAndDiscountDAO();
		couponList = dao.getValidCoupons();
		listCoupons.setModel(new CouponListModel());
	}

	public void actionPerformed(ActionEvent e) {
		if ("scrollUP".equals(e.getActionCommand())) {
			if (couponList == null || couponList.size() == 0)
				return;

			int selectedRow = listCoupons.getSelectedIndex();

			if (selectedRow <= 0) {
				selectedRow = 0;
			}
			else {
				--selectedRow;
			}

			listCoupons.setSelectedIndex(selectedRow);
			Rectangle cellRect = listCoupons.getCellBounds(selectedRow, selectedRow);
			listCoupons.scrollRectToVisible(cellRect);
		}
		else if ("scrollDown".equals(e.getActionCommand())) {
			if (couponList == null || couponList.size() == 0)
				return;

			int selectedRow = listCoupons.getSelectedIndex();

			if (selectedRow < 0) {
				selectedRow = 0;
			}
			else if (selectedRow >= couponList.size() - 1) {
				//return;
			}
			else {
				++selectedRow;
			}

			listCoupons.setSelectedIndex(selectedRow);
			Rectangle cellRect = listCoupons.getCellBounds(selectedRow, selectedRow);
			cellRect.y += 20;
			listCoupons.scrollRectToVisible(cellRect);
		}
	}

	public void updateCouponView(CouponAndDiscount coupon) {
		if (coupon == null) {
			tfName.setText("");
			tfNumber.setText("");
			tfType.setText("");
			tfValue.setText("");
			return;
		}
		
		btnEditValue.setEnabled(false);

		tfName.setText(coupon.getName());
		if (coupon.getType() == CouponAndDiscount.FREE_AMOUNT) {
			btnEditValue.setEnabled(true);
		}

		tfNumber.setText(String.valueOf(coupon.getId()));
		tfType.setText(CouponAndDiscount.COUPON_TYPE_NAMES[coupon.getType()]);
		tfValue.setText(NumberUtil.formatNumber(coupon.getValue()));
		
		double totalDiscount;
		double subtotal = ticket.getSubtotalAmount();

		ticketCoupon.setCouponAndDiscountId(coupon.getId());
		ticketCoupon.setName(coupon.getName());
		ticketCoupon.setType(coupon.getType());
		ticketCoupon.setValue(coupon.getValue());

		totalDiscount = ticket.calculateDiscountFromType(ticketCoupon, subtotal);
		ticketCoupon.setValue(totalDiscount);

		lblTotalDiscount.setText(NumberUtil.formatNumber(totalDiscount));
	}

	public void valueChanged(ListSelectionEvent e) {
		CouponAndDiscount coupon = (CouponAndDiscount) listCoupons.getSelectedValue();
		updateCouponView(coupon);
	}
	
	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
    
    class CouponListModel extends AbstractListModel {

		public int getSize() {
			if (couponList == null)
				return 0;

			return couponList.size();
		}

		public Object getElementAt(int index) {
			return couponList.get(index);
		}

	}

	class CouponListRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			CouponAndDiscount coupon = (CouponAndDiscount) value;
			//			String displayText = "<html><body>";
			//			displayText += "<p align='center'>Name:" + coupon.getName() + "</p>";
			//			displayText += "<p align='center'>" + coupon.getValue() + "</p>";
			//			displayText += "</body></html>";
			return super.getListCellRendererComponent(list, coupon.getName(), index, isSelected, cellHasFocus);
		}
	}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.floreantpos.swing.PosButton btnCancel;
    private com.floreantpos.swing.PosButton btnDown;
    private javax.swing.JButton btnEditValue;
    private com.floreantpos.swing.PosButton btnOk;
    private com.floreantpos.swing.PosButton btnUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblTotalDiscount;
    private javax.swing.JList listCoupons;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfNumber;
    private javax.swing.JTextField tfType;
    private javax.swing.JTextField tfValue;
    private com.floreantpos.ui.TitlePanel titlePanel1;
    // End of variables declaration//GEN-END:variables
    
}
