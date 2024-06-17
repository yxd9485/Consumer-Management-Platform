<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
%>

<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

    <script>
        $(function () {

            // 新增
            $("#addItem").click(function () {
                var url = "<%=cpath%>/waitActivationPrize/showWaitActivationPrizeCogAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/waitActivationPrize/showWaitActivationPrizeCogEdit.do?prizeKey=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });
            <%--// 删除--%>
            <%--$("a.del").off();--%>
            <%--$("a.del").on("click", function () {--%>
            <%--    var key = $(this).parents("td").data("key");--%>
            <%--    var url = "<%=cpath%>/anniversaryShare/doAnniversaryShareDelete.do?shareKey=" + key;--%>
            <%--    $.fn.confirm("确认删除吗？", function () {--%>
            <%--        $("form").attr("action", url);--%>
            <%--        $("form").submit();--%>
            <%--    });--%>
            <%--});--%>
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
            <li class="current"><a> 待激活红包配置</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动查询</span>
                    </div>
                    <div class="col-md-10 text-right">
                        <a id="addItem" class="btn btn-blue">
                            <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新建配置
                        </a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <hr style="height: 2px; margin: 10px 0px;"/>
                    </div>
                </div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                          action="<%=cpath%>/waitActivationPrize/showWaitActivationPrizeCogList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}"/>
                        <input type="hidden" class="tableStartIndex" value="${startIndex}"/>
                        <input type="hidden" class="tablePerPage" value="${countPerPage}"/>
                        <input type="hidden" class="tableCurPage" value="${currentPage}"/>
                        <input type="hidden" class="tableOrderCol" value="${orderCol}"/>
                        <input type="hidden" class="tableOrderType" value="${orderType}"/>
                        <input type="hidden" name="queryParam" value="${queryParam}"/>
                        <input type="hidden" name="pageParam" value="${pageParam}"/>
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group little_distance search">
                                    <div class="search-item">
                                        <label class="control-label">名称：</label>
                                        <input name="keyword" class="form-control input-width-larger" placeholder = "可输入关键字查询"
                                               autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">激活SKU：</label>
                                        <select name="skuKey" id="skuKey"
                                                class="form-control input-width-large search">
                                            <option value="">全部</option>
                                            <c:forEach items="${skuList}" var="item">
                                                <option value="${item.skuKey}">${item.skuName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">创建时间：</label>
                                        <input name="stGmt" id="stGmt"
                                               class="form-control input-width-medium Wdate search-date"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endGmt\')}'})"/>
                                        <label class="">-</label>
                                        <input name="endGmt" id="endGmt"
                                               class="form-control input-width-medium Wdate sufTime search-date"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'stGmt\')}'})"/>
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
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">配置列表</span>
                    </div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:15%;">待激活红包名称</th>
                                <th style="width:20%;">激活SKU</th>
                                <th style="width:8%;">每日可中出金额（元）</th>
                                <th style="width:8%;">当日已中出金额（元）</th>
                                <th style="width:8%;">累计可中出金额（元）</th>
                                <th style="width:8%;">累计已中出金额（元）</th>
                                <th style="width:8%;">启用状态</th>
                                <th style="width:10%;">创建时间</th>
                                <th style="width:10%;">操作</th>
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
                                            <td style="text-align:center;">
                                                <span>${item.prizeName}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${item.skuNames}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${item.dayMoneyLimit}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${item.dayMoney}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${item.totalMoneyLimit}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${item.totalMoney}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>
                                                	<c:choose>
                                                		<c:when test="${item.status eq '0'}">未启用</c:when>
                                                		<c:when test="${item.status eq '1'}">已启用</c:when>
                                                		<c:otherwise>其他</c:otherwise>
                                                	</c:choose>
                                                </span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${item.createTime}</span>
                                            </td>
                                            <td data-key="${item.prizeKey}" style="text-align: center;">
                                                <a class="btn btn-xs edit btn-orange"><i
                                                        class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
<%--                                                <a class="btn btn-xs del btn-red"><i--%>
<%--                                                        class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>--%>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8" style="text-align:center;">
                                            <span>查无数据！</span></td>
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
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="top:30%;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">提示消息</h4>
            </div>
            <div style="padding-left: 20px; padding-top: 10px">${errorMsg}</div>
            <div class="modal-body" style="padding: 5px;">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>
