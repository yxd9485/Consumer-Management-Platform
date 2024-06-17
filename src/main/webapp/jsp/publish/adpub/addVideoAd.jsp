<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_server_url");
    String allPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cpath;
    String imgUploadCpath = PropertiesUtil.getPropertyValue("image_upload_context_path");
    String loadPath = PropertiesUtil.getPropertyValue("image_default");
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>新建视频</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=4"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=cpath %>/inc/upload/jquery.imgareaselect-0.9.10/uploadify/css/uploadify.css" />
    <script type="text/javascript" src="<%=cpath %>/inc/upload/jquery.imgareaselect-0.9.10/uploadify/widgets/swfobject.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/upload/jquery.imgareaselect-0.9.10/uploadify/widgets/jquery.uploadify.v2.1.0.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/upload/jquery.imgareaselect-0.9.10/js/json2.js"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var filePath = '<%=filePath%>';

        var target = "";
        var resize = "";
        var imgSrc=[];

        // 本界面上传图片要求
        var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };

        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("form"));
            // 初始化功能
            initPage();
            updateVideo();
        });

        function initPage() {
            
            // 返回
            $(".btnReturn").click(function () {
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });

            // 保存
            $(".btnSave").on("click",function () {
                var flag = validForm();
                console.log("flag==",flag);
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

            // 触达人群类型
            $("input[name=crowdLimitType]").on("click",function(){
                if($(this).val() == '2' || $(this).val() == '3'){
                    $("#userGroupTr").css("display","");
                }else{
                    $("#userGroupTr").css("display","none");
                }
            });



            // 初始化区域事件
            $("#addArea").closest("div").initZone("<%=cpath%>", true, "", true, true, false, true);
            $("form").on("click", "#addArea", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copyArea = $(this).closest("div").clone();
                    $copyArea.initZone("<%=cpath%>", true, "", true, true, false, true);
                    $copyArea.find("#addArea").text("删除");
                    $(this).closest("td").append($copyArea);

                } else {
                    $(this).closest("div").remove();
                }
            });

            $("[name='closeType']").on("change", function () {
                if ($(this).val() == '1') {
                    $(this).closest("div").find("select[name='closeTime']").attr("disabled", "disabled").val("");
                } else {
                    $(this).closest("div").find("select[name='closeTime']").removeAttr("disabled");
                }
            });



        }
        function validForm() {
            // 检验名称是否重复
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }
            if(imgSrc.length>1){
                $.fn.alert('视频封面图片，限制上传一张');
                return false;
            }
            $("[name='videoCoverImage']").val(imgSrc[0]);
            // 获取文件列表
            var file = $('#ipt')[0].files
            // 判断是否选择了文件
            if (file.length <= 0) {
                $.fn.alert('请上传文件')
                return false;
            }
            var pathVideo = $('#pathVideo').val();
            if(!pathVideo){
                $.fn.alert("点击上传按钮");
                return false;
            }
            // 组建筛选区域
            var areaCode = "";
            var areaName = "";
            $("td.area div.area").each(function(i){
                var $province = $(this).find("select.zProvince");
                var $city = $(this).find("select.zCity");
                var $county = $(this).find("select.zDistrict");
                if ($county.val() != "") {
                    areaCode = areaCode + $county.val() + ",";
                } else if ($city.val() != "") {
                    areaCode = areaCode + $city.val() + ",";
                } else if ($province.val() != "") {
                    areaCode = areaCode + $province.val() + ",";
                } else {
                    areaCode = "";
                    areaName = "";
                    return false;
                }
                areaName = areaName + $province.find("option:selected").text() + "_"
                    + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
            });
            $("input[name='areaCode']").val(areaCode);
            $("input[name='areaName']").val(areaName);
            if(areaCode == ""){
                alert("请筛选区域！")
                return false;
            }
            return true;
        }


        function updateVideo() {
            $('#videoUpload').on('click', function() {
                // 获取文件列表
                var file = $('#ipt')[0].files
                // 判断是否选择了文件
                if (file.length <= 0) {
                    return alert('请上传文件')
                }
                // 创建formdata
                var fd = new FormData()
                // 向formdata中传入数据
                // fd.append()
                // file是一个伪数组
                fd.append('filePath', file[0])
                // 用ajax传送数据
                let url = basePath + '/actVideo/uploadVideo.do?vjfSessionId=${vjfSessionId}';
                $.ajax({
                    type: 'post',
                    url: url,
                    // 数据不需要编码
                    contentType: false,
                    // 数据对象不需要转换成键值对格式
                    processData: false,
                    data: fd,
                    beforeSend: function() {
                        $('#loading').show()
                    },
                    complete: function() {
                        $('#loading').hide()
                    },
                    success: function(res) {
                        // 判断是否接收成功
                        if (res.status == 200) {
                            $("#pathVideo").val(res.url)
                        }else{
                            $('#progress').css('width', "0%")
                            // 在进度条中显示百分比
                            $('#progress').html( '0%')
                        }
                        return $.fn.alert(res.msg)
                    },
                    xhr: function xhr() {
                        var xhr = new XMLHttpRequest()
                        // 获取文件上传的进度
                        xhr.upload.onprogress = function(e) {
                            // e.lengthComputable表示当前的进度是否是可以计算,返回布尔值
                            if (e.lengthComputable) {
                                // e.loaded表示下载了多少数据, e.total表示数据总量
                                var percentComplete = Math.ceil((e.loaded / e.total) * 100)
                                // 让进度条的宽度变化
                                $('#progress').css('width', percentComplete+"%")
                                // 在进度条中显示百分比
                                $('#progress').html(percentComplete + '%')
                            }
                        }
                        // 文件加载完成
                        xhr.upload.onload = function() {
                            $('#progress').removeClass('progress-bar progress-bar-striped').addClass('progress-bar progress-bar-success')
                        }
                        return xhr
                    }

                })

            })

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

        .upload_input {
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
            filter: alpha(opacity=0);
            /*This works in IE 8 & 9 too*/
            -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";
            /*IE4-IE9*/
            filter: progid:DXImageTransform.Microsoft.Alpha(Opacity=0);
        }
    </style>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/actVideo/save.do">
                <input type="hidden" name="videoCoverImage" value="">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>新建视频</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">标题：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="title" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="30"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="areaCode"/>
                                <input type="hidden" name="areaName"/>

                                <td class="ab_left"><label class="title">展示区域：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main area" colspan="3">
                                    <div class="content area" style="display: flex; margin: 5px 0px;">
                                        <select name="provinceAry" class="zProvince form-control input-width-normal required" tag="validate"></select>
                                        <select name="cityAry" class="zCity form-control input-width-normal required"></select>
                                        <select name="countyAry" class="zDistrict form-control input-width-normal required"></select>
                                        <label class="validate_tips title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea"></label>
                                    </div>
                                </td>



                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">展示时间：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">从</span>
                                        <input name="startTime" id="stGmt"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" style="width: 180px !important;" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd 00:00:00',  maxDate:'#F{$dp.$D(\'endGmt\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endTime" id="endGmt"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" style="width: 180px !important;" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd 23:59:59',  minDate:'#F{$dp.$D(\'stGmt\')}'})"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">精准触达人群：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='0' checked="checked"/>不限制</label>
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='1'/>黑名单用户不可领取</label>
                                        <c:if test="${not empty(groupList)}">
                                            <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='2'/>指定群组参与</label>
                                            <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='3'/>指定群组不可参与</label>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${not empty(groupList)}"> 
                                <tr id="userGroupTr" style="display: none;">    
                                    <td class="ab_left"><label class="title"></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div>
                                            <c:forEach items="${groupList }" var="group">
                                                <label class="blocker en-larger"><input name="userGroupIds" type="checkbox" value='${group.id }'/>${group.name}</label>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr class="jumpurl">
                                <td class="ab_left"><label class="title">关联商品：</label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="goodsId" class="form-control  input-width-larger ">
                                            <option value="">请选择</option>
                                            <c:forEach items="${goodsList}" var="goods" varStatus="st">
                                                <option value="${goods.goodsId}">${goods.goodsName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">视频封页上传：<span class="white">*</span><br/>建议750*421</label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" />
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
                            <input type="hidden" name="btnName" id="btnName" value="">
                            <tr>
                                <td class="ab_left"><label class="title">选择素材：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                       <input type="hidden" name="videoPath" id="pathVideo" value="" />
                                        <input type="file"  name="videoFile" id="ipt" >
                                        <div style="padding-top: 10px">
                                            <button id="videoUpload" type="button" class="btn btn-default btn-blue" style="width: 158px;">上传文件</button>
                                        </div>
                                    </div>
                                    <div style="width: 158px;">
                                        <div class="progress" style="margin-top: 10px;width: 100%;">
                                            <div id="progress" class="progress-bar progress-bar-striped active" role="progressbar"
                                                 aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 0;">
                                                0%
                                            </div>
                                        </div>
                                    </div>

                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title"><span class="required"></span></label> </td>

                                <td class="ab_main" colspan="3">
                                    <!-- 显示上传到服务器的图片 -->
                                    <video controls="controls" id="video" style="display: none">
                                        <source src=""  id="source"  type="video/mp4">
                                        你的浏览器不支持播放视频
                                    </video>
                                </td>
                            </tr>
                            <div class="modal fade" id="addBatchDialog" tabindex="-1" data-backdrop="static"
                                 aria-labelledby="myModalLabel" aria-hidden="false">
                                <div class="modal-dialog">
                                    <div class="modal-content" style="top:1%;">
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
                            <button class="btn btn-blue btnSave">确认发布</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-url="<%=cpath%>/actVideo/showVideoList.do">返 回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<script>
</script>