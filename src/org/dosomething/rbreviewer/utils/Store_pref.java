package org.dosomething.rbreviewer.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Store_pref {

	SharedPreferences pref_master;
	SharedPreferences.Editor editor_pref_master;
	Context c;

	public Store_pref(Context con) {
		c = con;
		pref_master = con.getSharedPreferences("RBR_pref", 0);
	}

	public void open_editor() {
		// TODO Auto-generated method stub
		editor_pref_master = pref_master.edit();
	}

	public void set_user_sessid(String sessid) {
		// TODO Auto-generated method stub
		open_editor();
		editor_pref_master.putString("user_sessid", sessid);
		editor_pref_master.commit();
	}

	public String get_user_sessid() {
		// TODO Auto-generated method stub
		return pref_master.getString("user_sessid", "");
	}

	public void set_user_token(String token) {
		// TODO Auto-generated method stub
		open_editor();
		editor_pref_master.putString("token", token);
		editor_pref_master.commit();
	}

	public String get_user_token() {
		// TODO Auto-generated method stub
		return pref_master.getString("token", "");
	}

	public void set_UserId(String UserId) {
		// TODO Auto-generated method stub
		open_editor();
		editor_pref_master.putString("UserId", UserId);
		editor_pref_master.commit();
	}

	public String get_UserId() {
		// TODO Auto-generated method stub
		return pref_master.getString("UserId", "");
	}
	
	public void set_UserName(String UserName) {
		// TODO Auto-generated method stub
		open_editor();
		editor_pref_master.putString("UserName", UserName);
		editor_pref_master.commit();
	}

	public String get_UserName() {
		// TODO Auto-generated method stub
		return pref_master.getString("UserName", "");
	}
	
	public void set_session_name(String session_name) {
		// TODO Auto-generated method stub
		open_editor();
		editor_pref_master.putString("session_name", session_name);
		editor_pref_master.commit();
	}

	public String get_session_name() {
		// TODO Auto-generated method stub
		return pref_master.getString("session_name", "");
	}

	

	public void Clear_data() {
		open_editor();
		editor_pref_master.putString("UserId", "");
		editor_pref_master.putString("user_sessid", "");
		editor_pref_master.putString("token", "");
		editor_pref_master.putString("session_name", "");
		editor_pref_master.commit();
	}
}
