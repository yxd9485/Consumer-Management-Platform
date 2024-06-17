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
    <title>新建商城轮播广告</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=6"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.2"></script>




    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.2"></script>

    <script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];
        var clickImg = function(id){
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
            <%--$("input[name= phoneNumberVpoints]").val('${vpsTask.phoneNumberVpoints}');--%>
            <%--$("input[name= userInfoVpoints]").val('${vpsTask.userInfoVpoints}');--%>
            <%--$("input[name= subscribeVpoints]").val('${vpsTask.subscribeVpoints}');--%>

            // 返回
            $(".btnReturn").click(function () {
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });

            // 保存
            $(".btnSave").click(function () {
                var flag = validForm();
                if (flag) {
                    var url = $(this).attr("url");
                    $.fn.confirm("确认发布？", function () {

                        $(".btnSave").attr("disabled", "disabled");

                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
                }
                return false;
            });
        }
        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
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
        .modal-body1 {
            display: block;
            height: 500px;
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
            left: 500px;
            top: 200px;
            z-index: 2;
            cursor: pointer;
        }
        .addImg1 {
            width: 190px;
            height: 190px;
            position: absolute;
            left: 0px;
            top: 0px;
            z-index: 2;
            cursor: pointer;
        }
        /*.preview,.preBlock{*/
        /*    position: absolute;*/
        /*    width: 190px;*/
        /*    height: 190px;*/
        /*    left: 0;*/
        /*    top: 0;*/
        /*}*/
        .delete {
            width: 30px;
            position: absolute;
            right: -30px;
            top: -15px;
            cursor: pointer;
            display: none;
        }
        .preBlock img {
            display: block;
            width: 190px;
            height: 190px;
        }
        /*.content{*/
        /*    width: 200px;*/
        /*}*/
        .upload_input{
            display: block;
            width: 0;
            height: 0;
            -webkit-opacity: 0.0;
            /* Netscape and Older than Firefox 0.9 */
            -moz-opacity: 0.0;
            /* Safari 1.x (pre WebKit!) 老式khtml内核的Safari浏览器*/
            -khtml-opacity: 0.0;
            /* IE9 + etc...modern browsers */
            opacity: .0;
            /* IE 4-9 */
            filter:alpha(opacity=0);
            /*This works in IE 8 & 9 too*/
            -ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";
            /*IE4-IE9*/
            filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=0);
        }
    </style>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/integral/doTaskConfigEdit.do">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>积分任务配置</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                        	<tr>
                              <td class="ab_left"><label class="title">展示时间：<span
                                      class="required">*</span></label></td>
                              <td class="ab_main" colspan="3">
                                  <div class="content">
                                      <span class="blocker">从</span>
                                      <input name="startDate" id="startDate"
                                             class="form-control input-width-medium Wdate required preTime"
                                             tag="validate" style="width: 180px !important;"
                                             autocomplete="off" value="${fn:substring(vpsTask.startDate, 0, 19)}"
                                             onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
                                      <span class="blocker en-larger">至</span>
                                      <input name="endDate" id="endDate"
                                             class="form-control input-width-medium Wdate required sufTime"
                                             tag="validate" style="width: 180px !important;"
                                             autocomplete="off" value="${fn:substring(vpsTask.endDate, 0, 19)}"
                                             onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
                                      <label class="validate_tips"></label>
                                  </div>
                              </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">绑定手机号送积分：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="phoneNumberVpoints" tag="validate"
                                               class="form-control positive number required" autocomplete="off" maxlength="30" value="${vpsTask.phoneNumberVpoints}"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">完善个人信息送积分：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="userInfoVpoints" tag="validate"
                                               class="form-control positive number required" autocomplete="off" maxlength="30" value="${vpsTask.userInfoVpoints}"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">首次关注公众号：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="subscribeVpoints" tag="validate"
                                               class="form-control positive number required" autocomplete="off" maxlength="30"  value="${vpsTask.subscribeVpoints}"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${vipSwitch eq '1'}">
                            <tr>
                                <td class="ab_left"><label class="title">首次参加扫码活动：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="sweepVpoints" tag="validate"
                                               class="form-control positive number required" autocomplete="off" maxlength="30"  value="${vpsTask.sweepVpoints}"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            </c:if>
                            <%--<tr>
                                <td class="ab_left"><label class="title">首次兑换：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="exchangeVpoints" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="30"  value="${vpsTask.exchangeVpoints}"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>--%>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
