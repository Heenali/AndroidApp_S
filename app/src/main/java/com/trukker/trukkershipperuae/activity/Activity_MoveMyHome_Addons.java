package com.trukker.trukkershipperuae.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.trukker.trukkershipperuae.Bubble_layout.ArrowDirection;
import com.trukker.trukkershipperuae.Bubble_layout.BubbleLayout;
import com.trukker.trukkershipperuae.Bubble_layout.BubblePopupHelper;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.adapter.DocumentAdapter;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.model.Document_method;
import com.trukker.trukkershipperuae.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by Ravi on 29/07/15.
 */
public class Activity_MoveMyHome_Addons extends AppCompatActivity
{
    String android_id;
    String json_save="";
    String  page="";
    String AddonServices_static="";
    String addonsvalue_price="";
    LinearLayout container_addons;
    BubbleLayout bubbleLayout_hometype;
    ListView listview_hometype_ho;
    ArrayList<Document_method> actorsList_ho;
    DocumentAdapter adapter_ho;
    ProgressDialog  loading;
    String Hometypename_store = "", Hometypeid_store = "", timename_store = "", timeid_store = "";
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    TextView tv_stop_address,tv_start_address,hometype_text;
    LinearLayout dis_layout;
    TextView submit;
    TextView total_value;
    List<String> store_addons_price = new ArrayList<String>();
    List<String> store_addons_view = new ArrayList<String>();
    List<String>  store_addons_spinner = new ArrayList<String>();
    List<String> store_addons = new ArrayList<String>();


    SpinAdapterTimeDelay adap_dri;
    String final_order="N";
    ArrayList<String> id=new ArrayList<String>();
    ArrayList<String> name=new ArrayList<String>();
    String current_addons="";
    TextView txt_discount;
    TextView blinktext;
    TextView back;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Log.d("---------------", "--------------------------");

            Constants.counter_addon=1;
            Constants.store_combodis=txt_discount.getText().toString();
            Constants.store_addonsprice=total_value.getText().toString();
            Constants.AddonServices= AddonServices_static.trim();

            Constants.Home_store_addons = new ArrayList<>(store_addons);
            Constants.Home_store_addons_price = new ArrayList<>(store_addons_price);
            Constants.Home_store_addons_spinner = new ArrayList<>(store_addons_spinner);
            Constants.Home_store_addons_view = new ArrayList<>(store_addons_view);
            String total=total_value.getText().toString();
            total=total.substring(4);
           // Toast.makeText(getApplicationContext(),Constants.promocode_discount_value_store,Toast.LENGTH_LONG).show();

            if(Constants.promocode_store.equalsIgnoreCase(""))
            {
            }
            else
                Constants.promocode_discountstore=total;


