package project.bsts.semut.ui;


import android.support.design.widget.Snackbar;
import android.view.View;

public class ShowSnackbar {
    public View view;

    public ShowSnackbar(View _v){
        view = _v;
    }

    public void show(String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
