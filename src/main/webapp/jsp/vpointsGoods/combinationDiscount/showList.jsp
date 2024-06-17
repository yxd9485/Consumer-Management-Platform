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
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script>
        $(function(){
            // 新增
            $("#addActivity").click(function(){
                var url = "<%=cpath%>/vpointsCombinationDiscount/add.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vpointsCombinationDiscount/edit.do?cogKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 删除
            $("a.delete").off();
            $("a.delete").on("click", function(){
                var key = $(this).parents("td").data("key");
                $.fn.confirm("确认删除",function () {
                    var url = "<%=cpath%>/vpointsCombinationDiscount/delete.do?cogKey="+key;
                    $("form").attr("action", url);
                    $("form").submit();
                })

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
            <li class="current"><a> 积分商城</a></li>
            <li class="current"><a> 组合优惠管理</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">组合优惠管理</span></div>
                    <div class="col-md-10 text-right">
                        <a id="addActivity" class="btn btn-blue">
                            <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增活动
                        </a>
                    </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                          action="<%=cpath%>/vpointsCombinationDiscount/showList.do">
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
                                <div class="form-group little_distance search"  style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">活动名称：</label>
                                        <input name="name" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">活动编码：</label>
                                        <input name="cogNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">商品名称：</label>
                                        <input name="goodsName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>

                                    <div class="search-item">
                                        <label class="control-label">是否开启：</label>
                                        <label>
                                            <select name="openFlag" class="form-control input-width-larger search" autocomplete="off" >
                                                <option style="padding: 20px;" value="">全部</option>
                                                <option value="1">开启</option>
                                                <option value="0">停用</option>
                                            </select>
                                        </label>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">开始时间：</label>
                                        <input name="startTime" id="exchangeStartTime" style="width: 240px !important;" class="form-control input-width-larger Wdate search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'exchangeEndTime\')}'})" autocomplete="off" />
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">结束时间：</label>
                                        <input name="endTime" id="exchangeEndTime" style="width: 240px !important;" class="form-control input-width-larger Wdate sufTime search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'exchangeStartTime\')}'})" autocomplete="off" />
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">活动状态：</label>
                                        <label>
                                            <select name="stateFlag" class="form-control input-width-larger search" autocomplete="off" >
                                                <option style="padding: 20px;" value="">全部</option>
                                                <option value="0">未开始</option>
                                                <option value="1">进行中</option>
                                                <option value="2">已结束</option>
                                            </select>
                                        </label>
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
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">商品列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,5,9,8,8,8,8,8,8,8,8,8,8" />
                            <thead>
                            <tr>
                                <th style="width:4%;">序号</th>
                                <th style="width:10%;" data-ordercol="rule_no">活动编号</th>
                                <th style="width:8%;" data-ordercol="rule_name">活动名称</th>
                                <th  data-ordercol="start_date">商品</th>
                                <th style="width:7%;" >活动价格（元）</th>
                                <th style="width:7%;" >是否开启</th>
                                <th style="width:7%;" data-ordercol="state_flag">活动状态</th>
                                <th style="width:10%;" data-ordercol="create_time" data-orderdef>开始时间</th>
                                <th style="width:10%;" data-ordercol="create_time" data-orderdef>结束时间</th>
                                <th style="width:10%;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="result" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${result.cogNo}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${result.name}</span>
                                            </td>
                                            <td style="text-align:left;">
                                                <span>1、${result.goodsNameA}</span><br/>
                                                <span>2、${result.goodsNameB}</span><br/>
                                                <c:if test="${not empty result.goodsNameC}">
                                                    <span>3、${result.goodsNameC}</span><br/>
                                                </c:if>
                                            </td>
                                            <td style="text-align: left;">
                                                <span title=""> ${result.activityPrice} </span>
                                            </td>
                                            <td style="text-align: center;">
                                                <span title="">
                                                   <c:choose>
                                                       <c:when test="${result.openFlag eq '0'}">停用</c:when>
                                                       <c:when test="${result.openFlag eq '1'}">开启</c:when>
                                                   </c:choose>
                                                </span>
                                            </td>
                                            <td style="text-align: center;">
                                                <span title="">
                                                   <c:choose>
                                                       <c:when test="${result.stateFlag eq '0'}">未开始</c:when>
                                                       <c:when test="${result.stateFlag eq '1'}">进行中</c:when>
                                                       <c:when test="${result.stateFlag eq '2'}">已结束</c:when>
                                                   </c:choose>
                                                </span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${result.startTime}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${result.endTime}</span>
                                            </td>
                                            <td data-key="${result.cogKey}" style="text-align: center;">
                                                <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>

                                                <c:choose>
                                                    <c:when test="${result.stateFlag eq '0'}"> <a class="btn btn-xs delete btn-red"> <i class="iconfont icon-lajixiang"></i>&nbsp;删 除 </a></c:when>
                                                </c:choose>

                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="10" align="center"><span>查无数据！</span></td>
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
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="top:30%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">提示消息</h4>
                </div>
                <div class="modal-body">
                    <h6 class="addFalse">保存失败</h6>
                    <h6 class="addSuccess">保存成功</h6>
                    <h6 class="deleteFalse">删除失败</h6>
                    <h6 class="deleteSuccess">删除成功</h6>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
