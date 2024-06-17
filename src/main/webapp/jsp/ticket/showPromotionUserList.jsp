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
            $("div.area").initZone("<%=cpath%>", true, "${areaCode}", true, false, false, false);

            // 新增
            $("#add").off();
            $("#add").click(function(){
            	var url = "<%=cpath%>/promotionUserAction/showAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            
			// 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/promotionUserAction/showEdit.do?isShow=2&infoKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 查看
            $("a.look").off();
            $("a.look").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/promotionUserAction/showEdit.do?isShow=0&infoKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });
			
        	// 审核
			$("a.check").off();
			$("a.check").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/promotionUserAction/showEdit.do?isShow=1&infoKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});

			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/promotionUserAction/deleteById.do?infoKey="+key;
				$.fn.confirm("确认删除吗？", function(){
				$("form").attr("action", url);
				$("form").submit();
				});
			});
            
			// 导入
            $("a.import").off();
            $("a.import").on("click", function(){
                var url = "<%=cpath%>/promotionUserAction/toImportPage.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            
			// 导出
            $("a.export").off();
            $("a.export").on("click", function(){
                var url = "<%=cpath%>/promotionUserAction/exportPromotionUserList.do";
                $("form").attr("action", url);
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/promotionUserAction/showPromotionUserList.do");
            });

            $("input[name='searchVal']").on("click", function(){
                if($(this).val() == "姓名/手机号/门店名称"){
                	$("input[name='searchVal']").val('');    
                }  
            });
            $("input[name='searchVal']").on("blur", function(){
                if($(this).val() == ""){
                    $("input[name='searchVal']").val('姓名/手机号/门店名称');    
                }  
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
            <li class="current"><a>系统管理</a></li>
            <li class="current"><a>${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }列表</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">列表查询</span></div>
                   <div class="col-md-10 text-right">
                       <c:if test="${operateButStatus eq 0}">
		                   <a id="add" class="btn btn-blue">
		                       <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
		                   </a>
		                   <a class="btn btn-blue import">
	                           <i class="iconfont icon-shangchuan" style="font-size: 14px;"></i>&nbsp; 导入
	                       </a>
	                   </c:if>
                       <a class="btn btn-blue export">
                           <i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 导出
                       </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath %>/promotionUserAction/showPromotionUserList.do">
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
                                    <label class="control-label"> 输入搜索：</label>
                                    <input name="searchVal" class="form-control input-width-larger" 
                                        value="姓名/手机号/门店名称" autocomplete="off" maxlength="20"/>
                                </div>
                               
                                <div class="search-item" style="width: 480px;">
			                        <label class="control-label">地区：</label>
	                                <select name="provinceCode" class="zProvince form-control input-width-normal search"></select>
                                    <select name="cityCode" class="zCity form-control input-width-normal search"></select>
                                    <select name="countyCode" class="zDistrict form-control input-width-normal search"></select>
                                </div>
                                
                                <div class="search-item" <c:if test="${warAreaFlag eq '1'}"> style="display: none;" </c:if>>
                                    <label class="control-label">战区：</label>
                                    <select class="form-control input-width-larger search" id="warAreaName" name="warAreaName">
                                        <option value="">请选择</option>
                                        <c:forEach items="${ticketWarareaList }" var="item">
                                            <option value="${item.warAreaName }">${item.warAreaName }
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="search-item">
                                    <label class="control-label">是否参与评选：</label>
                                    <select name="isJoinVote" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <option value="0">否</option>
                                        <option value="1">是</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">激活状态：</label>
                                    <select name="status" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <option value="0">未激活</option>
                                        <option value="1">已激活</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">审核状态：</label>
                                    <select name="checkStatus" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <option value="0">待审核</option>
                                        <option value="1">通过</option>
                                        <option value="3">驳回</option>
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
                                <th style="width:7%;">姓名</th>
                                <th style="width:7%;">手机号</th>
                                <th style="width:8%;">外勤365编号</th>
                                <th style="width:5%;">战区</th>
                                <th style="width:5%;">系统</th>
                                <th style="width:5%;">渠道</th>
                                <th style="width:10%;">门店名称</th>
                                <th style="width:15%;" >门店地址</th>
                                 <th style="width:6%;" >是否参与评选</th>
                                <th style="width:5%; text-align: center;" >状态</th>
                                <th style="width:14%; text-align: center;">操作</th> 
                            </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                <c:forEach items="${resultList}" var="result" varStatus="idx">
                                <tr>
                                	<td style="text-align:center;"><span>${idx.count}</span></td>
                                	<td style="text-align: left;"><span>${result.userName}</span></td>
                                	<td style="text-align: center;"><span>${result.phoneNum}</span></td>
                                	<td style="text-align: left;"><span>${result.userCode}</span></td>
                                    <td style="text-align: left;"><span>${result.warAreaName}</span></td>
                                    <td style="text-align: left;"><span>${result.terminalSystem}</span></td>
                                    <td style="text-align: left;">
                                        <span>
                                        <c:choose>
                                            <c:when test="${result.ticketChannel eq '1'}">餐饮渠道</c:when>
                                            <c:when test="${result.ticketChannel eq '2'}">KA渠道</c:when>
                                        </c:choose>
                                        </span>
                                    </td>
                                    <td style="text-align: left;"><span>${result.terminalName}</span></td>
                                    <td style="text-align: left;"><span>${result.terminalAddress}</span></td>
                                    <td style="text-align: center;">
	                                    <span>
	                                        <c:if test="${result.isJoinVote eq 0}">否</c:if>
	                                        <c:if test="${result.isJoinVote eq 1}">是</c:if>
	                                    </span>
                                    </td>
                                    <td style="text-align: center;">
                                        <span>
                                            <c:if test="${result.status eq 0}">未激活</c:if>
                                            <c:if test="${result.status eq 1}">已激活</c:if>
                                        </span>
                                    </td>
                                    <td data-key="${result.infoKey}" style="text-align:center;">
	                                    <span>
	                                       <a class="btn btn-xs look  btn-red">&nbsp;查看</a>
	                                       <c:if test="${operateButStatus eq 0}">
		                                       <a class="btn btn-xs edit  btn-red">&nbsp;编辑</a>
		                                       <a class="btn btn-xs del  btn-red">&nbsp;删 除</a>
	                                    		<c:if test="${result.isJoinVote eq '1' and result.checkStatus eq '0'}">
	                                    			<a class="btn btn-xs check btn-green">&nbsp;审核</a>
		                                    	</c:if>
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
