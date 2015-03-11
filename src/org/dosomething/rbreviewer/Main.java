//  RBReviewer
//
//  Created by Jared Arms on 3/9/15.
//  Copyright (c) 2015 DoSomething.org. All rights reserved.

package org.dosomething.rbreviewer;

import org.dosomething.rbreviewer.Main;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends ListActivity {
	
/** Called when the activity is first created. */
@Override

public void onCreate(Bundle savedInstancesState){
super.onCreate(savedInstancesState);

String[] attraction={
"Animals 2",
"Bullying 0",
"Disasters 0",
"Discrimination 0",
"Education 4",
"Environment 4",
"Homelessnes 1",
"Mental Health 2",
"Physical Health 0",
"Poverty 8",
"Relationships 0",
"Sex 0",
"Test Issue 0",
"Violence 0"

};

setListAdapter(new ArrayAdapter<String>(this,
android.R.layout.simple_list_item_1, attraction));
}

protected void onListItemClick(ListView l, View v, int position, long id){

switch(position){

case 0:
startActivity(new Intent(Main.this, Cp1.class));
break;

case 1:
startActivity(new Intent(Main.this, Cp2.class));
break;

case 2:
startActivity(new Intent(Main.this, Cp3.class));
break;

case 3:
startActivity(new Intent(Main.this, Cp4.class));
break;

case 4:
startActivity(new Intent(Main.this, Cp5.class));
break;

case 5:
startActivity(new Intent(Main.this, Cp6.class));
break;

case 6:
startActivity(new Intent(Main.this, Cp7.class));
break;

case 7:
startActivity(new Intent(Main.this, Cp8.class));
break;

case 8:
startActivity(new Intent(Main.this, Cp9.class));
break;

case 9:
startActivity(new Intent(Main.this, Cp10.class));
break;

}	
}
}