package com.stopgroup.stopcar.captain.modules;

public class Login {

    /**
     * token_type : Bearer
     * expires_in : 129599999999
     * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImUzZDUyNGQ5Nzg1YTYzZjFkYWM4YmI0MjVhMjVjZjc5Y2Y2MGMwYzZhMDU5NzdkNDQ3ZjQ3MzZhODgxYzY3OTBlYTBmYTI4M2RkNzMxNGQ4In0.eyJhdWQiOiIyIiwianRpIjoiZTNkNTI0ZDk3ODVhNjNmMWRhYzhiYjQyNWEyNWNmNzljZjYwYzBjNmEwNTk3N2Q0NDdmNDczNmE4ODFjNjc5MGVhMGZhMjgzZGQ3MzE0ZDgiLCJpYXQiOjE1MzUzMTAwOTAsIm5iZiI6MTUzNTMxMDA5MCwiZXhwIjoxMzExMzUzMTAwODksInN1YiI6IjU3Iiwic2NvcGVzIjpbXX0.UwrfyXZqfWSEv0TEpcGNPdv0Z5oCjkI1v6cRvgph-ql5v_0uM3ypTchiu8iD3ABLAdPkzfGzYGVCzSqnZEq8hZYK0cY7-PAfjGooQuWnmFykrKDHIqHkO5jitkX-RLo9dqeosWwUVoPukd2HN4fpA37YeWvpFp0f-U7EWGNwSAMe65g8xgYlQ4BnV1TbFKkq2c1ZhoWTJbwnv3ckTDtf6HX9wXCMcSl4sLmmxgLp9lo9opQo6HVb0eHk3t7OOGUwAs3tYpOUXNVxIl7R-dKPxm_Uv6CLSzXbB3RGTtO9PhnCQw3Hm2O1oQRcVdqi8zdAdQuBEaAu621I5m5qmzkaScMTbebY-UCkyqm4TkRnWx1WohtPHF7YNdFuI1iwzKYas4yeMbEm2EhcjALpoFF2dEJcvxKBUmH4mWQ8JIxFDZKO7E6ac2bVE13y9PzmbHWbkMxym26z1Pef_oM6doCapkO8sA6NnJUzpJY0SSokXxq9NxbCMkA9qwP1BrAOyKOn2dHR0G26O8cJvmrKbvHOPQo0GxHd9daVOpyO4wvZJsplszObKNn4KT9CNswOc0g7N3u5xKgbuNMPCTP7zYGIecZrq8p9jKeEU-EvvJoemiGZl1hL3KEyCqvBVzlY3in79M06KmTiEIjMVyXlEGngfJjm8Ual3LoPw8RnvHERN1Y
     * refresh_token : def50200198076f300299177e87a823c78144acde21c7066132c3e92dbb7d758f3469c1ee4f41f05e92307e14304dc65ee7e84447a486e94ef0c05f6fccda7d26544f3a44bebda436f203b6199aef5e9f2a7a16083edbe352ea89b9c0c2ae30987782e0a49f2922546e8035a61beaba82b0287c04a361e8d377ab8fa56477d02842e5d8eb1bbda85ededaa1979feb20daaff677b7cd89c53373cce57b95867f95e95b30260eb95e349f11edf5ba28f059ea1f09fc612f35f6fd66f05c9c95c3991a4447137a100045529fd58e7e43297d9b002ad9427ea049e789e736ddb4f2bfc5b2f1172102d1e0ee5705157e97af4907ed8dfbcf2d4c918a23f769c0601cd30d86e3715a37f5770d4f4917160a157559b25d592c1c18f999b18ccb9640238aaf08852f33f19757fad889c1653e53d79947a5b23733aadfcfe08e9d4144aecfa2bbd243a434235100277c52c3c29c7cc2a5813140f5b39ba7987905045eecd48
     * result : {"first_name":"Ahmed","last_name":"Driver","email":"driver@driver.com","mobile":"1013799885","country_id":1,"country_code":"00966","currency":"SAR","image":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-26-06-34-12-7057115-40b5e7170f1ec49d12d128d503df7150.jpeg","activation_code":0,"active":"1","block":"0","driver":{"id":33,"driving_license_img":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-07-07-27-16-4281899-fe4eb83813ae212d3e6da8d8acc49d7c.png","car_license_img":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-07-07-27-17-7362476-3aa7a0965c4eedc9a19c47091b5cc22c.png","car":{"id":18,"year":2015,"brand_id":1,"brand_name":"بي إم دبليو","model_id":1,"model_name":"218i","color_id":1,"color_name":"black","color":"#00f600","car_image":"/storage/app/public/uploads/2018-08-26-07-03-27-6247507-9880e004f40ae8a056115e6dbf8c4114.jpeg"},"online":true,"lat":"30.0001534","lng":"31.1743167","his_credit":0,"admin_credit":1536,"total_credit":1608,"rate":"5.0000","category_id":1,"category_name":"سيارة خصوصي","category_image":"https://173.236.24.193/~nashmi/storage/app/public/uploadsThum/acnu405eeYRINcoLzGoP2TlM9W6t4rR3LeXgYOoq.jpeg","statistics":{"today_trips":0,"today_earned":0,"online_hours":13}}}
     * statusCode : 200
     * statusText : OK
     */

