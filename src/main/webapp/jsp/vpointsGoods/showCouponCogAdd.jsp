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
    <title>添加一码双奖活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	
	<script>
	    var basePath='<%=cpath%>';
	    var allPath='<%=allPath%>';
	    var imgSrc=[];
    
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 初始化功能
			initPage();
			
			$("input[name=crowdLimitType]").on("click",function(){
         		if($(this).val() == '2' || $(this).val() == '3'){
         			$("#userGroupTr").css("display","");
         		}else{
         			$("#userGroupTr").css("display","none");
         		}
         	});
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
					$.fn.confirm("确认发布？", function(){
						
			            $(".btnSave").attr("disabled", "disabled");
			            $("#divId").css("display","block");
						
						$("form").attr("action", url);
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
					});
				}
				return false;
			});
            
            // 支付类型、券类型
            $("[name='couponPayType'], [name='couponType']").on("change", function(){
            	var couponType = $("[name='couponType']").val();
            	var $couponTypeDiv = $("div.couponType");
            	
            	// 隐藏
                $couponTypeDiv.find("span").css("display", "none");
                $couponTypeDiv.find("input").val("").attr("disabled", "disabled").css("display", "none");
            	
            	// 依据支付类型及优惠券类型显示 
           		$couponTypeDiv.find("span.couponType" + couponType).css("display", "");
            	if ($("[name='couponPayType']:checked").val() == "0") {
            		$couponTypeDiv.find("input.vpoints.couponType" + couponType).removeAttr("disabled").css("display", "");
            	} else {
                    $couponTypeDiv.find("input.money.couponType" + couponType).removeAttr("disabled").css("display", "");
            	}
            	
            	// 类型为折扣券时
            	if (couponType == "2") {
            		$couponTypeDiv.find("[name='couponDiscount']").removeAttr("disabled").css("display", "");
            	}
            });
            $("[name='couponPayType']").change();
            
            // 适用类型
            $("[name='couponGoodsType']").on("change", function(){
            	var couponGoodsType = $(this).val();
                if (couponGoodsType == "0") {
                	$("tr.goodsLimit").css("display", "none");
                    $("[name='goodsIdAry']").attr("disabled", "disabled");
                } else {
                    $("tr.goodsLimit").css("display", "");
                    $("[name='goodsIdAry']").removeAttr("disabled");
                }
                
                // 单品券时删除多余商品
                if (couponGoodsType == "1") {
                	$("#addGoods").css("display", "none");
                    $("[name='goodsIdAry']:gt(0)").closest("div").remove();
                } else {
                    $("#addGoods").css("display", "");
                }
            });
            $("[name='couponGoodsType']:checked").change();
            
            // 增加商品
            $("form").on("click", "#addGoods", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, false);
                    $copySku.find("#addGoods").text("删除");
                    $(this).closest("td").append($copySku);
                    
                } else {
                    $(this).closest("div").remove();
                }
            });
            
            // 使用截止类型
            $("[name='expireDateType']").on("change", function(){
                if($(this).val() == '0') {
                    $(this).closest("div").find("input[name='expireDateLimit']").removeAttr("disabled");
                    $(this).closest("div").find("input[name='expireDateDays']").attr("disabled", "disabled").val("");
                } else {
                    $(this).closest("div").find("input[name='expireDateLimit']").attr("disabled", "disabled").val("");
                    $(this).closest("div").find("input[name='expireDateDays']").removeAttr("disabled");
                }
            });
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
            if (imgSrc.length > 1) {
                $.fn.alert("最多上传1张");
                return false;
            } else {
                $("[name='couponActivityImgUrl']").val(imgSrc[0]);
            }
            
			return true;
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
    <div id="divId" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>优惠券生成中,请勿其他操作.....</b></h2>
    </div>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 首页</a></li>
        	<li class="current"><a> 积分商城</a></li>
            <li class="current"><a title="">商城优惠券</a></li>
        	<li class="current"><a title="">新增优惠券</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/coupon/doCouponCogAdd.do">
            	<input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
                <input type="hidden" name="couponActivityImgUrl"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>优惠券信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">优惠券名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="couponName" tag="validate"
	                       				    <c:if test="${currentUser.projectServerName eq 'henanpz'}">placeholder='例如：（河南专属）经典500*12'</c:if>
	                       					class="form-control required" autocomplete="off" maxlength="40" style="width: 500px;" />
	                       				<span class="blocker en-larger servershow" data-servershow="henanpz" style="display: none;">优惠券前方需要填写省区属性</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">支付类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio serverdis" name="couponPayType" value="0" data-serverdis="henanpz" style="float:left;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">积分</span>
                                        <input type="radio" class="tab-radio" name="couponPayType" value="1" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">金额</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="couponType" class="form-control input-width-larger">
                                            <option value="0">满减券</option>
                                            <option value="1">直减券</option>
                                            <option value="2">折扣券</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">优惠数值：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content couponType">
	                       				<span class="blocker couponType0 couponType2">满</span>
                                        <input name="moneyFullLimit" class="form-control input-width-medium required money couponType0 couponType2" tag="validate" autocomplete="off" />
                                        <input name="vpointsFullLimit" class="form-control input-width-medium required positive vpoints couponType0 couponType2" tag="validate" autocomplete="off" />
                                        <span class="blocker en-larger couponType0">减</span>
                                        <input name="moneyFullReduction" class="form-control input-width-medium required money couponType0" tag="validate" autocomplete="off" />
                                        <input name="vpointsFullReduction" class="form-control input-width-medium required positive vpoints couponType0" tag="validate" autocomplete="off" />
                                        <span class="blocker couponType1">直减</span>
                                        <input name="moneyDirectReduction" class="form-control input-width-medium required money couponType1" tag="validate" autocomplete="off" />
                                        <input name="vpointsDirectReduction" class="form-control input-width-medium required positive vpoints couponType1" tag="validate" autocomplete="off" />
                                        <span class="blocker en-larger couponType2">折扣</span>
                                        <input name="couponDiscount" class="form-control input-width-medium required number maxValue minValue couponType2" tag="validate" maxVal="10" minVal="0" autocomplete="off" />
                                        <span class="blocker en-larger couponType2">最多优惠</span>
                                        <input name="discountMaxMoney" class="form-control input-width-medium required money couponType2" tag="validate" maxValue="10" minValue="0" autocomplete="off" />
                                        <input name="discountMaxVpoints" class="form-control input-width-medium required integer vpoints couponType2" tag="validate" autocomplete="off" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">适用类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="couponGoodsType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">通用券（全部商品）</span>
                                        <input type="radio" class="tab-radio" name="couponGoodsType" value="1" style="float:left;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">单品券</span>
                                        <input type="radio" class="tab-radio" name="couponGoodsType" value="2" style="float:left;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">指定商品可用券</span>
                                        <input type="radio" class="tab-radio" name="couponGoodsType" value="3" style="float:left;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">指定商品不可用券</span>
                                    </div>
                                </td>
                            </tr>
                            <tr class="goodsLimit">
                                <td class="ab_left"></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control required" name="goodsIdAry" tag="validate" disabled="disabled" style="width: 450px !important;">
                                            <option value="">请选择商品</option>
                                            <c:if test="${!empty goodsLst}">
                                            <c:forEach items="${goodsLst}" var="goods">
                                            <option value="${goods.goodsId}">${goods.goodsName}</option>
                                            </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addGoods">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">投放渠道：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="couponChannel" class="form-control required input-width-larger"  tag="validate">
                                            <option value="">请选择</option>
                                            <option value="0">商城领取 （支持商城领取及扫码发券）</option>
                                            <option value="1">扫码发券</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">发行量：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="couponNum" tag="validate"
                                            class="form-control input-width-small required number integer minValue maxValue" autocomplete="off" maxVal="999999999" minValue="0" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">领取日期：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="receiveStartDate" id="receiveStartDate" class="form-control input-width-medium Wdate required preTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'receiveEndDate\')}'})"  />
                                        <span class="blocker en-larger">至</span>
                                        <input name="receiveEndDate" id="receiveEndDate" class="form-control input-width-medium Wdate required sufTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'receiveStartDate\')}'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">有效日期：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="startDate" id="startDate" class="form-control input-width-medium Wdate required preTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"  />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium Wdate required sufTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">使用截止日期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="expireDateType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">指定日期</span>
                                        <input name="expireDateLimit" class="form-control input-width-medium Wdate required"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <input type="radio" class="tab-radio" name="expireDateType" value="1" style="float:left; margin-left: 10px !important;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">有效天数</span>
                                        <input name="expireDateDays" tag="validate" disabled="disabled"
                                            class="form-control input-width-small required number positive" autocomplete="off" maxlength="3" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">优惠券图片上传：<span class="white">*</span><br/>建议300*300</label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="1">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                     <section class="z_file fl">
                                                        <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                        <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>
                                                     </section>
                                             </div>
                                         </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券详情：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <textarea name="couponDesc" rows="5" tag="validate"
                                            class="form-control" autocomplete="off" maxlength="200" ></textarea>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="widget-header top-only">
                		<h4><i class="iconfont icon-saoma"></i>领取限制</h4>
                    </div>
                	<div class="widget-content panel no-padding filteruser">
                		<table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">领取限制：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">每人每天限制</span>
                                        <input name="personDayLimit" class="form-control input-width-small number positive maxValue" tag="validate" maxVal="99999" autocomplete="off" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">每人每周限制</span>
                                        <input name="personWeekLimit" class="form-control input-width-small number positive maxValue" tag="validate" maxVal="99999" autocomplete="off" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">每人每月限制</span>
                                        <input name="personMonthLimit" class="form-control input-width-small number positive maxValue" tag="validate" maxVal="99999" autocomplete="off" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">每人总数限制</span>
                                        <input name="personTotalLimit" class="form-control input-width-small number positive maxValue" tag="validate" maxVal="99999" autocomplete="off" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">领取人限制：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="crowdLimitType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">不限制</span>
                                        <input type="radio" class="tab-radio" name="crowdLimitType" value="1" style="float:left;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">黑名单用户不可领取</span>
                                        
                                        <c:if test="${not empty(groupList)}">
                                            <span class="blocker en-larger">
                                            	<input name="crowdLimitType" type="radio" value='2'/>
                                            </span>
                                            <span class="blocker en-larger">指定群组参与</span>
                                            <span class="blocker en-larger">
                                            	<input name="crowdLimitType" type="radio" value='3'/>
                                            </span>
                                            <span class="blocker en-larger">指定群组不可参与</span>
                                       	</c:if>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${not empty(groupList)}"> 
	                                <tr id="userGroupTr" style="display: none;">	
	                                	<td class="ab_left"><label class="title"></label></td>
	                                	<td class="ab_main" colspan="3">
	                                		<div>
		                                        <c:forEach items="${groupList }" var="group">
	                                        		<span class="blocker en-larger">
		                                            	<input name="userGroupIds" type="checkbox" value='${group.id }'/>
		                                            </span>
		                                            <span class="blocker en-larger">${group.name}</span>
	                                        	</c:forEach>
		                                    </div>
	                                	</td>
	                                </tr>
								</c:if>
                		</table>
                	</div>
                	<div class="widget-header top-only">
                		<h4><i class="iconfont icon-saoma"></i>其它设置</h4>
                    </div>
                	<div class="widget-content panel no-padding filteruser">
                		<table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">过期提醒提前天数：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="expireRemindDay" class="form-control input-width-larger">
                                            <option value="">请选择</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" >保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/coupon/showCouponCogList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
