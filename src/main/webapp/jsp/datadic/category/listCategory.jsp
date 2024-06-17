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

			// 初始化界面
			initPage();

			// 新增
			$("#addActivity").click(function(){
				var url = "<%=cpath%>/sysDicCategoryInfo/toAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/sysDicCategoryInfo/toEdit.do?categoryKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/sysDicCategoryInfo/deleteCategory.do?categoryKey="+key;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});

			// 排序
            $("a.ordercol").on("click", function(){
                $("form").attr("action", "<%=cpath%>/sysDicCategoryInfo/listCategory.do");
                $("input.tableOrderCol").val($(this).data("ordercol"));
                $("input.tableOrderType").val($(this).data("ordertype") == "desc" ? "asc" : "desc");
                
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
                $orderCol.find("i.iconfont").removeClass("icon-paixu-jiangxu").addClass("icon-paixu-shengxu");
            } else {
                $orderCol.find("i.iconfont").removeClass("icon-paixu-shengxu").addClass("icon-paixu-jiangxu");
            }

			// 回显查询条件
			var queryParamAry = "activityName,dicType,invoker".split(",");
			$.each("${queryParam}".split(","), function(i, obj) {
				$("div.form-group").find(":input[name='" + queryParamAry[i] +"']:not(:hidden)").val(obj);
			});
		}

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
            <li class="current">数据字典</li>
            <li class="current">数据字典·类型</li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">字典查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addActivity" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 添加字典类型
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/sysDicCategoryInfo/listCategory.do">
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
		                            <label class="control-label">类型名称：</label>
                                    <input id="activityName" name="activityName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
		                            <label class="control-label">是否可编辑：</label>
		                            <select id="dicType" name="dicType" class="form-control input-width-larger search">
		                                <option value="">请选择</option>
		                                <option value="1">是</option>
		                                <option value="0">否</option>
		                            </select>
                                </div>
                                <div class="search-item">
		                            <label class="control-label">使用平台：</label>
		                            <select id="invoker" name="invoker" class="form-control input-width-larger search">
		                                <option value="">请选择</option>
		                                <option value="0">全部</option>
		                                <option value="1">运营</option>
		                                <option value="2">接口</option>
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
                                    <th style="width:5%; text-align: center;">序号</th>
                                    <th style="width:15%;" data-ordercol="a.CATEGORY_NAME">字典类型名称</th>
                                    <th style="width:15%;" data-ordercol="a.CATEGORY_CODE">字典类型Code</th>
                                    <th style="width:15%;" data-ordercol="a.DIC_TYPE">是否可编辑</th>
                                    <th style="width:15%;" data-ordercol="a.INVOKER">使用平台</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:20%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="category" varStatus="st">
                                        <tr>
                                            <td class="align-center">${st.count }</td>
                                            <td>${category.categoryName}</td>
                                            <td>${category.categoryCode}</td>
                                            <td>
                                                <c:if test="${category.dicType == '0'}">否</c:if>
                                                <c:if test="${category.dicType == '1'}">是</c:if>
                                            </td>
                                            <td>
                                                <c:if test="${category.invoker == '0'}">全部</c:if>
                                                <c:if test="${category.invoker == '1'}">运营</c:if>
                                                <c:if test="${category.invoker == '2'}">接口</c:if>
                                            </td>
                                            <td data-key="${category.categoryKey}">
                                                <c:if test="${category.dicType == '1'}">
                                                    <a flag="editCategoryA" class="btn edit btn-xs btn-info btn-orange" ><i class="iconfont icon-xiugai"></i> 修 改</a>
                                                    <a flag="deleteCategoryA" class="btn del btn-xs btn-danger btn-brownness btn-red"><i class="iconfont icon-lajixiang"></i> 删 除</a>
                                                </c:if>
                                                <!--<a flag="viewCategoryA" class="btn btn-xs" href="<%=cpath%>/sysDicCategoryInfo/viewCategory.do?categoryKey=${category.categoryKey}">查看</a>-->
                                            </td>
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
