package com.example.pass.activities.passOpenedActivity.bean.TopNumOver1;



import android.text.SpannableStringBuilder;

import com.baozi.treerecyclerview.annotation.TreeDataType;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;

import java.util.List;

public class HBean {

    public List<H4> h4List;

    @TreeDataType(iClass = ItemH4.class,bindField = "type")
    public static class H4{

        public List<H3> h3List;
        public DataContainedSpannableStringBuilder h4Text;

        @TreeDataType(iClass = ItemH3.class)
        public static class H3{

            public List<H2> h2List;
            public DataContainedSpannableStringBuilder h3Text;

            @TreeDataType(iClass = ItemH2.class)
            public static class H2{

                public List<H1> h1List;
                public DataContainedSpannableStringBuilder h2Text;

                @TreeDataType(iClass = ItemH1.class)
                public static class H1{

                    public DataContainedSpannableStringBuilder h1Text;

                    public SpannableStringBuilder detail;

                     public H1(){
                       detail = new SpannableStringBuilder();
                   }
                }

            }

        }

    }


}
