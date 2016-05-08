package com.erjiao.surveypark.struts2.action;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ServletContextAware;

import com.erjiao.surveypark.model.Survey;
import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.SurveyService;
import com.erjiao.surveypark.struts2.UserAware;
import com.erjiao.surveypark.util.ValidateUtil;

@Controller
@Scope("prototype")
public class SurveyAction extends BaseAction<Survey> implements UserAware, ServletContextAware{

	private static final long serialVersionUID = 1L;
	
	//注入 SurveyService
	@Resource
	public SurveyService surveyService;

	//调查集合
	private List<Survey> mySurveys;
	
	//接收用户对象
	private User user;
	
	//接收sid参数
	private Integer sid;
	
	//动态跳转错误页面
	private String inputPage;
	
	public String getInputPage() {
		return inputPage;
	}

	public void setInputPage(String inputPage) {
		this.inputPage = inputPage;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public List<Survey> getMySurveys() {
		return mySurveys;
	}

	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}
	
	/**
	 * 设计调查 
	 */
	public String designSurvey() {
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurveyPage";
	}
	
	/**
	 * 编辑调查 
	 */
	public String editSurvey() {
		this.model = surveyService.getSurvey(sid);
		return "editSurveyPage";
	}
	
	/**
	 * 更新调查 
	 */
	public String updateSurvey() {
		this.sid = model.getId();
		//保持关联关系
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction";
	}
	
	/**
	 * 该方法只在updateSurvey 之前运行
	 */
	public void prepareSurvey() {
		inputPage = "/editSurvey.jsp";
	}
	
//	/**
//	 * 该方法只在 designSurvey 之前调用
//	 */
//	public void prepareDesignSurvey() {
//		this.model = surveyService.getSurveyWithChildren(sid);
//	}

	/**
	 * 查询我的调查列表
	 */
	public String mySurveys() {
		this.mySurveys = surveyService.findMySurveys(user);
		return "mySurveyListPage";
	}
	
	/**
	 * 新建调查
	 */
	public String newSurvey() {
		model = surveyService.newSurvey(user);
		return "designSurveyPage";
	}
	
	/**
	 * 删除调查
	 */
	public String deleteSurvey() {
		surveyService.deleteSurvey(sid);
		return "findMySurveyAction";
	}
	
	/**
	 * 清除调查答案 
	 */
	public String clearAnswers() {
		surveyService.clearAnswers(sid);
		return "findMySurveyAction";
	}
	
	/**
	 * 切换调查状态
	 */
	public String toggleStatus() {
		surveyService.toggleStatus(sid);
		return "findMySurveyAction";
	}
	
	/**
	 * 到达增加logo 的页面
	 */
	public String toAddLogoPage() {
		return "addLogoPage";
	}
	
	//上传文件
	private File logoPhoto;
	//文件名称
	private String logoPhotoFileName;

	//接收ServletContext 对象
	private ServletContext sc;
	
	public File getLogoPhoto() {
		return logoPhoto;
	}

	public void setLogoPhoto(File logoPhoto) {
		this.logoPhoto = logoPhoto;
	}

	public String getLogoPhotoFileName() {
		return logoPhotoFileName;
	}

	public void setLogoPhotoFileName(String logoPhotoFileName) {
		this.logoPhotoFileName = logoPhotoFileName;
	}

	/**
	 * 实现logo 的上传  
	 */
	public String doAddLogo() {
		//1.实现上传
		if (ValidateUtil.isValid(logoPhotoFileName)) {
			// /upload文件夹真实路径
			String dir = sc.getRealPath("/upload");
			// 扩展名
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			//纳秒时间作为文件名
			long l = System.nanoTime();
			System.out.println(l + ext);
			//文件另存为
			File newFile = new File(dir, l + ext);
			logoPhoto.renameTo(newFile);
			
			//2.更新路径
			surveyService.updateLogoPhotoPath(sid, "/upload/" + l + ext);
		}
		return "designSurveyAction";
	}
	
	/**
	 * 该方法在 doAddLogo 方法之前调用
	 */
	public void prepareDoAddLogo() {
		inputPage = "/addLogo.jsp";
	}
	
	/**
	 * 图片是否存在 
	 */
	public boolean photoExist() {
		String path = model.getLogoPhotoPath();
		if (ValidateUtil.isValid(path)) {
			String absPath = sc.getRealPath(path);
			File file = new File(absPath);
			return file.exists();
		}
		return false;
	}
	
	/**
	 * 分析调查 
	 */
	public String analyzeSurvey() {
		this.model = surveyService.getSurveyWithChildren(sid);
		return "analyzeSurveyListPage";
	}
	
	
	//注入User
	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0;
	}
}
