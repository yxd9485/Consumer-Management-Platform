package com.dbt.vpointsshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.framework.base.action.reply.BaseResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardCog;
import com.dbt.vpointsshop.bean.VpointsGoodsGiftCardRelation;
import com.dbt.vpointsshop.bean.VpointsGoodsHalfPriceActivityCogEntity;
import com.dbt.vpointsshop.dao.IVpointsGoodsGiftCardCogDao;
import com.dbt.vpointsshop.dto.VpointsGoodsGiftCardCogVO;
import com.dbt.vpointsshop.dto.VpointsGoodsHalfPriceActivityCogVO;
import com.dbt.vpointsshop.service.IVpointsGoodsGiftCardCogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbt.vpointsshop.service.IVpointsGoodsGiftCardRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dbt.framework.base.bean.Constant.OrderNoType.type_BJHD;
import static com.dbt.framework.base.bean.Constant.OrderNoType.type_GF;

/**
 * <p>
 * 礼品卡活动表 服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Service
public class VpointsGoodsGiftCardCogServiceImpl extends BaseService<VpointsGoodsGiftCardCog> implements IVpointsGoodsGiftCardCogService {

    @Autowired
    private IVpointsGoodsGiftCardRelationService iVpointsGoodsGiftCardRelationService;
    @Autowired
    private IVpointsGoodsGiftCardCogDao iVpointsGoodsGiftCardCogDao;

    @Override
    public String getRuleNo() {
        return this.getBussionNo("giftCard", "info_no", type_GF);
    }

    @Override
    public IPage<VpointsGoodsGiftCardCogVO> selectPageVO(IPage initPage, VpointsGoodsGiftCardCog entity) {
        IPage<VpointsGoodsGiftCardCogVO> page = iVpointsGoodsGiftCardCogDao.selectPageVO(initPage,entity);
        return page;
    }

    @Override
    public boolean create(VpointsGoodsGiftCardCogVO vo) {
        VpointsGoodsGiftCardCog cog = new VpointsGoodsGiftCardCog();
        BeanUtils.copyProperties(vo, cog);
        String infoKey = UUIDTools.getInstance().getUUID();
        cog.setInfoKey(infoKey);
        cog.setDeleteFlag("0");
        cog.setInfoNo(getRuleNo());
        cog.setCreateTime(DateUtil.getNow());
        cog.setUpdateTime(DateUtil.getNow());
        int insert = iVpointsGoodsGiftCardCogDao.insert(cog);
        List<String> goodsIdList = vo.getGoodsIdList();
        for (String goodsId : goodsIdList) {
            VpointsGoodsGiftCardRelation relation = new VpointsGoodsGiftCardRelation();
            relation.setGiftCardInfoKey(infoKey);
            relation.setGoodsId(goodsId);
            iVpointsGoodsGiftCardRelationService.save(relation);
        }
        return insert > 0;
    }

    @Override
    public VpointsGoodsGiftCardCogVO findByInfoKey(String infoKey) {
        VpointsGoodsGiftCardCog vpointsGoodsGiftCardCog = iVpointsGoodsGiftCardCogDao.selectById(infoKey);
        VpointsGoodsGiftCardRelation relation = new VpointsGoodsGiftCardRelation();
        relation.setGiftCardInfoKey(vpointsGoodsGiftCardCog.getInfoKey());
        List<VpointsGoodsGiftCardRelation> list = iVpointsGoodsGiftCardRelationService.list(new QueryWrapper<>(relation));
        VpointsGoodsGiftCardCogVO vpointsGoodsGiftCardCogVO = new VpointsGoodsGiftCardCogVO();
        BeanUtils.copyProperties(vpointsGoodsGiftCardCog, vpointsGoodsGiftCardCogVO);
        vpointsGoodsGiftCardCogVO.setGoodsIdList(list.stream().map(VpointsGoodsGiftCardRelation::getGoodsId).collect(Collectors.toList()));
        return vpointsGoodsGiftCardCogVO;
    }

    @Override
    public boolean update(VpointsGoodsGiftCardCogVO vo) {
        VpointsGoodsGiftCardCog cog = new VpointsGoodsGiftCardCog();
        BeanUtils.copyProperties(vo, cog);
        cog.setUpdateTime(DateUtil.getNow());
        int insert = iVpointsGoodsGiftCardCogDao.update(cog);
        List<String> goodsIdList = vo.getGoodsIdList();
        if(goodsIdList!=null){
            iVpointsGoodsGiftCardRelationService.deleteByGiftCardInfoKey(cog.getInfoKey());
            for (String goodsId : goodsIdList) {
                VpointsGoodsGiftCardRelation relation = new VpointsGoodsGiftCardRelation();
                relation.setGiftCardInfoKey(cog.getInfoKey());
                relation.setGoodsId(goodsId);
                iVpointsGoodsGiftCardRelationService.save(relation);
            }
        }
        return insert > 0;
    }

    @Override
    public BaseResult<Map<String, Object>> checkGoodsActivity(VpointsGoodsGiftCardCogVO vo) {
        BaseResult<Map<String, Object>> baseResult = new BaseResult<>();

        List<VpointsGoodsGiftCardCogVO> result = iVpointsGoodsGiftCardCogDao.checkGoodsActivity(vo);

        if (result.size() > 0) {
            String msg = result.stream().map(msgMap->{
                return "【"+msgMap.getGoodsName()+"】";
            }).collect(Collectors.joining(","));
            baseResult.initReslut(Constant.ResultCode.SUCCESS, Constant.ResultCode.SUCCESS, "商品" + msg + "与【"+result.get(0).getGiftCardName()+"】活动商品冲突");
        } else {
            baseResult.initReslut(Constant.ResultCode.FILURE, Constant.ResultCode.FILURE, "未冲突");
        }
        return baseResult;
    }

    @Override
    public List<VpointsGoodsGiftCardCogVO> getCurrentActivity() {
        return iVpointsGoodsGiftCardCogDao.getCurrentActivity();
    }
}
