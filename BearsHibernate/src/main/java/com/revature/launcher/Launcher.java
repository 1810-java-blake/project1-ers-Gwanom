package com.revature.launcher;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.revature.model.Bear;
import com.revature.util.SessionUtil;

public class Launcher {
	
	private SessionFactory sessionFactory = SessionUtil.getSessionFactory();
	
	public static void main(String[] args) {
		Launcher l = new Launcher();
		System.out.println(l.findAllBears());
	}
	
	public List<Bear> findAllBears() {
		Session s = sessionFactory.openSession();
		Criteria criteria = s.createCriteria(Bear.class);
		List<Bear> bears = criteria.list();
		return bears;
	}
}
