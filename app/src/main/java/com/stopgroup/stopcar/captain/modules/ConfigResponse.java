package com.stopgroup.stopcar.captain.modules;

import java.util.ArrayList;
/**
 * Created by Tarek on 7/17/18.
 */

public class ConfigResponse {


    public ResultModel result;
    public int statusCode;
    public String statusText;

    public static class ResultModel {
        public ConfigModel config;
        public ArrayList<String> sliders;
        public ArrayList<CountriesModel> countries;
        public ArrayList<CategoriesModel> categories;
        public ArrayList<ColorsModel> colors;
        public ArrayList<ColorsModel> cancel_reasons;
        public ArrayList<BrandsModel> brands;

        public static class ConfigModel {
            public int id;
            public String email;
            public String mobile;
            public String address;
            public String about_us;
            public String usage_rules;
            public String intro;
        }

        public static class CountriesModel {
            public int id;
            public String name;
            public String code;
            public String image;
            public CurrencyModel currency;

            public static class CurrencyModel {
                public int id;
                public Object name;
                public String symbol;
            }
        }

        public static class CategoriesModel {
            public int id;
            public String name;
            public String description;
            public String img;
            public boolean has_sub;
            public boolean has_level2;
            public boolean auto_accept;
            public boolean driver_register;
            public String calculating_pricing;
        }

        public static class ColorsModel {
            public int id;
            public String name;
            public String color;
        }

        public static class BrandsModel {
            public int id;
            public String name;
            public String image;
            public int category_id;
            public int sub_category_id;
            public String type;
        }
    }
}


