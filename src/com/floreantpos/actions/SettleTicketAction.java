package com.floreantpos.actions;

import com.floreantpos.Messages;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.payment.SettleTicketView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SettleTicketAction extends AbstractAction {

	private int ticketId;

	public SettleTicketAction(int ticketId) {
		this.ticketId = ticketId;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public boolean execute() {
		Ticket ticket = TicketDAO.getInstance().loadFullTicket(ticketId);

		if (ticket.isPaid()) {
			POSMessageDialog.showError(Messages.getString("PosMessage.403"));
			return false;
		}

		SettleTicketView posDialog = new SettleTicketView();
		posDialog.setCurrentTicket(ticket);
		posDialog.setSize(800, 600);
		posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		posDialog.open();
		
		return !posDialog.isCanceled();
	}

}
