package com.geemeta.m.platform.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hongxueqian on 14-4-12.
 */
public class SecurityHelper {

    private static Logger logger = LoggerFactory.getLogger(SecurityHelper.class);

    public static ShiroDbRealm.ShiroUser getCurrentUser() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

        //开发环境中，若未登录可默认返回管理员，以便于可通过webstorm站点访问idea站点中的服务
//        String profile = System.gWetProperty("spring.profiles.active");
//        logger.debug("spring.profiles.active:{}", profile);
//        if (profile != null && profile.toLowerCase().equals("development")) {
//            if (user == null)
//                user = new ShiroDbRealm.ShiroUser(1L, "admin@devEvn", "管理员@开发环境");
//        }
        return user;
    }

    /**
     * 取出Shiro中的当前用户LoginName.
     */
    public static String getCurrentUserName() {
        ShiroDbRealm.ShiroUser user = getCurrentUser();
        return user == null ? null : user.loginName;
    }

    public static Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = getCurrentUser();
        return user == null ? null : user.id;
    }

    public static boolean hasRole(String roleIdentifier) {
        return SecurityUtils.getSubject().hasRole(roleIdentifier);
    }

    public static boolean isPermitted(String permission) {
        return SecurityUtils.getSubject().isPermitted(permission);
    }

    public static boolean isAuthenticatedForCurrentUser(){
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser == null) return false;
        else return currentUser.isAuthenticated();
    }
}
