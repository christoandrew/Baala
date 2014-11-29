package com.iconasystems.christo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.christo.baalafinal.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Christo on 11/14/2014.
 */
public class BarListAdapter extends BaseAdapter {
    private static final String TAG_BAR_ID = "bar_id";
    private final Activity activity;
    private final ArrayList<HashMap<String, String>> barsList;
    private LayoutInflater inflater;
    private ImageView mBarImage;
    private TextView mBarId;
    private TextView mBarName;
    Typeface typeface;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private static final String TAG_BAR_IMAGE = "bar_image";
    private static final String TAG_BAR_NAME = "bar_name";
    private DisplayImageOptions options;

    public BarListAdapter(Activity activity, ArrayList<HashMap<String, String>> barsList, Typeface typeface) {
        this.activity = activity;
        this.barsList = barsList;
        this.typeface = typeface;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return barsList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return position;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view  = convertView;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_empty)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedVignetteBitmapDisplayer(10, 5))
                .showImageOnFail(R.drawable.ic_error)
                .build();

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) view = inflater.inflate(R.layout.bar_list_item, null);

        mBarImage = (ImageView) view.findViewById(R.id.bar_photo);
        mBarId = (TextView) view.findViewById(R.id.bar_list_id);
        mBarName = (TextView) view.findViewById(R.id.bar_list_name);
        HashMap<String, String> map;
        map = barsList.get(position);

        String bar_id = map.get(TAG_BAR_ID);
        String image_url = map.get(TAG_BAR_IMAGE);
        String bar_name = map.get(TAG_BAR_NAME);
        mBarId.setText(bar_id);
        mBarName.setText(bar_name);
        mBarName.setTypeface(typeface);

        ImageLoader.getInstance().displayImage(image_url, mBarImage, options, animateFirstListener);

         /*Picasso.with(activity.getApplicationContext())
                    .load(image_url)
                    .into(mBarImage);*/

        return view;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
