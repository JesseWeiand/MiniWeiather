package com.xiantos.miniWeather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private WundergroundAPI wundergroundAPI = new WundergroundAPI();
    private ProgressDialog progressDialog;
    private static final String STATE_ND = "ND";


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        String url = wundergroundAPI.getCurrentUrl(1);
        //updateWeather(url);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        String cityName;
        double gps[];

        wundergroundAPI.setUseGps(false);
        switch (number) {
            case 1:
                cityName = getString(R.string.title_section1);
                wundergroundAPI.setStateName(STATE_ND);
                break;
            case 2:
                cityName = getString(R.string.title_section2);
                wundergroundAPI.setStateName(STATE_ND);
                break;
            case 3:
                cityName = getString(R.string.title_section3);
                wundergroundAPI.setStateName(STATE_ND);
                break;
            case 4:
                cityName = getString(R.string.title_section4);
                //set gps coordinates
                gps = getGPS();
                wundergroundAPI.setGpsLatitude(gps[0]);
                wundergroundAPI.setGpsLongitude(gps[1]);
                wundergroundAPI.setUseGps(true);
                wundergroundAPI.lookUpLocation();
                break;
            default:
                cityName = getString(R.string.title_section1);
                break;
        }
        mTitle = cityName;
        wundergroundAPI.setCityName(cityName);
        updateWeather();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.textLocation);
            int sectionId = getArguments().getInt(ARG_SECTION_NUMBER);
            String location = "";
            textView.setText(location);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
            //updateWeather();
        }
    }

    public void updateWeather(){
        String url = wundergroundAPI.getCurrentUrl(1);
        updateWeather(url);
    }

    public void updateWeather(String url){
        new JSONParse().execute();
    }

    public void updateWeather(View view) {
        String url = wundergroundAPI.getCurrentUrl(1);
        updateWeather(url);
        return;
    }

    public void updateWeatherFields(){
        TextView textCurTemp = (TextView) findViewById(R.id.textCurTemp);
        //TextView textCurLow = (TextView) findViewById(R.id.textLo);
        //TextView textCurHi = (TextView) findViewById(R.id.textHigh);
        TextView textCurWind = (TextView) findViewById(R.id.textWind);
        TextView textCurFeel = (TextView) findViewById(R.id.textFeel);

        int curTemp = wundergroundAPI.getCurTemp();
        //int curLow = wundergroundAPI.getLoTemp();
        //int curHi = wundergroundAPI.getHighTemp();
        int curWind = wundergroundAPI.getWindSpeed();
        String curWindDir = wundergroundAPI.getWindDir();
        String curFeel = wundergroundAPI.getWindChill();

        textCurTemp.setText(Integer.toString(curTemp) + "°");
        //textCurLow.setText("Low: " + Integer.toString(curLow));
        //textCurHi.setText("High: " + Integer.toString(curHi));
        textCurFeel.setText("Feels: " + curFeel);

        String windString = Integer.toString(curWind) + " mph, " + curWindDir;

        textCurWind.setText(windString);
    }

    public void updateForecastFields(){
        TextView textDate1 = (TextView) findViewById(R.id.textDate1);
        TextView textDate2 = (TextView) findViewById(R.id.textDate2);
        TextView textDate3 = (TextView) findViewById(R.id.textDate3);
//
        TextView textTemp1 = (TextView) findViewById(R.id.textTemp1);
        TextView textTemp2 = (TextView) findViewById(R.id.textTemp2);
        TextView textTemp3 = (TextView) findViewById(R.id.textTemp3);
//
        TextView textCond1 = (TextView) findViewById(R.id.textCond1);
        TextView textCond2 = (TextView) findViewById(R.id.textCond2);
        TextView textCond3 = (TextView) findViewById(R.id.textCond3);
//
        TextView textPrecip1 = (TextView) findViewById(R.id.textPrecip1);
        TextView textPrecip2 = (TextView) findViewById(R.id.textPrecip2);
        TextView textPrecip3 = (TextView) findViewById(R.id.textPrecip3);
//
        TextView textWind1 = (TextView) findViewById(R.id.textWind1);
        TextView textWind2 = (TextView) findViewById(R.id.textWind2);
        TextView textWind3 = (TextView) findViewById(R.id.textWind3);

        int forecastHigh[] = wundergroundAPI.getForecastHighTemp();
        int forecastLow[] = wundergroundAPI.getForecastLowTemp();
        int forecastDay[] = wundergroundAPI.getDay();
        int forecastMonth[] = wundergroundAPI.getMonth();
        int forecastPrecip[] = wundergroundAPI.getForecastPrecip();
        int forecastWind[] = wundergroundAPI.getForecastWind();
        String forecastConditions[] = wundergroundAPI.getForecastConditions();

        //textDate1.setText(Integer.toString(forecastDay[0]) + "/" + Integer.toString(forecastMonth[0]));
        //textDate2.setText(Integer.toString(forecastDay[1]) + "/" + Integer.toString(forecastMonth[1]));
        //textDate3.setText(Integer.toString(forecastDay[2]) + "/" + Integer.toString(forecastMonth[2]));
        textDate1.setText(Integer.toString(forecastMonth[0]) + "/" + Integer.toString(forecastDay[0]));
        textDate2.setText(Integer.toString(forecastMonth[1]) + "/" + Integer.toString(forecastDay[1]));
        textDate3.setText(Integer.toString(forecastMonth[2]) + "/" + Integer.toString(forecastDay[2]));
//
        textTemp1.setText(Integer.toString(forecastHigh[0]) + "/" + Integer.toString(forecastLow[0]));
        textTemp2.setText(Integer.toString(forecastHigh[1]) + "/" + Integer.toString(forecastLow[1]));
        textTemp3.setText(Integer.toString(forecastHigh[2]) + "/" + Integer.toString(forecastLow[2]));
//
        textPrecip1.setText(Integer.toString(forecastPrecip[0]) + "%");
        textPrecip2.setText(Integer.toString(forecastPrecip[1]) + "%");
        textPrecip3.setText(Integer.toString(forecastPrecip[2]) + "%");
//
        textWind1.setText(Integer.toString(forecastWind[0]));
        textWind2.setText(Integer.toString(forecastWind[1]));
        textWind3.setText(Integer.toString(forecastWind[2]));
//
        textCond1.setText(forecastConditions[0]);
        textCond2.setText(forecastConditions[1]);
        textCond3.setText(forecastConditions[2]);
    }

    public class JSONParse extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args){
            JSONParser jsonParser = new JSONParser();
            String url = wundergroundAPI.getCurrentUrl(1);
            JSONObject json;
            json = jsonParser.getJSONFromUrl(url);
            wundergroundAPI.readConditions(json);

            url = wundergroundAPI.getCurrentUrl(2);
            json = jsonParser.getJSONFromUrl(url);
            wundergroundAPI.readForecast(json);

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json){
            //call the weather api decoder set up in the other class
            progressDialog.cancel();
            updateWeatherFields();
            updateForecastFields();
        }
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }

        return gps;
    }




}
