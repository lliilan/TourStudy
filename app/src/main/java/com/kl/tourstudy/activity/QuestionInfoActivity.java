package com.kl.tourstudy.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.AnswerAdapter;
import com.kl.tourstudy.table.Answer;
import com.kl.tourstudy.util.Constant;
import com.kl.tourstudy.util.DateUtil;
import com.kl.tourstudy.util.HttpCallbackListener;
import com.kl.tourstudy.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * Created by Administrator on 2017/4/3.
 */
public class QuestionInfoActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener,AdapterView.OnItemClickListener,AbsListView.OnScrollListener,View.OnFocusChangeListener{

    private ListView answerList;
    private List<Answer> answers=new ArrayList<Answer>();
    private AnswerAdapter answerAdapter;
    private TextView question_title,question_info,question_date;
    private Button submit;
    private EditText answer_info;
    private String question_id,submitInfo;
    private ImageView back;
    private TextView temp_info,temp_date,temp_support;
    private View temp_layout;
    private String flag="ready",url;
    private ImageButton support;
    private TextView answer_support,question_user_name;
    private ImageView userimg;
    private SwipeRefreshLayout swipeRefreshLayout;//刷新控件
//    private Button loadmore;    //底部加载更多数据的按钮

    private View loadMoreView;
    private Button loadMoreButton;

    private int currPage=1;    //当前页码
    private int total;  //所有记录数

    private int support_count;

    String title;
    String info;
    String date;

