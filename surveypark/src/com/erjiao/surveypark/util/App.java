package com.erjiao.surveypark.util;

import com.erjiao.surveypark.model.User;

public class App {
	
	public static void main(String[] args) throws Exception {
		
		User user = new User();
		user.setEmail("erjiao@qq.com");
		user.setId(33);
		user.setName("liukai");
		System.out.println(user.toString());
		
		System.out.println(LogUtil.generateLogTableName(0));
		System.out.println(LogUtil.generateLogTableName(1));
		System.out.println(LogUtil.generateLogTableName(2));
		System.out.println(LogUtil.generateLogTableName(-1));
		System.out.println(LogUtil.generateLogTableName(-2));
//		char[] chars = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
//		
//		String src = "abc";
//		byte[] bytes = src.getBytes();
//		MessageDigest md = MessageDigest.getInstance("MD5");
//		byte[] targ = md.digest(bytes);
//		StringBuffer buffer = new StringBuffer();
//		for (byte b : targ) {
//			buffer.append(chars[b >> 4 & 0X0F]);
//			buffer.append(chars[b & 0X0F]);
//		}
//		
//		System.out.println(buffer.toString());
		
//		Survey s1 = new Survey();
//		s1.setTitle("s1");
//		
//		Page p1 = new Page();
//		p1.setTitle("p1");
//		
//		Question q1 = new Question();
//		q1.setTitle("q1");
//		
//		Question q2 = new Question();
//		q2.setTitle("q2");
//		
//		p1.setSurvey(s1);
//		p1.getQuestions().add(q1);
//		p1.getQuestions().add(q2);
//		
//		//进行深度赋值.
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(baos);
//		oos.writeObject(p1);
//		oos.close();
//		baos.close();
//		
//		byte[] bytes = baos.toByteArray();
//		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//		ObjectInputStream ois = new ObjectInputStream(bais);
//		Page copy = (Page) ois.readObject();
//		ois.close();
//		bais.close();
//		
//		System.out.println(copy);
		
		for (int i = 0; i < 64; i ++) {
			System.out.println("i<<" + i + "==" + (1L << i));
		}
		
	}
	
}
