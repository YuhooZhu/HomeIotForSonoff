package cn.mingzhu.iot.app.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cn.mingzhu.iot.app.bas.ResponseData;
import cn.mingzhu.iot.app.bas.constant.DmCode;
import cn.mingzhu.iot.app.bas.system.MenuEx;
import cn.mingzhu.iot.app.bas.system.MenuService;
import cn.mingzhu.iot.app.common.iot.device.IotDeviceEx;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoadingController {

	@Autowired
	private MenuService menuSvc;

	/**
	 * 页面入口：1)加载页面框架视图 2)加载用户菜单
	 * 
	 * @return
	 */
	@GetMapping("/")
	public String loading(Authentication auth, HttpServletRequest request, ModelMap model) {
		log.info("");
		log.info(".......... Loading ..........");
		log.info("REQUEST SESSION={}",  request.getRequestedSessionId());
		log.info("REQUEST auth={}",  auth);

		if (request.getParameter("from") != null) {
			return "redirect:./";
		}

		List<MenuEx> menus = menuSvc.listAllTreeNodes();
		model.addAttribute("menus", menus);
		
		return "loading";
	}
	@GetMapping("/api/menu")
	public ResponseData<List<MenuEx>> get(HttpServletRequest request) {

		List<MenuEx> menus = menuSvc.listAllTreeNodes();
		
		return new ResponseData<List<MenuEx>>(DmCode.OK, menus);
	}
}
