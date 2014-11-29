package com.iconasystems.christo.baalafinal;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.iconasystems.christo.utils.JSONParser;
import com.iconasystems.christo.utils.ListAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpiritsFragment extends ListFragment {
    private ProgressDialog progressDialog;
    private JSONParser jsonParser = new JSONParser();

    private static final String TAG_DRINK_NAME = "drink_name";
    private static final String TAG_DRINK_ID = "drink_id";
    private static final String TAG_DRINK_PRICE = "drink_price";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BAR_ID = "bar_id";
    private static final String TAG_DRINK_MENU = "drink_menu";
    private static final String TAG_DRINK_IMAGE = "drink_image";

    private static final String url_get_drink_menu = "http://10.0.3.2/baala/get_spirit.php";

    private JSONArray drinkMenu = null;
    private ArrayList<HashMap<String, String>> drinksList;
    private String[] urls;

    private ImageView mBeerPhoto;
    private String bar_id;

    public SpiritsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spirits, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        drinksList = new ArrayList<HashMap<String, String>>();
        final ListView lv = getListView();

        Intent i = getActivity().getIntent();
        bar_id = i.getStringExtra(TAG_BAR_ID);
        mBeerPhoto = (ImageView) lv.findViewById(R.id.beer_photo);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new LoadBars().execute();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    class LoadBars extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Bars...Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair(TAG_BAR_ID, bar_id));
            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_drink_menu, "GET", data);
                Log.d("Drinks Menu", jsonObject.toString());
                try {
                    int success = jsonObject.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        drinkMenu = jsonObject.getJSONArray(TAG_DRINK_MENU);
                        Log.d("Drinks", drinkMenu.toString());
                        for (int i = 0; i < drinkMenu.length(); i++) {

                            int size = drinkMenu.length();
                            urls = new String[size];
                            JSONObject json = drinkMenu.getJSONObject(i);

                            String drink_name = json.getString(TAG_DRINK_NAME);
                            String drink_price = json.getString(TAG_DRINK_PRICE);
                            String drink_id = json.getString(TAG_DRINK_ID);
                            String drink_image = json.getString(TAG_DRINK_IMAGE);

                            urls[i] = drink_image;

                            Log.d("Details", drink_name + " " + drink_price + " " + drink_id);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TAG_DRINK_ID, drink_id);
                            map.put(TAG_DRINK_NAME, drink_name);
                            map.put(TAG_DRINK_PRICE, drink_price);
                            map.put(TAG_DRINK_IMAGE, drink_image);


                            if (!map.isEmpty()) {
                                drinksList.add(map);
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Error Empty Hashmap", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            progressDialog.dismiss();

            ListAdapter listAdapter = new ListAdapter(getActivity(), drinksList);

            setListAdapter(listAdapter);

            /*ListAdapter listAdapter = new SimpleAdapter(DrinkListActivity.this, drinksList,
                    R.layout.drink_list_item, new String[]{TAG_DRINK_ID, TAG_DRINK_NAME, TAG_DRINK_PRICE, "Decription"},
                    new int[]{R.id.drink_id, R.id.drink_name, R.id.drink_price, R.id.drink_description}
            );
            setListAdapter(listAdapter);*/
        }
    }

}
