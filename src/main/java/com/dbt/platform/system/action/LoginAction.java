package com.dbt.platform.system.action;

import com.dbt.web.VjifenManageConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;


@Controller
public class LoginAction {
    @Autowired
    private VjifenManageConfig config;

    @RequestMapping("/")
    public String scanLogin(Model model, HttpServletRequest request) {
        try {
            model.addAttribute("appid", config.getAuthAppid());
            String redirectUri = SysLoginAction.getURI(request) + config.getAuthCallback();
            model.addAttribute("redirect_uri", redirectUri);
            model.addAttribute("state", RandomStringUtils.randomAlphanumeric(13));
            model.addAttribute("singleProjectServerName", config.getSingleProjectServerName());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "../login";
    }
}
