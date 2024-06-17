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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
            
         	// 初始化
			$(":checkbox").prop("checked", true);
			
			// 弹出新增核销记录窗口
            $("a.addItem").off();
            $("a.addItem").on("click", function(){
                // 弹出对话框
                $("#verificationDialog input[name]:not(:checkbox)").val("");
                $("#verificationDialog").modal("show");
            });

            // 新建核销表
            $.runtimeValidate($("#verificationDialog"));
            $("#verificationDialog").delegate("#addBtn", "click", function(){
                // 输入元素校验
                var validateResult = $.submitValidate($("#verificationDialog"));
                if(!validateResult){
                    return false;
                }
                
                var url = "<%=cpath%>/verificationDetail/showPreviewVerificationDetailList.do";
                var verificationEndDate = $("input[name='verificationEndDate']").val();
                var brandKeys = "";
                $("input[name='brandKey']:checked").each(function(){
                	brandKeys = brandKeys + $(this).attr("value") + ",";
                });
                
                if(verificationEndDate == '' || brandKeys == ''){
                	$.fn.alert("核销日期和品牌不能为空");
                	return false;
                }
                brandKeys = brandKeys.substring(0, brandKeys.length-1);
                url = url + "?verificationEndDate=" + verificationEndDate + "&brandKeys=" + brandKeys;
                $("form").attr("action", url);
                $("form").submit();
            });
            
            // 表格下载
            $("a.download").off();
            $("a.download").on("click", function(){
                var key = $(this).closest("td").data("key");
                var url = "<%=cpath%>/verificationDetail/exportVerificationDetailList.do?verificationId=" + key;
                $("form").attr("action", url);
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/verification/showVerificationInfoList.do");
            });
            
            // 预览确认
            $("a.view").off();
            $("a.view").on("click", function(){
                var key = $(this).closest("td").data("key");
                var url = "<%=cpath%>/verificationDetail/showVerificationDetailList.do?verificationId=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });
            
            // 终止核销
            $("a.termination").off();
            $("a.termination").on("click", function(){
            	var key = $(this).closest("td").data("key");
            	var url = "<%=cpath%>/verification/updateVerificationStatus.do?verificationId=" + key + "&status=2";
            	$.fn.confirm("确认终止该条核销记录？", function(){
	                $("form").attr("action", url);
	                $("form").submit();
            	});
            });
            
            $("#checkboxAll").on("click",function(){
            	if ($(this).prop("checked")) {
            		$(":checkbox").prop("checked", true);
            	} else {
            		$(":checkbox").prop("checked", false);
            	}

            });
			
			// 排序
			$("a.ordercol").on("click", function(){
                $("input.tableOrderCol").val($(this).data("ordercol"));
                $("input.tableOrderType").val($(this).data("ordertype") == "desc" ? "asc" : "desc");
				
				// 跳转到首页
				doPaginationOrder();
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
        	<li class="current"><a> 核销管理</a></li>
        	<li class="current"><a> 核销列表</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">核销查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${verificationFlag eq '1'}">
	                       <a class="btn btn-blue addItem">
	                       <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新建核销表
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/verification/showVerificationInfoList.do">
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
			                        <label class="control-label">核销编号：</label>
			                        <input name="verificationID" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">核销日期：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                <div class="search-item">
	                                <label class="control-label">核销状态：</label>
                                    <select name="statuS" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option style="padding: 20px;" value="0">待核销</option>
                                        <option style="padding: 20px;" value="1">已核销</option>
                                        <option style="padding: 20px;" value="2">终止核销</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">核销列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
	                            <tr>
		                            <th style="width:10%;" data-ordercol="verification_id">核销编号</th>
		                            <th style="width:7%;" data-ordercol="goods_type_num">商品种类</th>
		                            <th style="width:7%;" data-ordercol="goods_num">商品总数</th>
		                            <th style="width:11%;" data-ordercol="total_vpoints">兑换总积分</th>
		                            <th style="width:11%;" data-ordercol="total_money">商品总价（元）</th>
		                            <th style="width:13%;" data-ordercol="start_date">核销日期</th>
		                            <th style="width:8%;" data-ordercol="status">核销状态</th>
		                            <th style="width:13%;" data-ordercol="create_time"data-orderdef>创建时间</th>
		                            <th style="width:20%;">操作</th>
		                        </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
						        	<c:forEach items="${resultList}" var="item" varStatus="idx">
							        	<tr>
							        		<td style="text-align:center;">
		                                        <span>${item.verificationId}</span>
							        		</td>
							        		<td style="text-align:center;">
							        			<span>${item.goodsTypeNum}</span>
							        		</td>
							        		<td style="text-align: right;">
							        			<span><fmt:formatNumber value="${item.goodsNum}" pattern="#,##0"/></span>
							        		</td>
							        		<td style="text-align: right;">
							        			<span><fmt:formatNumber value="${item.totalVpoints}" pattern="#,##0"/></span>
							        		</td>
							        		<td style="text-align: right;">
							        			<span><fmt:formatNumber value="${item.totalMoney}" pattern="#,##0.00"/></span>
							        		</td>
							        		<td style="text-align:center;">
							        			<span>${item.startDate}&nbsp;-&nbsp;${item.endDate}</span>
							        		</td>
		                                    <td style="text-align:center;">
		                                        <c:choose>
		                                            <c:when test="${item.status == '0'}">
		                                                <span>待核销</span>
		                                            </c:when>
		                                            <c:when test="${item.status == '1'}">
		                                                <span>已核销</span>
		                                            </c:when>
		                                            <c:when test="${item.status == '2'}">
		                                                <span>终止核销</span>
		                                            </c:when>
		                                        </c:choose>
		                                    </td>
							        		<td style="text-align:center;">
							        			<span>${fn:substring(item.createTime, 0, 19)}</span>
							        		</td>
							        		<td data-key="${item.verificationId}" style="text-align: center;">
							        			<a class="btn btn-xs download btn-red"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp;表格下载</a>
							        			<c:if test="${item.status == '0'}">
								        			<a class="btn btn-xs view btn-red"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;预览确认</a>
								        			<c:if test="${verificationFlag eq '1'}">
									        			<a class="btn btn-xs termination btn-red"><i class="iconfont icon-zhongzhi" style="font-size: 14px;"></i>&nbsp;终止核销</a>
								        			</c:if>
							        			</c:if>
							        		</td>
							        	</tr>
						        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
						        	<tr>
	                                    <td colspan="9"><span>查无数据！</span></td>
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
    <div class="modal fade" id="verificationDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">新建核销表</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">截止时间：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="verificationEndDate" class="form-control input-width-medium Wdate"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">选择品牌：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                	<div><input type="checkbox" id="checkboxAll"/>全选</div>
                                    <c:if test="${not empty(brandLst)}">
                                    	<c:forEach items="${brandLst }" var="item" varStatus="idx">
                                    		<span style="width: 90px; display: block; float: left;">
                                    			<input name="brandKey" type="checkbox" value="${item.brandId }" style="cursor: pointer;"/> ${item.brandName }
                                    		</span>
                                    		<c:if test="${idx.index ne 0 and (idx.index + 1) % 4 == 0}"></br></c:if>
                                    	</c:forEach>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right" style="vertical-align: top; padding-top: 10px;"><label class="title">提示：<span class="required">*</span></label></td>
                            <td>
                                <ol style="margin-left: -15px; padding-top: 10px;">
								  <li>核销过的商品数据，不会重复统计。</li>
								  <li>此表会下载截止该时间段内，所有未核销过的商品汇总和明细信息。</li>
								</ol>
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
	</div>
  </body>
</html>
