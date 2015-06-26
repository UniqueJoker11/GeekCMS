package org.marker.mushroom.template;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.dao.impl.PluginDaoImpl;
import org.marker.mushroom.freemarker.LoadDirective;
import org.marker.mushroom.freemarker.UpperDirective;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.plugin.freemarker.EmbedDirectiveInvokeTag;
import org.marker.mushroom.template.tags.ITag;
import org.marker.mushroom.template.tags.impl.AbsoluteURLTagImpl;
import org.marker.mushroom.template.tags.impl.ExecuteTimeTagImpl;
import org.marker.mushroom.template.tags.impl.FormatDateTagImpl;
import org.marker.mushroom.template.tags.impl.IfTagImpl;
import org.marker.mushroom.template.tags.impl.ListTagImpl;
import org.marker.mushroom.template.tags.impl.LoopTagImpl;
import org.marker.mushroom.template.tags.impl.OnlineUsersTagImpl;
import org.marker.mushroom.template.tags.impl.PluginTagImpl;
import org.marker.mushroom.template.tags.impl.SqlExecuteTagImpl;
import org.marker.mushroom.template.tags.impl.StringSubTagImpl;
import org.marker.mushroom.template.tags.impl.URLRewriteTagImpl;
import org.marker.mushroom.template.tags.res.SqlDataSource;
import org.marker.mushroom.utils.FileTools;
import org.marker.mushroom.utils.FileUtils;
import org.marker.mushroom.utils.ReadLine;
import org.marker.mushroom.utils.WebUtils;
import org.marker.urlrewrite.freemarker.FrontURLRewriteMethodModel;
import org.springframework.stereotype.Service;

import sun.text.resources.FormatData;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;


/**
 * 模板引擎
 * @author marker
 * */
@Service(SystemStatic.SYSTEM_CMS_TEMPLATE)
public class MyCMSTemplate {
	
	/** 日志记录对象 */
	private static final Log log = LogFactory.getLog(MyCMSTemplate.class);
	
	/** 日志记录对象 */
	private static final Configuration config = new Configuration();
	
	
	private	static final StringTemplateLoader loader = new StringTemplateLoader();
	
	
	/** 系统配置信息 */
	private static final SystemConfig syscfg = SystemConfig.getInstance();
	// 编码集(默认UTF-8)
	public static final String encoding = "utf-8";
	
	// 本地语言(默认汉语)
	public static final Locale locale = Locale.CHINA;
	
	
	
	/** 模版标签库 */
	private static final List<ITag> tags = new ArrayList<ITag>();
	
  
	// 临时存储sql数据引擎
	public List<SqlDataSource> temp;
	
	// 存放模版读取时间，为是否更新JSP提供依据
	private Map<String, TemplateFileLoad> tplCache = Collections.synchronizedMap(new HashMap<String, TemplateFileLoad>());
	
	
	
 
	public MyCMSTemplate(){
		try {//加载标签数据
			String cfgFile = "tags"+File.separator+"res"+File.separator+"tags.res";
			String res = MyCMSTemplate.class.getResource(cfgFile).getFile();

			tags.add(new AbsoluteURLTagImpl());
			tags.add(new ExecuteTimeTagImpl());
			tags.add(new FormatDateTagImpl());
			tags.add(new IfTagImpl());
			tags.add(new ListTagImpl());
			tags.add(new LoopTagImpl());
			tags.add(new OnlineUsersTagImpl());
			tags.add(new PluginTagImpl());
			tags.add(new SqlExecuteTagImpl());
			tags.add(new StringSubTagImpl());
			tags.add(new URLRewriteTagImpl());
			
			
			
//			FileUtils.read(res, new ReadLine(){
//				@Override
//				public void rendLine(String className) throws Exception {
//					System.out.println(className);
//					Class<?> clzz = MyCMSTemplate.class.forName(className.trim());
//					new AbsoluteURLTagImpl();
//					tags.add((ITag)clzz.newInstance());
//					log.info("create tag："+className);
//				}
//			}, FileTools.FILE_CHARACTER_UTF8);
			config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			config.setDefaultEncoding(encoding);
		    config.setOutputEncoding(encoding);
			config.setEncoding(locale, encoding);//设置本地字符集
	        config.setLocale(locale);
	        config.setLocalizedLookup(false);
	        config.setTemplateLoader(loader);//设置模板加载器
		} catch (Exception e) {
			log.error("template tags init exception!", e);
		}
	}
	
	 
	
	
	
	/**
	 * 代理编译器
	 * @throws SystemException 
	 * @throws IOException 
	 * */
	public synchronized void proxyCompile(String tplFileName) throws SystemException, IOException{
		boolean isDevMode = Boolean.valueOf(syscfg.get("dev_mode")); 
		//解析模版 
		String start_tpl_path = WebRealPathHolder.REAL_PATH + syscfg.get("themes_path") + tplFileName;//原始模板文件路径
		File tplFile = new File(start_tpl_path);//模板文件 
		
		//如果模板文件存在 
		//检查是否修改
		if(isDevMode){//如果是开发模式，每次获取都将会编译
			compile(tplFileName, tplFile);
		}else{
			TemplateFileLoad tplModel = tplCache.get(tplFileName);
			if(tplModel != null){
				long rt = tplModel.getReadModified();//获取读取时间
				long mt = tplModel.lastModified();// 获取修改时间
				if (mt > rt) {// 模板文件被修改了滴
					compile(tplFileName, tplFile);
				}
			}else{
				compile(tplFileName,tplFile);
			}
		}
	}
	
