package cn.mingzhu.iot.app.common.iot.room.detail;

import javax.persistence.Transient;

import cn.mingzhu.iot.app.bas.entity.IotDevice;
import cn.mingzhu.iot.app.bas.entity.RoomDetail;
import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomDetailRow extends RoomDetail {

	private static final long serialVersionUID = 1L;

	 @Transient
	 private IotDevice device;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

}