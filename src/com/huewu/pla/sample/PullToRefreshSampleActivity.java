package com.huewu.pla.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Helper;
import com.dodowaterfall.widget.ScaleImageView;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;

public class PullToRefreshSampleActivity extends FragmentActivity implements IXListViewListener {
    private ImageFetcher mImageFetcher;
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    private int currentPage = 0;
    ContentTask task = new ContentTask(this, 2);

    private class ContentTask extends AsyncTask<String, Integer, List<DuitangInfo>> {

        private Context mContext;
        private int mType = 1;

        public ContentTask(Context context, int type) {
            super();
            mContext = context;
            mType = type;
        }

        @Override
        protected List<DuitangInfo> doInBackground(String... params) {
            try {
                return parseNewsJSON(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DuitangInfo> result) {
            if (mType == 1) {
                mAdapter.addItemTop(result);
                mAdapter.notifyDataSetChanged();
                mAdapterView.stopRefresh();

            } else if (mType == 2) {
                mAdapterView.stopLoadMore();
                mAdapter.addItemLast(result);
                mAdapter.notifyDataSetChanged();
            }else if(mType == 3){
            	  mAdapterView.stopRefresh();
            	  mAdapterView.stopLoadMore();
            	  mAdapter.notifyDataSetChanged();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        public List<DuitangInfo> parseNewsJSON(String url) throws IOException {
            List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
            String json = "";
            if (Helper.checkConnection(mContext)) {
                try {
                    json = Helper.getStringFromUrl(url);

                } catch (IOException e) {
                    Log.e("IOException is : ", e.toString());
                    e.printStackTrace();
                    return duitangs;
                }
            }
            Log.d("MainActiivty", "json:" + json);

            try {
                if (null != json) {
                    JSONObject newsObject = new JSONObject(json);
                	JSONArray json1 = newsObject.getJSONArray("dishsets");
					JSONArray blogsJson = json1.getJSONObject(0).getJSONArray("dishsetdetail");
                    for (int i = 0; i < blogsJson.length(); i++) {
               JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
               DuitangInfo newsInfo1 = new DuitangInfo();
               newsInfo1.setAlbid(newsInfoLeftObject.isNull("dishid") ? "": newsInfoLeftObject.getString("dishid"));
			   newsInfo1.setIsrc(newsInfoLeftObject.isNull("dishpicurl") ? "": "http://110.84.129.130:8080/Yuf"+newsInfoLeftObject.getString("dishpicurl"));
			   newsInfo1.setMsg(newsInfoLeftObject.isNull("dishname") ? "": newsInfoLeftObject.getString("dishname"));
			   newsInfo1.setHeight(150);
               duitangs.add(newsInfo1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return duitangs;
        }
    }

    /**
     * 添加内容
     * 
     * @param pageindex
     * @param type
     *            1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int pageindex, int type) {
    	if(currentPage==0){
    		 if (task.getStatus() != Status.RUNNING) {
//               String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
           	String url = "http://110.84.129.130:8080/Yuf/dishset/getRecommendedDishset";
               Log.d("MainActivity", "current url:" + url);
               ContentTask task = new ContentTask(this, type);
               task.execute(url);
           }	
    	}else{
    		 if (task.getStatus() != Status.RUNNING) {
//               String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
           	String url = "http://110.84.129.130:8080/Yuf/dishset/getRecommendedDishset";
               Log.d("MainActivity", "current url:" + url);
               ContentTask task = new ContentTask(this, 3);
               task.execute(url);
           }	
    	}
       
    }

    public class StaggeredAdapter extends BaseAdapter {
        private Context mContext;
        private LinkedList<DuitangInfo> mInfos;
        private XListView mListView;

        public StaggeredAdapter(Context context, XListView xListView) {
            this.mContext = context;
            this.mInfos = new LinkedList<DuitangInfo>();
            this.mListView = xListView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            DuitangInfo duitangInfo = mInfos.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.infos_list, null);
                holder = new ViewHolder();
                holder.tab0_imageView = (ScaleImageView) convertView.findViewById(R.id.tab0_news_pic);
                holder.tab0_nameOfDish = (TextView) convertView.findViewById(R.id.tab0_news_title);
               // holder.tab0_collection_button = (Button)convertView.findViewById(R.id.tab0_collection_button);
              //  holder.tab0_comment_button = (Button)convertView.findViewById(R.id.tab0_comment_button);
                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
            holder.tab0_imageView.setImageWidth(duitangInfo.getWidth());
            holder.tab0_imageView.setImageHeight(duitangInfo.getHeight());
            holder.tab0_nameOfDish.setText(duitangInfo.getMsg());
            mImageFetcher.loadImage(duitangInfo.getIsrc(), holder.tab0_imageView);
            return convertView;
        }

        class ViewHolder {
            ScaleImageView tab0_imageView;//图片
            TextView tab0_nameOfDish;//菜名
            Button tab0_collection_button;//收藏
            Button tab0_comment_button;//评论
        }

        @Override
        public int getCount() {
            return mInfos.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mInfos.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        public void addItemLast(List<DuitangInfo> datas) {
            mInfos.addAll(datas);
        }

        public void addItemTop(List<DuitangInfo> datas) {
            for (DuitangInfo info : datas) {
                mInfos.addFirst(info);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pull_to_refresh_sample);
        mAdapterView = (XListView) findViewById(R.id.list);
        mAdapterView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(PLA_AdapterView<?> parent, View view,
        			int position, long id) {
        		Toast.makeText(PullToRefreshSampleActivity.this, "cdc", Toast.LENGTH_SHORT).show();
        	}
        });
        mAdapterView.setPullLoadEnable(true);
        mAdapterView.setXListViewListener(this);

        mAdapter = new StaggeredAdapter(this, mAdapterView);

        mImageFetcher = new ImageFetcher(this, 240);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapterView.setAdapter(mAdapter);
        AddItemToContainer(currentPage, 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRefresh() {
        AddItemToContainer(++currentPage, 1);

    }

    @Override
    public void onLoadMore() {
        AddItemToContainer(++currentPage, 2);

    }
}// end of class