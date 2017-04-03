package project.bsts.semut;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.adapters.CityListAdapter;
import project.bsts.semut.connections.rest.IConnectionResponseHandler;
import project.bsts.semut.connections.rest.RequestRest;
import project.bsts.semut.setup.Constants;

public class CityCctvActivity extends AppCompatActivity implements IConnectionResponseHandler {



    @BindView(R.id.list_city)
    ListView mListViewCity;

    private CityListAdapter mCityListAdapter;
    private ProgressDialog mProgressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_cctv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        context = this;
        mProgressDialog = new ProgressDialog(context);

    }


    private void getCCtv(){
        mProgressDialog.setMessage("Memuat data...");
        mProgressDialog.show();
        RequestRest requestRest = new RequestRest(context, this);
        requestRest.getAllCctv();
    }

    @Override
    public void onSuccessRequest(String pResult, String type) {
        switch (type){
            case Constants.REST_GET_ALL_CCTV:
                break;
            case Constants.REST_ERROR:
                break;
        }
    }
}
