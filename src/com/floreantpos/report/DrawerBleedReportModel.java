package com.floreantpos.report;

import com.floreantpos.main.Application;
import com.floreantpos.model.CashDropTransaction;
import com.floreantpos.util.NumberUtil;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DrawerBleedReportModel extends AbstractTableModel {

    private DateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

	private String currencySymbol;

	private String[] columnNames = {"ID", "TIME", "USER","AMOUNT"};
	private List<CashDropTransaction> items;
	private double grandTotal;

	public DrawerBleedReportModel() {
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
        CashDropTransaction item = items.get(rowIndex);
		
		switch(columnIndex) {
			case 0:
				return String.valueOf(item.getId());

            case 1:
                return sdf.format(item.getTransactionTime());

            case 2:
                return item.getUser().getFirstName() + " " + item.getUser().getLastName();

			case 3:
                return " " + NumberUtil.formatToCurrency(item.getTotalAmount());

		}

		return null;
	}

	public List<CashDropTransaction> getItems() {
		return items;
	}

	public void setItems(List<CashDropTransaction> items) {
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

        for (CashDropTransaction item : items) {
            grandTotal += item.getTotalAmount();
        }
    }
}
