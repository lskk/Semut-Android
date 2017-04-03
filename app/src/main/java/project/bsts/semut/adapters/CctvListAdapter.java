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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.bsts.semut.CctvPlayerActivity;
import project.bsts.semut.R;
import project.bsts.semut.pojo.CityCctv;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.utilities.DownloadImageTask;

public class CctvListAdapter extends BaseAdapter {
    private ArrayList<CctvMap> cctvMaps = null;
    private Context mContext = null;
    private LayoutInflater mInflater = null;

    private TextView mTextViewCctvName;
    private ImageView mImagePreview;



    public CctvListAdapter(Context context, ArrayList<CctvMap> cctvMaps) {
        this.mContext = context;
        this.cctvMaps = cctvMaps;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (cctvMaps != null) {
            return cctvMaps.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (cctvMaps != null) {
            return cctvMaps.get(position);
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

        CctvMap cctv = cctvMaps.get(position);
        convertView = mInflater.inflate(R.layout.layout_list_cctv, null);
        mTextViewCctvName = (TextView)convertView.findViewById(R.id.cctv_name);
        mImagePreview = (ImageView) convertView.findViewById(R.id.cctv_preview);
        mTextViewCctvName.setText(cctv.getName());
      //  new DownloadImageTask(mImagePreview, null).execute(cctvMaps.get(position).getUrlImage());
        String urldisplay = cctvMaps.get(position).getUrlImage().replace("push-ios", "247");
        Picasso.with(mContext)
                .load(urldisplay)
                .placeholder(R.mipmap.loading_image)
                .error(R.mipmap.kamera_akses_error)
                .into(mImagePreview);

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, CctvPlayerActivity.class);
            intent.putExtra(Constants.INTENT_VIDEO_URL, cctvMaps.get(position).getUrlVideo());
            mContext.startActivity(intent);
        });

        return convertView;
    }
}