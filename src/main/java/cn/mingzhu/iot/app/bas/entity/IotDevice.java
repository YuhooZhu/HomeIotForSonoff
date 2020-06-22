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

/**
 * @文件名称：IotDevice.java
 * @创建时间：2020-05-26 09:34:16
 * @创  建  人：zyh 
 * @文件描述：iot_device 实体类
 * @文件版本：V0.01 
 */ 

@Data
@Entity
@NoArgsConstructor
@Table(name = "iot_device")
@Accessors(chain = true)
public class IotDevice implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC") // MySQL自增主键
    private Integer id;

	/**	* 设备编号	*/
	@Column(name = "code")
	private String code;

	/**	* 	*/
	@Column(name = "name")
	private String name;

	/**	* ip地址+端口	*/
	@Column(name = "ip")
	private String ip;

	/**	* 详细地址	*/
	@Column(name = "address")
	private String address;

	/**	* 状态	*/
	@Column(name = "state")
	private String state;

	/**	* 1:MINI 2:D1	*/
	@Column(name = "type")
	private Integer type;

	/**	* 备注	*/
	@Column(name = "remark")
	private String remark;

    @Override
    public String toString() {
        return JsonUtil.toString(this);
    }
}

