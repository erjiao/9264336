package com.erjiao.chat.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
public class MyDateConvert implements Converter{
    public Object convert(Class type, Object value){
    	System.out.println("-------------------------convert");
        if(value == null){
            return null;
        }else if(type == Timestamp.class){
            return convertToDate(type, value, "yyyyMMddHH:mm:ss");
        }else if(type == Date.class){
            return convertToDate(type, value, "yyyy-MM-dd");
        }else if(type == String.class){
            return convertToString(type, value);
        }

        throw new ConversionException("����ת�� " + value.getClass().getName() + " Ϊ " + type.getName());
    }

    protected Object convertToDate(Class type, Object value, String pattern) {
    	System.out.println("------------------------convertToDate");
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if(value instanceof String){
            try{
                Date date = sdf.parse((String) value);
                if(type.equals(Timestamp.class)){
                    return new Timestamp(date.getTime());
                }
                return date;
            }catch(Exception pe){
                return null;
            }
        }else if(value instanceof Date){
        	return value;
        }
        
        throw new ConversionException("����ת�� " + value.getClass().getName() + " Ϊ " + type.getName());
    }

    protected Object convertToString(Class type, Object value) {
    	System.out.println("------------------------convertToString");
        if(value instanceof Date){
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	
            if (value instanceof Timestamp) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } 
            
            try{
                return sdf.format(value);
            }catch(Exception e){
                throw new ConversionException("����ת��Ϊ�ַ���ʱ����");
            }
        }else{
            return value.toString();
        }
    }
}