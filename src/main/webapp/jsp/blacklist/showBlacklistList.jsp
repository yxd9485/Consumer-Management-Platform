<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.dbt.framework.securityauth.PermissionCode"%>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="/WEB-INF/tlds/securitytag.tld" prefix="st"%>
<!DOCTYPE html>
<% 
String cpath = request.getContextPath();
%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/jquery.uniform.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script>
        $(function(){
            
            // 切换userKey与openid的显示
            $("td.userKey").dblclick(function(){
                var title = $(this).attr("title");
                var conent = $(this).text();
                $(this).text(title);
                $(this).attr("title", conent);
                
                // openid与userkey颜色区分
                var txtColor = $(this).css("color");
                var preColor = $(this).data("preColor");
                if (preColor == undefined) {
                    $(this).css("color", "red");
                    $(this).data("preColor", txtColor);
                } else {
                    $(this).css("color", preColor);
                    $(this).data("preColor", txtColor);
                }
            });
    
            // 全选
            $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='itemCB']").prop("checked","checked");
                } else {
                    $("[name='itemCB']").prop("checked","");
                }
            });
    
            // 删除
            $("a.remove").off();
            $("a.remove").on("click", function(){
                var blacklistValue = $(this).parents("td").data("user");
                var url = "<%=cpath%>/blacklist/toDelete.do?blacklistValue="+blacklistValue;
                $.fn.confirm("确定移除？", function() {
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            // 批量移除
            $("#removeBatch").off();
            $("#removeBatch").on("click",function(){
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                })
                if (key == "") {
                    $.fn.alert("请选择要移除的黑名单用户!");
                } else {
                    var url = "<%=cpath%>/blacklist/toDelete.do?blacklistValue="+key;
                    $.fn.confirm("确定移除？", function() {
                        $("form").attr("action", url);
                        $("form").submit(); 
                    });
                }
            });
        });
    </script>
    <style type="text/css">
        table th{
            text-align: center
        }
    </style>
</head>
<body>
<div class="container">
    <!-- 面包屑begin -->
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a>首页</a></li>
            <li class="current"><a>消费者管理</a></li>
            <li class="current"><a>黑名单用户</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">黑名单用户查询</span></div>
                   <div class="col-md-10 text-right">
                       <a id="removeBatch" class="btn btn-red">
                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 批量移除
                       </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/blacklist/manage.do">
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
                                    <label class="control-label">用户ID：</label>
                                    <input name="openid" class="form-control input-width-larger" autocomplete="off" maxlength="40"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">昵称：</label>
                                    <input name="nickName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">活动名称：</label>
                                    <input name="vcodeActivityName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">手机号：</label>
                                    <input name="phoneNum" class="form-control input-width-larger" autocomplete="off" maxlength="11" oninput = "value=value.replace(/[^\d]/g,'')"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">加入原因：</label>
                                    <select name="blackReason" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option value="1">手动【不可挪出黑名单】</option>
                                        <option value="2">防黑客盗扫（1分钟内10次错误码）</option>
                                        <option value="3">防工厂盗扫用户(24小时内连续扫指定的2个SKU)</option>
                                        <option value="4">可疑自动加入</option>
                                        <option value="9">每分钟请求扫码大于60次</option>
                                        <option value="10">每分钟请求提现大于60次</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">加入时间：</label>
                                    <input name="" id="startDate" class="form-control input-width-medium Wdate search-date"
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
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">黑名单用户列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20" id="dataTable_data">
                            <thead>
                                <tr>
                                 <th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
                                 <th style="width:4%;">序号</th>
                                 <th style="width:17%;" data-ordercol="b.black_list_value">用户ID</th>
                                 <th style="width:10%;" data-ordercol="c.nick_name">昵称</th>
                                 <th style="width:10%;" data-ordercol="c.phone_number">手机号</th>
                                 <th style="width:18%;" data-ordercol="a.vcode_activity_name">活动名称</th>
                                 <th style="width:10%;" data-ordercol="b.create_time">加入原因</th>
                                 <th style="width:12%;" data-ordercol="address">加入区域</th>
                                 <th style="width:10%;" data-ordercol="b.create_user" data-orderdef>加入时间</th>
                                 <th style="width:9%;">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                <c:when test="${fn:length(blackList) gt 0}">
                                <c:forEach items="${blackList}" var="blackList" varStatus="status">
                                <tr>
                                 <td style="text-align:center;">
                                    <input name="itemCB" type="checkbox" value="${blackList.blackValue}" />
                                 </td>
                                    <td class="text_center" style="vertical-align: middle;">${status.count}</td>
                                 <td title="${blackList.openid}" class="userKey">
                                     ${blackList.blackValue}
                                 </td>
                                    <td><span>${blackList.nickName}</span></td>
                                 <td>
                                     <span>${blackList.phoneNum}</span>
                                 </td>
                                 <td>
                                     <span>${blackList.vcodeActivityName}</span>
                                 </td>
                                 <td>
                                    <c:choose>
                                        <c:when test="${blackList.creUser == '1'}">手动【不可挪出黑名单】</c:when>
                                        <c:when test="${blackList.creUser == '2'}">防黑客盗扫（1分钟内10次错误码）</c:when>
                                        <c:when test="${blackList.creUser == '3'}">防工厂盗扫用户(24小时内连续扫指定的2个SKU)</c:when>
                                        <c:when test="${blackList.creUser == '4'}">可疑自动加入</c:when>
                                        <c:when test="${blackList.creUser == '8'}">积分系统加入</c:when>
                                        <c:when test="${blackList.creUser == '9'}">每分钟请求扫码大于60次</c:when>
                                        <c:when test="${blackList.creUser == '10'}">每分钟请求提现大于60次</c:when>
                                        <c:otherwise>其它</c:otherwise>
                                    </c:choose>
                                 </td>
                                 <td style="text-align: center;">
                                    <span>${blackList.address}</span>
                                 </td>
                                 <td style="text-align: center;">
                                    <span>${fn:substring(blackList.creDate, 0, 19)}</span>
                                 </td>
                                 <td data-key="${blackList.blackKey}" data-user="${blackList.blackValue}">
                                     <a class="btn btn-xs remove btn-danger btn-red"><i class="iconfont icon-yichu" style="font-size: 14px;"></i> 移 除</a>
                                 </td>
                                </tr>
                                </c:forEach>
                                </c:when>
                                <c:otherwise>
                                <tr>
                                    <td colspan="10" style="text-align: center;">
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
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">提示消息</h4>
                </div>
                <div class="modal-body" style="height:160px; overflow-y:auto;">
                    <h6 class="addFalse">黑名单保存失败</h6>
                    <h6 class="addSuccess">黑名单保存成功</h6>
                    <h6 class="deleteFalse">黑名单删除失败</h6>
                    <h6 class="deleteSuccess">黑名删除存成功</h6>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>