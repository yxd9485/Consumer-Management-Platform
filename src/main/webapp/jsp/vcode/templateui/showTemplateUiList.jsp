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
    <title>青啤运营后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath }/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath }/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="${basePath }/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
	        var refresh = "${refresh}";
			if(refresh != "") {
				$("div.modal-body ."+refresh+"").show();
				$("div.modal-body :not(."+refresh+")").hide();
				$("#myModal").modal("show");
			}
			
			// 生成码
			$("a.addOrder").click(function(){
				var $td = $(this).parent("td");
				var key = $td.data("key");
				var url = "${basePath}/vpsQrcodeOrder/createCode.do?templateKey=" + key;
				 $.fn.confirm("确定要生成码源吗？", function(){
					$td.html("进行中...");
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			//	 $(this).parent("td").html("进行中...");
			});
			
			// 跳转新增模板页面
			$("#addItem").click(function(){
				var url = "${basePath}/templateUi/showTemplateUiAdd.do";
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 跳转修改码源订单
			$("a.editOrder").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/templateUi/showTemplateUiEdit.do?templateKey=" + key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 重新生成码（只有失败状态才会重新生成）
			$("a.anewAddOrder").click(function(){
				var $td = $(this).parent("td");
				var key = $td.data("key");
				var url = "${basePath}/vpsQrcodeOrder/createCode.do?templateKey=" + key;
				 $.fn.confirm("确定要生成码源吗？", function(){
					$td.html("进行中...");
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			});
			
			// 删除订单
			$("a.delOrder").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/vpsQrcodeOrder/doQrcodeOrderDelete.do?templateKey=" + key;
				 $.fn.confirm("确定要撤销该订单吗？", function(){
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			});

            // 全选
            $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='itemCB']").prop("checked","checked");
                } else {
                    $("[name='itemCB']").prop("checked","");
                }
            });
            
            // 实现Collapse同时只展开一个的功能
            $(".paneltitle").on("click", function(){
                var collapse = $(this).attr("aria-controls");
                $("[id^='collapseInner']").each(function(obj){
                    if ($(this).attr("id") != collapse) {
                        $(this).removeClass("in");
                        $(this).addClass("collapse");
                    }
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
			text-align: center;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动模板管理</a></li>
            <li class="current"><a title="">模板管理</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">模板查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addItem" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增模板
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="${basePath}/templateUi/showTemplateUiList.do">
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
		                            <label class="control-label">模板名称：</label>
		                            <input name="templateName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">模板列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20" id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,9,13,10,6,7,6,5,7,8,10" />
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:16%;" data-ordercol="qr.client_order_no">模板名称</th>
					            	<th style="width:10%;" data-ordercol="qr.order_name">业务类型</th>
					            	<th style="width:10%;" data-ordercol="sku.sku_name">时间类型</th>
					            	<th style="width:11%;" data-ordercol="qr.qrcode_manufacture">开始时间</th>
					            	<th style="width:11%;" data-ordercol="qr.brewery">结束时间</th>
					            	<th style="width:8%;" data-ordercol="qr.channel_count">发布状态</th>
					            	<th style="width:12%;" data-ordercol="qr.create_time" data-orderdef>创建时间</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:18%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="template" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: left;">${template.templateName}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">
					        			<c:choose>
					        				<c:when test="${template.businessType eq '0'}">扫码返利</c:when>
					        				<c:when test="${template.businessType eq '1'}">万能签到</c:when>
					        				<c:otherwise>-</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">
					        			<c:choose>
					        				<c:when test="${template.dateType eq '0'}">每天</c:when>
					        				<c:when test="${template.dateType eq '1'}">时间段</c:when>
					        				<c:when test="${template.dateType eq '2'}">节假日</c:when>
					        				<c:otherwise>-</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: left;">${template.startDate}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">${template.endDate}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">
					        			<c:choose>
					        				<c:when test="${template.publishStatus eq '0'}">新建</c:when>
					        				<c:when test="${template.publishStatus eq '1'}">预发布</c:when>
					        				<c:when test="${template.publishStatus eq '2'}">已发布</c:when>
					        				<c:otherwise>-</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px;  text-align: center;"><span>${fn:substring(template.createTime, 0, 19)}</span></td>
					        		<c:if test="${currentUser.roleKey ne '4'}">
						        		<td id="operationTd" data-key="${template.templateKey}" data-value="">
						        			<a class="btn btn-xs editOrder btn-red"></i>&nbsp;修改</a>
						        			<a class="btn btn-xs editClone btn-red"></i>&nbsp;克隆</a>
						        			<a class="btn btn-xs editPublish btn-red"></i>&nbsp;发布</a>
						        		</td>
						        	</c:if>
					        	</tr>
					        	
					        	
					        	
					        	<tr style="background-color: white;"><td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">12</c:when><c:otherwise>11</c:otherwise></c:choose>" style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
                                   <div id="collapseInnerArea${idx.count}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${idx.count}">
                                     <div class="panel-body" style="padding:5px;">
                                     
                                         <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
                                          <thead>
                                              <tr class="success" style="background-color: #f0d2d0;">
	                                              	<th style="width:5%;">序号</th>
									            	<th style="width:60%;">活动名称</th>
									            	<th style="width:35%;">创建时间</th>
                                              </tr>
                                          </thead>
                                          <tbody>
                                          <c:choose>
                                              <c:when test="${fn:length(template.activityTemplateUiList) gt 0}">
                                                 <c:forEach items="${template.activityTemplateUiList}" var="ativityTemplateUi" varStatus="batchIdx">
                                                  <tr>
                                                  		<td style="text-align:center;">
					                                        <span>${batchIdx.count}</span>
										        		</td>
										        		<td style="text-align:center;">${ ativityTemplateUi.activityName}</td>
										        		<td style="text-align:center;"><span>${ativityTemplateUi.createTime}</span></td>
                                                  </tr>
                                                 </c:forEach>
                                              </c:when>
                                              <c:otherwise>
                                                  <tr>
                                                      <td colspan="3"><span>查无数据！</span></td>
                                                  </tr>
                                              </c:otherwise>
                                          </c:choose>
                                          </tbody>
                                         </table>
                                     </div>
                                   </div>
                                  </td></tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">9</c:when><c:otherwise>8</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
	      		<div style="padding-left: 20px; padding-top: 10px">${errorMsg}</div>
      			<div class="modal-body" style="padding: 5px;">
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
