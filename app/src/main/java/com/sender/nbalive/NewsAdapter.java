package com.sender.nbalive;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by XieShengda on 2017/3/13.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<List<NewsBean>> lists;
    private int listNum;
//    private ViewHolder viewHolder;
//    private LruCache<String, Bitmap> mCache;
    private ViewGroup parent;
    private View view;


    public NewsAdapter(List<List<NewsBean>> lists, int listNum) {

        this.lists = lists;
        this.listNum = listNum;
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        int cacheSize = maxMemory / 4;
//        mCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getByteCount();
//            }
//        };

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsBean newsBean = lists.get(listNum).get(position);
        holder.textView.setText(newsBean.getTitle());
//        viewHolder = holder;
        String imgUrl = newsBean.getImgUrl();
//        viewHolder.imageView.setTag(imgUrl);
//        if (mCache.get(imgUrl) == null) {
//            ImageAsyncTask imageAsyncTask = new ImageAsyncTask(imgUrl);
//            imageAsyncTask.execute(imgUrl);
            Glide.with(parent.getContext())
            .load(imgUrl)
            .into(holder.imageView);
//        } else {
//            viewHolder.imageView.setImageBitmap(mCache.get(imgUrl));
//        }

    }

    @Override
    public int getItemCount() {

        return lists.get(listNum).size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.news_img);
            textView = (TextView) itemView.findViewById(R.id.news_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String targetUrl = lists.get(listNum).get(getPosition()).getNewsUrl();
            Intent intent = new Intent(parent.getContext(), WebActivity.class);
            intent.putExtra("target_url",targetUrl);
            parent.getContext().startActivity(intent);

        }
    }

//    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
//        String imgUrl;
//
//        public ImageAsyncTask(String imgUrl) {
//            this.imgUrl = imgUrl;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            viewHolder.imageView.setImageResource(R.drawable.nba_icon);
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            Bitmap bitmap = null;
//            InputStream is = null;
//            try {
//                URL url = new URL(strings[0]);
//                is = url.openStream();
//                bitmap = BitmapFactory.decodeStream(is);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (is != null) {
//                    try {
//                        is.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//            if (bitmap != null) {
//                mCache.put(strings[0], bitmap);
//            }
//            Glide.with();
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            ImageView imageView = viewHolder.imageView;
////            if (imageView.getTag() == imgUrl && bitmap != null) {
//            if (bitmap != null) {
//                imageView.setImageBitmap(bitmap);
//            }
////            }
//
//        }
//    }
}
