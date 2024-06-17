<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
    String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加素材</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>

    <script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];
        var clickImg = function(obj){
            $("#addBatchDialog").modal("show");
//移除选中项
            $('input:checkbox').removeAttr('checked');
            $("#btnName").val(obj.id);
        }
        $(function(){
// 初始化校验控件
            $.runtimeValidate($("div.tab-content"));

// 初始化功能
            initPage();
        });

        function initPage() {
            $("#telIsDefault").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='telIsDefault']").val("1");
                    }else{
                        $("input:hidden[name='telIsDefault']").val("0");
                    }
                }
            });
            $("#locIsDefault").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='locIsDefault']").val("1");
                    }else{
                        $("input:hidden[name='locIsDefault']").val("0");
                    }
                }
            });
            $("#appIsDefault").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='appIsDefault']").val("1");
                    }else{
                        $("input:hidden[name='appIsDefault']").val("0");
                    }
                }
            });
            $("#telAuthStatus").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='appAuthStatus']").val("1");
                    }else{
                        $("input:hidden[name='appAuthStatus']").val("0");
                    }
                }
            });
            $("#appAuthStatus").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='appAuthStatus']").val("1");
                    }else{
                        $("input:hidden[name='appAuthStatus']").val("0");
                    }
                }
            });
            $("#locAuthStatus").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='locAuthStatus']").val("1");
                    }else{
                        $("input:hidden[name='locAuthStatus']").val("0");
                    }
                }
            });
//初始化模板
            $(".btnSave").on("click", function(){
// 输入元素校验
                $validContent = $(this).closest("div.tab-content");
                var validateResult = $.submitValidate($validContent);
                if(!validateResult){
                    return false;
                }

// 识别次数校验
                if ($validContent.find("input[name='sameMinuteRestrict']").size() > 0) {
                    if (!checkDoubtCog()) {
                        $.fn.alert("四种识别次数中至少一个要大于0");
                        return false;
                    }
                }

                if ($validContent.find("input:radio[name='doubtfulTimeLimitType']:checked").size() > 0) {
                    if(!checkDoubtLimit){
                        $.fn.alert("可疑时间限制必须选择其中一个");
                        return false;
                    }}
// 当前Tab
                var tabIndex = $("ul.breadcrumb #currTab").data("tabindex");

// JSON
                var paramJson = {};
                $(this).closest("div.tab-content").find(":input:enabled[name]").each(function(){
// if($(this).attr("type") == "radio") {
// if ($(this).is(":checked")) {
// paramJson[$(this).attr("name")] = $(this).val();
// }
// } else {
                    paramJson[$(this).attr("name")] = $(this).val();
// }
                });

// 提交表单
                var templateKey = $(":hidden[name='templateKey']").val();
                var url = "<%=cpath%>/templateUi/doTemplateUiAdd.do";
                if (templateKey != "") {
                    url = "<%=cpath%>/templateUi/doTemplateUiEdit.do";
                    paramJson["templateKey"] = templateKey;
                }
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    success: function(data) {
                        if (data['errMsg'] != "添加成功") {
                            $.fn.alert(data['errMsg'], function(){
                                $("button.btn-primary").trigger("click");
                            });
                        } else {
// 活动添加成功后活动主键
                            if (data["templateKey"]) {
                                $(":hidden[name='templateKey']").val(data["templateKey"]);
                                $("#ruleFrame").attr("src", $("#ruleFrame").attr("src") + data["templateKey"]);
                            } else {
                                $("#ruleFrame").attr("src", $("#ruleFrame").attr("src"));
                            }
// 保存过风控规则
                            if (tabIndex == 1) {
                                $("div.tab-group a:eq(1)").data("saved", "true");
                            }
// 切换Tab
                            $("div.tab-group a").eq(tabIndex + 1).trigger("click");
                        }
                    }
                });

            });
// 类型为每天时，日期区域不可用
            $("#addBatchDialog").delegate("#comBtn", "click", function(){
                $(window.frames["my_iframe"].document).find("input[type=checkbox]:checked").each(function(){
                    var a = $(this).siblings("div[name = picName]");
                    var btnName = $("#btnName").val();
                    var picName = $(this).siblings("div[name = picName]").html();
                    var picWidth = $(this).siblings("div[name = picWidth]").html();
                    var picHeight = $(this).siblings("div[name = picHeight]").html();
                    var picX = $(this).siblings("div[name = picX]").html();
                    var picY = $(this).siblings("div[name = picY]").html();
// $("#"+btnName).attr("value",picName + "|" + picWidth + "|" + picHeight + "|" + picX + "|" + picY + "|" + $(this).val());
//给input框赋值
                    $("input[name= "+btnName+"]").val(picName + "|" + picWidth + "|" + picHeight + "|" + picX + "|" + picY + "|" + $(this).val());
// alert( $("input[name= "+btnName+"]").val())
                    alert( $("#"+btnName).attr("value"))
                    $("#"+btnName).attr("src",src="https://img.vjifen.com/images/vma/"+ $(this).val());
                });
                $("#addBatchDialog #closeBtn").trigger("click");
            });
        }

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }

            return true;
        }

        // 定时更新iframe的高度
        var iframeClock = setInterval("setIframeHeight()", 50);
        function setIframeHeight() {
            iframe = document.getElementById('ruleFrame');
            if (iframe) {
                var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
                if (iframeWin.document.body) {
                    iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
                }
            }
        }


    </script>

    <style>
        .white {
            color: white;
        }
        .white {
            color: white;
        }
        .top-only {
            border-top: 1px solid #e1e1e1;
        }
        .modal-body {
            display: block;
            height: 600px;
        }
        .frame-body {
            display: block;
            height: 100%;
            width: 100%;
        }
        .modal-dialog {
            display: block;
            width: 800px;
        }
        .item {
            width: 190px;
            height: 190px;
            float: left;
            position: relative;
            margin: 20px;
        }
        .addImg {
            width: 190px;
            height: 190px;
            position: absolute;
            left: 0;
            top: 0;
            z-index: 2;
            cursor: pointer;
        }
    </style>
