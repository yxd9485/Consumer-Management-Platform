<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
            // 初始化校验控件
            $.runtimeValidate($("#cloneBatchDialog"));
			
			// 新增
			$("#addItem").click(function(){
				var url = "<%=cpath%>/rebateRuleTemplet/showRebateRuleTempletAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/rebateRuleTemplet/showRebateRuleTempletEdit.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/rebateRuleTemplet/doRebateRuleTempletDelete.do?infoKey="+key;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});
            
            // 弹出克隆活动规则窗口
            $("a.clone").on("click", function(){
                // 重置输入框
                $("#cloneBatchDialog input[name]").val("");
                $("#cloneBatchDialog input[name='status']").val("1");
                $("#cloneBatchDialog input[name='infoKey']").val($(this).parents("td").data("key"));
                $("#cloneBatchDialog").modal("show");
            });
            
            // 确定添加批次
            $("#cloneBatchDialog").delegate("#addBtn", "click", function(){
                
                // 输入元素校验
                var validateResult = $.submitValidate($("#cloneBatchDialog"));
                if(!validateResult){
                    return false;
                }
                // 提交表单
                var url = "<%=cpath%>/rebateRuleTemplet/doRebateRuleTempletClone.do";
                var paramJson = {};
                $("#cloneBatchDialog input[name]").each(function(){
                    paramJson[$(this).attr("name")] = $(this).val();
                });
                console.log(paramJson);
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                    	console.log(data);
                        $("#cloneBatchDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });

            
            // 初始化模板状态显示样式
            $("#status").bootstrapSwitch({  
                onSwitchChange:function(event,state){  
                    if(state==true){  
                       $("input:hidden[name='status']").val("1");
                    }else{  
                       $("input:hidden[name='status']").val("0");
                    }
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
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
        	<li class="current"><a> 基础配置</a></li>
            <li class="current"><a title="">活动规则模板</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">规则模板查询</span></div>
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
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/rebateRuleTemplet/showRebateRuleTempletList.do">
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
		                    <div class="form-group little_distance search">
                                <div class="search-item">
			                        <label class="control-label">模板名称：</label>
			                        <input name="templetName" class="form-control input-width-larger" autocomplete="off" maxlength="50"/>
                                </div>    
                                <div class="search-item">
                                    <label class="control-label">创建时间：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                <div class="search-item">
	                                <label class="control-label">模板状态：</label>
	                                <select name="status" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
	                                    <option value="0">停用</option>
	                                    <option value="1">启用</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">规则模板列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:5%;">序号</th>
					            	<th style="width:20%;" data-ordercol="templet_name">模板名称</th>
					            	<th style="width:8%;" data-ordercol="unit_money">单品金额</th>
					            	<th style="width:8%;" data-ordercol="unit_vpoints">单品积分</th>
					            	<th style="width:8%;" data-ordercol="first_scan_percent">首扫占比</th>
					            	<th style="width:8%;" data-ordercol="common_scan_percent">普扫占比</th>
					            	<th style="width:8%;" data-ordercol="status">模板状态</th>
					            	<th style="width:13%;" data-ordercol="create_time" data-orderdef>创建时间</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:23%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td>
					        			<span>${item.templetName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.unitMoney}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.unitVpoints}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.firstScanPercent}%</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.commonScanPercent}%</span>
					        		</td>
					        		<td style="text-align:center;">
					        		     <c:choose>
					        		         <c:when test="${item.status eq '0'}">停用</c:when>
					        		         <c:when test="${item.status eq '1'}">启用</c:when>
					        		     </c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(item.createTime, 0, 19)}</span>
					        		</td>
					        		<c:if test="${currentUser.roleKey ne '4'}">
					        		<td data-key="${item.infoKey}" style="text-align: center;">
					        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
					        			<a class="btn btn-xs clone btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;克 隆</a>
					        		</td>
					        		</c:if>
					        	</tr>
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
    <div class="modal fade" id="cloneBatchDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">克隆活动规则模板</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">模板名称：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="infoKey" type="hidden"/>
                                    <input name="templetName" tag="validate"
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="50" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">模板状态：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="status" type="hidden" value="1" />
                                    <input id="status" type="checkbox" checked data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                </div>
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
