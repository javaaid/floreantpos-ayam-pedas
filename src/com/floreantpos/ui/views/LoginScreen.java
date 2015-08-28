/*
 * LoginScreen.java
 *
 * Created on August 14, 2006, 10:57 PM
 */

package com.floreantpos.ui.views;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.floreantpos.Messages;
import com.floreantpos.config.AppConfig;
import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;

import java.io.File;

/**
 *
 * @author  MShahriar
 */
public class LoginScreen extends JPanel {
	public final static String VIEW_NAME = "LOGIN_VIEW";

    private PasswordScreen passwordScreen;

	/** Creates new form LoginScreen */
	public LoginScreen() {
		setLayout(new MigLayout("ins 20 10 20 10, fill","[fill,growprio 100,grow][]",""));

        JLabel imageComponent;
        if (AppConfig.getLogoEnabled() && !AppConfig.getLogo().isEmpty()) {
            File f = new File(AppConfig.getLogo());
            if (!f.exists()) {
                JOptionPane.showMessageDialog(this, "File logo " + AppConfig.getLogo() + " tidak ditemukan.");
                //fallback to original logo
                imageComponent = new JLabel(IconFactory.getIcon("florent-pos.png"));
            } else {
                imageComponent = new JLabel(new ImageIcon(AppConfig.getLogo()));
            }
        } else {
            imageComponent = new JLabel(IconFactory.getIcon("florent-pos.png"));
        }

		imageComponent.setBorder(new EtchedBorder());
		
		add(imageComponent, "spany,grow,flowx");
		
		passwordScreen = new PasswordScreen();
		passwordScreen.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5,5,12,5)));
		add(passwordScreen, "width 200,growy 100");
	}
	
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if(aFlag) {
			passwordScreen.setFocus();
		}
	}

	public void setTerminalId(int terminalId) {
		passwordScreen.setTerminalId(terminalId);
	}
}
