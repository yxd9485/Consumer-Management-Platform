package com.dbt.vpointsshop.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.StringUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.fission.bean.VpsVcodeActivatePrizeRecordEntity;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpsCombinationDiscountCog;
import com.dbt.vpointsshop.dao.IVpsCombinationDiscountCogDao;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountCogVO;
import com.dbt.vpointsshop.dto.VpsCombinationDiscountDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dbt.framework.base.bean.Constant.OrderNoType.type_ZHYH;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-04-07
 */
@Service
public class VpsCombinationDiscountCogServiceImpl extends BaseService<VpsCombinationDiscountCog> implements IVpsCombinationDiscountCogService {
    @Autowired
    private IVpsCombinationDiscountCogDao iVpsCombinationDiscountCogDao;
    @Override
    public void insert(VpsCombinationDiscountCogVO queryParam, SysUserBasis user) {

        VpsCombinationDiscountCog vpsCombinationDiscountCog = new VpsCombinationDiscountCog();
        BeanUtils.copyProperties(queryParam, vpsCombinationDiscountCog);
        try {
            if (StringUtils.isNotEmpty(queryParam.getStartTime())) {
                vpsCombinationDiscountCog.setStartTime(DateUtil.parse(queryParam.getStartTime(), DateUtil.DEFAULT_DATE_FORMAT));
            }
            if (StringUtils.isNotEmpty(queryParam.getEndTime())) {
                vpsCombinationDiscountCog.setEndTime(DateUtil.parse(DateUtil.getEndDateTimeByDay(queryParam.getEndTime()), DateUtil.DEFAULT_DATETIME_FORMAT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            vpsCombinationDiscountCog.setCogKey(UUIDTools.getInstance().getUUID());
            vpsCombinationDiscountCog.setCogNo(getBussionNo("VpsCombinationDiscountCog", "cog_no", type_ZHYH));
            vpsCombinationDiscountCog.setCreateUser(user.getUserKey());
            vpsCombinationDiscountCog.setCreateTime(new Date());
            vpsCombinationDiscountCog.setUpdateTime(new Date());
            vpsCombinationDiscountCog.setUpdateUser(user.getUserKey());
            iVpsCombinationDiscountCogDao.insert(vpsCombinationDiscountCog);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(VpsCombinationDiscountCogVO queryParam, SysUserBasis user) {
        VpsCombinationDiscountCog vpsCombinationDiscountCog = new VpsCombinationDiscountCog();
        BeanUtils.copyProperties(queryParam, vpsCombinationDiscountCog);
        try {
            if (StringUtils.isNotEmpty(queryParam.getStartTime())) {
                vpsCombinationDiscountCog.setStartTime(DateUtil.parse(queryParam.getStartTime(), DateUtil.DEFAULT_DATE_FORMAT));
            }
            if (StringUtils.isNotEmpty(queryParam.getEndTime())) {
                vpsCombinationDiscountCog.setEndTime(DateUtil.parse(DateUtil.getEndDateTimeByDay(queryParam.getEndTime()), DateUtil.DEFAULT_DATETIME_FORMAT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            vpsCombinationDiscountCog.setUpdateTime(new Date());
            vpsCombinationDiscountCog.setUpdateUser(user.getUserKey());
            iVpsCombinationDiscountCogDao.updateById(vpsCombinationDiscountCog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public VpsCombinationDiscountCogVO selectById(String cogKey) {
            VpsCombinationDiscountCogVO vo = new VpsCombinationDiscountCogVO();
        try {
            VpsCombinationDiscountCog vpsCombinationDiscountCog = iVpsCombinationDiscountCogDao.selectById(cogKey);
            BeanUtils.copyProperties(vpsCombinationDiscountCog, vo);
            vo.setStartTime(DateUtil.getDateTime(vpsCombinationDiscountCog.getStartTime(),DateUtil.DEFAULT_DATE_FORMAT));
            vo.setEndTime(DateUtil.getDateTime(vpsCombinationDiscountCog.getEndTime(),DateUtil.DEFAULT_DATE_FORMAT));
        }catch (Exception e){
            e.printStackTrace();
        }

        return vo;
    }

    @Override
    public  Page<VpsCombinationDiscountDTO> getList(VpsCombinationDiscountCogVO queryParam) {
        VpsCombinationDiscountDTO vpsCombinationDiscountCog = new VpsCombinationDiscountDTO();
        try {
            vpsCombinationDiscountCog.setName(StringUtils.isEmpty(queryParam.getName()) ? null : queryParam.getName());
            vpsCombinationDiscountCog.setCogNo(StringUtils.isEmpty(queryParam.getCogNo()) ? null : queryParam.getCogNo());
            vpsCombinationDiscountCog.setOpenFlag(StringUtils.isEmpty(queryParam.getOpenFlag()) ? null : queryParam.getOpenFlag());
            vpsCombinationDiscountCog.setGoodsA(StringUtils.isEmpty(queryParam.getGoodsName()) ? null : queryParam.getGoodsName());
            vpsCombinationDiscountCog.setStateFlag(StringUtils.isEmpty(queryParam.getStateFlag()) ? null : queryParam.getStateFlag());
            if (StringUtils.isNotEmpty(queryParam.getStartTime())) {
                vpsCombinationDiscountCog.setStartTime(DateUtil.parse(DateUtil.getStartDateTimeByDay(queryParam.getStartTime()), DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            if (StringUtils.isNotEmpty(queryParam.getEndTime())) {
                vpsCombinationDiscountCog.setEndTime(DateUtil.parse(DateUtil.getEndDateTimeByDay(queryParam.getEndTime()), DateUtil.DEFAULT_DATETIME_FORMAT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageOrderInfo pageOrderInfo = queryParam.getPageOrderInfo();
        IPage<VpsCombinationDiscountCogVO> page = new Page<>();
        page.setCurrent(pageOrderInfo.getCurrentPage());
        page.setPages(pageOrderInfo.getStartCount());
        page.setSize(pageOrderInfo.getPagePerCount());
        Page<VpsCombinationDiscountDTO> vv =  iVpsCombinationDiscountCogDao.selectPageVO(page, vpsCombinationDiscountCog);

        return vv;
    }

    @Override
    public List<VpsCombinationDiscountDTO> selectSetUpGoodsByOnline() {
        return  iVpsCombinationDiscountCogDao.selectSetUpGoodsByOnline();
    }

    @Override
    public  BaseResult<VpsCombinationDiscountDTO> checkGoodsInfo(VpsCombinationDiscountCogVO queryParam) {
        BaseResult<VpsCombinationDiscountDTO> baseResult = new BaseResult<>();
        if ("0".equals(queryParam.getOpenFlag())) {
            baseResult.initReslut(Constant.ResultCode.SUCCESS, Constant.ResultCode.FILURE, "没有冲突");
            return baseResult;
        }
        List<VpsCombinationDiscountDTO> cogVOList  = iVpsCombinationDiscountCogDao.checkGoodsInfo(queryParam);
        if (cogVOList != null && cogVOList.size() > 0) {
            for (VpsCombinationDiscountDTO cogVO : cogVOList) {
                baseResult.setReply(cogVO);
                baseResult.initReslut(Constant.ResultCode.SUCCESS,Constant.ResultCode.SUCCESS, cogVO.getName() +"（活动编码："+cogVO.getCogNo()+"） 活动商品与此活动有冲突");
                break;
            }
        }else{
            baseResult.initReslut(Constant.ResultCode.SUCCESS, Constant.ResultCode.FILURE, "没有冲突");
        }
        return  baseResult;
    }

    @Override
    public void delete(String cogKey) {
        iVpsCombinationDiscountCogDao.deleteById(cogKey);
    }
}
