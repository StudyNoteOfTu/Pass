package com.example.pass.test.shade;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pass.R;
import com.example.pass.util.ImageFormatTools;


public class ShadeActivity extends AppCompatActivity {

    ShadeView shadeView;

    ShadeRelativeLayout shadeRelativeLayout;

    TextView textView;


    String content = "一月底，终于准备回老家，这天我们都在收拾行李，母亲的收拾能力总能让行李刚好塞满后备箱的最后一丝空隙。车的后座有一半铺上了垫子，是给两只狗准备的，前排准备了砂糖橘，红牛，还有烟。这将是个长达九个小时的自驾行，从广东开往福州。父亲想保养精神，下午去午睡，可还是抵不住返乡的激动，睡了一个多小时就下来接着收拾了。凌晨一点我们出发，车上放着几年不变口味的父亲喜欢的车载音乐，滚着月色，踏着星辰，我们返乡了。" +
            "老家没有变化，说没有变化，应该针对的是自己的老房子没有变化，其实村子已经变美了许多，修起了健身步道，桥头补上了夜晚景观的黄色霓虹灯。过去桥底下翻滚的土黄色的河水已经变得清澈，因为上游修起了水库。" +
            "家里热闹得很，奶奶，我们家三口，大叔叔家四口，小叔叔家四口，还有两只小黄狗。每天听到的几乎都是几个只有几岁的小娃娃的声音和叔叔婶婶们哄孩子的声音。乡下没有WiFi，没有闭路，只有将下载好的电影存在电脑里，连接到显示屏上才能看，过年我们最安逸的几件事也不过是在一起看恐怖电影还要在血腥画面出来的时候遮住小朋友的眼睛，在院子里打羽毛球到汗流浃背，吃本地糖拌年糕还有海蛎饼。" +
            "过年说是归宿，可以把自己拉回正道，我想应该是在这段安静的时间，最容易沉下心听到前辈说的话。奶奶在今年一月底，终于准备回老家，这天我们都在收拾行李，母亲的收拾能力总能让行李刚好塞满后备箱的最后一丝空隙。车的后座有一半铺上了垫子，是给两只狗准备的，前排准备了砂糖橘，红牛，还有烟。这将是个长达九个小时的自驾行，从广东开往福州。父亲想保养精神，下午去午睡，可还是抵不住返乡的激动，睡了一个多小时就下来接着收拾了。凌晨一点我们出发，车上放着几年不变口味的父亲喜欢的车载音乐，滚着月色，踏着星辰，我们返乡了。\" +\n" +
            "            \"老家没有变化，说没有变化，应该针对的是自己的老房子没有变化，其实村子已经变美了许多，修起了健身步道，桥头补上了夜晚景观的黄色霓虹灯。过去桥底下翻滚的土黄色的河水已经变得清澈，因为上游修起了水库。\" +\n" +
            "            \"家里热闹得很，奶奶，我们家三口，大叔叔家四口，小叔叔家四口，还有两只小黄狗。每天听到的几乎都是几个只有几岁的小娃娃的声音和叔叔婶婶们哄孩子的声音。乡下没有WiFi，没有闭路，只有将下载好的电影存在电脑里，连接到显示屏上才能看，过年我们最安逸的几件事也不过是在一起看恐怖电影还要在血腥画面出来的时候遮住小朋友的眼睛，在院子里打羽毛球到汗流浃背，吃本地糖拌年糕还有海蛎饼。\" +\n" +
            "            \"过年说是归宿，可以把自己拉回正道，我想应该是在这段安静的时间，最容易沉下心听到前辈说的话。奶奶在今年一月底，终于准备回老家，这天我们都在收拾行李，母亲的收拾能力总能让行李刚好塞满后备箱的最后一丝空隙。车的后座有一半铺上了垫子，是给两只狗准备的，前排准备了砂糖橘，红牛，还有烟。这将是个长达九个小时的自驾行，从广东开往福州。父亲想保养精神，下午去午睡，可还是抵不住返乡的激动，睡了一个多小时就下来接着收拾了。凌晨一点我们出发，车上放着几年不变口味的父亲喜欢的车载音乐，滚着月色，踏着星辰，我们返乡了。\" +\n" +
            "            \"老家没有变化，说没有变化，应该针对的是自己的老房子没有变化，其实村子已经变美了许多，修起了健身步道，桥头补上了夜晚景观的黄色霓虹灯。过去桥底下翻滚的土黄色的河水已经变得清澈，因为上游修起了水库。\" +\n" +
            "            \"家里热闹得很，奶奶，我们家三口，大叔叔家四口，小叔叔家四口，还有两只小黄狗。每天听到的几乎都是几个只有几岁的小娃娃的声音和叔叔婶婶们哄孩子的声音。乡下没有WiFi，没有闭路，只有将下载好的电影存在电脑里，连接到显示屏上才能看，过年我们最安逸的几件事也不过是在一起看恐怖电影还要在血腥画面出来的时候遮住小朋友的眼睛，在院子里打羽毛球到汗流浃背，吃本地糖拌年糕还有海蛎饼。\" +\n" +
            "            \"过年说是归宿，可以把自己拉回正道，我想应该是在这段安静的时间，最容易沉下心听到前辈说的话。奶奶在今年";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shade);

        Drawable drawable = getResources().getDrawable(R.drawable.icon);
        BitmapDrawable bd= (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();

        bitmap = ImageFormatTools.scaleBitmapByWidth(bitmap,800);



        SpannableStringBuilder sb = new SpannableStringBuilder(content);


        sb.insert(99,"\n");
        sb.insert(140,"\n");

        sb.insert(199,"\n");
        sb.insert(240,"\n");

        sb.insert(399,"\n");
        sb.insert(440,"\n");

        sb.insert(499,"\n");
        sb.insert(540,"\n");

        CenterImageSpan centerImageSpan = new CenterImageSpan(this,bitmap);
        centerImageSpan.setImgUrl("fafafa");
        sb.setSpan(centerImageSpan,100,140, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        centerImageSpan = new CenterImageSpan(this,bitmap);
        centerImageSpan.setImgUrl("hehehe");
        sb.setSpan(centerImageSpan,200,240, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        centerImageSpan = new CenterImageSpan(this,bitmap);
        centerImageSpan.setImgUrl("gagaga");
        sb.setSpan(centerImageSpan,400,440, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        centerImageSpan = new CenterImageSpan(this,bitmap);
        centerImageSpan.setImgUrl("dadada");
        sb.setSpan(centerImageSpan,500,540, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        shadeRelativeLayout = findViewById(R.id.shadeRelativeLayout);
        shadeView = findViewById(R.id.shadeView);

        textView = findViewById(R.id.textView);
        textView.setText(sb);
        textView.setMovementMethod(new ClickableLinkMovementMethod());


        shadeRelativeLayout.setShadeView(shadeView);
        ShadeManager shadeManager = ShadeManager.getInstance();
        shadeView.init(textView,shadeManager,sb);
        shadeManager.setHolder(textView);
        shadeManager.setOnLocateCallBack(shadeView);
    }
}
