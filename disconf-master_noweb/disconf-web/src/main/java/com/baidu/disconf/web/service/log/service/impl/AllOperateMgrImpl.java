package com.baidu.disconf.web.service.log.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.disconf.web.common.Constants;
import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.dao.AppDao;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.dao.EnvDao;
import com.baidu.disconf.web.service.log.bo.Log;
import com.baidu.disconf.web.service.log.dao.LogHistoryDao;
import com.baidu.disconf.web.service.log.service.AllOpertaerMgr;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.baidu.disconf.web.utils.redmine.RedmineService;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.github.knightliao.apollo.utils.time.DateUtils;

public class AllOperateMgrImpl implements AllOpertaerMgr {
    @Autowired
    private ConfigDao      configDao;

    @Autowired
    private AppDao         appDao;

    @Autowired
    private EnvDao         envDao;
    @Autowired
    private UserDao        userDao;

    @Autowired
    private LogHistoryDao  logHistoryDao;
    @Autowired
    private RedmineService redmineService;

    @Override
    public void updateLog(Map<String, Object> map) {

        String updateTime = DateUtils.format(new Date(), DataFormatConstants.STAND_TIME_FORMAT);
        String desc = "";
        String active = "";
        long appId = (Long) map.get("appId");
        long envId = (Long) map.get("envId");
        long userId = (Long) map.get("userId");
        String version = (String) map.get("version");
        String operation = (String) map.get("operation");

        App app = getAppById(appId);
        User user = getUser(userId);
        Env env = getEnv(envId);
        Config config = getConfig(appId, envId, version);
        if (operation.equals(Constants.Add)) {
            active = "新增";
        } else if (operation.equals(Constants.Delete)) {
            desc = updateTime + " ," + user.getName() + "删除 " + env.getName() + "环境里中的微服务： "
                   + app.getName();
        } else if (operation.equals(Constants.Update)) {
            active = "更新";
        }

        Log log = new Log();
        log.setAppId(appId);
        log.setEnvId(envId);
        log.setUserId(userId);
        log.setVersion(version);
        log.setDesc(desc);
        log.setOperation(operation);
        log.setUpdateTime(updateTime);

        Log logResult = logHistoryDao.create(log);
        if (logResult != null) {
            try {
                redmineService.send2Redmine(desc);//操作日志更新到redmine
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得app
     * @param appId
     * @return
     */
    public App getAppById(long appId) {
        return appDao.get(appId);
    }

    /**
     * 获得config
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    public Config getConfig(long appId, long envId, String version) {
        return configDao.getByParameter(appId, envId, version);
    }

    /**
     * 获得用户
     * @param userId
     * @return
     */
    public User getUser(long userId) {
        return userDao.get(userId);
    }

    /**
     * 获得环境
     * @param envId
     * @return
     */
    public Env getEnv(long envId) {
        return envDao.get(envId);
    }
}
