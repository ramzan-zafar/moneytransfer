package com.moneytransfer.exception;

public class UserException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3386793520819588183L;

	public UserException(String message) {
        super(message);
    }
	public UserException(String msg, Throwable cause) {
		super(msg, cause);
	}
}