            if(page.equalsIgnoreCase("standard"))
            {

                if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                    Constants.store_totalcost_s=total;

                else
                    Constants.store_totalcost_saving=total;

                Intent i = new Intent(Activity_MoveMyHome_Addons.this,Activity_GetQuote_standard.class);
                i.putExtra("page", "standard");
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
            else
            {
                Constants.store_totalcost_p=total;
                Intent i = new Intent(Activity_MoveMyHome_Addons.this,Activity_GetQuote_premium.class);
                i.putExtra("page", "premium");
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }


        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movemyhome_addons);
        init();
       // Toast.makeText(getApplicationContext(),Constants.promocode_store,Toast.LENGTH_LONG).show();
        if(Constants.counter_addon==1)
        {
            Log.d("---------------", "--------------------------");
            Log.d("store_addons_name", Constants.Home_store_addons.toString() );
            Log.d("store_addons_name", Constants.Home_store_addons_price.toString() );
            Log.d("store_addons_name", Constants.Home_store_addons_spinner.toString() );
            Log.d("store_addons_name", Constants.Home_store_addons_view.toString() );
            Log.d("---------------", "--------------------------");
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("---------------", "--------------------------");

                Constants.counter_addon=1;
                Constants.store_combodis=txt_discount.getText().toString();
                Constants.store_addonsprice=total_value.getText().toString();
                Constants.AddonServices= AddonServices_static.trim();

                Constants.Home_store_addons = new ArrayList<>(store_addons);
                Constants.Home_store_addons_price = new ArrayList<>(store_addons_price);
                Constants.Home_store_addons_spinner = new ArrayList<>(store_addons_spinner);
                Constants.Home_store_addons_view = new ArrayList<>(store_addons_view);
                String total=total_value.getText().toString();
                total=total.substring(4);

                if(Constants.promocode_store.equalsIgnoreCase(""))
                {
                }
                else
                    Constants.promocode_discountstore=total;
                if(page.equalsIgnoreCase("standard"))
                {

                    if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                        Constants.store_totalcost_s=total;
                    else
                        Constants.store_totalcost_saving=total;

                    Intent i = new Intent(Activity_MoveMyHome_Addons.this,Activity_GetQuote_standard.class);
                    i.putExtra("page", "standard");
                    startActivity(i);
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    finish();
                }
                else
                {
                    Constants.store_totalcost_p=total;
                    Intent i = new Intent(Activity_MoveMyHome_Addons.this,Activity_GetQuote_premium.class);
                    i.putExtra("page", "premium");
                    startActivity(i);
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    finish();
                }

            }
        });

        try
        {
            loading = new ProgressDialog(Activity_MoveMyHome_Addons.this);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(true);
            loading.show();
            loading.setContentView(R.layout.my_progress);
            SyncMethod_sizetype(UserFunctions.URL+"postorder/GetHomeSizeTypeCode?typecode=s");
            adap_dri=new SpinAdapterTimeDelay(Activity_MoveMyHome_Addons.this, name);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (cd.isConnectingToInternet())
                {
                    final_order="Y";

                    new GetJsonmovemyhome_save().execute();
                }
                else
                {
                    UF.msg("Check Your Internet Connection.");
                }


            }
        });




    }
    public void init()
    {
        Intent i = getIntent();
        page= i.getStringExtra("page");

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        container_addons = (LinearLayout)findViewById(R.id.container);
        sm = new SessionManager(Activity_MoveMyHome_Addons.this);
        UF = new UserFunctions(Activity_MoveMyHome_Addons.this);
        cd = new ConnectionDetector(Activity_MoveMyHome_Addons.this);
        hometype_text= (TextView) findViewById(R.id.hometype_text);
        back=(TextView)findViewById(R.id.back);
        blinktext= (TextView) findViewById(R.id. blinktext);
        tv_stop_address= (TextView) findViewById(R.id.tv_stop_address);
        dis_layout= (LinearLayout) findViewById(R.id.dis_layout);
        dis_layout.setVisibility(View.GONE);
        tv_start_address= (TextView) findViewById(R.id.tv_start_address);
        txt_discount= (TextView) findViewById(R.id.dis_value);
        submit= (TextView) findViewById(R.id.submit);
        total_value=(TextView) findViewById(R.id.total_value);
        tv_start_address.setText(Constants.store_movefrom);
        tv_stop_address.setText(Constants.store_moveto);
        hometype_text.setText(Constants.store_Hometypename);


        blinktext.setText("Save More with TruKKer Add-Ons");
        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(blinktext, "textColor", Color.RED, Color.rgb(34,139,34));
        textColorAnim.setDuration(1000);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.start();


        if(page.equalsIgnoreCase("standard"))
        {
            if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
            {
                total_value.setText("AED "+Constants.store_totalcost_s);
            }
            else
                total_value.setText("AED "+Constants.store_totalcost_saving);
        }
        else
        {
            total_value.setText("AED "+Constants.store_totalcost_p);
        }

    }

    public void SyncMethod_addons_list(final String GetUrl)
    {
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


                        aResponse = aResponse.trim();
                        aResponse= new JSONTokener(aResponse).nextValue().toString();

                        loading.cancel();

                        JSONObject get_res = new JSONObject(aResponse);
                        Log.e("mess", "screen>>" + get_res.toString());
                        JSONArray array = new JSONArray();
                        array = get_res.getJSONArray("message");


                        LayoutInflater layoutInflater =(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                        final List<String> items = Arrays.asList(Constants.store_addonsvalue_price.split(","));


                        final int leng=array.length();

                        //first crete static array
                        if(Constants.counter_addon==1)
                        {

                            store_addons = new ArrayList<>(Constants.Home_store_addons);
                            store_addons_price = new ArrayList<>(Constants.Home_store_addons_price);
                            store_addons_spinner = new ArrayList<>(Constants.Home_store_addons_spinner);
                            store_addons_view = new ArrayList<>(Constants.Home_store_addons_view);
                        }
                        else
                        {

                            for(int i=0;i<leng;i++)
                            {
                                store_addons.add("*");
                                store_addons_price.add("*");
                                store_addons_view.add("N");
                                store_addons_spinner.add("*");
                                Constants.selected_Home_name.add("*");
                                Constants.selected_Home_price.add("*");

                            }


                        }

                        for (int aa = 0; aa < array.length(); aa++)
                        {
                            final int a=aa;
                            final View addView = layoutInflater.inflate(R.layout.list_movemyhome_addons, null);

                            final String desc_display=array.getJSONObject(aa).getString("Description").toString();
                            final TextView name1 = (TextView)addView.findViewById(R.id.name);
                            final CheckBox togglevalue=(CheckBox)findViewById(R.id.tc_condition1) ;
                            final TextView price = (TextView)addView.findViewById(R.id.price);
                            LinearLayout open_layout = (LinearLayout)addView.findViewById(R.id.open_layout);
                            final Spinner sel_size= (Spinner) addView.findViewById(R.id.sel_size);
                            LinearLayout help_layout= (LinearLayout)addView.findViewById(R.id.help_layout);
                            final LinearLayout whole_layout = (LinearLayout)addView.findViewById(R.id.whole_layout);
                            final ImageView arrow_icon= (ImageView) addView.findViewById(R.id.arrow_icon);
                            final LinearLayout feture_layout = (LinearLayout)addView.findViewById(R.id.feture_layout);
                            final TextView hideselection= (TextView)addView.findViewById(R.id.hideselection);

                            //its get Like PT or CL
                            final String s=array.getJSONObject(aa).getString("ServiceTypeCode").toString();
                          //  final String homename=array.getJSONObject(aa).getString("ServiceTypeDescDisplay").toString();
                            name1.setText(array.getJSONObject(aa).getString("ServiceTypeDescDisplay").toString());


                            adap_dri=new SpinAdapterTimeDelay(Activity_MoveMyHome_Addons.this, name);
                            sel_size.setAdapter(adap_dri);

                            //defualt spinner selection and price
                            if(Constants.counter_addon==1)
                            {
                                sel_size.setSelection(id.indexOf(store_addons_spinner.get(a)));
                                price.setText(store_addons_price.get(a));
                                if(store_addons_view.get(a).toString().equalsIgnoreCase("Y"))
                                {
                                    arrow_icon.setBackgroundResource(R.drawable.toggle_on);
                                    feture_layout.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    arrow_icon.setBackgroundResource(R.drawable.toggle_off);
                                    feture_layout.setVisibility(View.GONE);
                                }

                                hideselection.setText(s+"^"+id.get(Integer.parseInt(sel_size.getSelectedItem().toString())));
                                store_addons_price.set(a,price.getText().toString());
                               store_addons_spinner.set(a,id.get(Integer.parseInt(sel_size.getSelectedItem().toString())));

                                Log.d("store_addons_name", store_addons.toString());
                                Log.d("store_addons_price", store_addons_price.toString());
                                Log.d("store_addons_spinner", store_addons_spinner.toString());
                                Log.d("store_addons_view", store_addons_view.toString());

                            }
                            else
                            {
                                sel_size.setSelection(name.indexOf(Constants.store_Hometypename));
                                price.setText("AED "+items.get(aa));

                                feture_layout.setVisibility(View.GONE);

                                hideselection.setText(s+"^"+id.get(Integer.parseInt(sel_size.getSelectedItem().toString())));
                                store_addons_price.set(a,price.getText().toString());
                                store_addons_spinner.set(a,id.get(Integer.parseInt(sel_size.getSelectedItem().toString())));

                                Log.d("store_addons_name", store_addons.toString());
                                Log.d("store_addons_price", store_addons_price.toString());
                                Log.d("store_addons_spinner", store_addons_spinner.toString());
                                Log.d("store_addons_view", store_addons_view.toString());



                            }


                                sel_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                                {
                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
                                    {

                                        try
                                        {


                                                hideselection.setText(s+"^"+id.get(arg2));
                                                Constants.IncludeAddonService="Y";
                                                store_addons.set(a,hideselection.getText().toString());

                                                convert_atos(store_addons.toString());

                                                String str_result= new GetJsonmovemyhome_save().execute().get();

                                                if(str_result.equalsIgnoreCase(""))
                                                    Toast.makeText(getApplicationContext(),str_result,Toast.LENGTH_LONG).show();
                                                else
                                                {
                                                    JSONObject jobj = new JSONObject(str_result);
                                                    JSONArray array=new JSONArray();
                                                    array=jobj.getJSONArray("message");

                                                    String NoOfTruck_s= array.getJSONObject(0).getString("Total_"+s+"_Charge").toString();
                                                    String totalvalue=array.getJSONObject(0).getString("Total_cost").toString();
                                                    String dis= array.getJSONObject(0).getString("TotalAddServiceDiscount").toString();

                                                    txt_discount.setText("- AED "+dis);
                                                    if(Constants.promocode_store.equalsIgnoreCase(""))
                                                    {
                                                        total_value.setText("AED "+totalvalue);
                                                    }
                                                    else
                                                    {
                                                        int val=Integer.parseInt(totalvalue)-Integer.parseInt(Constants.promocode_discount_value_store);
                                                        total_value.setText("AED "+val);

                                                    }

                                                    price.setText("AED "+NoOfTruck_s);
                                                    store_addons_price.set(a,"AED "+NoOfTruck_s);
                                                    store_addons_view.set(a,"Y");
                                                    store_addons_spinner.set(a,id.get(arg2));
                                                    Constants.selected_Home_price.set(a,NoOfTruck_s);
                                                    Log.d("store_addons_name", store_addons.toString());
                                                    Log.d("store_addons_price", store_addons_price.toString());
                                                    Log.d("store_addons_spinner", store_addons_spinner.toString());
                                                    Log.d("store_addons_view", store_addons_view.toString());
                                                }



                                        }
                                        catch (Exception e)
                                        {

                                        }

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0)
                                    {
                                    }
                                });


                            help_layout.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {

                                    PopupWindow popupWindow;
                                    final BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_tooltipwebview, null);
                                    popupWindow = BubblePopupHelper.create(getApplicationContext(), bubbleLayout);
                                    WebView web = (WebView)  bubbleLayout.findViewById(R.id.webview);
                                    int[] location = new int[2];
                                    v.getLocationInWindow(location);
                                    web.getSettings();
                                    web.setBackgroundColor(Color.TRANSPARENT);
                                    web.loadData(desc_display, "text/html", "UTF-8");
                                    bubbleLayout.setArrowDirection(ArrowDirection.TOP);
                                    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);

                                }
                            });
                            whole_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    if (feture_layout.getVisibility() == View.VISIBLE)
                                    {
                                        feture_layout.setVisibility(View.GONE);
                                        Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down2);
                                        feture_layout.startAnimation(anm);
                                        arrow_icon.setBackgroundResource(R.drawable.toggle_off);
                                        //price.setText("AED "+items.get(a));
                                        //remove element from here

                                        String remove_value=hideselection.getText().toString();
                                        Log.e("remove_value-------",remove_value);
                                        Log.e("string have--------",AddonServices_static);
                                        remove_string(remove_value);



                                       if(AddonServices_static.equalsIgnoreCase(""))
                                               Constants.IncludeAddonService="N";
                                       else
                                               Constants.IncludeAddonService="Y";

                                        String str_result= null;
                                        try {
                                            str_result = new GetJsonmovemyhome_save().execute().get();

                                            if(str_result.equalsIgnoreCase(""))

                                                Toast.makeText(getApplicationContext(),str_result,Toast.LENGTH_LONG).show();
                                            else
                                            {
                                                JSONObject jobj = new JSONObject(str_result);
                                                JSONArray array=new JSONArray();
                                                array=jobj.getJSONArray("message");

                                                String NoOfTruck_s= array.getJSONObject(0).getString("Total_"+s+"_Charge").toString();
                                                String totalvalue=array.getJSONObject(0).getString("Total_cost").toString();
                                                String dis= array.getJSONObject(0).getString("TotalAddServiceDiscount").toString();

                                                if(Constants.promocode_store.equalsIgnoreCase(""))
                                                {
                                                    total_value.setText("AED "+totalvalue);
                                                }
                                                else
                                                {
                                                    int val=Integer.parseInt(totalvalue)-Integer.parseInt(Constants.promocode_discount_value_store);
                                                    total_value.setText("AED "+val);
                                                }

                                                txt_discount.setText("- AED "+dis);
                                                //total_value.setText("AED "+totalvalue);
                                                price.setText("AED "+NoOfTruck_s);

                                                store_addons_price.set(a,"AED "+NoOfTruck_s);
                                                store_addons_view.set(a,"N");
                                                store_addons.set(a,"*");
                                                Constants.selected_Home_name.set(a,"*");
                                                Constants.selected_Home_price.set(a,"*");
                                                Log.d("store_addons_name", store_addons.toString());
                                                Log.d("store_addons_price", store_addons_price.toString());
                                                Log.d("store_addons_spinner", store_addons_spinner.toString());
                                                Log.d("store_addons_view", store_addons_view.toString());
                                                Log.d("store_addons_spinner",  Constants.selected_Home_name.toString());
                                                Log.d("store_addons_view",  Constants.selected_Home_price.toString());

                                            }

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        // AddonServices_static.replace(s+"^"+id.get(arg2),"");



                                    }
                                    else
                                    {
                                        feture_layout.setVisibility(View.VISIBLE);
                                        Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                                        feture_layout.startAnimation(anm);
                                        arrow_icon.setBackgroundResource(R.drawable.toggle_on);

                                        //add element from here

                                        try
                                        {

                                            Constants.IncludeAddonService="Y";
                                            store_addons.set(a,hideselection.getText().toString());

                                            convert_atos(store_addons.toString());

                                            String str_result= new GetJsonmovemyhome_save().execute().get();

                                            if(str_result.equalsIgnoreCase(""))

                                                Toast.makeText(getApplicationContext(),str_result,Toast.LENGTH_LONG).show();
                                            else
                                            {
                                                JSONObject jobj = new JSONObject(str_result);
                                                JSONArray array=new JSONArray();
                                                array=jobj.getJSONArray("message");

                                                String NoOfTruck_s= array.getJSONObject(0).getString("Total_"+s+"_Charge").toString();
                                                String totalvalue=array.getJSONObject(0).getString("Total_cost").toString();
                                                String dis= array.getJSONObject(0).getString("TotalAddServiceDiscount").toString();

                                                if(Constants.promocode_store.equalsIgnoreCase(""))
                                                {
                                                    total_value.setText("AED "+totalvalue);
                                                }
                                                else
                                                {
                                                    int val=Integer.parseInt(totalvalue)-Integer.parseInt(Constants.promocode_discount_value_store);
                                                    total_value.setText("AED "+val);
                                                }
                                                txt_discount.setText("- AED "+dis);
                                                //total_value.setText("AED "+totalvalue);
                                                price.setText("AED "+NoOfTruck_s);

                                                store_addons_price.set(a,"AED "+NoOfTruck_s);
                                                store_addons_view.set(a,"Y");
                                                Constants.selected_Home_name.set(a,name1.getText().toString());
                                                Constants.selected_Home_price.set(a,NoOfTruck_s);
                                                Log.d("store_addons_name", store_addons.toString());
                                                Log.d("store_addons_price", store_addons_price.toString());
                                                Log.d("store_addons_spinner", store_addons_spinner.toString());
                                                Log.d("store_addons_view", store_addons_view.toString());
                                                Log.d("store_addons_spinner",  Constants.selected_Home_name.toString());
                                                Log.d("store_addons_view",  Constants.selected_Home_price.toString());


                                            }

                                        }
                                        catch (Exception e)
                                        {

                                        }


                                    }


                                }
                            });


                            container_addons.addView(addView);
                           // container_addons.setTag(addView);


                        }

                    }
                    catch (Exception e)
                    {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }
    public String fetchResult(String urlString) throws JSONException {
        StringBuilder builder;
        BufferedReader reader;
        URLConnection connection = null;
        URL url = null;
        String line;
        builder = new StringBuilder();
        reader = null;
        try {
            url = new URL(urlString);
            connection = url.openConnection();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            //Log.d("DATA", builder.toString());
        } catch (Exception e) {

        }
        //JSONArray arr=new JSONArray(builder.toString());
        return builder.toString();
    }
    public void SyncMethod_sizetype(final String GetUrl)
    {
        Log.i("Url.............",GetUrl);
        final Thread background = new Thread(new Runnable()
        {
            // After call for background.start this run method call
            public void run()
            {
                try
                {
                    String url=GetUrl;
                    String SetServerString = "";


                    SetServerString=fetchResult(url);
                    threadMsg(SetServerString);
                }
                catch (Throwable t)
                {
                    Log.e("Animation", "Thread  exception " + t);
                }
            }
            private void threadMsg(String msg)
            {

                if (!msg.equals(null) && !msg.equals(""))
                {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }
            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler11 = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    try
                    {
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam","screen>>"+aResponse);
                        loading.cancel();
                        aResponse = aResponse.trim();
                        aResponse= new JSONTokener(aResponse).nextValue().toString();
                        JSONObject get_res = new JSONObject(aResponse);

                        JSONArray array = new JSONArray();
                        array = get_res.getJSONArray("message");

                        final String[] countryid = new String[array.length()];
                        final String[] countryname = new String[array.length()];

                        for(int aa=0;aa<array.length();aa++)
                        {
                            countryid[aa]=array.getJSONObject(aa).getString("SizeTypeCode");
                            countryname[aa]=array.getJSONObject(aa).getString("SizeTypeDesc");

                            id.add(array.getJSONObject(aa).getString("SizeTypeCode"));
                            name.add(array.getJSONObject(aa).getString("SizeTypeDesc"));




                        }


                        try
                        {
                            loading = new ProgressDialog(Activity_MoveMyHome_Addons.this);
                            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            loading.setIndeterminate(true);
                            loading.setCancelable(true);
                            loading.show();
                            loading.setContentView(R.layout.my_progress);
                            String url = UserFunctions.URL + "admin/GetAllServices?opt=P";
                            SyncMethod_addons_list(url);


                        }
                        catch (Exception e)
                        {

                        }



                    }
                    catch(Exception e)
                    {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }
    public class SpinAdapterTimeDelay extends BaseAdapter
    {
        ArrayList<String> list;
        Context a;
        private LayoutInflater mInflater;
        public SpinAdapterTimeDelay(Context cc,ArrayList<String> list) {
            this.a=cc;
            this.list=list;
            this.mInflater=(LayoutInflater)a.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View rowView = mInflater.inflate(R.layout.list_row, parent, false);


            TextView item = (TextView) rowView.findViewById(R.id.textView2);
            // Toast.makeText(a, String.valueOf(list.get(position)),Toast.LENGTH_LONG).show();
            item.setText(list.get(position));
            return rowView;
        }

    }
    public class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog  loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_MoveMyHome_Addons.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            int requiredproce=0;
            try
            {
                requiredproce=Integer.parseInt(Constants.promocode_discountstore)+Integer.parseInt(Constants.promocode_discount_value_store);
                Log.e("requiredprice value",requiredproce+"");
            }
            catch (Exception e)
            {

            }

            try
            {
                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("Area", Constants.store_area);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");

                if(page.equalsIgnoreCase("standard"))
                {
                    if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                    {

                        prmsLogin.put("NoOfTruck", Constants.store_NoOfTruck_s);
                        prmsLogin.put("NoOfDriver", "");
                        prmsLogin.put("NoOfLabour", Constants.store_NoOfLabour_s);
                        prmsLogin.put("NoOfHandiman", Constants.store_NoOfHandiman_s);

                        prmsLogin.put("rate_type_flag", "B");
                        prmsLogin.put("IncludePackingCharge",  Constants.packing_chcek_s);


                    }
                    else
                    {

                        prmsLogin.put("IncludePackingCharge", Constants.packing_chcek_s);
                        prmsLogin.put("rate_type_flag", "S");

                        prmsLogin.put("NoOfTruck", Constants.store_NoOfTruck_saving);
                        prmsLogin.put("NoOfDriver", "");
                        prmsLogin.put("NoOfLabour", Constants.store_NoOfLabour_saving);
                        prmsLogin.put("NoOfHandiman", Constants.store_NoOfHandiman_saving);


                    }

                }
                else
                {
                    prmsLogin.put("rate_type_flag", "P");
                    prmsLogin.put("IncludePackingCharge","Y");

                    prmsLogin.put("NoOfTruck", Constants.store_NoOfTruck_p);
                    prmsLogin.put("NoOfDriver", "");
                    prmsLogin.put("NoOfLabour", Constants.store_NoOfLabour_p);
                    prmsLogin.put("NoOfHandiman", Constants.store_NoOfHandiman_p);


                }
                //////////////////

                prmsLogin.put("shipper_id", Constants.store_shipperid);
                prmsLogin.put("email_id", Constants.store_email);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);

                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng",Constants.postloadsourceLong);
                prmsLogin.put("source_pincode", "");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time);
                prmsLogin.put("inquiry_destination_addr", Constants.store_moveto);
                prmsLogin.put("inquiry_destination_city", Constants.postload_dest_city);
                prmsLogin.put("inquiry_destionation_lat", Constants.postloaddestLat);
                prmsLogin.put("inquiry_destionation_lng", Constants.postloaddestLong);
                prmsLogin.put("destination_pincode", "");
                prmsLogin.put("remarks", "");


                prmsLogin.put("payment_mode", final_order);
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");

                if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                {

                    prmsLogin.put("required_price", requiredproce);
                }
                else
                {

                    prmsLogin.put("required_price", requiredproce);
                }


                prmsLogin.put("Isupdate", "N");
                prmsLogin.put("order_type_flag", "H");
                prmsLogin.put("TruckTypeCode", "");
                prmsLogin.put("goods_type_flag", "Y");
                prmsLogin.put("promocode", Constants.promocode_store);
                prmsLogin.put("Isupdatebillingadd","N");
                prmsLogin.put("IncludeAddonService", Constants.IncludeAddonService);
                prmsLogin.put("AddonServices", AddonServices_static.trim());

                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("postorder/SaveMovingHomeDetails", prms);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }


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

                        Log.e("standard get normal", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1"))
                        {


                            JSONArray array=new JSONArray();
                            array=jobj.getJSONArray("message");
                            Log.e("array",array.toString());
                            String dis= array.getJSONObject(0).getString("TotalAddServiceDiscount").toString();

                            Log.e("array0",array.getJSONObject(0).getString("Total_cost").toString());

                            String NoOfTruck_s= array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_s= array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_s= array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_s= array.getJSONObject(0).getString("BaseRate").toString();
                            String TotalLabourRate_s= array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_s= array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_s= array.getJSONObject(0).getString("TotalPackingRate").toString();
                            String totlecost_stan=array.getJSONObject(0).getString("Total_cost").toString();
                            String billing_name=array.getJSONObject(0).getString("billing_name").toString();
                            String billing_add=array.getJSONObject(0).getString("billing_add").toString();
                            String source_full_add=array.getJSONObject(0).getString("source_full_add").toString();
                            String destination_full_add=array.getJSONObject(0).getString("destination_full_add").toString();


                            List<String> items = Arrays.asList(Constants.store_addonsvalue.split(","));

                            if(Constants.promocode_store.equalsIgnoreCase(""))
                            {
                                total_value.setText("AED "+array.getJSONObject(0).getString("Total_cost").toString());
                            }
                            else
                            {
                                int val=Integer.parseInt(array.getJSONObject(0).getString("Total_cost").toString())-Integer.parseInt(Constants.promocode_discount_value_store);
                                total_value.setText("AED "+val);

                            }
                           // total_value.setText("AED "+array.getJSONObject(0).getString("Total_cost").toString());
                            txt_discount.setText("- AED "+dis);
                            if(dis.equalsIgnoreCase("0"))
                                dis_layout.setVisibility(View.GONE);
                            else
                                dis_layout.setVisibility(View.VISIBLE);

                            for(int b=0;b<items.size();b++)
                            {
                                if(b==0)
                                {
                                    addonsvalue_price=array.getJSONObject(0).getString(items.get(b).toString()).toString();
                                }
                                else
                                {
                                    //addonsvalue_price=addonsvxalue_price+","+array.getJSONObject(0).getString(items.get(b).toString()).toString();
                                }
                            }
                            Log.e("Addonsvalue price",addonsvalue_price);
                            Constants.store_addonsvalue_price=addonsvalue_price;

                            if(final_order.equalsIgnoreCase("Y"))
                            {

                                Constants.store_billing_name = billing_name;
                                Constants.store_billing_add = billing_add;
                                Constants.store_source_full_add = source_full_add;
                                Constants.store_destination_full_add = destination_full_add;

                                Constants.AddonServices=AddonServices_static.trim();
                                if(page.equalsIgnoreCase("standard"))
                                {
                                    if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                                    {
                                        Constants.store_totalcost_s = totlecost_stan;
                                        Constants.store_NoOfTruck_s = NoOfTruck_s;
                                        Constants.store_NoOfLabour_s = NoOfLabour_s;
                                        Constants.store_NoOfHandiman_s = NoOfHandiman_s;
                                        Constants.store_BaseRate_s = BaseRate_s;
                                        Constants.store_TotalLabourRate_s = TotalLabourRate_s;
                                        Constants.store_TotalHandimanRate_s = TotalHandimanRate_s;
                                        Constants.store_TotalPackingRate_s = TotalPackingRate_s;

                                        Constants.static_labourvalue_s = NoOfLabour_s;
                                        Constants.static_handymanvalue_s = NoOfHandiman_s;
                                        Log.e("NoOfLabour_s",Constants.store_NoOfLabour_s);
                                        Log.e("NoOfHandiman_s",Constants.store_NoOfHandiman_s);
                                        Log.e("store_BaseRate_s",Constants.store_BaseRate_s);
                                        Log.e("store_totalcost_s",Constants.store_totalcost_s);
                                    }
                                    else
                                    {
                                        Constants.store_totalcost_saving = totlecost_stan;
                                        Constants.store_NoOfTruck_saving = NoOfTruck_s;
                                        Constants.store_NoOfLabour_saving = NoOfLabour_s;
                                        Constants.store_NoOfHandiman_saving = NoOfHandiman_s;
                                        Constants.store_BaseRate_saving = BaseRate_s;
                                        Constants.store_TotalLabourRate_saving = TotalLabourRate_s;
                                        Constants.store_TotalHandimanRate_saving = TotalHandimanRate_s;
                                        Constants.store_TotalPackingRate_saving = TotalPackingRate_s;

                                        Constants.static_labourvalue_saving = NoOfLabour_s;
                                        Constants.static_handymanvalue_saving = NoOfHandiman_s;
                                        Log.e("NoOfLabour_saving",Constants.store_NoOfLabour_saving);
                                        Log.e("NoOfHandiman_saving",Constants.store_NoOfHandiman_saving);
                                        Log.e("store_BaseRate_saving",Constants.store_BaseRate_saving);
                                        Log.e("store_totalcost_saving",Constants.store_totalcost_saving);
                                    }
                                }
                                else
                                {
                                    Constants.store_totalcost_p= totlecost_stan;
                                    Constants.store_NoOfTruck_p=NoOfTruck_s;
                                    Constants.store_NoOfLabour_p=NoOfLabour_s;
                                    Constants.store_NoOfHandiman_p=NoOfHandiman_s;
                                    String BaseRate_s11= array.getJSONObject(0).getString("Total_cost_without_discount").toString();
                                    Constants.store_BaseRate_p=BaseRate_s11;
                                    Constants.store_TotalLabourRate_p=TotalLabourRate_s;
                                    Constants.store_TotalHandimanRate_p=TotalHandimanRate_s;
                                    Constants.store_TotalPackingRate_p=TotalPackingRate_s;
                                }


                                Log.d("---------------", "--------------------------");

                                Constants.counter_addon=1;
                                Constants.store_combodis=txt_discount.getText().toString();
                                Constants.store_addonsprice=total_value.getText().toString();
                                Constants.AddonServices= AddonServices_static.trim();


                                Constants.Home_store_addons = new ArrayList<>(store_addons);
                                Constants.Home_store_addons_price = new ArrayList<>(store_addons_price);
                                Constants.Home_store_addons_spinner = new ArrayList<>(store_addons_spinner);
                                Constants.Home_store_addons_view = new ArrayList<>(store_addons_view);
                                String total=total_value.getText().toString();
                                total=total.substring(4);


                                if(page.equalsIgnoreCase("standard"))
                                {

                                    if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                                        Constants.store_totalcost_s=total;
                                    else
                                        Constants.store_totalcost_saving=total;
                                    Intent i = new Intent(Activity_MoveMyHome_Addons.this,Activity_payment.class);
                                    i.putExtra("page", "standard");
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();

                                }
                                else
                                {
                                    Constants.store_totalcost_p=total;
                                    Intent i = new Intent(Activity_MoveMyHome_Addons.this,Activity_payment.class);
                                    i.putExtra("page", page);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                    finish();
                                }






                            }


                        } else
                        {
                            if(message.equalsIgnoreCase(""))
                            {}
                            else
                                UF.msg(message + "");
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

public void convert_atos(String f)
{
     f=store_addons.toString();
    f=f.replace("*,", "").replace("*","").replace("[","").replace("]","");
    f=f.trim();
    if (f.length() > 0)
    {
        String c=f.substring(f.length() - 1);
        if(c.contains(","))
            f=f.substring(0, f.length() - 1);

    }
    AddonServices_static=f;
    AddonServices_static=AddonServices_static.replaceAll("\\s+","");
}
    public void remove_string(String remove_value)
    {
        if(AddonServices_static.trim().contains(remove_value.trim()+","))
        {

                AddonServices_static=AddonServices_static.replace(remove_value.trim()+",","");
                Log.e("same as ,",AddonServices_static);


        }
        else if(AddonServices_static.contains(remove_value.trim())) {
            AddonServices_static = AddonServices_static.replace(remove_value.trim(), "");
            Log.e("same as only name", AddonServices_static);
        }
        if (AddonServices_static.length() > 0)
        {
            String c=AddonServices_static.substring(AddonServices_static.length() - 1);
            Log.e("check",c);
            if(c.contains(","))
                AddonServices_static=AddonServices_static.substring(0, AddonServices_static.length() - 1);
            Log.e("AddonServices_static",AddonServices_static);
        }
    }

}
