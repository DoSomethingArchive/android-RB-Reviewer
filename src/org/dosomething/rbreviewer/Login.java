//  RBReviewer
//
//  Created by Jared Arms on 3/9/15.
//  Copyright (c) 2015 DoSomething.org. All rights reserved.

package org.dosomething.rbreviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Login extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onCreate(savedInstanceState);
setContentView(R.layout.login);
Button b=(Button)findViewById(R.id.btnLogin);

b.setOnClickListener(new OnClickListener() {
	
@Override
public void onClick(View arg0) {
// TODO Auto-generated method stub
startActivity(new Intent(Login.this, Main.class));
	
	
	
}
});
}	
}
