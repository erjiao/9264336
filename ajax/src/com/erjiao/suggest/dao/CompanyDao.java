package com.erjiao.suggest.dao;

import java.util.ArrayList;
import java.util.List;

import com.erjiao.suggest.bean.Company;

public class CompanyDao {
	
	private static final List<Company> COMPANY = new ArrayList<Company>();
	
	static {
		COMPANY.add(new Company(1, "�й������й�ȷ���"));
		COMPANY.add(new Company(2, "�й�ȹ��ʽ������ű����ֲ�"));
		COMPANY.add(new Company(3, "������Ͳ�������"));
		COMPANY.add(new Company(4, "�й�Ƚ����Ƽ��о�����"));
		COMPANY.add(new Company(5, "������ҵ"));
		COMPANY.add(new Company(6, "���Ĺ���������"));
		COMPANY.add(new Company(7, "������ʳ��"));
		COMPANY.add(new Company(8, "�й����Ӱ�ӳ�"));
		COMPANY.add(new Company(9, "�й��ͼ������"));
		COMPANY.add(new Company(10, "�й��Ӫ���ܲ�"));
		COMPANY.add(new Company(11, "Ajaxѧϰָ��"));
		COMPANY.add(new Company(12, "��Web��Ŀ��ʹ��Ajax"));
		COMPANY.add(new Company(13, "Ajax�첽����"));
		COMPANY.add(new Company(14, "͸��Ajax����"));
		COMPANY.add(new Company(15, "���Ajax�첽��������"));
		COMPANY.add(new Company(16, "jQuery������Ļ"));
		COMPANY.add(new Company(17, "������jQuery��ܽṹ"));
		COMPANY.add(new Company(18, "jQuery�е�JavaScript����"));
	}
	
	public static List<Company> queryList(String queryStr) {
		queryStr = queryStr.toLowerCase();
		List<Company> companyList = new ArrayList<Company>();
		
		for (Company company : COMPANY) {
			String companyName = company.getCompanyName().toLowerCase();
			boolean contains = companyName.contains(queryStr);
			if(contains) {
				companyList.add(company);
			}
		}
		
		return companyList;
	}
	
}
