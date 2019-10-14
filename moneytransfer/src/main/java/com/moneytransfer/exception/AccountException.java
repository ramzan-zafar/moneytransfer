package com.moneytransfer.exception;

public class AccountException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3386793520819588183L;

	public AccountException(String message) {
        super(message);
    }
	public AccountException(String msg, Throwable cause) {
		super(msg, cause);
	}
}