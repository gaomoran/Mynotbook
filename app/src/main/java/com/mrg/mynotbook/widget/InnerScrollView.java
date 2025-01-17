package com.mrg.mynotbook.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.mrg.mynotbook.utils.Utils;


public class InnerScrollView extends ScrollView {
    /**
     */
    private int moveMinValue = 20;
    public RelativeLayout parentView;

    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private int lastScrollDelta = 0;

    public void resume() {
        overScrollBy(0, -lastScrollDelta, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
        lastScrollDelta = 0;
    }

    int mTop = 10;

    /**
     * 将targetView滚到最顶端
     */
    public void scrollTo(View targetView) {
        int oldScrollY = getScrollY();
        int top = targetView.getTop() - mTop;
        int delatY = top - oldScrollY;
        lastScrollDelta = delatY;
        overScrollBy(0, delatY, 0, getScrollY(), 0, getScrollRange(), 0, 0, true);
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getHeight() - (getHeight()));
        }
        return scrollRange;
    }

    int currentY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (parentView == null) {
            return super.onInterceptTouchEvent(ev);
        } else {
        	
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 将父scrollview的滚动事件拦截
                currentY = (int) ev.getY();
                setParentScrollAble(false);
//                Debuglog.i("innerScroll", "ACTION_DOWN");
                return super.onInterceptTouchEvent(ev);
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                // 把滚动事件恢复给父Scrollview
                setParentScrollAble(true);
//                Debuglog.i("innerScroll", "ACTION_UP");
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                float moveY = ev.getY();
                float dixY = moveY - currentY;
                if (Math.abs(dixY) > Utils.dp2Px(getContext(), moveMinValue)) {
//                	   Debuglog.i("innerScroll", "ACTION_MOVE");
                    return true;
                }
                //返回true把事件交给onTounch去处理
                //return true;
            }
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        View child = getChildAt(0);
        try {
            if (parentView != null) {
                if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                    int height = child.getMeasuredHeight();
                    height = height - getMeasuredHeight();

                    // System.out.println("height=" + height);
                    int scrollY = getScrollY();
                    // System.out.println("scrollY" + scrollY);
                    int y = (int) ev.getY();

                    // 手指向下滑动
                    if (currentY < y) {
                        if (scrollY <= 0) {
                            // 如果向下滑动到头，就把滚动交给父Scrollview
                            setParentScrollAble(true);
                            return false;
                        } else {
                            setParentScrollAble(false);

                        }
                    } else if (currentY > y) {
                        if (scrollY >= height) {
                            // 如果向上滑动到头，就把滚动交给父Scrollview
                            setParentScrollAble(true);
                            return false;
                        } else {
                            setParentScrollAble(false);

                        }

                    }
                    currentY = y;
                }
            }
          return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
        }
		return false;
    }

    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        parentView.requestDisallowInterceptTouchEvent(!flag);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	if(listener!=null){
    		listener.onScrollChanged(l, t, oldl, oldt);
    	}
        if(getScrollY() + getHeight() ==  computeVerticalScrollRange())
        {
            if (listener != null) {
                 int scrollY = getScrollY();
                View childView=getChildAt(0);
                if (scrollY==childView.getHeight()-getHeight()){
                    listener.scrollBottom();
                }
            }
        }
    }

    ScrollViewListener listener;
    public  void setScrollViewListener(ScrollViewListener listener){
        this.listener=listener;
    }
    public interface ScrollViewListener{
        public void scrollBottom();
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}