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
@Table(name = "iot_device_job")
@Accessors(chain = true)
public class DeviceJob implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC") // MySQL自增主键
    private Integer id;

	@Column(name = "name")
	private String name;

	/**
	* 
	*/
	@Column(name = "job")
	private String job;

	/**
	* 
	*/
	@Column(name = "cron")
	private String cron;
	
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

