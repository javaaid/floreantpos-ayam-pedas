package com.floreantpos.demo;

import com.floreantpos.model.Ticket;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.ticket.TicketViewerTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class KitchenTicketView extends JPanel {
	private JLabel lblNewLabel;
	private TicketViewerTable ticketViewerTable;
	
	public KitchenTicketView() {
		setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(1, 0, 10, 0));
		
		PosButton psbtnDone = new PosButton();
		psbtnDone.setPreferredSize(new Dimension(0, 40));
		psbtnDone.setText("Done");
		panel.add(psbtnDone);
		
		PosButton psbtnCancel = new PosButton();
		psbtnCancel.setPreferredSize(new Dimension(0, 40));
		psbtnCancel.setText("Cancel");
		panel.add(psbtnCancel);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setPreferredSize(new Dimension(0, 40));
		add(lblNewLabel, BorderLayout.NORTH);
		
		ticketViewerTable = new TicketViewerTable();
		ticketViewerTable.setBorder(new MatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
		add(ticketViewerTable, BorderLayout.CENTER);
		
	}

	public void setTicket(Ticket ticket) {
		ticketViewerTable.setTicket(ticket);
	}
	
	public void setTitle(String title) {
		lblNewLabel.setText(title);
	}

}
