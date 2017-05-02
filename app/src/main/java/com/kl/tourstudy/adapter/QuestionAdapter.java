package com.kl.tourstudy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kl.tourstudy.R;
import com.kl.tourstudy.table.Question;

import java.util.List;

/**
 * Created by Administrator on 2017/4/3.
 */
public class QuestionAdapter extends ArrayAdapter<Question>{

    private int resourceID;

    public QuestionAdapter(Context context, int resource, List<Question> objects) {
        super(context, resource, objects);
        resourceID=resource;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        Question question=getItem(position);
        View v;
        ViewHolder viewHolder;
        if (convertView==null){
            v= LayoutInflater.from(getContext()).inflate(resourceID,null);
            viewHolder=new ViewHolder();
            viewHolder.img=(ImageView) v.findViewById(R.id.question_user_img);
            viewHolder.name= (TextView) v.findViewById(R.id.question_user_name_item);
            viewHolder.title=(TextView) v.findViewById(R.id.question_title_item);
            viewHolder.info=(TextView) v.findViewById(R.id.question_info_item);
            viewHolder.date=(TextView) v.findViewById(R.id.question_date_item);
            viewHolder.visit= (TextView) v.findViewById(R.id.question_visit_count_item);
            viewHolder.answer= (TextView) v.findViewById(R.id.question_answer_count_item);
            v.setTag(viewHolder);
        }else{
            v=convertView;
            viewHolder=(ViewHolder) v.getTag();
        }
        viewHolder.img.setBackgroundResource(question.getImg());
        viewHolder.name.setText(question.getUser_name());
        viewHolder.title.setText(question.getQuestion_title());
        viewHolder.info.setText(question.getQuestion_info());
        viewHolder.date.setText(question.getQuestion_date());
        viewHolder.visit.setText(String.valueOf(question.getVisit_count()));
        viewHolder.answer.setText(String.valueOf(question.getAnswer_count()));
        return v;
    }

    class ViewHolder{
        ImageView img;
        TextView title,name,date,visit,answer,info;
    }
}
