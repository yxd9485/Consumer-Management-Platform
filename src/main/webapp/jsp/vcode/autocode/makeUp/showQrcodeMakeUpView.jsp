<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
String pathPrefix = cpath + "/" + imagePathPrx;
%>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>青啤运营后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath }/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath }/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="${basePath }/inc/custom/form-actions.js?v=2"></script>
	<script>
		$(function(){
            $("#tab_1_1").css('display','none')
	        var refresh = "${refresh}";
			if(refresh != "") {
				$("div.modal-body ."+refresh+"").show();
				// $("div.modal-body :not(."+refresh+")").hide();
				$("#myModal").modal("show");
			}
			$('#makeUp').click(function () {
                let qrcode = $('#qrcode').val();
                var qrcodeMakeUpUrl = "${basePath}/qrcodeMakeUp/makeUp.do?qr=" + qrcode;
                $("#makeUp").attr({disabled:'disabled'})
                $.ajax({
                    type: "POST",
                    url: qrcodeMakeUpUrl,
                    dataType: "json",
                    async: true,
                    contentType : false,
                    processData: false,
                    beforeSend:appendVjfSessionId,
                    success: function(data) {
                        $("#makeUp").removeAttr('disabled')
                        $('#batchDesc').empty()
                        $('#batchName').empty()
                        $('#batchStartDate').empty()
                        $('#batchEndDate').empty()
                        $('#activityName').empty()
                        $('#activityStartDate').empty()
                        $('#activityEndDate').empty()
                        if(data.errcode==="0"){
                            $.fn.alert(data.errmsg);
                            return
                        }
                        if(data.errcode==="1"){
                            $.fn.alert(data.errmsg);
                            return
                        }
			            $('#batchDesc').append(data.data.batchDesc)
			            $('#batchName').append(data.data.batchName)
			            $('#batchStartDate').append(data.data.batchStartDate)
			            $('#batchEndDate').append(data.data.batchEndDate)
			            $('#activityName').append(data.data.activityName)
			            $('#activityStartDate').append(data.data.activityStartDate)
			            $('#activityEndDate').append(data.data.activityEndDate)
                        $("#tab_1_1").css('display','flow-root')
                    },
                    error: function(data){

                    }
                });
            })
		 });
	</script>
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
			text-align: center;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
        <div class="crumbs">
            <ul id="breadcrumbs" class="breadcrumb">
                <li class="current"><a> 首页</a></li>
                <li class="current"><a> 码源管理</a></li>
                <li class="current"><a title="">二维码补录</a></li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12 tabbable tabbable-custom">
                <div class="widget box">
                    <div class="row">
                       <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">二维码补录</span></div>
                    </div>
                    <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                    <div class="widget-content form-inline">
                        <form class="listForm" method="post" action="">
                        <div class="row">
                            <div class="col-md-11 text-center mart19">
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">二维码：</label>
                                        <input name="qrcode" id="qrcode" class="form-control input-width-larger"  autocomplete="off" maxlength="30"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                           <div class="col-md-12 text-center mart20">
                               <button type="button" id="makeUp" class="btn makeUp btn-blue" >录 入</button>
                           </div>
                        </div>
                        </form>
                    </div>
                </div>

                <div class="widget box" id="tab_1_1" style="display: none">
                    <div class="row">
                        <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">参与活动详情</span></div>
                    </div>
                    <div class="widget-content">
                        <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20" id="dataTable_data">
                                <input type="hidden" id="screenWidth1366" value="5,9,9,13,10,6,7,6,5,7,8,10" />
                                <thead>
                                <tr>
                                    <th style="width:8%;" data-ordercol="qr.order_no">所属批次编号</th>
                                    <th style="width:8%;" data-ordercol="qr.client_order_no">批次名称</th>
                                    <th style="width:8%;" data-ordercol="qr.order_name">批次开始时间</th>
                                    <th style="width:8%;" data-ordercol="sku.sku_name">批次结束时间</th>
                                    <th style="width:8%;" data-ordercol="qr.qrcode_manufacture">所属活动名称</th>
                                    <th style="width:8%;" data-ordercol="qr.brewery">活动开始时间</th>
                                    <th style="width:8%;" data-ordercol="qr.channel_count">活动结束时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td id="batchDesc" class="paneltitle" data-toggle="collapse" data-parent="#accordionRule1" href="#collapseInnerArea1" aria-expanded="false" aria-controls="collapseInnerArea1" style="cursor: pointer;  border-top-width: 0px; text-align: center;"></td>
                                        <td id="batchName" class="paneltitle" data-toggle="collapse" data-parent="#accordionRule1" href="#collapseInnerArea1" aria-expanded="false" aria-controls="collapseInnerArea1}" style="cursor: pointer;  border-top-width: 0px; text-align: center;"></td>
                                        <td id="batchStartDate" class="paneltitle" data-toggle="collapse" data-parent="#accordionRule1" href="#collapseInnerArea1" aria-expanded="false" aria-controls="collapseInnerArea1" style="cursor: pointer;  border-top-width: 0px; text-align: center;"></td>
                                        <td id="batchEndDate" class="paneltitle" data-toggle="collapse" data-parent="#accordionRule1" href="#collapseInnerArea1" aria-expanded="false" aria-controls="collapseInnerArea1" style="cursor: pointer;  border-top-width: 0px; text-align: center;"></td>
                                        <td id="activityName" class="paneltitle" data-toggle="collapse" data-parent="#accordionRule1" href="#collapseInnerArea1" aria-expanded="false" aria-controls="collapseInnerArea1" style="cursor: pointer;  border-top-width: 0px; text-align: center;"></td>
                                        <td id="activityStartDate" class="paneltitle" data-toggle="collapse" data-parent="#accordionRule1" href="#collapseInnerArea1" aria-expanded="false" aria-controls="collapseInnerArea1" style="cursor: pointer;  border-top-width: 0px; text-align: center;"></td>
                                        <td id="activityEndDate" class="paneltitle" data-toggle="collapse" data-parent="#accordionRule1" href="#collapseInnerArea1" aria-expanded="false" aria-controls="collapseInnerArea1" style="cursor: pointer;  border-top-width: 0px; text-align: center; "></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal" name="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content" style="top:30%;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
                    </div>
                    <div style="padding-left: 20px; padding-top: 10px">${errorMsg}</div>
                    <div class="modal-body" style="padding: 5px;">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="qrcodeFloatDiv" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
            <h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
        </div>
    </div>
  </body>
</html>
