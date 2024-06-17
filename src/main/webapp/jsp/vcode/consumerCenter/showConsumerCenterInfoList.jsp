<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加一码双奖活动</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=1"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

    <script>
        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("form"));

            // 初始化功能
            initPage();
        });

        function initPage() {

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
                        $("#divId").css("display", "block");

                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
                }
                return false;
            });


            // 选择客服中心类型
            $("[name='consumerCenterType']").on("change", function(){
                $eruptperitem = $(this).closest("div.eruptperitem");
                $eruptperitem.find(".center-type").removeAttr("readonly");
                $eruptperitem.find(".center-type").css("display", "none");
                
                if ($eruptperitem.find("#consumerCenterType").val() == '0'){
                    $eruptperitem.find(".center-type0").css("display", "");

                } else if ($eruptperitem.find("#consumerCenterType").val() == '1'){
                    $eruptperitem.find(".center-type1").css("display", "");
                    
                } else if ($eruptperitem.find("#consumerCenterType").val() == '2'){
                    $eruptperitem.find(".center-type2").css("display", "");
                    $eruptperitem.find("#servicePhonenum").attr("readonly", "readonly");
                }
            });
            
            // 初始化界面
            $("div.eruptperitem").each(function(i,obj){
            	$eruptperitem = $(this);
                $eruptperitem.find(".center-type").removeAttr("readonly");
                $eruptperitem.find(".center-type").css("display", "none");
            	if ($eruptperitem.find("#consumerCenterType").val() == '0'){
                    $eruptperitem.find(".center-type0").css("display", "");

                } else if ($eruptperitem.find("#consumerCenterType").val() == '1'){
                    $eruptperitem.find(".center-type1").css("display", "");
                    
                } else if ($eruptperitem.find("#consumerCenterType").val() == '2'){
                    $eruptperitem.find(".center-type2").css("display", "");
                    $eruptperitem.find("#servicePhonenum").attr("readonly", "readonly");
                }
            });

            // 新增客服中心
            $("form").on("click", "#addEruptPerItem", function(){
                if ($(this).is("[disabled]")) return;
                if ($(this).text() == "新增") {
                    $cloneItem = $(this).closest("div").clone(true, true);
                    $cloneItem.find("input.center-type").val("");
                    $cloneItem.find("#consumerCenterType option:first").prop('selected','selected');
                    $cloneItem.find("#status option:first").prop('selected','selected');
                    $cloneItem.find("#consumerCenterType").trigger("change");
                    $cloneItem.find("#addEruptPerItem").html("删除");
                    $(this).closest("td.perItem").append($cloneItem);
                } else {
                    $(this).closest("div").remove();
                }
            });
        }



        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
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
    </style>
</head>

<body>
<div id="divId"
     style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
    <h2 align="center" style="margin-top: 21%;color: blue;"><b>筛选用户中,请勿其他操作.....</b></h2>
