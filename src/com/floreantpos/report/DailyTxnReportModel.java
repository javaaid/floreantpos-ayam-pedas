package com.floreantpos.report;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.util.NumberUtil;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DailyTxnReportModel extends AbstractTableModel {

	private String currencySymbol;

	private String[] columnNames = {"ID", "TOTAL_PRICE"};
	private List<Ticket> items;
	private double grandTotal;

	public DailyTxnReportModel() {
		super();
		currencySymbol = Application.getCurrencySymbol();
	}

	public int getRowCount() {
		if(items == null) {
			return 0;
		}
		
		return items.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        Ticket item = items.get(rowIndex);
		
		switch(columnIndex) {
			case 0:
				return String.valueOf(item.getId());
			case 1:
                return " " + NumberUtil.formatToCurrency(item.getTotalAmount());
		}

		return null;
	}

	public List<Ticket> getItems() {
		return items;
	}

	public void setItems(List<Ticket> items) {
		this.items = items;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public String getGrandTotalAsString() {
		return currencySymbol + " " + NumberUtil.formatToCurrency(grandTotal);
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

    public void calculateGrandTotal() {
        grandTotal = 0;
        if(items == null) {
            return;
        }

        for (Ticket item : items) {
            grandTotal += item.getTotalAmount();
        }
    }
}
