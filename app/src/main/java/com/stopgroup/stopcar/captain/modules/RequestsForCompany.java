package com.stopgroup.stopcar.captain.modules;

import java.util.List;

public class RequestsForCompany {







    /**
     * result : [{"id":133,"from_lat":29.92610158,"from_lng":31.51863165,"to_lat":31.002544,"to_lng":32.1444,"from_location":"Unnamed Road, محافظة القاهرة\u202c، مصر","to_location":"Unnamed Road, صان الحجر البحرية، مركز الحسينية، الشرقية، مصر","payment_method":0,"distance":165,"time_estimation":"182 Min","fare_estimation":"702","status":2,"status_text":"ملغاة","trip_price":20,"commission":22,"tax":660,"discount":0,"total_price":702,"driver_rate":null,"client_rate":null,"driver_comment":null,"client_comment":null,"end_date":null,"start_date":null,"created_at":"2018-08-31 11:25:18","type":"special","trip_special_description":"tesc desc desc","trip_special_notes":null,"trip_special_reassemble":true,"cancel_reason_id":1,"cancel_reason_name":"غير مريح","client":{"first_name":"client222","last_name":"client","email":"client@client.com","mobile":"1111111111","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-16-03-40-27-2913867-3a97ee8893a981453b0cbced934b1c75.jpeg","client_credit":40},"category_id":3,"category_name":"شركات نقل اثاث","sub_category_id":48,"sub_category_name":"sdf","requests":[],"statistics":{"arriving_minutes":"0 Min","arriving_distance":"0 km","arriving_time":"06:54 pm"}},{"id":134,"from_lat":29.92610158,"from_lng":31.51863165,"to_lat":31.002544,"to_lng":32.1444,"from_location":"Unnamed Road, محافظة القاهرة\u202c، مصر","to_location":"Unnamed Road, صان الحجر البحرية، مركز الحسينية، الشرقية، مصر","payment_method":0,"distance":165,"time_estimation":"182 Min","fare_estimation":"882","status":2,"status_text":"ملغاة","trip_price":200,"commission":22,"tax":660,"discount":0,"total_price":882,"driver_rate":null,"client_rate":null,"driver_comment":null,"client_comment":null,"end_date":null,"start_date":null,"created_at":"2018-08-31 11:29:09","type":"special","trip_special_description":"tesc desc desc","trip_special_notes":null,"trip_special_reassemble":true,"cancel_reason_id":1,"cancel_reason_name":"غير مريح","client":{"first_name":"client222","last_name":"client","email":"client@client.com","mobile":"1111111111","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-16-03-40-27-2913867-3a97ee8893a981453b0cbced934b1c75.jpeg","client_credit":40},"category_id":3,"category_name":"شركات نقل اثاث","sub_category_id":48,"sub_category_name":"sdf","requests":[],"statistics":{"arriving_minutes":"0 Min","arriving_distance":"0 km","arriving_time":"06:54 pm"}},{"id":135,"from_lat":29.92610158,"from_lng":31.51863165,"to_lat":31.002544,"to_lng":32.1444,"from_location":"Unnamed Road, محافظة القاهرة\u202c، مصر","to_location":"Unnamed Road, صان الحجر البحرية، مركز الحسينية، الشرقية، مصر","payment_method":0,"distance":165,"time_estimation":"182 Min","fare_estimation":"882","status":2,"status_text":"ملغاة","trip_price":200,"commission":22,"tax":660,"discount":0,"total_price":882,"driver_rate":null,"client_rate":null,"driver_comment":null,"client_comment":null,"end_date":null,"start_date":null,"created_at":"2018-08-31 11:29:51","type":"special","trip_special_description":"tesc desc desc","trip_special_notes":null,"trip_special_reassemble":true,"cancel_reason_id":1,"cancel_reason_name":"غير مريح","client":{"first_name":"client222","last_name":"client","email":"client@client.com","mobile":"1111111111","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-16-03-40-27-2913867-3a97ee8893a981453b0cbced934b1c75.jpeg","client_credit":40},"category_id":3,"category_name":"شركات نقل اثاث","sub_category_id":48,"sub_category_name":"sdf","requests":[],"statistics":{"arriving_minutes":"0 Min","arriving_distance":"0 km","arriving_time":"06:54 pm"}},{"id":136,"from_lat":29.92610158,"from_lng":31.51863165,"to_lat":31.002544,"to_lng":32.1444,"from_location":"Unnamed Road, محافظة القاهرة\u202c، مصر","to_location":"Unnamed Road, صان الحجر البحرية، مركز الحسينية، الشرقية، مصر","payment_method":0,"distance":165,"time_estimation":"182 Min","fare_estimation":"882","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":200,"commission":22,"tax":660,"discount":0,"total_price":882,"driver_rate":null,"client_rate":null,"driver_comment":null,"client_comment":null,"end_date":null,"start_date":null,"created_at":"2018-08-31 11:31:06","type":"special","trip_special_description":"tesc desc desc","trip_special_notes":null,"trip_special_reassemble":true,"client":{"first_name":"client222","last_name":"client","email":"client@client.com","mobile":"1111111111","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-16-03-40-27-2913867-3a97ee8893a981453b0cbced934b1c75.jpeg","client_credit":40},"category_id":3,"category_name":"شركات نقل اثاث","sub_category_id":48,"sub_category_name":"sdf"}]
     * current_request : {}
     * statusCode : 200
     * statusText : OK
     */

