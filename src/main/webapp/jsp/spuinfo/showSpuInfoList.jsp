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
			
			// 新增
			$("#addSpu").click(function(){
				var url = "<%=cpath%>/spuInfo/showSpuInfoAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 跳转修改SPU
			$("a.editSpu").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/spuInfo/showSpuInfoEdit.do?spuInfoKey=" + key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 删除SPU
			$("a.delSpu").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/spuInfo/doSpuInfoDelete.do?spuInfoKey=" + key;
				 $.fn.confirm("确定要删除SPU吗？", function(){
					$("form").attr("action", url);
					$("form").submit(); 
				 });
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
  	<div id="divId" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
  		<h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
  	</div>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 基础配置</a></li>
            <li class="current"><a title="">SPU配置</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">SPU查询</span></div>
                   <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addSpu" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
	                       </a>
                       </c:if>
	               </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="${basePath}/spuInfo/showSpuInfoList.do">
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
		                        <label class="control-label">SPU名称：</label>
		                        <input name="spuName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                               </div>
                               <div class="search-item">
		                        <label class="control-label">SPU编号：</label>
		                        <input name="spuCode" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                               </div>
                               <div class="search-item">
		                        <label class="control-label">SKU69码：</label>
		                        <input name="commodityCode" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                               </div>
                               <div class="search-item">
		                        <label class="control-label">SKU名称：</label>
		                        <input name="skuName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">SPU列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,9,13,10,6,7,6,5,7,8,10" />
                            <thead>
					            <tr>
                                      <th style="width:5%;">序号</th>
                                      <th style="width:15%;">SPU编码</th>
                                      <th style="width:40%;">SPU名称</th>
                                      <th style="width:10%;">下属SKU</th>
                                      <th style="width:15%;">创建时间</th>
	                                  <c:if test="${currentUser.roleKey ne '4'}">
	                                  <th style="width:15%;">操作</th>
	                                  </c:if>
	                              </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="spuInfo" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.index + 1}</span>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">${spuInfo.spuCode}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">${spuInfo.spuName}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">${fn:length(spuInfo.skuList)}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px;  text-align: center;"><span>${fn:substring(spuInfo.createTime, 0, 19)}</span></td>
					        		<c:if test="${currentUser.roleKey ne '4'}">
						        		<td id="operationTd" data-key="${spuInfo.spuInfoKey}" data-value="">
						        			<a class="btn btn-xs editSpu btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
						        			<a class="btn btn-xs delSpu btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
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
									            	<th style="width:30%;">69码</th>
									            	<th style="width:35%;">SKU名称</th>
									            	<th style="width:15%;">有效天数</th>
									            	<th style="width:15%;">产品类型</th>
                                              </tr>
                                          </thead>
                                          <tbody>
                                          <c:choose>
                                              <c:when test="${fn:length(spuInfo.skuList) gt 0}">
                                                 <c:forEach items="${spuInfo.skuList}" var="skuInfo" varStatus="skuIdx">
                                                  <tr>
                                                  		<td style="text-align:center;">
					                                        <span>${skuIdx.count}</span>
										        		</td>
										        		<td>${ skuInfo.commodityCode}</td>
										        		<td>${ skuInfo.skuName}</td>
										        		<td><span>${skuInfo.validDay}</span></td>
										        		<td>
										        			<span>
																<c:choose>
											        		         <c:when test="${skuInfo.skuType eq '0'}">瓶码</c:when>
											        		         <c:when test="${skuInfo.skuType eq '1'}">罐码</c:when>
											        		         <c:when test="${skuInfo.skuType eq '2'}">箱码</c:when>
											        		         <c:when test="${skuInfo.skuType eq '3'}">其他</c:when>
											        		     </c:choose>
															</span>
														</td>
                                                  </tr>
                                                 </c:forEach>
                                              </c:when>
                                              <c:otherwise>
                                                  <tr>
                                                      <td colspan="5"><span>查无数据！</span></td>
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
					        		<td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">6</c:when><c:otherwise>5</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
