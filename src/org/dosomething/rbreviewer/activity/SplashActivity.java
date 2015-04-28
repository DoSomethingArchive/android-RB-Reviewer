package org.dosomething.rbreviewer.activity;

import java.util.Timer;
import java.util.TimerTask;
import org.dosomething.rbreviewer.R;
import org.dosomething.rbreviewer.activity.SplashActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash_activity);
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
				finish();
			}
		};

		Timer opening = new Timer();
		opening.schedule(task, 4000);
	}
}
