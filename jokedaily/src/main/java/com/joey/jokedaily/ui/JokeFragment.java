package com.joey.jokedaily.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.joey.jokedaily.Constant;
import com.joey.jokedaily.R;
import com.joey.jokedaily.listener.RecyclerItemClickListener;
import com.joey.jokedaily.bean.StrJoke;
import com.joey.jokedaily.utils.IOUtils;
import com.joey.jokedaily.utils.L;
import com.joey.jokedaily.utils.PrefUtils;
import com.joey.jokedaily.utils.T;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joey on 2016/5/2.
 */
public class JokeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //TAG
    private final String TAG = getClass().getSimpleName();

    //当前状态 上拉 下拉 空闲
    private int mState = -1;
    //状态    下拉--------------->刷新
    private static final int STATE_PULL_DOWN = 1;
    //状态    上拉--------------->加载更多
    private static final int STATE_PULL_UP = 2;
    //状态    空闲
    private static final int STATE_IDLE = 3;

    //网络状态  空闲  忙碌
    private int mCurrNetState = NET_IDLE;
    private static final int NET_IDLE = 1;
    private static final int NET_BUSY = 2;
    private static final int NET_ERROR = 3;

    private int mFirstItemPosition;

    private int mCurrPage;

    private boolean isFirstLoad = true;

    private int mCount;//统计次数

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private MyAdapter mAdapter;
    private RequestQueue mQueue;
    private SwipeRefreshLayout mRefreshLayout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            L.e(TAG + "\thandleMessage\tmsg.what=" + msg.what + "\tmsg.arg1=" + msg.arg1);
            if (msg.what == STATE_PULL_UP || msg.what == STATE_PULL_DOWN) {
                refreshView(msg.what, (String) msg.obj, msg.arg1);
                setCurrPage(msg.arg1);
            }
            if (msg.what == NET_ERROR) {
                setState(STATE_IDLE);
            }

        }
    };

    private void refreshView(int state, String result, int page) {
        L.v(TAG + "\trefreshStrJson\treceive result=" + result.substring(0, 20));
        switch (state) {
            case STATE_PULL_UP://上拉加载更多
                break;
            case STATE_PULL_DOWN://下拉 刷新
                List<StrJoke> jokes = JSON.parseArray(result, StrJoke.class);
                if (jokes != null) {
                    List<StrJoke> strJokes = filterList(jokes);
                    mAdapter.updateTopItems(strJokes);

                    if (mCount == 1 && page == mCurrPage) {
                        mRecyclerView.scrollToPosition(mFirstItemPosition);//第一次加载
                    } else {
                        mRecyclerView.scrollToPosition(strJokes.size() + 1);
                    }
                }
                break;
        }
        setState(STATE_IDLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, container, false);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mQueue = NoHttp.newRequestQueue();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mFirstItemPosition = layoutManager.findFirstVisibleItemPosition();
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        /**
         * 1.读缓存
         * 2.若无。读网络
         * 缓存标识符 page
         */
        mAdapter = new MyAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mCount = 0;
        mCurrPage = PrefUtils.getInt(Constant.KEY_NEWS_PAGE, 1);
        mFirstItemPosition = PrefUtils.getInt(Constant.KEY_NEWS_PAGE_SELECTED, 0);
        initData();
    }

    private void initData() {
        int page = mCurrPage;

        for (int i=1;i<page+1;i++){
            setState(STATE_PULL_DOWN);
            load(i);
        }

    }

    private void load(int page) {
        L.e(TAG + "\tload(int " + page + ")");
        if (page >= mCurrPage) {
            mCount++;
        }

        if (page == mCurrPage && mCount != 1) {//重复加载。。同一个页面
            setState(STATE_IDLE);
            L.e(TAG + "load the same page=" + page);
            return;
        }
        if (getState() != STATE_PULL_DOWN && getState() != STATE_PULL_UP) {//状态有问题
            L.e(TAG + "\tload state error\tstate=" + getState());
            return;
        }


        //根据是否由缓存判断，页面初始化加载方式
        if (isFileExists(Constant.getTextCachePath(page))) {
            //存在缓存，读缓存
            String strJson = readStrJson(Constant.getTextCachePath(page));
            L.e(TAG + "\tload()" + "\tgetTextCachePath\tresult.length=" + strJson.length());
            Message message = handler.obtainMessage();
            message.what = getState();
            message.obj = strJson;
            message.arg1 = page;
            handler.sendMessage(message);
        } else {
            //不存在缓存，读网络数据
            loadTextJson(page);
        }
        L.e(" load(int page)  over>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private void loadTextJson(final int page) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(Constant.JSON_URL);
        request.add(Constant.KEY_URL_ARG1, Constant.URL_ARG1);
        request.add(Constant.KEY_URL_ARG2, Constant.URL_ARG2);//每页的大小，默认为10；
        request.add(Constant.KEY_URL_ARG3, page);
        mQueue.add(0, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                setCurrNetState(NET_BUSY);
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                JSONObject jsonObject = response.get();
                int error_code = jsonObject.optInt("error_code", -1);
                if (error_code != 0) {
                    L.e(TAG + "\tloadTextJson.onSucceed" + "\tresult.error_code=" + error_code);
                    L.e(TAG + "\tloadTextJson.onSucceed" + "\turl=" + response.url());
                }

                String result = jsonObject.optString("result");
                if (result != null) {
                    writeStrJson(result, Constant.getTextCachePath(page));//保存到缓存
                    L.e(TAG + "\tloadTextJson.onSucceed" + "\tresult.length=" + result.length());
                    Message message = handler.obtainMessage();
                    message.what = getState();
                    message.obj = result;
                    message.arg1 = page;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                L.e(TAG + "\tmQueue.add().OnResponseListener().onFailed()" + "\twhat=" + what);
                L.e(TAG + "\tmQueue.add().OnResponseListener().onFailed()" + "\turl=" + url);
                L.e(TAG + "\tmQueue.add().OnResponseListener().onFailed()" + "\ttag=" + tag);
                L.e(TAG + "\tmQueue.add().OnResponseListener().onFailed()" + "\texception=" + exception.toString());
                L.e(TAG + "\tmQueue.add().OnResponseListener().onFailed()" + "\tresponseCode=" + responseCode);
                L.e(TAG + "\tmQueue.add().OnResponseListener().onFailed()" + "\tnetworkMillis=" + networkMillis);
                setCurrNetState(NET_IDLE);
                setState(STATE_IDLE);
                handler.sendEmptyMessage(NET_ERROR);
                T.showShort("网络连接异常");
            }

            @Override
            public void onFinish(int what) {
                setCurrNetState(NET_IDLE);
            }
        });


    }


    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;
        if (state == STATE_IDLE) {
            progressBar.setVisibility(View.INVISIBLE);
            mRefreshLayout.setRefreshing(false);
            L.e("mRefreshLayout.setRefreshing(false)");
        } else {
            if (mCount == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public int getCurrNetState() {
        return mCurrNetState;
    }

    public void setCurrNetState(int currNetState) {
        this.mCurrNetState = currNetState;
    }

    public int getCurrPage() {
        return mCurrPage;
    }

    public void setCurrPage(int currPage) {
        if (currPage > mCurrPage) {
            this.mCurrPage = currPage;
            PrefUtils.putInt(Constant.KEY_NEWS_PAGE, currPage);
        }
        L.e(TAG + "\tsetCurrPage(int " + currPage + ")");
    }

    //判断文件是否存在
    private boolean isFileExists(String filePath) {
        L.e(TAG + "\tisFileExists()\tfilePath="+filePath);
        File file = new File(filePath);
        return file.exists();
    }

    //strJson--->File
    private void writeStrJson(String strJson, String filePath) {
        L.e(TAG + "\twriteStrJson()");
        try {
            IOUtils.write(strJson, filePath);
            L.e(TAG + "\twriteStrJson()\tfilePath="+filePath);
        } catch (IOException e) {
            L.e(TAG + "\twriteStrJson()\tfilePath="+filePath+"\te.printStackTrace"+e.toString());
            e.printStackTrace();
        }
    }

    //File--->strJson
    private String readStrJson(String filePath) {
        L.e(TAG + "\treadStrJson()\tfilePath="+filePath);
        try {
            String strJson = IOUtils.read(filePath);
            return strJson;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //        public MyAdapter(Context context) {
//            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//            mBackground = mTypedValue.resourceId;
//        }
//
//        public void onBindMyViewHolder(ViewHolder holder, int position) {
//            runEnterAnimation(holder.itemView, position);
//        }
//
//
//        private void runEnterAnimation(View view, int position) {
//            if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
//                return;
//            }
//
//            if (position > lastAnimatedPosition) {
//                lastAnimatedPosition = position;
//                view.setTranslationY(Utils.getScreenHeight(getActivity()));
//                view.animate()
//                        .translationY(0)
//                        .setStartDelay(100 * position)
//                        .setInterpolator(new DecelerateInterpolator(3.f))
//                        .setDuration(700)
//                        .start();
//            }
//        }
//
//            animateItems = animated;
//            lastAnimatedPosition = -1;
//            notifyDataSetChanged();
//        }


    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
//            Book book = mAdapter.getBook(position);
//            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
//            intent.putExtra("book", book);
//
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                            view.findViewById(R.id.ivBook), getString(R.string.transition_book_img));
//
//            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

        }
    };

    //下拉刷新
    @Override
    public void onRefresh() {
        L.e("onRefresh()");
        if (mCurrPage > 100) {
            T.showShort("无更新数据");
        } else if (mCurrNetState == NET_IDLE) {
            setState(STATE_PULL_DOWN);
            load(mCurrPage + 1);
        }
    }


    private class MyAdapter extends RecyclerView.Adapter {
        private List<StrJoke> mJokeList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public TextView tvContent;
            public TextView tvCaption;
            public int position;

            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
                tvCaption = (TextView) itemView.findViewById(R.id.tv_caption);
            }
        }

        public MyAdapter(Context context) {
            mJokeList = new ArrayList<StrJoke>();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_joke, null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            StrJoke joke = mJokeList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tvTitle.setText(joke.getTitle());
            viewHolder.tvContent.setText(joke.getContent());
            viewHolder.tvCaption.setText(joke.getType() + "\t\t更新时间：" + joke.getUpdatetime());
        }

        @Override
        public int getItemCount() {
            return mJokeList.size();

        }

        public void updateTopItems(List<StrJoke> jokeList) {
            if (jokeList != null) {
                addList(mJokeList, jokeList, 0);
                notifyDataSetChanged();
            }
        }

        public void updateBottomItems(List<StrJoke> jokeList) {
            if (jokeList != null) {
                mJokeList.addAll(jokeList);
                notifyDataSetChanged();
            }
        }

        private void addList(List<StrJoke> mJokes, List<StrJoke> jokes, int start) {
            List<StrJoke> isRemoveRepeat = new ArrayList<StrJoke>();
            for (StrJoke joke : jokes) {
                if (!mJokes.contains(joke)) {
                    isRemoveRepeat.add(joke);
                }
            }
            mJokes.addAll(start, isRemoveRepeat);
        }

        private void addList(List<StrJoke> mJokes, List<StrJoke> jokes) {
            List<StrJoke> isRemoveRepeat = new ArrayList<StrJoke>();
            for (StrJoke joke : jokes) {
                if (!mJokes.contains(joke)) {
                    isRemoveRepeat.add(joke);
                }
            }
            mJokes.addAll(isRemoveRepeat);
        }

        public void clearItems() {
            mJokeList.clear();
            notifyDataSetChanged();
        }
    }

    private List<StrJoke> filterList(List<StrJoke> jokes) {
        List<StrJoke> strJokes = new ArrayList<StrJoke>();
        for (StrJoke joke : jokes) {
            if (joke.getTitle() == null || joke.getTitle().toString().length() < 5 || joke.getTitle().toString().contains("&")) {
                continue;
            }
            if (joke.getContent() == null || joke.getContent().toString().length() < 5) {
                continue;
            }
            strJokes.add(joke);
        }
        return strJokes;
    }

    @Override
    public void onStop() {
        super.onStop();
        PrefUtils.putInt(Constant.KEY_NEWS_PAGE_SELECTED, mFirstItemPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
