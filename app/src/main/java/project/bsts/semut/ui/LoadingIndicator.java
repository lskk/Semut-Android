package project.bsts.semut.ui;


import android.view.View;

import io.github.yuweiguocn.lib.squareloading.SquareLoading;

public class LoadingIndicator {

    SquareLoading loading;

    public LoadingIndicator(SquareLoading _loading){
        loading = _loading;
    }

    public void show(){
        loading.setVisibility(View.VISIBLE);
    }

    public void hide(){
        loading.setVisibility(View.GONE);
    }
}
