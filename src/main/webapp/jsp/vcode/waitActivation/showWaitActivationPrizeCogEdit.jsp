<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String cpath = request.getContextPath();
    String allPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cpath;

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
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUpForMany.js?v=1.1.6"></script>

    <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>


    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];

        // 本界面上传图片要求
        var customerDefaults = {
            fileType: ["jpg", "png", "bmp", "jpeg"],   // 上传文件的类型
            fileSize: 1024 * 200 // 上传文件的大小 200K
        };

        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("form"));

            // 初始化功能
            initPage();

            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });

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

            // 增加SKU
            $("form").on("click", "#addSku", function () {
                isNext = false;
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    // 检验是否填写、删除已配置的选项
                    $("div .sku").each(function(){
                        if($(this).find("#skus").val() == ''){
                            alert("请完善SKU配置");
                            isNext = true;
                            return;
                        }
                        $copySku.find("#skus option[value='"+$(this).find("#skus").val()+"']").remove();
                    });
                    if(isNext) return;
                    // 如果没有可添加的sku时直接返回
                    if($copySku.find("#skus option").length == 1) return;
                    $copySku.find("#addSku").text("删除");
                    $copySku.find('select[name="skus"]').val('');
                    $(this).closest("td").append($copySku);
                } else {
                    $(this).closest("div").remove();
                }
            });

            // 初始化模板状态显示样式
            $("#status").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='status']").val("1");
                    }else{
                        $("input:hidden[name='status']").val("0");
                    }
                }
            });


            // 回显图片
            showImg();

        }


        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }

            if ($("#imgUrl").val() == ""){
                alert("激活产品图不能为空！")
                return false;
            }
            var imgUrlArr = $("#imgUrl").val().split(",");
            if (imgUrlArr.length > 1) {
                $.fn.alert("激活产品图只能上传一张");
                return false;
            }
            $('#activeImgUrl').val($("#imgUrl").val());



            var sHTML = $('.summernote').summernote('code');
            if(!sHTML || sHTML.trim()==='<p><br></p>'){
                alert("请输入说明");
                return false;
            }
            $('#prizeExplain').val(sHTML.trim());

            if (Number($("input[name='everyoneLimitNum']").val()) > 999) {
                alert("单人单日可中出最大值为999");
                return false;
            }
            if (Number($("input[name='cashPrizeEndDay']").val()) > 365) {
                alert("中出后有效天数最大值为365");
                return false;
            }

            return true;
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
// 		            console.info("上传完成");
                    var result = jQuery.parseJSON(xhr.responseText);
                    if(result["errMsg"]=="error"){
                        alert("上传失败");
                        return;
                    }
                    var srcList=result["ipUrl"].split(",");
                    for(var i = 0;i<srcList.length;i++){
                        $('.summernote').summernote('insertImage',srcList[i], srcList[i]);
                    }

                }else{
// 		        	console.info("上传失败");
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

        .validate_tips {
            padding: 8px !important;
        }

    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 新增待激活红包</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath%>/waitActivationPrize/doWaitActivationPrizeCogEdit.do">
                <input type="hidden" name="picUrl" value=""/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <input type="hidden" name="prizeExplain" id="prizeExplain" value="">
                            <input type="hidden" name="prizeKey" id="prizeKey" value="${cog.prizeKey}">
                            <input type="hidden" name="activeImgUrl" id="activeImgUrl" value="${cog.activeImgUrl}">

                        <%--                            <tr>--%>
<%--                                <td class="ab_left"><label class="title">红包类型：<span class="required">*</span></label>--%>
<%--                                </td>--%>
<%--                                <td class="ab_main" colspan="3">--%>
<%--                                    <div class="content">--%>
<%--                                        <label class="blocker en-larger" style="margin-left: 2px;"><input  type="radio" value='0' checked="checked"/>中出后有效天数</label>--%>
<%--                                    </div>--%>
<%--                                </td>--%>
<%--                            </tr>--%>
                            <tr>
                                <td class="ab_left"><label class="title">待激活红包名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="prizeName" tag="validate" value="${cog.prizeName}"
                                               class="form-control input-width-medium required" autocomplete="off" maxlength="50"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">启用状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="status" type="hidden"  value="${cog.status}"/>
                                        <input id="status" type="checkbox" data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning" <c:if test="${cog.status eq '1'}"> checked </c:if>/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">SKU：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main sku" colspan="3">
                                    <c:forEach items="${prizeSkuList}" var="prizeSku" varStatus="idx">
                                        <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                            <select class="form-control input-width-larger required" name="skus" tag="validate">
                                                <option value="">请选择SKU</option>
                                                <c:if test="${!empty skuList}">
                                                    <c:forEach items="${skuList}" var="sku">
                                                        <option value="${sku.skuKey}@@@${sku.skuName}@@@${sku.shortName}" <c:if test="${prizeSku.skuKey == sku.skuKey}"> selected</c:if>  >${sku.skuName}</option>
                                                    </c:forEach>
                                                </c:if>
                                            </select>
                                            <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addSku">${idx.count == 1 ? '新增' : '删除'}</label>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr >
                                <td class="ab_left"><label class="title">中出后有效天数：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="cashPrizeEndDay" tag="validate" value="${cog.cashPrizeEndDay}"
                                               class="form-control input-width-small required positive number integer" autocomplete="off" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">单人单日可中出：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="everyoneLimitNum" tag="validate" value="${cog.everyoneLimitNum}"
                                               class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1"  maxlength="6" />
                                        <span class="blocker en-larger">个</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">每日可中出：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="dayMoneyLimit" tag="validate" value="${cog.dayMoneyLimit}"
                                               class="form-control input-width-larger money minValue maxValue" autocomplete="off" minVal="0.01" maxVal="999999999.99"  maxlength="12" />
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">累计可中出：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="totalMoneyLimit" tag="validate" value="${cog.totalMoneyLimit}"
                                               class="form-control input-width-larger money minValue maxValue" autocomplete="off" minVal="0.01" maxVal="999999999.99"  maxlength="12" />
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">激活产品图：<span class="required">*</span><br/><span class=" blocker en-larger" style="color: red">建议尺寸：366PX*209PX</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="4">
                                            <input id="imgUrl" name="imgUrl" type="hidden" class='filevalue' value="${cog.activeImgUrl}"/>
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
                                            <p class="del-p ">您确定要删除图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span
                                                    class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动规则：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3"><div class="summernote">${cog.prizeExplain}</div></td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"
                                    data-url="<%=cpath%>/waitActivationPrize/showWaitActivationPrizeCogList.do">返 回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
