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
    <title>河北管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
            
            // 切换userKey与openid的显示
            $("td.userKey").dblclick(function(){
                var title = $(this).attr("title");
                var conent = $(this).text();
                $(this).text(title);
                $(this).attr("title", conent);
                
                // openid与userkey颜色区分
                var txtColor = $(this).css("color");
                var preColor = $(this).data("preColor");
                if (preColor == undefined) {
                    $(this).css("color", "red");
                    $(this).data("preColor", txtColor);
                } else {
                    $(this).css("color", preColor);
                    $(this).data("preColor", txtColor);
                }
            });
	        
	        // 更改幸运用户级别
	        $(".luckLevel").click(function(){
	            $("#changeLuckLevel").modal("show");
	            $(".do-add-lucklevel").data("key", $(this).data("key"));
	            $(".lucklevel-list-value").find("option").eq(0).prop("selected",true);
	        });
	        
	        // 确定更改幸运用户级别
	        $("#changeLuckLevel").delegate(".do-add-lucklevel", "click", function(){
	            var url = "<%=cpath%>/user/updateIsLuckyUser.do";
	            var userKey = $(this).data("key");
	            var luckLevel = $("#changeLuckLevel").find(".lucklevel-list-value").val().trim();
	            $.ajax({
	                type: "POST",
	                url: url,
	                data: {"userKey": userKey,
	                       "luckLevel": luckLevel},
	                dataType: "text",
	                async: false,
	                beforeSend:appendVjfSessionId,
                    success:  function(data) {
	                    $("#changeLuckLevel").modal("hide");
	                    $("div.modal-body .custom_msg").show();
	                    $("div.modal-body .custom_msg").text(data);
	                    $("div.modal-body :not(.custom_msg)").hide();
	                    $("#myModal").modal("show");
	                    $("#myModal .msgClose").one('click', function(){
	                        $("button.btn-primary").trigger("click");
	                    });
	                }
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
  	<img id="large" src="" style="display: none; width: 460px;height: 460px;position: fixed;top: 24%;left: 34%;z-index:1;">
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a>首页</a></li>
        	<li class="current"><a>消费者管理</a></li>
        	<li class="current"><a title="">幸运用户</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">幸运用户查询</span></div>
                   <div class="col-md-10 text-right">
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/user/showUserInfoList.do">
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
	                                <input name="userKey" class="form-control input-width-larger" autocomplete="off" maxlength="40"/>
                                </div>
                                <div class="search-item">
	                                <label class="control-label">昵称：</label>
	                                <input name="nickName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">手机号：</label>
                                    <input name="phoneNumber" class="form-control input-width-larger" autocomplete="off" maxlength="11"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">用户类型：</label>
                                    <select name="isLuckyUser" class="form-control input-width-larger search" autocomplete="off" >
                                        <option value="">全部</option>
                                        <option value="0">普通用户</option>
                                        <option value="1" selected="selected">幸运用户</option>
                                        <option value="2">餐饮服务员</option>
                                        <option value="3">扫码专员</option>
                                        <option value="4">回收站用户</option>
                                        <option value="5">非黑名单用户</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">幸运用户列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:5%;">序号</th>
					            	<th style="width:10%;">头像</th>
                                    <th style="width:25%;" data-ordercol="c.user_key">用户ID</th>
					            	<th style="width:10%;" data-ordercol="c.nick_name">昵称</th>
					            	<th style="width:10%;" data-ordercol="c.phone_number">手机号</th>
					            	<th style="width:10%;" data-ordercol="c.is_lucky_user">用户类型</th>
					            	<th style="width:15%;" data-ordercol="c.update_time">更新时间</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:15%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="result" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;"><span>${idx.count}</span></td>
					        		<td style="text-align:center;">
					        			<span>
					        				<c:if test="${not empty(result.headImgUrl)}">
					        				<img onmouseover="imgBig('${result.headImgUrl}');" onmouseleave="imgSmall();" 
					        						style="width: 50px;height: 50px; cursor: pointer;" alt="头像" src="${result.headImgUrl}"/>
					        				</c:if>
					        			</span>
					        		</td>
                                    <td title="${result.openid}" class="userKey">
                                        ${result.userKey}
                                    </td>
					        		<td>
					        			<span>${result.nickName}</span>
					        		</td>
					        		<td>
					        			<span>${result.phoneNumber}</span>
					        		</td>
					        		<td>
					        		    <c:choose>
					        		        <c:when test="${result.isLuckyUser eq '0'}"><span>普通用户</span></c:when>
					        		        <c:when test="${result.isLuckyUser eq '1'}"><span>幸运用户</span></c:when>
					        		        <c:when test="${result.isLuckyUser eq '2'}"><span>餐饮服务员</span></c:when>
					        		        <c:when test="${result.isLuckyUser eq '3'}"><span>扫码专员</span></c:when>
					        		        <c:when test="${result.isLuckyUser eq '4'}"><span>回收站用户</span></c:when>
					        		        <c:when test="${result.isLuckyUser eq '5'}"><span>非黑名单用户</span></c:when>
					        		    </c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(result.updateTime, 0, 19)}</span>
					        		</td>
                                    <c:if test="${currentUser.roleKey ne '4'}">
						        		<td style="text-align:center;">
				        				     <a class="btn btn-xs luckLevel btn-danger" data-key="${result.userKey}"><i class="iconfont icon-jibie" style="font-size: 14px;"></i>&nbsp;设置用户级别</a>
						        		</td>
                                    </c:if>
					        	</tr>
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
    
    <div class="modal fade" id="changeLuckLevel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"  data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">设置幸运用户级别</h4>
                </div>
                <div class="modal-body">
                    <table class="lucklevel-table">
                        <tbody>
                        <tr>
                            <td style="width:20%;"></td>
                            <td style="width:80%;">
                                <span class="valid-place"></span>
                            </td>
                        </tr>
                        <tr>
                            <td style="width:20%;">幸运用户级别：<span class="required">*</span></td>
                            <td style="width:80%;">
	                            <select name="isLuckyUser" class="form-control input-width-large lucklevel-list-value" autocomplete="off" >
	                               <option value="0">普通用户</option>
	                               <option value="1">幸运用户</option>
	                               <option value="2">餐饮服务员</option>
	                               <option value="3">扫码专员</option>
	                               <option value="4">回收站用户</option>
	                               <option value="5">非黑名单用户</option>
	                           </select>
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
                    <button type="button" class="btn btn-default do-add-lucklevel btn-red" data-key="" data-dismiss="">确 认</button>
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
      				<h6 class="edit_success">编辑成功</h6>
      				<h6 class="edit_fail">编辑失败</h6>
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
