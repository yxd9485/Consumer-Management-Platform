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
	  <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>


	  <script>
		var activityTicketLimit =  '';
		$(function(){
			var flag = "${flag}";
			if(flag != "") {
				$("div.modal-body ."+flag+"").show();
				$("div.modal-body :not(."+flag+")").hide();
				$("#myModal").modal("show");
			}


			$("select[name='ticketNo']").on("change",function(){
				checkOption($(this).val());
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

			// 新增
			$("#addActivity").click(function(){
				var url = "<%=cpath%>/vcodeTicketActivity/showTicketActivityAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeTicketActivity/showTicketActivityEdit.do?vcodeActivityKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});

			var activityKey = "";

			// 弹出导入面板
            $("a.import").off();
            $("a.import").on("click", function(){
            	activityKey = $(this).parents("td").data("key");
            	var ticketlimit = $(this).parents("td").data("ticketlimit");
            	activityTicketLimit = ticketlimit;
            	var sysTicketInfoGro = $(this).parents("td").data("value");
            	if(sysTicketInfoGro != ''){
            		console.log(sysTicketInfoGro)
            		$("select[name='ticketNo']").empty();
            		var ticketNo,ticketName,ticketType,selectedFlag;
            		var ticketInfos = new Array();
            		ticketInfos = sysTicketInfoGro.split(";");
            		$(ticketInfos).each(function(i){
            			ticketNo = ticketInfos[i].split(":")[0];
            			ticketName = ticketInfos[i].split(":")[1];
            			ticketType = ticketInfos[i].split(":")[2];
            			if(i == 0){
            				selectedFlag = "selected=\"selected\"";
            				checkOption(ticketNo);
            			}else{
            				selectedFlag = "";
            			}
            			$("select[name='ticketNo']").append("<option value='"+ticketNo+"' "+selectedFlag+">"+ticketName + ticketType+"</option>");
            		});
            	}
                // 弹出对话框
                $('h4').html('优惠券新增');
                $("#qrcodeUploadDialog input.import-file").val("");
                $("#qrcodeUploadDialog").modal("show");
                $("#addBtn").show();
                $("#editBtn").hide();
            });

            // 确定导入
            $("#qrcodeUploadDialog").delegate("#addBtn", "click", function(){

            	var ticketNo = $("#qrcodeUploadDialog select[name='ticketNo']").val();
            	var ticketLimit = $("#qrcodeUploadDialog select[name='ticketLimit']").val();
            	var ticketLimitNum = $("#qrcodeUploadDialog input[name='ticketLimitNum']").val();

            	if($("#limitTr").css("display") != 'none'
        			&& $("select[name='ticketLimit']").val() == ''){
	        		$.fn.alert("中出方式不能为空!");
	                return false;
	        	}

            	if($("#limitTr").css("display") != 'none' && !$("select[name='ticketLimit']").val().isEmpty && $("select[name='ticketLimit']").val() != '0'){
            		console.log("ticketLimitNum=",$("#ticketLimitNum").val())
            		if(!$("#ticketLimitNum").val() || $("#ticketLimitNum").val() < 1){
						$.fn.alert("中出方式次数不能为空且大于0!");
						return false;
					}
				}

                // 弹出遮罩层
                $("#qrcodeFloatDiv").show();

                // 提交表单
                // var url = "<%=cpath%>/vcodeTicketActivity/importTicketCode.do";
                var url = "<%=cpath%>/vcodeTicketActivity/importTicketCodeByTxt.do";
                var formData = new FormData();
                formData.append("ticketNo", ticketNo);
                formData.append("activityKey", activityKey);
                formData.append("ticketLimit", ticketLimit);
                formData.append("ticketLimitNum", ticketLimitNum);



                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData,
                    dataType: "json",
                    async: true,
                    contentType : false,
                    processData: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {

                        // 隐藏遮罩层
                        $("#qrcodeFloatDiv").css("display","none");
                        $("#qrcodeUploadDialog #closeBtn").trigger("click");
                        if(!data['errMsg']){
							$.fn.alert("新增失败", function(){
								$("button.btn-primary").trigger("click");
							});
						}else{
							$.fn.alert(data['errMsg'], function(){
								$("button.btn-primary").trigger("click");
							});
						}

                    }
                });
            });
			$('[name="ticketLimit"]').change(function () {
				if($(this).val()=='0' || !$(this).val()){
					$(".ticketLimitNum").hide();
				}else{
					$(".ticketLimitNum").show();
				}

			});
			$('[name="ticketLimit"]').trigger("change");
		});

		function checkOption(ticketNo){
			if(activityTicketLimit !== ''){
				$("select[name='ticketLimit']").val("");
				$("#limitTr").css("display","none");
			}else{
				$("#limitTr").css("display","");
			}

			// 青啤优惠券目前只支持活动期间只中一次
			/* if(ticketNo.substring(0,1) == "J"
				&& $("#limitTr").css("display") != 'none'){
				$("select[name='ticketLimit']").val("2");
			} */
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
        	<li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">优惠券活动列表</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                    	 <div class="row">
			               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动查询</span></div>
			               <div class="col-md-10 text-right">
				               <c:if test="${currentUser.roleKey ne '4'}">
			                       <a id="addActivity" class="btn btn-blue">
	                            	   <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;
                            	     	  新 增
                            	  </a>
		                       </c:if>
			               </div>
			            </div>
			            <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                        <div class="widget-content form-inline">
		    				<form class="listForm" method="post"
		    					action="<%=cpath%>/vcodeTicketActivity/showTicketActivityList.do">
                           		<input type="hidden" class="tableTotalCount" value="${showCount}" />
                           		<input type="hidden" class="tableStartIndex" value="${startIndex}" />
                           		<input type="hidden" class="tablePerPage" value="${countPerPage}" />
                           		<input type="hidden" class="tableCurPage" value="${currentPage}" />
                           		<input type="hidden" name="queryParam" value="${queryParam}" />
                           		<input type="hidden" name="pageParam" value="${pageParam}" />
                           		<div class="row">
		            				<div class="col-md-12 ">
	                    				<div class="form-group little_distance search" style="line-height: 35px;">
			                                <div class="search-item">
						                        <label class="control-label">活动名称：</label>
						                        <input name="activityName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
			                                </div>
			                                <div class="search-item">
			                                    <label class="control-label">优惠券类型：</label>
			                                    <select class="form-control input-width-larger search" tag="validate" name="categoryKey">
			                                    	<option style="padding: 20px;" value="">全部</option>
			                       					<c:forEach items="${ticketCategoryList }" var="item">
			                       						<option value="${item.categoryKey }">${item.categoryName }</option>
			                       					</c:forEach>
			                       				</select>
			                                </div>
			                                <div class="search-item">
			                                    <label class="control-label">活动时间：</label>
			                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
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
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:5%;">序号</th>
					            	<th style="width:25%;">活动名称</th>
					            	<th style="width:10%;">优惠券类型</th>
					            	<th style="width:10%;">中出方式</th>
					            	<th style="width:12%;">活动时间</th>
					            	<th style="width:10%;">时间状态</th>
					            	<th style="width:10%;">是否启用</th>
					            	<th style="width:18%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="activity" varStatus="idx">
					        	<tr style="cursor:pointer">
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
					        			style="text-align:center;
					        			<c:choose>
                                            <c:when test="${activity.isBegin == '0'}">background-color: #ecb010;</c:when>
                                            <c:when test="${activity.isBegin == '1'}">background-color: #ea918c;</c:when>
                                            <c:when test="${activity.isBegin == '2'}">background-color: #999999;</c:when>
                                            <c:otherwise>background-color: red;</c:otherwise>
                                        </c:choose>">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}">
					        			<span>${activity.activityName}</span>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
					        			style="text-align:center;">
					        			<span>${activity.categoryName }</span>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
					        			style="text-align:center;">
					        			<c:choose>
					        				<c:when test="${ activity.ticketLimit eq '0'}">
					        					<span>无限制</span>
					        				</c:when>
											<c:when test="${ activity.ticketLimit eq '1'}">
					        					<span>每天${activity.ticketLimitNum}次</span>
					        				</c:when>
					        				<c:when test="${ activity.ticketLimit eq '2'}">
					        					<span>活动期间${activity.ticketLimitNum}次</span>
					        				</c:when>
					        				<c:otherwise>--</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
					        			style="text-align:center;">
					        			<span>从&nbsp;${activity.startDate}</span>
					        			<span>到&nbsp;${activity.endDate}</span>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
					        			style="text-align:center;">
					        			<c:choose>
                                            <c:when test="${activity.isBegin == '0'}">
                                                <span style="color: #ecb010;"><b>待上线</b></span>
                                            </c:when>
                                            <c:when test="${activity.isBegin == '1'}">
                                                <span style="color: #ea918c;"> <b>已上线</b> </span>
                                            </c:when>
                                            <c:when test="${activity.isBegin == '2'}">
                                                <span style="color: #999999;"> <b>已下线</b> </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: red;"><b>${activity.isBegin}</b></span>
                                            </c:otherwise>
                                        </c:choose>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
					        			style="text-align:center;">
					        			<c:choose>
                                            <c:when test="${activity.status == '0'}">
                                                <span><b>停用</b></span>
                                            </c:when>
                                            <c:when test="${activity.status == '1'}">
                                                <span><b>启用</b></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span><b>--</b></span>
                                            </c:otherwise>
                                        </c:choose>
					        		</td>
					        		<td data-key="${activity.activityKey}" data-value="${activity.sysTicketInfoGro }" data-ticketlimit="${ activity.ticketLimit}" style="text-align: center;">
					        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        			<a class="btn btn-xs import btn-orange"><i class="iconfont icon-tianjiajiahaowubiankuang"></i>&nbsp;新增优惠券</a>
					        			<c:if test="activity.isBegin == '0'">
					        				<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
					        			</c:if>
					        		</td>
					        	</tr>



					        	<tr style="background-color: white;"><td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">8</c:when><c:otherwise>7</c:otherwise></c:choose>" style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
                                   <div id="collapseInnerArea${idx.count}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${idx.count}">
                                     <div class="panel-body" style="padding:5px;">

                                         <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
                                          <thead>
                                              <tr class="success" style="background-color: #f0d2d0;">
	                                              	<th style="width:5%;">序号</th>
									            	<th style="width:8%;">优惠券编号</th>
									            	<th style="width:10%;">优惠券名称</th>
									            	<th style="width:8%;">优惠券类型</th>
									            	<th style="width:8%;">中出方式</th>
									            	<th style="width:8%;">券数量</th>
									            	<th style="width:8%;">使用数量</th>
									            	<th style="width:30%;">跳转URL或编码</th>
                                              </tr>
                                          </thead>
                                          <tbody>
                                          <c:choose>
                                              <c:when test="${fn:length(activity.vcodeTicketInfoList) gt 0}">
                                                 <c:forEach items="${activity.vcodeTicketInfoList}" var="ticketInfo" varStatus="tacketIdx">
                                                  <tr>
                                                  		<td style="text-align:center;">
					                                        <span>${tacketIdx.count}</span>
										        		</td>
										        		<td style="text-align:center;">${ ticketInfo.ticketNo}</td>
										        		<td style="text-align:center;">${ ticketInfo.ticketName}</td>
										        		<td style="text-align:center;">
										        			<c:set var="isCode" value="0"></c:set>
										        			<c:choose>
										        				<c:when test="${ ticketInfo.ticketType eq '0'}">
										        					链接方式
										        				</c:when>
										        				<c:when test="${ ticketInfo.ticketType eq '1'}">
										        					券码方式
										        					<c:set var="isCode" value="1"></c:set>
										        				</c:when>
										        				<c:when test="${ ticketInfo.ticketType eq '2'}">
										        					图片方式
										        					<c:set var="isCode" value="0"></c:set>
										        				</c:when>
										        				<c:when test="${ ticketInfo.ticketType eq '3'}">
										        					动态券码
										        					<c:set var="isCode" value="0"></c:set>
										        				</c:when>
										        				<c:when test="${ ticketInfo.ticketType eq '4'}">
										        					活动编码
										        					<c:set var="isCode" value="0"></c:set>
										        				</c:when>
										        				<c:otherwise>其他方式</c:otherwise>
										        			</c:choose>
										        		</td>
										        		<td style="text-align:center;">
										        			<%-- <c:choose>
										        				<c:when test="${empty(activity.ticketLimit)}">
										        					--
										        				</c:when>
										        				<c:otherwise>
										        					<c:choose>
										        						<c:when test="${ ticketInfo.ticketLimit eq '0'}">
												        					无限制
												        				</c:when>
																		<c:when test="${ ticketInfo.ticketLimit eq '1'}">
												        					每天一次
												        				</c:when>
												        				<c:when test="${ ticketInfo.ticketLimit eq '2'}">
												        					活动期间一次
												        				</c:when>
												        				<c:otherwise>--</c:otherwise>
												        			</c:choose>
																</c:otherwise>
										        			</c:choose> --%>
										        			<c:choose>
								        						<c:when test="${ ticketInfo.ticketLimit eq '0' and activity.ticketLimit eq '' || activity.ticketLimit == null}">
										        					无限制
										        				</c:when>
																<c:when test="${ ticketInfo.ticketLimit eq '1' and activity.ticketLimit eq '' || activity.ticketLimit == null}">
										        					每天${ ticketInfo.ticketLimitNum}次
										        				</c:when>
										        				<c:when test="${ ticketInfo.ticketLimit eq '2' and activity.ticketLimit eq '' || activity.ticketLimit == null}">
										        					活动期间${ ticketInfo.ticketLimitNum}次
										        				</c:when>
										        				<c:otherwise>--</c:otherwise>
										        			</c:choose>
										        		</td>
										        		<c:choose>
										        			<c:when test="${isCode eq '1' }">
										        				<td><span>${ticketInfo.ticketCount}</span></td>
												        		<td><span>${ticketInfo.ticketUseCount}</span></td>
												        		<td><span>--</span></td>
										        			</c:when>
										        			<c:otherwise>
										        				<td><span>--</span></td>
												        		<td><span>${ticketInfo.ticketUseCount}</span></td>
												        		<td><span>${ticketInfo.ticketUrl}</span></td>
										        			</c:otherwise>
										        		</c:choose>
                                                  </tr>
                                                 </c:forEach>
                                              </c:when>
                                              <c:otherwise>
                                                  <tr>
                                                      <td colspan="8"><span>查无数据！</span></td>
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
					        		<td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">8</c:when><c:otherwise>7</c:otherwise></c:choose>"><span>查无数据！</span></td>
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

   	<div id="qrcodeFloatDiv" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
    </div>
    <div class="modal fade" id="qrcodeUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel2">优惠券新增</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <input type="hidden" name="batchKeys" id="batchKeys"/>
                        <input type="hidden" name="batchCounts" id="batchCounts"/>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">优惠券面额：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <select class="form-control input-width-larger" tag="validate" name="ticketNo">
	                       			</select>
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr id="limitTr">
                            <td width="25%" class="text-right"><label class="title">中出方式：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <select class="form-control input-width-larger required" tag="validate" name="ticketLimit">
                                    	<option value="">请选择</option>
                                    	<option value="0">无限制</option>
                                    	<option value="1">每天</option>
                                    	<option value="2">活动期间</option>
	                       			</select>
									<input name="ticketLimitNum" id="ticketLimitNum" tag="validate"
										   class="form-control required input-width-small positive ticketLimitNum integer " autocomplete="off" maxlength="11" />
									<span class="blocker en-larger ticketLimitNum">次</span>
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="editBtn" class="btn btn-default do-add btn-orange" data-dismiss="">修 改</button>
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-orange" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
	</div>
  </body>
</html>
