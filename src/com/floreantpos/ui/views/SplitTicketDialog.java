/*
 * SplitTicketView.java
 *
 * Created on September 3, 2006, 11:11 PM
 */

package com.floreantpos.ui.views;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.TicketForSplitView;

/**
 *
 * @author  MShahriar
 */
public class SplitTicketDialog extends POSDialog {
	private Ticket ticket;

	/** Creates new form SplitTicketView */
	public SplitTicketDialog() {
		super(Application.getPosWindow(), true, true);

		initComponents();

		mainTicketView.setViewNumber(1);
		ticketView2.setViewNumber(2);
		ticketView3.setViewNumber(3);
		ticketView4.setViewNumber(4);

		mainTicketView.setTicketView1(ticketView2);
		mainTicketView.setTicketView2(ticketView3);
		mainTicketView.setTicketView3(ticketView4);

		ticketView2.setTicketView1(mainTicketView);
		ticketView2.setTicketView2(ticketView3);
		ticketView2.setTicketView3(ticketView4);

		ticketView3.setTicketView1(mainTicketView);
		ticketView3.setTicketView2(ticketView2);
		ticketView3.setTicketView3(ticketView4);

		ticketView4.setTicketView1(mainTicketView);
		ticketView4.setTicketView2(ticketView2);
		ticketView4.setTicketView3(ticketView3);

		ticketView3.setVisible(false);
		ticketView4.setVisible(false);

		setSize(794, 575);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        titlePanel1 = new com.floreantpos.ui.TitlePanel();
        transparentPanel1 = new com.floreantpos.swing.TransparentPanel();
        btnFinish = new com.floreantpos.swing.PosButton();
        btnCancel = new com.floreantpos.swing.PosButton();
        transparentPanel2 = new com.floreantpos.swing.TransparentPanel();
        transparentPanel3 = new com.floreantpos.swing.TransparentPanel();
        transparentPanel5 = new com.floreantpos.swing.TransparentPanel();
        pOSTitleLabel1 = new com.floreantpos.swing.POSTitleLabel();
        btnNumSplit2 = new com.floreantpos.swing.POSToggleButton("2");
        btnNumSplit3 = new com.floreantpos.swing.POSToggleButton("3");
        btnNumSplit4 = new com.floreantpos.swing.POSToggleButton("4");
        lblTicketId = new com.floreantpos.swing.POSTitleLabel();
        transparentPanel4 = new com.floreantpos.swing.TransparentPanel();
        ticket1Container = new org.jdesktop.swingx.JXImagePanel();
        mainTicketView = new com.floreantpos.ui.views.order.TicketForSplitView();
        ticket2Container = new org.jdesktop.swingx.JXImagePanel();
        ticketView2 = new com.floreantpos.ui.views.order.TicketForSplitView();
        ticket3Container = new org.jdesktop.swingx.JXImagePanel();
        ticketView3 = new com.floreantpos.ui.views.order.TicketForSplitView();
        ticket4Container = new org.jdesktop.swingx.JXImagePanel();
        ticketView4 = new com.floreantpos.ui.views.order.TicketForSplitView();

        titlePanel1.setTitle(com.floreantpos.POSConstants.SPLIT_TICKET);
        getContentPane().add(titlePanel1, java.awt.BorderLayout.NORTH);

        btnFinish.setText(com.floreantpos.POSConstants.FINISH);
        btnFinish.setPreferredSize(new java.awt.Dimension(140, 50));
        btnFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinishActionPerformed(evt);
            }
        });

        transparentPanel1.add(btnFinish);

        btnCancel.setText(com.floreantpos.POSConstants.CANCEL);
        btnCancel.setPreferredSize(new java.awt.Dimension(140, 50));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        transparentPanel1.add(btnCancel);

        getContentPane().add(transparentPanel1, java.awt.BorderLayout.SOUTH);

        transparentPanel2.setLayout(new java.awt.BorderLayout());

        transparentPanel3.setLayout(new java.awt.BorderLayout());

        transparentPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        pOSTitleLabel1.setText(com.floreantpos.POSConstants.NUMBER_OF_SPLITS);
        transparentPanel5.add(pOSTitleLabel1);

        buttonGroup1.add(btnNumSplit2);
        btnNumSplit2.setSelected(true);
        btnNumSplit2.setText("2");
        btnNumSplit2.setPreferredSize(new java.awt.Dimension(60, 40));
        btnNumSplit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNumSplit2ActionPerformed(evt);
            }
        });

        transparentPanel5.add(btnNumSplit2);

        buttonGroup1.add(btnNumSplit3);
        btnNumSplit3.setText("3");
        btnNumSplit3.setPreferredSize(new java.awt.Dimension(60, 40));
        btnNumSplit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNumSplit3ActionPerformed(evt);
            }
        });

        transparentPanel5.add(btnNumSplit3);

        buttonGroup1.add(btnNumSplit4);
        btnNumSplit4.setText("4");
        btnNumSplit4.setPreferredSize(new java.awt.Dimension(60, 40));
        btnNumSplit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNumSplit4ActionPerformed(evt);
            }
        });

        transparentPanel5.add(btnNumSplit4);

        transparentPanel3.add(transparentPanel5, java.awt.BorderLayout.EAST);

        lblTicketId.setText("   ORIGINAL TICKET ID:TICKET_ID");
        transparentPanel3.add(lblTicketId, java.awt.BorderLayout.CENTER);

        transparentPanel2.add(transparentPanel3, java.awt.BorderLayout.NORTH);

        transparentPanel4.setLayout(new java.awt.GridLayout(1, 0));

        ticket1Container.setLayout(new java.awt.BorderLayout());

        ticket1Container.add(mainTicketView, java.awt.BorderLayout.CENTER);

        transparentPanel4.add(ticket1Container);

        ticket2Container.setLayout(new java.awt.BorderLayout());

        ticket2Container.add(ticketView2, java.awt.BorderLayout.CENTER);

        transparentPanel4.add(ticket2Container);

        ticket3Container.setLayout(new java.awt.BorderLayout());

        ticket3Container.add(ticketView3, java.awt.BorderLayout.CENTER);

        transparentPanel4.add(ticket3Container);

        ticket4Container.setLayout(new java.awt.BorderLayout());

        ticket4Container.add(ticketView4, java.awt.BorderLayout.CENTER);

        transparentPanel4.add(ticket4Container);

        transparentPanel2.add(transparentPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(transparentPanel2, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		setTicket(null);
		dispose();
	}//GEN-LAST:event_btnCancelActionPerformed

	private void btnFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinishActionPerformed
		Session session = null;
		Transaction tx = null;

		try {
			TicketDAO dao = new TicketDAO();
			session = dao.getSession();
			tx = session.beginTransaction();

			saveTicket(mainTicketView, session);
			saveTicket(ticketView2, session);
			saveTicket(ticketView3, session);
			saveTicket(ticketView4, session);

			tx.commit();
			
			//save the action
			ActionHistoryDAO.getInstance().saveHistory(Application.getCurrentUser(), ActionHistory.SPLIT_CHECK, com.floreantpos.POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":"+mainTicketView.getTicket().getId());
			
			dispose();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (Exception x) {
			}
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		} finally {
			try {
				if (session!=null) session.close();
			} catch (Exception x) {
                x.printStackTrace();
			}
		}
	}//GEN-LAST:event_btnFinishActionPerformed

	private void btnNumSplit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNumSplit4ActionPerformed
		ticketView3.setVisible(true);
		ticketView4.setVisible(true);
	}//GEN-LAST:event_btnNumSplit4ActionPerformed

	private void btnNumSplit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNumSplit3ActionPerformed
		ticketView3.setVisible(true);

		if (ticketView4.isVisible()) {
			Ticket ticket4 = ticketView4.getTicket();

			List<TicketItem> ticketItems = ticket4.getTicketItems();
			for (TicketItem item : ticketItems) {
				ticketView4.transferAllTicketItem(item, mainTicketView);
			}
			ticketView4.setVisible(false);
		}
	}//GEN-LAST:event_btnNumSplit3ActionPerformed

	private void btnNumSplit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNumSplit2ActionPerformed
		if (ticketView3.isVisible()) {
			Ticket ticket3 = ticketView3.getTicket();

			List<TicketItem> ticketItems = new ArrayList<TicketItem>(ticket3.getTicketItems());
			for (TicketItem item : ticketItems) {
				ticketView3.transferAllTicketItem(item, mainTicketView);
			}
			ticketView3.setVisible(false);
		}

		if (ticketView4.isVisible()) {
			Ticket ticket4 = ticketView4.getTicket();

			List<TicketItem> ticketItems = ticket4.getTicketItems();
			for (TicketItem item : ticketItems) {
				ticketView4.transferAllTicketItem(item, mainTicketView);
			}
			ticketView4.setVisible(false);
		}
	}//GEN-LAST:event_btnNumSplit2ActionPerformed

	public void saveTicket(TicketForSplitView view, Session session) {
		if (!view.isVisible())
			return;

		view.updateModel();

		Ticket ticket = view.getTicket();
		if (ticket.getTicketItems().size() <= 0)
			return;
		
		ticket.setTableNumber(mainTicketView.getTicket().getTableNumber());
		session.saveOrUpdate(ticket);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.floreantpos.swing.PosButton btnCancel;
    private com.floreantpos.swing.PosButton btnFinish;
    private com.floreantpos.swing.POSToggleButton btnNumSplit2;
    private com.floreantpos.swing.POSToggleButton btnNumSplit3;
    private com.floreantpos.swing.POSToggleButton btnNumSplit4;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.floreantpos.swing.POSTitleLabel lblTicketId;
    private com.floreantpos.ui.views.order.TicketForSplitView mainTicketView;
    private com.floreantpos.swing.POSTitleLabel pOSTitleLabel1;
    private org.jdesktop.swingx.JXImagePanel ticket1Container;
    private org.jdesktop.swingx.JXImagePanel ticket2Container;
    private org.jdesktop.swingx.JXImagePanel ticket3Container;
    private org.jdesktop.swingx.JXImagePanel ticket4Container;
    private com.floreantpos.ui.views.order.TicketForSplitView ticketView2;
    private com.floreantpos.ui.views.order.TicketForSplitView ticketView3;
    private com.floreantpos.ui.views.order.TicketForSplitView ticketView4;
    private com.floreantpos.ui.TitlePanel titlePanel1;
    private com.floreantpos.swing.TransparentPanel transparentPanel1;
    private com.floreantpos.swing.TransparentPanel transparentPanel2;
    private com.floreantpos.swing.TransparentPanel transparentPanel3;
    private com.floreantpos.swing.TransparentPanel transparentPanel4;
    private com.floreantpos.swing.TransparentPanel transparentPanel5;
    // End of variables declaration//GEN-END:variables

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
		if (ticket != null)
			lblTicketId.setText(com.floreantpos.POSConstants.ORIGINAL_TICKET_ID + ": " + ticket.getId());
		mainTicketView.setTicket(ticket);
	}
}