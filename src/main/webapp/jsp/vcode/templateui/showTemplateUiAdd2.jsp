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
    <title>添加积分活动</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/demo.css" type="text/css">
    <link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.excheck.js"></script>

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
            //初始化模板

            $("#logoPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
            $("input[name= logoPic]").val("俄罗斯|111|111|111|111|wwww");
            $("#backgroundPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
            $("input[name= backgroundPic]").val("俄罗斯|111|111|111|111|wwww")
            $("#sloganPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
            $("input[name= sloganPic]").val("俄罗斯|111|111|111|111|wwww")
            $("#openRedpacketPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
            $("input[name= openRedpacketPic]").val("俄罗斯|111|111|111|111|wwww")

            $("[name='coverPic']").on("change", function(){
                if($(this).val() == '0') {
                    $("#logoPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
                    $("input[name= logoPic]").val("俄罗斯|111|111|111|111|wwww");
                    $("#backgroundPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
                    $("input[name= backgroundPic]").val("俄罗斯|111|111|111|111|wwww")
                    $("#sloganPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
                    $("input[name= sloganPic]").val("俄罗斯|111|111|111|111|wwww")
                    $("#openRedpacketPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/0331/ziyuanwei/540*280.png");
                    $("input[name= openRedpacketPic]").val("俄罗斯|111|111|111|111|wwww")
                } else {
                    $("#logoPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/xingzhijihua.png");
                    $("input[name= logoPic]").val("并顿顿|111|111|111|111|wwww");
                    $("#backgroundPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/xingzhijihua.png");
                    $("input[name= backgroundPic]").val("并顿顿|111|111|111|111|wwww")
                    $("#sloganPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/xingzhijihua.png");
                    $("input[name= sloganPic]").val("并顿顿|111|111|111|111|wwww")
                    $("#openRedpacketPic").attr("src",src="https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/xingzhijihua.png");
                    $("input[name= openRedpacketPic]").val("并顿顿|111|111|111|111|wwww")
                }
            });


            // Tab切换
            $("div.tab-group a").on("click", function(){
                // 导航
                var tabIndex = $("div.tab-group a").index($(this));
                if (tabIndex != 0 && $(":hidden[name='templateKey']").val() == '') {
                    $.fn.alert("请先选择模板");
                } else if (tabIndex == 2 && $("div.tab-group a:eq(1)").data("saved") != "true") {
                    $.fn.alert("请先保存页面配置！");
                } else {
                    $("ul.breadcrumb #currTab").text($(this).text());
                    $("ul.breadcrumb #currTab").data("tabindex", tabIndex);
                    $("div.activityinfo").css("display", tabIndex == 2 ? "none" : "");

                    // 按钮状态
                    $("div.tab-group a").removeClass("btn-red");
                    $(this).addClass("btn-red");

                    // 显示内容
                    $("div.tab-content").css("display", "none");
                    $("div.tab-content").eq(tabIndex).css("display", "");
                }

            });

            // 保存活动信息及风控规则
            $(".btnSave").on("click", function(){
                // 输入元素校验
                $validContent = $(this).closest("div.tab-content");
                var validateResult = $.submitValidate($validContent);
                if(!validateResult){
                    return false;
                }


                // 当前Tab
                var tabIndex = $("ul.breadcrumb #currTab").data("tabindex");

                // JSON
                var paramJson = {};
                $(this).closest("div.tab-content").find(":input:enabled[name]").each(function(){
                    paramJson[$(this).attr("name")] = $(this).val();
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
            $("[name='ruleType']").on("change", function(evt) {

                // 节假日、时间段
                if ($(this).val() == '1' || $(this).val() == '2') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input, #addDate").removeAttr("disabled");
                    $("div.week input, #addWeek").attr("disabled", "disabled");
                    $("[name='beginTime'], [name='endTime'], #addTime").removeAttr("disabled");
                }

                // 每天
                if ($(this).val() == '3') {
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
            $("#addBatchDialog").delegate("#comBtn", "click", function(){
                $(window.frames["my_iframe"].document).find("input[type=checkbox]:checked").each(function(){
                    var a =  $(this).siblings("div[name = picName]");
                    var btnName = $("#btnName").val();
                    var picName =  $(this).siblings("div[name = picName]").html();
                    var picWidth = $(this).siblings("div[name = picWidth]").html();
                    var picHeight = $(this).siblings("div[name = picHeight]").html();
                    var picX = $(this).siblings("div[name = picX]").html();
                    var picY = $(this).siblings("div[name = picY]").html();
                    // $("#"+btnName).attr("value",picName + "|" + picWidth + "|" + picHeight + "|" + picX + "|" + picY + "|" + $(this).val());
                    //给input框赋值
                    $("input[name= "+btnName+"]").val(picName + "|" + picWidth + "|" + picHeight + "|" + picX + "|" + picY + "|" + $(this).val());
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
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 活动页面配置</a></li>
            <li class="current"><a> 新增活动模板</a></li>
            <li class="current"><a id="currTab" data-tabindex="0"> 活动模板</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tab-group">
            <a class="btn tab btn-red">活动模板</a>
            <a class="btn tab">页面配置</a>
            <a class="btn tab">授权管理</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form">
                <input type="hidden" name="templateKey"/>
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                <div class="widget box activityinfo">
                    <!-- 活动模板 -->
                    <div class="tab-content">
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>活动模板</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                   id="dataTable_data">
                                <thead>
                                <tr>
                                    <div class="content" class="required">
                                        <th style="width:50%; top: 0%"><input type="radio" name="coverPic"  value="0" checked="checked"/>俄罗斯之旅通用版</th>
                                        <th style="width:50%;"><input type="radio" name="coverPic" value="1" />东奥黄金冰墩墩通用版</th>
                                    </div>
                                </tr>
                                </thead>
                                <tbody>
                                <tr style="cursor:pointer">
                                    <td   style="text-align:center;">
                                        <span><img src="<%=cpath %>/inc/vpoints/img/u3239.png" style="height: 380px;width: 250px; text-align:center;"></span>
                                    </td>
                                    <td   style="text-align:center;">
                                        <span><img src="<%=cpath %>/inc/vpoints/img/u3248.png" style="height: 380px;width: 250px; text-align:center;"></span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="active_table_submit mart20">
                            <div class="button_place">
                                <a class="btn btn-blue btnSave">保存并下一步</a>
                            </div>
                        </div>
                    </div>
                    <!--          页面配置 -->
                    <div class="tab-content" style="display: none;">
                        <div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>页面配置</h4></div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">模板名称：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3" style="width:45% !important;">
                                        <div class="content">
                                            <input name="templateName" tag="validate"
                                                   class="form-control required" autocomplete="off" maxlength="10" style="width:250px;"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>

                                <tr>
                                    <td class="ab_left"><label class="title mart5">规则类型：<span class="white">*</span></label></td>
                                    <td class="ab_main">
                                        <div class="content">
                                            <select name="dateType" style="height: 25px;">
                                                <option value="1">节假日</option>
                                                <option value="2" selected="selected">时间段</option>
                                                <option value="3">每天</option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <input type="hidden" name="filterDateAry" />
                                    <td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                                    <td class="ab_main date">
                                        <div class="content date" style="margin: 5px 0px;">
                                            <span class="blocker">从</span>
                                            <input name="startDate" id="beginDate0" class="Wdate form-control input-width-medium"
                                                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate0\')}'})" autocomplete="off"/>
                                            <span class="blocker en-larger">至</span>
                                            <input name="endDate" id="endDate0" class="Wdate form-control input-width-medium"
                                                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate0\')}'})" autocomplete="off"/>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <input type="hidden" name="filterTimeAry" />
                                    <td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                                    <td class="ab_main time">
                                        <div class="content time" style="margin: 5px 0px;">
                                            <span class="blocker">从</span>
                                            <input name="beginTime" id="beginTime0" class="form-control input-width-medium Wdate"
                                                   onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'endTime0\')}'})" value="00:00:00" autocomplete="off"/>
                                            <span class="blocker en-larger">至</span>
                                            <input name="endTime" id="endTime0" class="form-control input-width-medium Wdate"
                                                   onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime0\')}'})" value="23:59:59" autocomplete="off"/>
                                        </div>
                                    </td>
                                </tr>

                            </table>
                        </div>

                        <!-- 页面配置 - 选择素材 -->
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>选择素材 </h4>
                        </div>

                        <div class="widget-content panel no-padding">
<%--                            <tr>--%>
<%--                                <td class="ab_left" style="display:table-cell; vertical-align:middle;"></td>--%>
<%--                                <td class="ab_main" colspan="3">--%>
<%--                                    <div class="content_wrap">--%>
<%--                                        <div class="zTreeDemoBackground left" >--%>
<%--                                            <ul id="tree" class="ztree" style="background-color: white;">--%>
<%--                                                <li>--%>
<%--                                <td style="width:50%;"><input type="radio" name="coverPic"  value="u3239" checked="checked"/>扫码红包首页</td>--%>
<%--                                </li>--%>
<%--                                <li>--%>
<%--                                    <td style="text-align:center;">--%>
<%--                                        <span><img src="<%=cpath %>/inc/vpoints/img/u3239.png" style="height: 280px;width: 200px; text-align:center;"></span>--%>
<%--                                    </td>--%>
<%--                                </li>--%>

<%--                                <li>--%>
<%--                                    <td style="width:50%;"><input type="radio" name="coverPic" value="u3248" />大于一元</td>--%>
<%--                                </li>--%>
<%--                                <li>--%>
<%--                                    <td style="text-align:center;">--%>
<%--                                        <span><img src="<%=cpath %>/inc/vpoints/img/u3248.png" style="height: 280px;width: 200px; text-align:center;"></span>--%>
<%--                                    </td>--%>
<%--                                </li>--%>
<%--                                </ul>--%>
<%--                                </div>--%>
<%--                                </div>--%>
<%--                                 </td>--%>
<%--                            </tr>--%>

                    <input type="hidden" name="picUrl" id="picUrl" value="">
                    <input type="hidden" name="btnName" id="btnName" value="">
                    <table class="active_board_table">
                        <tr>
                            <td class="ab_left"><label class="title">选择背景图：<span class="required">*</span></label></td>
                            <td class="ab_main" colspan="3">
                                <div class="content">
                                    <div class="item">
                                        <img class="addImg"  id = "backgroundPic"  onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a11.png" width="20" height="20" />
                                        <input type="hidden" name="backgroundPic">
                                    </div>
                                    <div style="clear: left;"></div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">slogan：<span class="required">*</span></label></td>
                            <td class="ab_main" colspan="3">
                                <div class="content">
                                    <div class="item">
                                        <img class="addImg"  id = "sloganPic" onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a11.png" width="20" height="20" />
                                        <input  type="hidden" name="sloganPic">
                                    </div>
                                    <div style="clear: left;"></div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">拆红包按钮：<span class="required">*</span></label></td>
                            <td class="ab_main" colspan="3">
                                <div class="content">
                                    <div class="item">
                                        <img class="addImg"  id = "openRedpacketPic" onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a11.png" width="50" height="50" />
                                        <input  type="hidden" name="openRedpacketPic">
                                    </div>
                                    <div style="clear: left;"></div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">品牌logo：<span class="required">*</span></label></td>
                            <td class="ab_main" colspan="3">
                                <div class="content">
                                    <div class="item">
                                        <img class="addImg"  id = "logoPic" onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a11.png" width="50" height="50"  />
                                        <input  type="hidden" name="logoPic" >
                                    </div>
                                    <div style="clear: left;"></div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>

                <div class="active_table_submit mart20">
                    <div class="button_place">
                        <a class="btn btn-blue btnSave">保存并下一步</a>
                    </div>
                </div>
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
    </div>
    <!--          配置活动规则 -->
    <div class="tab-content" style="margin-top:-20px; display: none;">
        <iframe id="ruleFrame" class="mart20" src="<%=cpath%>/templateUi/showAuthAdd.do?vjfSessionId=${vjfSessionId}&templateKey=${templateKey}" style="width: 100%;" scrolling="no" frameborder="no" border="0" onload="setIframeHeight()" on></iframe>
    </div>
    </form>
</div>
</div>
</div>
</body>
</html>
