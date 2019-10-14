package com.moneytransfer.exception;



public class NotSufficientBalanceException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5587687426413325374L;

	public NotSufficientBalanceException(String exception) {
        super(exception);
    }
}
