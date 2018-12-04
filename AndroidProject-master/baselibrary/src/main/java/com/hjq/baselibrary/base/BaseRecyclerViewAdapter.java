package com.hjq.baselibrary.base;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.baselibrary.listener.OnItemClickListener;
import com.hjq.baselibrary.listener.OnItemLongClickListener;
import com.hjq.baselibrary.listener.OnScrollingListener;

import java.util.ArrayList;
import java.util.List;

/**
 *    author : HJQ
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : RecyclerView适配器基类
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends BaseRecyclerViewAdapter.ViewHolder>
                        extends RecyclerView.Adapter<VH> {
    //列表数据
    private List<T> mDataSet;
    //当前列表的页码，默认为第一页，用于分页加载功能
    private int mPageNumber = 1;
    //是否是最后一页，默认为false，用于分页加载功能
    private boolean mLastPage;

    //RecyclerView对象
    private RecyclerView mRecyclerView;
    //上下文对象，注意不要在构造函数中使用
    private Context mContext;

    //标记对象
    private Object mTag;

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置新的数据
     */
    public void setData(List<T> data) {
        mDataSet = data;
        notifyDataSetChanged();
    }

    /**
     * 获取当前数据
     */
    public List<T> getData() {
        return mDataSet;
    }

    /**
     * 追加一些数据
     */
    public void addData(List<T> data) {
        //追加的数据不能为空
        if (data == null || data.size() == 0) return;

        if (mDataSet == null || mDataSet.size() == 0) {
            setData(data);
        }else {
            mDataSet.addAll(data);
            notifyItemRangeInserted(mDataSet.size() - data.size(), data.size());
        }
    }

    /**
     * 清空当前数据
     */
    public void clearData() {
        //当前的数据不能为空
        if (mDataSet == null || mDataSet.size() == 0) return;

        mDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取某个位置上的数据
     */
    public T getItem(int position) {
        return mDataSet.get(position);
    }

    /**
     * 更新某个位置上的数据
     */
    public void setItem(int position, T item) {
        if (mDataSet == null) mDataSet = new ArrayList<>();
        mDataSet.set(position, item);
        notifyItemChanged(position);
    }

    /**
     * 添加单条数据
     */
    public void addItem(T item) {
        addItem(mDataSet.size() - 1, item);
    }

    public void addItem(int position, T item) {
        if (mDataSet == null) mDataSet = new ArrayList<>();

        //如果是在for循环添加后要记得position++
        if (position < mDataSet.size()) {
            mDataSet.add(position, item);
        }else {
            mDataSet.add(item);
            position = mDataSet.size() - 1;
        }
        //告诉适配器添加数据的位置，会有动画效果
        notifyItemInserted(position);
    }

    /**
     * 删除单条数据
     */
    public void removeItem(T item) {
        int index = mDataSet.indexOf(item);
        if (index != -1) {
            removeItem(index);
        }
    }

    public void removeItem(int position) {
        //如果是在for循环删除后要记得i--
        mDataSet.remove(position);
        //告诉适配器删除数据的位置，会有动画效果
        notifyItemRemoved(position);
    }

    /**
     * 获取RecyclerView对象，需要在setAdapter之后绑定
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 获取上下文对象，注意不要在构造方法中调用
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 如果非要在构造方法中使用上下文对象，可以提前设置，否则只能setAdapter之后才能获取
     */
    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 获取当前的页码
     */
    public int getPageNumber() {
        return mPageNumber;
    }

    /**
     * 设置当前的页码
     */
    public void setPageNumber(int pageNumber) {
        mPageNumber = pageNumber;
    }

    /**
     * 当前是否为最后一页
     */
    public boolean isLastPage() {
        return mLastPage;
    }

    /**
     * 设置是否为最后一页
     */
    public void setLastPage(boolean lastPage) {
        mLastPage = lastPage;
    }

    /**
     * 获取标记
     */
    public Object getTag() {
        return mTag;
    }

    /**
     * 设置标记
     */
    public void setTag(Object tag) {
        mTag = tag;
    }

    /**
     * 条目ViewHolder，需要子类ViewHolder继承
     */
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        public ViewHolder(ViewGroup parent, int layoutId) {
            this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }

        public ViewHolder(View itemView) {
            super(itemView);
            //这里可以设置条目的监听事件
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null) {
                return onItemLongClickListener.onItemLongClick(v, getLayoutPosition());
            }
            return false;
        }

        public final <T extends View> T findView(@IdRes int id) {
            return (T) itemView.findViewById(id);
        }

        public final ViewHolder setText(@IdRes int id, String text) {
            if (text == null) text = "";
            View view = findView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        public final ViewHolder setVisibility(@IdRes int id, int visibility) {
            View view = findView(id);
            if (view != null) {
                view.setVisibility(visibility);
            }
            return this;
        }

        public final ViewHolder setColor(@IdRes int id, @ColorInt int color) {
            View view = findView(id);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            }
            return this;
        }

        public final ViewHolder setImage(@IdRes int id, int resID) {
            View view = findView(id);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(resID);
            }
            return this;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mContext == null) {
            mContext = recyclerView.getContext();
        }
        //用户设置了滚动监听，需要给RecyclerView设置监听
        if (mScrollListener != null) {
            //添加滚动监听
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
        //判断当前的布局管理器是否为空，如果为空则设置默认的布局管理器
        if (mRecyclerView.getLayoutManager() == null) {
            RecyclerView.LayoutManager manager = getDefaultLayoutManager(mContext);
            if (manager != null) {
                mRecyclerView.setLayoutManager(manager);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        //移除滚动监听
        if (mScrollListener != null) {
            mRecyclerView.removeOnScrollListener(mScrollListener);
        }
        mRecyclerView = null;
        mContext = null;
    }

    /**
     * 获取默认的布局摆放器
     */
    public RecyclerView.LayoutManager getDefaultLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    /**
     * 设置RecyclerView条目点击监听
     */
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }

    /**
     * 设置RecyclerView条目长按监听
     */
    private OnItemLongClickListener onItemLongClickListener;
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        onItemLongClickListener = l;
    }

    /**
     * 设置RecyclerView条目滚动监听
     */
    private OnScrollingListener onScrollingListener;
    public void setOnScrollingListener(OnScrollingListener l) {
        onScrollingListener = l;

        //如果当前已经有设置滚动监听，再次设置需要移除原有的监听器
        if (mScrollListener == null) {
            mScrollListener = new ScrollListener();
        }else {
            mRecyclerView.removeOnScrollListener(mScrollListener);
        }
        //用户设置了滚动监听，需要给RecyclerView设置监听
        if (mRecyclerView != null) {
            //添加滚动监听
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
    }

    //自定义滚动监听器
    private ScrollListener mScrollListener;

    private class ScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            if (onScrollingListener == null) return;

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                if (!recyclerView.canScrollVertically(1)) {
                    //是否能向下滚动，false表示已经滚动到底部
                    onScrollingListener.onScrollDown(recyclerView);
                }else if (!recyclerView.canScrollVertically(-1)) {
                    //是否能向上滚动，false表示已经滚动到顶部
                    onScrollingListener.onScrollTop(recyclerView);
                }

            }else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                //正在滚动中
                onScrollingListener.onScrolling(recyclerView);
            }
        }
    }
}
