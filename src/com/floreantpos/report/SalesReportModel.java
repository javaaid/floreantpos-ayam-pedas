package com.floreantpos.report;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.util.NumberUtil;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SalesReportModel extends AbstractTableModel {

	private String currencySymbol;
	
	private String[] columnNames = {POSConstants.NAME,
            "Price",
            "QTY",
            "Tax",
            POSConstants.TOTAL};
	private List<ReportItem> items;
	private double grandTotal;
	
	public SalesReportModel() {
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
		ReportItem item = items.get(rowIndex);
		
		switch(columnIndex) {
			case 0:
				return item.getName();
				
			case 1:
				return currencySymbol + " " + NumberUtil.formatToCurrency(item.getPrice());
				
			case 2:
				return String.valueOf(item.getQuantity());
				
			case 3:
				return String.valueOf(item.getTaxRate()) + "%";
				
			case 4:
				return currencySymbol + " " + NumberUtil.formatToCurrency(item.getTotal());
		}
		
		
		return null;
	}

	public List<ReportItem> getItems() {
		return items;
	}

	public void setItems(List<ReportItem> items) {
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
		
		for (ReportItem item : items) {
			grandTotal += item.getTotal();
		}
	}
}
