package com.moneytransfer.exception;

public class AlreadyExistException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3386793520819588183L;

	public AlreadyExistException(String message) {
        super(message);
    }
}