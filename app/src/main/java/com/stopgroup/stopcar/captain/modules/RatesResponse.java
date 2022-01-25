package com.stopgroup.stopcar.captain.modules;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tarek on 8/5/18.
 */

public class RatesResponse {


    /**
     * result : {"avg_rate":3,"count_rates":5,"statistics":{"1":0,"2":0,"3":4,"4":0,"5":1},"rates":[{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:13:13","comment":"good","rate":3},{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:30:20","comment":"good","rate":3},{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:37:13","comment":"good","rate":3},{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:41:01","comment":"good","rate":3},{"client_name":"mahmoud ezzat","client_image":"","end_date":"2018-08-03 18:47:50","comment":"hi","rate":5}]}
     * statusCode : 200
     * statusText : OK
     */

    public ResultBean result;
    public int statusCode;
    public String statusText;

    public static class ResultBean {
        /**
         * avg_rate : 3
         * count_rates : 5
         * statistics : {"1":0,"2":0,"3":4,"4":0,"5":1}
         * rates : [{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:13:13","comment":"good","rate":3},{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:30:20","comment":"good","rate":3},{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:37:13","comment":"good","rate":3},{"client_name":"sssss zffff","client_image":"","end_date":"2018-07-26 01:41:01","comment":"good","rate":3},{"client_name":"mahmoud ezzat","client_image":"","end_date":"2018-08-03 18:47:50","comment":"hi","rate":5}]
         */

        public int avg_rate;
        public int count_rates;
        public StatisticsBean statistics;
        public ArrayList<RatesBean> rates;

        public static class StatisticsBean {
            /**
             * 1 : 0
             * 2 : 0
             * 3 : 4
             * 4 : 0
             * 5 : 1
             */

            @SerializedName("1")
            public int rate1;
            @SerializedName("2")
            public int rate2;
            @SerializedName("3")
            public int rate3;
            @SerializedName("4")
            public int rate4;
            @SerializedName("5")
            public int rate5;
        }

        public static class RatesBean {
            /**
             * client_name : sssss zffff
             * client_image :
             * end_date : 2018-07-26 01:13:13
             * comment : good
             * rate : 3
             */

            public String client_name;
            public String client_image;
            public String end_date;
            public String comment;
            public int rate;
        }
    }
}
