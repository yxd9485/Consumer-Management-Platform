<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<% 
    String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
    String prizeType0 = new String(PropertiesUtil.getPropertyValue("prize_type0").getBytes("ISO-8859-1"), "UTF-8");
    String prizeType1 = new String(PropertiesUtil.getPropertyValue("prize_type1").getBytes("ISO-8859-1"), "UTF-8");
    String prizeType2 = new String(PropertiesUtil.getPropertyValue("prize_type2").getBytes("ISO-8859-1"), "UTF-8");
    String prizeType5 = new String(PropertiesUtil.getPropertyValue("prize_type5").getBytes("ISO-8859-1"), "UTF-8");
    String prizeType6 = new String(PropertiesUtil.getPropertyValue("prize_type6").getBytes("ISO-8859-1"), "UTF-8");
    String prizeType7 = new String(PropertiesUtil.getPropertyValue("prize_type7").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeP = new String(PropertiesUtil.getPropertyValue("prize_typeP").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeQ = new String(PropertiesUtil.getPropertyValue("prize_typeQ").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeR = new String(PropertiesUtil.getPropertyValue("prize_typeR").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeS = new String(PropertiesUtil.getPropertyValue("prize_typeS").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeT = new String(PropertiesUtil.getPropertyValue("prize_typeT").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeH = new String(PropertiesUtil.getPropertyValue("prize_typeH").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeI = new String(PropertiesUtil.getPropertyValue("prize_typeI").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeJ = new String(PropertiesUtil.getPropertyValue("prize_typeJ").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeK = new String(PropertiesUtil.getPropertyValue("prize_typeK").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeL = new String(PropertiesUtil.getPropertyValue("prize_typeL").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeM = new String(PropertiesUtil.getPropertyValue("prize_typeM").getBytes("ISO-8859-1"), "UTF-8");
    String prizeTypeN = new String(PropertiesUtil.getPropertyValue("prize_typeN").getBytes("ISO-8859-1"), "UTF-8");
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    
    <script>
        $(function(){
            
            var flag = "${flag}";
            if(flag != "") {
                $("div.modal-body ."+flag+"").show();
                $("div.modal-body :not(."+flag+")").hide();
                $("#myModal").modal("show");
            }
            
            // 新增
            $("#addRule").click(function(){
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogAdd.do";
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 区域下新增规则
            $("a.areaRuleAdd").off();
            $("a.areaRuleAdd").on("click", function(){
                var key = $(this).data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogAdd.do?areaCode="+key;
                $("form").attr("action", url);
                $("form").submit(); 
                return false;
            });
            
            // 区域规则删除
            $("a.areaRuleDel").off();
            $("a.areaRuleDel").on("click", function(){
                var key = $(this).data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/doRebateRuleDelete.do?areaCode="+key;
                $.fn.confirm("确定要删除此区域及其规则吗？",function(){
                    $("form").attr("action", url);
                    $("form").submit();
                    return false;
                	
                });
            });
            
         	// 删除活动下无效规则
            $("#delActivityRule").off();
            $("#delActivityRule").on("click", function(){
                var url = "<%=cpath%>/vcodeActivityRebateRule/doRebateRuleDelete.do";
                $.fn.confirm("确定要删除该活动下所有无效规则吗？",function(){
                    $("form").attr("action", url);
                    $("form").submit();
                    return false;
                	
                });
            });
            
            // 修改
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogEdit.do?rebateRuleKey="+key;
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
            	var key = $(this).parents("td").data("key");
            	var url = "<%=cpath%>/vcodeActivityRebateRule/doRebateRuleCogDelete.do?rebateRuleKey="+key;
                $.fn.confirm("确定要移除吗？",function(){
                    $("form").attr("action", url);
                    $("form").submit();	
                });
            }); 
                    
            // 克隆
            $("a.clone").off();
            $("a.clone").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogClone.do?rebateRuleKey="+key;
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 爆点红包
            $("a.erupt").off();
            $("a.erupt").on("click", function(){
            	var key = $(this).parents("td").data("key");
            	 var url = "<%=cpath%>/vcodeActivityRebateRule/queryEruptRedpacketList.do?rebateRuleKey="+key;
                 $("form").attr("action", url);
                 $("form").submit();
            });
            
            // 实现Collapse同时只展开一个的功能
            $(".paneltitle").on("click", function(){
                var collapse = $(this).attr("aria-controls");
                $("[id^='collapseInner']").each(function(obj){
                    if ($(this).attr("id") != collapse) {
                        $(this).removeClass("in");
                        $(this).addClass("collapse");
                    }
                });
            });
            
            //  默认第一个展开 
            // $("#collapseArea1").addClass("in");
            
        });
    </script>
    <style>
        table.table tr th {
            text-align: center;
        }
        table.table tr td {
            vertical-align: middle;
        }
    </style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 活动管理</a></li>
            <c:if test="${activityType eq '1' }">
        		<li class="current"><a title="">一罐一码规则</a></li>
        	</c:if>
        	<c:if test="${activityType eq '2' }">
        		<li class="current"><a title="">一瓶一码规则</a></li>
        	</c:if>
        	<c:choose>
	        	<c:when test="${currentUser.roleKey ne '4'}">
	        		<li class="current"><a title="">规则设置</a></li>
	        	</c:when>
	        	<c:otherwise>
	        		<li class="current"><a title="">查看设置</a></li>
	        	</c:otherwise>
        	</c:choose>
            
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <div class="row">
                            <div class="dataTables_header clearfix">
                                <c:if test="${currentUser.roleKey ne '4'}">
                                    <div class="button_nav col-md-12">
                                        <a id="addRule" class="btn btn-blue"><i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;新 增</a>
                                        <a id="delActivityRule" class="btn btn-red">&nbsp;一键删除无效规则</a>
                                    </div>
                                </c:if>
                                <form class="listForm" method="post">
                                    <input type="hidden" name="vcodeActivityKey" value="${vcodeActivityKey}" />
                                    <input type="hidden" name="activityType" value="${activityType}" />
                                </form>
                            </div>
                        </div>
                        <div class="panel-group" id="accordionArea">
                            <c:forEach items="${resultList}" var="ruleAreaCog" varStatus="idxArea">
						    <div class="panel panel-default">
						        <div class="panel-heading" data-toggle="collapse" data-parent="#accordionArea" href="#collapseArea${idxArea.count}" >
						           <span>${ruleAreaCog.areaName}</span>
						           
                                   <c:if test="${currentUser.roleKey ne '4'}">
							           <div style="display: block; float: right;">
		                                   <a class="btn btn-xs areaRuleAdd btn-red" data-key="${ruleAreaCog.areaCode}" ><i class="iconfont icon-xiugai"></i>&nbsp;添加规则</a>
		                                   <c:if test="${ruleAreaCog.areaCode != '000000'}">
		                                       <a class="btn btn-xs areaRuleDel btn-brownness" data-key="${ruleAreaCog.areaCode}"><i class="iconfont icon-lajixiang"></i>&nbsp;删除无效规则</a>
		                                   </c:if>
							           </div>
						           </c:if>
						        </div>
						        <div id="collapseArea${idxArea.count}" class="panel-collapse collapse">
						            <div class="panel-body" style="padding:5px;">
				                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart5"
				                               id="dataTable_data">
				                            <thead>
				                                <tr>
                                                    <th style="width:5%;">序号</th>
                                                    <th style="width:6%;">规则类型</th>
                                                    <th style="width:8%;">规则日期</th>
                                                    <th style="width:6%;">规则时间</th>
                                                    <th style="width:6%;">规则特性</th>
                                                    <th style="width:6%;">限制积分</th>
                                                    <th style="width:6%;">限制金额</th>
                                                    <th style="width:6%;">限制瓶数</th>
                                                    <th style="width:6%;">用户限次</th>
                                                    <th style="width:6%;">继承限制</th>
                                                    <th style="width:6%;">扫码积分</th>
                                                    <th style="width:6%;">扫码金额</th>
                                                    <th style="width:6%;">扫码瓶数</th>
				                                    <!-- <th style="width:5%;">限制</br>有效</th> -->
				                                    <c:if test="${currentUser.roleKey ne '4'}">
				                                    <th style="width:21%;">操作</th>
				                                    </c:if>
				                                </tr>
				                            </thead>
				                            <tbody>
				                                <c:choose>
				                                <c:when test="${fn:length(ruleAreaCog.rebateRuleCogLst) gt 0}">
				                                   <div class="panel-group" id="accordionRule${idxArea.count}" role="tablist" aria-multiselectable="true">
				                                      <c:forEach items="${ruleAreaCog.rebateRuleCogLst}" var="ruleTypeCog" varStatus="idxType">
				                                        <tr>
				                                            <td style="text-align:center; border-top-width: 0px; 
				                                            	<c:choose>
						                                            <c:when test="${ruleTypeCog.ruleStatus == '1' and ruleTypeCog.isValid == '0'}">background-color: #ea918c;</c:when>
						                                            <c:when test="${ruleTypeCog.ruleStatus == '2' or ruleTypeCog.isValid == '1'}">background-color: #999999;</c:when>
						                                            <c:otherwise>background-color: #ecb010;</c:otherwise>
						                                        </c:choose>">
				                                                <span>${idxType.count}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px">
				                                                <span>
				                                                   <c:choose>
				                                                       <c:when test="${ruleTypeCog.ruleType == '1'}">节假日</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '2'}">时间段</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '3'}">周几</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '4'}">每天</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '5'}">周签到</c:when>
				                                                   </c:choose>
				                                                </span>
				                                                <c:if test="${ruleTypeCog.validMsg != ''}">
				                                                   <br />
				                                                   <span style="color: red; font-weight: bold;">${ruleTypeCog.validMsg }</span>
				                                                </c:if>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; text-align: center;">
				                                            	<c:choose>
				                                            		 <c:when test="${ruleTypeCog.ruleType == '3'}"><span>${ruleTypeCog.beginDate} - ${ruleTypeCog.endDate}</span></c:when>
				                                            		 <c:when test="${ruleTypeCog.ruleType == '1' or ruleTypeCog.ruleType == '2'}">
				                                            		 	<span>${ruleTypeCog.beginDate}</span>
					                                                  	<hr style="margin-bottom: 8px; margin-top: 9px; border-top: 0px solid white;">
					                                                  	<span>${ruleTypeCog.endDate}</span>
				                                            		 </c:when>
				                                            	</c:choose>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; text-align: center;">
				                                                  <span>${ruleTypeCog.beginTime}</span>
				                                                  <hr style="margin-bottom: 8px; margin-top: 9px; border-top: 0px solid white;">
				                                                  <span>${ruleTypeCog.endTime}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px">
				                                                  <span>
				                            						<c:if test="${not empty(ruleTypeCog.hotAreaKey)}"> 热</c:if>                      
				                                                  	<c:if test="${not empty(ruleTypeCog.eruptRuleInfo)}"> 爆</c:if>
				                                                  </span>
				                                            </td>
                                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px">
                                                                  <span><fmt:formatNumber value="${ruleTypeCog.restrictVpoints eq 0 ? '' : ruleTypeCog.restrictVpoints}" pattern="#,###" /></span>
                                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px">
				                                                  <span><fmt:formatNumber value="${ruleTypeCog.restrictMoney eq 0 ? '' : ruleTypeCog.restrictMoney}" pattern="#,##0.00#" /></span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px">
				                                                  <span>${ruleTypeCog.restrictBottle eq 0 ? '' : ruleTypeCog.restrictBottle}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px;">
				                                            	<span>${ruleTypeCog.restrictCount eq 0 ? '' : ruleTypeCog.restrictCount}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">
			                                                    <span>
				                                                    <c:choose>
				                                                        <c:when test="${not empty(ruleTypeCog.appointRebateRuleKey)}">是</c:when>
				                                                        <c:otherwise>否</c:otherwise>
				                                                    </c:choose>
			                                                  </span>
				                                            </td>
                                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px;">
                                                                <c:choose>
                                                                    <c:when test="${ruleTypeCog.totalConsumeVpoints > 0}">
                                                                        <span title="当前规则已消耗积分">C：<fmt:formatNumber value="${ruleTypeCog.consumeVpoints eq 0 ? '' : ruleTypeCog.consumeVpoints}" pattern="#,###" /></span>
                                                                        <hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
                                                                        <span title="该限制已消耗总积分">T：<fmt:formatNumber value="${ruleTypeCog.totalConsumeVpoints eq 0 ? '' : ruleTypeCog.totalConsumeVpoints}" pattern="#,###" /></span>    
                                                                    </c:when>
                                                                    <c:otherwise><span title="当前规则已消耗积分"><fmt:formatNumber value="${ruleTypeCog.consumeVpoints eq 0 ? '' : ruleTypeCog.consumeVpoints}" pattern="#,###" /></span></c:otherwise>
                                                                </c:choose>
                                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px;">
				                                            	<c:choose>
				                                            		<c:when test="${ruleTypeCog.totalConsumeMoney > 0.00}">
				                                            			<span title="当前规则已消耗金额">C：<fmt:formatNumber value="${ruleTypeCog.consumeMoney eq 0 ? '' : ruleTypeCog.consumeMoney}" pattern="#,##0.00#" /></span>
					                                                    <hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
					                                                    <span title="该限制已消耗总金额">T：<fmt:formatNumber value="${ruleTypeCog.totalConsumeMoney eq 0 ? '' : ruleTypeCog.totalConsumeMoney}" pattern="#,##0.00#" /></span>	
				                                            		</c:when>
				                                            		<c:otherwise><span title="当前规则已消耗金额"><fmt:formatNumber value="${ruleTypeCog.consumeMoney eq 0 ? '' : ruleTypeCog.consumeMoney}" pattern="#,##0.00#" /></span></c:otherwise>
				                                            	</c:choose>
			                                                   
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px;">
				                                            	<c:choose>
				                                            		<c:when test="${ruleTypeCog.totalConsumeBottle > 0}">
				                                            			<span title="当前规则已消耗瓶数">C：${ruleTypeCog.consumeBottle eq 0 ? '' : ruleTypeCog.consumeBottle}</span>
			                                                  			<hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
			                                                  			<span title="该限制已消耗总瓶数">T：${ruleTypeCog.totalConsumeBottle eq 0 ? '' : ruleTypeCog.totalConsumeBottle}</span>
				                                            		</c:when>
				                                            		<c:otherwise><span title="当前规则已消耗瓶数">${ruleTypeCog.consumeBottle eq 0 ? '' : ruleTypeCog.consumeBottle}</span></c:otherwise>
				                                            	</c:choose>
				                                            </td>
				                                            <%-- <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">
				                                                  <span>
				                                                    <c:choose>
				                                                        <c:when test="${ruleTypeCog.isValid == '0'}">是</c:when>
				                                                        <c:otherwise>否</c:otherwise>
				                                                    </c:choose>
				                                                  </span>
				                                            </td> --%>
				                                            <c:if test="${currentUser.roleKey ne '4'}">
				                                                <td data-key="${ruleTypeCog.rebateRuleKey}" style="text-align:left; border-top-width: 0px">
				                                                    <a class="btn btn-xs edit btn-red">修改</a>
				                                                    <c:if test="${ruleTypeCog.ruleType != '4' or ruleAreaCog.areaCode != '000000'}">
				                                                       <a class="btn btn-xs del btn-brownness">删除</a>
				                                                    </c:if>
				                                                    <c:if test="${ruleTypeCog.ruleType == '1' or ruleTypeCog.ruleType == '2'}">
				                                                    	<c:if test="${not empty(ruleTypeCog.eruptRuleInfo)}">
						                                                    <a class="btn btn-xs erupt btn-orange">爆点红包</a>
				                                                    	</c:if>
				                                                    </c:if>
				                                                    <a class="btn btn-xs clone btn-red">克隆</a>
				                                                </td>
				                                            </c:if>
				                                        </tr>
				                                        <tr style="background-color: white;"><td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">14</c:when><c:otherwise>13</c:otherwise></c:choose>" style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
				                                         <div id="collapseInnerArea${idxArea.count}Rule${idxType.count}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${idxType.count}">
				                                           <div class="panel-body" style="padding:5px;">
				                                           
				                                               <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
				                                                <thead>
				                                                    <tr class="success" style="background-color: #f0d2d0;">
                                                                        <th style="width:5%; background: transparent;">序号</th>
                                                                        <th style="width:12%; background: transparent;">奖品类型</th>
                                                                        <th style="width:11%; background: transparent;">配置积分</th>
                                                                        <th style="width:11%; background: transparent;">配置金额</th>
                                                                        <th style="width:11%; background: transparent;">总个数</th>
                                                                        <th style="width:11%; background: transparent;">剩余数</th>
                                                                        <th style="width:12%; background: transparent;">概率值</th>
                                                                        <th style="width:12%; background: transparent;">概率</th>
                                                                        <th style="width:15%; background: transparent;">阶梯区间</th>
				                                                    </tr>
				                                                </thead>
				                                                <tbody>
				                                                <c:choose>
				                                                    <c:when test="${fn:length(ruleTypeCog.vpointsCogLst) gt 0}">
				                                                       <c:forEach items="${ruleTypeCog.vpointsCogLst}" var="vpoint" varStatus="idxV">
				                                                        <tr>
				                                                            <td style="text-align:center;">
				                                                                <span>${idxV.count}</span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>
				                                                                  <c:choose>
				                                                                    <c:when test="${vpoint.prizeType == '0'}"><%=prizeType0%></c:when>
                                                                                    <c:when test="${vpoint.prizeType == '1'}"><%=prizeType1%></c:when>
                                                                                    <c:when test="${vpoint.prizeType == '2'}"><%=prizeType2%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == '5'}"><%=prizeType5%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == '6'}"><%=prizeType6%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == '7'}"><%=prizeType7%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'P'}"><%=prizeTypeP%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'Q'}"><%=prizeTypeQ%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'R'}"><%=prizeTypeR%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'S'}"><%=prizeTypeS%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'T'}"><%=prizeTypeT%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'H'}"><%=prizeTypeH%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'I'}"><%=prizeTypeI%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'J'}"><%=prizeTypeJ%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'K'}"><%=prizeTypeK%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'L'}"><%=prizeTypeL%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'M'}"><%=prizeTypeM%></c:when>
				                                                                    <c:when test="${vpoint.prizeType == 'N'}"><%=prizeTypeN%></c:when>
				                                                                </c:choose>
				                                                                  </span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.vcodeVpoints}</span>
				                                                            </td>
                                                                            <td>
                                                                                  <span>${vpoint.vcodeMoney}</span>
                                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.cogAmounts}</span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.restAmounts}</span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.rangeVal}</span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.rangePercent}%</span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.scanNum}</span>
				                                                            </td>
				                                                        </tr>
				                                                       </c:forEach>
				                                                    </c:when>
				                                                    <c:otherwise>
				                                                        <tr>
				                                                            <td colspan="9"><span>查无数据！</span></td>
				                                                        </tr>
				                                                    </c:otherwise>
				                                                </c:choose>
				                                                </tbody>
				                                               </table>
				                                           </div>
				                                         </div>
				                                        </td></tr>
				                                       </c:forEach>
				                                    </div>
				                                </c:when>
				                                <c:otherwise>
				                                    <tr>
				                                        <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">15</c:when><c:otherwise>14</c:otherwise></c:choose>"><span>查无数据！</span></td>
				                                    </tr>
				                                </c:otherwise>
				                                </c:choose>
				                            </tbody>
				                        </table>
			                        </div>
					           </div>
					        </div>
					        </c:forEach>
				       </div>
                  </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="top:30%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">提示消息</h4>
                </div>
                <div style="padding-left: 20px; padding-top: 10px">${errorMsg}</div>
                <div class="modal-body">
                    <h6 class="add_success">添加成功</h6>
                    <h6 class="add_fail">添加失败</h6>
                    <h6 class="edit_success">编辑成功</h6>
                    <h6 class="edit_fail">编辑失败</h6>
                    <h6 class="clone_success">${areaName}克隆成功</h6>
                    <h6 class="clone_fail">${areaName}克隆失败</h6>
                    <h6 class="import_success">${areaName}保存成功</h6>
                    <h6 class="import_fail">${areaName}保存失败</h6>
                    <h6 class="import_fail_lackFirstMoney">${areaName}地区规则编辑失败：导入金额缺少首扫区间金额</h6>
                    <h6 class="import_fail_lackFirstVpoints">${areaName}地区规则编辑失败：导入积分缺少首扫区间积分</h6>
                    <h6 class="import_fail_lackDoubtMoney">${areaName}地区规则编辑失败：导入金额缺少可疑区间金额</h6>
                    <h6 class="import_fail_lackDoubtVpoints">${areaName}地区规则编辑失败：导入积分缺少可疑区间积分</h6>
                    <h6 class="import_fail_repeatarea">${areaName}区域下的规则已存在</h6>
                    <h6 class="import_fail_scannum">${areaName}保存失败,不允许所有奖项配置项都配置阶梯区间</h6>
                    <h6 class="not_delete">请至少选择一条数据进行删除</h6>
                    <h6 class="is_delete">删除成功</h6>
                    <h6 class="xx_delete">删除失败</h6>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    </div>
  </body>
</html>
