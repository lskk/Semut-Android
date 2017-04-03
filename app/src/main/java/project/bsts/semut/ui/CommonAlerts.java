package project.bsts.semut.ui;


import android.app.Activity;
import android.content.Context;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import project.bsts.semut.R;

public class CommonAlerts {

    public static void commonError(Context context, String msg){
        FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(context)
                .setImageDrawable(new IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_cloud_off)
                        .sizeDp(100)
                        .color(context.getResources().getColor(R.color.cochineal_red)))
                .setTextTitle("Gagal memuat permintaan")
                .setTextSubTitle("Permintaan Anda tidak dapat diproses")
                .setBody(msg)
                .setAutoHide(false)
                .setPositiveButtonText("OK")
                .setPositiveColor(R.color.primary)
                .setOnPositiveClicked((view, dialog) -> {
                    ((Activity)context).finish();
                })
                .build();


        alert.show();
    }
}
