package cn.mingzhu.iot.app.bas.system;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @文件名称：SysMenu.java
 * @创建时间：2019-06-08 21:32:10
 * @创  建  人：zyh 
 * @文件描述：sys_menu 实体类
 * @文件版本：V0.01 
 */ 


@Data
@Entity
@NoArgsConstructor
@Table(name = "sys_menu")
public class SysMenu implements Serializable{

	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "JDBC") // MySQL自增主键
	private Integer id;

	/**
	* 功能名称
	*/
	@Column(name = "menuname")
	private String menuname;

	/**
	* 上级菜单
	*/
	@Column(name = "pid")
	private Integer pid;

	/**
	* 点击连接
	*/
	@Column(name = "url")
	private String url;

	/**
	* 标识
	*/
	@Column(name = "code")
	private String code;

	/**
	* 类型1类，2方法
	*/
	@Column(name = "type")
	private Integer type;

	/**
	* 状态1正常，0不正常
	*/
	@Column(name = "state")
	private Integer state;

	/**
	* 图标
	*/
	@Column(name = "icon")
	private String icon;

	/**
	* 排序
	*/
	@Column(name = "serialno")
	private Integer serialno;

	/**
	* 是否带数据权限
	*/
	@Column(name = "ispower")
	private Integer ispower;

	/**
	* 是否开启实体权限
	*/
	@Column(name = "isentitypower")
	private Integer isentitypower;

	/**
	* 默认权限标识
	*/
	@Column(name = "default_code")
	private String defaultCode;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}
}

