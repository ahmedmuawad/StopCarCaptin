package com.stopgroup.stopcar.captain.modules;

import java.util.List;

/**
 * Created by tarek on 31/07/18.
 */

public class History {


    /**
     * result : {"today":{"trips":[{"id":33,"from_lat":31.000111,"from_lng":30.010001,"to_lat":31.002544,"to_lng":32.1444,"from_location":"كوم الفرج، مركز أبو المطامير، البحيرة، مصر","to_location":"Unnamed Road, San Al Hagar Al Bahreyah, Markaz El-Hosayneya, Ash Sharqia Governorate, مصر","payment_method":1,"payment_method_text":"cash","distance":312,"time_estimation":"267 Min","fare_estimation":"956","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":624,"commission":20,"tax":312,"discount":0,"total_price":936,"driver_rate":null,"client_rate":4,"driver_comment":null,"client_comment":"goods","end_date":"2018-07-31 03:06:18","created_at":"2018-07-31 00:12:31","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"}],"statistics":{"total_earned":936,"count_trips":1,"spend_time":"4h 27m"}},"week":{"trips":[{"id":26,"from_lat":31.528625,"from_lng":31.029095,"to_lat":null,"to_lng":null,"from_location":"Unnamed Road, Al Abaad Al Bahreyah, El-Hamoul, Kafr El Sheikh Governorate, مصر","to_location":"الإسكندرية، مصر","payment_method":1,"payment_method_text":"cash","distance":152,"time_estimation":"120 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:13:13","created_at":"2018-07-25 01:48:00","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":30,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:30:20","created_at":"2018-07-26 01:26:03","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":31,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:37:13","created_at":"2018-07-26 01:32:30","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":32,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:41:01","created_at":"2018-07-26 01:40:12","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":33,"from_lat":31.000111,"from_lng":30.010001,"to_lat":31.002544,"to_lng":32.1444,"from_location":"كوم الفرج، مركز أبو المطامير، البحيرة، مصر","to_location":"Unnamed Road, San Al Hagar Al Bahreyah, Markaz El-Hosayneya, Ash Sharqia Governorate, مصر","payment_method":1,"payment_method_text":"cash","distance":312,"time_estimation":"267 Min","fare_estimation":"956","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":624,"commission":20,"tax":312,"discount":0,"total_price":936,"driver_rate":null,"client_rate":4,"driver_comment":null,"client_comment":"goods","end_date":"2018-07-31 03:06:18","created_at":"2018-07-31 00:12:31","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"}],"statistics":{"total_earned":936,"count_trips":5,"spend_time":"6h 27m"}}}
     * statusCode : 200
     * statusText : OK
     */

    public ResultBean result;
    public int statusCode;
    public String statusText;

    public static class ResultBean {
        /**
         * today : {"trips":[{"id":33,"from_lat":31.000111,"from_lng":30.010001,"to_lat":31.002544,"to_lng":32.1444,"from_location":"كوم الفرج، مركز أبو المطامير، البحيرة، مصر","to_location":"Unnamed Road, San Al Hagar Al Bahreyah, Markaz El-Hosayneya, Ash Sharqia Governorate, مصر","payment_method":1,"payment_method_text":"cash","distance":312,"time_estimation":"267 Min","fare_estimation":"956","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":624,"commission":20,"tax":312,"discount":0,"total_price":936,"driver_rate":null,"client_rate":4,"driver_comment":null,"client_comment":"goods","end_date":"2018-07-31 03:06:18","created_at":"2018-07-31 00:12:31","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"}],"statistics":{"total_earned":936,"count_trips":1,"spend_time":"4h 27m"}}
         * week : {"trips":[{"id":26,"from_lat":31.528625,"from_lng":31.029095,"to_lat":null,"to_lng":null,"from_location":"Unnamed Road, Al Abaad Al Bahreyah, El-Hamoul, Kafr El Sheikh Governorate, مصر","to_location":"الإسكندرية، مصر","payment_method":1,"payment_method_text":"cash","distance":152,"time_estimation":"120 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:13:13","created_at":"2018-07-25 01:48:00","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":30,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:30:20","created_at":"2018-07-26 01:26:03","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":31,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:37:13","created_at":"2018-07-26 01:32:30","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":32,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:41:01","created_at":"2018-07-26 01:40:12","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":33,"from_lat":31.000111,"from_lng":30.010001,"to_lat":31.002544,"to_lng":32.1444,"from_location":"كوم الفرج، مركز أبو المطامير، البحيرة، مصر","to_location":"Unnamed Road, San Al Hagar Al Bahreyah, Markaz El-Hosayneya, Ash Sharqia Governorate, مصر","payment_method":1,"payment_method_text":"cash","distance":312,"time_estimation":"267 Min","fare_estimation":"956","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":624,"commission":20,"tax":312,"discount":0,"total_price":936,"driver_rate":null,"client_rate":4,"driver_comment":null,"client_comment":"goods","end_date":"2018-07-31 03:06:18","created_at":"2018-07-31 00:12:31","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"}],"statistics":{"total_earned":936,"count_trips":5,"spend_time":"6h 27m"}}
         */

