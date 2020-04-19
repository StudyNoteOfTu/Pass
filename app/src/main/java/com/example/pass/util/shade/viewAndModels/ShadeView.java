package com.example.pass.util.shade.viewAndModels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pass.util.shade.ShadeManager;
import com.example.pass.util.spanUtils.SpanToXmlUtil;
import com.example.pass.util.spans.customSpans.MyImageSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShadeView extends View implements ShadeManager.OnLocateCallBack {

    //树的形式存储shader
    ConcurrentHashMap<String, ConcurrentHashMap<String, Shader>> dataSourceShaderMap = new ConcurrentHashMap<>();

    ConcurrentHashMap<String,Shader> deleteShaderMap = new ConcurrentHashMap<>();

    ConcurrentHashMap<String, String> appearPic = new ConcurrentHashMap<>();

    String url = "aabb";

    //shader盖住的textView的所有MyImageSpan
    List<MyImageSpan> myImageSpanList = new ArrayList<>();

    //文字shader方片
    List<TextShader> myTextShaderList = new ArrayList<>();

    //覆盖的textView
    private TextView mTextView;

    //手指滑动的路线
    private FingerLine fingerPathLine;

    //手指滑动路线的画笔
    private Paint fingerLinePaint;

    //绘制遮罩矩形的画笔
    private Paint mDrawRectPaint;

    //上层调度者
    private ShadeManager mShadeManager;

    //TextView的Spannable（需要传入，无法直接从textView获取）
    private Spannable mSpannable;

    private boolean isEditable = false;


    private int downX;
    private int downY;

    private int lastX;
    private int lastY;

    boolean isDealingShader = false;

    //当前触摸的ImageSpan
    MyImageSpan touchedSpan;

    List<MyImageSpan> myImageSpansShown = new ArrayList<>();

    //span在屏幕上显示的top
    int y_top;
    //span在屏幕上显示的bottom
    int y_bottom;
    //是否初始化
    private boolean initialed = false;

    public ShadeView(Context context) {
        super(context);
    }

    public ShadeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShadeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(List<Shader> shaders, TextView textView, ShadeManager shadeManager, Spannable spannable, Paint drawRectPaint) {
        Log.d("CreateShadeView", "init createShadeView data" + shadeManager.data);
        mSpannable = spannable;
        mShadeManager = shadeManager;
        mTextView = textView;
        //手指画笔
        fingerPathLine = new FingerLine();
        fingerLinePaint = new Paint();
        fingerLinePaint.setColor(Color.RED);
        fingerLinePaint.setStrokeWidth(10f);
        fingerLinePaint.setStyle(Paint.Style.FILL);

        //矩形画笔
        if (drawRectPaint != null) {
            mDrawRectPaint = drawRectPaint;
        } else {
            mDrawRectPaint = new Paint();
            mDrawRectPaint.setColor(0xffffff00);
            mDrawRectPaint.setStyle(Paint.Style.FILL);
        }
        //防止获取layout为空
        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //初始化信息

                if (mShadeManager.getHolder() == null) {
                    mShadeManager.setHolder(textView);
                }

                //获取所有centerImageSpan
                myImageSpanList.clear();
                myImageSpanList.addAll(mShadeManager.getAllCenterImageSpan(spannable));

                Log.d("MyImageSpanList", "centerImageSpan size = " + myImageSpanList.size());
                myTextShaderList.clear();
                myTextShaderList.addAll(mShadeManager.getAllShadeSpan(spannable));

                //将shaders添加到map当中去
                String image_path;
                String time_tag;
                ConcurrentHashMap<String, Shader> inner;
                boolean needContinue = true;
                Log.d("Loop", "--------------------");
                for (Shader shader : shaders) {
                    //不是自己的不加进来
                    needContinue = true;
                    //如果有image
                    //有一样的就加入
                    for (MyImageSpan myImageSpan : myImageSpanList) {
                        if (shader.getImgUrl().equalsIgnoreCase(myImageSpan.getImgUrl())) {
                            //如果有一样的，那么就添加进来
                            needContinue = false;
                        }
                    }
                    //如果压根没有image
                    if (myImageSpanList.size() == 0) needContinue = true;

                    if (needContinue) continue;

                    image_path = shader.getImgUrl();
                    time_tag = shader.getTimeTag();
                    //获取路径
                    inner = dataSourceShaderMap.get(image_path);
                    if (inner == null) {
                        inner = new ConcurrentHashMap<>();
                        inner.put(time_tag, shader);
                        dataSourceShaderMap.put(image_path, inner);
                    } else {
                        inner.put(time_tag, shader);
                    }

                }

                initialed = true;
            }
        });
    }


    private void addShaderToMap(String url, String timeTag, Shader shader) {
        if (shader != null) {
            ConcurrentHashMap<String, Shader> map = dataSourceShaderMap.get(url);
            if (map != null) {
                map.put(timeTag, shader);
            } else {
                ConcurrentHashMap<String, Shader> inner = new ConcurrentHashMap<>();
                inner.put(timeTag, shader);
                dataSourceShaderMap.put(url, inner);
            }
        }
    }


    public void delShader(String url, String timeTag) {

        try {
            ConcurrentHashMap<String, Shader> inner = dataSourceShaderMap.get(url);

            if (inner != null) {
                if (inner.get(timeTag) == Shader.currentTouchShader) {
                    Shader.currentTouchShader = null;
                }
                deleteShaderMap.put(timeTag,inner.get(timeTag));
                inner.remove(timeTag);

            }
        } catch (Exception e) {
            Log.d("TestException", e.getMessage());
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!initialed) return false;


        int x = (int) event.getX();
        int y = (int) event.getY();
        //触摸必须在图片上，且不能超出去

        //触摸，如果在图片内，则进行接下来的方片操作
        //如果超出图片范围则取消接下来的操作

        //判断一开始触摸的是在谁里面
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //判断点的在不在优先Shader中,如果不在就把它取消掉
            if (Shader.currentTouchShader != null && !Shader.currentTouchShader.isPointInner(x, y)) {
                Shader.currentTouchShader = null;
            }

            for (Map<String, Shader> inner : dataSourceShaderMap.values()) {
                for (Shader shader : inner.values()) {
                    if (shader.isPointInner(x, y)) {
                        isDealingShader = true;
                    } else {
                        //如果不在，就设置Editable = false
                        shader.setEditable(false);
                    }
                }
            }
        }

        if (!isEditable) {
            isDealingShader = false;
        }

        //判断按下的位置在哪里。如果不在shader里面，那么就不传给shader
        if (!isDealingShader) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    lastX = (int) event.getX();
                    lastY = (int) event.getY();

                    touchedSpan = mShadeManager.getPressedImageSpan(mSpannable, event);

                    Log.d("ShadeView", "down");
                    downX = (int) event.getX();
                    downY = (int) event.getY();
                    if (isEditable) {
                        fingerPathLine.setDownX(downX);
                        fingerPathLine.setDownY(downY);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    if (isEditable) {
                        fingerPathLine.setCalculating(true);
                        //获取末端位置，绘制线条
                        fingerPathLine.setNowX((int) event.getX());
                        fingerPathLine.setNowY((int) event.getY());
                        fingerPathLine.setWorking(true);

                    }
                    //判断fingerPath是否在重要方向区域内
                    //如果不是，则控制TextView

                    if (!fingerPathLine.isInDangerousDirection() || !isEditable) {//可编辑，不在危险区，或者不可编辑
                        int delY = (int) event.getY() - lastY;

                        Layout layout = mTextView.getLayout();
                        int line = mTextView.getLineCount() - 1;
                        int bottom = 0;
                        if (layout != null) bottom = layout.getLineTop(line + 1);

                        //判断滑动范围
                        if (bottom <= mTextView.getHeight()) {
                            //view 并不高，无法滑动
                        } else if ((mTextView.getScrollY() - delY) <= 0) {
                            //如果滑动到顶部
                            mTextView.scrollTo(0, 0);
                        } else if (layout != null && (bottom - mTextView.getHeight()) <= (mTextView.getScrollY() - delY)) {
                            //如果滑动到底部,思路借鉴ScrollingMovementMethod.class
                            mTextView.scrollTo(0, bottom - mTextView.getHeight());
                        } else {
                            mTextView.scrollBy(0, -delY);
                        }
                    }

                    lastX = (int) event.getX();
                    lastY = (int) event.getY();


                    break;
                case MotionEvent.ACTION_UP:

                    if (isEditable) {//如果可编辑
                        if (fingerPathLine.isCalculating()) {
                            fingerPathLine.setWorking(false);
                            MyImageSpan imageSpan = mShadeManager.getPressedImageSpan(mSpannable, event);
                            if (touchedSpan == null || imageSpan == null || imageSpan != touchedSpan) {
                                //如果不在同个图片上，或者down的时候不在图片上，或者up时候不在图片内
                                //则不可绘制shader
                                if (touchedSpan == null && imageSpan == null) {
                                    //判断是否在文字上且在同一行

                                    int downX = fingerPathLine.getDownX();
                                    int nowX = fingerPathLine.getNowX();
                                    int downY = fingerPathLine.getDownY();
                                    int nowY = fingerPathLine.getNowY();
                                    if (mShadeManager.getPressedImageSpan(mSpannable, downX, downY) == null
                                            && mShadeManager.getPressedImageSpan(mSpannable, nowX, nowY) == null
                                            && mShadeManager.getPressedImageSpan(mSpannable, (downX + nowX) / 2, (downY + nowY) / 2) == null) {
                                        dealFingerPathLine();
                                    }
                                }
                            } else {
                                dealFingerPathLine(touchedSpan);
                            }

                            if (touchedSpan == null && imageSpan == null) {
                                //如果down和up都不在图片上
                                //可能是文字涂抹，交给TextView处理？
                            }

                            fingerPathLine.setCalculating(false);
                        }
                    }
                    //重置touchedSpan
                    touchedSpan = null;
                    break;

            }
        } else {
            Log.d("ShadeView", "downInShader");
            //如果在方块里面
            //交给最后添加的shader
            //优先交给顶层优先Shader
            if (Shader.currentTouchShader != null) {
                Shader.currentTouchShader.onTouch(event);
                isDealingShader = true;
            } else {
                for (Map<String, Shader> inner : dataSourceShaderMap.values()) {
                    for (Shader shader : inner.values()) {
                        if (shader.onTouch(event)) {
                            //如果处理了，拦截
                            isDealingShader = true;
                            break;
                        }
                    }

                }
            }

        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            isDealingShader = false;
        }
        return false;
    }

    //判断是否在同一行文字上,只需要判断是否同一行即可
    private void dealFingerPathLine() {
        if (fingerPathLine != null) {
            //让manager去判断
            int downX = fingerPathLine.getDownX();
            int nowX = fingerPathLine.getNowX();
            int downY = fingerPathLine.getDownY();
            int nowY = fingerPathLine.getNowY();
            int delX = fingerPathLine.getNowX() - fingerPathLine.getDownX();
            //判断方向
            if (delX > 0) {
                int pressedLine = mShadeManager.getPressTextInLine(downY + mTextView.getScrollY(), nowY + mTextView.getScrollY());
                //如果是
                if (pressedLine != -1) {
                    //TODO:如果在同一行，则进行span设置，并且更新span，以及更新文字shader图片
                    mSpannable = mShadeManager.insertTextShadeSpan(mSpannable, pressedLine, downX, nowX);
                    myTextShaderList.clear();
                    myTextShaderList.addAll(mShadeManager.getAllShadeSpan(mSpannable));
                }
            }
        }
    }

    private void dealFingerPathLine(MyImageSpan touchedSpan) {
        if (fingerPathLine != null) {
            //判断方位，添加Shader
            int delX = fingerPathLine.getNowX() - fingerPathLine.getDownX();
            int delY = fingerPathLine.getNowY() - fingerPathLine.getDownY();
            if (delY > Shader.MIN_HEIGHT && delX > Shader.MIN_WIDTH && delY <= 2 * delX) {
//                addShader(fingerPathLine.getDownX(), fingerPathLine.getDownY(), delX, delY);
                Shader shader = new Shader(this, fingerPathLine.getDownX(), fingerPathLine.getDownY(), delX, delY);
                String millisTag = getMillisTAG();
                //设置对应的图片
                shader.setImgUrl(touchedSpan.getImgUrl());
                shader.setTimeTag(millisTag);
                //设置相对位置
                //当前图片在TextView显示的位置
                int top = (int) touchedSpan.y - mTextView.getScrollY();
                int left = (int) touchedSpan.x + mTextView.getScrollX();

                shader.setLeftPadding(fingerPathLine.getDownX() - left);
                shader.setTopPadding(fingerPathLine.getDownY() - top);

                //addShader(shader);

                addShaderToMap(touchedSpan.getImgUrl(), millisTag, shader);


            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //绘制文字遮罩
        for (TextShader shader : myTextShaderList) {
            shader.draw(canvas, mDrawRectPaint, mTextView.getScrollX(), mTextView.getScrollY());
        }
        //绘制图片遮罩
        //判断哪些CenterImageSpan在屏幕上
        myImageSpansShown.clear();
        for (MyImageSpan centerImageSpan : myImageSpanList) {
            y_top = (int) (centerImageSpan.y - mTextView.getScrollY());
            y_bottom = (int) (centerImageSpan.bottom - mTextView.getScrollY());

            if (y_bottom < 0 || y_top > mTextView.getMeasuredHeight() || mShadeManager != null && appearPic.get(centerImageSpan.getImgUrl()) == null) {
                //底部已经在上面或者顶部在下面
                Log.d("2020418DrawTest", "can not draw，url= " + centerImageSpan.getImgUrl());
            } else {
                //在绘制
                myImageSpansShown.add(centerImageSpan);
            }
        }


        Shader topShader = null;
        MyImageSpan topShadersImageSpan = null;

        //根据url获取需要绘制的shader

        for (MyImageSpan myImageSpan : myImageSpansShown) {
            Map<String, Shader> inner = dataSourceShaderMap.get(myImageSpan.getImgUrl());

            if (inner != null) {
                //删除数据
                for (String s : inner.keySet()) {
                    if (deleteShaderMap.get(s)!=null){
                        inner.remove(s);
                    }
                }
                for (Shader shader : inner.values()) {
                    if (shader != null) {
                        if (shader == Shader.currentTouchShader) {
                            topShader = shader;
                            topShadersImageSpan = myImageSpan;
                        }
                        shader.draw(mDrawRectPaint, (int) myImageSpan.x, (int) myImageSpan.y, myImageSpan.bottom, mTextView.getScrollX(), mTextView.getScrollY(), canvas);
                    }
                }
            }
        }

        //如果是当前Touch的顶层绘制，由于topShader是从需要绘制的里面抽取出来的，所以肯定需要绘制
        if (topShader != null) {
            topShader.draw(mDrawRectPaint, (int) topShadersImageSpan.x, (int) topShadersImageSpan.y, topShadersImageSpan.bottom, mTextView.getScrollX(), mTextView.getScrollY(), canvas);
        }

        if (isEditable) {
            //finger线是最顶层绘制的
            if (fingerPathLine != null && fingerPathLine.isWorking() && fingerLinePaint != null) {
                canvas.drawLine(fingerPathLine.getDownX(), fingerPathLine.getDownY(),
                        fingerPathLine.getNowX(), fingerPathLine.getNowY(), fingerLinePaint);
            }
        }

        super.onDraw(canvas);
        //不停地更新绘制
        invalidate();
    }

    /**
     * 获取时间转换为的TAG
     *
     * @return 六位TAG
     */
    private String getMillisTAG() {
        String tag = String.valueOf(System.currentTimeMillis());
        return tag.substring(tag.length() - 6);
    }

    public List<Shader> getAllShaders() {
        List<Shader> shaders = new ArrayList<>();
        for (Map<String, Shader> values : dataSourceShaderMap.values()) {
            shaders.addAll(values.values());
        }
        Log.d("getAllShaders", "shader hashcode = " + dataSourceShaderMap.hashCode() + "size = " + shaders.size());
        return shaders;
    }


    @Override
    public void onLocate(String url) {
        addUrl(url);
    }

    public void addUrl(String url) {
        this.appearPic.put(url, "");
        Log.d("2020419UrlTest", "url = " + url);
    }

    public String getXmlString() {
        return SpanToXmlUtil.transformAllSpanToXmlFile(new SpannableStringBuilder(mSpannable));
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Paint getmDrawRectPaint() {
        return mDrawRectPaint;
    }
}
