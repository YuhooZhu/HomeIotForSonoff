package cn.mingzhu.iot.app.common.iot.deviceJob;

import javax.persistence.Transient;

import cn.mingzhu.iot.app.bas.entity.DeviceJob;
import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;

@Data
public class DeviceWork extends DeviceJob {

	private static final long serialVersionUID = 1L;

	@Transient
	private String seq;

	@Transient
	private String time;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

}
