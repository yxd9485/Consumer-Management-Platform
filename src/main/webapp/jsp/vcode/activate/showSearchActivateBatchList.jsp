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
    <title>青岛啤酒活动运营后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath }/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${basePath }/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="${basePath }/inc/custom/form-actions.js"></script>
    
    <script type="text/javascript">
        $(function(){
            
            // 提交
            $(".btn-primary").on("click", function(){
                $("form").attr("action", "<%=cpath%>/searchBatchActivate/showSearchActivateBatchList.do");
                $("form").submit();
            });
            
            // 导出
            $("#export").on("click", function(){
                $("form").attr("action", "<%=cpath%>/searchBatchActivate/exportActivateBatchList.do");
                $("form").submit();
                
                // 还原查询Action
                $("form").attr("action", "<%=cpath%>/searchBatchActivate/showSearchActivateBatchList.do");
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
        
        .listTitle {
            color: white;
            line-height: 2;
            float: right;
            padding-right: 10px;
        }
        .listTitle label {
            padding-left: 30px;
        }
        #dataTable_data td{
            text-align: left;
        }
    </style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 码源管理</a></li>
            <li class="current"><a title="">箱码激活查询</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom"><div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">箱码激活查询</span></div>
                   <div class="col-md-10 text-right">
                       <c:if test="${currentUser.roleKey eq '1' and currentUser.userKey eq '2'}">
                           <a id="export" class="btn btn-blue">
                               <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 导出当前数据
                           </a>
                       </c:if>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="${basePath}/searchBatchActivate/showSearchActivateBatchList.do">
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
                                    <label class="control-label">产品名称：</label>
                                    <select name="skuKey" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <c:if test="${not empty skuList}">
                                            <c:forEach items="${skuList }" var="sku">
                                                <option value="${sku.skuKey }">${sku.skuName }</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">激活工厂：</label>
                                    <select name="activateFactoryName" class="form-control input-width-larger transStatus search" >
                                        <option value="">全部</option>
                                        <c:if test="${not empty factoryNameList}">
                                            <c:forEach items="${factoryNameList }" var="factorName">
                                                <option value="${factorName }">${factorName }
                                            </c:forEach>
                                        </c:if>
                                        <option value="运营管理平台">运营管理平台</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">批码编号：</label>
                                    <input name="batchDesc" id="batchDesc" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">激活人员：</label>
                                    <input name="activateUserName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">手 机 号 ： </label>
                                    <input name="activatePhone" class="form-control input-width-larger" autocomplete="off" maxlength="11" oninput = "value=value.replace(/[^\d]/g,'')"/>
                                </div>
                                <div class="search-item">
                                <label class="control-label">激活时间：</label>
                                   <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                       tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                   <label class="">-</label>
                                   <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                       tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">批码名称：</label>
                                    <input name="batchName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">激活状态：</label>
                                    <select name="activationStatus" class="form-control input-width-larger transStatus search">
                                        <option value="">全部</option>
                                        <option value="0" >未激活</option>
                                        <option value="1" >已激活</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">箱码激活列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                                <tr>
                                    <th style="width:5%;">序号</th>
                                    <th style="width:9%;" data-ordercol="s.sku_name">产品名称</th>
                                    <th style="width:13%;" data-ordercol="b.batch_name">批码名称/编号</th>
                                    <%--<th style="width:10%;" data-ordercol="b.batch_name">批码名称</th>--%>
                                    <th style="width:10%;" data-ordercol="b.batch_desc">辅料箱码/垛码</th>
                                    <th style="width:9%;" >批码时间</th>
                                    <th style="width:10%;" data-ordercol="(b.pack_amounts * b.qrcode_amounts)">
                                        该箱/垛码数量</th>
                                    <th style="width:8%;" data-ordercol="b.activate_factory_name">激活工厂</th>
                                    <th style="width:7%;" data-ordercol="b.activate_user_name">激活人</th>
                                    <th style="width:7%;" data-ordercol="b.activate_phone">手机号</th>
                                    <th style="width:5%;" >状态</th>
                                    <th style="width:9%;" data-ordercol="b.activate_time" data-orderdef>激活时间</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                <c:when test="${fn:length(batchInfoList) gt 0}">
                                <c:forEach items="${batchInfoList}" var="item" varStatus="idx">
                                <tr>
                                    <td style="text-align:center">
                                        <span>${idx.count}</span>
                                    </td>
                                    <td>${item.skuName}</td>
                                    <td title="${item.batchDesc}" class="dblclick">
                                        <span>${item.batchName}</span>
                                    </td>
                                  <%--  <td>${item.batchNo}</td>--%>
                                    <td>${item.batchDesc}</td>
                                    <td style="text-align:center;">
                                        <c:choose>
                                            <c:when test="${empty(item.startDate)}">
                                                <span>---</span>
                                                <c:if test="${not empty(item.endDate)}">
                                                    <br><span>到${item.endDate}</span>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <span>从${item.startDate}</span><br>
                                                <span>到${item.endDate}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="text-align: right;">${item.vcodeCounts}</td>
                                    <td>${item.activateFactoryName}</td>
                                    <td>${item.activateUserName}</td>
                                    <td>${item.activatePhone}</td>
                                    <td style="text-align:center;">
                                        <c:choose>
                                            <c:when test="${empty(item.startDate)}">
                                                <span>未激活</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span>激活<c:if test="${item.activationStatus eq '1'}">-未开始</c:if><c:if test="${item.activationStatus eq '2'}">-已开始</c:if><c:if test="${item.activationStatus eq '3'}">-已结束</c:if>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${fn:substring(item.activateTime, 0, 19)}</td>
                                </tr>
                                </c:forEach>
                                </c:when>
                                <c:otherwise>
                                <tr>
                                    <td colspan="11"><span>查无数据！</span></td>
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
