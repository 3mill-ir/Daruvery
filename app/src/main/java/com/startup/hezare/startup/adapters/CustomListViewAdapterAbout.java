package com.startup.hezare.startup.adapters;

/**
 * Created by rf on 18/07/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.startup.hezare.startup.R;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class CustomListViewAdapterAbout extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, String>> ItemsList;

    public CustomListViewAdapterAbout(Context context, ArrayList<HashMap<String, String>> data) {

        this.ItemsList = data;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return ItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0 || position == 5) {
            return false;
        } else
            return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_drawer_item, parent, false);
        }

        View view = convertView;

        //Fonts
        Typeface BHoma = Typeface.createFromAsset(mContext.getAssets(), "fonts/BHoma.ttf");
        Typeface ANegar = Typeface.createFromAsset(mContext.getAssets(), "fonts/ANegaar.ttf");
        Typeface BYekan = Typeface.createFromAsset(mContext.getAssets(), "fonts/BYekan.ttf");

        TextView list_text = (TextView) view.findViewById(R.id.list_item);
        ImageView right_icon = (ImageView) view.findViewById(R.id.left_icon);
        ImageView left_icon = (ImageView) view.findViewById(R.id.right_icon);

        HashMap<String, String> item;

        item = ItemsList.get(position);

        list_text.setText(item.get("name"));
        if (position == 0 || position == 5) {
            list_text.setTypeface(ANegar);
            left_icon.setImageResource(0);
            right_icon.setImageResource(0);
        } else {
            list_text.setTypeface(BYekan);
            left_icon.setImageResource(Integer.parseInt(ItemsList.get(position).get("icon")));
            right_icon.setImageResource(R.drawable.ic_chevron_left_black_30dp);

        }

        // check for states
        //Log.i("my tag", " " + position);

        return view;
    }
}