<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
    String imagePath = PropertiesUtil.getPropertyValue("image_server_url");
    String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;

%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>折扣活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
      <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>

      <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];
        var clickImg = function (id) {
            $("#addBatchDialog").modal("show");
            parent.$("#my_iframe").contents().find("#queryTY").click();
            //移除选中项
            $('input:checkbox').removeAttr('checked');
            $("#btnName").val(id);
            console.log("id=",id, $("#btnName").val())
        }

        // 本界面上传图片要求
        var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };
        var chk_value = [];
        var goodsIdList = [];
        var goodsList = [];
        var goodsAllList = [];
        var goodsObj = ${activityJSON};
        var changeCheckBoxArray = [];
        $(function(){
            initGoodsList(goodsObj.goodsIdList);
            // 初始化校验控件
            $.runtimeValidate($("div.tab-content"));
            // 初始化功能
            initPage();
         	// 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
        });
        
        function initPage() {
            $("#cover").attr("src",src='${activityCog.cover}');
            $("input[name='cover']").val('${activityCog.cover}');
            $(".qrcodeUploadDialog, #goodsSelectBtn").on("click", function () {
                var goodsName = $("#keyWord").val()
                var categoryParent = $("#categoryParent option:selected").val()
                var exchangeType = $("#exchangeType option:selected").val()
                var paramJson = {
                    "goodsName":goodsName,
                    "categoryParent":categoryParent,
                    "exchangeType":exchangeType,
                    "isGiftCard":"1",
                    "goodsIdArrayStr":changeCheckBoxArray.join(","),
            
                }
                var url = "<%=cpath%>/vpointsGoods/getShopGoods.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(datas) {
                        console.log("datas",datas)
                        var reply = datas.reply;
                        if(datas.result.businessCode==='0'){
                            goodsList = reply;
                            goodsAllList = goodsAllList.length==0 ? reply : goodsAllList;
                            $("#goodsDetailsBody").empty()
                            reply.forEach(val => {
                                var goodsUrl = "<%=imagePath%>/"+  val.goodsUrl?.split(",")[0]
                                var exchangeType = getExchangeType(val.exchangeType);
                                var realMoney = val.realPay / 100;
                                //回显选中
                                var inputType = '';
                                if(changeCheckBoxArray.length > 0 && changeCheckBoxArray.indexOf(val.goodsId)>-1){
                                    inputType = `checked`
                                }
                                let iType = "checkbox";
                                if($("#giftCardType").val()=='2'){
                                    iType = "radio"
                                }
                                $("#goodsDetailsBody").append('<tr class="checkboxs">' +
                                    '<td class="text_center firstTd" style="vertical-align: middle;">' +
                                    '<input type=' +iType+
                                    ' name="goodsDetailsBodyCheckbox" ' +
                                    inputType +
                                    ' value="'+val.goodsId+'">' +
                                    '</td>' +
                                    '<td class="text_center" style="vertical-align: middle;"><img src="'+goodsUrl+'"></td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+val.goodsName+'</br><li style="color: red;list-style: none;">'+realMoney+'元</li></td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+exchangeType+'</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+val.categoryParent+'</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+val.goodsNum+'</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+val.goodsRemains+'</td>' +
                                    '</tr>')
                            });
                        }
                        $("#qrcodeUploadDialog input.import-file").val("");
                        $("#qrcodeUploadDialog").modal("show");
                        // $('input[name="goodsDetailsBodyCheckbox"]').on('change',function(){
                        //
                        //     console.log("changeCheckBoxArray==",changeCheckBoxArray)
                        // });
                        //除了表头（第一行）以外所有的行添加click事件.
                        $(".checkboxs").click(function () {
                            // 切换样式
                            // $(this).toggleClass("tr_active");
                            // 找到checkbox对象
                            var chks = $("input[name='goodsDetailsBodyCheckbox']",this);
                            if(chks.is(':checked')){
                                // 之前已选中，设置为未选中
                                chks.attr("checked",false);
                            }else{
                                // 之前未选中，设置为选中
                                chks.prop("checked",true);
                            }
                            // chks.trigger("change");
                            changeCheckboxArrayPush(chks)
                        });
                    }
                });
               
            });
            $("#addBtn").on("click", function () {
                $("#qrcodeUploadDialog #closeBtn").trigger("click");
                $("#goodsDetailsBodyTable").empty()
                $("[name='goodsIdList']").remove()
                for (let i = 0; i < goodsAllList.length; i++) {
                    if (changeCheckBoxArray.indexOf(goodsAllList[i].goodsId) < 0) {
                        continue;
                    }
                    appendGoodsDetailsTable(goodsAllList[i]);
                    $("#code_form").append('<input type="hidden" name="goodsIdList" value="'+goodsAllList[i].goodsId+'">')
                }

            });
            $("#giftCardType").change(function () {
                let val = $(this).val()
                if(val=='2'){
                    changeCheckBoxArray = [];
                    $("#goodsDetailsBodyTable").empty();
                    $("#giftCardStatus").empty();
                    $("#giftCardStatus").append('  <option value="">请选择</option>\n' +
                        '<option value="2">兑付礼品卡</option>\n');
                }
                if(val=='0'){
                    $("#giftCardStatus").empty();
                    $("#giftCardStatus").append('  <option value="">请选择</option>\n' +
                        '<option value="0">普通礼品卡</option>\n' +
                        '<option value="1">商务礼品卡</option>\n');
                }
                $("#giftCardStatus").triggerHandler("change");
            })
            $("#giftCardStatus").on('change',function () {
                var tips = $(this).parent().find(".validate_tips");
                if(!$("#giftCardStatus").val()){
                    tips.addClass("valid_fail_text");
                    tips.html("礼品卡商品类型分类不能为空");
                    tips.show();
                }else{
                    tips.removeClass("valid_fail_text");
                    tips.html("");
                    tips.hide();
                }
            })
            function changeCheckboxArrayPush(chks){
                if($("#giftCardType").val()=='2'){
                    changeCheckBoxArray=[]
                }
                if(chks.is(':checked')){
                    changeCheckBoxArray.push(chks.val())
                }else{
                    changeCheckBoxArray = removeElement(changeCheckBoxArray,chks.val())
                }
            }
            $("#goodsResetBtn").on("click", function () {
                $("#categoryParent").find("option").eq(0).prop("selected", "selected");
                $("#exchangeType").find("option").eq(0).prop("selected", "selected");
                $("#keyWord").val("");
            });
            $(".close,#closeBtn").on("click", function () {
                $("#categoryParent").find("option").eq(0).prop("selected", "selected");
                $("#exchangeType").find("option").eq(0).prop("selected", "selected");
                $("#keyWord").val("");
            });
            $("#addBatchDialog").delegate("#comBtn", "click", function () {
                $(window.frames["my_iframe"].document).find("input[type=radio]:checked").each(function () {
                    var btnName = $("#btnName").val();
                    var picWidth = $(this).siblings("div[name = picWidth]").html();
                    var picHeight = $(this).siblings("div[name = picHeight]").html();
                    var picX = $(this).siblings("div[name = picX]").html();
                    var picY = $(this).siblings("div[name = picY]").html();
                    console.log("btnName=",btnName);
                    $("#" + btnName).attr("src", "https://img.vjifen.com/images/vma/" + $(this).val());
                    $('input[name="' + btnName + '"]').val("https://img.vjifen.com/images/vma/" + $(this).val());
                });
                $("#addBatchDialog #closeBtnP").trigger("click");
            });
            // 保存活动信息
            $(".btnSave").on("click", function(){
                if (changeCheckBoxArray.length <= 0) {
                    $.fn.alert("商品选择不能为空")
                    return;
                }
               validForm();
            });

        }

        function checkGoodsActivity() {
            var checkGoodsActivityUrl = "<%=cpath%>/giftCardAction/checkGoodsActivity.do";
            var that = $(this);
            $.ajax({
                type: "POST",
                url: checkGoodsActivityUrl,
                data: {
                    "goodsIdList": changeCheckBoxArray.join(","),
                    "startDate": $("#startDate").val(),
                    "endDate": $("#endDate").val(),
                    "infoKey": $("#infoKey").val(),
                },
                dataType: "json",
                async: false,
                beforeSend: appendVjfSessionId,
                success: function (datas) {
                    console.log("datas", datas)
                    if(datas.result.businessCode=='0'){
                        $.fn.alert(datas.result.msg);
                        return false;
                    }

                    var url = that.attr("url");
                    $.fn.confirm("确认发布？", function(){
                        $(".btnSave").attr("disabled", "disabled");
                        $("#divId").css("display","block");
                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
                    return  true;
                }
            });
        }
        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }
            if($("input[name= cover]").val() == "" || $("input[name= cover]").val() == null){
                $.fn.alert("礼品卡图片不能为空");
                return false;
            }
            checkGoodsActivity();
            return true;
        }
        function initGoodsList(goods) {
            let paramJson2 = {
            }
            var url = "<%=cpath%>/vpointsGoods/getShopGoods.do";
            $.ajax({
                type: "POST",
                url: url,
                data: paramJson2,
                dataType: "json",
                async: false,
                beforeSend:appendVjfSessionId,
                success:  function(datas) {
                    console.log("datas",datas)
                    var reply = datas.reply;
                    if(datas.result.businessCode==='0'){
                        goodsList = reply;
                        $("#goodsDetailsBodyTable").empty()
                        for (var index in reply) {
                            if(goods.indexOf(goodsList[index].goodsId) < 0){
                                continue;
                            }
                            changeCheckBoxArray.push(goodsList[index].goodsId);
                            appendGoodsDetailsTable(goodsList[index]);
                        }
                    }
                }
            });
        }
        function appendGoodsDetailsTable(reply) {
            var goodsUrl = "<%=imagePath%>/"+  reply.goodsUrl?.split(",")[0]
            var exchangeType = getExchangeType(reply.exchangeType);
            var realMoney = reply.realPay / 100;
            $("#goodsDetailsBodyTable").append('<tr>' +
                '<td class="text_center" style="vertical-align: middle;"><img src="'+goodsUrl+'"></td>' +
                '<td class="text_center" style="vertical-align: middle;">'+reply.goodsName+'</br><li style="color: red;list-style: none;">'+realMoney+'元</li></td>' +
                '<td class="text_center" style="vertical-align: middle;">'+exchangeType+'</td>' +
                '<td class="text_center" style="vertical-align: middle;">'+reply.categoryParent+'</td>' +
                '<td class="text_center" style="vertical-align: middle;">'+reply.goodsNum+'</td>' +
                '<td class="text_center" style="vertical-align: middle;">'+reply.goodsRemains+'</td>' +
                '<td class="text_center" style="vertical-align: middle;">' +
                '<button type="button" class="btn btn-default btn-red removeCheckboxGoodsArray" ' +
                'value="'+reply.goodsId+'">删 除' +
                '                                        </button>' +
                '</td>' +
                '</tr>')
            $(".removeCheckboxGoodsArray").click(function () {
                $(this).parent().parent().remove()
                $("[name='goodsIdList']").remove()
                changeCheckBoxArray = removeElement(changeCheckBoxArray,$(this).val())
                for (let i = 0; i < changeCheckBoxArray.length; i++) {
                    $("#code_form").append('<input type="hidden" name="goodsIdList" value="'+changeCheckBoxArray[i]+'">')
                }
            })
        }
        function getExchangeType(exchange) {
            if ("1" == exchange) {
                return "实物";
            }
            if ("2" == exchange) {
                return "电子券";
            }
            if ("3" == exchange) {
                return "话费";
            }
        }
        function removeElement(arr, item) {
            return arr.filter(function(i){
                return i!=item;
            })
        }
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
        .blocker {
            float: left;
            vertical-align: middle;
            margin-right: 8px;
            margin-top: 8px;
        }
        .en-larger {
            margin-left: 8px;
        }

        .validate_tips {
            padding: 8px !important;
        }
        .tablebox table {
            width: 100%;
        }

        .tablebox table th,
        .tablebox table td {
            font-size: 12px;
            color: #7ca6f4;
            line-height: 26px;
            text-align: center;
        }

        .tablebox table tr th {
            background: #022852;
            cursor: pointer;
        }

        .tablebox table tr td {
            border-bottom: 1px solid #202e64;
        }
        tr td img{
            width: 20%;
            margin-top: 2%;
        }
        tr td p{
            width: 60px;
            height: 20px;
            font-weight: bold;
            border-radius: 5px;
            margin: auto;
            text-align: center;
            line-height: 20px;
            color: #000000;
        }
        .addImg1 {
            width: 120px;
            height: 120px;
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
            width: 120px;
            height: 120px;
            float: left;
            position: relative;
            margin: 20px;
        }
    </style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 积分商城</a></li>
            <li class="current"><a> 礼品卡管理</a></li>
            <li class="current"><a> 修改礼品卡</a></li>
        </ul>
    </div>
    
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" action="<%=cpath%>/giftCardAction/giftCardActivityUpdate.do" class="form-horizontal row-border validate_form" id="code_form">
                <input type="hidden" name="infoKey" id="infoKey" value="${activityCog.infoKey}">
                <input type="hidden" name="btnName" id="btnName" value="">
                <div class="widget box activityinfo">
				<!--- 活动信息 -->
                    <div class="tab-content">
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">礼品卡类型：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <select id="giftCardType" name="giftCardType" class="form-control required input-width-larger" tag="validate">
                                                <option value="">请选择</option>
                                                <option value="0" <c:if test="${activityCog.giftCardType eq '0'}"> selected</c:if>>普通卡</option>
