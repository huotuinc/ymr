package com.huotu.ymr.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie操作
 * Created by lgh on 2015/12/18.
 */
public class CookieHelper {

    /**
     * @param response
     * @param name     cookie名称
     * @param value    cookie值
     * @param domain   域名
     * @param expiry   过期时间 秒
     */
    public static void set(HttpServletResponse response, String name, String value, String domain, int expiry) {
        //创建加密的用户cookie
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(expiry);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie的值
     *
     * @param request
     * @param name    cookie名称
     * @return
     */
    public static String get(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) return cookie.getValue();
            }
        }
        return null;
    }
}
