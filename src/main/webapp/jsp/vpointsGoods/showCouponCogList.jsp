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
			
			// 新增
			$("#addActivity").click(function(){
				var url = "<%=cpath%>/coupon/showCouponCogAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/coupon/showCouponCogEdit.do?couponKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
            
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/coupon/doCouponCogDelete.do?couponKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
         	// 数据
			$("a.data").off();
			$("a.data").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/coupon/showCouponCogData.do?couponKey="+key;
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
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 首页</a></li>
            <li class="current"><a> 积分商城</a></li>
        	<li class="current"><a> 商城优惠券</a></li>
            <li class="current">
            	<c:choose>
            		<c:when test="${tabsFlag eq '0'}">未开始</c:when>
            		<c:when test="${tabsFlag eq '1'}">进行中</c:when>
            		<c:when test="${tabsFlag eq '2'}">已失效</c:when>
            		<c:otherwise>全部</c:otherwise>
            	</c:choose>	
            </li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable">
	        <a href="<%=cpath%>/coupon/showCouponCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=0" 
	             class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">未开始</a>
	        <a href="<%=cpath%>/coupon/showCouponCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">进行中</a>
	        <a href="<%=cpath%>/coupon/showCouponCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">已失效</a>
	        <a href="<%=cpath%>/coupon/showCouponCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3" 
	             class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">全部</a>
	    </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">商城优惠券查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addActivity" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增优惠券
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/coupon/showCouponCogList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
	                <div class="row">
			            <div class="col-md-12 ">
			                    <div class="form-group little_distance search" style="">
                                    <div class="search-item">
				                        <label class="control-label">关键字：</label>
				                        <input name="keyword" class="form-control input-width-larger" autocomplete="off" maxlength="30" placeholder="优惠券编号/名称"/>
                                    </div>
	                                <div class="search-item">
	                                    <label class="control-label">使用时间：</label>
	                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
	                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
	                                    <label class="">-</label>
	                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
	                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
	                                </div>
	                                <div class="search-item">
	                                    <label class="control-label">优惠券类型：</label>
	                                    <select name="couponType" class="form-control input-width-larger search" autocomplete="off" >
	                                        <option style="padding: 20px;" value="">全部</option>
	                                        <option style="padding: 20px;" value="0">满减券</option>
	                                        <option style="padding: 20px;" value="1">直减券</option>
	                                        <option style="padding: 20px;" value="2">折扣券</option>
	                                    </select>
	                                </div>
                                    <div class="search-item">
                                        <label class="control-label">适合类型：</label>
                                        <select name="couponGoodsType" class="form-control input-width-larger search" autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option style="padding: 20px;" value="0">通用券</option>
                                            <option style="padding: 20px;" value="1">单品券</option>
                                            <option style="padding: 20px;" value="2">指定商品可用券</option>
                                            <option style="padding: 20px;" value="3">指定商品不可用券</option>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">投放渠道：</label>
                                        <select name="couponChannel" class="form-control input-width-larger search" autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option style="padding: 20px;" value="0">商城领取</option>
                                        </select>
                                    </div>
                                    <c:if test="${tabsFlag eq '3'}">
                                    <div class="search-item">
                                        <label class="control-label">优惠券状态：</label>
                                        <select name="couponType" class="form-control input-width-larger search" autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option style="padding: 20px;" value="0">未开始</option>
                                            <option style="padding: 20px;" value="1">进行中</option>
                                            <option style="padding: 20px;" value="2">已失效</option>
                                        </select>
                                    </div>
                                    </c:if>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">优惠券列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,10,15,10,9,9,10,8,8,9,12" />
                            <thead>
					            <tr>
                                    <th style="width:4%;">序号</th>
					            	<th style="width:9%;" data-ordercol="a.coupon_no">优惠券编号</th>
					            	<th style="width:15%;" data-ordercol="a.coupon_name">优惠券名称</th>
					            	<th style="width:7%;" data-ordercol="a.coupon_type">优惠券类型</th>
					            	<th style="width:7%;" data-ordercol="a.coupon_goods_type">适用类型</th>
					            	<th style="width:7%;" data-ordercol="a.coupon_num">优惠券总数</th>
					            	<th style="width:7%;" data-ordercol="a.coupon_surplus_num">剩余数量</th>
					            	<th style="width:12%;" data-ordercol="a.receive_start_date">领取日期</th>
					            	<th style="width:12%;" data-ordercol="a.start_date">有效日期</th>
					            	<th style="width:6%;" data-ordercol="isBegin">状态</th>
					            	<th style="width:13%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="cog" varStatus="idx">
					        	<tr>
                                    <td style="text-align:center;
                                        <c:choose>
                                            <c:when test="${cog.isBegin == '0'}">background-color: #ecb010;</c:when>
                                            <c:when test="${cog.isBegin == '1'}">background-color: #ea918c;</c:when>
                                            <c:when test="${cog.isBegin == '2'}">background-color: #999999;</c:when>
                                            <c:otherwise>background-color: red;</c:otherwise>
                                        </c:choose>">
                                        <span>${idx.count}</span>
                                    </td>
					        		<td style="text-align:center;">
                                        <span>${cog.couponNo}</span>
					        		</td>
					        		<td>
					        			<span>${cog.couponName}</span>
					        		</td>
                                    <td style="text-align:center;">
                                        <c:choose>
                                            <c:when test="${cog.couponType eq '0'}">满减券</c:when>
                                            <c:when test="${cog.couponType eq '1'}">直减券</c:when>
                                            <c:when test="${cog.couponType eq '2'}">折扣券</c:when>
                                        </c:choose>
                                    </td>
                                    <td style="text-align:center;">
                                        <c:choose>
                                            <c:when test="${cog.couponGoodsType eq '0'}">通用券</c:when>
                                            <c:when test="${cog.couponGoodsType eq '1'}">单品券</c:when>
                                            <c:when test="${cog.couponGoodsType eq '2'}">指定商品可用券</c:when>
                                            <c:when test="${cog.couponGoodsType eq '3'}">指定商品不可用券</c:when>
                                        </c:choose>
                                    </td>
                                    <td style="text-align:right;">
                                        <span><fmt:formatNumber value="${cog.couponNum}" pattern="#,##0"/></span>
                                    </td>
                                    <td style="text-align:right;">
                                        <span><fmt:formatNumber value="${cog.couponSurplusNum}" pattern="#,##0"/></span>
                                    </td>
					        		<td style="text-align:center;">
					        			<span>从&nbsp;${cog.receiveStartDate}</span>
					        			<span>到&nbsp;${cog.receiveEndDate}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>从&nbsp;${cog.startDate}</span>
					        			<span>到&nbsp;${cog.endDate}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<c:choose>
                                            <c:when test="${cog.isBegin == '0'}">
                                                <span style="color: #ecb010;"><b>未开始</b></span>
                                            </c:when>
                                            <c:when test="${cog.isBegin == '1'}">
                                                <span style="color: #ea918c;"> <b>进行中</b> </span>
                                            </c:when>
                                            <c:when test="${cog.isBegin == '2'}">
                                                <span style="color: #999999;"> <b>已失效</b> </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: red;"><b>${cog.isBegin}</b></span>
                                            </c:otherwise>
                                        </c:choose>
					        		</td>
					        		<td data-key="${cog.couponKey}" style="text-align: center;">
					        			<c:if test="${currentUser.roleKey ne '4'}">
						        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
						        			<c:if test="${cog.isBegin == '0'}">
						        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
						        			</c:if>
					        			</c:if>
					        			<c:if test="${currentUser.roleKey eq '4'}">
						        			<a class="btn btn-xs edit btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;查 看</a>
					        			</c:if>
					        			<!-- <a class="btn btn-xs data btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;数 据</a> -->
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
	</div>
  </body>
</html>
