package org.dosomething.rbreviewer.activity;

import java.util.ArrayList;
import java.util.List;

import org.dosomething.rbreviewer.R;
import org.dosomething.rbreviewer.adpaters.TermsListAdapter;
import org.dosomething.rbreviewer.app.AppController;
import org.dosomething.rbreviewer.model.TermsModel;
import org.dosomething.rbreviewer.utils.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends Activity {

	// Movies json url
	private static final String url = "https://www.dosomething.org/api/v1/terms.json";
	private ProgressDialog pDialog;
	private List<TermsModel> termsList = new ArrayList<TermsModel>();
	private ListView termslistView;
	private TermsListAdapter termsAdapter;
	private String TAG = "TAG Main Activity";
	
	// flag for Internet connection status
		Boolean isInternetPresent = false;
		// Connection detector class
		ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		setContentView(R.layout.main_activity);
		ActionBar ab = getActionBar();
		ab.setHomeButtonEnabled(true);
		ab.show();

		termslistView = (ListView) findViewById(R.id.termsListView);
		termsAdapter = new TermsListAdapter(this, termsList);
		termslistView.setAdapter(termsAdapter);

		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		termslistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent termDetailActity = new Intent(MainActivity.this,
						TermsDetailActivity.class);
				TermsModel term = termsList.get(position);
				termDetailActity.putExtra("title", term.getName());
				termDetailActity.putExtra("desc", term.getDescription());
				termDetailActity.putExtra("tid", term.getTid());
				startActivity(termDetailActity);
			}
		});
		
		cd = new ConnectionDetector(MainActivity.this);
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {

		// Creating volley request obj
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();

						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								TermsModel terms = new TermsModel();
								terms.setTid(obj.getString("tid"));
								terms.setVid(obj.getString("vid"));
								terms.setName(obj.getString("name"));
								terms.setDescription(obj
										.getString("description"));
								terms.setFormat(obj.getString("format"));
								terms.setWeight(obj.getString("weight"));
								terms.setUuid(obj.getString("uuid"));
								terms.setDepth(obj.getString("depth"));
								if (obj.has("inbox")) {
									terms.setInbox(obj.getString("inbox"));
								}

								// adding movie to movies array
								termsList.add(terms);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						termsAdapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						hidePDialog();

					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(movieReq);
		}else{
			Toast.makeText(getApplicationContext(),
					"No Internet", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

}