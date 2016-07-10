package pointinapp.sachin_jadhav3.pointinnav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private NavigationView navigationView;
    private static Fragment currentFragment;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private static String api_key = "AIzaSyBaNI3REfq9WONyf9pwdS3z3rS1U-zLyvI";
    private static String sender = "9561598599";
    private static String receiver = "9970016888";
    public  final static String REQUEST = "1";
    public final static String SHARE = "0";


    public static void setCurrentFragment(Fragment fragment)
    {
        currentFragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .build();
        // set save place fragment on load

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_save_place);
        navigationView.setNavigationItemSelectedListener(this);

        if(currentFragment == null) {


            SavePlaceFragment fragment = new SavePlaceFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        else
        {
            if(currentFragment instanceof ListFragment)
            {
                ListFragment listFragment = new ListFragment();
                listFragment.setMainActivityContext(this);
            	listFragment.setNavigationView(navigationView);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, listFragment);
                fragmentTransaction.commit();
            }

            if(currentFragment instanceof SavePlaceFragment)
            {

                         android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, currentFragment);
                        fragmentTransaction.commit();
            }

        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true))
        {

            //Checking play service is available or not
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

            //if play service is not available
            if(ConnectionResult.SUCCESS != resultCode) {
                //If play service is supported but not installed
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    //Displaying message that play service is not installed
                    Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                    GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                    //If play service is not supported
                    //Displaying an error message
                }
                else {
                    Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
                }
            }
            //If play service is available}
            else {
                //Starting intent to register device
                Intent itent = new Intent(this, GCMRegistrationIntentService.class);
                startService(itent);
            }

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Toast toast = Toast.makeText(this, result.toString(),Toast.LENGTH_LONG);
        toast.show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_save_place) {
            SavePlaceFragment fragment = new SavePlaceFragment();

            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            // Handle the camera action
        } else if (id == R.id.nav_locations) {
            ListFragment fragment = new ListFragment();
            fragment.setNavigationView(navigationView);

            fragment.setMainActivityContext(this);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();

        }
        else if(id == R.id.request_pointin)
        {
            AsyncTaskRunner runner  = new AsyncTaskRunner();
            runner.execute(MainActivity.api_key,MainActivity.sender,MainActivity.receiver,MainActivity.REQUEST);
        }
        else if (id == R.id.nav_share_fb) {
            try{
                CallbackManager callbackManager;
                ShareDialog shareDialog;
                FacebookSdk.sdkInitialize(getApplicationContext());
                shareDialog = new ShareDialog(this);

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    Resources res = getResources();
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()


                    .setContentTitle("PointIn : If you hate remembering places, then this is the perfect app for you !")
                            .setContentUrl(Uri.parse(Uri.parse("https://play.google.com/store/apps/details?id=com.PointIn.User.pointin").toString()))
                            .setShareHashtag(new ShareHashtag.Builder()
                                    .setHashtag("#PointIn")
                                    .build())
                            .build();

                    shareDialog.show(linkContent);
                }
            }
            catch(Exception ex){
                Toast.makeText(getApplicationContext(),"Internal error occured. Try again !", Toast.LENGTH_LONG).show();
            }

        }
        else if(id == R.id.nav_share_wapp){
            String whatsAppMessage = "PointIn : If you hate remembering places, then this is the perfect app for you ! " + Uri.parse("https://play.google.com/store/apps/details?id=com.PointIn.User.pointin").toString();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            this.startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AsyncTaskRunner extends AsyncTask<String,Void,String>
    {
        private String resp = "Result is:";

        @Override
        protected String doInBackground(String... params) {

            try
            {
                URL requestPointin = new URL("http://10.131.125.221/sendget.php");
                HttpURLConnection connection = (HttpURLConnection) requestPointin.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("apikey", params[0])
                        .appendQueryParameter("sender", params[1])
                        .appendQueryParameter("receiver", params[2])
                        .appendQueryParameter("type", params[3]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        resp+=line;
                    }
                }

           }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG).show();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
