package com.floreantpos.report.service;

import com.floreantpos.model.*;
import com.floreantpos.model.dao.CouponAndDiscountDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.*;
import com.floreantpos.report.CreditCardReport.CreditCardReportData;
import com.floreantpos.report.JournalReportModel.JournalReportData;
import com.floreantpos.report.MenuUsageReport.MenuUsageReportData;
import com.floreantpos.report.SalesDetailedReport.DrawerPullData;
import com.floreantpos.report.ServerProductivityReport.ServerProductivityReportData;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReportService {
	private static SimpleDateFormat fullDateFormatter = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
	private static SimpleDateFormat shortDateFormatter = new SimpleDateFormat("dd MMM yyyy ");
	
	public static String formatFullDate(Date date) {
		return fullDateFormatter.format(date);
	}
	
	public static String formatShortDate(Date date) {
		return shortDateFormatter.format(date);
	}
	
	public CreditCardReport getCreditCardReport(Date fromDate, Date toDate) {
		CreditCardReport report = new CreditCardReport();

		TicketDAO dao = TicketDAO.getInstance();
		Session session = dao.getSession();
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
		criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
		criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
		criteria.add(Restrictions.or(Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, PosTransaction.TYPE_CREDIT_CARD), Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, PosTransaction.TYPE_DEBIT_CARD)));
		List list = criteria.list();

		int totalSalesCount = 0;
		double totalSales = 0;
		double netTips = 0;
		double tipsPaid = 0;
		double netCharge = 0;

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();
			CreditCardReportData data = new CreditCardReportData();

			data.setRefId(ticket.getId());
			data.setCardType(ticket.getCardType());
			data.setSubtotal(ticket.getSubtotalAmount());
			data.setTotal(ticket.getTotalAmount());

			if (ticket.getGratuity() != null) {
				Gratuity gratuity = ticket.getGratuity();
				data.setTips(gratuity.getAmount());
				netTips += gratuity.getAmount();
				if (gratuity.isPaid()) {
					tipsPaid += gratuity.getAmount();
				}
			}

			totalSales += ticket.getSubtotalAmount();
			report.addReportData(data);
		}
		totalSalesCount = list.size();
		netCharge = totalSales + netTips;

		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setNetCharge(netCharge);
		report.setTotalSales(totalSales);
		report.setTotalSalesCount(totalSalesCount);
		report.setNetTips(netTips);

		return report;
	}

	public MenuUsageReport getMenuUsageReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		MenuUsageReport report = new MenuUsageReport();
		Session session = null;

		try {

			session = dao.getSession();

			Criteria criteria = session.createCriteria(MenuCategory.class);
			List<MenuCategory> categories = criteria.list();
			MenuCategory miscCategory = new MenuCategory();
			miscCategory.setName(com.floreantpos.POSConstants.MISC);
			categories.add(miscCategory);

			for (MenuCategory category : categories) {
				criteria = session.createCriteria(TicketItem.class, "item");
				criteria.createCriteria("ticket", "t");
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
				projectionList.add(Projections.sum(TicketItem.PROP_DISCOUNT_AMOUNT));
				criteria.setProjection(projectionList);
				criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName()));
				criteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, fromDate));
				criteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, toDate));
				criteria.add(Restrictions.eq("t." + Ticket.PROP_PAID, Boolean.TRUE));

				List datas = criteria.list();
				if (datas.size() > 0) {
					Object[] objects = (Object[]) datas.get(0);

					MenuUsageReportData data = new MenuUsageReportData();
					data.setCategoryName(category.getName());

					if (objects.length > 0 && objects[0] != null)
						data.setCount(((Number) objects[0]).intValue());

					if (objects.length > 1 && objects[1] != null)
						data.setGrossSales(((Number) objects[1]).doubleValue());

					if (objects.length > 2 && objects[2] != null)
						data.setDiscount(((Number) objects[2]).doubleValue());

					data.calculate();
					report.addReportData(data);
				}
			}

			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public ServerProductivityReport getServerProductivityReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		ServerProductivityReport report = new ServerProductivityReport();
		Session session = null;

		try {

			session = dao.getSession();

			Criteria criteria = session.createCriteria(User.class);
			//criteria.add(Restrictions.eq(User.PROP_USER_TYPE, User.USER_TYPE_SERVER));
			List<User> servers = criteria.list();

			criteria = session.createCriteria(MenuCategory.class);
			List<MenuCategory> categories = criteria.list();
			MenuCategory miscCategory = new MenuCategory();
			miscCategory.setName(com.floreantpos.POSConstants.MISC);
			categories.add(miscCategory);
			
			for (User server : servers) {
				ServerProductivityReportData data = new ServerProductivityReportData();
				data.setServerName(server.getUserId() + "/" + server.toString());
				criteria = session.createCriteria(Ticket.class);
				criteria.add(Restrictions.eq(Ticket.PROP_OWNER, server));
				criteria.add(Restrictions.eq(Ticket.PROP_PAID, Boolean.TRUE));
				criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
				criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
				
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.rowCount());
				projectionList.add(Projections.sum(Ticket.PROP_NUMBER_OF_GUESTS));
				projectionList.add(Projections.sum(TicketItem.PROP_TOTAL_AMOUNT));
				
				criteria.setProjection(projectionList);
				
				Object[] o = (Object[]) criteria.uniqueResult();
				int totalCheckCount = 0;
				double totalServerSale = 0;
				if(o != null) {
					if(o.length > 0 && o[0] != null) {
						int i = ((Number) o[0]).intValue();
						data.setTotalCheckCount(totalCheckCount = i);
					}
					if(o.length > 1 && o[1] != null) {
						int i = ((Number) o[1]).intValue();
						data.setTotalGuestCount(i);
					}
					if(o.length > 2 && o[2] != null) {
						totalServerSale = ((Number) o[2]).doubleValue();
						data.setTotalSales(totalServerSale);
					}
				}
				
				data.calculate();
				report.addReportData(data);
				
				for (MenuCategory category : categories) {
					data = new ServerProductivityReportData();
					data.setServerName(server.getUserId() + "/" + server.toString());
					
					criteria = session.createCriteria(TicketItem.class, "item");
					criteria.createCriteria(TicketItem.PROP_TICKET, "t");
					
					projectionList = Projections.projectionList();
					criteria.setProjection(projectionList);
					projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
					projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
					projectionList.add(Projections.sum("t." + Ticket.PROP_DISCOUNT_AMOUNT));
					projectionList.add(Projections.rowCount());
					
					criteria.add(Restrictions.eq("item." + TicketItem.PROP_CATEGORY_NAME, category.getName()));
					criteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, fromDate));
					criteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, toDate));
					criteria.add(Restrictions.eq("t." + Ticket.PROP_OWNER, server));
					criteria.add(Restrictions.eq("t." + Ticket.PROP_PAID, Boolean.TRUE));

					List datas = criteria.list();
					if (datas.size() > 0) {
						Object[] objects = (Object[]) datas.get(0);

						
						data.setCategoryName(category.getName());
						data.setTotalCheckCount(totalCheckCount);
						if (objects.length > 0 && objects[0] != null) {
							int i = ((Number) objects[0]).intValue();
							data.setCheckCount(i);
						}

						if (objects.length > 1 && objects[1] != null) {
							double d = ((Number) objects[1]).doubleValue();
							data.setGrossSales(d);
						}

						if (objects.length > 2 && objects[2] != null) {
							double d = ((Number) objects[2]).doubleValue();
							data.setSalesDiscount(d);
						}
						data.setAllocation( (data.getGrossSales() / totalServerSale) * 100.0 ); 
						data.calculate();
						report.addReportData(data);
					}
				}
			}
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public JournalReportModel getJournalReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		JournalReportModel report = new JournalReportModel();
		Session session = null;
		
		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {

			session = dao.getSession();
			Criteria criteria = session.createCriteria(ActionHistory.class);
			criteria.add(Restrictions.ge(ActionHistory.PROP_ACTION_TIME, fromDate));
			criteria.add(Restrictions.le(ActionHistory.PROP_ACTION_TIME, toDate));
			List<ActionHistory> list = criteria.list();
			
			for (ActionHistory history : list) {
				JournalReportData data = new JournalReportData();
				data.setRefId(history.getId());
				data.setAction(history.getActionName());
				data.setUserInfo(history.getPerformer().getUserId() + "/" + history.getPerformer());
				data.setTime(history.getActionTime());
				data.setComments(history.getDescription());
				report.addReportData(data);
			}
			
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public SalesBalanceReport getSalesBalanceReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		SalesBalanceReport report = new SalesBalanceReport();
		Session session = null;
		
		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {
			
			session = dao.getSession();
			
			//gross taxable sales
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TAX_EXEMPT, Boolean.FALSE));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			Object object = criteria.uniqueResult();
			if(object != null && object instanceof Number) {
				double amount = ((Number) object).doubleValue();
				report.setGrossTaxableSalesAmount(amount);
			}
			
			//gross non-taxable sales
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TAX_EXEMPT, Boolean.TRUE));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			object = criteria.uniqueResult();
			if(object != null && object instanceof Number) {
				double amount = ((Number) object).doubleValue();
				report.setGrossNonTaxableSalesAmount(amount);
			}
			
			//discounts
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			
			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				List<TicketCouponAndDiscount> discounts = ticket.getCouponAndDiscounts();
				if (discounts != null) {
					for (TicketCouponAndDiscount discount : discounts) {
						report.setDiscountAmount(report.getDiscountAmount() + discount.getValue());
					}
				}
			}

			//tax
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(Ticket.PROP_TAX_AMOUNT));
			criteria.setProjection(projectionList);
			Object o1 =  criteria.uniqueResult();
			if(o1 instanceof Number) {
				double amount = ((Number) o1).doubleValue();
				report.setSalesTaxAmount(amount);
			}
			
			//tips
			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "g");
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("g." + Gratuity.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			object = criteria.uniqueResult();
			if(object instanceof Number) {
				double amount = ((Number) object).doubleValue();
				report.setChargedTipsAmount(amount);
			}
			
			//cash receipt
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, PosTransaction.TYPE_CASH));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(Ticket.PROP_TOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			object = criteria.uniqueResult();
			if(object != null && object instanceof Number) {
				double amount = ((Number) object).doubleValue();
				report.setCashReceiptsAmount(amount);
			}
			
			//credit card receipt
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(
					Restrictions.or(
							Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, PosTransaction.TYPE_CREDIT_CARD),
							Restrictions.eq(Ticket.PROP_TRANSACTION_TYPE, PosTransaction.TYPE_DEBIT_CARD)
							));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(Ticket.PROP_SUBTOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			object = criteria.uniqueResult();
			if(object != null && object instanceof Number) {
				double amount = ((Number) object).doubleValue();
				report.setCreditCardReceiptsAmount(amount);
			}
			
//			gift cert
			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.createAlias(GiftCertificateTransaction.PROP_TICKET, "t");
			criteria.add(Restrictions.ge("t." + Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le("t." + Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq("t." + Ticket.PROP_VOIDED, Boolean.FALSE));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_FACE_VALUE));
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_CASH_BACK_AMOUNT));
			criteria.setProjection(projectionList);
			Object[] o = (Object[]) criteria.uniqueResult();
			if(o.length > 0 && o[0] instanceof Number) {
				double amount = ((Number) o[0]).doubleValue();
				report.setGiftCertReturnAmount(amount);
			}
			if(o.length > 1 && o[1] instanceof Number) {
				double amount = ((Number) o[1]).doubleValue();
				report.setGiftCertChangeAmount(amount);
			}
			
