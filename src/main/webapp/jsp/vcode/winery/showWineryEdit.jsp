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
    <title>产品生产工厂管理</title>
    
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
			//复选框弹入弹出
            $("#projectServerName").click(function() {
                $("#checkboxDiv").toggle();
               //获取所有复选框的值
   			 	var checkBoxAll= $("input[name='checkboxValue']");		
   	         	var projectServerName='${vpsWinery.projectServer}';
   	         	//拆分
   	 		 	var checkArray =projectServerName.split(","); 	 
   	 		 	for(var i=0;i<checkArray.length;i++){
   	             //获取所有复选框对象的value属性，然后，用checkArray[i]和他们匹配，如果有，则说明他应被选中
   	             $.each(checkBoxAll,function(j,checkbox){
   	                //获取复选框的value属性
   	                 var checkValue=$(checkbox).val();
   	                 if(checkArray[i]==checkValue){
   	                     $(checkbox).attr("checked",true);
   	                 }
   	             })
   	           }
            });
			
			
            $("#determineDiv").click(function() {
                $("#checkboxDiv").toggle();
            });
          
            //复选框选中事件
            $('input[name="checkboxValue"]').click(function(){
                var chk_value =[];
                var chk_name =[];           	 		 	
                $('input[name="checkboxValue"]:checked').each(function(){               	
                    chk_value.push($(this).val());
                    chk_name.push($(this).next().html());
                });
                $('#projectServer').val(chk_value);
                $('#projectServerName').val(chk_name);
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
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a title="">基础配置</a></li>
            <li class="current"><a title="">产品生产工厂列表</a></li>
            <li class="current"><a title="">修改产品生产工厂</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vcodeWinery/doWineryEdit.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
            	<input type="hidden" name="id" value="${vpsWinery.id}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>产品生产工厂信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">产品生产工厂名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="wineryName" tag="validate"  
	                       					class="form-control input-width-larger required" value="${vpsWinery.wineryName}" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">产品生产工厂简称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="wineryShort" tag="validate"  
	                       					class="form-control input-width-larger required varlength" value="${vpsWinery.wineryShort}" autocomplete="off" data-length="10" maxlength="10" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	<tr>
                                <td class="ab_left"><label class="title">服务的省区：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                     <input id="projectServerName" autocomplete="off" name="projectServerName" value="${vpsWinery.projectServerName}" readonly="true"   type="text"  class="form-control input-width-larger" placeholder="点击选择">
									<input type="hidden" id="projectServer" name="projectServer" type="text"/>
									<div id="checkboxDiv" class="checkboxDiv" style="line-height: -10px;">
										<label><input type="checkbox" name="allCB" id="allCB">全选</label><br>
										<c:forEach items="${projectServerLst}" var="item">
												<input type="checkbox" name="checkboxValue" value="${item.projectServerName}"><label>${item.serverName}</label><br>
										</c:forEach>
										<div id="determineDiv" class="determineCls">确定</div>
									</div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave marr20">保 存</button>
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/vcodeWinery/showWineryList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
