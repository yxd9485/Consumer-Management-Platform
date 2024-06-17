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
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

    <script>
        $(function(){

            // 初始化界面
            initPage();
            // 弹出批次信息
            $("#addGoods").off();
            $("#addGoods").on("click", function(){
                // 弹出对话框
                $('h4').html('导入excel');
                $("#qrcodeUploadDialog input.import-file").val("");
                $("#qrcodeUploadDialog").modal("show");
                $("#addBtn").show(); 
                $("#editBtn").hide();
            });

            // 确定批次信息
            $("#qrcodeUploadDialog").delegate("#addBtn", "click", function(){
                $uploadFile = $("#qrcodeUploadDialog input.import-file");
                var files = $uploadFile.val();
                if(files == "") {
                    $.fn.alert("未选择任何文件，不能导入!");
                    return false;
                } else if(files.indexOf("xlsx") == -1 && files.indexOf("xls") == -1  ) {
                    $.fn.alert("不是有效的Excel文件");
                    return false;
                }

                // 弹出遮罩层
                $("#qrcodeFloatDiv").show();

                // 提交表单
                var url = "<%=cpath%>/questionOrderAction/importFileList.do";
                var formData = new FormData();
                formData.append("file", $uploadFile[0].files[0]);
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
                        
                        // 隐藏遮罩层
                        $("#qrcodeFloatDiv").css("display","none");
                        $("#expressUploadDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });
            });


      
        
        
        // 初始化界面
        function initPage() {
            
            // 初始化排序
            $orderCol = $("a[data-ordercol='${orderCol}']");
            $orderCol.data("ordertype", "${orderType}");
            if ("${orderType}" == "asc") {
                $orderCol.find("i.iconfont").removeClass("icon-paixu-jiangxu").addClass("icon-paixu-shengxu");
            } else {
                $orderCol.find("i.iconfont").removeClass("icon-paixu-shengxu").addClass("icon-paixu-jiangxu");
            }

        
        }

     
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
            <li class="current"><a>系统管理</a></li>
            <li class="current"><a>京东编号导入</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">京东编号查询</span></div>
                   <div class="col-md-10 text-right">
                       <c:if test="${currentUser.roleKey ne '4'}">
                           <a id="addGoods" class="btn addGoods btn-blue" href="#">
                               <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 导入
                           </a>
                       
                       </c:if>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/questionOrderAction/showQuestionOrderList.do">
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
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item">
                                    <label class="control-label">京东编号：</label>
                                    <input name="orderNo" id="orderNo" class="form-control input-width-larger" autocomplete="off" type="text"/>
                                    </div>
                               		<div class="search-item">
                                    <label class="control-label">编号类型：</label>
                                    <select name="operationType" class="form-control input-width-larger transStatus search">
                                        <option value="0">全部</option>
                                        <option value="1">未使用</option>
                                        <option value="2">已使用</option>
                                    </select>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">列表</span></div>
                 
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width: 5%">序号</th>
                                <th style="width: 20%">京东商品编号</th>
                                <th style="width: 10%">V码</th>
                                <th style="width: 10%">是否使用</th>
                                <th style="width: 15%">录入时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="result" varStatus="status">
                                        <tr>
                                            <td class="text_center" style="vertical-align: middle;">${status.count}</td>
                                            <td style="text-align:center;"><span>${result.orderNo}</span></td>
                                            <td style="text-align:center;"><span>${result.prizeVcode}</span></td>
                                            <td style="text-align:center;">
                                            	 <c:choose>
                                                    <c:when test="${not empty  result.userKey}">已使用</c:when>      
                                                    <c:otherwise>未使用</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="text-align:center;">
					        					<span>${fn:substring(result.createTime, 0, 19)}</span>
					        				</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8" style="text-align: left;">
                                            <span>查无数据！</span>
                                        </td>
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
        <div id="qrcodeFloatDiv" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
            <h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
        </div>
    
     <div class="modal fade" id="qrcodeUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content" style="top:20%;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">新增</h4>
                    </div>
                    <div class="modal-body">
                        <table class="active_board_table">
                            <tr>
                                <td width="25%" class="text-right"><label class="title">导入excel：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                        <input type="file"  class="import-file" id="batchFile" name="batchFile" single style="padding-bottom: 15px; margin-left: 10px;"/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="editBtn" class="btn btn-default do-add btn-orange" data-dismiss="">修 改</button>
                        <button type="button" id="addBtn" class="btn btn-default do-add btn-orange" data-dismiss="">确 认</button>
                        <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </body>
</html>
