package org.dosomething.rbreviewer.activity;

import org.dosomething.rbreviewer.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReportBackDetail extends Activity {

	DisplayImageOptions options;
	ImageLoader imageLoader = ImageLoader.getInstance();

	private TextView titleTextView;
	private ImageView rbr_image;
	private TextView captionTextView;
	private TextView quantityTextView;
	private TextView whyParticipatedTextView;
	
	private String title;
	private String rbrImgSrc;
	private String caption;
	private String quantity;
	private String whyParticipated;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstancesState) {
		super.onCreate(savedInstancesState);
		setContentView(R.layout.report_back_detail_activity);
		ActionBar ab = getActionBar();
		ab.setHomeButtonEnabled(true);
		ab.show();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		titleTextView = (TextView) findViewById(R.id.titleTextView);
		rbr_image = (ImageView) findViewById(R.id.rbr_image);
		captionTextView = (TextView) findViewById(R.id.captionTextView);
		quantityTextView = (TextView) findViewById(R.id.quantityTextView);
		whyParticipatedTextView = (TextView) findViewById(R.id.whyParticipatedTextView);
		
		
		Intent getPreviousIntent = getIntent();
		title = getPreviousIntent.getStringExtra("title");
		rbrImgSrc =  getPreviousIntent.getStringExtra("imgsrc");
		caption= getPreviousIntent.getStringExtra("caption");
		quantity = getPreviousIntent.getStringExtra("quantity");
		whyParticipated = getPreviousIntent.getStringExtra("why_participated");
		
		titleTextView.setText(title);
		captionTextView.setText(caption);
		quantityTextView.setText(quantity);
		whyParticipatedTextView.setText(whyParticipated);
		imageLoader.displayImage(rbrImgSrc, rbr_image,
				options);
	

	}

}
