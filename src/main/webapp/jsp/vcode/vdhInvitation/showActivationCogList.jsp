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
    <script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>

    <script>
        $(function(){
            // 加载完成后自动格式化千分符
            addThousandth();

            // 邀请码
            $("#addCode").click(function(){
                var url = "<%=cpath%>/vdhInvitationOrder/showOrderLst.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 新增
            $("#addItem").click(function(){
                var url = "<%=cpath%>/vdhInvitation/showAddActivity.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 修改
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vdhInvitation/showEditActivity.do?infoKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vdhInvitation/deleteActivity.do?infoKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });

            // 二维码

        });

        function addThousandth(){
            // 遍历所有包含 "thousandth" 类名的 span 元素
            $("span.thousandth").each(function () {
                // 获取当前 span 元素中的文本内容
                var text = $(this).text();

                // 将文本内容转换为数字类型，并添加千位分隔符
                var number = parseFloat(text);
                if (!isNaN(number)) {
                    var formattedNumber = number.toLocaleString();
                    $(this).text(formattedNumber);
                }
            });
        }
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
                <li class="current"><a> V店惠邀请有礼</a></li>
                <li class="current"><a title="">
                    <c:if test="${isBegin eq '0'}">待上线</c:if>
                    <c:if test="${isBegin eq '1'}">已上线</c:if>
                    <c:if test="${isBegin eq '2'}">已下线</c:if>
                    <c:if test="${isBegin eq '3'}">全部活动</c:if>
                </a></li>
            </ul>
        </div>

        <div class="row">
            <div class="col-md-12 tabbable">
                <a href="<%=cpath%>/vdhInvitation/showActivityList.do?vjfSessionId=${vjfSessionId}&isBegin=0"
                   class="btn <c:if test="${isBegin eq '0'}">btn-blue</c:if>">待上线</a>
                <a href="<%=cpath%>/vdhInvitation/showActivityList.do?vjfSessionId=${vjfSessionId}&isBegin=1"
                   class="btn <c:if test="${isBegin eq '1'}">btn-blue</c:if>">已上线</a>
                <a href="<%=cpath%>/vdhInvitation/showActivityList.do?vjfSessionId=${vjfSessionId}&isBegin=2"
                   class="btn <c:if test="${isBegin eq '2'}">btn-blue</c:if>">已下线</a>
                <a href="<%=cpath%>/vdhInvitation/showActivityList.do?vjfSessionId=${vjfSessionId}&isBegin=3"
                   class="btn <c:if test="${isBegin eq '3'}">btn-blue</c:if>">全部活动</a>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12 tabbable tabbable-custom">
                <div class="widget box">
                    <%--新增活动--%>
                    <div class="row">
                        <div class="col-md-2"><span style="font-size: 12px; color: black;">查询</span></div>
                        <div class="col-md-10 text-right">
                            <c:if test="${currentUser.roleKey ne '4'}">
                                <a id="addCode" class="btn btn-blue">
                                    <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 创建邀请码
                                </a>
                                <a id="addItem" class="btn btn-blue">
                                    <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增活动
                                </a>
                            </c:if>
                        </div>
                    </div>

                    <%--查询条件--%>
                    <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                    <div class="widget-content form-inline">
                        <form class="listForm" method="post" action="<%=cpath%>/vdhInvitation/showActivityList.do">
                            <input type="hidden" class="tableTotalCount" value="${showCount}" />
                            <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                            <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                            <input type="hidden" class="tableCurPage" value="${currentPage}" />
                            <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                            <input type="hidden" class="tableOrderType" value="${orderType}" />
                            <input type="hidden" name="isBegin" value="${isBegin}"/>
                            <input type="hidden" name="queryParam" value="${queryParam}" />
                            <input type="hidden" name="pageParam" value="${pageParam}" />

                            <div class="row">
                                <div class="col-md-12 ">
                                    <div class="form-group little_distance search"  style="line-height: 35px;">
                                        <div class="search-item">
                                            <label class="control-label">活动名称：</label>
                                            <input name="ruleName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                        </div>
                                        <div class="search-item">
                                            <label class="control-label">创建时间：</label>
                                            <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                                   tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                            <label class="">至</label>
                                            <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                                   tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
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

                <%--数据列表--%>
                <div class="widget box" id="tab_1_1">
                    <div class="row">
                        <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">数据列表</span></div>
                    </div>
                    <div class="widget-content">
                        <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                   id="dataTable_data">
                                <input type="hidden" id="screenWidth1366" value="5,9,14,8,5,8,8,8,8,8,8,8,8" />
                                <thead>
                                    <tr>
                                        <th style="width:4%;">序号</th>
                                        <th style="width:8%;">活动编号</th>
                                        <th style="width:8%;" data-ordercol="rule_name">活动名称</th>
                                        <th style="width:10%;" data-ordercol="start_date">活动有效期</th>
                                        <th style="width:8%;" >活动状态</th>
                                        <c:if test="${isBegin eq '1'}">
                                            <th style="width:8%;" >总金额</th>
                                        </c:if>
                                        <c:if test="${isBegin eq '1'}">
                                            <th style="width:8%;" >已投放金额</th>
                                        </c:if>
                                        <th style="width:10%;" data-ordercol="create_time" data-orderdef>创建时间</th>
                                        <th style="width:8%;" data-ordercol="state_flag">启用状态</th>
                                        <th style="width:10%;">操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${fn:length(resultList) gt 0}">
                                            <c:forEach items="${resultList}" var="activity" varStatus="idx">
                                                <tr>
                                                    <td style="text-align:center;">
                                                        <span>${idx.count}</span>
                                                    </td>
                                                    <td>
                                                        <span>${activity.activityNo}</span>
                                                    </td>
                                                    <td style="text-align:center;">
                                                        <span>${activity.activityName}</span>
                                                    </td>
                                                    <td style="text-align:center;">
                                                        <span>${activity.startTime}</span>
                                                        <span>&nbsp;至&nbsp;${activity.endTime}</span>
                                                    </td>
                                                    <td style="text-align:center;">
                                                        <span>
                                                            <c:choose>
                                                                <c:when test="${activity.isBegin eq '0'}">待上线</c:when>
                                                                <c:when test="${activity.isBegin eq '1'}">已上线</c:when>
                                                                <c:otherwise>已下线</c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </td>
                                                    <c:if test="${isBegin eq '1'}">
                                                        <td style="text-align:center;">
                                                            <span class="thousandth">${activity.money}</span>
                                                        </td>
                                                    </c:if>
                                                    <c:if test="${isBegin eq '1'}">
                                                        <td style="text-align:center;">
                                                            <span class="thousandth">${activity.putInMoney}</span>
                                                        </td>
                                                    </c:if>
                                                    <td style="text-align:center;">
                                                        <span>${activity.createTime}</span>
                                                    </td>
                                                    <td style="text-align:center;">
                                                        <c:choose>
                                                            <c:when test="${activity.status eq '0'}">停用</c:when>
                                                            <c:when test="${activity.status eq '1'}">启用</c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td data-key="${activity.infoKey}" style="text-align: center;">
                                                        <%--0未开始  1已开始  2已结束  --%>
                                                        <c:if test="${activity.isBegin eq '0'}">
                                                            <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修改</a>
                                                            <a class="btn btn-xs del btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;删除</a>
                                                        </c:if>
                                                        <c:if test="${activity.isBegin eq '1'}">
                                                            <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修改</a>
                                                        </c:if>
                                                        <c:if test="${activity.isBegin eq '2'}">
                                                            <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修改</a>
                                                        </c:if>
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
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content" style="top:30%;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
                    </div>
                    <div class="modal-body">
                        <h6 class="addFalse">保存失败</h6>
                        <h6 class="addSuccess">保存成功</h6>
                        <h6 class="deleteFalse">删除失败</h6>
                        <h6 class="deleteSuccess">删除成功</h6>
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
