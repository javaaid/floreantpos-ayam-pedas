package com.floreantpos.model.dao;

import com.floreantpos.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;


public class TerminalDAO extends BaseTerminalDAO {
	
	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public TerminalDAO () {}

	public void refresh(Terminal terminal) {
		Session session = null;
        try {
			session = getSession();
			session.refresh(terminal);
		} finally {
			closeSession(session);
		}
	}
	
	public void resetCashDrawer(DrawerPullReport report, Terminal terminal, User user) throws Exception {
		Session session = null;
		Transaction  tx = null;
		
		CashDrawerResetHistory history = new CashDrawerResetHistory();
		history.setDrawerPullReport(report);
		history.setResetedBy(user);
		history.setResetTime(new Date());
		
		try {
			session = createNewSession();
			tx = session.beginTransaction();

			String hql = "update Ticket t set t.drawerResetted=true where t." + Ticket.PROP_CLOSED + "=true and t.drawerResetted=false and t.terminal=:terminal";
			Query query = session.createQuery(hql);
			query.setEntity("terminal", terminal);
			query.executeUpdate();

			hql = "update PosTransaction t set t.drawerResetted=true where t.drawerResetted=false and t.terminal=:terminal";
			query = session.createQuery(hql);
			query.setEntity("terminal", terminal);
			query.executeUpdate();
			
			terminal.setCurrentBalance(terminal.getOpeningBalance());
			update(terminal);
			save(report);
			save(history);
			tx.commit();
			
			terminal.setCurrentBalance(terminal.getOpeningBalance());
		} catch (Exception e) {
			try {
                if (tx != null) {
                    tx.rollback();
                }
            }catch(Exception x) {}
			throw e;
		} finally {
			closeSession(session);
		}
	}
}