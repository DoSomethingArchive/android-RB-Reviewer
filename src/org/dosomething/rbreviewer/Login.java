//  RBReviewer
//
//  Created by Jared Arms on 3/9/15.
//  Copyright (c) 2015 DoSomething.org. All rights reserved.

package org.dosomething.rbreviewer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	
	private EditText emailAddressView;
	private EditText passwordView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		emailAddressView =(EditText)findViewById(R.id.email_address);
		passwordView =(EditText)findViewById(R.id.password);
		Button b=(Button)findViewById(R.id.btnLogin);
		
		
		b.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(Login.this, Main.class));
			
			Log.v("Login", emailAddressView.getText().toString() + " " + passwordView.getText().toString());
			
			
			
			}
		});
	}	
}
