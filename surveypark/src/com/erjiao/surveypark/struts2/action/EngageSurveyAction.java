package com.erjiao.surveypark.struts2.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ServletContextAware;

import com.erjiao.surveypark.model.Answer;
import com.erjiao.surveypark.model.Page;
import com.erjiao.surveypark.model.Survey;
import com.erjiao.surveypark.service.SurveyService;
import com.erjiao.surveypark.util.StringUtil;
import com.erjiao.surveypark.util.ValidateUtil;

/**
 * ������� action 
 */
@Controller
@Scope("prototype")
public class EngageSurveyAction extends BaseAction<Survey> implements ServletContextAware, 
													SessionAware, ParameterAware{

	private static final long serialVersionUID = 1L;
	
	//��ǰ�����key
	private static final String CURRENT_SURVEY = "current_survey";
	//���в�����Map��key
	private static final String ALL_PARAMS_MAP = "all_params_map";
	
	private List<Survey> surveys;
	
	private Integer sid;
	
	//��ǰҳ��
	private Page currPage;
	
	//��ǰҳ���id
	private Integer currPid;
	
	public Integer getCurrPid() {
		return currPid;
	}

	public void setCurrPid(Integer currPid) {
		this.currPid = currPid;
	}

	public Page getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Page currPage) {
		this.currPage = currPage;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	@Resource
	private SurveyService surveyService;

	//����ServletContext 
	private ServletContext sc;
	
	//����sessionMap
	private Map<String, Object> sessionMap;

	//�������в�����Map
	private Map<String, String[]> paramsMap;
	
	public List<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}
	
	/**
	 * ��ѯ���п��õĵ���
	 */
	public String findAllAvailableSurveys() {
		this.surveys = surveyService.findAllAvailableSurveys();
		return "engageSurveyListPage";
	}
	
	/**
	 * ��ȡͼƬurl ��ַ
	 */
	public String getImageUrl(String path) {
		if (ValidateUtil.isValid(path)) {
			String realPath = sc.getRealPath(path);
			File f = new File(realPath);
			if (f.exists()) {
				return sc.getContextPath() + path;
			}
		}
		return sc.getContextPath() + "/question.bmp" ;
	}
	
	/**
	 * �״ν���������
	 */
	public String entry() {
		//��ѯ��ҳ
		this.currPage = surveyService.getFirstPage(sid);
		//���sruvey-->session
		sessionMap.put(CURRENT_SURVEY, currPage.getSurvey());
		//��������в����Ĵ�Map --> session��(�û�����𰸺ͻ���)
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer, Map<String, String[]>>());
		return "engageSurveyPage";
	}
	
	/**
	 * ����������
	 */
	public String doEngageSurvey() {
		String submitName = getSubmitName();
		//��һ��
		if (submitName.endsWith("pre")) 
		{
			mergeParamsIntoSession();
			this.currPage = surveyService.getPrevPage(currPid);
			return "engageSurveyPage";
		}
		//��һ��
		else if (submitName.endsWith("next"))
		{
			mergeParamsIntoSession();
			this.currPage = surveyService.getNextPage(currPid);
			return "engageSurveyPage";
		}
		//���
		else if (submitName.endsWith("done")) 
		{
			mergeParamsIntoSession();
			//��token ����ǰ�߳�
//			SurveyparkToken token = new SurveyparkToken();
//			token.setSurvey(getCurrentSurvey());
//			SurveyparkToken.bindToken(token);//������
			
			//TODO �����
			surveyService.saveAnswers(processAnswers());
			clearSessionData();
			return "engageSurveyAction";
		} 
		//ȡ��
		else if (submitName.endsWith("exit")) 
		{
			clearSessionData();
			return "engageSurveyAction";
		}
		return "";
	}
	
	private List<Answer> processAnswers() {
		//����ʽ��ѡ��ť��map
		Map<Integer, String> matrixRadioMap = new HashMap<Integer, String>();
		
		//���д𰸵ļ���
		List<Answer> answers = new ArrayList<Answer>();
		Answer a = null;
		String key = null;
		String[] value = null;
		Map<Integer, Map<String, String[]>> allParamsMap = getAllParamsMap();
		for (Map<String, String[]> map : allParamsMap.values()) {
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				//��������q��ͷ�Ĳ���
				if (key.startsWith("q")) {
					//�������
					if (!(key.contains("other") || key.contains("_"))) {
						a = new Answer();
						a.setAnswerIds(StringUtil.arr2str(value));//answeids
						a.setQuestionId(getQid(key));
						a.setSurveyId(getCurrentSurvey().getId());
						a.setOtherAnswer(StringUtil.arr2str(map.get(key + "other")));
						answers.add(a);
					} 
					//����ʽ��ѡ��ť
					else if (key.contains("_")) {
						Integer radioQid = getMatrixRadioQid(key);
						String oldValue = matrixRadioMap.get(radioQid);
						if (oldValue == null) {
							matrixRadioMap.put(radioQid, StringUtil.arr2str(value));
						} else {
							matrixRadioMap.put(radioQid, oldValue + "," + StringUtil.arr2str(value));
						}
					}
				}
			}
		}
		
		//�����������ʽ��ѡ��ť
		processMatrixRadioMap(matrixRadioMap, answers);
		return answers;
	}
	
	/**
	 * �����������ʽ��ѡ��ť 
	 */
	private void processMatrixRadioMap(Map<Integer, String> matrixRadioMap,
			List<Answer> answers) {
		Answer a = null;
		Integer key = null;
		String value = null;
		for (Map.Entry<Integer, String> entry : matrixRadioMap.entrySet()) {
			key = entry.getKey();
			value = entry.getValue();
			a = new Answer();
			a.setAnswerIds(value);
			a.setQuestionId(key);
			a.setSurveyId(getCurrentSurvey().getId());
			answers.add(a);
		}
	}

	/**
	 * ��ȡ����ʽ����id q12_0 --> 12
	 */
	private Integer getMatrixRadioQid(String key) {
		return Integer.parseInt(key.substring(1, key.lastIndexOf("_")));
	}

	/**
	 * ��ȡ��ǰ������� 
	 */
	private Survey getCurrentSurvey() {
		return (Survey) sessionMap.get(CURRENT_SURVEY);
	}

	/**
	 * ��ȡ�����id --> q12 -> 12
	 */
	private Integer getQid(String key) {
		return Integer.parseInt(key.substring(1));
	}

	/**
	 * ���session ����
	 */
	private void clearSessionData() {
		sessionMap.remove(CURRENT_SURVEY);
		sessionMap.remove(ALL_PARAMS_MAP);
	}
	
	/**
	 * �ϲ�������session��
	 */
	private void mergeParamsIntoSession() {
		Map<Integer, Map<String, String[]>> allParamsMap = getAllParamsMap();
		allParamsMap.put(currPid, paramsMap);
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, String[]>> getAllParamsMap() {
		return (Map<Integer, Map<String, String[]>>) sessionMap.get(ALL_PARAMS_MAP);
	}

	/**
	 * ��ȡ�ύ��ť������
	 */
	public String getSubmitName() {
		for (String key : paramsMap.keySet()) {
			if (key.startsWith("submit_")) {
				return key;
			}
		}
		return "";
	}
	
	/**
	 * ���ñ��, ���ڴ𰸵Ļ���,��Ҫ����radio|checkbox|select ��ѡ�б��
	 */
	public String setTag(String name, String value, String selTag) {
		Map<String, String[]> map = getAllParamsMap().get(currPage.getId());
		String[] values = map.get(name);
		if (StringUtil.contains(values, value)) {
			return selTag;
		}
		return "";
	}
	
	/**
	 * ���ñ��, other �ı���Ļ���
	 */
	public String setText(String name) {
		Map<String, String[]> map = getAllParamsMap().get(currPage.getId());
		String[] values = map.get(name);
		return "value='" + values[0] + "'";
	}

	//ע��ServletContext ����
	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0;
	}
	
	//ע��sessionMap
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}

	//ע���ύ�����в�����Map
	@Override
	public void setParameters(Map<String, String[]> arg0) {
		this.paramsMap = arg0;
	}
}
