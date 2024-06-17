package com.dbt.platform.publish.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.publish.bean.VpsActVideo;
import com.dbt.platform.publish.dao.IVpsActVideoDao;
import com.dbt.platform.publish.vo.VpsActVideoVO;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 规则表 服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-04-11
 */
@Service
public class VpsActVideoService {
    @Autowired
    private IVpsActVideoDao iVpsActVideoDao;
    @Autowired
    private CRMServiceServiceImpl crmService;

    public IPage<VpsActVideo> queryForLst(VpsActVideoVO queryBean, PageOrderInfo pageInfo) {
        IPage<VpsActVideo> page = new Page<>();
        page.setCurrent(pageInfo.getCurrentPage());
        page.setPages(pageInfo.getStartCount());
        page.setSize(pageInfo.getPagePerCount());
        VpsActVideo vpsActVideo = new VpsActVideo();
        try {
            if(StringUtils.isNotEmpty(queryBean.getKeyword())){
                vpsActVideo.setTitle(queryBean.getKeyword());
            }
            if(StringUtils.isNotEmpty(queryBean.getStatus())){
                vpsActVideo.setStatus(queryBean.getStatus());
            }
            if(StringUtils.isNotEmpty(queryBean.getStartTime())){
                vpsActVideo.setStartTime(DateUtil.parse(DateUtil.getStartDateTimeByDay(queryBean.getStartTime()),DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            if(StringUtils.isNotEmpty(queryBean.getEndTime())){
                vpsActVideo.setEndTime(DateUtil.parse(DateUtil.getEndDateTimeByDay(queryBean.getEndTime()),DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            if(StringUtils.isNotEmpty(queryBean.getModStGmt())){
                vpsActVideo.setModStGmt(DateUtil.parse(DateUtil.getStartDateTimeByDay(queryBean.getModStGmt()),DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            if(StringUtils.isNotEmpty(queryBean.getModEndGmt())){
                vpsActVideo.setModEndGmt(DateUtil.parse(DateUtil.getEndDateTimeByDay(queryBean.getModEndGmt()),DateUtil.DEFAULT_DATETIME_FORMAT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryWrapper<VpsActVideo> vpsActVideoQueryWrapper = new QueryWrapper<>(vpsActVideo);
        IPage<VpsActVideo> vpsActVideoIPage = iVpsActVideoDao.selectPageVO(page, vpsActVideo);

        return vpsActVideoIPage;
    }

    public void updateVpsActVideo(VpsActVideoVO vpsActRule) {
    }
    public void initCrmGroup(Model model) throws NoSuchAlgorithmException {
        String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.FILTER_SWITCH_SETTING, DatadicKey.filterSwitchSetting.SWITCH_GROUP);
        if(DatadicUtil.isSwitchON(groupSwitch)) {
            model.addAttribute("groupList", crmService.queryVcodeActivityCrmGroup());
        }
        model.addAttribute("groupSwitch", DatadicUtil.isSwitchON(groupSwitch) ? "1" : "0");
    }

    public void insert(VpsActVideoVO vpsActVideoVO, SysUserBasis currentUser) {
        VpsActVideo vpsActVideo = new VpsActVideo();
        BeanUtils.copyProperties(vpsActVideoVO, vpsActVideo);
        try {
            vpsActVideo.setStartTime(DateUtil.parse(vpsActVideoVO.getStartTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsActVideo.setEndTime(DateUtil.parse(vpsActVideoVO.getEndTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsActVideo.setCreateTime(DateUtil.getNow());
            vpsActVideo.setCreateUser(currentUser.getUserName());
            vpsActVideo.setUpdateTime(DateUtil.getNow());
            vpsActVideo.setUpdateUser(currentUser.getUserName());
            iVpsActVideoDao.insert(vpsActVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VpsActVideoVO findById(String actRuleKey) {
        VpsActVideo vpsActVideo = iVpsActVideoDao.selectById(actRuleKey);
        VpsActVideoVO vo = new VpsActVideoVO();
        BeanUtils.copyProperties(vpsActVideo,vo);
        try {
            vo.setStartTime(DateUtil.getDateTime(vpsActVideo.getStartTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
            vo.setEndTime(DateUtil.getDateTime(vpsActVideo.getEndTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
        }catch (Exception e){
            e.printStackTrace();
        }
        return vo;
    }

    public void update(VpsActVideoVO vpsActVideoVO, SysUserBasis currentUser) {
        VpsActVideo vpsActVideo = new VpsActVideo();
        BeanUtils.copyProperties(vpsActVideoVO, vpsActVideo);
        try {
            vpsActVideo.setStartTime(DateUtil.parse(vpsActVideoVO.getStartTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsActVideo.setEndTime(DateUtil.parse(vpsActVideoVO.getEndTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsActVideo.setUpdateTime(DateUtil.getNow());
            vpsActVideo.setUpdateUser(currentUser.getUserName());
            iVpsActVideoDao.updateById(vpsActVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String videoKey) {
        iVpsActVideoDao.deleteById(videoKey);
    }

    public void updateVpsActVideoShow(VpsActVideoVO vpsActVideoVO) {
        iVpsActVideoDao.updateVpsActVideoShow(vpsActVideoVO);
        iVpsActVideoDao.updateVpsActVideoShowOther(vpsActVideoVO);
    }
    public void updateVpsActVideoNoShow(VpsActVideoVO vpsActVideoVO) {
        iVpsActVideoDao.updateVpsActVideoShow(vpsActVideoVO);
    }
}
