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
		$(function(){
			// 初始化功能
			initPage();
		});
		
		function initPage() {
		    // 合计
            calculateTotalMoney();
		    
			// 返回
            $(".btnReturn").click(function(){
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });
			
          //监听图片切换事件
    		 $("#myPic").click(function() {
    				if($("#pictd").is(':visible')== true){
    					$("#pictd").hide(); 
    					$("#texttd").show();
			 		}else{
			 			$("#pictd").show(); 
    					$("#texttd").hide();
			 		}
	       	});
		}
		
        // 计算商品总个数、商品总价
        function calculateTotalMoney() {
            var num = 0;
            var price = 0; 
            $("#firstScanPrize tr").each(function(i,obj){
                if($(this).find("[name='skuNum']").val()) {
                    num += Number($(this).find("[name='skuNum']").val());
                } 
                if($(this).find("[name='ticketSkuMoney']").val()) {
                    price += Number($(this).find("[name='ticketSkuMoney']").val());
                } 
            console.log("num:" + num + " price:" + price);
            });
            console.log("num:" + num + " price:" + price);
            $("#firstScanPrize").find("#totalNum").text(num.toFixed(0))
            $("#firstScanPrize").find("#totalPrice").text(price.toFixed(2))
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
            <li class="current"><a> 小票审核</a></li>
            <li class="current"><a> 审核列表</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/smallticketAction/doTicketRecordEdit.do">
            	<div class="widget box activityinfo">
                    <!-- 促销员信息 -->
                    <c:if test="${vpsTicketRecord.ticketChannel eq '2'}">
                    <div class="widget-header"><h4><i class="iconfont icon-daoru"></i>促销员信息</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">促销员姓名：<span class="required">*</span></label></td>
                                <td class="ab_main" style="width: 25% !important;">
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.userName}</span>
                                    </div>
                                </td>
                                <td class="ab_left"><label class="title">手机号：<span class="required">*</span></label></td>
                                <td class="ab_main" >
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.phoneNum}</span>
                                    </div>
                                </td>
                           </tr>
                            <tr>
                                <td class="ab_left"><label class="title">所属终端：<span class="required">*</span></label></td>
                                <td class="ab_main"  style="width: 25% !important;">
                                    <div class="content">
                                    <c:choose>
                                        <c:when test="${not empty(vpsTicketRecord.promotionTerminalName) && vpsTicketRecord.promotionTerminalName ne promotionUser.terminalName }">
                                            <span class="blocker en-larger">${promotionUser.terminalName}(${vpsTicketRecord.promotionTerminalName})</span>
                                        </c:when>
                                        <c:otherwise><span class="blocker en-larger">${promotionUser.terminalName}</span></c:otherwise>
                                    </c:choose>
                                    </div>
                                </td>
                                <td class="ab_left"><label class="title">所属体系：<span class="required">*</span></label></td>
                                <td class="ab_main" >
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.terminalSystem}</span>
                                    </div>
                                </td>
                           </tr>
                            <tr>
                                <td class="ab_left"><label class="title">门店地址：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker en-larger">${promotionUser.province}${promotionUser.city}${promotionUser.county}${promotionUser.terminalAddress}</span>
                                    </div>
                                </td>
                           </tr>
                        </table>
                    </div>
                    </c:if>
