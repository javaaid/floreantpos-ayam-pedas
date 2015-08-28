package com.floreantpos.report;

import com.floreantpos.POSConstants;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.PrintConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.*;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.util.NumberUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.floreantpos.util.ReceiptUploader;

public class JReportPrintService {
    private static final String TIP_AMOUNT = "tipAmount";
    private static final String SERVICE_CHARGE = "serviceCharge";
    private static final String TAX_AMOUNT = "taxAmount";
    private static final String DISCOUNT_AMOUNT = "discountAmount";
    private static final String HEADER_LINE5 = "headerLine5";
    private static final String HEADER_LINE4 = "headerLine4";
    private static final String HEADER_LINE3 = "headerLine3";
    private static final String HEADER_LINE2 = "headerLine2";
    private static final String HEADER_LINE1 = "headerLine1";
    private static final String REPORT_DATE = "reportDate";
    private static final String SERVER_NAME = "serverName";
    private static final String GUEST_COUNT = "guestCount";
    private static final String TABLE_NO = "tableNo";
    private static final String CHECK_NO = "checkNo";
    private static final String TERMINAL = "terminal";
    private static final String SHOW_FOOTER = "showFooter";
    private static final String SHOW_HEADER_SEPARATOR = "showHeaderSeparator";
    private static final String SHOW_SUBTOTAL = "showSubtotal";
    private static final String RECEIPT_TYPE = "receiptType";
    private static final String SUB_TOTAL_TEXT = "subTotalText";
    private static final String QUANTITY_TEXT = "quantityText";
    private static final String ITEM_TEXT = "itemText";
    private static final String CURRENCY_SYMBOL = "currencySymbol";
    private static Log logger = LogFactory.getLog(JReportPrintService.class);

    public static JasperPrint createJasperPrint(String reportFile, Map<String, Object> properties, JRDataSource dataSource) throws Exception {
        InputStream ticketReportStream = null;

        try {

            ticketReportStream = JReportPrintService.class.getResourceAsStream(reportFile);
            JasperReport ticketReport = (JasperReport) JRLoader.loadObject(ticketReportStream);

            return JasperFillManager.fillReport(ticketReport, properties, dataSource);

        } finally {
            IOUtils.closeQuietly(ticketReportStream);
        }
    }

    public static JasperPrint createPrint(Ticket ticket, TicketPrintProperties printProperties) throws Exception {
        HashMap map = populateTicketProperties(ticket, printProperties);

        final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/TicketReceiptReport.jasper";

        TicketDataSource dataSource = new TicketDataSource(ticket, printProperties.isKitchenPrint(), printProperties.isPrintModifers(),
                printProperties.isPrintCookingInstructions());
        return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
    }

    public static JasperPrint createTextReceipt(Ticket ticket, TicketPrintProperties printProperties) throws Exception {

        HashMap map = populateTicketProperties(ticket, printProperties);

        final String FILE_RECEIPT_REPORT = "/com/floreantpos/report/TicketReceiptReportTXT.jasper";

        TicketDataSource dataSource = new TicketDataSource(ticket, printProperties.isKitchenPrint(), printProperties.isPrintModifers(),
                printProperties.isPrintCookingInstructions());
        return createJasperPrint(FILE_RECEIPT_REPORT, map, new JRTableModelDataSource(dataSource));
    }

