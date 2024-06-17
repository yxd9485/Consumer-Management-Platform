<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String cpath = request.getContextPath(); 
	String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="<%=cpath%>/assets/js/plugins/zonesheets.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
			// 初始化功能
			initPage();
			$('[name="ticketLimit"]').trigger("change");
		});
		
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			// 页面校验
			var v_flag = true;
			$(".validate_tips:not(:hidden)").each(function(){
				if($(this).text() != ""){
					$.fn.alert($(this).text());
					v_flag = false;
				}
			});
			if(!v_flag){
				return false;
			}
			
			return true;
		}
		
		function initPage() {
			if('${activityCog.ticketLimit}'=='0'){
				$(".ticketLimitNum").hide();
			}
			// 当优惠券类型为值得买类型时，中出方式默认无限制并不可选
			/*if ($('.js-select-category-list').val() === 'f1aeda85-f385-11ed-91fa-46ef71ea4d78') {
				$("input[name='ticketLimit']").val('0');
				$(".js-select-ticket-limit").attr('disabled', 'disabled')
			}*/
			// 按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
					var blacklistCogFlag = "0";
					$("input[mark=blacklist]:not([type=radio])").each(function(){
						if($(this).val() != ""){
							blacklistCogFlag = "1";
						}
					});
					$("input[name=blacklistFlag]").val(blacklistCogFlag);
					
					var flag = validForm();
					if(flag) {
						if(btnEvent == "2"){
							if(confirm("确认提交？")){
								return true;
							} else {
								return false;
							}
						} else {
							//$(".js-select-ticket-limit").removeAttr('disabled');
							return true;
						}
					} else {
						return false;
					}
				}
			});
			$('[name="ticketLimit"]').change(function () {
				if($(this).val()=='0' || !$(this).val()){
					$(".ticketLimitNum").hide();
				}else{
					$(".ticketLimitNum").show();
				}

			});
			// 初始化模板状态显示样式
			$("#status").bootstrapSwitch({  
	            onSwitchChange:function(event,state){ 
		            if(state==true){  
		               $("input:hidden[name='status']").val("1");
		            }else{  
	                   $("input:hidden[name='status']").val("0");
		            }
	            }
	        });
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
        	<li class="current"><a> 活动管理</a></li>
        	<li class="current"><a title="">优惠券活动</li>
        	<li class="current"><a title="">修改活动</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vcodeTicketActivity/doTicketActivityEdit.do">
            	<input type="hidden" name="activityKey" value="${activityCog.activityKey}" />
            	<input type="hidden" name="queryParam" value="${queryParam}" />
            	<input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="activityName" tag="validate" value="${activityCog.activityName}"
	                       					class="form-control required" autocomplete="off" maxlength="200" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="startDate" class="form-control input-width-medium Wdate required preTime"
                                       		tag="validate" autocomplete="off" value="${activityCog.startDate}"
                                       		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" class="form-control input-width-medium Wdate required sufTime"
                                       		tag="validate" autocomplete="off" value="${activityCog.endDate}"
                                       		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">优惠券类型：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<select class="form-control input-width-larger js-select-category-list" tag="validate" name="categoryKey" disabled="disabled">
	                       				<c:forEach items="${ticketCategoryList }" var="item">
                       						<option value="${item.categoryKey }"
                       							<c:if test="${activityCog.categoryKey eq item.categoryKey}"> 
                       								selected="selected" 
                       							</c:if>>
                       							${item.categoryName }
                       						</option>
                       					</c:forEach>
                       				</select>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">中出方式：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       				<select class="form-control input-width-larger js-select-ticket-limit" tag="validate" name="ticketLimit">
	                                    	<option value="">请选择</option>
	                                    	<option value="0" <c:if test="${activityCog.ticketLimit eq '0'}"> selected="selected" </c:if>>无限制</option>
	                                    	<option value="1" <c:if test="${activityCog.ticketLimit eq '1'}"> selected="selected" </c:if>>每天</option>
	                                    	<option value="2" <c:if test="${activityCog.ticketLimit eq '2'}"> selected="selected" </c:if>>活动期间</option>
		                       			</select>
										<input name="ticketLimitNum" tag="validate"
											   class="form-control required input-width-small positive ticketLimitNum" id="ticketLimitNum"
											   value="${activityCog.ticketLimitNum}" autocomplete="off" maxlength="11" />
										<span class="blocker en-larger ticketLimitNum" <c:if test="${activityCog.ticketLimit eq '0'}"> hidden </c:if>>次</span>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">活动描述：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <div class="content">
                                        	<textarea rows="5" class="form-control required" autocomplete="off" name="activityDesc" >${activityCog.activityDesc}</textarea>
                                       		<label class="validate_tips"></label>
                        				</div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
									    <input name="status" type="hidden" value=${activityCog.status } />
									    <input id="status" type="checkbox" data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" 
									    data-off-color="warning" <c:if test="${activityCog.status eq '1'}"> checked </c:if>/>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/vcodeTicketActivity/showTicketActivityList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
      			<div class="modal-body">
      				<h6></h6>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
    </div>
  </body>
</html>
