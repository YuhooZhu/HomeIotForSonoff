package cn.mingzhu.iot.app.common.iot.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/view/device")
@Slf4j
public class IotDeviceViewController{

	@Autowired
	private IotDeviceService service;

	@GetMapping("/list")
	public String list(ModelMap model,
			@RequestParam(value = "type", required = false, defaultValue = "list") String type) {

		model.addAttribute("type", type);

		return "iot/device/list";
	}

//	@GetMapping("/edit")
//	public String edit(ModelMap model, HttpServletRequest request,
//			@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
//
//
//		FormEx form = formSvc.findExById(getSiteId(), FormIndex.WRK_PROJECT.getIndex());
//		if (form != null) {
//			model.addAttribute("udf1", form.getFieldEx("udf1"));
//			model.addAttribute("udf2", form.getFieldEx("udf2"));
//			model.addAttribute("udf3", form.getFieldEx("udf3"));
//			model.addAttribute("udf4", form.getFieldEx("udf4"));
//			model.addAttribute("udf5", form.getFieldEx("udf5"));
//			model.addAttribute("udf6", form.getFieldEx("udf6"));
//		}
//		
//		
//		if (id > 0) {
//			model.addAttribute("title", "修改");
//
//			PrjEx record = service.findExById(getSiteId(), id);
//			log.info("record={}", record);
//			model.addAttribute("record", record);
//		} else {
//			model.addAttribute("title", "新建");
//		}
//		return "trc/prj/edit";
//	}
}
