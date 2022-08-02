package com.dragon.crm.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionUtil {
	
	private SqlSessionUtil(){}
	
	private static SqlSessionFactory factory;
	
	static{
		
		String resource = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			factory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
	}
	
	private static ThreadLocal<SqlSession> t = new ThreadLocal<SqlSession>();
	
	public static SqlSession getSqlSession(){
		
		SqlSession session = t.get();
		
		if(session==null){
			
			session = factory.openSession();
			t.set(session);
		}
		
		return session;
		
	}
	
	public static void myClose(SqlSession session){
		
		if(session!=null){
			session.close();
			t.remove();
		}
		
	}
	
	
}