    int state;
    private String name="用户";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questioninfo_layout);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getView();
        initValue();

        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh_answer);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });
        swipeRefreshLayout.setOnRefreshListener(lister);
    }

    /**
     * 获取UI控件
     */
    public void getView(){
        question_title= (TextView) findViewById(R.id.question_title_info);
        question_info= (TextView) findViewById(R.id.question_info_info);
        question_date= (TextView) findViewById(R.id.question_date_info);
        submit= (Button) findViewById(R.id.answer_submit);
        answer_info= (EditText) findViewById(R.id.answer_info);
        answer_info.setOnFocusChangeListener(this);
        userimg= (ImageView) findViewById(R.id.question_user_img);
        question_user_name= (TextView) findViewById(R.id.question_user_name);
        submit.setOnClickListener(this);
//        back= (ImageView) findViewById(R.id.back);
//        back.setOnTouchListener(this);
    }

    /**
     * 初始化控件的value，获取intent传过来的值
     */
    public void initValue(){
        Intent intent=getIntent();
        title=intent.getStringExtra("question_title");
        info=intent.getStringExtra("question_info");
        date=intent.getStringExtra("question_date");
        userimg.setBackgroundResource(R.mipmap.icon); //用户头像
        question_user_name.setText("用户");   //用户昵称
        question_title.setText(title);
        question_info.setText(info);
        question_date.setText(date);
        question_id=String.valueOf(intent.getIntExtra("question_id",0));
        getData();
    }

    private ProgressDialog progressDialog;
    /**
     * 显示进度对话框
     */
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     *获取服务器传回来的数据
     */
    public void getData(){
        showProgressDialog();
        HttpUtil.sendHttpRequest(IP + PROJECT + "AnswerServlet?action=firstSelectAnswer&question_id=" + question_id+"&currPage="+currPage+"&user_id=1", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    String list=object.get("list").toString();  //获取问题信息
                    JSONArray array=new JSONArray(list);       //问题信息的json
//                    JSONObject q=qs.getJSONObject(0);
//                    String allAnswer=q.get("answers").toString();     //获取问题里的回答信息
//                    JSONArray array=new JSONArray(allAnswer);
                    for (int i=0;i<array.length();i++){
                        JSONObject item=array.getJSONObject(i);
                        String answer_info=item.getString("answer_info");
                        String date=item.getString("answer_date");
                        String answer_date= DateUtil.compareDate(DateUtil.getDate(),date);
                        int support_count=item.getInt("support_count");
                        int showState=item.getInt("showState");
                        int answer_id=item.getInt("answer_id");
                        int user_img=R.mipmap.icon;//用户头像
                        String answer_user_name=name;//用户昵称
                        Answer answer=null;
                        if(showState==0){
                            answer=new Answer(answer_id,answer_date,answer_info,support_count,user_img,R.drawable.dot2,showState,answer_user_name);
                        }else{
                            answer=new Answer(answer_id,answer_date,answer_info,support_count,user_img ,R.drawable.dot1,showState,answer_user_name);
                        }
                        answers.add(answer);
                    }
                    flag="success";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(flag.equals("success")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            answerList= (ListView) findViewById(R.id.answer_lv);
                            loadMoreView=getLayoutInflater().inflate(R.layout.loadmore_layout,null);
                            answerList.addFooterView(loadMoreView);
                            loadMoreButton= (Button) loadMoreView.findViewById(R.id.loadmore_button);
                            loadMoreButton.setOnClickListener(QuestionInfoActivity.this);
                            answerAdapter=new AnswerAdapter(QuestionInfoActivity.this,R.layout.answer_item,answers);
                            answerList.setAdapter(answerAdapter);
                            answerList.setOnItemClickListener(QuestionInfoActivity.this);
                            answerList.setOnScrollListener(QuestionInfoActivity.this);
                            closeProgressDialog();
                            flag="ready";
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QuestionInfoActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
            }
        });
    }

    SwipeRefreshLayout.OnRefreshListener lister=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            currPage=1;
            answers.clear();
            HttpUtil.sendHttpRequest(IP + PROJECT + "AnswerServlet?action=selectAll&question_id=" + question_id+"&&currPage="+currPage+"&user_id=1", new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    try {
                        JSONObject object=new JSONObject(response);
                        String list=object.get("list").toString();  //获取问题信息
                        JSONArray array=new JSONArray(list);       //问题信息的json
//                    JSONObject q=qs.getJSONObject(0);
//                    String allAnswer=q.get("answers").toString();     //获取问题里的回答信息
//                    JSONArray array=new JSONArray(allAnswer);
                        for (int i=0;i<array.length();i++){
                            JSONObject item=array.getJSONObject(i);
                            String answer_info=item.getString("answer_info");
                            String date=item.getString("answer_date");
                            String answer_date= DateUtil.compareDate(DateUtil.getDate(),date);
                            int support_count=item.getInt("support_count");
                            int showState=item.getInt("showState");
                            int answer_id=item.getInt("answer_id");
                            int user_img=R.mipmap.icon  ;//用户头像
                            String answer_user_name=name ;//用户昵称
                            Answer answer=null;
                            if(showState==0){
                                answer=new Answer(answer_id,answer_date,answer_info,support_count,user_img,R.drawable.dot2,showState,answer_user_name);
                            }else{
                                answer=new Answer(answer_id,answer_date,answer_info,support_count,user_img ,R.drawable.dot1,showState,answer_user_name);
                            }
                            answers.add(answer);
                        }
                        flag="success";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(flag.equals("success")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                    answerList= (ListView) findViewById(R.id.answer_lv);
//                                    answerAdapter=new AnswerAdapter(QuestionInfoActivity.this,R.layout.answer_item,answers);
//                                    answerList.setAdapter(answerAdapter);
//                                    answerList.setOnItemClickListener(QuestionInfoActivity.this);
                                if (answerAdapter==null){
                                    answerList= (ListView) findViewById(R.id.answer_lv);
                                    loadMoreView=getLayoutInflater().inflate(R.layout.loadmore_layout,null);
                                    answerList.addFooterView(loadMoreView);
                                    loadMoreButton= (Button) loadMoreView.findViewById(R.id.loadmore_button);
                                    loadMoreButton.setOnClickListener(QuestionInfoActivity.this);
                                    answerAdapter=new AnswerAdapter(QuestionInfoActivity.this,R.layout.answer_item,answers);
                                    answerList.setAdapter(answerAdapter);
                                    answerList.setOnItemClickListener(QuestionInfoActivity.this);
                                    answerList.setOnScrollListener(QuestionInfoActivity.this);
                                }else{
                                    answerAdapter.notifyDataSetChanged();
                                    answerList.setOnItemClickListener(QuestionInfoActivity.this);
                                    answerList.setOnScrollListener(QuestionInfoActivity.this);
                                }

                                flag="ready";
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(QuestionInfoActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    };


    /**
     * 提交答案/点击底部加载数据
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.answer_submit:    //点击提交答案的按钮
                submitInfo=answer_info.getText().toString();
                if (!isEmpty(submitInfo)){
                }else{
                    try {
                        submitInfo= URLEncoder.encode(submitInfo,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    answer_info.clearFocus();
                    url=IP + PROJECT + "AnswerServlet?action=insert&answer_info="+submitInfo+"&question_id="+question_id+"&user_id=1";
                    HttpUtil.sendHttpRequest(url,new HttpCallbackListener(){
                        @Override
                        public void onFinish(String response) {
                            if(response.equals("success")){
                                Log.e("y运行 ","开始");
                                swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh_answer);
                                swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
                                swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                    }
                                });
                                lister.onRefresh();
                                answer_info.setText("");
                            }

                        }

                        @Override
                        public void onError(Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(QuestionInfoActivity.this,"提交失败", Toast.LENGTH_SHORT).show();
                                    closeProgressDialog();
                                }
                            });
                        }
                    });
                }
                break;
            case R.id.loadmore_button:
                loadMoreButton.setText("加载中");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                        answerAdapter.notifyDataSetChanged();
                        answerList.setOnItemClickListener(QuestionInfoActivity.this);
                        answerList.setOnScrollListener(QuestionInfoActivity.this);
                        loadMoreButton.setText("点击查看更多");
                        answerList.setOnItemClickListener(QuestionInfoActivity.this);
                        answerList.setOnScrollListener(QuestionInfoActivity.this);
                    }
                });
        }


    }

    /**
     * 加载更多数据
     */
    private void loadMoreData(){
        currPage++;
        HttpUtil.sendHttpRequest(IP + PROJECT +  "AnswerServlet?action=selectAll&question_id=" + question_id+"&&currPage="+currPage+"&user_id=1", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    String list=object.get("list").toString();  //获取问题信息
                    JSONArray array=new JSONArray(list);       //问题信息的json
//                    JSONObject q=qs.getJSONObject(0);
//                    String allAnswer=q.get("answers").toString();     //获取问题里的回答信息
//                    JSONArray array=new JSONArray(allAnswer);
                    for (int i=0;i<array.length();i++){
                        JSONObject item=array.getJSONObject(i);
                        String answer_info=item.getString("answer_info");
                        String date=item.getString("answer_date");
                        String answer_date= DateUtil.compareDate(DateUtil.getDate(),date);
                        int support_count=item.getInt("support_count");
                        int showState=item.getInt("showState");
                        int answer_id=item.getInt("answer_id");
                        int user_img=R.mipmap.icon  ;//用户头像
                        String answer_user_name=name ;//用户昵称
                        Answer answer=null;
                        if(showState==0){
                            answer=new Answer(answer_id,answer_date,answer_info,support_count,user_img,R.drawable.dot2,showState,answer_user_name);
                        }else{
                            answer=new Answer(answer_id,answer_date,answer_info,support_count,user_img ,R.drawable.dot1,showState,answer_user_name);
                        }
                        answers.add(answer);
                    }
                    flag="success";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(flag.equals("success")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            answerAdapter.notifyDataSetChanged();
                            answerList.setOnItemClickListener(QuestionInfoActivity.this);
                            closeProgressDialog();
                            flag="ready";
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QuestionInfoActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });
            }
        });
    }

    /**
     * 按左上角的返回图标
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            back.setBackgroundResource(R.drawable.back_black);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            back.setBackgroundResource(R.drawable.back_white);
        }
        Intent intent=new Intent(QuestionInfoActivity.this,QuestionListActivity.class);
        setResult(RESULT_OK,intent);
        finish();
        return false;
    }

    private boolean isEmpty(String info) {
        if (info.equals("")) {
            Toast.makeText(QuestionInfoActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        support= (ImageButton) view.findViewById(R.id.support_image);
        answer_support= (TextView) view.findViewById(R.id.answer_support_items);
        support_count=Integer.parseInt(answer_support.getText().toString());
        Log.e("点赞数",support_count+"");
        Log.e("布局获取点赞数",answer_support.getText().toString());
//        answer_support.setText(support_count+1);
//        answer_support.setTextColor(Color.RED);
        int answer_id=answers.get(position).getAnswer_id();
        state=answers.get(position).getShowState();
//        int answer_id=a.getAnswer_id();
        Log.e("点击获取的id是",answer_id+"");

        url= IP + PROJECT + "Answer_supportServlet?&answer_id="+answer_id+"&user_id=1";
        support.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.e("点击到了","点到了");
//                if(state==0){
//                    support.setBackgroundResource(R.drawable.support_red);
//                    support_count=support_count+1;
//                    answer_support.setText(String.valueOf(support_count));
//                    answer_support.setTextColor(Color.RED);
//                    answers.get(position).setShowState(1);
//                }
//                if (state==1){
//                    support.setBackgroundResource(R.drawable.support_gray);
//                    support_count=support_count-1;
//                    answer_support.setText(String.valueOf(support_count));
//                    answer_support.setTextColor(Color.GRAY);
//                    answers.get(position).setShowState(0);
//                }
                HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.equals("red")){
                                    Log.e("红","红");
                                    support.setBackgroundResource(R.drawable.dot1);
                                    support_count=support_count+1;
                                    answer_support.setText(String.valueOf(support_count));
                                    answer_support.setTextColor(Color.RED);
                                }
                                if (response.equals("gray")){
                                    Log.e("灰","灰");
                                    support.setBackgroundResource(R.drawable.dot2);
                                    support_count=support_count-1;
                                    answer_support.setText(String.valueOf(support_count));
                                    answer_support.setTextColor(Color.GRAY);
                                }
                            }
                        });
                    }

                    public void onError(Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(QuestionInfoActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
//                                closeProgressDialog();
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //        scrollState：即滑动的状态。分为三种 0，1，2
//                =0 表示停止滑动的状态 SCROLL_STATE_IDLE
//                =1表示正在滚动，用户手指在屏幕上 SCROLL_STATE_TOUCH_SCROLL
//                =2表示正在滑动。用户手指已经离开屏幕 SCROLL_STATE_FLING
        if (scrollState==0){
            loadMoreData();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Log.e("焦点","j");
        }else {
            Log.e("焦点取消","的较量");
            InputMethodManager inputMethodManager= (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(answer_info.getWindowToken(),0);
        }
    }
}
