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


                if (imgSrc.length == 0) {
                    alert("请上传活动奖品图片");
                    return false;
                }
                if (imgSrc.length > 1) {
                    $.fn.alert("奖品图片最多上传1张");
                    return false;
                } else {
                    $("[name='turntablePic']").val(imgSrc[0]);
                }

                if (imgSrc[0].indexOf("png") == -1) {
                    alert("请上传png格式的图片")
                    return false;
                }

                var url = $(this).attr("url");
                $("form").attr("action", url);
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
                return false;
            });


            $('input[name=turntablePrizeType]').on("click", function () {

                if ($(this).val() == '2') {
                    $("#money").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#prizeType").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                } else if ($(this).val() == '1') {
                    $("#money").attr("disabled", "disabled");
                    $("#prizeType").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#vpoints").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                } else if ($(this).val() == '0') {
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                } else if ($(this).val() == '4') {
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").removeAttr("disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                } else if ($(this).val() == '5') {
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#launchNumber").attr("disabled");
                } else {
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#launchNumber").attr("disabled", "disabled");
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
            <li class="current"><a title="">幸运转盘</a></li>
            <li class="current"><a title="">新增奖品</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/turntable/doTurntablePrizeAdd.do">
                <input type="hidden" name="activityKey" value="${activityKey}"/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基本信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="areaCode"/>
                        <input type="hidden" name="turntablePic" id="picUrl" value="">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">奖品名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="turntablePrizeName" tag="validate"
                                               class="form-control input-width-larger required varlength"
                                               placeholder="最多可填6字"
                                               autocomplete="off" data-length="10" maxlength="7"/>
                                        <span class="blocker en-larger" style="margin-top: 0px; color: red">/ 为换行符 同一奖品名称仅能使用一次!</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">奖项类型：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="turntablePrizeType" value="2"
                                               style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;实物奖
                                    </div>
                                    <select id="prizeType" name="prizeType" class="form-control input-width-small"
                                            style="float: left; cursor: pointer;" disabled>
                                        <option value="">请选择</option>
                                        <c:forEach items="${bigPrizes}" var="item">
                                            <option value="${item.prizeNo}">${item.prizeName}</option>
                                        </c:forEach>
                                    </select>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="turntablePrizeType" value="4"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>三方优惠券</span>
                                            </div>
                                            <select id="coupon" name="prizeType" class="form-control input-width-small"
                                                    disabled style="float: left; cursor: pointer;">
                                                <option value="">请选择</option>
                                                <c:forEach items="${couponList}" var="item">
                                                    <option value="${item.ticketNo}">${item.ticketName}</option>
                                                </c:forEach>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="turntablePrizeType" value="5"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>商城优惠券</span>
                                            </div>
                                            <select id="shopCoupon" name="prizeType" class="form-control input-width-small"
                                                    disabled style="float: left; cursor: pointer;">
                                                <option value="">请选择</option>
                                                <c:forEach items="${couponList}" var="item">
                                                    <option value="${item.ticketNo}">${item.ticketName}</option>
                                                </c:forEach>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="turntablePrizeType" value="1"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>积分</span>
                                            </div>
                                            <input name="vpoints" id="vpoints"
                                                   class="form-control required number vpoints input-width-small"
                                                   disabled tag="validate"
                                                   autocomplete="off" maxlength="9"/>
                                            <span class="blocker en-larger" style="margin-top: 0px;">积分</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="turntablePrizeType" value="0"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>红包</span>
                                            </div>
                                            <input name="money" id="money"
                                                   class="form-control money required money input-width-small rule"
                                                   disabled tag="validate"
                                                   autocomplete="off" maxlength="9"/>
                                            <span class="blocker en-larger" style="margin-top: 0px;">元</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="turntablePrizeType" value="3"
                                               style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;谢谢参与
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">奖品发放量：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input id="launchNumber" name="launchNumber" tag="validate" disabled
                                               class="form-control input-width-larger  required varlength"
                                               autocomplete="off" data-length="11" maxlength="11"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">转盘显示图片：<span
                                        class="required">*</span><br/></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;"
                                                 id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value=""
                                                           accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>
                                                </section>
                                            </div>
                                        </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除作品图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span
                                                    class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"
                                    data-url="<%=cpath%>/turntable/showTurntableActivityList.do">返
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
