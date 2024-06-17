<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
    String allPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cpath;
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>跑马灯管理</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>

    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>

    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];

        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("form"));
            //容积千分符
            $("input[name='volume']").on('keyup', function () {
                $("input[name='volume']").val(thousandth($("input[name='volume']").val()));
            });

            // 初始化功能
            initPage();
        });

        function initPage() {

            // 初始化模板状态显示样式
            $("#status").bootstrapSwitch({
                onSwitchChange: function (event, state) {
                    if (state == true) {
                        $("input:hidden[name='status']").val("0");
                    } else {
                        $("input:hidden[name='status']").val("1");
                    }
                }
            });

            // 返回
            $(".btnReturn").click(function () {
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });

            // 保存
            $(".btnSave").click(function () {
                var validateResult = $.submitValidate($("form"));
                if (!validateResult) {
                    return false;
                }

                if($("input[name= exchangeEndTime]").val() == '' && $("input[name= exchangeEndDay]").val() == ''){
                    alert("兑奖截止时间不能为空");
                    return false;
                }
                if($("input[name= exchangeEndDay]").val() == '0'){
                    alert("兑奖截止时间不能为0");
                    return false;
                }

                var url = $(this).attr("url");
                $("form").attr("action", url);
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
                return false;
            });


            $('input[name=exchangeLimitRadio]').on("click", function () {
                if ($(this).val() == '0') {
                    $("#exchangeLimit").attr("disabled", "disabled");
                } else {
                    $("#exchangeLimit").removeAttr("disabled");
                }
            });

            $("[name='cashPrize']").on("change", function () {
                if ($(this).val() == '0') {
                    $("input[name='exchangeEndDay']").attr("disabled", "disabled");
                    $("input[name='exchangeEndDay']").val('')
                    $("input[name='exchangeEndTime']").removeAttr("disabled");
                }
                if ($(this).val() == '1') {
                    $("input[name='exchangeEndDay']").removeAttr("disabled");
                    $("input[name='exchangeEndTime']").val('')
                    $("input[name='exchangeEndTime']").attr("disabled", "disabled");
                }
            });

        }

    </script>

    <style>
        .white {
            color: white;
        }

        .blocker {
            float: left;
            vertical-align: middle;
            margin-right: 8px;
            margin-top: 8px;
        }

        .en-larger {
            margin-left: 8px;
        }

        .en-larger2 {
            margin-left: 12px;
        }

        .ex-larger {
            margin-right: 8px;
        }

        .show-sku-name {
            float: left;
            margin-left: 8px;
            margin-top: 8px;
        }

        .top-only {
            border-top: 1px solid #e1e1e1;
        }

        .tab-radio {
            margin: 10px 0 0 !important;
        }

        .validate_tips {
            padding: 8px !important;
        }

        .mx-right {
            float: right;
            margin-top: 2px;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a title="">活动管理</a></li>
            <li class="current"><a title="">积分兑换优惠券</a></li>
            <li class="current"><a title="">新增优惠券</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/vcodeTicketExchange/doTicketExchangeAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}"/>
                <input type="hidden" name="pageParam" value="${pageParam}"/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>优惠券新增</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="areaCode"/>
                        <input type="hidden" name="rulePic" id="picUrl" value="">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">优惠券：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="ticketNo" class="form-control required input-width-larger" tag="validate">
                                            <option value="">请选择</option>
                                            <c:forEach items="${stringStringMap}" var="item">
                                                <option value="${item.key}">${item.value}</option>
                                            </c:forEach>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券有效期：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">从</span>
                                        <input name="startDate" id="startDate"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">使用截止时间：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 100px; line-height: 30px;">
                                                <input type="radio" name="cashPrize" value="0" checked
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>固定截止时间</span>
                                            </div>
                                            <input name="exchangeEndTime" id="exchangeEndTime"
                                                   class="form-control input-width-medium Wdate required preTime"
                                                   tag="validate" style="width: 180px !important;" autocomplete="off"
                                                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 110px; line-height: 30px;">
                                                <input type="radio" name="cashPrize" value="1"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>兑换后有效天数</span>
                                            </div>
                                            <input name="exchangeEndDay" onkeyup="value=value.replace(/[^\d]/g,'')" id="exchangeEndDay" tag="validate"
                                                   class="form-control required input-width-small rule"
                                                   disabled
                                                   autocomplete="off" maxlength="10"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">消耗积分：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="vpointsCog" tag="validate"
                                               class="form-control number input-width-larger positive required" autocomplete="off" maxlength="10" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">领取限制：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="exchangeLimitRadio" value="0" class="positive" tag="validate"
                                               style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;无限制
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 40px; line-height: 25px;">
                                                <input type="radio" name="exchangeLimitRadio" value="1"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>每人</span>
                                            </div>
                                            <input name="exchangeLimit" id="exchangeLimit" tag="validate"
                                                   class="form-control required number positive input-width-small rule"
                                                   disabled
                                                   autocomplete="off" maxlength="10"/>&nbsp;张
                                            <span class="validate_tips"></span>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"
                                    data-url="<%=cpath%>/vcodeTicketExchange/showTicketExchangeList.do">返
                                回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="imageDiv" style="display: none">
        <jsp:include page="/inc/upload/uploadImage.jsp"></jsp:include>
    </div>
</div>
</body>
</html>
