package com.dbt.platform.job;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.UrlToMultipartFile;
import com.dbt.framework.util.WechatUtil;
import com.dbt.vpointsshop.bean.VpsOrderComment;
import com.dbt.vpointsshop.service.VpsOrderCommentService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Service("CheckImgByWechatJob")
public class CheckImgByWechatJob {
    @Autowired
    private VpsOrderCommentService vpsOrderCommentService;
    /**
     * 检测图片是否合格
     * @throws InterruptedException
     */
    public void checkImg() throws Exception {
        // 获取job执行的省区
        DbContextHolder.clearDBType();
        Set<String> nameList = null;
        String projectServerNames = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.PROJECT_JOB,
                DatadicKey.ProjectJob.CHECK_IMG);
        if(org.apache.commons.lang.StringUtils.isBlank(projectServerNames)) return;

        if(!"ALL".equals(projectServerNames)){
            nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
        }else{
            nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
                    .getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
        }
        // 循环执行任务
        for (String projectServerName : nameList) {
            this.checkImg(DateUtil.getDateTime(DateUtil.addDays(-1), "yyyy-MM-dd"), projectServerName);
            Thread.sleep(500);
        }
    }
    @RequestMapping("/checkImg")
    private void checkImg(String expireTime, String projectServerName) throws Exception {
        DbContextHolder.setDBType(projectServerName);
        List<VpsOrderComment> comments = vpsOrderCommentService.queryCommentImg(expireTime);
        if(CollectionUtils.isEmpty(comments)){
            return;
        }
        for (VpsOrderComment c : comments){
            if(StringUtils.isBlank(c.getImageUrl())){
                continue;
            }
            List<String> imgs = Arrays.asList(c.getImageUrl().split(","));
            List<String> imgResult = new ArrayList<>();
            for (String i : imgs){
                Boolean checkResult = WechatUtil.checkPic(UrlToMultipartFile.urlToMultipartFile(i));
                if(checkResult){
                    imgResult.add(i);
                }
            }
            if(imgResult.size() != imgs.size()){
                String imgString = StringUtils.join(imgResult, ",");
                c.setImageUrl(imgString);
                vpsOrderCommentService.updateImg(c);
            }
        }
    }
}
