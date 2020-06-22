package mapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 从数据库表反射出实体类，自动生成实体类
 *
 * 
 *
 */
public class ServiceHelper {
    //基本数据配置
    private String authorName = "zyh";// 作者名字
    private String version = "V0.01"; // 版本
    private String defaultPath = "/src/main/java/";

    /*
     * 构造函数
     */
    public ServiceHelper(String packageOutPath,String mappername,String parentname) {
        String content = parse(packageOutPath,mappername,parentname);

        try {
            File directory = new File("");
            String path = this.getClass().getResource("").getPath();

            System.out.println(path);

            String outputPath = directory.getAbsolutePath() + this.defaultPath
                    + packageOutPath.replace(".", "/") + "/" + initcap(mappername) + "Service.java";
            System.out.println("执行完毕，生成路径为："+outputPath);
            FileWriter fw = new FileWriter(outputPath);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：生成实体类主体代码
     *
     * @param colnames
     * @param colTypes
     * @param colSizes
     * @return
     */
    private String parse(String packageOutPath,String mappername,String parentname) {
        StringBuffer sb = new StringBuffer();
        // 生成package包路径
        sb.append("package " + packageOutPath + ";\r\n\n");
        // 判断是否导入工具包
        
        sb.append("import java.util.List;\r\n");
        sb.append("import org.springframework.stereotype.Service;\r\n");
        sb.append("import cn.dm89.app.db.spring.boot.autoconfigure.service.BaseService;\r\n");
        sb.append("import lombok.extern.slf4j.Slf4j;\r\n");
        sb.append("import tk.mybatis.mapper.entity.Example;\r\n");
        sb.append("import tk.mybatis.mapper.entity.Example.Criteria;\r\n");

        sb.append("\r\n");
        // 注释部分
        sb.append("   /**\r\n");
        sb.append("    * @文件名称：" + initcap(mappername) + "Service.java\r\n");
        sb.append("    * @创建时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\r\n");
        sb.append("    * @创  建  人：" + this.authorName + " \r\n");
        sb.append("    * @文件描述：" + initcap(mappername) + " Service\r\n");
        sb.append("    * @文件版本：" + this.version + " \r\n");
        sb.append("    */ \r\n");
        sb.append("\n@Slf4j");
        sb.append("\n@Service");

        // 实体部分
        sb.append("\npublic class " + initcap(mappername)+"Service extends BaseService<" + initcap(mappername)+">{\r\n\n");
	        /**
	    	 * 根据ID获取详细信息，不包括子表数据列表
	    	 * @param id
	    	 * @return
	    	 */
        	sb.append("\n\tpublic " + initcap(mappername)+" findById(Integer id) {\r");
        		sb.append("\n\t\t" + initcap(mappername)+" srch = new " + initcap(mappername)+"();\r");
        			sb.append("\n\t\tsrch.setId(id);\r\n");
        				sb.append("\n\t\t" + initcap(mappername)+" rslt = super.selectByPrimaryKey(srch);\r\n");
        					sb.append("\n\t\tif (rslt == null) {\r\t\t\treturn null;\n\t\t}\r\n\t\treturn rslt;\n");
        	sb.append("\t}\r\n");
        	
        	/**
        	 * 根据父ID获取信息
        	 * @param id
        	 * @return
        	 */
        	sb.append("\n\tpublic List<" + initcap(mappername)+"> selectBy" + initcap(parentname)+"Id(Integer " + parentname+"id) {\r");
    		sb.append("\n\t\tExample example = new Example(" + initcap(mappername)+".class);\r");
    			sb.append("\n\t\tCriteria criteria = example.createCriteria();\r\n");
    				sb.append("\n\t\tcriteria.andEqualTo(\"" + parentname+"id\""+", " + parentname+"id);\r\n");
        				sb.append("\n\t\treturn super.selectByExample(example);\r\n");
    		sb.append("\t}\r\n");
        	
    		/**
        	 * 根据关联字段删除子表所有关联记录
        	 * @param 
        	 * @return
        	 */
        	sb.append("\n\tpublic int deleteBy" + initcap(parentname)+"Id(Integer " + parentname+"id) {\r");
    			sb.append("\n\t\tExample example = new Example(" + initcap(mappername)+".class);\r");
    				sb.append("\n\t\tCriteria criteria = example.createCriteria();\r\n");
    					sb.append("\n\t\tcriteria.andEqualTo(\"" + parentname+"id\""+", " + parentname+"id);\r\n");
        					sb.append("\n\t\treturn super.deleteByExample(example);\r\n");
    		sb.append("\t}\r\n");
        	
        sb.append("}\r\n");

        return sb.toString();
    }
    
    /**
     * 功能：将输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    private String initcap(String str) {

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }
}
