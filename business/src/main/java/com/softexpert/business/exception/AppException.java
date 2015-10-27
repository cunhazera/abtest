package com.softexpert.business.exception;

public class AppException extends Exception {

	private static final long serialVersionUID = 223250845931222356L;
	public int status = 404;

	public AppException(String message) {
		super(message);
	}

}
