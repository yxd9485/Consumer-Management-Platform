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
	
	<script type="text/javascript">
	$(function(){
		// 初始化校验控件
        $.runtimeValidate($("form"));
        
		// 保存（审核）
	    $(".btnSave").click(function(){
            var url = "<%=cpath%>/promotionUserAction/doPromotionUserCheck.do";
            $.fn.confirm("确认操作吗？", function() {
                $(".btnSave").attr("disabled", "disabled");
                $("form").attr("action", url);
                $("form").attr("onsubmit", "return true;");
                $("form").submit();

                $("form").attr("action", "<%=cpath%>/promotionUserAction/showPromotionUserList.do");
            });
            return false;
	    });

	});
	
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
            <li class="current"><a> 小票审核</a></li>
            <c:if test="${isShow eq '0' }"><li class="current"><a> ${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }管理</a></li></c:if>
            <c:if test="${isShow eq '1' }"><li class="current"><a> 审核列表</a></li></c:if>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/promotionUserAction/showPromotionUserList.do">
            	<input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="infoKey" value="${infoKey}" />
                <input type="hidden" name="terminalOpenid" value="${promotionUser.terminalOpenid}" />
                <input type="hidden" name="firstImgH" value="${promotionUser.firstImgH}" />
                
            	<div class="widget box activityinfo">
                    <!-- 促销员信息 -->
                    <div class="widget-header"><h4><i class="iconfont icon-daoru"></i>${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }信息</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">姓名：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.userName}</span>
                                    </div>
                                </td>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">手机号：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.phoneNum}</span>
                                    </div>
                                </td>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">外勤365编码：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.userCode}</span>
                                    </div>
                                </td>
                           </tr>
                            <tr>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">所属战区：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
	                                    <span class="blocker en-larger">${promotionUser.warAreaName}</span>
                                    </div>
                                </td>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">所属渠道：</label></td>
                                <td class="ab_main"style="width: 23% !important;" >
                                    <div class="content">
                                        <span class="blocker en-larger">
                                        <c:choose>
                                            <c:when test="${promotionUser.ticketChannel eq '1'}">餐饮渠道</c:when>
                                            <c:when test="${promotionUser.ticketChannel eq '2'}">KA渠道</c:when>
                                        </c:choose>
                                        </span>
                                    </div>
                                </td>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">所属系统：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.terminalSystem}</span>
                                    </div>
                                </td>
                           </tr>
                            <tr>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">门店名称：</label></td>
                                <td class="ab_main"style="width: 23% !important;" >
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.terminalName}</span>
                                    </div>
                                </td>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">门店地址：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.terminalAddress}</span>
                                    </div>
                                </td>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">是否参与评选：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">
                                            <c:choose>
                                                <c:when test="${promotionUser.isJoinVote eq 1}">是</c:when>
                                                <c:otherwise>否</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </td>
                           </tr>
                           <tr>
                                <td class="ab_left" style="width: 10% !important;"><label class="title">激活状态：</label></td>
                                <td class="ab_main" style="width: 23% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">
                                            <c:choose>
                                                <c:when test="${promotionUser.status eq 1}">已激活</c:when>
                                                <c:otherwise>未激活</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </td>
                                <td colspan="4"></td>
                           </tr>
                        </table>
                    </div>
                		
                	<!-- 审核信息 -->
                	<c:if test="${promotionUser.isJoinVote eq 1}">
	                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>审核信息</h4></div>
	                	<div class="widget-content panel no-padding">
	                		<table class="active_board_table">
	                		    <tr>
	                                <td class="ab_left" style="width: 5% !important;"></td>
	                                <td class="ab_main" style="width: 90% !important;" >
	                                                                        推荐官宣言：<br/><br/>
	                                     <span>${promotionUser.introduce}</span>
	                                </td>
	                                <td class="ab_main" style="width: 5% !important;"></td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left" style="width: 5% !important;"></td>
	                                <td class="ab_main" style="width: 90% !important;" >
	                                     <br/>推荐官照片：<br/><br/>
	                                     <c:if test="${not empty(promotionUser.uploadPic)}">
		                                     <c:forEach items="${fn:split(promotionUser.uploadPic, ',')}" var="pic">
		                                          <a href="${pic }" target="_blank">
		                                            <img alt="" src="${pic }" style="width: 250px; height: 258px; margin-right: 10px;"/>
		                                          </a>
		                                     </c:forEach>
	                                     </c:if>
	                                </td>
	                                <td class="ab_main" style="width: 5% !important;"></td>
	                            </tr>
	                			<c:choose>
	                				<c:when test="${promotionUser.isJoinVote eq '1' and promotionUser.checkStatus eq '0' and isShow ne 0}">
	                				    <tr>
                                            <td class="ab_left" style="width: 5% !important;"></td>
                                            <td class="ab_main" style="width: 90% !important;" >
                                                <br/>
                                                <span style="float: left; margin-right: 10px; line-height: 32px;">审核结果：</span>
                                                <input type="radio" class="radio" name="checkStatus" id="checkStatus1" value="1" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
                                                <span class="blocker en-larger">通过</span>
                                                <input type="radio" class="radio" name="checkStatus" id="checkStatus3" value="3" style="float:left; cursor: pointer; min-height: 33px;" />
                                                <span class="blocker en-larger">驳回</span>
                                                <label class="validate_tips"></label>
                                            </td>
                                            <td class="ab_main" style="width: 5% !important;"></td>
                                        </tr>
                                        <tr>
                                            <td class="ab_left" style="width: 5% !important;"></td>
                                            <td class="ab_main" style="width: 90% !important;" >
                                                 <br/>
                                                 <span style="float: left; margin-right: 10px; line-height: 32px;">审核备注：</span>
                                                 <input class="form-control" maxlength="30" tag="validate" name="checkOpinion" style="float:left; width:420px; min-height: 33px;"></input>
                                                 <label class="validate_tips"></label>
                                            </td>
                                            <td class="ab_main" style="width: 5% !important;"></td>
                                        </tr>
	                				</c:when>
	                				<c:otherwise>
	                                   <tr>
                                            <td class="ab_left" style="width: 5% !important;"></td>
                                            <td class="ab_main" style="width: 90% !important;" >
                                                 <br/>
                                                 <span>审核结果：</span>
                                                 <span>
                                                     <c:choose>
                                                        <c:when test="${promotionUser.checkStatus eq 1}">通过</c:when>
                                                        <c:when test="${promotionUser.checkStatus eq 3}">驳回</c:when>
                                                        <c:otherwise>未审核</c:otherwise>
                                                    </c:choose>
                                                 </span>
                                            </td>
                                            <td class="ab_main" style="width: 5% !important;"></td>
                                        </tr>
                                        <tr>
                                            <td class="ab_left" style="width: 5% !important;"></td>
                                            <td class="ab_main" style="width: 90% !important;" >
                                                 <br/>
                                                 <span>审核备注：</span>
                                                 <span>${ empty(promotionUser.checkOpinion) ? '-' : promotionUser.checkOpinion}</span>
                                            </td>
                                            <td class="ab_main" style="width: 5% !important;"></td>
                                        </tr>    
	                			    </c:otherwise>
	                			</c:choose>
	                		</table>
	                	</div>
                	</c:if>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <c:if test="${promotionUser.isJoinVote eq '1' and promotionUser.checkStatus eq '0' and isShow ne 0}">
                                <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/promotionUserAction/showPromotionUserList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>
