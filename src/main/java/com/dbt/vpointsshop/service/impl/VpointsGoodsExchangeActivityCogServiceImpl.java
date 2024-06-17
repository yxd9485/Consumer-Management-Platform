package com.dbt.vpointsshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeActivityCogEntity;
import com.dbt.vpointsshop.bean.VpointsGoodsExchangeRelationCogEntity;
import com.dbt.vpointsshop.dao.IVpointsGoodsExchangeActivityCogDao;
import com.dbt.vpointsshop.dao.IVpointsGoodsExchangeActivityRecordDao;
import com.dbt.vpointsshop.dao.IVpointsGoodsExchangeRelationCogDao;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityCogVO;
import com.dbt.vpointsshop.dto.VpointsGoodsExchangeActivityRecordVO;
import com.dbt.vpointsshop.service.ExchangeService;
import com.dbt.vpointsshop.service.IVpointsGoodsExchangeActivityCogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class VpointsGoodsExchangeActivityCogServiceImpl extends BaseService<VpointsGoodsExchangeActivityCogEntity> implements IVpointsGoodsExchangeActivityCogService {

    @Autowired
    private IVpointsGoodsExchangeActivityCogDao iVpointsGoodsExchangeActivityCogDao;
    @Autowired
    private IVpointsGoodsExchangeActivityRecordDao iVpointsGoodsExchangeActivityRecordDao;
    @Autowired
    private IVpointsGoodsExchangeRelationCogDao relationCogDao;
    @Autowired
    private ExchangeService exchangeService;
    @Override
    public String getRuleNo() {
        return this.getBussionNo("exchageActivity", "info_no", type_BJHD);
    }
    @Override
    public IPage<VpointsGoodsExchangeActivityCogVO> selectPageVO(IPage initPage, VpointsGoodsExchangeActivityCogEntity entity) {
        IPage<VpointsGoodsExchangeActivityCogEntity>  page = iVpointsGoodsExchangeActivityCogDao.selectPageVO(initPage, entity);
        IPage<VpointsGoodsExchangeActivityCogVO> resultPage = new Page<>();
        List<VpointsGoodsExchangeActivityCogVO> collect = page.getRecords().stream().map(ee -> {
            VpointsGoodsExchangeActivityCogVO vo = new VpointsGoodsExchangeActivityCogVO();
                BeanUtils.copyProperties(ee, vo);
                if(ee.getStartDate()!=null){
                    vo.setStartDate(DateUtil.getDateTime(ee.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
                }
                if(ee.getEndDate()!=null){
                    vo.setEndDate(DateUtil.getDateTime(ee.getEndDate(),DateUtil.DEFAULT_DATE_FORMAT));
                }
            return vo;
        }).collect(Collectors.toList());
        resultPage.setTotal(page.getTotal());
        resultPage.setCurrent(page.getCurrent());
        resultPage.setPages(page.getPages());
        resultPage.setSize(page.getSize());
        return resultPage.setRecords(collect);
    }

    @Override
    public boolean create(VpointsGoodsExchangeActivityCogVO vo) {
        String exchangeActivityInfoKey = callUUID().toString();
        VpointsGoodsExchangeActivityCogEntity entity = new VpointsGoodsExchangeActivityCogEntity();
        BeanUtils.copyProperties(vo,entity);
        try {
            entity.setStartDate(DateUtil.parse(vo.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
            entity.setEndDate(DateUtil.parse(vo.getEndDate(),DateUtil.DEFAULT_DATE_FORMAT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        entity.setCreateTime(DateUtil.getDateTime());
        entity.setUpdateTime(DateUtil.getDateTime());
        entity.setInfoKey(exchangeActivityInfoKey);
        entity.setInfoNo(getRuleNo());
        iVpointsGoodsExchangeActivityCogDao.insert(entity);
        if(vo.getGoodsIdList()!= null){
            int i = 0;
            for (String id : vo.getGoodsIdList()) {
                VpointsGoodsExchangeRelationCogEntity relationCogEntity = new VpointsGoodsExchangeRelationCogEntity();
                relationCogEntity.setGoodsId(id);
                relationCogEntity.setExchangeActivityInfoKey(exchangeActivityInfoKey);
                relationCogEntity.setExchangePrice(vo.getExchangePriceList().get(i));
                relationCogDao.insert(relationCogEntity);
                i++;
            }
        }
        String cacheStr = CacheUtilNew.cacheKey.discountExchangeActivity.exchangeActivity + Constant.DBTSPLIT
                + CacheUtilNew.cacheKey.discountExchangeActivity.getCurrentActivity;
        try {
            CacheUtilNew.removeGroupByKey(cacheStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    @Transactional
    public boolean update(VpointsGoodsExchangeActivityCogVO vo) throws Exception {
        VpointsGoodsExchangeActivityCogEntity entity = new VpointsGoodsExchangeActivityCogEntity();
        BeanUtils.copyProperties(vo,entity);
        entity.setStartDate(DateUtil.parse(vo.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
        entity.setEndDate(DateUtil.parse(vo.getEndDate(),DateUtil.DEFAULT_DATE_FORMAT));
        entity.setUpdateTime(DateUtil.getDateTime());
        iVpointsGoodsExchangeActivityCogDao.updateById(entity);
        if(vo.getGoodsIdList()!=null){
            relationCogDao.deleteByInfoKey(vo.getInfoKey());
            int i = 0;
            for (String id : vo.getGoodsIdList()) {
                VpointsGoodsExchangeRelationCogEntity relationCogEntity = new VpointsGoodsExchangeRelationCogEntity();
                relationCogEntity.setGoodsId(id);
                relationCogEntity.setExchangeActivityInfoKey(vo.getInfoKey());
                relationCogEntity.setExchangePrice(vo.getExchangePriceList().get(i));
                relationCogDao.insert(relationCogEntity);
                i++;
            }
        }
        String cacheStr = CacheUtilNew.cacheKey.discountExchangeActivity.exchangeActivity + Constant.DBTSPLIT
                + CacheUtilNew.cacheKey.discountExchangeActivity.getCurrentActivity;
        try {
            CacheUtilNew.removeGroupByKey(cacheStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public VpointsGoodsExchangeActivityCogVO findByInfoKey(String infoKey) {
        VpointsGoodsExchangeActivityCogEntity entity = iVpointsGoodsExchangeActivityCogDao.findByInfoKey(infoKey);
        VpointsGoodsExchangeActivityCogVO vo = new VpointsGoodsExchangeActivityCogVO();
        if(entity!=null){
            BeanUtils.copyProperties(entity, vo);
            List<VpointsGoodsExchangeRelationCogEntity> relationCogEntities = relationCogDao.selectByExchangeInfoKey(entity.getInfoKey());
            if (entity.getStartDate() != null) {
                vo.setStartDate(DateUtil.getDateTime(entity.getStartDate(), DateUtil.DEFAULT_DATE_FORMAT));
            }
            if (entity.getEndDate() != null) {
                vo.setEndDate(DateUtil.getDateTime(entity.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT));
            }
            vo.setGoodsIdList(relationCogEntities.stream().map(VpointsGoodsExchangeRelationCogEntity::getGoodsId).collect(Collectors.toList()));
            vo.setExchangePriceList(relationCogEntities.stream().map(VpointsGoodsExchangeRelationCogEntity::getExchangePrice).collect(Collectors.toList()));
        }
        return vo;
    }

    @Override
    public List<VpointsGoodsExchangeActivityCogEntity> getCurrentActivity() {
        return iVpointsGoodsExchangeActivityCogDao.getCurrentActivity(DateUtil.getDate());
    }

    @Override
    public BaseResult<Map<String, Object>> checkGoodsActivity(VpointsGoodsExchangeActivityCogVO vo) {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();

        List<VpointsGoodsExchangeActivityCogEntity> result = iVpointsGoodsExchangeActivityCogDao.checkGoodsActivity(vo);

        if (result.size() > 0) {
            String msg = result.stream().map(msgMap->{
                return "【"+msgMap.getGoodsName()+"】";
            }).collect(Collectors.joining(","));
            baseResult.initReslut(Constant.ResultCode.SUCCESS, Constant.ResultCode.SUCCESS, "商品"+msg+"与其他活动商品冲突");
        }else{
            baseResult.initReslut(Constant.ResultCode.FILURE, Constant.ResultCode.FILURE, "未冲突");
        }
        return baseResult;
    }

    @Override
    public IPage<VpointsGoodsExchangeActivityRecordVO> selectDataPageVO(IPage initPage, VpointsGoodsExchangeActivityRecordVO vo) {
        IPage<VpointsGoodsExchangeActivityRecordVO>  page = iVpointsGoodsExchangeActivityRecordDao.selectDataPageVO(initPage,vo);
        for (VpointsGoodsExchangeActivityRecordVO record : page.getRecords()) {
            initExchange(record.getExchangeLog());
            initExchange(record.getExchangeOrderLog());
        }
        return page;
    }

    @Override
    public List<VpointsGoodsExchangeActivityRecordVO> selectDataPageVO(VpointsGoodsExchangeActivityRecordVO vo) {
        IPage<VpointsGoodsExchangeActivityRecordVO> initPage = new Page<>();
        initPage.setSize(9999999);
        IPage<VpointsGoodsExchangeActivityRecordVO>  page = iVpointsGoodsExchangeActivityRecordDao.selectDataPageVO(initPage,vo);
        for (VpointsGoodsExchangeActivityRecordVO activityRecordVO : page.getRecords()) {
            initExchange(activityRecordVO.getExchangeLog());
            initExchange(activityRecordVO.getExchangeOrderLog());
        }
        return page.getRecords();
    }

    private void initExchange(VpointsExchangeLog item){
        item.setOrderStatus(exchangeService.transExchangeOrderStatus(item));
        //判断是否是礼品卡
        String isGiftCard = "1";
        if ((isGiftCard.equals(item.getIsGiftCard()) && StringUtils.isNotEmpty(item.getGiftCardExchangeId())) || item.getGiftCardPay()>0) {
            item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_3);
            item.setPayTypeName("礼品卡兑换," + item.getGiftCardExchangeId());
            if (item.getExchangeVpoints() == 0 && item.getExchangePay() > 0) {
                item.setPayTypeName("礼品卡兑换 + 现金支出");
            }
        } else {
            // 支付方式
            if (item.getExchangeVpoints() > 0 && item.getExchangePay() == 0) {
                item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_0);
                item.setPayTypeName("积分支出");
            } else if (item.getExchangeVpoints() == 0 && item.getExchangePay() > 0) {
                item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_1);
                item.setPayTypeName("现金支出");
            } else if (item.getExchangeVpoints() > 0 && item.getExchangePay() > 0) {
                item.setPayType(Constant.VPOINTS_PAY_TYPE.TYPE_2);
                item.setPayTypeName("混合支出");
            }
        }
        item.setExchangePayForExcel(MathUtil.round(item.getExchangePay() / 100D, 2));
        item.setDiscountsMoneyForExcel(MathUtil.round(item.getDiscountsMoney() / 100D, 2));
        item.setCouponDiscountPayForExcel(MathUtil.round(item.getCouponDiscountPay() / 100D, 2));
        exchangeService.pushOrderTypeName(item);
        if ("henanpz".equals(DbContextHolder.getDBType())
                && !item.getExchangeChannel().equals(Constant.exchangeChannel.CHANNEL_9)) {
            item.setUpdateTime(null);
        }
    }
}
