package com.kl.tourstudy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kl.tourstudy.R;
import com.kl.tourstudy.table.Answer;

import java.util.List;

/**
 * Created by Administrator on 2017/4/4.
 */
public class AnswerAdapter extends ArrayAdapter<Answer>{

    private int resourceID;

    public AnswerAdapter(Context context, int resource, List<Answer> object) {
        super(context, resource,object);
        resourceID=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Answer answer=getItem(position);
        View v;
        ViewHolder viewHolder;
        if (convertView==null){
            v= LayoutInflater.from(getContext()).inflate(resourceID,null);
            viewHolder=new ViewHolder();
            viewHolder.img=(ImageView) v.findViewById(R.id.answer_user_img);
            viewHolder.name= (TextView) v.findViewById(R.id.answer_user_name_item);
            viewHolder.info=(TextView) v.findViewById(R.id.answer_info_item);
            viewHolder.date=(TextView) v.findViewById(R.id.answer_date_item);
            viewHolder.support= (TextView) v.findViewById(R.id.answer_support_items);
            viewHolder.support_img= (ImageButton) v.findViewById(R.id.support_image);
            v.setTag(viewHolder);
        }else{
            v=convertView;
            viewHolder=(ViewHolder) v.getTag();
        }
        viewHolder.img.setImageResource(answer.getImg());
        viewHolder.name.setText(answer.getUser_name());
        viewHolder.info.setText(answer.getAnswer_info());
        viewHolder.date.setText(answer.getAnswer_date());
        viewHolder.support.setText(String.valueOf(answer.getSupport_count()));
        if(answer.getShowState()==1){
            Log.e("应该变红的是",answer.getAnswer_info());
            viewHolder.support.setTextColor(Color.RED);
        }else {
            viewHolder.support.setTextColor(Color.GRAY);
        }
        viewHolder.support_img.setBackgroundResource(answer.getSupport_img());
        return v;
    }

    class ViewHolder{
        ImageView img;
        TextView info,date,support,name;
        ImageButton support_img;
    }
}
