package com.sender.nbalive;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by XieShengda on 2016/12/24.
 */

public class ContentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DataLoader dataLoader;
    private Handler handler, handler1;
    private GameAdapter adapter;
    private View view;
    private final String DATA_URL = "http://api.avatardata.cn/Nba/NomalRace?key=4088c2836e424814b06ae1648c85b608";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_content, container, false);
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //接收dataLoader返回的list
                    List<GameBean> list = (List<GameBean>) msg.obj;
                    if (adapter == null) {
                        adapter = new GameAdapter(getActivity(),list, handler1);
                        recyclerView.setAdapter(adapter);
                        int todayPosition = adapter.getTodayPosition();
                        if (todayPosition != -1) {
                            recyclerView.scrollToPosition(todayPosition);
                        }
                    } else {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                }
            };
            handler1 = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    recyclerView.smoothScrollToPosition((Integer) msg.obj);
                }
            };
            recyclerView = (RecyclerView) view.findViewById(R.id.game_view);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorSecondaryText);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            dataLoader = new DataLoader();
            dataLoader.loadData(DATA_URL, handler);
        }
        return view;
    }

    @Override
    public void onRefresh() {
       dataLoader.loadData(DATA_URL, handler);
    }
}
