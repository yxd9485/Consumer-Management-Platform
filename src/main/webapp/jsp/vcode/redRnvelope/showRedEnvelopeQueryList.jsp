<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=3"></script>

    <script>
        $(function(){

            // 新增
            $("#addItem").click(function(){
                var url = "<%=cpath%>/redEnvelopeRain/showRedEnvelopeAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/redEnvelopeRain/showRedEnvelopeEdit.do?activityKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 查看
            $("a.view").off();
            $("a.view").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/redEnvelopeRain/showRedEnvelopeEdit.do?activityKey="+key+"&isBegin=2";
                $("form").attr("action", url);
                $("form").submit();
            });



            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/redEnvelopeRain/redEnvelopeDelete.do?activityKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
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
    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 红包雨活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">红包雨活动查询</span></div>
                    <div class="col-md-10 text-right">
                        <c:if test="${currentUser.userKey ne '-1'}">
                            <a id="addItem" class="btn btn-red">
                                <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
                            </a>
                        </c:if>
                    </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                          action="<%=cpath%>/redEnvelopeRain/showRedEnvelopeQueryList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">活动名称：</label>
                                        <input name="activityName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">活动状态：</label>
                                        <select name="isBegin" class="form-control input-width-larger search" autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">待上线</option>
                                            <option value="1">已上线</option>
                                            <option value="2">已下线</option>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">创建时间：</label>
                                        <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                        <label class="">-</label>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 text-center mart20">
                                <button type="button" class="btn btn-primary btn-red">查 询</button>
                                <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">红包雨活动列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,19,9,14,9,9,9,6,9,9" />
                            <thead>
                            <tr>
                                <th style="width:4%;text-align: center;">序号</th>
                                <th style="width:9%;text-align: center;">活动名称</th>
                                <th style="width:10%;text-align: center;">活动有效期</th>
                                <th style="width:10%;text-align: center;">活动区域</th>
                                <th style="width:10%;text-align: center;">活动状态</th>
                                <th style="width:10%;text-align: center;">活动开关</th>
                                <th style="width:10%;text-align: center;">创建时间</th>
                                <th style="width:10%;text-align: center;">更新时间</th>
                                <th style="width:12%;text-align: center;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr  style="text-align:center;">

                                            <td style="text-align: center; <c:choose>
                                            <c:when test="${item.isBegin == '0'}">background-color: #ecb010;</c:when>
                                            <c:when test="${item.isBegin == '1'}">background-color: #ea918c;</c:when>
                                            <c:when test="${item.isBegin == '2'}">background-color: #999999;</c:when>
                                            <c:otherwise>background-color: red;</c:otherwise>
                                                    </c:choose>" >
                                                <span>${idx.count}</span>
                                            </td>
                                            <td>
                                                <span>${item.activityName}</span>
                                            </td>
                                            <td>
                                                <span>${item.startDate}&nbsp;-&nbsp;${item.endDate}</span>
                                            </td>
                                            <td>

                                                    <c:forEach items="${item.areaCode}"  var="areaName">
                                                        <span> ${areaName} </span>
                                                    </c:forEach>

                                            </td>
                                            <td style="text-align:center;">
					        			     <span>
										     	<c:choose>
                                                     <c:when test="${item.isBegin eq '0'}">待上线</c:when>
                                                     <c:when test="${item.isBegin eq '1'}">已上线</c:when>
                                                     <c:otherwise>已下线</c:otherwise>
                                                 </c:choose>
										     </span>
                                            </td>
                                            <td style="text-align:center;">
					        			     <span>
										     	<c:choose>
                                                    <c:when test="${item.statusFlag eq '0'}">关</c:when>
                                                    <c:when test="${item.statusFlag eq '1'}">开</c:when>
                                                </c:choose>
										     </span>
                                            </td>
                                            <td>
                                                <span>${item.createTime}</span>
                                            </td>
                                            <td>
                                                <span>${item.updateTime}</span>
                                            </td>
                                            <td data-key="${item.infoKey}" style="text-align: center;">
<%--                                                <c:if test="${item.isBegin eq '0' or item.isBegin eq '1'}">--%>
                                                    <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
<%--                                                </c:if>--%>
                                                <c:if test="${item.isBegin eq '2'}">
                                                    <a class="btn btn-xs view btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;查 看</a>
                                                </c:if>
                                                <c:if test="${item.isBegin eq '0'}">
                                                    <a class="btn btn-xs del btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;删 除</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="11"><span>查无数据！</span></td>
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
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="top:30%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">提示消息</h4>
                </div>
                <div class="modal-body">
                    <h6 class="add_success">添加成功</h6>
                    <h6 class="add_fail">添加失败${msg}</h6>
                    <h6 class="edit_success">编辑成功</h6>
                    <h6 class="edit_fail">编辑失败${msg}</h6>
                    <h6 class="del_success">删除成功</h6>
                    <h6 class="del_fail">删除失败</h6>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>