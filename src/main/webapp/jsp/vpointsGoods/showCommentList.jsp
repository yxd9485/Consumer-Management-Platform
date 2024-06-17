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
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=6"></script>
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
        	
        	// 查看
			$("a.add").off();
			$("a.add").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/commentAction/show.do?isShow=0&commentId="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
        	// 审核
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/commentAction/show.do?isShow=1&commentId="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				console.log(${result.isTop})
				var url = "<%=cpath%>/commentAction/del.do?commentId="+key;
				$.fn.confirm("确认删除吗？", function(){
				$("form").attr("action", url);
				$("form").submit();
				});
			});
	
			
			
			 $('[name="my-checkbox"]').bootstrapSwitch({ 
				    onColor:"success",  
				    offColor:"info",
				    onText:"启用",  
		            offText:"停用",   
				    size:"small",  
				  	onSwitchChange:function(event,state){  
					    var key = $(this).parents("td").data("key");	
	                    var currentRow=$(this).closest("tr"); 
	                    var isDisplay = currentRow.find("td:eq(0)").text() == '0' ? '1' :'0';
	    				var url = "<%=cpath%>/commentAction/update.do?isDisplay="+isDisplay+"&commentId="+key;
	    				$("form").attr("action", url);
	    				$("form").submit();
				  	}  
				 })
	
			
			 $('[name="my-checkbox1"]').bootstrapSwitch({ 
				    onColor:"success",  
				    offColor:"info",
				    onText:"启用",  
		            offText:"停用",  
				    size:"small",  
				  	onSwitchChange:function(event,state){  
				  		var key = $(this).parents("td").data("key");	
	                    var currentRow=$(this).closest("tr"); 
	                    var isTop = currentRow.find("td:eq(1)").text() == '0' ? '1' : '0';
	    				var url = "<%=cpath%>/commentAction/update.do?isTop="+isTop+"&commentId="+key;
	    				$("form").attr("action", url);
	    				$("form").submit();
				  	}  
				 }) 

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
            <li class="current"><a>积分商城</a></li>
            <li class="current"><a>评论管理</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">评论查询</span></div>
                   <div class="col-md-10 text-right">
 
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath %>/commentAction/showCommentList.do">
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
                            <div class="form-group little_distance search">
                                <div class="search-item">
                                    <label class="control-label">按关键词查询：</label>
                                    <input name="keyword" class="form-control input-width-larger keyword" 
                                           autocomplete="off" maxlength="30" placeholder="商品名称/评论内容"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label"> 用户昵称：</label>
                                    <input name="userName" class="form-control input-width-larger keyword" 
                                           autocomplete="off" maxlength="10"/>
                                </div>
                               
                                <div class="search-item">
                                    <label class="control-label">状态：</label>
                                    <select name="status" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <option value="0">未审核</option>
                                        <option value="1">审核通过</option>
                                        <option value="2">审核不通过</option>
                                    
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
		                                <th style="width:10%; text-align: center;">订单编号</th>
		                                <th style="width:6%; text-align: center;">类型</th>
		                                <th style="width:5%; text-align: center;">用户昵称</th>
		                                <th style="width:12%; text-align: center;">商品名称</th>
		                                <th style="width:13%; text-align: center;" >评论</th>
		                                <th style="width:9%;">评论时间</th>
		                                <th style="width:10%;">优惠券</th>
		                                <th style="width:9%;" >是否显示</th>
		                                <th style="width:9%;" >是否置顶</th>
		                                <th style="width:4%; text-align: center;" >状态</th>
		                                <th style="width:13%; text-align: center;">操作</th>
		                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                <c:forEach items="${resultList}" var="result" varStatus="idx">
                                <tr>
                                	<td style="display:none"><span>${result.isDisplay}</span></td>
                                	<td style="display:none"><span>${result.isTop}</span></td>
                                    <td style="text-align: center;"><span>${result.exchangeId}</span></td>
                                    <td style="text-align: center;">
                                         <span>
                                             <c:choose>
                                             	<c:when test="${result.commentType eq '1'}">积分商城</c:when>
                                             	<c:when test="${result.commentType eq '2'}">竞价赢奖</c:when>
                                             	<c:otherwise>积分商城</c:otherwise>
                                             </c:choose>
                                         </span>
                                    </td>
                                    <td style="text-align: center;"><span>${result.userName}</span></td>
                                    <td style="text-align: center;"><span>${result.goodsName}</span></td>
                                    <td style="text-align: center;"><span>${result.orderContent}</span></td>
                                    <td style="text-align: center;"><span>${result.createTime}</span></td>
                                    <td style="text-align: center;"><span>${result.couponName}</span></td>
                                   	<td  data-key="${result.commentId}" style="text-align:center;">
	  									<input type="checkbox" <c:if test="${result.isDisplay eq '0'}">checked</c:if> name="my-checkbox">
                                    </td>
                                    
                                    <td data-key="${result.commentId}" style="text-align:center;">
		                                 <input type="checkbox" <c:if test="${result.isTop eq '0'}">checked</c:if> name="my-checkbox1">
                                    </td>
                                    <td style="text-align: center;">
                                            <span>
                                                <c:if test="${result.status == 0}">未审核</c:if>
                                                <c:if test="${result.status == 1}">审核通过</c:if>
                                                <c:if test="${result.status == 2}">审核不通过</c:if>
                                            </span>
                                    </td>
                                    <td data-key="${result.commentId}" style="text-align:center;">
	                                    <span>
	                                    	<a class="btn btn-xs add  btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;查 看</a>
	                                    		<c:if test="${result.status eq '0' or result.status eq '2'}">
	                                    			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;审 核</a>
	                                    		</c:if>
		                                    <a class="btn btn-xs del  btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
	                                    </span>
                                    </td>
                            
                                </tr>
                                </c:forEach>
                                </c:when>
                                <c:otherwise>
                                <tr>
                                    <td colspan="10"><span>查无数据！</span></td>
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
