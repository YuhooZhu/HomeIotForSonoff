package cn.mingzhu.iot.app.config;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import cn.mingzhu.iot.app.bas.IotRequestData;
import cn.mingzhu.iot.app.bas.Response;
import cn.mingzhu.iot.app.bas.entity.IotDevice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HttpClient {
	public Response post(String type, HashMap<String, String> map, IotDevice iotDevice) {
		RestTemplate restTemplate = RestTemplatePoolConfig.getRestTemplate();

		IotRequestData<Object> iotRequestData = new IotRequestData<Object>();
		iotRequestData.setDeviceid("");
		iotRequestData.setData(JSONObject.toJSON(map));

		String uri = "http://" + iotDevice.getIp() + "/zeroconf/" + type;
//		String uri = "http://192.168.1.10:8088/api/iotDevice/40";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<Object> entity = new HttpEntity<>(JSONObject.toJSON(iotRequestData), headers);

//		String strbody = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class).getBody();
		ResponseEntity<String> strbody = restTemplate.postForEntity(uri, entity, String.class);
		String body = strbody.getBody();
		Response response = JSONObject.parseObject(body, Response.class);

		return response;
	}
	
	public Response putSelf(String type) {
		RestTemplate restTemplate = new RestTemplate();

//		IotRequestData<Object> iotRequestData = new IotRequestData<Object>();
//		iotRequestData.setDeviceid("");
//		iotRequestData.setData(JSONObject.toJSON(map));

//		String uri = "http://" + iotDevice.getIp() + "/zeroconf/" + type;
		String uri = "http://192.168.1.10:8088/api/iotDevice/40/state/" + type;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<Object> entity = new HttpEntity<>(headers);

		String strbody = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class).getBody();

		Response response = JSONObject.parseObject(strbody, Response.class);

		return response;
	}
}
