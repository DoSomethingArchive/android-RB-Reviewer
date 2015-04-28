package org.dosomething.rbreviewer.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.dosomething.rbreviewer.R;
import org.dosomething.rbreviewer.app.AppController;
import org.dosomething.rbreviewer.model.ReportBacksModel;
import org.dosomething.rbreviewer.utils.ConnectionDetector;
import org.dosomething.rbreviewer.utils.ExpandableTextView;
import org.dosomething.rbreviewer.utils.Store_pref;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TermsDetailActivity extends Activity {

	private String title;
	private String desc;
	private String tid;

	private String url = "https://www.dosomething.org/api/v1/terms/";
	private String rbrURL = "https://www.dosomething.org/api/v1/reportback_files/";

	private static String TAG = TermsDetailActivity.class.getSimpleName();

	// Progress dialog
	private ProgressDialog pDialog;

	// temporary string to show the parsed response
	private String jsonResponse;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	private Store_pref mStore_pref;

	private String sessionID;
	private String sessionName;
	private String userID;
	private String token;

	private ArrayList<ReportBacksModel> reportBacksModelsArrayList = new ArrayList<ReportBacksModel>();
	ReportBacksModel reportBacksModel = new ReportBacksModel();

	DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();

	int windowwidth;
	int screenCenter;
	int screenHeightCenter;
	int x_cord, y_cord, x, y;
	int Likes = 0;
	RelativeLayout parentView;
	float alphaValue = 0;
	private Context m_context;

	private static final int INVALID_POINTER_ID = -1;

	// The active pointer is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;
	private float aPosX;
	private float aPosY;
	private float aLastTouchX;
	private float aLastTouchY;
	float alphacount = 0;

	public static int topViewIndes = 0;

	TextView swipeFunctionTextView;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		setContentView(R.layout.terms_detail_actitivty);

		ActionBar ab = getActionBar();
		ab.setHomeButtonEnabled(true);
		ab.show();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		m_context = TermsDetailActivity.this;

		parentView = (RelativeLayout) findViewById(R.id.layoutview);
		swipeFunctionTextView = (TextView) parentView
				.findViewById(R.id.swipeFunctionTextView);

		imageLoader = ImageLoader.getInstance();

		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		desc = intent.getStringExtra("desc");
		tid = intent.getStringExtra("tid");

		Log.i("tid :", tid + " value of tid");

		mStore_pref = new Store_pref(TermsDetailActivity.this);

		sessionID = mStore_pref.get_user_sessid();
		sessionName = mStore_pref.get_session_name();
		userID = mStore_pref.get_UserId();
		token = mStore_pref.get_user_token();

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		// pDialog.setCancelable(false);

		makeJsonArrayRequest();

		initRBRView();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Method to make json array request where response starts with [
	 * */
	private void makeJsonArrayRequest() {

		showpDialog();

		JsonArrayRequest req = new JsonArrayRequest(url + tid
				+ "/inbox.json?count=1", new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				Log.d(TAG, response.toString() + "is response");

				try {
					if (response.length() > 0) {
						jsonResponse = "";
						reportBacksModelsArrayList = new ArrayList<ReportBacksModel>();
						for (int i = 0; i < response.length(); i++) {
							reportBacksModel = new ReportBacksModel();
							JSONObject rbr = (JSONObject) response.get(i);
							if (rbr != null) {
								if (rbr.has("fid")) {
									reportBacksModel.setFid(rbr
											.getString("fid"));
								}
								if (rbr.has("caption")) {
									reportBacksModel.setCaption(rbr
											.getString("caption"));
								}
								if (rbr.has("rbid")) {
									reportBacksModel.setRbid(rbr
											.getString("rbid"));
								}

								if (rbr.has("status")) {
									reportBacksModel.setStatus(rbr
											.getString("status"));
								}

								if (rbr.has("quantity")) {
									reportBacksModel.setQuantity(rbr
											.getString("quantity"));
								}

								if (rbr.has("uid")) {
									reportBacksModel.setUid(rbr
											.getString("uid"));
								}

								if (rbr.has("why_participated")) {
									reportBacksModel.setWhy_participated(rbr
											.getString("why_participated"));
								}

								if (rbr.has("nid")) {
									reportBacksModel.setNid(rbr
											.getString("nid"));
								}

								if (rbr.has("title")) {
									reportBacksModel.setTitle(rbr
											.getString("title"));
								}

								if (rbr.has("timestamp")) {
									reportBacksModel.setTimestamp(rbr
											.getString("timestamp"));
								}

								if (rbr.has("uri")) {
									reportBacksModel.setUri(rbr
											.getString("uri"));
								}

								if (rbr.has("quantity_label")) {
									reportBacksModel.setQuantity_label(rbr
											.getString("quantity_label"));
								}
								if (rbr.has("src")) {
									reportBacksModel.setSrc(rbr
											.getString("src"));
								}

								reportBacksModelsArrayList
										.add(reportBacksModel);
								Log.i("Img path", reportBacksModel.getSrc()
										+ "");

							} else {
								Toast.makeText(TermsDetailActivity.this,
										"No Result", Toast.LENGTH_SHORT);
							}

						}
						Log.i("Array size", reportBacksModelsArrayList.size()
								+ "size of response");
						if (reportBacksModelsArrayList.size() > 0) {
							initRBRView();
						} else {
							Toast.makeText(TermsDetailActivity.this,
									"No Result", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(TermsDetailActivity.this, "No Result",
								Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();

					Toast.makeText(getApplicationContext(),
							"Error: " + e.getMessage(), Toast.LENGTH_LONG)
							.show();
					Toast.makeText(TermsDetailActivity.this, "No Result",
							Toast.LENGTH_SHORT).show();
				}

				hidepDialog();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(), error.getMessage(),
						Toast.LENGTH_SHORT).show();
				Toast.makeText(TermsDetailActivity.this, "No Result",
						Toast.LENGTH_SHORT).show();
				hidepDialog();
			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("X-CSRF-Token", token);
				headers.put("Cookie", sessionName + "=" + sessionID);
				return headers;
			}
		};
		;

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(req);
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	@SuppressLint("NewApi")
	public void initRBRView() {

		windowwidth = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		screenCenter = windowwidth / 2;
		screenHeightCenter = height / 2;

		Log.i("size of reportback", reportBacksModelsArrayList.size() + "");

		topViewIndes = reportBacksModelsArrayList.size();

		swipeFunctionTextView.bringToFront();
		swipeFunctionTextView.setVisibility(View.GONE);

		for (int i = 0; i < reportBacksModelsArrayList.size(); i++) {
			LayoutInflater inflate = (LayoutInflater) m_context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			reportBacksModel = reportBacksModelsArrayList.get(i);

			final View m_view = inflate.inflate(R.layout.custom_rbr_layout,
					null);
			m_view.setLayoutParams(new LayoutParams((windowwidth - 40),
					LayoutParams.MATCH_PARENT));

			final ImageView rbr_image = (ImageView) m_view
					.findViewById(R.id.rbr_image);
			final TextView titleTextView = (TextView) m_view
					.findViewById(R.id.titleTextView);
			final TextView captionTextView = (TextView) m_view
					.findViewById(R.id.captionTextView);
			final TextView quantityTextView = (TextView) m_view
					.findViewById(R.id.quantityTextView);
			final ExpandableTextView whyParticipatedTextView = (ExpandableTextView) m_view
					.findViewById(R.id.whyParticipatedTextView);

			final ImageView flagStampImageView = (ImageView) m_view
					.findViewById(R.id.flagStampImageView);
			flagStampImageView.setVisibility(View.INVISIBLE);

			final ImageView approveStampImageView = (ImageView) m_view
					.findViewById(R.id.approveStampImageView);
			approveStampImageView.setVisibility(View.INVISIBLE);

			final ImageView rejectStampImageView = (ImageView) m_view
					.findViewById(R.id.rejectStampImageView);
			rejectStampImageView.setVisibility(View.INVISIBLE);

			final ImageView promoteStampImageView = (ImageView) m_view
					.findViewById(R.id.promoteStampImageView);

			promoteStampImageView.setVisibility(View.INVISIBLE);
			FrameLayout m_topLayout = (FrameLayout) m_view
					.findViewById(R.id.mainFrameLayout);


			int Measuredheight = 0;
			Point size = new Point();
			WindowManager w = getWindowManager();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				w.getDefaultDisplay().getSize(size);
				Measuredheight = size.y;
			} else {
				Display d = w.getDefaultDisplay();
				Measuredheight = d.getHeight();
			}
			
			
			whyParticipatedTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent rebortBackDetail = new Intent(TermsDetailActivity.this, ReportBackDetail.class);
				    rebortBackDetail.putExtra("title", reportBacksModel.getTitle());
				    rebortBackDetail.putExtra("caption", reportBacksModel.getCaption());
				    rebortBackDetail.putExtra("quantity", reportBacksModel.getQuantity() + " "
							+ reportBacksModel.getQuantity_label());
				    rebortBackDetail.putExtra("why_participated",reportBacksModel
							.getWhy_participated());
				    rebortBackDetail.putExtra("imgsrc", reportBacksModel.getSrc());
				    startActivity(rebortBackDetail);
				}
			});
			


			imageLoader.displayImage(reportBacksModel.getSrc(), rbr_image,
					options);
			Log.i("Image URL", reportBacksModel.getSrc());
			titleTextView.setText(reportBacksModel.getTitle());
			captionTextView.setText(reportBacksModel.getCaption());
			quantityTextView.setText(reportBacksModel.getQuantity() + " "
					+ reportBacksModel.getQuantity_label());
			whyParticipatedTextView.setText(reportBacksModel
					.getWhy_participated());

			

			// ADD dynamically like button on image.
			final Button imageLike = new Button(m_context);
			imageLike.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, 100));
			imageLike.setBackgroundResource(R.drawable.approved_stamp);

			imageLike.setAlpha(alphaValue);

			// ADD dynamically dislike button on image.
			final Button imagePass = new Button(m_context);
			imagePass.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, 100));
			imagePass.setBackgroundResource(R.drawable.rejected_stamp);
	
			imagePass.setAlpha(alphaValue);

			final Button imagePromote = new Button(m_context);
			imagePromote.setLayoutParams(new LayoutParams(300, 100));
			imagePromote.setBackgroundResource(R.drawable.promote_stamp);
			imagePromote.setGravity(Gravity.BOTTOM);
			imagePromote.setAlpha(alphaValue);

			final Button imageFlag = new Button(m_context);
			imageFlag.setLayoutParams(new LayoutParams(300, 100));
			imageFlag.setBackgroundResource(R.drawable.flag_stamp);
			imageFlag.setAlpha(alphaValue);
			imageFlag.setGravity(Gravity.BOTTOM);

			final RelativeLayout myRelView;
			myRelView = new RelativeLayout(TermsDetailActivity.this);
			myRelView.setLayoutParams(new LayoutParams((windowwidth - 60),
					LayoutParams.WRAP_CONTENT - 60));
			myRelView.setX(20);
			myRelView.setY(20);
			myRelView.setTag(i);
			myRelView.addView(m_view);

			// Touch listener on the image layout to swipe image right or left.
			myRelView.setOnTouchListener(new OnTouchListener() {
				float initialX, initialY;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					x_cord = (int) event.getRawX();
					y_cord = (int) event.getRawY();

					swipeFunctionTextView.bringToFront();

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						swipeFunctionTextView.setVisibility(View.VISIBLE);
						swipeFunctionTextView.bringToFront();
						x = (int) event.getX();
						y = (int) event.getY();

						initialX = event.getX();
						initialY = event.getY();

						alphacount = 0;
						mActivePointerId = event.getPointerId(0);
						final float x = event.getX(mActivePointerId);
						final float y = event.getY(mActivePointerId);
						// Remember where we started
						aLastTouchX = x;
						aLastTouchY = y;
						// to prevent an initial jump of the magnifier, aposX
						// and aPosY must
						// have the values from the magnifier frame
						if (aPosX == 0) {
							aPosX = myRelView.getX();
						}
						if (aPosY == 0) {
							aPosY = myRelView.getY();
						}

						Log.v("On touch", x + " " + y);

						break;
					}
					case MotionEvent.ACTION_MOVE: {
						swipeFunctionTextView.setVisibility(View.VISIBLE);
						swipeFunctionTextView.bringToFront();
						x_cord = (int) event.getRawX(); // Updated for more
														// smoother animation.
						y_cord = (int) event.getRawY();

						// Find the index of the active pointer and fetch its
						// position
						final int pointerIndex = MotionEventCompat
								.findPointerIndex(event, mActivePointerId);

						final float x = MotionEventCompat.getX(event,
								pointerIndex);
						final float y = MotionEventCompat.getY(event,
								pointerIndex);

						// Calculate the distance moved
						final float dx = x - aLastTouchX;
						final float dy = y - aLastTouchY;

						aPosX += dx;
						aPosY += dy;

						myRelView.invalidate();

						myRelView.setX(aPosX);
						myRelView.setY(aPosY);

						if (x_cord >= screenCenter) {
							myRelView
									.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 90)));
							if (x_cord > (screenCenter + (screenCenter / 2))) {
								imageLike.setAlpha(1);
								swipeFunctionTextView.setText("Approve");
								swipeFunctionTextView
										.setBackgroundResource(R.drawable.approved_bg);
								if (x_cord > (windowwidth - (screenCenter / 2))) {
									Likes = 2;
								} else {
									if (screenHeightCenter < y_cord) {
										Log.d("Event Status inside like 2",
												"Up to Down swipe performed");
										swipeFunctionTextView.setText("Flag");
										swipeFunctionTextView
												.setBackgroundResource(R.drawable.flag_bg);
										imageFlag.setAlpha(1);
										imagePromote.setAlpha(0);
										imageLike.setAlpha(0);
										imagePass.setAlpha(0);
									}

									if (screenHeightCenter > y_cord) {
										Log.d("Event Status inside like 2",
												"Down to Up swipe performed");
										imagePromote.setAlpha(1);
										swipeFunctionTextView
												.setText("Promote");
										swipeFunctionTextView
												.setBackgroundResource(R.drawable.promote_bg);
										imageFlag.setAlpha(0);
										imageLike.setAlpha(0);
										imagePass.setAlpha(0);
									}
									Likes = 0;
								}
							} else {
								if (screenHeightCenter < y_cord) {
									Log.d("Event Status inside else like 2",
											"Up to Down swipe performed");
									imageFlag.setAlpha(1);
									swipeFunctionTextView.setText("Flag");
									swipeFunctionTextView
											.setBackgroundResource(R.drawable.flag_bg);
									imagePromote.setAlpha(0);
									imageLike.setAlpha(0);
									imagePass.setAlpha(0);
								}

								if (screenHeightCenter > y_cord) {
									Log.d("Event Status else inside like 2",
											"Down to Up swipe performed");
									// Likes=3;
									imagePromote.setAlpha(1);
									swipeFunctionTextView.setText("Promote");
									swipeFunctionTextView
											.setBackgroundResource(R.drawable.promote_bg);
									imageFlag.setAlpha(0);
									imageLike.setAlpha(0);
									imagePass.setAlpha(0);
								}
								Likes = 0;
								imageLike.setAlpha(0);
								approveStampImageView
										.setVisibility(View.INVISIBLE);
							}
							imagePass.setAlpha(0);
							rejectStampImageView.setVisibility(View.INVISIBLE);
						} else {
							// rotate
							myRelView
									.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 90)));
							if (x_cord < (screenCenter / 2)) {
								imagePass.setAlpha(1);
								swipeFunctionTextView.setText("Exclude");
								swipeFunctionTextView
										.setBackgroundResource(R.drawable.rejected_bg);
								if (x_cord < screenCenter / 4) {
									Likes = 1;
								} else {
									if (initialY < y_cord) {
										Log.d("Event Status inside like 1",
												"Up to Down swipe performed");
										imageFlag.setAlpha(1);
										imagePromote.setAlpha(0);
										imageLike.setAlpha(0);
										imagePass.setAlpha(0);
										
									}

									if (initialY > y_cord) {
										Log.d("Event Status inside like 1",
												"Down to Up swipe performed");
										
										imagePromote.setAlpha(1);
										promoteStampImageView
												.setVisibility(View.INVISIBLE);

										imageFlag.setAlpha(0);
										imageLike.setAlpha(0);
										imagePass.setAlpha(0);
									}
									Likes = 0;
								}
							} else {
								if (initialY < y_cord) {
									Log.d("Event Status inside else like 1",
											"Up to Down swipe performed");
									imageFlag.setAlpha(1);

									imagePromote.setAlpha(0);
									imageLike.setAlpha(0);
									imagePass.setAlpha(0);
								}

								if (initialY > y_cord) {
									Log.d("Event Status  inside else like 1",
											"Down to Up swipe performed");
									imagePromote.setAlpha(1);
									imageFlag.setAlpha(0);
									imageLike.setAlpha(0);
									imagePass.setAlpha(0);
								}
								Likes = 0;
								imagePass.setAlpha(0);
								rejectStampImageView
										.setVisibility(View.INVISIBLE);
							}
							imageLike.setAlpha(0);
							approveStampImageView.setVisibility(View.INVISIBLE);
						}

						break;
					}
					case MotionEvent.ACTION_UP: {
						x_cord = (int) event.getRawX();
						y_cord = (int) event.getRawY();
						swipeFunctionTextView.setVisibility(View.GONE);
						swipeFunctionTextView.bringToFront();

						Log.e("X Point", "" + x_cord + " , Y " + y_cord);
						imagePass.setAlpha(0);
						imageLike.setAlpha(0);
						approveStampImageView.setVisibility(View.INVISIBLE);
						rejectStampImageView.setVisibility(View.INVISIBLE);
						flagStampImageView.setVisibility(View.INVISIBLE);
						promoteStampImageView.setVisibility(View.INVISIBLE);

						if (Likes == 0) {
							myRelView.setX(20);
							myRelView.setY(20);
							myRelView.setRotation(0);

							if (screenHeightCenter < y_cord) {
								Log.d("Event Status",
										"Up to Down swipe performed");
								Likes = 4;
							}

							if (screenHeightCenter > y_cord) {
								Log.d("Event Status",
										"Down to Up swipe performed");
								Likes = 3;
							}

							if (Likes == 3) {

								Log.e("Event Status", "Promote");

								myRelView.setX(40);
								myRelView.setY(40);
								myRelView.setRotation(0);
								confirmActionDialog("PROMOTE");

							} else if (Likes == 4) {

								Log.e("Event Status", "FLAG");
								showFlagDialog();

							}

						} else if (Likes == 1) {
							Log.e("Event Status", "Passed");
							myRelView.setX(40);
							myRelView.setY(40);
							myRelView.setRotation(0);
							confirmActionDialog("EXCLUDE");

						} else if (Likes == 2) {

							Log.e("Event Status", "Liked");
							myRelView.setX(40);
							myRelView.setY(40);
							myRelView.setRotation(0);
							confirmActionDialog("APPROVE");
						}

						break;
					}
					default:
						break;

					}
					return true;
				}
			});

			parentView.addView(myRelView);

		}
	}

	AlertDialog flagDialog;

	public void showFlagDialog() {

		// custom dialog
		final Dialog dialog = new Dialog(TermsDetailActivity.this);
		dialog.setContentView(R.layout.custom_flag_dialog);
		dialog.setTitle("Select Reason of Flagging");

		final RadioGroup flagRadioGroup = (RadioGroup) dialog
				.findViewById(R.id.flagRadioGroup);

		Button dialogButtonOk = (Button) dialog
				.findViewById(R.id.dialogButtonOK);
		Button dialogButtonCancel = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				int selectedId = flagRadioGroup.getCheckedRadioButtonId();
				RadioButton selectedFlagReason = (RadioButton) dialog
						.findViewById(selectedId);

				Toast.makeText(TermsDetailActivity.this,
						selectedFlagReason.getText(), Toast.LENGTH_SHORT)
						.show();
				if (selectedFlagReason.getText().toString()
						.equalsIgnoreCase("Delete this Image")) {
					confirmDeleteDialog();
				} else {
					sendRBRStatus("flagged", true, selectedFlagReason.getText()
							.toString());

				}
			}
		});

		dialogButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	public void confirmDeleteDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				TermsDetailActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle("Confirm Delete...");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want delete this Report?");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						sendRBRStatus("flagged", true, "Delete this Report");

					}
				});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to invoke NO event

						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	public void confirmActionDialog(final String status) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				TermsDetailActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle("Confirm " + status + "...");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want " + status
				+ " this Report?");

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						switch (status) {
						case "APPROVE":
							sendRBRStatus("approved");
							break;
						case "EXCLUDE":
							sendRBRStatus("excluded");
							break;
						case "PROMOTE":
							sendRBRStatus("promoted");
							break;
						default:
							break;
						}

					}
				});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to invoke NO event

						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	/**
	 * Method to make json object request where response starts with {
	 * */
	private void sendRBRStatus(String status) {

		JSONObject object2 = new JSONObject();
		try {
			object2.put("status", status);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		showpDialog();

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				rbrURL
						+ reportBacksModelsArrayList.get(topViewIndes - 1)
								.getFid() + "/review", object2,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());

						try {
							parentView.removeViewAt(topViewIndes - 1);
							reportBacksModelsArrayList.remove(topViewIndes - 1);
							topViewIndes--;
							if (reportBacksModelsArrayList.size() == 0) {
								makeJsonArrayRequest();

							}
							String reviewed = response.getString("reviewed");
							String options_processed = response
									.getString("options_processed");
							String fid = response.getString("fid");
							String review_source = response
									.getString("review_source");
							String status = response.getString("status");
							String rbid = response.getString("rbid");
							String remote_addr = response
									.getString("remote_addr");
							String reviewer = response.getString("reviewer");
							String caption = response.getString("caption");
							String fid_processed = response
									.getString("fid_processed");
							Toast.makeText(getApplicationContext(),
									"Reportback Staus: " + "\n" + status,
									Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + "\n Submit RBR but error",
									Toast.LENGTH_LONG).show();
						}
						hidepDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								"Error: " + "\nCant send status",
								Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("X-CSRF-Token", token);
				headers.put("Cookie", sessionName + "=" + sessionID);
				return headers;
			}
		};
		;

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	/**
	 * Method to make json array request where response starts with [
	 * */
	private void sendRBRStatus(String status, boolean isDelete,
			String flagReason) {

		JSONObject object2 = new JSONObject();
		try {
			object2.put("status", status);
			object2.put("flagged_reason", status);
			if (isDelete) {
				object2.put("delete", 1);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		showpDialog();
		Log.i("URL", rbrURL
				+ reportBacksModelsArrayList.get(topViewIndes - 1).getFid()
				+ "/review");

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				rbrURL
						+ reportBacksModelsArrayList.get(topViewIndes - 1)
								.getFid() + "/review", object2,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, response.toString());
						try {
							parentView.removeViewAt(topViewIndes - 1);
							reportBacksModelsArrayList.remove(topViewIndes - 1);
							topViewIndes--;
							if (reportBacksModelsArrayList.size() == 0) {
								makeJsonArrayRequest();

							}
							String reviewed = response.getString("reviewed");
							String options_processed = response
									.getString("options_processed");
							String fid = response.getString("fid");
							String review_source = response
									.getString("review_source");
							String status = response.getString("status");
							String rbid = response.getString("rbid");
							String remote_addr = response
									.getString("remote_addr");
							String reviewer = response.getString("reviewer");
							String caption = response.getString("caption");
							String fid_processed = response
									.getString("fid_processed");
							Toast.makeText(getApplicationContext(),
									"Reportback Staus: " + "\n" + status,
									Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + "\n Submit RBR but error",
									Toast.LENGTH_LONG).show();
						}

						hidepDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								"Error: " + "\nCant send status",
								Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("X-CSRF-Token", token);
				headers.put("Cookie", sessionName + "=" + sessionID);
				return headers;
			}
		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}
}
