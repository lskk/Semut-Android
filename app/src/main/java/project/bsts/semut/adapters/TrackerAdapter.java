package project.bsts.semut.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import project.bsts.semut.R;
import project.bsts.semut.pojo.mapview.Tracker;

public class TrackerAdapter extends BaseAdapter {

    private Context mContext = null;
    private Tracker[] trackers;
    private LayoutInflater mInflater = null;
    private TextView gpsNameText, gpsLocText, gpsDetail;
    private RadioButton stateRadio;
    private int checkedState;
    private View sortView;

    public interface MarkerPositionListener{
        public void onMarkerSelected(int position);
    }


    MarkerPositionListener listener;
    public TrackerAdapter(Context mContext, Tracker[] trackers, int checkedState, View sortView, MarkerPositionListener listener){
        this.mContext = mContext;
        this.trackers = trackers;
        this.mInflater = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
        this.checkedState = checkedState;
        this.sortView = sortView;
    }



    @Override
    public int getCount() {
        if (trackers != null) {
            return trackers.length;
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

      //  View identityView = getView(i, view, viewGroup);
      //  identityView.setTag(i);

        view = mInflater.inflate(R.layout.layout_list_tracker_filter, null);
        gpsNameText = (TextView)view.findViewById(R.id.gps_name);
        stateRadio = (RadioButton)view.findViewById(R.id.state);
        gpsLocText = (TextView)view.findViewById(R.id.gps_location);
        gpsDetail = (TextView)view.findViewById(R.id.gps_detail);

        boolean state = (i == checkedState) ? true : false;
        String detail = "Speed : "+trackers[i].getSpeed()+" KM/H | Tanggal : "+trackers[i].getDate()+" "+trackers[i].getTime();

        gpsNameText.setText(trackers[i].getKeterangan());
        gpsLocText.setText(trackers[i].getLokasi());
        gpsDetail.setText(detail);
        stateRadio.setChecked(state);

        view.setOnClickListener(view1 -> {
            listener.onMarkerSelected(i);
        //    sortView.setVisibility(View.GONE);

        });

    //    stateRadio.setOnCheckedChangeListener((compoundButton, b) -> {
    //        listener.onMarkerSelected(i);
    //        sortView.setVisibility(View.GONE);
    //    });

        stateRadio.setOnClickListener(view1 -> {
            listener.onMarkerSelected(i);
        //    sortView.setVisibility(View.GONE);

        });

        view.setTag(i);
        return view;
    }
}