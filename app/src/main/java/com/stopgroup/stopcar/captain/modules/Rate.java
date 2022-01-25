package com.stopgroup.stopcar.captain.modules;

import java.util.List;

/**
 * Created by tarek on 13/08/18.
 */

public class Rate {

    /**
     * result : {}
     * rate_last_trip : {"id":62,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"عاصم أحمد حموده، أولى الهرم، العمرانية، الجيزة، مصر","payment_method":1,"payment_method_text":"cash","distance":228,"time_estimation":"159 Min","fare_estimation":"1378","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":456,"commission":10,"tax":912,"discount":0,"total_price":1438,"driver_rate":null,"client_rate":null,"driver_comment":null,"client_comment":null,"end_date":"2018-08-13 00:32:04","created_at":"2018-08-09 00:02:51","type":"normal","client":{"first_name":"tarek ali","last_name":"yehia","email":"tarek@tarek.com","mobile":"101919557","country_id":1,"country_code":"00966","currency":"SAR","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"1","payment_method_text":"cash","places":[]},"category_id":1,"category_name":"سيارة خصوصي"}
     * statusCode : 200
     * statusText : OK
     */

    public ResultBean result;
    public RateLastTripBean rate_last_trip;
    public int statusCode;
    public String statusText;

    public static class ResultBean {



    }

    public static class RateLastTripBean {
        /**
         * id : 62
         * from_lat : 31.1969733
         * from_lng : 30.0093933
         * to_lat : null
         * to_lng : null
         * from_location : الإسكندرية، مصر
         * to_location : عاصم أحمد حموده، أولى الهرم، العمرانية، الجيزة، مصر
         * payment_method : 1
         * payment_method_text : cash
         * distance : 228
         * time_estimation : 159 Min
         * fare_estimation : 1378
         * status : 7
         * status_text : تم الانتهاء من الرحلة
         * trip_price : 456
         * commission : 10
         * tax : 912
         * discount : 0
         * total_price : 1438
         * driver_rate : null
         * client_rate : null
         * driver_comment : null
         * client_comment : null
         * end_date : 2018-08-13 00:32:04
         * created_at : 2018-08-09 00:02:51
         * type : normal
         * client : {"first_name":"tarek ali","last_name":"yehia","email":"tarek@tarek.com","mobile":"101919557","country_id":1,"country_code":"00966","currency":"SAR","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"1","payment_method_text":"cash","places":[]}
         * category_id : 1
         * category_name : سيارة خصوصي
         */

        public int id;
        public double from_lat;
        public double from_lng;
        public String to_lat;
        public String to_lng;
        public String from_location;
        public String to_location;
        public int payment_method;
        public String payment_method_text;
        public double distance;
        public String time_estimation;
        public String fare_estimation;
        public int status;
        public String status_text;
        public double trip_price;
        public double commission;
        public double tax;
        public double discount;
        public int total_price;
        public String driver_rate;
        public String client_rate;
        public String driver_comment;
        public String client_comment;
        public String end_date;
        public String created_at;
        public String type;
        public ClientBean client;
        public int category_id;
        public String category_name;

        public static class ClientBean {
            /**
             * first_name : tarek ali
             * last_name : yehia
             * email : tarek@tarek.com
             * mobile : 101919557
             * country_id : 1
             * country_code : 00966
             * currency : SAR
             * image : https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg
             * activation_code : 0
             * active : 1
             * block : 0
             * client_credit : 0
             * payment_method : 1
             * payment_method_text : cash
             * places : []
             */

            public String first_name;
            public String last_name;
            public String email;
            public String mobile;
            public int country_id;
            public String country_code;
            public String currency;
            public String image;

            public String active;
            public String block;
            public int client_credit;
            public String payment_method;
            public String payment_method_text;
            public List<?> places;
        }
    }
}
