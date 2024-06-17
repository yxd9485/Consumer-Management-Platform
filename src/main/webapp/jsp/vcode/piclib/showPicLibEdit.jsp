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
            if(imgSrc.length==0){
                alert("请上传图片");
                return false;
            }
            var a = imgSize[imgSize.length -1];
            if(a >= 1024 * 1024 * 0.5){
                $.fn.alert("素材不能超过500k");
                return false;
            }
            if (imgSrc.length > 2) {
                $.fn.alert("广告图最多上传1张");
                return false;
            } else {
                $("[name='picUrl']").val(imgSrc[0]);
            }

            return true;
        }

        function initPage() {
            $("#isDefault").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='isDefault']").val("1");
                    }else{
                        $("input:hidden[name='isDefault']").val("0");
                    }
                }
            });
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
            var imgData='${picLibrary.picUrl}';
            if(imgData){
                showImg(imgData);
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
    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">素材库</a></li>
            <li class="current"><a title="">上传素材</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/picLib/doPicLibEdit.do">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>素材信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="picUrl" id="picUrl">
                        <input type="hidden" name="infoKey" value="${picLibrary.infoKey}">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">素材名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="picName" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="100"  style="width:250px;" value="${picLibrary.picName}">
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">默认模板：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content firstRebate">
                                        <select name="picTemplate"  class="form-control required"  tag="validate" autocomplete="off"  style="width:250px;"/>
                                            <option value="0"  <c:if test="${picLibrary.picTemplate eq '0'}"> selected="selected"</c:if>>2020通用版</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">所属品牌：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content firstRebate">
                                        <select name="picBrandType"  class="form-control required"  tag="validate" autocomplete="off" style="width:250px;"/>
                                        <c:forEach items="${picBrandMap}" var="brand">
                                            <option value="${brand.key}" <c:if test="${picLibrary.picBrandType eq brand.key}"> selected="selected"</c:if>>${picBrandMap[brand.key]}</option>
                                        </c:forEach>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">所属分组：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content firstRebate">
                                        <select name="picGroup" class="form-control required"  tag="validate" autocomplete="off"  style="width:250px;"/>
                                            <option value=""  <c:if test="${picLibrary.picGroup eq ''}"> selected="selected"</c:if>>请选择</option>
                                            <option value="1"  <c:if test="${picLibrary.picGroup eq '1'}"> selected="selected"</c:if>>LOGO</option>
                                            <option value="2"  <c:if test="${picLibrary.picGroup eq '2'}"> selected="selected"</c:if>>SLOGAN</option>
                                            <option value="3"  <c:if test="${picLibrary.picGroup eq '3'}"> selected="selected"</c:if>>背景图</option>
                                            <option value="4"  <c:if test="${picLibrary.picGroup eq '4'}"> selected="selected"</c:if>>红包样式</option>
                                            <option value="5"  <c:if test="${picLibrary.picGroup eq '5'}"> selected="selected"</c:if>>品牌产品图</option>
                                            <option value="6"  <c:if test="${picLibrary.picGroup eq '6'}"> selected="selected"</c:if>>提示图</option>
                                            <option value="7"  <c:if test="${picLibrary.picGroup eq '7'}"> selected="selected"</c:if>>按钮样式</option>
                                            <option value="8"  <c:if test="${picLibrary.picGroup eq '8'}"> selected="selected"</c:if>>弹窗广告</option>
                                            <option value="9"  <c:if test="${picLibrary.picGroup eq '9'}"> selected="selected"</c:if>>首页轮播</option>
                                            <option value="10" <c:if test="${picLibrary.picGroup eq '10'}"> selected="selected"</c:if>>商城轮播</option>
                                            <option value="11" <c:if test="${picLibrary.picGroup eq '11'}"> selected="selected"</c:if>>活动规则</option>
                                            <option value="12" <c:if test="${picLibrary.picGroup eq '12'}"> selected="selected"</c:if>>弹窗跳转图片</option>
                                            <option value="13" <c:if test="${picLibrary.picGroup eq '13'}"> selected="selected"</c:if>>实物奖</option>
                                            <option value="14" <c:if test="${picLibrary.picGroup eq '14'}"> selected="selected"</c:if>>阶梯弹窗</option>
                                            <option value="15" <c:if test="${picLibrary.picGroup eq '15'}"> selected="selected"</c:if>>终端实物奖</option>
                                            <option value="16" <c:if test="${picLibrary.picGroup eq '16'}"> selected="selected"</c:if>>活动专区</option>
                                            <option value="17" <c:if test="${picLibrary.picGroup eq '17'}"> selected="selected"</c:if>>N元乐购实物奖</option>
                                            <option value="18" <c:if test="${picLibrary.picGroup eq '18'}"> selected="selected"</c:if>>公告图</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">素材分辨率：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="picWidth" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="10" placeholder = "宽" value="${picLibrary.picWidth}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" style="width:250px;"/>
                                        <label class="validate_tips"></label>
                                        <input name="picHeight" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="10"  placeholder = "高" value="${picLibrary.picHeight}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"  style="width:250px;"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">素材坐标：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="picX" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="10"  placeholder = "坐标x" value="${picLibrary.picX}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"  style="width:250px;"/>
<%--                                        <label class="validate_tips"></label>--%>
                                        <input name="picY" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="10" placeholder = "坐标y" value="${picLibrary.picY}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"  style="width:250px;"/>
<%--                                        <label class="validate_tips"></label>--%>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">坐标起点：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content firstRebate">
                                        <select name="startPoint" class="form-control required"  tag="validate" autocomplete="off" >
                                            <option value=""  <c:if test="${picLibrary.startPoint eq ''}"> selected="selected"</c:if>>请选择</option>
                                            <option value="1"  <c:if test="${picLibrary.startPoint eq '1'}"> selected="selected"</c:if>>左下</option>
                                            <option value="2"  <c:if test="${picLibrary.startPoint eq '2'}"> selected="selected"</c:if>>左上</option>
                                            <option value="3"  <c:if test="${picLibrary.startPoint eq '3'}"> selected="selected"</c:if>>右下</option>
                                            <option value="4"  <c:if test="${picLibrary.startPoint eq '4'}"> selected="selected"</c:if>>右上</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">图片上传：<span class="white">*</span><br/>图片质量不能大于500kb<br/><a href="https://tinypng.com/" target="_blank"  style="text-decoration:underline;color: #b81919">请务必点此进行图片压缩</a></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp,image/gif" multiple/>
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
                            <tr>
                                <td class="ab_left"><label class="title">设为默认图片<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="isDefault" type="hidden" value=${picLibrary.isDefault} />
                                        <input id="isDefault" type="checkbox"  data-size="small" data-on-text="是" data-off-text="否" data-on-color="success" data-off-color="warning"
                                                <c:if test="${picLibrary.isDefault eq '1'}"> checked </c:if>/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/picLib/showPicLibList.do?group=">返 回</button>
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
