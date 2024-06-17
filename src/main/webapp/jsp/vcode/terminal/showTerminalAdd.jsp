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
		var basePath='<%=cpath%>';
		var allPath='<%=allPath%>';
		var imgSrc=[];

    	// 本界面上传图片要求
    	var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };

		
		$(function(){
			
			// 初始化省市县
            $("div.area").initZone("<%=cpath%>", true, "${areaCode}", true, false, false);


        	// 增加大奖下拉框
            $("form").on("click", "#addPrize", function() {
            	var idx = $("td.activity div").index($(this).parent("div.activity"));
                if (idx == 0) {
                    var count = $("td.activity div").length;
                    if(count < 10){
                        var $activityCopy = $("div.activity").eq(0).clone(true);
                        $("td.activity").append($activityCopy);
                        $activityCopy.find("#addPrize").text("删除");
                        var $activity = $("td.activity div.activity:last").find("input[name='prizeKyes']");
                        $activity.attr("id", "prizeKyes" + count).val("");
                    }
                } else {
                    $(this).parent("div.activity").remove();
                }
            });
		
			  
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
					$("form").submit();
				}
				return false;
			});
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			if (imgSrc.length > 1) {
				$.fn.alert("SKU图标最多上传1张");
				return false;
			} else {
				$("[name='imageUrl']").val(imgSrc[0]);
			}
			
			var coordinate = $("[name='coordinate']").val().split(',');
			if(coordinate[0] > 180){
				alert("经度不能超出180,请重新选择门店坐标！")
				return false;
			}
			if(coordinate[1] > 90){
				alert("纬度不能超出90,请重新选择门店坐标！")
				return false;
			}
			$("[name='longitude']").val(coordinate[0]);
			$("[name='latitude']").val(coordinate[1]);
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
                    areaName = "";
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
            // 组装活动KEY
			var activityAry = "";
			$("td.activity div.activity").each(function(i){
            	var $activity = $(this).find("select[name='prizeKyes']");
            	if($activity.val() != ""){
            	if(activityAry.indexOf($activity.val()) == -1){
	            	activityAry += $activity.val() + ",";
            	  }                    	
            	}
            });
			if(activityAry != ""){
				$("input[name='prizeTyleLst']").val(activityAry.substring(0, activityAry.length - 1));				
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
            <li class="current"><a title="">模块化</a></li>
            <li class="current"><a title="">门店管理</a></li>
            <li class="current"><a title=""> 新增门店</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/terminal/createTerminal.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="imageUrl"/>
                <input type="hidden" name="longitude"/>
                <input type="hidden" name="latitude"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>新增门店</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
								
							 <tr>
                                <input type="hidden" name="areaCode"/>
                				<input type="hidden" name="province"/>
                            	<input type="hidden" name="city"/>
                            	<input type="hidden" name="county"/>

                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label>
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
								
                			<tr>
	                       		<td class="ab_left"><label class="title">门店名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="terminalName" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title"> 门店地址：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="address" tag="validate"  
	                       					class="form-control input-width-larger required varlength" autocomplete="off" data-length="80" maxlength="80" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	<tr>
	                       		<td class="ab_left"><label class="title"> 门店坐标：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="coordinate" tag="validate"  
	                       					class="form-control input-width-larger required varlength" autocomplete="off" data-length="50" maxlength="50" />
	                       				<div class="content">
	                       					<a class="marl15 preview-batch" style="text-decoration: none; color: rgb(77, 116, 150);" target="_blank" 
	                       					href="http://api.map.baidu.com/lbsapi/getpoint/index.html"
	                       					>获取坐标</a>
	                       				</div>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title"> 联系电话：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="phoneNum" tag="validate"  
	                       					class="form-control input-width-larger required varlength" autocomplete="off" data-length="15" maxlength="15" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	<tr style="height: 163px;">
		                       	<td class="ab_left"><label class="title">图片上传：<span class="white">*</span><br/>建议500*500</label></td>
		                       	<td class="ab_main" colspan="3">
									<div style="height: 163px;width: 800px; float: left;" class="img-box full">
										<section class=" img-section">
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
											<p class="del-p ">您确定要删除作品图片吗？</p>
											<p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>
										</div>
									</aside>
		                       	</td>
	                       	</tr>
	                       	

	            
              
	                       	<tr>
	                       		<td class="ab_left"><label class="title">可兑奖品： </label></td>
	                       		<td class="ab_main activity">
	                       			<input type="hidden" name="prizeTyleLst" />
	                       			<div class="content activity" style="margin: 5px 0px; display: flex;">
	                       				<select class="form-control input-width-larger" id="prizeKyes" name="prizeKyes">
	                       					<option value="">请选择奖品</option>
	                       					<c:forEach items="${bigPrizes }" var="item">
	                       						<option value="${item.prizeNo }">${item.prizeName }</option>
	                       					</c:forEach>
	                       				</select>
	                       				<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addPrize">新增</label>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                           
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/terminal/showTerminalList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
  </body>
</html>
