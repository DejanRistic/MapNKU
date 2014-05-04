package com.fuzzydev.mapnku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavigationDrawerAdapter extends BaseAdapter {

	private Context mContext;
	private String[] mNavigationItems;
	private NavigationHolder holder;

	public NavigationDrawerAdapter(Context context, String[] navigationItems) {
		mNavigationItems = navigationItems;
		mContext = context;
	}

	@Override
	public Object getItem(int position) {
		return mNavigationItems[position];
	}

	@Override
	public long getItemId(int position) {
		return mNavigationItems[position].hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new NavigationHolder();

			convertView = LayoutInflater.from(mContext).inflate(
				R.layout.drawer_list_item, parent, false);

			holder.title = (TextView) convertView.findViewById(R.id.item_title);

			convertView.setTag(holder);
		} else {
			holder = (NavigationHolder) convertView.getTag();
		}

		holder.title.setText(mNavigationItems[position]);

		return convertView;
	}

	@Override
	public int getCount() {
		return mNavigationItems.length;
	}

	static class NavigationHolder {
		TextView title;
	}
}
