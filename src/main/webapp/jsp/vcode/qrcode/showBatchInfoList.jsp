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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
            // 初始化校验控件
            $.runtimeValidate($("#addBatchDialog"));
			
			// 弹出创建批次窗口
			$("#addItem").on("click", function(){
				// 重置输入框
				$("#addBatchDialog input[name]").val("");
                
				// 重置克隆输入框
				var $batchCopyNum = $("#addBatchDialog input[name='batchCopyNum']");
				$batchCopyNum.closest("tr").css("display", "");
				$batchCopyNum.removeAttr("disabled").val("1");
                $("#addBatchDialog").modal("show");
			});
            
            // 弹出修改批次窗口
            $("a.edit").off();
            $("a.edit").on("click", function(){
                // 重置克隆输入框
                var $batchCopyNum = $("#addBatchDialog input[name='batchCopyNum']");
                $batchCopyNum.closest("tr").css("display", "none");
                $batchCopyNum.attr("disabled", "disabled").val("1");
                
                // 初始化批次数据
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/qrcodeBatchInfo/findBatchInfo.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {batchKey : key},
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#addBatchDialog").modal("show");
                        var batchInfo = data['batchInfo'];
                        for(var name in batchInfo) {
                            $("#addBatchDialog input[name='" + name + "']").val(batchInfo[name]);
                        }
                    }
                });
            });

            // 确定添加批次
            $("#addBatchDialog").delegate("#addBtn", "click", function(){
            	
            	// 输入元素校验
            	var validateResult = $.submitValidate($("#addBatchDialog"));
                if(!validateResult){
                    return false;
                }
                // 提交表单
                var url = "<%=cpath%>/qrcodeBatchInfo/doBatchInfoAdd.do";
                if ($("#addBatchDialog input[name='batchKey']").val() != "") {
                	url = "<%=cpath%>/qrcodeBatchInfo/doBatchInfoEdit.do";
                } 
                var paramJson = {};
                $("#addBatchDialog input[name]").each(function(){
                	paramJson[$(this).attr("name")] = $(this).val();
                });
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#addBatchDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });
            
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/qrcodeBatchInfo/doBatchInfoDelete.do?batchKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            // 批量删除
            $("#delItems").off();
            $("#delItems").on("click", function(){
            	var batchNum = $("[name='itemCB']:checked").size();
            	if (batchNum == 0) {
            		$.fn.alert("请选择要删除的批次！");
            	}
            	
            	// 校验并组建删除批次主键
            	var batchKey = "";
            	var clientOrderNo = "";
            	var itemClientOrderNo = "";
                $("[name='itemCB']:checked").each(function(idx, obj) {
                	itemClientOrderNo = $(this).closest("tr").find("#clientOrderNo").text();
                	if (idx == 0) {
                		clientOrderNo = itemClientOrderNo;
                	} else if (clientOrderNo != itemClientOrderNo) {
                		batchKey = ""; // 清空批次主键
                		$.fn.alert("删除失败，批量删除只能选择同一客户订单号下的批次！");
                		return false;
                	}
                	batchKey = $(this).val() + "," + batchKey;
                })
                
                if (batchKey != "") {
	                var url = "<%=cpath%>/qrcodeBatchInfo/doBatchInfoDelete.do?batchKey="+batchKey;
	                $.fn.confirm("确认删除这" + batchNum + "个批次吗？", function(){
	                    $("form").attr("action", url);
	                    $("form").submit();
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
            
            //创建码源订单
            $("a.adQrcodeOrder").off();
            $("a.adQrcodeOrder").on("click",function(){
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                })
                if (key == "") {
                    $.fn.alert("请选择生成订单所需批次!");
                } else {
                    var url = "${basePath}/qrcodeBatchInfo/showQrcodeOrderAdd.do?batchKey="+key;
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
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
        	<li class="current"><a> 码源管理</a></li>
            <li class="current"><a title="">生成码源</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">码源批次查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="delItems" class="btn btn-red">
	                           <i class="iconfont icon-lajixiang" style="font-size: 12px;"></i>&nbsp; 删除码源批次
	                       </a>
	                       <a id="addItem" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 创建码源批次
	                       </a>
	                       <a id="addOrder" class="btn btn-blue adQrcodeOrder">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 创建码源订单
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/qrcodeBatchInfo/showBatchInfoList.do">
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
			                        <label class="control-label">客户订单号：</label>
			                        <input name="clientOrderNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">批码编号：</label>
			                        <input name="batchDesc" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">批码名称：</label>
			                        <input name="batchName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">码源批次列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                                <tr>
                                    <th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:15%;" data-ordercol="client_order_no">客户订单号</th>
                                    <th style="width:15%;" data-ordercol="batch_desc">批码编号</th>
                                    <th style="width:20%;" data-ordercol="batch_name">批码名称</th>
                                    <th style="width:7%;" data-ordercol="qrcode_amounts">码数量</th>
                                    <th style="width:7%;" data-ordercol="pack_amounts">包码数</th>
                                    <th style="width:13%;" data-ordercol="create_time" data-orderdef>创建时间</th>
                                    <c:if test="${currentUser.roleKey ne '4'}">
                                    <th style="width:16%;">操作</th>
                                    </c:if>
                                </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
                                   <td style="text-align:center;">
                                      <input name="itemCB" type="checkbox" value="${item.batchKey}" />
                                   </td>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span id="clientOrderNo">${item.clientOrderNo}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.batchDesc}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.batchName}</span>
					        		</td>
					        		<td style="text-align:right;">
					        			<span>${item.qrcodeAmounts}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.packAmounts}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(item.createTime, 0, 19)}</span>
					        		</td>
					        		<c:if test="${currentUser.roleKey ne '4'}">
					        		<td data-key="${item.batchKey}" style="text-align: center;">
					        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
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
    <div class="modal fade" id="addBatchDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">创建码源批次</h4>
                </div>
                <div class="modal-body">
	                <table class="active_board_table">
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">客户订单号：<span class="required">*</span></label></td>
		                    <td>
		                        <div class="content">
		                            <input name="batchKey" type="hidden"/>
		                            <input name="clientOrderNo" tag="validate"
		                                class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
		                            <label class="validate_tips" style="width:35%"></label>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td style="text-align:center;" width="25%" class="text-right"><label class="title">批次编号：<span class="required">*</span></label></td>
		                    <td style="text-align:center;">
		                        <div class="content">
		                            <input name="batchDesc" tag="validate"
		                                class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
		                            <label class="validate_tips" style="width:35%"></label>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td style="text-align:center;" width="25%" class="text-right"><label class="title">批次名称：<span class="required">*</span></label></td>
		                    <td style="text-align:center;">
		                        <div class="content">
		                            <input name="batchName" tag="validate"
		                                class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
		                            <label class="validate_tips" style="width:35%"></label>
		                        </div>
		                    </td>
		                </tr>
                        <tr>
                            <td style="text-align:center;" width="25%" class="text-right"><label class="title">生成包码数：<span class="required">*</span></label></td>
                            <td style="text-align:center;">
                                <div class="content" style="display:flex; width: 100%;">
                                    <input name="qrcodeAmounts" tag="validate"
                                        class="form-control input-width-small required number positive maxValue" placeholder="包中二维码数" autocomplete="off" maxVal="500000" maxlength="6" />
                                    <label style="line-height: 30px;">&nbsp;&nbsp;&nbsp;X&nbsp;&nbsp;&nbsp;</label> 
                                    <input name="packAmounts" tag="validate"
                                        class="form-control input-width-small required number positive input-width-normal" placeholder="包码数" autocomplete="off" maxlength="4" />
                                    <b class="blocker en-larger"></b>
                                    <samp class="blocker en-larger"></samp>
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="text-align:center;" width="25%" class="text-right"><label class="title">克隆个数：<span class="required">*</span></label></td>
                            <td style="text-align:center;">
                                <div class="content">
                                    <input name="batchCopyNum" tag="validate"
                                        class="form-control input-width-larger positive" autocomplete="off" maxlength="4" value="1" />
                                    <label class="validate_tips" style="width:35%"></label>
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
