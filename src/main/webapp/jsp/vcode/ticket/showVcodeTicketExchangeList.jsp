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

            // 新增
            $("#addActivity").click(function(){
                var url = "<%=cpath%>/vcodeTicketExchange/showTicketExchangeAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeTicketExchange/showTicketExchangeEdit.do?infoKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeTicketExchange/doTicketExchangeDelete.do?infoKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            // 数据页
            $("a.data").off();
            $("a.data").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeTicketExchange/showTicketExchangeData.do?infoKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });

            $("[name='minVpointCog']").on("change", function () {
                //js只能输入正整数（不包括负整数和0）
                var places = $(this).val();
                if(!(/(^[1-9]\d*$)/.test(places))){
                    alert("兑换积分应为正整数");
                    $(this).val('')
                }
            });

            $("[name='maxVpointCog']").on("change", function () {
                //js只能输入正整数（不包括负整数和0）
                var places = $(this).val();
                if(!(/(^[1-9]\d*$)/.test(places))){
                    alert("兑换积分应为正整数");
                    $(this).val('')
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
    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">积分兑换优惠券</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">优惠券查询</span></div>
                    <div class="col-md-10 text-right">
                        <c:if test="${currentUser.roleKey ne '4'}">
                            <a id="addActivity" class="btn btn-blue">
                                <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增优惠券
                            </a>
                        </c:if>
                    </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="<%=cpath%>/vcodeTicketExchange/showTicketExchangeList.do">
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
                                        <label class="control-label">优惠券编号：</label>
                                        <input name="ticketNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">优惠券名称：</label>
                                        <input name="ticketName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">三方小程序id：</label>
                                        <input name="jumpId" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">跳转页面链接：</label>
                                        <input name="ticketUrl" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">兑换积分：</label>
                                        <input name="minVpointCog" class="form-control" style="width: 111px !important"  autocomplete="off" maxlength="30"/>
                                        -
                                        <input name="maxVpointCog" class="form-control" style="width: 111px !important"  autocomplete="off" maxlength="30"/>
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
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">产品列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:8%;">优惠券编号</th>
                                <th style="width:10%;">优惠券名称</th>
                                <th style="width:7%;">消耗积分</th>
                                <th style="width:10%;">跳转三方小程序id</th>
                                <th style="width:12%;">跳转页面链接</th>
                                <th style="width:7%;">中出限制</th>
                                <th style="width:7%;">消耗总积分</th>
                                <th style="width:7%;">兑换张数</th>
                                <th style="width:11%;">创建时间</th>
                                <th style="width:11%;">兑换截止时间</th>
                                <c:if test="${currentUser.roleKey ne '4'}">
                                    <th style="width:12%;">操作</th>
                                </c:if>
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
                                                <span>${item.ticketNo}</span>
                                            </td>
                                            <td>
                                                <span>${item.ticketName}</span>
                                            </td>
                                            <td>
                                                <span>${item.vpointsCog}</span>
                                            </td>
                                            <td>
                                                <span>${item.jumpId}</span>
                                            </td>
                                            <td>
                                                <span>${item.ticketUrl}</span>
                                            </td>
                                            <td>
                                                <span>${item.exchangeLimit}</span>
                                            </td>
                                            <td>
                                                <span>${item.ticketExchangeConsumeVpoints}</span>
                                            </td>
                                            <td>
                                                <span>${item.ticketExchangeCount}</span>
                                            </td>
                                            <td>
                                                <span>${item.createTime}</span>
                                            </td>
                                            <td>
                                                <c:if test="${item.exchangeEndTime != null && item.exchangeEndTime ne ''}"><span>${item.exchangeEndTime}</span></c:if>
                                                <c:if test="${item.exchangeEndDay != null && item.exchangeEndDay ne ''}"><span>兑换后${item.exchangeEndDay}天有效</span></c:if>
                                            </td>
                                            <c:if test="${currentUser.roleKey ne '4'}">
                                                <td data-key="${item.infoKey}" style="text-align: center;">
                                                    <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                                    <a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
                                                    <a class="btn btn-xs data btn-blue"><i class="iconfont icon-erweima"></i>&nbsp;兑换数据</a>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">11</c:when><c:otherwise>10</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
