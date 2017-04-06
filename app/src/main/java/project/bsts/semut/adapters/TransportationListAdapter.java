package project.bsts.semut.adapters;


import android.annotation.SuppressLint;
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

import project.bsts.semut.TrackerActivity;
import project.bsts.semut.R;
import project.bsts.semut.setup.Constants;


public class TransportationListAdapter extends BaseAdapter {
    private ArrayList<String> transportList = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private TextView mTextViewCityName;
    private ImageView mImageViewIcon;



    public TransportationListAdapter(Context context, ArrayList<String> transportList) {
        this.mContext = context;
        this.transportList = transportList;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (transportList != null) {
            return transportList.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (transportList != null) {
            return transportList.get(position);
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

        convertView = mInflater.inflate(R.layout.layout_list_transportation, null);
        mTextViewCityName = (TextView)convertView.findViewById(R.id.transportation_name);
        mImageViewIcon = (ImageView) convertView.findViewById(R.id.transportation_icon);
        mTextViewCityName.setText(transportList.get(position));
        mImageViewIcon.setImageDrawable(new IconicsDrawable(mContext)
                .icon(GoogleMaterial.Icon.gmd_directions_bus)
                .sizeDp(24)
                .color(mContext.getResources().getColor(R.color.primary_dark)));

        Intent intent = new Intent(mContext, TrackerActivity.class);
        if(transportList.get(position).toLowerCase().equals("angkot")) intent.putExtra(Constants.INTENT_TRACKER_TYPE, Constants.MQ_ROUTES_BROADCAST_TRACKER_ANGKOT);
        else intent.putExtra(Constants.INTENT_TRACKER_TYPE, Constants.MQ_ROUTES_BROADCAST_TRACKER_BUS);

        convertView.setOnClickListener(view -> {
            mContext.startActivity(intent);
        });

        return convertView;
    }
}