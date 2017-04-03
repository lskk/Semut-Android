package project.bsts.semut.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

import project.bsts.semut.R;
import project.bsts.semut.pojo.CityCctv;

public class CityListAdapter extends BaseAdapter {
    private ArrayList<CityCctv> detailText = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private TextView mTextViewCityName;
    private ImageView mImageViewIcon;



    public CityListAdapter(Context context, ArrayList<CityCctv> detailText) {
        this.mContext = context;
        this.detailText = detailText;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (detailText != null) {
            return detailText.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (detailText != null) {
            return detailText.get(position);
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

        CityCctv cctv = detailText.get(position);
        convertView = mInflater.inflate(R.layout.layout_list_city, null);
        mTextViewCityName = (TextView)convertView.findViewById(R.id.city_name);
        mImageViewIcon = (ImageView) convertView.findViewById(R.id.city_icon);
        mTextViewCityName.setText(cctv.getName());
        mImageViewIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_videocam)
                .sizeDp(24)
                .color(mContext.getResources().getColor(R.color.primary_dark)));

        return convertView;
    }
}