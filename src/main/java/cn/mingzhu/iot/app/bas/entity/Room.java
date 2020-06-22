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
 * @文件名称：Room.java
 * @创建时间：2020-06-05 09:44:59
 * @创  建  人：zyh 
 * @文件描述：room 实体类
 * @文件版本：V0.01 
 */ 

@Data
@Entity
@NoArgsConstructor
@Table(name = "room")
@Accessors(chain = true)
public class Room implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC") // MySQL自增主键
    private Integer id;

	/**	* 房间编号	*/
	@Column(name = "code")
	private String code;

	/**	* 房间名称	*/
	@Column(name = "name")
	private String name;

	/**	* 状态	*/
	@Column(name = "state")
	private String state;

	/**	* 备注	*/
	@Column(name = "remark")
	private String remark;

    @Override
    public String toString() {
        return JsonUtil.toString(this);
    }
}