    public static void printTicket(Ticket ticket) {

        Runnable genericPrinterExecutor = null;

        try {

            TicketPrintProperties printProperties = new TicketPrintProperties("*** BUKTI PEMBAYARAN ***", true, true, true);

            printProperties.setKitchenPrint(false);

            printProperties.setPrintCookingInstructions(false);

            File folder = new File(AppConfig.getReceiptFolderLocation()); //default is receipt

            if (!folder.exists()) {
                folder.mkdirs();
            }

            final File f = new File(folder, Application.getInstance().getRestaurant().getName().replaceAll(" ","")+"_"+ticket.getId() + ".txt");

            if (f.exists()) {
                f.delete();
            } else {
                f.createNewFile();
            }

            JasperPrint jasperPrintTXT = createTextReceipt(ticket, printProperties);
            JRTextExporter text_exporter = new JRTextExporter();
            text_exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrintTXT);
            text_exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, f.getAbsolutePath());
            text_exporter.setParameter(JRTextExporterParameter.LINE_SEPARATOR, "\n");
            text_exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, Float.valueOf("10"));
            text_exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, Float.valueOf("7.4"));
            text_exporter.exportReport();

            genericPrinterExecutor = new Runnable() {
                @Override
                public void run() {
                    try {

                        Runtime.getRuntime().exec("/usr/bin/lp -d drawer /opt/floreant/drawertxt.txt");

                        String cmd = "/usr/bin/lp -d "+PrintConfig.getReceiptPrinterName() + " " + f.getAbsolutePath();
                        Runtime.getRuntime().exec(cmd);

                        //System.out.println(cmd);
                        //System.out.println("Receipt printed");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };


        } catch (Exception e) {
            logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
        }


        if (!Application.getInstance().isARMLinux()) {

            try {

                TicketPrintProperties printProperties = new TicketPrintProperties("*** BUKTI PEMBAYARAN ***", true, true, true);
                printProperties.setKitchenPrint(false);
                printProperties.setPrintCookingInstructions(false);

                final JasperPrint jasperPrint = createPrint(ticket, printProperties);
                jasperPrint.setProperty("printerName", PrintConfig.getReceiptPrinterName());

                new Thread() {
                    public void run() {

                        try {JasperPrintManager.printReport(jasperPrint, false);} catch (Exception e) {
                            logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
                        }

                    }}.start();

            } catch (Exception e) {
                logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
            }

        } else {

            try {

                if (genericPrinterExecutor != null) {
                    Thread t = new Thread(genericPrinterExecutor);
                    t.start();
                }
            } catch (Exception e) {
                logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
            }
        }


        //TRIGGER UPLOADING PROCESS
//        Thread t = new Thread(new ReceiptUploader());
//        t.setPriority(Thread.MIN_PRIORITY);
//        t.start();
    }

    public static void saveReceiptFile(Ticket ticket) {

        Runnable genericPrinterExecutor = null;

        try {

            TicketPrintProperties printProperties = new TicketPrintProperties("*** BUKTI PEMBAYARAN ***", true, true, true);

            printProperties.setKitchenPrint(false);

            printProperties.setPrintCookingInstructions(false);

            File folder = new File(AppConfig.getReceiptFolderLocation()); //default is receipt

            if (!folder.exists()) {
                folder.mkdirs();
            }

            final File f = new File(folder, Application.getInstance().getRestaurant().getName().replaceAll(" ","")+"_"+ticket.getId() + ".txt");

            if (f.exists()) {
                f.delete();
            } else {
                f.createNewFile();
            }

            JasperPrint jasperPrintTXT = createTextReceipt(ticket, printProperties);
            JRTextExporter text_exporter = new JRTextExporter();
            text_exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrintTXT);
            text_exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, f.getAbsolutePath());
            text_exporter.setParameter(JRTextExporterParameter.LINE_SEPARATOR, "\n");
            text_exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, Float.valueOf("10"));
            text_exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, Float.valueOf("7.4"));
            text_exporter.exportReport();

        } catch (Exception e) {
            logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
        }
    }

    private static void beginRow(StringBuilder html) {
        html.append("<div>");
    }

    private static void endRow(StringBuilder html) {
        html.append("</div>");
    }

    private static void addColumn(StringBuilder html, String columnText) {
        html.append("<span>").append(columnText).append("</span>");
    }

    public static HashMap populateTicketProperties(Ticket ticket, TicketPrintProperties printProperties) {
        Restaurant restaurant = RestaurantDAO.getWorkingRestaurant();

        double totalAmount = ticket.getTotalAmount();
        double tipAmount = 0;

        HashMap map = new HashMap();
        String currencySymbol = Application.getCurrencySymbol();
        map.put(CURRENCY_SYMBOL, currencySymbol);
        map.put(ITEM_TEXT, POSConstants.RECEIPT_REPORT_ITEM_LABEL);
        map.put(QUANTITY_TEXT, POSConstants.RECEIPT_REPORT_QUANTITY_LABEL);
        map.put(SUB_TOTAL_TEXT, POSConstants.RECEIPT_REPORT_SUBTOTAL_LABEL);
        map.put(RECEIPT_TYPE, printProperties.getReceiptTypeName());
        map.put(SHOW_SUBTOTAL, printProperties.isShowSubtotal());
        map.put(SHOW_HEADER_SEPARATOR, Boolean.TRUE);
        map.put(SHOW_FOOTER, printProperties.isShowFooter());

        map.put(TERMINAL, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());
        map.put(CHECK_NO, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
        map.put(TABLE_NO, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumber());
        map.put(GUEST_COUNT, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
        map.put(SERVER_NAME, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
        map.put(REPORT_DATE, POSConstants.RECEIPT_REPORT_DATE_LABEL + Application.formatDate(new Date()));

        String transactionType = ticket.getTransactionType();
        if(PosTransaction.TYPE_CREDIT_CARD.equalsIgnoreCase(transactionType) || PosTransaction.TYPE_DEBIT_CARD.equalsIgnoreCase(transactionType)) {
            map.put("cardPayment", true);
            map.put("approvalCode", "Approval: " + ticket.getCardNumber());
        }

        StringBuilder ticketHeaderBuilder = buildTicketHeader(ticket, printProperties);

        map.put("ticketHeader", ticketHeaderBuilder.toString());

        if (printProperties.isShowHeader()) {
            map.put(HEADER_LINE1, restaurant.getName());
            map.put(HEADER_LINE2, restaurant.getAddressLine1());
            map.put(HEADER_LINE3, restaurant.getAddressLine2());
            map.put(HEADER_LINE4, restaurant.getAddressLine3());
            map.put(HEADER_LINE5, restaurant.getTelephone());
        }

        if (printProperties.isShowFooter()) {
            if (ticket.getDiscountAmount() > 0.0) {
                map.put(DISCOUNT_AMOUNT, NumberUtil.formatNumber(ticket.getDiscountAmount()));
            }

            if (ticket.getTaxAmount() > 0.0) {
                map.put(TAX_AMOUNT, NumberUtil.formatNumber(ticket.getTaxAmount()));
            }

            if (ticket.getServiceCharge() > 0.0) {
                map.put(SERVICE_CHARGE, NumberUtil.formatNumber(ticket.getServiceCharge()));
            }

            if (ticket.getGratuity() != null) {
                tipAmount = ticket.getGratuity().getAmount();
                map.put(TIP_AMOUNT, NumberUtil.formatNumber(tipAmount));
            }

            double netAmount = totalAmount;
            double changedAmount = ticket.getTenderedAmount() - netAmount;

            if (changedAmount < 0) {
                changedAmount = 0;
            }

            map.put("totalText", POSConstants.RECEIPT_REPORT_TOTAL_LABEL + " " + currencySymbol);
            map.put("discountText", POSConstants.RECEIPT_REPORT_DISCOUNT_LABEL + " " +  currencySymbol);
            map.put("taxText", POSConstants.RECEIPT_REPORT_TAX_LABEL + " " + currencySymbol);
            map.put("serviceChargeText", POSConstants.RECEIPT_REPORT_SERVICE_CHARGE_LABEL + " " + currencySymbol);
            map.put("tipsText", POSConstants.RECEIPT_REPORT_TIPS_LABEL + " " + currencySymbol);
            map.put("netAmountText", POSConstants.RECEIPT_REPORT_NETAMOUNT_LABEL + " " + currencySymbol);
            map.put("paidAmountText", POSConstants.RECEIPT_REPORT_PAIDAMOUNT_LABEL + " " + currencySymbol);
            map.put("dueAmountText", POSConstants.RECEIPT_REPORT_DUEAMOUNT_LABEL + " " + currencySymbol);
            map.put("changeAmountText", POSConstants.RECEIPT_REPORT_CHANGEAMOUNT_LABEL + " " + currencySymbol);

            map.put("netAmount", NumberUtil.formatNumber(netAmount));
            map.put("paidAmount", NumberUtil.formatNumber(ticket.getTenderedAmount()));
            map.put("dueAmount", NumberUtil.formatNumber(ticket.getDueAmount()));
            map.put("changedAmount", NumberUtil.formatNumber(changedAmount));
            map.put("grandSubtotal", NumberUtil.formatNumber(ticket.getSubtotalAmount()));
            map.put("footerMessage", restaurant.getTicketFooterMessage());

            String messageString = "<html>";
            if(ticket.getCustomer() != null) {
                Customer customer = ticket.getCustomer();
                if(customer.hasProperty("mykalaid")) {
                    messageString += "<br/>Customer: " + customer.getName();
                }
            }
            if(ticket.hasProperty("mykaladiscount")) {
                messageString += "<br/>My Kala point: " + ticket.getProperty("mykalapoing");
                messageString += "<br/>My Kala discount: " + ticket.getDiscountAmount();
            }
            messageString += "</html>";
            map.put("additionalProperties", messageString);
        }

        return map;
    }

    private static StringBuilder buildTicketHeader(Ticket ticket, TicketPrintProperties printProperties) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy, h:m a");

        StringBuilder ticketHeaderBuilder = new StringBuilder();
        ticketHeaderBuilder.append("<html>");

        beginRow(ticketHeaderBuilder);
        addColumn(ticketHeaderBuilder, "*" + (ticket.getTicketType() == null ? Ticket.DINE_IN : ticket.getTicketType()) + "*");
        endRow(ticketHeaderBuilder);

        beginRow(ticketHeaderBuilder);
        addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TERMINAL_LABEL + Application.getInstance().getTerminal().getId());
        endRow(ticketHeaderBuilder);

        beginRow(ticketHeaderBuilder);
        addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ticket.getId());
        endRow(ticketHeaderBuilder);

        if (Ticket.DINE_IN.equalsIgnoreCase(ticket.getTicketType())) {
            beginRow(ticketHeaderBuilder);
            addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_TABLE_NO_LABEL + ticket.getTableNumber());
            endRow(ticketHeaderBuilder);

            beginRow(ticketHeaderBuilder);
            addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_GUEST_NO_LABEL + ticket.getNumberOfGuests());
            endRow(ticketHeaderBuilder);
        }

        beginRow(ticketHeaderBuilder);
        addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_SERVER_LABEL + ticket.getOwner());
        endRow(ticketHeaderBuilder);

        beginRow(ticketHeaderBuilder);
        addColumn(ticketHeaderBuilder, POSConstants.RECEIPT_REPORT_DATE_LABEL + dateFormat.format(new Date()));
        endRow(ticketHeaderBuilder);

        beginRow(ticketHeaderBuilder);
        addColumn(ticketHeaderBuilder, "");
        endRow(ticketHeaderBuilder);

        //customer info section
        if (!printProperties.isKitchenPrint() && !Ticket.DINE_IN.equalsIgnoreCase(ticket.getTicketType())) {

            Customer customer = ticket.getCustomer();

            if (customer != null) {
                beginRow(ticketHeaderBuilder);
                addColumn(ticketHeaderBuilder, "*Delivery to*");
                endRow(ticketHeaderBuilder);

                if (StringUtils.isNotEmpty(customer.getName())) {
                    beginRow(ticketHeaderBuilder);
                    addColumn(ticketHeaderBuilder, customer.getName());
                    endRow(ticketHeaderBuilder);
                }

                if (StringUtils.isNotEmpty(ticket.getDeliveryAddress())) {
                    beginRow(ticketHeaderBuilder);
                    addColumn(ticketHeaderBuilder, ticket.getDeliveryAddress());
                    endRow(ticketHeaderBuilder);
                }
                else {
                    beginRow(ticketHeaderBuilder);
                    addColumn(ticketHeaderBuilder, "Pickup from hotel");
                    endRow(ticketHeaderBuilder);
                }

                if (StringUtils.isNotEmpty(customer.getTelephoneNo())) {
                    beginRow(ticketHeaderBuilder);
                    addColumn(ticketHeaderBuilder, "Tel: " + customer.getTelephoneNo());
                    endRow(ticketHeaderBuilder);
                }

                if(ticket.getDeliveryDate() != null) {
                    beginRow(ticketHeaderBuilder);
                    addColumn(ticketHeaderBuilder, "Delivery: " + dateFormat.format(ticket.getDeliveryDate()));
                    endRow(ticketHeaderBuilder);
                }
            }
        }

        ticketHeaderBuilder.append("</html>");
        return ticketHeaderBuilder;
    }

    public static void printTicketToKitchen(Ticket ticket) {

        File kitchenFile = null;

        try {

            File folder = new File("kitchen_spool");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            kitchenFile = new File(folder, "ticket_"+ticket.getId() + ".txt");

            if (kitchenFile.exists()) {
                kitchenFile.delete();
            } else {
                kitchenFile.createNewFile();
            }

            TicketPrintProperties printProperties = new TicketPrintProperties("*** DAPUR ***", false, false, false);
            printProperties.setKitchenPrint(true);

            JasperPrint jasperPrintTXT = createTextReceipt(ticket, printProperties);
            jasperPrintTXT.setName("KitchenReceipt");
            jasperPrintTXT.setProperty("printerName", PrintConfig.getKitchenPrinterName());
            JRTextExporter text_exporter = new JRTextExporter();
            text_exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrintTXT);
            text_exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, kitchenFile.getAbsolutePath());
            text_exporter.setParameter(JRTextExporterParameter.LINE_SEPARATOR, "\n");
            text_exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, Float.valueOf("10"));
            text_exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, Float.valueOf("7.4"));
            text_exporter.exportReport();

            System.out.println(kitchenFile.getAbsoluteFile());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Application.getInstance().isARMLinux()) {
            try {
                TicketPrintProperties printProperties = new TicketPrintProperties("*** DAPUR ***", false, false, false);
                printProperties.setKitchenPrint(true);
                JasperPrint jasperPrint = createPrint(ticket, printProperties);
                jasperPrint.setName("KitchenReceipt");
                jasperPrint.setProperty("printerName", PrintConfig.getKitchenPrinterName());
                JasperPrintManager.printReport(jasperPrint, false);
            } catch (Exception e) {
                logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
            }

        } else {

            try {

                Runtime.getRuntime().exec("/usr/bin/lp -d " + PrintConfig.getKitchenPrinterName() + " " + kitchenFile.getAbsolutePath());

                System.out.println("/usr/bin/lp -d " + PrintConfig.getKitchenPrinterName() + " " + kitchenFile.getAbsolutePath());
                //System.out.println("Receipt printing");
                //no exception, so print to kitchen successful.
                //now mark items as printed.
                markItemsAsPrinted(ticket);

            } catch (Exception e) {
                logger.error(com.floreantpos.POSConstants.PRINT_ERROR, e);
            }
        }
    }

    private static void markItemsAsPrinted(Ticket ticket) {
        List<TicketItem> ticketItems = ticket.getTicketItems();
        if (ticketItems != null) {
            for (TicketItem ticketItem : ticketItems) {
                if (!ticketItem.isPrintedToKitchen()) {
                    ticketItem.setPrintedToKitchen(true);
                }

                List<TicketItemModifierGroup> modifierGroups = ticketItem.getTicketItemModifierGroups();
                if (modifierGroups != null) {
                    for (TicketItemModifierGroup modifierGroup : modifierGroups) {
                        modifierGroup.setPrintedToKitchen(true);
                    }
                }

                List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
                if (cookingInstructions != null) {
                    for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
                        ticketItemCookingInstruction.setPrintedToKitchen(true);
                    }
                }
            }
        }
    }
}
