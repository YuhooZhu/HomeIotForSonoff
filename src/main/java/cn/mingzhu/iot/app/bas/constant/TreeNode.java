package cn.mingzhu.iot.app.bas.constant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TreeNode {

	private Integer id;
	
	private Integer pid;
	
	/**
	 * 树节点值
	 */
	private String key;
	
	/**
	 * 树节点显示名称
	 */
	private String name;

	/**
	 * 树节点显示名称
	 */
	private String title;
	
	/**
	 * 树节点是否选中
	 */
	private boolean checked;
	
	private String icon;

	private String iconSkin;
	
	private boolean open;

}
