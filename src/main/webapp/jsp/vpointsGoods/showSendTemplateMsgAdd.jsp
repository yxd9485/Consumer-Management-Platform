<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
	String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加模板信息</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script>
		var basePath='<%=cpath%>';
		$(document).ready(function () {
			initPage();
			
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
		});
		
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			var myDate = new Date();//获取系统当前时间
			var sendTime = new Date($("input[name='sendTime']").val()+":00:00");
			if(myDate > sendTime){
				alert("发送时间必须大于当前时间");	
				return false;
			}
			
			var userKey = $("input[name='userKey']").val();
			var minVpoint = $("input[name='minVpoint']").val();
			var maxVpoint = $("input[name='maxVpoint']").val();
			if(minVpoint == '' && maxVpoint == '' && userKey == ''){
				alert("账户积分和指定用户最少选择一项");	
				return false;
			}
			
			// 判断积分区间
			if(minVpoint != '' && maxVpoint != ''){
				if(minVpoint > maxVpoint){
					alert("前一个值不能大于后一个值");
					return false;
				}
			}
			
			// 获取与发送人数
			if(minVpoint != '' || maxVpoint != ''){
				$.ajax({
					url : "${basePath}/sendWechatMsg/queryValidCount.do",
				    data:{
				    		"minVpoint":minVpoint,
				    		"maxVpoint":maxVpoint,
				    		"messageType":$("select[name='messageType']").val()
				    	},
		            type : "POST",
		            dataType : "json",
		            async : false,
		            beforeSend:appendVjfSessionId,
                    success:  function(data){
		            	var estimateSendCount = data.split('-')[0];
		        		var alreadySendCount = data.split('-')[1];
		        		var totalCount = estimateSendCount + alreadySendCount;
		            	if(estimateSendCount == 0){
		        		   $.fn.alert("本次预估推送"+estimateSendCount+"条，请编辑后再提交");
		        		   validateResult = false;
						}else if(estimateSendCount > 0 && totalCount >= 50000){
		        		   $.fn.alert("本次预估推送"+estimateSendCount+"条，当天已推送"+alreadySendCount+"条，超过当天上限5万条，无法推送");
		        		   validateResult = false;
						}else{
							if(confirm("本次预估推送"+estimateSendCount+"条，当天已推送"+alreadySendCount+"条，是否确认提交？")){
								validateResult = true;
							} else {
								validateResult = false;
							}
						}
		            }
			  	});
			}
			if(!validateResult){
				return false;
			}
			return true;
		}
		
		function selectMessageTemplate(messageType, templateType){
			var flag="";
			if("2" == messageType){
				flag = "applet_";
			}
			$(".templateTable").html($("#" + flag + "templateType" + templateType).html());
		}
		
		function initPage() {
			// 初始化模板
			$(".templateTable").html($("#templateType1").html());
			
			// 消息类型
			$("select[name='messageType']").on("change", function(){
				selectMessageTemplate($(this).val(), $("select[name='templateType']").val());
				// 初始化校验控件
				$.runtimeValidate($("#code_form"));
			});
			
			// 模板类型
			$("select[name='templateType']").on("change", function(){
				selectMessageTemplate($("select[name='messageType']").val(), $(this).val());
				// 初始化校验控件
				$.runtimeValidate($("#code_form"));
			});
			
			// 底部按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
					var flag = validForm();
					if(flag) {
						return true;
					} else {
						return false;
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
        	<li class="current"><a> 积分商城</a></li>
        	<li class="current"><a title="">消息推送</a></li>
        	<li class="current"><a title="">新增模板信息</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" 
            	action="<%=cpath%>/sendWechatMsg/doSendTemplateMsgAdd.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>模板信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">消息类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="messageType" class="form-control input-width-larger search" autocomplete="off" >
	                                        <option style="padding: 20px;" value="1">公众号推送</option>
	                                        <option style="padding: 20px;" value="2">小程序推送</option>
	                                    </select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">模板类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="templateType" class="form-control input-width-larger search" autocomplete="off" >
	                                        <option style="padding: 20px;" value="1">服务通知</option>
	                                        <option style="padding: 20px;" value="2">单品推送</option>
	                                        <option style="padding: 20px;" value="3">特殊推送</option>
	                                    </select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">模板跳转：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="urlType" class="form-control input-width-larger search" autocomplete="off" >
	                                        <option style="padding: 20px;" value="0">无跳转</option>
	                                        <option style="padding: 20px;" value="1">跳转商城</option>
	                                    </select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                		<table class="active_board_table templateTable">
                		</table>
                	</div>
                	<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>发送条件</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
				           		<td class="ab_left"><label class="title">发送时间：<span class="required">*</span></label></td>
				           		<td class="ab_main" colspan="3">
				           			<div class="content">
				           				 <input name="sendTime" class="form-control input-width-medium required Wdate" style="width: 160px !important;"
				                          		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH', minDate:'%y-%M-%d'})" />
				           				<label class="validate_tips"></label>
				           			</div>
				           		</td>
				           	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">账户积分：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
	                       				<input name="minVpoint"  
	                       					class="form-control required integer input-width-small preValue" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">至</span>
	                       				<input name="maxVpoint"  
	                       					class="form-control required integer input-width-small sufValue" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">积分</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">账户金额：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
	                       				<input name="minMoney"  
	                       					class="form-control required money input-width-small preValue" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">至</span>
	                       				<input name="maxMoney"  
	                       					class="form-control required money input-width-small sufValue" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">指定账户：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content userKey">
                                        <input name="userKey" tag="validate" style="width: 500px;" 
                                            class="form-control" autocomplete="off" maxlength="400" />
                                        <span style="color: green; margin-left: 5px;">注意：多个userKey使用英文逗号拼接</span>
                                    </div>
	                       		</td>
	                       	</tr>
	                      </table>
	                  </div>
	                  <div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>其他</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
	                       	<tr>
	                       		<td class="ab_left"><label class="title">描述信息：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="describes" class="form-control" autocomplete="off" maxlength="100" style="width: 500px;"></textarea>
                                        <label class="validate_tips"></label>
                                    </div>
	                       		</td>
	                       	</tr>
	                      </table>
	                  </div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/sendWechatMsg/showSendTemplateMsgList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
        
        <!-- 公众号-服务通知模板 -->
        <table id="templateType1" class="active_board_table" style="display: none;">
        	<tr>
           		<td class="ab_left"><label class="title">模板标题：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content title">
           				<input name="first" id="title" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">公司名称：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword1" id="keyword1" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">服务内容：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword2" tag="validate" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">咨询电话：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword3" tag="validate" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">服务备注：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword4" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
               <tr>
                   <td class="ab_left"><label class="title">模板备注：</label></td>
                   <td class="ab_main" colspan="3">
                       <div class="content commendInfo">
                           <input name="remark" id="remark" style="width: 500px;" 
                               class="form-control" autocomplete="off" maxlength="50" />
                           <label class="validate_tips"></label>
                       </div>
                   </td>
               </tr>
        </table>
        
        <!-- 公众号-单品推送 -->
        <table id="templateType2" class="active_board_table" style="display: none;">
        	<tr>
           		<td class="ab_left"><label class="title">模板标题：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content title">
           				<input name="first" id="title" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">物品名称：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword1" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">物品价格：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword2" tag="validate" 
           					class="form-control required integer input-width-small" autocomplete="off" maxlength="9" />
           				<span class="blocker en-larger">积分</span>
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">兑换时间：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				 <input name="keyword3" class="form-control input-width-medium required Wdate" style="width: 160px !important;"
                          		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
               <tr>
                   <td class="ab_left"><label class="title">模板备注：</label></td>
                   <td class="ab_main" colspan="3">
                       <div class="content commendInfo">
                           <input name="remark" id="remark" tag="validate" style="width: 500px;" 
                               class="form-control" autocomplete="off" maxlength="50" />
                           <label class="validate_tips"></label>
                       </div>
                   </td>
               </tr>
        </table>
        
        <!-- 公众号-特殊推送 -->
        <table id="templateType3" class="active_board_table" style="display: none;">
        	<tr>
           		<td class="ab_left"><label class="title">模板标题：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content title">
           				<input name="first" id="title" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">通知内容：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword1" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">联系电话：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword2" tag="validate" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
               <tr>
                   <td class="ab_left"><label class="title">模板备注：</label></td>
                   <td class="ab_main" colspan="3">
                       <div class="content commendInfo">
                           <input name="remark" id="remark" tag="validate" style="width: 500px;" 
                               class="form-control" autocomplete="off" maxlength="50" />
                           <label class="validate_tips"></label>
                       </div>
                   </td>
               </tr>
           </table>
           
           <!-- 小程序-服务通知模板 -->
        <table id="applet_templateType1" class="active_board_table" style="display: none;">
           	<tr>
           		<td class="ab_left"><label class="title">商户名称：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword1" id="keyword1" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">服务类型：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword2" tag="validate" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
            <tr>
                <td class="ab_left"><label class="title">模板备注：</label></td>
                <td class="ab_main" colspan="3">
                    <div class="content commendInfo">
                        <input name="remark" id="remark" style="width: 500px;" 
                            class="form-control" autocomplete="off" maxlength="50" />
                        <label class="validate_tips"></label>
                    </div>
                </td>
            </tr>
        </table>
        
        <!-- 小程序-单品推送 -->
        <table id="applet_templateType2" class="active_board_table" style="display: none;">
           	<tr>
           		<td class="ab_left"><label class="title">服务类型：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword1" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">商品价格：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword2" tag="validate" 
           					class="form-control required integer input-width-small" autocomplete="off" maxlength="9" />
           				<span class="blocker en-larger">积分</span>
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">日期：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				 <input name="keyword3" class="form-control input-width-medium required Wdate" style="width: 160px !important;"
                          		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
               <tr>
                   <td class="ab_left"><label class="title">模板备注：</label></td>
                   <td class="ab_main" colspan="3">
                       <div class="content commendInfo">
                           <input name="remark" id="remark" tag="validate" style="width: 500px;" 
                               class="form-control" autocomplete="off" maxlength="50" />
                           <label class="validate_tips"></label>
                       </div>
                   </td>
               </tr>
        </table>
        
        <!-- 小程序-特殊推送 -->
        <table id="applet_templateType3" class="active_board_table" style="display: none;">
           	<tr>
           		<td class="ab_left"><label class="title">服务类型：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword1" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
           	<tr>
           		<td class="ab_left"><label class="title">电话：<span class="required">*</span></label></td>
           		<td class="ab_main" colspan="3">
           			<div class="content">
           				<input name="keyword2" tag="validate" tag="validate" style="width: 500px;"
           					class="form-control required" autocomplete="off" maxlength="20" />
           				<label class="validate_tips"></label>
           			</div>
           		</td>
           	</tr>
               <tr>
                   <td class="ab_left"><label class="title">模板备注：</label></td>
                   <td class="ab_main" colspan="3">
                       <div class="content commendInfo">
                           <input name="remark" id="remark" tag="validate" style="width: 500px;" 
                               class="form-control" autocomplete="off" maxlength="50" />
                           <label class="validate_tips"></label>
                       </div>
                   </td>
               </tr>
           </table>
        </div>
    </div>
    </div>
  </body>
</html>
