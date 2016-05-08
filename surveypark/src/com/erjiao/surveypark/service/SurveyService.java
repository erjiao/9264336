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
	 * ��ѯ�����б� 
	 */
	List<Survey> findMySurveys(User user);

	/**
	 * �½����� 
	 */
	Survey newSurvey(User user);

	/**
	 *  ����id ��ѯSurvey
	 */
	Survey getSurvey(Integer sid);
	
	/**
	 * ����id ��ѯSurvey, ͬʱд�����еĺ���
	 */
	Survey getSurveyWithChildren(Integer sid);

	/**
	 * ���µ��� 
	 */
	void updateSurvey(Survey model);

	/**
	 * ����/����ҳ��
	 */
	void saveOrUpdatePage(Page model);

	/**
	 * ����id��ѯҳ��
	 */
	Page getPage(Integer pid);
	
	/**
	 * ����/�������� 
	 */
	void saveOrUpdateQuestion(Question model);

	/**
	 * ɾ������, ͬʱɾ���� 
	 */
	void deleteQuestion(Integer qid);
	
	/**
	 * ɾ��ҳ��, ͬʱɾ������ʹ� 
	 */
	void deletePage(Integer pid);
	
	/**
	 * ɾ������, ͬʱɾ��ҳ��, ����, ��
	 */
	void deleteSurvey(Integer sid);
	
	/**
	 * ��ȡ����
	 */
	Question getQuestion(Integer qid);

	/**
	 * ������� 
	 */
	void clearAnswers(Integer sid);

	/**
	 * �л�����״̬ 
	 */
	void toggleStatus(Integer sid);

	/**
	 * ����logo ��·��
	 */
	void updateLogoPhotoPath(Integer sid, String string);
	
	/**
	 *  ��ѯ���鼯��, Я��pages
	 */
	List<Survey> getSurveyWithPages(User user);
	
	/**
	 * ����ҳ���ƶ�/��ֵ
	 */
	void moveOrCopyPage(Integer srcPid, Integer targPid, int pos);
	
	/**
	 * ��ѯ���п��õĵ��� 
	 */
	List<Survey> findAllAvailableSurveys();
	
	/**
	 * ��ѯ�������ҳ
	 */
	Page getFirstPage(Integer sid);
	
	/**
	 * ��ѯ��ǰҳ����һҳ 
	 */
	Page getPrevPage(Integer currPid);

	/**
	 * ��ѯ��ǰҳ����һҳ
	 */
	Page getNextPage(Integer currPid);

	/**
	 * ��������� 
	 */
	void saveAnswers(List<Answer> processAnswers);

	/**
	 * ��ѯָ��������������� 
	 */
	List<Question> getQuestions(Integer sid);

	/**
	 * ��ȡָ����������д�
	 */
	List<Answer> getAnswers(Integer sid);
	

}
