package pointinapp.sachin_jadhav3.pointinnav;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    ListView lv;
    Context context;
    private TextView listtitle;
    private ImageView goHome;
    private String placeNames = "";
    private String placeDesc = "";
    private String placeDate = "";
    private String placeImgs = "";
    ArrayList prgmName;
    public static ArrayList<Integer> prgmImagesTemp;
    public static ArrayList<String> prgmNameListTemp;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private Context mainActivityContext;
    private NavigationView navigationView;
    private CustomAdapter customAdapter;
    boolean successMultiSelect = true;
    EditText groupTitle;
    private ActionMode globalMode;

    public ListFragment() {
        // Required empty public constructor
    }

    public void setMainActivityContext(Context context) {
        mainActivityContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        Typeface robo = Typeface.createFromAsset(getActivity().getAssets(), "roboto.ttf");

///        listtitle = (TextView)v.findViewById(R.id.listtitle);
        //     listtitle.setTypeface(robo);
        MainActivity.setCurrentFragment(this);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                try {
                    SavePlaceFragment fragment = new SavePlaceFragment();
                    fragment.setNavigationView(navigationView);

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.commit();
                } catch (Exception ex) {
                    Toast.makeText(context, "Not able to navigate, please use navigation drawer to navigate!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        context = getActivity();

        lv = (ListView) v.findViewById(R.id.listView);
        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(this);

        String filename = "pointins.txt";

        //reading data from file
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    context.openFileInput(filename)));
            String inputString;
            //Reading data line by line and storing it into the stringbuffer
            while ((inputString = inputReader.readLine()) != null) {
                String[] places = inputString.split("\\*");
                if (placeNames == "") {
                    placeNames = places[0];
                    placeDesc = places[1];
                    placeDate = places[4];
                } else {
                    placeNames = placeNames + "," + places[0];
                    placeDesc = placeDesc + "*" + places[1];
                    placeDate = placeDate + "," + places[4];
                }

            }

        } catch (IOException e) {

        }
        if (placeNames.equals("")) {
            try {
                Toast.makeText(context, "No locations added yet!",
                        Toast.LENGTH_LONG).show();
                //SavePlaceFragment f = new SavePlaceFragment();

            } catch (Exception ex) {

            }

        } else {
            String[] placesList = placeNames.split("\\,");
            int placesCount = placesList.length;
            int[] imagesList = new int[placesCount];
            for (int i = 0; i < placesCount; i++) {


                if (placesList[i].contains("Parking")) {
                    imagesList[i] = (R.drawable.ic_local_parking_black_48dp);
                } else if (placesList[i].contains("Air")) {
                    imagesList[i] = (R.drawable.ic_flight_black_48dp);
                } else if (placesList[i].contains("Bar")) {
                    imagesList[i] = (R.drawable.ic_local_bar_black_48dp);
                } else if (placesList[i].contains("Cafe")) {
                    imagesList[i] = (R.drawable.ic_local_cafe_black_48dp);
                } else if (placesList[i].contains("ATM")) {
                    imagesList[i] = (R.drawable.ic_local_atm_black_48dp);
                } else if (placesList[i].contains("Hill")) {
                    imagesList[i] = (R.drawable.ic_hill_station_black_48dp);
                } else if (placesList[i].contains("Camp")) {
                    imagesList[i] = (R.drawable.ic_camp_tent_black_48dp);
                } else if (placesList[i].contains("Place")) {
                    imagesList[i] = (R.drawable.ic_place_black_48dp);
                } else if (placesList[i].contains("Theatre")) {
                    imagesList[i] = (R.drawable.ic_local_movies_black_48dp);
                } else if (placesList[i].contains("Building")) {
                    imagesList[i] = (R.drawable.ic_building_black_48dp);
                } else if (placesList[i].contains("Shop")) {
                    imagesList[i] = (R.drawable.ic_local_grocery_store_black_48dp);
                } else if (placesList[i].contains("Post Office")) {
                    imagesList[i] = (R.drawable.ic_local_post_office_black_48dp);
                } else if (placesList[i].contains("Bus")) {
                    imagesList[i] = (R.drawable.ic_directions_bus_black_48dp);
                } else if (placesList[i].contains("Smoking Zone")) {
                    imagesList[i] = (R.drawable.ic_smoking_rooms_black_48dp);
                } else if (placesList[i].contains("Casino")) {
                    imagesList[i] = (R.drawable.ic_casino_black_48dp);
                } else if (placesList[i].contains("Pool")) {
                    imagesList[i] = (R.drawable.ic_pool_black_48dp);
                } else if (placesList[i].contains("Food")) {
                    imagesList[i] = (R.drawable.ic_local_dining_black_48dp);
                } else if (placesList[i].contains("Hospital")) {
                    imagesList[i] = (R.drawable.ic_local_hospital_black_48dp);
                } else if (placesList[i].contains("Library")) {
                    imagesList[i] = (R.drawable.ic_local_library_black_48dp);
                } else if (placesList[i].contains("Park")) {
                    imagesList[i] = (R.drawable.ic_nature_people_black_48dp);
                } else if (placesList[i].contains("Petrol")) {
                    imagesList[i] = (R.drawable.ic_local_gas_station_black_48dp);
                } else if (placesList[i].contains("Friend")) {
                    imagesList[i] = (R.drawable.ic_group_black_48dp);
                } else if (placesList[i].contains("Home")) {
                    imagesList[i] = (R.drawable.ic_home_black_48dp);
                } else if (placesList[i].contains("Mall")) {
                    imagesList[i] = (R.drawable.ic_local_mall_black_48dp);
                } else if (placesList[i].contains("Hotel")) {
                    imagesList[i] = (R.drawable.ic_hotel_black_48dp);
                } else if (placesList[i].contains("Person")) {
                    imagesList[i] = (R.drawable.ic_perm_identity_black_48dp);
                } else if (placesList[i].contains("Motorcycle")) {
                    imagesList[i] = (R.drawable.ic_motorcycle_black_48dp);
                } else if (placesList[i].contains("Train")) {
                    imagesList[i] = (R.drawable.ic_directions_railway_black_48dp);
                } else if (placesList[i].contains("Work")) {
                    imagesList[i] = (R.drawable.ic_work_black_48dp);
                } else {
                    imagesList[i] = (R.drawable.ic_apps_black_48dp);
                }


            }

            try {

                customAdapter = new CustomAdapter(context, mainActivityContext, placeDesc.split("\\*"), imagesList, placeDate.split("\\,"), placesList);
                lv.setAdapter(customAdapter);

//                lv.setonlo
            } catch (Exception ex) {
                Toast.makeText(context, "Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
            }


        }
        return v;
    }


    public void setNavigationView(NavigationView navigationView) {
        this.navigationView = navigationView;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        // Capture ListView item click
        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                mode.setTitle(lv.getCheckedItemCount() + " Selected");
                customAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                //instance mode variable to access in inner class.
                globalMode = mode;

                if (item.getItemId() == R.id.deleteItems) {
                    SparseBooleanArray selected = customAdapter.getSelectedIds();
                    int size =  selected.size();
                    for (int i = (size-1); i >=0 ; i--) {
                        if (selected.valueAt(i))
                            customAdapter.remove(selected.keyAt(i));
                    }

                    mode.finish();
                    Toast.makeText(context, "Place deleted succesfully.", Toast.LENGTH_LONG).show();
                    return true;
                }

                if(item.getItemId() == R.id.shareItems)
                {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.tripdialog);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    lp.gravity = Gravity.CENTER;

                    dialog.getWindow().setAttributes(lp);

                    Button cancel ,done;

                    done = (Button) dialog.findViewById(R.id.doneB);
                    cancel = (Button) dialog.findViewById(R.id.cancelB);

                    groupTitle = (EditText) dialog.findViewById(R.id.triptitle);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                                    Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                    vib.vibrate(25);
                                }
                            dialog.dismiss();
                        }
                    });

                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                                    Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                    vib.vibrate(25);
                                }
                                if (groupTitle.getText().toString().equals(""))
                                    Toast.makeText(context, "Enter title ", Toast.LENGTH_LONG).show();
                                else{
                                    dialog.dismiss();

                                    SparseBooleanArray selected = customAdapter.getSelectedIds();
                                    int size =  selected.size();
                                    String trip = "";
                                    for(int i=0;i<size;i++)
                                    {
                                        trip +=  customAdapter.sharePlaceInGroup(selected.keyAt(i)) + System.getProperty("line.separator");
                                    }

                                    trip = groupTitle.getText().toString() + System.getProperty("line.separator") + System.getProperty("line.separator") + trip;

                                    Toast.makeText(getActivity(), trip, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, trip);
                                    intent.setType("text/plain");
                                    context.startActivity(Intent.createChooser(intent, "Share place using:"));

                                    globalMode.finish();
                                }
                            }
                            catch (Exception ex){
                                Toast.makeText(context, "Could not delete the place. Try again !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.context_action_bar_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                customAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

        });
        return false;
    }
}
