package project.bsts.semut.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import project.bsts.semut.R;


/**
 * Created by Hynra on 6/8/16.
 */
public class MainDrawer {

    private Context context;
    private Toolbar toolbar;
    private int identifier;
    private com.mikepenz.materialdrawer.Drawer result;
    private SecondaryDrawerItem bantuanItem;

    public MainDrawer(Context _context, Toolbar _toolbar, int _identifier){
        context = _context;
        toolbar = _toolbar;
        identifier = _identifier;
    }

    public void initDrawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity((Activity) context)
                .withHeaderBackground(context.getResources().getDrawable(R.drawable.default_cover))
                .build();

        PrimaryDrawerItem dashBoard;
        PrimaryDrawerItem profile;

        ExpandableDrawerItem transaksiItem;
        ExpandableDrawerItem depositItem;
        PrimaryDrawerItem produkItem;
        ExpandableDrawerItem historyItem;
        ExpandableDrawerItem editProfileItem;
        SecondaryDrawerItem tentangItem;
        SecondaryDrawerItem logoutItem;


        result = new DrawerBuilder()
                .withActivity((Activity) context)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withFullscreen(true)
                .addDrawerItems(
                        dashBoard = new PrimaryDrawerItem().withName("Dashboard").withIcon(GoogleMaterial.Icon.gmd_dashboard).withIdentifier(100),
                        profile = new PrimaryDrawerItem().withName("Profil").withIcon(GoogleMaterial.Icon.gmd_face).withIdentifier(10),
                        transaksiItem = new ExpandableDrawerItem().withName("Transaksi").withIcon(GoogleMaterial.Icon.gmd_shopping_cart).withIdentifier(1).withSubItems(
                                new SecondaryDrawerItem().withName("Pembayaran").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_payment).withIdentifier(11),
                                new SecondaryDrawerItem().withName("Informasi").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_announcement).withIdentifier(12)
                        ),
                        depositItem = new ExpandableDrawerItem().withName("Deposit").withIcon(GoogleMaterial.Icon.gmd_account_balance_wallet).withIdentifier(2).withSubItems(
                                new SecondaryDrawerItem().withName("Transfer Deposit").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_keyboard_tab).withIdentifier(22),
                                new SecondaryDrawerItem().withName("Tiket Deposit").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_picture_in_picture).withIdentifier(23)
                        ),
                        historyItem = new ExpandableDrawerItem().withName("Riwayat").withIcon(GoogleMaterial.Icon.gmd_history).withIdentifier(4).withSubItems(
                                new SecondaryDrawerItem().withName("Riwayat Transaksi").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_assignment_return).withIdentifier(41),
                                new SecondaryDrawerItem().withName("Riwayat Deposit").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_assignment_returned).withIdentifier(42)
                        ),
                        new DividerDrawerItem(),
                        editProfileItem = new ExpandableDrawerItem().withName("Pengaturan").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(5).withSubItems(
                                new SecondaryDrawerItem().withName("Ubah Kontak").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_create).withIdentifier(51),
                                new SecondaryDrawerItem().withName("Ubah Password").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_create).withIdentifier(52)
                        ),
                        tentangItem = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("Tentang").withIcon(GoogleMaterial.Icon.gmd_perm_device_information).withIdentifier(6).withSelectable(false),
                        bantuanItem = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("Bantuan").withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(8).withSelectable(false),
                        logoutItem = (SecondaryDrawerItem) new SecondaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app).withIdentifier(7).withSelectable(false)



                )
                .withSelectedItem(identifier)
                .withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        return false;
                    }
                })
                .withShowDrawerOnFirstLaunch(true)
                .build();
        result.closeDrawer();
    }
}