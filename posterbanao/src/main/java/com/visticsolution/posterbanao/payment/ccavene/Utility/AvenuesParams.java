package com.visticsolution.posterbanao.payment.ccavene.Utility;


import com.visticsolution.posterbanao.classes.Constants;

public class AvenuesParams {
    public static final String COMMAND = "command";
    public static final String ACCESS_CODE = "access_code";
    public static final String MERCHANT_ID = "merchant_id";
    public static final String ORDER_ID = "order_id";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String ENC_VAL = "enc_val";
    public static final String REDIRECT_URL = "redirect_url";
    public static final String CANCEL_URL = "cancel_url";
    public static final String RSA_KEY_URL = "rsa_key_url";

    public static final String currency = "INR";
    public static final String redirect_url = Constants.BASE_URL+"ccavenue/success.php";
    public static final String cancel_url = Constants.BASE_URL+"ccavenue/cancel.php";
    public static final String rsa_key_url = Constants.BASE_URL+"ccavenue/GetRSA.php";
}
