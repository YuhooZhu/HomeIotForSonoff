package cn.mingzhu.iot.app.common.iot.room;

import java.util.List;

import javax.persistence.Transient;

import cn.mingzhu.iot.app.common.iot.room.detail.RoomDetailRow;
import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomEx extends RoomRow {

    private static final long serialVersionUID = 2L;

    @Transient
    private List<RoomDetailRow> items;
    
    @Override
	public String toString() {
		return JsonUtil.toString(this);
	}
}