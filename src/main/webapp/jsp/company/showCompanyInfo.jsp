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
				var url = "<%=cpath%>/skuInfo/showSkuInfoAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/skuInfo/showSkuInfoEdit.do?skuKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/skuInfo/doSkuInfoDelete.do?skuKey="+key;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});

			// 排序
            $("a.ordercol").on("click", function(){
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
			var queryParamAry = "skuName,commodityCode,validDay,skuType".split(",");
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
            <li class="current"><a> 首页</a></li>
        	<li class="current"><a> 基础配置</a></li>
            <li class="current"><a title="">产品列表</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">基础信息"</span></div>
	               <div class="col-md-10 text-right">
		              <%-- <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addActivity" class="btn btn-red">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
	                       </a>
                       </c:if>--%>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/skuInfo/showSkuInfoList.do">
	                  <%--  <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />--%>
	                <div class="row">
			            <div class="col-md-12 ">
			                    <div class="form-group little_distance search">
                                    <table class="active_board_table">
                                        <tr>
                                            <td class="ab_left" style="width: 10%;"><label class="title">账户名称：</label></td>
                                            <td class="ab_main" style="width: 15%;">
                                                <div class="content" >
                                                    <span style="color:#FFFFFF">${companyInfo.companyName}</span>
                                                </div>
                                            </td>
                                            <td class="ab_left" style="width: 8%;"><label class="title">账户到期日：</label></td>
                                            <td class="ab_main" style="width: 15%;">
                                                <div class="content" style="color:#FFFFFF">
                                                    <span>${companyInfo.companyContractDdate}</span>&nbsp;&nbsp;&nbsp;
                                                    <span>还剩<font style="font-size: 16px;">&nbsp;<b style="color: black;">${intervalDay}</b>&nbsp;</font>天</span>
                                                </div>
                                            </td>
                                            <td class="ab_left" style="width: 10%;"><label class="title">联&nbsp;系&nbsp;人&nbsp;：</label></td>
                                            <td class="ab_main" colspan="3" style="width: 15%;">
                                                <div class="content" style="color:#FFFFFF">
                                                    <span>${companyInfo.companyLinkUser}</span>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="ab_left" style="width: 10%;"><label class="title">联系电话：</label></td>
                                            <td class="ab_main" colspan="3" style="width: 15%;">
                                                <div class="content" style="color:#FFFFFF">
                                                    <span>${companyInfo.companyPhone}</span>
                                                </div>
                                            </td>
                                            <td class="ab_left" style="width: 10%;"><label class="title">联系邮箱：</label></td>
                                            <td class="ab_main" colspan="3" style="width: 15%;">
                                                <div class="content" style="color:#FFFFFF">
                                                    <span>${companyInfo.companyEmail}</span>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
			                    </div>
			            </div>
		            </div>
		          <%--  <div class="row">
		               <div class="col-md-12 text-center mart20">
		                   <button type="button" class="btn btn-primary btn-blue">查 询</button>
		                   <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
		               </div>
		            </div>--%>
	                </form>
	            </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">登录用户信息</span></div>
                 <%--  <div class="col-md-10 text-right">
                        <a class="btn btn-red marr20 ordercol" data-ordercol="valid_day" data-ordertype="desc">有效天数 &nbsp;<i class="iconfont icon-queren"></i></a>
                        <a class="btn btn-red ordercol" data-ordercol="create_time" data-ordertype="desc">创建时间 &nbsp;<i class="iconfont icon-queren"></i></a>
                   </div>--%>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
                                    <th style="width:10%; text-align: center;">序号</th>
                                    <th style="width:20%; text-align: center;">姓名</th>
                                    <th style="width:20%; text-align: center;">手机号</th>
                                    <th style="width:10%; text-align: center;">状态</th>
					            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${empty(companyLoginPhoneList)}">
                                    <tr>
                                        <td colspan="3"><span>查无数据！</span></td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${companyLoginPhoneList }" var="item" varStatus="idx">
                                        <tr>
                                            <td align="center">${idx.index + 1}</td>

                                            <td align="center">
                                               <%-- <c:when test="${empty(item)}">--%>
                                                   <c:set var="item" value="${item }"  scope="session"/>
                                                <%  String result = (String)session.getAttribute("item");
                                                    String name="";
                                                if(result.length()>11) {
                                                     name = result.substring(12, result.length() - 1).replace(")","");

                                                }
                                                %>
                                                   <%=name%>
                                              <%--  </c:when>--%></td>
                                            <td align="center">  <%  String phoneResult = (String)session.getAttribute("item");
                                                String phone="";
                                                if(result.length()>0) {
                                                    phone = result.substring(0, 11);
                                                }
                                            %>
                                                <%=phone%></td>
                                            <td align="center">有效</td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
					        </tbody>
                        </table>
                  <%--      <table id="pagination"></table>--%>
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
