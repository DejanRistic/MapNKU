package com.fuzzydev.mapnku;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class NavigationDrawerAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mNavigationItems;
    private NavigationHolder holder;
    private Bundle mBundle;
    private int mCount;

    public NavigationDrawerAdapter(Context context, String[] navigationItems) {
        mNavigationItems = navigationItems;
        mContext = context;
        mCount = navigationItems.length;
    }

    public void update(Bundle bundle) {

        mBundle = bundle;
        if (mCount == mNavigationItems.length) {
            mCount = mCount + 1;
        }
        if (bundle == null && mCount > 1) {
            mCount = mCount - 1;
        }

        this.mNavigationItems = Arrays.copyOf(mNavigationItems, mNavigationItems.length);
        notifyDataSetChanged();
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

        if (mBundle != null && position != 0) {

            holder = new NavigationHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.drawer_list_item, parent, false);

            holder.imageView = (ImageView) convertView.findViewById(R.id.marker);

            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            holder.startAddress = (TextView) convertView.findViewById(R.id.start_address);
            holder.endAddress = (TextView) convertView.findViewById(R.id.end_address);
            convertView.setTag(holder);


            holder.title.setText(mBundle.getString("name"));
            holder.description.setText(mBundle.getString("description").isEmpty() ? "No description Available" : mBundle.getString("description"));
            holder.startAddress.setText(mBundle.getString("start_address"));
            holder.endAddress.setText(mBundle.getString("end_address"));
            holder.duration.setText("Duration: " + mBundle.getString("duration"));
            holder.distance.setText("Distance: " + mBundle.getString("distance"));

            return convertView;

        } else {
            if (mCount > 1) {
                return new View(mContext);
            } else{
                return LayoutInflater.from(mContext).inflate(
                        R.layout.drawer_direction_placeholder, parent, false);
            }
        }
    }


    @Override
    public int getCount() {
        return mCount;
    }

static class NavigationHolder {
    ImageView imageView;
    TextView title;
    TextView description;
    TextView distance;
    TextView duration;
    TextView startAddress;
    TextView endAddress;
}
}
