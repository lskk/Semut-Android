package project.bsts.semut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SQLiteHandler;
import project.bsts.semut.helper.SessionManager;

/**
 * Created by fiyyanp on 9/8/2015.
 * Startup PPTIK 2015
 */
public class BarcodeActivity  extends Activity {
    private SessionManager session;
    private SQLiteHandler db;
    private ImageButton btnBarcodeMenu;
    private ImageButton btnBack;
    private String userId;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_barcode);

        btnBarcodeMenu = (ImageButton) findViewById(R.id.btn_barcode_menu);
        btnBarcodeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuList(v);
            }
        });

        btnBack = (ImageButton) findViewById(R.id.btn_barcode_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        // session manager
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        WriteBarcode();
    }

    private void showMenuList(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.menu_barcode, popup.getMenu());
        // Force icons to show
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            Log.w("error", "error forcing menu icons to show", e);
            popup.show();
            return;
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.generate_barcode:
                        generateBarcode();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }


    private String getUserBarcode() {
        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        userId = user.get("ID");
        return user.get("Barcode");
    }

    private void WriteBarcode(){
        //fetch user data from API
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_MYPROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Cek Response: " , response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");

                    // Check for error node in json
                    if (success) {
                        JSONObject myprofile = jObj.getJSONObject("Profile");

                        //update sqllite
                        String[] params = new String[]{myprofile.getString("ID")};
                        db.updateBarcodeUsers(myprofile.getString("Barcode"), params);
                        createbarcode();
                    }else{

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid",deviceid);
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "myprofile");

    }

    private void createbarcode() {
        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;

        //Encode with a QR Code image
        QRCodeWriter writer = new QRCodeWriter();
        ImageView barcode = (ImageView)findViewById(R.id.imageBarcode);
        try {

            String b = getUserBarcode();
            BitMatrix bitMatrix = writer.encode(b, BarcodeFormat.QR_CODE, width, height);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x,y))
                        bmp.setPixel(x, y, Color.BLACK);
                    else
                        bmp.setPixel(x, y, Color.WHITE);
                }
            }
            barcode.setImageBitmap(bmp);
        } catch (WriterException e) {
            Log.e("QR ERROR", ""+e);
            e.printStackTrace();
        }
    }

    private void generateBarcode() {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_GENERATEBARCODE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Cek Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if(success){
                        String[] params = new String[]{userId};
                        db.updateBarcodeUsers(jObj.getString("Barcode"), params);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Generate barcode success",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Generate barcode failed",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("barcode generate: ", ""+error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid",deviceid);
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "generatebarcode");

    }
}
