package com.startup.hezare.startup.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.startup.hezare.startup.R;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rf on 27/08/2017.
 */


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String, String>> RequestsList;
    private int lastPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView center_name,date,price,status,response_code;
        public  ImageView image,Status_ImageView;
        public  ProgressBar img_progress;
        public Typeface BYekan;

        public MyViewHolder(View view) {
            super(view);
            center_name = (TextView) view.findViewById(R.id.center_name);
            date = (TextView) view.findViewById(R.id.date);
            price = (TextView) view.findViewById(R.id.price);
            status = (TextView) view.findViewById(R.id.status);
            response_code = (TextView) view.findViewById(R.id.response_code);
            image = (ImageView) view.findViewById(R.id.thumbnail);
            Status_ImageView = (ImageView) view.findViewById(R.id.status_icon);
            img_progress=(ProgressBar)view.findViewById(R.id.image_progressbar);
            BYekan=Typeface.createFromAsset(mContext.getAssets(), "fonts/BYekan.ttf");
        }
    }


    public MyRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        this.RequestsList = data;
        this.mContext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        HashMap<String, String> Request;

        //Log.i("my tag", " " + position);
        //setAnimation(holder.itemView, position);

        //Animation animation= AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? android.R.anim.slide_in_left : android.R.anim.slide_out_right);
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);

        holder.itemView.startAnimation(anim);


        Request = RequestsList.get(position);
        holder.center_name.setText(Request.get("name"));
        holder.center_name.setTypeface(holder.BYekan);
        holder.date.setText(Request.get("date"));
        //date.setTypeface(BHoma);
        if(!(Request.get("cost").equals("")))
        {
            holder.price.setTypeface(holder.BYekan);
            holder.price.setText(Request.get("cost")+"  تومان") ;
        }
        else {
            holder.price.setTypeface(holder.BYekan);
            holder.price.setText("تعین نشده");
        }

        holder.response_code.setText(Request.get("responsecode"));
        //price.setTypeface(ANegar);
        //

        // check for states
        if (RequestsList.get(position).get("state").equals("1")) {
            //Picasso.with(this.mContext).load(R.drawable.ic_done_black_24dp).into(Status_imageview);
            holder.Status_ImageView.setImageResource(R.drawable.ic_done_black_24dp);
            holder.status.setText("تائید شده");
            holder.status.setTypeface(holder.BYekan);
        }else if(RequestsList.get(position).get("state").equals("2")){
            //Picasso.with(this.mContext).load(R.drawable.ic_close_black_24dp).into(Status_imageview);
            holder.Status_ImageView.setImageResource(R.drawable.ic_close_black_24dp);
            holder.status.setText("تائید نشده");
            holder.status.setTypeface(holder.BYekan);
        } else if(RequestsList.get(position).get("state").equals("3")){
            //Picasso.with(this.mContext).load(R.drawable.ic_watch_later_black_24dp).into(Status_imageview);
            holder.Status_ImageView.setImageResource(R.drawable.ic_watch_later_black_24dp);
            holder.status.setText("در صف انتظار");
            holder.status.setTypeface(holder.BYekan);
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
                        holder.image.setImageBitmap(bitmap);
                        holder.img_progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        holder.img_progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        //lastPosition = position;

    }
    @Override
    public int getItemCount() {
        return RequestsList.size();
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }
}