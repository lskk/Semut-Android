package project.bsts.semut.ui;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import project.bsts.semut.R;
import project.bsts.semut.utilities.CustomDrawable;

public class ShowSnackbar {
    public View view;

    public ShowSnackbar(View _v){
        view = _v;
    }

    public void show(String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }


    public static TSnackbar errorSnackbar(Context context){
        TSnackbar snackbar = TSnackbar.make(((Activity)context).findViewById(R.id.maposm), "Oops! Koneksi internet Anda tidak stabil", TSnackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor("#000000"));
        snackbar.setAction("Keluar", view1 -> {
            ((Activity)context).finish();
        });
        snackbar.setMaxWidth(3000); //fullsize on tablets
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#ff8100"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#EEEEEE"));
        return snackbar;
    }
}
