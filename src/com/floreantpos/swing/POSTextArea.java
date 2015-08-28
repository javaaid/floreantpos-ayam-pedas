package com.floreantpos.swing;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class POSTextArea extends JTextArea implements FocusListener {

	public POSTextArea() {
		super(5,50);
		addFocusListener(this);
	}

	public POSTextArea(String text) {
		super(text,5,50);
		addFocusListener(this);
	}

	public POSTextArea(int rows, int columns) {
		super(rows, columns);
		addFocusListener(this);
	}

	public POSTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		addFocusListener(this);
	}

	public POSTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		addFocusListener(this);
	}

	public void focusGained(FocusEvent e) {
		selectAll();
	}

	public void focusLost(FocusEvent e) {
	}

}
