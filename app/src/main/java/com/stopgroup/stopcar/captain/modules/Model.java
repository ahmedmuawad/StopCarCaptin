package com.stopgroup.stopcar.captain.modules;

import java.util.List;

public class Model {
    public int statusCode;
    public String statusText;
    public List<ResultBean> result;
    public static class ResultBean {
        public int id;
        public String name;

    }
}
