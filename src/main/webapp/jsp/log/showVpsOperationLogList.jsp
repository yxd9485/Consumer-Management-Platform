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
			$("#addPromotion").click(function(){
				var url = "<%=cpath%>/sysDataDic/toAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
                var $this = $(this);
                var questionKey = $this.prev("input.uniform").val();
                var url = "<%=cpath%>/sysDataDic/toEdit.do?dataDicKey="+questionKey;
                $("form").attr("action", url);
                $("form").submit();
			});
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
                var $this = $(this);
                var questionKey = $this.prev("input.uniform").val();
				var url = "<%=cpath%>/sysDataDic/deleteDataDic.do?dataDicKey="+questionKey;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});
            
        });

        function imgBig(_url){
            if(!_url){
                return;
            }
            var img2 = document.querySelector('#large');
            img2.src=_url;
            img2.style.display = 'block';
        }
        function imgSmall(){
            var img2 = document.querySelector('#large');
            img2.style.display = 'none';
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
            <li class="current"><a> 系统管理</a></li>
            <li class="current">操作日志</li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">产品查询</span></div>
	               <div class="col-md-10 text-right">
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/vpsOperationLog/showVpsOperationLogList.do">
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
                                <input type="hidden" name="pageFlag" value="1">
                                <div class="search-item">
                                    <label class="control-label">菜单名称：</label>
                                    <select name="menuFlag" class="form-control input-width-larger transStatus search">
                                        <option value="">全部</option>
                                        <c:forEach items="${menuMap }" var="menu">
                                            <option value="${menu.key }" <c:if test="${queryBean.menuFlag eq menu.key}">selected="selected"</c:if>>
                                                    ${menu.value }
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">操作类型：</label>
                                    <select name="operationType" class="form-control input-width-larger transStatus search">
                                        <option value="">全部</option>
                                        <option value="0" <c:if test="${queryBean.operationType eq '0'}">selected="selected"</c:if>>登录</option>
                                        <option value="1" <c:if test="${queryBean.operationType eq '1'}">selected="selected"</c:if>>新增</option>
                                        <option value="2" <c:if test="${queryBean.operationType eq '2'}">selected="selected"</c:if>>修改</option>
                                        <option value="3" <c:if test="${queryBean.operationType eq '3'}">selected="selected"</c:if>>删除</option>
                                    </select>
                                </div>
                                <div class="search-item">

                                    <label class="control-label">操作时间：</label>
                                    <input name="startDate" id="startDate" class="input-width-medium Wdate search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" autocomplete="off"/>
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="input-width-medium Wdate search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startDate\')}' });" autocomplete="off"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">手机号：</label>
                                    <input name="phoneNumber" id="phoneNumber" class="form-control input-width-larger" value="${queryBean.phoneNumber }"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">操作账户：</label>
                                    <select name="loginName" class="form-control input-width-larger transStatus search">
                                        <option value="">全部</option>
                                        <c:forEach items="${listUser}" var="menu">
                                            <option value="${menu.userName }" <c:if test="${queryBean != null and queryBean.loginName eq menu.userName}">selected="selected"</c:if>>
                                                    ${menu.userName }
                                            </option>
                                        </c:forEach>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">产品列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
                                    <th style="width:5%;">序号</th>
                                    <th style="width:10%;" data-ordercol="MENU_FLAG">菜单名称</th>
                                    <th style="width:10%;" data-ordercol="OPERATION_TYPE">操作类型</th>
                                    <th style="width:10%;" data-ordercol="LOGIN_NAME">登录账户</th>
                                    <th style="width:10%;" data-ordercol="PHONE_NUMBER">登录手机号</th>
                                    <th style="width:12%;" data-ordercol="CREATE_TIME">操作时间</th>
                                    <th style="width:43%;" >操作描述</th>
					            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="log" varStatus="idx">
                                        <tr>
                                            <td>${idx.count}</td>
                                            <td>${menuMap[log.menuFlag]}</td>
                                            <td style="text-align:center;">
                                                <c:choose>
                                                    <c:when test="${log.operationType == '0'}">登录</c:when>
                                                    <c:when test="${log.operationType == '1'}">新增</c:when>
                                                    <c:when test="${log.operationType == '2'}">修改</c:when>
                                                    <c:when test="${log.operationType == '3'}">删除</c:when>
                                                    <c:otherwise>其他</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${log.loginName}</td>
                                            <td>${log.phoneNumber}</td>
                                            <td>${log.createTime}</td>
                                            <td style="text-align: left;">${log.operationDescription}</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7"><span>查无数据！</span></td>
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
      			<div class="modal-body">
      				<h6 class="add_success">添加成功</h6>
      				<h6 class="add_fail">添加失败</h6>
      				<h6 class="edit_success">编辑成功</h6>
      				<h6 class="edit_fail">编辑失败</h6>
      				<h6 class="del_success">删除成功</h6>
      				<h6 class="del_fail">删除失败</h6>
      				<h6 class="del_invalid">删除失败,先删除该sku关联的活动</h6>
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
