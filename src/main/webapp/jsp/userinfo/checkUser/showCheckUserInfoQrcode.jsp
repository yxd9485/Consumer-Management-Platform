<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加一万一批次激活人员</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath}/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript" src="${basePath}/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${basePath}/assets/js/custom/giftspack/utils.js"></script>
    <script type="text/javascript" src="${basePath }/assets/js/libs/jquery.qrcode.min.js"></script>
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

        .en-larger2 {
            margin-left: 12px;
        }

        .ex-larger {
            margin-right: 8px;
        }

        .validate_tips {
            padding: 8px !important;
        }

        div b {
            color: red;
        }
    </style>
    <script>
        $(function () {
            $("#code").attr('src', outputQRCod("${serverName}", 280, 280));

        });

        //生成二维码
        function outputQRCod(txt, width, height) {
            //先清空
            $("#qrcode").empty();
            //中文格式转换
            var str = toUtf8(txt);
            //生成二维码
            $("#qrcode").qrcode({
                render: "canvas",//canvas和table两种渲染方式
                width: width,
                height: height,
                text: str
            });
        }

        function toUtf8(str) {
            var out, i, len, c;
            out = "";
            len = str.length;
            for (i = 0; i < len; i++) {
                c = str.charCodeAt(i);
                if ((c >= 0x0001) && (c <= 0x007F)) {
                    out += str.charAt(i);
                } else if (c > 0x07FF) {
                    out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
                    out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                } else {
                    out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
                    out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
                }
            }
            return out;
        }
    </script>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 大奖核销人员管理</a></li>
            <li class="current"><a title="">核销人员注册</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" align="center">
                <div class="widget-header" style="width: 50%;">
                    <h4><i class="icon-list-alt"></i>注册二维码</h4>
                    <h4 id="" style="float: right; margin-top: 10px; cursor: pointer;">
                        <a href="${basePath}/checkUser/showCheckUserInfoList.do?vjfSessionId=${vjfSessionId}">返回</a>
                    </h4>
                </div>
                <div class="widget-content panel no-padding" style="width: 50%;">
                    <div style="width: 100%;">
                        <%--				     	<img style="width: 300px; hight:300px; display: block; margin: auto;" align="center" src="${basePath }/upload/checkuser/${serverName}_register.png">--%>
                        <div id="qrcode" style="width: 100px;">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
