package com.zlylib.slidetogglelib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;


/**
 * @author zhangliyang
 * @date 2021/3/18.
 * des：滑动开关
 */

public class SlideToggleView extends FrameLayout {

    private final static String TAG= "SlideToggleView";

    private int mWidth=0,mHeight;

    private ViewDragHelper mViewDragHelper;

    //滑块
    private ImageView mBlockView;

    //滑块外边距
    private int mBlockLeftMargin;
    private int mBlockRightMargin;
    private int mBlockTopMargin;
    private int mBlockBottomMargin;

    //触发打开事件允许剩余的距离
    private int mRemainDistance;
    //滑动总长度
    private int slideTotal;

    //文字
    private ShimmerTextView mShimmerTextView;

    //从左滑动开始还是从右滑动开始
    /**
    *  1 左边开始滑动 滑动到右边算开锁
     * 2 右边开始滑动 滑动到左边算开锁
    * */
    private int leftOrRightStart = 1;

    //打开所显示的文字，关闭所显示的文字
    String openText,closeText;

    private SlideToggleListener mSlideToggleListener;


    public SlideToggleView(@NonNull Context context) {
        super(context);
        init(context,null,0);
    }

    public SlideToggleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public SlideToggleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs, defStyleAttr);
    }

    private void init(Context context,AttributeSet attrs,int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideToggleView, defStyleAttr, 0);
        openText = typedArray.getString(R.styleable.SlideToggleView_stv_openText);
        closeText = typedArray.getString(R.styleable.SlideToggleView_stv_closeText);
        int textColor = typedArray.getColor(R.styleable.SlideToggleView_stv_textColor, 0xffffffff);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.SlideToggleView_stv_textSize, DisplayUtils.dp2px(context, 14));
        Drawable slideBlock = typedArray.getDrawable(R.styleable.SlideToggleView_stv_slideBlock);
        mBlockLeftMargin = typedArray.getDimensionPixelSize(R.styleable.SlideToggleView_stv_blockLeftMargin, 1);
        mBlockRightMargin = typedArray.getDimensionPixelSize( R.styleable.SlideToggleView_stv_blockRightMargin, 1);
        mBlockTopMargin = typedArray.getDimensionPixelSize(R.styleable.SlideToggleView_stv_blockTopMargin, 1);
        mBlockBottomMargin = typedArray.getDimensionPixelSize( R.styleable.SlideToggleView_stv_blockBottomMargin, 1);
        mRemainDistance = typedArray.getDimensionPixelSize(R.styleable.SlideToggleView_stv_remain, 10);
        int stv_slideBlock_width = typedArray.getDimensionPixelSize(R.styleable.SlideToggleView_stv_slideBlockWidth, DisplayUtils.dp2px(context, 50));
        leftOrRightStart = typedArray.getInt(R.styleable.SlideToggleView_stv_leftOrRightStart,1);
        typedArray.recycle();

        Log.d(TAG,"mWidth=="+mWidth+"--mWidth=="+mHeight +"-slideTotal--"+slideTotal +"-leftOrRightStart--"+leftOrRightStart);

        mViewDragHelper = ViewDragHelper.create(this, 1.0f, mDragCallback);

        //文字
        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        mShimmerTextView = new ShimmerTextView(context);
        mShimmerTextView.setText(closeText);
        mShimmerTextView.setTextColor(textColor);
        mShimmerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        addView(mShimmerTextView, textParams);

        //滑块
        mBlockView = new ImageView(context);
        mBlockView.setScaleType(ImageView.ScaleType.FIT_XY);
        mBlockView.setImageDrawable(slideBlock);
        LayoutParams blockParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        blockParams.width = stv_slideBlock_width;
        blockParams.setMargins(0, mBlockTopMargin, mBlockRightMargin,mBlockBottomMargin);
        addView(mBlockView, blockParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mWidth==0){
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();

            //滑动总长度
            slideTotal =  mWidth - getPaddingLeft() - getPaddingRight() - mBlockLeftMargin - mBlockRightMargin - mBlockView.getMeasuredWidth();
            if(leftOrRightStart==2){
                mRemainDistance =slideTotal - mRemainDistance;
                openToggle();

            }
            Log.d(TAG,"mWidth=2="+mWidth+"--mWidth=="+mHeight +"-slideTotal--"+slideTotal +"-mRemainDistance--"+mRemainDistance);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //固定写法
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        } else {
            super.computeScroll();
        }
    }


    private ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(@NonNull View view, int pointerId) {
            return view == mBlockView;
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            ViewParent parent = capturedChild.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int min = getPaddingLeft() + mBlockLeftMargin;
            if (left < min) {
                left = min;
            }
            int max = getMeasuredWidth() - getPaddingRight() - mBlockRightMargin
                    - mBlockView.getMeasuredWidth();
            if (left > max) {
                left = max;
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            //  return getPaddingTop() + mBlockTopMargin;
            return super.clampViewPositionVertical(child, top, dy);//默认为0
            /*final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mBlockView.getHeight();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;*/
        }



        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx,
                                          int dy) {
            if (mSlideToggleListener != null) {
                int slide = left - getPaddingLeft() - mBlockLeftMargin;
                mSlideToggleListener.onBlockPositionChanged(SlideToggleView.this, left, slideTotal,
                        slide);
                 Log.d(TAG,"total=="+slideTotal+"---left--"+left+"---slide--"+slide);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int slide = releasedChild.getLeft() - getPaddingLeft() - mBlockLeftMargin;
            Log.d(TAG,"total=="+slideTotal+"---slide--"+slide+"---xvel--"+xvel+"---yvel--"+yvel );
            if(leftOrRightStart==2){
                if(xvel==0.0 && slide== slideTotal){
                    //防止点击滑块直接滑动到最左边
                    return;
                }
            }

            if (releasedChild == mBlockView) {
                if (slideTotal - slide <= mRemainDistance) {
                    Log.d(TAG,"打开");
                    if(leftOrRightStart==1){
                        mShimmerTextView.setText(openText);
                    }else if(leftOrRightStart==2){
                        mShimmerTextView.setText(closeText);
                    }
                    int finalLeft = getMeasuredWidth() - getPaddingRight() - mBlockRightMargin
                            - mBlockView.getMeasuredWidth();
                    int finalTop = getPaddingTop() + mBlockTopMargin;
                    Log.d(TAG,"打开"+finalLeft);
                   // mViewDragHelper.smoothSlideViewTo(mBlockView, finalLeft, finalTop);
                    //postInvalidate();

                    mViewDragHelper.smoothSlideViewTo(mBlockView, finalLeft, finalTop);
                    postInvalidate();
                    mShimmerTextView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //mViewDragHelper.smoothSlideViewTo(mBlockView, finalLeft, finalTop);
                            //postInvalidate();
                        }
                    },100);
                    if (mSlideToggleListener != null) {
                        mSlideToggleListener.onSlideListener(SlideToggleView.this,1);
                    }
                } else {
                    Log.d(TAG,"关闭");
                    if(leftOrRightStart==2){
                        mShimmerTextView.setText(openText);
                    }else if(leftOrRightStart==1){
                        mShimmerTextView.setText(closeText);
                    }
                    int finalLeft = getPaddingLeft() + mBlockLeftMargin;
                    int finalTop = getPaddingTop() + mBlockTopMargin;
                    mViewDragHelper.settleCapturedViewAt(finalLeft, finalTop);
                    invalidate();
                    if (mSlideToggleListener != null) {
                        mSlideToggleListener.onSlideListener(SlideToggleView.this,0);
                    }
                }
            }
        }
    };

    /**
     * 打开滑动开关
     */
    public void openToggle() {
        int finalLeft = getMeasuredWidth() - getPaddingRight() - mBlockRightMargin
                - mBlockView.getMeasuredWidth();
        int finalTop = getPaddingTop() + mBlockTopMargin;
        Log.d(TAG,"finalLeft=="+finalLeft);
        mViewDragHelper.smoothSlideViewTo(mBlockView, finalLeft, finalTop);
        invalidate();
    }

    /**
     * 设置滑动开关监听器
     *
     * @param listener 滑动开关监听器
     */
    public void setSlideToggleListener(SlideToggleListener listener) {
        this.mSlideToggleListener = listener;
    }

    public interface SlideToggleListener {
        /**
         * 滑块位置改变回调
         *
         * @param left  滑块左侧位置，值等于{@link #getLeft()}
         * @param total 滑块可以滑动的总距离
         * @param slide 滑块已经滑动的距离
         */
        void onBlockPositionChanged(SlideToggleView view, int left, int total, int slide);
        /**
         * 滑动打开
         *  @param leftOrRight  0 左边
         *  @param leftOrRight  1 右边
         */
        void onSlideListener(SlideToggleView view,int leftOrRight);
    }
}
