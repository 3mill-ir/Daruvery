package com.startup.hezare.startup.adapters;

/**
 * Created by rf on 18/07/2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.startup.hezare.startup.R;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.MaskTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class CustomListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, String>> RequestsList;

    public CustomListViewAdapter(Context context, ArrayList<HashMap<String, String>> data) {

        this.RequestsList = data;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return RequestsList.size();
    }

    @Override
    public Object getItem(int position) {
        return RequestsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private int lastposition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

       if(convertView==null)
       {
           convertView=LayoutInflater.from(mContext)
                   .inflate(R.layout.list_row,parent,false);
       }
        View view = convertView;
        Typeface BHoma = Typeface.createFromAsset(mContext.getAssets(), "fonts/BHoma.ttf");
        Typeface ANegar = Typeface.createFromAsset(mContext.getAssets(), "fonts/ANegaar.ttf");
        Typeface BYekan = Typeface.createFromAsset(mContext.getAssets(), "fonts/BYekan.ttf");

        TextView center_name = (TextView) view.findViewById(R.id.center_name);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView response_code = (TextView) view.findViewById(R.id.response_code);
        final ImageView image = (ImageView) view.findViewById(R.id.thumbnail);
        ImageView Status_ImageView = (ImageView) view.findViewById(R.id.status_icon);
        final ProgressBar img_progress=(ProgressBar)view.findViewById(R.id.image_progressbar);

        Animation animation= AnimationUtils.loadAnimation(mContext, (position > lastposition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        lastposition=position;

        HashMap<String, String> Request;

        Request = RequestsList.get(position);

        center_name.setText(Request.get("name"));
        center_name.setTypeface(BYekan);
        date.setText(Request.get("date"));
        //date.setTypeface(BHoma);
        if(!(Request.get("cost").equals("")))
        {
            price.setTypeface(BYekan);
            price.setText(Request.get("cost")+"  تومان") ;
        }
        else {
            price.setTypeface(BYekan);
            price.setText("تعین نشده");
        }

        response_code.setText(Request.get("responsecode"));
        //price.setTypeface(ANegar);
        //

        // check for states
        //Log.i("my tag", " " + position);


        lastposition = position;
        if (RequestsList.get(position).get("state").equals("1")) {
            //Picasso.with(this.mContext).load(R.drawable.ic_done_black_24dp).into(Status_imageview);
            Status_ImageView.setImageResource(R.drawable.ic_done_black_24dp);
            status.setText("تائید شده");
            status.setTypeface(BYekan);
        }else if(RequestsList.get(position).get("state").equals("2")){
            //Picasso.with(this.mContext).load(R.drawable.ic_close_black_24dp).into(Status_imageview);
            Status_ImageView.setImageResource(R.drawable.ic_close_black_24dp);
            status.setText("تائید نشده");
            status.setTypeface(BYekan);
        } else if(RequestsList.get(position).get("state").equals("3")){
            //Picasso.with(this.mContext).load(R.drawable.ic_watch_later_black_24dp).into(Status_imageview);
            Status_ImageView.setImageResource(R.drawable.ic_watch_later_black_24dp);
            status.setText("در صف انتظار");
            status.setTypeface(BYekan);
        }else {
            //Picasso.with(this.mContext).load(R.drawable.ic_watch_later_black_24dp).into(Status_imageview);
        }
            Picasso.with(this.mContext).load(Request.get("imageURL"))//.placeholder(R.drawable.camera128)
                    //.fit()
                    .resize(160,160)
                    //.transform(new CropCircleTransformation())
                    .transform(new RoundedCornersTransformation(40,0))
                    .into(new Target() {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            image.setImageBitmap(bitmap);
                            img_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            img_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        return view;
    }
}