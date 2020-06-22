package cn.mingzhu.iot.app.bas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class ApiIllegalArgumentException extends DmApiException {
	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public ApiIllegalArgumentException() {
		super(HttpStatus.BAD_REQUEST);
	}
	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 */
	public ApiIllegalArgumentException(@Nullable String reason) {
		super(HttpStatus.BAD_REQUEST, reason);
	}

	/**
	 * Constructor with a response status and a reason to add to the exception
	 * message as explanation, as well as a nested exception.
	 * @param status the HTTP status (required)
	 * @param reason the associated reason (optional)
	 * @param cause a nested exception (optional)
	 */
	public ApiIllegalArgumentException(@Nullable String reason, @Nullable Throwable cause) {
		super(HttpStatus.BAD_REQUEST, reason, cause);
	}
}
