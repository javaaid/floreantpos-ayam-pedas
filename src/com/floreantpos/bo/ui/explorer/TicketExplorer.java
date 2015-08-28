package com.floreantpos.bo.ui.explorer;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TicketExplorer extends TransparentPanel {
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, h:m a"); //$NON-NLS-1$

	JXTable explorerTable;
	private List<Ticket> tickets;
	
	public TicketExplorer() {
		setLayout(new BorderLayout());
		
		explorerTable = new JXTable(new TicketExplorerTableModel()) {
			PosTableRenderer renderer = new PosTableRenderer();
			
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				return renderer;
			}
		};
		explorerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		explorerTable.setColumnControlVisible(true);
		add(new JScrollPane(explorerTable));
	}
	
	public void init() {
		try {
			TicketDAO dao = new TicketDAO();
			tickets = dao.findAll();
			explorerTable.packAll();
			explorerTable.repaint();
		} catch (Exception e) {
		BOMessageDialog.showError(e);
		}
	}
	
	class TicketExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {
                POSConstants.ID,
                POSConstants.CREATED_BY,
                POSConstants.CREATE_TIME,
                POSConstants.SETTLE_TIME,
                POSConstants.SUBTOTAL,
                POSConstants.DISCOUNT,
                POSConstants.TAX,
                POSConstants.TOTAL,
                POSConstants.PAID,
                POSConstants.VOID
		};
		
		public int getRowCount() {
			if(tickets == null) {
				return 0;
			}
			return tickets.size();
		}

		public int getColumnCount() {
			return 11;
		}
		
		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if(tickets == null)
				return ""; //$NON-NLS-1$
			
			Ticket ticket = tickets.get(rowIndex);
			
			switch(columnIndex) {
				case 0:
					return String.valueOf(ticket.getId());
					
				case 1:
					return ticket.getOwner().toString();
					
				case 2:
					return dateFormat.format(ticket.getCreateDate());
					
				case 3:
					if(ticket.getClosingDate() != null) {
						return dateFormat.format(ticket.getClosingDate());
					}
					return ""; //$NON-NLS-1$
					
				case 4:
					return (ticket.getSubtotalAmount());
					
				case 5:
					return (ticket.getDiscountAmount());
					
				case 6:
					return (ticket.getTaxAmount());
					
				case 7:
					return (ticket.getTotalAmount());
					
				case 8:
					return (ticket.isPaid());
					
				case 9:
					return (ticket.isVoided());
			}
			return null;
		}
	}
}
