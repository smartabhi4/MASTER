package com.user;


public interface UserHandler {
	User getUserByEmail(String email);
	User addNewUser(User user);
	User updateUser(User user);
	Boolean deleteUser(User user);
	User addNewSamlUser(com.zoho.saml.User user);
}

