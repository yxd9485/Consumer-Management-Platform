<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String cpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title></title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>


    <script>

        var basePath = '<%=cpath%>';
        var imgSrc = [];

        // 本界面上传图片要求
        var customerDefaults = {
            fileType: ["jpg", "png", "bmp", "jpeg"],   // 上传文件的类型
            fileSize: 1024 * 500 // 上传文件的大小 200K
        };


        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("form"));

            // 获取选择活动类型的下拉框
            var shareTypeSelect = document.querySelector('select[name="shareType"]');

            // 添加一个事件监听器，当选择类型改变时触发
            shareTypeSelect.addEventListener('change', function () {
                var shareType = this.value;
                // 根据选择的类型显示或隐藏相关组件
                if (shareType === '0') { // 微信公众号
                    $("tr.articleJump").css("display", ""); // 显示公众号跳转链接
                    $("tr.videoJump").css("display", "none"); // 隐藏视频号相关组件
                    $("tr.videoJump input").val('')
                } else if (shareType === '1') { // 微信视频号
                    $("tr.articleJump").css("display", "none"); // 隐藏公众号跳转链接
                    $("tr.articleJump input").val('')

                    $("tr.videoJump").css("display", "");// 显示视频号相关组件
                }
            });

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
                    $("form").attr("action", url);
                    $("form").attr("onsubmit", "return true;");
                    $("form").submit();
                }
                return false;
            });


        }


        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }

            if (imgSrc.length > 1) {
                alert("通用图片最多上传1张");
                return false;
            } else if (imgSrc.length == 0) {
                alert("请选择图片上传");
                return false;
            } else {
                $("[name='picUrl']").val(imgSrc[0]);
            }

            var shareType = $("select[name='shareType']").val();
            if (shareType == '1') {
                var videoChannelId = $("input[name='videoChannelId']").val();
                if (videoChannelId.trim() == "") {
                    alert("未填写视频号ID");
                    return false;
                }
                var videoId = $("input[name='videoId']").val();
                if (videoId.trim() == "") {
                    alert("未填写视频号ID");
                    return false;
                }
            }

            return true;
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

        .image-tips {
            display: flex;
            flex-direction: column;
            height: 100%;
        }

    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 基础配置</a></li>
            <li class="current"><a> 私享活动</a></li>
            <li class="current"><a> 私享活动新增</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath%>/anniversaryShare/doAnniversaryShareAdd.do">
                <input type="hidden" name="picUrl" value=""/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>新建私享活动</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">活动类型：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="shareType" class="form-control input-width-large search">
                                            <option value="0">微信公众号</option>
                                            <option value="1">微信视频号</option>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动标题：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="shareTitle" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="30"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="articleJump">
                                <td class="ab_left"><label class="title">公众号链接：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="jumpUrl" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="150"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="videoJump" style="display: none;">
                                <td class="ab_left"><label class="title">视频号ID：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="videoChannelId" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="150"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="videoJump" style="display: none;">
                                <td class="ab_left"><label class="title">视频ID：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="videoId" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="150"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">展示时间：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">从</span>
                                        <input name="stgmt" id="stgmt"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" style="width: 180px !important;" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',  maxDate:'#F{$dp.$D(\'endgmt\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endgmt" id="endgmt"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" style="width: 180px !important;" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',  minDate:'#F{$dp.$D(\'stgmt\')}'})"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">排序<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div tag="validate" class="content">
                                        <input name="shareOrder" tag="validate"
                                               class="form-control input-width-small required number integer"
                                               autocomplete="off"
                                               value="10"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 200px;">
                                <td class="ab_left"><label class="title">选择代表图：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px; width: 400px; float: left;" class="img-box full">
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
                                    <div class="image-tips">
                                        <p style="color: red">图片尺寸：680*560</p>
                                        <p style="color: red">图片大小：500kb以内</p>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"
                                    data-url="<%=cpath%>/anniversaryShare/showAnniversaryShareList.do">返 回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
