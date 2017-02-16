package project.bsts.semut;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.ui.MainDrawer;

public class SemutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MainDrawer drawer;
    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semut);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        context = this;

        drawer = new MainDrawer(context, toolbar, 0);
        drawer.initDrawer();


    }

}
