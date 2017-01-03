package com.sender.nbalive;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieShengda on 2016/12/28.
 */

public class DataLoader {
    //加载Json数据
    public void loadData(final String dataUrl, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                InputStream is = null;
                BufferedReader reader = null;
                try {
                    url = new URL(dataUrl);
                    is = url.openStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder data = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        data.append(line);
                    }

                    decodeJson(data, handler);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    //处理下载好的Json数据，转为Bean集合，通过Handler传给主线程
    private void decodeJson(StringBuilder data, Handler handler) throws JSONException {
        JSONObject jsonObject = new JSONObject(data.toString());
        if (jsonObject.getString("reason").equals("Succes")){
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray list = result.getJSONArray("list");
            List<GameBean> gameBeanList = new ArrayList<>();
            for (int i = 0; i < list.length(); i++){
                JSONObject listItem = list.getJSONObject(i);
                String title = listItem.getString("title");
                boolean addTitle = false;
                JSONArray tr = listItem.getJSONArray("tr");
                for (int j = 0; j < tr.length(); j++){
                    GameBean gameBean = new GameBean();
                    if (!addTitle){
                        gameBean.title = title;
                        addTitle = true;
                    }
                    JSONObject trItem = tr.getJSONObject(j);
                    gameBean.watchWayText = trItem.getString("link1text");
                    gameBean.watchWayUrl = trItem.getString("link1url");
                    gameBean.techUrl = trItem.getString("link2url");
                    gameBean.team1Name = trItem.getString("player1");
                    gameBean.team2Name = trItem.getString("player2");
                    gameBean.score = trItem.getString("score");
                    gameBean.state = trItem.getString("status");
                    gameBean.time = trItem.getString("time");
                    gameBeanList.add(gameBean);
                }

//                将处理成list的Json数据返回给主线程
                Message message = Message.obtain();
                message.obj = gameBeanList;
                handler.sendMessage(message);




            }



        }
    }

}
