package com.floreantpos.config.ui;

import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.swing.POSTextArea;
import com.floreantpos.swing.POSTextField;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BannerConfigurationView extends ConfigurationView implements ActionListener {

	private POSTextField bannerIcon;
	private POSTextField bannerDelay;
	private POSTextArea bannerField;
	private JLabel bannerLabel;
    private JCheckBox cbBannerEnabled = new JCheckBox("Aktifkan Banner");

	public BannerConfigurationView() throws HeadlessException {
		super();
		initUI();

	}

	protected void initUI() {
		setLayout(new MigLayout("fill", "[][grow,fill]", "[][][][][][][][grow,fill]"));
        bannerIcon = new POSTextField();
        bannerField = new POSTextArea();
        bannerDelay = new POSTextField("");

        add(cbBannerEnabled, "grow,wrap");

        bannerLabel = new JLabel("Banner :");
		add(bannerLabel, "grow, wrap");
		add(bannerField, "grow, wrap"); //$NON-NLS-1$

        add (new JLabel("Banner delay (millisecond):"),"grow, wrap");
        add(bannerDelay,"grow, wrap");

        add (new JLabel("Banner icon:"),"grow, wrap");
        add(bannerIcon,"grow, wrap");

	}

    public boolean save() throws Exception {

        String bannerTxt = bannerField.getText();

        Application.getInstance().setSystemInitialized(false);

        saveConfig(cbBannerEnabled.isSelected(),bannerTxt,bannerDelay.getText(), bannerIcon.getText());


        return true;

    }

	public void actionPerformed(ActionEvent e) {

        boolean bannerEnabled = cbBannerEnabled.isSelected();
		String bannerTxt = bannerField.getText();
		String delay = bannerDelay.getText();
		String bannerIcon = bannerDelay.getText();

        Application.getInstance().setSystemInitialized(false);

        saveConfig(bannerEnabled,bannerTxt,delay,bannerIcon);

	}

	private void saveConfig(boolean bannerStatus, String banner, String delay, String bannerIcon) {
		AppConfig.setBannerEnabled(bannerStatus);
		AppConfig.setBanner(banner);
        AppConfig.setBannerDelay(delay);
        AppConfig.setBannerIcon(bannerIcon);
	}

	@Override
	public void initialize() throws Exception {

        cbBannerEnabled.setSelected(AppConfig.getBannerEnabled());
		bannerField.setText(AppConfig.getBanner());
		bannerDelay.setText(AppConfig.getBannerDelay());
        bannerIcon.setText(AppConfig.getBannerIcon());

		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Banner";
	}

}
