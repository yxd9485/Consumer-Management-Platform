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

    <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
    <%-- 	<link href="<%=cpath%>/inc/htmlEdit/dist/bootstrap.css" rel="stylesheet" type="text/css"/> --%>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>


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
            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });
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

                $.fn.confirm("保存后活动即生效，确定要保存吗？", function () {
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

                    var sHTML = $('.summernote').summernote('code');
                    $('#activityRule').val(sHTML.trim());

                    var url = $(this).attr("url");
                    $("form").attr("action", url);
                    $("form").attr("onsubmit", "return true;");
                    $("form").submit();
                });
                return false;
            });

            // 增加sku
            $("form").on("click", "#addPrize", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addPrize").text("删除");
                    $copySku.find("option:first").prop('selected', 'selected');
                    $(this).closest("td").append($copySku);
                } else {
                    $(this).closest("div").remove();
                }
            });

            $("[name='skuCog']").on("change", function(){
                if($(this).val() == '000000') {
                    $("[name='skuCog']").find('option').removeAttr("selected");
                    $(".addPrize").hide();
                    var $copySku = $(".addPrize:eq(0)").closest("div").clone(true, true);
                    $(".skuTdCog div").remove();
                    $(".skuTdCog").append($copySku);
                    $("[name='skuCog']").find('option[value="000000"]').attr('selected','selected');
                } else {
                    $(".addPrize").show();
                }
            });
        }
        function uploadImages(files) {
            var formData = new FormData();
            for (f in files) {
                formData.append("file", files[f]);
            }
            // XMLHttpRequest 对象
            var xhr;
            if (XMLHttpRequest) {
                xhr = new XMLHttpRequest();
            } else {
                xhr = new ActiveXObject('Microsoft.XMLHTTP');
            }
            xhr.open("post", basePath+"/skuInfo/imgUploadUrl.do?vjfSessionId=" + $("#vjfSessionId").val(), true);
            xhr.onreadystatechange = function(){
                if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200){
// 		            console.info("上传完成");
                    var result = jQuery.parseJSON(xhr.responseText);
                    if(result["errMsg"]=="error"){
                        alert("上传失败");
                        return;
                    }
                    var srcList=result["ipUrl"].split(",");
                    for(var i = 0;i<srcList.length;i++){
                        $('.summernote').summernote('insertImage',srcList[i], srcList[i]);
                    }

                }else{
// 		        	console.info("上传失败");
                }
            };
            xhr.send(formData);
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
            <li class="current"><a title="">抽奖签到</a></li>
            <li class="current"><a title="">新增活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/lotterySign/doLotterySignAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}"/>
                <input type="hidden" name="pageParam" value="${pageParam}"/>
                <input type="hidden" name="skuLogo"/>
                <input type="hidden" name="activityRule" id="activityRule" value="">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基础配置</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activityName" tag="validate"
                                               class="form-control input-width-larger required varlength"
                                               autocomplete="off" data-length="25" maxlength="25"/>
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
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="widget-content panel no-padding">
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>规则配置</h4>
                        </div>
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">抽奖周期：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="cycleType" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option value="1" selected>月</option>
                                            <%--<option value="0">周</option>--%>
                                        </select>
                                        <span style="color: red" class="blocker en-larger">备注:自然周/自然月</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">扫码抽奖条件：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker en-larger">每扫</span>
                                        <input name="scanCountCog" tag="validate"
                                               class="form-control input-width-larger required positive number minValue maxValue"
                                               style="width: 500px;" autocomplete="off" maxlength="30" minVal="1"
                                               maxVal="1000"/>
                                        <span class="blocker en-larger">次码可抽一次</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
                                <td class="ab_main skuTdCog" colspan="3">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="skuCog"
                                                tag="validate">
                                            <option value="">请选择</option>
                                            <option value="000000">全部</option>
                                            <c:if test="${!empty skuList}">
                                                <c:forEach items="${skuList}" var="item">
                                                    <option value="${item.skuKey}">${item.skuName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="title mart5 addPrize btn-txt-add-red"
                                               style="font-weight: normal; margin-left: 5px;" id="addPrize">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动规则：<span class="required"></span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="summernote">
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-url="<%=cpath%>/lotterySign/showLotterySignList.do">返
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
