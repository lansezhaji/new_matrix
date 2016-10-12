package com.baidu.disconf.web.service.log.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.utils.redmine.RedmineService;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.ub.common.commons.ThreadContext;
import com.github.knightliao.apollo.utils.time.DateUtils;

@Service
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

        try {
            Visitor visitor = ThreadContext.getSessionVisitor();
            String updateTime = DateUtils.format(new Date(), DataFormatConstants.STAND_TIME_FORMAT);
            String desc = "";
            long appId = (Long) (map.get("appId") == null ? null : map.get("appId"));
            long envId = (Long) (map.get("envId") == null ? null : map.get("envId"));
            String version = (String) (map.get("version") == null ? null : map.get("version"));
            String operation = (String) (map.get("operation") == null ? null : map.get("operation"));

            App app = getAppById(appId);
            Env env = getEnv(envId);
            //Config config = getConfig(appId, envId, version);
            if (operation.equals(Constants.Add)) {//复制
                long oldAppId = (Long) (map.get("oldAppId") == null ? null : map.get("oldAppId"));
                String oldVersion = (String) (map.get("oldVersion") == null ? null : map
                    .get("oldVersion"));
                App oldApp = getAppById(oldAppId);

                desc = updateTime + " ," + visitor.getLoginUserName() + "从 " + env.getName()
                       + "环境 " + oldApp.getName() + "微服务" + oldVersion + "版本，复制到了" + env.getName()
                       + "环境" + app.getName() + "微服务" + version + "版本";
            } else if (operation.equals(Constants.Delete)) {
                //deleteInfo(map);
                desc = updateTime + " ," + visitor.getLoginUserName() + "delete " + env.getName()
                       + "app:" + app.getName();
            } else if (operation.equals(Constants.Update)) {

                //active = "更新";
            }

            Log log = new Log();
            log.setAppId(appId);
            log.setEnvId(envId);
            log.setUserId(visitor.getLoginUserId());
            log.setVersion(version);
            log.setDescription(desc);
            log.setOperation(operation);
            log.setUpdateTime(updateTime);

            Log logResult = logHistoryDao.create(log);
            if (logResult != null) {
                redmineService.send2Redmine(desc);//操作日志更新到redmine
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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

    public void deleteInfo(Map<String, Object> map) {

    }
}
