package com.trukker.trukkershipperuae.FirebaseService;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.MainActivity;
import com.trukker.trukkershipperuae.badgers.ShortcutBadger;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "MyFirebaseMsgService";
    String json_save;
    UserFunctions UF;
    SessionManager sm;
    String mess_counter="";
    Notification notification;
        Activity a;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        // Check if message contains a notification payload.

        try {
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Log.d(TAG, "From: " + remoteMessage.getFrom());
                sendNotification(remoteMessage.getData().get("alert"));
            }
        } catch (Exception e) {
            Log.e("error_is", e.toString());
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts 
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Constants.notification_open=1;
        intent.putExtra("from", "splash");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        int color = ContextCompat.getColor(getApplicationContext(), R.color.app_orange_dark);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.appicon);
        notification = new Notification.Builder(getApplicationContext())


                .setSmallIcon(R.drawable.appicon)
                .setLargeIcon(bitmap)
                .setContentTitle("Trukker Shipper")
                .setContentText(messageBody)
                .setAutoCancel(true)
               // .setColor(color)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .getNotification();


//burger
/////////////////////////////////////////////////////////////////////////////////////


        UF=new UserFunctions(getApplicationContext());
        sm=new SessionManager(getApplicationContext());
        new GetJson_save_mail().execute();
    }

    class GetJson_save_mail extends AsyncTask<Void, Void, String> {



        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            try {


                prmsLogin.put("AppName", "TrukkerUAE");
                prmsLogin.put("UniqueId",sm.getUniqueId());



            } catch (JSONException e)
            {

                e.printStackTrace();
            }

            json_save = UF.RegisterUser("Login/GetUnreadCount", prmsLogin);

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            if (json_save.equals("lost")) {
                UF.msg("Connection Problem.");
            } else {
                if (json_save.equalsIgnoreCase("0"))
                {
                    UF.msg("Invalid");
                } else {
                    try {
                        JSONObject jobj = new JSONObject(json_save);
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        Log.e("contaus mail......>",status.toString());
                        Log.e("contaus mail......>",message.toString());
                        if (status.equalsIgnoreCase("1"))
                        {
                            int m_counter=Integer.parseInt(message.toString());

                            try
                            {
                                a.finish();

                            }
                            catch (Exception e)
                            {

                            }
                           // Constants.notification_move=1;
                            boolean success = ShortcutBadger.applyCount(getApplication(), m_counter);
                            notification.flags |= Notification.FLAG_AUTO_CANCEL;
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            manager.notify(m_counter, notification);
                        }
                        else
                        {
                            UF.msg(message + "");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


        }
    }


}

