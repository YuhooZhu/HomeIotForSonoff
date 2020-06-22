package cn.mingzhu.iot.app.bas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * This is base Exception for API Application. All API application exception should
 * extends it.
 *
 * @version 1.0
 */
public class DmApiException extends DmException {
	
	/**
	 * Constructor with a response status.
	 * @param status the HTTP status (required)
	 */
	public DmApiException(HttpStatus status) {
		super(status);
	}

	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public DmApiException(HttpStatus status, @Nullable String reason) {
		super(status, reason);
	}

	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation, as well as a nested exception.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 * @param cause a nested exception (optional)
	 */
	public DmApiException(HttpStatus status, @Nullable String reason, @Nullable Throwable cause) {
		super(status, reason, cause);
		
	}
}
