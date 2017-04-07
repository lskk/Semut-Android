package project.bsts.semut.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

import project.bsts.semut.CctvListActivity;
import project.bsts.semut.R;
import project.bsts.semut.pojo.CityCctv;
import project.bsts.semut.setup.Constants;

public class CityListAdapter extends BaseAdapter {
    private ArrayList<CityCctv> cityCctvs = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private TextView mTextViewCityName;
    private ImageView mImageViewIcon;



    public CityListAdapter(Context context, ArrayList<CityCctv> cityCctvs) {
        this.mContext = context;
        this.cityCctvs = cityCctvs;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (cityCctvs != null) {
            return cityCctvs.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (cityCctvs != null) {
            return cityCctvs.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CityCctv cctv = cityCctvs.get(position);
        convertView = mInflater.inflate(R.layout.layout_list_city, null);
        mTextViewCityName = (TextView)convertView.findViewById(R.id.city_name);
        mImageViewIcon = (ImageView) convertView.findViewById(R.id.city_icon);
        mTextViewCityName.setText(cctv.getName());
        mImageViewIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_videocam)
                .sizeDp(24)
                .color(mContext.getResources().getColor(R.color.primary_dark)));

        Intent intent = new Intent(mContext, CctvListActivity.class);
        intent.putExtra(Constants.INTENT_CCTV_LIST, cityCctvs.get(position).getCctv());
        intent.putExtra(Constants.INTENT_CCTV_CITY, cityCctvs.get(position).getName());
        convertView.setOnClickListener(view -> {
            mContext.startActivity(intent);
        });

        return convertView;
    }
}