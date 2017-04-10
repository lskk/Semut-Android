package project.bsts.semut.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import project.bsts.semut.LoginActivity;
import project.bsts.semut.R;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.setup.Constants;


/**
 * Created by Hynra on 6/8/16.
 */
public class MainDrawer {

    private Context context;
    private Toolbar toolbar;
    private int identifier;
    private com.mikepenz.materialdrawer.Drawer result;
    private SecondaryDrawerItem bantuanItem;
    private PreferenceManager preferenceManager;

    public MainDrawer(Context _context, Toolbar _toolbar, int _identifier){
        context = _context;
        toolbar = _toolbar;
        identifier = _identifier;
        preferenceManager = new PreferenceManager(context);
    }

    public void hideDrawer(){
        DrawerLayout drawerLayout = result.getDrawerLayout();
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void showDrawer(){
        DrawerLayout drawerLayout = result.getDrawerLayout();
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public void hideToolbar(boolean state, View[] views){
        if(state){
            hideDrawer();
            for(int i = 0 ; i < views.length; i++){
                views[i].setVisibility(View.GONE);
            }
        }else {
            showDrawer();
            for(int i = 0 ; i < views.length; i++){
                views[i].setVisibility(View.VISIBLE);
            }
        }
    }

    public void initDrawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity((Activity) context)
                .withHeaderBackground(context.getResources().getDrawable(R.drawable.banner))
                .build();

        PrimaryDrawerItem dashBoard;
        PrimaryDrawerItem profile;

        ExpandableDrawerItem friends;
        ExpandableDrawerItem cctvs;
        PrimaryDrawerItem produkItem;
        ExpandableDrawerItem transportasi;
        ExpandableDrawerItem editProfileItem;
        SecondaryDrawerItem tentangItem;
        SecondaryDrawerItem logoutItem;


        result = new DrawerBuilder()
                .withActivity((Activity) context)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withFullscreen(true)
                .addDrawerItems(
                    /*    dashBoard = new PrimaryDrawerItem().withName("Map").withIcon(GoogleMaterial.Icon.gmd_map).withIdentifier(0),
                        profile = new PrimaryDrawerItem().withName("Profil").withIcon(GoogleMaterial.Icon.gmd_face).withIdentifier(10),
                        friends = new ExpandableDrawerItem().withName("Pertemanan").withIcon(GoogleMaterial.Icon.gmd_people).withIdentifier(20).withSubItems(
                                new SecondaryDrawerItem().withName("Tambah Teman").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_add).withIdentifier(21),
                                new SecondaryDrawerItem().withName("Daftar Teman").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_people_outline).withIdentifier(22)
                        ),
                        cctvs = new ExpandableDrawerItem().withName("CCTV").withIcon(GoogleMaterial.Icon.gmd_videocam).withIdentifier(30).withSubItems(
                                new SecondaryDrawerItem().withName("Terdekat").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_near_me).withIdentifier(31),
                                new SecondaryDrawerItem().withName("Semua CCTV").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_list).withIdentifier(32)
                        ),
                        transportasi = new ExpandableDrawerItem().withName("Transportasi").withIcon(GoogleMaterial.Icon.gmd_local_shipping).withIdentifier(40).withSubItems(
                                new SecondaryDrawerItem().withName("Angkot").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_tram).withIdentifier(41)
                        ),
                        new DividerDrawerItem(),
                        editProfileItem = new ExpandableDrawerItem().withName("Pengaturan").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(50).withSubItems(
                                new SecondaryDrawerItem().withName("Ubah Password").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_create).withIdentifier(51)
                        ), */
                 //       new PrimaryDrawerItem().withName("Tentang").withIcon(GoogleMaterial.Icon.gmd_perm_device_information).withIdentifier(60).withSelectable(false),
                //        new PrimaryDrawerItem().withName("Bantuan").withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(70).withSelectable(false),
                        new PrimaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withIdentifier(80).withSelectable(false)
                )
                .withSelectedItem(identifier)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {

                    switch ((int) drawerItem.getIdentifier()){
                        case 80:
                            preferenceManager.save(false, Constants.IS_LOGGED_IN);
                            preferenceManager.apply();
                            Toast.makeText(context, "Signout berhasil", Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(context, LoginActivity.class));
                            ((Activity)context).finish();
                            break;
                    }
                    return false;
                })
                .withShowDrawerOnFirstLaunch(true)
                .build();
        result.closeDrawer();


    }
}