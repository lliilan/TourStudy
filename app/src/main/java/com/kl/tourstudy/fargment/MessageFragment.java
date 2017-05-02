package com.kl.tourstudy.fargment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kl.tourstudy.R;

/**
 * Created by KL on 2017/4/12 0012.
 */

public class MessageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main2, container, false);
        initView();
        return view;
    }

    private void initView() {
    }
}
