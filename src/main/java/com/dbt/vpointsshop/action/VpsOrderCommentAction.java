package com.dbt.vpointsshop.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpsOrderComment;
import com.dbt.vpointsshop.dao.IVpointsCouponCogDao;
import com.dbt.vpointsshop.service.VpsOrderCommentService;

/**
 * 订单评论Action
 */
@Controller
@RequestMapping("/commentAction")
public class VpsOrderCommentAction extends BaseAction{

	@Autowired
	private VpsOrderCommentService vpsOrderCommentService;
	@Autowired
	private IVpointsCouponCogDao vpointsCouponDao;
	
	/**
	 * 获取订单评论列表
	 */
	@RequestMapping("/showCommentList")
	public String showCommentList(HttpSession session, String pageParam, String queryParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);	
			VpsOrderComment queryBean = new VpsOrderComment(queryParam);

			List<VpsOrderComment> resultList = vpsOrderCommentService.queryForLst(queryBean, pageInfo);
			for(VpsOrderComment c : resultList){
				if(StringUtils.isNotBlank(c.getOrderContent())){
					c.setOrderContent(URLDecoder.decode(c.getOrderContent(), "utf-8"));
				}
				if(StringUtils.isNotBlank(c.getUserName())){
					c.setUserName(URLDecoder.decode(c.getUserName(), "utf-8"));
				}
			}
			int countResult = vpsOrderCommentService.queryForCount(queryBean);

			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("orderCol", pageInfo.getOrderCol());
			model.addAttribute("orderType", pageInfo.getOrderType());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			log.error(ex);
		}
		return "vpointsGoods/showCommentList";
	}
	
	
	/**
     * 评论详情
     */
	@RequestMapping("/show")
	public String edit(HttpSession session,int isShow, String commentId, Model model) throws UnsupportedEncodingException {
		VpsOrderComment comment =  vpsOrderCommentService.findById(commentId);
		String contentString = StringUtils.isNotBlank(comment.getContent()) ? URLDecoder.decode(comment.getContent(), "utf-8") : "";
		comment.setContent(contentString);
		comment.setUserName(StringUtils.isNotBlank(comment.getUserName()) ? URLDecoder.decode(comment.getUserName(), "utf-8") : "");
		model.addAttribute("comment", comment);
		model.addAttribute("couponLst", vpointsCouponDao.queryAllCouponCog());
		return isShow == 0 ? "vpointsGoods/showCommentDetails" :"vpointsGoods/orderCommentDetails";
	}
	

	@RequestMapping("/update")
	public String update(Model model,HttpSession session,VpsOrderComment bean){
		try {
			SysUserBasis user=getUserBasis(session);
			bean.fillFields(user.getUserKey());
			vpsOrderCommentService.update(bean);
			model.addAttribute("errMsg", "操作成功");
		} catch (Exception e) {
			model.addAttribute("errMsg", "操作失败");
			e.printStackTrace();
		}
		return "forward:showCommentList.do";
	}
	
	
	@RequestMapping("/del")
	public String del(Model model,HttpSession session,String commentId){
		try {
			vpsOrderCommentService.del(commentId);
			model.addAttribute("errMsg", "删除成功");
		} catch (Exception e) {
			model.addAttribute("errMsg", "删除失败");
			e.printStackTrace();
		}
		return "forward:showCommentList.do";
	}
	
}
