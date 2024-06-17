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
    <title>新建阶梯中奖UI</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>

    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>

    <script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];
        var clickImg = function (id) {
            $("#addBatchDialog").modal("show");
            parent.$("#my_iframe").contents().find("#queryTY").click();
            //移除选中项
            $('input:checkbox').removeAttr('checked');
            $("#btnName").val(id);
        }

        // 本界面上传图片要求
        var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };

        $(function(){
            // 初始化校验控件
            $.runtimeValidate($("form"));

            // 初始化功能
            initPage();
        });

        function initPage() {
            $("#ladderPic").attr("src",src="https://img.vjifen.com/images/vma/"+'${ladderUI.ladderPic}');
            $("input[name= ladderPic]").val('${ladderUI.ladderPic}');
            $("[name='ladderType']").on("change", function () {
                if ($(this).val() == '0') {
                    $("#ladderStNum").css("display", "none");
                    $("#ladderEndNum").css("display", "none");
                    $("#a").css("display", "none");
                    $("#ladderMultiple").css("display", "block");
                    $("#ladderStNum").val("");
                    $("#ladderEndNum").val("");
                    $("#title1").css("display", "none");
                    $("#title").css("display", "block");
                }
                if ($(this).val() == '1') {
                    $("#ladderMultiple").css("display", "none");
                    $("#ladderEndNum").css("display", "block");
                    $("#ladderStNum").css("display", "block");
                    $("#a").css("display", "block");
                    $("#ladderMultiple").val("");
                    $("#title").css("display", "none");
                    $("#title1").css("display", "block");
                }
            });
            $("#ladderType").change();

            // 返回
            $(".btnReturn").click(function(){
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });

            // 保存
            $(".btnSave").click(function(){
                var flag = validForm();
                if (flag) {
                    var url = $(this).attr("url");
                    $.fn.confirm("确认发布？", function(){

                        $(".btnSave").attr("disabled", "disabled");

                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
                }
                return false;
            });
            $("#ruleKey").attr("value",'${ruleKey}');
            $("#addBatchDialog").delegate("#comBtn", "click", function () {
                $(window.frames["my_iframe"].document).find("input[type=radio]:checked").each(function () {
                    var btnName = $("#btnName").val();
                    $("#" + btnName).attr("src", "https://img.vjifen.com/images/vma/" + $(this).val());
                    $('input[name="' + btnName + '"]').val($(this).val());
                });
                $("#addBatchDialog #closeBtn").trigger("click");
            });

            // 删除阶梯图片
            $("div.item").mouseover(function(){
            	if($(this).find("#ladderPic").attr("src").indexOf("a12.png") == -1) {
            	    $("img.clear-img").css("display", "");
            	}
            });
            $("div.item").mouseout(function(){
                $("img.clear-img").css("display", "none");
            });
            $("img.clear-img").click(function(){
                $(this).css("display", "none");
            	$(this).closest("div").find("#ladderPic").attr("src", "/VjifenCOM/inc/vpoints/img/a12.png");
            	$(this).closest("div").find("[name='ladderPic']").val("");
            });

        }
        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
            if($("#ladderType").val() == "1" && ($("#ladderStNum").val() == "" || $("#ladderEndNum").val() == "")){
                $.fn.alert("开始支数结束支数都不能为空!");
                return false;
            }
            if($("[name='ladderPic']").val() =='' && $("[name='ladderDesc']").val() == ''){
                $.fn.alert("阶梯弹窗或阶梯描述不能同时为空!");
                return false;
            }
            if($("#ladderType").val() == "0" && $("[name='ladderMultiple']").val() ==''){
                $.fn.alert("阶梯倍数不能为空!");
                return false;
            }
            return true;
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
        .addImg1 {
            width: 150px;
            height: 150px;
            position: absolute;
            left: 0;
            top: 0;
            z-index: 2;
            cursor: pointer;
        }
        .modal-body1 {
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
            width: 150px;
            height: 150px;
            float: left;
            position: relative;
            margin: 20px;
        }
	    .clear-img{
	        position: absolute;
	        top: 6px;
	        right: 8px;
	        z-index: 10;
	    }
    </style>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/ladderUI/doLadderUIEdit.do">
                <input name="infoKey" type="hidden" value="${ladderUI.infoKey}"/>
                <input name="ruleKey"  id="ruleKey" type="hidden" />
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>新建阶梯中奖UI</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">阶梯类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="2" style="width:45% !important;">
                                    <div class="content">
                                        <select id="ladderType" name="ladderType" class="form-control input-width-larger search" autocomplete="off" >
                                            <option value="0" <c:if test="${ladderUI.ladderType eq '0'}"> selected="selected" </c:if>>按倍数中出</option>
                                            <option value="1" <c:if test="${ladderUI.ladderType eq '1'}"> selected="selected" </c:if>>按支数中出</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title" name="title1" id="title1" style="display: none">阶梯支数：<span class="required">*</span></label>
                                    <label class="title" name="title" id="title"  >阶梯倍数：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="2" style="width:45% !important;">
                                    <div class="content">
                                        <input name="ladderMultiple" id="ladderMultiple" tag="validate" autocomplete="off" maxlength="10" style="width:250px;" value="${ladderUI.ladderMultiple}"/>
                                        <input name="ladderStNum" id="ladderStNum" tag="validate" value="${ladderUI.ladderStNum}"
                                               class="form-control required" autocomplete="off" maxlength="10" style="width:250px; display: none"/>
                                        <span   id ="a" class="blocker en-larger" style="display: none">至</span>
                                        <input name="ladderEndNum" id="ladderEndNum" tag="validate" value="${ladderUI.ladderEndNum}"
                                               class="form-control required" autocomplete="off" maxlength="10" style="width:250px; display: none"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">阶梯描述：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="ladderDesc" tag="validate" style="width: 400px !important;"
                                            class="form-control" autocomplete="off" maxlength="30" value="${ladderUI.ladderDesc}"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <input type="hidden" name="btnName" id="btnName" value="">
                            <tr>
                                <td class="ab_left"><label class="title">阶梯弹窗：<span class="required">*</span></label>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=14"
                                               target= "my_iframe" class="addImg1"  onclick="clickImg('ladderPic');" style="z-index: 3; opacity: 0;"></a>
                                            <img class="clear-img" src="<%=cpath %>/inc/vpoints/img/a7.png" style="display: none"/>
                                            <img class="addImg1" id="ladderPic" src="<%=cpath %>/inc/vpoints/img/a12.png"/>
                                            <input type="hidden" name="ladderPic" value="${ladderUI.ladderPic}">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
                            <div class="modal fade" id="addBatchDialog" tabindex="-1" data-backdrop="static"
                                 aria-labelledby="myModalLabel" aria-hidden="false">
                                <div class="modal-dialog">
                                    <div class="modal-content" style="top:1%;">
                                        <div class="modal-body1">
                                            <iframe class="frame-body" name="my_iframe"
                                                    src=""
                                                    scrolling="y"></iframe>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" id="comBtn" class="btn btn-default do-add btn-red"
                                                    data-dismiss="">确 认
                                            </button>
                                            <button type="button" id="closeBtn" class="btn btn-default"
                                                    data-dismiss="modal">关 闭
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" >确认发布</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/ladderUI/showLadderUIList.do?ruleKey =  '${ruleKey}'">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
