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

			// 排序
            $("a.ordercol").on("click", function(){
                $("form").attr("action", "<%=cpath%>/sysDataDic/dataDicList.do");
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
			var queryParamAry = "categoryKey,dataAlias".split(",");
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
            <li class="current">数据字典·数据</li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">字典查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addPromotion" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 添加数据字典
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/sysDataDic/dataDicList.do">
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
		                            <label class="control-label">字典类型：</label>
		                            <select name="categoryKey" id="categoryKey" class="form-control input-width-larger search">
		                                <option value="">全部</option>
		                                <c:forEach items="${categoryList}" var="categoryList">
		                                    <option value="${categoryList.categoryKey}">
		                                            ${categoryList.categoryName}
		                                    </option>
		                                </c:forEach>
		                            </select>
                                </div>
                                <div class="search-item">
		                            <label class="control-label">数据字典别名：</label>
                                    <input id="dataAlias" name="dataAlias" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                                    <th style="width:5%;" >序号</th>
                                    <th style="width:15%;" data-ordercol="dic.CATEGORY_NAME">字典类型</th>
                                    <th style="width:15%;" data-ordercol="da.DATA_ID">数据键</th>
                                    <th style="width:30%;" data-ordercol="da.DATA_VALUE">数据值</th>
                                    <th style="width:20%;" data-ordercol="da.DATA_ALIAS">别名</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:20%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(dataDicList) gt 0}">
                                    <c:forEach items="${dataDicList}" var="dataDicList" varStatus="aastatus">
                                        <tr>
                                            <td class="align-center">${aastatus.index+1}</td>
                                            <td>${dataDicList.categoryName}</td>
                                            <td>${dataDicList.dataId}</td>
                                            <td>${dataDicList.dataValue}</td>
                                            <td>${dataDicList.dataAlias}</td>
                                            <td>
                                                <c:if test="${dataDicList.dicType == '1'}">
                                                    <input type="hidden" class="uniform" name="eventsKey" value="${dataDicList.dataDicKey}" />
                                                    <a class="btn btn-xs edit btn-info btn-orange" id="editGoodKidQu"><i class="iconfont icon-xiugai"></i> 修 改</a>
                                                    <input type="hidden" class="uniform" name="eventsKey" value="${dataDicList.dataDicKey}" />
                                                    <a class="btn btn-xs del btn-danger btn-brownness btn-red" id="delDataDic"><i class="iconfont icon-lajixiang"></i> 删 除</a>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6"><span>查无数据！</span></td>
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