        public TodayBean today;
        public WeekBean week;

        public static class TodayBean {
            /**
             * trips : [{"id":33,"from_lat":31.000111,"from_lng":30.010001,"to_lat":31.002544,"to_lng":32.1444,"from_location":"كوم الفرج، مركز أبو المطامير، البحيرة، مصر","to_location":"Unnamed Road, San Al Hagar Al Bahreyah, Markaz El-Hosayneya, Ash Sharqia Governorate, مصر","payment_method":1,"payment_method_text":"cash","distance":312,"time_estimation":"267 Min","fare_estimation":"956","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":624,"commission":20,"tax":312,"discount":0,"total_price":936,"driver_rate":null,"client_rate":4,"driver_comment":null,"client_comment":"goods","end_date":"2018-07-31 03:06:18","created_at":"2018-07-31 00:12:31","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"}]
             * statistics : {"total_earned":936,"count_trips":1,"spend_time":"4h 27m"}
             */

            public StatisticsBean statistics;
            public List<TripsBean> trips;

            public static class StatisticsBean {
                /**
                 * total_earned : 936
                 * count_trips : 1
                 * spend_time : 4h 27m
                 */

                public int total_earned;
                public int count_trips;
                public String spend_time;
            }

            public static class TripsBean {
                /**
                 * id : 33
                 * from_lat : 31.000111
                 * from_lng : 30.010001
                 * to_lat : 31.002544
                 * to_lng : 32.1444
                 * from_location : كوم الفرج، مركز أبو المطامير، البحيرة، مصر
                 * to_location : Unnamed Road, San Al Hagar Al Bahreyah, Markaz El-Hosayneya, Ash Sharqia Governorate, مصر
                 * payment_method : 1
                 * payment_method_text : cash
                 * distance : 312
                 * time_estimation : 267 Min
                 * fare_estimation : 956
                 * status : 7
                 * status_text : تم الانتهاء من الرحلة
                 * trip_price : 624
                 * commission : 20
                 * tax : 312
                 * discount : 0
                 * total_price : 936
                 * driver_rate : null
                 * client_rate : 4
                 * driver_comment : null
                 * client_comment : goods
                 * end_date : 2018-07-31 03:06:18
                 * created_at : 2018-07-31 00:12:31
                 * client : {"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]}
                 * category_id : 1
                 * category_name : سيارة ملاكي
                 */

                public int id;
                public double from_lat;
                public double from_lng;
                public double to_lat;
                public double to_lng;
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
                public Object driver_rate;
                public int client_rate;
                public Object driver_comment;
                public String client_comment;
                public String end_date;
                public String created_at;
                public ClientBean client;
                public int category_id;
                public String category_name;

