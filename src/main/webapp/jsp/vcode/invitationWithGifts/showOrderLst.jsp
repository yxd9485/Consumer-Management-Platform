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

            // 跳转门店码
            $("a.shop").off();
            $("a.shop").on("click", function(){
                var url = "<%=cpath%>/invitationCode/showCodeLst.do?vjfSessionId=${vjfSessionId}&tabsFlag=1&activityKey=${activityKey}";
                window.location.href = url;
            });

            // 跳转导购码
            $("a.person").off();
            $("a.person").on("click", function(){
                var url = "<%=cpath%>/invitationCode/showCodeLst.do?vjfSessionId=${vjfSessionId}&tabsFlag=2&activityKey=${activityKey}";
                window.location.href = url;
            });

            // 创建订单
            $("#addItem").click(function(){
                $("#orderWindow :input[orderName]:not(:radio)").val("");
                $("#orderWindow :input[qrcodeNum]:not(:radio)").val("");
                $("#orderWindow .modal-title").text("创建订单");
                $("#orderWindow tr.reward-setting").hide();
                $("#orderWindow input[name='qrcodeType']").eq(0).prop("checked", "true");
                $("#orderWindow").modal("show");
            });

            // 修改订单
            $("a.edit").off();
            $("a.edit").on("click", function(){
                $("#orderWindow .modal-title").text("修改订单");
                $("#orderWindow input[name='infoKey']").val($(this).parents("td").data("key"));
                $("#orderWindow input[name='orderName']").val($(this).closest("tr").find("#orderName").text());
                $("#orderWindow input[name='qrcodeNum']").val($(this).closest("tr").find("#qrcodeNum").text());
                var qrcodeType = $(this).closest("tr").find("#qrcodeType").text().trim();
                if(qrcodeType == '门店码'){
                    $("#orderWindow input[name='qrcodeType']").eq(0).prop("checked", "true");
                }else{
                    $("#orderWindow input[name='qrcodeType']").eq(1).prop("checked", "true");
                }
                $("#orderWindow tr.reward-setting").hide();
                $("#orderWindow").modal("show");
            });

            // 确定创建或修改订单
            $("#orderWindow").delegate("#addBtn", "click", function(){
                // 输入元素校验
                var validateResult = $.submitValidate($("#orderWindow"));
                if(!validateResult){
                    return false;
                }

                var infoKey = $("#orderWindow input[name='infoKey']").val();
                if (infoKey == ""){
                    // 创建订单
                    // 名称检查
                    var isFail = checkName("");
                    if (isFail){
                        return false;
                    }

                    var tips = "您确定创建订单吗? </br></br></br>" + $("#orderWindow input[name='orderName']").val();
                    $.fn.confirm(tips, function(){
                        // 提交表单
                        var url = "<%=cpath%>/invitationOrder/addOrder.do";
                        var paramJson = {};
                        $("#orderWindow :input[name]").each(function(){
                            paramJson[$(this).attr("name")] = $(this).val();
                        });
                        paramJson['activityKey'] = '${activityKey}';
                        var qrcodeType = $("input[name='qrcodeType']:checked").val();
                        paramJson['qrcodeType'] = qrcodeType;
                        $.ajax({
                            type: "POST",
                            url: url,
                            data: paramJson,
                            dataType: "json",
                            async: false,
                            beforeSend:appendVjfSessionId,
                            success:  function(data) {
                                $("#orderWindow #closeBtn").trigger("click");
                                $.fn.alert(data['errMsg'], function(){
                                    $("button.btn-primary").trigger("click");
                                });
                            }
                        });
                    });
                }else {
                    // 修改订单
                    // 名称检查
                    var isFail = checkName(infoKey);
                    if (isFail){
                        return false;
                    }

                    var tips = "您确定修改订单吗? </br></br></br>" + $("#orderWindow input[name='orderName']").val();
                    $.fn.confirm(tips, function(){
                        // 提交表单
                        var url = "<%=cpath%>/invitationOrder/editOrder.do";
                        var paramJson = {};
                        $("#orderWindow :input[name]").each(function(){
                            paramJson[$(this).attr("name")] = $(this).val();
                        });
                        paramJson['activityKey'] = '${activityKey}';
                        var qrcodeType = $("input[name='qrcodeType']:checked").val();
                        paramJson['qrcodeType'] = qrcodeType;
                        $.ajax({
                            type: "POST",
                            url: url,
                            data: paramJson,
                            dataType: "json",
                            async: false,
                            beforeSend:appendVjfSessionId,
                            success:  function(data) {
                                $("#orderWindow #closeBtn").trigger("click");
                                $.fn.alert(data['errMsg'], function(){
                                    $("button.btn-primary").trigger("click");
                                });
                            }
                        });
                    });
                }
            });

            // 删除订单
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/invitationOrder/deleteOrder.do?infoKey="+key;
                $.fn.confirm("确认删除订单吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });

            // 生成码
            $("a.addCode").click(function(){
                var $td = $(this).parent("td");
                var key = $td.data("key");
                var url = "<%=cpath%>/invitationOrder/createCode.do?infoKey="+key+"&projectServerName=${projectServerName}";
                $.fn.confirm("确定要生成邀请码吗？", function(){
                    $td.html("进行中...");
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
        });

        function checkName(infoKey){
            var orderName = $("#orderWindow input[name='orderName']").val();
            var checkFailFlag = false;
            $.ajax({
                url : "${basePath}/invitationOrder/checkName.do",
                data:{
                    "activityKey":'${activityKey}',
                    "infoKey":infoKey,
                    "checkName":orderName
                },
                type : "POST",
                dataType : "json",
                async : false,
                beforeSend: appendVjfSessionId,
                success: function (data) {
                    if (data == "0") {
                        $.fn.alert("订单名称已存在，请重新输入");
                        checkFailFlag = true;
                    }
                }
            });
            return checkFailFlag;
        }

        /*下载邀请码*/
        function downloadQrcode(orderKey, projectServerName) {
            var url = "${basePath}/invitationOrder/exportCode.do?orderKey=" + orderKey
                + "&projectServerName=" + projectServerName + "&vjfSessionId=" + $("#vjfSessionId").val();
            window.open(url,"_blank");
        }
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
            <li class="current"><a> 邀请有礼</a></li>
            <li class="current"><a title="">
                <c:if test="${tabsFlag eq '0'}">二维码订单</c:if>
                <c:if test="${tabsFlag eq '1'}">店主码</c:if>
                <c:if test="${tabsFlag eq '2'}">导购码</c:if>
            </a></li>
        </ul>
    </div>

    <div class="row">
        <div class="col-md-12 tabbable">
            <a href="<%=cpath%>/invitationOrder/showOrderLst.do?vjfSessionId=${vjfSessionId}&tabsFlag=0&activityKey=${activityKey}"
               class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">二维码订单</a>
            <a class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if> shop">店主码</a>
            <a class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if> person">导购码</a>
        </div>
    </div>

    <div class="row" id="orderData">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <%--新增活动--%>
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 12px; color: black;">查询</span></div>
                    <div class="col-md-10 text-right">
                        <c:if test="${currentUser.roleKey ne '4'}">
                            <a id="addItem" class="btn btn-blue">
                                <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 创建订单
                            </a>
                        </c:if>
                    </div>
                </div>

                <%--查询条件--%>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="<%=cpath%>/invitationOrder/showOrderLst.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                        <input type="hidden" name="activityKey" value="${activityKey}"/>

                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group little_distance search"  style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">订单名称：</label>
                                        <input name="orderName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                            <input type="hidden" id="screenWidth1366" value="5,5,9,8,8,8,8,8,8,8,8,8,8" />
                            <thead>
                                <tr>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:8%;" >订单名称</th>
                                    <th style="width:10%;" >码类型</th>
                                    <th style="width:12%;" >码数量</th>
                                    <th style="width:12%;" >订单状态</th>
                                    <th style="width:8%;" >创建时间</th>
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
                                                    <span id="orderName">${item.orderName}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span id="qrcodeType">
                                                        <c:if test="${item.qrcodeType eq '0'}">门店码</c:if>
                                                        <c:if test="${item.qrcodeType eq '1'}">导购码</c:if>
                                                    </span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span id="qrcodeNum">${item.qrcodeNum}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>
                                                        <c:if test="${item.status eq '0'}">未生成邀请码</c:if>
                                                        <c:if test="${item.status eq '1'}">正在生成邀请码</c:if>
                                                        <c:if test="${item.status eq '2'}">已生成邀请码</c:if>
                                                        <c:if test="${item.status eq '3'}">邀请码生成失败</c:if>
                                                    </span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.createTime}</span>
                                                </td>
                                                <td data-key="${item.infoKey}" style="text-align: center;">
                                                    <c:if test="${item.status eq '0'}">
                                                        <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修改</a>
                                                        <a class="btn btn-xs del btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;删除</a>
                                                        <a class="btn btn-xs addCode btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;生成邀请码</a>
                                                    </c:if>
                                                    <c:if test="${item.status eq '1'}">
                                                        <span>进行中...</span>
                                                    </c:if>
                                                    <c:if test="${item.status eq '2'}">
                                                        <a class="btn btn-xs export btn-orange"
                                                           onclick="downloadQrcode('${item.infoKey}', '${projectServerName}')"><i class="iconfont icon-xiugai"></i>&nbsp;下载邀请码</a>
                                                    </c:if>
                                                    <c:if test="${item.status eq '3'}">
                                                        <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修改</a>
                                                        <a class="btn btn-xs del btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;删除</a>
                                                        <a class="btn btn-xs addCode btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;重新生成邀请码</a>
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

    <%--弹窗--%>
    <div class="modal fade" id="orderWindow" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="orderWindowTitle">编辑订单</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table" style="line-height: 30px;">
                        <tr class="reward-setting">
                            <td width="25%" class="text-right"><label class="title">订单主键：<span class="white">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input type="hidden" name="infoKey">
                                    <label id="clientOrderNo"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">订单名称：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="orderName" tag="validate" class="form-control required input-width-larger" autocomplete="off" maxlength="30" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">邀请码类型：<span class="required">*</span></label></td>
                            <td>
                                <div class="content" style="display: flex; line-height: 30px;">
                                    <input type="radio" class="tab-radio" name="qrcodeType" value="0" checked style="margin-top:8px; cursor: pointer;" /> <span> &nbsp;门店码&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    <input type="radio" class="tab-radio" name="qrcodeType" value="1" style="margin-top:8px; cursor: pointer;" /> <span> &nbsp;导购码&nbsp;</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">邀请码个数：<span class="required">*</span></label></td>
                            <td class="ab_main" colspan="3">
                                <div class="content">
                                    <input name="qrcodeNum" value="1" type="number" class="form-control input-width-larger integer minValue maxValue required"
                                           autocomplete="off" maxlength="6" minval="1" maxVal="200000" tag="validate" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

