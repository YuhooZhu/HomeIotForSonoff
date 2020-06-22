package cn.mingzhu.iot.app.bas;

import java.io.Serializable;
import java.util.Map;

import cn.mingzhu.iot.app.bas.constant.DmCode;
import cn.mingzhu.iot.app.util.JsonUtil;

public class ResponseData<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 返回码
     */
    private int code;

    /**
     * 返回描述
     */
    private String msg;

    private T data;

    public ResponseData() {
    }

    public ResponseData(DmCode code) {
        this.code = code.value();
        this.msg = code.getReasonPhrase();
    }
    
    public ResponseData(DmCode code, T data) {
        this.code = code.value();
        this.msg = code.getReasonPhrase();
        this.data = data;
    }

    public ResponseData(DmCode code, String msg, T data) {
        this.code = code.value();
        this.msg = msg;
        this.data = data;
    }
    
    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    public static <T> ResponseData<T> success() {
        return new ResponseData<T>(DmCode.OK);
    }

    public static <T> ResponseData<T> success(DmCode code, T data) {
        return new ResponseData<T>(code, data);
    }

    public static <T> ResponseData<T> success(DmCode code, String msg, T data) {
        return new ResponseData<T>(code, msg, data);
    }
    
    public static ResponseData<Map<String, String>> failed(String msg, Map<String, String> data) {
        return new ResponseData<Map<String, String>>(DmCode.BAD_REQUEST, msg, data);
    }
    
    public String toJsonString() {
    	return JsonUtil.toString(this);
    }
}