</head>

<div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/templateUi/doAuthInfoEdit.do">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>新增授权页面</h4>
                    </div>
                    <div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>手机号授权</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">状态<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="telAuthStatus" type="hidden" value="1" />
                                        <input id="telAuthStatus" type="checkbox" checked data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">是否强制授权<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="telIsDefault" type="hidden" value="1" />
                                        <input id="telIsDefault" type="checkbox" checked data-size="small" data-on-text="是" data-off-text="否" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">授权页面：</label><span class="white">*</span></td>
                                <td class="ab_main"  colspan="3">
                                    <div class="content">
                                        <select class="form-control required" name="telAuthPage">
                                            <option value="1">扫码红包首页</option>
                                            <option value="2">大于一元</option>
                                            <option value="3">小于一元</option>
                                            <option value="4">您已扫过</option>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">拆红包按钮：</label><span class="white">*</span></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <select class="form-control required" name="telAuthCondition">
                                            <option value="1">点击拆红包按钮</option>
                                            <option value="2">拆开红包</option>
                                            <option value="3">点击立即提现</option>
                                            <option value="4">点击存入零钱包</option>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">上传提示图：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <img class="addImg"  id = "telTemplateProperty" onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a11.png" width="20" height="20" />
                                            <input  type="hidden" name="telTemplateProperty">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>
                    <div class="widget-header"><h4><i class="iconfont icon-xinxi"></i>位置授权</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">状态<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="locAuthStatus" type="hidden" value="1" />
                                        <input id="locAuthStatus" type="checkbox" checked data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">是否强制授权<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="locIsDefault" type="hidden" value="1" />
                                        <input id="locIsDefault" type="checkbox" checked data-size="small" data-on-text="是" data-off-text="否" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">授权页面：</label><span class="white">*</span></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <select class="form-control required" name="locAuthPage">
                                            <option value="1">扫码红包首页</option>
                                            <option value="2">大于一元</option>
                                            <option value="3">小于一元</option>
                                            <option value="4">您已扫过</option>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">拆红包按钮：</label><span class="white">*</span></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <select class="form-control required" name="locAuthCondition">
                                            <option value="1">点击拆红包按钮</option>
                                            <option value="2">拆开红包</option>
                                            <option value="3">点击立即提现</option>
                                            <option value="4">点击存入零钱包</option>
                                        </select>
                                    </div>
                                </td>
                            <tr>
                                <td class="ab_left"><label class="title">上传提示图：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <img class="addImg"  id = "locTemplateProperty" onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a11.png" width="20" height="20" />
                                            <input  type="hidden" name="locTemplateProperty">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="widget-header"><h4><i class="iconfont icon-xinxi"></i>公众号授权</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <div class="widget-content panel no-padding">
                                <table class="active_board_table">
                                    <tr>
                                        <td class="ab_left"><label class="title">状态<span class="required">*</span></label></td>
                                        <td class="ab_main" colspan="3">
                                            <div class="content">
                                                <input name="appAuthStatus" type="hidden" value="1" />
                                                <input id="appAuthStatus" type="checkbox" checked data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr style="height: 60px;">
                                        <td class="ab_left"><label class="title">上传提示图：</label></td>
                                        <td class="ab_main" colspan="3">
                                            <div class="content">
                                                <div class="item">
                                                    <img class="addImg"  id = "appTemplateProperty" onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a11.png" width="20" height="20" />
                                                    <input  type="hidden" name="appTemplateProperty">
                                                </div>
                                                <div style="clear: left;"></div>
                                            </div>
                                        </td>
                                    </tr>

                                </table>
                            </div>
                        </table>
                    </div>

                    <div class="modal fade" id="addBatchDialog" tabindex="-1"  data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
                        <div class="modal-dialog">
                            <div class="modal-content" style="top:1%;">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    <h4 class="modal-title" >选择素材</h4>
                                </div>
                                <div class="modal-body">
                                    <iframe class="frame-body" name="my_iframe" src="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}" frameborder="0" scrolling="no"></iframe>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" id="comBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" >确认发布</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/actRule/showActRuleList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</html>
