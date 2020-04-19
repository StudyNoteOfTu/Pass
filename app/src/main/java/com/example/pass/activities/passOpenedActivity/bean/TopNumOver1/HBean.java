package com.example.pass.activities.passOpenedActivity.bean.TopNumOver1;



import android.text.SpannableStringBuilder;

import com.baozi.treerecyclerview.annotation.TreeDataType;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;

import org.apache.poi.poifs.property.Parent;

import java.util.List;

public class HBean {

    public List<H4> h4List;


    @TreeDataType(iClass = ItemH4.class,bindField = "type")
    public static class H4 implements ParentInterface<HBean> {

        public HBean parent;
        public List<H3> h3List;
        public DataContainedSpannableStringBuilder h4Text;
        public int marginLeftLevel;


        @Override
        public HBean getParent() {
            return parent;
        }

        @TreeDataType(iClass = ItemH3.class)
        public static class H3 implements ParentInterface<H4>{

            public H4 parent;
            public List<H2> h2List;
            public DataContainedSpannableStringBuilder h3Text;
            public int marginLeftLevel;


            @Override
            public H4 getParent() {
                return parent;
            }

            @TreeDataType(iClass = ItemH2.class)
            public static class H2 implements ParentInterface<H3>{

                public H3 parent;
                public List<H1> h1List;
                public DataContainedSpannableStringBuilder h2Text;
                public int marginLeftLevel;


                @Override
                public H3 getParent() {
                    return parent;
                }

                @TreeDataType(iClass = ItemH1.class)
                public static class H1 implements ParentInterface<H2>{

                    public H2 parent;
                    public int marginLeftLevel;
                    public DataContainedSpannableStringBuilder h1Text;

                    public SpannableStringBuilder detail;


                    @Override
                    public H2 getParent() {
                        return parent;
                    }

                     public H1(){
                       detail = new SpannableStringBuilder();
                   }
                }

            }

        }

    }


}