	/**
	 * 编译
	 * 此方法相当消耗资源，主要瓶颈在于IO操作
	 * @param 
	 * @throws SystemException 
	 * @throws IOException 
	 * */
	private synchronized void compile(String tplFileName, File tplFile) throws SystemException, IOException{
		log.info("start compiling template file " + tplFileName);
		 
		
		//第一步：加载模板内容
		TemplateFileLoad tplloader = new TemplateFileLoad(tplFile);
			
		
		String tempContent = tplloader.getContent();
		 
		this.temp = new ArrayList<SqlDataSource>();// 创建此模板页面的数据池
		tempContent = system_tag_compile(tempContent);// 全部标签解析
		tplloader.setSqls(temp);// 设置SQL集合
		 
		//向模板加载器中写入模板信息
		loader.putTemplate(tplFileName, tempContent);
		tplCache.put(tplFileName, tplloader);
		this.temp = null;
	}
	
	
 
	
	
	/**
	 * 将对象传递到view
	 * */
	public void sendModeltoView(String tpl) throws SystemException{
		HttpServletRequest request   = ActionContext.getReq();
		HttpServletResponse response = ActionContext.getResp();
		ServletContext application   = ActionContext.getApplication();
		
		Map<String,Object> root = new HashMap<String,Object>(); 
		
		
		root.put("encoder", new FrontURLRewriteMethodModel());//URL重写  
		root.put("list",  new UpperDirective());// 调用
		root.put("load", new LoadDirective());//
		root.put("plugin", new EmbedDirectiveInvokeTag());// 嵌入式指令插件
		@SuppressWarnings("unchecked")
		Enumeration<String> attrs3 = application.getAttributeNames(); 
		while (attrs3.hasMoreElements()) {
			String attrName = attrs3.nextElement();
			root.put(attrName, application.getAttribute(attrName));
		}
		//转移Session数据
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		Enumeration<String> attrs2 = session.getAttributeNames();
		while (attrs2.hasMoreElements()) {
			String attrName = attrs2.nextElement();
			root.put(attrName, session.getAttribute(attrName));
		}
		//这里是进行数据转移
		@SuppressWarnings("unchecked")
		Enumeration<String> attrs = request.getAttributeNames(); 
		while (attrs.hasMoreElements()) {
			String attrName = attrs.nextElement();
			root.put(attrName, request.getAttribute(attrName));
		}

		//需要使用的组件准备就绪
		ISupportDao dao = SpringContextHolder.getBean("commonDao");
		
		
		for(SqlDataSource dataTmp : getData(tpl)){//一个一个的数据提取策略
			if(dataTmp == null) continue;
		 
			String queryString = dataTmp.getQueryString();
			
			//获取当前栏目
			Channel current =  (Channel)request.getAttribute(AppStatic.WEB_CURRENT_CHANNEL);
			queryString = queryString.replaceAll("upid", current.getId()+""); 
			root.put(dataTmp.getItems(), dao.queryForList(queryString));
					 
		}
		
		Template template;
		try {
			template = config.getTemplate(tpl);
		} catch (IOException e) {
			throw new SystemException("dsadasda");
		}
		
//		config.set

		
		
		try {
			Writer writer = null;
			boolean isGzip = Boolean.valueOf(syscfg.get("gzip"));//
			if(isGzip){//开启Gzip压缩
				if(WebUtils.checkAccetptGzip(request)){
					OutputStream os = WebUtils.buildGzipOutputStream(response);
					
						writer = new OutputStreamWriter(os,"utf-8");
					
				}else{
					writer = response.getWriter();
				}
			}else{
				writer = response.getWriter();
			} 
			template.process(root, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			new SystemException("发送对象失败");
		}
	}







	
	
	/**
	 * 标记库替换
	 * @param content 内容
	 * @return String 
	 * @throws SystemException 
	 * */
	private String system_tag_compile(String content) throws SystemException{
		for(ITag tag : tags){//遍历编译
			tag.iniContent(content);
			tag.doTag();
			content = tag.getContent();
		}
		return content;
	}



	/**
	 * 推送数据引擎到模版引擎
	 * 
	 * @param items 传递名称
	 * @param data2 数据引擎
	 * @throws SystemException 
	 * */
	public void put(SqlDataSource sqlDs) throws SystemException {
		sqlDs.generateSql();//生成SQL语句 
		temp.add(sqlDs);
	}



	/**
	 * 获取数据引擎集合
	 * @return Map<String,SQLDataEngine> 集合
	 * */
	public List<SqlDataSource> getData(String tpl){
		if(tplCache.containsKey(tpl)){
			return tplCache.get(tpl).getSqls();
		}
		return new ArrayList<SqlDataSource>(0);
	}

}
