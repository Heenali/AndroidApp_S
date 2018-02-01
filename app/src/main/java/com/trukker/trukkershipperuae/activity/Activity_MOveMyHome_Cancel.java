package com.trukker.trukkershipperuae.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.trukker.trukkershipperuae.Bubble_layout.ArrowDirection;
import com.trukker.trukkershipperuae.Bubble_layout.BubbleLayout;
import com.trukker.trukkershipperuae.Bubble_layout.BubblePopupHelper;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.adapter.DocumentAdapter;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.httpsrequest.HTTPUtils;
import com.trukker.trukkershipperuae.model.Document_method;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class Activity_MOveMyHome_Cancel extends AppCompatActivity {
    static public ViewPager pager;
    String jsonLogin;
    EditText reason_edittext;
    Button submit;
    String json_save;
    String android_id;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    String shipper_id,load_inquiry_no;
    TextView back;

    ListView listview_hometype_ho;
    ArrayList<Document_method> actorsList_ho;
    DocumentAdapter adapter_ho;
    BubbleLayout bubbleLayout_hometype;
    ProgressDialog  loading;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //Intent i = new Intent(Activity_MOveMyHome_Cancel.this, MainActivity.class);
           // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           // startActivity(i);
           Activity_MOveMyHome_Cancel.this.overridePendingTransition( R.anim.slide_in, R.anim.slide_out);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movemyhome_cancel);
        init();
        reason_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //CustomDialogClass_hometype hometypeclass = new CustomDialogClass_hometype(Activity_MOveMyHome_Cancel.this);
                //hometypeclass.show();


                if (cd.isConnectingToInternet())
                {
                    bubbleLayout_hometype = (BubbleLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_hometype, null);
                    final PopupWindow popupWindow;
                    popupWindow = BubblePopupHelper.create(getApplicationContext(), bubbleLayout_hometype);

                    listview_hometype_ho = (ListView) bubbleLayout_hometype.findViewById(R.id.listView);
                    bubbleLayout_hometype.setVisibility(v.GONE);
                    loading = new ProgressDialog(getApplication());
                    try {
                        loading = new ProgressDialog(Activity_MOveMyHome_Cancel.this);
                        loading.getWindow().setBackgroundDrawable(new

                                ColorDrawable(android.graphics.Color.TRANSPARENT));
                        loading.setIndeterminate(true);
                        loading.setCancelable(true);
                        loading.show();
                        loading.setContentView(R.layout.my_progress);

                        String url = UserFunctions.URL + "shipper/GetCancellationReasonsDetails";
                        SyncMethod_hometype(url);


                    }
                    catch (Exception e)
                    {

                    }

                    listview_hometype_ho.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                long id) {
                            String CatID = actorsList_ho.get(position).getId();
                            String CatName = actorsList_ho.get(position).getName();

                            String Hometypename_store = CatName;
                            String Hometypeid_store = CatID;
                            reason_edittext.setText(Hometypename_store);
                            //  Constants.store_Hometypeid=Hometypeid_store;
                            // Constants.store_Hometypename=Hometypename_store;
                            reason_edittext.setError(null);

                            // Toast.makeText(getContext(),Hometypename_store+"  "+Hometypeid_store,Toast.LENGTH_SHORT).show();

                            popupWindow.dismiss();

                            // Toast.makeText(getContext(),Hometypename_store+"  "+Hometypeid_store,Toast.LENGTH_SHORT).show();


                        }
                    });


                    int[] location = new int[2];
                    v.getLocationInWindow(location);

                    bubbleLayout_hometype.setArrowDirection(ArrowDirection.TOP);

                    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);



                }
                else
                {

                    UF.msg("Check Your Internet Connection.");
                }


            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                if(reason_edittext.getText().toString().equalsIgnoreCase(""))
                {
                    reason_edittext.setError("Select valid Reason");
                    Toast.makeText(getApplicationContext(),"Select valid Reason",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (cd.isConnectingToInternet())
                    {

                        new GetJson_save().execute();
                    }
                    else
                    {
                        UF.msg("Check Your Internet Connection.");
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                Activity_MOveMyHome_Cancel.this.overridePendingTransition(
                        R.anim.slide_in, R.anim.slide_out);
                finish();
            }
        });
    }

    public void init() {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_MOveMyHome_Cancel.this);
        UF = new UserFunctions(Activity_MOveMyHome_Cancel.this);
        cd = new ConnectionDetector(Activity_MOveMyHome_Cancel.this);

        reason_edittext = (EditText) findViewById(R.id.reason_edittext);
        submit = (Button) findViewById(R.id.submit);
        back = (TextView) findViewById(R.id.back);
        Intent i = getIntent();
        shipper_id = i.getStringExtra("shipper_id");
        load_inquiry_no= i.getStringExtra("load_inquiry_no");

    }

    class CustomDialogClass_hometype extends Dialog
   {

        public Context context;
        public Dialog d;
        ListView listview_hometype;
        ProgressDialog loading;
        Button title;
        ArrayList<Document_method> actorsList;
        DocumentAdapter adapter;

        public CustomDialogClass_hometype(Context a) {
            super(a);
            this.context = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_hometype);
            context = this.getContext();
            title = (Button) findViewById(R.id.title);
            title.setText("Select Reason");
            listview_hometype = (ListView) findViewById(R.id.listView);

            loading = new ProgressDialog(context);
            try {


                loading = new ProgressDialog(Activity_MOveMyHome_Cancel.this);
                loading.getWindow().setBackgroundDrawable(new

                        ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setIndeterminate(true);
                loading.setCancelable(true);
                loading.show();
                loading.setContentView(R.layout.my_progress);


                String url = UserFunctions.URL + "shipper/GetCancellationReasonsDetails";
                SyncMethod(url);
            } catch (Exception e) {

            }
            listview_hometype.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long id) {
                    String CatID = actorsList.get(position).getId();
                    String CatName = actorsList.get(position).getName();

                    String Hometypename_store = CatName;
                    String Hometypeid_store = CatID;
                    reason_edittext.setText(Hometypename_store);
                    //  Constants.store_Hometypeid=Hometypeid_store;
                    // Constants.store_Hometypename=Hometypename_store;
                    reason_edittext.setError(null);

                    // Toast.makeText(getContext(),Hometypename_store+"  "+Hometypeid_store,Toast.LENGTH_SHORT).show();
                    dismiss();

                }
            });


        }


        public void SyncMethod(final String GetUrl) {
            Log.i("Url.............", GetUrl);
            final Thread background = new Thread(new Runnable() {
                // After call for background.start this run method call
                public void run() {
                    try {
                        String url = GetUrl;
                        String SetServerString = "";
                        // document all_stuff = null;

                        SetServerString = fetchResult(url);
                        threadMsg(SetServerString);
                    } catch (Throwable t) {
                        Log.e("Animation", "Thread  exception " + t);
                    }
                }

                private void threadMsg(String msg) {

                    if (!msg.equals(null) && !msg.equals("")) {
                        Message msgObj = handler11.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", msg);
                        msgObj.setData(b);
                        handler11.sendMessage(msgObj);
                    }
                }

                // Define the Handler that receives messages from the thread and update the progress
                private final Handler handler11 = new Handler() {
                    public void handleMessage(Message msg) {
                        try {
                            String aResponse = msg.getData().getString("message");
                            Log.e("Exam", "screen>>" + aResponse);
                            aResponse = aResponse.trim();
                            aResponse = aResponse.substring(1, aResponse.length() - 1);
                            aResponse = aResponse.replace("\\", "");
                            loading.cancel();
                            title.setVisibility(View.VISIBLE);
                            listview_hometype.setVisibility(View.VISIBLE);
                            JSONObject get_res = new JSONObject(aResponse);

                            actorsList = new ArrayList<Document_method>();
                            JSONArray array = new JSONArray();

                            array = get_res.getJSONArray("message");
                            Log.e("mess", "screen>>" + array.toString());
                            for (int aa = 0; aa < array.length(); aa++) {


                                Log.e("mess", "screen>>" + array.getJSONObject(aa).getString("description").toString());
                                actorsList.add(new Document_method(array.getJSONObject(aa).getString("id"), array.getJSONObject(aa).getString("description"), array.getJSONObject(aa).getString("description")));


                            }
                            adapter = new DocumentAdapter(Activity_MOveMyHome_Cancel.this, actorsList);
                            listview_hometype.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {

                        }


                    }
                };
            });
            // Start Thread
            background.start();
        }

    }

    public String fetchResult(String url) throws JSONException
    {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith("https"));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpGet getMethod = new HttpGet(newURI);
        try {
            response = httpClient.execute(getMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }
    public static String convertStreamToString(InputStream is) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
    private class GetJson_save extends AsyncTask<Void, Void, String> {

        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_MOveMyHome_Cancel.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);

        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            try {


                prmsLogin.put("load_inquiry_no", load_inquiry_no);
                prmsLogin.put("shipper_id", shipper_id);
                prmsLogin.put("Reason", reason_edittext.getText().toString());


               // jsonArray.put(prmsLogin);

               // prms.put("Shipper/SendCancelOrderRequestByShipper", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prmsLogin + "");
            json_save = UF.RegisterUser("Shipper/SendCancelOrderRequestByShipper", prmsLogin);

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();


            if (json_save.equals("lost")) {
                UF.msg("Connection Problem.");
            } else {
                if (json_save.equalsIgnoreCase("0")) {
                    UF.msg("Invalid");
                } else {
                    try {
                        JSONObject jobj = new JSONObject(json_save);
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1")) {

                            UF.msg(message + "");
                           // Intent i = new Intent(Activity_MOveMyHome_Cancel.this, MainActivity.class);
                          //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           // startActivity(i);
                           // overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            Constants.cancel_conformation="Yes";
                            finish();
                            //UF.msg("Registration Successfully.");

                        } else {
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
    public void SyncMethod_hometype(final String GetUrl) {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
                    // document all_stuff = null;

                    SetServerString = fetchResult(url);
                    threadMsg(SetServerString);
                } catch (Throwable t) {
                    Log.e("Animation", "Thread  exception " + t);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler11 = new Handler() {
                public void handleMessage(Message msg) {
                    try {
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam", "screen>>" + aResponse);
                        aResponse = aResponse.trim();
                        aResponse = aResponse.substring(1, aResponse.length() - 1);
                        aResponse = aResponse.replace("\\", "");
                        loading.cancel();

                        bubbleLayout_hometype.setVisibility(View.VISIBLE);
                        JSONObject get_res = new JSONObject(aResponse);

                        actorsList_ho = new ArrayList<Document_method>();
                        JSONArray array = new JSONArray();

                        array = get_res.getJSONArray("message");
                        Log.e("mess", "screen>>" + array.toString());
                        for (int aa = 0; aa < array.length(); aa++) {


                            Log.e("mess", "screen>>" + array.getJSONObject(aa).getString("description").toString());
                            actorsList_ho.add(new Document_method(array.getJSONObject(aa).getString("id"), array.getJSONObject(aa).getString("description"), array.getJSONObject(aa).getString("description")));


                        }
                        adapter_ho = new DocumentAdapter(Activity_MOveMyHome_Cancel.this, actorsList_ho);
                        listview_hometype_ho.setAdapter(adapter_ho);
                        adapter_ho.notifyDataSetChanged();

                    } catch (Exception e) {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }

}

