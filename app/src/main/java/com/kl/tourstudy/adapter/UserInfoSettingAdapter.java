package com.kl.tourstudy.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kl.tourstudy.R;
import com.kl.tourstudy.activity.ABookActivity;
import com.kl.tourstudy.activity.ChooseIconActivity;
import com.kl.tourstudy.activity.ListBookActivity;
import com.kl.tourstudy.activity.NavigationActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * 用户个人信息设置适配器
 * Created by KL on 2017/5/3 0003.
 */

public class UserInfoSettingAdapter extends RecyclerView.Adapter<UserInfoSettingAdapter.ViewHolder> {

    private static final String TAG = "UserInfoSettingAdapter";
    private Context mContext;
    private String[] setting = {"修改头像","详细资料","订单管理","退出登录"};

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView text;

        ViewHolder(final View itemView, final Context context) {
            super(itemView);
            cardView = (CardView) itemView;
            image = (ImageView) itemView.findViewById(R.id.user_image);
            text = (TextView) itemView.findViewById(R.id.user_text);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //写跳转Intent
                    switch (getAdapterPosition()){
                        case 0:
                            //修改用户头像
                            updateIcon(context);
                            break;
                        case 1:
                            //详细资料
                            findUserInfo(context);
                            break;
                        case 2:
                            //订单管理
                            bookManage(context);
                            break;
                        case 3:
                            //退出登录
                            loginOut(context);
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        private void bookManage(final Context context) {
            Intent intent = new Intent(context, ListBookActivity.class);
            context.startActivity(intent);
        }

        private void findUserInfo(Context context) {
//            Intent intent = new Intent(context, PayTestActivity.class);
//            context.startActivity(intent);
        }

        private void updateIcon(Context context) {
            Intent intent = new Intent(context, ChooseIconActivity.class);
            context.startActivity(intent);
        }


        private void loginOut(final Context context) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("退出登录");
            dialog.setMessage("确认要退出登录吗?");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    editor.putString("name", null);
                    editor.putString("pwd", null);
                    editor.putInt("userId", 0);
                    editor.putInt("LOG_STATUS", 0);
                    editor.apply();
                    context.startActivity(new Intent(context, NavigationActivity.class));
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
    }

    public UserInfoSettingAdapter(){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recy_user, parent, false);
        return new UserInfoSettingAdapter.ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(setting[position]);
    }

    @Override
    public int getItemCount() {
        return setting.length;
    }

}
