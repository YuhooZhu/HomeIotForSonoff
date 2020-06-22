package cn.mingzhu.iot.app.common.iot.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.mingzhu.iot.app.bas.entity.IotDevice;
import cn.mingzhu.iot.app.bas.exception.ApiIllegalArgumentException;

/**
 **/
@Component("deviceTask")
public class DeviceTask {

	@Autowired
	private IotDeviceService service;

	public IotDevice switchOn(Integer id) {

		IotDevice iotDevice = service.updateStateToOn(id);

		if (iotDevice == null) {
			throw new ApiIllegalArgumentException("定时任务错误");
		}
		return iotDevice;
	}

	public IotDevice switchOff(Integer id) {

		IotDevice iotDevice = service.updateStateToOff(id);

		if (iotDevice == null) {
			throw new ApiIllegalArgumentException("定时任务错误");
		}
		return iotDevice;
	}
}
