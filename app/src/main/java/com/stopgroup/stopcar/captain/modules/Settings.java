package com.stopgroup.stopcar.captain.modules;

import android.graphics.Bitmap;

import java.util.List;

public class Settings {


    /**
     * result : {"config":{"id":1,"email":"sasa","mobile":"asd","about_us":"asd","usage_rules":"asd"},"sliders":["https://173.236.24.193/~nashmi/storage/app/public/uploadsThum/ZwTlQGfDtJWMbthKZuNErgNl5jtAyiW6nLKDGK4e.jpeg"],"countries":[{"id":1,"name":"مصر","code":"020","image":null,"currency":{"id":1,"name":null,"symbol":"USD"}}],"categories":[{"id":1,"name":"سيارة ملاكي","img":null,"has_sub":false},{"id":2,"name":"شاحنة نقل","img":null,"has_sub":true},{"id":3,"name":"شاحنة نقل اثاث","img":null,"has_sub":false}],"brands":[{"id":1,"name":"BMW","image":null}],"colors":[{"id":1,"name":"black","color":"#00f600"}],"cancel_reasons":[{"id":1,"name":"غير مريح"}]}
     * statusCode : 200
     * statusText : OK
     */

    public ResultBean result;
    public int statusCode;
    public String statusText;

    public static class ResultBean {
        /**
         * config : {"id":1,"email":"sasa","mobile":"asd","about_us":"asd","usage_rules":"asd"}
         * sliders : ["https://173.236.24.193/~nashmi/storage/app/public/uploadsThum/ZwTlQGfDtJWMbthKZuNErgNl5jtAyiW6nLKDGK4e.jpeg"]
         * countries : [{"id":1,"name":"مصر","code":"020","image":null,"currency":{"id":1,"name":null,"symbol":"USD"}}]
         * categories : [{"id":1,"name":"سيارة ملاكي","img":null,"has_sub":false},{"id":2,"name":"شاحنة نقل","img":null,"has_sub":true},{"id":3,"name":"شاحنة نقل اثاث","img":null,"has_sub":false}]
         * brands : [{"id":1,"name":"BMW","image":null}]
         * colors : [{"id":1,"name":"black","color":"#00f600"}]
         * cancel_reasons : [{"id":1,"name":"غير مريح"}]
         */

        public ConfigBean config;
        public List<String> sliders;
        public List<CountriesBean> countries;
        public List<CategoriesBean> categories;
        public List<BrandsBean> brands;
        public List<ColorsBean> colors;
        public List<CancelReasonsBean> cancel_reasons;

        public static class ConfigBean {
            /**
             * id : 1
             * email : sasa
             * mobile : asd
             * about_us : asd
             * usage_rules : asd
             */

            public int id;
            public String email;
            public String mobile;
            public String about_us;
            public String usage_rules;
            public String address;
        }

        public static class CountriesBean {
            /**
             * id : 1
             * name : مصر
             * code : 020
             * image : null
             * currency : {"id":1,"name":null,"symbol":"USD"}
             */

            public int id;
            public String name;
            public String code;
            public String image;
            public CurrencyBean currency;

            public static class CurrencyBean {
                /**
                 * id : 1
                 * name : null
                 * symbol : USD
                 */

                public int id;
                public String name;
                public String symbol;
            }
        }

        public static class CategoriesBean {
            /**
             * id : 1
             * name : سيارة ملاكي
             * img : null
             * has_sub : false
             */

            public int id;
            public String calculating_pricing;
            public int attachment_id;
            public String name;
            public String img;
            public String description = "";
            public boolean has_sub;
            public boolean auto_accept;
            public Bitmap bitmap;
        }

        public static class BrandsBean {
            /**
             * id : 1
             * name : BMW
             * image : null
             */

            public int id;
            public String name;
            public String image;
        }

        public static class ColorsBean {
            /**
             * id : 1
             * name : black
             * color : #00f600
             */

            public int id;
            public String name;
            public String color;
        }

        public static class CancelReasonsBean {
            /**
             * id : 1
             * name : غير مريح
             */

            public int id;
            public String name;
        }
    }
}
