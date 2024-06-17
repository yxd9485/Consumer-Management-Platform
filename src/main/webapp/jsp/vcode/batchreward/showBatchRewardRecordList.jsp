<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
    String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

    <script>
        $(function(){

            // 跳转批量发放奖励页面
            $("#grantReward").click(function(){
                var url = "<%=cpath%>/batchreward/showBatchRewardRecordAdd.do";
                $("form").attr("action", url);
                $("form").submit();
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
            <li class="current"><a title="">批量发放激励</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: black; font-weight: bold;">批量发放激励查询</span></div>
                    <div class="col-md-10 text-right">
                         <a id="grantReward" class="btn btn-blue">
                             <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 批量发放激励
                         </a>
                    </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="<%=cpath%>/batchreward/showBatchRewardRecordList.do">
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
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">上传编号查询：</label>
                                        <input name="keyword" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item" style="width: 500px !important;">
                                        <label class="control-label">上传时间：</label>
                                        <input name="startTime" id="startDate" style="width: 180px !important;" class="form-control input-width-medium Wdate search-date"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                        <label class="">-</label>
                                        <input name="endTIme" id="endDate" style="width: 180px !important;" class="form-control input-width-medium Wdate sufTime search-date"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'startDate\')}'})" />
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
                    <div class="col-md-2"><span style="font-size: 15px; color: black; font-weight: bold;">激励列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:7%;" data-ordercol="sku_name">上传编号</th>
                                <th style="width:10%;" data-ordercol="commodity_code">用户数</th>
                                <th style="width:10%;" data-ordercol="commodity_code">积分激励</th>
                                <th style="width:7%;" data-ordercol="valid_day">红包激励(元)</th>
                                <th style="width:7%;" data-ordercol="valid_day">实物奖激励</th>
                                <th style="width:10%;" data-ordercol="valid_day">上传时间</th>
                                <th style="width:12%;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td>
                                                <span>${item.grantNo}</span>
                                            </td>
                                            <td>
                                                <span>${item.userCount}</span>
                                            </td>
                                            <td>
                                                <span>${item.grantVpoints}</span>
                                            </td>
                                            <td>
                                                <span>${item.grantMoney}</span>
                                            </td>
                                            <td>
                                                <span>${item.grantBigprize}</span>
                                            </td>
                                            <td>
                                                <span>${item.grantTime}</span>
                                            </td>
                                            <td data-key="${item.infoKey}" style="text-align: center;">
                                                <a class="btn btn-blue" href="http://123.56.84.162:9009/images/vma/${item.filePath}">下载</a>
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
                    <h6 class="add_fail">添加失败</h6>
                    <h6 class="edit_success">编辑成功</h6>
                    <h6 class="edit_fail">编辑失败</h6>
                    <h6 class="del_success">删除成功</h6>
                    <h6 class="del_fail">删除失败</h6>
                    <h6 class="del_invalid">删除失败,先删除该sku关联的活动</h6>
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
