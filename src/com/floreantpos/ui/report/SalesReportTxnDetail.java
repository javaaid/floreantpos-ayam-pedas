package com.floreantpos.ui.report;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.SalesReportModelFactory;
import com.floreantpos.report.SalesReportTxnDetailModel;
import com.floreantpos.report.TxnDetailReportItem;
import com.floreantpos.report.service.ReportService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import org.jdesktop.swingx.calendar.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalesReportTxnDetail extends Report {
	private SalesReportTxnDetailModel itemReportModel;
	private SalesReportTxnDetailModel modifierReportModel;
    static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public SalesReportTxnDetail() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		createModels();

        SalesReportTxnDetailModel itemReportModel = this.itemReportModel;
        SalesReportTxnDetailModel modifierReportModel = this.modifierReportModel;

		JasperReport itemReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/ui/report/sales_sub_report_txn_detail.jasper"));
		JasperReport modifierReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/ui/report/sales_sub_report_txn_detail.jasper"));

		HashMap map = new HashMap();
		ReportUtil.populateRestaurantProperties(map);
		map.put("reportTitle", "Laporan Penjualan Detail");
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		map.put("dateRange", ReportService.formatShortDate(getStartDate()) + " - " + ReportService.formatShortDate(getEndDate()));
		map.put("terminalName", com.floreantpos.POSConstants.ALL);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("modifierDataSource", new JRTableModelDataSource(modifierReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("modifierGrandTotal", modifierReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
		map.put("modifierReport", modifierReport);

		JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReportTxnDetail.class.getResource("/com/floreantpos/ui/report/sales_report_txn_detail.jasper"));

		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		viewer = new JRViewer(print);
	}

	@Override
	public boolean isDateRangeSupported() {
		return true;
	}

	@Override
	public boolean isTypeSupported() {
		return true;
	}

	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());

		List<Ticket> tickets = TicketDAO.getInstance().findTicketsOrdered(date1, date2, getReportType() == Report.REPORT_TYPE_1);

		LinkedHashMap<String, TxnDetailReportItem> itemMap = new LinkedHashMap<String, TxnDetailReportItem>();

        LinkedHashMap<String, TxnDetailReportItem> modifierMap = new LinkedHashMap<String, TxnDetailReportItem>();

		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket t = (Ticket) iter.next();
			
			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());

			List<TicketItem> ticketItems = ticket.getTicketItems();
			if (ticketItems == null)
				continue;

			String key = null;

			for (TicketItem ticketItem : ticketItems) {
				if (ticketItem.getItemId() == null) {
					key = ticketItem.getName() + "-" +ticketItem.getTicket().getCreateDate();
				}
				else {
					key = ticketItem.getItemId().toString() + "-" +ticketItem.getTicket().getCreateDate();
				}

                TxnDetailReportItem reportItem = itemMap.get(key);

				if (reportItem == null) {
					reportItem = new TxnDetailReportItem();
                    reportItem.setTgl(sdf.format(ticketItem.getTicket().getCreateDate()));
					reportItem.setId(key);
					reportItem.setTicketId(String.valueOf(ticketItem.getTicket().getId()));
					reportItem.setPrice(ticketItem.getUnitPrice());
					reportItem.setName(ticketItem.getName());
					reportItem.setTaxRate(ticketItem.getTaxRate());

					itemMap.put(key, reportItem);
				}

				reportItem.setQuantity(ticketItem.getItemCount() + reportItem.getQuantity());
				reportItem.setTotal(reportItem.getTotal() + ticketItem.getSubtotalAmountWithoutModifiers());

				if (ticketItem.isHasModifiers() && ticketItem.getTicketItemModifierGroups() != null) {
					List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();

					for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
						List<TicketItemModifier> modifiers = ticketItemModifierGroup.getTicketItemModifiers();
						for (TicketItemModifier modifier : modifiers) {
							if (modifier.getItemId() == null) {
								key = modifier.getName();
							}
							else {
								key = modifier.getItemId().toString();
							}
                            TxnDetailReportItem modifierReportItem = modifierMap.get(key);
							if (modifierReportItem == null) {
								modifierReportItem = new TxnDetailReportItem();
								modifierReportItem.setId(key);
								modifierReportItem.setPrice(modifier.getUnitPrice());
								modifierReportItem.setName(modifier.getName());
								modifierReportItem.setTaxRate(modifier.getTaxRate());

								modifierMap.put(key, modifierReportItem);
							}
							modifierReportItem.setQuantity(modifierReportItem.getQuantity() + 1);
							//modifierReportItem.setTotal(modifierReportItem.getTotal() + modifier.getTotal());
						}
					}
				}
			}
			//ticket = null;
			iter.remove();
		}

        itemReportModel = new SalesReportTxnDetailModel();
		itemReportModel.setItems(new ArrayList<TxnDetailReportItem>(itemMap.values()));
		itemReportModel.calculateGrandTotal();

		modifierReportModel = new SalesReportTxnDetailModel();
		modifierReportModel.setItems(new ArrayList<TxnDetailReportItem>(modifierMap.values()));
		modifierReportModel.calculateGrandTotal();
	}
}
