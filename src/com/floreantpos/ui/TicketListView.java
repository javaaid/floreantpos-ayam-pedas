package com.floreantpos.ui;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TicketListView extends JPanel {
	private JXTable table;
	private TicketListTableModel tableModel;

	public TicketListView() {
		table = new TicketListTable();
		table.setModel(tableModel = new TicketListTableModel());
		table.setRowHeight(40);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.LIGHT_GRAY);

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(20);
		columnModel.getColumn(1).setPreferredWidth(20);
		columnModel.getColumn(2).setPreferredWidth(200);
		columnModel.getColumn(3).setPreferredWidth(100);

		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(30, 60));

		setLayout(new BorderLayout());

		add(scrollPane);
	}

	public void setTickets(List<Ticket> tickets) {
		tableModel.setRows(tickets);
	}

	public void addTicket(Ticket ticket) {
		tableModel.addItem(ticket);
	}

	public Ticket getSelectedTicket() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}

		return (Ticket) tableModel.getRowData(selectedRow);
	}

	public List<Ticket> getSelectedTickets() {
		int[] selectedRows = table.getSelectedRows();

		ArrayList<Ticket> tickets = new ArrayList<Ticket>(selectedRows.length);

		for (int i = 0; i < selectedRows.length; i++) {
			Ticket ticket = (Ticket) tableModel.getRowData(selectedRows[i]);
			tickets.add(ticket);
		}
		return tickets;
	}

	// public void removeTicket(Ticket ticket) {
	// tableModel.
	// }

	private class TicketListTable extends JXTable {
		
		public TicketListTable() {
			setColumnControlVisible(true);
		}
		
		@Override
		public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
			ListSelectionModel selectionModel = getSelectionModel();
			boolean selected = selectionModel.isSelectedIndex(rowIndex);
			if (selected) {
				selectionModel.removeSelectionInterval(rowIndex, rowIndex);
			}
			else {
				selectionModel.addSelectionInterval(rowIndex, rowIndex);
			}
		}
	}

	private class TicketListTableModel extends ListTableModel {
		public TicketListTableModel() {
			super(new String[] { POSConstants.ID, POSConstants.TABLE, POSConstants.SERVER, POSConstants.CREATED, POSConstants.CUSTOMER,
					POSConstants.TICKET_DELIVERY_DATE, POSConstants.TICKET_TYPE, "STATUS", POSConstants.TOTAL, POSConstants.DUE });
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Ticket ticket = (Ticket) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return (ticket.getId());

			case 1:
				return ticket.getTableNumber();

			case 2:
					User owner = ticket.getOwner();
					return owner.getFirstName();

			case 3:
				return ticket.getCreateDate();

			case 4:
				Customer customer = ticket.getCustomer();
				if (customer != null) {
					return ticket.getCustomer().getTelephoneNo();
				}

				return com.floreantpos.util.StringUtils.removeAndHump(POSConstants.GUEST);

			case 5:
				return ticket.getDeliveryDate();

			case 6:
				if(StringUtils.isEmpty(ticket.getTicketType())) {
					ticket.setTicketType(Ticket.DINE_IN);
					
					return Ticket.DINE_IN;
				}
				
				return ticket.getTicketType();
				
			case 7:
				if(Ticket.PICKUP.equals(ticket.getTicketType())) {
					return "Akan diambil sendiri";
				}
				else if(Ticket.HOME_DELIVERY.equals(ticket.getTicketType())) {
					if(ticket.getAssignedDriver() == null) {
						return "Pengantar belum ditentukan";
					}
					return "Driver assigned";
				}
				else if(Ticket.DRIVE_THROUGH.equals(ticket.getTicketType())) {
					return com.floreantpos.util.StringUtils.removeAndHump(POSConstants.DRIVE_THRU);
				}
				
				if(ticket.isPaid()) {
					return POSConstants.PAID;
				}
				
				return "Aktif";

			case 8:
				return ticket.getTotalAmount();

			case 9:
				return ticket.getDueAmount();

			}

			return null;
		}

	}

	// private class TicketListTableCellRenderer extends PosTableRenderer {
	// @Override
	// public Component getTableCellRendererComponent(JTable table, Object
	// value, boolean isSelected, boolean hasFocus, int row, int column) {
	// Component rendererComponent = super.getTableCellRendererComponent(table,
	// value, isSelected, hasFocus, row, column);
	//
	// if(isSelected || value == null) {
	// return rendererComponent;
	// }
	//
	// if(Ticket.isDineIn(value.toString())) {
	// rendererComponent.setBackground(SwitchboardView.DINE_IN_COLOR);
	// }
	// else if(Ticket.isDriveThrough(value.toString())) {
	// rendererComponent.setBackground(SwitchboardView.DRIVE_THROUGH_COLOR);
	// }
	// else if(Ticket.isOnlineOrder(value.toString())) {
	// rendererComponent.setBackground(SwitchboardView.ONLINE_ORDER_COLOR);
	// }
	// else if(Ticket.isHomeDelivery(value.toString())) {
	// rendererComponent.setBackground(SwitchboardView.HOME_DELIVERY_COLOR);
	// }
	// else if(Ticket.isTakeOut(value.toString())) {
	// rendererComponent.setBackground(SwitchboardView.TAKE_OUT_COLOR);
	// }
	//
	// return rendererComponent;
	// }
	// }
}
