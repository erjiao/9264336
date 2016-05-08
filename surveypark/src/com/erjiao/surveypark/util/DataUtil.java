package com.erjiao.surveypark.util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.erjiao.surveypark.model.BaseEntity;

/**
 * ���ݹ�����
 * @author erjiao
 *
 */
public class DataUtil {

	/**
	 * ʹ�� md5 �㷨���м���
	 * @param src
	 * @return
	 */
	public static String md5(String src) {

		try {
			StringBuffer buffer = new StringBuffer();
			char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'A', 'B', 'C', 'D', 'E', 'F' };
			byte[] bytes = src.getBytes();

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] target = md.digest(bytes);

			for (byte b : target) {
				buffer.append(chars[b >> 4 & 0X0F]);
				buffer.append(chars[b & 0X0F]);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȸ���, ���Ƶ���������ͼ 
	 */
	public static Serializable deeplyCopy(Serializable src) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(src);
			oos.close();
			baos.close();
			
			byte[] bytes = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Serializable copy = (Serializable) ois.readObject();
			ois.close();
			bais.close();
			return copy;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȡʵ���id, �γ��ַ���
	 */
	public static String extractEntityIds(Set<? extends BaseEntity> entites) {
		String temp = "";
		if (ValidateUtil.isValid(entites)) {
			for (BaseEntity e : entites) {
				temp = temp + e.getId() + ",";
			}
			return temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

}
