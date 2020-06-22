package cn.mingzhu.iot.app.bas;

import java.io.Serializable;
import java.util.Map;

import cn.mingzhu.iot.app.bas.constant.DmCode;
import cn.mingzhu.iot.app.util.JsonUtil;

public class IotRequestData<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String deviceid;
    
    private T data;

    public IotRequestData() {
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    public String toJsonString() {
    	return JsonUtil.toString(this);
    }
}
