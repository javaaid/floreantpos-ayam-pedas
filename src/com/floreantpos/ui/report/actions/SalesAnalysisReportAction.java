package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.SalesSummaryReportView;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SalesAnalysisReportAction extends AbstractAction {
	
	public SalesAnalysisReportAction() {
		super(com.floreantpos.POSConstants.SALES_ANALYSIS);
	}

	public SalesAnalysisReportAction(String name) {
		super(name);
	}

	public SalesAnalysisReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		SalesSummaryReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_ANALYSIS);
		if (index == -1) {
			reportView = new SalesSummaryReportView();
			reportView.setReportType(SalesSummaryReportView.REPORT_SALES_ANALYSIS);
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_ANALYSIS, reportView);
		}
		else {
			reportView = (SalesSummaryReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
