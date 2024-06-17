package com.dbt.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.platform.job.UpdateActivityVpointsCogJob;

//注册异常错误类型显示页面
@Component
public class DefaultErrorPageRegistrar implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(
                new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.do"),
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.do")
        );
    }

    //错误页
    @Controller
    @RequestMapping("/error")
    public static class ErrorPageController {
        @RequestMapping("/{code}")
        public String e404(@PathVariable String code) {
            return "../" + code;
        }

        @Autowired
        private UpdateActivityVpointsCogJob job;

        @RequestMapping("/abc")
        public String abc(Model model) {
            try {
                job.updateBathWaitActivityVpointsCog();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("aa");
//            RedisApiUtil.getInstance().addSet("abc-test1", "aaa");
//            Set<String> cacheKeySet = RedisApiUtil.getInstance().getSet("abc-test1");
//            System.out.println("aa");
//            try {
//                System.out.println(RedisUtils.get().STRING.get("abc"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//            String factoryKey = CacheKey.cacheKey.company.KEY_COMPANY_FACTORY_INFO;
//
//            String set = "redis.call('del', KEYS[1])";
//            RedisApiUtil.getInstance().evalEx(set, 1, factoryKey);





//            Map<String, String> d = Maps.newHashMap();
//            d.put("a", "a");
//            d.put("b", "b");
//            RedisApiUtil.getInstance().setObject(false, "keyProjectServerInfo1113:", d);
//
//            Map<String, String> dd = (Map<String, String>) RedisApiUtil.getInstance().getObject(false, "keyProjectServerInfo1113:");

            return "hello";
        }
    }
}