    public ResultBean current_request;
    public String statusCode;
    public String statusText;
    public List<ResultBean> result;

    public static class CurrentRequestBean {


    }

    public static class ResultBean {
        /**
         * id : 133
         * from_lat : 29.92610158
         * from_lng : 31.51863165
         * to_lat : 31.002544
         * to_lng : 32.1444
         * from_location : Unnamed Road, محافظة القاهرة‬، مصر
         * to_location : Unnamed Road, صان الحجر البحرية، مركز الحسينية، الشرقية، مصر
         * payment_method : 0
         * distance : 165
         * time_estimation : 182 Min
         * fare_estimation : 702
         * status : 2
         * status_text : ملغاة
         * trip_price : 20
         * commission : 22
         * tax : 660
         * discount : 0
         * total_price : 702
         * driver_rate : null
         * client_rate : null
         * driver_comment : null
         * client_comment : null
         * end_date : null
         * start_date : null
         * created_at : 2018-08-31 11:25:18
         * type : special
         * trip_special_description : tesc desc desc
         * trip_special_notes : null
         * trip_special_reassemble : true
         * cancel_reason_id : 1
         * cancel_reason_name : غير مريح
         * client : {"first_name":"client222","last_name":"client","email":"client@client.com","mobile":"1111111111","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-16-03-40-27-2913867-3a97ee8893a981453b0cbced934b1c75.jpeg","client_credit":40}
         * category_id : 3
         * category_name : شركات نقل اثاث
         * sub_category_id : 48
         * sub_category_name : sdf
         * requests : []
         * statistics : {"arriving_minutes":"0 Min","arriving_distance":"0 km","arriving_time":"06:54 pm"}
         */

        public int id;
        public double from_lat;
        public double from_lng;
        public double to_lat;
        public double to_lng;
        public String from_location;
        public String to_location;
        public String payment_method="";
        public String payment_method_text="";
        public String distance;
        public String time_estimation;
        public String fare_estimation;
        public String status;
        public String status_text;
        public String trip_price;
        public String commission;
        public String tax;
        public String discount;
        public String total_price;
        public String driver_rate;
        public String client_rate;
        public String driver_comment;
        public String client_comment;
        public String end_date;
        public String start_date;
        public String created_at;
        public String type;
        public String trip_special_description;
        public String trip_special_notes = "";
        public String trip_special_reassemble;
        public String cancel_reason_id;
        public String cancel_reason_name;
        public ClientBean client;
        public String category_id;
        public String category_name;
        public String sub_category_id;
        public String sub_category_name;
        public String trip_delivery_note;
        public StatisticsBean statistics;
        public List<?> requests;
        public String category_calculating_pricing;

        public static class ClientBean {
            /**
             * first_name : client222
             * last_name : client
             * email : client@client.com
             * mobile : 1111111111
             * image : https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-16-03-40-27-2913867-3a97ee8893a981453b0cbced934b1c75.jpeg
             * client_credit : 40
             */

            public String first_name;
            public String last_name;
            public String email;
            public String mobile;
            public String image;
            public String client_credit;
        }

        public static class StatisticsBean {
            /**
             * arriving_minutes : 0 Min
             * arriving_distance : 0 km
             * arriving_time : 06:54 pm
             */

            public String arriving_minutes;
            public String arriving_distance;
            public String arriving_time;
        }
    }
}
