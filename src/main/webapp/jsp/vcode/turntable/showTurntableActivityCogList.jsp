<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.dbt.framework.util.PropertiesUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
    String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
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
            $("#addActivity").click(function () {
                var url = "<%=cpath%>/turntable/showTurntableActivityAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/turntable/showTurntableActivityEdit.do?activityKey=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 删除
            $("a.del").off();
            $("a.del").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/marquee/doMarqueeCogInfoDelete.do?infoKey=" + key;
                $.fn.confirm("确认删除吗？", function () {
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            // 规则
            $("a.rule").off();
            $("a.rule").on("click", function(){
                var key = $(this).parents("td").data("key");
                var drainageType = $(this).parents("td").data("type");
                let activityType ='8';
                if(drainageType==0){
                    activityType = '9'
                }
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType="+activityType;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 数据
            $("a.record").off();
            $("a.record").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/turntable/showTurnTableRecordList.do?activityKey=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 新增奖品
            $("a.prize").off();
            $("a.prize").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/turntable/showTurntablePrizeAdd.do?key=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 编辑奖品
            $("a.editPrize").off();
            $("a.editPrize").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/turntable/showTurntablePrizeEdit.do?infoKey=" + key;
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
            <li class="current"><a title="">积分活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">跑马灯查询</span>
                    </div>
                    <div class="col-md-10 text-right">
                        <c:if test="${currentUser.roleKey ne '4'}">
                            <a id="addActivity" class="btn btn-blue">
                                <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;新建积分活动
                            </a>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <hr style="height: 2px; margin: 10px 0px;"/>
                    </div>
                </div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="<%=cpath%>/turntable/showTurntableActivityList.do">
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
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">活动名称：</label>
                                        <input name="activityName" class="form-control input-width-larger"
                                               autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">活动状态：</label>
                                        <select name="activityStatus" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">待上线</option>
                                            <option value="1">进行中</option>
                                            <option value="2">已下线</option>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">启用状态：</label>
                                        <select name="status" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">启用</option>
                                            <option value="1">停用</option>
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
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">产品列表</span>
                    </div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:4%;">活动渠道</th>
                                <th style="width:6%;">参与条件</th>
                                <th style="width:8%;">活动名称</th>
                                <th style="width:10%;">活动时间</th>
                                <th style="width:4%;">活动类型</th>
                                <th style="width:10%;">活动区域</th>
                                <th style="width:8%;">抽奖次数</th>
                                <c:if test="${currentUser.projectServerName eq 'mengniu'}">
                                    <th style="width:8%;">参与角色</th>
                                </c:if>
                                <th style="width:8%;">活动状态</th>
                                <th style="width:4%;">抽奖人数</th>
                                <th style="width:4%;">中奖人数</th>
                                <th style="width:8%;">启用状态</th>
                                <th style="width:8%;">创建时间</th>
                                <th style="width:30%;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="activityCog" varStatus="idx">
                                        <tr style="cursor:pointer">
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse" style="text-align: center"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>
                                                <c:if test="${activityCog.drainageType eq '0'}">终端引流</c:if>
                                                <c:if test="${activityCog.drainageType eq '1'}">商城转盘活动</c:if>
                                                <c:if test="${activityCog.drainageType eq '2'}">二重惊喜</c:if>
                                                <c:if test="${activityCog.drainageType eq '3'}">异常提示抽奖活动</c:if>
                                                <c:if test="${activityCog.drainageType eq '4'}">福利站</c:if>
                                                </span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse" style="text-align: center"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>
                                                <c:if test="${activityCog.joinCondition eq '0'}">消耗积分</c:if>
                                                <c:if test="${activityCog.joinCondition eq '1'}">消耗抽奖次数</c:if>
                                                </span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>${activityCog.activityName}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>${activityCog.startDate}至${activityCog.endDate}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse" style="text-align: center"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${activityCog.activityType eq '0'}">转盘</c:when>
                                                        <c:when test="${activityCog.activityType eq '1'}">盲盒</c:when>
                                                        <c:when test="${activityCog.activityType eq '2'}">福袋</c:when>
                                                    </c:choose>
                                                </span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${activityCog.areaName != null and activityCog.areaName != ''}">
                                                            ${activityCog.areaName}
                                                        </c:when>
                                                        <c:otherwise>全部</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <c:if test="${activityCog.everyDayLimit != null and activityCog.everyDayLimit != 0}">
                                                    <span>每天${activityCog.everyDayLimit}次</span><br>
                                                </c:if>
                                                <c:if test="${activityCog.totalLimit != null and activityCog.totalLimit != 0}">
                                                    <span>活动${activityCog.totalLimit}次</span>
                                                </c:if>
                                                <c:if test="${activityCog.everyDayLimit == null || activityCog.everyDayLimit == 0}">
                                                    <span>每天无限制</span>
                                                </c:if>
                                                <c:if test="${activityCog.totalLimit == null || activityCog.totalLimit == 0}">
                                                        <span>活动无限制</span>
                                                </c:if>
                                            </td>
                                            <c:if test="${currentUser.projectServerName eq 'mengniu'}">
                                                <td class="paneltitle" data-toggle="collapse"
                                                    data-parent="#accordionRule${idx.count}"
                                                    href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                    aria-controls="collapseInnerArea${idx.count}"
                                                    style="text-align:center;">
                                                <span>
                                                <c:forEach items="${fn:split(activityCog.roleLimit, ',')}" var='role'>
                                                    <c:if test="${role eq '1'}">终端老板</c:if>
                                                    <c:if test="${role eq '0'}">配送员</c:if>
                                                    <c:if test="${role eq '3'}">分销商</c:if><br>
                                                </c:forEach>
                                                </span>
                                                </td>
                                            </c:if>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <c:choose>
                                                    <c:when test="${activityCog.isBegin == '0'}">
                                                        <span style="color: #ecb010;"><b>待上线</b></span>
                                                    </c:when>
                                                    <c:when test="${activityCog.isBegin == '1'}">
                                                        <span style="color: #ea918c;"> <b>进行中</b> </span>
                                                    </c:when>
                                                    <c:when test="${activityCog.isBegin == '2'}">
                                                        <span style="color: #999999;"> <b>已下线</b> </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="color: red;"><b>${activityCog.isBegin}</b></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${activityCog.activityDrawNumber}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${activityCog.activityWinNumber}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <c:choose>
                                                    <c:when test="${activityCog.status eq '0'}">
                                                        <span>启用</span>
                                                    </c:when>
                                                    <c:otherwise>停用</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${activityCog.createTime}</span>
                                            </td>
                                            <td data-key="${activityCog.activityKey}" data-type="${activityCog.drainageType}" style="text-align: center;">
                                                <a class="btn btn-xs edit btn-orange"><i
                                                        class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                                    <%--<a class="btn btn-xs delCategory btn-orange"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>--%>
                                                <%--<a class="btn btn-xs prize btn-twitter"><i
                                                        class="iconfont icon-tianjiajiahaowubiankuang"></i>&nbsp;新增奖品</a>--%>
                                                <a class="btn btn-xs rule btn-orange"><i
                                                        class="iconfont icon-tianjiajiahaowubiankuang"></i>&nbsp;规 则</a>
                                                <a class="btn btn-xs btn-orange record"><i
                                                        class="iconfont icon-xinxi"></i>&nbsp;数 据</a>
                                            </td>
                                        </tr>


                                        <tr style="background-color: white;">
                                            <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">14</c:when><c:otherwise>9</c:otherwise></c:choose>"
                                                style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
                                                <div id="collapseInnerArea${idx.count}" class="panel-collapse collapse"
                                                     role="tabpanel" aria-labelledby="heading${idx.count}">
                                                    <div class="panel-body" style="padding:5px;">

                                                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
                                                            <thead>
                                                            <tr class="success" style="background-color: #f0d2d0;">
                                                                <th style="width:5%;">序号</th>
                                                                <th style="width:8%;">奖品位置</th>
                                                                <th style="width:10%;">奖品名称</th>
                                                                <th style="width:10%;">奖品类型</th>
                                                                <th style="width:8%;">发放总量</th>
                                                                <th style="width:8%;">已发放</th>
                                                                <th style="width:8%;">奖品图片</th>
                                                                <th style="width:30%;">操作</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:choose>
                                                                <c:when test="${fn:length(activityCog.turntablePrizeCogList) gt 0}">
                                                                    <c:forEach
                                                                            items="${activityCog.turntablePrizeCogList}"
                                                                            var="prizeInfo" varStatus="tacketIdx">
                                                                        <tr>
                                                                            <td style="text-align:center;">
                                                                                <span>${tacketIdx.count}</span>
                                                                            </td>
                                                                            <td style="text-align:center;">${prizeInfo.turntablePosition}</td>
                                                                            <td style="text-align:center;">${prizeInfo.turntablePrizeName}</td>
                                                                            <td style="text-align:center;">
                                                                                <c:choose>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '0'}">
                                                                                        <span>红包</span>
                                                                                    </c:when>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '1'}">
                                                                                        <span>积分</span>
                                                                                    </c:when>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '7'}">
                                                                                        <span>积分</span>
                                                                                    </c:when>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '2'}">
                                                                                        <span>实物奖</span>
                                                                                    </c:when>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '4'}">
                                                                                        <span>三方优惠券</span>
                                                                                    </c:when>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '5'}">
                                                                                        <span>商城优惠券</span>
                                                                                    </c:when>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '6'}">
                                                                                        <span>再来一次</span>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <span>谢谢参与</span>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td style="text-align:center;">
                                                                                <c:choose>
                                                                                    <c:when test="${prizeInfo.turntablePrizeType eq '3' or prizeInfo.turntablePrizeType eq '6' }">
                                                                                        <span>--</span>
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <span>${prizeInfo.launchNumber}</span>
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td style="text-align:center;">${prizeInfo.receiveNumber}</td>
                                                                            <td style="text-align:center;">
                                                                                <span><img
                                                                                        onmouseover="imgBig('<%=imageServerUrl%>
                                                                                            ${fn:split(not empty(prizeInfo.turntablePic) ? prizeInfo.turntablePic : prizeInfo.turntablePic,',')[0]}');"
                                                                                        onmouseleave="imgSmall();"
                                                                                        style="width: 50px;height: 50px;"
                                                                                        alt="图片"
                                                                                        src="<%=imageServerUrl%>${fn:split(not empty(prizeInfo.turntablePic) ? prizeInfo.turntablePic : 'prizeImg/xxcy.png',',')[0]}"/>
                                                                                </span>
                                                                            </td>
                                                                            <td data-key="${prizeInfo.infoKey}"  data-type="${activityCog.drainageType}"
                                                                                style="text-align: center;">
                                                                                <a class="btn btn-xs editPrize btn-orange"><i
                                                                                        class="iconfont icon-xiugai"></i>&nbsp;修
                                                                                    改</a>
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
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">12</c:when><c:otherwise>10</c:otherwise></c:choose>">
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
