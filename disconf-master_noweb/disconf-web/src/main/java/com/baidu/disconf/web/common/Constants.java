package com.baidu.disconf.web.common;

/**
 * Created by knightliao on 15/12/25.
 */
public class Constants {

    public final static Integer STATUS_NORMAL    = 1;
    public final static Integer STATUS_DELETE    = 0;

    /**
     * 发送redmine的地址<br>
     * 
     * 
     * add by GaoJean  on 10/10/2016
     */
    public final static String  Redmine_URL      = "http://182.92.230.201:3000/issues/6369.json";
    /**
     * 登录redmine的用户key<br>
     * 
     * 
     * add by GaoJean  on 10/10/2016
     */
    public final static String  Redmine_User_KEY = "c2a6e7e9bd8e1b3f8cdabe8e64f92c510006efb4";

    /**
     * 操作日志  删除
     */
    public final static String  Delete           = "1";
    /**
     * 操作日志  新增
     */
    public final static String  Add              = "2";
    /**
     * 操作日志  更新
     */
    public final static String  Update           = "3";
}
