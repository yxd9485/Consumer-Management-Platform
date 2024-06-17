<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil" %>
<%
    String cpath = request.getContextPath();
    String allPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cpath;
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>SKU管理</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUpForMany.js?v=1.1.6"></script>

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
            $("input[name='volume']").val(thousandth($("input[name='volume']").val()));
            //容积千分符
            $("input[name='volume']").on('keyup', function () {
                $("input[name='volume']").val(thousandth($("input[name='volume']").val()));
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
                    $("input[name='volume']").val(unThousandth($("input[name='volume']").val()));
                    var url = $(this).attr("url");
                    $("form").attr("action", url);
                    $("form").attr("onsubmit", "return true;");
                    $("form").submit();
                }
                return false;
            });

            //监听产品码类型切换
            $('input[name="operationsType"]').on('change', function() {
                var operationsType = $(this).val();

                if (operationsType === '3') { // 当产品码类型为“消费者+终端一码双扫”时
                    $('.consumer-reward-group, .post-scan-group').show(); // 显示相关组件
                } else {
                    $('.consumer-reward-group, .post-scan-group').hide(); // 隐藏相关组件
                    $('.time-limit-input').hide();
                    // 清除终端获取消费者权益的选择
                    $('input[name="canGetConsumerReward"]').prop('checked', false);

                    // 清除消费者后扫权益的选择
                    $('input[name="canConsumerScanAfterTerminal"]').prop('checked', false);

                    // 将时间限制设置为0
                    $('input[name="scanAfterTimeLimit"]').val('0');

                }
            });

            // 监听消费者后扫权益的选择变化
            $('input[name="canConsumerScanAfterTerminal"]').on('change', function() {
                var canConsumerScanAfterTerminal = $(this).val();
                if (canConsumerScanAfterTerminal === '1') { // 时间限制内可获取
                    $('.time-limit-input').show();
                } else { // 不可获取
                    $('.time-limit-input').hide();
                    // 将时间限制设置为0
                    $('input[name="scanAfterTimeLimit"]').val('0');
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

            var bigImgUrl = $("#bigImgUrl").val()
            if (!bigImgUrl) {
                $.fn.alert("请上传品牌介绍页图片");
                return false;
            }

            var bigImgUrlArr = bigImgUrl.split(",")
            if (bigImgUrlArr.length > 1) {
                $.fn.alert("品牌介绍页图片只能上传一张");
                return false;
            }

            // 当产品码类型为“消费者+终端一码双扫”时，检查相关字段是否有效
            if (selectedOperationsType === '3') {
                var consumerRewardRadios = $("input[name='canGetConsumerReward']:checked").val();
                if (!consumerRewardRadios) {
                    $.fn.alert("终端获取消费者权益未选择!");
                    return false;
                }

                var consumerScanAfterTerminalRadios = $("input[name='canConsumerScanAfterTerminal']:checked").val();
                if (!consumerScanAfterTerminalRadios) {
                    $.fn.alert("消费者后扫权益未选择!");
                    return false;
                }
            }

            $('#skuLogo').val($("#imgUrl").val());
            $('#brandIntroduceUrl').val($("#bigImgUrl").val());

            var imgUrlArr = $("#imgUrl").val().split(",")
            if (imgUrlArr.length > 1) {
                $.fn.alert("sku图片只能上传一张");
                return false;
            }
            if ('${currentUser.brandCode}' == 'qingpi') {
                if ($("input[name='itemName']").val() == "" || $("input[name='itemName']").val() == null) {
                    console.log($("input[name='itemName']").val())
                    $.fn.alert("请输入item名称");
                    return false;
                }

                // 新增对产品码类型的选择校验
                var selectedOperationsType = $("input[name='operationsType']:checked").val();

                if (!selectedOperationsType) {
                    $.fn.alert("请选择一项产品码类型!");
                    return false;
                }
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
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a title="">基础配置</a></li>
            <li class="current"><a title="">产品配置</a></li>
            <li class="current"><a title="">修改产品</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/skuInfo/doSkuInfoEdit.do">
                <input type="hidden" name="queryParam" value="${queryParam}"/>
                <input type="hidden" name="pageParam" value="${pageParam}"/>
                <input type="hidden" name="skuKey" value="${skuInfo.skuKey}"/>
                <input type="hidden" name="skuLogo" id="skuLogo" value="${skuInfo.skuLogo}"/>
                <input type="hidden" name="brandIntroduceUrl" id="brandIntroduceUrl"
                       value="${skuInfo.brandIntroduceUrl}">

                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>SKU信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">SKU名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="skuName" tag="validate" value="${skuInfo.skuName}"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="50"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">SKU简称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="shortName" tag="validate" value="${skuInfo.shortName}"
                                               class="form-control input-width-larger required varlength"
                                               autocomplete="off" data-length="20" maxlength="20"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">69码：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="commodityCode" tag="validate" value="${skuInfo.commodityCode}"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">品牌统一编码：<span
                                        class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="unificationCode" value="${skuInfo.unificationCode}"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">品牌统一名称：<span
                                        class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="unificationName" value="${skuInfo.unificationName}"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="50"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">产品类型：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                     <select name="skuType" tag="validate"
                                             class="form-control input-width-larger required">
                                             <c:choose>
                                                 <c:when test="${currentUser.projectServerName eq 'zuijiu' }">
	                                               <option value="0" <c:if test="${skuInfo.skuType eq '0'}">selected</c:if>>瓶码</option>
	                                               <option value="2" <c:if test="${skuInfo.skuType eq '2'}">selected</c:if>>箱码</option>
                                                 </c:when>
                                                 <c:when test="${currentUser.projectServerName eq 'mengniu' or currentUser.projectServerName eq 'mengniuzhi' }">
	                                               <option value="2" <c:if test="${skuInfo.skuType eq '2'}">selected</c:if>>箱码</option>
	                                               <option value="4" <c:if test="${skuInfo.skuType eq '4'}">selected</c:if>>支码</option>
	                                               <option value="3" <c:if test="${skuInfo.skuType eq '3'}">selected</c:if>>其他</option>
                                                 </c:when>
                                                 <c:otherwise>
	                                               <option value="0" <c:if test="${skuInfo.skuType eq '0'}">selected</c:if>>瓶码</option>
	                                               <option value="1" <c:if test="${skuInfo.skuType eq '1'}">selected</c:if>>罐码</option>
	                                               <option value="2" <c:if test="${skuInfo.skuType eq '2'}">selected</c:if>>箱码</option>
	                                               <option value="3" <c:if test="${skuInfo.skuType eq '3'}">selected</c:if>>其他</option>
                                                 </c:otherwise>
                                             </c:choose>
                                     </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            
                            <tr <c:if test="${empty skuInfo.skuChannel}"> style="display: none" </c:if>>
                                <td class="ab_left"><label class="title">所属渠道：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select class="form-control input-width-larger required" name="channel"
                                                id="skuChannel" tag="validate">
                                            <option value="">请选择渠道</option>
                                            <c:if test="${!empty skuInfo.skuChannel}">
                                                <c:forEach items="${skuInfo.skuChannel}" var="sku">
                                                    <option value="${sku.skuChannelCode}"<c:if
                                                            test="${sku.skuChannelCode eq skuInfo.channel}"> selected="selected" </c:if>>${sku.skuChannelName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">年份：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="skuYear" tag="validate"
                                                class="form-control input-width-larger required">
                                            <option value="">请选择</option>
                                            <option value="2016" <c:if
                                                    test="${skuInfo.skuYear eq 2016}"> selected="selected" </c:if>>2016
                                            </option>
                                            <option value="2017" <c:if
                                                    test="${skuInfo.skuYear eq 2017}"> selected="selected" </c:if>>2017
                                            </option>
                                            <option value="2018" <c:if
                                                    test="${skuInfo.skuYear eq 2018}"> selected="selected" </c:if>>2018
                                            </option>
                                            <option value="2019" <c:if
                                                    test="${skuInfo.skuYear eq 2019}"> selected="selected" </c:if>>2019
                                            </option>
                                            <option value="2020" <c:if
                                                    test="${skuInfo.skuYear eq 2020}"> selected="selected" </c:if>>2020
                                            </option>
                                            <option value="2021" <c:if
                                                    test="${skuInfo.skuYear eq 2021}"> selected="selected" </c:if>>2021
                                            </option>
                                            <option value="2022" <c:if
                                                    test="${skuInfo.skuYear eq 2022}"> selected="selected" </c:if>>2022
                                            </option>
                                            <option value="2023" <c:if
                                                    test="${skuInfo.skuYear eq 2023}"> selected="selected" </c:if>>2023
                                            </option>
                                            <option value="2024" <c:if
                                                    test="${skuInfo.skuYear eq 2024}"> selected="selected" </c:if>>2024
                                            </option>
                                            <option value="2025" <c:if
                                                    test="${skuInfo.skuYear eq 2025}"> selected="selected" </c:if>>2025
                                            </option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">品牌：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="skuBrand" tag="validate"
                                                class="form-control input-width-larger required">
                                            <option value="">请选择</option>
                                            <c:choose>
                                                <c:when test="${currentUser.projectServerName eq 'hgquqi'}">
                                                    <option value="Danisa" <c:if
                                                            test="${skuInfo.skuBrand eq 'Danisa'}"> selected="selected" </c:if>>
                                                        Danisa
                                                    </option>
                                                </c:when>
                                                <c:when test="${currentUser.projectServerName eq 'yuyuan'}">
                                                    <option value="梨膏露" <c:if
                                                            test="${skuInfo.skuBrand eq '梨膏露'}"> selected="selected" </c:if>>
                                                        梨膏露
                                                    </option>
                                                </c:when>
                                                <c:when test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}">
                                                    <option value="随变" <c:if
                                                            test="${skuInfo.skuBrand eq '随变'}"> selected="selected" </c:if>>
                                                        随变
                                                    </option>
                                                    <option value="绿色心情" <c:if
                                                            test="${skuInfo.skuBrand eq '绿色心情'}"> selected="selected" </c:if>>
                                                        绿色心情
                                                    </option>
                                                    <option value="蒂兰圣雪" <c:if
                                                            test="${skuInfo.skuBrand eq '蒂兰圣雪'}"> selected="selected" </c:if>>
                                                        蒂兰圣雪
                                                    </option>
                                                    <option value="冰+" <c:if
                                                            test="${skuInfo.skuBrand eq '冰+'}"> selected="selected" </c:if>>
                                                        冰+
                                                    </option>
                                                </c:when>
                                                <c:when test="${currentUser.projectServerName eq 'zhongLJH'}">
                                                    <option value="其他" <c:if
                                                            test="${skuInfo.skuBrand eq '其他'}"> selected="selected" </c:if>>
                                                        其他
                                                    </option>
                                                    <option value="玖" <c:if
                                                            test="${skuInfo.skuBrand eq '玖'}"> selected="selected" </c:if>>
                                                        玖
                                                    </option>
                                                </c:when>
                                                <c:when test="${currentUser.projectServerName eq 'zhongLWX'}">
                                                    <option value="桑干" <c:if
                                                            test="${skuInfo.skuBrand eq '桑干'}"> selected="selected" </c:if>>
                                                        桑干
                                                    </option>
                                                    <option value="五星" <c:if
                                                            test="${skuInfo.skuBrand eq '五星'}"> selected="selected" </c:if>>
                                                        五星
                                                    </option>
                                                    <option value="华夏" <c:if
                                                            test="${skuInfo.skuBrand eq '华夏'}"> selected="selected" </c:if>>
                                                        华夏
                                                    </option>
                                                    <option value="天赋" <c:if
                                                            test="${skuInfo.skuBrand eq '天赋'}"> selected="selected" </c:if>>
                                                        天赋
                                                    </option>
                                                    <option value="海岸" <c:if
                                                            test="${skuInfo.skuBrand eq '海岸'}"> selected="selected" </c:if>>
                                                        海岸
                                                    </option>
                                                    <option value="其他" <c:if
                                                            test="${skuInfo.skuBrand eq '其他'}"> selected="selected" </c:if>>
                                                        其他
                                                    </option>
                                                </c:when>
                                                <c:when test="${currentUser.projectServerName eq 'zuijiu'}">
                                                    <option value="最酒" <c:if
                                                            test="${skuInfo.skuBrand eq '最酒'}"> selected="selected" </c:if>>
                                                        最酒
                                                    </option>
                                                    <option value="其他" <c:if
                                                            test="${skuInfo.skuBrand eq '其他'}"> selected="selected" </c:if>>
                                                        其他
                                                    </option>
                                                </c:when>


                                                <c:otherwise>
		                                            <option value="经典" <c:if test="${skuInfo.skuBrand eq '经典'}"> selected="selected" </c:if>>经典</option>    
		                                            <option value="奥古特" <c:if test="${skuInfo.skuBrand eq '奥古特'}"> selected="selected" </c:if>>奥古特</option>    
		                                            <option value="全麦白啤" <c:if test="${skuInfo.skuBrand eq '全麦白啤'}"> selected="selected" </c:if>>全麦白啤</option>    
		                                            <option value="崂山" <c:if test="${skuInfo.skuBrand eq '崂山'}"> selected="selected" </c:if>>崂山</option>    
		                                            <option value="纯生" <c:if test="${skuInfo.skuBrand eq '纯生'}"> selected="selected" </c:if>>纯生</option>    
		                                            <option value="精致绿罐" <c:if test="${skuInfo.skuBrand eq '精致绿罐'}"> selected="selected" </c:if>>精致铝罐</option>
		                                            <option value="冰醇" <c:if test="${skuInfo.skuBrand eq '冰醇'}"> selected="selected" </c:if>>冰醇</option>    
		                                            <option value="红金" <c:if test="${skuInfo.skuBrand eq '红金'}"> selected="selected" </c:if>>红金</option>    
		                                            <option value="尊品纯生" <c:if test="${skuInfo.skuBrand eq '尊品纯生'}"> selected="selected" </c:if>>尊品纯生</option>    
		                                            <option value="青岛2000" <c:if test="${skuInfo.skuBrand eq '青岛2000'}"> selected="selected" </c:if>>青岛2000</option>    
		                                            <option value="清爽" <c:if test="${skuInfo.skuBrand eq '清爽'}"> selected="selected" </c:if>>清爽</option>    
		                                            <option value="淡爽" <c:if test="${skuInfo.skuBrand eq '淡爽'}"> selected="selected" </c:if>>淡爽</option>    
		                                            <option value="金麦" <c:if test="${skuInfo.skuBrand eq '金麦'}"> selected="selected" </c:if>>金麦</option>    
		                                            <option value="银麦" <c:if test="${skuInfo.skuBrand eq '银麦'}"> selected="selected" </c:if>>银麦</option>
		                                            <option value="欢动" <c:if test="${skuInfo.skuBrand eq '欢动'}"> selected="selected" </c:if>>欢动</option>
		                                            <option value="皮尔森" <c:if test="${skuInfo.skuBrand eq '皮尔森'}"> selected="selected" </c:if>>皮尔森</option>  
		                                            <option value="干啤" <c:if test="${skuInfo.skuBrand eq '干啤'}"> selected="selected" </c:if>>干啤</option>  
		                                            <option value="汉斯" <c:if test="${skuInfo.skuBrand eq '汉斯'}"> selected="selected" </c:if>>汉斯</option>  
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${currentUser.projectServerName eq 'shandongagt'}">
                                                <option value="趵突泉" <c:if
                                                        test="${skuInfo.skuBrand eq '趵突泉'}"> selected="selected" </c:if>>
                                                    趵突泉
                                                </option>
                                            </c:if>
                                            
                                            <c:if test="${currentUser.projectServerName ne 'mengniu' and currentUser.projectServerName ne 'zuijiu' and !fn:startsWith(currentUser.projectServerName, 'zhongL')}">
	                                            <option value="1903" <c:if test="${skuInfo.skuBrand eq '1903'}"> selected="selected" </c:if>>1903</option>
	                                            <option value="国潮" <c:if test="${skuInfo.skuBrand eq '国潮'}"> selected="selected" </c:if>>国潮</option>
	                                            <option value="新主流" <c:if test="${skuInfo.skuBrand eq '新主流'}"> selected="selected" </c:if>>新主流</option>  
	                                            <option value="超高端" <c:if test="${skuInfo.skuBrand eq '超高端'}"> selected="selected" </c:if>>超高端</option>
		                                    </c:if>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <c:choose>
                                <c:when test="${currentUser.projectServerName eq 'hgquqi'}">
                                    <tr>
                                        <td class="ab_left"><label class="title">重量：<span
                                                class="required">*</span></label></td>
                                        <td class="ab_main" colspan="3">
                                            <div class="content">
                                                <input name="volume" tag="validate" value="${skuInfo.volume}"
                                                       class="form-control input-width-larger thousand required"
                                                       oninput="value=value.replace(/[\u4e00-\u9fa5]/ig,'')"
                                                       autocomplete="off" maxlength="9"/>
                                                <span class="blocker en-larger">g</span>
                                                <label class="validate_tips"></label>
                                            </div>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td class="ab_left"><label class="title">容积：<c:if test="${!fn:startsWith(currentUser.projectServerName, 'mengniu')}">
                                            <span class="required">*</span> </c:if></label></td>
                                        <td class="ab_main" colspan="3">
                                            <div class="content">
                                                <input name="volume" tag="validate" value="${skuInfo.volume}"
                                                       class="form-control input-width-larger thousand <c:if test="${!fn:startsWith(currentUser.projectServerName, 'mengniu')}">required</c:if>"
                                                       autocomplete="off" maxlength="9"/>
                                                <span class="blocker en-larger">ml</span>
                                                <label class="validate_tips"></label>
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            <tr>
                                <td class="ab_left"><label class="title">建议零售价：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="suggestPrice" tag="validate" value="${skuInfo.suggestPrice }"
                                               class="form-control number money input-width-small rule required"
                                               autocomplete="off" maxlength="9"/>
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${currentUser.projectServerName eq 'zhongLCNY'}">
                                <tr>
                                    <td class="ab_left"><label class="title">促销激励：<span
                                            class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="skuPromotionMoney" tag="validate"
                                                   value="${skuInfo.skuPromotionMoney }"
                                                   class="form-control number money input-width-small rule required"
                                                   autocomplete="off" maxlength="9"/>
                                            <span class="blocker en-larger">元</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="ab_left"><label class="title">有效天数：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="validDay" tag="validate" value="${skuInfo.validDay}"
                                               class="form-control input-width-larger number positive required"
                                               autocomplete="off" maxlength="4"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">批次编码：<span
                                        class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="autocodePrefix" tag="validate" value="${skuInfo.autocodePrefix}"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="20"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">推送日报：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">

                                        <input type="radio" class="tab-radio doubtRadioMoney" name="pushMsgFlag"
                                               value="0"
                                               <c:if test="${skuInfo.pushMsgFlag ne '1'}">checked</c:if>
                                               style="float:left; cursor: pointer;"/>
                                        <span class="blocker en-larger">不推送</span>
                                        <input type="radio" class="tab-radio doubtRadioScore" name="pushMsgFlag"
                                               value="1"
                                               <c:if test="${skuInfo.pushMsgFlag eq '1'}">checked</c:if>
                                               style="float:left; cursor: pointer;"/>
                                        <span class="blocker en-larger">推送</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${currentUser.brandCode eq 'qingpi'}">
	                            <tr>
	                                <td class="ab_left"><label class="title">合同内产品<span class="required">*</span></label>
	                                </td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input type="radio" class="tab-radio doubtRadioMoney" name="contractFlag" value="1" <c:if test="${skuInfo.contractFlag eq '1'}">checked</c:if> style="float:left; cursor: pointer;"/>
	                                        <span class="blocker en-larger">是</span>
	                                        <input type="radio" class="tab-radio doubtRadioMoney" name="contractFlag" value="0" <c:if test="${skuInfo.contractFlag eq '0'}">checked</c:if> style="float:left; cursor: pointer;"/>
	                                        <span class="blocker en-larger">否</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
                            </c:if>
                            <c:if test="${zjSwitchFlag eq '1'}">
                                <tr>
                                    <td class="ab_left"><label class="title">最酒SKU主键：<span
                                            class="white">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="zjSku" tag="validate" value="${skuInfo.zjSku}"
                                                   class="form-control input-width-larger" autocomplete="off"
                                                   maxlength="36"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${jhSwitchFlag eq '1'}">
                                <tr>
                                    <td class="ab_left"><label class="title">嘉华SKU主键：<span
                                            class="white">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="jhSku" tag="validate" value="${skuInfo.jhSku}"
                                                   class="form-control input-width-larger" autocomplete="off"
                                                   maxlength="36"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${mztSwitchFlag eq '1'}">
                                <tr>
                                    <td class="ab_left"><label class="title">码中台SKU主键：<span class="white">*</span></label>
                                    </td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="mztSku" tag="validate" value="${skuInfo.mztSku}"
                                                   class="form-control input-width-larger" autocomplete="off"
                                                   maxlength="36"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${acMztSwitchFlag eq '1'}">
                                <tr>
                                    <td class="ab_left"><label class="title">码智联SKU主键：<span
                                            class="white">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="acMztSku" tag="validate" value="${skuInfo.acMztSku}"
                                                   class="form-control input-width-larger" autocomplete="off"
                                                   maxlength="500"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${currentUser.projectServerName ne 'zhongLWX' && currentUser.projectServerName ne 'zuijiu'}">
                                <tr>
                                    <td class="ab_left"><label class="title">终端平台SKU主键：<span
                                            class="white">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="zdSku" tag="validate" value="${skuInfo.zdSku}"
                                                   class="form-control input-width-larger" autocomplete="off"
                                                   maxlength="500"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>

                            <c:if test="${currentUser.brandCode ne 'qingpi'}">
                                <input type="hidden" name="operationsType" value="1" />
                            </c:if>
                            <c:if test="${currentUser.brandCode eq 'qingpi'}">
                                <tr>
                                    <td class="ab_left"><label class="title">item名称：<span class="required">*</span></label>
                                    </td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="itemName" tag="validate" value="${skuInfo.itemName}"
                                                   class="form-control input-width-larger required" autocomplete="off"
                                                   maxlength="36"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">产品码类型：<span class="required">*</span></label>
                                    </td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input type="radio" class="tab-radio " name="operationsType"
                                                   <c:if test="${skuInfo.operationsType eq '1'}">checked</c:if>
                                                   value="1" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">仅消费者促销活动</span>
                                            <input type="radio" class="tab-radio " name="operationsType"
                                                   <c:if test="${skuInfo.operationsType eq '2'}">checked</c:if>
                                                   value="2" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">仅终端促销活动</span>
                                            <input type="radio" class="tab-radio " name="operationsType"
                                                   <c:if test="${skuInfo.operationsType eq '3'}">checked</c:if>
                                                   value="3" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">消费者+终端一码双扫</span>
                                            <input type="radio" class="tab-radio " name="operationsType"
                                                   <c:if test="${skuInfo.operationsType eq '4'}">checked</c:if>
                                                   value="4" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">无促销活动</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="consumer-reward-group" <c:if test="${skuInfo.operationsType ne '3'}">style="display:none;"</c:if>>
                                    <td class="ab_left"><label class="title">终端获取消费者权益：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input type="radio" class="tab-radio " name="canGetConsumerReward"
                                                   <c:if test="${skuInfo.canGetConsumerReward eq '0'}">checked</c:if>
                                                   value="0" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">不可获取</span>
                                            <input type="radio" class="tab-radio " name="canGetConsumerReward"
                                                   <c:if test="${skuInfo.canGetConsumerReward eq '1'}">checked</c:if>
                                                   value="1" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">可获取</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="post-scan-group" <c:if test="${skuInfo.operationsType ne '3'}">style="display:none;"</c:if>>
                                    <td class="ab_left"><label class="title">消费者后扫权益：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input type="radio" class="tab-radio " name="canConsumerScanAfterTerminal"
                                                   <c:if test="${skuInfo.canConsumerScanAfterTerminal eq '0'}">checked</c:if>
                                                   value="0" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">不可获取</span>
                                            <input type="radio" class="tab-radio " name="canConsumerScanAfterTerminal"
                                                   <c:if test="${skuInfo.canConsumerScanAfterTerminal eq '1'}">checked</c:if>
                                                   value="1" style="float:left; cursor: pointer;"/>
                                            <span class="blocker en-larger">时间限制内可获取</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="time-limit-input" <c:if test="${skuInfo.operationsType ne '3' or skuInfo.canConsumerScanAfterTerminal eq '0'}">style="display:none;"</c:if>>
                                    <td class="ab_left"><label class="title">时间限制：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="scanAfterTimeLimit" value="${skuInfo.scanAfterTimeLimit}"
                                                   class="form-control input-width-medium number integer required" autocomplete="off" maxlength="6" tag="validate"/>
                                            <span class="blocker en-larger">分钟  (默认0时不限制)</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>

                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">图片上传：<span class="white">*</span><br/></label>
                                </td>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="4">
                                            <input id="imgUrl" name="prizeImgAry" type="hidden" class='filevalue'
                                                   value="${skuInfo.skuLogo}"/>
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
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">品牌介绍页:<span
                                        class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="4">
                                            <input id="bigImgUrl" name="prizeImgAry" type="hidden" class='filevalue'
                                                   value="${skuInfo.brandIntroduceUrl}"/>
                                            <div class="z_photo upimg-div clear"
                                                 style="overflow: hidden;height: auto;"
                                                 id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value=""
                                                           accept="image/jpg,image/jpeg,image/png,image/bmp"
                                                           multiple/>
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
                                <td class="ab_left"></td>
                                <td class="ab_main" colspan="3">
                                    <div style="color: green;">
                                        解释：<br>
                                        <c:if test="${!fn:startsWith(currentUser.projectServerName, 'mengniu')}">
                                        瓶装：默认填写180天<br>
                                        灌装：默认填写365天<br>
                                        </c:if>
                                        SKU有效期 = 批次有效截止日期 - 批次激活日期（该配置主要适用于批次扫码激活功能，请认真填写）<br>
                                        根据需要可以填写其他天数
                                        批次编码 = 用在码源订单的，只能填写数字和字母
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>
                            <button class="btn btnReturn btn-radius3" data-url="<%=cpath%>/skuInfo/showSkuInfoList.do">返
                                回
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