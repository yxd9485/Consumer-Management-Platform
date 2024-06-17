<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% 
    String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>活动运营后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="${basePath }/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${basePath }/inc/pagination/jquery.pagination.js?v=1"></script>
    <script type="text/javascript" src="${basePath }/inc/custom/form-actions.js?v=2"></script>
      <script type="text/javascript" src="${basePath }/assets/js/libs/jquery.qrcode.min.js"></script>
    
    <script>
        $(function() {
            // 下载模板
            $("a.downloadTemplate").on("click", function () {
                $("form").attr("action", "<%=cpath%>/batchSerialQr/downTemplate.do");
                $("form").submit();
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/batchSerialQr/showbatchSerialQrList.do");

            });


            // 弹出上传
            $(".batchImport").off();
            $(".batchImport").on("click", function () {
                // 弹出对话框
                $("#uploadDialog input.import-file").val("");
                $("#uploadDialog").modal("show");
            });
            // 确定上传
            $("#uploadDialog").delegate("#addBtn", "click", function () {
                $uploadFile = $("#uploadDialog input.import-file");
                var files = $uploadFile.val();
                if (files == "") {
                    $.fn.alert("未选择任何文件，不能导入!");
                    return false;
                } else if (files.indexOf("xls") == -1 && files.indexOf("xlsx") == -1 && files.indexOf("csv") == -1) {
                    $.fn.alert("不是有效的EXCEL文件");
                    return false;
                }

                // 提交表单
                var url = "<%=cpath%>/batchSerialQr/importExcel.do";
                var formData = new FormData();
                formData.append("filePath", $uploadFile[0].files[0]);
                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData,
                    dataType: "json",
                    async: true,
                    contentType: false,
                    processData: false,
                    beforeSend: appendVjfSessionId,
                    success: function (data) {
                        $("#uploadDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function () {
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });

            });
        });

        function downloadQrcode(filePath) {
            var url = "${basePath}/batchSerialQr/downloadQrImage.do?filePath=" + filePath
                + "&vjfSessionId=" + $("#vjfSessionId").val();
            window.open(url,"_blank");
        }
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
            <li class="current"><a title="">批量二维码序号查询</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">批量二维码序号查询</span></div>
                   <div class="col-md-10 text-right">
                        <a class="btn btn-blue downloadTemplate"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 模板下载 </a>
                        <a class="btn btn-blue batchImport"><i class="iconfont icon-shangchuan" style="font-size: 14px;"></i>&nbsp; 批量查询 </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/batchSerialQr/showbatchSerialQrList.do" >
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                    <div class="row">
                        <div class="col-md-12 ">
                            <div class="form-group little_distance search" style="line-height: 35px;position: relative;">
                                <div class="search-item">
                                    <label class="control-label">上传时间：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                       <div class="col-md-12 text-center mart20">
                           <button type="button" class="btn btn-primary btn-blue">查 询</button>
                           <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
                       </div>
                    </div>
                    </form>
                </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">批量二维码序号查询列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                               id="dataTable_data">
                            <thead>
                                <tr>
                                  <th style="width:4%;">序号</th>
                                  <th style="width:8%;" >查询条数</th>
                                  <th style="width:8%;" >上传人手机号</th>
                                  <th style="width:15%;" >上传时间</th>
                                  <th style="width:15%;" >操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;"><span>${item.id}</span></td>
                                            <td>${item.queryNumber}</td>
                                            <td>${item.queryUserPhoneNum}</td>
                                            <td>${fn:substring(item.createTime, 0, 19)}</td>
                                            <td data-id="${item.id}">
                                                <a class="btn btn-xs excelDownload btn-group querying" <c:if test="${item.queryStatus ne '0'}">style ="display:none"</c:if>  >&nbsp;查询中</a>
                                                <a class="btn btn-xs excelDownload btn-blue queryEnd"  <c:if test="${item.queryStatus eq '0'}" >style ="display:none"</c:if> onclick="downloadQrcode('${item.importExcelPath}')" >&nbsp;表格下载</a>
                                                <a class="btn btn-xs qrDownload btn-blue queryEnd"  <c:if test="${item.queryStatus eq '0'}">style ="display:none"</c:if> onclick="downloadQrcode('${item.qrUrl}')" >&nbsp;码源下载</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                    <tr>
                                        <td colspan="11"><div>查无数据！</div></td>
                                    </tr>
                                    </c:otherwise>
                                    </c:choose>
                            </tbody>
                        </table>
                        <table id="pagination"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>
    <div class="modal fade" id="uploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">批量查询</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">二维码数据：<span class="required">*</span></label></td>
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
  </body>
</html>
