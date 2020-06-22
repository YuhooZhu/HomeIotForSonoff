package cn.mingzhu.iot.app.bas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;
/**
 * This is base exception for System. All application exception should
 * extends it.
 *
 * @version 1.0
 */
public class DmException extends ResponseStatusException {

	/**
	 * Constructor with a response status.
	 * @param status the HTTP status (required)
	 */
	public DmException(HttpStatus status) {
		super(status);
	}

	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public DmException(HttpStatus status, @Nullable String reason) {
		super(status, reason);
	}

	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation, as well as a nested exception.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 * @param cause a nested exception (optional)
	 */
	public DmException(HttpStatus status, @Nullable String reason, @Nullable Throwable cause) {
		super(status, reason, cause);
		
	}

}
