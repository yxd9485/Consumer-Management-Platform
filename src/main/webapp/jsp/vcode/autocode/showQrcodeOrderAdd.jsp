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
    <title>码源订单编辑</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath}/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript" src="${basePath}/inc/My97DatePicker/WdatePicker.js"></script>


    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${basePath}/assets/js/custom/giftspack/utils.js"></script>
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

            /*点击图片的文本框*/
            //$(".file").change(changeDo);

            // 返回
            $(".btnReturn").click(function () {
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });

            $("#closeBtn").click(function () {
                $("[name='orderTotalQrcodeCount']").val('');
            });


            // 二次确认
            $("#bettenSave").click(function () {
                var flag = validForm();
                console.log(flag)
                if (flag) {
                    var url = "<%=cpath%>/vpsQrcodeOrder/checkBatchInfo.do";
                    var myForm = new FormData(document.getElementById("adQrcodeOrderForm"));
                    console.log(myForm.get("orderQrcodeCount"))
                    var formData = new FormData();
                    formData.append("factoryId", myForm.get("factoryId"));
                    formData.append("wineryId", myForm.get("wineryId"));
                    formData.append("skuKey", myForm.get("skuKey"));
                    formData.append("isWanBatch", myForm.get("isWanBatch"));
                    formData.append("isSerialNo", myForm.get("isSerialNo"));
                    formData.append("orderTime", myForm.get("orderTime"));
                    formData.append("qrcodeType", myForm.get("qrcodeType"));
                    formData.append("orderQrcodeCount", parseInt(Math.round(myForm.get("orderQrcodeCount") * 10000)));
                    formData.append("channelCount", myForm.get("channelCount"));


                    $.ajax({
                        type: "POST",
                        url: url,
                        data: formData,
                        dataType: "json",
                        async: false,
                        contentType: false,
                        processData: false,
                        beforeSend: appendVjfSessionId,
                        success: function (data) {
                            if (data["errMsg"]) {
                                $.fn.alert(data["errMsg"]);
                            } else {
                                $("#addBatchDialog").modal("show");
                                var vpsQrcodeOrder = data['vpsQrcodeOrder'];
                                for (var name in vpsQrcodeOrder) {
                                    $("#addBatchDialog label[name='" + name + "']").text(vpsQrcodeOrder[name]);
                                    if (name = "packagePassword") {
                                        $("[name='packagePassword']").val(vpsQrcodeOrder[name]);
                                    }
                                    if (name = "skuName") {
                                        $("[name='skuName']").val(vpsQrcodeOrder[name]);
                                    }
                                    if (name = "orderName") {
                                        $("[name='orderName']").val(vpsQrcodeOrder[name]);
                                    }
                                    if (name = "isLabel") {
                                        $("[name='isLabel']").val(vpsQrcodeOrder[name]);
                                    }
                                    if (name = "orderTotalQrcodeCount") {
                                        $("[name='orderTotalQrcodeCount']").val(vpsQrcodeOrder[name]);
                                    }
                                    if (name = "qrcodeType") {
                                        $("[name='qrcodeType']").val(vpsQrcodeOrder[name])
                                    }
                                }
                            }
                        }
                    });
                }
                return false;
            });


            //保存
            $("#addBatchDialog").delegate("#addBtn", "click", function () {

                // 弹出遮罩层
                $("#qrcodeFloatDiv").show();
                //赋值中文
                $("[name='qrcodeManufacture']").val($("#factoryId").find("option:selected").text());
                $("[name='brewery']").val($("#wineryId").find("option:selected").text());
                $("[name='imageUrl']").val(imgSrc);
                var a = document.getElementById("orderQrcodeCount").value;
                $("[name='orderQrcodeCount']").val(parseInt(Math.round(a * 10000)));
                $("#addBtn").attr("disabled", "disabled");
                $("#closeBtn").attr("disabled", "disabled");
                $("form").attr("action", $(this).attr("url"));
                $("form").submit();
            });


            // 初始化校验控件
            $.runtimeValidate($("#adQrcodeOrderForm"));
            // 预览批次
            $("a.preview-batch").on("click", function () {
                if ($("tr.preview-batch").css("display") == 'none') {
                    $("tr.preview-batch").css("display", "");
                    var myForm = new FormData(document.getElementById("adQrcodeOrderForm"));
                    var formData = new FormData();
                    formData.append("channelCount", myForm.get("channelCount"));
                    formData.append("orderQrcodeCount", parseInt(Math.round(myForm.get("orderQrcodeCount") * 10000)));
                    ;
                    formData.append("factoryId", myForm.get("factoryId"));
                    formData.append("wineryId", myForm.get("wineryId"));
                    formData.append("skuKey", myForm.get("skuKey"));
                    formData.append("orderTime", myForm.get("orderTime"));

                    $.ajax({
                        type: "POST",
                        url: "<%=cpath%>/vpsQrcodeOrder/showBatchInfoLst.do",
                        async: false,
                        contentType: false,
                        processData: false,
                        data: formData,
                        dataType: "json",
                        beforeSend: appendVjfSessionId,
                        success: function (result) {
                            if (result) {
                                if (result["errMsg"]) {
                                    $.fn.alert(result["errMsg"]);
                                } else {
                                    $.each(result.batchLst, function (i, item) {
                                        initPrizeItem(item, i);
                                    });
                                }

                            }
                        },
                        error: function (data) {
                            $.fn.alert("预览错误!请联系研发");
                        }
                    });

                } else {
                    $("tr.preview-batch").css("display", "none");
                    //删除原有的tr
                    $("#batchShowTable tbody tr:gt(0)").remove();
                }

            });

            $("#factoryId").on("change", function () {
                var key = $("#factoryId").val();
                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/vpsQrcodeOrder/findWineryList.do",
                    async: false,
                    data: {factoryId: key},
                    dataType: "json",
                    beforeSend: appendVjfSessionId,
                    success: function (result) {
                        var content = "<option value='' selected>请选择</option>";
                        if (result) {
                            $.each(result.wineryLst, function (i, item) {
                                content += "<option value='" + item.id + "'>" + item.wineryName + "</option>";
                            });
                            $("[name='wineryId']").html(content);
                            $("[name='channelCount']").val(result.factory.channelCount);
                            var qrcodeFormat = result.factory.qrcodeFormat;
                            var isWanBatch = result.factory.isWanBatch;
                            var qrcodeMerageFlag = result.factory.qrcodeMerageFlag;
                            var isWanBatchValue;
                            var qrcodeFormatValue;
                            if (qrcodeFormat == '1') {
                                qrcodeFormatValue = "二维码";
                            } else if (qrcodeFormat == '2') {
                                qrcodeFormatValue = "二维码,串前6,串后6";
                            } else if (qrcodeFormat == '3') {
                                qrcodeFormatValue = "二维码,串码";
                            } else if (qrcodeFormat == '4') {
                                qrcodeFormatValue = "二维码 串前6 串后6";
                            } else if (qrcodeFormat == '5') {
                                qrcodeFormatValue = "二维码,,串前6,串后6";
                            } else if (qrcodeFormat == '6') {
                                qrcodeFormatValue = "二维码 串码";
                            } else if (qrcodeFormat == '7') {
                                qrcodeFormatValue = "二维码,串前6,串后6,序号";
                            } else if (qrcodeFormat == '8') {
                                qrcodeFormatValue = "二维码,串码,序号(主推)";
                            } else if (qrcodeFormat == '9') {
                                qrcodeFormatValue = "二维码 串码 序号(主推)";
                            } else if (qrcodeFormat == '10') {
                                qrcodeFormatValue = "二维码 串前6 串后6 序号";
                            } else if (qrcodeFormat == '11') {
                                qrcodeFormatValue = "序号 二维码";
                            } else if (qrcodeFormat == '12') {
                                qrcodeFormatValue = "序号 二维码,串前6,串后6";
                            } else if (qrcodeFormat == '13') {
                                qrcodeFormatValue = "序号 二维码,串码";
                            } else if (qrcodeFormat == '14') {
                                qrcodeFormatValue = "序号 二维码 串前6 串后6";
                            } else if (qrcodeFormat == '15') {
                                qrcodeFormatValue = "序号,二维码,串码";
                            } else if (qrcodeFormat == '16') {
                                qrcodeFormatValue = "批次序号,二维码";
                            } else if (qrcodeFormat == '17') {
                                qrcodeFormatValue = "序号,二维码,串前6,串后6";
                            } else if (qrcodeFormat == '18') {
                                qrcodeFormatValue = "二维码,,串码,串前6,串后6";
                            } else if (qrcodeFormat == '19') {
                                qrcodeFormatValue = "二维码,串码,串前6,串后6";
                            } else if (qrcodeFormat == '20') {
                                qrcodeFormatValue = "二维码 奖项描述";
                            } else if (qrcodeFormat == '21') {
                                qrcodeFormatValue = "二维码,奖项描述";
                            } else if (qrcodeFormat == '22') {
                                qrcodeFormatValue = "二维码;奖项描述";
                            }
                            if (isWanBatch == '1') {
                                isWanBatchValue = "是";
                            } else {
                                isWanBatchValue = "否";
                            }
                            $("#qrcodeFormatValue").attr("value", qrcodeFormatValue);
                            $("[name='isWanBatchValue']").attr("value", isWanBatchValue);
                            $("[name='qrcodeFormat']").val(qrcodeFormat);
                            $("[name='isWanBatch']").val(isWanBatch);
                            $("#qrcodeMerageFlag").val(qrcodeMerageFlag == '1' ? "是" : "否");


                        }
                    },
                    error: function (data) {
                        $.fn.alert("查询错误!请联系研发");
                    }
                });
            });

        });

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }

            // 无奖描述校验
            if (Number($("#qrcodePercent").val()) != 100 && $("#noPrizeDesc").val() == '') {
                $.fn.alert("请输入无奖描述");
                return false;
            }

            return true;
        }

        // 初始化批次显示
        function initPrizeItem(templetDetail, itemIndex) {
            if (itemIndex > 0) {
                var $cloneItem = $("#batchShowTable tbody tr").eq(0).clone();
                $cloneItem.appendTo("#batchShowTable");
            }
            var $currTr = $("#batchShowTable").find("tbody tr").eq(itemIndex);
            $currTr.find("label[id$='id']").text(templetDetail["id"]);
            $currTr.find("label[id$='batchDesc']").text(templetDetail["batchDesc"]);
            $currTr.find("label[id$='batchName']").text(templetDetail["batchName"]);
            $currTr.find("label[id$='qrcodeAmounts']").text(templetDetail["qrcodeAmounts"]);
            $currTr.find("label[id$='packAmounts']").text(templetDetail["packAmounts"]);
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

        .validate_tips {
            padding: 8px !important;
        }

        div b {
            color: red;
        }

        .input-width-larger {
            width: 330px !important;
        }

    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 码源管理</a></li>
            <li class="current"><a title="">码源订单管理</a></li>
            <li class="current"><a title="">创建码源订单</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="adQrcodeOrderForm"
                  action="${basePath}/vpsQrcodeOrder/addQrcodeOrderAndBatch.do">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="icon-list-alt"></i>创建码源订单</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="queryParam" value="${queryParam}"/>
                        <input type="hidden" name="pageParam" value="${pageParam}"/>
                        <input type="hidden" name="packagePassword"/>
                        <input type="hidden" name="qrcodeManufacture"/>
                        <input type="hidden" name="orderTotalQrcodeCount"/>
                        <input type="hidden" name="brewery"/>
                        <input type="hidden" name="orderName"/>
                        <input type="hidden" name="skuName"/>
                        <input type="hidden" name="isLabel"/>
                        <input type="hidden" name="imageUrl"/>
                        <input type="hidden" name="qrcodeFormat"/>
                        <input type="hidden" name="isWanBatch"/>
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">客户订单号：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="clientOrderNo" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <c:if test="${currentUser.projectServerName eq 'zuijiu'}">
                                <tr>
                                    <td class="ab_left"><label class="title">码源类型：<span
                                            class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <select name="qrcodeType" tag="validate"
                                                    class="form-control input-width-larger required">
                                                <option value="">请选择</option>
                                                <option value="X">箱码</option>
                                                <option value="W">盖外码</option>
                                                <option value="N">盖内码</option>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="ab_left"><label class="title">赋码厂：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select class="form-control input-width-larger required" tag="validate"
                                                id="factoryId" name="factoryId">
                                            <option value="">请选择</option>
                                            <c:if test="${not empty factoryList}">
                                                <c:forEach items="${factoryList}" var="item">
                                                    <option value="${item.id}">${item.factoryName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title">产品生产工厂：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select class="form-control input-width-larger required" name="wineryId"
                                                id="wineryId" tag="validate">
                                            <option value="">请选择</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>


                            <tr>
                                <td class="ab_left"><label class="title">订单日期：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="orderTime" class="form-control input-width-medium Wdate required"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                                        <label class="validate_tips" style="width:35%"></label>
                                    </div>
                                </td>
                            </tr>


                            <tr>
                                <td class="ab_left"><label class="title">SKU：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select class="form-control input-width-larger required" name="skuKey"
                                                id="skuKey" tag="validate">
                                            <option value="">请选择</option>
                                            <c:if test="${not empty skuLst}">
                                                <c:forEach items="${skuLst}" var="sku">
                                                    <option value="${sku.skuKey}">${sku.skuName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">订单数量：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="orderQrcodeCount" id="orderQrcodeCount" tag="validate"
                                               class="form-control input-width-larger number rule required"
                                               autocomplete="off" maxlength="6"/>
                                        <span class="blocker en-larger">万</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">有奖赋码率：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="qrcodePercent" id="qrcodePercent" tag="validate"
                                               class="form-control input-width-larger number maxValue minValue rule required"
                                               maxVal="100" minVal="0.0001" autocomplete="off" maxlength="6"/>
                                        <span class="blocker en-larger">%</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">无奖描述<span class="white">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="noPrizeDesc" id="noPrizeDesc" tag="validate"
                                               class="form-control input-width-larger" autocomplete="off"
                                               maxlength="20"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">有奖描述<span class="white">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="prizeDesc" id="prizeDesc" tag="validate"
                                               class="form-control input-width-larger" autocomplete="off"
                                               maxlength="20"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">码源格式：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="qrcodeFormatValue" id="qrcodeFormatValue" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30" value="" readonly="true"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">文件格式：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="fileFormat" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30" value="txt" readonly="true"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title">通道数量：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="channelCount" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30" readonly="true"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>


                            <tr>
                                <td class="ab_left"><label class="title">是否一万一批次：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="isWanBatchValue" id="isWanBatchValue" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30" value="" readonly="true"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title">是否合并码源文件：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input id="qrcodeMerageFlag" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30" value="" readonly="true"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">图片上传：<span class="white">*</span><br/>建议500*500</label>
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


                            <tr>
                                <td class="ab_left"><label class="title">预览批次：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <a class="marl15 preview-batch"
                                           style="text-decoration: none; color: rgb(77, 116, 150); cursor:pointer;">预览批次</a>
                                    </div>
                                </td>
                            </tr>


                            <tr class="preview-batch" style="display: none;">
                                <td colspan="11">
                                    <div class="panel-body">
                                        <table id="batchShowTable"
                                               class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
                                            <thead>
                                            <tr class="success" style="background-color: #f0d2d0;">
                                                <th style="width:5%;">序号</th>
                                                <th style="width:30%;">批码编号</th>
                                                <th style="width:35%;">批码名称</th>
                                                <th style="width:15%;">码数量</th>
                                                <th style="width:15%;">包码数</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>
                                                    <label id="id" style="line-height: 30px;"></label>
                                                </td>
                                                <td>
                                                    <label id="batchDesc" style="line-height: 30px;"></label>
                                                </td>
                                                <td>
                                                    <label id="batchName" style="line-height: 30px;"></label>
                                                </td>

                                                <td>
                                                    <label id="qrcodeAmounts" style="line-height: 30px;"></label>
                                                </td>
                                                <td>
                                                    <label id="packAmounts" style="line-height: 30px;"></label>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" id="bettenSave">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"
                                    data-url="${basePath}/vpsQrcodeOrder/showQrcodeOrderList.do">返 回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div id="qrcodeFloatDiv"
         style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
    </div>

    <div class="modal fade" id="addBatchDialog" tabindex="-1" role="dialog" data-backdrop="static"
         aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">确定生成码源订单</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">订单名称：<span
                                    class="required"></span></label></td>
                            <td>
                                <div class="content">
                                    <label name="orderName" style="line-height: 30px;"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">密码：<span class="required"></span></label>
                            </td>
                            <td>
                                <div class="content">
                                    <label name="packagePassword" style="line-height: 30px;"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">一万一批次：<span
                                    class="required"></span></label></td>
                            <td>
                                <div class="content">
                                    <label name="isWanBatch" style="line-height: 30px;"></label>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认
                    </button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
