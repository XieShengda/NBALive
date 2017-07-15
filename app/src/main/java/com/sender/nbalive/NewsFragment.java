package com.sender.nbalive;


import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private int listNum;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listNum = (int) getArguments().get("list_number");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.news_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        new DataAsyncTask().execute();
        return view;
    }

    private class DataAsyncTask extends AsyncTask<Void, Void, List<List<NewsBean>>>{


        @Override
        protected void onPostExecute(List<List<NewsBean>> lists) {
            super.onPostExecute(lists);
            NewsAdapter adapter = new NewsAdapter(lists, listNum);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new MyItemDecoration(4));
//            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
//            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//                @Override
//                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                    super.onDraw(c, parent, state);
//                    Paint paint = new Paint();
//                    paint.setColor(getResources().getColor(R.color.colorAccent));
//                    paint.set
//
//                }
//            });
        }

        @Override
        protected List<List<NewsBean>> doInBackground(Void... voids) {
            return Reptile.getNews();
        }
    }

    public static NewsFragment getInstance(int listNum) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("list_number", listNum);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration{
        private int offset;

        public MyItemDecoration(int offset) {
            this.offset = offset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            Context context = getContext();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            int density = (int) metrics.density;

            int itemMargin = density * offset;
            outRect.set(itemMargin, itemMargin, itemMargin, itemMargin);
        }
    }
}
