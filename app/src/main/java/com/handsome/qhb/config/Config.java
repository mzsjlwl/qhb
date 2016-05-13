package com.handsome.qhb.config;

public class Config {


    public static final String APP_NAME="桦商城";
    public static final String BASE_URL = "http://192.168.111.106/QHB/";
    public static final String ORDER_TAG = "order";
    public static final String ACCESSID = "2100189658";
    public static final String ACCESSIDIOS = "2200195440";
    public static final String SECRETKEY = "92319602671d73ebab46ff60537e3754";
    public static final String SECRETKEYIOS = "0e8d5682dc81ab5a2dbd2b211895a389";

    public static final int SLIDER_PICTURE = 1;
    public static final int INIT_SLIDER_PICTURE = 2;
    public static final int INIT_PRODUCT = 3;
    public static final int REFERSH_PRODUCT = 4;
    public static final int INITROOM_MESSAGE = 5;

    public static final int CHAT_MESSAGE = 6;
    public static final int RANDOMBONUS_MESSAGE = 7;
    public static final int CDSBONUS_MESSAGE = 8;
    public static final int ADD_MESSAGE=9;
    public static final int ROOM_REFRESH_LASTMESSAGE = 10;
    public static final int DSPERSION_MESSAGE = 11;


    public static final int DS_RESULT = 13;
    public static final int CDS_TIME = 15;
    public static final int REFRESH_USER = 16;
    public static final int ORDER_MESSAGE = 17;
    public static final int REFRESH_ORDER = 18;
    public static final int LOADMORE_ORDER = 19;


    public static final String XG_PUSH_URL = "http://openapi.xg.qq.com/v2/push/";


    public static final int TYPE_MESSAGE = 2;

    public static final int TYPE_TEXT=1;
    public static final int TYPE_RANDOMBONUS=2;
    public static final int TYPE_CDSBONUS=3;
    public static final int TYPE_CANCELBONUS=4;
    public static final int TYPE_DSRESULT = 5;
    public static final int TYPE_DSPERSION = 6;

    public static final int STATE_CDSBONUS_START = 1;
    public static final int STATE_CDSBONUS_GUESSED = 2;
    public static final int STATE_CDSBONUS_END = 3;

    public static final String GETROOM_URL = "Room/sendRoomPHP";
    public static final String ENTERROOM_URL = "Room/enterRoom";
    public static final String GETSLIDER_URL = "Slider/getJson";
    public static final String GETPRODUCT_URL = "Product/getJson";
    public static final String USERNOTICE_URL = "User/notice";
    public static final String DSCDS_URL = "DS/cds";
    public static final String INSERTORDER_URL = "Order/insert";
    public static final String GETORDER_URL = "Order/getJson";
    public static final String USERUPDATE_URL= "User/update";
    public static final String USERUPDATEPHOTO_URL = "User/updatePhoto";
    public static final String USERINFO_URL = "User/getInfo";
    public static final String DSGETRESULT_URL = "DS/getResult";
    public static final String UPDATEVERSION_URL = "Download/updateAPP";
    public static final String DOWNLOADAPP_URL = "Download/downloadAPP";

    public static final int GETROOM_TAG = 1;
    public static final int GETSLIDER_TAG = 2;
    public static final int GETPRODUCT_TAG = 3;
    public static final int USERLOGIN_TAG = 4;
    public static final int USERNOTICE_TAG = 5;
    public static final int DSCDS_TAG = 6;
    public static final int INSERTORDER_TAG = 7;
    public static final int GETORDER_TAG = 8;
    public static final int USERREGISTER_TAG=9;
    public static final int USERUPDATE_TAG= 10;
    public static final int REFRESHPRODUCT_TAG = 11;
    public static final int LOADMOREPRODUCT_TAG = 12;
    public static final int USERINFO_TAG = 13;
    public static final int SENDMSG_TAG = 14;
    public static final int REFRESHORDER_TAG = 16;
    public static final int LOADMOREORDER_TAG = 17;
    public static final int UPDATEDATA_TAG = 18;
    public static final int UPDATEPASSWORD_TAG = 19;
    public static final int UPDATEPHOTO_TAG = 20;
    public static final int ENTERROOM_TAG = 21;
    public static final int DSGETMYGUESS_TAG = 23;
    public static final int DSGETPERSONNUM_TAG = 24;

}
