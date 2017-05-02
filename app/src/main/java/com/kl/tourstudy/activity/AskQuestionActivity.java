package com.kl.tourstudy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kl.tourstudy.R;
import com.kl.tourstudy.util.Constant;
import com.kl.tourstudy.util.HttpCallbackListener;
import com.kl.tourstudy.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

public class AskQuestionActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{

    private Button submit;
    private EditText title,info;
    String url,question_title,question_info;
    private ImageView back;

    private int tour_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.askquestion_layout);
        getView();
    }

    public void getView(){
        submit= (Button) findViewById(R.id.question_submit);
        info= (EditText) findViewById(R.id.question_info);
        title= (EditText) findViewById(R.id.question_title);
        submit.setOnClickListener(this);
//        back= (ImageView) findViewById(R.id.back);
//        back.setOnTouchListener(this);
        Intent intent=getIntent();
        tour_id=intent.getIntExtra("tour_id",0);
    }

    @Override
    public void onClick(View v) {
        question_title=title.getText().toString();
        question_info=info.getText().toString();
        if (!isEmpty(question_title,question_info)){

        } else {
            try {
                question_title= URLEncoder.encode(question_title, "utf-8");
                question_info=URLEncoder.encode(question_info, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url= IP + PROJECT +"QuestionServlet?action=insert&question_title="+question_title+"&question_info="+question_info+"&tour_id=1"+"&question_user_id=1";
            HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Log.e("传回来的数据是：",response.toString());
                    if (response.equals("success")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AskQuestionActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(AskQuestionActivity.this,QuestionListActivity.class);
                                setResult(RESULT_OK,i);
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AskQuestionActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }

    private boolean isEmpty(String title,String info) {
        if (title.equals("")) {
            Toast.makeText(AskQuestionActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (info.equals("")) {
            Toast.makeText(AskQuestionActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            back.setBackgroundResource(R.drawable.back_black);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            back.setBackgroundResource(R.drawable.back_white);
        }
        Intent intent=new Intent(AskQuestionActivity.this,QuestionListActivity.class);
        setResult(RESULT_OK,intent);
        finish();
        return false;
    }
}
