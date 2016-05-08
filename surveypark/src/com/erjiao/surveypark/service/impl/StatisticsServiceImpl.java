package com.erjiao.surveypark.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erjiao.surveypark.dao.BaseDao;
import com.erjiao.surveypark.model.Answer;
import com.erjiao.surveypark.model.Question;
import com.erjiao.surveypark.model.statistics.OptionStatisticsModel;
import com.erjiao.surveypark.model.statistics.QuestionStatisticsModel;
import com.erjiao.surveypark.service.StatisticsService;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
	
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao;
	
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao;
	
	@Override
	public QuestionStatisticsModel statistics(Integer qid) {
		Question q = questionDao.getEntity(qid);
		QuestionStatisticsModel qsm = new QuestionStatisticsModel();
		qsm.setQuestion(q);
		
		//统计回答问题的总人数
		String hql = "select count(*) from Answer a where a.questionId = ?";
		Long qcount = (Long) answerDao.uniqueResult(hql, qid);
		qsm.setCount(qcount.intValue());
		
		//统计回答问题中选中某一个选项的人数
		String ohql = "select count(*) from Answer a where a.questionId = ? and ',' || a.answerIds || ',' like ?";
		Long ocount = null;
		
		//统计每个问题选项的情况
		int qt = q.getQuestionType();
		switch(qt){
			//非矩阵式问题
			case 0 : 
			case 1 : 
			case 2 : 
			case 3 : 
			case 4 : 
				String[] arr = q.getOptionArr();
				OptionStatisticsModel osm = null;
				for (int i = 0; i < arr.length; i++) {
					osm = new OptionStatisticsModel();
					osm.setOptionIndex(i);
					osm.setOptionLabel(arr[i]);
					ocount = (Long) answerDao.uniqueResult(ohql, qid, "%" + i + "%");
					osm.setCount(ocount.intValue());
					qsm.getOsms().add(osm);
				}
				//other
				if (q.isOther()) {
					osm = new OptionStatisticsModel();
					osm.setOptionLabel("其他");
					ocount = (Long) answerDao.uniqueResult(ohql, qid, "%other%");
					osm.setCount(ocount.intValue());
					qsm.getOsms().add(osm);
				}
				break;
			//矩阵式问题
			case 6 : 
			case 7 : 
			case 8 : 
				String[] rows = q.getMatrixRowTitleArr();
				String[] cols = q.getMatrixColTitleArr();
				String[] opts = q.getMatrixSelectOptionArr();
				
				for (int i = 0; i < rows.length; i ++) {
					for (int j = 0; j < cols.length; j ++) {
						//matrix raido | checkbox
						if (qt != 8) {
							osm = new OptionStatisticsModel();
							osm.setMatrixRowIndex(i);
							osm.setMatrixRowLabel(rows[i]);
							osm.setMatrixColIndex(j);
							osm.setMatrixColLabel(cols[j]);
							ocount = (Long) answerDao.uniqueResult(ohql, qid, "%" + i + "_" + j + "%");
							osm.setCount(ocount.intValue());
							qsm.getOsms().add(osm);
						} else {
							for (int k = 0; k < opts.length; k ++) {
								osm = new OptionStatisticsModel();
								osm.setMatrixRowIndex(i);
								osm.setMatrixRowLabel(rows[i]);
								osm.setMatrixColIndex(j);
								osm.setMatrixColLabel(cols[j]);
								osm.setMatrixSelectIndex(k);
								osm.setMatrixSelectLabel(opts[k]);
								ocount = (Long) answerDao.uniqueResult(ohql, qid, "%" + i + "_" + j + "_" + k + "%");
								osm.setCount(ocount.intValue());
								qsm.getOsms().add(osm);
							}
						}
					}
				}
				
				break;
		}
		return qsm;
	}

}
