package cn.mingzhu.iot.app.bas.constant;


public enum DmCode {
	/**
	 * GET, PUT, PATCH (OK)
	 */
	SUCCESS(0, "成功"), 
	
	// --- 1xx Informational ---
	
	// --- 2xx Success ---
	/**
	 * GET, PUT, PATCH (OK)
	 */
	OK(200, "成功"), 
	
	/**
	 * POST (Created)
	 */
	CREATED(201, "创建成功"), 
	
	/**
	 * DELETE (No Content)
	 */
	NO_CONTENT(204, "已删除"),
	
	// --- 3xx Redirection ---
	
	SEE_OTHER(303, "重定向"),
	
	// --- 4xx Client Error ---
	
	BAD_REQUEST(400, "请求错误"),

	UNAUTHORIZED(401, "无权限"),
	
	FORBIDDEN(403, "禁止"),

	NOT_FOUND(404, "失败"),
	
	METHOD_NOT_ALLOWED(405, "方法不允许"),

	NOT_ACCEPTABLE(406, "数据不可接受"),
	
	LOCKED(423, "锁定"),
	
	TOO_MANY_REQUESTS(429, "请求过多"),
	
	// --- 5xx Server Error ---
	
	INTERNAL_SERVER_ERROR(500, "服务内部错误"),
	
	NOT_IMPLEMENTED(501, "未实现");
	
	
	private final int value;

	private final String reasonPhrase;
	
	DmCode(int value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}
	
	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return this.value;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	public String getReasonPhrase() {
		return this.reasonPhrase;
	}
	
	/**
	 * Return the HTTP status series of this status code.
	 * @see DmCode.Series
	 */
	public Series series() {
		return Series.valueOf(this);
	}
	
	/**
	 * Whether this status code is in the HTTP series
	 * {@link org.springframework.http.DmCode.Series#INFORMATIONAL}.
	 * This is a shortcut for checking the value of {@link #series()}.
	 * @see #series()
	 */
	public boolean is1xxInformational() {
		return (series() == Series.INFORMATIONAL);
	}

	/**
	 * Whether this status code is in the HTTP series
	 * {@link org.springframework.http.DmCode.Series#SUCCESSFUL}.
	 * This is a shortcut for checking the value of {@link #series()}.
	 * @see #series()
	 */
	public boolean is2xxSuccessful() {
		return (series() == Series.SUCCESSFUL);
	}

	/**
	 * Whether this status code is in the HTTP series
	 * {@link org.springframework.http.DmCode.Series#REDIRECTION}.
	 * This is a shortcut for checking the value of {@link #series()}.
	 * @see #series()
	 */
	public boolean is3xxRedirection() {
		return (series() == Series.REDIRECTION);
	}

	/**
	 * Whether this status code is in the HTTP series
	 * {@link org.springframework.http.DmCode.Series#CLIENT_ERROR}.
	 * This is a shortcut for checking the value of {@link #series()}.
	 * @see #series()
	 */
	public boolean is4xxClientError() {
		return (series() == Series.CLIENT_ERROR);
	}

	/**
	 * Whether this status code is in the HTTP series
	 * {@link org.springframework.http.DmCode.Series#SERVER_ERROR}.
	 * This is a shortcut for checking the value of {@link #series()}.
	 * @see #series()
	 */
	public boolean is5xxServerError() {
		return (series() == Series.SERVER_ERROR);
	}

	/**
	 * Whether this status code is in the HTTP series
	 * {@link org.springframework.http.DmCode.Series#CLIENT_ERROR} or
	 * {@link org.springframework.http.DmCode.Series#SERVER_ERROR}.
	 * This is a shortcut for checking the value of {@link #series()}.
	 * @since 5.0
	 * @see #is4xxClientError()
	 * @see #is5xxServerError()
	 */
	public boolean isError() {
		return (is4xxClientError() || is5xxServerError());
	}
	
	/**
	 * Return a string representation of this status code.
	 */
	@Override
	public String toString() {
		return this.value + " " + name();
	}
	
	/**
	 * Return the enum constant of this type with the specified numeric value.
	 * @param statusCode the numeric value of the enum to be returned
	 * @return the enum constant with the specified numeric value
	 * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
	 */
	public static DmCode valueOf(int statusCode) {
		DmCode status = resolve(statusCode);
		if (status == null) {
			throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
		}
		return status;
	}
	
	/**
	 * Resolve the given status code to an {@code DmCode}, if possible.
	 * @param statusCode the HTTP status code (potentially non-standard)
	 * @return the corresponding {@code DmCode}, or {@code null} if not found
	 * @since 5.0
	 */
	public static DmCode resolve(int statusCode) {
		for (DmCode status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		return null;
	}
	
	
	public enum Series {

		INFORMATIONAL(1),
		SUCCESSFUL(2),
		REDIRECTION(3),
		CLIENT_ERROR(4),
		SERVER_ERROR(5);

		private final int value;

		Series(int value) {
			this.value = value;
		}

		/**
		 * Return the integer value of this status series. Ranges from 1 to 5.
		 */
		public int value() {
			return this.value;
		}

		/**
		 * Return the enum constant of this type with the corresponding series.
		 * @param status a standard HTTP status enum value
		 * @return the enum constant of this type with the corresponding series
		 * @throws IllegalArgumentException if this enum has no corresponding constant
		 */
		public static Series valueOf(DmCode status) {
			return valueOf(status.value);
		}

		/**
		 * Return the enum constant of this type with the corresponding series.
		 * @param statusCode the HTTP status code (potentially non-standard)
		 * @return the enum constant of this type with the corresponding series
		 * @throws IllegalArgumentException if this enum has no corresponding constant
		 */
		public static Series valueOf(int statusCode) {
			Series series = resolve(statusCode);
			if (series == null) {
				throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
			}
			return series;
		}

		/**
		 * Resolve the given status code to an {@code DmCode.Series}, if possible.
		 * @param statusCode the HTTP status code (potentially non-standard)
		 * @return the corresponding {@code Series}, or {@code null} if not found
		 * @since 5.1.3
		 */
		public static Series resolve(int statusCode) {
			int seriesCode = statusCode / 100;
			for (Series series : values()) {
				if (series.value == seriesCode) {
					return series;
				}
			}
			return null;
		}
	}
}
