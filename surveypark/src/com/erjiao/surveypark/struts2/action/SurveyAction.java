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
	
	//ע�� SurveyService
	@Resource
	public SurveyService surveyService;

	//���鼯��
	private List<Survey> mySurveys;
	
	//�����û�����
	private User user;
	
	//����sid����
	private Integer sid;
	
	//��̬��ת����ҳ��
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
	 * ��Ƶ��� 
	 */
	public String designSurvey() {
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurveyPage";
	}
	
	/**
	 * �༭���� 
	 */
	public String editSurvey() {
		this.model = surveyService.getSurvey(sid);
		return "editSurveyPage";
	}
	
	/**
	 * ���µ��� 
	 */
	public String updateSurvey() {
		this.sid = model.getId();
		//���ֹ�����ϵ
		model.setUser(user);
		surveyService.updateSurvey(model);
		return "designSurveyAction";
	}
	
	/**
	 * �÷���ֻ��updateSurvey ֮ǰ����
	 */
	public void prepareSurvey() {
		inputPage = "/editSurvey.jsp";
	}
	
//	/**
//	 * �÷���ֻ�� designSurvey ֮ǰ����
//	 */
//	public void prepareDesignSurvey() {
//		this.model = surveyService.getSurveyWithChildren(sid);
//	}

	/**
	 * ��ѯ�ҵĵ����б�
	 */
	public String mySurveys() {
		this.mySurveys = surveyService.findMySurveys(user);
		return "mySurveyListPage";
	}
	
	/**
	 * �½�����
	 */
	public String newSurvey() {
		model = surveyService.newSurvey(user);
		return "designSurveyPage";
	}
	
	/**
	 * ɾ������
	 */
	public String deleteSurvey() {
		surveyService.deleteSurvey(sid);
		return "findMySurveyAction";
	}
	
	/**
	 * �������� 
	 */
	public String clearAnswers() {
		surveyService.clearAnswers(sid);
		return "findMySurveyAction";
	}
	
	/**
	 * �л�����״̬
	 */
	public String toggleStatus() {
		surveyService.toggleStatus(sid);
		return "findMySurveyAction";
	}
	
	/**
	 * ��������logo ��ҳ��
	 */
	public String toAddLogoPage() {
		return "addLogoPage";
	}
	
	//�ϴ��ļ�
	private File logoPhoto;
	//�ļ�����
	private String logoPhotoFileName;

	//����ServletContext ����
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
	 * ʵ��logo ���ϴ�  
	 */
	public String doAddLogo() {
		//1.ʵ���ϴ�
		if (ValidateUtil.isValid(logoPhotoFileName)) {
			// /upload�ļ�����ʵ·��
			String dir = sc.getRealPath("/upload");
			// ��չ��
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			//����ʱ����Ϊ�ļ���
			long l = System.nanoTime();
			System.out.println(l + ext);
			//�ļ����Ϊ
			File newFile = new File(dir, l + ext);
			logoPhoto.renameTo(newFile);
			
			//2.����·��
			surveyService.updateLogoPhotoPath(sid, "/upload/" + l + ext);
		}
		return "designSurveyAction";
	}
	
	/**
	 * �÷����� doAddLogo ����֮ǰ����
	 */
	public void prepareDoAddLogo() {
		inputPage = "/addLogo.jsp";
	}
	
	/**
	 * ͼƬ�Ƿ���� 
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
	 * �������� 
	 */
	public String analyzeSurvey() {
		this.model = surveyService.getSurveyWithChildren(sid);
		return "analyzeSurveyListPage";
	}
	
	
	//ע��User
	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0;
	}
}
