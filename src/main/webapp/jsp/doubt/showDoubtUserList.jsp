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
			
			// 加入可疑
			$("#addDoubtBatch").on("click", function(){
				$("#addDoubt").modal("show");
				$("#addDoubt").find(".doubt-list-value").val("");
			});
			
			// 校验
			$("#addDoubt").delegate(".doubt-list-value", "blur", function(){
				var $current = $(this);
				var value = $current.val().trim();
				if(value == ""){
					$("#addDoubt").find(".valid-place").text("可疑账户不能为空");
				}
			});
			
			// 滑入时处理
			$("#addDoubt").delegate(".doubt-list-value", "focus", function(){
				$("#addDoubt").find(".valid-place").text("");
			});
			
			// 确定添加
			$("#addDoubt").delegate(".do-add-doubt", "click", function(){
				var url = "<%=cpath%>/vcodeDoubtUser/doDoubtUserAdd.do";
				var value = $("#addDoubt").find(".doubt-list-value").val().trim();
				if(value == ""){
					$("#addDoubt").find(".valid-place").text("可疑账户不能为空");
				} else {
					$.ajax({
						type: "POST",
		                url: url,
		                data: {paramValue : value},
		                dataType: "json",
		                async: false,
		                beforeSend:appendVjfSessionId,
                    success:  function(data) {
	                		$("#addDoubt").modal("hide");
	                		$("div.modal-body .custom_msg").show();
	                		$("div.modal-body .custom_msg").text(data['errMsg']);
	    					$("div.modal-body :not(.custom_msg)").hide();
	    					$("#myModal").modal("show");
	    					$("#myModal .msgClose").one('click', function(){
	    						$("button.btn-primary").trigger("click");
	    					});
		                }
					});
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
                var url = "<%=cpath%>/vcodeDoubtUser/doDoubtUserRemove.do?blacklistValue="+blacklistValue;
                $.fn.confirm("确定移除？", function() {
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            // 批量移除
            $("#removeDoubtBatch").off();
            $("#removeDoubtBatch").on("click",function(){
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                })
                if (key == "") {
                    $.fn.alert("请选择要移除的可疑用户!");
                } else {
                    var url = "<%=cpath%>/vcodeDoubtUser/doDoubtUserRemove.do?blacklistValue="+key;
                    $.fn.confirm("确定移除？", function() {
                        $("form").attr("action", url);
                        $("form").submit();
                    });
                }
            });
            
            // 加入黑名单
            $("a.addBlacklist").off();
            $("a.addBlacklist").on("click", function(){
                var key = $(this).parents("td").data("key");
                var blacklistValue = $(this).parents("td").data("user");
                var url = "<%=cpath%>/vcodeDoubtUser/doBlacklistAdd.do?blacklistValue="+blacklistValue;
                $.fn.confirm("确定将此用户加入黑名单吗？", function() {
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            // 批量加入黑名单
            $("#addBlackBatch").off();
            $("#addBlackBatch").on("click",function(){
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                })
                if (key == "") {
                    $.fn.alert("请选择要加入黑名单的可疑用户!");
                } else {
                    var url = "<%=cpath%>/vcodeDoubtUser/doBlacklistAdd.do?blacklistValue="+key;
                    $("form").attr("action", url);
                    $("form").submit(); 
                }
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
		.doubt-table {
			width: 98%;
			maring: 0 auto;
		}
		.doubt-table > thead > tr > th {
			font-weight: normal;
		}
		b {
			margin: 0 8px;
			color: #c33;
		}
		.doubt-table > tbody > tr > td {
			padding: 6px;
		}
		
		.doubt-table > tbody > tr > td:nth-child(1) {
			text-align: right;
		}
		.doubt-list-value {
			width: 95%;
			height: 32px;
			border: 1px solid #e1e1e1;
			background-color: #fff;
		}
		.doubt-list-value:focus {
			border: 1px solid #e1e1e1;
		}
		.valid-place, .required {
			color: #c33;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
        	<li class="current"><a>消费者管理</a></li>
        	<li class="current"><a title="">可疑用户</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">可疑用户查询</span></div>
                   <div class="col-md-10 text-right">
	                   <a id="addDoubtBatch" class="btn btn-blue">
	                       <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 添加可疑用户
	                   </a>
	                   <a id="removeDoubtBatch" class="btn btn-red">
	                       <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 批量移除
	                   </a>
	                   <a id="addBlackBatch" class="btn btn-blue">
	                       <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 批量加入黑名单
	                   </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/vcodeDoubtUser/showDoubtUserList.do">
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
                                    <select name="doubtReason" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option value="1">达到每分钟限定次数</option>
                                        <option value="2">达到每天限定次数</option>
                                        <option value="5">达到每月限定次数</option>
                                        <option value="3">达到累计限定次数</option>
                                        <option value="4">手动加入</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">加入时间：</label>
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
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">可疑用户列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
                                    <th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:16%;" data-ordercol="b.BLACK_LIST_VALUE">用户ID</th>
                                    <th style="width:8%;" data-ordercol="c.NICK_NAME">昵称</th>
                                    <th style="width:10%;" data-ordercol="c.PHONE_NUMBER">手机号</th>
                                    <th style="width:15%;" data-ordercol="a.vcode_activity_name">活动名称</th>
                                    <th style="width:10%;" data-ordercol="b.DOUBT_REASON">加入原因</th>
                                    <th style="width:12%;" data-ordercol="address">加入区域</th>
                                    <th style="width:10%;" data-ordercol="b.CREATE_TIME" data-orderdef>加入时间</th>
                                    <th style="width:10%;" data-ordercol="b.DOUBTFUL_TIME_LIMIT_TYPE" >时间限制</th>
                                    <th style="width:15%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="result" varStatus="idx">
					        	<tr>
                                    <td style="text-align:center;">
	                                   <input name="itemCB" type="checkbox" value="${result.blacklistValue}" />
	                                </td>
					        		<td class="text_center" style="vertical-align: middle;"><span>${idx.count}</span></td>
					        		<td title="${result.openid}" class="dblclick">
					        			<span>${result.blacklistValue}</span>
					        		</td>
					        		<td>
					        			<span>${result.nickName}</span>
					        		</td>
                                    <td>
                                        <span>${result.phoneNum}</span>
                                    </td>
                                    <td>
                                    	<span>${result.vcodeActivityName}</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.doubtReason == '1' }">达到每分钟限定次数</c:when>
                                            <c:when test="${result.doubtReason == '2' }">达到每天限定次数</c:when>
                                            <c:when test="${result.doubtReason == '3' }">达到累计限定次数</c:when>
                                            <c:when test="${result.doubtReason == '5' }">达到每月限定次数</c:when>
                                            <c:when test="${result.doubtReason == '4' }">手动加入</c:when>
                                            <c:otherwise>未知原因</c:otherwise>
                                        </c:choose>
                                    </td>
					        		<td>
					        			<span>${result.address}</span>
					        		</td>
					        		<td>
					        			<span>${fn:substring(result.createTime, 0, 19)}</span>
					        		</td>
                                    
                                    <td>
                                        <c:choose>
                                            <c:when test="${result.doubtfulTimeLimitType == '0' }">3天</c:when>
                                            <c:when test="${result.doubtfulTimeLimitType == '1' }">7天</c:when>
                                            <c:when test="${result.doubtfulTimeLimitType == '2' }">15天</c:when>
                                            <c:when test="${result.doubtfulTimeLimitType == '3' }">30天</c:when>
                                            <c:when test="${result.doubtfulTimeLimitType == '4' }">本周</c:when>
                                            <c:when test="${result.doubtfulTimeLimitType == '5' }">本月</c:when>
                                            <c:when test="${result.doubtfulTimeLimitType == '6' }">永久</c:when>

                                        </c:choose>
                                    </td>
					        		<td data-key="${result.blacklistKey}" data-user="${result.blacklistValue}">
					        			<a class="btn btn-xs remove btn-danger btn-red"><i class="iconfont icon-lajixiang" style="font-size: 14px;"></i> 移 除</a>
					        			<a class="btn btn-xs addBlacklist btn-orange "><i class="iconfont icon-heimingdan" style="font-size: 14px;"></i> 加入黑名单</a>
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
    <div class="modal fade" id="addDoubt" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"  data-backdrop="static">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:20%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">添加到可疑名单</h4>
	      		</div>
      			<div class="modal-body">
      				<table class="doubt-table">
      					<thead>
      					<tr>
      						<th colspan="2">
      							<ol>
      								<li><b>格式为：userKey,userKey....</b>注意逗号为英文半角</li>
      							</ol>
      						</th>
      					</tr>
      					</thead>
      					<tbody>
      					<tr>
      						<td style="width:20%;">可疑账户：<span class="required">*</span></td>
      						<td style="width:80%;">
      							<input class="doubt-list-value" />
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
			        <button type="button" class="btn btn-default do-add-doubt btn-red" data-dismiss="">确 认</button>
	      		</div>
		    </div>
	  	</div>
	</div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="top:30%;">
                <div class="modal-header">
                    <button type="button" class="close msgClose" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">提示消息</h4>
                </div>
                <div class="modal-body">
                    <h6 class="add_success">添加成功</h6>
                    <h6 class="add_fail">添加失败</h6>
                    <h6 class="edit_success">编辑成功</h6>
                    <h6 class="edit_fail">编辑失败</h6>
                    <h6 class="sort_success">更新排序成功</h6>
                    <h6 class="sort_fail">更新排序失败</h6>
                    <h6 class="not_delete">请至少选择一条数据进行删除</h6>
                    <h6 class="is_delete">删除成功</h6>
                    <h6 class="xx_delete">删除失败</h6>
                    <h6 class="custom_msg">定制消息</h6>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default msgClose btn-red" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
	</div>
  </body>
</html>
