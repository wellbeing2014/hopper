package org.zxp.funk.hopper.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

/**
 * @ClassName: WebUtil
 * @Description: request帮助类，在controller中装配，可以使用
 * @author: 朱新培 zxp@wisoft.com.cn
 * @date: 2017年8月23日 下午3:10:30
 */
@Component
public class WebUtil {

    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getClientIp() {

    	String ip = request.getHeader("X-Real-IP");
        if (!Strings.isNullOrEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!Strings.isNullOrEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

}
