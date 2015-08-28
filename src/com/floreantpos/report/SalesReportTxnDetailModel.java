package com.floreantpos.report;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.util.NumberUtil;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SalesReportTxnDetailModel extends AbstractTableModel {

	private String currencySymbol;

	private String[] columnNames = {
            "Tgl",
            "TicketID",
            POSConstants.NAME,
            "Price",
            "QTY",
            "Tax",
            POSConstants.TOTAL};
	private List<TxnDetailReportItem> items;
	private double grandTotal;

	public SalesReportTxnDetailModel() {
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
        TxnDetailReportItem item = items.get(rowIndex);
		
		switch(columnIndex) {
            case 0:
                return (item.getTgl());

			case 1:
				return item.getTicketId();

            case 2:
                return item.getName();

			case 3:
				return currencySymbol + " " + NumberUtil.formatToCurrency(item.getPrice());
				
			case 4:
				return String.valueOf(item.getQuantity());
				
			case 5:
				return String.valueOf(item.getTaxRate()) + "%";
				
			case 6:
				return currencySymbol + " " + NumberUtil.formatToCurrency(item.getTotal());
		}
		
		
		return null;
	}

	public List<TxnDetailReportItem> getItems() {
		return items;
	}

	public void setItems(List<TxnDetailReportItem> items) {
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
		
		for (TxnDetailReportItem item : items) {
			grandTotal += item.getTotal();
		}
	}
}
