package com.alibaba.cloud.examples;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description:
 */

@RestController
public class LoginController {


    @Autowired
    AuthorizationServerEndpointsConfiguration endpoints;

    /**



    /**
     * 获取当前登录用户信息-SSO单点登录
     *
     * @param principal
     * @return
     */
    @GetMapping("/user")
    public OpenUserDetails principal(Principal principal) {
         OpenUserDetails openUserDetails = new OpenUserDetails();
         openUserDetails.setUsername(principal.getName());
         return openUserDetails;
    }
    
    
    @GetMapping("/sayHello")
    public String sayHello(String  name) {
        return "hello "+name;
    }
 

   

}
