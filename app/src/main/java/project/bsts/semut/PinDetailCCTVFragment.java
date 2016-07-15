package project.bsts.semut;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.imgutil.DownloadImageTask;


public class PinDetailCCTVFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = PinDetailCCTVFragment.class.getSimpleName();
    private JSONObject data;
    private TextView nameText;
    private TextView cityText;
    private TextView provinceText;
    private ImageView thumb;
    private Button watchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View view = inflater.inflate(R.layout.fragment_pin_detail_cctv, container, false);

        nameText = (TextView) view.findViewById(R.id.nameText);
        cityText = (TextView) view.findViewById(R.id.cityText);
        provinceText = (TextView) view.findViewById(R.id.provinceText);
        thumb = (ImageView) view.findViewById(R.id.thumb);
        watchButton = (Button) view.findViewById(R.id.watchButton);
        watchButton.setOnClickListener(this);

        nameText.setText(data.optString("Name"));
        cityText.setText(data.optString("City"));
        provinceText.setText(data.optString("Province"));
        new DownloadImageTask(thumb).execute(data.optString("Screenshot"));

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
        Intent intent = new Intent(getActivity().getApplicationContext(), CctvPlayerActivity.class);
        intent.putExtra("urlStr", data.optString("Video"));
        startActivity(intent);
    }
}
