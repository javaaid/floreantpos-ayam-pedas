package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.ReportViewer;
import com.floreantpos.ui.report.SalesReportTxnDetail;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SalesReportPerTxnAction extends AbstractAction {

	public SalesReportPerTxnAction() {
		super("Laporan Penjualan Detail");
	}

	public SalesReportPerTxnAction(String name) {
		super(name);
	}

	public SalesReportPerTxnAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		ReportViewer viewer = null;
		int index = tabbedPane.indexOfTab("Laporan Penjualan Detail");
		if (index == -1) {
			viewer = new ReportViewer(new SalesReportTxnDetail());
			tabbedPane.addTab("Laporan Penjualan Detail", viewer);
		}
		else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(viewer);
	}

}
