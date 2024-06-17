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
			$("#register").click(function(){
				var url = "<%=cpath%>/checkUser/showCheckUserInfoQrcode.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 启用
			$("a.startItem").off();
			$("a.startItem").on("click", function(){
                var key = $(this).parents("td").data("key");
				$.fn.confirm("确定启用该用户的核销权限吗？", function(){
					var url = "<%=cpath%>/checkUser/updateCheckUserStatus.do?infoKey="+key+"&status=1";
					$("form").attr("action", url);
					$("form").submit();
				});
			});
			
			// 停用
			$("a.stopItem").off();
			$("a.stopItem").on("click", function(){
				var key = $(this).parents("td").data("key");
				$.fn.confirm("确定停用该用户的核销权限吗？", function(){
					var url = "<%=cpath%>/checkUser/updateCheckUserStatus.do?infoKey="+key+"&status=0";
					$("form").attr("action", url);
					$("form").submit();
				});
			});
			
			// 删除
			$("a.delItem").off();
			$("a.delItem").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/checkUser/deleteCheckUser.do?infoKey="+key;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});

			// 排序
            $("a.ordercol").on("click", function(){
                $("form").attr("action", "<%=cpath%>/batchActivate/showActivateCogList.do");
                $("input.tableOrderCol").val($(this).data("ordercol"));
                $("input.tableOrderType").val($(this).data("ordertype") == "desc" ? "asc" : "desc");
                
                // 跳转到首页
                doPaginationOrder();
            });
			
            // 查询结果导出
            $("a.prizeExport").on("click", function(){
                $("form").attr("action", "<%=cpath%>/checkUser/exportUserList.do");
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/checkUser/showCheckUserInfoList.do");
                
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
			var queryParamAry = "openid,userName,phoneNum,factoryName,userStatus".split(",");
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
            <li class="current"><a>系统管理</a></li>
            <li class="current"><a title="">大奖核销人员管理</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">核销人员查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="register" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 核销人员注册
	                       </a>
	                       <a class="btn btn-blue prizeExport">
	                     		<i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 核销人员导出
	                      </a>
                       </c:if>
	                   
	               </div>	          
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/checkUser/showCheckUserInfoList.do">
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
	                                <label class="control-label">openid：</label>
	                                <input name="openid" id="openid" class="form-control input-width-larger" value="${queryBean.openid}"/>
	                            </div>
	                            <div class="search-item">
	                                <label class="control-label">核销人员：</label>
	                                <input name="userName" id="userName" class="form-control input-width-larger" value="${queryBean.userName }"/>
	                            </div>
	                            <div class="search-item">
	                                <label class="control-label">手机号：</label>
	                                <input name="phoneNum" id="phoneNum" class="form-control input-width-larger" value="${queryBean.phoneNum }" oninput = "value=value.replace(/[^\d]/g,'')"/>
	                            </div>
	                             <div class="search-item">
	                                <label class="control-label">门店名称：</label>
	                                <input name="terminalName" id="terminalName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
	                            </div>
                                <div class="search-item">
                                    <label class="control-label">状态：</label>
                                    <select name="userStatus" class="form-control input-width-larger transStatus search" >
                                        <option value="">请选择</option>
                                        <option value="0" <c:if test="${queryBean.userStatus eq '0'}">selected</c:if>>停用</option>
                                        <option value="1" <c:if test="${queryBean.userStatus eq '1'}">selected</c:if>>启用</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">激活人员列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">

                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
                                    <th style="width:5%;">序号</th>
                                    <th style="width:18%;"data-ordercol="openid">openid</th>
                                    <th style="width:9%;"data-ordercol="user_name">核销人员</th>
                                    <th style="width:9%;"data-ordercol="phone_num">手机号</th>
                                    <th style="width:26%;"data-ordercol="address">地址</th>
                                    <th style="width:10%;">门店名称</th>
                                    <th style="width:12%;" data-ordercol="create_time" data-orderdef>注册时间</th>
                                    <th style="width:8%;"data-ordercol="user_status">状态</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:13%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td style="text-align:left">${item.openid}</td>
                                            <td style="text-align:left">${item.userName}</td>
                                            <td style="text-align:center">${item.phoneNum}</td>
                                            <td style="text-align:left">${item.province}-${item.city}-${item.county}-${item.address}</td>
                                            <td style="text-align:left">${item.terminalName}</td>
                                            <td style="text-align:center">${item.createTime}</td>
                                            <td style="text-align:center">
												<c:choose>
                                                        <c:when test="${item.userStatus eq '0'}">
                                                        	<span style="color:red;">停用</span>
                                                        </c:when>
                                                        <c:otherwise>启用</c:otherwise>
                                                    </c:choose>
											</td>
                                            <c:if test="${currentUser.roleKey ne '4'}">
                                                <td data-key="${item.infoKey}" style="text-align:center">
                                                	<c:choose>
                                                        <c:when test="${item.userStatus eq '0'}">
                                                            <a class="btn btn-xs startItem btn-info btn-orange"><i class="iconfont icon-xiugai"></i> 启 用</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="btn btn-xs stopItem btn-info btn-orange"><i class="iconfont icon-xiugai"></i> 停 用</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                	<a class="btn btn-xs delItem btn-info btn-red"><i class="iconfont icon-lajixiang"></i> 删 除</a>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="${currentUser.roleKey ne '4' ? 9 : 8}"><span>查无数据！</span></td>
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
