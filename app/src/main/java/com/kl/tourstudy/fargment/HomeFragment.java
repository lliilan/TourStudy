package com.kl.tourstudy.fargment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.TourAdapter;

/**
 * 主页面Fragment
 * Created by KL on 2017/4/12 0012.
 */

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main1,container,false);
        initView(view);
        initListener();
        return view;
    }

    private void initListener() {

    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        TourAdapter adapter = new TourAdapter();
        recyclerView.setAdapter(adapter);
    }

}
