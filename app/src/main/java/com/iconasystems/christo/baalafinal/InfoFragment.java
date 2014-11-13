package com.iconasystems.christo.baalafinal;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iconasystems.christo.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    private ProgressDialog progressDialog;
    private TextView mBarName;
    private TextView mBarWebsite;
    private TextView mBarContact;
    private String mBarId;

    private static final String TAG_BAR_NAME = "bar_name";
    private static final String TAG_BAR_WEBSITE = "bar_website";
    private static final String TAG_BAR_CONTACT = "bar_contact";
    private static final String TAG_BAR_IMAGE = "bar_image";
    private static final String TAG_DATE_ADDED = "date_added";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BAR_ID = "bar_id";

    private JSONParser jsonParser;

    private static final String url_get_info = "http://10.0.3.2/baala/get_bar_details.php";

    public InfoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        mBarName = (TextView) view.findViewById(R.id.bar_name);
        mBarContact = (TextView) view.findViewById(R.id.bar_contact_phone);
        mBarWebsite = (TextView) view.findViewById(R.id.bar_website);


        Intent i = getActivity().getIntent();
        mBarId = i.getStringExtra(TAG_BAR_ID);
        jsonParser = new JSONParser();
    }

    class LoadInfo extends AsyncTask<String, String, String> {
        @Override
        public void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Details...Please Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair(TAG_BAR_ID, mBarId));
            JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_info, "GET", data);

            try{
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    String bar_name = jsonObject.getString(TAG_BAR_NAME);
                    String bar_website = jsonObject.getString(TAG_BAR_WEBSITE);
                    String bar_contact = jsonObject.getString(TAG_BAR_CONTACT);
                    String bar_image = jsonObject.getString(TAG_BAR_IMAGE);
                    String date_added = jsonObject.getString(TAG_DATE_ADDED);

                    mBarWebsite.setText(bar_website);
                    mBarContact.setText(bar_contact);

                } else {
                    // Todo get messages from the json and show in toasts
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

        }
    }

}
