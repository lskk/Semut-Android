package project.bsts.semut;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.adapters.MainMenuAdapter;
import project.bsts.semut.pojo.MainMenuObject;
import project.bsts.semut.ui.MainDrawer;
import project.bsts.semut.utilities.CustomDrawable;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.gridview)
    GridView gridView;

    private Context context;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        new MainDrawer(context, toolbar, -1).initDrawer();


        ArrayList<MainMenuObject> arrObj = new ArrayList<MainMenuObject>();

        MainMenuObject object1 = new MainMenuObject();
        object1.setTitle("Social \nReport");
        object1.setClassIntent(SocialReportActivity.class);
        object1.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_question_answer, 34, R.color.primary_dark));
        arrObj.add(object1);

        MainMenuObject object2 = new MainMenuObject();
        object2.setTitle("Street \nCamera");
        object2.setClassIntent(CityCctvActivity.class);
        object2.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_videocam, 34, R.color.primary_dark));
        arrObj.add(object2);

        MainMenuObject object3 = new MainMenuObject();
        object3.setTitle("Public \nTransport");
        object3.setClassIntent(TransportationListActivity.class);
        object3.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_directions_bus, 34, R.color.primary_dark));
        arrObj.add(object3);

        MainMenuObject object4 = new MainMenuObject();
        object4.setTitle("Tombol \nDarurat");
        object4.setClassIntent(EmergencyActivity.class);
        object4.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_pan_tool, 34, R.color.primary_dark));
        arrObj.add(object4);

    /*    MainMenuObject object4 = new MainMenuObject();
        object4.setTitle("Profile\n");
        object4.setClassIntent(SocialReportActivity.class);
        object4.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_account_circle, 34, R.color.primary_dark));
        arrObj.add(object4);

        MainMenuObject object5 = new MainMenuObject();
        object5.setTitle("Friends\n");
        object5.setClassIntent(SocialReportActivity.class);
        object5.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_supervisor_account, 34, R.color.primary_dark));
        arrObj.add(object5);

        MainMenuObject object6 = new MainMenuObject();
        object6.setTitle("Chat\n");
        object6.setClassIntent(SocialReportActivity.class);
        object6.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_chat, 34, R.color.primary_dark));
        arrObj.add(object6);

        MainMenuObject object7 = new MainMenuObject();
        object7.setTitle("Personal \nPayment");
        object7.setClassIntent(SocialReportActivity.class);
        object7.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_account_balance_wallet, 34, R.color.primary_dark));
        arrObj.add(object7);

        MainMenuObject object8 = new MainMenuObject();
        object8.setTitle("Settings\n");
        object8.setClassIntent(SocialReportActivity.class);
        object8.setIcon(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_settings, 34, R.color.primary_dark));
        arrObj.add(object8); */

        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(context,arrObj);
        gridView.setAdapter(mainMenuAdapter);

    }

}
