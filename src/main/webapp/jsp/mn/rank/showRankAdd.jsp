<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath();
    String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
    String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

    <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>


    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.2"></script>

    <link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.excheck.js"></script>

    <link href="<%=cpath%>/plugins/bootstrap-select-1.13.14/dist/css/bootstrap-select.min.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-select-1.13.14/dist/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-select-1.13.14/dist/js/i18n/defaults-zh_CN.js"></script>
    <script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];
        var setting = {
            check: {
                enable: true,
                chkboxType: { "Y": "p", "N": "ps" }
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };

        var zNodes = eval('${areaJson}');
        var zDepartmentNodes = eval('${mnDepartmentJson}');
        var zDepartmentNodes4_5 = eval('${mnDepartmentJsonLeave4_5}');

        $(document).ready(function () {
            // 区域初始化
            $.fn.zTree.init($("#tree"), setting, zNodes);
            $.fn.zTree.init($("#DepartmentTree"), setting, zDepartmentNodes);
            $.fn.zTree.init($("#departmentTree4_5"), setting, zDepartmentNodes4_5);
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));

            initPage();
            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });

        });

        function validForm() {
            var validateResult = $.submitValidate($("#code_form"));
            if(!validateResult){
                return false;
            }
            // 页面校验
            var v_flag = true;
            $(".validate_tips:not(:hidden)").each(function(){
                if($(this).text() != ""){
                    $.fn.alert($(this).text());
                    v_flag = false;
                }
            });
            if(!v_flag){
                return false;
            }
            treeObj();
            var sHTML = $('.summernote').summernote('code');
            $('#ruleContent').val(sHTML.trim());
            $('#initBannerUrl').val(ids.toString());
            $('#activityBannerUrl').val(ids2.toString());

            if(!validateResult){
                return false;
            }
            if ($('#sku input[type=checkbox]:checked').length == 0) {
                $.fn.alert("请至少选择一个SKU");
                return false;
            }
            if (!sHTML || sHTML.trim()=='') {
                $.fn.alert("规则必须填写");
                return false;
            }
            if(ids.length > 1 || ids2.length>1){
                $.fn.alert("只能上传一张照片");
                return false;
            }if(ids.length < 1 || ids2.length < 1){
                $.fn.alert("图片未上传");
                return false;
            }
            let activityAreaCode = $("#activityArea").val();
            let department = $("#department").val();
            if(!activityAreaCode && !department ){
                $.fn.alert("活动区域必需选择");
                return false;
            }

            $("[name='receiveEndTime']").attr("disabled",false);
            return true;
        }

        function initPage() {
            $("input[name=activityName]").on("change",function () {
                let that = $(this)
                    $.ajax({
                        url : basePath+"/mnRank/checkActivityName.do",
                        data:{
                            "activityName":that.val()
                        },
                        type : "POST",
                        dataType : "json",
                        async : true,
                        beforeSend: appendVjfSessionId,
                        success: function (data) {
                            if (data) {
                                that.val("")
                                $.fn.alert("活动名称已存在，请重新输入");
                            }
                        }
                    });
            })
            // 底部按钮事件
            $(".button_place").find("button").click(function(){
                var btnEvent = $(this).data("event");
                // 返回
                if(btnEvent == "0"){
                    var url = $(this).data("url");
                    $("form").attr("onsubmit", "");
                    $("form").attr("action", url);
                    $("form").submit();

                    // 保存生效
                } else {
                    var flag = validForm();
                    if(flag) {
                        if(btnEvent == "2"){
                            if(confirm("确认发布？")){
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
            });
            // 全选
            $("#allCheck").on("change", function () {
                if ($(this).prop("checked")) {
                    $("[name='skuKeys']").prop("checked", "checked");
                } else {
                    $("[name='skuKeys']").prop("checked", "");
                }
            });


            $("input[name='srearchSku']").on("keyup", function () {
                let inputSkuName = $(this).val()
                $(".srearchSku").each(function(){
                    // let this = $(this);
                    if($(this).attr("sku").indexOf(inputSkuName)<0){
                        $(this).hide()
                    }else{
                        $(this).show()
                    }
                });
            });
            var departmentTreeObj = null;
            $("input[name='activityRole']").on("change", function () {
                if ($("input[name='activityRole']:checked").val() == '1'){
                    departmentTreeObj = $("#mndepartmentTree");
                    $("#mndepartmentTree").show();
                    $("#mndepartmentTree4_5").hide();
                } else {
                    departmentTreeObj = $("#mndepartmentTree4_5");
                    $("#mndepartmentTree").hide();
                    $("#mndepartmentTree4_5").show();
                }
                $("input[name='activityType']").triggerHandler("change")
            });
            departmentTreeObj = $("#mndepartmentTree4_5");

            $("input[name='activityType']").on("change", function () {
                if ($("input[name='activityType']:checked").val() == '1'){
                    $("#areaTree").show();
                    $("#mndepartmentTree").hide();
                    $("#mndepartmentTree4_5").hide();
                } else {
                    departmentTreeObj.show()
                    $("#areaTree").hide();
                }
            });
            departmentTreeObj.show()
            $("#areaTree").hide();
            $("#mndepartmentTree").hide();
        }
        function treeObj(){
            var treeObj = {};
            if($("input[name=activityType]:checked").val()=='1'){
                 treeObj = $.fn.zTree.getZTreeObj("tree");
            }else{
                if($("input[name=activityRole]:checked").val()=='1'){
                    treeObj = $.fn.zTree.getZTreeObj("DepartmentTree");
                }else{
                    treeObj = $.fn.zTree.getZTreeObj("departmentTree4_5");
                }
            }
            var areaStr = "";
            var areaCodeStr = "";
            var nodes = treeObj.getCheckedNodes(true);
            var getSelectedNodes = treeObj.getSelectedNodes();
            if(nodes != ''){
                // 省-市-县;省-市;省;省-市-县...
                var lastChar;
                var level0;
                var level1;
                var level2;
                var levelCode0;
                var levelCode1;
                var levelCode2;
                var idx = 0;
                for ( var i = 0; i <nodes.length; i++){
                    if(nodes[i].level == '0'){
                        level0 = nodes[i].name;
                        levelCode0 = nodes[i].id;
                        lastChar = areaStr.substring(areaStr.length - 1);
                        if(lastChar == '-'){
                            areaStr = areaStr.substring(0, areaStr.length - 1) + ";";
                            areaCodeStr = areaCodeStr.substring(0, areaCodeStr.length - 1) + ";";
                        }
                        areaStr += level0 + "-";
                        areaCodeStr += levelCode0 + "-";
                        idx = 0;
                    }
                    if(nodes[i].level == '1'){
                        lastChar = areaStr.substring(areaStr.length - 1);
                        if(lastChar == '-' && idx>0 ){
                            areaStr = areaStr.substring(0, areaStr.length - 1) + ";";
                            areaCodeStr = areaCodeStr.substring(0, areaCodeStr.length - 1) + ";";
                        }
                        if(idx>0  ){
                            areaStr += level0 + "-";
                            areaCodeStr += levelCode0 + "-";
                        }
                        level1 = nodes[i].name;
                        areaStr += level1;
                        areaStr += "-";
                        levelCode1 = nodes[i].id;
                        areaCodeStr += levelCode1 + "-";
                        idx = 1;
                    }
                    if(nodes[i].level == '2'){
                        lastChar = areaStr.substring(areaStr.length - 1);
                        if(lastChar == '-' && idx >1){
                            areaStr = areaStr.substring(0, areaStr.length - 1) + ";";
                            areaCodeStr = areaCodeStr.substring(0, areaCodeStr.length - 1) + ";";
                        }
                        if( idx >1){
                            areaStr += level0+"-"+level1 + "-";
                            areaCodeStr += levelCode0+"-"+levelCode1 + "-";
                        }
                        level2 = nodes[i].name;
                        areaStr += level2+"-";
                        levelCode2 = nodes[i].id;
                        areaCodeStr += levelCode2 + "-";
                        idx = 2;
                    }
                    if(nodes[i].level == '3'){
                        if(idx == 3){
                            areaStr += level0 + "-" + level1 + "-"+level2+"-";
                            areaCodeStr += levelCode0 + "-" + levelCode1 + "-"+levelCode2+"-";
                        }
                        areaStr += nodes[i].name+";";
                        areaCodeStr += nodes[i].id + ";";
                        idx = 3;
                    }
                }
                if(lastChar.length > 0 || areaStr.length > 0){
                    lastChar = areaStr.substring(areaStr.length - 1);
                    if(lastChar == '-'){
                        areaStr = areaStr.substring(0, areaStr.length - 1)+";";
                        areaCodeStr = areaCodeStr.substring(0, areaCodeStr.length - 1)+";";

                    }
                }
                if($("input[name=activityType]:checked").val()=='1'){
                    $("#activityArea").val(areaCodeStr);
                    $("#activityAreaName").val(areaStr);
                }else{
                    $("#department").val(areaCodeStr);
                    $("#departmentName").val(areaStr);
                }

            }
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
                    var result = {};
                    result = jQuery.parseJSON(xhr.responseText);
                    if(result["errMsg"]=="error"){
                        alert("上传失败");
                        return;
                    }
                    var srcList = [];
                    srcList = result["ipUrl"].split(",");
                    for(var i = 0;i<srcList.length;i++){
                        $('.summernote').summernote('insertImage',srcList[i], srcList[i]);
                    }

                }else{
                }
            };
            xhr.send(formData);
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

        .top-only {
            border-top: 1px solid #e1e1e1;
        }
        .tab-radio {
            margin: 10px 0 0 !important;
        }
        .validate_tips {
            padding: 8px !important;
        }
        .inner {
            margin-top: 26px !important;
        }
    </style>
</head>

<body>



<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">排行榜</a></li>
            <li class="current"><a title="">新增</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/mnRank/rankSave.do">
                <input type="hidden" name="roleDescribe" id="roleDescribe" value="">
                <input type="hidden" name="initBannerUrl" id="initBannerUrl" value="">
                <input type="hidden" name="activityBannerUrl" id="activityBannerUrl" value="">
                <input type="hidden" name="activityArea" id="activityArea" value="">
                <input type="hidden" name="activityAreaName" id="activityAreaName" value="">
                <input type="hidden" name="department" id="department" value="">
                <input type="hidden" name="departmentName" id="departmentName" value="">
                <input type="hidden" name="ruleContent" id="ruleContent" value="">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activityName" tag="validate"
                                               class="form-control input-width-larger  required" autocomplete="off" maxlength="25" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动有效期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="startTime" id="startTime"  tag="validate" class="form-control input-width-larger Wdate required   "
                                               autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s', maxDate:'#F{$dp.$D(\'endTime\')}'})" />
                                        <span class="blocker en-larger">--</span>
                                        <input name="endTime" id="endTime" tag="validate" class="form-control input-width-larger Wdate required "
                                                autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'startTime\')}'})" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">角色<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">

                                        <span class="blocker en-larger">   <input type="radio" class="tab-radio required" tag="validate" checked name="activityRole" value="0" style="float:left; cursor: pointer;" />配送员</span>
                                        <input type="radio" class="tab-radio required" tag="validate" name="activityRole" value="3" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">分销商</span>
                                        <input type="radio" class="tab-radio required" tag="validate" name="activityRole" value="1" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">终端店主</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="sku">
                                <td class="ab_left"><label class="title">sku：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3" align="left">
                                    <div class="content  " style="  margin: 5px 0px;">
                                        <div style="float: left" >
                                            <input type="checkbox" name="skuKeys"  id="allCheck" class="tab-radio" style=" " value="">
                                            <span class="" style="margin-top: 12px">全选</span>
                                            <input name="srearchSku"  id="srearchSku"  class=" input-width-larger"  />
                                        </div>
                                        <br>
                                        <br>
                                        <c:if test="${!empty skuList}">
                                            <c:forEach items="${skuList}" var="item" varStatus="idx">
                                                <div style="float: left;position: relative;width: 20%" class="srearchSku"  sku="${item.skuName}" >
                                                    <input type="checkbox" name="skuKeys"  sku="${item.skuName}" class="tab-radio  srearchSku"  value="${item.skuKey}">
                                                    <span class="srearchSku" sku="${item.skuName}"  style="margin-top: 12px">${item.skuName}</span>
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">是否包含级联数据<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio required"  tag="validate" checked name="cascadeFlag" value="1" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">包含</span>
                                        <input type="radio" class="tab-radio required"  tag="validate" name="cascadeFlag" value="0" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">不包含</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动规则：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="summernote"></div>
                                </td>
                            </tr>
                            <tr style="height: 200px;">
                                <td class="ab_left"><label class="title">入口banner图：<span class="required">*</span><br/>建议700*166</label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px;" >
                                        <form enctype="multipart/form-data">
                                            <input type="hidden" id="imagepath" name="">  <!-- 保存的图片id 用于表单提交 -->
<%--                                            <input id="initBannerUrl" name="prizeImgAry" type="hidden" class='filevalue' />--%>
                                            <!-- 上传按钮 -->
                                            <div id="upload_duixiang" class="scroll"><img src="<%=cpath %>/inc/vpoints/img/a11.png" style="width: 130px;height:120px"></div>
                                            <div class="show scroll"  >
                                            </div> <!-- 输出图片 -->
                                        </form>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 200px;">
                               <td class="ab_left"><label class="title">活动banner图：<span class="required">*</span><br/>建议750*620</label></td>
                               <td class="ab_main" colspan="3">
                                   <div style="height: 163px;width: 800px;" >
                                       <form enctype="multipart/form-data">
                                           <input type="hidden" id="imagepath2" name="">  <!-- 保存的图片id 用于表单提交 -->
<%--                                           <input id="activityBannerUrl" name="prizeImgAry" type="hidden" class='filevalue' />--%>
                                           <!-- 上传按钮 -->
                                           <div id="upload_duixiang2" class="scroll"><img src="<%=cpath %>/inc/vpoints/img/a11.png" style="width: 130px;height:120px"></div>
                                           <div class="show2 scroll"  >
                                           </div> <!-- 输出图片 -->
                                       </form>
                                   </div>


                               </td>
                            </tr>
                        </table>
                    </div>
                    <div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>区域配置</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">活动类型<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio required" checked name="activityType"  tag="validate" value="0" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">组织架构</span>
                                        <input type="radio" class="tab-radio required" name="activityType"  tag="validate" value="1" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">行政区域</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="areaTree">
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3" >

                                    <div class="content_wrap" style="display:table-cell; vertical-align:middle;">
                                        <div class="ztree" >
                                            <ul id="tree" class="ztree" style="background-color: white;width: 100%"></ul>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr id="mndepartmentTree" hidden>
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3" >
                                    <div class="content_wrap" style="display:table-cell; vertical-align:middle;">
                                        <div class="ztree" >
                                            <ul id="DepartmentTree" class="ztree" style="background-color: white;width: 100%"></ul>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr id="mndepartmentTree4_5" >
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3" >
                                    <div class="content_wrap" style="display:table-cell; vertical-align:middle;">
                                        <div class="ztree" >
                                            <ul id="departmentTree4_5" class="ztree" style="background-color: white;width: 100%"></ul>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-red btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/mnRank/showRankList.do">返 回</button>
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
                    <h6 class="add_success">添加成功</h6>
                    <h6 class="add_fail">添加失败</h6>
                    <h6 class="edit_success">编辑成功</h6>
                    <h6 class="edit_fail">编辑失败</h6>
                    <h6 class="import_success">导入成功</h6>
                    <h6 class="import_fail">导入失败</h6>
                    <h6 class="not_delete">请至少选择一条数据进行删除</h6>
                    <h6 class="is_delete">删除成功</h6>
                    <h6 class="xx_delete">删除失败</h6>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
<link rel="stylesheet" type="text/css" href="<%=cpath%>/inc/goods/upload/FraUpload.css">
<link rel="stylesheet" type="text/css" href="<%=cpath%>/inc/goods/upload/FraUpload2.css">
<script type="text/javascript" src="<%=cpath%>/inc/goods/upload/Sortable.js"></script>
<script type="text/javascript" src="<%=cpath%>/inc/goods/upload/MD5.js"></script>
<script type="text/javascript" src="<%=cpath%>/inc/goods/upload/FraUpload.js"></script>
<script type="text/javascript" src="<%=cpath%>/inc/goods/upload/FraUpload2.js"></script>
<script onload="this" >
    var ids = [];
    // 商品图片上传
    var a = $("#upload_duixiang").FraUpload({
        view: ".show",      // 视图输出位置
        url:  basePath+"/skuInfo/imgUploadUrl.do", // 上传接口
        fetch: "img",   // 视图现在只支持img
        debug: false,    // 是否开启调试模式
        beforeSend:appendVjfSessionId,
        /* 外部获得的回调接口 */
        onLoad: function (e,viewId) { // 选择文件的回调方法
        },
        breforePort: function (e) {         // 发送前触发
            console.log("文件发送之前触发");
        },
        successPort: function (e) {         // 发送成功触发
            console.log("文件发送成功");
            onload_image()
        },
        errorPort: function (e) {       // 发送失败触发
            console.log("文件发送失败");
            onload_image()
        },
        deletePost: function (e) {    // 删除文件触发
            console.log("删除文件");
            alert('删除了' + e.filename)
            onload_image()
        },
        sort: function (e) {      // 排序触发
            console.log("排序");
            onload_image()
        },
    });
    // 获取图片上传信息
    function onload_image() {
        var res = a.FraUpload.show()
        ids = [];
        for (let k in res) {
            let this_val = res[k]
            if (!empty(this_val['is_upload']) && !empty(this_val['ajax'])) {
                let ajax_value = this_val['ajax'];
                ajax_value = eval("(" + ajax_value + ")");
                ids.push(ajax_value.path)
            }
        }
        $("#imagepath").val(ids);
    }
    /**
     * 判断变量是否为空
     */
    function empty(value) {
        if (value == "" || value == undefined || value == null || value == false || value == [] || value == {}) {
            return true;
        } else {
            return false;
        }
    }
    var ids2 = [];
    // 商品图片上传
    var a2 = $("#upload_duixiang2").FraUpload2({
        view: ".show2",      // 视图输出位置
        url:  basePath+"/skuInfo/imgUploadUrl.do", // 上传接口
        fetch: "img",   // 视图现在只支持img
        debug: false,    // 是否开启调试模式
        beforeSend:appendVjfSessionId,
        /* 外部获得的回调接口 */
        onLoad: function (e) {// 选择文件的回调方法
            console.log("外部: 初始化完成...",e);

        },
        breforePort: function (e) {         // 发送前触发
            console.log("文件发送之前触发");
        },
        successPort: function (e) {         // 发送成功触发
            console.log("文件发送成功");
            onload_image2()
        },
        errorPort: function (e) {       // 发送失败触发
            console.log("文件发送失败");
            onload_image2()
        },
        deletePost: function (e) {    // 删除文件触发
            console.log("删除文件");
            alert('删除了' + e.filename)
            onload_image2()
        },
        sort: function (e) {      // 排序触发
            console.log("排序");
            onload_image2()
            console.log("ids2", ids2)
        },
    });
    // 获取图片上传信息
    function onload_image2() {
        var res = a2.FraUpload2.show()
        ids2 = [];
        for (let k in res) {
            let this_val = res[k]
            if (!empty(this_val['is_upload']) && !empty(this_val['ajax'])) {
                let ajax_value = this_val['ajax'];
                ajax_value = eval("(" + ajax_value + ")");
                ids2.push(ajax_value.path)
            }
        }
        $("#imagepath2").val(ids2);
    }


</script>
</html>

