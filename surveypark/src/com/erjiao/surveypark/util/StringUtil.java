package com.erjiao.surveypark.util;

/**
 * �ַ���������
 */
public class StringUtil {
	/**
	 * ���ַ���ת��Ϊ����.����tag �ָ�
	 */
	public static String[] str2Arr (String str, String tag) {
		if (ValidateUtil.isValid(str)) {
			return str.split(tag);
		}
		return null;
	}

	/**
	 * �ж���values �������Ƿ���ָ��value �ַ���
	 */
	public static boolean contains(String[] values, String value) {
		if (ValidateUtil.isValid(values)) {
			for (String s : values) {
				if (s.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ������任���ַ���, ʹ��"," �ָ�
	 */
	public static String arr2str(Object[] arr) {
		String temp = "";
		if (ValidateUtil.isValid(arr)) {
			for (Object s : arr) {
				temp = temp + s + ",";
			}
			return temp.substring(0, temp.length() - 1);
		}
		return temp;
	}
	
	/**
	 * ����ַ�����������Ϣ
	 */
	public static String getDescString(String str) {
		if (null != str && str.trim().length() > 30) {
			return str.substring(0, 30);
		}
		System.out.println(str);
		return str;
	}
	
}
