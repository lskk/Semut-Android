package project.bsts.semut.fragments.map;


import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import net.qiujuer.genius.ui.widget.Button;
import de.hdodenhof.circleimageview.CircleImageView;
import project.bsts.semut.R;
import project.bsts.semut.pojo.mapview.UserMap;

public class MapUserFragment extends Fragment {

    CircleImageView profileImg;
    TextView nameUserText;
    TextView pointUserText;
    TextView lastSeenText;
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

        Context context = getActivity();
        Drawable drawable = (userMap.getGender()==1) ? new IconicsDrawable(getActivity())
                .color(context.getResources().getColor(R.color.accent))
                .sizeDp(96)
                .icon(CommunityMaterial.Icon.cmd_emoticon_cool) : new IconicsDrawable(getActivity())
                .color(context.getResources().getColor(R.color.accent))
                .sizeDp(96)
                .icon(CommunityMaterial.Icon.cmd_emoticon_excited);


        profileImg.setImageDrawable(drawable);
        isFriend = userMap.getFriend();
        if(isFriend) addOrProfileBtn.setText("Lihat Profile");
        else addOrProfileBtn.setText("Tambahkan Teman");
        addOrProfileBtn.setVisibility(View.GONE);


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
