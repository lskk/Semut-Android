package project.bsts.semut.fragments.map;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.qiujuer.genius.ui.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import project.bsts.semut.R;
import project.bsts.semut.pojo.mapview.UserMap;

public class MapUserFragment extends Fragment {

 //   @BindView(R.id.profile_image)
    CircleImageView profileImg;
 //   @BindView(R.id.name_user)
    TextView nameUserText;
 //   @BindView(R.id.point_user)
    TextView pointUserText;
 //   @BindView(R.id.lastseen_user)
    TextView lastSeenText;
 //   @BindView(R.id.add_or_profile_btn)
    Button addOrProfileBtn;

    private UserMap userMap;
    private boolean isFriend = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_user_fragment, container, false);
        profileImg = (CircleImageView) view.findViewById(R.id.profile_image);
        nameUserText = (TextView)view.findViewById(R.id.name_user);
        pointUserText = (TextView)view.findViewById(R.id.point_user);
        lastSeenText = (TextView)view.findViewById(R.id.lastseen_user);
        addOrProfileBtn = (Button)view.findViewById(R.id.add_or_profile_btn);
//        ButterKnife.bind(getActivity(), view);


        isFriend = userMap.getFriend();
        if(isFriend) addOrProfileBtn.setText("Lihat Profile");
        else addOrProfileBtn.setText("Tambahkan Teman");


        nameUserText.setText(userMap.getName());
        String tmp = "<b>Point : </b>"+ userMap.getPoin();
        pointUserText.setText(Html.fromHtml(tmp));
        tmp = "<b>Terakhir dilihat : </b>"+ userMap.getLastLocation().getTimespan();
        lastSeenText.setText(Html.fromHtml(tmp));


        return view;
    }


    public void setData(UserMap userMap){
        this.userMap = userMap;
    }

}
