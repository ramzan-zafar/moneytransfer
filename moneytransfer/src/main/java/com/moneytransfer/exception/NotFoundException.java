package com.moneytransfer.exception;



public class NotFoundException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2253055046347302993L;

	public NotFoundException(String message){
        super(message);
    }
	
	public NotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
