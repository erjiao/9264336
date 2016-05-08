package com.erjiao.surveypark.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erjiao.surveypark.dao.BaseDao;
import com.erjiao.surveypark.model.Answer;
import com.erjiao.surveypark.model.Page;
import com.erjiao.surveypark.model.Question;
import com.erjiao.surveypark.model.Survey;
import com.erjiao.surveypark.model.User;
import com.erjiao.surveypark.service.SurveyService;
import com.erjiao.surveypark.util.DataUtil;

/**
 * SurveyService ʵ��
 */
@Service("surveyService")
public class SurveyServiceImpl implements SurveyService {

	@Resource(name = "surveyDao")
	private BaseDao<Survey> surveyDao;

	@Resource(name = "pageDao")
	private BaseDao<Page> pageDao;
	
	@Resource(name = "questionDao")	
	private BaseDao<Question> questionDao; 
	
	@Resource(name = "answerDao")
	private BaseDao<Answer> answerDao;

	/**
	 * ��ѯ���鼯��
	 */
	@Override
	public List<Survey> findMySurveys(User user) {
		String hql = "from Survey s where s.user.id = ?";
		return surveyDao.findEntityByHQL(hql, user.getId());
	}

	@Override
	public Survey newSurvey(User user) {
		Survey s = new Survey();
		Page p = new Page();

		// ���ù�����ϵ, ���� SURVEYPARK_SURVEYS ���� userid Ϊ��.
		s.setUser(user);
		p.setSurvey(s);
		s.getPages().add(p);
		surveyDao.saveEntity(s);
		pageDao.saveEntity(p);
		return s;
	}

	@Override
	public Survey getSurvey(Integer sid) {
		return surveyDao.getEntity(sid);
	}

	@Override
	public Survey getSurveyWithChildren(Integer sid) {
		// Survey s = surveyDao.getEntity(sid);
		Survey s = this.getSurvey(sid);
		// ǿ�г�ʼ��pages ��questions ����
		for (Page p : s.getPages()) {
			p.getQuestions().size();
		}

		return s;
	}

	@Override
	public void updateSurvey(Survey model) {
		surveyDao.updateEntity(model);
	}

	@Override
	public void saveOrUpdatePage(Page model) {
		pageDao.saveOrUpdate(model);
	}

	@Override
	public Page getPage(Integer pid) {
		return pageDao.getEntity(pid);
	}

	@Override
	public void saveOrUpdateQuestion(Question model) {
		questionDao.saveOrUpdate(model);
	}

	@Override
	public void deleteQuestion(Integer qid) {
		//1.ɾ��answer
		String hql = "delete from Answer a where a.questionId = ?";
		answerDao.batchEntityByHQL(hql, qid);
		//2.ɾ������
		hql = "delete from Question q where q.id = ?";
		questionDao.batchEntityByHQL(hql, qid);
	}

	@Override
	public void deletePage(Integer pid) {
		//delete answer
		String hql = "delete from Answer a where a.questionId in (select q.id from Question q where q.page.id = ?)";
		answerDao.batchEntityByHQL(hql, pid);
		//delete questions
		hql = "delete from Question q where q.page.id = ?";
		questionDao.batchEntityByHQL(hql, pid);
		//delete page
		hql = "delete from Page p where p.id = ?";
		pageDao.batchEntityByHQL(hql, pid);
	}

	@Override
	public void deleteSurvey(Integer sid) {
		//delete answers
		String hql = "delete from Answer a where a.surveyId = ?";
		answerDao.batchEntityByHQL(hql, sid);
		
		//delete questions
		//hibernate ��д������, �������������ϵ�����
		//hql = "delete from Question q where q.page.survey.id = ?";
		hql = "delete from Question q where q.page.id in (select p.id from Page p where p.survey.id = ?)";
		questionDao.batchEntityByHQL(hql, sid);
		
		//delete page
		hql = "delete from Page p where p.survey.id = ?";
		pageDao.batchEntityByHQL(hql, sid);
		
		//delete survey
		hql = "delete from Survey s where s.id = ?";
		surveyDao.batchEntityByHQL(hql, sid);
	}

	@Override
	public Question getQuestion(Integer qid) {
		return questionDao.getEntity(qid);
	}

	@Override
	public void clearAnswers(Integer sid) {
		String hql = "delete from Answer a where a.surveyId = ?";
		answerDao.batchEntityByHQL(hql, sid);
	}

