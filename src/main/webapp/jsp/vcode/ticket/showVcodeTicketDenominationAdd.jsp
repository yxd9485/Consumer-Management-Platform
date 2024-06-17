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
    <title>添加积分活动</title>

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

    <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
    <%-- 	<link href="<%=cpath%>/inc/htmlEdit/dist/bootstrap.css" rel="stylesheet" type="text/css"/> --%>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>

    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];
        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));
            // 初始化功能
            initPage();
            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });
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
            if(imgSrc.length==0){
                alert("请上传优惠券图片");
                return false;
            }
            if (imgSrc.length > 1) {
                $.fn.alert("优惠券图片最多上传1张");
                return false;
            } else {
                $("[name='TicketPicUrl']").val(imgSrc[0]);
            }
            // 校验有效期
            if (!$("[name='endDate']").val() && !$("[name='expiredDay']").val()) {
                $.fn.alert("优惠券领取有效时间不能为空");
                return false;
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

                    /*var flag = validForm();
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

            // 初始化模板状态显示样式
            $("#ticketStatus").bootstrapSwitch({
                onSwitchChange: function (event, state) {
                    if (state == true) {
                        $("input:hidden[name='ticketStatus']").val("1");
                    } else {
                        $("input:hidden[name='ticketStatus']").val("0");
                    }
                }
            });
            // 保存
            $(".btnSave").click(function () {
                var flag = validForm();
                if (flag) {
                    $.fn.confirm("确认发布？", function () {

                        $("select[name=categoryKey]").removeAttr("disabled");

                        var ticketName = $("input[name=ticketName]").val();
                        var startDate = $("input[name=startDate]").val();
                        var endDate = $("input[name=endDate]").val();
                        var expiredDay = $("input[name=expiredDay]").val();
                        var releaseStartDate = $("input[name=releaseStartDate]").val();
                        var releaseEndDate = $("input[name=releaseEndDate]").val();
                        var categoryKey = $("select[name=categoryKey]").val();
                        var ticketType = $("select[name=ticketType]").val();
                        var ticketUrl = $("input[name=ticketUrl]").val();
                        var ticketMoney = $("input[name=ticketMoney]").val();
                        var ticketDesc = $('.summernote').summernote('code').trim();
                        var TicketPicUrl = $("input[name=TicketPicUrl]").val();
                        var ticketStatus = $("input[name=ticketStatus]").val();

                        var formData = new FormData();
                        formData.append("ticketName", ticketName);
                        formData.append("startDate", startDate);
                        formData.append("endDate", endDate);
                        formData.append("expiredDay", expiredDay);
                        formData.append("releaseStartDate", releaseStartDate);
                        formData.append("releaseEndDate", releaseEndDate);
                        formData.append("categoryKey", categoryKey);
                        formData.append("ticketType", ticketType);
                        formData.append("ticketUrl", ticketUrl);
                        formData.append("ticketMoney", ticketMoney);
                        formData.append("ticketDesc", ticketDesc);
                        formData.append("TicketPicUrl", TicketPicUrl);
                        formData.append("ticketStatus", ticketStatus);

                        $uploadFile = $("#ticketFile");

                        var files = $uploadFile.val();

                        if(ticketType === "1"){
                            if(files == "") {
                                $.fn.alert("未选择任何文件，不能导入!");
                                return false;
                            } else if(files.indexOf("csv") == -1) {
                                $.fn.alert("不是有效的csv文件");
                                return false;
                            }
                            if($uploadFile[0].files[0] != ''){
                                formData.append("ticketFile", $uploadFile[0].files[0]);
                            }
                        }else{
                            let file = new File([''],'',{
                                type: 'application/octet-stream',
                            });
                            formData.append("ticketFile", file);
                        }

                        $(".btnSave").attr("disabled", "disabled");
                        $.ajax({
                            type: "POST",
                            url: "<%=cpath %>/vpsVcodeTicketDenomination/doTicketDenominationAdd.do",
                            data: formData,
                            dataType: "json",
                            async: false,
                            contentType : false,
                            processData: false,
                            beforeSend:appendVjfSessionId,
                            success:  function(data) {
                                $.fn.alert(data['errMsg'], function(){
                                    $("button.btnReturn").trigger("click");
                                });
                            }
                        });
                    });
                }
                return false;
            });
            // 兑奖截止类型
            $("[name='prizeExpireType']").on("change", function () {
                if ($(this).val() == '0') {
                    $(this).closest("div").find("input[name='endDate']").removeAttr("disabled");
                    $(this).closest("div").find("input[name='expiredDay']").attr("disabled", "disabled").val("");
                } else {
                    $(this).closest("div").find("input[name='endDate']").attr("disabled", "disabled").val("");
                    $(this).closest("div").find("input[name='expiredDay']").removeAttr("disabled");
                }
            });

            // 兑奖截止类型
            $("#ticketType").on("change", function () {
                if ($(this).val() == '0' || $(this).val() == '3' || $(this).val() == '4') {
                    $("#fileTr").css("display","none");
                    $("#urlTr").show();
                    $("#ticketFile").attr("disabled", "disabled").val("");
                    $("#ticketUrl").removeAttr("disabled");
                } else {
                    $("#urlTr").css("display","none");
                    $("#fileTr").show();
                    $("#ticketFile").removeAttr("disabled");
                    $("#ticketUrl").attr("disabled", "disabled").val("");
                }
            });

            var sHTML = $('.summernote').summernote('code');
            $('#ticketDesc').val(sHTML.trim());

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
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">优惠券配置</a></li>
            <li class="current"><a title="">添加活动</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/vpsVcodeTicketDenomination/doTicketDenominationAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}"/>
                <input type="hidden" name="pageParam" value="${pageParam}"/>
                <input type="hidden" name="ticketDesc" id="ticketDesc" value="">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="TicketPicUrl" id="picUrl" value="">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">优惠券名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="ticketName" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="100"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券发布时间：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="releaseStartDate" class="form-control input-width-medium required Wdate preTime"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="releaseEndDate" class="form-control input-width-medium required Wdate sufTime"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券领取有效时间：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="prizeExpireType" value="0"
                                               style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">指定日期</span>
                                        <input name="endDate"
                                               class="form-control input-width-medium required Wdate sufTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
                                        <input type="radio" class="tab-radio" name="prizeExpireType" value="1"
                                               style="float:left; margin-left: 10px !important;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">有效天数</span>
                                        <input name="expiredDay" tag="validate" disabled="disabled"
                                               class="form-control input-width-small required number positive"
                                               autocomplete="off" maxlength="3"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券类型：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content firstRebate">
                                        <select class="form-control input-width-larger required" tag="validate" <c:if test="${!empty categoryKey}">disabled</c:if>
                                                name="categoryKey">
                                            <c:choose>
                                                <c:when test="${!empty categoryKey}">
                                                    <c:forEach items="${ticketCategoryList}" var="item">
                                                        <option value="${item.categoryKey}"
                                                        <c:if test="${item.categoryKey eq categoryKey}"> selected="selected" </c:if>>
                                                        ${item.categoryName}
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${ticketCategoryList }" var="item">
                                                        <option value="${item.categoryKey }">${item.categoryName }</option>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">中出方式：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content firstRebate">
                                        <select id="ticketType" name="ticketType" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option value="0">跳转静态链接</option>
                                            <option value="1">券码</option>
                                            <%--暂时隐藏图片中出方式--%>
                                            <%--<option value="2">图片</option>--%>
                                            <option value="3">跳转动态链接</option>
                                            <option value="4">三方活动编码</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="urlTr">
                                <td class="ab_left"><label class="title">优惠券链接：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input id="ticketUrl" name="ticketUrl" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="1000" PLACEHOLDER="请输入URL"/>                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="fileTr" style="display: none">
                                <td class="text-right"><label class="title">优惠券信息：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                        <input type="file"  class="import-file" id="ticketFile" name="ticketFile" single style="padding-bottom: 15px; margin-left: 10px;"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券面额：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="ticketMoney" tag="validate"
                                               class="form-control maxValue money required" autocomplete="off"
                                               maxlength="100"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <%--<tr>
                                <td class="ab_left"><label class="title mart5">优惠券备注：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <div class="content">
                                            <textarea tag="validate" rows="5" class="form-control required"
                                                      autocomplete="off" name="ticketDesc" maxlength="500"></textarea>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>--%>
                            <tr>
                                <td class="ab_left"><label class="title">优惠券备注：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="summernote"></div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">启用状态：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="ticketStatus" type="hidden" value="1"/>
                                        <input id="ticketStatus" type="checkbox" checked data-size="small"
                                               data-on-text="启用" data-off-text="停用" data-on-color="success"
                                               data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">优惠券图片上传：<span class="required">*</span><br/></label>
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
                            <button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-event="0"
                                    data-url="<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationList.do">返 回
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
