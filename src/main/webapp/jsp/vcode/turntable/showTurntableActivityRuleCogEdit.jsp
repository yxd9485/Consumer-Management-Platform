<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil" %>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=1"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

    <script>

        $(function () {
            // 为减少初始化时的计算量，默认为false
            calculateUnitFlag = false;
            initPrizeItemFlag = false;
            // 初始化校验控件
            $.runtimeValidate($("form"));
            // 初始化功能
            initPage();
            // 初始化功能
            initRuleTempletPage();
            // 初始化完成后，打开计算开关
            calculateUnitFlag = true;
        });

        function initPage() {

            // 返回
            $(".btnReturn").click(function () {
                $("form").attr("action", $(this).attr("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });

            // 保存
            $(".btnSave").click(function () {
                var flag = validFile();
                if (flag) {
                    if ('1' === $("#projectName").val()) {
                        if (!$('.roleInfo input[type=radio]:checked').val()) {
                            $.fn.alert("请至少选择一个扫码角色");
                            return false;
                        }
                    }

                    // 校验奖项配置项百分比
                    if ($("#commonScanPrize").find("#totalPrizePercent").text() != "100.0000%") {
                        $.fn.alert("奖项配置项占比之和未达到100%");
                        return false;
                    }

                    // 校验奖项配置项总数
                    if ($("#commonScanPrize").find("#totalPrizeCount").text() != $("input[name='ruleTotalPrize']").val()) {
                        $.fn.alert("奖项配置项总数不等于转盘中奖数!");
                        return false;
                    }

                    var checkFlag = true;
                    // 校验三重防护
                    var url = "<%=cpath%>/vcodeActivityRebateRule/checkTripleProtection.do";
                    var myForm = new FormData(document.getElementById("import_form"));
                    var formData = new FormData();
                    <!--上传文件 -->
                    $uploadFile = $("input.import-file");
                    var files = $uploadFile.val();
                    if ($("[name='prizeCogType']").val() == "0" && files != "") {
                        formData.append("filePath", $uploadFile[0].files[0]);
                    }
                    <!-- 配置规则配置对象 后台解析-->
                    formData.append("activityType", myForm.get("activityType"));
                    formData.append("prizeCogType", myForm.get("prizeCogType"));
                    formData.append("ruleTotalPrize", myForm.get("ruleTotalPrize"));
                    formData.append("firstScanPercent", myForm.get("firstScanPercent"));
                    formData.append("vcodeActivityKey", myForm.get("vcodeActivityKey"));
                    formData.append("ruleUnitMoney", myForm.get("ruleUnitMoney"));
                    formData.append("ruleType", myForm.get("ruleType"));
                    formData.append("endDate", myForm.get("endDate"));
                    if (myForm.get("moneyDanping") != '') {
                        formData.append("moneyDanping", myForm.get("moneyDanping"));
                    }
                    formData.append("randomType", myForm.getAll("randomType"));
                    formData.append("minMoney", myForm.getAll("minMoney"));
                    formData.append("scanType", myForm.getAll("scanType"));
                    formData.append("fixationMoney", myForm.getAll("fixationMoney"));

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
                            console.log(data);
                            if (data["errMsg"]) {
                                checkFlag = false;
                                console.log(data["errMsg"])
                                if (data["errMsg"] == "奖项配置项解析异常") {
                                    $.fn.alert(data["errMsg"]);
                                } else {
                                    $.fn.confirm(data["errMsg"], function () {
                                        $(".btnSave").attr("disabled", "disabled");
                                        // 去除文件的disabled属性
                                        $("input.import-file").removeAttr("disabled", "disabled")
                                        $("form").attr("action", $(this).attr("url"));
                                        $("form").attr("onsubmit", "return true;");
                                        $("form").submit();
                                    });
                                }
                            }
                        }
                    });
                    if (checkFlag) {
                        $(".btnSave").attr("disabled", "disabled");
                        // 去除文件的disabled属性
                        $("input.import-file").removeAttr("disabled", "disabled")
                        $("form").attr("action", $(this).attr("url"));
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    }
                }
                return false;
            });

            // 类型为每天时，日期区域不可用
            $("[name='ruleType']").on("change", function (evt) {

                if ($(this).val() == '3' || $(this).val() == '4' || $(this).val() == '5') {
                    $("[name='hotAreaKey']").attr("disabled", true);
                    $("div.erupt-content :input, #addEruptItem, #addEruptPerItem").attr("disabled", "disabled");

                    $("#appointActivityKey").attr("disabled", true);
                    $("[name='appointRebateRuleKey']").attr("disabled", true);
                    $("[name='restrictVpoints']").attr("readonly", true);
                    $("[name='restrictMoney']").attr("readonly", true);
                    $("[name='restrictBottle']").attr("readonly", true);
                    $("[name='restrictCount']").attr("readonly", true);
                    $("#restrictTimeType1").attr("disabled", true);
                    $("#restrictTimeType2").attr("disabled", true);

                } else {
                    $("div.erupt-content :input, #addEruptItem, #addEruptPerItem").removeAttr("disabled");

                    $("#appointActivityKey").attr("disabled", false);
                    $("[name='appointRebateRuleKey']").attr("disabled", false);
                    $("[name='restrictVpoints']").attr("readonly", false);
                    $("[name='restrictMoney']").attr("readonly", false);
                    $("[name='restrictBottle']").attr("readonly", false);
                    $("[name='restrictCount']").attr("readonly", false);
                    $("#restrictTimeType1").attr("disabled", false);
                    $("#restrictTimeType2").attr("disabled", false);

                    if ("${areaName}" != "省外") {
                        $("[name='hotAreaKey']").attr("disabled", false);
                    }
                }

                // 节假日、每天
                if ($(this).val() == '1' || $(this).val() == '2') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input, #addDate").removeAttr("disabled");
                    $("div.week input, #addWeek").attr("disabled", "disabled");
                    $("[name='beginTime'], [name='endTime'], #addTime").removeAttr("disabled");
                }

                // 周几
                if ($(this).val() == '3') {
                    $("div.date").css("display", "none");
                    $("div.week").css("display", "block");
                    $("div.date input, #addDate").attr("disabled", "disabled");
                    $("div.week input, #addWeek").removeAttr("disabled");
                    $("[name='beginTime'], [name='endTime'], #addTime").removeAttr("disabled");
                }

                // 每天
                if ($(this).val() == '4') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input, #addDate").attr("disabled", "disabled");
                    $("div.week input, #addWeek").attr("disabled", "disabled");
                    $("[name='beginTime'], [name='endTime'], #addTime").removeAttr("disabled");
                }
                $("[name='beginDate']:enabled").val("");
                $("[name='endDate']:enabled").val("");
            });
            $("[name='ruleType']").trigger("change");

            $("div.week #beginDate").on("blur", function () {
                var beginVal = $(this).val();
                var endVal = $("div.week #endDate").val();
                if (beginVal != "") {
                    if (!$.isNumeric(beginVal) || beginVal > 7 || beginVal < 1) {
                        $.fn.alert("请输入1~7的数字");
                        $(this).val("");

                    } else if (endVal != "" && beginVal > endVal) {
                        $.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
                    }
                }
            });

            /* $("div.week #endDate").on("blur", function(){
                var beginVal = $("div.week #beginDate").val();
                var endVal = $(this).val();
                if (endVal != "") {
                    if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1) {
                        $.fn.alert("请输入1~7的数字");
                        $(this).val("");

                    } else if(beginVal != "" && beginVal > endVal) {
                        $.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
                    }
                }
            }); */

            // 选择规则
            $("[name='appointRebateRuleKey']").on("change", function () {
                var restrict = $(this).find("option:selected").attr("data-value");
                if (typeof (restrict) != 'undefined' || restrict == '') {
                    $("input[name='restrictVpoints']").val(restrict.split("-")[0]);
                    $("input[name='restrictVpoints']").attr("readonly", true);
                    $("input[name='restrictMoney']").val(Number(restrict.split("-")[1]).toFixed(2));
                    $("input[name='restrictMoney']").attr("readonly", true);
                    $("input[name='restrictBottle']").val(restrict.split("-")[2] == '0' ? "" : restrict.split("-")[2]);
                    $("input[name='restrictBottle']").attr("readonly", true);
                    $("input[name='restrictCount']").val(restrict.split("-")[3] == '0' ? "" : restrict.split("-")[3]);
                    $("input[name='restrictCount']").attr("readonly", true);

                    // 限制类型
                    if ("0" == restrict.split("-")[4]) {
                        $("#restrictTimeType1").trigger("click")
                    } else if ("1" == restrict.split("-")[4]) {
                        $("#restrictTimeType2").trigger("click")
                    }
                    $("#restrictTimeType1").attr("disabled", true);
                    $("#restrictTimeType2").attr("disabled", true);

                    $("#restrictTimeType").removeAttr("disabled");
                    $("#restrictTimeType").val(restrict.split("-")[4]);

                } else {
                    $("input[name='restrictVpoints']").val('');
                    $("input[name='restrictMoney']").val('');
                    $("input[name='restrictBottle']").val('');
                    $("input[name='restrictCount']").val('');
                    $("input[name='restrictVpoints']").attr("readonly", false);
                    $("input[name='restrictMoney']").attr("readonly", false);
                    $("input[name='restrictBottle']").attr("readonly", false);
                    $("input[name='restrictCount']").attr("readonly", false);

                    // 限制类型
                    $("#restrictTimeType1").removeAttr("disabled").trigger("click");
                    $("#restrictTimeType2").removeAttr("disabled");
                    $("#restrictTimeType").val("");
                    $("#restrictTimeType").attr("disabled", true);


                }

            });
            $("div.week").css("display", "none");

            // 省市县变化时初始化热区
            $("form").on("change", "div.area select", function () {
                if ($(this).val() != "") {
                    var areaCode = "";
                    $("td.area div.area").each(function (i) {
                        var $province = $(this).find("select.zProvince");
                        var $city = $(this).find("select.zCity");
                        var $county = $(this).find("select.zDistrict");
                        if ($county.val() != "") {
                            areaCode = areaCode + $county.val() + ",";
                        } else if ($city.val() != "") {
                            areaCode = areaCode + $city.val() + ",";
                        } else if ($province.val() != "") {
                            areaCode = areaCode + $province.val() + ",";
                        }
                    });

                    // 加载区域对应的热区
                    findHotAreaListByAreaCode(areaCode);

                }
            });

            // 初始化省市县
            $("div.area").initZone("<%=cpath%>", false, "", false, true, true, true);

            // 增加区域
            $("form").on("click", "#addArea", function () {

                if ($(this).text() == "新增") {
                    var $areaCopy = $("div.area").eq(0).clone();
                    $areaCopy.find("#addArea").text("删除");
                    $("td.area").append($areaCopy);
                    $areaCopy.initZone("<%=cpath%>", false, "", false, true, true, true);

                } else {
                    $(this).closest("div.area").remove();
                }

                if ($("td.area div.area").size() > 1) {
                    $("[name='hotAreaKey']").attr("disabled", true);
                    $("[name='appointRebateRuleKey']").attr("disabled", true);
                    $("#appointActivityKey").attr("disabled", true);

                } else {
                    $("div.area select").eq(0).change(); // 触发初始化热区
                    $("[name='hotAreaKey']").attr("disabled", false);
                    $("[name='appointRebateRuleKey']").attr("disabled", false);
                    $("#appointActivityKey").attr("disabled", false);
                }
            });


            // 增加日期
            $("form").on("click", "#addDate", function () {
                if ($(this).is("[disabled]")) return;
                if ($(this).text() == "新增") {
                    var count = $(this).closest("td.date").find("div.date").size();
                    var $dateCopy = $("div.date").eq(0).clone();
                    $dateCopy.find("#addDate").text("删除");
                    var $beginDate = $dateCopy.find("#beginDate0");
                    $beginDate.attr("id", "beginDate" + count).val("");
                    $beginDate.attr("onfocus", $beginDate.attr("onfocus").replace("endDate0", "endDate" + count));
                    var $endDate = $dateCopy.find("#endDate0");
                    $endDate.attr("id", "endDate" + count).val("");
                    $endDate.attr("onfocus", $endDate.attr("onfocus").replace("beginDate0", "beginDate" + count));
                    $("td.date").append($dateCopy);
                } else {
                    $(this).closest("div.date").remove();
                }
            });

            // 增加周几
            $("form").on("click", "#addWeek", function () {
                if ($(this).is("[disabled]")) return;
                if ($(this).text() == "新增") {
                    var $weekCopy = $("div.week").eq(0).clone();
                    $weekCopy.find("#addWeek").text("删除");
                    $weekCopy.find("#beginDate,#endDate").val("");
                    $("td.date").append($weekCopy);
                } else {
                    $(this).closest("div.week").remove();
                }
            });

            // 增加时间
            $("form").on("click", "#addTime", function () {
                if ($(this).is("[disabled]")) return;
                if ($(this).text() == "新增") {
                    var count = $(this).closest("td.time").find("div.time").size();
                    var $timeCopy = $("div.time").eq(0).clone();
                    $timeCopy.find("#addTime").text("删除");
                    var $beginTime = $timeCopy.find("#beginTime0");
                    $beginTime.attr("id", "beginTime" + count).val("00:00:00");
                    $beginTime.attr("onfocus", $beginTime.attr("onfocus").replace("endTime0", "endTime" + count));
                    var $endTime = $timeCopy.find("#endTime0");
                    $endTime.attr("id", "endTime" + count).val("23:59:59");
                    $endTime.attr("onfocus", $endTime.attr("onfocus").replace("beginTime0", "beginTime" + count));
                    $("td.time").append($timeCopy);
                } else {
                    $(this).closest("div.time").remove();
                }
            });

            // 配置规则方案
            $("[name='prizeCogType']").on("change", function () {
                if ($(this).val() == '1') {
                    $("div.templetitem").css("display", "flex");
                    $("div.fileitem").css("display", "none");
                    $("input.import-file").attr("disabled", "disabled").val("");
                } else {
                    $("div.templetitem").css("display", "none");
                    $("tr.preview-templet").css("display", "none");
                    $("div.fileitem").css("display", "flex");
                    $("input.import-file").removeAttr("disabled", "disabled");
                }
            });

            // 群组用户单选
            $("[name='isGroupUser']").on("change", function () {
                if ($(this).val() == '1') {
                    $("[name='groupId']").css("display", "flex");
                    $.ajax({
                        type: "POST",
                        url: "<%=cpath%>/vcodeActivityRebateRule/findGroupList.do",
                        async: false,
                        dataType: "json",
                        beforeSend: appendVjfSessionId,
                        success: function (result) {
                            var content = "<option value='' selected>请选择</option>";
                            if (result) {
                                $.each(result, function (i, item) {
                                    content += "<option value='" + item.id + "'>" + item.name + "</option>";
                                });
                                $("[name='groupId']").html(content);
                            } else {
                                $("[name='groupId']").html(content);
                            }
                        },
                        error: function (data) {
                            $.fn.alert("群组查询错误!请联系研发");
                        }
                    });
                } else {
                    $("[name='groupId']").css("display", "none");
                    $("#selectGroup").css("display", "none");
                    $("input[name='groupName']").val("");
                    $("[name='groupId']").html("");

                }
            });

            //根据下拉框显示按钮
            $("[name='groupId']").change(function () {
                var groupType = $(this).val();
                if (groupType != "" && groupType != "请选择") {
                    $("#selectGroup").css("display", "flex");
                    var opts = $("#groupId").find("option:selected").text();
                    $("input[name='groupName']").val(opts);
                } else {
                    $("#selectGroup").css("display", "none");
                }
            });

            // 查看群组弹窗 TODO
            $("a.edit").off();
            $("a.edit").on("click", function () {
                // 初始化批次数据
                var key = $("#groupId").val();
                var url = "<%=cpath%>/vcodeActivityRebateRule/findGroupInfo.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {groupId: key},
                    dataType: "json",
                    async: false,
                    beforeSend: appendVjfSessionId,
                    success: function (data) {
                        if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                        } else {
                            $("#selectloadDialog").modal("show");
                            var groupInfo = data['groupInfo'];
                            for (var name in groupInfo) {
                                console.log(name + "_" + "_" + groupInfo[name]);
                                if (name == "valid" && groupInfo[name] == "-1") {
                                    $("#selectloadDialog input[name='" + name + "']").val('永久');
                                } else {
                                    $("#selectloadDialog input[name='" + name + "']").val(groupInfo[name]);
                                    $("#selectloadDialog textarea[name='" + name + "']").val(groupInfo[name]);

                                }
                            }
                        }
                    }
                });
            });

            // 预览规则方案
            $("a.preview-templet").on("click", function () {
                if ($("tr.preview-templet").css("display") == 'none') {
                    $("tr.preview-templet").css("display", "");
                } else {
                    $("tr.preview-templet").css("display", "none");
                }
            });

            // 爆点新增
            $("form").on("click", "#addEruptItem", function () {
                if ($(this).is("[disabled]")) return;
                if ($(this).text() == "新增") {
                    var count = $(this).closest("div.erupt-content").find("table").size();
                    var $cloneItem = $(this).closest("table").clone(true, true);
                    $cloneItem.addClass("mart30");
                    $cloneItem.find("div.eruptperitem:gt(0)").remove();
                    var $eruptStartDate = $cloneItem.find("#eruptStartDate");
                    $eruptStartDate.attr("id", "eruptStartDate" + count).val("");
                    $eruptStartDate.attr("onfocus", $eruptStartDate.attr("onfocus").replace("eruptEndDate", "eruptEndDate" + count));
                    var $eruptEndDate = $cloneItem.find("#eruptEndDate");
                    $eruptEndDate.attr("id", "eruptEndDate" + count).val("");
                    $eruptEndDate.attr("onfocus", $eruptEndDate.attr("onfocus").replace("eruptStartDate", "eruptStartDate" + count));
                    var $eruptStartTime = $cloneItem.find("#eruptStartTime");
                    $eruptStartTime.attr("id", "eruptStartTime" + count).val("");
                    $eruptStartTime.attr("onfocus", $eruptStartTime.attr("onfocus").replace("eruptEndTime", "eruptEndTime" + count));
                    var $eruptEndTime = $cloneItem.find("#eruptEndTime");
                    $eruptEndTime.attr("id", "eruptEndTime" + count).val("");
                    $eruptEndTime.attr("onfocus", $eruptEndTime.attr("onfocus").replace("eruptStartTime", "eruptStartTime" + count));
                    $cloneItem.find("#eruptPerNum, #eruptPerMoney, #eruptPerVpoints, #eruptPerPrize").removeAttr("disabled").data("oldval", "").val("");
                    $cloneItem.find("#addEruptItem").text("删除")
                    $(this).closest("div.erupt-content").append($cloneItem);
                } else {
                    $(this).closest("table").remove();
                }
            });

            // 爆点新增倍数
            $("form").on("click", "#addEruptPerItem", function () {
                if ($(this).is("[disabled]")) return;
                if ($(this).text() == "+") {
                    $cloneItem = $(this).closest("div").clone(true, true);
                    $cloneItem.find("#eruptPerNum, #eruptPerMoney, #eruptPerVpoints, #eruptPerPrize").removeAttr("disabled").data("oldval", "").val("");
                    $cloneItem.find("#addEruptPerItem").html("-");
                    $(this).closest("td.perItem").append($cloneItem);
                } else {
                    $(this).closest("div").remove();
                }
            });

            // 爆点输入倍数时
            $("form").on("change", "#eruptPerNum", function () {
                $eruptperitem = $(this).closest("div.eruptperitem");
                if ($eruptperitem.find("#eruptPerMoney").val() == "") {
                    $eruptperitem.find("#eruptPerMoney").data("oldval", "0.00").val("0.00");
                }
                if ($eruptperitem.find("#eruptPerVpoints").val() == "") {
                    $eruptperitem.find("#eruptPerVpoints").data("oldval", "0").val("0");
                }
            });

            // 爆点中金额积分与大奖类型互斥
            $("form").on("change", "#eruptPerMoney, #eruptPerVpoints", function () {
                $eruptperitem = $(this).closest("div.eruptperitem");
                if ($eruptperitem.find("#eruptPerMoney").val() > 0 || $eruptperitem.find("#eruptPerVpoints").val() > 0) {
                    $eruptperitem.find("#eruptPerPrize").attr("disabled", "disabled");
                } else {
                    $eruptperitem.find("#eruptPerPrize").removeAttr("disabled");
                }
            });

            // 爆点中金额积分与大奖类型互斥
            $("form").on("change", "#eruptPerPrize", function () {
                $eruptperitem = $(this).closest("div.eruptperitem");
                if ($(this).val()) {
                    $eruptperitem.find("#eruptPerMoney").attr("disabled", "disabled");
                    $eruptperitem.find("#eruptPerVpoints").attr("disabled", "disabled");
                } else {
                    $eruptperitem.find("#eruptPerMoney").removeAttr("disabled");
                    $eruptperitem.find("#eruptPerVpoints").removeAttr("disabled");
                }
            });

            // 单击时清空选择的文件，当选择同一文件时才能触发change事件
            $("input[name='filePath']").on("click", function () {
                $(this).val("");
            });

            // 校验奖项配置项配置文件
            $("input[name='filePath']").on("change", function () {
                $uploadFile = $(this);
                var files = $uploadFile.val();
                if ($("[name='prizeCogType']").val() != "0"
                    || files == "" || files.indexOf("xls") == -1) {
                    return false;
                }
                $("div.fileitem input[id]").removeData("validval");

                // 提交表单
                var url = "<%=cpath%>/vcodeActivityRebateRule/checkActivityVpointsForImport.do";
                var formData = new FormData();
                formData.append("filePath", $uploadFile[0].files[0]);
                formData.append("activityType", $("input[name='activityType']").val());
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
                        console.log(data);
                        if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                            $("input[name='filePath']").val("");
                        } else {
                            var validFlag = false;
                            $.each(data, function (key) {
                                var $validItem = $("div.fileitem #" + key);
                                console.log(key + "_" + $validItem.val() + "_" + data[key]);
                                $validItem.data("validval", data[key]);
                                if ($validItem.val() != "") {
                                    if (key == "minMoney" || key == "minVpoints") {
                                        if (Number($validItem.val()) > Number(data[key])) {
                                            validFlag = true;
                                        }
                                    } else if (Number($validItem.val()) < Number(data[key])) {
                                        validFlag = true;
                                    }
                                    if (validFlag) {
                                        validFlag = false;
                                        $validItem.css("color", "red");
                                        $validItem.trigger("change");
                                    }
                                }
                            });
                        }
                    }
                });
            });

            // 校验奖项配置项配置文件
            $("div.fileitem input[id]").on("change", function () {
                $validItem = $(this);
                var validItemId = $validItem.attr("id");
                if (validItemId == "minMoney" || validItemId == "maxMoney") {
                    if ($("div.fileitem #minMoney").val() != "" && $("div.fileitem #maxMoney").val() != ""
                        && Number($("div.fileitem #minMoney").val()) > Number($("div.fileitem #maxMoney").val())) {
                        $validItem.val("");
                        $.fn.alert("金额区间最小值必须要小于等于区间最大值！");
                    }
                } else if (validItemId == "minVpoints" || validItemId == "maxVpoints") {
                    if ($("div.fileitem #minVpoints").val() != "" && $("div.fileitem #maxVpoints").val() != ""
                        && Number($("div.fileitem #minVpoints").val()) > Number($("div.fileitem #maxVpoints").val())) {
                        $validItem.val("");
                        $.fn.alert("积分区间最小值必须要小于等于区间最大值！");
                    }
                }

                $validItem.css("color", "#555");
                if ($validItem.val() != "") {
                    if (validItemId == "minMoney" && Number($validItem.val()) > Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最小金额小于校验金额区间最小值！");
                    } else if (validItemId == "maxMoney" && Number($validItem.val()) < Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最大金额大于校验金额区间最大值！");
                    } else if (validItemId == "minVpoints" && Number($validItem.val()) > Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最小积分小于校验积分区间最小值！");
                    } else if (validItemId == "maxVpoints" && Number($validItem.val()) < Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最大积分大于校验积分区间最大值！");
                    } else if ((validItemId == "totalMoney" || validItemId == "totalPrize" || validItemId == "totalVpoints")
                        && Number($validItem.val()) < Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件的" + $validItem.prev("span").text().replace("：", "") + "大于校验值！");
                    }
                }
            });

            // 指定继承活动
            $("#appointActivityKey").on("change", function () {
                $("[name='appointRebateRuleKey']").html("<option value=''>请选择</option>");
                var vcodeActivityKey = $(this).val();
                if (vcodeActivityKey == "") return;
                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/vcodeActivityRebateRule/queryAppointRebateRule.do",
                    async: false,
                    data: {"vcodeActivityKey": vcodeActivityKey},
                    dataType: "json",
                    beforeSend: appendVjfSessionId,
                    success: function (data) {
                        if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                        } else {
                            var content = "<option value=''>请选择</option>";
                            if (data["rebateRuleCogLst"]) {
                                $.each(data["rebateRuleCogLst"], function (i, item) {
                                    content += "<option value='" + item.rebateRuleKey + "' data-value='" + item.restrictVpoints + "-" + item.restrictMoney + "-" + item.restrictBottle + "-" + item.restrictCount + "-" + item.restrictTimeType + "'>";
                                    if (item.ruleType == "1") {
                                        content += item.areaName + " 节假日："
                                    } else if (item.ruleType == "2") {
                                        content += item.areaName + " 时间段："
                                    }
                                    content += item.beginDate + " " + item.beginTime + " - " + item.endDate + " " + item.endTime + "</option>";
                                });
                            }
                            $("[name='appointRebateRuleKey']").html(content);
                        }
                    }
                });
            });

            // 翻倍返利类型切换
            $("input[name='allowanceaRebateType']").on("change", function () {
                if ($(this).val() == "0") {
                    $("#allowanceVpointsTr input").val("");
                    $("#allowanceMoneyTr").css("display", "");
                    $("#allowanceVpointsTr").css("display", "none");
                } else {
                    $("#allowanceMoneyTr input").val("");
                    $("#allowanceMoneyTr").css("display", "none");
                    $("#allowanceVpointsTr").css("display", "");
                }
            });
        }

        function initRuleTempletPage() {

            // 金额格式
            $("form").on("change", ".moneyFmt", function(){
                $(this).val(Number($(this).val()).toFixed(2));
            });

            // 奖项配置项
            $("form").on("change", ".cogItemCB", function(){
                var optCogItem = $(this).attr("id");
                var $parentTable = $(this).closest("table.scan_type_table");
                $parentTable.find("." + optCogItem).css("display", $(this).is(':checked') ? "" : "none");
            });
            $(".cogItemCB").change();

            // 输入首扫点比
            $("input[name='firstScanPercent']").on("input", function(){
                var firstScanPercent = Number($(this).val());
                var commonScanPercent = 100 - firstScanPercent;
                $("input[name='commonScanPercent']").val(commonScanPercent).attr("maxVal", commonScanPercent);

                $("[id='totalPrizePercent']").each(function(i, obj){
                    $(this).css("color", $(this).text() == "100.0000%" ? "green" : "red");
                });

                // 重新计算均价
                calculateUnit();
            });

            // 动态增加奖项配置项
            $("form").on("click", "#addPrizeItem", function(){
                var $scanPercent = $(this).closest("table.scan_type_table").find("input[name$='ScanPercent']");
                if (Number($scanPercent.val()) == 0) {
                    $.fn.alert($scanPercent.attr("name") == "firstScanPercent" ? "请先配置首扫占比" : "请先配置普扫占比");

                } else {
                    if ($(this).closest("tr").find("#NO").text() == "1") {
                        var $cloneItem = $(this).closest("tr").clone(true, true);
                        $cloneItem.find("input[name$='Money']").data("oldval", "0.00").val("0.00");
                        $cloneItem.find("input[name$='Vpoints']").data("oldval", "0").val("0");
                        $cloneItem.find("input[name$='Percent']").data("oldval", "0.00").val("0.00");
                        $cloneItem.find("label[id^='unit'], label[id$='TotalMoney'], label[id$='TotalVpoints']").text("0.00");
                        $cloneItem.find("input[name='scanNum'],input[name='bigPrizeType'],input[name='cardNo'],input[name='prizePercentWarn'],input[name='cogAmounts']").val("");
                        $cloneItem.find("#addPrizeItem").text("删除");
                        $(this).closest("tbody").find("tr:last-child").before($cloneItem);
                        $cloneItem.find("#randomType,[name='bigPrizeType']").trigger("change");
                    } else {
                        $triggerObj = $(this).closest("tbody").find("input[name='prizePercent']:first");
                        $(this).closest("tr").remove();
                        $triggerObj.trigger("change");

                        // 重新计算均价
                        calculateUnit();
                    }

                    $(this).closest("tbody").find("tr").each(function(i, obj){
                        $(this).find("#NO").text(i+1);
                    });
                }
            });

            // 奖项模式切换
            $("form").on("change", "#randomType", function(){
                // 随机模式
                if ($(this).val() == "0") {
                    $(this).closest("tr").find("div.random-prize").css("display", "inline-flex");
                    $(this).closest("tr").find("div.fixation-prize").css("display", "none");
                    $(this).closest("tr").find("div.random-prize input[name^='max']").trigger("change");

                    // 固定模式
                } else {
                    $(this).closest("tr").find("div.random-prize").css("display", "none");
                    $(this).closest("tr").find("div.fixation-prize").css("display", "inline-flex");
                    $(this).closest("tr").find("div.fixation-prize input").trigger("change");
                }

                // 模式切换时重新计算均价
                calculateUnit();
            });

            // 大奖类型切换
            $("form").on("change", "[name='bigPrizeType'], [name='scanNum']", function(){
                var $closestTr = $(this).closest("tr");
                if ($(this).attr("name") == "bigPrizeType") {
                    $closestTr.find("[name='prizePayMoney']").val("0.00");
                    $closestTr.find("[name='prizeDiscount']").val("0.00");
                }
                $closestTr.find("#cogAmountsLabel").text("0");
                if ($closestTr.find("[name='bigPrizeType']").val() == ""
                    && $closestTr.find("[name='scanNum']").val() == "") {
                    $closestTr.find("#cogAmountsLabel").css("display", "");
                    $closestTr.find("[name='cogAmounts']").css("display", "none");
                } else {
                    $closestTr.find("#cogAmountsLabel").css("display", "none");
                    $closestTr.find("[name='cogAmounts']").css("display", "inline");
                }

                if ($closestTr.find("[name='scanNum']").val() == "") {
                    $closestTr.find("#prizePercentWarnLabel").css("display", "none");
                    $closestTr.find("#prizePercentWarnDiv").css("display", "inline-flex");
                } else {
                    $closestTr.find("#prizePercentWarnLabel").css("display", "");
                    $closestTr.find("#prizePercentWarnDiv").css("display", "none");
                }

                // 模式切换时重新计算均价
                $closestTr.find("input[name='prizePercent']").trigger("change");
            });

            // 津贴卡类型切换
            $("form").on("change", "[name='allowanceType']", function(){
                if ($(this).val() == "") {
                    $(this).closest("tr").find("[name='allowanceMoney']").val("0.00");
                }
            });

            // 计算均价-随机
            $("form").on("change", "div.random-prize input", function(){
                var suffix = $(this).attr("name").substr(3);
                var $minInput = $(this).closest("td").find("input[name='min" + suffix + "']");
                var $maxInput = $(this).closest("td").find("input[name='max" + suffix + "']");
                var minVal = Number($minInput.val());
                var maxVal = Number($maxInput.val());
                if (minVal > maxVal) {
                    maxVal = minVal;
                }
                if (suffix == 'Money') {
                    $minInput.val(minVal.toFixed(2));
                    $maxInput.val(maxVal.toFixed(2));
                } else {
                    $minInput.val(minVal);
                    $maxInput.val(maxVal);
                }
                $(this).closest("tr").find("#unit" + suffix).text(((minVal + maxVal) / 2).toFixed(2));

                // 模式切换时重新计算均价
                calculateUnit();
            });
            // 计算均价-固定
            $("form").on("change", "div.fixation-prize input", function(){
                var suffix = $(this).attr("name").substr(8);
                if (suffix == 'Money') {
                    $(this).val(Number($(this).val()).toFixed(2));
                }else {
                    $(this).val(Number($(this).val()));
                }
                $(this).closest("tr").find("#unit" + suffix).text((Number($(this).val())).toFixed(2));

                // 模式切换时重新计算均价
                calculateUnit();
            });

            // 修改奖项配置项占比
            $("form").on("change", "input[name='prizePercent']", function(){
                if (initPrizeItemFlag) return;
                var scanNumPercent = 0; // 阶梯奖项总百分比
                var bigPrizePercent = 0; // 大奖奖项总百分比
                var commonPercent = 0; // 普通奖项总百分比
                $(this).closest("tbody").find("input[name='prizePercent']").each(function(i,obj){
                    commonPercent += Number($(this).val());
                });
                var currVal = Number($(this).val());
                var currTotalPercent =  commonPercent;
                if (currTotalPercent > 100) {
                    currVal = 100 - (currTotalPercent - currVal);
                }
                $(this).val(currVal.toFixed(4));

                var totalPercent = commonPercent;

                if (totalPercent > 100) totalPercent = 100;
                $(this).closest("tbody").find("#totalPrizePercent").text(totalPercent.toFixed(4) + "%").css("color", totalPercent.toFixed(4) == 100 ? "green" : "red");

                // 重新计算均价
                calculateUnit();
            });

            // 修改投放个数
            $("form").on("change", "input[name='cogAmounts']", function(){

                // 计算奖项总数
                var totalPrizeCount = 0;
                $(this).closest("tbody").find(".cogAmounts").each(function(i,obj){
                    totalPrizeCount += Number($(this).val());
                });

                // 转盘总奖数
                var ruleTotalPrizeCount = $("input[name='ruleTotalPrize']").val();
                var currVal = Number($(this).val());
                if (totalPrizeCount > ruleTotalPrizeCount){
                    alert("奖项配置项数量不能超过转盘中奖数!")
                    $(this).val(0);
                    $(this).closest("tbody").find("#totalPrizeCount").text(totalPrizeCount - currVal);
                    return false;
                } else {
                    // 计算谢谢参与奖项数量
                    var participatePrizeCount = 0;
                    participatePrizeCount = ruleTotalPrizeCount - totalPrizeCount;
                    $(this).closest("tbody").find("#participatePrizeCount").val(participatePrizeCount);
                    $(this).closest("tbody").find("#totalPrizeCount").text(ruleTotalPrizeCount);
                }
                // 重新计算均价
                calculateUnit();
            });

            $("form").on("input", "input[name='prizePercent']", function(){
                if (Number($(this).val()) >= 100) {
                    $(this).trigger("change");
                }
            });

            $("form").on("change", "input[name='prizePercentWarn']", function(){
                if ($(this).val() != '') {
                    var currVal = Number($(this).val());
                    $(this).val(currVal.toFixed(4));
                }
            });

            // 奖项总数
            $("input[name='ruleTotalPrize']").on("input", function(){
                // 重新计算均价
                calculateUnit();
            });

            // 选择配置方案
            $("#rebateRuleTemplet").on("change", function(){

                // 校验翻倍红包区间
                $currOption = $(this).find("option:selected");
                var allowanceaRebateType = $currOption.data("allowancearebatetype");
                var allowanceaRebateTypeForRule = $("input[name='allowanceaRebateType']").val();
                if("0" == allowanceaRebateType){
                    if(allowanceaRebateType == allowanceaRebateTypeForRule){
                        var allowanceaMinMoney = $currOption.data("allowanceaminmoney");
                        var allowanceaMaxMoney = $currOption.data("allowanceamaxmoney");
                        if ($("input[name='allowanceaMinMoney']").val() != ""
                            && $("input[name='allowanceaMaxMoney']").val() != "") {
                            if ($("input[name='allowanceaMinMoney']").val() != allowanceaMinMoney
                                || $("input[name='allowanceaMaxMoney']").val() != allowanceaMaxMoney) {
                                $.fn.alert("所选模板的翻倍金额区间为：" + allowanceaMinMoney + "-" + allowanceaMaxMoney + ", 与规则翻倍配置不一致");
                            }
                        }else {
                            $("input[name='allowanceaMinMoney']").val(allowanceaMinMoney);
                            $("input[name='allowanceaMaxMoney']").val(allowanceaMaxMoney);
                        }
                    }else{
                        $.fn.alert("翻倍规则不一致：所选模板为翻倍金额区间，规则设置为翻倍积分区间");
                    }

                } else if("1" == allowanceaRebateType){
                    if(allowanceaRebateType == allowanceaRebateTypeForRule){
                        var allowanceaMinVpoints = $currOption.data("allowanceaminvpoints");
                        var allowanceaMaxVpoints = $currOption.data("allowanceamaxvpoints");
                        if ($("input[name='allowanceaMinVpoints']").val() != ""
                            && $("input[name='allowanceaMaxVpoints']").val() != "") {
                            if ($("input[name='allowanceaMinVpoints']").val() != allowanceaMinVpoints
                                || $("input[name='allowanceaMaxVpoints']").val() != allowanceaMaxVpoints) {
                                $.fn.alert("所选模板的翻倍积分区间为：" + allowanceaMinVpoints + "-" + allowanceaMaxVpoints + ", 与规则翻倍配置不一致");
                            }
                        }else {
                            $("input[name='allowanceaMinVpoints']").val(allowanceaMinVpoints);
                            $("input[name='allowanceaMaxMoney']").val(allowanceaMaxMoney);
                        }
                    }else{
                        $.fn.alert("翻倍规则不一致：所选模板为翻倍积分区间，规则设置为翻倍金额区间");
                    }

                }


                // 重置奖项配置项
                resetPrizeItem();

                // 获取当前规则方案
                var key = $(this).val();
                if (key != "" && key != "请选择") {
                    var url = "<%=cpath%>/vcodeActivityRebateRule/findRebateRuleTemplet.do";
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: {templetKey : key},
                        dataType: "json",
                        async: false,
                        beforeSend:appendVjfSessionId,
                        success:  function(data) {
                            var ruleTemplet = data['ruleTemplet'];

                            // 开始初始化文案
                            initPrizeItemFlag = true;

                            // 首扫占比
                            $("input[name='firstScanPercent']").val(ruleTemplet["firstScanPercent"]);
                            $("input[name='firstScanPercent']").trigger("input");

                            // 首扫
                            $.each(ruleTemplet['firstDetailLst'], function(i, item){
                                initPrizeItem($("#firstScanPrize"), item, i);
                            });

                            // 普扫
                            $.each(ruleTemplet['commonDetailLst'], function(i, item){
                                initPrizeItem($("#commonScanPrize"), item, i);
                            });

                            // 初化显示列表
                            $(".cogItemCB").change();

                            // 初始化结束
                            initPrizeItemFlag = false;

                            // 计算总的百分比
                            $("#commonScanPrize").find("tbody tr").eq(0).find("input[name='prizePercent']").trigger("change");
                            $("#firstScanPrize").find("tbody tr").eq(0).find("input[name='prizePercent']").trigger("change");

                            // 初始化完成后打开计算开关
                            calculateUnitFlag = true;
                            calculateUnit();
                        }
                    });
                }
            });
        }

        // 根据地区获取热区
        function findHotAreaListByAreaCode(areaCode) {
            if (areaCode == "" || areaCode == null) return;
            var oldVal = $("#hotAreaKey").val();
            $.ajax({
                type: "POST",
                url: "<%=cpath%>/vcodeActivityHotArea/findHotAreaListByAreaCode.do",
                async: false,
                data: {"areaCode": areaCode},
                dataType: "json",
                beforeSend: appendVjfSessionId,
                success: function (result) {
                    var content = "<option value=''>请选择</option>";
                    if (result) {
                        $.each(result, function (i, item) {
                            if (i >= 0) {
                                if (oldVal == item.hotAreaKey) {
                                    content += "<option value='" + item.hotAreaKey + "' selected>" + item.hotAreaName + "</option>";
                                } else {
                                    content += "<option value='" + item.hotAreaKey + "'>" + item.hotAreaName + "</option>";
                                }
                            }
                        });
                        $("#hotAreaKey").html(content);
                    } else {
                        $("#hotAreaKey").html(content);
                    }
                },
                error: function (data) {
                    $.fn.alert("热区回显错误!");
                }
            });
        }

        function changeWeek(obj) {
            var beginVal = $(obj).parent(".week").find("input[name='beginDate']").val();
            var endVal = $(obj).val();
            if (endVal != "") {
                if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1 || endVal > 7) {
                    $.fn.alert("请输入1~7的数字");
                    $(obj).val("");
                    $(obj).parent(".week").find("input[name='beginDate']").val('');
                } else if (beginVal != "" && beginVal > endVal) {
                    $.fn.alert("起始值不可大于终止值!");
                    $(obj).val("");
                    $(obj).parent(".week").find("input[name='beginDate']").val('');
                }
            }
        }

        function validFile() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }

            // 规则区域
            var areaCode = "";
            var areaName = "";
            $("td.area div.area").each(function (i) {
                var $province = $(this).find("select.zProvince");
                var $city = $(this).find("select.zCity");
                var $county = $(this).find("select.zDistrict");
                if ($county.val() != "") {
                    areaCode = areaCode + $county.val() + ",";
                } else if ($city.val() != "") {
                    areaCode = areaCode + $city.val() + ",";
                } else if ($province.val() != "") {
                    areaCode = areaCode + $province.val() + ",";
                }
                areaName = areaName + $province.find("option:selected").text() + "_"
                    + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
            });
            if (areaCode != "") {
                $("input[name='filterAreaCode']").val(areaCode.substring(0, areaCode.length - 1));
                $("input[name='filterAreaName']").val(areaName.substring(0, areaName.length - 1));
            }

            // 规则类型
            var returnFlag = false;
            var dateAry = "";
            var divType = "";
            var ruleType = $("select[name='ruleType']").val();
            if (ruleType == "1" || ruleType == "2") {
                divType = "date";
            } else if (ruleType == "3") {
                divType = "week";
            }

            // 组装多组日期
            if (divType != "") {
                $("td.date div." + divType).each(function (i) {
                    var $beginDate = $(this).find("input[name='beginDate']");
                    var $endDate = $(this).find("input[name='endDate']");
                    if ($beginDate.val() == "" || $endDate.val() == "") {
                        if (divType == "date") {
                            $.fn.alert("日期范围不能为空!");
                        } else {
                            $.fn.alert("周几不能为空!");
                        }
                        returnFlag = true;
                        return false; // 相当于break
                    }
                    dateAry += $beginDate.val() + "@" + $endDate.val() + ",";
                });

                if (returnFlag) {
                    return false;
                }

                if (dateAry != "") {
                    $("input[name='filterDateAry']").val(dateAry.substring(0, dateAry.length - 1));
                }
            }

            // 组装多组时间
            var timeAry = "";
            $("td.time div.time").each(function (i) {
                var $beginTime = $(this).find("input[name='beginTime']");
                var $endTime = $(this).find("input[name='endTime']");
                if ($beginTime.val() == "" || $endTime.val() == "") {
                    $.fn.alert("时间范围不能为空!");
                    returnFlag = true;
                    return false; // 相当于break
                }
                timeAry += $beginTime.val() + "@" + $endTime.val() + ",";
            });

            if (returnFlag) {
                return false;
            }

            if (timeAry != "") {
                $("input[name='filterTimeAry']").val(timeAry.substring(0, timeAry.length - 1));
            }

            if ($("[name='prizeCogType']:checked").val() == '0') {
                var files = $("input.import-file").val();
                if (files == "") {
                    $.fn.alert("未选择任何文件，不能导入!");
                    return false;
                } else if (files.indexOf("xls") == -1) {
                    $.fn.alert("不是有效的EXCEL文件!");
                    return false;
                }
            } else {
                if (Number($("input[name='ruleTotalPrize']").val()) <= 0) {
                    $.fn.alert("请输入有效的奖项总数!");
                    return false;
                }

            }

            // 爆点规则:只用于节假日及时间段规则
            var ruleType = $("[name='ruleType']").val();
            if (ruleType == "1" || ruleType == "2") {
                var eruptRule = "";
                var eruptFlag = false;
                $("div.erupt-content table.erupt_table").each(function () {
                    var valNum = 0;
                    $(this).find(":input:enabled").each(function () {
                        if ($(this).val() != '') valNum++;
                    });
                    if (valNum > 0 && valNum != $(this).find(":input:enabled").size()) {
                        $.fn.alert("请完善爆点规则");
                        eruptFlag = true;
                        return false;
                    }
                    if (valNum > 0) {
                        eruptRule = eruptRule + $(this).find("input.eruptStartDate").val() + "#" + $(this).find("input.eruptEndDate").val() + ","
                        eruptRule = eruptRule + $(this).find("input.eruptStartTime").val() + "#" + $(this).find("input.eruptEndTime").val() + ","
                        $(this).find("div.eruptperitem").each(function () {
                            eruptRule = eruptRule + $(this).find("#eruptPerNum").val() + ":" + $(this).find("#eruptPerMoney").val() + ":" + $(this).find("#eruptPerVpoints").val() + ":" + $(this).find("#eruptPerPrize").val() + ","
                        });
                        eruptRule += ";";
                    }
                });
                $("input[name='eruptRuleInfo']").val(eruptRule);
                if (eruptFlag) {
                    return false;
                }
            } else {
                $("input[name='eruptRuleInfo']").val();
            }

            if ($("input[name='restrictVpoints']").val() == "") {
                $("input[name='restrictVpoints']").val("0");
            }
            if ($("input[name='restrictMoney']").val() == "") {
                $("input[name='restrictMoney']").val("0.00");
            }
            if ($("input[name='restrictBottle']").val() == "") {
                $("input[name='restrictBottle']").val("0");
            }
            if ($("input[name='restrictCount']").val() == "") {
                $("input[name='restrictCount']").val("0");
            }
            if ($("input[name='moneyDanping']").val() == "") {
                $("input[name='moneyDanping']").val("0.00");
            }

            if ($("input[name='restrictFirstDayBottle']").val() == "") {
                $("input[name='restrictFirstDayBottle']").val("0");
            }
            if ($("input[name='restrictFirstDayMoney']").val() == "") {
                $("input[name='restrictFirstDayMoney']").val("0.00");
            }
            if ($("input[name='restrictFirstMonthBottle']").val() == "") {
                $("input[name='restrictFirstMonthBottle']").val("0");
            }
            if ($("input[name='restrictFirstMonthMoney']").val() == "") {
                $("input[name='restrictFirstMonthMoney']").val("0.00");
            }
            if ($("input[name='restrictFirstTotalBottle']").val() == "") {
                $("input[name='restrictFirstTotalBottle']").val("0");
            }
            if ($("input[name='restrictFirstTotalMoney']").val() == "") {
                $("input[name='restrictFirstTotalMoney']").val("0.00");
            }

            return true;
        }

        // 计算单瓶成本及积分
        function calculateUnit() {
            if (!calculateUnitFlag) return;

            // 假设总瓶数
            var totalPrize = $("input[name='ruleTotalPrize']").val() ? $("input[name='ruleTotalPrize']").val() : 0;
            var totalMoney = 0.00;
            var totalVpoints = 0.00;
            var scanTypeQrcodeNum = 0;

            var itemTotalMoney = 0.00;
            var itemTotalVpoints = 0.00;
            var itemQrcodeNum = 0;
            var itemMoney = 0.00;
            var itemVpoints = 0.00;
            var itemUnitMoney = 0.00;
            var itemUnitVpoints = 0.00;
            var itemPercent = 0;
            $("table[id$='ScanPrize']").each(function () {
                itemTotalMoney = 0.00;
                itemTotalVpoints = 0;

                if ($(this).attr("id") == "firstScanPrize") {
                    scanTypeQrcodeNum = (totalPrize * Number($("input[name='firstScanPercent']").val()) / 100).toFixed(0);
                } else {
                    scanTypeQrcodeNum = (totalPrize * (100 - Number($("input[name='firstScanPercent']").val())) / 100).toFixed(0);
                }

                var trLength = $(this).find("tbody tr").size();
                $(this).find("tbody tr").each(function (i, obj) {
                    if (i < trLength - 1) {
                        itemUnitMoney = Number($(this).find("#unitMoney").text());
                        itemUnitVpoints = Number($(this).find("#unitVpoints").text());
                        itemPercent = Number($(this).find("input[name='prizePercent']").val());

                        if ($(this).find("[name='cogAmounts']").css("display") == "none") {
                            itemQrcodeNum = (scanTypeQrcodeNum * itemPercent / 100).toFixed(0);
                        } else {
                            itemQrcodeNum = Number($(this).find("[name='cogAmounts']").val());
                        }
                        itemMoney = itemQrcodeNum * itemUnitMoney;
                        itemVpoints = itemQrcodeNum * itemUnitVpoints;
                        $(this).find("#itemTotalMoney").text(itemMoney.toFixed(2));
                        $(this).find("#itemTotalVpoints").text(itemVpoints.toFixed(2));
                        $(this).find("#cogAmountsLabel").text(itemQrcodeNum);
                        $(this).find("[name='cogAmounts']").val(itemQrcodeNum);
                        itemTotalMoney += itemMoney;
                        itemTotalVpoints += itemVpoints;
                    }
                });
                $(this).closest("tbody").find("[id$='ScanQrcodeNum']").val((scanTypeQrcodeNum + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,'));
                $(this).closest("tbody").find("[id$='ScanTotalMoney']").val(((itemTotalMoney).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,'));
                $(this).closest("tbody").find("[id$='ScanTotalVpoints']").val(((itemTotalVpoints).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,'));

                totalMoney += itemTotalMoney;
                totalVpoints += itemTotalVpoints;
            });

            // 单价
            $("input[name='ruleTotalMoney']").val("≈" + ((totalMoney).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,'));
            $("input[name='ruleUnitMoney']").val("≈" + (totalMoney / totalPrize).toFixed(2));
            $("input[name='ruleTotalVpoints']").val("≈" + ((totalVpoints).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,'));
            $("input[name='ruleUnitVpoints']").val("≈" + (totalVpoints / totalPrize).toFixed(2));
        }


    </script>

    <style>
        .blocker {
            float: left;
            vertical-align: middle;
            margin-right: 8px;
            margin-top: 8px;
        }

        .en-larger {
            margin-left: 8px;
        }

        .cool-wood {
            padding-left: 1em;
        }

        fieldset {
            border: 1px solid #d1d1d1;
            max-height: 160px;
            overflow: auto;
        }

        fieldset legend {
            font-weight: bold;
        }

        fieldset .remove-target {
            float: right;
            color: red;
            cursor: pointer;
        }

        fieldset .remove-target > span {
            margin-left: 2px;
        }

        fieldset .remove-target > span:hover {
            text-decoration: underline;
        }

        fieldset div {
            column-count: 2;
            max-height: 150px;
            height: auto;
            margin-top: 25px;
        }

        fieldset div > label {
            width: 48%;
        }

        fieldset div > label > span {
            margin-left: 8px;
            font-weight: normal;
        }

        .white {
            color: white;
        }
    </style>
</head>

<body>
<div class="container" style="padding: 0px;">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form"
                  enctype="multipart/form-data"
                  action="<%=cpath %>/turntable/doTurntableActivityRuleCogAdd.do"
                  onsubmit="return validFile();">
                <input type="hidden" name="rebateRuleKey" value="${rebateRuleCog.rebateRuleKey}" />
                <input type="hidden" name="vcodeActivityKey" value="${rebateRuleCog.vcodeActivityKey}" />
                <input type="hidden" name="areaCode" value="${rebateRuleCog.areaCode}" />
                <input type="hidden" name="firstScanPercent"/>
                <input type="hidden" name="ruleUnitMoney"/>
                <div class="widget box">
                    <!--             	基础信息 -->
                    <div class="widget-header"><h4><i class="iconfont icon-daoru"></i>基础信息</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="rebateRuleName" tag="validate" value="${rebateRuleCog.rebateRuleName}"
                                               class="form-control required" style="width: 352px;" autocomplete="off"
                                               maxlength="100"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="filterAreaCode" value="${areaCode}"/>
                                <input type="hidden" name="filterAreaName"/>
                                <td class="ab_left"><label class="title">筛选区域：<span class="white"></span></label>
                                </td>
                                <td class="ab_main area" colspan="3">
                                    <c:if test="${not empty(areaName)}">
                                        <div class="content areaName">
                                            <span>${areaName}</span>
                                        </div>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">规则类型：<span class="white">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content" id="ruleType" data-ruletype="${rebateRuleCog.ruleType}">
                                        <c:choose>
                                            <c:when test="${rebateRuleCog.ruleType == '1'}">节假日</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '2'}">时间段</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '3'}">周几</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '4'}">每天</c:when>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">活动时间：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <c:choose>
                                        <c:when test="${rebateRuleCog.ruleType == '4' or rebateRuleCog.ruleType == '5'}">
                                            <div class="content">
                                                <span class="blocker">从</span>
                                                <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium" disabled="disabled"/>
                                                <span class="blocker en-larger">至</span>
                                                <input name="endDate" id="endDate" class="Wdate form-control input-width-medium" disabled="disabled"/>
                                            </div>
                                        </c:when>
                                        <c:when test="${rebateRuleCog.ruleType == '3'}">
                                            <div class="content">
                                                <span class="blocker">从</span>
                                                <input name="beginDate" id="beginDate" class="form-control input-width-medium"
                                                       <c:if test="${isEditFlag eq '1' or isEditFlag eq '2'}">readonly="readonly" disabled</c:if>
                                                       data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.beginDate }"/>
                                                <span class="blocker en-larger">至</span>
                                                <input name="endDate" id="endDate" class="form-control input-width-medium"
                                                       <c:if test="${isEditFlag eq '2'}">readonly="readonly" disabled</c:if>
                                                       data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.endDate }"/>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="content">
                                                <span class="blocker">从</span>
                                                <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium"
                                                       <c:if test="${isEditFlag eq '1' or isEditFlag eq '2'}">readonly="readonly" disabled</c:if>
                                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.beginDate }"/>
                                                <span class="blocker en-larger">至</span>
                                                <input name="endDate" id="endDate" class="Wdate form-control input-width-medium"
                                                       <c:if test="${isEditFlag eq '2'}">readonly="readonly" disabled</c:if>
                                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')||\'%y-%M-%d\'}'})" data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.endDate }"/>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">规则备注：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="remarks" rows="5"
                                                  class="form-control required" autocomplete="off" value="${rebateRuleCog.remarks}"
                                                  maxlength="200"></input>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">转盘中奖数：<span class="red">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="ruleTotalPrize" tag="validate"
                                               class="form-control input-width-normal number positive marl5 required"
                                               autocomplete="off" maxlength="10"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">总金额：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" style="width: 25% !important;">
                                    <div class="content">
                                        <input name="ruleTotalMoney" readonly="readonly" value="≈0.00"
                                               class="form-control input-width-larger" tabindex="-1"
                                               style="color: red; font-weight: bold; width: 160px !important;"/>
                                        <span class="blocker en-larger">元</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">总积分：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" style="width: 30% !important;">
                                    <div class="content">
                                        <input name="ruleTotalVpoints" readonly="readonly" value="≈0.00"
                                               class="form-control input-width-larger" tabindex="-1"
                                               style="color: red; font-weight: bold; width: 160px !important;"/>
                                        <span class="blocker en-larger">积分</span>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>配置奖项</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table scan_type_table">
                            <tr>
                                <td class="ab_left"><label class="title">中出配置项：<span class="required"> </span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemMoney" class="cogItemCB"
                                               style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;积分红包
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemVpoints" class="cogItemCB"
                                               style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;商城积分
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemPrize" class="cogItemCB"
                                               style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;实物
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_main" colspan="4">
                                    <table id="commonScanPrize"
                                           class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                           style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                        <tr>
                                            <th style="width:5%; text-align: center;">序号</th>
                                            <th style="width:8%; text-align: center;">中出类型</th>
                                            <th style="width:13%; text-align: center;" class="cogItemMoney">积分红包范围(元)
                                            </th>
                                            <th style="width:9%; text-align: center;" class="cogItemMoney">积分红包均价(元)
                                            </th>
                                            <th style="width:9%; text-align: center;" class="cogItemMoney">积分红包成本(元)
                                            </th>
                                            <th style="width:13%; text-align: center;" class="cogItemVpoints">商城积分范围
                                            </th>
                                            <th style="width:9%; text-align: center;" class="cogItemVpoints">积分均价</th>
                                            <th style="width:9%; text-align: center;" class="cogItemVpoints">积分成本</th>
                                            <th style="width:12%; text-align: center;" class="cogItemPrize">实物奖</th>
                                            <th style="width:9%; text-align: center;" class="cogItemPrize">支付金额(元)</th>
                                            <th style="width:10%; text-align: center;">中出占比</th>
                                            <th style="width:9%; text-align: center;">投放个数</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>
                                                <label id="NO" style="line-height: 30px;">1</label>
                                            </td>
                                            <td>
                                                <input type="hidden" name="scanType" value="1">
                                                <select id="randomType" name="randomType"
                                                        class="form-control input-width-small"
                                                        style="display: initial; width: 60px !important;">
                                                    <option value="1">固定</option>
                                                    <option value="0">随机</option>
                                                </select>
                                            </td>
                                            <td class="cogItemMoney">
                                                <div class="random-prize content" style="display: none;">
                                                    <input type="text" name="minMoney"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off"
                                                           tag="validate" data-oldval="0.00" value="0.00"
                                                           maxVal="999.99" maxlength="6"
                                                           style="display: initial; width: 60px !important;">
                                                    <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                                    <input type="text" name="maxMoney"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off"
                                                           tag="validate" data-oldval="0.00" value="0.00"
                                                           maxVal="999.99" maxlength="6"
                                                           style="display: initial; width: 60px !important;">
                                                </div>
                                                <div class="fixation-prize" style="display: inline-flex;">
                                                    <input type="text" name="fixationMoney"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off"
                                                           tag="validate" data-oldval="0.00" value="0.00"
                                                           maxVal="999.99" maxlength="6"
                                                           style="display: initial; width: 132px !important;">
                                                </div>
                                            </td>
                                            <td class="cogItemMoney">
                                                <label id="unitMoney" style="line-height: 30px;">0.00</label>
                                            </td>
                                            <td class="cogItemMoney">
                                                <label id="itemTotalMoney" style="line-height: 30px; min-width: 40px;">0.00</label>
                                            </td>
                                            <td class="cogItemVpoints">
                                                <div class="random-prize content" style="display: none;">
                                                    <input type="text" name="minVpoints"
                                                           class="form-control input-width-small number integer maxValue"
                                                           autocomplete="off" tag="validate" data-oldval="0" value="0"
                                                           maxVal="999999" maxlength="6"
                                                           style="display: initial; width: 60px !important;">
                                                    <label style="line-height: 30px;">&nbsp;-&nbsp;</label>
                                                    <input type="text" name="maxVpoints"
                                                           class="form-control input-width-small number integer maxValue"
                                                           autocomplete="off" tag="validate" data-oldval="0" value="0"
                                                           maxVal="999999" maxlength="6"
                                                           style="display: initial; width: 60px !important;">
                                                </div>
                                                <div class="fixation-prize" style="display: inline-flex;">
                                                    <input type="text" name="fixationVpoints"
                                                           class="form-control input-width-small number integer maxValue"
                                                           autocomplete="off"
                                                           tag="validate" data-oldval="0" value="0" maxVal="999999"
                                                           maxlength="6"
                                                           style="display: initial; width: 132px !important;">
                                                </div>
                                            </td>
                                            <td class="cogItemVpoints">
                                                <label id="unitVpoints" style="line-height: 30px;">0.00</label>
                                            </td>
                                            <td class="cogItemVpoints">
                                                <label id="itemTotalVpoints"
                                                       style="line-height: 30px; min-width: 40px;">0.00</label>
                                            </td>
                                            <td class="cogItemPrize">
                                                <div class="content">
                                                    <select name="bigPrizeType" class="form-control input-width-normal"
                                                            style="display: initial; width: 100% !important;">
                                                        <option value="">请选择</option>
                                                        <c:forEach items="${prizeTypeMap}" var="item">
                                                            <option value="${item.key}">${item.value}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </td>
                                            <td class="cogItemPrize">
                                                <input type="text" name="prizePayMoney"
                                                       class="form-control input-width-small moneyFmt number maxValue"
                                                       autocomplete="off"
                                                       tag="validate" data-oldval="0.00" value="0.00" maxVal="999.99"
                                                       maxlength="6" style="display: initial;">
                                            </td>
                                            <td style=" position: relative;">
                                                <div style="display: inline-flex;">
                                                    <input type="text" id="prizePercent" name="prizePercent"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off" tag="validate" data-oldval="0.0000"
                                                           value="0.0000" maxVal="100" maxlength="7"
                                                           style="display: initial; width: 60px !important;">
                                                    <label style="line-height: 30px;">%</label>
                                                </div>
                                            </td>
                                            <td style="position: relative;">
                                                <input type="text" id="cogAmounts" name="cogAmounts"
                                                       class="form-control cogAmounts input-width-small number maxValue positive"
                                                       autocomplete="off" tag="validate"
                                                       maxlength="7"
                                                       style="display: initial; width: 60px !important;">
                                                <label id="addPrizeItem" class="btn-txt-add-red"
                                                       style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label style="line-height: 30px;">2</label>
                                                <label style="line-height: 30px;">233332123123</label>
                                            </td>
                                            <td>
                                                <input type="hidden" name="scanType" value="1">
                                                <input type="hidden" name="randomType" value="1">
                                                <span>谢谢参与</span>
                                            </td>
                                            <td class="cogItemMoney">
                                                <div class="random-prize content" style="display: none;">
                                                    <input type="text" name="minMoney"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off" readonly
                                                           tag="validate" data-oldval="0.00" value="0.00"
                                                           maxVal="999.99" maxlength="6"
                                                           style="display: initial; width: 60px !important;">
                                                    <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                                    <input type="text" name="maxMoney"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off" readonly
                                                           tag="validate" data-oldval="0.00" value="0.00"
                                                           maxVal="999.99" maxlength="6"
                                                           style="display: initial; width: 60px !important;">
                                                </div>
                                                <div class="fixation-prize" style="display: inline-flex;">
                                                    <input type="text" name="fixationMoney"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off" readonly
                                                           tag="validate" data-oldval="0.00" value="0.00"
                                                           maxVal="999.99" maxlength="6"
                                                           style="display: initial; width: 132px !important;">
                                                </div>
                                            </td>
                                            <td class="cogItemMoney">
                                                <label style="line-height: 30px;">0.00</label>
                                            </td>
                                            <td class="cogItemMoney">
                                                <label style="line-height: 30px; min-width: 40px;">0.00</label>
                                            </td>
                                            <td class="cogItemVpoints">
                                                <div class="random-prize content" style="display: none;">
                                                    <input type="text" name="minVpoints"
                                                           class="form-control input-width-small number integer maxValue"
                                                           autocomplete="off" tag="validate" data-oldval="0" value="0"
                                                           maxVal="999999" maxlength="6" readonly
                                                           style="display: initial; width: 60px !important;">
                                                    <label style="line-height: 30px;">&nbsp;-&nbsp;</label>
                                                    <input type="text" name="maxVpoints"
                                                           class="form-control input-width-small number integer maxValue"
                                                           autocomplete="off" tag="validate" data-oldval="0" value="0"
                                                           maxVal="999999" maxlength="6" readonly
                                                           style="display: initial; width: 60px !important;">
                                                </div>
                                                <div class="fixation-prize" style="display: inline-flex;">
                                                    <input type="text" name="fixationVpoints"
                                                           class="form-control input-width-small number integer maxValue"
                                                           autocomplete="off" readonly
                                                           tag="validate" data-oldval="0" value="0" maxVal="999999"
                                                           maxlength="6"
                                                           style="display: initial; width: 132px !important;">
                                                </div>
                                            </td>
                                            <td class="cogItemVpoints">
                                                <label style="line-height: 30px;">0.00</label>
                                            </td>
                                            <td class="cogItemVpoints">
                                                <label
                                                        style="line-height: 30px; min-width: 40px;">0.00</label>
                                            </td>
                                            <td class="cogItemPrize">
                                                <div class="content">
                                                    <select name="bigPrizeType" class="form-control input-width-normal" readonly="true"
                                                            style="display: initial; width: 100% !important;">
                                                        <option value="">请选择</option>
                                                        <c:forEach items="${prizeTypeMap}" var="item">
                                                            <option value="${item.key}">${item.value}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </td>
                                            <td class="cogItemPrize">
                                                <input type="text" name="prizePayMoney"
                                                       class="form-control input-width-small moneyFmt number maxValue"
                                                       autocomplete="off" readonly
                                                       tag="validate" data-oldval="0.00" value="0.00" maxVal="999.99"
                                                       maxlength="6" style="display: initial;">
                                            </td>
                                            <td style=" position: relative;">
                                                <div style="display: inline-flex;">
                                                    <input type="text"  name="prizePercent"
                                                           class="form-control input-width-small number maxValue"
                                                           autocomplete="off" tag="validate" data-oldval="0.0000"
                                                           value="0.0000" maxVal="100" maxlength="7"
                                                           style="display: initial; width: 60px !important;">
                                                    <label style="line-height: 30px;">%</label>
                                                </div>
                                            </td>
                                            <td style="position: relative;">
                                                <input type="text" name="cogAmounts" id="participatePrizeCount" readonly
                                                       class="form-control input-width-small number maxValue positive"
                                                       autocomplete="off" tag="validate"
                                                       maxlength="7"
                                                       style="display: initial; width: 60px !important;">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>合计</td>
                                            <td>--</td>
                                            <td class="cogItemMoney">--</td>
                                            <td class="cogItemMoney">--</td>
                                            <td class="cogItemMoney">--</td>
                                            <td class="cogItemVpoints">--</td>
                                            <td class="cogItemVpoints">--</td>
                                            <td class="cogItemVpoints">--</td>
                                            <td class="cogItemPrize">--</td>
                                            <td class="cogItemPrize">--</td>
                                            <td><span id="totalPrizePercent" data-currval="0"
                                                      style="font-weight: bold;">0%</span></td>
                                            <td><span id="totalPrizeCount" data-currval="0"
                                                      style="font-weight: bold;">0</span></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" type="submit">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"
                                    url="<%=cpath%>/turntable/showTurntableActivityRuleCogList.do?activityKey=${rebateRuleCog.vcodeActivityKey}">返 回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