	@Override
	public void toggleStatus(Integer sid) {
		Survey s = this.getSurvey(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?";
		surveyDao.batchEntityByHQL(hql, !s.isClosed(), sid);
	}

	@Override
	public void updateLogoPhotoPath(Integer sid, String path) {
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?";
		surveyDao.batchEntityByHQL(hql, path, sid);
	}

	@Override
	public List<Survey> getSurveyWithPages(User user) {
		String hql = "from Survey s where s.user.id = ?";
		List<Survey> list = surveyDao.findEntityByHQL(hql, user.getId());
		//ǿ�г�ʼ��page, ��ֹ������
		for (Survey s : list) {
			s.getPages().size();
		}
		return list;
	}

	@Override
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos) {
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		
		//�ж��ƶ�/��ֵ
		if (srcSurvey.getId().equals(targSurvey.getId())) {
			setOrderno(srcPage, targPage, pos);
		}
		//��ֵ
		else {
			//ǿ�г�ʼ�����⼯��, ������ȸ���ҳ�����û�����⼯��.
			srcPage.getQuestions().size();
			//��ȸ���
			Page copyPage = (Page) DataUtil.deeplyCopy(srcPage);
			//����ҳ����Ŀ��������
			copyPage.setSurvey(targSurvey);
			//����ҳ��
			pageDao.saveEntity(copyPage);
			//��������
			for (Question q : copyPage.getQuestions()) {
				questionDao.saveEntity(q);
			}
			setOrderno(copyPage, targPage, pos);
		}
			
	}

	/**
	 * ����ҳ��
	 */
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//֮ǰ
		if (pos == 0) {
			//�ж�Ŀ��ҳ�Ƿ�����ҳ
			if (isFirstPage(targPage)) {
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			} else {
				//ȡ��Ŀ��ҳ��ǰһҳ
				Page prevPage = getPrevPage(targPage);
				srcPage.setOrderno((targPage.getOrderno() + prevPage.getOrderno()) / 2);
			}
		}
		//֮��
		else {
			//�ж�Ŀ��ҳ�Ƿ���βҳ
			if (isLastPage(targPage)) {
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			} else {
				//ȡ��Ŀ��ҳ�ĺ�һҳ
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((targPage.getOrderno() + nextPage.getOrderno()) / 2);
			}
		}
		
	}

	/**
	 * ��ѯָ��ҳ��ĺ�һҳ
	 */
	private Page getNextPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno > ? order by p.orderno asc";
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(), targPage.getOrderno());
		return list.get(0);
	}

	/**
	 * �ж�ָ��ҳ���Ƿ������ڵ����βҳ
	 */
	private boolean isLastPage(Page targPage) {
		String hql = "select count(*) from Page p where p.survey.id = ? and p.orderno > ?";
		Long count = (Long) pageDao.uniqueResult(hql, targPage.getSurvey().getId(), targPage.getOrderno());
		return count == 0;
	}
	
	/**
	 * ��ѯָ��ҳ�����һҳ
	 */
	private Page getPrevPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno < ? order by p.orderno desc";
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(), targPage.getOrderno());
		return list.get(0);
	}
	
	/**
	 * �ж�ָ��ҳ���Ƿ������ڵ������ҳ
	 */
	private boolean isFirstPage(Page targPage) {
		String hql = "select count(*) from Page p where p.survey.id = ? and p.orderno < ?";
		Long count = (Long) pageDao.uniqueResult(hql, targPage.getSurvey().getId(), targPage.getOrderno());
		return count == 0;
	}

	@Override
	public List<Survey> findAllAvailableSurveys() {
		String hql = "from Survey s where s.closed = ?";
		return surveyDao.findEntityByHQL(hql, false);
	}

	@Override
	public Page getFirstPage(Integer sid) {
		String hql = "from Page p where p.survey.id = ? order by p.orderno asc";
		List<Page> lists = pageDao.findEntityByHQL(hql, sid);
		Page p = lists.get(0);
		//��ʼ�����⼯��
		p.getQuestions().size();
		//�ϼ�����ҲҪ��ʼ��
		p.getSurvey().getTitle();
		return p;
	}

	@Override
	public Page getPrevPage(Integer currPid) {
		Page p = this.getPage(currPid);
		p = this.getPrevPage(p);
		p.getQuestions().size();//ҳ����Ҫ��Ⱦ���⼯��,��ֹ������
		return p;
	}

	@Override
	public Page getNextPage(Integer currPid) {
		Page p = this.getPage(currPid);
		p = this.getNextPage(p);
		p.getQuestions().size(); //ҳ����Ҫ��Ⱦ���⼯��,��ֹ������
		return p;
	}

	@Override
	public void saveAnswers(List<Answer> processAnswers) {
		Date date = new Date();
		String uuid = UUID.randomUUID().toString();
		for (Answer a : processAnswers) {
			a.setUuid(uuid);
			a.setAnswerTime(date);
			answerDao.saveEntity(a);
		}
	}

	@Override
	public List<Question> getQuestions(Integer sid) {
		String hql = "from Question q where q.page.survey.id = ?";
		return questionDao.findEntityByHQL(hql, sid);
	}

	@Override
	public List<Answer> getAnswers(Integer sid) {
		String hql = "from Answer a where a.surveyId = ? order by a.uuid";
		return answerDao.findEntityByHQL(hql, sid);
	}
	
	
}
