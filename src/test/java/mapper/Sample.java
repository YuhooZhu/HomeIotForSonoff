package mapper;

import java.io.IOException;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.util.Util;

public class Sample {
	// 设置APPID/AK/SK
	public static final String APP_ID = "20414205";
	public static final String API_KEY = "vFX2G5G3bdeQxIzFQELKaynq";
	public static final String SECRET_KEY = "DzSIu8bA3u2BMEU67eXrDIZ5TlfQkmNf";

	public static void main(String[] args) throws IOException {
		// 初始化一个AipSpeech
		AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

		String path = "E:\\mingzhu\\iot\\16k.pcm";
//		String pcmfilepath = "E:\\mingzhu\\iot\\16k.pcm";
//
//		AudioUtils.convertMP3ToPcm(path, pcmfilepath);
		
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

//		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//		client.setHttpProxy("proxy_host", proxy_port); // 设置http代理
//		client.setSocketProxy("proxy_host", proxy_port); // 设置socket代理

		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
//		System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
		// 调用接口
		byte[] data = Util.readFileByBytes(path);     //readFileByBytes仅为获取二进制数据示例
	    JSONObject asrRes2 = client.asr(data, "pcm", 16000, null);
	    System.out.println(asrRes2);
	    
//		JSONObject res = client.asr(path, "pcm", 16000, null);
//		System.out.println(res.toString(2));

	}
}
