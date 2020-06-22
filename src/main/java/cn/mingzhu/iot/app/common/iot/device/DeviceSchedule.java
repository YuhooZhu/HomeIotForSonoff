package cn.mingzhu.iot.app.common.iot.device;

import javax.persistence.Entity;

import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;

@Data
@Entity
public class DeviceSchedule {

	private Integer id; // 设备ID

	private Integer time; // 时间

	private String methodName; // 方法名

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

}