<!--                    小票信息 -->
             		<div class="widget-content panel no-padding">
                         <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                id="dataTable_data">
                             <thead>
                             <tr>
                                 <div class="content" class="required">
                                     <th >小票照片 
                                     <i id="myPic" name="myPic" class="iconfont icon-jinggao" style="color: red; font-size: 20px;"></i>
                                     <th >小票信息
                                 </div>
                             </tr>
                             </thead>
                             <tbody>
                             <tr>
                            		
                                 <td style="text-align:center;" id ="pictd" name = "pictd">
                                     <span><img src="${vpsTicketRecord.ticketUrl}" width=300 height=300 text-align:center;"></span>
                                 </td>     
                                 <td style="display:none" id ="texttd" name = "texttd">            
                                     <span id="text" name="text" style="width:300px;height:300px;display:block;overflow:auto;">${vpsTicketRecord.ocrContent}</span>
                                 </td>       	                      
                     			<td>	   
          			
                     				  <div class="row">
            						<div class="col-md-12 ">
                         						<div class="form-group little_distance search" style="padding-bottom: 5px;">
                        					<label class="control-label" style="width: 10%;" >门店名称：</label>
                             						<div class="search-ticket">
                        								<input name="ticketTerminalName" id="ticketTerminalName" value= "${vpsTicketRecord.ticketTerminalName}"  class="form-control input-width-larger" autocomplete="off" maxlength="10" readonly/>
                             						</div>               						                     
                              					<label class="control-label" style="width: 10%;">店内体系码：</label>
                             						<div class="search-ticket">
                              							<select style="width:29px" disabled="disabled"	 name="insideCodeType" id="insideCodeType" class="form-control input-width-larger" autocomplete="off" >
                                  						<option value="">请选择</option>
                                  							<c:if test="${!empty insideLst}">
                                             						<c:forEach items="${insideLst}" var="inside">
                                             							<option value="${inside.insideCodeType}" <c:if test="${vpsTicketRecord.insideCode eq inside.insideCodeType}">selected</c:if>>${inside.insideCodeName}</option>
                                             						</c:forEach>
                                             					</c:if>
                              							</select>
                             						</div>
                        				 			</div>
            						</div>
           						</div>

                     				  <div class="row">
            						<div class="col-md-12 ">
                         						<div class="form-group little_distance search" style="padding-bottom: 5px;">
                        					<label class="control-label" style="width: 10%;">流&nbsp;水&nbsp;号&nbsp;&nbsp;：</label>
                             						<div class="search-ticket">
                        						<input name="ticketNo" id="ticketNo" value= "${vpsTicketRecord.ticketNo}" class="form-control input-width-larger" autocomplete="off" maxlength="10" readonly/>
                             						</div> 
                        					<label class="control-label" style="width: 10%;">地区：</label>
                             						<div class="search-ticket">
                        						<label>${vpsTicketRecord.warAreaName}-${vpsTicketRecord.province}-${vpsTicketRecord.city}-${vpsTicketRecord.county}</label>
                             						</div>
                        				 			</div>
            						</div>
           						</div>
                     			
                     			 		<div class="row">
            						<div class="col-md-12 ">
                         						<div class="form-group little_distance search" style="padding-bottom: 5px;">
                        					<label class="control-label" style="width: 10%;">小票总金额：</label>
                             						<div class="search-ticket">
                        							<input name="ticketMoney" value="${vpsTicketRecord.ticketMoney}" class="form-control  input-width-larger"  maxlength="10" readonly/>
                             						</div> 
                                                     <label class="control-label" style="width: 10%;">上传渠道：</label>
                                                     <div class="search-ticket">
                                                         <label>${vpsTicketRecord.ticketChannel eq '1' ? '餐饮渠道' : '商超渠道'}</label>
                                                     </div>
                        				 			</div>
            						</div>
           						</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                     					<div>	                       						
                                 		<table id="firstScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 95%; margin: 0 auto; text-align: center; overflow: auto !important">
                                     	<thead>
                                     	<tr>
                                         	<th style="width:5%; text-align: center;">序号</th>
                                         	<th style="width:6%; text-align: center;">商品条码</th>
                                         	<th style="width:6%; text-align: center;">商品名称</th>
                                         	<th style="width:20%; text-align: center;">活动产品名称</th>
                                             <th style="width:11%; text-align: center;" class="cogItemVpoints">数量</th>
                                             <th style="width:11%; text-align: center;" class="cogItemVpoints">单价</th>
                                             <th style="width:11%; text-align: center;" class="cogItemVpoints">合计</th>
                                     	</tr>
                                     	</thead>
                                     	<tbody>

                                             <c:forEach items="${vpsTicketRecord.detailLst}" var="detail" varStatus="idx">
                                                 <tr class="alertChart">
                                                  <td>
                                                     <label id="NO" style="line-height: 30px;">${idx.count}</label>
                                                 </td>
                                                 <td>
                                                     <input readonly type="text" name="terminalInsideCode"  id="terminalInsideCode" value="${detail.terminalInsideCode}"   class="form-control  input-width-wide"  autocomplete="off"
                                                          	   tag="validate"  maxlength="8" style="display: initial;">	
                                                   
                                                 </td>
                                                 <td>
                                                     <input readonly type="text" name="ticketSkuName" id="ticketSkuName" value="${detail.ticketSkuName}" class="form-control input-width-larger"  autocomplete="off"
                                                                tag="validate" maxlength="18" style="display: initial;">	
                                                
                                                 </td>
                                                 <td>                                                       
                                                     <select id="skuKey" name="skuKey" disabled="disabled" class="form-control input-width-larger">
                                                     	<option value=""></option>
                                                         <c:if test="${!empty skuLst}">
                                             				<c:forEach items="${skuLst}" var="sku">
                                             					<option value="${sku.skuKey}" <c:if test="${detail.skuKey eq sku.skuKey}">selected</c:if>>${sku.skuName}</option>
                                             				</c:forEach>
                                             			</c:if>
                                                     </select>
                                                 </td>
                                                <td class="cogItemVpoints">
                                                     	<input readonly id= "skuNum" name="skuNum" value="${detail.skuNum}"  class="form-control input-width-small number integer maxValue" value = "1"
                                                     		autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="3" style="display: initial; width: 50px !important;">	
                                                 </td>
                                                 <td style="position: relative;">
                                                         <input  readonly type="text" id="ticketSkuUnitMoney" value="${detail.ticketSkuUnitMoney}" name="ticketSkuUnitMoney" class="form-control input-width-small number maxValue"
                                                                autocomplete="off" tag="validate" data-oldval="0.00" value="0.00" maxVal="100" maxlength="5" style="display: initial; ">
                                                 </td>
                                                 <td style="position: relative;">
                                                         <input  readonly type="text" id="ticketSkuMoney" value="${detail.ticketSkuMoney}" name="ticketSkuMoney" class="form-control input-width-small number maxValue"
                                                                autocomplete="off" tag="validate" data-oldval="0.00" value="0.00" maxVal="100" maxlength="5" style="display: initial; ">
                                                 </td>
                                                 
                                             </tr>
                                             </c:forEach>
                                     <tr>
                                         <td>合计</td>
                                         <td>--</td>
                                         <td>--</td>
                                         <td>--</td>
                                         <td><span id="totalNum" name="totalNum" data-currval="0" style="font-weight: bold;">0</span></td>
                                         <td>--</td>
                                         <td><span id="totalPrice" name="totalPrice " data-currval="0" style="font-weight: bold;">0</span></td>
                                     </tr>
                                     </tbody>
                                 </table>
						</div>
                     			</td>
                             </tr>
                             </tbody>
                         </table>
                     </div>
                		
                	<!-- 审核信息 -->
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>审核信息</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                		
                			<c:choose>
                				<c:when test="${vpsTicketRecord.submintCheckReason == null || vpsTicketRecord.submintCheckReason == ''}">
                					<c:if test="${vpsTicketRecord.ticketStatus eq '2' or vpsTicketRecord.ticketStatus eq '4'}">
                						<tr>
	                       					<td class="ab_left"><label class="title">审核结果：</label></td>
	                       					<td class="ab_main" colspan="3">
		                       					<div class="content">
		                       						<span>通过</span>	                    				
		                       					</div>
		                  					</td>
	                       				</tr>
			                          <tr>
			                              <td class="ab_left"><label class="title">是否给促销员返利：</label></td>
			                              <td class="ab_main" colspan="3">
			                                  <div class="content">
			                                      <c:choose>
			                                        <c:when test="${vpsTicketRecord.promotionEarnFlag eq '1'}">返利</c:when>
			                                        <c:otherwise>不返利</c:otherwise>
			                                      </c:choose>
			                                  </div>
			                              </td>
			                           </tr>
	                       	 			<tr>
	                       					<td class="ab_left"><label class="title">审核备注：</label></td>
	                       					<td class="ab_main" colspan="3">
	                       					<div class="content">
	                       						<span>${vpsTicketRecord.remark}</span>	                    				
	                       					</div>
	                  						</td>
	                       				</tr>
	                       				<tr>
	                       				<td class="ab_left"><label class="title">审核人：</label></td>
	                       				<td class="ab_main" colspan="3">
	                       					<div class="content">
	                       						<span>${vpsTicketRecord.inputUserName}</span>	                    				
	                       					</div>
	                  					</td>
	                       				</tr>
	                       				<tr>
	                       				<td class="ab_left"><label class="title">审核时间：</label></td>
	                       				<td class="ab_main" colspan="3">
	                       					<div class="content">
	                       						<span>${vpsTicketRecord.inputTime}</span>	                    				
	                       					</div>
	                  					</td>
	                       				</tr>
                					</c:if>
                					<c:if test="${vpsTicketRecord.ticketStatus eq '1'}">
                						<tr>
		                       				<td class="ab_left"><label class="title">审核结果：</label></td>
		                       				<td class="ab_main" colspan="3">
			                       				<div class="content">
			                       					<span>不通过</span>	                    				
			                       				</div>
		                  					</td>
	                       				</tr>
			                          <tr>
			                              <td class="ab_left"><label class="title">是否给促销员返利：</label></td>
			                              <td class="ab_main" colspan="3">
			                                  <div class="content">
			                                      <c:choose>
			                                        <c:when test="${vpsTicketRecord.promotionEarnFlag eq '1'}">返利</c:when>
			                                        <c:otherwise>不返利</c:otherwise>
			                                      </c:choose>
			                                  </div>
			                              </td>
			                           </tr>
	                       	 			<tr>
	                       				<td class="ab_left"><label class="title">驳回原因：</label></td>
	                       				<td class="ab_main" colspan="3">
	                       				<div class="content">
	                       					<span>${vpsTicketRecord.dismissReason}</span>	                    				
	                       				</div>
	                  					</td>
	                       				</tr>
	                       				<tr>
	                       				<td class="ab_left"><label class="title">审核人：</label></td>
	                       				<td class="ab_main" colspan="3">
	                       				<div class="content">
	                       					<span>${vpsTicketRecord.inputUserName}</span>	                    				
	                       				</div>
	                  					</td>
	                       				</tr>
	                       				<tr>
	                       				<td class="ab_left"><label class="title">审核时间：</label></td>
	                       				<td class="ab_main" colspan="3">
	                       				<div class="content">
	                       					<span>${vpsTicketRecord.inputTime}</span>	                    				
	                       				</div>
	                  					</td>
	                       				</tr>
                					</c:if>
                				</c:when>
                				<c:otherwise>
                					 <tr>
			                       		<td class="ab_left"><label class="title">审核结果：</label></td>
			                       		<td class="ab_main" colspan="3">
			                       			<div class="content">
			                       				<span>转发给客户审核</span>	                    				
			                       			</div>
			                  			</td>
			                       	</tr>
			                       	 <tr>
			                       		<td class="ab_left"><label class="title">送审原因：</label></td>
			                       		<td class="ab_main" colspan="3">
			                       			<div class="content">
			                       				<span>${vpsTicketRecord.submintCheckReason}</span>	                    				
			                       			</div>
			                  			</td>
			                       	</tr>
                                      <tr>
                                          <td class="ab_left"><label class="title">是否给促销员返利：</label></td>
                                          <td class="ab_main" colspan="3">
                                              <div class="content">
                                                  <c:choose>
                                                    <c:when test="${vpsTicketRecord.promotionEarnFlag eq '1'}">返利</c:when>
                                                    <c:otherwise>不返利</c:otherwise>
                                                  </c:choose>
                                              </div>
                                          </td>
                                       </tr>
			                       	<tr>
			                       		<td class="ab_left"><label class="title">客户名称：</label></td>
			                       		<td class="ab_main" colspan="3">
			                       			<div class="content">
			                       				<span>${vpsTicketRecord.checkUserName}</span>	                    				
			                       			</div>
			                  			</td>
			                       	</tr>
			                       	<tr>
			                       		<td class="ab_left"><label class="title">客户审核结果：</label></td>
			                       		<td class="ab_main" colspan="3">
			                       			<div class="content">
			                       				<c:choose>
			                       					<c:when test="${vpsTicketRecord.ticketStatus eq '2'}">
			                       						<span>通过</span>
			                       					</c:when>
			                       					<c:when test="${vpsTicketRecord.ticketStatus eq '1'}">
			                       						<span>不通过</span>
			                       					</c:when>
			                       					<c:otherwise>
			                       						<span>送审中</span>
			                       					</c:otherwise>
			                       				</c:choose>                  				
			                       			</div>
			                  			</td>
			                       	</tr>
			                       	<tr>
			                       		<td class="ab_left"><label class="title">客户审核备注：</label></td>
			                       		<td class="ab_main" colspan="3">
			                       			<div class="content">
			                       				<span>${vpsTicketRecord.remark}</span>	                    				
			                       			</div>
			                  			</td>
			                       	</tr>
			                       	<tr>
			                       		<td class="ab_left"><label class="title">审核时间：</label></td>
			                       		<td class="ab_main" colspan="3">
			                       			<div class="content">
			                       				<span>${vpsTicketRecord.inputTime}</span>	                    				
			                       			</div>
			                  			</td>
			                       	</tr>
                			</c:otherwise>
                			</c:choose>
                		
  
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/smallticketAction/showTicketList.do">返 回</button>
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
