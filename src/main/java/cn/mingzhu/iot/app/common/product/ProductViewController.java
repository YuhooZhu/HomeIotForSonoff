package cn.mingzhu.iot.app.common.product;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/view/product")
@Slf4j
public class ProductViewController {

	@GetMapping("/list")
	public String list(ModelMap model,
			@RequestParam(value = "type", required = false, defaultValue = "list") String type) {

		model.addAttribute("type", type);

		return "iot/product/list";
	}

	@Autowired
	private ProductService productService;

	@GetMapping("/edit")
	public String edit(ModelMap model, HttpServletRequest request,
			@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
		if (id > 0) {
			model.addAttribute("title", "修改");

			ProductEx record = productService.findExById(id);
			log.info("record={}", record);
			model.addAttribute("record", record);
		} else {
			model.addAttribute("title", "新建");
		}
		return "iot/product/edit";
	}

	@GetMapping(value = "/qrcode", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] generateQRCode() throws FileNotFoundException, IOException {
	        File file = new File("E:/biao.jpg");
	        FileInputStream inputStream = new FileInputStream(file);
	        byte[] bytes = new byte[inputStream.available()];
	        inputStream.read(bytes, 0, inputStream.available());
	        return bytes;
	}
}
