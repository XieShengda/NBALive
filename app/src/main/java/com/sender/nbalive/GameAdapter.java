package com.sender.nbalive;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by XieShengda on 2016/12/28.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder>{
    private static final String TAG = "GameAdapter";
    private Context context;
    private List<GameBean> list;
    private boolean isCurrent = true;
    private Handler mHandler;
    private int todayPosition = -1;
    

    public GameAdapter(Context context, List<GameBean> list, Handler handler) {
        this.context = context;
        this.list = list;
        mHandler = handler;
        for (int i = 0; i < list.size(); i++){
            GameBean bean = list.get(i);
            if (bean.title != null){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                Date date = new Date();
                String today = simpleDateFormat.format(date);
                String titleDate = bean.title.substring(0, 5);
                Log.d(TAG, "today:"+ today + ",titleDate:" + titleDate);
                if (titleDate.equals(today)){
                    todayPosition = i;
                }
            }
        }
    }

    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //给自定义ViewHolder传入参数item布局
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.game_item, parent, false));
    }


//    将数据适配到UI
    @Override
    public void onBindViewHolder(final GameAdapter.MyViewHolder holder, int position) {
        final GameBean gameBean = list.get(position);
        //如果获取了当日日期，则将日期的View显示出来，否则隐藏
        if (gameBean.title != null){
            TextView title = holder.title;
            title.setVisibility(View.VISIBLE);
            title.setText(gameBean.title);
        }else{
            holder.title.setVisibility(View.GONE);
        }
        //设置队伍名
        String team1Name, team2Name, state;
        team1Name = gameBean.team1Name;
        team2Name = gameBean.team2Name;
        holder.team1Name.setText(team1Name);
        holder.team2Name.setText(team2Name);
        //根据队伍名设置Logo图片
        setLogo(holder, team1Name, team2Name);
        //根据比赛状态显示不同的颜色和文字信息
        state = gameBean.state;
        String[] score = gameBean.score.split("-");
        switch (state){
            case GameBean.NOT_BEGIN:
                holder.state.setText("未开赛");
                holder.state.setBackground(context.getResources().getDrawable(R.drawable.state_bg_0));
                holder.team1Score.setText("00");
                holder.team2Score.setText("00");
                if (isCurrent){
                    Message message = mHandler.obtainMessage();
                    message.obj = position;
                    mHandler.sendMessage(message);
                    isCurrent = false;
                }
                break;
            case GameBean.IN_GAME:
                holder.state.setText("比赛中");
                holder.team1Score.setText(score[0]);
                holder.state.setBackground(context.getResources().getDrawable(R.drawable.state_bg_1));
                holder.team2Score.setText(score[1]);
                if (isCurrent){
                    Message message = mHandler.obtainMessage();
                    message.obj = position;
                    mHandler.sendMessage(message);
                    isCurrent = false;
                }
                break;
            case GameBean.GAME_OVER:
                holder.state.setText("已结束");
                holder.state.setBackground(context.getResources().getDrawable(R.drawable.state_bg_2));
                holder.team1Score.setText(score[0]);
                holder.team2Score.setText(score[1]);
        }

        holder.time.setText(gameBean.time);
        Button watchWay = holder.watchWay;
        watchWay.setText(gameBean.watchWayText);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = null;
                switch (view.getId()){
                    case R.id.item_view:
                        String wwu = gameBean.watchWayUrl;
                        if (!wwu.equals("")) {
                            uri = Uri.parse(wwu);
                            Log.d(TAG, uri.toString());
                            if (uri != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                context.startActivity(intent);
                            }
                        } else {
                            Toast.makeText(context, "暂无直播或集锦", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.game_tech:
                        String tu = gameBean.techUrl;
                        if (!tu.equals("")) {
                            Intent intent = new Intent(context, WebActivity.class);
                            intent.putExtra("target_url",tu);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "暂无技术统计", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }
        };
        holder.itemView.setOnClickListener(listener);
        holder.tech.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    自定义ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView team1Logo, team2Logo;
        TextView title, team1Name, team2Name, team1Score, team2Score, time, state;
        Button watchWay, tech;
        View itemView;
        public MyViewHolder(View view){
            super(view);
            itemView = view;
            team1Logo = (ImageView) view.findViewById(R.id.team1_logo);
            team2Logo = (ImageView) view.findViewById(R.id.team2_logo);
            title = (TextView) view.findViewById(R.id.title);
            team1Name = (TextView) view.findViewById(R.id.team1_name);
            team2Name = (TextView) view.findViewById(R.id.team2_name);
            team1Score = (TextView) view.findViewById(R.id.team1_score);
            team2Score = (TextView) view.findViewById(R.id.team2_score);
            time = (TextView) view.findViewById(R.id.game_time);
            state = (TextView) view.findViewById(R.id.game_state);
            watchWay = (Button) view.findViewById(R.id.watch_way);
            tech = (Button) view.findViewById(R.id.game_tech);


        }

    }
//根据API返回的队伍名为ImageView设置队伍Logo
    private void setLogo(MyViewHolder holder, String team1Name, String team2Name) {
        switch (team1Name){
            case "凯尔特人":
                holder.team1Logo.setImageResource(R.drawable.kaierteren);
                break;
            case "篮网":
                holder.team1Logo.setImageResource(R.drawable.lanwang);
                break;
            case "尼克斯":
                holder.team1Logo.setImageResource(R.drawable.nikesi);
                break;
            case "76人":
                holder.team1Logo.setImageResource(R.drawable.qishiliuren);
                break;
            case "猛龙":
                holder.team1Logo.setImageResource(R.drawable.menglong);
                break;
            case "公牛":
                holder.team1Logo.setImageResource(R.drawable.gongniu);
                break;
            case "骑士":
                holder.team1Logo.setImageResource(R.drawable.qishi);
                break;
            case "活塞":
                holder.team1Logo.setImageResource(R.drawable.huosai);
                break;
            case "步行者":
                holder.team1Logo.setImageResource(R.drawable.buxingzhe);
                break;
            case "雄鹿":
                holder.team1Logo.setImageResource(R.drawable.xionglu);
                break;
            case "老鹰":
                holder.team1Logo.setImageResource(R.drawable.laoying);
                break;
            case "黄蜂":
                holder.team1Logo.setImageResource(R.drawable.huangfeng);
                break;
            case "热火":
                holder.team1Logo.setImageResource(R.drawable.rehuo);
                break;
            case "魔术":
                holder.team1Logo.setImageResource(R.drawable.moshu);
                break;
            case "奇才":
                holder.team1Logo.setImageResource(R.drawable.qicai);
                break;
            case "小牛":
                holder.team1Logo.setImageResource(R.drawable.xiaoniu);
                break;
            case "火箭":
                holder.team1Logo.setImageResource(R.drawable.huojian);
                break;
            case "灰熊":
                holder.team1Logo.setImageResource(R.drawable.huixiong);
                break;
            case "鹈鹕":
                holder.team1Logo.setImageResource(R.drawable.tihu);
                break;
            case "马刺":
                holder.team1Logo.setImageResource(R.drawable.maci);
                break;
            case "掘金":
                holder.team1Logo.setImageResource(R.drawable.juejin);
                break;
            case "森林狼":
                holder.team1Logo.setImageResource(R.drawable.senglinglang);
                break;
            case "开拓者":
                holder.team1Logo.setImageResource(R.drawable.kaituozhe);
                break;
            case "雷霆":
                holder.team1Logo.setImageResource(R.drawable.leiting);
                break;
            case "爵士":
                holder.team1Logo.setImageResource(R.drawable.jueshi);
                break;
            case "勇士":
                holder.team1Logo.setImageResource(R.drawable.yongshi);
                break;
            case "快船":
                holder.team1Logo.setImageResource(R.drawable.kuaichuan);
                break;
            case "湖人":
                holder.team1Logo.setImageResource(R.drawable.huren);
                break;
            case "太阳":
                holder.team1Logo.setImageResource(R.drawable.taiyang);
                break;
            case "国王":
                holder.team1Logo.setImageResource(R.drawable.guowang);
                break;
            default:
                holder.team1Logo.setImageResource(R.drawable.nba_icon);


        }
        switch (team2Name){
            case "凯尔特人":
                holder.team2Logo.setImageResource(R.drawable.kaierteren);
                break;
            case "篮网":
                holder.team2Logo.setImageResource(R.drawable.lanwang);
                break;
            case "尼克斯":
                holder.team2Logo.setImageResource(R.drawable.nikesi);
                break;
            case "76人":
                holder.team2Logo.setImageResource(R.drawable.qishiliuren);
                break;
            case "猛龙":
                holder.team2Logo.setImageResource(R.drawable.menglong);
                break;
            case "公牛":
                holder.team2Logo.setImageResource(R.drawable.gongniu);
                break;
            case "骑士":
                holder.team2Logo.setImageResource(R.drawable.qishi);
                break;
            case "活塞":
                holder.team2Logo.setImageResource(R.drawable.huosai);
                break;
            case "步行者":
                holder.team2Logo.setImageResource(R.drawable.buxingzhe);
                break;
            case "雄鹿":
                holder.team2Logo.setImageResource(R.drawable.xionglu);
                break;
            case "老鹰":
                holder.team2Logo.setImageResource(R.drawable.laoying);
                break;
            case "黄蜂":
                holder.team2Logo.setImageResource(R.drawable.huangfeng);
                break;
            case "热火":
                holder.team2Logo.setImageResource(R.drawable.rehuo);
                break;
            case "魔术":
                holder.team2Logo.setImageResource(R.drawable.moshu);
                break;
            case "奇才":
                holder.team2Logo.setImageResource(R.drawable.qicai);
                break;
            case "小牛":
                holder.team2Logo.setImageResource(R.drawable.xiaoniu);
                break;
            case "火箭":
                holder.team2Logo.setImageResource(R.drawable.huojian);
                break;
            case "灰熊":
                holder.team2Logo.setImageResource(R.drawable.huixiong);
                break;
            case "鹈鹕":
                holder.team2Logo.setImageResource(R.drawable.tihu);
                break;
            case "马刺":
                holder.team2Logo.setImageResource(R.drawable.maci);
                break;
            case "掘金":
                holder.team2Logo.setImageResource(R.drawable.juejin);
                break;
            case "森林狼":
                holder.team2Logo.setImageResource(R.drawable.senglinglang);
                break;
            case "开拓者":
                holder.team2Logo.setImageResource(R.drawable.kaituozhe);
                break;
            case "雷霆":
                holder.team2Logo.setImageResource(R.drawable.leiting);
                break;
            case "爵士":
                holder.team2Logo.setImageResource(R.drawable.jueshi);
                break;
            case "勇士":
                holder.team2Logo.setImageResource(R.drawable.yongshi);
                break;
            case "快船":
                holder.team2Logo.setImageResource(R.drawable.kuaichuan);
                break;
            case "湖人":
                holder.team2Logo.setImageResource(R.drawable.huren);
                break;
            case "太阳":
                holder.team2Logo.setImageResource(R.drawable.taiyang);
                break;
            case "国王":
                holder.team2Logo.setImageResource(R.drawable.guowang);
                break;
            default:
                holder.team1Logo.setImageResource(R.drawable.nba_icon);

        }
    }

    public int getTodayPosition() {
        return todayPosition;
    }
}
