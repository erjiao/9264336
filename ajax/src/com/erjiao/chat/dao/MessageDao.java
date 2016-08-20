package com.erjiao.chat.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.erjiao.chat.bean.Message;


public class MessageDao extends BaseDao<Message>{
	
	//�������¼��Ϣ���浽���ݿ���
	public void saveMessage(Message message) {
		String sql = "INSERT INTO MESSAGE (MESSAGE_ID ,MESSAGE_CONTENT,MESSAGE_TIME) VALUES(message_seq.nextval,?,?)";
		this.update(sql, message.getMessage(), new Timestamp(message.getMessageTime().getTime()));
	}
	
	//�������������ı���������ϢID��ѯ�ȱ�����Ϣ��Ҫ�µ������¼
	public List<Message> getNewMessage(String finalMessageId) {
		String sql = "SELECT MESSAGE_ID messageId, MESSAGE_CONTENT message,MESSAGE_TIME messageTime FROM message WHERE MESSAGE_ID>? ORDER BY MESSAGE_ID";
		return this.getBeanList(sql, finalMessageId);
	}
	
	//�������������ı���������ϢID��ѯ�Ƿ�����µ������¼
	public boolean hasNew(String finalMessageId) {
		String sql = "SELECT COUNT(0) FROM message WHERE MESSAGE_ID>?";
		long count = ((BigDecimal)this.getSingleValue(sql, finalMessageId)).longValue();
		return count > 0;
	}

}
