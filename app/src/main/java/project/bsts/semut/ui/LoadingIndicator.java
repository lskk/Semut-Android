package project.bsts.semut.ui;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import io.github.yuweiguocn.lib.squareloading.SquareLoading;
import project.bsts.semut.R;

public class LoadingIndicator {

    SquareLoading loading;
    LinearLayout foreGround;
    Context context;

    public LoadingIndicator(Context ctx){
        context = ctx;
        loading = (SquareLoading) ((Activity) context).findViewById(R.id.loadingIndicator);
        foreGround = (LinearLayout)((Activity) context).findViewById(R.id.foreground);
    }

    public void show(){
        loading.setVisibility(View.VISIBLE);
        foreGround.setVisibility(View.VISIBLE);
    }

    public void hide(){
        loading.setVisibility(View.GONE);
        foreGround.setVisibility(View.GONE);
    }
}
