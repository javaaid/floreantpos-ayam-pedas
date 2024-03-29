package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.SalesExceptionReportView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SalesExceptionReportAction extends AbstractAction {

	public SalesExceptionReportAction() {
		super(com.floreantpos.POSConstants.SALES_EXCEPTION_REPORT);
	}

	public SalesExceptionReportAction(String name) {
		super(name);
	}

	public SalesExceptionReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesExceptionReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_EXCEPTION_REPORT);
		if (index == -1) {
			reportView = new SalesExceptionReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_EXCEPTION_REPORT, reportView);
		}
		else {
			reportView = (SalesExceptionReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
