package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.SalesDetailReportView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SalesDetailReportAction extends AbstractAction {

	public SalesDetailReportAction() {
		super(com.floreantpos.POSConstants.SALES_DETAILED_REPORT);
	}

	public SalesDetailReportAction(String name) {
		super(name);
	}

	public SalesDetailReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesDetailReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_DETAILED_REPORT);
		if (index == -1) {
			reportView = new SalesDetailReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_DETAILED_REPORT, reportView);
		}
		else {
			reportView = (SalesDetailReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
