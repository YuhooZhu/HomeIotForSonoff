package cn.mingzhu.iot.app.common.iot.deviceInfo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @文件名称：IotDeviceInfo.java
 * @创建时间：2020-05-26 09:34:16
 * @创  建  人：zyh 
 * @文件描述：iot_device_info 实体类
 * @文件版本：V0.01 
 */ 

@Data
@Entity
@NoArgsConstructor
@Table(name = "iot_device_info")
@Accessors(chain = true)
public class IotDeviceInfo implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC") // MySQL自增主键
    private Integer id;

	/**	* 	*/
	@Column(name = "pid")
	private Integer pid;

	/**	* 开关
	on: turn the switch on, off: turn the switch off
	*/
	@Column(name = "switching")
	private String switching;

	/**	* 开机时状态
on: the device is on when power supply is recovered.
off: the device is off when power supply is recovered.
stay: the device status keeps as the same as the state before power supply is gone	*/
	@Column(name = "startup")
	private String startup;

	/**	* 点动
on: activate the inching function;
off: disable the inching function	*/
	@Column(name = "pulse")
	private String pulse;

	/**	* 点动范围
Required when “pulse” is on, pulse time length, positive integer, ms, only supports multiples of 500 in range of 500~36000000
	*/
	@Column(name = "pulseWidth")
	private Integer pulsewidth;

	/**	* SSID of the WiFi network to which the device will connect	*/
	@Column(name = "ssid")
	private Integer ssid;

	/**	* ota锁定	*/
	@Column(name = "otaUnlock")
	private String otaunlock;

	/**	* 固件版本	*/
	@Column(name = "fwVersion")
	private String fwversion;

	/**	* 设备ID	*/
	@Column(name = "deviceid")
	private Integer deviceid;

	/**	* MAC地址	*/
	@Column(name = "bssid")
	private Integer bssid;

	/**	* 信号强度	*/
	@Column(name = "signalStrength")
	private String signalstrength;

	/**	* 更新时间	*/
	@Column(name = "updated_time")
	private Date updatedTime;

    @Override
    public String toString() {
        return JsonUtil.toString(this);
    }
}

