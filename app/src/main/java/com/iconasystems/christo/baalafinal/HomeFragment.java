package com.iconasystems.christo.baalafinal;


import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iconasystems.christo.utils.EventListAdapter;
import com.iconasystems.christo.utils.JSONParser;

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
public class HomeFragment extends Fragment implements LocationListener{
    private static final String TAG_EVENT_DATE = "event_date";
    private static final String TAG_EVENT_NAME = "event_name";
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_EVENT_LOCATION = "bar_name";
    private static final String TAG_EVENT_DRESS_CODE = "event_dress_code";
    private static final String TAG_EVENT_IMAGE = "event_image";
    private static final String TAG_EVENT_ENTRANCE = "entrance";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_SUCCESS = "success";

    private JSONParser jsonParser;
    private ArrayList<HashMap<String, String>> eventsList;
    private JSONArray events;

    private ProgressDialog progressDialog;

    private static final String url_get_all_events = "http://10.0.3.2/baala/get_all_events.php";

    private ListView listView;
    private View rootView;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        eventsList = new ArrayList<HashMap<String, String>>();
        jsonParser = new JSONParser();

        listView = (ListView) rootView.findViewById(R.id.list_view_event);

        new LoadEvents().execute();
    }
    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {

    }

    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     * @param status   {@link android.location.LocationProvider#OUT_OF_SERVICE} if the
     *                 provider is out of service, and this is not expected to change in the
     *                 near future; {@link android.location.LocationProvider#TEMPORARILY_UNAVAILABLE} if
     *                 the provider is temporarily unavailable but is expected to be available
     *                 shortly; and {@link android.location.LocationProvider#AVAILABLE} if the
     *                 provider is currently available.
     * @param extras   an optional Bundle which will contain provider specific
     *                 status variables.
     *                 <p/>
     *                 <p> A number of common key/value pairs for the extras Bundle are listed
     *                 below. Providers that use any of the keys on this list must
     *                 provide the corresponding value as described below.
     *                 <p/>
     *                 <ul>
     *                 <li> satellites - the number of satellites used to derive the fix
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }


    class LoadEvents extends AsyncTask<String, String, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Events...Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> data = new ArrayList<NameValuePair>();

            try {
                JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_all_events, "GET", data);

                Log.d("All Events", jsonObject.toString());

                try {
                    int success = jsonObject.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        events = jsonObject.getJSONArray(TAG_EVENTS);
                        for (int i = 0; i < events.length(); i++) {
                           JSONObject event = events.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TAG_EVENT_DATE, event.getString(TAG_EVENT_DATE));
                            map.put(TAG_EVENT_DRESS_CODE, event.getString(TAG_EVENT_DRESS_CODE));
                            map.put(TAG_EVENT_ENTRANCE, event.getString(TAG_EVENT_ENTRANCE));
                            map.put(TAG_EVENT_IMAGE, event.getString(TAG_EVENT_IMAGE));
                            map.put(TAG_EVENT_LOCATION, event.getString(TAG_EVENT_LOCATION));
                            map.put(TAG_EVENT_ID, event.getString(TAG_EVENT_ID));
                            map.put(TAG_EVENT_NAME, event.getString(TAG_EVENT_NAME));

                            eventsList.add(map);
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
            super.onPostExecute(result);
            progressDialog.dismiss();

            EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), eventsList);
            listView.setAdapter(eventListAdapter);
        }
    }

}
