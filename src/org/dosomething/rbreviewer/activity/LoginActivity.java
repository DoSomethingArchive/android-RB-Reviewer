package org.dosomething.rbreviewer.activity;

import org.dosomething.rbreviewer.R;
import org.dosomething.rbreviewer.app.AppController;
import org.dosomething.rbreviewer.utils.ConnectionDetector;
import org.dosomething.rbreviewer.utils.JSONParser;
import org.dosomething.rbreviewer.utils.Store_pref;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class LoginActivity extends Activity {

	private EditText emailAddressView;
	private EditText passwordView;
	private Button loginButton;

	// json object response url
	private String urlJsonObj ="https://www.dosomething.org/api/v1/auth/login";

	private static String TAG = LoginActivity.class.getSimpleName();

	// Progress dialog
	private ProgressDialog pDialog;

	// temporary string to show the parsed response
	private String jsonResponse;

	private String userID;	
	private String password;

	private JSONParser jsonParser;

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	private Store_pref mStore_pref;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		ActionBar ab = getActionBar();
		ab.setHomeButtonEnabled(true);
		ab.show();

		emailAddressView = (EditText) findViewById(R.id.email_address);
		passwordView = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.btnLogin);

		mStore_pref = new Store_pref(LoginActivity.this);
		
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		cd = new ConnectionDetector(LoginActivity.this);
		isInternetPresent = cd.isConnectingToInternet();

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(Login.this, Main.class));

				userID = emailAddressView.getText().toString().trim();
				password = passwordView.getText().toString().trim();

				if (userID != null && !TextUtils.isEmpty(userID)) {
					if (password != null && !TextUtils.isEmpty(password)) {
						Log.v("Login", emailAddressView.getText().toString()
								+ " " + passwordView.getText().toString());
						isInternetPresent = cd.isConnectingToInternet();
						if (isInternetPresent) {
							makeJsonObjectRequest();
							// new YourAsyncTaskLogin().execute();
						} else {
							Toast.makeText(getApplicationContext(),
									"No Internet", Toast.LENGTH_SHORT).show();
						}
					} else {
						passwordView.setError("Enter Password.");
						passwordView.requestFocus();
					}
				} else {
					emailAddressView.setError("Enter User ID.");
					emailAddressView.requestFocus();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Method to make json object request where json response starts wtih {
	 * */
	private void makeJsonObjectRequest() {

		showpDialog();
		JSONObject object2 = new JSONObject();
		try {
			object2.put("username", userID);
			object2.put("password", password);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, object2, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());

						try {
							// Parsing json object response
							// sessid will be a json object
							String sessid = response.getString("sessid");
							String session_name = response
									.getString("session_name");
							String token = response
									.getString("token");
							JSONObject user = response.getJSONObject("user");
							String uid = user.getString("uid");
							String name = user.getString("name");
							
							mStore_pref.set_session_name(session_name);
							mStore_pref.set_user_sessid(sessid);
							mStore_pref.set_UserId(uid);
							mStore_pref.set_UserName(name);
							mStore_pref.set_user_token(token);

							jsonResponse = "";
							jsonResponse += "sessid: " + sessid + "\n\n";
							jsonResponse += "session_name: " + session_name
									+ "\n\n";
							jsonResponse += "uid: " + uid + "\n\n";
							jsonResponse += "Mobile: " + name + "\n\n";

							Log.i(TAG, jsonResponse);
							if (sessid != null && session_name != null) {
								if (!TextUtils.isEmpty(sessid)
										&& !TextUtils.isEmpty(session_name)) {
									
									emailAddressView.setText("");
									passwordView.setText("");
									Intent mainActivity = new Intent(
											LoginActivity.this,
											MainActivity.class);
									mainActivity.putExtra("sessid", sessid);
									mainActivity.putExtra("session_name",
											session_name);
									mainActivity.putExtra("uid", uid);
									mainActivity.putExtra("name", name);
									startActivity(mainActivity);
									

								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + "\nInvalid Login",
									Toast.LENGTH_LONG).show();
						}
						hidepDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								"Error: " +"\nInvalid Login", Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	class YourAsyncTaskLogin extends AsyncTask<Void, Void, Void> {

		private ProgressDialog _ProgressDialog;

		@Override
		protected void onPreExecute() {
			// show your dialog here

			_ProgressDialog = ProgressDialog.show(LoginActivity.this, "",
					"Loading", true);

		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject object2 = new JSONObject();
			try {
				object2.put("username", userID);
				object2.put("password", password);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			jsonParser = new JSONParser();
			JSONObject jsonResponseObject = jsonParser.getJSONFromUrl(
					urlJsonObj, object2);
			Log.e("JSONaaaaaaaaaaaaaaaa", jsonResponseObject.toString());
			return null;
		}

		protected void onPostExecute(Void result) {
			_ProgressDialog.dismiss();
		}
	}
}
