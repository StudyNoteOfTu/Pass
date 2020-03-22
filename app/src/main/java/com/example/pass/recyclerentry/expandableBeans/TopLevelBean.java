package com.example.pass.recyclerentry.expandableBeans;


import com.baozi.treerecyclerview.annotation.TreeDataType;

import java.util.List;

@TreeDataType(iClass = Level3Item.class,bindField = "type")
public class TopLevelBean {

    public List<Level2Bean> level2Beans;

    @TreeDataType(iClass = Level2Item.class)
    public static class Level2Bean{

        public List<Level1Bean> level1Beans;

        @TreeDataType(iClass = Level1Item.class)
        public static class Level1Bean{

            public List<Level0Item> level0Items;

            @TreeDataType(iClass = Level0Item.class)
            public static class Level0Bean{


            }

        }

    }

}
