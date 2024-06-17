<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>修改一万一批次激活人员</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath}/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="${basePath}/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath}/assets/js/custom/giftspack/utils.js"></script>
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			$("select[name=userStatus]").change(function(){
				if($(this).val() == '0'){
					$("select[name=userPrivilege]").attr("disabled", true);
				}else{
					$("select[name=userPrivilege]").attr("disabled", false);
				}
			});
			
		});

	    // 表单校验 
        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
            return true;
        }
		
	</script>
	
	<style>
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
		.validate_tips {
			padding: 8px !important;
		}
		div b{
			color:red;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><i class="icon-home"></i><a> 激活人员管理</a></li>
        	<li class="current"><a title="">修改激活人员</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="addBatchInfoForm"
            	action="${basePath}/batchActivate/doActivateCogEdit.do" onsubmit="return validForm();">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>激活人员</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                        <input type="hidden" name="isCheck" value="${isCheck}" />
                        <input type="hidden" name="openid" value="${activateCog.openid}" />
                        <input type="hidden" name="infoKey" value="${activateCog.infoKey}" />
                        <input type="hidden" name="phoneNum" value="${activateCog.phoneNum}" />
                        <input type="hidden" name="userName" value="${activateCog.userName}" />
                        <input type="hidden" name="factoryName" value="${activateCog.factoryName}" />
                        <input type="hidden" name="oldUserStatus" value="${activateCog.userStatus}" />
                        <input type="hidden" name="oldUserPrivilege" value="${activateCog.userPrivilege}" />
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                		<table class="active_board_table">
                			<tr style="display: none;">
	                       		<td class="ab_left"><label class="title">openid：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${activateCog.openid}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">激活人员：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${activateCog.userName}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">手机号：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${activateCog.phoneNum}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">所属工厂：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${activateCog.factoryName }</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户状态：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="userStatus" <c:if test="${isCheck eq '1'}"> disabled </c:if>>
		                       				<option value="2" 
		                       					<c:if test="${activateCog.userStatus eq '2' or isCheck eq '1'}"> selected </c:if>>正常</option>
	                                        <option value="0" <c:if test="${activateCog.userStatus eq '0'}">selected</c:if>>停用</option>
                                         </select>
                                         <c:if test="${isCheck eq '1'}">
											<input type="hidden" name="userStatus" value="2"/>
										 </c:if>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">权限分配：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="userPrivilege" <c:if test="${activateCog.userStatus eq '0'}">disabled</c:if>>
		                       				<option value="1" <c:if test="${activateCog.userPrivilege eq '1'}">selected</c:if>>激活、查询检测</option>
	                                        <option value="2" <c:if test="${activateCog.userPrivilege eq '2'}">selected</c:if>>查询检测</option>
                                         </select>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<!-- 
	                       	<tr>
	                       		<td class="ab_left"><label class="title">授权项目：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="serverName" 
	                       					value=""
	                       					class="form-control input-width-xlarge" autocomplete="off" maxlength="200" />
	                       				<span style="color: green;margin-left: 5px; line-height: 2;" >注：请参考V码通用中各项目filter的serverName，格式：serverName1,serverName2...</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title"></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<ul style="color: green; padding:0px; line-height: 2; list-style: none; ">
	                       					<li>项目标志对应关系：</li>
	                       					<li>vmts:终端促销</li>
	                       					<li>beixiao:北销青啤、BYinMai:奥麦、qpec:青啤电商、fujian:福建青啤、guangxi:广西青啤、hainan:海南青啤</li>
	                       					<li>hebei:河北青、heilj:黑龙江青啤、huanan:华南青啤、hunan:湖南青啤、jiangxi:江西青啤、qmbaipi:全麦白啤</li>
	                       					<li>sichuan:四川青啤、shandongagt:山东奥古特、shanxi:山西青啤、xianqp:陕西青啤、XinYM:新银麦、yunnan:云南青啤、zhejiang:浙江青啤</li>
	                       				</ul>
	                       			</div>
	                       		</td>
	                       	</tr> 
	                       	-->
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-primary btnSave" data-event="1" type="submit">保 存</button>
					    	<a class="btn btnReturn"  href="${basePath}/batchActivate/showActivateCogList.do?vjfSessionId=${vjfSessionId}">返 回</a>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
