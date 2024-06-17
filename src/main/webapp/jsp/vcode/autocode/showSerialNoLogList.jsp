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
        $(function(){
            
            // 查询结果导出
            $("a.exportSerialNoLog").on("click", function(){
                $("form").attr("action", "<%=cpath%>/batchSerialNo/exportSerialNoLog.do");
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/batchSerialNo/showSerialNoLogList.do");
                
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
        .modal-body1 {
            display: block;
        }
        .checkboxImg {
            width: 80px;
            height: 20px;
            border: 1px solid #999999;
            /* background: url(answerImg.png) no-repeat center right; /自己弄一张13px下拉列表框的图标/ */
            }
            .checkboxDiv {
            width: 240px;
            padding-left:5px;
            color:white;
            position:fixed;
            margin-left:94px;
            margin-top:-10px;
            z-index:99;
            background-color: rgba(100,100,100,1);
            display: none;
            border: 1px solid #999999;
            border-top: none;
            overflow-y:auto; 
            overflow-x:auto; 
            min-height:10px;
            max-height:400px;
            min-width:200px;
                }
        .determineCls {
            width: 76px;
            height: 20px;
            line-height: 20px;
            border: 1px solid #999999;
            font-size: 12px;
            margin: 3px auto;
            border-radius: 5px;
            text-align: center;
            cursor: pointer;
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">二维码序号查询</span></div>
                   <div class="col-md-10 text-right">
                        <a class="btn btn-blue exportSerialNoLog"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 查询日志导出 </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/batchSerialNo/showSerialNoLogList.do" >
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
                                    <label class="control-label">查询时间：</label>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">二维码序号查询列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                               id="dataTable_data">
                            <thead>
                                <tr>
                                  <th style="width:4%;">序号</th>
                                  <th style="width:8%;" >二维码序号</th>
                                  <th style="width:8%;" >码源订单编码</th>
                                  <th style="width:15%;" >码源订单名称</th>
                                  <th style="width:8%;" >客户订单编号</th>
                                  <th style="width:8%;" >赋码厂</th>
                                  <th style="width:8%;" >赋码厂编码</th>
                                  <th style="width:10%;" >SKU名称</th>
                                  <th style="width:10%;" >批次编码</th>
                                  <th style="width:5%;" >原因</th>
                                  <th style="width:8%;" >查询人</th>
                                  <th style="width:12%;" >查询时间</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;"><span>${idx.count}</span></td>
                                            <td>${item.batchSerialNo}</td>
                                            <td>${item.orderNo}</td>
                                            <td>${item.orderName}</td>
                                            <td>${item.clientOrderNo}</td>
                                            <td>${item.qrcodeManufacture}</td>
                                            <td>${item.qrcodeFactoryName}</td>
                                            <td>${item.skuName}</td>
                                            <td>${item.batchDesc}</td>
                                            <td>${item.queryNotes}</td>
                                            <td>${item.queryUserPhoneNum}</td>
                                            <td>${fn:substring(item.createTime, 0, 19)}</td>
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
  </body>
</html>
