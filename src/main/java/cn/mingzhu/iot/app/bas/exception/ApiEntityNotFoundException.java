package cn.mingzhu.iot.app.bas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;


public class ApiEntityNotFoundException extends ZyhApiException {
	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public ApiEntityNotFoundException() {
		super(HttpStatus.NOT_FOUND);
	}
	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public ApiEntityNotFoundException(@Nullable String reason) {
		super(HttpStatus.NOT_FOUND, reason);
	}

	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation, as well as a nested exception.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 * @param cause a nested exception (optional)
	 */
	public ApiEntityNotFoundException(@Nullable String reason, @Nullable Throwable cause) {
		super(HttpStatus.NOT_FOUND, reason, cause);
	}

}
