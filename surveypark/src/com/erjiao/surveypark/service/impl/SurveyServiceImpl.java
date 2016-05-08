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
 * SurveyService 实现
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
	 * 查询调查集合
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

		// 设置关联关系, 否则 SURVEYPARK_SURVEYS 表中 userid 为空.
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
		// 强行初始化pages 和questions 集合
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
		//1.删除answer
		String hql = "delete from Answer a where a.questionId = ?";
		answerDao.batchEntityByHQL(hql, qid);
		//2.删除问题
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
		//hibernate 在写操作中, 不允许两级以上的链接
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
		//强行初始化page, 防止懒加载
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
		
		//判断移动/赋值
		if (srcSurvey.getId().equals(targSurvey.getId())) {
			setOrderno(srcPage, targPage, pos);
		}
		//赋值
		else {
			//强行初始化问题集合, 否则深度复制页面对象没有问题集合.
			srcPage.getQuestions().size();
			//深度复制
			Page copyPage = (Page) DataUtil.deeplyCopy(srcPage);
			//设置页面与目标调查关联
			copyPage.setSurvey(targSurvey);
			//保存页面
			pageDao.saveEntity(copyPage);
			//保存问题
			for (Question q : copyPage.getQuestions()) {
				questionDao.saveEntity(q);
			}
			setOrderno(copyPage, targPage, pos);
		}
			
	}

	/**
	 * 设置页序
	 */
	private void setOrderno(Page srcPage, Page targPage, int pos) {
		//之前
		if (pos == 0) {
			//判断目标页是否是首页
			if (isFirstPage(targPage)) {
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			} else {
				//取得目标页的前一页
				Page prevPage = getPrevPage(targPage);
				srcPage.setOrderno((targPage.getOrderno() + prevPage.getOrderno()) / 2);
			}
		}
		//之后
		else {
			//判断目标页是否是尾页
			if (isLastPage(targPage)) {
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			} else {
				//取得目标页的后一页
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((targPage.getOrderno() + nextPage.getOrderno()) / 2);
			}
		}
		
	}

	/**
	 * 查询指定页面的后一页
	 */
	private Page getNextPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno > ? order by p.orderno asc";
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(), targPage.getOrderno());
		return list.get(0);
	}

	/**
	 * 判断指定页面是否是所在调查的尾页
	 */
	private boolean isLastPage(Page targPage) {
		String hql = "select count(*) from Page p where p.survey.id = ? and p.orderno > ?";
		Long count = (Long) pageDao.uniqueResult(hql, targPage.getSurvey().getId(), targPage.getOrderno());
		return count == 0;
	}
	
	/**
	 * 查询指定页面的上一页
	 */
	private Page getPrevPage(Page targPage) {
		String hql = "from Page p where p.survey.id = ? and p.orderno < ? order by p.orderno desc";
		List<Page> list = pageDao.findEntityByHQL(hql, targPage.getSurvey().getId(), targPage.getOrderno());
		return list.get(0);
	}
	
	/**
	 * 判断指定页面是否是所在调查的首页
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
		//初始化问题集合
		p.getQuestions().size();
		//上级调查也要初始化
		p.getSurvey().getTitle();
		return p;
	}

	@Override
	public Page getPrevPage(Integer currPid) {
		Page p = this.getPage(currPid);
		p = this.getPrevPage(p);
		p.getQuestions().size();//页面需要渲染问题集合,防止懒加载
		return p;
	}

	@Override
	public Page getNextPage(Integer currPid) {
		Page p = this.getPage(currPid);
		p = this.getNextPage(p);
		p.getQuestions().size(); //页面需要渲染问题集合,防止懒加载
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
