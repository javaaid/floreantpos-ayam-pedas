package com.floreantpos.config.ui;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.floreantpos.main.Application;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.swing.POSTextField;
import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;

public class DrawerPullConfigurationView extends ConfigurationView {
	private RestaurantDAO restaurantDAO;
	private TerminalDAO terminalDAO;
	private Restaurant restaurant;
	private Terminal terminal;

	JComboBox cbHour;
	JComboBox cbMin;
	JCheckBox chkAutoDrawerPull;
    private POSTextField tfOpeningBalance;
	
	public DrawerPullConfigurationView() {
		setLayout(new MigLayout("align 50% 50%")); //$NON-NLS-1$
		
		Integer[] hours = new Integer[24];
		Integer[] minutes = new Integer[60];
		
		for(int i = 0; i < 24; i++) {
			hours[i] = i;
		}
		for(int i = 0; i < 60; i++) {
			minutes[i] = i;
		}
		
		add(chkAutoDrawerPull = new JCheckBox(com.floreantpos.POSConstants.AUTO_DRAWER_PULL_EVERY_DAY_AT_), "wrap, span"); //$NON-NLS-1$
		add(new JLabel(com.floreantpos.POSConstants.HOUR + ":"), ""); //$NON-NLS-1$ //$NON-NLS-2$
		add(cbHour = new JComboBox(hours), ""); //$NON-NLS-1$
		
		add(new JLabel(Messages.getString("DrawerPullConfigurationView.MINUTE") + ":"), ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		add(cbMin = new JComboBox(minutes), ""); //$NON-NLS-1$

        setLayout(new MigLayout("fill", "[][grow,fill]", "[][][][][][][][grow,fill]"));

        setLayout(new MigLayout("", "[]", "[]"));

        add(chkAutoDrawerPull, "cell 0 0");
        add(new JLabel(com.floreantpos.POSConstants.HOUR + ":"), "cell 1 0");
        add(cbHour = new JComboBox(hours), "cell 2 0");
        add(new JLabel(Messages.getString("DrawerPullConfigurationView.MINUTE") + ":"), "cell 3 0");
        add(cbMin = new JComboBox(minutes), "cell 4 0");

        tfOpeningBalance = new POSTextField();

        add(new JLabel(""), "cell 0 1");

        add(new JLabel(Messages.getString("InitCash")), "cell 0 2");

        add(tfOpeningBalance, "cell 1 2 4 0");

	}

	@Override
	public boolean save() throws Exception {
		if(!isInitialized()) {
			return true;
		}

		restaurant.setAutoDrawerPullEnable(chkAutoDrawerPull.isSelected());
		restaurant.setDrawerPullHour((Integer) cbHour.getSelectedItem());
		restaurant.setDrawerPullMin((Integer) cbMin.getSelectedItem());

		restaurantDAO.saveOrUpdate(restaurant);

        terminal.setOpeningBalance(Double.parseDouble(tfOpeningBalance.getText()));

        terminalDAO.saveOrUpdate(terminal);
		
		JOptionPane.showMessageDialog(this, Messages.getString("DrawerPullConfigurationView.RESTART_MESSAGE")); //$NON-NLS-1$

        return true;
	}
	
	@Override
	public void initialize() throws Exception {
		restaurantDAO = new RestaurantDAO();
		restaurant = restaurantDAO.get(1);

        terminalDAO = new TerminalDAO();
        terminal = Application.getInstance().getTerminal();

        chkAutoDrawerPull.setSelected(restaurant.isAutoDrawerPullEnable());
		cbHour.setSelectedItem(restaurant.getDrawerPullHour());
		cbMin.setSelectedItem(restaurant.getDrawerPullMin());

        tfOpeningBalance.setText(String.valueOf(terminal.getOpeningBalance()));

		setInitialized(true);
	}
	
	@Override
	public String getName() {
		return Messages.getString("CONFIG_TAB_DRAWERPULL"); //$NON-NLS-1$
	}
}
