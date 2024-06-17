package com.dbt.platform.mn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.mn.bean.TerminalRankActivityAreaCog;
import com.dbt.platform.mn.bean.TerminalRankActivityCog;
import com.dbt.platform.mn.dao.ITerminalRankActivityCogDao;
import com.dbt.platform.mn.dto.TerminalRankActivityCogVO;
import com.dbt.platform.mn.service.ITerminalRankActivityAreaCogService;
import com.dbt.platform.mn.service.ITerminalRankActivityCogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2023-07-17
 */
@Service
public class TerminalRankActivityCogServiceImpl extends ServiceImpl<ITerminalRankActivityCogDao, TerminalRankActivityCog> implements ITerminalRankActivityCogService {
    @Autowired
    private ITerminalRankActivityAreaCogService terminalRankActivityAreaCogService;
    @Override
    public IPage<TerminalRankActivityCogVO> queryPageVo(PageOrderInfo pageInfo, TerminalRankActivityCogVO queryBean) {
        IPage<TerminalRankActivityCog> page = pageInfo.initPage();
        IPage<TerminalRankActivityCogVO> iPage = this.baseMapper.selectPageVO(page, queryBean);
        return iPage;
    }

    @Override
    public void saveRank(TerminalRankActivityCogVO cogVO) {
        TerminalRankActivityCog entity = new TerminalRankActivityCog();
        entity.setInfoKey(UUIDTools.getInstance().getUUID());
        entity.setActivityName(cogVO.getActivityName());
        entity.setActivityType(cogVO.getActivityType());
        entity.setActivityRole(cogVO.getActivityRole());
        entity.setActivityBannerUrl("https://img.vjifen.com/images/vma/"+cogVO.getActivityBannerUrl());
        entity.setInitBannerUrl("https://img.vjifen.com/images/vma/"+cogVO.getInitBannerUrl());
        entity.setRuleContent(cogVO.getRuleContent());
        entity.setSkuKeys(cogVO.getSkuKeys());
        entity.setCascadeFlag(cogVO.getCascadeFlag());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateUser(cogVO.getCreateUser());
        entity.setUpdateUser(cogVO.getUpdateUser());
        entity.setDeleteFlag("1");
        try {
            entity.setStartTime(DateUtil.parse(cogVO.getStartTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
            entity.setEndTime(DateUtil.parse(cogVO.getEndTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        cogVO.setInfoKey(entity.getInfoKey());
        List<TerminalRankActivityAreaCog> areaCogList = initAreaCogVO(cogVO);
        this.baseMapper.insert(entity);
        terminalRankActivityAreaCogService.saveBatch(areaCogList);
    }

    @Override
    public TerminalRankActivityCogVO showRankEdit(String infoKey) {
        TerminalRankActivityCog pi = this.baseMapper.selectById(infoKey);
        TerminalRankActivityCogVO vo = new TerminalRankActivityCogVO();
        vo.setActivityName(pi.getActivityName());
        vo.setActivityRole(pi.getActivityRole());
        vo.setActivityType(pi.getActivityType());
        vo.setInfoKey(pi.getInfoKey());
        vo.setActivityName(pi.getActivityName());
        vo.setActivityType(pi.getActivityType());
        vo.setActivityRole(pi.getActivityRole());
        vo.setActivityBannerUrl(pi.getActivityBannerUrl());
        vo.setInitBannerUrl(pi.getInitBannerUrl());
        vo.setRuleContent(pi.getRuleContent());
        vo.setSkuKeys(pi.getSkuKeys());
        vo.setCascadeFlag(pi.getCascadeFlag());
        if(pi.getStartTime()!=null){
            vo.setStartTime(DateUtil.getDateTime(pi.getStartTime(),DateUtil.DEFAULT_DATETIME_FORMAT));
        }
        if(pi.getEndTime()!=null){
            vo.setEndTime(DateUtil.getDateTime(pi.getEndTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
        }
        vo.setCreateUser(pi.getCreateUser());
        if(pi.getCreateTime()!=null){
            vo.setCreateTime(DateUtil.getDateTime(pi.getCreateTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
        }
        if(pi.getUpdateTime()!=null){
            vo.setUpdateTime(DateUtil.getDateTime(pi.getCreateTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
        }
        if (DateUtil.diffSecond(new Date(), pi.getStartTime()) < 0) {
            vo.setIsBegin("0");
        }else{
            if(pi.getEndTime()!=null){
                if(DateUtil.diffSecond(pi.getEndTime(),new Date()) < 0){
                    vo.setIsBegin("2");
                }else{
                    vo.setIsBegin("1");
                }
            }

        }
        return vo;
    }

    @Override
    public void saveUpdate(TerminalRankActivityCogVO cogVO) {
        TerminalRankActivityCog entity = new TerminalRankActivityCog();
        entity.setInfoKey(cogVO.getInfoKey());
        entity.setActivityName(cogVO.getActivityName());
        if(cogVO.getActivityBannerUrl().indexOf("https://img.vjifen.com/images/vma/")<0){
            entity.setActivityBannerUrl("https://img.vjifen.com/images/vma/"+cogVO.getActivityBannerUrl());
        }else{
            entity.setActivityBannerUrl(cogVO.getActivityBannerUrl());
        }
        if(cogVO.getInitBannerUrl().indexOf("https://img.vjifen.com/images/vma/")<0){
            entity.setInitBannerUrl("https://img.vjifen.com/images/vma/"+cogVO.getInitBannerUrl());
        }else{
            entity.setInitBannerUrl(cogVO.getInitBannerUrl());
        }
        entity.setRuleContent(cogVO.getRuleContent());
        entity.setActivityType(cogVO.getActivityType());
        entity.setActivityRole(cogVO.getActivityRole());
        entity.setCascadeFlag(cogVO.getCascadeFlag());
        entity.setSkuKeys(cogVO.getSkuKeys());
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(cogVO.getUpdateUser());
        entity.setDeleteFlag("1");
        try {
            if(StringUtils.isNotEmpty(cogVO.getStartTime())){
                entity.setStartTime(DateUtil.parse(cogVO.getStartTime(), DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            entity.setEndTime(DateUtil.parse(cogVO.getEndTime(), DateUtil.DEFAULT_DATETIME_FORMAT));

        List<TerminalRankActivityAreaCog> areaCogList = initAreaCogVO(cogVO);
        this.baseMapper.updateById(entity);
        if(StringUtils.isNotEmpty(cogVO.getStartTime()) && DateUtil.diffSecond(new Date(),DateUtil.parse(cogVO.getStartTime(), DateUtil.DEFAULT_DATETIME_FORMAT)) < 0){
            if(areaCogList.size()>0){
                this.terminalRankActivityAreaCogService.remove(new QueryWrapper<TerminalRankActivityAreaCog>().eq(TerminalRankActivityAreaCog.TERMINAL_RANK_INFO_KEY,cogVO.getInfoKey()));
                this.terminalRankActivityAreaCogService.saveBatch(areaCogList);
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<TerminalRankActivityAreaCog> initAreaCogVO(TerminalRankActivityCogVO cogVO){
        List<TerminalRankActivityAreaCog> areaCogList = new ArrayList<>();
        //1 行政区域
        if ("1".equals(cogVO.getActivityType())) {
            if (StringUtils.isNotEmpty(cogVO.getActivityAreaName())) {
                String[] activityAreaNameArray = cogVO.getActivityAreaName().split(";");
                String[] activityAreaArray = cogVO.getActivityArea().split(";");
                for (int i = 0; i < activityAreaNameArray.length; i++) {
                    TerminalRankActivityAreaCog addAreaCog = new TerminalRankActivityAreaCog();
                    addAreaCog.setTerminalRankInfoKey(cogVO.getInfoKey());
                    String[] name = activityAreaNameArray[i].split("-");
                    String[] code = activityAreaArray[i].split("-");
                    for (int j = 0; j < name.length; j++) {
                        switch (j) {
                            case 0:
                                addAreaCog.setProvince(code[j]);
                                addAreaCog.setProvinceName(name[j]);
                                break;
                            case 1:
                                addAreaCog.setCity(code[j]);
                                addAreaCog.setCityName(name[j]);
                                break;
                            case 2:
                                addAreaCog.setCounty(code[j]);
                                addAreaCog.setCountyName(name[j]);
                                break;
                            default:
                                break;
                        }
                        areaCogList.add(addAreaCog);
                    }
                }
            }
        }else{
            if (StringUtils.isNotEmpty(cogVO.getDepartmentName())) {
                String[] nameArray = cogVO.getDepartmentName().split(";");
                String[] codeArray = cogVO.getDepartment().split(";");
                for (int i = 0; i < nameArray.length; i++) {
                    TerminalRankActivityAreaCog addAreaCog = new TerminalRankActivityAreaCog();
                    addAreaCog.setTerminalRankInfoKey(cogVO.getInfoKey());
                    String[] name = nameArray[i].split("-");
                    String[] code = codeArray[i].split("-");
                    for (int j = 0; j < name.length; j++) {
                        switch (j) {
                            case 0:
                                addAreaCog.setLevelCode2(code[j]);
                                addAreaCog.setLevelName2(name[j]);
                                break;
                            case 1:
                                addAreaCog.setLevelCode3(code[j]);
                                addAreaCog.setLevelName3(name[j]);
                                break;
                            case 2:
                                addAreaCog.setLevelCode4(code[j]);
                                addAreaCog.setLevelName4(name[j]);
                                break;
                            case 3:
                                addAreaCog.setLevelCode5(code[j]);
                                addAreaCog.setLevelName5(name[j]);
                                break;
                            default:
                                break;
                        }
                    }
                    areaCogList.add(addAreaCog);
                }
            }
        }
        return areaCogList;
    }
}
