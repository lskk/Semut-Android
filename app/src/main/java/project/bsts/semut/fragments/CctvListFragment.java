package project.bsts.semut.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import project.bsts.semut.R;
import project.bsts.semut.adapters.CctvListAdapter;
import project.bsts.semut.pojo.mapview.CctvMap;


public class CctvListFragment extends Fragment {

    ListView mListViewCctv;
    private ArrayList<CctvMap> list = new ArrayList<CctvMap>();

    CctvListAdapter cctvListAdapter;

    public CctvListFragment(){

    }

    public void setData(ArrayList<CctvMap> list){
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View convertView = inflater.inflate(R.layout.fragment_cctv_list, container, false);
        mListViewCctv = (ListView)convertView.findViewById(R.id.list_cctv);
        cctvListAdapter = new CctvListAdapter(getActivity(), list);
        mListViewCctv.setAdapter(cctvListAdapter);

        return convertView;
    }
}
