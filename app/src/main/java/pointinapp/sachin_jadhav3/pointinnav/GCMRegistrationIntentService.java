package pointinapp.sachin_jadhav3.pointinnav;

/**
 * Created by vivek on 7/4/2016.
 */
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GCMRegistrationIntentService extends IntentService {

    //Constants for success and errors
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    public GCMRegistrationIntentService() {
        super("");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //Registering gcm to the device
        registerGCM();
    }

    private void registerGCM() {

        //Registration complete intent initially null
        Intent registrationComplete = null;

        //we will get the token on successfull registration
        String token = null;
        try {

            //Creating an instanceid
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            //Getting the token from the instance id, save it in database.
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //mapping token with mobile number at database side.
            URL requestPointin = new URL("http://1.11.192.168:8000/register?phone=9561598599&token="+token);
            HttpURLConnection connection = (HttpURLConnection) requestPointin.openConnection();
            connection.setRequestMethod("GET");

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                registrationComplete = new Intent(REGISTRATION_SUCCESS);

                String line = "", result = "";
                BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line=br.readLine()) != null) {
                    result += line;
                }

                //Putting the token to the intent
                registrationComplete.putExtra("token", result + token);
            }
            else
                registrationComplete = new Intent(REGISTRATION_ERROR);

        } catch (Exception e) {
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}