package com.moneytransfer.exception;

public class SameAccountException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6970073724227333065L;

	public SameAccountException(String exception) {
        super(exception);
    }
}