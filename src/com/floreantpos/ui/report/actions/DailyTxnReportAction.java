package com.floreantpos.ui.report.actions;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.report.DailyReportViewer;
import com.floreantpos.ui.report.DailyTxnReport;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DailyTxnReportAction extends AbstractAction {

	public DailyTxnReportAction() {
		super("Transaksi Harian");
	}

	public DailyTxnReportAction(String name) {
		super(name);
	}

	public DailyTxnReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

        DailyReportViewer viewer;
		int index = tabbedPane.indexOfTab("Transaksi Harian");
		if (index == -1) {
			viewer = new DailyReportViewer(new DailyTxnReport());
			tabbedPane.addTab("Transaksi Harian", viewer);
		}
		else {
			viewer = (DailyReportViewer) tabbedPane.getComponentAt(index);
		}

        tabbedPane.setSelectedComponent(viewer);

    }

}
