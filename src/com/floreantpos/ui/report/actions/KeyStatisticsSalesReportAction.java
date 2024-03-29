package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.SalesSummaryReportView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class KeyStatisticsSalesReportAction extends AbstractAction {

	public KeyStatisticsSalesReportAction() {
		super(com.floreantpos.POSConstants.SALES_SUMMARY_KEY_STATISTICS_REPORT);
	}

	public KeyStatisticsSalesReportAction(String name) {
		super(name);
	}

	public KeyStatisticsSalesReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesSummaryReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_SUMMARY_KEY_STATISTICS);
		if (index == -1) {
			reportView = new SalesSummaryReportView();
			reportView.setReportType(SalesSummaryReportView.REPORT_KEY_STATISTICS);
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_SUMMARY_KEY_STATISTICS, reportView);
		}
		else {
			reportView = (SalesSummaryReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
