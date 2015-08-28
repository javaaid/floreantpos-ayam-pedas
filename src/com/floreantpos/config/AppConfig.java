package com.floreantpos.config;

import com.floreantpos.Database;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

public class AppConfig {
    private static final String TERMINAL_ID = "terminal_id"; //$NON-NLS-1$
    public static final String DATABASE_URL = "database_url"; //$NON-NLS-1$
    public static final String DATABASE_PORT = "database_port"; //$NON-NLS-1$
    public static final String DATABASE_NAME = "database_name"; //$NON-NLS-1$
    public static final String DATABASE_USER = "database_user"; //$NON-NLS-1$
    public static final String DATABASE_PASSWORD = "database_pass"; //$NON-NLS-1$
    public static final String CONNECTION_STRING = "connection_string"; //$NON-NLS-1$
    public static final String DATABASE_PROVIDER_NAME = "database_provider_name"; //$NON-NLS-1$

    private static final String KITCHEN_PRINT_ON_ORDER_SETTLE = "kitchen_print_on_order_settle"; //$NON-NLS-1$
    private static final String KITCHEN_PRINT_ON_ORDER_FINISH = "kitchen_print_on_order_finish"; //$NON-NLS-1$
    private static final String PRINT_RECEIPT_ON_ORDER_SETTLE = "print_receipt_on_order_settle"; //$NON-NLS-1$
    private static final String PRINT_RECEIPT_ON_ORDER_FINISH = "print_receipt_on_order_finish"; //$NON-NLS-1$

    private static final String LOGO = "logo";
    private static final String LOGO_ENABLED = "logo_enabled";
    private static final String BANNER_ENABLED = "banner_enabled"; //$NON-NLS-1$
    private static final String BANNER_ICON = "banner_icon"; //$NON-NLS-1$
    private static final String BANNER = "banner"; //$NON-NLS-1$
    private static final String BANNER_DELAY = "banner_delay"; //$NON-NLS-1$
    private static final String ARM_LINUX_FLAG = "arm_linux_flag"; //$NON-NLS-1$
    private static final String RECEIPT_FOLDER_LOCATION = "receipt_folder_location"; //$NON-NLS-1$
    private static final String DB_HAS_BEEN_CONFIGURED = "db_has_been_configured"; //$NON-NLS-1$
    private static final String LICENCE_KEY = "license_key"; //$NON-NLS-1$
    private static final String MACHINE_ID = "machine_id"; //$NON-NLS-1$

    private static final String  FEATURE_PENARIKAN_FLAG = "feature_penarikan_flag";
    private static final String  FEATURE_GROUP_TICKET_FLAG = "feature_group_ticket_flag";
    private static final String  FEATURE_SPLIT_TICKET_FLAG = "feature_split_ticket_flag";
    private static final String  FEATURE_MANAGER_MENU_FLAG = "feature_manager_menu_flag";
    private static final String  FEATURE_ENABLE_ALL_REPORTS_FLAG = "feature_enable_all_reports_flag";

    private static PropertiesConfiguration config;

