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
                let queryNotes = $('#queryNotes').val();
                var qrcodeMakeUpUrl = "${basePath}/qrcodeMakeUp/query.do?col5=" + qrcode+"&queryNotes="+queryNotes;
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
                        $('#orderNo').empty()
                        $('#orderName').empty()
                        $('#clientOrderNo').empty()
                        $('#qrcodeManufacture').empty()
                        $('#qrcodeFactoryName').empty()
                        $('#qrcodeProductLineName').empty()
                        $('#qrcodeMachineId').empty()
                        $('#qrcodeWorkGroup').empty()
                        $('#qrcodeImg').attr("src","")
                        debugger
                        if(data.errcode==="0"){
                            $.fn.alert(data.errmsg);
                        }
                        if(data.errcode==="1"){
                            $.fn.alert(data.errmsg);
                            return
                        }
                        if(data.errcode==="2"){
                            $.fn.alert(data.errmsg);
                        }
			            $('#batchDesc').append(data.data.batchDesc)
			            $('#batchName').append(data.data.batchName)
			            $('#batchStartDate').append(data.data.batchStartDate)
			            $('#batchEndDate').append(data.data.batchEndDate)
			            $('#activityName').append(data.data.activityName)
			            $('#activityStartDate').append(data.data.activityStartDate)
			            $('#activityEndDate').append(data.data.activityEndDate)
			            $('#orderNo').append(data.data.orderNo)
			            $('#clientOrderNo').append(data.data.clientOrderNo)
			            $('#orderName').append(data.data.orderName)
			            $('#qrcodeManufacture').append(data.data.qrcodeManufacture)
			            $('#qrcodeFactoryName').append(data.data.qrcodeFactoryName)
			            $('#qrcodeProductLineName').append(data.data.qrcodeProductLineName)
			            $('#qrcodeMachineId').append(data.data.qrcodeMachineId)
			            $('#qrcodeWorkGroup').append(data.data.qrcodeWorkGroup)
			            $('#qrcodeImg').attr("src","data:image/png;base64,"+data.data.qrcodeImg)
                        $("#tab_1_1").css('display','flow-root')
                    },
                    error: function(data){

                    }
                });
            })
            
            // 查询日志
            $("#batchSerialNoLog").click(function(){
                var url = "<%=cpath%>/batchSerialNo/showSerialNoLogList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
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

        .blocker {
            float: left;
            vertical-align: middle;
            margin-right: 8px;
        }
        .approval-table > thead > tr > th, .approval-table > tbody > tr > td {
            border-right: 1px solid #e1e1e1;
            border-bottom: 1px solid #e1e1e1;
        }
        .approval-table > thead > tr > th {
            padding: 6px;
            text-align: center;
            background-color: #efefef;
        }
        .approval-table > tbody > tr > td {
            padding: 4px;
            padding-left: 1em;
        }
	</style>
  </head>
  
  <body>
    <div class="container">
        <div class="crumbs">
            <ul id="breadcrumbs" class="breadcrumb">
                <li class="current"><a> 首页</a></li>
                <li class="current"><a> 码源管理</a></li>
                <li class="current"><a title="">二维码序号查询</a></li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12 tabbable tabbable-custom">
                <div class="widget box">
                    <div class="row">
                       <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">二维码查询</span></div>
	                   <div class="col-md-10 text-right">
	                        <a id="batchSerialNoLog" class="btn btn-blue"><i class="iconfont" style="font-size: 14px;"></i>查询日志 </a>
	                   </div>
                    </div>
                    <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                    <div class="widget-content form-inline">
                        <form class="listForm" method="post" action="">
                        <div class="row">
                            <div class="col-md-11 text-center mart19">
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">序列号：</label>
                                        <input name="qrcode" id="qrcode" class="form-control input-width-larger"  autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">原因：</label>

                                        <select name="queryNotes" id="queryNotes" class="form-control input-width-larger" >
                                            <option value="二维码喷码变形">二维码喷码变形</option>
                                            <option value="二维码喷码折叠">二维码喷码折叠</option>
                                            <option value="二维码喷码重叠">二维码喷码重叠</option>
                                            <option value="二维码喷码不清晰">二维码喷码不清晰</option>
                                            <option value="二维码定位快周边有白色点">二维码定位快周边有白色点</option>
                                            <option value="二维码定位点位扭曲">二维码定位点位扭曲</option>
                                            <option value="无二维码">无二维码</option>
                                        </select>
                                    </div>
                            </div>
                        </div>
                        </div>
                        <div class="row">
                           <div class="col-md-12 text-center mart20">
                               <button type="button" id="makeUp" class="btn makeUp btn-blue" >查 询</button>
                           </div>
                        </div>
                        </form>
                    </div>
                </div>

                <div class="widget box" id="tab_1_1" style="display: none">
                    <div class="widget-header">
                        <h4><i class="icon-list-alt"></i>二维码信息</h4>
                    </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tbody>
                                <tr>
                                    <td class="ab_left"><label class="title">二维码图片：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <img id="qrcodeImg" class="blocker" src=""></img>
                                        </div>
                                    </td>
                                </tr> <tr>
                                    <td class="ab_left"><label class="title">所属批次编号：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="batchDesc" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">所属批次名称：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="batchName" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">批次开始时间：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="batchStartDate"  class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">批次结束时间：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="batchEndDate"  class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">所属活动名称：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="activityName" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">活动开始时间：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="activityStartDate" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">活动结束时间：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="activityEndDate" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">码源订单编号：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="orderNo" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">码源订单客户编号：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="clientOrderNo" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">码源订单名称：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="orderName" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">赋码厂：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="qrcodeManufacture" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">赋码厂编码：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="qrcodeFactoryName" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">产线名称：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="qrcodeProductLineName" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">设备编码：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="qrcodeMachineId" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">班组：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span id="qrcodeWorkGroup" class="blocker"></span>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20" id="dataTable_data">
                                <input type="hidden" id="screenWidth1366" value="5,9,9,13,10,6,7,6,5,7,8,10" />
                                <thead>

                                </tbody>
                            </table>
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
