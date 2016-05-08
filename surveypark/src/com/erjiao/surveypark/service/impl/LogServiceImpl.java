package com.erjiao.surveypark.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.id.UUIDHexGenerator;
import org.springframework.stereotype.Service;

import com.erjiao.surveypark.dao.BaseDao;
import com.erjiao.surveypark.model.Log;
import com.erjiao.surveypark.service.LogService;
import com.erjiao.surveypark.util.LogUtil;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log> implements 
		LogService{
	
	@Resource(name="logDao")
	public void setDao(BaseDao<Log> dao) {
		super.setDao(dao);
	}

	@Override
	public void createLogTable(String tableName) {
		String sql = "create table " + tableName + " as select * from surveypark_logs where 1 = 0";
		this.executeSQL(sql);
	}
	
	/**
	 * ��д�÷���, ��̬������־���¼(��̬��)
	 */
	public void saveEntity(Log t) {
		String sql = "insert into " + LogUtil.generateLogTableName(0) + 
					 "(id,operator, opername, operparams, operresult, resultmsg, opertime) " +
				     "values(?,?,?,?,?,?,?)";
		UUIDHexGenerator uuid = new UUIDHexGenerator();
		String id = (String) uuid.generate(null, null);
		this.executeSQL(sql, id, t.getOperator(), t.getOperName(), t.getOperParams(),t.getOperResult(), t.getResultMsg(), t.getOperTime());
	}

	@SuppressWarnings("unchecked")
	public List<Log> findNearestLogs(int i) {
		String tableName = LogUtil.generateLogTableName(0).toUpperCase();
		//��ѯ���������־������
		String sql = "select t.table_name from all_tables t where t.table_name like 'SURVEYPARK_LOGS_%' " +
				     "and table_name <= '" + tableName + "' and rownum < ? order by table_name desc";
		List<String> list = (List<String>) this.executeSQLQuery(null, sql, i + 1);
		
		//��ѯ����������ڵ���־
		String logSql = "";
		for (String tn : list) {
			logSql = logSql + " select * from " + tn + " union";
		}
		logSql = logSql.substring(0, logSql.length() - 6);
		//ָ�� Log ʵ����
		return (List<Log>) this.executeSQLQuery(Log.class, logSql);
	}
	
}
