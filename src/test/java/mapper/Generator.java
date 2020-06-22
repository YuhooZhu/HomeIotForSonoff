package mapper;
//导入所需的包
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Generator {

    // 数据库连接
	public static final String DB = "jjjy";
	public static final String URL = "jdbc:mysql://192.168.1.10:3306/" + DB + "?serverTimezone=GMT%2B8";
	public static final String NAME = "root";
	public static final String PASS = "pass";
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";

	
	private static String tablename = "dev_collar";// 表名
    private static String classname = "news";// 类名
    
    private static String packageOutPath = "mapper." + classname +"";// 指定实体生成所在包的路径
    private final static String packageOutDir = "E:\\mingzhu\\iot\\src\\test\\java";// 指定实体生成所在包的路径
    public static String entityPkg = "";
    public static String mapperPkg = "";

	public static void main(String[] args) {
		
		String targetDir = packageOutDir;
		
		SimpleDateFormat format = new SimpleDateFormat( "yyyyMMddHHmm" );
		Long time=new Long(Calendar.getInstance().getTimeInMillis());
		String d = format.format(time);
		
		entityPkg = "entity" + d;
		mapperPkg = "mapper" + d;

		File file = new File(targetDir);
		if (!(file.isDirectory() && file.exists())) {
			file.mkdir();
		}
		
        try {
            Class.forName(Generator.DRIVER);
        } catch (ClassNotFoundException ex) {
            log.error("Driver exception:", ex);
        }
        
        // 创建连接
        Connection entitycon = null;
        try {
        	entitycon = DriverManager.getConnection(Generator.URL, Generator.NAME, Generator.PASS);
		} catch (SQLException ex) {
            log.error("Connection exception:", ex);
		}

		new EntityHelper(targetDir, entitycon);

		Connection mappercon = null;
        try {
        	mappercon = DriverManager.getConnection(Generator.URL, Generator.NAME, Generator.PASS);
		} catch (SQLException ex) {
            log.error("Connection exception:", ex);
		}
	        
		new MapperHelper(targetDir, mappercon);
    }
}
