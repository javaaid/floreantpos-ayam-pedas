package com.floreantpos.config.ui;

import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoConfigurationView extends ConfigurationView implements ActionListener {

	private JTextField logoFileName;
    private JCheckBox cbLogoEnabled = new JCheckBox("Ganti logo tampilan depan");

	public LogoConfigurationView() throws HeadlessException {
		super();
		initUI();

	}

	protected void initUI() {
		setLayout(new MigLayout("fill", "[][grow,fill]", "[][][][][][][][grow,fill]"));
        logoFileName = new JTextField();

        add(cbLogoEnabled, "grow,wrap");

        add(new JLabel("Logo yang dapat digunakan harus dengan spesifikasi berikut:"),"grow, wrap");
        add(new JLabel("- Format PNG"),"grow, wrap");
        add(new JLabel("- Ukuran lebar 600 pixel, tinggi 400 pixel"),"grow, wrap");

        JLabel logo = new JLabel("Lokasi file :");
		add(logo, "grow, wrap");
		add(logoFileName, "grow, wrap");

	}

    public boolean save() throws Exception {

        Application.getInstance().setSystemInitialized(false);

        saveConfig(cbLogoEnabled.isSelected(),logoFileName.getText());

//        JOptionPane.showMessageDialog(this, Messages.getString("Requires.RESTART_MESSAGE"));

        return true;

    }

	public void actionPerformed(ActionEvent e) {

        boolean logoEnabled = cbLogoEnabled.isSelected();
		String logoFileNameText  = logoFileName.getText();

        Application.getInstance().setSystemInitialized(false);

        saveConfig(logoEnabled,logoFileNameText);

	}

	private void saveConfig(boolean logoStatus, String logo) {
		AppConfig.setLogoEnabled(logoStatus);
		AppConfig.setLogo(logo);
	}

	@Override
	public void initialize() throws Exception {

        cbLogoEnabled.setSelected(AppConfig.getLogoEnabled());
		logoFileName.setText(AppConfig.getLogo());

		setInitialized(true);
	}

	@Override
	public String getName() {
		return "Logo";
	}

}
