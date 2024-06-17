<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% 
    String cpath = request.getContextPath();
	String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
	String pathPrefix = cpath + "/" + imagePathPrx;
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>青岛啤酒广西活动运营后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="${basePath }/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath }/inc/pagination/jquery.pagination.js?v=1"></script>
	<script type="text/javascript" src="${basePath }/inc/custom/form-actions.js?v=2"></script>
      <script type="text/javascript" src="${basePath }/assets/js/libs/jquery.qrcode.min.js"></script>
	
	<script>
        function myFunction(key){
            // var key = $(this).parents("td").data("key");
            $("#addBatchDialog").modal("show");
            outputQRCod(key, 280, 280);
        }
        //生成二维码
        function outputQRCod(txt, width, height) {
            //先清空
            $("#qrcode").empty();
            //中文格式转换
            var str = toUtf8(txt);
            //生成二维码
            $("#qrcode").qrcode({
                render: "canvas",//canvas和table两种渲染方式
                width: width,
                height: height,
                text: str
            });
        }
        function toUtf8(str) {
            var out, i, len, c;
            out = "";
            len = str.length;
            for (i = 0; i < len; i++) {
                c = str.charCodeAt(i);
                if ((c >= 0x0001) && (c <= 0x007F)) {
                    out += str.charAt(i);
                } else if (c > 0x07FF) {
                    out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                    out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                } else {
                    out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                }
            }
            return out;
        }
		$(function(){
			
	        // 查看
	        $("a.btnView").off();
	        $("a.btnView").on("click", function(){
	            var key = $(this).parents("td").data("key");
	            var url = "${basePath}/major/showMajorInfoView.do?infoKey="+key;
	            $("form").attr("action", url);
	            $("form").submit(); 
	        });
	        
            // 查询结果导出
            $("a.prizeExport").on("click", function(){
            	if(!validForm){
            		return false;
            	}
                $("form").attr("action", "<%=cpath%>/major/exportPrizeList.do");
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/major/showMajorInfoList.do");
                
            });
            
          	//复选框弹入弹出
            $("#checkboxImg").click(function() {
                $("#checkboxDiv").toggle();
            });
            $("#determineDiv").click(function() {
                $("#checkboxDiv").toggle();
            });
            //复选框选中事件
            $('input[name="checkboxValue"]').click(function(){
                /*if( $('input[name="checkboxValue"]').checked(true)){
                    alert(1)
                }*/
                var chk_value =[];
                $('input[name="checkboxValue"]:checked').each(function(){
                    chk_value.push($(this).val());
                });
                //alert(chk_value);
                $('#checkboxImg').val(chk_value);
            });
            
            // 弹出修改批次窗口
            $("a.btnCheck").off();
            $("a.btnCheck").on("click", function(){
                
                // 弹出对话框
                $("#expressDialog input[name]").val("");
                $("#expressDialog span[id]").text("");
                $("#expressDialog").modal("show");
                
                // 标题
                if ($(this).text() == '发货') {
                    $("#expressDialog #myModalLabel").text("物流发货");
                }else{
                    $("#expressDialog #myModalLabel").text("修改物流信息");
                }
                
                // 订单主键
                var key = $(this).parents("td").data("key");
                $("#expressDialog").find("input[name='infoKey']").val(key);
                $("#expressDialog").find("textarea[name='expressSendMessage']").val($(this).closest("tr").find("span[id='expressSendMessage']").text());
                // 订单信息
                $(this).closest("tr").find("span[id]").each(function(){
                    $("#expressDialog").find("span[id='" + $(this).attr("id") + "']").text($(this).text());
                    $("#expressDialog").find("input[name='" + $(this).attr("id") + "']").val($(this).text());
                });
            });

            // 确定修改物流单号
            $.runtimeValidate($("#expressDialog"));
            $("#expressDialog").delegate("#addBtn", "click", function(){
                // 输入元素校验
                var validateResult = $.submitValidate($("#expressDialog"));
                if(!validateResult){
                    return false;
                }
                
                // 提交表单
                var url = "<%=cpath%>/major/checkMajorInfo.do";
                var paramJson = {};
                $("#expressDialog input[name]").each(function(){
                    paramJson[$(this).attr("name")] = $(this).val();    
                });
                paramJson["expressSendMessage"] = $("[name='expressSendMessage']").val();
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#expressDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });
        });
		
		function changeData(param){
    		var dataNew=param.replaceAll(";",",");
    		$("input[name=checkboxImg]").val(dataNew);
            var datas=param.split(";");
            for(j = 0; j < datas.length; j++) {
            	   var va=datas[j];
            	   $('input[name="checkboxValue"]').each(function(){
            		   var ck=$(this).val();
            		   if(ck==va){
            			   $(this).prop("checked", true);
            		   }
                   });   
            } 
    	}
		
		function validForm(){
			// 中奖时间
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			
			// 领取时间
			var useStartDate = $("#useStartDate").val();
			var useEndDate = $("#useEndDate").val();
			
			// 兑奖时间
			var checkStartDate = $("#checkStartDate").val();
			var checkEndDate = $("#checkEndDate").val();
			
			if(startDate == '' && endDate == '' && useStartDate == '' && useEndDate == '' && checkStartDate == '' && checkEndDate == ''){
				$.fn.alert("中奖时间、领取时间和兑奖时间必须填写其中一个！");
				return false;
			}
			
			var msg = '';
			msg = checkTime(startDate, endDate);
			if(msg != ''){
				$.fn.alert("中奖" + msg);
				return false;
			}
			
			msg = checkTime(useStartDate, useEndDate);
			if(msg != ''){
				$.fn.alert("领取" + msg);
				return false;
			}
			
			msg = checkTime(checkStartDate, checkEndDate);
			if(msg != ''){
				$.fn.alert("兑奖" + msg);
				return false;
			}
			
			return true;
		}
		
		function checkTime(startDate, endDate){
			if(startDate != '' || endDate != ''){
				if(startDate == ''){
					return "时间的开始时间不能为空！";
				}else if(endDate == ''){
					return "时间的结束时间不能为空！";
				}else{
					if(timeDifferenceForDay(startDate, endDate) > 31){
						return "时间设置范围必须小于31天！";
					}
				}	
			}
			return "";
		}
		
		// 计算两个时间相差的天数
		function timeDifferenceForDay(start, end){
			start=start.replace(/-/g,"/");
			var startdate=new Date(start);
			end=end.replace(/-/g,"/");
			var enddate=new Date(end);
			var time=enddate.getTime()-startdate.getTime();
			var days=parseInt(time/(1000 * 60 * 60 * 24));
			return days;
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
        .modal-body1 {
            display: block;
        }
		.checkboxImg {
			width: 80px;
			height: 20px;
			border: 1px solid #999999;
			/* background: url(answerImg.png) no-repeat center right; /自己弄一张13px下拉列表框的图标/ */
			}
			.checkboxDiv {
			width: 240px;
			padding-left:5px;
			color:white;
			position:fixed;
			margin-left:94px;
			margin-top:-10px;
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
        	<li class="current"><a>首页</a></li>
        	<li class="current"><a>消费者管理</a></li>
        	<li class="current"><a title="">中出用户</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">中奖用户查询</span></div>
                   <div class="col-md-10 text-right">
                        <a class="btn btn-blue prizeExport"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 大奖列表导出 </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/major/showMajorInfoList.do" onsubmit="return validForm();">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                    <div class="row">
                        <div class="col-md-12 ">
                            <div class="form-group little_distance search" style="line-height: 35px;position: relative;">
                                <div class="search-item">
	                                <label class="control-label">中出类型：</label>
									<input id="checkboxImg" autocomplete="off" name="checkboxImg" type="text"  value="" class="form-control input-width-larger" placeholder="点击选择">
									<div id="checkboxDiv" class="checkboxDiv" style="line-height: -10px;">
										<c:forEach items="${prizeTypeMap}" var="item">
											<label><input type="checkbox" name="checkboxValue" value="${item.key}">${item.value}</label><br>
										</c:forEach>
										<div id="determineDiv" class="determineCls">确定</div>
									</div>
								</div>
                                <div class="search-item">
                                    <label class="control-label">中出手机号：</label>
                                    <input name="phoneNum" class="form-control input-width-larger" autocomplete="off" maxlength="11" oninput="value=value.replace(/[^\d]/g,'')"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">中出时间：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">中出V码：</label>
                                    <input name="prizeVcode" class="form-control input-width-larger" autocomplete="off" maxlength="12" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">领取时间：</label>
                                    <input name="useStartDate" id="useStartDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'useEndDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="useEndDate" id="useEndDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'useStartDate\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">兑出时间：</label>
                                    <input name="checkStartDate" id="checkStartDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'useEndDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="checkEndDate" id="checkEndDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'useStartDate\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">兑出人手机号：</label>
                                    <input name="checkPhoneNum" class="form-control input-width-larger" autocomplete="off" maxlength="11" oninput="value=value.replace(/[^\d]/g,'')"/>
                                </div>
                                <div class="search-item">
	                                <label class="control-label">兑出状态：</label>
	                                <select name="checkStatus" tag="validate" class="form-control input-width-larger search">
	                                    <option value="">全部</option>
	                                    <c:if test="${projectServerName eq 'anhui' or projectServerName eq 'hebei'}">
		                                <option value="0">未兑奖</option>
		                                </c:if>
		                                <option value="1">已兑奖</option>
		                                <option value="9">已过期</option>
		                                <option value="10">已折现</option>
	                                </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">兑出门店：</label>
                                    <input name="terminalName" class="form-control input-width-larger" autocomplete="off" maxlength="11"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                       <div class="col-md-12 text-center mart20">
                           <button type="button" class="btn btn-primary btn-blue">查 询</button>
                           <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
                       </div>
                    </div>
                    </form>
                </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">中出用户列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                               id="dataTable_data">
                            <thead>
					            <tr>
                                  <th style="width:4%;">序号</th>
                                  <!-- <th style="width:8%;" data-ordercol="u.nick_name">昵称</th> -->
                                  <th style="width:9%;" data-ordercol="prizeType">中出类型</th>
                                  <th style="width:7%;" data-ordercol="s.SKU_NAME">中出SKU</th>
                                  <th style="width:6%;" data-ordercol="p.prize_vcode">二维码</th>
                                  <th style="width:9%;" data-ordercol="p.phone_num">中出用户手机号码</th>
                                  <th style="width:7%;" data-ordercol="p.prize_vcode">兑换金额（元）</th>
                                  <th style="width:15%;" data-ordercol="p.phone_num">中出用户地址</th>
                                  <th style="width:10%;" data-ordercol="p.earn_time" data-orderdef>中出/领取时间</th>
                                  <th style="width:9%;" data-ordercol="p.check_time">兑出/截止时间</th>
                                  <th style="width:9%;" data-ordercol="cu.phone_num">兑出人手机号</th>
                                  <th style="width:4%;" data-ordercol="p.check_status">状态</th>
                                  <c:if test="${projectServerName eq 'liaoning'}">
                                  	<th style="width:4%;">大奖来源</th>
                                  </c:if>
                                  <th style="width:7%;">兑换门店</th>
                                  <th style="width:7%;">操作</th>
                                </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
                                    <c:when test="${fn:length(majorInfoList) gt 0}">
                                    <c:forEach items="${majorInfoList}" var="majorInfo" varStatus="idx">
                                        <tr>
                                            <td>
                                                <span id="infoKey" style="display: none;">${majorInfo.infoKey}</span>
                                                ${idx.count}
                                            </td>
                                            <%-- <td>${majorInfo.nickName}</td> --%>
                                            <td><span id="grandPrizeType">${majorInfo.grandPrizeType}</span></td>
                                            <td>${majorInfo.skuName}</td>
                                            <td data-key="${majorInfo.prizeVcodeUrl}" onclick="myFunction('${majorInfo.prizeVcodeUrl}')">
                                                    ${majorInfo.prizeVcode}</td>
                                            <td>
                                            	<c:if test="${not empty(majorInfo.phoneNum)}">
                                            		${majorInfo.phoneNum}(${majorInfo.userName})
                                            	</c:if>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${empty(majorInfo.exchangeMoney)}">--</c:when>
                                                    <c:otherwise><fmt:formatNumber value="${majorInfo.exchangeMoney }" pattern="#,##0.00#" /></c:otherwise>
                                                </c:choose>
                                                
                                            </td>
                                            <td style="text-align: left;">
		                                        <span id="userKey" style="display: none;">${majorInfo.userKey}</span>
		                                        <span id="userName" style="display: none;">${majorInfo.userName}</span>
		                                        <span id="phoneNum" style="display: none;">${majorInfo.phoneNum}</span>
		                                        <span id="address" style="display: none;">${majorInfo.address}</span>
		                                        <span id="expressCompany" style="display: none;">${majorInfo.expressCompany}</span>
		                                        <span id="expressNumber" style="display: none;">${majorInfo.expressNumber}</span>
		                                        <span id="expressSendMessage" style="display: none;">${majorInfo.expressSendMessage}</span>
                                                ${majorInfo.address}
                                                <c:if test="${not empty majorInfo.expressCompany}">${majorInfo.expressCompany}:${majorInfo.expressNumber}</c:if>
                                            </td>
                                            <td style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
	                                            <span title="中奖时间">${fn:substring(majorInfo.earnTime, 0, 19)}</span>
	                                            <hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
	                                            <span title="领取时间">${fn:substring(majorInfo.useTime, 0, 19)}</span>
                                            </td>
                                            <td style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
	                                            <span title="兑奖时间">${fn:substring(majorInfo.checkTime, 0, 19)}</span>
	                                            <hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
	                                            <span title="兑奖截止时间">${fn:substring(majorInfo.expireTime, 0, 19)}</span>
                                            </td>
                                            <td>
                                            	<c:if test="${not empty(majorInfo.checkPhoneNum)}">
                                            		${majorInfo.checkPhoneNum}(${majorInfo.checkUserName})
                                            	</c:if>
                                            </td>
                                            <td>
                                            	<c:choose>
                                                    <c:when test="${majorInfo.expireFlag and majorInfo.useStatus eq '0'}">已过期</c:when>
                                            		<c:when test="${majorInfo.checkStatus eq '0'}">未兑奖</c:when>
                                            		<c:when test="${majorInfo.exchangeMoney > 0.00}">已折现</c:when>
                                            		<c:otherwise>已兑奖</c:otherwise>
                                            	</c:choose>
                                            </td>
                                            <c:if test="${projectServerName eq 'liaoning'}">
                                            		<td>${majorInfo.prizeSource}</td>
                                            </c:if>
                                            <td style="text-align: left;">${majorInfo.terminalName}</td>
                                            <td data-key="${majorInfo.infoKey}">
                                                <a class="btn btn-xs btn-info btnView btn-orange"><i class="icon-edit"></i> 查 看</a>
                                                <c:if test="${majorInfo.useStatus eq '1'}">
	                                                <a class="btn btn-xs btn-info btnCheck btn-orange"><i class="icon-edit"></i>${majorInfo.checkStatus eq '0' ? '发 货' : '修改物流'}</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                    <tr>
                                        <td colspan="12"><div>查无数据！</div></td>
                                    </tr>
                                    </c:otherwise>
                                    </c:choose>
					        </tbody>
                        </table>
                        <table id="pagination"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="myModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
      			<div class="modal-body">
      				<h6 class="currentAlter"></h6>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
        <div class="modal fade" id="addBatchDialog" name="addBatchDialog" tabindex="-1"  data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
            <div class="modal-dialog" style="height: 100px;width: 300px;">
                <div class="modal-content" style="top:1%;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" >核销二维码</h4>
                    </div>
                    <div class="modal-body1">
                        <div id="qrcode"style="left: 20px; width: 100px;left: 100px;">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                    </div>
                </div>
            </div>
        </div>
        
    <div class="modal fade" id="expressDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">物流发货</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">用户主键：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="infoKey" type="hidden"/>
                                    <span id="userKey"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">中出类型：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="grandPrizeType"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">收件人：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="userName"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">电话：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="phoneNum"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">地址：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="address"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">物流公司：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="expressCompany" type="text" 
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="10" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">物流单号：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="expressNumber" type="text" 
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                        
                            <td width="25%" class="text-right"><label class="title">备注：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <textarea name="expressSendMessage"
                                        class="form-control input-width-larger required" autocomplete="off" rows="5" maxlength="100"></textarea>
                                </div>
                            </td>
                        </tr>
                   </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
	</div>
  </body>
</html>
