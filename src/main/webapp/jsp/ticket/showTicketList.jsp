<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
String pathPrefix = cpath + "/" + imagePathPrx;
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	 <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script>
        $(function(){
        	// 初始化省市县
        	//$("div.area").initZone("<%=cpath%>", true);
       
        	// 查看
			$("a.add").off();
			$("a.add").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/smallticketAction/showEdit.do?isShow=0&infoKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
        	// 审核
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/smallticketAction/showEdit.do?isShow=1&infoKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/smallticketAction/del.do?infoKey="+key;
				$.fn.confirm("确认删除吗？", function(){
				$("form").attr("action", url);
				$("form").submit();
				});
			});
            
            // 提醒
            $("a.remind").off();
            $("a.remind").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/smallticketAction/doRemind.do?infoKey="+key;
                
                $.fn.confirm("确认发送提醒消息给省区审核人员？", function(){
	                $.ajax({
	                    type: "POST",
	                    url: url,
	                    dataType: "json",
	                    async: false,
	                    beforeSend:appendVjfSessionId,
	                    success:  function(data) {
	                    	console.log(data);
	                        $.fn.alert(data['errMsg']);
	                    }
	                });
                });
            });

        });
    </script>
    <style>
        table.table tr th {
            text-align: center;
        }
        table.table tr td {
            vertical-align: middle;
        }
        .active_board_table .ab_main {
            width: 65% !important;
            margin: 6px;
            padding: 6px;
        }
    </style>
  </head>

  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a>首页</a></li>
            <li class="current"><a>小票审核</a></li>
            <li class="current"><a>审核列表</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">审核列表查询</span></div>
                   <div class="col-md-10 text-right">
 
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath %>/smallticketAction/showTicketList.do">
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
                            <div class="form-group little_distance search area" style="line-height: 35px;">

                                <div class="search-item">
                                    <label class="control-label"> 来源渠道：</label>
                                    <select name="ticketChannel" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <option value="1">餐饮渠道</option>
                                        <option value="2">KA渠道</option>
                                    </select>
                                </div>
                               
                                <div class="search-item">
			                        <label class="control-label">所属区域：</label>
	                                <select id="province" name="province" class="zProvince form-control input-width-larger search">
                                        <option value="">请选择</option>
                                        <c:forEach items="${provinceList}" var="item">
                                            <option value="${item.areaCode}">${item.areaName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="search-item">
                                    <label class="control-label">上传时间：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                
                                <div class="search-item">
                                    <label class="control-label">审核人：</label>
                                    <input name="inputUserName" class="form-control input-width-larger" 
                                           autocomplete="off" maxlength="10"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">审核状态：</label>
                                    <select name="ticketStatus" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <option value="0">未审核</option>
                                        <option value="1">审核不通过</option>
                                        <option value="2">审核通过</option>
                                    	<option value="3">送核中</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">审核时间：</label>
                                    <input name="inputStartTime" id="inputStartTime" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'inputEndTime\')}'})" />
                                    <label class="">-</label>
                                    <input name="inputEndTime" id="inputEndTime" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'inputStartTime\')}'})" />
                                </div>
                                 <div class="search-item">
                                    <label class="control-label"> 流水号：</label>
                                    <input name="ticketNo" class="form-control input-width-larger" 
                                           autocomplete="off" maxlength="100"/>
                                </div>
                                 <div class="search-item">
                                    <label class="control-label"> 促销员：</label>
                                    <input name="promotionPhoneNum" class="form-control input-width-larger" 
                                           autocomplete="off" maxlength="11" placeholder="姓名/手机号"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">实物：</label>
                                    <select name="prizeType" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <c:forEach items="${prizeTypeMap}" var="item">
                                            <option value="${item.key}">${item.value}</option>
                                        </c:forEach>
                                    </select>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">数据列表</span></div>
                  
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                            	<th style="width:4%;">序号</th>
                                <th style="width:13%;">小票图片</th>
                                <th style="width:8%;">用户昵称</th>
                                <th style="width:8%;">来源渠道</th>
                                <th style="width:8%;">上传时间</th>
                                <th style="width:5%;">地区</th>
                                <th style="width:10%;">审核人</th>
                                <th style="width:10%;">促销员</th>
                                <th style="width:8%;" >中出类型</th>
                                 <th style="width:8%;" >正在审核人</th>
                                <th style="width:8%; text-align: center;" >审核状态</th>
                                <th style="width:6%; text-align: center;">操作</th> 
                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                <c:forEach items="${resultList}" var="result" varStatus="idx">
                                <tr>
                                	<td style="text-align:center;"><span>${idx.count}</span></td>
                                	<td>
										<span><img src="${result.ticketUrl}" style="height: 150px;width: 300px; float: left;"></span>
									</td>
                                	<td style="text-align: center;"><span>${result.userName}</span></td>
                                	<td style="text-align: center;">
                                            <span>
                                                <c:if test="${result.ticketChannel == 0}">酒行渠道</c:if>
                                                <c:if test="${result.ticketChannel == 1}">餐饮渠道</c:if>
                                                <c:if test="${result.ticketChannel == 2}">KA渠道</c:if>
                                                <c:if test="${result.ticketChannel == 3}">电商渠道</c:if>
                                            </span>
                                    </td>
   
                                    <td style="text-align: center;"><span>${result.createTime}</span></td>
                                    <td style="text-align: center;"><span>${result.province}</span></td>
                                    <td style="text-align: center;">
                                   		<c:choose>
                                   			<c:when test="${not empty result.checkUserName}">                                   			
                                   				<span>${result.checkUserName}</span>
                                   			</c:when>
                                   			<c:otherwise>
                                   				<span>${result.inputUserName}</span>
                                   			</c:otherwise>
                                   		</c:choose>
                                   		<c:if test="${not empty result.inputTime}"><br/><span>${result.inputTime}</span></c:if>
                                   </td>
                                    <td style="text-align: center;">
	                                    <span>${result.promotionUserName}</span><br/><span>${result.promotionPhoneNum}
                                    </td>
                                    <td style="text-align: center;">
                                    		<c:choose>
                                    			<c:when test="${not empty result.prizeName}">                                   			
                                    				<span>${result.prizeName}</span>
                                    			</c:when>
                                    			<c:otherwise>
                                    				<span>${result.earnMoney}</span>
                                    			</c:otherwise>
                                    		</c:choose>
									</td>
                                    <td style="text-align: center;"><span>${result.projressName}</span></td>
                                    <td style="text-align: center;">
                                            <span>
                                                <c:if test="${result.ticketStatus == 0}">未审核</c:if>
                                                <c:if test="${result.ticketStatus == 1}">驳回</c:if>
                                                <c:if test="${result.ticketStatus == 2 or result.ticketStatus == 4}">审核通过</c:if>
                                                <c:if test="${result.ticketStatus == 3}">送审中</c:if>
                                            </span>
                                    </td>
                                    <td data-key="${result.infoKey}" style="text-align:center;">
	                                    <span>
                                            <c:if test="${result.ticketStatus == 3}">
                                                <a class="btn btn-xs remind  btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;提 醒</a>
                                            </c:if>
	                                    	<c:if test="${result.ticketStatus != '0'}">
	                                    			<a class="btn btn-xs add  btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;查 看</a>
	                                    	</c:if>
	                                    		<c:if test="${result.ticketStatus eq '0'}">
	                                    			<a class="btn btn-xs edit  btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;审 核</a>
	                                    	</c:if>
	                                    </span>
                                    </td>
                            
                                </tr>
                                </c:forEach>
                                </c:when>
                                <c:otherwise>
                                <tr>
                                    <td colspan="12"><span>查无数据！</span></td>
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
    </div>
  </body>
</html>