                public static class ClientBean {
                    /**
                     * first_name : sssss
                     * last_name : zffff
                     * email : xxxx@yyyy.com
                     * mobile : 556321789
                     * country_id : 1
                     * country_code : 020
                     * currency : USD
                     * image : https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg
                     * activation_code : 0
                     * active : 1
                     * block : 0
                     * client_credit : 0
                     * payment_method : 2
                     * payment_method_text : credit
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

        public static class WeekBean {
            /**
             * trips : [{"id":26,"from_lat":31.528625,"from_lng":31.029095,"to_lat":null,"to_lng":null,"from_location":"Unnamed Road, Al Abaad Al Bahreyah, El-Hamoul, Kafr El Sheikh Governorate, مصر","to_location":"الإسكندرية، مصر","payment_method":1,"payment_method_text":"cash","distance":152,"time_estimation":"120 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:13:13","created_at":"2018-07-25 01:48:00","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":30,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:30:20","created_at":"2018-07-26 01:26:03","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":31,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:37:13","created_at":"2018-07-26 01:32:30","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":32,"from_lat":31.1969733,"from_lng":30.0093933,"to_lat":null,"to_lng":null,"from_location":"الإسكندرية، مصر","to_location":"الإسكندرية، مصر","payment_method":2,"payment_method_text":"credit","distance":0,"time_estimation":"0 Min","fare_estimation":"0","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":0,"commission":null,"tax":0,"discount":0,"total_price":0,"driver_rate":3,"client_rate":4,"driver_comment":"good","client_comment":"goods","end_date":"2018-07-26 01:41:01","created_at":"2018-07-26 01:40:12","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"},{"id":33,"from_lat":31.000111,"from_lng":30.010001,"to_lat":31.002544,"to_lng":32.1444,"from_location":"كوم الفرج، مركز أبو المطامير، البحيرة، مصر","to_location":"Unnamed Road, San Al Hagar Al Bahreyah, Markaz El-Hosayneya, Ash Sharqia Governorate, مصر","payment_method":1,"payment_method_text":"cash","distance":312,"time_estimation":"267 Min","fare_estimation":"956","status":7,"status_text":"تم الانتهاء من الرحلة","trip_price":624,"commission":20,"tax":312,"discount":0,"total_price":936,"driver_rate":null,"client_rate":4,"driver_comment":null,"client_comment":"goods","end_date":"2018-07-31 03:06:18","created_at":"2018-07-31 00:12:31","client":{"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]},"category_id":1,"category_name":"سيارة ملاكي"}]
             * statistics : {"total_earned":936,"count_trips":5,"spend_time":"6h 27m"}
             */

            public StatisticsBeanX statistics;
            public List<TripsBeanX> trips;

            public static class StatisticsBeanX {
                /**
                 * total_earned : 936
                 * count_trips : 5
                 * spend_time : 6h 27m
                 */

                public double total_earned;
                public double count_trips;
                public String spend_time;

                public double  weeklyDriver ;
                public double  weeklyAdmin ;
            }

            public static class TripsBeanX {
                /**
                 * id : 26
                 * from_lat : 31.528625
                 * from_lng : 31.029095
                 * to_lat : null
                 * to_lng : null
                 * from_location : Unnamed Road, Al Abaad Al Bahreyah, El-Hamoul, Kafr El Sheikh Governorate, مصر
                 * to_location : الإسكندرية، مصر
                 * payment_method : 1
                 * payment_method_text : cash
                 * distance : 152
                 * time_estimation : 120 Min
                 * fare_estimation : 0
                 * status : 7
                 * status_text : تم الانتهاء من الرحلة
                 * trip_price : 0
                 * commission : null
                 * tax : 0
                 * discount : 0
                 * total_price : 0
                 * driver_rate : 3
                 * client_rate : 4
                 * driver_comment : good
                 * client_comment : goods
                 * end_date : 2018-07-26 01:13:13
                 * created_at : 2018-07-25 01:48:00
                 * client : {"first_name":"sssss","last_name":"zffff","email":"xxxx@yyyy.com","mobile":"556321789","country_id":1,"country_code":"020","currency":"USD","image":"https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg","activation_code":0,"active":"1","block":"0","client_credit":0,"payment_method":"2","payment_method_text":"credit","places":[]}
                 * category_id : 1
                 * category_name : سيارة ملاكي
                 */

                public int id;
                public double from_lat;
                public double from_lng;
                public Object to_lat;
                public Object to_lng;
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
                public Object commission;
                public double tax;
                public double discount;
                public int total_price;
                public int driver_rate;
                public int client_rate;
                public String driver_comment;
                public String client_comment;
                public String end_date;
                public String created_at;
                public ClientBeanX client;
                public int category_id;
                public String category_name;

                public static class ClientBeanX {
                    /**
                     * first_name : sssss
                     * last_name : zffff
                     * email : xxxx@yyyy.com
                     * mobile : 556321789
                     * country_id : 1
                     * country_code : 020
                     * currency : USD
                     * image : https://173.236.24.193/~nashmi/public/uploads/no_avatar.jpg
                     * activation_code : 0
                     * active : 1
                     * block : 0
                     * client_credit : 0
                     * payment_method : 2
                     * payment_method_text : credit
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
    }
}
