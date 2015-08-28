package com.floreantpos.config.ui;

import com.floreantpos.Messages;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class FeaturesView extends ConfigurationView {

    public static final String CONFIG_TAB_TAX = "Features";

    private JCheckBox cbGroupTicket = new JCheckBox("Bayar Group (Menggabungkan beberapa tiket)");
    private JCheckBox cbSplitTicket = new JCheckBox("Pisah Tiket (Membagi satu tiket menjadi dua tiket)");
    private JCheckBox cbPenarikan = new JCheckBox("Penarikan (Buka register untuk menarik uang untuk tujuan khusus)");
    private JCheckBox cbManagerial = new JCheckBox("Menu Manajerial");
    private JCheckBox cbEnableAllReport = new JCheckBox("Aktifkan semua laporan");

	public FeaturesView() throws HeadlessException {
		super();
		initUI();

	}

	protected void initUI() {

		setLayout(new MigLayout("fill", "[][grow,fill]", "[][][][][][][][grow,fill]"));

        setLayout(new MigLayout("", "[]", "[]"));

        add(cbPenarikan, "cell 0 0");
        add(cbSplitTicket, "cell 0 1");
        add(cbGroupTicket, "cell 0 2");
        add(cbManagerial, "cell 0 3");
        add(cbEnableAllReport, "cell 0 4");

	}

    public boolean save() throws Exception {

        Application.getInstance().setSystemInitialized(false);

        AppConfig.setFeaturePenarikanFlag(String.valueOf(cbPenarikan.isSelected()));
        AppConfig.setFeatureGroupTicketFlag(String.valueOf(cbGroupTicket.isSelected()));
        AppConfig.setFeatureSplitTicketFlag(String.valueOf(cbSplitTicket.isSelected()));
        AppConfig.setManagerMenuFlag(String.valueOf(cbManagerial.isSelected()));
        AppConfig.setFeatureEnableAllReportsFlag(String.valueOf(cbEnableAllReport.isSelected()));

//        JOptionPane.showMessageDialog(this, Messages.getString("Requires.RESTART_MESSAGE"));

        return true;

    }

	@Override
	public void initialize() throws Exception {

        String groupTicketFlag = AppConfig.getFeatureGroupTicketFlag();
        cbGroupTicket.setSelected(Boolean.valueOf(groupTicketFlag));

        String splitTicketFlag = AppConfig.getFeatureSplitTicketFlag();
        cbSplitTicket.setSelected(Boolean.valueOf(splitTicketFlag));

        String penarikanFlag = AppConfig.getFeaturePenarikanFlag();
        cbPenarikan.setSelected(Boolean.valueOf(penarikanFlag));

        String managerMenuFlag = AppConfig.getManagerMenuFlag();
        cbManagerial.setSelected(Boolean.valueOf(managerMenuFlag));

        String enableAllReportsFlag = AppConfig.getFeatureEnableAllReportsFlag();
        cbEnableAllReport.setSelected(Boolean.valueOf(enableAllReportsFlag));

        setInitialized(true);

    }

	@Override
	public String getName() {
		return "Features";
	}

}
