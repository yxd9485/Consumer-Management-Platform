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
            // 新增奖品
            $("a.prize").off();
            $("a.prize").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/turntable/showTurntablePrizeAdd.do?key=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 规则
            $("#addTurntableRule").off();
            $("#addTurntableRule").on("click", function () {
                var key = $(this).data("key");
                var url = "<%=cpath%>/turntable/showTurntableActivityRuleCogAdd.do?activityKey=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/turntable/showTurntableActivityRuleCogEdit.do?rebateRuleKey=" + key;
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
            <li class="current"><a title="">幸运转盘</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">转盘规则</span>
                    </div>
                    <div class="col-md-10 text-right">
                        <c:if test="${currentUser.roleKey ne '4'}">
                            <a id="addTurntableRule" data-key="${activityKey}" class="btn btn-blue">
                                <i class="iconfont  icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;添加规则
                            </a>
                        </c:if>
                    </div>
                </div>

                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                          action="<%=cpath%>/turntable/showTurntableActivityRuleCogList.do">
                    </form>
                </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">规则列表</span>
                    </div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:8%;">规则名称</th>
                                <th style="width:10%;">规则区域</th>
                                <th style="width:8%;">规则类型</th>
                                <th style="width:8%;">活动时间</th>
                                <th style="width:8%;">规则备注</th>
                                <th style="width:25%;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="ruleCog" varStatus="idx">
                                        <tr style="cursor:pointer">
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>${ruleCog.rebateRuleName}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${ruleCog.areaName != null and ruleCog.areaName != ''}">
                                                            ${ruleCog.areaName}
                                                        </c:when>
                                                        <c:otherwise>全部</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}">
                                                <span>
                                                    <c:choose>
                                                          <c:when test="${ruleCog.ruleType == '1'}">节假日</c:when>
                                                          <c:when test="${ruleCog.ruleType == '2'}">时间段</c:when>
                                                          <c:when test="${ruleCog.ruleType == '3'}">周几</c:when>
                                                          <c:when test="${ruleCog.ruleType == '4'}">每天</c:when>
                                                          <c:when test="${ruleCog.ruleType == '5'}">周签到</c:when>
                                                      </c:choose>
				                                   </span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <c:choose>
                                                    <c:when test="${ruleCog.ruleType == '3'}"><span>${ruleCog.beginDate} - ${ruleCog.endDate}</span></c:when>
                                                    <c:when test="${ruleCog.ruleType == '1' or ruleCog.ruleType == '2'}">
                                                        <span>${ruleCog.beginDate}</span>
                                                        <hr style="margin-bottom: 8px; margin-top: 9px; border-top: 0px solid white;">
                                                        <span>${ruleCog.endDate}</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse"
                                                data-parent="#accordionRule${idx.count}"
                                                href="#collapseInnerArea${idx.count}" aria-expanded="false"
                                                aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${ruleCog.remarks}</span>
                                            </td>
                                            <td data-key="${ruleCog.rebateRuleKey}" style="text-align: center;">
                                                <a class="btn btn-xs edit btn-orange"><i
                                                        class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                            </td>
                                        </tr>


                                        <tr style="background-color: white;">
                                            <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">10</c:when><c:otherwise>7</c:otherwise></c:choose>"
                                                style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
                                                <div id="collapseInnerArea${idx.count}" class="panel-collapse collapse"
                                                     role="tabpanel" aria-labelledby="heading${idx.count}">
                                                    <div class="panel-body" style="padding:5px;">

                                                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
                                                            <thead>
                                                            <tr class="success" style="background-color: #f0d2d0;">
                                                                <th style="width:5%;">序号</th>
                                                                <th style="width:10%;">奖品类型</th>
                                                                <th style="width:8%;">随机类型</th>
                                                                <th style="width:8%;">配置积分</th>
                                                                <th style="width:8%;">配置金额</th>
                                                                <th style="width:8%;">总个数</th>
                                                                <th style="width:8%;">剩余数</th>
                                                                <th style="width:8%;">概率</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:choose>
                                                                <c:when test="${fn:length(ruleCog.vpointsCogLst) gt 0}">
                                                                    <c:forEach
                                                                            items="${ruleCog.vpointsCogLst}"
                                                                            var="vpoint" varStatus="tacketIdx">
                                                                        <tr>
                                                                            <td style="text-align:center;">
                                                                                <span>${tacketIdx.count}</span>
                                                                            </td>
                                                                            <td style="text-align:center;">
				                                                                  <span>
				                                                                  <c:forEach items="${prizeTypeMap}"
                                                                                             var="item">
                                                                                      <c:if test="${vpoint.prizeType == item.key}">
                                                                                          ${item.value}
                                                                                          <c:if test="${vpoint.prizePayMoney > 0}">-支付:${vpoint.prizePayMoney}</c:if>
                                                                                          <c:if test="${vpoint.prizeDiscount > 0}">-优惠:${vpoint.prizeDiscount}</c:if>
                                                                                      </c:if>
                                                                                  </c:forEach>
				                                                                  </span>
                                                                            </td>
                                                                            <td style="text-align:center;">
                                                                                <span>
                                                                                <c:choose>
                                                                                    <c:when test="${vpoint.randomType eq '0'}">随机</c:when>
                                                                                    <c:when test="${vpoint.randomType eq '1'}">固定</c:when>
                                                                                </c:choose>
                                                                                </span>
                                                                            </td>
                                                                            <td style="text-align:center;">
                                                                                <c:choose>
                                                                                    <c:when test="${vpoint.randomType eq '0'}">
                                                                                        <span>${vpoint.minVpoints}&nbsp;-&nbsp;${vpoint.maxVpoints}</span>
                                                                                    </c:when>
                                                                                    <c:when test="${vpoint.randomType eq '1'}">
                                                                                        <span>${vpoint.minVpoints}</span>
                                                                                    </c:when>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td style="text-align:center;">
                                                                                <c:choose>
                                                                                    <c:when test="${vpoint.randomType eq '0'}">
                                                                                        <span>${vpoint.minMoney}&nbsp;-&nbsp;${vpoint.maxMoney}</span>
                                                                                    </c:when>
                                                                                    <c:when test="${vpoint.randomType eq '1'}">
                                                                                        <span>${vpoint.minMoney}</span>
                                                                                    </c:when>
                                                                                </c:choose>
                                                                            </td>
                                                                            <td>
                                                                                <span>${vpoint.cogAmounts}</span>
                                                                            </td>
                                                                            <td>
                                                                                <span>${vpoint.restAmounts}</span>
                                                                            </td>
                                                                            <td style="text-align:center;">
                                                                                <span>${vpoint.rangePercent}%</span>
                                                                            </td>
                                                                        </tr>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <tr>
                                                                        <td colspan="8"><span>查无数据！</span></td>
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
                                        <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">8</c:when><c:otherwise>7</c:otherwise></c:choose>">
                                            <span>查无数据！</span></td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
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