    static {
        try {
            //File workingDir = Application.getWorkingDir();
            File configFile = new File("floreantpos.config.properties"); //$NON-NLS-1$
            if(!configFile.exists()) {
                configFile.createNewFile();
            }

            config = new PropertiesConfiguration(configFile);
            config.setAutoSave(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getTerminalId() {
        return config.getInt(TERMINAL_ID, -1);
    }

    public static void setTerminalId(int id) {
        config.setProperty(TERMINAL_ID, id);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        config.setProperty(key, value);
    }

    public static String getString(String key) {
        return config.getString(key, null);
    }

    public static String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public static void put(String key, boolean value) {
        config.setProperty(key, value);
    }

    public static void put(String key, String value) {
        config.setProperty(key, value);
    }

    public static String getDatabaseHost() {
        return config.getString(DATABASE_URL, "localhost"); //$NON-NLS-1$
    }

    public static void setDatabaseHost(String url) {
        config.setProperty(DATABASE_URL, url);
    }

    public static String getConnectString() {
        return config.getString(CONNECTION_STRING, Database.DERBY_SINGLE.getConnectString("", "", ""));  //$NON-NLS-1$
    }

    public static void setConnectString(String connectionString) {
        config.setProperty(CONNECTION_STRING, connectionString);
    }

    public static String getLogo() {
        return config.getString(LOGO, "");
    }

    public static void setLogo(String logo) {
        config.setProperty(LOGO, logo);
    }

    public static boolean getLogoEnabled() {
        return config.getBoolean(LOGO_ENABLED, false);
    }

    public static void setLogoEnabled(boolean logoEnabled) {
        config.setProperty(LOGO_ENABLED, logoEnabled);
    }

    public static boolean getBannerEnabled() {
        return config.getBoolean(BANNER_ENABLED, false);
    }

    public static void setBannerEnabled(boolean bannerEnabled) {
        config.setProperty(BANNER_ENABLED, bannerEnabled);
    }

    public static String getBanner() {
        return config.getString(BANNER, "");
    }

    public static void setBanner(String banner) {
        config.setProperty(BANNER, banner);
    }

    public static String getDbHasBeenConfigured() {
        return config.getString(DB_HAS_BEEN_CONFIGURED,"false");
    }

    public  static void setDbHasBeenConfigured(String set) {
        config.setProperty(DB_HAS_BEEN_CONFIGURED,set);
    }

    public static String getArmLinuxFlag() {
        return config.getString(ARM_LINUX_FLAG, "false");
    }

    public static String getReceiptFolderLocation() {
        return config.getString(RECEIPT_FOLDER_LOCATION, "receipt");
    }

    public static String getFeaturePenarikanFlag() {
        return config.getString(FEATURE_PENARIKAN_FLAG, "false");
    }

    public static String getFeatureSplitTicketFlag() {
        return config.getString(FEATURE_SPLIT_TICKET_FLAG, "false");
    }

    public static String getFeatureGroupTicketFlag() {
        return config.getString(FEATURE_GROUP_TICKET_FLAG, "false");
    }

    public static String getManagerMenuFlag() {
        return config.getString(FEATURE_MANAGER_MENU_FLAG, "false");
    }

    public static String getFeatureEnableAllReportsFlag() {
        return config.getString(FEATURE_ENABLE_ALL_REPORTS_FLAG, "false");
    }

    public static String getLicenceKey() {
        return config.getString(LICENCE_KEY, "");
    }

    public static String getMachineId() {
        return config.getString(MACHINE_ID, "");
    }

    public static void setArmLinuxFlag(String flag) {
        config.setProperty(ARM_LINUX_FLAG, flag);
    }
    public static void setReceiptFolderLocation(String flag) {
        config.setProperty(RECEIPT_FOLDER_LOCATION, flag);
    }

    public static void setFeaturePenarikanFlag(String flag) {
        config.setProperty(FEATURE_PENARIKAN_FLAG, flag);
    }

    public static void setFeatureGroupTicketFlag(String flag) {
        config.setProperty(FEATURE_GROUP_TICKET_FLAG, flag);
    }

    public static void setFeatureSplitTicketFlag(String flag) {
        config.setProperty(FEATURE_SPLIT_TICKET_FLAG, flag);
    }

    public static void setManagerMenuFlag(String flag) {
        config.setProperty(FEATURE_MANAGER_MENU_FLAG, flag);
    }

    public static void setFeatureEnableAllReportsFlag(String flag) {
        config.setProperty(FEATURE_ENABLE_ALL_REPORTS_FLAG, flag);
    }

    public static void setLicenceKey(String key) {
        config.setProperty(LICENCE_KEY, key);
    }

    public static void setMachineId(String id) {
        config.setProperty(MACHINE_ID, id);
    }

    public static String getBannerIcon() {
        return config.getString(BANNER_ICON, "");
    }

    public static void setBannerIcon(String bannerICON) {
        config.setProperty(BANNER_ICON, bannerICON);
    }

    public static String getBannerDelay() {
        return config.getString(BANNER_DELAY, "");
    }

    public static void setBannerDelay(String bannerDelay) {
        config.setProperty(BANNER_DELAY, bannerDelay);
    }

    public static String getDatabasePort() {
        return config.getString(DATABASE_PORT, null);
    }

    public static void setDatabasePort(String port) {
        config.setProperty(DATABASE_PORT, port);
    }

    public static String getDatabaseName() {
        return config.getString(DATABASE_NAME, "posdb"); //$NON-NLS-1$
    }

    public static void setDatabaseName(String name) {
        config.setProperty(DATABASE_NAME, name);
    }

    public static String getDatabaseUser() {
        return config.getString(DATABASE_USER, "app"); //$NON-NLS-1$
    }

    public static void setDatabaseUser(String user) {
        config.setProperty(DATABASE_USER, user);
    }

    public static String getDatabasePassword() {
        return config.getString(DATABASE_PASSWORD, "sa"); //$NON-NLS-1$
    }

    public static void setDatabasePassword(String password) {
        config.setProperty(DATABASE_PASSWORD, password);
    }

    public static void setDatabaseProviderName(String databaseProviderName) {
        config.setProperty(DATABASE_PROVIDER_NAME, databaseProviderName);
    }

    public static String getDatabaseProviderName() {
        return config.getString(DATABASE_PROVIDER_NAME, Database.DERBY_SINGLE.getProviderName()); //$NON-NLS-1$
    }

    public static Database getDefaultDatabase() {
        return Database.getByProviderName(getDatabaseProviderName());
    }

    public static boolean isPrintReceiptOnOrderFinish() {
        return getBoolean(PRINT_RECEIPT_ON_ORDER_FINISH, false);
    }

    public static void setPrintReceiptOnOrderFinish(boolean print) {
        config.setProperty(PRINT_RECEIPT_ON_ORDER_FINISH, print);
    }

    public static boolean isPrintReceiptOnOrderSettle() {
        return getBoolean(PRINT_RECEIPT_ON_ORDER_SETTLE, false);
    }

    public static void setPrintReceiptOnOrderSettle(boolean print) {
        config.setProperty(PRINT_RECEIPT_ON_ORDER_SETTLE, print);
    }

    public static boolean isPrintToKitchenOnOrderFinish() {
        return getBoolean(KITCHEN_PRINT_ON_ORDER_FINISH, false);
    }

    public static void setPrintToKitchenOnOrderFinish(boolean print) {
        config.setProperty(KITCHEN_PRINT_ON_ORDER_FINISH, print);
    }

    public static boolean isPrintToKitchenOnOrderSettle() {
        return getBoolean(KITCHEN_PRINT_ON_ORDER_SETTLE, false);
    }

    public static void setPrintToKitchenOnOrderSettle(boolean print) {
        config.setProperty(KITCHEN_PRINT_ON_ORDER_SETTLE, print);
    }
}
