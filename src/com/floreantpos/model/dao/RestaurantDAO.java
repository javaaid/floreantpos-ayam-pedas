package com.floreantpos.model.dao;

import com.floreantpos.model.Restaurant;



public class RestaurantDAO extends BaseRestaurantDAO {

	/**
	 * Default constructor.  Can be used in place of getInstance()
	 */
	public RestaurantDAO () {}

	public static Restaurant getWorkingRestaurant() {
		return getInstance().get(1);
	}
}