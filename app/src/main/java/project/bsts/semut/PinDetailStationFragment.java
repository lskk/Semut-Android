package project.bsts.semut;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.imgutil.DownloadImageTask;


public class PinDetailStationFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = PinDetailCCTVFragment.class.getSimpleName();
    private JSONObject data;
    private TextView nameText;
    private TextView detailText;
    private ImageView thumb;
    private Button watchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View view = inflater.inflate(R.layout.fragment_pin_detail_station, container, false);

        nameText = (TextView) view.findViewById(R.id.nameText);
        detailText = (TextView) view.findViewById(R.id.detail);

        watchButton = (Button) view.findViewById(R.id.jadwalBtn);
        watchButton.setOnClickListener(this);

        nameText.setEllipsize(TextUtils.TruncateAt.END);
        nameText.setMaxLines(1);
        nameText.setText(data.optString("stop_name"));

        detailText.setEllipsize(TextUtils.TruncateAt.END);
        detailText.setMaxLines(3);
        detailText.setText(data.optString("stop_desc"));

        return view;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Jadwal Belum Tersedia", Toast.LENGTH_LONG).show();
    }
}
