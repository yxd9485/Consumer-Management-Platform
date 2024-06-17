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
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>

    <script>
        $(function(){
            // 初始化校验控件
            $.runtimeValidate($("#orderWindow"));

            //容积千分符
            $("input[name='volume']").on('keyup', function () {
                $("input[name='volume']").val(thousandth($("input[name='volume']").val()));
            });

            // 跳转二维码订单
            $("a.codeOrder").off();
            $("a.codeOrder").on("click", function(){
                var url = "<%=cpath%>/vdhInvitationOrder/showOrderLst.do?vjfSessionId=${vjfSessionId}&tabsFlag=0";
                window.location.href = url;
            });

            // 查询结果导出
            $(".export").off();
            $(".export").on("click", function(){
                $.fn.confirm("确定要导出吗？", function(){
                    $("form").attr("action", "<%=cpath%>/vdhInvitationCode/uploadCode.do?uploadCodeType=0");
                    $("form").submit();
                    // 还原查询action
                    $("form").attr("action", "<%=cpath%>/vdhInvitationCode/showCodeLst.do");
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
        .tab-radio {
            margin: 10px 0 0 !important;
        }
        .validate_tips {
            padding: 8px !important;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> V店惠邀请有礼</a></li>
            <li class="current"><a title="">
                <c:if test="${tabsFlag eq '0'}">二维码订单</c:if>
                <c:if test="${tabsFlag eq '1'}">门店码</c:if>
            </a></li>
        </ul>
    </div>

    <div class="row">
        <div class="col-md-12 tabbable">
            <a class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if> codeOrder">二维码订单</a>
            <a href="<%=cpath%>/vdhInvitationCode/showCodeLst.do?vjfSessionId=${vjfSessionId}&tabsFlag=1"
               class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">门店码</a>
        </div>
    </div>

    <div class="row" id="orderData">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <%--新增活动--%>
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 12px; color: black;">查询</span></div>
                </div>

                <%--查询条件--%>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="<%=cpath%>/vdhInvitationCode/showCodeLst.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />

                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group little_distance search"  style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">订单名称：</label>
                                        <input name="orderName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>

                                    <div class="search-item">
                                        <label class="control-label">邀请码编号：</label>
                                        <input name="codeNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>

                                    <div class="search-item">
                                        <label class="control-label">注册门店Key：</label>
                                        <input name="registrantKey" class="form-control input-width-larger" autocomplete="off" maxlength="36"/>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12 text-center mart20">
                                <button type="button" class="btn btn-primary btn-blue">查 询</button>
                                <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
                                <button type="button" class="btn btn-reset export btn-red marl20">导 出</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <%--数据列表--%>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">数据列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,5,9,8,8,8,8,8,8,8,8,8,8" />
                            <thead>
                                <tr>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:8%;" >订单名称</th>
                                    <th style="width:10%;" >邀请码编号</th>
                                    <th style="width:10%;" >注册门店Key</th>
                                    <th style="width:12%;" >注册时间</th>
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
                                                    <span>${item.orderName}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.codeNo}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.registrantKey}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.registerTime}</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="9"><span>查无数据！</span></td>
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

