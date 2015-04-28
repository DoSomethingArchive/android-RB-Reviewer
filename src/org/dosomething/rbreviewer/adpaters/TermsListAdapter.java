package org.dosomething.rbreviewer.adpaters;

import java.util.List;

import org.dosomething.rbreviewer.R;
import org.dosomething.rbreviewer.model.TermsModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TermsListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<TermsModel> termsItems;

	public TermsListAdapter(Activity activity, List<TermsModel> termsItems) {
		this.activity = activity;
		this.termsItems = termsItems;
	}

	@Override
	public int getCount() {
		return termsItems.size();
	}

	@Override
	public Object getItem(int location) {
		return termsItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.terms_list_item, null);

		TextView title = (TextView) convertView
				.findViewById(R.id.titleTextView);
		TextView inbox = (TextView) convertView
				.findViewById(R.id.inboxCountTextView);

		// getting Terms data for the row
		TermsModel m = termsItems.get(position);

		// title
		title.setText(m.getName());

		// inbox
		inbox.setText(m.getInbox());

		return convertView;
	}

}
