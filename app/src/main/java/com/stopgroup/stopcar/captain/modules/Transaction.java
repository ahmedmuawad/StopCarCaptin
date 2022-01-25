package com.stopgroup.stopcar.captain.modules;

import java.util.List;

public class Transaction {

    /**
     * result : [{"id":78,"admin_credit":165,"driver_credit":0,"driver_id":266,"type_text":"Cash to admin","type":"1","finish":true,"created_at":"2019-01-04 18:04:11"},{"id":65,"admin_credit":165,"driver_credit":0,"driver_id":266,"type_text":"Cash to driver","type":"0","finish":true,"created_at":"2019-01-04 17:20:59"},{"id":64,"admin_credit":165,"driver_credit":0,"driver_id":266,"type_text":"Cash to driver","type":"0","finish":true,"created_at":"2019-01-04 17:17:49"},{"id":63,"admin_credit":7,"driver_credit":0,"driver_id":266,"type_text":"Cash to driver","type":"0","finish":true,"created_at":"2019-01-04 17:09:20"},{"id":20,"admin_credit":15,"driver_credit":0,"driver_id":266,"type_text":"Cash to driver","type":"0","finish":true,"created_at":"2018-12-25 15:19:05"},{"id":18,"admin_credit":6,"driver_credit":0,"driver_id":266,"type_text":"Cash to driver","type":"0","finish":true,"created_at":"2018-12-24 15:09:18"}]
     * statusCode : 200
     * statusText : OK
     */

    public int statusCode;
    public String statusText;
    public List<ResultBean> result;

    public static class ResultBean {
        /**
         * id : 78
         * admin_credit : 165
         * driver_credit : 0
         * driver_id : 266
         * type_text : Cash to admin
         * type : 1
         * finish : true
         * created_at : 2019-01-04 18:04:11
         */

        public int id;
        public int admin_credit;
        public int driver_credit;
        public int driver_id;
        public String type_text;
        public String type;
        public boolean finish;
        public String created_at;
    }
}
