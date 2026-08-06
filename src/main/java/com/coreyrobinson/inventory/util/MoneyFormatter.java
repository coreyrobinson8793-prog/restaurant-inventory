/*
 * AUTHOR: Corey Robinson
 * PURPOSE: Formats prices with a dollar sign and to two decimal places.
 * DATE: 08/06/26
 */
package com.coreyrobinson.inventory.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MoneyFormatter {
	
	/**
	 * Formats a BigDecimal amount as a string with a dollar sign and two decimal places.
	 * @param amount	The amount to format.
	 * @return			A formatted string representation of the amount.
	 */
	public static String format(BigDecimal amount) {
		if (amount == null) {
			return "";
		}
		return "$" + amount.setScale(2, RoundingMode.HALF_UP).toString();
	}
	
	private MoneyFormatter() {
		
	}
}
