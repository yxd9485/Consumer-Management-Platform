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
            // 重新生成码（只有失败状态才会重新生成）
            $("#exportExcel").click(function(){
                $.fn.confirm("确定要导出吗？", function(){
                    $("form").attr("action", "<%=cpath%>/activatePrizeRecord/exportExcel.do");
                    var submit = $("form").submit();
                    // 还原查询action
                    $("form").attr("action", "<%=cpath%>/activatePrizeRecord/showQueryList.do");
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
            <li class="current"><a> 积分商城</a></li>
            <li class="current"><a> 分享裂变中出记录</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">查询条件</span></div>
                    <div class="col-md-10 text-right">
                        <a id="exportExcel" class="btn btn-blue">
                            <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 名单下载
                        </a>
                    </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
                          action="<%=cpath%>/activatePrizeRecord/showQueryList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                        <input type="hidden" name="activityKey" value="${activityKey}" />
	                <div class="row">
			            <div class="col-md-12 ">
                            <div class="form-group little_distance search"  style="line-height: 35px;">
                                <div class="search-item">
                                    <label class="control-label">userID：</label>
                                    <input name="userKey" class="form-control input-width-larger" autocomplete="off" maxlength="50"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">昵称：</label>
                                    <input name="nickName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">二维码：</label>
                                    <input name="winQrcodeContent" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">中出状态：</label>
                                    <label>
                                        <select name="moneyFlag" class="form-control input-width-larger search" autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">未激活</option>
                                            <option value="1">已激活</option>
                                        </select>
                                    </label>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">中出时间：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                           tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                           tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">中出活动：</label>
                                    <input name="ruleName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">获得方式：</label>
                                    <label>
                                        <select name="obtainFlag" class="form-control input-width-larger search" autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">主动获得</option>
                                            <option value="1">被动获得</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">中出记录列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,5,9,8,8,8,8,8,8,8,8,8,8" />
                            <thead>
                            <tr>
                                <th style="width:4%;">序号</th>
                                <th style="width:8%;" >userID</th>
                                <th style="width:6%;" >昵称</th>
                                <th style="width:6%;" >二维码</th>
                                <th style="width:10%;" >中出sku</th>
                                <th style="width:10%;" >中出活动</th>
                                <th style="width:4%;" >状态</th>
                                <th style="width:6%;" >中出金额</th>
                                <th style="width:6%;">中出积分</th>
                                <th style="width:6%;">奖励金额</th>
                                <th style="width:6%;">奖励积分</th>
                                <th style="width:6%;" >分享人</th>
                                <th style="width:6%;" >获得方式</th>
                                <th style="width:10%;" >中出时间</th>
                                <th style="width:10%;" >结束时间</th>
                            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="activity" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.userKey}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.nickName}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.winQrcodeContent}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.winSkuName}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.ruleName}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span><c:if test="${activity.moneyFlag eq '0'}">未激活</c:if>
                                                       <c:if test="${activity.moneyFlag eq '1'}">已激活</c:if></span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.winningAmount}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.winningPoint}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.rewardAmount}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.rewardPoint}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.shareUserName}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>
                                                    <c:choose>
		                                                <c:when test="${activity.obtainFlag eq '0'}">
		                                                    主动获得
		                                                </c:when>
		                                                <c:when test="${activity.obtainFlag eq '1'}">
		                                                    被动获得
		                                                </c:when>
                                                        <c:otherwise>主动获得</c:otherwise>
		                                            </c:choose>
                                                </span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span><fmt:formatDate pattern="yyyy年MM月dd日 HH:mm:ss" value="${activity.winDate}" /></span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span><fmt:formatDate pattern="yyyy年MM月dd日 HH:mm:ss" value="${activity.winEndTime}" /></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="14"><span>查无数据！</span></td>
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
