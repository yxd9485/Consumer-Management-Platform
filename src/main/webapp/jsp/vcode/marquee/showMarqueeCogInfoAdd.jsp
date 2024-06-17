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

        // 本界面上传图片要求
        var customerDefaults = {
            fileType: ["jpg", "png", "bmp", "jpeg"],   // 上传文件的类型
            fileSize: 1024 * 200 // 上传文件的大小 200K
        };

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

                if ($('[name="moneyCog"]').val() == 0 && $('#moneyCheck').is(":checked")){
                    alert("配置金额区间不能为0!");
                    return false;
                }
                if ($('[name="vpointsCog"]').val() == 0 && $('#vpointsCheck').is(":checked")){
                    alert("配置积分区间不能为0!");
                    return false;
                }
                var url = $(this).attr("url");
                $("form").attr("action", url);
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
                return false;
            });

            // 增加实物奖
            $("form").on("click", "#addPrize", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addPrize").text("删除");
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });

            // 增加金额配置
            $("form").on("click", "#addMoneyCog", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addMoneyCog").text("删除");
                    $copySku.find(".money").val("");
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });

            // 增加积分配置
            $("form").on("click", "#addVpointsCog", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addVpointsCog").text("删除");
                    $copySku.find(".vpoints").val("");
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });
            // 增加实物奖配置
            $("form").on("click", "#addCouponCog", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addCouponCog").text("删除");
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });

            $('#prizeCheck').click(function() {
                if($(this).is(":checked")) {
                    $('#prize').show();
                    $('[name="prizeCog"]').removeAttr("disabled");
                } else {
                    $('#prize').hide();
                    $('[name="prizeCog"]').attr("disabled", "disabled");
                }
            });
            $('#moneyCheck').click(function() {
                if($(this).is(":checked")) {
                    $('#money').show();
                    $('[name="moneyCog"]').removeAttr("disabled");
                } else {
                    $('#money').hide();
                    $('[name="moneyCog"]').attr("disabled", "disabled");
                }
            });
            $('#vpointsCheck').click(function() {
                if($(this).is(":checked")) {
                    $('#vpoints').show();
                    $('[name="vpointsCog"]').removeAttr("disabled");
                } else {
                    $('#vpoints').hide();
                    $('[name="vpointsCog"]').attr("disabled", "disabled");
                }
            });
            $('#couponCheck').click(function() {
                if($(this).is(":checked")) {
                    $('#coupon').show();
                    $('[name="couponCog"]').removeAttr("disabled");
                } else {
                    $('#coupon').hide();
                    $('[name="couponCog"]').attr("disabled", "disabled");
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
            <li class="current"><a title="">跑马灯配置</a></li>
            <li class="current"><a title="">新增跑马灯</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/marquee/doMarqueeCogInfoAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}"/>
                <input type="hidden" name="pageParam" value="${pageParam}"/>
                <input type="hidden" name="skuLogo"/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>跑马灯新增</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">是否启用：<span class="required"> </span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="status" type="hidden" value="0"/>
                                        <input id="status" type="checkbox" checked data-size="small" data-on-text="启用"
                                               data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="marqueeName" tag="validate"
                                               class="form-control input-width-larger required varlength"
                                               autocomplete="off" data-length="10" maxlength="10"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">渠道类型：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" name="winType" value="0" checked="checked"/>扫码抽奖
                                        <input type="radio" name="winType" value="2" />转盘抽奖
                                        <input type="radio" name="winType" value="3" />盲盒抽奖
                                        <input type="radio" name="winType" value="4" />福利站
                                        <input type="radio" name="winType" value="5" />福袋抽奖
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">显示时间：<span class="required">*</span></label>
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
                                <td class="ab_left"><label class="title">显示条数：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="showNum" tag="validate"
                                               class="form-control required number positive minValue maxValue"
                                               style="width: 500px;" autocomplete="off" maxlength="30" minVal="1"
                                               maxVal="50"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">奖项类型：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <input type="checkbox" id="prizeCheck" name="prizeCheck" >实物奖
                                    <input type="checkbox" id="moneyCheck" name="moneyCheck" checked="checked">现金
                                    <input type="checkbox" id="vpointsCheck" name="vpointsCheck">积分
                                    <input type="checkbox" id="couponCheck" name="couponCheck">优惠券
                                </td>
                            </tr>
                            <tr id="prize" style="display: none">
                                <td class="ab_left"><label class="title">实物奖：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="prizeCog"
                                                tag="validate" disabled>
                                            <option value="">请选择实物奖</option>
                                            <c:if test="${!empty bigPrizes}">
                                                <c:forEach items="${bigPrizes}" var="item">
                                                    <option value="${item.prizeNo}">${item.prizeName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="title mart5 btn-txt-add-red"
                                               style="font-weight: normal; margin-left: 5px;" id="addPrize">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="money">
                                <td class="ab_left"><label class="title">配置金额：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content commendInfo" style="display: flex; margin: 5px 0px;">
                                        <input name="moneyCog"
                                               class="form-control required preValue money input-width-small preValue required minValue"
                                               tag="validate"
                                               autocomplete="off" maxlength="9"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="moneyCog"
                                               class="form-control required sufValue money input-width-small sufValue required minValue"
                                               tag="validate"
                                               autocomplete="off" maxlength="9"/>
                                        <span class="blocker en-larger">元</span>
                                        <label class="title mart5 btn-txt-add-red"
                                               style="font-weight: normal; margin-left: 5px;"
                                               id="addMoneyCog">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="vpoints" style="display: none">
                                <td class="ab_left"><label class="title">配置积分：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content commendInfo" style="display: flex; margin: 5px 0px;">
                                        <input name="vpointsCog"
                                               class="form-control required preValue  vpoints input-width-small preValue required"
                                               tag="validate"
                                               autocomplete="off" maxlength="9" disabled/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="vpointsCog"
                                               class="form-control required sufValue vpoints input-width-small sufValue required"
                                               tag="validate"
                                               autocomplete="off" maxlength="9" disabled/>
                                        <label class="title mart5 btn-txt-add-red"
                                               style="font-weight: normal; margin-left: 5px;"
                                               id="addVpointsCog">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="coupon" style="display: none">
                                <td class="ab_left"><label class="title">优惠券：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="couponCog"
                                                tag="validate" disabled>
                                            <option value="">请选择优惠券</option>
                                            <c:if test="${!empty couponMap}">
                                                <c:forEach items="${couponMap}" var="item">
                                                    <option value="${item.key}">${item.value}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="title mart5 btn-txt-add-red"
                                               style="font-weight: normal; margin-left: 5px;"
                                               id="addCouponCog">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-url="<%=cpath%>/marquee/showMarqueeList.do">返
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
