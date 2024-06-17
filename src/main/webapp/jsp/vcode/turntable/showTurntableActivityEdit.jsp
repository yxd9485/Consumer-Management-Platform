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
    <title>积分活动管理</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>

    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];
        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));
            // 初始化功能
            initPage();
        });

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }
            // 页面校验
            var v_flag = true;
            $(".validate_tips:not(:hidden)").each(function () {
                if ($(this).text() != "") {
                    $.fn.alert($(this).text());
                    v_flag = false;
                }
            });
            if (!v_flag) {
                return false;
            }
            let drainageTypeVal = $('input[name=drainageType]:checked').val();
            if("2"==drainageTypeVal || "0"==drainageTypeVal || "3"==drainageTypeVal || "4"==drainageTypeVal){
                let scan =  $("input[name=scanSkuNumber]").val();
                let sku =  $("select[name=skuKeys]").val();
                let draw = $("input[name=everyDayDrawLimit]").val();
                if(!scan){
                    $.fn.alert("固定扫码该产品，可得抽奖次数不能为空");
                    return false;
                }
                if("2"==drainageTypeVal || "3"==drainageTypeVal || "4"==drainageTypeVal){
                    if(!sku){
                        $.fn.alert("SKU不能为空");
                        return false;
                    }
                }
                if(!draw && ("2"==drainageTypeVal || "3"==drainageTypeVal)){
                    $.fn.alert("每人每天二重惊喜弹窗上限不能为空");
                    return false;
                }
                if(!draw && "4"==drainageTypeVal){
                    $.fn.alert("每日获取抽奖次数上限");
                    return false;
                }

            }

            if (imgSrc.length == 0) {
                alert("请上传活动规则图片");
                return false;
            }
            if (imgSrc.length > 1) {
                $.fn.alert("活动规则图片最多上传1张");
                return false;
            } else {
                $("[name='rulePic']").val(imgSrc[0]);
            }

            var chk_value = [];
            $('input[name="roleLimit"]:checked').each(function(){
                chk_value.push($(this).val());
            });
            console.log(chk_value)
            if ('${projectServerName}' == 'mengniu' && chk_value.length == 0){
                alert("请选择参与角色!")
                return false
            }

            if ($("input[name='participationNumLimit']:checked").val() == '1'){
                if ($('input[name=everyDayLimit]').val() == null || $('input[name=everyDayLimit]').val() == '' || $('input[name=totalLimit]').val() == null || $('input[name=totalLimit]').val() == ''){
                    $.fn.alert("请至少填写一种抽奖次数限制");
                    return false;
                }
            }

            return true;
        }

        function initPage() {
            // 按钮事件
            $(".button_place").find("button").click(function () {
                var btnEvent = $(this).data("event");
                if (btnEvent == "0") {
                    var url = $(this).data("url");
                    $("form").attr("onsubmit", "");
                    $("form").attr("action", url);
                    $("form").submit();
                } else {
                    var blacklistCogFlag = "0";
                    $("input[mark=blacklist]:not([type=radio])").each(function () {
                        if ($(this).val() != "") {
                            blacklistCogFlag = "1";
                        }
                    });
                    $("input[name=blacklistFlag]").val(blacklistCogFlag);

                    /* var flag = validForm();
                     if (flag) {
                         if (btnEvent == "2") {
                             if (confirm("确认发布？")) {
                                 return true;
                             } else {
                                 return false;
                             }
                         } else {
                             return true;
                         }
                     } else {
                         return false;
                     }*/
                }
            });
            var imgData = '${activityCog.rulePic}';
            if (imgData) {
                showImg(imgData);
            }

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

            if ($("input[name='participationNumLimit']:checked").val() == '0') {
                $("#everyDayLimit").attr("disabled", "disabled");
                $("#totalLimit").attr("disabled", "disabled");
                $('input[name=userLimit]').attr("disabled", "disabled");
            } else {
                $("#everyDayLimit").removeAttr("disabled");
                $("#totalLimit").removeAttr("disabled");
                $("input[name=userLimit]").removeAttr("disabled");
            }

            if ($("input[name='prizeLimit']:checked").val() == '0') {
                $("#bigPrizeLimit").attr("disabled", "disabled");
            } else {
                $("#bigPrizeLimit").removeAttr("disabled");
            }

            // 保存
            $(".btnSave").click(function () {
                var flag = validForm();
                if (flag) {
                    var url = $(this).attr("url");
                    $.fn.confirm("确认发布？", function () {

                        $(".btnSave").attr("disabled", "disabled");
                        var areaCode = "";
                        var areaName = "";
                        // 组建筛选区域
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
                            } else {
                                areaCode = "";
                                areaName = "";
                                return false;
                            }
                            $("input[name='province']").val($province.find("option:selected").text());
                            $("input[name='city']").val($city.find("option:selected").text());
                            $("input[name='county']").val($county.find("option:selected").text());
                            areaName = areaName + $province.find("option:selected").text() + "_"
                                + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
                        });
                        if(areaName.indexOf("全部") != -1){
                            areaCode="000000"
                        }

                        // 组织机构
                        var departmentIds = "";
                        var departmentNames = "";
                        $("td.department div.department").each(function(i){
                            var $dmRegionAry = $(this).find("select.zDmRegionAry");
                            var $dmProvinceAry = $(this).find("select.zDmProvinceAry");
                            var $agencyAry = $(this).find("select.zAgencyAry");

                            if ($dmRegionAry.val() != "") {
                                departmentIds = departmentIds + $dmRegionAry.val() + ($dmProvinceAry.val() != "" ? "," : ";");
                            }
                            if ($dmProvinceAry.val() != "") {
                                departmentIds = departmentIds + $dmProvinceAry.val() + ($agencyAry.val() != "" ? "," : ";");
                            }
                            if ($agencyAry.val() != "") {
                                departmentIds = departmentIds + $agencyAry.val() + ";";
                            }

                            departmentNames = departmentNames + $dmRegionAry.find("option:selected").text() + "_"
                                + $dmProvinceAry.find("option:selected").text() + "_" + $agencyAry.find("option:selected").text() + ";";
                        });
                        if(departmentIds != ""){
                            $("input[name='filterDepartmentIds']").val(departmentIds.substring(0, departmentIds.length - 1));
                            $("input[name='filterDepartmentNames']").val(departmentNames.substring(0, departmentNames.length - 1));
                        }

                        $("input[name='areaCode']").val(areaCode);

                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
                }
                return false;
            });
            // 兑奖截止类型
            $("[name='prizeExpireType']").on("change", function () {
                if ($(this).val() == '0') {
                    $(this).closest("div").find("input[name='endDate']").removeAttr("disabled");
                    $(this).closest("div").find("input[name='expiredDay']").attr("disabled", "disabled");
                } else {
                    $(this).closest("div").find("input[name='endDate']").attr("disabled", "disabled");
                    $(this).closest("div").find("input[name='expiredDay']").removeAttr("disabled");
                }
            });
            <%--$(":radio[name='prizeExpireType'][value='${perMantissaCog.prizeExpireType}']").prop("checked", true).trigger("change");--%>
            // 初始化筛选区域
            $("#addArea").closest("div").initZone("<%=cpath%>", false, "", false, true, false, true);

            // 增加区域
            $("form").on("click", "#addArea", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copyArea = $(this).closest("div").clone();
                    $copyArea.initZone("<%=cpath%>", false, "", false, true, false, true);
                    $copyArea.find("#addArea").text("删除");
                    $(this).closest("td").append($copyArea);

                } else {
                    $(this).closest("div").remove();
                }
            });
            // 增加SKU
            $("form").on("click", "#addShareSku", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addShareSku").text("删除");
                    $copySku.find('option:selected').eq(0).removeAttr("selected")
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });
            // 初始化筛选区域
            var filterAreaCodeAry = "${activityCog.areaCode}".split(",");
            $.each(filterAreaCodeAry, function (idx, val, ary) {
                if (val != '') {
                    if (val == '000000') {

                        $("td.area div:last-child").find("select").val(val);
                    } else {
                        if (idx > 0) $("#addArea").click();
                        $("td.area div:last-child").initZone("<%=cpath%>", false, val, false, true, false, true);
                    }
                }
            });

            $("input[name='activityType']").on('click',function(){return false;});

            $('input[name=participationNumLimit]').on("click", function () {
                if ($(this).val() == '0') {
                    $("#everyDayLimit").attr("disabled", "disabled");
                    $("#totalLimit").attr("disabled", "disabled");
                    $('input[name=userLimit]').attr("disabled", "disabled");
                } else {
                    $("#everyDayLimit").removeAttr("disabled");
                    $("#totalLimit").removeAttr("disabled");
                    $("input[name=userLimit]").removeAttr("disabled");
                }
            });
            //活动渠道切换
            $('input[name=drainageType]').on("click", function () {
                let drainageTypeVal = $('input[name=drainageType]:checked').val();
                $("#drawNumberUseTitle").html("每人每天获取抽奖机会上限 <span class=\"required\">*</span>")
                $('input[name=custcarePhonenum]').addClass("required")
                $('.custcarePhonenum').show();
                if (drainageTypeVal == '0') {
                    $("input[name=everyDayDrawLimit]").attr("readonly",false)
                    $("#joinCondition0Radio").attr("disabled",true)
                    $("#drainageSku").attr("disabled",true)
                    $("#joinCondition1Radio").attr("disabled",false)
                    $(".drawNumberUse").show();
                    $(".drainageSku").hide();
                    $("#vpointUse").hide();
                    $('#joinCondition1Radio').prop("checked",true)
                    $('#joinCondition0Radio').prop("checked",false)
                    $("#remark").hide();
                } else if(drainageTypeVal == '1') {
                    $("input[name=everyDayDrawLimit]").attr("readonly",false)
                    $("#joinCondition0Radio").attr("disabled",false)
                    $("#joinCondition1Radio").attr("disabled",true)
                    $("#drainageSku").attr("disabled",true)
                    $(".drawNumberUse").hide();
                    $(".drainageSku").hide();
                    $("#vpointUse").show();
                    $('#joinCondition0Radio').prop("checked",true)
                    $('#joinCondition1Radio').prop("checked",false)
                    $("#remark").hide();
                }else if(drainageTypeVal == '2' || drainageTypeVal == '3'){
                    $("input[name=everyDayDrawLimit]").attr("readonly",false)
                    $("#joinCondition0Radio").attr("disabled",true)
                    $("#drainageSku").attr("disabled",true)
                    $("#joinCondition1Radio").attr("disabled",false)
                    $(".drawNumberUse").show();
                    $("#drawNumberUseTitle").html("每人每天二重惊喜弹窗上限 <span class=\"required\">*</span>")
                    $(".drainageSku").show();
                    $("#vpointUse").hide();
                    $('#joinCondition1Radio').prop("checked",true)
                    $('#joinCondition0Radio').prop("checked",false)
                    $('input[name=custcarePhonenum]').removeClass("required")
                    $('.custcarePhonenum').hide();
                    if (drainageTypeVal == '3'){
                        $("#remark").show();
                    } else {
                        $("#remark").hide();
                    }
                } else if (drainageTypeVal == '4'){
                    $("#joinCondition0Radio").attr("disabled",true)
                    $("#joinCondition1Radio").attr("disabled",false)
                    $("#drainageSku").attr("disabled",false)
                    $(".drawNumberUse").show();
                    $(".drainageSku").show();
                    $("#vpointUse").hide();
                    $('#joinCondition1Radio').prop("checked",true)
                    $('#joinCondition0Radio').prop("checked",false)
                    $("#drawNumberUseTitle").html("单个用户每日扫码次数上限 <span class=\"required\">*</span>")
                    $("input[name=everyDayDrawLimit]").attr("readonly",true)
                    $('input[name=custcarePhonenum]').removeClass("required")
                    $('.custcarePhonenum').hide();
                    $("#remark").hide();
                }
            });
            $('input[name=drainageType]').triggerHandler("click")
            //参与条件
            $('input[name=joinCondition]').on("click", function () {
                if ($(this).val() == '0') {
                    $(".drawNumberUse").hide();
                    $(".drainageSku").hide();
                    $("#vpointUse").show();
                } else {
                    $("#vpointUse").hide();
                    $(".drawNumberUse").show();
                    $(".drainageSku").hide();
                    let drainageTypeVal = $('input[name=drainageType]:checked').val();
                    if(drainageTypeVal=='2' || drainageTypeVal=='3' || drainageTypeVal=='4'){
                        $(".drainageSku").show();
                    }
                }
            });

            $('input[name=prizeLimit]').on("click", function () {
                if ($(this).val() == '0') {
                    $("#bigPrizeLimit").attr("disabled", "disabled");
                } else {
                    $("#bigPrizeLimit").removeAttr("disabled");
                }
            });

            // 返利范围初始化
            if ('${projectServerName}' == 'mengniu') {
                $("#areaTrId").css("display", "none");
                $("#departmentTrId").css("display", "");
                $("#rebateScopeType2").attr("checked", "checked");
                $("input[name='areaCode']").attr("disabled", true);
            } else {
                $("#areaTrId").css("display", "");
                $("#departmentTrId").css("display", "none");
                $("#rebateScopeTrId").css("display", "none");
                $("input[name='filterDepartmentIds']").attr("disabled", true);
            }

            // 返利范围选择
            $("input[name='rebateScopeType']").on("change", function () {
                if ($(this).attr("id") == "rebateScopeType1") {
                    $("#areaTrId").css("display", "");
                    $("#departmentTrId").css("display", "none");
                    $("input[name='filterDepartmentIds']").attr("disabled", true);
                    $("input[name='areaCode']").attr("disabled", false);
                } else {
                    $("#areaTrId").css("display", "none");
                    $("#departmentTrId").css("display", "");
                    $("input[name='areaCode']").attr("disabled", true);
                    $("input[name='filterDepartmentIds']").attr("disabled", false);
                }
            });

            // 增加组织机构
            $("form").on("click", "#addDepartment", function () {

                if ($(this).text() == "新增") {
                    var $areaCopy = $("div.department").eq(0).clone();
                    $areaCopy.find("#addDepartment").text("删除");
                    $areaCopy.find("select[name='depRegionId'] option:first").attr('selected','selected');
                    $areaCopy.find("select[name='depProvinceId']").html("<option value=''>请选择</option>");
                    $areaCopy.find("select[name='firstDealerKey']").html("<option value=''>请选择</option>");
                    $("td.department").append($areaCopy);
                    $areaCopy.initZone("<%=cpath%>", false, "", true, true, true, false);

                } else {
                    $(this).closest("div.department").remove();
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

            // 初始化组织结构
            var depRegionIdAry = "${activityCog.depRegionId}".split(",");
            var depProvinceIdAry = "${activityCog.depProvinceId}".split(",");
            var firstDealerKeyAry = "${activityCog.firstDealerKey}".split(",");
            $.each(depRegionIdAry, function (idx, val) {
                if (depProvinceIdAry[idx] == '' || depProvinceIdAry[idx] == null){
                    $("select[name='depProvinceId']").eq(idx).html("<option value=''>请选择</option>");
                    $("select[name='firstDealerKey']").eq(idx).html("<option value=''>请选择</option>");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "<%=cpath%>/mnDepartment/queryListByPid.do",
                        async: false,
                        data: {"pid":depRegionIdAry[idx]},
                        dataType: "json",
                        beforeSend:appendVjfSessionId,
                        success:  function(result){
                            var content = "<option value=''>请选择</option>";
                            if(result){
                                $.each(result, function(i, item) {
                                    if (depProvinceIdAry[idx] == item.id){
                                        content += "<option value='"+item.id+"' selected>"+item.dep_name+"</option>";
                                    }
                                    else if (i >= 0 && item.mdgprovinceid != '' && item.mdgprovinceid != null) {
                                        content += "<option value='"+item.id+"'>"+item.dep_name+"</option>";
                                    }
                                });
                                $("select[name='depProvinceId']").eq(idx).html(content);
                            } else {
                                $("select[name='depProvinceId']").eq(idx).html(content);
                            }
                        },
                        error: function(data){
                            $.fn.alert("组织机构回显错误!");
                        }
                    });
                }

                if (firstDealerKeyAry[idx] == '' || firstDealerKeyAry[idx] == null){
                    $("select[name='firstDealerKey']").eq(idx).html("<option value=''>请选择</option>");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "<%=cpath%>/mnAgency/queryListByMap.do",
                        async: false,
                        data: {"departmentId":depProvinceIdAry[idx], "agency_type":"0"},
                        dataType: "json",
                        beforeSend:appendVjfSessionId,
                        success:  function(result){
                            var content = "<option value=''>请选择</option>";
                            if(result){
                                $.each(result, function(i, item) {
                                    if (firstDealerKeyAry[idx] == item.id){
                                        content += "<option value='"+item.id+"' selected>"+item.agency_name+"</option>";
                                    }
                                    else if (i >= 0) {
                                        content += "<option value='"+item.id+"' >"+item.agency_name+"</option>";
                                    }
                                });
                                $("select[name='firstDealerKey']").eq(idx).html(content);
                            } else {
                                $("select[name='firstDealerKey']").eq(idx).html(content);
                            }
                        },
                        error: function(data){
                            $.fn.alert("经销商回显错误!");
                        }
                    });
                }
            });

        }

        // 查询组织机构
        function checkDmRegionAry(dmRegion){
            var $departmentDiv = $(dmRegion).parent(".department");
            var departmentId = $(dmRegion).val();
            if(departmentId == '' || departmentId == null){
                $departmentDiv.find("select[name='depProvinceId']").html("<option value=''>请选择</option>");
                $departmentDiv.find("select[name='firstDealerKey']").html("<option value=''>请选择</option>");
                return;
            }

            $.ajax({
                type: "POST",
                url: "<%=cpath%>/mnDepartment/queryListByPid.do",
                async: false,
                data: {"pid":departmentId},
                dataType: "json",
                beforeSend:appendVjfSessionId,
                success:  function(result){
                    var content = "<option value=''>请选择</option>";
                    if(result){
                        $.each(result, function(i, item) {
                            if (i >= 0 && item.mdgprovinceid != '' && item.mdgprovinceid != null) {
                                content += "<option value='"+item.id+"'>"+item.dep_name+"</option>";
                            }
                        });
                    }
                    $departmentDiv.find("select[name='depProvinceId']").html(content);
                    $departmentDiv.find("select[name='firstDealerKey']").html("<option value=''>请选择</option>");
                },
                error: function(data){
                    $.fn.alert("组织机构回显错误!");
                }
            });
        }

        // 查询经销商
        function checkDmProvinceAry(dmProvince){
            var $departmentDiv = $(dmProvince).parent(".department");
            var id = $(dmProvince).val();
            if(id == '' || id == null){
                $departmentDiv.find("select[name='firstDealerKey']").html("<option value=''>请选择</option>");
                return;
            }

            $.ajax({
                type: "POST",
                url: "<%=cpath%>/mnAgency/queryListByMap.do",
                async: false,
                data: {"departmentId":id, "agency_type":"0"},
                dataType: "json",
                beforeSend:appendVjfSessionId,
                success:  function(result){
                    var content = "<option value=''>请选择</option>";
                    if(result){
                        $.each(result, function(i, item) {
                            if (i >= 0) {
                                content += "<option value='"+item.id+"'>"+item.agency_name+"</option>";
                            }
                        });
                    }
                    $departmentDiv.find("select[name='firstDealerKey']").html(content);
                },
                error: function(data){
                    $.fn.alert("经销商回显错误!");
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
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">积分活动</a></li>
            <li class="current"><a title="">修改积分活动</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/turntable/doTurntableActivityInfoEdit.do">
                <input type="hidden" name="activityKey" value="${activityCog.activityKey}"/>
                <input type="hidden" name="activityType" value="${activityCog.activityType}"/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="areaCode"/>
                        <input type="hidden" name="rulePic" id="picUrl" value="">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">活动渠道：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <c:choose>
                                            <c:when test="${fn:startsWith(currentUser.projectServerName, 'mengniu') || currentUser.projectServerName eq 'zhongliang'}">
                                                <input type="radio" class="radio"  name="drainageType"
                                                       <c:if test="${activityCog.drainageType eq '1'}">checked</c:if>
                                                       value="1" style="float:left; cursor: pointer; min-height: 33px;"/>
                                                <span class="blocker en-larger">商城活动</span>
                                                <label class="validate_tips"></label>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" class="radio"  name="drainageType"
                                                        <c:choose>
                                                            <c:when test="${activityCog.drainageType eq '0'}">checked</c:when>
                                                            <c:otherwise>disabled</c:otherwise>
                                                        </c:choose>
                                                       value="0" style="float:left; cursor: pointer; min-height: 33px;" />
                                                <span class="blocker en-larger">终端引流</span>
                                                <input type="radio" class="radio"  name="drainageType"
                                                        <c:choose>
                                                            <c:when test="${activityCog.drainageType eq '1'}">checked</c:when>
                                                            <c:otherwise>disabled</c:otherwise>
                                                        </c:choose>
                                                       value="1" style="float:left; cursor: pointer; min-height: 33px;"/>
                                                <span class="blocker en-larger">商城活动</span>
                                                <input type="radio" class="radio"  name="drainageType"
                                                        <c:choose>
                                                            <c:when test="${activityCog.drainageType eq '2'}">checked</c:when>
                                                            <c:otherwise>disabled</c:otherwise>
                                                        </c:choose>
                                                       value="2" style="float:left; cursor: pointer; min-height: 33px;"/>
                                                <span class="blocker en-larger">二重惊喜</span>
                                                <input type="radio" class="radio"  name="drainageType"
                                                        <c:choose>
                                                            <c:when test="${activityCog.drainageType eq '3'}">checked</c:when>
                                                            <c:otherwise>disabled</c:otherwise>
                                                        </c:choose>
                                                       value="3" style="float:left; cursor: pointer; min-height: 33px;"/>
                                                <span class="blocker en-larger">异常提示抽奖活动  <b id="remark" style="color: red">注意:该类型同一二维码可一直被扫，可一直中出奖项。</b></span>
                                                <input type="radio" class="radio" name="drainageType"
                                                        <c:choose>
                                                            <c:when test="${activityCog.drainageType eq '4'}">checked</c:when>
                                                            <c:otherwise>disabled</c:otherwise>
                                                        </c:choose>
                                                       value="4" style="float:left; cursor: pointer; min-height: 33px;"/>
                                                <span class="blocker en-larger">福利站</span>
                                                <label class="validate_tips"></label>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activityName" tag="validate" value="${activityCog.activityName}"
                                               class="form-control input-width-larger required varlength"
                                               autocomplete="off" data-length="10" maxlength="10"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">从</span>
                                        <input name="startDate" id="startDate"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" autocomplete="off" value="${activityCog.startDate}"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" autocomplete="off" value="${activityCog.endDate}"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动类型 ：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="radio"  name="activityType" disabled <c:if test="${activityCog.activityType eq '0'}">checked</c:if>
                                               value="0" style="float:left; cursor: pointer; min-height: 33px;"/>
                                        <span class="blocker en-larger">转盘</span>
                                        <input type="radio" class="radio"  name="activityType" disabled <c:if test="${activityCog.activityType eq '1'}">checked</c:if>
                                               value="1" style="float:left; cursor: pointer; min-height: 33px;"/>
                                        <span class="blocker en-larger">盲盒</span>
                                        <input type="radio" class="radio"  name="activityType" disabled <c:if test="${activityCog.activityType eq '2'}">checked</c:if>
                                               value="2" style="float:left; cursor: pointer; min-height: 33px;"/>
                                        <span class="blocker en-larger">福袋</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr <c:if test="${currentUser.projectServerName ne 'mengniu'}">style="display: none" </c:if>>
                                <td class="ab_left"><label class="title">参与角色 ：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <input type="checkbox" name="roleLimit" value="0"
                                    <c:forEach items="${fn:split(activityCog.roleLimit, ',')}" var='role'>
                                           <c:if test="${role eq '0'}">checked</c:if>
                                    </c:forEach>> 配送员
                                    <input type="checkbox" name="roleLimit" value="1"
                                    <c:forEach items="${fn:split(activityCog.roleLimit, ',')}" var='role'>
                                           <c:if test="${role eq '1'}">checked</c:if>
                                    </c:forEach>> 终端老板
                                    <input type="checkbox" name="roleLimit" value="" disabled> 经销商
                                    <input type="checkbox" name="roleLimit" value="3" 
                                    <c:forEach items="${fn:split(activityCog.roleLimit, ',')}" var='role'>
                                           <c:if test="${role eq '3'}">checked</c:if>
                                    </c:forEach>> 分销商
                                    <input type="checkbox" name="roleLimit" value="" disabled> 网服
                                </td>
                            </tr>
                            <tr id="rebateScopeTrId">
                                <td class="ab_left"><label class="title">返利范围 ：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="radio" id="rebateScopeType1" name="rebateScopeType"
                                                <c:if test="${activityCog.areaCode != null && activityCog.areaCode ne ''}">checked</c:if>
                                               value="0" style="float:left; cursor: pointer; min-height: 33px;"/>
                                        <span class="blocker en-larger">区域</span>
                                        <input type="radio" class="radio" id="rebateScopeType2" name="rebateScopeType"
                                               <c:if test="${(activityCog.areaCode == null || activityCog.areaCode eq '') && (activityCog.depRegionId == null || activityCog.depRegionId eq '')}">checked </c:if>
                                               <c:if test="${activityCog.depRegionId != null && activityCog.depRegionId ne ''}">checked</c:if>
                                               value="1" style="float:left; cursor: pointer; min-height: 33px;"/>
                                        <span class="blocker en-larger">组织机构</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="areaTrId" <c:if test="${activityCog.depRegionId != null && activityCog.depRegionId ne ''}">style="display: none" </c:if>
                                <c:if test="${(activityCog.areaCode == null || activityCog.areaCode eq '') && (activityCog.depRegionId == null || activityCog.depRegionId eq '')}">style="display: none" </c:if>>
                                <td class="ab_left"><label class="title">区域限制：<span class=""></span></label>
                                </td>
                                <td class="ab_main area" colspan="3">
                                    <div class="area" style="display: flex; margin: 5px 0px;">
                                        <select name="provinceAry"
                                                class="zProvince form-control input-width-normal"></select>
                                        <select name="cityAry"
                                                class="zCity form-control input-width-normal"></select>
                                        <select name="countyAry"
                                                class="zDistrict form-control input-width-normal"></select>
                                        <label class="title mart5 btn-txt-add-red"
                                               style="font-weight: normal; margin-left: 5px;"
                                               id="addArea">新增</label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="departmentTrId" <c:if test="${activityCog.areaCode != null && activityCog.areaCode ne ''}">style="display: none" </c:if>>
                                <input type="hidden" name="filterDepartmentIds" value="${departmentIds}"/>
                                <input type="hidden" name="filterDepartmentNames"/>
                                <td class="ab_left"><label class="title">组织架构：<span class=""></span></label>
                                </td>
                                <td class="ab_main department" colspan="3">
                                <c:forEach items="${fn:split(activityCog.depRegionId,',')}" var="item" varStatus="idx">
                                    <div class="department" style="display: flex; margin: 5px 0px;">
                                        <select name="depRegionId"
                                                class="zDmRegionAry form-control input-width-normal" onchange="checkDmRegionAry(this)">
                                            <option value="">请选择</option>
                                            <c:forEach items="${departmentList }" var="department">
                                                    <option value="${department.id}" <c:if test="${department.id eq item}">selected</c:if>>
                                            ${department.dep_name}</option>
                                            </c:forEach>
                                        </select>
                                        <select name="depProvinceId"
                                                class="zDmProvinceAry form-control input-width-normal" onchange="checkDmProvinceAry(this)">
                                            <option value="">请选择</option>
                                        </select>
                                        <select name="firstDealerKey" class="zAgencyAry form-control input-width-normal">
                                            <option value="">请选择</option>
                                        </select>
                                        <c:if test="${idx.index eq 0 }">
                                            <label class="title mart5 btn-txt-add-red"
                                                   style="font-weight: normal; margin-left: 5px;"
                                                   id="addDepartment">新增</label>
                                        </c:if>
                                        <c:if test="${idx.index > 0 }">
                                            <label class="title mart5 btn-txt-add-red"
                                                   style="font-weight: normal; margin-left: 5px;"
                                                   id="addDepartment">删除</label>
                                        </c:if>
                                    </div>
                                </c:forEach>
                                </td>
                            </tr>
                            <tr <c:if test="${activityCog.activityType ne '0'}">style="display: none"</c:if> id="turntablePrizeNum">
                                <td class="ab_left"><label class="title">转盘奖品数量 ：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <select name="turntableNum" class="form-control input-width-larger search" disabled
                                            autocomplete="off">
                                        <option value="${activityCog.turntableNum}">${activityCog.turntableNum}个</option>
                                    </select>
                                </td>
                            </tr>
                            <tr <c:if test="${activityCog.activityType eq '0'}">style="display: none"</c:if> id="blindPrizeNum">
                                <td class="ab_left"><label class="title"><c:if test="${activityCog.activityType eq '1'}">盲盒奖品数量 ：</c:if><c:if test="${activityCog.activityType eq '2'}">福袋奖品数量 ：</c:if><span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="turntableNum" tag="validate" value="${activityCog.turntableNum}" disabled
                                               class="form-control input-width-larger number required varlength positive"
                                               autocomplete="off" data-length="11" maxlength="11"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">参与条件：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">

                                        <div class="content">
                                            <input type="radio" class="radio"  name="joinCondition" id="joinCondition0Radio"  <c:if test="${activityCog.joinCondition eq '0'}">checked</c:if>
                                                   value="0" style="float:left; cursor: pointer; min-height: 33px;"/>
                                            <span class="blocker en-larger">消耗积分</span>
                                            <input type="radio" class="radio"  name="joinCondition" id="joinCondition1Radio"  <c:if test="${activityCog.joinCondition eq '1'}">checked</c:if>
                                                   value="1" style="float:left; cursor: pointer; min-height: 33px;"/>
                                            <span class="blocker en-larger">消耗抽奖次数</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr id="vpointUse" <c:if test="${activityCog.joinCondition eq '1'}">hidden</c:if>>
                                <td class="ab_left"><label class="title"></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">

                                        <span class="blocker en-larger">每次抽奖消耗</span>
                                        <input name="consumeVpoints" tag="validate"
                                               class="form-control required thousand input-width-small rule"
                                               value="${activityCog.consumeVpoints}"
                                               autocomplete="off" maxlength="6"/>
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="drawNumberUse" <c:if test="${activityCog.joinCondition eq '0'}">hidden</c:if>>
                                <td class="ab_left"><label class="title"></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker en-larger">固定扫码该产品</span>
                                        <input name="scanSkuNumber" tag="validate"
                                               class="form-control required thousand positive input-width-small rule"
                                               value="${activityCog.scanSkuNumber}"
                                               autocomplete="off" maxlength="10"/>
                                        <span class="blocker en-larger">次，可得一次抽奖次数</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <%--    <tr class="drawNumberUse" <c:if test="${activityCog.drainageType ne '2'}">hidden</c:if>>--%>
                            <tr class="drainageSku" >
                                <td class="ab_left"><label class="title">SKU<span class="required">*</span></label>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty activityCog.skuKeyArray}">
                                            <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                                <select class="form-control input-width-larger required" name="skuKeys" tag="validate">
                                                    <option value="">请选择SKU</option>
                                                    <c:if test="${!empty skuList}">
                                                        <c:forEach items="${skuList}" var="sku">
                                                            <option value="${sku.skuKey}" data-img="${sku.skuLogo}" >${sku.skuName}</option>
                                                        </c:forEach>
                                                    </c:if>
                                                </select>
                                                <span class="blocker en-larger">（挂接此sku可以完成激活积分红包任务）</span>
                                                <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addShareSku">新增</label>
                                                <label class="validate_tips"></label>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${activityCog.skuKeyArray}" var="skuKey" varStatus="idx">
                                                <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                                    <select class="form-control input-width-larger required" name="skuKeys" tag="validate">
                                                        <option value="">请选择SKU</option>
                                                        <c:if test="${!empty skuList}">
                                                            <c:forEach items="${skuList}" var="sku">
                                                                <option value="${sku.skuKey}" <c:if test="${skuKey == sku.skuKey}"> selected</c:if> data-img="${sku.skuLogo}" >${sku.skuName}</option>
                                                            </c:forEach>
                                                        </c:if>
                                                    </select>
                                                    <span class="blocker en-larger">（挂接此sku可以完成激活积分红包任务）</span>
                                                    <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addShareSku">${idx.count == 1 ? '新增' : '删除'}</label>
                                                    <label class="validate_tips"></label>
                                                </div>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr class="drawNumberUse" <c:if test="${activityCog.joinCondition eq '0'}">hidden</c:if>>
                                <td class="ab_left"><label class="title" id="drawNumberUseTitle">每人每天获取抽奖机会上限<span class="required">*</span></label>
                                </td>
                                <td>
                                    <div class="content">
                                        <input name="everyDayDrawLimit" tag="validate"
                                               class="form-control required thousand input-width-larger number positive  rule"
                                               autocomplete="off" maxlength="9"             value="${activityCog.everyDayDrawLimit}"/>

                                        <span class="blocker en-larger">次</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title">抽奖次数：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="participationNumLimit" value="0"
                                               style="float: left; height:20px; cursor: pointer;" <c:if test="${empty activityCog.everyDayLimit &&
                                               empty activityCog.totalLimit}">checked="checked"</c:if>>&nbsp;无限制
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;">
                                        <div style="display: flex;" class="content">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="participationNumLimit" value="1"
                                                       <c:if test="${!empty activityCog.everyDayLimit &&
                                               !empty activityCog.totalLimit}">checked="checked"</c:if>
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>&nbsp;按人限制</span>
                                            </div>
                                            <%--<input type="checkbox" name="userLimit" value="1" disabled
                                                   style="float: left; height:20px; cursor: pointer;">--%>
                                            <span class="blocker en-larger" style="margin-top: 0px;">每人每天抽</span>
                                            <input name="everyDayLimit" id="everyDayLimit" tag="validate"
                                                   value="${activityCog.everyDayLimit}"
                                                   class="form-control thousand input-width-small rule"
                                                   autocomplete="off" maxlength="9"/>
                                            <span class="blocker en-larger" style="margin-top: 0px;">次&nbsp;&nbsp;&nbsp;&nbsp;0表示不限制</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"></td>
                                <td class="ab_main" colspan="3">
                                    <div style="line-height: 35px;">
                                        <div style="display: flex;" class="content">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                            </div>
                                            <%--<input type="checkbox" name="userLimit" value="1" disabled
                                                   style="float: left; height:20px; cursor: pointer;">--%>
                                            <span class="blocker en-larger" style="margin-top: 0px;">每人一共抽</span>
                                            <input name="totalLimit" id="totalLimit" tag="validate"
                                                   value="${activityCog.totalLimit}"
                                                   class="form-control thousand input-width-small rule"
                                                   autocomplete="off" maxlength="9"/>
                                            <span class="blocker en-larger" style="margin-top: 0px;">次&nbsp;&nbsp;&nbsp;&nbsp;0表示不限制</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">实物奖中奖次数：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="prizeLimit" value="0" class="positive" tag="validate"
                                               <c:if test="${empty activityCog.bigPrizeLimit || activityCog.bigPrizeLimit == 0}">checked="checked"</c:if>
                                               style="float: left; height:20px; cursor: pointer;">&nbsp;无限制
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 150px; line-height: 25px;">
                                                <input type="radio" name="prizeLimit" value="1"
                                                       <c:if test="${!empty activityCog.bigPrizeLimit && activityCog.bigPrizeLimit != 0}">checked="checked"</c:if>
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>活动期间每人最多抽中</span>
                                            </div>
                                            <input name="bigPrizeLimit" id="bigPrizeLimit" tag="validate"
                                                   class="form-control required thousand input-width-small rule" disabled
                                                   value="${activityCog.bigPrizeLimit}"
                                                   autocomplete="off" maxlength="9"/>
                                            <span class="blocker en-larger" style="margin-top: 0px;">次&nbsp;&nbsp;&nbsp;&nbsp;0表示不限制</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">客服电话：<span class="required custcarePhonenum">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="custcarePhonenum" tag="validate"
                                               class="form-control input-width-larger required "
                                               value="${activityCog.custcarePhonenum}"
                                               autocomplete="off" data-length="36" maxlength="36"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">启用：<span class="required"> </span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="status" type="hidden" value="${activityCog.status}"/>
                                        <input id="status" type="checkbox" <c:if test="${activityCog.status eq '0'}">checked</c:if> data-size="small" data-on-text="启用"
                                               data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">活动规则上传：<span
                                        class="required">*</span><br/></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;"
                                                 id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file"
                                                           value=""
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
                            <button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-event="0"
                                    data-url="<%=cpath%>/turntable/showTurntableActivityList.do">返 回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
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
                    <h6></h6>
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
