package com.handsome.qhb.listener;

/**
 * Created by zhang on 2016/4/20.
 * 网络请求返回接口
 */
public interface MyListener  {

    void dataController(String response,int tag);
    void requestError(String error);

}
