package com.util;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONTokener;

public class JSONObject extends org.json.JSONObject
{
	
	@Override
	public Object get(String key) throws JSONException {
		if(super.isNull(key))
		{
			return null;
		}
		return super.get(key);
	}

	public JSONObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JSONObject(org.json.JSONObject arg0, String[] arg1)
			throws JSONException {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public JSONObject(JSONTokener x) throws JSONException {
		super(x);
		// TODO Auto-generated constructor stub
	}

	public JSONObject(Map map) {
		super(map);
		// TODO Auto-generated constructor stub
	}

	public JSONObject(String string) throws JSONException {
		super(string);
		// TODO Auto-generated constructor stub
	}
} 

