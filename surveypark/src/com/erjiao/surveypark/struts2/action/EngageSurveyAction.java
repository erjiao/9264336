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
 * 参与调查 action 
 */
@Controller
@Scope("prototype")
public class EngageSurveyAction extends BaseAction<Survey> implements ServletContextAware, 
													SessionAware, ParameterAware{

	private static final long serialVersionUID = 1L;
	
	//当前调查的key
	private static final String CURRENT_SURVEY = "current_survey";
	//所有参数的Map的key
	private static final String ALL_PARAMS_MAP = "all_params_map";
	
	private List<Survey> surveys;
	
	private Integer sid;
	
	//当前页面
	private Page currPage;
	
	//当前页面的id
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

	//接收ServletContext 
	private ServletContext sc;
	
	//接收sessionMap
	private Map<String, Object> sessionMap;

	//接收所有参数的Map
	private Map<String, String[]> paramsMap;
	
	public List<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}
	
	/**
	 * 查询所有可用的调查
	 */
	public String findAllAvailableSurveys() {
		this.surveys = surveyService.findAllAvailableSurveys();
		return "engageSurveyListPage";
	}
	
	/**
	 * 获取图片url 地址
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
	 * 首次进入参与调查
	 */
	public String entry() {
		//查询首页
		this.currPage = surveyService.getFirstPage(sid);
		//存放sruvey-->session
		sessionMap.put(CURRENT_SURVEY, currPage.getSurvey());
		//将存放所有参数的大Map --> session中(用户保存答案和回显)
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer, Map<String, String[]>>());
		return "engageSurveyPage";
	}
	
	/**
	 * 处理参与调查
	 */
	public String doEngageSurvey() {
		String submitName = getSubmitName();
		//上一步
		if (submitName.endsWith("pre")) 
		{
			mergeParamsIntoSession();
			this.currPage = surveyService.getPrevPage(currPid);
			return "engageSurveyPage";
		}
		//下一步
		else if (submitName.endsWith("next"))
		{
			mergeParamsIntoSession();
			this.currPage = surveyService.getNextPage(currPid);
			return "engageSurveyPage";
		}
		//完成
		else if (submitName.endsWith("done")) 
		{
			mergeParamsIntoSession();
			//绑定token 到当前线程
//			SurveyparkToken token = new SurveyparkToken();
//			token.setSurvey(getCurrentSurvey());
//			SurveyparkToken.bindToken(token);//绑定令牌
			
			//TODO 答案入库
			surveyService.saveAnswers(processAnswers());
			clearSessionData();
			return "engageSurveyAction";
		} 
		//取消
		else if (submitName.endsWith("exit")) 
		{
			clearSessionData();
			return "engageSurveyAction";
		}
		return "";
	}
	
	private List<Answer> processAnswers() {
		//矩阵式单选按钮的map
		Map<Integer, String> matrixRadioMap = new HashMap<Integer, String>();
		
		//所有答案的集合
		List<Answer> answers = new ArrayList<Answer>();
		Answer a = null;
		String key = null;
		String[] value = null;
		Map<Integer, Map<String, String[]>> allParamsMap = getAllParamsMap();
		for (Map<String, String[]> map : allParamsMap.values()) {
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				//处理所有q开头的参数
				if (key.startsWith("q")) {
					//常规参数
					if (!(key.contains("other") || key.contains("_"))) {
						a = new Answer();
						a.setAnswerIds(StringUtil.arr2str(value));//answeids
						a.setQuestionId(getQid(key));
						a.setSurveyId(getCurrentSurvey().getId());
						a.setOtherAnswer(StringUtil.arr2str(map.get(key + "other")));
						answers.add(a);
					} 
					//矩阵式单选按钮
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
		
		//单独处理矩阵式单选按钮
		processMatrixRadioMap(matrixRadioMap, answers);
		return answers;
	}
	
	/**
	 * 单独处理矩阵式单选按钮 
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
	 * 获取矩阵式问题id q12_0 --> 12
	 */
	private Integer getMatrixRadioQid(String key) {
		return Integer.parseInt(key.substring(1, key.lastIndexOf("_")));
	}

	/**
	 * 获取当前调查对象 
	 */
	private Survey getCurrentSurvey() {
		return (Survey) sessionMap.get(CURRENT_SURVEY);
	}

	/**
	 * 提取问题的id --> q12 -> 12
	 */
	private Integer getQid(String key) {
		return Integer.parseInt(key.substring(1));
	}

	/**
	 * 清除session 数据
	 */
	private void clearSessionData() {
		sessionMap.remove(CURRENT_SURVEY);
		sessionMap.remove(ALL_PARAMS_MAP);
	}
	
	/**
	 * 合并参数到session中
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
	 * 获取提交按钮的名称
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
	 * 设置标记, 用于答案的回显,主要用于radio|checkbox|select 的选中标记
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
	 * 设置标记, other 文本框的回显
	 */
	public String setText(String name) {
		Map<String, String[]> map = getAllParamsMap().get(currPage.getId());
		String[] values = map.get(name);
		return "value='" + values[0] + "'";
	}

	//注入ServletContext 对象
	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0;
	}
	
	//注入sessionMap
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}

	//注入提交的所有参数的Map
	@Override
	public void setParameters(Map<String, String[]> arg0) {
		this.paramsMap = arg0;
	}
}
