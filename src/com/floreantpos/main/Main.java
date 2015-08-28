package com.floreantpos.main;

import com.floreantpos.config.AppConfig;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.dialog.LicenseDialog;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.codec.binary.Base64;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.MessageDigest;


public class Main  {

	private static final String DEVELOPMENT_MODE = "developmentMode";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

        if (isLicenseValid()) {
//        if (true) { //todo remove later

            Options options = new Options();
            options.addOption(DEVELOPMENT_MODE, true, "State if this is developmentMode");
            CommandLineParser parser = new BasicParser();
            CommandLine commandLine = parser.parse(options, args);
            String optionValue = commandLine.getOptionValue(DEVELOPMENT_MODE);

            Application application = Application.getInstance();

            if (optionValue != null) {
                application.setDevelopmentMode(Boolean.valueOf(optionValue));
            }

            application.start();

        } else {
            new LicenseDialog().setVisible(true);
        }

    }

    private static boolean isLicenseValid() {

        String machineId = AppConfig.getMachineId();

        if (org.apache.commons.lang.StringUtils.isEmpty(machineId)) {
            try {
                String[] nets = new Sigar().getNetInterfaceList();
                NetInterfaceConfig c = new Sigar().getNetInterfaceConfig(nets[0]);
                Base64 base64 = new Base64(true);
                machineId = base64.encodeAsString(c.getHwaddr().getBytes()).trim().toUpperCase();
                AppConfig.setMachineId(machineId);
            } catch (SigarException e) {
                e.printStackTrace();
            }
        }

        try {

            String[] nets = new Sigar().getNetInterfaceList();

            boolean machineIdValid = false;

            for (String i : nets)
            {

                NetInterfaceConfig c = new Sigar().getNetInterfaceConfig(i);
                Base64 base64 = new Base64(true);
                if (machineId.equalsIgnoreCase(base64.encodeAsString(c.getHwaddr().getBytes()).trim()))
                {
                    machineIdValid=true;
                    MessageDigest md = MessageDigest.getInstance("SHA");

                    md.update(machineId.getBytes("UTF-8"));

                    String key = new String(md.digest(),"UTF-8");

                    key = base64.encodeAsString(key.getBytes("UTF-8")).trim().toUpperCase();

                    key = key.replaceAll("_","").replaceAll("-","");

                    if (key.equals(AppConfig.getLicenceKey())) return true;
                }
            }

            if (!machineIdValid)
            {
                String correctMachineID = "";
                try
                {
                    NetInterfaceConfig c = new Sigar().getNetInterfaceConfig(nets[0]);
                    Base64 base64 = new Base64(true);
                    correctMachineID = base64.encodeAsString(c.getHwaddr().getBytes("UTF-8")).trim().toUpperCase();

                } catch (SigarException e) {
                    e.printStackTrace();
                }

                MessageDialog.showError("ID Mesin tidak valid.\n Gunakan ID Mesin berikut: "+correctMachineID+"\n(ID Mesin baru sudah dicopy ke clipboard)");

                StringSelection selection = new StringSelection(correctMachineID);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

}

