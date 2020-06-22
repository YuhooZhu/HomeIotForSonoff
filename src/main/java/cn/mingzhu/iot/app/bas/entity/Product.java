package cn.mingzhu.iot.app.bas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.mingzhu.iot.app.bas.BaseEntity;
import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @文件名称：TbProduct.java
 * @创建时间：2019-09-27 10:03:21
 * @创  建  人：zyh 
 * @文件描述：tb_product 实体类
 * @文件版本：V0.01 
 */ 

@Data
@Entity
@NoArgsConstructor
@Table(name = "product")
@Accessors(chain = true)
public class Product implements Serializable {

	private static final long serialVersionUID = 5824349669141075967L;

	@Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC") // MySQL自增主键
    private Integer id;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * 类型
	 */
	@Column(name = "cate")
	private Integer cate;
	
	/**
	 * 备注
	 */
	@Column(name = "memo")
	private String memo;
	
	
	
    @Override
    public String toString() {
        return JsonUtil.toString(this);
    }
}


