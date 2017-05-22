//$Id$
package com.hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/*
 * singleton HBSEssionFactory class
 * 
 * Made as singleton to limit the creation of unnecessary sessionFactory
 */
public class HBSessionFacroty {
	private static HBSessionFacroty instance = null;
	private SessionFactory sessionFactory = null;

	private HBSessionFacroty()
	{
		System.out.println("factory was instantiated");
		Configuration cf = new Configuration();
		cf.configure("com/hibernate/hibernate.cfg.xml");
		sessionFactory = cf.buildSessionFactory();
	}
	public static HBSessionFacroty getInstance()
	{
		if(instance==null)
		{	
			instance =new HBSessionFacroty(); 
		}
		return instance;
	}
	public SessionFactory getSessionFactory() 
	{
		return sessionFactory;
	}
}

