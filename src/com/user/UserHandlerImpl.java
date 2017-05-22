package com.user;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.hibernate.HBSessionFacroty;
import com.util.Util;
import com.zoho.saml.SAMLConstants;

public class UserHandlerImpl implements UserHandler {
	public User getUserByEmail(String email)
	{
		Session session = HBSessionFacroty.getInstance().getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("email",email));
		List<User> usersList = criteria.list();
		
		session.getTransaction().commit();
		
		if(usersList!=null &&!usersList.isEmpty())
		{
			return usersList.get(0);
		}
		return null;
		
	}
	public User addNewSamlUser(com.zoho.saml.User user){
		/*
		 * Name is persisted in the DB - Making it an user editable prop
		 * Authtoken will be null - Authorise button will be displayed on the user home page
		 */
		User userInfo = new User();
		userInfo.setEmail(user.getEmail());
		userInfo.setName(user.getName());
		User checkDuplicate = getUserByEmail(userInfo.getEmail());
		if(checkDuplicate == null){
			userInfo= addNewUser(userInfo);
		}
		return userInfo;
	}
	public User addNewUser(User user)
	{
		/*
		 * Name is persisted in the DB - Making it an user editable prop
		 * Authtoken will be null - Authorise button will be displayed on the user home page
		 */
		 User userinfo = new User()
								.setEmail(user.getEmail())
								.setName(user.getEmail().replaceAll("@.*", ""));
		persistUserInfo(userinfo);
		return userinfo;
	}
	public User updateUser(User user)
	{
		Session session = HBSessionFacroty.getInstance().getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(user);

		session.getTransaction().commit();
		return user;
	}
	public Boolean deleteUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	private User persistUserInfo(User user)
	{
		Session session = HBSessionFacroty.getInstance().getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.persist(user);
		
		session.getTransaction().commit();
		return user;
	}
}

