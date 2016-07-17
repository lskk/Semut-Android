package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SessionManager;
import project.bsts.semut.imgutil.ImageLoader;


public class TaxiOrderListActivity extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG = TaxiOrderListActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    final Context context = this;
    private RelativeLayout noActiveOrder;
    private RelativeLayout activeDate;
    private RelativeLayout activeData;
    private ImageView taxiPhoto;
    private TextView taxiDriver;
    private TextView taxiNopol;
    private TextView taxiCompany;
    private TextView taxiPhone;
    private TextView activeOrderDate;
    private ImageButton btnBack;
    private ArrayList<HashMap<String, String>> data;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        setContentView(R.layout.activity_taxi_order_list);
        noActiveOrder = (RelativeLayout)findViewById(R.id.no_active_order);
        activeDate = (RelativeLayout)findViewById(R.id.active_order_date);
        activeData = (RelativeLayout)findViewById(R.id.active_order_detail);
        btnBack = (ImageButton)findViewById(R.id.backButton);
        activeOrderDate = (TextView)findViewById(R.id.order_date);
        taxiPhoto = (ImageView)findViewById(R.id.taxi_photo);
        taxiDriver = (TextView)findViewById(R.id.taxi_driver);
        taxiNopol = (TextView)findViewById(R.id.taxi_nopol);
        taxiCompany = (TextView)findViewById(R.id.taxi_company);
        taxiPhone = (TextView)findViewById(R.id.taxi_phone);
        list = (ListView)findViewById(R.id.orderlist);
        list.setOnItemClickListener(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        data = new ArrayList<HashMap<String, String>>();
        fetchData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        //viewingMap();
    }
    private void fetchData() {
        // Tag used to cancel the request
        String tag_string_req = "req_order_history";

        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_TAXIORDERHISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Order history Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    boolean isOrder = jObj.getBoolean("isOrder");
                    // Check for error node in json
                    if (isOrder){
                        JSONObject dataactive = jObj.getJSONObject("Orderdata");
                        noActiveOrder.setVisibility(View.GONE);
                        activeData.setVisibility(View.VISIBLE);
                        activeDate.setVisibility(View.VISIBLE);
                        final String date = convertDate(dataactive.getString("ReservTime"));
                        activeOrderDate.setText(date);
                        taxiDriver.setText(dataactive.getString("Driver"));
                        taxiNopol.setText(dataactive.getString("Nopol"));
                        taxiCompany.setText(dataactive.getString("Company"));
                        taxiPhone.setText(dataactive.getString("Phone"));
                        int loader = R.drawable.loading;
                        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                        imgLoader.DisplayImage(dataactive.optString("Photo"), loader, taxiPhoto);
                        final String reservID = dataactive.getString("ReservationID");
                        final ImageButton btnDetailOrder = (ImageButton)findViewById(R.id.btn_detail_order);
                        btnDetailOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent idetail = new Intent(getApplicationContext(),TaxiOrderActivity.class);
                                idetail.putExtra("reservationid",reservID);
                                startActivity(idetail);
                            }
                        });
                    }else{
                        activeDate.setVisibility(View.GONE);
                        activeData.setVisibility(View.GONE);
                        noActiveOrder.setVisibility(View.VISIBLE);
                    }
                    if (success) {
                        JSONArray arr = jObj.getJSONArray("Data");

                        for (int i=0; i<arr.length(); i++) {
                            JSONObject one = arr.getJSONObject(i);
                            final String dateorder = convertDate(one.optString("ReservTime"));
                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put("date", dateorder);
                            item.put("taxi", one.optString("Driver") + " -> " + one.optString("Company") + " - " + one.optString("Nopol"));
                            item.put("reserv_id", one.optString("ID"));

                            data.add(item);
                        }

                        final String[] fromMapKey = new String[] {"date", "taxi"};
                        final int[] toLayoutId = new int[] {R.id.order_date_item, R.id.order_taxi_item};

                        SimpleAdapter adapter =  new SimpleAdapter(getApplicationContext(), data, R.layout.taxi_order_list, fromMapKey, toLayoutId);
                        list.setAdapter(adapter);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Taxi Order history Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                SessionManager session = new SessionManager(getApplicationContext());
                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid", deviceid);

                Log.d("HEADER",String.valueOf(session_id)+"-"+deviceid);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> item = data.get(position);

        Intent intent = new Intent(this, TaxiOrderActivity.class);
        intent.putExtra("reservationid", item.get("reserv_id"));
        startActivity(intent);
    }
    private String convertDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date myDate = dateFormat.parse(date);
            SimpleDateFormat format = new SimpleDateFormat("EEEE, dd-MM-yyyy hh:mm");
            return format.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
