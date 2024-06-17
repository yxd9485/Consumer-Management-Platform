<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ page import="org.apache.poi.util.SystemOutLogger" %>
<%@ page import="com.dbt.vpointsshop.bean.VpointsExchangeLog" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
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


			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/doubleprize/showDoublePrizeView.do?activityKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
            // 下载积分列表
            $("a.downItem").on("click", function(){
                var userKey= $("input[name=userKey]").val()
                $("form").attr("action", "<%=cpath%>/user/exportUserVpointsList.do?type=1");
                $("form").submit();
                <c:if test="${tabsFlag eq '0'}">
                $("form").attr("action",   "<%=cpath%>/user/showUserScanList.do?tabsFlag=0"   )
                </c:if>
                <c:if test="${tabsFlag eq '1'}">
                $("form").attr("action",   "<%=cpath%>/user/showUserScanList.do?tabsFlag=1")
                </c:if>
            });
            // 兑换积分列表
            $("a.uploadItem").on("click", function(){
                var userKey= $("input[name=userKey]").val()
                $("form").attr("action", "<%=cpath%>/user/exportUserVpointsList.do?type=2");
                $("form").submit();
                <c:if test="${tabsFlag eq '0'}">
                $("form").attr("action",   "<%=cpath%>/user/showUserScanList.do?tabsFlag=0"   )
                </c:if>
                <c:if test="${tabsFlag eq '1'}">
                $("form").attr("action",   "<%=cpath%>/user/showUserScanList.do?tabsFlag=1")
                </c:if>
            });
			// 品牌查询
			$("[name='brandId']").on("change", function() {
                $("#categoryType").html("<option value=''>全部</option>");

				var brandId = $(this).val();
                var url = "<%=cpath%>/vpointsGoods/queryGoodsByBrandId.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {brandId : brandId},
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        var goodsAry = data['goodsAry'];
                        for(var goods in goodsAry) {
                            $("[name='brandId']").append("<option value='"+goods.goodsId+"'>"+goods.goodsName+"</option>");
                        }
                    }
                });
			});

			// 排序
			$("a.ordercol").on("click", function(){
				var orderType = $(this).data("ordertype");
				if ("desc" == orderType) {
					$(this).data("ordertype", "asc");
					$(this).find("i.iconfont").removeClass("icon-queren").addClass("icon-paixu-shengxu");
				} else {
                    $(this).data("ordertype", "desc");
                    $(this).find("i.iconfont").removeClass("icon-yichu").addClass("icon-paixu-jiangxu");
				}
				$("input.tableOrderCol").val($(this).data("ordercol"));
				$("input.tableOrderType").val($(this).data("ordertype"));

				// 跳转到首页
				doPaginationOrder();
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
			var queryParam = "";
			if ("${tabsFlag}" == "0") {
				queryParam = "exchangeId,brandId,goodsId,userName";
			} else if ("${tabsFlag}" == "1" || "${tabsFlag}" == "2") {
                queryParam = "exchangeId,brandId,goodsId,expressNumber,userName";
			} else if ("${tabsFlag}" == "3") {
				queryParam = "exchangeId,brandId,goodsId,expressNumber,userName,expressStatus";
			}
			var queryParamAry = queryParam.split(",");
			$.each("${queryParam}".split(","), function(i, obj) {
				$("div.form-group").find(":input[name='" + queryParamAry[i] +"']:not(:hidden)").val(obj);
			});
		}
        var isClose=true;
        function showDalig(longitude,latitude){
            if(isClose){

            	var vjfSessionId = $("#vjfSessionId").val();
                var url_="<%=cpath%>/user/showMap.do?lon="+longitude+"&lat="+latitude+"&vjfSessionId="+vjfSessionId;
                isClose=false;
                $("#mapDiv").attr("src",url_);
                $("#addBatchDialog").show();

            }else{
                $("#addBatchDialog").hide();
                isClose=true;
            }

        }
        function closeDalig(){

                $("#addBatchDialog").hide();
                isClose=true;

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
  <div id="addBatchDialog"   style="display: none;overflow:hidden !important; width: 350px;height:240px;position:
  fixed;top: 0;right: 0;z-index:5;" alt="">
      <iframe height="220" style="overflow: hidden; border-width: 0px;" width="330" id="mapDiv"  src=""></iframe>
  </div>

    <div class="container" >
    <div class="crumbs" onclick="closeDalig()">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 首页</a></li>
            <li class="current"><a> 消费者管理</a></li>
        	<li class="current"><a> 用户积分查询</a></li>
            <li class="current">
            	<c:choose>

            		<c:when test="${tabsFlag eq '0'}">获取积分</c:when>
            		<c:when test="${tabsFlag eq '1'}">兑换积分</c:when>

            	</c:choose>
            </li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable" onclick="closeDalig()">
            <!-- href="javascript:void(0);"-->
	        <a href="<%=cpath%>/user/showUserScanList.do?tabsFlag=0&userKey=${user.userKey}&vjfSessionId=${vjfSessionId}"  id ="findVoption" class="btn findVoption <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>" >获取积分</a>
	        <a href="<%=cpath%>/user/showUserScanList.do?tabsFlag=1&userKey=${user.userKey}&vjfSessionId=${vjfSessionId}"  id="exchangeVoption"  class="btn exchangeVoption <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>" >兑换积分</a>
	    </div>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row" onclick="closeDalig()">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">用户信息</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
		                  <c:if test="${tabsFlag eq '0'}">
                            <a id="downItem" class="btn downItem btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 下载获取积分详情
                            </a>
		                  </c:if>
		                  <c:if test="${tabsFlag eq '1'}">
                            <a id="uploadItem" class="btn uploadItem btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 下载兑换积分详情
                            </a>
		                  </c:if>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12" onclick="closeDalig()"><hr style="height: 2px; margin: 10px
                0px;"/></div></div>
	            <div class="widget-content form-inline" onclick="closeDalig()" >
		            <form class="listForm" method="post" action="<%=cpath%>/user/showUserScanList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
                        <input type="hidden" name="userKey" value="${user.userKey}">
	                <div class="row">
			            <div class="col-md-12 ">
		                    <div class="form-group little_distance search" style="line-height: 55px;width: 100%">
                                <table style="width: 100%">
                                    <tr>
                                        <td style="width: 50%;color: #FFFFFF;" > <span style="color: #FFFFFF; margin-left: 15%"><strong> 用户ID：</strong>${user.userKey}</span></td>
                                        <td style="width: 50%;color: #FFFFFF;" ><span style="color: #FFFFFF; margin-left: 5%"><strong> 昵称：</strong>${user.nickName}</span></td>
                                    </tr>
		                            <tr>
                                        <td style="width: 30%;color: #FFFFFF;" > <span style="color: #FFFFFF; margin-left: 15%"><strong>联系电话：</strong>${user.phoneNumber}</span></td>
                                        <td></td>
		                            </tr>
		                      </table>
		                    </div>
			            </div>
		            </div>

	                </form>

	            </div>
            </div>
            <div class="widget box" id="tab_1_1">
            	 <div class="row" onclick="closeDalig()">
                   <div class="col-md-2">	<c:choose>
                       <c:when test="${tabsFlag eq '0'}"><span style="font-size: 15px; color: white; font-weight: bold;">用户积分详情</span></c:when>
                       <c:when test="${tabsFlag eq '1'}"><span style="font-size: 15px; color: white; font-weight: bold;">用户积分列表</span></c:when>

                   </c:choose>	</div>
                   <div class="col-md-10 text-right">
                        <a class="btn btn-blue ordercol"
                           <c:if test="${tabsFlag eq '0'}">data-ordercol="a.EARN_TIME"</c:if><c:if test="${tabsFlag
                           eq '1'}">data-ordercol="e.exchange_time"</c:if> data-ordertype="desc"> <c:if test="${tabsFlag eq '0'}">积分获取时间 &nbsp;</c:if>
                            <c:if test="${tabsFlag eq '1'}">订单时间 &nbsp;</c:if><i
                                    class="iconfont icon-paixu-jiangxu"></i></a>

                   </div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
<!--                                 待发货 -->
	                            <c:if test="${tabsFlag eq '0' }">
		                            <tr>
	                                    <th style="width:5%;" onclick="closeDalig()">序号</th>
	                                    <th style="width:10%;"onclick="closeDalig()">中奖积分</th>
	                                    <th style="width:30%;"onclick="closeDalig()">中奖SKU</th>
	                                    <th style="width:15%;"onclick="closeDalig()">中奖V码</th>
	                                    <th style="width:20%;">中奖区域</th>
	                                    <th style="width:20%;"onclick="closeDalig()">积分获取时间</th>

	                                </tr>
	                            </c:if>
<!--                                 已发货 -->
	                            <c:if test="${tabsFlag eq '1' }">
                                    <tr>
                                        <th style="width:5%;">序号</th>
                                        <th style="width:8%;">订单编号</th>
                                        <th style="width:8%;">兑换品牌</th>
                                        <th style="width:13%;">兑换商品名称</th>
                                        <th style="width:8%;">商品编号</th>
                                        <th style="width:5%;">数量</th>
                                        <th style="width:7%;">单品积分</th>
                                        <th style="width:7%;">兑换积分</th>
                                        <th style="width:7%;">收件人</th>
                                        <th style="width:9%;">电话</th>
                                        <th style="width:14%;">地址</th>
                                        <th style="width:9%;">订单时间</th>

                                    </tr>
	                            </c:if>

					        </thead>
					        <tbody>
                            <!-- 获取积分 开始 -->
                            <c:if test="${tabsFlag eq '0' }">
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
                                    <td style="text-align:center;" onclick="closeDalig()">
                                        <span>${idx.count}</span>
                                    </td>
					        		<td style="text-align:right;" onclick="closeDalig()">
                                        <span><fmt:formatNumber value="${item.earnVpoints}" pattern="#,##0"/></span>
					        		</td>
					        		<td style="text-align:left;"onclick="closeDalig()">
					        			<span>${item.skuName}</span>
					        		</td>
					        		<td  style="text-align:center;" onclick="closeDalig()">
					        			<span>${item.qrcodeContent}</span>
					        		</td>

					        		<td style="text-align:left;">
					        			<span>${item.province}-${item.city}-${item.county}
                                         <i onclick="showDalig('${item.longitude}','${item.latitude}')"
                                                  style="color: #00acee"  class="iconfont icon-buoumaotubiao23"></i>
                                        </span>
					        		</td>
					        		<td  style="text-align:center;" onclick="closeDalig()">
					        			<span>${fn:substring(item.earnTime, 0, 19)}</span>
					        		</td>

					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        	  <c:choose>
					        	      <c:when test="${tabsFlag eq '0'}">
						        		<td colspan="6"><span>查无数据！</span></td>
					        	      </c:when>
					        	      <c:when test="${tabsFlag eq '1'}">
						        		<td colspan="12"><span>查无数据！</span></td>
					        	      </c:when>

					        	  </c:choose>
					        	</tr>
					        	</c:otherwise>
					        	</c:choose>
                            </c:if>
                            <!-- 获取积分 结束 -->
                            <!-- 兑换积分 开始 -->
                            <c:if test="${tabsFlag eq '1' }">
                                <c:choose>
                                    <c:when test="${fn:length(resultList) gt 0}">
                                        <c:forEach items="${resultList}" var="item" varStatus="idx">
                                            <tr>
			                                    <td style="text-align:center;">
			                                        <span>${idx.count}</span>
			                                    </td>
                                                <td style="text-align:center;">
                                                    <span>${item.exchangeId}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.brandName}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.goodsName}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.goodsClientNo}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.exchangeNum}</span>
                                                </td>
                                                <td style="text-align:right;">
                                                    <c:set var="exchangeNum" value="${item.exchangeNum}"/>
                                                    <c:set var="exchangeVpoints" value="${item.exchangeVpoints}"/>
                                                    <c:set var="userKey" value="${item.userKey}"/>

                                                    <%
                                                        String exchangeNum = pageContext.getAttribute("exchangeNum").toString();
                                                        String exchangeVpoints = pageContext.getAttribute("exchangeVpoints").toString();

                                                        int num ;
                                                        if("0".equals(exchangeNum)&&"0".equals(exchangeVpoints)){
                                                                    num=0;
                                                        }else {
                                                            num=
                                                                    Integer.parseInt(exchangeVpoints)/Integer.parseInt(exchangeNum) ;

                                                        }

                                                    %>
                                                    <span><fmt:formatNumber value="<%=num%>" pattern="#,##0"/></span>
                                                </td>
                                                <td style="text-align:right;">
                                                    <span><fmt:formatNumber value="${item.exchangeVpoints}" pattern="#,##0"/></span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.userName}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.phoneNum}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.address} </span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${fn:substring(item.exchangeTime, 0, 19)}</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <c:choose>
                                                <c:when test="${tabsFlag eq '0'}">
                                                    <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">12</c:when><c:otherwise>11</c:otherwise></c:choose>"><span>查无数据！</span></td>
                                                </c:when>
                                                <c:when test="${tabsFlag eq '1'}">
                                                    <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">14</c:when><c:otherwise>13</c:otherwise></c:choose>"><span>查无数据！</span></td>
                                                </c:when>
                                            </c:choose>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                <%
                                    List<VpointsExchangeLog> jgj= (ArrayList<VpointsExchangeLog>)request.getAttribute("resultList");           //这一步要强转
                                    System.out.println(jgj.size());
                                %>
                            </c:if>

                            <!-- 兑换积分 结束 -->
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