</div>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 系统管理</a></li>
            <li class="current"><a title="">客服中心</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/consumerCenter/doConsumerCenterAdd.do">
                <input type="hidden" name="companyKey" value="${companyKey}"/>
                <input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
                <input type="hidden" name="lotteryMoney"/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>客服中心</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_main perItem">
                            <c:if test="${not empty(resultList) }">
                                <c:forEach items="${resultList}" var="item" varStatus="idx">
                                    <div class="content eruptperitem" style="margin: 5px 0px; display: flex;">
                                        <span class="blocker en-larger marl30">客服类型</span>
                                        <select id="consumerCenterType" name="consumerCenterType" tag="validate" class="form-control input-width-small" data-oldval="" autocomplete="off">
                                            <option value="0" <c:if test="${item.consumerCenterType eq '0'}"> selected="selected" </c:if>>电话</option>
                                            <option value="1" <c:if test="${item.consumerCenterType eq '1'}"> selected="selected" </c:if>>链接</option>
                                            <option value="2" <c:if test="${item.consumerCenterType eq '2'}"> selected="selected" </c:if>>原生客服</option>
                                        </select>
                                        <span id="servicePhonenumDescribeSpan" class="blocker en-larger marl30 center-type center-type0 center-type2" >客服描述</span>
                                        <input id="servicePhonenumDescribe" name="servicePhonenumDescribe" value="${item.servicePhonenumDescribe}"
                                               tag="validate" class="form-control input-width-larger center-type center-type0 center-type2" data-oldval="" autocomplete="off" maxlength="100"/>
                                        <span id="servicePhonenumSpan" class="blocker en-larger marl30 center-type center-type0 center-type2" >电话号码</span>
                                        <input id="servicePhonenum" name="servicePhonenum" <c:if test="${item.servicePhonenum != null}">value="${item.servicePhonenum}"</c:if>
                                               tag="validate" class="form-control input-width-larger center-type center-type0 center-type2" data-oldval="" autocomplete="off" maxlength="100"/>
                                        <span id="companyWechatDescribeSpan" class="blocker en-larger marl30 center-type center-type1" >链接描述&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                        <input id="companyWechatDescribe" name="companyWechatDescribe" value="${item.companyWechatDescribe}"
                                               tag="validate" class="form-control input-width-larger center-type center-type1" data-oldval="" autocomplete="off" maxlength="100"/>
                                        <span id="companyWechatLinkSpan" class="blocker en-larger marl30 center-type center-type1" >&nbsp;&nbsp;&nbsp;链接&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                        <input id="companyWechatLink" name="companyWechatLink" value="${item.companyWechatLink}"
                                               tag="validate" class="form-control input-width-larger center-type center-type1" data-oldval="" autocomplete="off" maxlength="1000"/>
                                        <span class="blocker en-larger marl30">启用状态</span>
                                        <select id="status" name="status" tag="validate" class="form-control input-width-small" data-oldval="" autocomplete="off">
                                            <option value="0" <c:if test="${item.status eq '0'}"> selected="selected" </c:if>>启用</option>
                                            <option value="1" <c:if test="${item.status eq '1'}"> selected="selected" </c:if>>停用</option>
                                        </select>
                                        <c:if test="${idx.index eq 0 }">
                                            <span id="addEruptPerItem" class="marl10 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;">新增</span>
                                        </c:if>
                                        <c:if test="${idx.index > 0 }">
                                            <span id="addEruptPerItem" class="marl10 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;">删除</span>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty(resultList) }">
                                <div class="content eruptperitem" style="margin: 5px 0px; display: flex;">
                                    <span class="blocker en-larger marl30">客服类型</span>
                                    <select id="consumerCenterType" name="consumerCenterType" tag="validate" class="form-control input-width-small" data-oldval="" autocomplete="off">
                                        <option value="0">电话</option>
                                        <option value="1">链接</option>
                                        <option value="2">原生客服</option>
                                    </select>
                                    <span id="servicePhonenumDescribeSpan" class="blocker en-larger marl30 center-type center-type0 center-type2">客服电话描述</span>
                                    <input id="servicePhonenumDescribe" name="servicePhonenumDescribe"
                                           tag="validate" class="form-control input-width-larger center-type center-type0 center-type2" data-oldval="" autocomplete="off" maxlength="100"/>
                                    <span id="servicePhonenumSpan" class="blocker en-larger marl30 center-type center-type0 center-type2">电话号码</span>
                                    <input id="servicePhonenum" name="servicePhonenum"
                                           tag="validate" class="form-control input-width-larger center-type center-type0 center-type2" data-oldval="" autocomplete="off" maxlength="100"/>
                                    <span id="companyWechatDescribeSpan" style="display: none" class="blocker en-larger marl30 center-type center-type1">链接描述&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    <input id="companyWechatDescribe" name="companyWechatDescribe" style="display: none" 
                                           tag="validate" class="form-control input-width-larger center-type center-type1" data-oldval="" autocomplete="off" maxlength="100"/>
                                    <span id="companyWechatLinkSpan" style="display: none" class="blocker en-larger marl30 center-type center-type1">&nbsp;&nbsp;&nbsp;链接&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                    <input id="companyWechatLink" name="companyWechatLink" style="display: none" 
                                           tag="validate" class="form-control input-width-larger center-type center-type1" data-oldval="" autocomplete="off" maxlength="1000"/>
                                    <span class="blocker en-larger marl30">启用状态</span>
                                    <select id="status" name="status" tag="validate" class="form-control input-width-small" data-oldval="" autocomplete="off">
                                        <option value="0">启用</option>
                                        <option value="1">停用</option>
                                    </select>
                                    <span id="addEruptPerItem" class="marl10 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;">新增</span>
                                </div>
                            </c:if>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave">保存生效</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
