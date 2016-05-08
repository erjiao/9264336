package com.erjiao.surveypark.service;

import java.util.List;

import com.erjiao.surveypark.model.Answer;
import com.erjiao.surveypark.model.Page;
import com.erjiao.surveypark.model.Question;
import com.erjiao.surveypark.model.Survey;
import com.erjiao.surveypark.model.User;

/**
 * 
 */
public interface SurveyService {

	/**
	 * 查询调查列表 
	 */
	List<Survey> findMySurveys(User user);

	/**
	 * 新建调查 
	 */
	Survey newSurvey(User user);

	/**
	 *  按照id 查询Survey
	 */
	Survey getSurvey(Integer sid);
	
	/**
	 * 按照id 查询Survey, 同时写到所有的孩子
	 */
	Survey getSurveyWithChildren(Integer sid);

	/**
	 * 更新调查 
	 */
	void updateSurvey(Survey model);

	/**
	 * 保存/更新页面
	 */
	void saveOrUpdatePage(Page model);

	/**
	 * 按照id查询页面
	 */
	Page getPage(Integer pid);
	
	/**
	 * 保存/更新问题 
	 */
	void saveOrUpdateQuestion(Question model);

	/**
	 * 删除问题, 同时删除答案 
	 */
	void deleteQuestion(Integer qid);
	
	/**
	 * 删除页面, 同时删除问题和答案 
	 */
	void deletePage(Integer pid);
	
	/**
	 * 删除调查, 同时删除页面, 问题, 答案
	 */
	void deleteSurvey(Integer sid);
	
	/**
	 * 获取问题
	 */
	Question getQuestion(Integer qid);

	/**
	 * 清楚调查 
	 */
	void clearAnswers(Integer sid);

	/**
	 * 切换调查状态 
	 */
	void toggleStatus(Integer sid);

	/**
	 * 更新logo 的路径
	 */
	void updateLogoPhotoPath(Integer sid, String string);
	
	/**
	 *  查询调查集合, 携带pages
	 */
	List<Survey> getSurveyWithPages(User user);
	
	/**
	 * 进行页面移动/赋值
	 */
	void moveOrCopyPage(Integer srcPid, Integer targPid, int pos);
	
	/**
	 * 查询所有可用的调查 
	 */
	List<Survey> findAllAvailableSurveys();
	
	/**
	 * 查询调查的首页
	 */
	Page getFirstPage(Integer sid);
	
	/**
	 * 查询当前页的上一页 
	 */
	Page getPrevPage(Integer currPid);

	/**
	 * 查询当前页的下一页
	 */
	Page getNextPage(Integer currPid);

	/**
	 * 批量保存答案 
	 */
	void saveAnswers(List<Answer> processAnswers);

	/**
	 * 查询指定调查的所有问题 
	 */
	List<Question> getQuestions(Integer sid);

	/**
	 * 获取指定调查的所有答案
	 */
	List<Answer> getAnswers(Integer sid);
	

}
