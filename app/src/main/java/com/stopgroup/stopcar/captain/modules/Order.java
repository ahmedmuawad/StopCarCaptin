package com.stopgroup.stopcar.captain.modules;

public class Order {


    /**
     * result : {"id":132,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"Rd Number 38, Khorshid Al Qebleyah, Qism El-Raml, Alexandria Governorate, Egypt","to_location":null,"payment_method":1,"payment_method_text":"cash","distance":0,"time_estimation":"0 Min","fare_estimation":"10","status":0,"status_text":"في الانتظار","trip_price":0,"commission":10,"tax":0,"discount":0,"total_price":10,"driver_rate":null,"client_rate":null,"driver_comment":null,"client_comment":null,"end_date":null,"start_date":null,"created_at":"2018-08-26 21:04:20","type":"normal","client":{"first_name":"mahmoud","last_name":"ezzat","email":"mmezzat23@gmail.com","mobile":"123457822","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-06-01-46-31-6069118-e1e7901ed9918304a96d5885dd1afb63.png"},"category_id":1,"category_name":"سيارة خصوصي"}
     * statusCode : 200
     * statusText : OK
     */

    public ResultBean result;
    public int statusCode;
    public String statusText;

    public static class ResultBean {
        /**
         * id : 132
         * from_lat : 31.1969733
         * from_lng : 30.0093933
         * to_lat : null
         * to_lng : null
         * from_location : Rd Number 38, Khorshid Al Qebleyah, Qism El-Raml, Alexandria Governorate, Egypt
         * to_location : null
         * payment_method : 1
         * payment_method_text : cash
         * distance : 0
         * time_estimation : 0 Min
         * fare_estimation : 10
         * status : 0
         * status_text : في الانتظار
         * trip_price : 0
         * commission : 10
         * tax : 0
         * discount : 0
         * total_price : 10
         * driver_rate : null
         * client_rate : null
         * driver_comment : null
         * client_comment : null
         * end_date : null
         * start_date : null
         * created_at : 2018-08-26 21:04:20
         * type : normal
         * client : {"first_name":"mahmoud","last_name":"ezzat","email":"mmezzat23@gmail.com","mobile":"123457822","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-06-01-46-31-6069118-e1e7901ed9918304a96d5885dd1afb63.png"}
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
        public String delay_time;
        public String fare_estimation;
        public int status = -1;
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
        public String start_date;
        public String created_at;
        public String type;
        public String trip_special_notes;
        public String trip_delivery_note;
        public ClientBean client;
        public int category_id;
        public String category_name;
        public String category_calculating_pricing;
        public String trip_special_description;

        public String country_tax;
        public String sub_category_name;


        public static class ClientBean {
            /**
             * first_name : mahmoud
             * last_name : ezzat
             * email : mmezzat23@gmail.com
             * mobile : 123457822
             * image : https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-06-01-46-31-6069118-e1e7901ed9918304a96d5885dd1afb63.png
             */

            public String first_name;
            public String last_name;
            public String email;
            public String mobile;
            public String image;
            public String client_credit;
        }
    }
}
