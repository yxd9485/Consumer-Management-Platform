package com.dbt.vpointsshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.fission.bean.VpsVcodeActivateRedEnvelopeRuleCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsHalfPriceActivityCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsHalfPriceRelationCogEntity;
import com.dbt.vpointsshop.dao.IVpointsGoodsHalfPriceActivityCogDao;
import com.dbt.vpointsshop.dao.IVpointsGoodsHalfPriceRelationCogDao;
import com.dbt.vpointsshop.dto.VpointsGoodsHalfPriceActivityCogVO;
import com.dbt.vpointsshop.service.IVpointsGoodsHalfPriceActivityCogService;
import com.dbt.vpointsshop.service.IVpointsGoodsHalfPriceRelationCogService;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.dbt.framework.base.bean.Constant.OrderNoType.type_BJHD;

/**
 * <p>
 * 商城第二件半价活动 服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
@Service
public class VpointsGoodsHalfPriceActivityCogServiceImpl extends BaseService<VpointsGoodsHalfPriceActivityCogEntity> implements IVpointsGoodsHalfPriceActivityCogService {

    @Autowired
    private IVpointsGoodsHalfPriceActivityCogDao iVpointsGoodsHalfPriceActivityCogDao;
    @Autowired
    private IVpointsGoodsHalfPriceRelationCogDao relationCogDao;
    @Override
    public String getRuleNo() {
        return this.getBussionNo("halfPrice", "info_no", type_BJHD);
    }
    @Override
    public IPage<VpointsGoodsHalfPriceActivityCogVO> selectPageVO(IPage initPage, VpointsGoodsHalfPriceActivityCogEntity entity) {
        IPage<VpointsGoodsHalfPriceActivityCogEntity>  page = iVpointsGoodsHalfPriceActivityCogDao.selectPageVO(initPage, entity);
        IPage<VpointsGoodsHalfPriceActivityCogVO> resultPage = new Page<>();
        List<VpointsGoodsHalfPriceActivityCogVO> collect = page.getRecords().stream().map(ee -> {
            VpointsGoodsHalfPriceActivityCogVO vo = new VpointsGoodsHalfPriceActivityCogVO();
                BeanUtils.copyProperties(ee, vo);
                vo.setStartDate(DateUtil.getDateTime(ee.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
                vo.setEndDate(DateUtil.getDateTime(ee.getEndDate(),DateUtil.DEFAULT_DATE_FORMAT));
            return vo;
        }).collect(Collectors.toList());
        resultPage.setTotal(page.getTotal());
        resultPage.setCurrent(page.getCurrent());
        resultPage.setPages(page.getPages());
        resultPage.setSize(page.getSize());
        return resultPage.setRecords(collect);
    }

    @Override
    public boolean create(VpointsGoodsHalfPriceActivityCogVO vo) {
        String halfPriceInfoKey = callUUID().toString();
        VpointsGoodsHalfPriceActivityCogEntity entity = new VpointsGoodsHalfPriceActivityCogEntity();
        BeanUtils.copyProperties(vo,entity);
        try {
            entity.setStartDate(DateUtil.parse(vo.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
            entity.setEndDate(DateUtil.parse(vo.getEndDate(),DateUtil.DEFAULT_DATE_FORMAT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        entity.setCreateTime(DateUtil.getDateTime());
        entity.setUpdateTime(DateUtil.getDateTime());
        entity.setInfoKey(halfPriceInfoKey);
        entity.setInfoNo(getRuleNo());
        iVpointsGoodsHalfPriceActivityCogDao.insert(entity);
        if(vo.getGoodsIdList()!= null){
            for (String id : vo.getGoodsIdList()) {
                VpointsGoodsHalfPriceRelationCogEntity relationCogEntity = new VpointsGoodsHalfPriceRelationCogEntity();
                relationCogEntity.setGoodsId(id);
                relationCogEntity.setHalfPriceInfoKey(halfPriceInfoKey);
                relationCogDao.insert(relationCogEntity);
            }
        }
        String cacheStr = CacheUtilNew.cacheKey.discountExchangeActivity.halfPriceActivity + Constant.DBTSPLIT
                + CacheUtilNew.cacheKey.discountExchangeActivity.getCurrentActivity;
        try {
            CacheUtilNew.removeGroupByKey(cacheStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public boolean update(VpointsGoodsHalfPriceActivityCogVO vo) {
        VpointsGoodsHalfPriceActivityCogEntity entity = new VpointsGoodsHalfPriceActivityCogEntity();
        BeanUtils.copyProperties(vo,entity);
        try {
            entity.setStartDate(DateUtil.parse(vo.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
            entity.setEndDate(DateUtil.parse(vo.getEndDate(),DateUtil.DEFAULT_DATE_FORMAT));
            entity.setUpdateTime(DateUtil.getDateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        iVpointsGoodsHalfPriceActivityCogDao.updateByInfoKey(entity);
        if(vo.getGoodsIdList()!=null){
            relationCogDao.deleteByInfoKey(vo.getInfoKey());
            for (String id : vo.getGoodsIdList()) {
                VpointsGoodsHalfPriceRelationCogEntity relationCogEntity = new VpointsGoodsHalfPriceRelationCogEntity();
                relationCogEntity.setGoodsId(id);
                relationCogEntity.setHalfPriceInfoKey(vo.getInfoKey());
                relationCogDao.insert(relationCogEntity);
            }
        }
        String cacheStr = CacheUtilNew.cacheKey.discountExchangeActivity.halfPriceActivity + Constant.DBTSPLIT
                + CacheUtilNew.cacheKey.discountExchangeActivity.getCurrentActivity;
        try {
            CacheUtilNew.removeGroupByKey(cacheStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public VpointsGoodsHalfPriceActivityCogVO findByInfoKey(String infoKey) {
        VpointsGoodsHalfPriceActivityCogEntity entity = iVpointsGoodsHalfPriceActivityCogDao.findByInfoKey(infoKey);
        VpointsGoodsHalfPriceActivityCogVO vo = new VpointsGoodsHalfPriceActivityCogVO();
        if(entity!=null){
            BeanUtils.copyProperties(entity, vo);
            List<String> relationCogEntities = relationCogDao.selectByHalfPriceInfoKey(entity.getInfoKey());
            vo.setStartDate(DateUtil.getDateTime(entity.getStartDate(), DateUtil.DEFAULT_DATE_FORMAT));
            vo.setEndDate(DateUtil.getDateTime(entity.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT));
            vo.setGoodsIdList(relationCogEntities);
        }
        return vo;
    }

    @Override
    public List<VpointsGoodsHalfPriceActivityCogVO> getCurrentActivity() {
        return iVpointsGoodsHalfPriceActivityCogDao.getCurrentActivity(DateUtil.getDate());
    }

    @Override
    public BaseResult<Map<String, Object>> checkGoodsActivity(VpointsGoodsHalfPriceActivityCogVO vo) {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();

        List<VpointsGoodsHalfPriceActivityCogVO> result = iVpointsGoodsHalfPriceActivityCogDao.checkGoodsActivity(vo);

        if (result.size() > 0) {
            String msg = result.stream().map(msgMap->{
                return "【"+msgMap.getGoodsName()+"】";
            }).collect(Collectors.joining(","));
            baseResult.initReslut(Constant.ResultCode.SUCCESS, Constant.ResultCode.SUCCESS, "商品" + msg + "与其他活动商品冲突");
        } else {
            baseResult.initReslut(Constant.ResultCode.FILURE, Constant.ResultCode.FILURE, "未冲突");
        }
        return baseResult;
    }

    @Override
    public List<VpointsGoodsHalfPriceActivityCogVO> selectPageVO(VpointsGoodsHalfPriceActivityCogEntity entity) {
        return null;
    }
}
