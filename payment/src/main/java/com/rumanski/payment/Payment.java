package com.rumanski.payment;

import java.util.Date;

public class Payment {

	Long orderid;

	Long creditcardid;

	PaymentStatus status;

	Date lastTry;

	Date paid;

	Date refunded;

}
