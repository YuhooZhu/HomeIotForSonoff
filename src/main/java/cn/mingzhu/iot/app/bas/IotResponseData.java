package cn.mingzhu.iot.app.bas;

import java.io.Serializable;
import java.util.Map;

import cn.mingzhu.iot.app.bas.constant.ZyhCode;
import cn.mingzhu.iot.app.util.JsonUtil;

public class IotResponseData<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 返回码
     */
    private int seq;
    
    /**
     * 错误码
     */
    private int error;
    
    private T data;

    public IotResponseData() {
    }

    public IotResponseData(ZyhCode code) {
        this.error = code.value();
    }
    
    public IotResponseData(ZyhCode code, T data) {
        this.error = code.value();
        this.data = data;
    }

    public IotResponseData(ZyhCode code, String msg, T data) {
        this.error = code.value();
        this.data = data;
    }
    
    public IotResponseData(int seq, int error) {
        this.seq = seq;
        this.error = error;
    }

    public IotResponseData(int code, String msg, T data) {
        this.error = code;
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setErrore(int error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    public static <T> IotResponseData<T> success() {
        return new IotResponseData<T>(ZyhCode.OK);
    }

    public static <T> IotResponseData<T> success(ZyhCode code, T data) {
        return new IotResponseData<T>(code, data);
    }

    public static <T> IotResponseData<T> success(ZyhCode code, String msg, T data) {
        return new IotResponseData<T>(code, msg, data);
    }
    
    public static IotResponseData<Map<String, String>> failed(String msg, Map<String, String> data) {
        return new IotResponseData<Map<String, String>>(ZyhCode.BAD_REQUEST, msg, data);
    }
    
    public String toJsonString() {
    	return JsonUtil.toString(this);
    }
}
