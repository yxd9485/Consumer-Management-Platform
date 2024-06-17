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
        $(function(){
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));
            // 初始化功能
            initPage();
        });

        function validForm() {
            var validateResult = $.submitValidate($("form"));
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
            /*if(imgSrc.length==0){
                alert("请上传图片");
                return false;
            }*/
            if (imgSrc.length > 1) {
                $.fn.alert("优惠券类型图片最多上传1张");
                return false;
            } else {
                $("[name='categoryPicUrl']").val(imgSrc[0]);
            }

            return true;
        }

        function initPage() {
            // 按钮事件
            $(".button_place").find("button").click(function(){
                var btnEvent = $(this).data("event");
                if(btnEvent == "0"){
                    var url = $(this).data("url");
                    $("form").attr("onsubmit", "");
                    $("form").attr("action", url);
                    $("form").submit();
                } else {
                    var blacklistCogFlag = "0";
                    $("input[mark=blacklist]:not([type=radio])").each(function(){
                        if($(this).val() != ""){
                            blacklistCogFlag = "1";
                        }
                    });
                    $("input[name=blacklistFlag]").val(blacklistCogFlag);

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

            // 初始化模板状态显示样式
            $("#ticketStatus").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='ticketStatus']").val("1");
                    }else{
                        $("input:hidden[name='ticketStatus']").val("0");
                    }
                }
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
            <li class="current"><a title="">优惠券面额配置</a></li>
            <li class="current"><a title="">添加优惠券类型</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/vpsVcodeTicketDenomination/doTicketCategoryAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>类型信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="categoryPicUrl" id="picUrl" value="">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">优惠券类型名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="categoryName" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="100" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">跳转小程序id：<span class="required"></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="jumpId" tag="validate"
                                               class="form-control" autocomplete="off" maxlength="100" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">描述：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <div class="content">
                                            <textarea tag="validate" rows="5" class="form-control required" autocomplete="off" name="categoryDesc" maxlength="500"></textarea>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">优惠券类型图片上传：<span class="white">*</span><br/></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>
                                                </section>
                                            </div>
                                        </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除作品图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationList.do">返 回</button>
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
