package com.iconasystems.christo.baalafinal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.iconasystems.christo.utils.BarListAdapter;
import com.iconasystems.christo.utils.JSONParser;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewestFragment extends ListFragment {
    public static final String TAG_BAR_NAME = "bar_name";
    public static final String TAG_BAR_ID = "bar_id";
    public static final String TAG_BAR_IMAGE = "bar_image";
    public static final String TAG_BARS = "bars";
    public static final String TAG_SUCCESS = "success";

    public JSONParser jsonParser;
    public JSONArray barsArray = null;
    public ProgressDialog progressDialog;
    public ArrayList<HashMap<String, String>> barsList;

    public ImageView mBarImage;

    public TextView mBarId;

    public static final String url_get_newest_bars = "http://10.0.3.2/baala/get_bars.php";

    public NewestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_newest, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        jsonParser = new JSONParser();
        new LoadNewestBars().execute();
        barsList = new ArrayList<HashMap<String, String>>();

        ListView listView = getListView();
        mBarImage = (ImageView) listView.findViewById(R.id.bar_photo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBarId = (TextView) view.findViewById(R.id.bar_list_id);
                final String bar_id = mBarId.getText().toString();

                Intent i = new Intent(getActivity(), RealDetailsActivity.class);
                i.putExtra(TAG_BAR_ID, bar_id);
                startActivity(i);
            }
        });

    }


    class LoadNewestBars extends AsyncTask<String, String, String> {
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Newest...Please Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();

            JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_newest_bars, "GET", data);

            Log.d("Newest Bars", jsonObject.toString());

            try {
                int success = jsonObject.getInt(TAG_SUCCESS);

                if (success == 1) {
                    barsArray = jsonObject.getJSONArray(TAG_BARS);

                    for (int i = 0; i < barsArray.length(); i++) {
                        JSONObject json = barsArray.getJSONObject(i);

                        String bar_name = json.getString(TAG_BAR_NAME);
                        String bar_id = json.getString(TAG_BAR_ID);
                        String bar_image = json.getString(TAG_BAR_IMAGE);

                        HashMap<String, String> hashMap = new HashMap<String, String>();

                        hashMap.put(TAG_BAR_NAME, bar_name);
                        hashMap.put(TAG_BAR_IMAGE, bar_image);
                        hashMap.put(TAG_BAR_ID, bar_id);

                        barsList.add(hashMap);
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            BarListAdapter barListAdapter = new BarListAdapter(getActivity(), barsList);

            setListAdapter(barListAdapter);

            /*ListAdapter adapter = new SimpleAdapter(
                    getActivity(), barsList, R.layout.bar_list_item,
                    new String[]{TAG_BAR_NAME,  TAG_BAR_ID},
                    new int[]{R.id.bar_name_list,  R.id.bar_list_id});

            setListAdapter(adapter);*/
        }
    }

}
