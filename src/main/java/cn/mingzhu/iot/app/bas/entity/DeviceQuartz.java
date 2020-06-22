package cn.mingzhu.iot.app.bas.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@NoArgsConstructor
@Table(name = "iot_device_quartz")
@Accessors(chain = true)
public class DeviceQuartz implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC") // MySQL自增主键
    private Integer id;

	/**
	* 设备ID
	*/
	@Column(name = "device_id")
	private Integer deviceId;

	/**
	* 
	*/
	@Column(name = "job_id")
	private Integer jobId;

	/**
	* 分组
	*/
	@Column(name = "team")
	private String group;

	/**
	* 状态
	*/
	@Column(name = "state")
	private String state;

	/**
	* 备注
	*/
	@Column(name = "remark")
	private String remark;

    @Override
    public String toString() {
        return JsonUtil.toString(this);
    }
}

