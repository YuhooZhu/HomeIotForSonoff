package cn.mingzhu.iot.app.common.iot.deviceInfo;

import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IotDeviceInfoEx extends IotDeviceInfoRow {

    private static final long serialVersionUID = 2L;

//    @Transient
//    @Valid
//    private List<BomDetailEx> items;
//    
    
    @Override
	public String toString() {
		return JsonUtil.toString(this);
	}
}