//			tips paid
			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "gratuity");
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			criteria.add(Restrictions.eq("gratuity." + Gratuity.PROP_PAID, Boolean.TRUE));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("gratuity." + Gratuity.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			object = criteria.uniqueResult();
			if(object != null && object instanceof Number) {
				double amount = ((Number) object).doubleValue();
				report.setGrossTipsPaidAmount(amount);
			}
			
			//cash payout
			criteria = session.createCriteria(PayOutTransaction.class);
			criteria.add(Restrictions.ge(PayOutTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(PayOutTransaction.PROP_TRANSACTION_TIME, toDate));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(PayOutTransaction.PROP_SUBTOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			object = criteria.uniqueResult();
			if(object != null && object instanceof Number) {
				double amount = ((Number) object).doubleValue();
				report.setCashPayoutAmount(amount);
			}
			
			//drawer pulls
			criteria = session.createCriteria(DrawerPullReport.class);
			criteria.add(Restrictions.ge(DrawerPullReport.PROP_REPORT_TIME, fromDate));
			criteria.add(Restrictions.le(DrawerPullReport.PROP_REPORT_TIME, toDate));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(DrawerPullReport.PROP_DRAWER_ACCOUNTABLE));
			projectionList.add(Projections.sum(DrawerPullReport.PROP_BEGIN_CASH));
			criteria.setProjection(projectionList);
			o = (Object[]) criteria.uniqueResult();
			if(o.length > 0 && o[0] instanceof Number) {
				double amount = ((Number) o[0]).doubleValue();
				report.setDrawerPullsAmount(amount);
			}
			if(o.length > 1 && o[1] instanceof Number) {
				double amount = ((Number) o[1]).doubleValue();
				report.setDrawerPullsAmount(report.getDrawerPullsAmount() - amount);
			}
			
			report.calculate();
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public SalesExceptionReport getSalesExceptionReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		SalesExceptionReport report = new SalesExceptionReport();
		Session session = null;
		
		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {
			
			session = dao.getSession();
			
			//gross taxable sales
			
			//void tickets
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.TRUE));
			
			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				report.addVoidToVoidData(ticket);
			}
			
			
			//discounts
			criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.eq(Ticket.PROP_VOIDED, Boolean.FALSE));
			
			list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();
				report.addDiscountData(ticket);
			}
			
			//find all valid discounts
			CouponAndDiscountDAO discountDAO = new CouponAndDiscountDAO();
			List<CouponAndDiscount> availableCoupons = discountDAO.getValidCoupons();
			report.addEmptyDiscounts(availableCoupons);
			
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	public SalesDetailedReport getSalesDetailedReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		SalesDetailedReport report = new SalesDetailedReport();
		Session session = null;
		
		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {
			
			session = dao.getSession();
			
			Criteria criteria = session.createCriteria(DrawerPullReport.class);
			criteria.add(Restrictions.ge(DrawerPullReport.PROP_REPORT_TIME, fromDate));
			criteria.add(Restrictions.le(DrawerPullReport.PROP_REPORT_TIME, toDate));
			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				DrawerPullReport drawerPullReport = (DrawerPullReport) iter.next();
				DrawerPullData data = new DrawerPullData();
				data.setDrawerPullId(drawerPullReport.getId());
				data.setTicketCount(drawerPullReport.getTicketCount());
				data.setIdealAmount(drawerPullReport.getDrawerAccountable());
				data.setActualAmount(drawerPullReport.getCashToDeposit());
				data.setVarinceAmount(drawerPullReport.getDrawerAccountable() - drawerPullReport.getCashToDeposit());
				report.addDrawerPullData(data);
			}
			
			criteria = session.createCriteria(CreditCardTransaction.class);
			criteria.add(Restrictions.ge(CreditCardTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(CreditCardTransaction.PROP_TRANSACTION_TIME, toDate));
			list = criteria.list();
			
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				CreditCardTransaction t = (CreditCardTransaction) iter.next();
				report.addCreditCardData(t);
			}
			
			criteria = session.createCriteria(DebitCardTransaction.class);
			criteria.add(Restrictions.ge(DebitCardTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(DebitCardTransaction.PROP_TRANSACTION_TIME, toDate));
			list = criteria.list();
			
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				DebitCardTransaction t = (DebitCardTransaction) iter.next();
				report.addCreditCardData(t);
			}
			
			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.add(Restrictions.ge(GiftCertificateTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(GiftCertificateTransaction.PROP_TRANSACTION_TIME, toDate));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_TOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			Object[] object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setGiftCertReturnCount(((Number)object[0]).intValue());
			}
			if(object != null && object.length > 1 && object[1] instanceof Number) {
				report.setGiftCertReturnAmount(((Number)object[1]).doubleValue());
			}
			
			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.add(Restrictions.ge(GiftCertificateTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(GiftCertificateTransaction.PROP_TRANSACTION_TIME, toDate));
			criteria.add(Restrictions.gt(GiftCertificateTransaction.PROP_CASH_BACK_AMOUNT, (double) 0));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_CASH_BACK_AMOUNT));
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setGiftCertChangeCount(((Number)object[0]).intValue());
			}
			if(object != null && object.length > 1 && object[1] instanceof Number) {
				report.setGiftCertChangeAmount(((Number)object[1]).doubleValue());
			}
			
			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "g");
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_AMOUNT, (double) 0));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum("g." + Gratuity.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setTipsCount(((Number)object[0]).intValue());
			}
			if(object != null && object.length > 1 && object[1] instanceof Number) {
				report.setChargedTips(((Number)object[1]).doubleValue());
			}
			
			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "g");
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_AMOUNT, (double) 0));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_PAID, Boolean.TRUE));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("g." + Gratuity.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setTipsPaid(((Number)object[0]).doubleValue());
			}
			
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
