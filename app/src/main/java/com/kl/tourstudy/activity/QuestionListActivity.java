package com.kl.tourstudy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.kl.tourstudy.R;
import com.kl.tourstudy.adapter.QuestionAdapter;
import com.kl.tourstudy.table.Question;
import com.kl.tourstudy.util.DateUtil;
import com.kl.tourstudy.util.HttpCallbackListener;
import com.kl.tourstudy.util.HttpUtil;
import com.kl.tourstudy.util.UI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * Created by Administrator on 2017/3/31.
 */
public class QuestionListActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener,AdapterView.OnItemClickListener,View.OnTouchListener{

    private ListView questionList;
    private QuestionAdapter questionAdapter;
    private List<Question> questions=new ArrayList<Question>();

    private ImageView back;

    private SwipeRefreshLayout swipeRefreshLayout;//刷新控件
    private String flag="ready";//是否从服务器获取到数据的标记

    private View loadMoreView;
    private Button loadMoreButton;

    private Button askQuestion;

    private int currPage=1;    //当前页码
    private int total;  //所有记录数
    private int tour_id = 1;
    private String name="用户";

    private static final String TAG = "QuestionListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionlist_layout);
        initValue();
        askQuestion= (Button) findViewById(R.id.ask_question_button);
        askQuestion.setOnClickListener(this);
//        Intent intent=getIntent();   //获取tour_id
//        tour_id=intent.getIntExtra("tour_id",1);

        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh_question);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currPage=1;
                questions.clear();
                HttpUtil.sendHttpRequestTest(IP + PROJECT + "QuestionServlet?action=selectAll&currPage="+currPage+"&tour_id="+tour_id, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        try {
                            Log.e("当前页",currPage+"");
                            JSONObject object=new JSONObject(response);
                            String totalRecords=object.get("totalRecords").toString();
                            total=Integer.parseInt(totalRecords);
                            String list=object.get("list").toString();
                            JSONArray array=new JSONArray(list);
                            for (int i=0;i<array.length();i++){
                                JSONObject item=array.getJSONObject(i);
                                String question_title=item.getString("question_title");
                                String question_info=item.getString("question_info");
                                String date=item.getString("question_date");
                                String question_date= DateUtil.compareDate(DateUtil.getDate(),date);
                                int answer_count=item.getInt("answer_count");
                                int visit_count=item.getInt("visit_count");
                                int question_id=item.getInt("question_id");
                                int user_img= R.mipmap.icon ;//用户头像
                                String user_name=name ;//用户昵称
                                Question question=new Question(question_id,visit_count,answer_count,question_title,question_info,question_date,user_img,user_name);
                                questions.add(question);
                            }
                            flag="success";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (flag.equals("success")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(questionAdapter==null){
                                        questionList= (ListView) findViewById(R.id.question_lv);
                                        loadMoreView=getLayoutInflater().inflate(R.layout.loadmore_layout,null);
                                        questionList.addFooterView(loadMoreView);
                                        loadMoreButton= (Button) loadMoreView.findViewById(R.id.loadmore_button);
                                        loadMoreButton.setOnClickListener(QuestionListActivity.this);
                                        questionAdapter=new QuestionAdapter(QuestionListActivity.this, R.layout.question_item,questions);
                                        questionList.setAdapter(questionAdapter);
                                        questionList.setOnItemClickListener(QuestionListActivity.this);
                                    }else {
                                        questionAdapter.notifyDataSetChanged();
                                        questionList.setOnItemClickListener(QuestionListActivity.this);
                                    }

                                    swipeRefreshLayout.setRefreshing(false);
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
//                                UI.closeProgressDialog();
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(QuestionListActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
    }

    public void getData(){
//        UI.showProgressDialog(QuestionListActivity.this);
        HttpUtil.sendHttpRequestTest(IP + PROJECT + "QuestionServlet?action=selectAll&currPage="+currPage+"&tour_id="+tour_id, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    String totalRecords=object.get("totalRecords").toString();
                    total=Integer.parseInt(totalRecords);
                    String list=object.get("list").toString();
                    JSONArray array=new JSONArray(list);
                    for (int i=0;i<array.length();i++){
                        JSONObject item=array.getJSONObject(i);
                        String question_title=item.getString("question_title");
                        String question_info=item.getString("question_info");
                        String date=item.getString("question_date");
                        String question_date= DateUtil.compareDate(DateUtil.getDate(),date);
                        int answer_count=item.getInt("answer_count");
                        int visit_count=item.getInt("visit_count");
                        int question_id=item.getInt("question_id");
                        int user_img= R.mipmap.icon  ;//用户头像
                        String user_name=name ;//用户昵称
                        Question question=new Question(question_id,visit_count,answer_count,question_title,question_info,question_date,user_img,user_name);
                        questions.add(question);
                    }
                    flag="success";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(flag.equals("success")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            questionList= (ListView) findViewById(R.id.question_lv);
                            loadMoreView=getLayoutInflater().inflate(R.layout.loadmore_layout,null);
                            questionList.addFooterView(loadMoreView);
                            loadMoreButton= (Button) loadMoreView.findViewById(R.id.loadmore_button);
                            loadMoreButton.setOnClickListener(QuestionListActivity.this);
                            questionAdapter=new QuestionAdapter(QuestionListActivity.this, R.layout.question_item,questions);
                            questionList.setAdapter(questionAdapter);
//                            questionList.addFooterView(loadMoreView);
//                            loadMoreButton= (Button) findViewById(R.id.loadmore_button);
//                            loadMoreButton.setOnClickListener(QuestionListActivity.this);
                            questionList.setOnItemClickListener(QuestionListActivity.this);
                            UI.closeProgressDialog();
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
                        UI.closeProgressDialog();
                        Toast.makeText(QuestionListActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void initValue(){
//        back= (ImageView) findViewById(R.id.back);
//        back.setOnTouchListener(this);
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
//                questionAdapter.notifyDataSetChanged();
                break;
            case 2:
//                questionAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loadmore_button:
                loadMoreButton.setText("加载中");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                        loadMoreButton.setText("点击查看更多");
                    }
                });
                break;
            case R.id.ask_question_button:
                Intent intent=new Intent(QuestionListActivity.this,AskQuestionActivity.class);
                intent.putExtra("tour_id",tour_id);
                startActivityForResult(intent,2);
//                startActivity(intent);
                break;
        }

    }

    /**
     * 加载更多数据
     */
    private void loadMoreData(){
        currPage++;
        HttpUtil.sendHttpRequestTest(IP + PROJECT + "QuestionServlet?action=selectAll&currPage="+currPage+"&tour_id="+tour_id, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    String list=object.get("list").toString();
                    JSONArray array=new JSONArray(list);
                    for (int i=0;i<array.length();i++){
                        JSONObject item=array.getJSONObject(i);
                        String question_title=item.getString("question_title");
                        String question_info=item.getString("question_info");
                        String date=item.getString("question_date");
                        String question_date= DateUtil.compareDate(DateUtil.getDate(),date);
                        int answer_count=item.getInt("answer_count");
                        int visit_count=item.getInt("visit_count");
                        int question_id=item.getInt("question_id");
                        int user_img= R.mipmap.icon  ;//用户头像
                        String user_name=name ;//用户昵称
                        Question question=new Question(question_id,visit_count,answer_count,question_title,question_info,question_date,user_img,user_name);
                        questions.add(question);
                    }
                    flag="success";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (flag.equals("success")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            questionAdapter.notifyDataSetChanged();
                            questionList.setOnItemClickListener(QuestionListActivity.this);
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
                        UI.closeProgressDialog();
                        Toast.makeText(QuestionListActivity.this,"加载失败", Toast.LENGTH_SHORT).show();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(QuestionListActivity.this,QuestionInfoActivity.class);
        int question_id=questions.get(position).getQuestion_id();
        String question_title=questions.get(position).getQuestion_title();
        String question_info=questions.get(position).getQuestion_info();
        String question_date=questions.get(position).getQuestion_date();
        intent.putExtra("question_id",question_id);
        intent.putExtra("question_title",question_title);
        intent.putExtra("question_info",question_info);
        intent.putExtra("question_date",question_date);
        startActivityForResult(intent,1);
//        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            back.setBackgroundResource(R.drawable.back_black);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            back.setBackgroundResource(R.drawable.back_white);
        }
//        Intent intent=new Intent(QuestionListActivity.this,RecyclerItemActivity.class); //返回推荐的路线页面
//        setResult(RESULT_OK,intent);                            //
        finish();
        return false;
    }
}
