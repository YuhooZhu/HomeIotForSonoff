package cn.mingzhu.iot.app.bas.entity;
import java.io.Serializable;
import javax.persistence.Table;

import cn.mingzhu.iot.app.util.JsonUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @文件名称：RoomDetail.java
 * @创建时间：2020-06-05 09:44:59
 * @创  建  人：zyh 
 * @文件描述：room_detail 实体类
 * @文件版本：V0.01 
 */ 

@Data
@Entity
@NoArgsConstructor
@Table(name = "room_detail")
@Accessors(chain = true)
public class RoomDetail implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC") // MySQL自增主键
    private Integer id;

	/**	* 房间号	*/
	@Column(name = "pid")
	private Integer pid;

	/**	* 设备hao	*/
	@Column(name = "device_id")
	private Integer deviceId;

    @Override
    public String toString() {
        return JsonUtil.toString(this);
    }
}