    public String token_type;
    public long expires_in;
    public String access_token;
    public String refresh_token;
    public ResultBean result;
    public int statusCode;
    public String statusText;

    public static class ResultBean {
        /**
         * first_name : Ahmed
         * last_name : Driver
         * email : driver@driver.com
         * mobile : 1013799885
         * country_id : 1
         * country_code : 00966
         * currency : SAR
         * image : https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-26-06-34-12-7057115-40b5e7170f1ec49d12d128d503df7150.jpeg

         * active : 1
         * block : 0
         * driver : {"id":33,"driving_license_img":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-07-07-27-16-4281899-fe4eb83813ae212d3e6da8d8acc49d7c.png","car_license_img":"https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-07-07-27-17-7362476-3aa7a0965c4eedc9a19c47091b5cc22c.png","car":{"id":18,"year":2015,"brand_id":1,"brand_name":"بي إم دبليو","model_id":1,"model_name":"218i","color_id":1,"color_name":"black","color":"#00f600","car_image":"/storage/app/public/uploads/2018-08-26-07-03-27-6247507-9880e004f40ae8a056115e6dbf8c4114.jpeg"},"online":true,"lat":"30.0001534","lng":"31.1743167","his_credit":0,"admin_credit":1536,"total_credit":1608,"rate":"5.0000","category_id":1,"category_name":"سيارة خصوصي","category_image":"https://173.236.24.193/~nashmi/storage/app/public/uploadsThum/acnu405eeYRINcoLzGoP2TlM9W6t4rR3LeXgYOoq.jpeg","statistics":{"today_trips":0,"today_earned":0,"online_hours":13}}
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
        public DriverBean driver;

        public static class DriverBean {
            /**
             * id : 33
             * driving_license_img : https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-07-07-27-16-4281899-fe4eb83813ae212d3e6da8d8acc49d7c.png
             * car_license_img : https://173.236.24.193/~nashmi/storage/app/public/uploads/2018-08-07-07-27-17-7362476-3aa7a0965c4eedc9a19c47091b5cc22c.png
             * car : {"id":18,"year":2015,"brand_id":1,"brand_name":"بي إم دبليو","model_id":1,"model_name":"218i","color_id":1,"color_name":"black","color":"#00f600","car_image":"/storage/app/public/uploads/2018-08-26-07-03-27-6247507-9880e004f40ae8a056115e6dbf8c4114.jpeg"}
             * online : true
             * lat : 30.0001534
             * lng : 31.1743167
             * his_credit : 0
             * admin_credit : 1536
             * total_credit : 1608
             * rate : 5.0000
             * category_id : 1
             * category_name : سيارة خصوصي
             * category_image : https://173.236.24.193/~nashmi/storage/app/public/uploadsThum/acnu405eeYRINcoLzGoP2TlM9W6t4rR3LeXgYOoq.jpeg
             * statistics : {"today_trips":0,"today_earned":0,"online_hours":13}
             */

            public int id;
            public String driving_license_img;
            public String car_license_img;
            public CarBean car;
            public boolean online = false;
            public String lat;
            public String lng;
            public double his_credit;
            public double admin_credit;
            public double total_credit;
            public String rate;
            public int category_id;
            public String category_name;
            public String category_calculating_pricing;
            public String category_image;
            public StatisticsBean statistics;

            public static class CarBean {
                /**
                 * id : 18
                 * year : 2015
                 * brand_id : 1
                 * brand_name : بي إم دبليو
                 * model_id : 1
                 * model_name : 218i
                 * color_id : 1
                 * color_name : black
                 * color : #00f600
                 * car_image : /storage/app/public/uploads/2018-08-26-07-03-27-6247507-9880e004f40ae8a056115e6dbf8c4114.jpeg
                 */

                public int id;
                public int year;
                public int brand_id;
                public String brand_name;
                public int model_id;
                public String model_name;
                public int color_id;
                public String color_name;
                public String color;
                public String car_image;
            }

            public static class StatisticsBean {
                /**
                 * today_trips : 0
                 * today_earned : 0
                 * online_hours : 13
                 */

                public int today_trips;
                public int today_earned;
                public int online_hours;
            }
        }
    }
}
