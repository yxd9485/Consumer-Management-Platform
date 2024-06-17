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
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	
	<script>
		var basePath='<%=cpath%>';
		var allPath='<%=allPath%>';

		
		$(function(){
			
			 // 全选
             $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='checkboxValue']").prop("checked","checked");
                    var chk_value =[];
                    var chk_name =[];
                    $('input[name="checkboxValue"]:checked').each(function(){
                        chk_value.push($(this).val());
                        chk_name.push($(this).next().html());
                    });
                    $('#projectServer').val(chk_value);
                    $('#projectServerName').val(chk_name);
                } else {
                    $("[name='checkboxValue']").prop("checked","");
                    $('#projectServer').val(null);
                    $('#projectServerName').val(null);
                }
            });
             $("#allCBTwo").on("change", function(){
                 if($(this).prop("checked")){
                     $("[name='checkboxValueTwo']").prop("checked","checked");
                     var chk_value =[];
                     var chk_name =[];
                     $('input[name="checkboxValueTwo"]:checked').each(function(){
                         chk_value.push($(this).val());
                         chk_name.push($(this).next().html());
                     });
                     $('#serverWinery').val(chk_value);
                     $('#serverWineryName').val(chk_name);
                 } else {
                     $("[name='checkboxValueTwo']").prop("checked","");
                     $('#serverWinery').val(null);
                     $('#serverWineryName').val(null);
                 }
             });
         	//复选框弹入弹出
             $("#projectServerName").click(function() {
                 $("#checkboxDiv").toggle();
                 $("#checkboxDivTwo").hide();
             });
             $("#determineDiv").click(function() {
                 $("#checkboxDiv").toggle();
             });
             $("#serverWineryName").click(function() {
                 $("#checkboxDivTwo").toggle();
             });
             $("#determineDivTwo").click(function() {
                 $("#checkboxDivTwo").toggle();
             });
            //复选框选中事件
            $('input[name="checkboxValue"]').click(function(){
                var chk_value =[];
                var chk_name =[];
                $('input[name="checkboxValue"]:checked').each(function(){
                	// console.log($(this).next().html())
                    chk_value.push($(this).val());
                    chk_name.push($(this).next().html());
                });
                console.log(chk_name)
                $('#projectServer').val(chk_value);
                $('#projectServerName').val(chk_name);
            });
            
            
            
            $('input[name="checkboxValueTwo"]').click(function(){
            	 var chk_value =[];
                 var chk_name =[];
                $('input[name="checkboxValueTwo"]:checked').each(function(){
                    chk_value.push($(this).val());
                    chk_name.push($(this).next().html());
                });
                $('#serverWinery').val(chk_value);
                $('#serverWineryName').val(chk_name);
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
				if($('#projectServerName').val() == ""){
					$.fn.alert("省区不可为空");
					return false;
				}
				if($('#serverWineryName').val() == ""){
					$.fn.alert("生产厂商不可为空");
					return false;
				}
				var flag = validForm();
				if (flag) {
					$("input[name='volume']").val(unThousandth($("input[name='volume']").val()));
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
		.projectServerName {
			width: 80px;
			height: 20px;
			border: 1px solid #999999;
			/* background: url(answerImg.png) no-repeat center right; /自己弄一张13px下拉列表框的图标/ */
			}
			.checkboxDiv {
            width: 240px;
			padding-left:5px;
			padding-right:5px;
			color:white;
			position:fixed;
			z-index:99;
			background-color: rgba(100,100,100,1);
			display: none;
			border: 1px solid #999999;
			border-top: none;
			overflow-y:auto; 
			overflow-x:auto; 
			min-height:10px;
			max-height:400px;
			min-width:200px;
		}
		
		.serverWineryName {
			width: 80px;
			height: 20px;
			border: 1px solid #999999;
			/* background: url(answerImg.png) no-repeat center right; /自己弄一张13px下拉列表框的图标/ */
			}
			.checkboxDivTwo {
            width: 240px;
			padding-left:5px;
			padding-right:5px;
			color:white;
			position:fixed;
			z-index:99;
			background-color: rgba(100,100,100,1);
			display: none;
			border: 1px solid #999999;
			border-top: none;
			overflow-y:auto; 
			overflow-x:auto; 
			min-height:10px;
			max-height:400px;
			min-width:200px;
		}
			    
			    
			    
			    
			    
	    .determineCls {
	        width: 76px;
	        height: 20px;
	        line-height: 20px;
	        border: 1px solid #999999;
	        font-size: 12px;
	        margin: 3px auto;
	        border-radius: 5px;
	        text-align: center;
	        cursor: pointer;
	    }
	    
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
            <li class="current"><a title="">基础配置</a></li>
            <li class="current"><a title="">赋码厂列表</a></li>
            <li class="current"><a title="">创建赋码厂</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vcodeFactory/doVcodeFactoryAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
            	<input type="hidden" name="skuLogo"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>创建赋码厂</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">赋码厂名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="factoryName" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">赋码厂缩写：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="factoryShort" tag="validate"  
	                       					class="form-control input-width-larger required varlength" autocomplete="off" data-length="10" maxlength="10" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                                <td class="ab_left"><label class="title">赋码厂类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="factoryType"tag="validate"  
                                            class="form-control input-width-larger required">
											<c:choose>
												<c:when test="${!fn:startsWith(currentUser.projectServerName, 'mengniu')}">
													<option value="1">瓶盖厂</option>
													<option value="2">拉环厂</option>
													<option value="3">纸箱厂</option>
												</c:when>
												<c:otherwise>
													<option value="4">支码厂</option>
													<option value="3">纸箱厂</option>
												</c:otherwise>
											</c:choose>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">通道数量：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="channelCount" tag="validate"  
	                       					class="form-control input-width-larger number positive required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	<tr>
                                <td class="ab_left"><label class="title">服务的省区：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <input id="projectServerName"   autocomplete="off" name="projectServerName"  type="text"  class="form-control input-width-larger required" readonly="true" placeholder="点击选择">
									<input type="hidden" id="projectServer" tag="validate" class="form-control input-width-larger required"  name="projectServer" type="text"/>
									<div id="checkboxDiv" class="checkboxDiv" style="line-height: -10px;">
										<label><input type="checkbox" name="allCB" id="allCB">全选</label><br>
										<c:forEach items="${projectServerLst}" var="item">
												<input type="checkbox"   name="checkboxValue" value="${item.projectServerName}"><label>${item.serverName}</label><br>
										</c:forEach>
										<div id="determineDiv" class="determineCls">确定</div>
									</div>
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="ab_left"><label class="title">服务的酒厂：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <input id="serverWineryName" autocomplete="off" name="serverWineryName" type="text"  class="form-control input-width-larger required" readonly="true" placeholder="点击选择">
                                    <input type="hidden" id="serverWinery" name="serverWinery" type="text"/>
									<div id="checkboxDivTwo" class="checkboxDivTwo" style="line-height: -10px;">
										<label><input type="checkbox" name="allCBTwo" id="allCBTwo">全选</label><br>
										<c:forEach items="${wineryLst}" var="item">
												<input type="checkbox" name="checkboxValueTwo" value="${item.id}"><label>${item.wineryName}</label><br>
										</c:forEach>
										<div id="determineDivTwo" class="determineCls">确定</div>
									</div>
                                </td>
                            </tr>
                            
                            <tr>
	                       		<td class="ab_left"><label class="title">耗损比例：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="lossRatio" tag="validate"  
	                       					class="form-control input-width-larger number rule" autocomplete="off" maxlength="4" />
	                       				<span class="blocker en-larger">%</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	<tr>
	                       		<td class="ab_left"><label class="title">每批次数量：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="batchNum" tag="validate"  
	                       					class="form-control input-width-larger number positive required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">码包联系人：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="bagName" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">码包联系人电话：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="bagTel" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" oninput="value=value.replace(/[^\d]/g,'')"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">码包联系人邮箱：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="bagEmail" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="100" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                     	<tr>
	                       		<td class="ab_left"><label class="title">密码联系人：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="passwordName" tag="validate"  
	                       					class="form-control input-width-larger required required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                        	<tr>
	                       		<td class="ab_left"><label class="title">密码联系人电话：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="passwordTel" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" oninput="value=value.replace(/[^\d]/g,'')"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                      
	                      	<tr>
	                       		<td class="ab_left"><label class="title">密码联系人邮箱：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="passwordEmail" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="100" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                                <td class="ab_left"><label class="title">是否生成标签：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="isLabel"tag="validate"  
                                            class="form-control input-width-larger required">  
                                            <option value="0">否</option>    
                                            <option value="1">是</option>  
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">二维码格式：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="qrcodeFormat"tag="validate"  
                                            class="form-control input-width-larger required">  
                                            <option value="1">二维码</option>    
                                            <option value="2">二维码,串前6,串后6</option> 
                                            <option value="3">二维码,串码</option> 
                                            <option value="4">二维码 串前6 串后6 </option>
                                            <option value="5">二维码,,串前6,串后6</option>
                                            <option value="6">二维码 串码</option>
                                            <option value="7">二维码,串前6,串后6,序号</option>
                                            <option value="8">二维码,串码,序号(主推)</option>
                                            <option value="9">二维码 串码 序号(主推)</option>
                                            <option value="10">二维码 串前6 串后6 序号</option>
                                            <option value="11">序号 二维码</option>
                                            <option value="12">序号 二维码,串前6,串后6</option>
                                            <option value="13">序号 二维码,串码</option>
                                            <option value="14">序号 二维码 串前6 串后6</option>
                                            <option value="15">序号,二维码,串码</option>
                                            <option value="16">批次序号,二维码</option>
                                            <option value="17">序号,二维码,串前6,串后6</option>
                                            <option value="18">二维码,,串码,串前6,串后6</option>
                                            <option value="19">二维码,串码,串前6,串后6</option>
                                            <option value="20">二维码 奖项描述</option>
                                            <option value="21">二维码,奖项描述</option>
                                            <option value="22">二维码;奖项描述</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">是否一万一批次：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select  tag="validate" class="form-control input-width-larger required" id="isWanBatch" name="isWanBatch">
	                       					<option value="">请选择</option>
	                       					<option value="0">否</option>
	                       					<option value="1">是</option>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">是否合并码包文件：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select  tag="validate" class="form-control input-width-larger required" id="qrcodeMerageFlag" name="qrcodeMerageFlag">
	                       					<option value="">请选择</option>
	                       					<option value="0">否</option>
	                       					<option value="1">是</option>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/vcodeFactory/showVcodeFactoryList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
  </body>
</html>
