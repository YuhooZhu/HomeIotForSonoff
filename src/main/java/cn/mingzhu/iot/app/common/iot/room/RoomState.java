package cn.mingzhu.iot.app.common.iot.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RoomState {
	// 状态：DRAFT-草稿，CONFIRMED-确认（待审批），APPROVED-已审批，DONE-已完成
	
	ON(0, "on", "gray", true),
	OFF(1, "off", "yellow", false);

	private Integer index;
	private String name;
	private String color;
	private Boolean deletable;

	RoomState(Integer index, String name, String color, Boolean deletable) {
		this.index = index;
		this.name = name;
		this.color = color;
		this.deletable = deletable;
	}
	
	public static RoomState getByIndex(Integer index) {
		for (RoomState c : RoomState.values()) {
			if (c.getIndex() == index) {
				return c;
			}
		}
		return null;
	}

	public static String getName(Integer index) {
		for (RoomState c : RoomState.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		}
		return null;
	}

	public static int getIndex(String name) {
		for (RoomState c : RoomState.values()) {
			if (c.getName() == name) {
				return c.index;
			}
		}
		return -1;
	}

	/**
	 * 所有枚举类索引值
	 * @return
	 */
	public static List<Integer> getIndexes() {
		List<Integer> rslt = new ArrayList<Integer>();
		for (RoomState c : RoomState.values()) {
			rslt.add(c.getIndex());
		}
		return rslt;
	}
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
	}

	public static List<Map<String, Object>> getValues() {
		List<Map<String, Object>> rslt = new ArrayList<Map<String, Object>>();
		for (RoomState c : RoomState.values()) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("code", c.toString());
			item.put("id", c.getIndex());
			item.put("name", c.getName());
			item.put("color", c.getColor());
			item.put("deletable", c.getDeletable());
			rslt.add(item);
		}
		return rslt;
	}

}
