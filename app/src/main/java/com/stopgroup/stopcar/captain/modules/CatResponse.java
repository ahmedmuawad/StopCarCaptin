package com.stopgroup.stopcar.captain.modules;

import java.util.List;

public class CatResponse {


    /**
     * result : {"config":{"id":1,"email":"sasa","mobile":"asd","about_us":"asd","usage_rules":"asd"},"sliders":["https://173.236.24.193/~nashmi/storage/app/public/uploadsThum/ZwTlQGfDtJWMbthKZuNErgNl5jtAyiW6nLKDGK4e.jpeg"],"countries":[{"id":1,"name":"مصر","code":"020","image":null,"currency":{"id":1,"name":null,"symbol":"USD"}}],"categories":[{"id":1,"name":"سيارة ملاكي","img":null,"has_sub":false},{"id":2,"name":"شاحنة نقل","img":null,"has_sub":true},{"id":3,"name":"شاحنة نقل اثاث","img":null,"has_sub":false}],"brands":[{"id":1,"name":"BMW","image":null}],"colors":[{"id":1,"name":"black","color":"#00f600"}],"cancel_reasons":[{"id":1,"name":"غير مريح"}]}
     * statusCode : 200
     * statusText : OK
     */

    public List<Settings.ResultBean.CategoriesBean> result;
    public int statusCode;
    public String statusText;

}
