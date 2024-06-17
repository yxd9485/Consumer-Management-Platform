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

            // 排序-有效天数,创建时间
            $("a.ordercol").on("click", function(){
                var orderType = $(this).data("ordertype");
                if ("desc" == orderType) {
                    $(this).data("ordertype", "asc");
                    $(this).find("i.iconfont").removeClass("icon-queren").addClass("icon-ypaixu-shengxu");
                } else {
                    $(this).data("ordertype", "desc");
                    $(this).find("i.iconfont").removeClass("icon-yichu").addClass("icon-paixu-jiangxu");
                }
                $("input.tableOrderCol").val($(this).data("ordercol"));
                $("input.tableOrderType").val($(this).data("ordertype"));

                // 跳转到首页
                doPaginationOrder();
            });
            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
           		var url = "<%=cpath%>/user/showUserScanList.do?userKey="+key;
           		$("input[name='pageParam']").val('');
                $("form").attr("action", url);
                $("form").submit();
            });
            // 下载订单列表
            $("a.downItem").on("click", function(){
                $("form").attr("action", "<%=cpath%>/user/exportUserVpointsList.do?type=0");
                $("form").submit();
                $("form").attr("action",   "<%=cpath%>/user/showUserVpointsList.do"   )
            });


        });

        // 初始化界面
        function initPage() {

            // 初始化排序
            $orderCol = $("a[data-ordercol='${orderCol}']");
            $orderCol.data("ordertype", "${orderType}");
            if ("${orderType}" == "asc") {
                $orderCol.find("i.iconfont").removeClass("icon-queren").addClass("icon-paixu-shengxu");
            } else {
                $orderCol.find("i.iconfont").removeClass("icon-yichu").addClass("icon-paixu-jiangxu");
            }

            // 回显查询条件
            var queryParamAry = "userKey,nickName,phoneNum,startDate,endDate".split(",");
            $.each("${queryParam}".split(","), function(i, obj) {
                $("div.form-group").find(":input[name='" + queryParamAry[i] +"']:not(:hidden)").val(obj);
            });
        }

        function imgBig(_url){
            if(!_url){
                return;
            }
            var img2 = document.querySelector('#large');
            img2.src=_url;
            img2.style.display = 'block';
        }
        function imgSmall(){
            var img2 = document.querySelector('#large');
            img2.style.display = 'none';
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
            <li class="current"><a>首页</a></li>
            <li class="current"><a>消费者管理</a></li>
            <li class="current"><a title="">用户积分查询</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">用户积分查询</span></div>
                    <div class="col-md-10 text-right">
	                    <a id="downItem" class="btn downItem btn-blue">
	                        <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 下载用户积分
	                    </a>
                    </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                          action="<%=cpath%>/user/showUserVpointsList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                        <div class="row">
                            <div class="col-md-12" style="line-height: 35px;">
                                 <div class="form-group little_distance search">
                                    <div class="search-item">
                                        <label class="control-label">用户ID：</label>
                                        <input name="userKey" class="form-control input-width-larger" 
                                               autocomplete="off" maxlength="36"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">昵称：</label>
                                        <input name="nickName" class="form-control input-width-larger" 
                                               autocomplete="off" maxlength="36"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">手机号：</label>
                                        <input name="phoneNum" class="form-control input-width-larger" autocomplete="off" maxlength="11" oninput="value=value.replace(/[^\d]/g,'')"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">首次扫码时间：</label>
                                        <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                        <label class="">-</label>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">是否参与活动：</label>
                                        <select name="inActivities" class="form-control input-width-larger search" 
                                                autocomplete="off" >
                                            <option style="padding: 20px;" value="1">是</option>
                                            <option style="padding: 20px;" value="2">否</option>
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
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">用户积分列表</span></div>
                   <%-- <div class="col-md-10 text-right">
                        <a class="btn btn-red marr20 ordercol" data-ordercol="account_vpoints" data-ordertype="desc">总积分数 &nbsp;<i class="iconfont icon-paixu-jiangxu"></i></a>
                        <a class="btn btn-red ordercol" data-ordercol="create_time" data-ordertype="desc">首次领取时间 &nbsp;<i class="iconfont icon-paixu-jiangxu"></i></a>
                    </div>--%>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:10%;"data-ordercol="vca.USER_KEY">用户ID</th>
                                <th style="width:7%;"data-ordercol="vcu.NICK_NAME">昵称</th>
                                <th style="width:9%;"data-ordercol="vcu.PHONE_NUMBER">手机号</th>
                                <th style="width:8%;"data-ordercol="vca.account_vpoints">总积分</th>
                                <th style="width:10%;">兑换积分</th>
                                <th style="width:10%;"data-ordercol="vca.SURPLUS_VPOINTS">剩余积分</th>
                                <th style="width:10%;"data-ordercol="vca.TOTAL_SCAN">扫码次数</th>
                                <th style="width:10%;"data-ordercol="vca.TOTAL_EXCHANGE_NUM">兑换次数</th>
                                <th style="width:12%;"data-ordercol="vvfs.CREATE_TIME" data-orderdef>首次领取时间</th>
                                <th style="width:8%;">操作</th>
                            </tr>
 
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td style="text-align:left;">${item.userKey}</td>
                                            <td style="text-align:left;">${item.nikeName}</td>
                                            <td style="text-align:center;">${item.phoneNum}</td>
                                            <td style="text-align:right;"><fmt:formatNumber value="${item.accountVpoints}" pattern="#,##0"/></td>
                                            <td style="text-align:right;"><fmt:formatNumber value="${item.exchangePoints}" pattern="#,##0"/></td>
                                            <td style="text-align:right;"><fmt:formatNumber value="${item.surplusVpoints}" pattern="#,##0"/></td>
                                            <td style="text-align:right;"><fmt:formatNumber value="${item.totalScan}" pattern="#,##0"/></td>
                                            <td style="text-align:right;"><fmt:formatNumber value="${item.totalExchangeNum}" pattern="#,##0"/></td>
                                            <td style="text-align:center;">${fn:substring(item.createTime, 0, 19)}</td>
                                            <td data-key="${item.userKey}" style="text-align:left">
	                                            <a id="edit" class="btn btn-xs edit btn-info btn-blue"><i class="icon-xinxi"></i> 详 情</a>
                                            </td>
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
    <div class="modal fade" id="changeLuckLevel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">设置幸运用户级别</h4>
                </div>
                <div class="modal-body">
                    <table class="lucklevel-table">
                        <tbody>
                        <tr>
                            <td style="width:20%;"></td>
                            <td style="width:80%;">
                                <span class="valid-place"></span>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:20%;">幸运用户级别：<span class="required">*</span></td>
                            <td style="width:80%;">
                                <select name = "isLuckyUser" class="form-control input-width-large lucklevel-list-value" autocomplete="off" >
                                    <option value = "0">普通用户</option>
                                    <option value = "1">幸运用户</option>
                                    <option value = "2">餐饮服务员</option>
                                    <option value = "3">扫码专员</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:20%;"></td>
                            <td style="width:80%;">
                                <span class="valid-place"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default do-add-lucklevel btn-red" data-key="" data-dismiss="">确 认</button>
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
</div>
</body>
</html>
