package cn.mingzhu.iot.app.common.iot.deviceQuartz;

import javax.persistence.Transient;

import cn.mingzhu.iot.app.bas.entity.DeviceJob;
import cn.mingzhu.iot.app.bas.entity.DeviceQuartz;
import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeviceQuartzRow extends DeviceQuartz {

	private static final long serialVersionUID = 1L;

	 /**
	 * 是否有已审核的明细记录(版本)
	 */
	 @Transient
	 private DeviceJob deviceJob;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

}