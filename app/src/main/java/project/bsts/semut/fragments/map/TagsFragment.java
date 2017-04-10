package project.bsts.semut.fragments.map;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import project.bsts.semut.R;


public class TagsFragment extends Fragment implements View.OnClickListener {
    private ImageButton closeButton;
    private ImageButton backButton;
    Context context;
    private int level;
    private int parentID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        int layout = R.layout.fragment_tags;

        if(getLevel() == 1){
            switch (getParentID()){
                case 0:
                    layout = R.layout.fragment_tags0;
                    break;
                case 1:
                    layout = R.layout.fragment_tags1;
                    break;
                case 2:
                    layout = R.layout.fragment_tags2;
                    break;
                case 3:
                    layout = R.layout.fragment_tags3;
                    break;
                case 4:
                    layout = R.layout.fragment_tags4;
                    break;
                case 5:
                    layout = R.layout.fragment_tags5;
                    break;
                case 6:
                    layout = R.layout.fragment_tags6;
                    break;
            }
        }

        View v = inflater.inflate(layout, container, false);

        backButton = (ImageButton) v.findViewById(R.id.backButton);
        closeButton = (ImageButton) v.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(v1 -> getActivity().finish());

        if(backButton != null) {
            backButton.setOnClickListener(v12 -> back());
        }

        int[] ids = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6};
        for(int i=0; i<7; i++){
            ImageButton btn = (ImageButton)v.findViewById(ids[i]);
            if(btn != null) {
                btn.setOnClickListener(this);
            }
        }

        return v;
    }

    // actions
    @Override
    public void onClick(View v) {
        if(getLevel() == 0){
            FragmentManager fragmentManager = getFragmentManager();

            if(fragmentManager.getBackStackEntryCount() > 1){
                return;
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.animator.come_from_right_fragment,
                    R.animator.out_to_right_fragment,
                    R.animator.come_from_right_fragment,
                    R.animator.out_to_right_fragment);

            TagsFragment tagsFragment = new TagsFragment();

            switch (v.getId()){
                case R.id.btn0:
                    tagsFragment.setLevel(1);
                    tagsFragment.parentID = 0;
                    break;
                case R.id.btn1:
                    tagsFragment.setLevel(1);
                    tagsFragment.parentID = 1;
                    break;
                case R.id.btn2:
                    tagsFragment.setLevel(1);
                    tagsFragment.parentID = 2;
                    break;
                case R.id.btn3:
                    tagsFragment.setLevel(1);
                    tagsFragment.parentID = 3;
                    break;
                case R.id.btn4:
                    tagsFragment.setLevel(1);
                    tagsFragment.parentID = 4;
                    break;
                case R.id.btn5:
                    tagsFragment.setLevel(1);
                    tagsFragment.parentID = 5;
                    break;
                case R.id.btn6:
                    tagsFragment.setLevel(1);
                    tagsFragment.parentID = 6;
                    break;
            }

            fragmentTransaction.add(R.id.container, tagsFragment);
            fragmentTransaction.addToBackStack("second");
            fragmentTransaction.commit();
        }else{
            FragmentManager fragmentManager = getFragmentManager();

            if(fragmentManager.getBackStackEntryCount() > 2){
                return;
            }

            int index = 0;
            switch (v.getId()){
                case R.id.btn0:
                    index = 0;
                    break;
                case R.id.btn1:
                    index = 1;
                    break;
                case R.id.btn2:
                    index = 2;
                    break;
                case R.id.btn3:
                    index = 3;
                    break;
                case R.id.btn4:
                    index = 4;
                    break;
                case R.id.btn5:
                    index = 5;
                    break;
                case R.id.btn6:
                    index = 6;
                    break;
            }

            SubmitTagFragment submit = new SubmitTagFragment();
            submit.setPostID(this.parentID);
            submit.setSubPostID(index);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.animator.come_from_right_fragment, R.animator.out_to_right_fragment, R.animator.come_from_right_fragment, R.animator.out_to_right_fragment);
            fragmentTransaction.add(R.id.container, submit);
            fragmentTransaction.addToBackStack("third");
            fragmentTransaction.commit();
        }
    }

    public void back(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
}
