package com.iconasystems.christo.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.iconasystems.christo.baalafinal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Christo on 11/11/2014.
 */
public class ListAdapter extends BaseAdapter {
    private final Activity activity;
    private final ArrayList<HashMap<String, String>> drinksList;
    private LayoutInflater inflater;
    private TextView mDrinkName;
    private TextView mDrinkDescription;
    private TextView mDrinkPrice;
    private ImageView mBeerPhoto;
    private TextView mDrinkId;

    private static final String TAG_DRINK_NAME = "drink_name";
    private static final String TAG_DRINK_ID = "drink_id";
    private static final String TAG_DRINK_PRICE = "drink_price";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_DRINK_MENU = "drink_menu";
    private static final String TAG_DRINK_IMAGE = "drink_image";

    public ListAdapter(Activity activity, ArrayList<HashMap<String, String>> drinksList) {
        this.activity = activity;
        this.drinksList = drinksList;
    }

    @Override
    public int getCount() {
        return this.drinksList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) view = inflater.inflate(R.layout.drink_list_item, null);

        mDrinkId = (TextView) view.findViewById(R.id.drink_id);
        mDrinkName = (TextView) view.findViewById(R.id.drink_name);
        mDrinkDescription = (TextView) view.findViewById(R.id.drink_description);
        mBeerPhoto = (ImageView) view.findViewById(R.id.beer_photo);
        mDrinkPrice = (TextView) view.findViewById(R.id.drink_price);

        HashMap<String, String> map;
        map = drinksList.get(position);

        mDrinkId.setText(map.get(TAG_DRINK_ID));
        mDrinkName.setText(map.get(TAG_DRINK_NAME));
        mDrinkPrice.setText(map.get(TAG_DRINK_PRICE));

        String image_url = map.get(TAG_DRINK_IMAGE);

        Picasso.with(activity.getApplicationContext())
                .load(image_url)
                .into(mBeerPhoto);
        return view;
    }
}
