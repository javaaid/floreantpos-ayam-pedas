package com.floreantpos.util;

import com.floreantpos.model.*;
import com.floreantpos.model.dao.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DatabaseUtil {
	private static Log logger = LogFactory.getLog(DatabaseUtil.class);

	public static void checkConnection(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password)
			throws DatabaseConnectionException {
		Configuration configuration = _RootDAO.getNewConfiguration(null);

		configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
		configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

		configuration = configuration.setProperty("hibernate.connection.url", connectionString);
		configuration = configuration.setProperty("hibernate.connection.username", user);
		configuration = configuration.setProperty("hibernate.connection.password", password);

		checkConnection(configuration);
	}

	public static void checkConnection() throws DatabaseConnectionException {
		Configuration configuration = _RootDAO.getNewConfiguration(null);
		checkConnection(configuration);
	}

	public static void checkConnection(Configuration configuration) throws DatabaseConnectionException {
		try {
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			session.close();
		} catch (Exception e) {
			throw new DatabaseConnectionException(e);
		}
	}

	public static boolean createDatabase(String connectionString, String hibernateDialect, String hibernateConnectionDriverClass, String user, String password) {
		try {
			Configuration configuration = _RootDAO.getNewConfiguration(null);

			configuration = configuration.setProperty("hibernate.dialect", hibernateDialect);
			configuration = configuration.setProperty("hibernate.connection.driver_class", hibernateConnectionDriverClass);

			configuration = configuration.setProperty("hibernate.connection.url", connectionString);
			configuration = configuration.setProperty("hibernate.connection.username", user);
			configuration = configuration.setProperty("hibernate.connection.password", password);
			configuration = configuration.setProperty("hibernate.hbm2ddl.auto", "create");
			//configuration = configuration.setProperty("hibernate.connection.autocommit", "true");

			SchemaExport schemaExport = new SchemaExport(configuration);
			schemaExport.create(true, true);

			_RootDAO.initialize();

			Restaurant restaurant = new Restaurant();
			restaurant.setId(1);
			restaurant.setName("Demo Restaurant");
			restaurant.setAddressLine1("Jakarta");
			restaurant.setTelephone("+6262626262");
//            restaurant.setInitialCash(0d);
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);

			Tax tax = new Tax();
			tax.setName("US");
			tax.setRate((double) 6);
			TaxDAO.getInstance().saveOrUpdate(tax);

			Shift shift = new Shift();
			shift.setName(com.floreantpos.POSConstants.GENERAL);
			java.util.Date shiftStartTime = ShiftUtil.buildShiftStartTime(0, 0, 0, 11, 59, 1);
			java.util.Date shiftEndTime = ShiftUtil.buildShiftEndTime(0, 0, 0, 11, 59, 1);

			shift.setStartTime(shiftStartTime);
			shift.setEndTime(shiftEndTime);
			long length = Math.abs(shiftStartTime.getTime() - shiftEndTime.getTime());

			shift.setShiftLength(length);
			ShiftDAO.getInstance().saveOrUpdate(shift);

			UserType type = new UserType();
			type.setName(com.floreantpos.POSConstants.ADMINISTRATOR);
			type.setPermissions(new HashSet<UserPermission>(Arrays.asList(UserPermission.permissions)));
			UserTypeDAO.getInstance().saveOrUpdate(type);

			User u = new User();
			u.setUserId(123);
			u.setSsn("123");
			u.setPassword("7777");
			u.setFirstName("Admin");
			u.setLastName(com.floreantpos.POSConstants.USER);
			u.setNewUserType(type);

			UserDAO dao = new UserDAO();
			dao.saveOrUpdate(u);

			MenuCategory category = new MenuCategory();
			category.setName(com.floreantpos.POSConstants.BEVERAGE);
			category.setBeverage(Boolean.TRUE);
			category.setVisible(Boolean.TRUE);
			MenuCategoryDAO.getInstance().saveOrUpdate(category);

			MenuCategory category2 = new MenuCategory();
			category2.setName("BREAKFAST");
			category2.setBeverage(Boolean.FALSE);
			category2.setVisible(Boolean.TRUE);
			MenuCategoryDAO.getInstance().saveOrUpdate(category2);

			MenuGroup group1 = new MenuGroup();
			group1.setParent(category);
			group1.setName("MINUMAN RINGAN");
			group1.setVisible(Boolean.TRUE);
			MenuGroupDAO.getInstance().save(group1);

			MenuGroup group2 = new MenuGroup();
			group2.setParent(category2);
			group2.setName("FAVORIT");
			group2.setVisible(Boolean.TRUE);
			MenuGroupDAO.getInstance().save(group2);

			MenuItem item1 = new MenuItem();
			item1.setParent(group1);
			item1.setName("Coke");
			item1.setPrice(2.0);
			item1.setTax(tax);
			item1.setVisible(Boolean.TRUE);
			MenuItemDAO.getInstance().save(item1);

			MenuItem item2 = new MenuItem();
			item2.setParent(group2);
			item2.setName("Telor");
			item2.setPrice(2.0);
			item2.setTax(tax);
			item2.setVisible(Boolean.TRUE);
			MenuItemDAO.getInstance().save(item2);

            PayoutReason poReason1 = new PayoutReason(1);
            poReason1.setReason("Bayar Tagihan");
            PayoutReasonDAO.getInstance().save(poReason1);

            PayoutReason poReason2 = new PayoutReason(2);
            poReason2.setReason("Keperluan Karyawan");
            PayoutReasonDAO.getInstance().save(poReason2);

            PayoutReason poReason3 = new PayoutReason(3);
            poReason3.setReason("Keperluan Toko");
            PayoutReasonDAO.getInstance().save(poReason3);

            PayoutReason poReason4 = new PayoutReason(4);
            poReason4.setReason("Lain-lain");
            PayoutReasonDAO.getInstance().save(poReason4);

            PayoutRecepient poRecipient1 = new PayoutRecepient(1);
            poRecipient1.setName("PLN");
            PayoutRecepientDAO.getInstance().save(poRecipient1);

            PayoutRecepient poRecipient2 = new PayoutRecepient(2);
            poRecipient2.setName("TELKOM");
            PayoutRecepientDAO.getInstance().save(poRecipient2);

            PayoutRecepient poRecipient3 = new PayoutRecepient(3);
            poRecipient2.setName("PDAM");
            PayoutRecepientDAO.getInstance().save(poRecipient3);

            PayoutRecepient poRecipient4 = new PayoutRecepient(4);
            poRecipient4.setName("LAIN-LAIN");
            PayoutRecepientDAO.getInstance().save(poRecipient4);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
	}

	public static Configuration initialize() throws DatabaseConnectionException {
		try {

			return _RootDAO.reInitialize();

		} catch (Exception e) {
			logger.error(e);
			throw new DatabaseConnectionException(e);
		}

	}

//	public static void main(String[] args) throws Exception {
//		initialize();
//
//		List<PosTransaction> findAll = PosTransactionDAO.getInstance().findAll();
//		for (PosTransaction posTransaction : findAll) {
//			PosTransactionDAO.getInstance().delete(posTransaction);
//		}
//
//		List<Ticket> list = TicketDAO.getInstance().findAll();
//		for (Ticket ticket : list) {
//			TicketDAO.getInstance().delete(ticket);
//		}
//	}
}
