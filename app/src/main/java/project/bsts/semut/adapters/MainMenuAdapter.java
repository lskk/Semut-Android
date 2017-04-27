package project.bsts.semut.adapters;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import project.bsts.semut.CityCctvActivity;
import project.bsts.semut.EmergencyActivity;
import project.bsts.semut.R;
import project.bsts.semut.SocialReportActivity;
import project.bsts.semut.TransportationListActivity;
import project.bsts.semut.pojo.MainMenuObject;

public class MainMenuAdapter extends BaseAdapter {

    private ArrayList<MainMenuObject> detailText = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;
    private ImageView imageMenu;
    private TextView textMenu;

    public MainMenuAdapter(Context c, ArrayList<MainMenuObject> detailText) {
        this.mContext = c;
        this.detailText = detailText;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if (detailText != null) {
            return detailText.size();
        }
        else {
            return 0;
        }
    }

    public Object getItem(int position) {
        if (detailText != null) {
            return detailText.get(position);
        }
        else {
            return null;
        }
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.layout_menu_main, null);

        imageMenu = (ImageView)convertView.findViewById(R.id.menu_icon);
        textMenu = (TextView)convertView.findViewById(R.id.menu_title);
        textMenu.setText(detailText.get(position).getTitle());
        imageMenu.setImageDrawable(detailText.get(position).getIcon());
        convertView.setOnClickListener(view -> {
            if(detailText.get(position).getClassIntent().toString()
                    .equals(SocialReportActivity.class.toString()))
                mContext.startActivity(new Intent(mContext, SocialReportActivity.class));
            else if(detailText.get(position).getClassIntent().toString()
                    .equals(CityCctvActivity.class.toString()))
                mContext.startActivity(new Intent(mContext, CityCctvActivity.class));
            else if(detailText.get(position).getClassIntent().toString()
                    .equals(TransportationListActivity.class.toString()))
                mContext.startActivity(new Intent(mContext, TransportationListActivity.class));
            else if(detailText.get(position).getClassIntent().toString()
                    .equals(EmergencyActivity.class.toString()))
                mContext.startActivity(new Intent(mContext, EmergencyActivity.class));
        });

        return convertView;
    }

}