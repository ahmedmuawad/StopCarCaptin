package com.stopgroup.stopcar.captain.modules;

import java.util.List;

/**
 * Created by Tarek on 12/24/18.
 */

public class RequestPaymentModel {

    /**
     * result : [{"id":18,"admin_credit":6,"driver_credit":0,"driver_id":266,"type_text":"Cash to driver","type":"0","finish":true}]
     * statusCode : 200
     * statusText : OK
     */

    public int statusCode;
    public String statusText;
    public List<ResultBean> result;

    public static class ResultBean {
        /**
         * id : 18
         * admin_credit : 6
         * driver_credit : 0
         * driver_id : 266
         * type_text : Cash to driver
         * type : 0
         * finish : true
         */

        public int id;
        public int admin_credit;
        public int driver_credit;
        public int driver_id;
        public String type_text;
        public String type;
        public boolean finish;
    }
}
