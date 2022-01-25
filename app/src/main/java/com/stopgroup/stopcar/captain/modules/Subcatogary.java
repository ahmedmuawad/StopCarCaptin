package com.stopgroup.stopcar.captain.modules;

import java.util.List;

public class Subcatogary {
    public int statusCode;
    public String statusText;
    public List<ResultBean> result;
    public static class ResultBean {
        public int id;
        public String name;
        public String image;
        public String description = "";
        public List<ChildsBean> childs;

        public static class ChildsBean {
            public int id;
            public String name;
            public String image;
            public int fixed_price;
            public int parent_id;
        }
    }
}
