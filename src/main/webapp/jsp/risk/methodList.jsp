<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.dbt.framework.securityauth.PermissionCode"%>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib uri="/WEB-INF/tlds/securitytag.tld" prefix="st"%>
<!DOCTYPE html>
<% 
String cpath = request.getContextPath();
%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/jquery.uniform.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script>
    $(document).ready(function () {
        //返回主页消息提示
        var refresh = "${refresh}";
        if(refresh){
            if(refresh == "addFalse"){
                $("div.modal-body .addFalse").show();
                $("div.modal-body :not(.addFalse)").hide();
                $("#myModal").modal("show");
            }else if(refresh == "addSuccess"){
            	$("div.modal-body .addSuccess").show();
                $("div.modal-body :not(.addSuccess)").hide();
                $("#myModal").modal("show");
            }else if(refresh == "deleteFalse"){
            	$("div.modal-body .deleteFalse").show();
                $("div.modal-body :not(.deleteFalse)").hide();
                $("#myModal").modal("show");
            }else if(refresh == "deleteSuccess"){
            	$("div.modal-body .deleteSuccess").show();
                $("div.modal-body :not(.deleteSuccess)").hide();
                $("#myModal").modal("show");
            }else{
            	 $("div.modal-body .deleteFalse").html(refresh);
            	 $("div.modal-body .deleteFalse").show();
                 $("div.modal-body :not(.deleteFalse)").hide();
                 $("#myModal").modal("show");
            }
        }
        
        // 新增
		$("#addMethod").click(function(){
			var url = "<%=cpath%>/riskMethod/methodAdd.do";
			$("form").attr("action", url);
			$("form").submit();
		});
		// 新增
		$("#syncData").click(function(){
			$.ajax({
				url:'<%=cpath%>/riskMethod/syncData.do',
				async: false,
				type: 'POST',
					beforeSend:appendVjfSessionId,
                    success: function(data){
						alert(data);
					},
					error:function(data){
						alert('同步失败');
	     			 }
			});
		});
        
    });
   function editMethod(infoKey){
	   var url = "<%=cpath%>/riskMethod/getMethodDetail.do?infoKey="+infoKey;
	   $("form").attr("action", url);
       $("form").submit();
   }
   function delMethod(infoKey){
	   if(confirm("确认删除？")){
		   var url = "<%=cpath%>/riskMethod/delMethod.do?infoKey="+infoKey;
		   $("form").attr("action", url);
	       $("form").submit();
		}
   }
   function editGlue(infoKey){
	   var url = "<%=cpath%>/riskMethod/glueEdit.do?infoKey="+infoKey;
	   $("form").attr("action", url);
       $("form").submit();
   }
    </script>
    <style type="text/css">
    	table th{
    		text-align: center
    	}
    </style>
</head>
<body>
<div class="container">
    <!-- 面包屑begin -->
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 风控配置</a></li>
            <li class="current"><a> 接口配置</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">接口查询</span></div>
                   <div class="col-md-10 text-right">
                       <a id="addMethod" class="btn btn-blue">
                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增接口
                       </a>
                       <a id="syncData" class="btn btn-blue">
                           <i class="iconfont icon-xinxi" style="font-size: 12px;"></i>&nbsp; 同步缓存
                       </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/riskMethod/getMethodList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                    <div class="row">
                        <div class="col-md-12 ">
                            <div class="form-group little_distance search" style="line-height: 35px;">
                                <div class="search-item">
                                    <label class="control-label">接口地址：</label>
                                    <input name="methodName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">接口描述：</label>
                                    <input name="methodDesc" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                                   id="dataTable_data">
                                <input type="hidden" id="screenWidth1366" value="5,9,14,9,9,9,9,9,9,9,8" />
                                <thead>
                                    <tr>
                                        <th style="width: 5%">序号</th>
                                        <th style="width: 30%" data-ordercol="t.methodName" data-orderdef>接口名称</th>
                                        <th style="width: 30%" data-ordercol="t.methodDesc">接口描述</th>
                                        <th style="width: 10%" data-ordercol="t.methodStatus">风控状态</th>
                                        <th style="width: 25%" >操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                    <c:when test="${fn:length(secList) gt 0}">
                                    <c:forEach items="${secList}" var="sec" varStatus="status">
                                    <tr>
                                        <td class="text_center" style="vertical-align: middle;">${status.count}</td>
                                        
		                                <td style="vertical-align: middle;"><span>${sec.methodName}</span></td>
		                                <td style="vertical-align: middle;"><span>${sec.methodDesc}</span></td>
		                                <td style="vertical-align: middle;"><span>
		                                <c:choose>
                                        	<c:when test="${sec.methodStatus == '1'}">已暂停</c:when>
                                        	<c:when test="${sec.methodStatus == '0'}">开启中</c:when>
                                        </c:choose>
		                                </span></td>
		                                
		                                
                                        <td style="vertical-align: middle;">
	                                        <span>
	                                            <a class="btn btn-xs edit btn-danger btn-orange" href="#" onclick="editMethod('${sec.infoKey}');"><i class="iconfont icon-xiugai" style="font-size: 14px;"></i> 编 辑</a>
						        			</span>
						        			<span>
	                                            <a class="btn btn-xs del btn-danger btn-blue" onclick="delMethod('${sec.infoKey}');"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>  删 除</a>
						        			</span>
<%-- 						        			<c:if test="${sec.handleType == '2'}"> --%>
<!-- 							        			<span> -->
<%-- 		                                            <a class="btn btn-xs edit btn-danger btn-orange" href="#" onclick="editGlue('${sec.infoKey}');"><i class="iconfont icon-xiugai" style="font-size: 14px;"></i>  代 码</a> --%>
<!-- 							        			</span> -->
<%-- 						        			</c:if> --%>
					        			</td>
                                    </tr>
                                    </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                    <tr>
                                        <td colspan="11" style="text-align: left;">
                                        		<span>查无数据！</span>
                                        </td>
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
		    <div class="modal-content">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
      			<div class="modal-body" style="height:160px; overflow-y:auto;">
      			    <h6 class="addFalse">保存失败</h6>
      				<h6 class="addSuccess">保存成功</h6>
      				<h6 class="deleteFalse">删除失败</h6>
      				<h6 class="deleteSuccess">删除成功</h6>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
</div>
</body>
</html>