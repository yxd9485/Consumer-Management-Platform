<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String cpath = request.getContextPath(); 
	String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
	String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
	String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title></title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	<script>
	var basePath='<%=cpath%>';
	var allPath='<%=allPath%>';
	var imgSrc=[];
		$(function(){
			// 返回
            $(".btnReturn").click(function(){
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });
			// 初始化功能
			initPage();
			
		});
		
		function initPage() {		
			var imageUrlLst='${comment.imageUrl}';
			if(imageUrlLst == ""){
				return 
			}else{
	         //拆分
	 		var checkArray =imageUrlLst.split(","); 
	 		for(var i=0;i<checkArray.length;i++){
	 				if(i == 0){
	 					$("[name='pice']").removeAttr("style");
						$("#picUrl").attr("src",src=checkArray[i]);	
	 				}
	 				if(i == 1){
	 					$("[name='pice1']").removeAttr("style");
	 					$("#picUrl1").attr("src",src=checkArray[i]);	
	 				}
	 				if(i == 2){
						$("[name='pice2']").removeAttr("style");
						$("#picUrl2").attr("src",src=checkArray[i]);
	 				}
	 						
	        	 }				
			}
         
		}
		
		

	</script>
	
	<style>
	
	
	
		.white {
			color: white;
		}
		.blocker {
			float: left;
			vertical-align: middle;
			margin-right: 8px;
			margin-top: 8px;
		}
		.en-larger {
			margin-left: 8px;
		}
		.en-larger2 {
			margin-left: 12px;
		}
		.ex-larger {
			margin-right: 8px;
		}
		.show-sku-name {
			float: left;
			margin-left: 8px;
			margin-top: 8px;
		}
		.top-only {
			border-top: 1px solid #e1e1e1;
		}
		.tab-radio {
			margin: 10px 0 0 !important;
		}
		.validate_tips {
			padding: 8px !important;
		}
		.mx-right {
			float: right;
			margin-top: 2px;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 积分商场</a></li>
            <li class="current"><a>评论管理</a></li>
            <li class="current"><a>查看</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
            	<div class="widget box">
            		<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>基础信息</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
							<input type="hidden" name="userKey" value= "${comment.userKey}"/>
        					<input type="hidden" name="commentId" value= "${comment.commentId}"/>
                			<input type="hidden" name="isDisplay" value= "${comment.isDisplay}"/>
                			<input type="hidden" name="isTop" value= "${comment.isTop}"/>
                			<tr>
	                       		<td class="ab_left"><label class="title">用户头像：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<img src="${comment.headimgUrl}" width=200 height=200 /> 	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户手机号：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.phoneNum}</span>                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	
	                       	<tr>
	                       		<td class="ab_left"><label class="title">订单号：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.exchangeId}</span>                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	
                			<tr>
	                       		<td class="ab_left"><label class="title mart5">用户昵称：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.userName}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title mart5">购买商品：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.goodsName}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title mart5">规格：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.goodsSpecification}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title mart5">日期：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.createTime}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
							<c:if test="${comment.commentType eq '2'}">
	                       	<tr>
	                       		<td class="ab_left"><label class="title mart5">关联活动：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.activityName}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title mart5">参与擂台：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>
	                       					<c:choose>
	                       						<c:when test="${comment.activityType eq '1'}">日擂台</c:when>
	                       						<c:when test="${comment.activityType eq '2'}">月擂台</c:when>
	                       						<c:otherwise>-</c:otherwise>
	                       					</c:choose>	
	                       				</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title mart5">擂台时间：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.periodsNumber}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title mart5">擂台人数：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.openingNumber}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	</c:if>
                		</table>
                	</div>
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>评价信息</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<c:if test="${comment.commentType eq '1'}">
                			<tr>
	                       		<td class="ab_left"><label class="title">评价等级：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<c:if test="${comment.level eq '1'}"><span style="color:#FF0000;">★</span></c:if>
	                       				<c:if test="${comment.level eq '2'}"><span style="color:#FF0000;">★★</span></c:if>	  
	                       				<c:if test="${comment.level eq '3'}"><span style="color:#FF0000;">★★★</span></c:if>	  
	                       				<c:if test="${comment.level eq '4'}"><span style="color:#FF0000;">★★★★</span></c:if>	  
	                       				<c:if test="${comment.level eq '5'}"><span style="color:#FF0000;">★★★★★</span></c:if>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品描述相符：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<c:if test="${comment.goodsLevel eq '1'}"><span style="color:#FF6600;">★</span></c:if>
	                       				<c:if test="${comment.goodsLevel eq '2'}"><span style="color:#FF6600;">★★</span></c:if>	  
	                       				<c:if test="${comment.goodsLevel eq '3'}"><span style="color:#FF6600;">★★★</span></c:if>	  
	                       				<c:if test="${comment.goodsLevel eq '4'}"><span style="color:#FF6600;">★★★★</span></c:if>	  
	                       				<c:if test="${comment.goodsLevel eq '5'}"><span style="color:#FF6600;">★★★★★</span></c:if>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">物流服务：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<c:if test="${comment.logisticsLevel eq '1'}"><span style="color:#FFFF00;">★</span></c:if>
	                       				<c:if test="${comment.logisticsLevel eq '2'}"><span style="color:#FFFF00;">★★</span></c:if>	  
	                       				<c:if test="${comment.logisticsLevel eq '3'}"><span style="color:#FFFF00;">★★★</span></c:if>	  
	                       				<c:if test="${comment.logisticsLevel eq '4'}"><span style="color:#FFFF00;">★★★★</span></c:if>	  
	                       				<c:if test="${comment.logisticsLevel eq '5'}"><span style="color:#FFFF00;">★★★★★</span></c:if>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	</c:if>
	                      	<tr>
	                       		<td class="ab_left"><label class="title">评价内容：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<c:choose>
                       						<c:when test="${comment.commentType eq '1'}">${comment.content}</c:when>
                       						<c:when test="${comment.activityType eq '2'}">-</c:when>
                       						<c:otherwise>-</c:otherwise>
                       					</c:choose>	               				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	
	  
	                      	<tr>
								<td></td>
								<td name=pice style="display:none">
										<div class="ab_main" colspan="3">
											<img  id = "picUrl" />	
										</div>
								</td>
								<td name=pice1 style="display:none">
										<div class="ab_main" colspan="3">
											<img  id = "picUrl1" />	
										</div>
								</td>
								<td name=pice2 style="display:none">
										<div class="ab_main" colspan="3">
											<img  id="picUrl2"/>	
										</div>
								</td>
							</tr>
	                      
	                       <tr >
                                <td class="ab_left"><label class="title">是否匿名发布：</label></td>
                                <td class="ab_main" colspan="3">
                                   <div class="content">
                                   	 <c:choose>
                                   	 	<c:when test="${comment.isIncognito eq '0'}">
                                   	 		<span>是</span>
                                   	 	</c:when>
                                   	 	<c:otherwise>
                                   	 		<span>否</span>
                                   	 	</c:otherwise>
                                   	 </c:choose>
                                   </div>
                                </td>
                          </tr>
	                      
	                      <c:if test="${comment.status eq '1'}">
	                     	 <tr>
	                       		<td class="ab_left"><label class="title">审核结果：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>通过</span>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	 <tr>
	                       		<td class="ab_left"><label class="title">审核备注：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.remarks}</span>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	<c:if test="${comment.commentType eq '1'}">
	                       	<tr>
	                       		<td class="ab_left"><label class="title">赠送优惠券：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.couponName}</span>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	</c:if>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">回复评论：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.reply}</span>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                      </c:if>
	                      
	                       <c:if test="${comment.status eq '2'}">
	                     	 <tr>
	                       		<td class="ab_left"><label class="title">审核结果：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>不通过</span>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                       	 <tr>
	                       		<td class="ab_left"><label class="title">审核备注：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${comment.remarks}</span>	                    				
	                       			</div>
	                  			</td>
	                       	</tr>
	                      </c:if>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/commentAction/showCommentList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    
    
    
<script>
</script>
  </body>
</html>
