package project.bsts.semut;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.adapters.TransportationListAdapter;

public class TransportationListActivity extends AppCompatActivity {

    @BindView(R.id.list_transportation)
    ListView mListViewTransport;

    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        arrayList = new ArrayList<String>();
    //    arrayList.add("Angkot");
        arrayList.add("Bus");

        TransportationListAdapter transportationListAdapter = new TransportationListAdapter(this, arrayList);
        mListViewTransport.setAdapter(transportationListAdapter);
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
