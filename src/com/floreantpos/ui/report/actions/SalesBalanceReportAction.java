package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.SalesBalanceReportView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SalesBalanceReportAction extends AbstractAction {

	public SalesBalanceReportAction() {
		super(com.floreantpos.POSConstants.SALES_BALANCE_REPORT);
	}

	public SalesBalanceReportAction(String name) {
		super(name);
	}

	public SalesBalanceReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesBalanceReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_BALANCE_REPORT);
		if (index == -1) {
			reportView = new SalesBalanceReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_BALANCE_REPORT, reportView);
		}
		else {
			reportView = (SalesBalanceReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