<%--                                                <option value="1" <c:if test="${activityCog.giftCardType eq '1'}"> selected</c:if>>充值卡</option>--%>
                                                <option value="2" <c:if test="${activityCog.giftCardType eq '2'}"> selected</c:if>>兑付卡</option>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">礼品卡商品类型分类：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <select id="giftCardStatus" name="giftCardStatus" class="form-control required input-width-larger" tag="validate">
                                                <option value="">请选择</option>
                                                <c:if test="${activityCog.giftCardType eq '0'}" >
                                                    <option value="0"  <c:if test="${activityCog.giftCardStatus eq '0'}"> selected</c:if>>普通礼品卡</option>
                                                    <option value="1" <c:if test="${activityCog.giftCardStatus eq '1'}"> selected</c:if>>商务礼品卡</option>
                                                </c:if>
                                                <c:if test="${activityCog.giftCardType eq '2'}" >
                                                    <option value="2" <c:if test="${activityCog.giftCardStatus eq '2'}"> selected</c:if>>兑付礼品卡</option>
                                                </c:if>
<%--                                                <option value="3" <c:if test="${activityCog.giftCardStatus eq '3'}"> selected</c:if>>至尊礼品卡</option>--%>
<%--                                                <option value="4" <c:if test="${activityCog.giftCardStatus eq '4'}"> selected</c:if>>超级至尊礼品卡</option>--%>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">礼品卡名称：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="giftCardName" tag="validate" value="${activityCog.giftCardName}"
                                                   class="form-control required input-width-larger" autocomplete="off" maxlength="8"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">礼品卡图片：<span class="required">*</span></label>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <div class="item">
                                                <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=13"
                                                   target= "my_iframe" class="addImg1"  onclick="clickImg('cover');" style="z-index: 3; opacity: 0;"></a>
                                                <img class="addImg1" id="cover"
                                                     src="<%=cpath %>/inc/vpoints/img/a12.png"/>
                                                <input type="hidden" name="cover" >
                                            </div>
                                            <div style="clear: left;"></div>
                                        </div>
                                    </td>
                                </tr>


                                <tr>
                                    <td class="ab_left"><label class="title">礼品卡上线状态：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content ">
                                            <input type="radio" name="cardStatus" value="0" <c:if test="${activityCog.cardStatus eq '0'}"> checked</c:if> class="tab-radio cardStatus tabsObj"  style="float:left; cursor: pointer;margin-top: 12px"> <span class="blocker en-larger">待上线</span>
                                            <input type="radio" name="cardStatus" value="1" <c:if test="${activityCog.cardStatus eq '1'}"> checked</c:if> class="tab-radio cardStatus tabsObj" style="float:left; cursor: pointer;margin-top: 12px"><span class="blocker en-larger">已上线</span>
                                            <input type="radio" name="cardStatus" value="2" <c:if test="${activityCog.cardStatus eq '2'}"> checked</c:if> class="tab-radio cardStatus tabsObj" style="float:left; cursor: pointer;margin-top: 12px"><span class="blocker en-larger">已下线</span>
                                            <label class="validate_tips"></label>
                                        </div>

                                    </td>
                                </tr>

                                <tr>
                                    <td class="ab_left"><label class="title">礼品卡商品：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content ">
                                            <span class="blocker en-larger">单个商品仅允许关联一个礼品卡</span>
                                            <label class="validate_tips"></label>
                                        </div>

                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title"><span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="bottom-left">
                                            <a class="blue qrcodeUploadDialog" href="#">选择商品</a>
                                        </div>
                                    </td>
                                </tr>
                                 <tr>
                                    <td class="ab_left"><label class="title"><span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
										<div class="bottom-left">
                                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                                     style=''>
                                                <thead>
                                                <tr>
                                                    <th style="width: 6%;text-align: center;" >图片</th>
                                                    <th style="width: 13%;text-align: center;" >商品名称</th>
                                                    <th style="width: 6%;text-align: center;" >商品分类</th>
                                                    <th style="width: 6%;text-align: center;" >一级分类</th>
                                                    <th style="width: 6%;text-align: center;" >库存</th>
                                                    <th style="width: 6%;text-align: center;" >剩余个数</th>
                                                    <th style="width: 6%;text-align: center;" >操作</th>
                                                </tr>
                                                </thead>
                                                <tbody id="goodsDetailsBodyTable">
                                                
                                                </tbody>
                                            </table>
										</div>
                                    </td>
                                </tr>
                               
                            </table>
                        </div>
                        <div class="active_table_submit mart20">
                            <div class="button_place">
                                <a class="btn btn-blue btnSave">保 存</a>
                                <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/giftCardAction/showGoodsGiftCardActivityList.do">返 回</button>
                            </div>
                        </div>
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
                                        <button type="button" id="closeBtnP" class="btn btn-default"
                                                data-dismiss="modal">关 闭
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    </div>
<%--    <div id="tableMater" style="margin-top:-5px;max-height: 278px; overflow: auto; box-sizing: border-box;"><jsp:include page="table1.jsp"></jsp:include></div>--%>

    <div class="modal fade" id="qrcodeUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-hidden="false"
         style="margin-top:-40px;max-height: 100%; overflow: auto; box-sizing: border-box;">
        <div class="modal-dialog" >
            <div class="modal-content" style="top:5%;left: -45%; width: 180%; height: auto;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel2">选择可用商品</h4>
                </div>
                
                <div class="widget-content form-inline">
                        <div class="row">
                            <div class="col-md-9 text-center" style="width:109%;  margin-left: -10%;">
                                <div class="form-group  search mart20" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">商品分类：</label>
                                        <select name="goodsType" id="exchangeType" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="1">实物</option>
                                            <option value="2">电子券</option>
                                            <option value="3">话费</option>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">一级分类：</label>
                                        <select name="firstType" id="categoryParent" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">全部</option>
                                            <c:forEach items="#{firstCategoryList}" var="cate">
                                                <option style="padding: 20px;" value="${cate.categoryType}">${cate.categoryName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="search-item" >
                                        <label class="control-label" style="width: 105px !important;">商品名称：</label>
                                        <input name="keyWord" id="keyWord" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                   
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12 text-center" style=" margin-top: 70px !important;" >
                                    <button type="button" class="btn btn-primary btn-blue" id="goodsSelectBtn">查 询</button>
                                    <button type="button" class="btn btn-reset btn-radius3 marl20" id="goodsResetBtn">重 置</button>
                                </div>
                            </div>
                        </div>
                </div>
                <div class="modal-body" id="modal-body" >
                    <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                           id="dataTable_data"  style='white-space: nowrap; '>
                        <thead>
                        <tr>
                            <th style="width: 4%;text-align: center;" ></th>
                            <th style="width: 6%;text-align: center;" >图片</th>
                            <th style="width: 13%;text-align: center;" >商品名称</th>
                            <th style="width: 6%;text-align: center;" >商品分类</th>
                            <th style="width: 6%;text-align: center;" >一级分类</th>
                            <th style="width: 6%;text-align: center;" >库存</th>
                            <th style="width: 6%;text-align: center;" >剩余个数</th>
                        </tr>
                        </thead>
                        <tbody id="goodsDetailsBody">
                     
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-orange" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
  </body>
</html>
