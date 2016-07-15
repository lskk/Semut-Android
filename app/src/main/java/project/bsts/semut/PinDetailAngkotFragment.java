package project.bsts.semut;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

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


public class PinDetailAngkotFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = PinDetailCCTVFragment.class.getSimpleName();
    private JSONObject data;
    private TextView nameText;
    private TextView detailText;
    private TextView jmlText;
    private ImageView thumb;
    private Button watchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View view = inflater.inflate(R.layout.fragment_pin_detail_angkot, container, false);

        nameText = (TextView) view.findViewById(R.id.nameText);
        detailText = (TextView) view.findViewById(R.id.plat);
        jmlText = (TextView)view.findViewById(R.id.jmlPenumpang);

        watchButton = (Button) view.findViewById(R.id.jadwalBtn);
        watchButton.setOnClickListener(this);

        nameText.setEllipsize(TextUtils.TruncateAt.END);
        nameText.setMaxLines(1);
        String namatrayek = data.optString("NAMA_TRAYEK").replaceAll("_", " ");
        nameText.setText(namatrayek );

        detailText.setEllipsize(TextUtils.TruncateAt.END);
        detailText.setMaxLines(1);
        detailText.setText("Plat Nomor : "+data.optString("PLAT_NOMER"));

        jmlText .setEllipsize(TextUtils.TruncateAt.END);
        jmlText.setMaxLines(1);
        jmlText .setText("Jumlah Penumpang : "+data.optString("JUMLAH_PENUMPANG"));

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
        Toast.makeText(getActivity(), "Detail Belum Tersedia", Toast.LENGTH_LONG).show();
    }
}

