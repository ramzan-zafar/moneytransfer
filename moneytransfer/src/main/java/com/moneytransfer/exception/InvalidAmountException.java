package com.moneytransfer.exception;


public class InvalidAmountException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1229069898156723082L;

	public InvalidAmountException(String message){
        super(message);
    }
}
