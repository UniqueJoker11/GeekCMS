package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Page;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.model.ContentModelContext;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 内容模型管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/module")
public class ContentModelController extends SupportController {

	@Autowired private ISupportDao commonDao;
	
	
	public ContentModelController() {
		this.viewPath = "/admin/module/";
	}
	
	//添加模型
	@RequestMapping("/add")
	public String add(){
		return this.viewPath + "add";
	}
	
	
	
//	/** 编辑用户 */
//	@RequestMapping("/edit")
//	public ModelAndView edit(@RequestParam("id") long id){
//		ModelAndView view = new ModelAndView(this.viewPath+"edit");
//		view.addObject("chip", commonDao.findById(Chip.class, id));
//		return view;
//	}
//	
//	
//	/** 安装服务器上的内容模型 */
//	@ResponseBody
//	@RequestMapping("/install")
//	public Object install(@RequestParam("path") String path,@RequestParam("name") String name){
//		try {
//			name = new String(name.getBytes("iso-8859-1"),"utf-8");
//		} catch (UnsupportedEncodingException e) { 
//			e.printStackTrace();
//		}
//		String moduleFilePath = WebRealPathHolder.REAL_PATH + path + File.separator + name;
//		 
//		int stauts = moduleManagement.install(moduleFilePath);
//		switch (stauts) {
//			case IModuleManagement.INSTALL_SUCCESS:
//				return new ResultMessage(true, "安装模型成功!");
//			case IModuleManagement.INSTALL_EXIST:
//				return new ResultMessage(true, "模型已经存在!");
//			default:
//				return new ResultMessage(false, "安装模型失败!"); 
//		} 
//	}
//	
//	
//	/** 保存 */
//	@ResponseBody
//	@RequestMapping("/save")
//	public Object save(@RequestParam("module") MultipartFile file){
//		
//		 
//		String filePath = WebRealPathHolder.REAL_PATH+File.separator+"upload"+File.separator+"modules"+File.separator+file.getOriginalFilename();
//		File new_file = new File(filePath);
//		 
//		if(new_file.exists()){
//			return new ResultMessage(true, "文件已存在!"); 
//		}
//		
//		try {
//			if (!file.isEmpty()) {  
//	            byte[] bytes = file.getBytes();
//	            FileOutputStream fos = new FileOutputStream(new_file);
//				fos.write(bytes);
//				fos.flush();
//				fos.close();
//			} 
//		} catch (Exception e) { 
//			return new ResultMessage(true, "上次模型失败!");
//		} 
//		int stauts = moduleManagement.install(filePath);
//		switch (stauts) {
//			case IModuleManagement.INSTALL_SUCCESS:
//				return new ResultMessage(true, "安装模型成功!");
//			case IModuleManagement.INSTALL_EXIST:
//				return new ResultMessage(true, "模型已经存在!");
//			default:
//				return new ResultMessage(true, "安装模型失败!"); 
//		}  
//	}
//	
//	
//	
//	/** 更新模型 */
//	@ResponseBody
//	@RequestMapping("/update")
//	public Object update(Module module){
//		if(commonDao.update(module)){
//			return new ResultMessage(true, "更新模型成功");
//		}else{
//			return new ResultMessage(false,"更新模型失败!"); 
//		}
//	}
//	
//	
//	//删除
//	@ResponseBody
//	@RequestMapping("/delete")
//	public Object delete(@RequestParam("type") String moduleType){
//		boolean status = moduleManagement.uninstall(moduleType);
//		if(status){
//			return new ResultMessage(true,"卸载成功!");
//		}else{
//			return new ResultMessage(false,"卸载失败!"); 
//		}
//	}
//	
//	
//	
	//显示列表
	@RequestMapping("/list")
	public ModelAndView list(){ 
		ModelAndView view = new ModelAndView(this.viewPath + "list"); 
		ContentModelContext cmc = ContentModelContext.getInstance(); 
		view.addObject("data",cmc.getCurrentList());
		return view;
	}
	
 
}
