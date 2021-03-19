# SlideToggle


# 简介
 滑动解锁，滑动接听。仿IOS滑动关机。



# 运行截图
<img src="https://github.com/ZLYang110/UpperDialog/raw/master/screenshot/20210319_133742.jpg" width = "180" height = "300" alt="图片名称"/>


# 使用说明


```java
        //设置监听

        SlideToggleView slideToggleView = findViewById(R.id.slideToggleView);
        slideToggleView.setSlideToggleListener(new SlideToggleView.SlideToggleListener() {
            @Override
            public void onBlockPositionChanged(SlideToggleView view, int left, int total, int slide) {
             /**
                     * 滑块位置改变回调
                     *
                     * @param left  滑块左侧位置，值等于{@link #getLeft()}
                     * @param total 滑块可以滑动的总距离
                     * @param slide 滑块已经滑动的距离
                     */

            }
            @Override
            public void onSlideListener(SlideToggleView view, int leftOrRight) {
                   /**
                          * 滑动打开
                          *  @param leftOrRight  0 左边
                          *  @param leftOrRight  1 右边
                          */
            }
        });

```


```java

  <com.zlylib.slidetogglelib.SlideToggleView
        android:id="@+id/slideToggleView"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:background="@drawable/bg_silde_toggle"
        android:layout_marginTop="50dp"
        app:stv_blockBottomMargin="1dp"
        app:stv_blockLeftMargin="1dp"
        app:stv_blockRightMargin="1dp"
        app:stv_blockTopMargin="1dp"
        app:stv_remain="20dp"
        app:stv_slideBlock="@drawable/btn"
        app:stv_slideBlockWidth="70dp"
        app:stv_openText="Slide To Unlock"
        app:stv_closeText="Slide To lock"
        app:stv_leftOrRightStart="left"
        />

```

### 属性列表

---

名称 | 描述 |  默认值
---|---|---
stv_openText | 开锁显示的文字 | 无
stv_closeText | 关闭显示的文字 | 无
stv_textSize | 文字大小 |14
stv_textColor | 文字颜色 | 0xffffffff
stv_slideBlock | 滑动图片 | 无
stv_slideBlockWidth | 滑动模块宽度  | 50
stv_blockLeftMargin | 滑块外左边距  | 1
stv_blockRightMargin | 滑块外右边距  | 1
stv_blockTopMargin | 滑块外上边距  | 1
stv_blockBottomMargin | 滑块外下边距  | 1
stv_remain | 敏感度 距离触发开关距离  | 10
stv_leftOrRightStart | 左边开始滑动 滑动到右边算开锁 \n 右边开始滑动 滑动到左边算开锁  | 1