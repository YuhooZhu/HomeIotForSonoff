package cn.mingzhu.iot.app.bas.system;

import java.util.List;

import javax.persistence.Transient;

import cn.mingzhu.iot.app.util.JsonUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuEx extends SysMenu {

	
	/**
	 * 模块
	 * @deprecated
	 */
	private String module;

	/**
	 * @deprecated
	 */
	public void setModule(String module) {
		
	}

	/**
	 * @deprecated
	 */
	public String getModule() {
		String rslt = super.getUrl();
		if (rslt != null && rslt.indexOf("/") > 0) {
			rslt = rslt.substring(0, rslt.indexOf("/"));
		}
		return rslt;
	}
	
    /**
     * 子菜单
     */
    @Transient
    private List<MenuEx> items;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}
}
