package project.bsts.semut;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.adapters.CityListAdapter;
import project.bsts.semut.connections.rest.IConnectionResponseHandler;
import project.bsts.semut.connections.rest.RequestRest;
import project.bsts.semut.pojo.CityCctv;
import project.bsts.semut.pojo.RequestStatus;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.CommonAlerts;

public class CityCctvActivity extends AppCompatActivity implements IConnectionResponseHandler {



    @BindView(R.id.list_city)
    ListView mListViewCity;

    private CityListAdapter mCityListAdapter;
    private ProgressDialog mProgressDialog;
    private Context context;
    private ArrayList<CityCctv> list = new ArrayList<CityCctv>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_cctv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Lokasi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        context = this;
        mProgressDialog = new ProgressDialog(context);
        getCCtv();

    }

    private void initList(JSONArray arr) {
        list.clear();
        try {
            if(arr.length() < 0 ){
                //
            }else {

                for (int i = 0; i < arr.length(); i++) {
                    CityCctv cctv = new Gson().fromJson(String.valueOf(arr.getJSONObject(i)), CityCctv.class);
                    list.add(cctv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /******************* bandar lampung only *******************/
        Intent intent = new Intent(context, CctvListActivity.class);
        intent.putExtra(Constants.INTENT_CCTV_LIST, list.get(1).getCctv());
        intent.putExtra(Constants.INTENT_CCTV_CITY, list.get(1).getName());
        startActivity(intent);
        finish();
        /******************* end *******************/

    //    mListViewCity.setAdapter(null);
    //    mCityListAdapter = new CityListAdapter(context, list);
    //    mListViewCity.setAdapter(mCityListAdapter);
    }

    private void getCCtv(){
        mProgressDialog.setMessage("Memuat data...");
        mProgressDialog.show();
        RequestRest requestRest = new RequestRest(context, this);
        requestRest.getAllCctv();
    }

    @Override
    public void onSuccessRequest(String pResult, String type) {
        mProgressDialog.dismiss();
        switch (type){
            case Constants.REST_GET_ALL_CCTV:
                RequestStatus requestStatus = new Gson().fromJson(pResult, RequestStatus.class);
                if(requestStatus.getSuccess()){
                    try {
                        JSONObject object = new JSONObject(pResult);
                        initList(object.getJSONArray("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else CommonAlerts.commonError(context, requestStatus.getMessage());
                break;
            case Constants.REST_ERROR:
                CommonAlerts.commonError(context, Constants.MESSAGE_HTTP_ERROR);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
