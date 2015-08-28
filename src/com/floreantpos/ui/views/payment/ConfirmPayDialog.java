package com.floreantpos.ui.views.payment;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.util.NumberUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmPayDialog extends POSDialog {
	private JLabel lblInfo;
	public ConfirmPayDialog() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 300);
		setResizable(false);
		
		createUI();
	}
	private void createUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JSeparator separator = new JSeparator();
		panel.add(separator, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		
		PosButton psbtnConfirm = new PosButton();
		psbtnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(false);
				dispose();
			}
		});
		psbtnConfirm.setText(POSConstants.CONFIRM.toUpperCase());
        psbtnConfirm.setForeground(Color.DARK_GRAY);
		panel_1.add(psbtnConfirm);
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		psbtnCancel.setText(POSConstants.CANCEL.toUpperCase());
        psbtnCancel.setForeground(Color.DARK_GRAY);
		panel_1.add(psbtnCancel);
		
		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle(POSConstants.PAYMENT_CONFIRMATION);
		getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		lblInfo = new JLabel("");
		lblInfo.setBorder(new EmptyBorder(10, 10, 30, 30));
        lblInfo.setForeground(Color.DARK_GRAY);
		getContentPane().add(lblInfo, BorderLayout.CENTER);
	}

	public void setMessage(String message) {
		lblInfo.setText(message);
	}

	public void setAmount(double amount) {
		lblInfo.setText("<html><center><h1>"+ POSConstants.PAYMENT_CONFIRMATION_CONTENT +" " + Application.getCurrencySymbol() +" " + NumberUtil.formatToCurrency(amount) +"</h1></center></html>");
	}
}
