//  RBReviewer
//
//  Created by Jared Arms on 3/9/15.
//  Copyright (c) 2015 DoSomething.org. All rights reserved.

package org.dosomething.rbreviewer;

import java.util.Timer;
import java.util.TimerTask;
import org.dosomething.rbreviewer.R;
import org.dosomething.rbreviewer.Splash;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Splash extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.splash);
TimerTask task = new TimerTask() {

@Override
public void run() {
// TODO Auto-generated method stub
finish();
startActivity(new Intent(Splash.this, Login.class));
}	
};
	
Timer opening = new Timer();
opening.schedule(task, 4000);
}
}

