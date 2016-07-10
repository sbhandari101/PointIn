package pointinapp.sachin_jadhav3.pointinnav;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavePlaceFragment extends Fragment implements View.OnClickListener, PlaceSelectionListener {

    private SupportPlaceAutocompleteFragment autocompleteFragment;

    private Spinner spinner;
    private Button pointinButton;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude,longitude, selectedPlace;
    protected boolean gps_enabled,network_enabled;
    private EditText description;
    protected  String descText;
    private ImageView showList, shareOnFB;
    private TextView goHome;
    Boolean isInternetPresent = true;
    boolean isGPSEnabled = false;
    private NavigationView navigationView;
    private String locationToSave = "c";

    private RadioGroup radioGroup;
    private RadioButton currentLoc, srchLoc;
    private LinearLayout fl;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    // Connection detector class

    //facebook
//    CallbackManager callbackManager;
//    ShareDialog shareDialog;
    //end facebook init

    public SavePlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try{
            View v = inflater.inflate(R.layout.fragment_save_place, container, false);
            final boolean showbox ;

            MainActivity.setCurrentFragment(this);

            if(navigationView != null)
            {
                    navigationView.setCheckedItem(R.id.nav_save_place);
            }
            // location radio group

            radioGroup = (RadioGroup) v.findViewById(R.id.placeGroup);
            fl = (LinearLayout) v.findViewById(R.id.llplacesearch);
            fl.setVisibility(View.GONE);




 if(autocompleteFragment == null) {
                autocompleteFragment = new SupportPlaceAutocompleteFragment();

                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.autocomplete, autocompleteFragment);
                fragmentTransaction.commit();
            }
            else
            {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.autocomplete, autocompleteFragment);
                fragmentTransaction.commit();
            }

            autocompleteFragment.setOnPlaceSelectedListener(this);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId  == R.id.current){
                        fl.setVisibility(View.GONE);
                        locationToSave = "c";
                    }
                    else if(checkedId == R.id.search){
                        //  showbox = true;

                        try{
                            boolean state = isMobileDataEnable();
                            if(!state)
                            {
                                Toast.makeText(context,"Turn On internet connection!",Toast.LENGTH_LONG).show();
                                //checking if mobile data is off then enabling mobile data.
//                                toggleMobileDataConnection(true);
                            }
                        }
                        catch (Exception ex){

                        }
                        fl.setVisibility(View.VISIBLE);
                        locationToSave = "s";
                    }
                    else{

                    }
                }
            });




//            View v = getView();

            context = getActivity();
            pointinButton = (Button)v.findViewById(R.id.button);
            pointinButton.setOnClickListener(this);

            description = (EditText)v.findViewById(R.id.editText);
