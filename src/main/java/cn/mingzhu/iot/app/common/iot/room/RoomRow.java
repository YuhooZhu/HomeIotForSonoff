package cn.mingzhu.iot.app.common.iot.room;

import cn.mingzhu.iot.app.bas.entity.Room;
import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomRow extends Room {

	private static final long serialVersionUID = 1L;

	// @Transient
	// private Boolean isStateActived;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

}