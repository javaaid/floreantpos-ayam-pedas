package com.floreantpos.model;

import com.floreantpos.model.base.BaseCustomer;

import java.util.HashMap;



public class Customer extends BaseCustomer {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Customer () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Customer (java.lang.Integer autoId) {
		super(autoId);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public void addProperty(String name, String value) {
		if(getProperties() == null) {
			setProperties(new HashMap<String, Object>());
		}
		
		getProperties().put(name, value);
	}
	
	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}
	
	public Object getProperty(String key) {
		if(getProperties() == null) {
			return null;
		}
		
		return getProperties().get(key);
	}
	
	@Override
	public String toString() {
        return getName();
	}
}