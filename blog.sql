-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.26-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 blog 的数据库结构
CREATE DATABASE IF NOT EXISTS `blog` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `blog`;

-- 导出  表 blog.announcement 结构
CREATE TABLE IF NOT EXISTS `announcement` (
  `announcement_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告唯一id',
  `announcement_title` varchar(255) NOT NULL COMMENT '公告标题',
  `announcement_body` text NOT NULL COMMENT '公告内容',
  `announcement_top` int(11) NOT NULL COMMENT '是否置顶0 置顶 1未置顶',
  `announcement_time` datetime NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.announcement 的数据：~2 rows (大约)
DELETE FROM `announcement`;
/*!40000 ALTER TABLE `announcement` DISABLE KEYS */;
INSERT INTO `announcement` (`announcement_id`, `announcement_title`, `announcement_body`, `announcement_top`, `announcement_time`) VALUES
	(1, '行香子', '无也闲愁，有也闲愁，有无见愁的白头。<br />花能助喜，酒解忘忧，对东篱，思北海，忆南楼。', 1, '2021-03-24 14:54:05');
/*!40000 ALTER TABLE `announcement` ENABLE KEYS */;

-- 导出  表 blog.blog 结构
CREATE TABLE IF NOT EXISTS `blog` (
  `blog_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一博文id--主键',
  `blog_title` varchar(255) NOT NULL COMMENT '博文标题',
  `blog_body` text NOT NULL COMMENT '博文内容',
  `blog_discussCount` int(11) NOT NULL COMMENT '博文评论数',
  `blog_blogViews` int(11) NOT NULL COMMENT '博文浏览数',
  `blog_time` datetime NOT NULL COMMENT '博文发布时间',
  `blog_state` int(11) NOT NULL COMMENT '博文状态--0 删除 1正常',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`blog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.blog 的数据：~6 rows (大约)
DELETE FROM `blog`;
/*!40000 ALTER TABLE `blog` DISABLE KEYS */;
INSERT INTO `blog` (`blog_id`, `blog_title`, `blog_body`, `blog_discussCount`, `blog_blogViews`, `blog_time`, `blog_state`, `user_id`) VALUES
	(1, 'JAVA获取客户端IP', 'import javax.servlet.http.HttpServletRequest;\r\nimport com.tbtech.common.utils.StringUtils;\r\n\r\npublic final class RequestHelper {\r\n	/* \r\n	 * 获取访问者IP\r\n	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。\r\n	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，* 如果还不存在则调用Request .getRemoteAddr()。\r\n	 * @param request\r\n	 * @return\r\n	 */\r\n	public static String getIpAddr(HttpServletRequest request) {\r\n	     String ip = request.getHeader("X-Forwarded-For");\r\n             if(!StringUtils.isBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){\r\n                 //多次反向代理后会有多个ip值，第一个ip才是真实ip\r\n                 int index = ip.indexOf(",");\r\n                 if(index != -1){\r\n                     return ip.substring(0,index);\r\n                 }else{\r\n                     return ip;\r\n                 }\r\n             }\r\n             ip = request.getHeader("X-Real-IP");\r\n       \r\n             //StringUtils.isBlank只是一个判断非空字符的方法\r\n             if(!StringUtils.isBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){\r\n                 return ip;\r\n             }\r\n             return request.getRemoteAddr();\r\n	}\r\n\r\n}', 1, 116, '2020-03-14 17:39:41', 1, 2),
	(2, 'Java 获取两个日期之间的所有日期', 'import java.text.DateFormat;\r\nimport java.text.ParseException;\r\nimport java.text.SimpleDateFormat;\r\nimport java.util.ArrayList;\r\nimport java.util.Calendar;\r\nimport java.util.Date;\r\nimport java.util.List;\r\n\r\n/**\r\n * 获取两个日期之间的所有日期\r\n * @param startTime 开始日期\r\n * @param endTime 结束日期\r\n * @return\r\n */\r\npublic static List<String> getDays(String startTime, String endTime) {\r\n\r\n    // 返回的日期集合\r\n    List<String> days = new ArrayList<String>();\r\n\r\n    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");\r\n    try {\r\n        Date start = dateFormat.parse(startTime);\r\n        Date end = dateFormat.parse(endTime);\r\n\r\n        Calendar tempStart = Calendar.getInstance();\r\n        tempStart.setTime(start);\r\n\r\n        Calendar tempEnd = Calendar.getInstance();\r\n        tempEnd.setTime(end);\r\n        tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)\r\n        while (tempStart.before(tempEnd)) {\r\n            days.add(dateFormat.format(tempStart.getTime()));\r\n            tempStart.add(Calendar.DAY_OF_YEAR, 1);\r\n        }\r\n\r\n    } catch (ParseException e) {\r\n        e.printStackTrace();\r\n    }\r\n\r\n    return days;\r\n}', 0, 225, '2020-03-14 17:39:41', 1, 2),
	(3, 'JAVA MAP转实体', 'public static <T> T map2Object(Map<String, Object> map, Class<T> clazz) {\r\n        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");\r\n         \r\n        if (map == null) {\r\n            return null;\r\n        }\r\n        T obj = null;\r\n        try {\r\n            // 使用newInstance来创建对象\r\n            obj = clazz.newInstance();\r\n            // 获取类中的所有字段\r\n            Field[] fields = obj.getClass().getDeclaredFields();\r\n            for (Field field : fields) {\r\n                int mod = field.getModifiers();\r\n                // 判断是拥有某个修饰符\r\n                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {\r\n                    continue;\r\n                }\r\n                // 当字段使用private修饰时，需要加上\r\n                field.setAccessible(true);\r\n                // 获取参数类型名字\r\n                String filedTypeName = field.getType().getName();\r\n                // 判断是否为时间类型，使用equalsIgnoreCase比较字符串，不区分大小写\r\n                // 给obj的属性赋值\r\n                if (filedTypeName.equalsIgnoreCase("java.util.date")) {\r\n                    String datetimestamp = (String) map.get(field.getName());\r\n                    if (datetimestamp.equalsIgnoreCase("null")) {\r\n                        field.set(obj, null);\r\n                    } else {\r\n                        field.set(obj, sdf.parse(datetimestamp));\r\n                    }\r\n                } else {\r\n                    field.set(obj, map.get(field.getName()));\r\n                }\r\n            }\r\n        } catch (Exception e) {\r\n            e.printStackTrace();\r\n        }\r\n        return obj;\r\n    }', 1, 337, '2020-03-14 17:39:41', 1, 2),
	(4, 'vue 获取字符串日期间的差值', '/**\r\n   * 获取当前日期\r\n   * @returns {string}\r\n   * @Example getNowTime(\'-\')\r\n   */\r\n  getNowTime(str){\r\n    let nowDate = new Date();\r\n    let y = nowDate.getFullYear();\r\n    let m = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;\r\n    let d = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();\r\n    if(str){\r\n      return y + str + m + str + d;\r\n    }else{\r\n      return y + \'年\' + m + \'月\' + d + \'日\';\r\n    }\r\n  },\r\n\r\n  /**\r\n   * 获取两个字符串日期间的差值\r\n   * @param startDay\r\n   * @param endDay\r\n   * @Example getDifferentialValForDate(\'2020-01-05\',\'2020-01-10\')\r\n   */\r\n  getDifferentialValForDate(startDay,endDay){\r\n    return Math.abs(parseInt((new Date(endDay).getTime() - new Date(startDay).getTime())/(1000 * 60 * 60 * 24)));\r\n  },\r\n\r\n  /**\r\n   * 获取当前月份\r\n   * @returns {string}\r\n   * @Example getNowMonth(\'-\')\r\n   */\r\n  getNowMonth(str){\r\n    let nowDate = new Date();\r\n    let y = nowDate.getFullYear();\r\n    let m = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;\r\n    if(str){\r\n      return y + str + m;\r\n    }else{\r\n      return y + \'年\' + m + \'月\';\r\n    }\r\n  },\r\n\r\n  /**\r\n   * 获取两个字符串月份间的差值\r\n   * @param startMonth\r\n   * @param endMonth\r\n   * @Example getDifferentialValForMonth(\'2020-04\',\'2020-01\')\r\n   */\r\n  getDifferentialValForMonth(startMonth,endMonth){\r\n    let startMonths = startMonth.split(\'-\');\r\n    let endMonths = endMonth.split(\'-\');\r\n    return Math.abs((parseInt(startMonths[0]) * 12 + parseInt(startMonths[1])) - (parseInt(endMonths[0]) * 12 + parseInt(endMonths[1])));\r\n  },', 0, 335, '2020-03-14 17:39:41', 1, 2),
	(5, 'win10 解决端口被占用', '- 查看端口\n```\nnetstat -aon|findstr "端口"\n```\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20190816093057670.png)\n\n- 通过PID查找应用程序\n```\ntasklist|findstr "PID"\n```\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20190816093159992.png)\n\n- 关闭进程\n```\ntaskkill /f /t /im 进程\n```\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20190816093236347.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTE4MjcyNw==,size_16,color_FFFFFF,t_70)', 0, 3, '2021-03-23 20:03:03', 1, 2),
	(6, 'WebService之Spring+CXF整合示例', '## 一、Spring+CXF整合示例\n```WebService```是一种跨编程语言、跨操作系统平台的远程调用技术，它是指一个应用程序向外界暴露一个能通过```Web```调用的```API```接口，我们把调用这个```WebService```的应用程序称作客户端，把提供这个```WebService```的应用程序称作服务端。\n\n#### 环境\n```win10+Spring5.1+cxf3.3.2```\n\n#### 下载\n- 官网下载：[https://archive.apache.org/dist/cxf/](https://archive.apache.org/dist/cxf/)\n- 百度网盘：\n链接：[https://pan.baidu.com/s/1nsUweTFG_6CcZKaVBCQ7uQ](https://pan.baidu.com/s/1nsUweTFG_6CcZKaVBCQ7uQ )\n提取码：```4qp7```\n\n#### 服务端\n- 新建```web```项目\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607125410198.png)\n- 放入依赖\n将```apache-cxf-3.3.2\\lib```中的```jar```包全部```copy```至项目```WEB-INF\\lib```目录下（偷个懒，这些```jar```包中包含了```Spring```所需的```jar```包）\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607125835497.png)\n- 在```web.xml```中添加```webService```的配置拦截\n```\n<!--webService  -->\n<servlet>\n    <servlet-name>CXFService</servlet-name>\n    <servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>\n</servlet>\n<servlet-mapping>\n    <servlet-name>CXFService</servlet-name>\n    <url-pattern>/webservice/*</url-pattern>\n</servlet-mapping>\n```\n- ```webservice```服务接口\n在项目```src```目录下新建```pms.inface.WebServiceInterface```类\n```\npackage pms.inface;\n\nimport javax.jws.WebMethod;\nimport javax.jws.WebParam;\nimport javax.jws.WebResult;\nimport javax.jws.WebService;\n\n@WebService(targetNamespace = "http://spring.webservice.server", name = "WebServiceInterface")\npublic interface WebServiceInterface {\n\n	@WebMethod\n    @WebResult(name = "result", targetNamespace = "http://spring.webservice.server")\n	public String sayBye(@WebParam(name = "word", targetNamespace = "http://spring.webservice.server") String word);\n\n}\n\n```\n- 接口实现类\n在项目```src```目录下新建```pms.impl.WebServiceImpl```类\n```\npackage pms.impl;\n\nimport javax.jws.WebService;\n\nimport pms.inface.WebServiceInterface;\n\n@WebService\npublic class WebServiceImpl implements WebServiceInterface{\n\n	@Override\n	public String sayBye(String word) {\n		return word + "当和这个真实的世界迎面撞上时，你是否找到办法和自己身上的欲望讲和，又该如何理解这个铺面而来的人生？";\n	}\n\n}\n\n```\n- ```webservice```配置文件\n在```WEB-INF```目录下新建```webservice```配置文件```cxf-webService.xml```\n```\n<?xml version="1.0" encoding="UTF-8"?>\n<beans xmlns="http://www.springframework.org/schema/beans"\n	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n	xmlns:jaxws="http://cxf.apache.org/jaxws"\n	xmlns:cxf="http://cxf.apache.org/core"\n	xmlns:http-conf="http://cxf.apache.org/transports/http/configuration"\n	xsi:schemaLocation="http://www.springframework.org/schema/beans\n       http://www.springframework.org/schema/beans/spring-beans-3.1.xsd\n       http://cxf.apache.org/jaxws\n       http://cxf.apache.org/schemas/jaxws.xsd\n       http://cxf.apache.org/core\n	   http://cxf.apache.org/schemas/core.xsd\n	   http://cxf.apache.org/transports/http/configuration\n	   http://cxf.apache.org/schemas/configuration/http-conf.xsd\n	   ">\n	   \n	<import resource="classpath:META-INF/cxf/cxf.xml" />\n\n	<!-- 使用jaxws:server标签发布WebService服务 ,设置address为访问地址, 和web.xml文件中配置的CXF配合为一个完整的路径 -->\n	<!-- serviceClass为实现类的接口 serviceBean引用配置好的WebService实现类 -->\n	<jaxws:server address="/webServiceInterface"\n		serviceClass="pms.inface.WebServiceInterface">\n		<jaxws:serviceBean>\n			<ref bean="WebServiceImpl" />\n		</jaxws:serviceBean>\n	</jaxws:server>\n	\n	<!-- 为所有的WS设置超时时间 ,此时为默认值 连接时间30s,等待回复时间为60s-->	\n	<http-conf:conduit name="*.http-conduit">\n		<http-conf:client ConnectionTimeout="60000" ReceiveTimeout="120000"/>\n	</http-conf:conduit>\n\n</beans>\n```\n- ```spring```配置文件\n在```WEB-INF```目录下新建```spring```配置文件```applicationContext.xml```\n```\n<?xml version="1.0" encoding="UTF-8"?>\n\n<beans xmlns="http://www.springframework.org/schema/beans"\n    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n    xsi:schemaLocation="http://www.springframework.org/schema/beans\n    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">\n\n   <bean id="WebServiceImpl" class="pms.impl.WebServiceImpl"></bean>\n	\n	<import resource="cxf-webService.xml" />\n\n</beans>\n```\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在```web.xml```中配置```applicationContext.xml```\n```\n<context-param>\n    <param-name>contextConfigLocation</param-name>\n    <param-value>\n		    /WEB-INF/applicationContext.xml\n		</param-value>\n  </context-param>\n  <listener>\n    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>\n  </listener>\n```\n- 将项目放至```tomcat```中启动\n启动后访问地址：```localhost:PORT/项目名/webservice/webServiceInterface?wsdl```，如下图所示，```webservice```接口发布成功\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607132925804.png)\n\n## 二、SoapUI测试\n```SoapUI```是一个开源测试工具,通过```soap/http```来检查、调用、实现```Web Service```的功能/负载/符合性测试。\n\n#### 下载\n- 百度网盘\n链接：```https://pan.baidu.com/s/1N2RTqhvrkuzx7YJvmDeY7Q```\n提取码：```e1w3```\n\n#### 测试\n- 打开```SoapUI```，新建一个```SOAP```项目，将刚才的发布地址```copy```至```Initial WSDL```栏，点击```OK```按钮\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607133431173.png)\n- 发起接口请求\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607133807442.png)\n\n## 三、客户端\n#### 使用```wsdl2java工具```生成```webservice```客户端代码\n- 该工具在刚才下载的```apache-cxf-3.3.2\\bin```目录下\n![在这里插入图片描述](https://img-blog.csdnimg.cn/2020060713475928.png)\n- 配置环境变量\n设置```CXF_HOME```，并添加```%CXF_HOME %/bin```到```path```环境变量。\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607134834803.png)\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607134926785.png)\n- ```CMD```命令行输入```wsdl2java -help```，有正常提示说明环境已经正确配置\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607135044312.png)\n- wsdl2java.bat用法：\n\n```\nwsdl2java –p 包名 –d 存放目录 -all wsdl地址\n\n-p 指定wsdl的命名空间，也就是要生成代码的包名\n\n-d 指令要生成代码所在目录\n\n-client 生成客户端测试web service的代码\n\n-server 生成服务器启动web service代码\n\n-impl 生成web service的实现代码，我们在方式一用的就是这个\n\n-ant 生成build.xml文件\n\n-all 生成所有开始端点代码\n```\n- 生成客户端代码\n```\nwsdl2java -p pms.inface -d ./ -all http://localhost:8080/spring_webservice_server/webservice/webServiceInterface?wsdl\n```\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607135412387.png)\n#### 客户端调用\n- 新建```web```项目\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607133954278.png)\n- 放入依赖\n将```apache-cxf-3.3.2\\lib```中的```jar```包全部```copy```至项目```WEB-INF\\lib```目录下\n- 将```wsdl2java```生成的代码放至```src.pms.inface```目录下\n![在这里插入图片描述](https://img-blog.csdnimg.cn/2020060713580735.png)\n###### 调用方法一：\n- 新建```webServiceClientMain```测试\n```\npackage pms;\n\nimport org.apache.cxf.jaxws.JaxWsProxyFactoryBean;\nimport pms.inface.WebServiceInterface;\n\npublic class webServiceClientMain {\n	public static void main(String[] args) {\n		JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();\n		svr.setServiceClass(WebServiceInterface.class);\n		svr.setAddress("http://localhost:8080/spring_webservice_server/webservice/webServiceInterface?wsdl");\n		WebServiceInterface webServiceInterface = (WebServiceInterface) svr.create();\n\n		System.out.println(webServiceInterface.sayBye("honey,"));\n	}\n}\n```\n- 运行```webServiceClientMain ```\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607140026284.png)\n###### 调用方法二：\n- 在src目录下新建```applicationContext.xml```\n```\n<?xml version="1.0" encoding="UTF-8"?>\n<beans xmlns="http://www.springframework.org/schema/beans"\n	xmlns:context="http://www.springframework.org/schema/context"\n	xmlns:jaxws="http://cxf.apache.org/jaxws"\n	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n	xsi:schemaLocation="http://www.springframework.org/schema/beans\n		http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n		http://www.springframework.org/schema/context\n		http://www.springframework.org/schema/context/spring-context-3.0.xsd\n		http://cxf.apache.org/jaxws\n		http://cxf.apache.org/schemas/jaxws.xsd">\n\n	<jaxws:client id="webServiceInterface"\n		serviceClass="pms.inface.WebServiceInterface"\n		address="http://localhost:8080/spring_webservice_server/webservice/webServiceInterface?wsdl" >\n	</jaxws:client>	\n</beans>\n```\n\n- 新建```webServiceClientTest```测试\n```\npackage pms;\n\nimport org.springframework.context.ApplicationContext;\nimport org.springframework.context.support.ClassPathXmlApplicationContext;\nimport pms.inface.WebServiceInterface;\n\npublic class webServiceClientTest {\n\n	public static void main(String[] args) {\n		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");\n		WebServiceInterface webServiceInterface = context.getBean(WebServiceInterface.class);\n		String result = webServiceInterface.sayBye("honey,");\n		System.out.println(result);\n	}\n	\n}\n```\n- 运行```webServiceClientTest```\n![在这里插入图片描述](https://img-blog.csdnimg.cn/202006071428238.png)\n\n## 四、服务端拦截器\n- 需求场景：服务提供方安全验证，也就是```webservice```自定义请求头的实现，服务接口在身份认证过程中的密码字段满足```SM3```（哈希函数算法标准）的加密要求\n- ```SM3```加密所需```jar```包：```commons-lang3-3.9.jar```、```bcprov-jdk15on-1.60.jar```，这两个```jar```包在刚才下载的```apache-cxf-3.3.2\\lib```下就有\n- 请求头格式\n```\n<security>\n	<username></username>\n	<password></password>\n</auth>\n```\n- 在```src.pms.interceptor```下新建```WebServiceInInterceptor```拦截器拦截请求，解析头部\n```\npackage pms.interceptor;\n\nimport java.util.List;\nimport javax.servlet.http.HttpServletRequest;\nimport javax.xml.namespace.QName;\nimport org.apache.cxf.binding.soap.SoapMessage;\nimport org.apache.cxf.headers.Header;\nimport org.apache.cxf.interceptor.Fault;\nimport org.apache.cxf.phase.AbstractPhaseInterceptor;\nimport org.apache.cxf.phase.Phase;\nimport org.apache.cxf.transport.http.AbstractHTTPDestination;\nimport org.w3c.dom.Element;\nimport org.w3c.dom.Node;\nimport org.w3c.dom.NodeList;\nimport pms.support.Sm3Utils;\nimport pms.support.StringUtils;\n\n/**\n * WebService的输入拦截器\n * @author coisini\n * @date May 2020, 13\n *\n */\npublic class WebServiceInInterceptor extends AbstractPhaseInterceptor<SoapMessage> {\n	\n	private static final String USERNAME = "admin";\n    private static final String PASSWORD = "P@ssw0rd";\n    \n    /**\n     * 允许访问的IP\n     */\n    private static final String ALLOWIP = "127.0.0.1;XXX.XXX.XXX.XXX";\n\n	public WebServiceInInterceptor() {\n		/*\n		 * 拦截器链有多个阶段，每个阶段都有多个拦截器，拦截器在拦截器链的哪个阶段起作用，可以在拦截器的构造函数中声明\n		 * RECEIVE 接收阶段，传输层处理\n		 * (PRE/USER/POST)_STREAM 流处理/转换阶段\n		 * READ SOAPHeader读取 \n		 * (PRE/USER/POST)_PROTOCOL 协议处理阶段，例如JAX-WS的Handler处理 \n		 * UNMARSHAL SOAP请求解码阶段 \n		 * (PRE/USER/POST)_LOGICAL SOAP请求解码处理阶段 \n		 * PRE_INVOKE 调用业务处理之前进入该阶段 \n		 * INVOKE 调用业务阶段 \n		 * POST_INVOKE 提交业务处理结果，并触发输入连接器\n		 */\n		super(Phase.PRE_INVOKE);\n	}\n\n    /**\n     * 客户端传来的 soap 消息先进入拦截器这里进行处理，客户端的账目与密码消息放在 soap 的消息头<security></security>中，\n     * 类似如下：\n     * <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">\n     * <soap:Header><security><username>admin</username><password>P@ssw0rd</password></security></soap:Header>\n     * <soap:Body></soap:Body></soap:Envelope>\n     * 现在只需要解析其中的 <head></head>标签，如果解析验证成功，则放行，否则这里直接抛出异常，\n     * 服务端不会再往后运行，客户端也会跟着抛出异常，得不到正确结果\n     *\n     * @param message\n     * @throws Fault\n     */\n    @Override\n    public void handleMessage(SoapMessage message) throws Fault {\n    	    System.out.println("PRE_INVOKE");\n		\n	    HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);\n	    String ipAddr=request.getRemoteAddr();\n	    System.out.println("客户端访问IP----"+ipAddr);\n	    \n	    if(!ALLOWIP.contains(ipAddr)) {\n		throw new Fault(new IllegalArgumentException("非法IP地址"), new QName("0009"));\n	    }\n		\n	    /**\n	     * org.apache.cxf.headers.Header\n             * QName ：xml 限定名称，客户端设置头信息时，必须与服务器保持一致，否则这里返回的 header 为null，则永远通不过的\n             */\n		Header authHeader = null;\n		//获取验证头\n		List<Header> headers = message.getHeaders();\n		for(Header h:headers){\n			if(h.getName().toString().contains("security")){\n				authHeader=h;\n				break;\n			}\n		}\n		System.out.println("authHeader");\n		System.out.println(authHeader);\n		\n		if(authHeader !=null) {\n			Element auth = (Element) authHeader.getObject();\n			NodeList childNodes = auth.getChildNodes();\n			String username = null,password = null;\n			for(int i = 0, len = childNodes.getLength(); i < len; i++){\n					Node item = childNodes.item(i);\n					if(item.getNodeName().contains("username")){\n						username = item.getTextContent();\n						System.out.println(username);\n					}\n					if(item.getNodeName().contains("password")){\n						password = item.getTextContent();\n						System.out.println(password);\n					}\n			}\n			\n			if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) { \n		    	throw new Fault(new IllegalArgumentException("用户名或密码不能为空"), new QName("0001")); \n		    }\n			\n			if(!Sm3Utils.verify(USERNAME, username) || !Sm3Utils.verify(PASSWORD,password)) { \n		    	throw new Fault(new IllegalArgumentException("用户名或密码错误"), new QName("0008")); \n		    }\n		  \n		    if (Sm3Utils.verify(USERNAME, username) && Sm3Utils.verify(PASSWORD,password)) { \n		    	System.out.println("webService 服务端自定义拦截器验证通过...."); \n		    	return;//放行\n		    } \n		}else {\n			throw new Fault(new IllegalArgumentException("请求头security不合法"), new QName("0010"));\n		}\n	}\n\n	// 出现错误输出错误信息和栈信息\n	public void handleFault(SoapMessage message) {\n		Exception exeption = message.getContent(Exception.class);\n		System.out.println(exeption.getMessage());\n	}\n	\n}\n```\n\n- 在```src.pms.support```下新建```Sm3Utils```加密类\n```\npackage pms.support;\n\nimport java.io.UnsupportedEncodingException;\nimport java.security.Security;\nimport java.util.Arrays;\nimport org.bouncycastle.crypto.digests.SM3Digest;\nimport org.bouncycastle.crypto.macs.HMac;\nimport org.bouncycastle.crypto.params.KeyParameter;\nimport org.bouncycastle.jce.provider.BouncyCastleProvider;\nimport org.bouncycastle.pqc.math.linearalgebra.ByteUtils;\n\n/**\n * SM3加密\n * @author coisini\n * @date May 2020, 13\n */\npublic class Sm3Utils {\n	 private static final String ENCODING = "UTF-8";\n     static {\n         Security.addProvider(new BouncyCastleProvider());\n     }\n	    \n    /**\n     * sm3算法加密\n     * @explain\n     * @param paramStr\n     * 待加密字符串\n     * @return 返回加密后，固定长度=32的16进制字符串\n     */\n    public static String encrypt(String paramStr){\n        // 将返回的hash值转换成16进制字符串\n        String resultHexString = "";\n        try {\n            // 将字符串转换成byte数组\n            byte[] srcData = paramStr.getBytes(ENCODING);\n            // 调用hash()\n            byte[] resultHash = hash(srcData);\n            // 将返回的hash值转换成16进制字符串\n            resultHexString = ByteUtils.toHexString(resultHash);\n        } catch (UnsupportedEncodingException e) {\n            e.printStackTrace();\n        }\n        return resultHexString;\n    }\n    \n    /**\n     * 返回长度=32的byte数组\n     * @explain 生成对应的hash值\n     * @param srcData\n     * @return\n     */\n    public static byte[] hash(byte[] srcData) {\n        SM3Digest digest = new SM3Digest();\n        digest.update(srcData, 0, srcData.length);\n        byte[] hash = new byte[digest.getDigestSize()];\n        digest.doFinal(hash, 0);\n        return hash;\n    }\n    \n    /**\n     * 通过密钥进行加密\n     * @explain 指定密钥进行加密\n     * @param key\n     *            密钥\n     * @param srcData\n     *            被加密的byte数组\n     * @return\n     */\n    public static byte[] hmac(byte[] key, byte[] srcData) {\n        KeyParameter keyParameter = new KeyParameter(key);\n        SM3Digest digest = new SM3Digest();\n        HMac mac = new HMac(digest);\n        mac.init(keyParameter);\n        mac.update(srcData, 0, srcData.length);\n        byte[] result = new byte[mac.getMacSize()];\n        mac.doFinal(result, 0);\n        return result;\n    }\n    \n    /**\n     * 判断源数据与加密数据是否一致\n     * @explain 通过验证原数组和生成的hash数组是否为同一数组，验证2者是否为同一数据\n     * @param srcStr\n     *            原字符串\n     * @param sm3HexString\n     *            16进制字符串\n     * @return 校验结果\n     */\n    public static boolean verify(String srcStr, String sm3HexString) {\n        boolean flag = false;\n        try {\n            byte[] srcData = srcStr.getBytes(ENCODING);\n            byte[] sm3Hash = ByteUtils.fromHexString(sm3HexString);\n            byte[] newHash = hash(srcData);\n            if (Arrays.equals(newHash, sm3Hash))\n                flag = true;\n        } catch (UnsupportedEncodingException e) {\n            e.printStackTrace();\n        }\n        return flag;\n    }\n    \n    public static void main(String[] args) {\n        // 测试二：account\n        String account = "admin";\n        String passoword = "P@ssw0rd";\n        String hex = Sm3Utils.encrypt(account);\n        System.out.println(hex);//dc1fd00e3eeeb940ff46f457bf97d66ba7fcc36e0b20802383de142860e76ae6\n        System.out.println(Sm3Utils.encrypt(passoword));//c2de40449a2019db9936381fa9810c22c8548a8635ed2b7fb3c7ec362e37429d\n        //验证加密后的16进制字符串与加密前的字符串是否相同\n        boolean flag =  Sm3Utils.verify(account, hex);\n        System.out.println(flag);// true\n    }\n}\n```\n- ```StringUtils```工具类\n```\npackage pms.support;\n\n/**\n * 字符串工具类\n * @author coisini\n * @date Nov 27, 2019\n */\npublic class StringUtils {\n\n	/**\n	 * 判空操作\n	 * @param value\n	 * @return\n	 */\n	public static boolean isBlank(String value) {\n		return value == null || "".equals(value) || "null".equals(value) || "undefined".equals(value);\n	}\n\n}\n```\n- 在```cxf-webService.xml```添加拦截器配置	\n```\n<!-- 在此处引用拦截器 -->\n<bean id="InInterceptor"\n	class="pms.interceptor.WebServiceInInterceptor" >\n</bean>\n\n<cxf:bus>\n	<cxf:inInterceptors>\n		<ref bean="InInterceptor" />\n	</cxf:inInterceptors>\n</cxf:bus> \n```\n- ```SoapUI```调用\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607154834110.png)\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607154807201.png)\n- ```java```调用\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607155002194.png)\n服务端拦截器到此结束，由上图可以看出拦截器配置生效\n## 五、客户端拦截器\n- 在```src.pms.support```下新建```AddHeaderInterceptor```拦截器拦截请求，添加自定义认证头部\n```\npackage pms.support;\n\nimport java.util.List;\nimport javax.xml.namespace.QName;\nimport org.apache.cxf.binding.soap.SoapHeader;\nimport org.apache.cxf.binding.soap.SoapMessage;\nimport org.apache.cxf.headers.Header;\nimport org.apache.cxf.helpers.DOMUtils;\nimport org.apache.cxf.interceptor.Fault;\nimport org.apache.cxf.phase.AbstractPhaseInterceptor;\nimport org.apache.cxf.phase.Phase;\nimport org.w3c.dom.Document;\nimport org.w3c.dom.Element;\n\npublic class AddHeaderInterceptor extends AbstractPhaseInterceptor<SoapMessage>{ \n    \n    private String userName; \n    private String password; \n       \n    public AddHeaderInterceptor(String userName, String password) { \n        super(Phase.PREPARE_SEND); \n        this.userName = userName; \n        this.password = password;  \n    } \n   \n    @Override \n    public void handleMessage(SoapMessage msg) throws Fault { \n    	   System.out.println("拦截...");\n        \n           /**\n            * 生成的XML文档\n            * <authHeader>\n            *      <userName>admin</userName>\n            *      <password>P@ssw0rd</password>\n            * </authHeader>\n            */ \n        \n        	// SoapHeader部分待添加的节点\n     		QName qName = new QName("security");\n     		Document doc = DOMUtils.createDocument();\n\n     		Element pwdEl = doc.createElement("password");\n     		pwdEl.setTextContent(password);\n     		Element userEl = doc.createElement("username");\n     		userEl.setTextContent(userName);\n     		Element root = doc.createElement("security");\n     		root.appendChild(userEl);\n     		root.appendChild(pwdEl);\n     		// 创建SoapHeader内容\n     		SoapHeader header = new SoapHeader(qName, root);\n     		// 添加SoapHeader内容\n     		List<Header> headers = msg.getHeaders();\n     		headers.add(header); \n    } \n}\n```\n- ```java```调用，修改```webServiceClientMain```调用代码如下\n```\npublic class webServiceClientMain {\n	public static void main(String[] args) {\n		JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();\n		svr.setServiceClass(WebServiceInterface.class);\n		svr.setAddress("http://localhost:8081/spring_webservice_server/webservice/webServiceInterface?wsdl");\n		WebServiceInterface webServiceInterface = (WebServiceInterface) svr.create();\n		\n		// jaxws API 转到 cxf API 添加日志拦截器\n		org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy\n				.getClient(webServiceInterface);\n		org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();\n		//添加自定义的拦截器\n		cxfEndpoint.getOutInterceptors().add(new AddHeaderInterceptor("dc1fd00e3eeeb940ff46f457bf97d66ba7fcc36e0b20802383de142860e76ae6", "c2de40449a2019db9936381fa9810c22c8548a8635ed2b7fb3c7ec362e37429d"));\n		\n		System.out.println(webServiceInterface.sayBye("honey,"));\n	}\n}\n```\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607155439994.png)\n- ```SoapUI```调用\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200607155554740.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTE4MjcyNw==,size_16,color_FFFFFF,t_70)\n## 六、代码示例\n服务端：[https://github.com/Maggieq8324/spring_webservice_server.git](https://github.com/Maggieq8324/spring_webservice_server.git)\n客户端：[https://github.com/Maggieq8324/spring_webservice_client.git](https://github.com/Maggieq8324/spring_webservice_client.git)\n\n.end\n', 0, 4, '2021-03-23 20:11:13', 1, 2);
/*!40000 ALTER TABLE `blog` ENABLE KEYS */;

-- 导出  表 blog.blog_tag 结构
CREATE TABLE IF NOT EXISTS `blog_tag` (
  `blog_id` int(11) NOT NULL COMMENT '博文id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  UNIQUE KEY `blog_id` (`blog_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  blog.blog_tag 的数据：~6 rows (大约)
DELETE FROM `blog_tag`;
/*!40000 ALTER TABLE `blog_tag` DISABLE KEYS */;
INSERT INTO `blog_tag` (`blog_id`, `tag_id`) VALUES
	(1, 1),
	(2, 1),
	(3, 1),
	(4, 3),
	(5, 4),
	(6, 1);
/*!40000 ALTER TABLE `blog_tag` ENABLE KEYS */;

-- 导出  表 blog.code 结构
CREATE TABLE IF NOT EXISTS `code` (
  `code_id` varchar(32) NOT NULL COMMENT '邀请码主键',
  `code_state` int(11) NOT NULL COMMENT '激活码状态0 未使用 1已使用 2 已删除',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  blog.code 的数据：~0 rows (大约)
DELETE FROM `code`;
/*!40000 ALTER TABLE `code` DISABLE KEYS */;
INSERT INTO `code` (`code_id`, `code_state`, `user_id`) VALUES
	('90E2077030AB4920A0DE6C16BA21F0C1', 0, NULL);
/*!40000 ALTER TABLE `code` ENABLE KEYS */;

-- 导出  表 blog.discuss 结构
CREATE TABLE IF NOT EXISTS `discuss` (
  `discuss_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '评论唯一id',
  `discuss_body` varchar(255) NOT NULL COMMENT '评论内容',
  `discuss_time` datetime NOT NULL COMMENT '评论时间',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `blog_id` int(11) NOT NULL COMMENT '博文id',
  PRIMARY KEY (`discuss_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.discuss 的数据：~0 rows (大约)
DELETE FROM `discuss`;
/*!40000 ALTER TABLE `discuss` DISABLE KEYS */;
INSERT INTO `discuss` (`discuss_id`, `discuss_body`, `discuss_time`, `user_id`, `blog_id`) VALUES
	(2, '写的不错', '2021-03-23 17:41:13', 1, 1),
	(3, '写得不错', '2021-03-24 18:55:56', 3, 3);
/*!40000 ALTER TABLE `discuss` ENABLE KEYS */;

-- 导出  表 blog.login 结构
CREATE TABLE IF NOT EXISTS `login` (
  `login_time` datetime NOT NULL COMMENT '最后登录时间',
  `login_ip` varchar(16) NOT NULL COMMENT '最后登录ip',
  `user_id` int(11) NOT NULL COMMENT '用户id--主键',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  blog.login 的数据：~0 rows (大约)
DELETE FROM `login`;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
/*!40000 ALTER TABLE `login` ENABLE KEYS */;

-- 导出  表 blog.message 结构
CREATE TABLE IF NOT EXISTS `message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言唯一id',
  `message_name` varchar(30) NOT NULL COMMENT '游客保存为ip地址，用户保存用户名',
  `message_body` varchar(255) NOT NULL COMMENT '留言主体',
  `message_time` datetime NOT NULL COMMENT '留言时间',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.message 的数据：~0 rows (大约)
DELETE FROM `message`;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` (`message_id`, `message_name`, `message_body`, `message_time`) VALUES
	(1, 'admin', '万事从未风过耳，一身只是梦游身', '2021-03-22 22:53:29'),
	(2, 'maggieq', '晚来天欲雪，能饮一杯无？', '2021-03-24 18:59:29');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;

-- 导出  表 blog.reply 结构
CREATE TABLE IF NOT EXISTS `reply` (
  `reply_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '回复唯一id',
  `reply_body` varchar(255) NOT NULL COMMENT '回复内容',
  `reply_time` datetime NOT NULL COMMENT '回复时间',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `discuss_id` int(11) NOT NULL COMMENT '评论id',
  `reply_rootid` int(11) DEFAULT NULL COMMENT '父回复节点id',
  PRIMARY KEY (`reply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  blog.reply 的数据：~0 rows (大约)
DELETE FROM `reply`;
/*!40000 ALTER TABLE `reply` DISABLE KEYS */;
/*!40000 ALTER TABLE `reply` ENABLE KEYS */;

-- 导出  表 blog.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(30) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.role 的数据：~2 rows (大约)
DELETE FROM `role`;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role_id`, `role_name`) VALUES
	(1, 'USER'),
	(2, 'ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;

-- 导出  表 blog.tag 结构
CREATE TABLE IF NOT EXISTS `tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标签id--主键',
  `tag_name` varchar(20) NOT NULL COMMENT '标签名',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.tag 的数据：~2 rows (大约)
DELETE FROM `tag`;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` (`tag_id`, `tag_name`, `user_id`) VALUES
	(1, 'JAVA', 2),
	(3, 'VUE', 2),
	(4, 'Record', 2),
	(5, '111', 3);
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;

-- 导出  表 blog.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户唯一id--主键',
  `user_name` varchar(30) NOT NULL COMMENT '用户名--不能重复',
  `user_password` varchar(255) NOT NULL COMMENT '用户密码',
  `user_mail` varchar(50) NOT NULL COMMENT '用户邮箱',
  `user_state` int(11) NOT NULL COMMENT '用户状态 0 封禁 1正常',
  `user_reward` varchar(255) DEFAULT NULL COMMENT '用户打赏码图片路径',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.user 的数据：~4 rows (大约)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `user_name`, `user_password`, `user_mail`, `user_state`, `user_reward`) VALUES
	(1, 'admin', '$2a$10$RLfa78wYc7/cPH0IAVmH5umQ1rsqTDEBk0EtgKnN7oZrqttbnPtwO', 'maggieq8324@foxmail.com', 1, NULL),
	(2, 'coisini', '$2a$10$cmM8sb/F1v5shzgnSIMK7es7u7rv63g3kMc0hd.dihvwD5bBdjv6.', 'maggieq8324@gmail.com', 1, '/img/a3a19830159a4d5c9ebd6618fdb5b0d3.jpg'),
	(3, 'maggieq', '$2a$10$4q6mG4r5fX2NKdlVXJCKOes90pzVFMRW0a/XHnK0T/fUgG4dD8JKi', '1936524252@qq.com', 1, NULL),
	(4, 'silent', '$2a$10$CMqaeN324VTmxMzZnVX/juTuCLZxW5XQwO8GBr8sAtTN2ND0THleG', 'coisinihcx@gmail.com', 1, NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 导出  表 blog.user_role 结构
CREATE TABLE IF NOT EXISTS `user_role` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- 正在导出表  blog.user_role 的数据：~4 rows (大约)
DELETE FROM `user_role`;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`user_role_id`, `user_id`, `role_id`) VALUES
	(1, 1, 2),
	(2, 2, 1),
	(3, 3, 1),
	(4, 4, 1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
