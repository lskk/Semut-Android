package project.bsts.semut.fragments.map;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import project.bsts.semut.R;
import project.bsts.semut.connections.rest.IConnectionResponseHandler;
import project.bsts.semut.connections.rest.RequestRest;
import project.bsts.semut.pojo.RequestStatus;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.CommonAlerts;


public class SubmitTagFragment extends Fragment implements TextWatcher, IConnectionResponseHandler{
    private TextView titleText;
    private TextView dateText;
    private TextView counterText;
    private EditText remarks;
    private ImageView thumb;
    private ImageButton closeButton;
    private ImageButton backButton;
    private Button submitButton;

    private int postID;
    private int subPostID;
    Date currentDate;
    private ProgressDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_tag, container, false);

        titleText = (TextView)view.findViewById(R.id.title);
        dateText = (TextView)view.findViewById(R.id.date);
        counterText = (TextView)view.findViewById(R.id.counter);
        remarks = (EditText)view.findViewById(R.id.remarks);
        remarks.addTextChangedListener(this);
        thumb = (ImageView)view.findViewById(R.id.thumb);
        submitButton = (Button)view.findViewById(R.id.submitButton);

        dialog = new ProgressDialog(getActivity(), R.style.MaterialBaseTheme_Light_AlertDialog);
        dialog.setMessage("Memuat...");
        dialog.setCancelable(false);

        currentDate = Calendar.getInstance().getTime();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEEE, dd MMWW yyyy HH:mm:ss");
        String formattedCurrentDate = format.format(currentDate);
        dateText.setText(formattedCurrentDate);

        HashMap<String, String> info = getPostInfo();
        titleText.setText(info.get("text"));
        thumb.setImageResource(Integer.parseInt(info.get("res")));

        backButton = (ImageButton) view.findViewById(R.id.backButton);
        closeButton = (ImageButton) view.findViewById(R.id.closeButton);



        closeButton.setOnClickListener(v -> getActivity().finish());
        backButton.setOnClickListener(v -> back());
        submitButton.setOnClickListener(v -> {
            submit();
        });

        return view;
    }

    public void back(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    private HashMap<String, String> getPostInfo(){
        int[] childNums = {3, 2, 3, 2, 5, 4};
        String[] texts = {"Normal", "Padat", "Macet Total", "Polisi Patroli", "Pemeriksaan Polisi", "Kecelakaan", "Kecelakaan dengan Korban Jiwa", "Mobil Mogok", "Pohon Tumbang", "Banjir", "Perbaikan Jalan Ringan", "Perbaikan Jalan Rusak", "Event", "Pembangunan", "Demonstrasi", "Tidak Ada Tanda", "Jalan Rusak", "Bus Berhenti", "Tempat Padat/Ramai", "BRT", "Angkot"};

        int postID = 0;
        for(int i=0; i<this.postID; i++){
            postID += childNums[i];
        }
        postID += this.subPostID;

        int resID = getResId("menu_post_sub_" + twoDigitString(postID+1));

        HashMap<String, String> map = new HashMap();
        map.put("res", ""+resID);
        map.put("text", texts[postID]);
        map.put("id", ""+(postID+1));

        return map;
    }

    private String twoDigitString(int a){
        if(a < 10) return "0"+a;

        return "" + a;
    }

    private static int getResId(String resName) {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(resName);
            int drawableId = field.getInt(null);

            return drawableId;
        }
        catch (Exception e) {
            return -1;
        }
    }

    private void submit() {
        if(remarks.getText().toString().equals("")) Toast.makeText(getActivity(), "Anda belum mengisi keterangan", Toast.LENGTH_LONG).show();
        else {
            dialog.show();
            RequestRest requestRest = new RequestRest(getActivity(), this);
            requestRest.insertPost(getPostID(), getSubPostID(), remarks.getText().toString());
        }

    }

    // setter dan getter
    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getSubPostID() {
        return subPostID;
    }

    public void setSubPostID(int subPostID) {
        this.subPostID = subPostID;
    }

    // listener text
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        counterText.setText(s.length() + " of 128");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onSuccessRequest(String pResult, String type) {
        dialog.dismiss();
        switch (type){
            case Constants.REST_INSERT_POST:
                RequestStatus requestStatus = new Gson().fromJson(pResult, RequestStatus.class);
                if(requestStatus.getSuccess()) {
                    Toast.makeText(getActivity(), "Berhasil mengirimkan laporan. Laporan Anda akan tampil dalam beberapa menit", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else CommonAlerts.commonError(getActivity(), requestStatus.getMessage());
                break;
            case Constants.REST_ERROR:
                CommonAlerts.commonError(getActivity(), Constants.MESSAGE_HTTP_ERROR);
                break;
        }
    }
}