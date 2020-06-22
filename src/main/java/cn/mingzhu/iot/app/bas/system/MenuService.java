package cn.mingzhu.iot.app.bas.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import cn.mingzhu.iot.app.bas.BaseService;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class MenuService extends BaseService<SysMenu> {
	/**
	 * 子表持久类对象转换为视图扩展类对象
	 * 
	 * @param source
	 * @return
	 */
	private MenuEx toEx(SysMenu source) {
		log.info("source={}", source);

		MenuEx rslt = new MenuEx();
		BeanUtils.copyProperties(source, rslt);

		List<MenuEx> sysMenus = listAllChildrenByParentId(source.getId());
		if (sysMenus != null) {
			rslt.setItems(sysMenus);
		}
		log.info("rslt={}", rslt);
		return rslt;

	}

	/**
	 * 查询某个项目的树结构列表
	 * 
	 * @param siteId
	 * @param parentId
	 * @return
	 */
	public List<MenuEx> listAllTreeNodes() {
		List<MenuEx> rslt = null;

		Example example = new Example(SysMenu.class);

		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("pid", 0);
		example.and(criteria);
		List<SysMenu> items = selectByExample(example);

		if (items != null) {
			rslt = new ArrayList<MenuEx>();
			for (SysMenu item : items) {
				rslt.add(toEx(item));
			}
		}
		return rslt;
	}

	/**
	 * 递归查询父ID下属的中间节点及叶子节点列表
	 * 
	 * @param siteId
	 * @param parentId
	 * @return
	 */
	private List<MenuEx> listAllChildrenByParentId(Integer parentId) {

		Example example = new Example(SysMenu.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("pid", parentId);

		List<SysMenu> found = super.selectByExample(example);
		if (found == null || found.size() < 1) {
			return null; // 1) 递归空值返回
		}

		List<MenuEx> rslt = new ArrayList<MenuEx>();
		for (SysMenu item : found) {
			MenuEx subItem = toEx(item); // 3) 递归自调用

			log.info("listTreeChildren -> item={}", item.getId());
			rslt.add(subItem);
		}

		return rslt; // 2) 递归非空值返回
	}
}
