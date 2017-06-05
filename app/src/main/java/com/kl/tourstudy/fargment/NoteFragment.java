package com.kl.tourstudy.fargment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kl.tourstudy.R;
import com.kl.tourstudy.activity.NoteListActivity;

/**
 * Created by KL on 2017/4/12 0012.
 */

public class NoteFragment extends Fragment {

    private View socail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main3, container, false);
        socail=view.findViewById(R.id.socail);
        socail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(getActivity(), NoteListActivity.class);
                startActivity(intent);
                return false;
            }
        });
        initView();
        return view;
    }

    private void initView() {

    }
}
