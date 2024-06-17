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
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>SKU管理</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	
	<script>
		$(function(){
			
			// 初始化省市县
            $("div.area").initZone("<%=cpath%>", true, "${promotionUser.areaCode}", true, false, false);

			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 初始化功能
			initPage();
		});
		
		function initPage() {
			
			  // 返回
            $(".btnReturn").click(function(){
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });
            
            // 保存
			$(".btnSave").click(function(){
				var flag = validForm();
				if (flag) {
					var url = $(this).attr("url");
					$("form").attr("action", url);
					$("form").attr("onsubmit", "return true;");

					if('${promotionUser.isJoinVote}' == '1'){
                        $.fn.confirm("该用户已参与评选，此次修改后用户需要重新激活，重新报名评选活动，是否继续？", function() {
                            $("form").submit();
                        });
                    }else if('${promotionUser.status}' == '1'){
						$.fn.confirm("该用户已激活，此次修改后用户需要重新激活，是否继续？", function() {
							$("form").submit();
						});
					}else{
						$("form").submit();
					}
					
				}
				return false;
			});
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			 var areaCode = "";
			// 组建筛选区域
            $("td.area div.area").each(function (i) {
                var $province = $(this).find("select.zProvince");
                var $city = $(this).find("select.zCity");
                var $county = $(this).find("select.zDistrict");
                if ($county.val() != "") {
                    areaCode = areaCode + $county.val() + ",";
                } else if ($city.val() != "") {
                    areaCode = areaCode + $city.val() + ",";
                } else if ($province.val() != "") {
                    areaCode = areaCode + $province.val() + ",";
                } else {
                    areaCode = "";
                    return false;
                }
            	$("input[name='province']").val($province.find("option:selected").text());
           	 	$("input[name='city']").val($city.find("option:selected").text());
            	$("input[name='county']").val($county.find("option:selected").text());
            });
            $("input[name='areaCode']").val(areaCode);
            if(areaCode == ""){
				alert("请筛选区域！")
				return false;
			}
            if($("input[name='city']").val() == "--"){
                alert("请筛选市！")
                return false;
            }
            if($("input[name='county']").val() == "--"){
                alert("请筛选县区！")
                return false;
            }
            
			return true;
		}
		
	</script>
	
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
			text-align: center;
			
		}
		
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a title="">小票审核</a></li>
            <li class="current"><a title="">${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }管理</a></li>
            <li class="current"><a title="">${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }修改</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                action="<%=cpath %>/promotionUserAction/doPromotionUserEdit.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="infoKey" value="${promotionUser.infoKey}" />
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">姓名：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="userName" tag="validate"  value="${promotionUser.userName}"
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>   
                            <tr>
                                <td class="ab_left"><label class="title">手机号：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="phoneNum" tag="validate"  value="${promotionUser.phoneNum}"
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="11" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">外勤365编号：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="userCode" tag="validate"  value="${promotionUser.userCode}"
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="36" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>   
                            <tr>
                                <td class="ab_left"><label class="title">战区： </label></td>
                                <td class="ab_main activity">
                                    <div class="content activity" style="margin: 5px 0px; display: flex;">
                                        <select class="form-control input-width-larger" id="warAreaName" name="warAreaName">
                                            <c:forEach items="${ticketWarareaList }" var="item">
                                                <option value="${item.warAreaName }" <c:if test="${item.warAreaName eq promotionUser.warAreaName}"> selected </c:if> >${item.warAreaName }</option>
                                            </c:forEach>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">所属渠道： </label></td>
                                <td class="ab_main activity">
                                    <div class="content activity" style="margin: 5px 0px; display: flex;">
                                        <select class="form-control input-width-larger" id="ticketChannel" name="ticketChannel">
                                            <option value="">请选择</option>
                                            <option value="1" <c:if test="${promotionUser.ticketChannel eq '1'}"> selected </c:if> >餐饮渠道</option>
                                            <option value="2" <c:if test="${promotionUser.ticketChannel eq '2'}"> selected </c:if> >KA渠道</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">系统：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="terminalSystem" tag="validate"  value="${promotionUser.terminalSystem}"
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="10" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">门店编号：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="terminalCode" tag="validate"  value="${promotionUser.terminalCode}"
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">门店名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="terminalName" tag="validate"  value="${promotionUser.terminalName}"
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title"> 门店地址：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="terminalAddress" tag="validate"  value="${promotionUser.terminalAddress}"
                                            class="form-control input-width-larger required varlength" autocomplete="off" data-length="80" maxlength="80" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                             <tr>
                                <input type="hidden" name="areaCode"/>
                                <input type="hidden" name="province"/>
                                <input type="hidden" name="city"/>
                                <input type="hidden" name="county"/>

                                <td class="ab_left"><label class="title">所属区域：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main area" colspan="3">
                                    <div class="content area" style="display: flex; margin: 5px 0px;">
                                        <select  name="provinceAry"
                                                class="zProvince form-control input-width-normal required"></select>
                                        <select  name="cityAry"
                                                class="zCity form-control input-width-normal required"></select>
                                        <select  name="countyAry"
                                                class="zDistrict form-control input-width-normal required"></select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>   
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保存后请提醒${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }重新激活</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/promotionUserAction/showPromotionUserList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>
