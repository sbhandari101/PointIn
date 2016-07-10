package pointinapp.sachin_jadhav3.pointinnav;

/**
 * Created by Sachin_Jadhav3 on 5/4/2016.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter{
//    String [] result;
//    String [] placeDate;
//    String [] placeNames;
    Context context;
    LocationManager lm ;
//    int [] imageId;
    private static LayoutInflater inflater=null;
    private Context mainActivityContext;
    private SparseBooleanArray mSelectedItemsIds;
    private ArrayList<String> resultList;
    private ArrayList<String> placeDateList;
    private ArrayList<String>  placeNameList;
    private ArrayList<Integer> imageIdList;
    private int position;
    private Holder holder;

    public CustomAdapter(){

    }

    public CustomAdapter(Context contxt, Context mainActivityContext, String [] prgmNameList, int [] prgmImages, String [] dates, String [] placeList) {
        // TODO Auto-generated constructor stub
//        result=prgmNameList;
//        placeDate = dates;
//        placeNames = placeList;
        context = contxt;
        mSelectedItemsIds = new SparseBooleanArray();

//        imageId=prgmImages;

        resultList = new ArrayList<String>();
        placeDateList = new ArrayList<String>();
        placeNameList = new ArrayList<String>();
        imageIdList = new ArrayList<Integer>();

        for(int i=0;i<prgmImages.length;i++)
        {
            resultList.add(prgmNameList[i]);
            placeDateList.add(dates[i]);
            placeNameList.add(placeList[i]);
            imageIdList.add(Integer.valueOf(prgmImages[i]));
        }

        this.mainActivityContext = mainActivityContext;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView dt;
        TextView nm;
        ImageView del;
        ImageView img;
        ImageView share;
        ImageView wapp;
        ImageView fb;
        TextView shareAsEvent, shareAsAddress;
        LinearLayout imL, textL, delL;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        holder=new Holder();
        View rowView;
        this.position = position;

        rowView = inflater.inflate(R.layout.activity_listview, null);
        Typeface robo = Typeface.createFromAsset(mainActivityContext.getAssets(), "roboto.ttf");
        holder.nm=(TextView) rowView.findViewById(R.id.textView1);
//        holder.nm.setTypeface(robo);
        holder.dt=(TextView) rowView.findViewById(R.id.date);
        holder.dt.setTypeface(robo);
        holder.tv=(TextView) rowView.findViewById(R.id.textView3);
        holder.tv.setTypeface(robo);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.del=(ImageView) rowView.findViewById(R.id.imageView7);
        holder.share=(ImageView) rowView.findViewById(R.id.imageView8);
        holder.imL = (LinearLayout) rowView.findViewById(R.id.imgL);
        holder.textL = (LinearLayout) rowView.findViewById(R.id.textL);
      //  holder.delL = (LinearLayout) rowView.findViewById(R.id.delL);
        holder.fb = (ImageView) rowView.findViewById(R.id.imageView3);
        holder.shareAsEvent = (TextView) rowView.findViewById(R.id.shareAsEvent);
       // holder.shareAsEvent.setTypeface(robo);
        holder.shareAsAddress = (TextView) rowView.findViewById(R.id.shareAsAddress);
       // holder.shareAsAddress.setTypeface(robo);
        // holder.wapp = (ImageView) rowView.findViewById(R.id.imageView4);
        holder.tv.setText(resultList.get(position));
        holder.nm.setText(placeNameList.get(position) );
        holder.dt.setText(placeDateList.get(position) );
        holder.img.setImageResource(imageIdList.get(position).intValue());


        holder.del.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED){
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }
                   // AlertDialog diaBox = AskOption(position);
                    //diaBox.show();
                    deletePointInDialog(position);
                }
                catch (Exception ex){
                    Toast.makeText(context,"Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
                }

            }
        });


        holder.fb.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED){
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }
                    postOnFb(position);
                }
                catch (Exception ex){
                    Toast.makeText(context,"Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
                }

            }
        });



        holder.share.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED){
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }
                    showShareOpts(position);
                }
                catch (Exception ex){
                    Toast.makeText(context,"Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.shareAsEvent.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED){
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }

                    showShareAsEventDialog(position);
                }
                catch (Exception ex){
                    Toast.makeText(context,"Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.shareAsAddress.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED){
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }

                    showShareAsAddressDialog(position);
                }
                catch (Exception ex){
                    Toast.makeText(context,"Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED){
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }
                    handleShowMap(position);
                }
                catch (Exception ex){
                    Toast.makeText(context,"Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.textL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED){
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }
                    handleShowMap(position);
                }
                catch (Exception ex){
                    Toast.makeText(context,"Internal error occured. Try again !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rowView;
    }

    public void remove(int position)
    {
        deletePointIn(position);
    }

    public String sharePlaceInGroup(int position)
    {
        return sharePlaceInfo(position);
    }

    private String sharePlaceInfo(int position){
        int i = 0;
        String shareLat = "" , shareLang = "", shareDesc = "", shareName = "";
        String filename = "pointins.txt";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {

                stringBuffer.append(inputString + "\n");
                if (i == position) {
                    String[] items = inputString.split("\\*");
                    String latD = "";
                    String longD = "";
                    if (items.length > 0) {
                        shareLat = items[2];
                        shareLang = items[3];
                        shareDesc =  items[1];
                        shareName = items[0];
                    }
                    break;
                }
                i++;
            }
        }
        catch (IOException e) {
        }

        String separator = " ";
        try{

            separator = System.getProperty("line.separator");
        }
        catch (Exception ex){

        }


            String shareMessage = shareName + " : " + shareDesc + separator + " Place : " + Uri.parse("http://maps.google.com/maps?q=" + shareLat + "," + shareLang).toString() +
                    separator ;
        return shareMessage;

    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }



    public void postOnFb(int pos){

        int i = 0;
        String shareLat = "" , shareLong = "", shareDesc = "", shareName = "";
        String filename = "pointins.txt";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {

                stringBuffer.append(inputString + "\n");
                if (i == pos) {
                    String[] items = inputString.split("\\*");
                    String latD = "";
                    String longD = "";
                    if (items.length > 0) {
                        shareLat = items[2];
                        shareName = items[0];
                        shareLong = items[3];
                        shareDesc =  items[1];
                    }
                    break;
                }
                i++;
            }
        }
        catch (IOException e) {
        }


        try{
            CallbackManager callbackManager;
            ShareDialog shareDialog;
            FacebookSdk.sdkInitialize(context);
            shareDialog = new ShareDialog((MainActivity)mainActivityContext);
//


            //Toast.makeText(getApplicationContext(),Boolean.toString(ShareDialog.canShow(ShareLinkContent.class)) , Toast.LENGTH_SHORT).show();
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                Resources res = context.getResources();
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(shareName + " : " + shareDesc)
                        .setContentDescription( "Shared using easy to use PointIn app!")
                        .setContentUrl(Uri.parse(Uri.parse("http://maps.google.com/maps?q=" + shareLat + "," + shareLong).toString()))
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag("#PointIn")
                                .build())
                        .build();

              //  shareDialog.show(linkContent);
                if(isMobileDataEnable()){
                    shareDialog.show(linkContent);
                }
                else{
                    Toast.makeText(context,"No internet connection!", Toast.LENGTH_LONG).show();
                }

            }
        }
        catch(Exception ex){
            Toast.makeText(context,"Could not post, please try again !", Toast.LENGTH_LONG).show();
        }

    }

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




    private void showShareOpts(int position){
        int i = 0;
        String shareLat = "" , shareLang = "", shareDesc = "", shareName = "";
        String filename = "pointins.txt";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {

                stringBuffer.append(inputString + "\n");
                if (i == position) {
                    String[] items = inputString.split("\\*");
                    String latD = "";
                    String longD = "";
                    if (items.length > 0) {
                        shareLat = items[2];
                        shareLang = items[3];
                        shareDesc =  items[1];
                        shareName = items[0];
                    }
                    break;
                }
                i++;
            }
        }
        catch (IOException e) {
        }

        String separator = " ";
        try{

            separator = System.getProperty("line.separator");
        }
        catch (Exception ex){

        }

        try {
            String shareMessage = shareName + " : " + shareDesc + separator + " Place : " + Uri.parse("http://maps.google.com/maps?q=" + shareLat + "," + shareLang).toString() +
                    separator + separator + "I had used easy to use PointIn app to save this place. " + Uri.parse("https://play.google.com/store/apps/details?id=com.PointIn.User.pointin").toString();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            intent.setType("text/plain");
            context.startActivity(Intent.createChooser(intent, "Share place using:"));
        }
        catch (Exception ex){
            Toast.makeText(context,"Could not share the place, please try again !",
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
        This method contains pointin deletetion logic and getting called in remove() and deletePointInDialog().

     */
    public void deletePointIn(int delpos)
    {
                    int i = 0;
                    String filename = "pointins.txt";
                    StringBuffer stringBuffer = new StringBuffer();
                    try {

                        //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                        String inputString;

                        //Reading data line by line and storing it into the stringbuffer
                        while ((inputString = inputReader.readLine()) != null) {

                            if (i != delpos) {
                                stringBuffer.append(inputString + "\n");
                            } else {

                            }
                            i++;
                        }

                    } catch (IOException e) {
                    }

                    // delete and write back
                    FileOutputStream fos;
                    try {
                        fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        //default mode is PRIVATE, can be APPEND etc.
                        fos.write(stringBuffer.toString().getBytes());
                        fos.close();

                        resultList.remove(delpos);
                        placeNameList.remove(delpos);
                        placeDateList.remove(delpos);
                        imageIdList.remove(delpos);
                        notifyDataSetChanged();

                    } catch (FileNotFoundException e) {
                        Toast.makeText(context, "Could not delete the place. Try again !",
                                Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(context, "Could not delete the place. Try again !",
                                Toast.LENGTH_LONG).show();
                    }
    }


    public void deletePointInDialog(final int delpos)
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.
        dialog.setContentView(R.layout.deletedialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        Button no ,yes;


        yes = (Button) dialog.findViewById(R.id.yesB);
        no = (Button) dialog.findViewById(R.id.noB);

        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(25);
                    }
                }
                catch (Exception ex){

                }
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(25);
                        }
                        deletePointIn(delpos);
                        Toast.makeText(context, "Place deleted succesfully.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                }
                catch (Exception ex){
                    Toast.makeText(context, "Could not delete the place. Try again !",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }


    public void showShareAsAddressDialog(final int pos){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.
            dialog.setContentView(R.layout.addressdialog);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialog.getWindow().setAttributes(lp);
            Button button, send, cancel;

            cancel = (Button) dialog.findViewById(R.id.cancelB);
            send = (Button) dialog.findViewById(R.id.sendB);

            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(25);
                        }
                    } catch (Exception ex) {
                    }
                    dialog.dismiss();
                }
            });

            send.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(25);
                        }
                        String nameA = ((EditText) dialog.findViewById(R.id.addressName)).getText().toString();
                        String numberA = ((EditText) dialog.findViewById(R.id.addressNumber)).getText().toString();

                        String separator = " ";
                        try {

                            separator = System.getProperty("line.separator");
                        } catch (Exception ex) {

                        }


                        if (nameA.equals("")) {
                            Toast.makeText(context, "Provide person name for address.", Toast.LENGTH_LONG).show();
                        } else {
                            String eventDetails = "Person name : " + nameA;


                            if (numberA.equals("")) {
                                eventDetails += separator;
                            } else {
                                eventDetails += separator + "Contact number : " + numberA + separator;
                            }


                            dialog.dismiss();

                            int i = 0;
                            String shareLat = "", shareLong = "", shareDesc = "", shareName = "";
                            String filename = "pointins.txt";
                            StringBuffer stringBuffer = new StringBuffer();
                            try {
                                BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                                String inputString;
                                while ((inputString = inputReader.readLine()) != null) {

                                    stringBuffer.append(inputString + "\n");
                                    if (i == pos) {
                                        String[] items = inputString.split("\\*");
                                        String latD = "";
                                        String longD = "";
                                        if (items.length > 0) {
                                            shareLat = items[2];
                                            shareLong = items[3];
                                            shareDesc = items[1];
                                        }
                                        break;
                                    }
                                    i++;
                                }
                            } catch (IOException e) {
                            }


                            try {

                                eventDetails += "Address location : " + Uri.parse("http://maps.google.com/maps?q=" + shareLat + "," + shareLong).toString();


                                String shareMessage = separator + "I had used easy to use PointIn app  to share this address with location." + Uri.parse("https://play.google.com/store/apps/details?id=com.PointIn.User.pointin").toString();

                                eventDetails += separator + shareMessage;
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT, eventDetails);
                                intent.setType("text/plain");
                                context.startActivity(Intent.createChooser(intent, "Share address using:"));
                            } catch (Exception ex) {
                                Toast.makeText(context, "Could not share the address, please try again !",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(context, "Could not send, please try again !", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            dialog.show();
        }
        catch (Exception ex){
            Toast.makeText(context, "Could not open share as address dialog, please try again !", Toast.LENGTH_SHORT).show();
        }

    }

    public void showShareAsEventDialog(final int pos){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.
            dialog.setContentView(R.layout.eventdialog);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            dialog.getWindow().setAttributes(lp);
            Button button, send, cancel;

            cancel = (Button) dialog.findViewById(R.id.cancelB);
            send = (Button) dialog.findViewById(R.id.sendB);

            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(25);
                        }
                    } catch (Exception ex) {

                    }

                    dialog.dismiss();
                }
            });

            send.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            vib.vibrate(25);
                        }
                        String nameE = ((EditText) dialog.findViewById(R.id.editText)).getText().toString();
                        String descE = ((EditText) dialog.findViewById(R.id.editText2)).getText().toString();
                        String venueE = ((EditText) dialog.findViewById(R.id.editText3)).getText().toString();
                        String timeE = ((EditText) dialog.findViewById(R.id.eventtime)).getText().toString();

                        String separator = " ";
                        try {

                            separator = System.getProperty("line.separator");
                        } catch (Exception ex) {

                        }


                        if (nameE.equals("")) {
                            Toast.makeText(context, "Enter event name.", Toast.LENGTH_LONG).show();
                        } else if (descE.equals("")) {
                            Toast.makeText(context, "Enter event description.", Toast.LENGTH_LONG).show();
                        } else if (timeE.equals("")) {
                            Toast.makeText(context, "Enter event time.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String eventDetails = "Event Name : " + nameE;
                            eventDetails += separator + "Event Description : " + descE;
                            eventDetails += separator + "Event Time : " + timeE;
                            if (venueE.equals("")) {
                                eventDetails += separator;
                            } else {
                                eventDetails += separator + "Event Venue : " + ((EditText) dialog.findViewById(R.id.editText3)).getText() + separator;
                            }


                            dialog.dismiss();

                            int i = 0;
                            String shareLat = "", shareLong = "", shareDesc = "", shareName = "";
                            String filename = "pointins.txt";
                            StringBuffer stringBuffer = new StringBuffer();
                            try {
                                BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                                String inputString;
                                while ((inputString = inputReader.readLine()) != null) {

                                    stringBuffer.append(inputString + "\n");
                                    if (i == pos) {
                                        String[] items = inputString.split("\\*");
                                        String latD = "";
                                        String longD = "";
                                        if (items.length > 0) {
                                            shareLat = items[2];
                                            shareLong = items[3];
                                            shareDesc = items[1];
                                        }
                                        break;
                                    }
                                    i++;
                                }
                            } catch (IOException e) {
                            }


                            try {

                                eventDetails += "Event Location : " + Uri.parse("http://maps.google.com/maps?q=" + shareLat + "," + shareLong).toString();


                                String shareMessage = separator + "I had used easy to use PointIn app  to share this event with location." + Uri.parse("https://play.google.com/store/apps/details?id=com.PointIn.User.pointin").toString();

                                eventDetails += separator + shareMessage;
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT, eventDetails);
                                intent.setType("text/plain");
                                context.startActivity(Intent.createChooser(intent, "Share place using:"));
                            } catch (Exception ex) {
                                Toast.makeText(context, "Could not share the place, please try again !",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception ex) {
                        Toast.makeText(context, "Could not send, please try again !", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            dialog.show();
        }
        catch (Exception ex){
            Toast.makeText(context, "Could not open share as event dialog, please try again !", Toast.LENGTH_SHORT).show();
        }

    }

    private void shareOnWapp(int position){

        int i = 0;
        String shareLat = "" , shareLong = "", shareDesc = "", shareName = "";
        String filename = "pointins.txt";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {

                stringBuffer.append(inputString + "\n");
                if (i == position) {
                    String[] items = inputString.split("\\*");
                    String latD = "";
                    String longD = "";
                    if (items.length > 0) {
                        shareLat = items[2];
                        shareLong = items[3];
                        shareDesc =  items[1];
                        shareName =  items[0];
                    }
                    break;
                }
                i++;
            }
        }
        catch (IOException e) {
        }


        String separator = " ";
        try{

            separator = System.getProperty("line.separator");
        }
        catch (Exception ex){

        }

        try {
            String whatsAppMessage =  "Place description : " + shareDesc + separator + " Place location: " + Uri.parse("http://maps.google.com/maps?q=" + shareLat + "," + shareLong).toString()
                    + separator + "I had used easy to use PointIn app to save this place. " + Uri.parse("https://play.google.com/store/apps/details?id=com.PointIn.User.pointin").toString();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
            intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_STREAM,uri);
//        intent.setType("image/jpeg");
            intent.setPackage("com.whatsapp");
            context.startActivity(intent);
        }
        catch (Exception ex){
            Toast.makeText(context,"Could not share this place on whatsApp, please try again !",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void handleShowMap(int position){
        // TODO Auto-generated method stub

         final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
        final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Connection detector class
        int status = context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                context.getPackageName());
        if (status == PackageManager.PERMISSION_GRANTED) {
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
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
        }

        String latitude = "" , longitude = "";
        boolean isGPSEnabled = false;

        boolean isNetworkEnabled = false;
        boolean canGetLocation = false;

        if(checkPermissionFineLoc() || checkPermissionCoarseLoc()){
                try{
                    isGPSEnabled = lm
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);

                    // Getting network status
                    isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                }
                catch (Exception ex){
                }
        }

        if (!isGPSEnabled && !isNetworkEnabled) { }
        else {

            canGetLocation = true;
            if (isNetworkEnabled) {
                String getLoc = getLocationByNetwork(context);
                if (getLoc != null) {
                    String[] locAr = getLoc.split("\\*");
                    latitude = locAr[0];
                    longitude = locAr[1];

                }
            }
            // If GPS enabled, get latitude/longitude using GPS Services
            if (isGPSEnabled && (latitude == null || longitude == null)) {
                String getLoc = getLocationByGPS(context);

                if (getLoc != null) {
                    String[] locAr = getLoc.split("\\*");
                    latitude = locAr[0];
                    longitude = locAr[1];
                }
            }
        }



        int i = 0;
        String filename = "pointins.txt";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
            String inputString;
            //           Reading data line by line and storing it into the stringbuffer
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
                if(i == position){
                    String [] items = inputString.split("\\*");
                    String latD = "";
                    String longD = "";
                    if(items.length > 0){
                        latD = items[2];
                        longD = items[3];
                    }

                    if(latD == null || longD == null){
                        Toast.makeText(context,"No location found. Better delete this pointin to free up the space !",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(latitude.equals(latD) && longitude.equals(longD)){
                            Toast.makeText(context,"You are already at this place !",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(latitude.equals("") || longitude.equals("")){
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?q="+ latD +"," + longD ));
                                context.startActivity(intent);
                        }
                            else{
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr=" +  latitude +","+ longitude +  "&daddr="+ latD +"," + longD ));
                                context.startActivity(intent);
                            }

                        }


                    }

                }
                i++;
            }

        } catch (IOException e) {
        }


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


    public static String getLocationByNetwork(Context context) {
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
        int status = context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                context.getPackageName());
        if (status == PackageManager.PERMISSION_GRANTED) {
            LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            mgr.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
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
            if (providers != null && providers.contains(LocationManager.NETWORK_PROVIDER)) {
                Location loc = mgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (loc != null) {
                    return loc.getLatitude() + "*" + loc.getLongitude();
                }
            }
        }
        return null;
    }

    public static String getLocationByGPS(Context context) {
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
        int status = context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName());
        if (status == PackageManager.PERMISSION_GRANTED) {
            LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            mgr.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
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

    private AlertDialog AskOption(final int pos)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to delete this PointIn ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        int i = 0;
                        String filename = "pointins.txt";
                        StringBuffer stringBuffer = new StringBuffer();
                        try {
                            //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
                            BufferedReader inputReader = new BufferedReader(new InputStreamReader(context.openFileInput(filename)));
                            String inputString;
                            //           Reading data line by line and storing it into the stringbuffer
                            while ((inputString = inputReader.readLine()) != null) {

                                if(i != pos){
                                    stringBuffer.append(inputString + "\n" );
                                }
                                else{

                                }
                                i++;
                            }

                        } catch (IOException e) {
                        }

                        // delete and write back
                        FileOutputStream fos;
                        try {
                            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                            //default mode is PRIVATE, can be APPEND etc.
                            fos.write(stringBuffer.toString().getBytes());
                            fos.close();
                            Toast.makeText(context,"Place deleted succesfully.",
                                    Toast.LENGTH_LONG).show();
//                            Intent myIntent = new Intent(context, PointinsList.class);
  //                          ((Activity)context).finish();
    //                        context.startActivity(myIntent);
                      //      ListFragment
//                            ListFragment fragment = new ListFragment() ;
//                            fragment.ref
//                            android.support.v4.app.FragmentTransaction fragmentTransaction =  new MainActivity().getSupportFragmentManager().beginTransaction();
//
//                            fragmentTransaction.replace(R.id.fragment_container, fragment);
//                            fragmentTransaction.commit();

                        } catch (FileNotFoundException e) {
                            Toast.makeText(context,"Could not delete the place. Try again !",
                                    Toast.LENGTH_LONG).show();
                        }
                        catch (IOException e) {
                            Toast.makeText(context,"Could not delete the place. Try again !",
                                    Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
