package com.baidu.disconf.web.service.log.service;

import java.util.Map;


/**
 * 
 * @author GaoJean
 *
 */
public interface AllOpertaerMgr {
    
    /**
     * 更新操作日志
     * @param map
     * @return
     */
    void updateLog(Map<String, Object> map);

}