//        showList = (ImageView) v.findViewById(R.id.imageView9);
//        shareOnFB = (ImageView) v.findViewById(R.id.imageView5);
            spinner = (Spinner) v.findViewById(R.id.spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                    R.array.places, android.R.layout.simple_spinner_dropdown_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            return v;
        }
        catch(Exception ex){
            Toast.makeText(context,"Permission granted.",Toast.LENGTH_LONG).show();
            return null;
        }

    }

    @Override
    public void onPlaceSelected(Place place) {
        // TODO: Get info about the selected place.
//        Toast toast = Toast.makeText(context, place.getLatLng().toString(),Toast.LENGTH_LONG);
//        Toast.makeText(context, Double.toString( place.getLatLng().latitude),Toast.LENGTH_LONG).show();
        latitude = Double.toString( place.getLatLng().latitude);
        longitude = Double.toString( place.getLatLng().longitude);
//        Toast.makeText(context, latitude,Toast.LENGTH_LONG).show();
//        toast.show();
    }

    @Override
    public void onError(Status status) {
        // TODO: Handle the error.
        Toast toast = Toast.makeText(context, status.toString(),Toast.LENGTH_LONG);
        toast.show();
    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//
//
//        View v = getView();
//
//        context = getActivity();
//        pointinButton = (Button)v.findViewById(R.id.button);
//        description = (EditText)v.findViewById(R.id.editText);
////        showList = (ImageView) v.findViewById(R.id.imageView9);
////        shareOnFB = (ImageView) v.findViewById(R.id.imageView5);
//        spinner = (Spinner) v.findViewById(R.id.spinner);
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
//                R.array.places, android.R.layout.simple_spinner_dropdown_item);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//    }
//
    @Override
    public void onClick(final View v) { //check for what button is pressed
       // Toast.makeText(context,Integer.toString(v.getId()) ,Toast.LENGTH_LONG).show();
        switch (v.getId()) {
            case R.id.button:
                 pointinButtonClick();
                break;
            default:
            //    c *= fComm.fragmentContactActivity(100);
                break;
        }
    }

    public boolean isVibratePermissions(){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED;
    }

    // current date
    public String currentDate() {
        Calendar cal = Calendar.getInstance();
        String date = Integer.toString(cal.get(Calendar.DATE));
        String month = Integer.toString(cal.get(Calendar.MONTH));
        String year = Integer.toString(cal.get(Calendar.YEAR));
        return date + "/" + month + "/" + year;
    }


    public static String getLocationByNetwork(Context context) {

        int status = context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                context.getPackageName());
        if (status == PackageManager.PERMISSION_GRANTED) {
            LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            mgr.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {}

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {}

                        @Override
                        public void onProviderEnabled(String provider) {}

                        @Override
                        public void onProviderDisabled(String provider) {}
                    });

            List<String> providers = mgr.getAllProviders();
            if (providers != null && providers.contains(LocationManager.NETWORK_PROVIDER)) {
                Location loc = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    return loc.getLatitude() + "*" + loc.getLongitude();
                }
            }
        }
        return null;
    }

    public void setNavigationView(NavigationView navigationView)
    {
        this.navigationView = navigationView ;
    }

    //test mobile internet connection
    public boolean isMobileDataEnable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static String getLocationByGPS(Context context) {
        int status = context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName());
        if (status == PackageManager.PERMISSION_GRANTED) {
            LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            mgr.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
            List<String> providers = mgr.getAllProviders();
            if (providers != null && providers.contains(LocationManager.GPS_PROVIDER)) {
                Location loc = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (loc != null) {
                    return loc.getLatitude() + "*" + loc.getLongitude();
                }

            }
        }

        return null;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }




    private String descConverter(String desc){
        String withoutNewLine = "";
        int lenth = desc.length();
        for(int i = 0 ;i < lenth; i++){
            if(desc.charAt(i) == '\n'){
                withoutNewLine = withoutNewLine +   ' ';
            }
            else{
                withoutNewLine = withoutNewLine +   desc.charAt(i);
            }
        }
        return withoutNewLine;
    }

    private boolean checkPermissionFineLoc(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPermissionCoarseLoc(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity)context ,Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions((MainActivity)context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context,"Permission granted.",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context,"Permission denied.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

//    public void showAlertDialog(Context context, String title, String message, Boolean status) {
//        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(message);
//        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        alertDialog.show();
//    }

    public void pointinButtonClick(){
        try{
            if (isVibratePermissions()){
                Vibrator vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(25);
            }

            if(checkPermissionFineLoc() || checkPermissionCoarseLoc()) {

                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                descText = description.getText().toString();
                selectedPlace = spinner.getSelectedItem().toString();

                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (locationToSave.equals("c") && !isGPSEnabled && !isNetworkEnabled) {
                    Toast.makeText(context, "Check your Location sharing or Internet connection settings.", Toast.LENGTH_LONG).show();
                }
                else{
                    canGetLocation = true;
                    if(locationToSave.equals("c")){
                        if (isNetworkEnabled) {
                            String getLoc = getLocationByNetwork(context);
                            if (getLoc != null) {
                                String[] locAr = getLoc.split("\\*");
                                latitude = locAr[0];
                                longitude = locAr[1];

                            }
                        }
                        // If GPS enabled, get latitude/longitude using GPS Services
                        if (isGPSEnabled && ( (latitude.equals("") || longitude.equals("")))) {
                            String getLoc = getLocationByGPS(context);
                            if (getLoc != null) {
                                String[] locAr = getLoc.split("\\*");
                                latitude = locAr[0];
                                longitude = locAr[1];
                            }
                        }
                    }


                    if ((latitude != null && longitude != null ) && !latitude.equals("") && !longitude.equals("")) {
                        if (selectedPlace.contains("Select")) {
                            Toast.makeText(context, "Select your location type.", Toast.LENGTH_SHORT).show();
                        } else if (descText.matches("")) {
                            Toast.makeText(context, "Provide description for location.", Toast.LENGTH_SHORT).show();
                        } else if (descText.contains("*") || descText.contains(";")) {
                            Toast.makeText(context, "Description should not contain '*' and ';'. Remove and try again.", Toast.LENGTH_SHORT).show();
                        } else {

                            // get date
                            String today = currentDate();

                            //proceed with saving

                            String filename = "pointins.txt";
                            StringBuffer stringBuffer = new StringBuffer();
                            try {
                                //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
                                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                                        context.openFileInput(filename)));
                                String inputString;
                                //Reading data line by line and storing it into the stringbuffer
                                while ((inputString = inputReader.readLine()) != null) {
                                    stringBuffer.append(inputString + "\n");
                                }

                            } catch (Exception e) {

                            }


                            //save data
                            String data = selectedPlace + "*" + descConverter(descText) + "*" + latitude + "*" + longitude + "*" + today + System.getProperty("line.separator") + stringBuffer.toString();

                            FileOutputStream fos;
                            try {
                                fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                                //default mode is PRIVATE, can be APPEND etc.
                                fos.write(data.getBytes());
                                fos.close();

                                Toast.makeText(context, "PointIn saved succesfully.",
                                        Toast.LENGTH_LONG).show();

                                SavePlaceFragment newSavePlacesFragment = new SavePlaceFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, newSavePlacesFragment);
                                fragmentTransaction.commit();

                            } catch (FileNotFoundException e) {
                                Toast.makeText(context, "Could not save the location. Try again !",
                                        Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(context, "Could not save the location. Try again !",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        if(locationToSave.equals("s")){
                            if(!isMobileDataEnable()){
                                Toast.makeText(context, "Turn On internet connection for searching locations!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context, "Please search location and try again !",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                        else {
                            Toast.makeText(context, "Unable to get the location. Check your Location sharing or internet connection setting and try again !",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }

            }
            else{
                requestPermission();
            }
        }
        catch (Exception ex)  {
            Toast.makeText(context," Could not save location. Try again !", Toast.LENGTH_SHORT).show();
        }
    }

}
