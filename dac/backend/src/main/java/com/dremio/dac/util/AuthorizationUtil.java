package com.dremio.dac.util;

import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthorizationUtil{
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationUtil.class);

    public static boolean isAuthorized(String user, String action){
        Properties roleProps = new Properties();
        Properties privilegeProps = new Properties();

        try{
            logger.info("[Duy] user.dir: " + System.getProperty("user.dir"));
            roleProps.load(new FileInputStream(System.getProperty("user.dir") + "/roles.properties"));
            privilegeProps.load(new FileInputStream(System.getProperty("user.dir") + "/privileges.properties"));
            String roleStr = roleProps.getProperty(user.toUpperCase().trim()).trim();
            if(roleStr == null || roleStr.equals("")){
                logger.info("[Duy] No roles assigned to user " + user.toUpperCase().trim());
                return false;
            }

            String[] roles = roleStr.split(",");
            for(int i=0; i<roles.length; i++){
                String privileges = privilegeProps.getProperty(roles[i].toUpperCase().trim());
                if(privileges.toUpperCase().contains(action.toUpperCase().trim())){
                    logger.info("[Duy] user " + user.toUpperCase().trim() + " can do " + action.toUpperCase().trim());
                    return true;
                }
            }
        }
        catch(Exception ex){
            logger.info("[Duy] AuthorizationUtil.isAuthorized() failed with error: " + ex.toString());            
            return false;
        }
        logger.info("[Duy] user " + user.toUpperCase().trim() + " is not authorized to do " + action.toUpperCase().trim());
        return false;
    }
}