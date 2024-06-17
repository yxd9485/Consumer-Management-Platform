<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
    String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

    <script>
        $(function(){

            // 保存
            $(".grantReward").on("click", function(){
                    var url = "<%=cpath%>/batchreward/executeGrantReward.do";
                    $.fn.confirm("请仔细确认发送数据是否正确!一经上传则直接到达用户帐户，无法撤回!确认发放激励？", function(){
                        $(".grantReward").attr("disabled", "disabled");
                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
            });

            // 弹出选择发放激励文件
            $(".uploadItem").off();
            $(".uploadItem").on("click", function(){
                // 弹出对话框
                $("#expressUploadDialog input.import-file").val("");
                $("#expressUploadDialog").modal("show");
            });

            // 确定选择发放激励文件
            $("#expressUploadDialog").delegate("#addBtn", "click", function(){
                $uploadFile = $("#expressUploadDialog input.import-file");
                var files = $uploadFile.val();
                if(files == "") {
                    $.fn.alert("未选择任何文件，不能导入!");
                    return false;
                } else if(files.indexOf("xls") == -1) {
                    $.fn.alert("不是有效的EXCEL文件");
                    return false;
                }

                // 提交表单
                var url = "<%=cpath%>/batchreward/importBatchRewardRecordList.do";
                var formData = new FormData();
                formData.append("filePath", $uploadFile[0].files[0]);
                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData,
                    dataType: "json",
                    async: true,
                    contentType : false,
                    processData: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#expressUploadDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            if (data['errMsg'] == "校验通过"){
                                var content = "";
                                $.each(data['resultList'], function(i, item) {
                                    content += "<tr><td><span>"+(i+1)+"</span></td><td><span>"+item.userRole+
                                        "</span></td><td><span>"+item.userName+"</span></td><td><span>"+item.userPhone+"</span></td><td><span>"+item.userBigregion+"</span></td>" +
                                        "<td><span>"+item.userDealer+"</span></td><td><span>"+item.contractYear+"</span></td><td><span>"+item.mallVpoints+"</span></td>" +
                                        "<td><span>"+item.province+"</span></td><td><span>"+item.city+"</span></td><td><span>"+item.county+"</span></td>" +
                                        "<td><span>"+item.address+"</span></td><td><span>"+item.appointSkuSweepNum+"</span></td><td><span>"+item.rewardVpoints+"</span></td>" +
                                        "<td><span>"+item.rewardMoney+"</span></td><td><span>"+item.prizeNo+"</span></td><td><span>"+item.prizeName+"</span></td>" +
                                        "<td><span>"+item.idcard+"</span></td><td><span>"+item.excelDate+"</span></td></tr>";
                                });
                                $("#rewardDetailJson").val(JSON.stringify(data['resultList']));
                                $("#fileUrl").val(data['fileUrl']);
                                $("#rewardDetails").html(content);
                                $(".userTotal").text(data['userTotal']);
                                $(".vpointsTotal").text(data['vpointsTotal']);
                                $(".moneyTotal").text(data['moneyTotal']);
                                $(".bigPrizeTotal").text(data['bigPrizeTotal']);
                                $("#userTotal").val(data['userTotal']);
                                $("#vpointsTotal").val(data['vpointsTotal']);
                                $("#moneyTotal").val(data['moneyTotal']);
                                $("#bigPrizeTotal").val(data['bigPrizeTotal']);
                                console.log(data['resultList'])
                                $(".grantReward").show();
                            } else {
                                $(".grantReward").hide();
                            }
                        });
                    }
                });
            });

        });

    </script>
    <style>
        table.table tr th {
            text-align: center;
        }
        table.table tr td {
            vertical-align: middle;
        }

    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">批量发放激励</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: black; font-weight: bold;">批量发放激励查询</span></div>
                    <div class="col-md-10 text-right">
                        <button class="btn btn-blue uploadItem">选择文件</button>
                        <a class="btn btn-blue" href="<%=cpath%>/upload/cogFolder/${projectServerName}/批量发放激励模板.xls?v=<%=System.currentTimeMillis()%>">模板下载</a>
                        <button class="btn btn-blue grantReward" style="display: none;">确定导入</button>
                    </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="form-horizontal row-border validate_form" method="post" action="<%=cpath%>/batchreward/executeGrantReward.do">
                        <input id="rewardDetailJson" name="rewardDetailJson" type="hidden"  value="" />
                        <input id="fileUrl" name="fileUrl" type="hidden"  value="" />
                        <input id="userTotal" name="userTotal" type="hidden"  value="" />
                        <input id="vpointsTotal" name="vpointsTotal" type="hidden"  value="" />
                        <input id="moneyTotal" name="moneyTotal" type="hidden"  value="" />
                        <input id="bigPrizeTotal" name="bigPrizeTotal" type="hidden"  value="" />
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="widget-header">
                                    <i class="iconfont icon-tishi" id="bodyMsg" style="color: red; font-size: 20px;"></i></br>
                                    <span>
                                        <b>1</b>.批量导入发放激励功能，以用户ID作为会员信息归一标准。</br>
                                        <b>2</b>.批量导入只能发放激励，并不能修改用户其它信息。</br>
                                        <b>3</b>.确认导入前，务必确认<b style="color: red">A、C、M、N、O列</b>信息，此五列为关键数据信息，一经上传则直接到达用户帐户，无法撤回。</br>
                                        <b>4</b>.同一用户发放两个实物奖，需创建两行信息。</br>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: black; font-weight: bold;">激励列表</span></div>
                </div>
                <div>
                    <span style="font-size: 15px; width: 200px;height: 34px;margin-right: 0px;margin-left: 152px;">待导入数据:</span>
                    <span style="font-size: 18px;margin-left: 37px;">用户</span>
                    <span class="userTotal" style="font-size: 18px; color: red">0</span>
                    <span style="font-size: 18px;">人</span>
                    <span style="font-size: 18px;margin-left: 45px;">积分</span>
                    <span class="vpointsTotal" style="font-size: 18px; color: red">0</span>
                    <span style="font-size: 18px;margin-left: 45px;">红包</span>
                    <span class="moneyTotal" style="font-size: 18px; color: red">0</span>
                    <span style="font-size: 18px;">元</span>
                    <span style="font-size: 18px;margin-left: 45px;">实物</span>
                    <span class="bigPrizeTotal" style="font-size: 18px; color: red">0</span>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper"  style="overflow-x:scroll">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data" style="width: 2100px">
                            <thead>
                            <tr>
                                <th style="width:3%;">序号</th>
                                <th style="width:5%;">系统角色</th>
                                <th style="width:5%;" >用户姓名</th>
                                <th style="width:5%;">手机号</th>
                                <th style="width:5%;" >归属大区</th>
                                <th style="width:5%;">归属经销商</th>
                                <th style="width:5%;">合同年份</th>
                                <th style="width:5%;">商城积分</th>
                                <th style="width:5%;">省份</th>
                                <th style="width:5%;">城市</th>
                                <th style="width:5%;">区</th>
                                <th style="width:5%;">详细地址</th>
                                <th style="width:8%;">指定产品扫码件数</th>
                                <th style="width:5%;">应发积分</th>
                                <th style="width:5%;">应发金额(元)</th>
                                <th style="width:5%;">实物奖编号</th>
                                <th style="width:5%;">实物奖名称</th>
                                <th style="width:5%;">身份证号</th>
                                <th style="width:7%;">日期</th>
                            </tr>
                            </thead>
                            <tbody id="rewardDetails">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
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
                    <h6 class="del_success">删除成功</h6>
                    <h6 class="del_fail">删除失败</h6>
                    <h6 class="del_invalid">删除失败,先删除该sku关联的活动</h6>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="expressUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">批量发放激励</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">发放文件：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input type="file" class="import-file" id="expressFile" name="filePath" single style="padding-bottom: 15px; margin-left: 10px;"/>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
