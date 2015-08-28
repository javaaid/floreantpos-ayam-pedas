package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.OpenTicketSummaryReport;
import com.floreantpos.ui.report.ReportViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenTicketSummaryReportAction extends AbstractAction {

	public OpenTicketSummaryReportAction() {
		super(com.floreantpos.POSConstants.OPEN_TICKET_SUMMARY);
	}

	public OpenTicketSummaryReportAction(String name) {
		super(name);
	}

	public OpenTicketSummaryReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		ReportViewer viewer;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.OPEN_TICKET_SUMMARY);
		if (index == -1) {
			viewer = new ReportViewer(new OpenTicketSummaryReport());
			tabbedPane.addTab(com.floreantpos.POSConstants.OPEN_TICKET_SUMMARY_REPORT, viewer);
		}
		else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(viewer);
	}

}
