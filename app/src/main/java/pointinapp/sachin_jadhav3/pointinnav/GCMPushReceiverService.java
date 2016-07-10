package pointinapp.sachin_jadhav3.pointinnav;

/**
 * Created by vivek on 7/4/2016.
 */


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService {

    public static final String ACTION_1 = "REQUEST";
    public static final String ACTION_2 = "SHARE";

    private Bundle bundle;

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {

        bundle = data;
        //Getting the message from the bundle
        String message = data.getString("message");
        //Displaying a notiffication with the message
        sendNotification(message);
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String message) {

        int requestCode = 0;

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logohand)
                .setContentText(message)
                .setAutoCancel(true);

        if(bundle.get("type").equals(MainActivity.REQUEST)) {

            Intent requestIntent = new Intent(this, NotificationActionService.class).setAction(ACTION_1);
            PendingIntent requestPendingIntent = PendingIntent.getService(this, 0, requestIntent, PendingIntent.FLAG_ONE_SHOT);
            noBuilder.addAction(new NotificationCompat.Action(R.drawable.logohand, "Share Location", requestPendingIntent));
        }
        else
        {
            Intent shareIntent = new Intent(this, NotificationActionService.class).setAction(ACTION_2);
            PendingIntent sharePendingIntent = PendingIntent.getService(this, 0, shareIntent, PendingIntent.FLAG_ONE_SHOT);
            noBuilder.addAction(new NotificationCompat.Action(R.drawable.logohand, "View Location", sharePendingIntent));
        }


        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build());
    }


    public static class NotificationActionService extends IntentService
    {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent)
        {
            String action = intent.getAction();
            if (ACTION_1.equals(action))
            {

                ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
            }

            if(ACTION_2.equals(action))
            {
                ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);

            }
        }
    }
}