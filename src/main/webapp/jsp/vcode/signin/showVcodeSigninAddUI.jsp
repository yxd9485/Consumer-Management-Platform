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
		var activityTicketLimit =  '';
		$(function(){
			var flag = "${flag}";
			if(flag != "") {
				$("div.modal-body ."+flag+"").show();
				$("div.modal-body :not(."+flag+")").hide();
				$("#myModal").modal("show");
			}

			$("#fileTr").css("display","none");

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
			$("#add").click(function(){
				var url = "<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationEdit.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});

			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vpsVcodeTicketDenomination/doTicketDenominationDel.do?infoKey="+key;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
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
			<li class="current"><a> 活动管理</a></li>
			<li class="current"><a title="">万能签到模板</a></li>
		</ul>
	</div>
	<div class="row mart20">
		<div class="col-md-12 tabbable tabbable-custom">
			<div class="widget box" id="tab_1_1">
				<div class="widget-content">
					<div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
						<div class="row">
							<div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">万能签到模板</span></div>
							<div class="col-md-10 text-right">
								<c:if test="${currentUser.roleKey ne '4'}">
									<a id="add" class="btn btn-blue">
										<i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;
										万能签到模板
									</a>
								</c:if>
							</div>
						</div>
						<div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
						<div class="widget-content form-inline">

						</div>
						<table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
							   id="dataTable_data">
							<thead>
							<tr>
								<th style="width:50%;"><input type="radio" name="a" id="a" />常规签到模板</th>
								<th style="width:50%;"><input type="radio" name="a" id="b" />东奥黄金冰墩墩</th>
							</tr>
							</thead>
							<tbody>
										<tr style="cursor:pointer">
											<td   style="text-align:center;">
												<span><img src="https://d1icd6shlvmxi6.cloudfront.net/gsc/ZN4Z0O/c8/c6/0c/c8c60c2e4cdd4d6c90afbcc573b98d1e/images/4_3_2前端样式/u447.png?token=99860837afcedac565bac7a55c4d54dec721d927b6813850d9d635b69a5e6f3f" style="height: 280px;width: 200px; text-align:center;"></span>
											</td>
											<td   style="text-align:center;">
												<span><img src="https://d1icd6shlvmxi6.cloudfront.net/gsc/ZN4Z0O/c8/c6/0c/c8c60c2e4cdd4d6c90afbcc573b98d1e/images/4_3_2前端样式/u448.png?token=f701661fd7c7102a9d0cac9883cd31c61a222ddba625ace3da55b6675f63cb04" style="height: 280px;width: 200px; text-align:center;"></span>
											</td>
							</tbody>
						</table>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">保存</button>
						</div><div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">预发布</button>
					</div><div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
						<table id="pagination"></table>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>
</body>
</html>
