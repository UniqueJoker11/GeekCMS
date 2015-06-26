package org.marker.mushroom.controller;

import java.util.Date;

import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.support.SupportController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 文章管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/aticle")
public class ArticleController extends SupportController {

	private final static int ATICLE_CURRENTPAGE=1;
	private final static int ATICLE_PAGESIZE=20;
	
	public ArticleController() {
		this.viewPath = "/admin/article/";
		
	}
	
	//发布文章
	@RequestMapping("/publish")
	public ModelAndView publish(){
		ModelAndView view = new ModelAndView(this.viewPath+"publish");
		view.addObject("channels", commonDao.queryForList("select * from  "+getPrefix()+"channel where module in ('article','help')"));
		return view;
	}
	
	//编辑文章
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int id){
		ModelAndView view = new ModelAndView(this.viewPath+"edit");
		view.addObject("article", commonDao.findById(Article.class, id));
		view.addObject("channels", commonDao.queryForList("select * from  "+getPrefix()+"channel where module in ('article','help')"));
		return view;
	}
	
	
	/**
	 * 持久化文章操作
	 * @param article
	 * @param pid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(Article article, @RequestParam("pid") int pid){
		article.setTime(new Date());
		article.setPid(pid);// 这里是因为不能注入bean里
		
		String msg = "";
		if(article.getStatus() == 1){
			msg = "发布";
		}else{
			msg = "保存草稿";
		} 
		
		if(commonDao.save(article)){ 
			return new ResultMessage(true, msg+"成功!"); 
		}else{
			return new ResultMessage(false, msg+"失败!"); 
		}
	}
	
	
	//保存
	@ResponseBody
	@RequestMapping("/update")
	public Object update(@ModelAttribute("article") Article article){
		article.setTime(new Date());
		if(commonDao.update(article)){
			return new ResultMessage(true, "更新成功!");
		}else{
			return new ResultMessage(false,"更新失败!"); 
		}
	}
	
	
	
	//删除文章
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
		boolean status = commonDao.deleteByIds(Article.class, rid);
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}
 
	
	
	/**    
	 * 方法描述：   初始化所有的的文章列表
	 * 注意事项：    
	 * @return 
	 * @Exception 异常对象 
	*/
	@RequestMapping(value="list",method=RequestMethod.GET)
	public ModelAndView initAticleList(){
		ModelAndView view=new ModelAndView(this.viewPath+"list");
		Page aticlePage=commonDao.findByPage(this.ATICLE_CURRENTPAGE, this.ATICLE_PAGESIZE, 
				"select a.*,c.name  as cname ,c.url as ctype, c.module from  "+
		getPrefix()+"article as a left join  "+getPrefix()+"channel c on c.id=a.pid order by a.id desc");
		view.addObject("page", aticlePage);
		return view;
		
	}
	
	/**
	 * 文章列表接口
	 * @param currentPageNo
	 * @return
	 */
	@RequestMapping(value = "aticleList", method = RequestMethod.GET)
	@ResponseBody 
	public Object getAticleList(@RequestParam("currentPageNo") int currentPageNo){
		
		//view.addObject("channels", dao.queryForList("select * from  "+getPrefix()+"channel where module in ('article','help')"));
		return commonDao.findByPage(currentPageNo, this.ATICLE_PAGESIZE, 
				"select a.*,c.name  as cname ,c.url as ctype, c.module from  "+
		getPrefix()+"article as a left join  "+getPrefix()+"channel c on c.id=a.pid order by a.id desc");
	 
	}
	
}
