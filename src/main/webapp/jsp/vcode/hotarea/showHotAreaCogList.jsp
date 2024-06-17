<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<% 
    String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=7"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    
    <script>
        $(function(){
        	// 初始化界面
			initPage();
        	
            var flag = "${flag}";
            if(flag != "") {
                $("div.modal-body ."+flag+"").show();
                $("div.modal-body :not(."+flag+")").hide();
                $("#myModal").modal("show");
            }
            
         	// 初始化省市县
            $("div.area").initZone("<%=cpath%>", true, "${areaCode}", true, false, false, false);
            
            // 新增热区
            $("#addHotArea").click(function(){
                var url = "<%=cpath%>/vcodeActivityHotArea/showHotAreaCogAdd.do";
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 区域下新增热区
            $("a.areaHotAreaAdd").off();
            $("a.areaHotAreaAdd").on("click", function(){
                var key = $(this).data("key");
                var url = "<%=cpath%>/vcodeActivityHotArea/showHotAreaCogAdd.do?areaCode="+key;
                $("form").attr("action", url);
                $("form").submit(); 
                return false;
            });
            
            // 区域删除
            $("a.areaHotAreaDel").off();
            $("a.areaHotAreaDel").on("click", function(){
                var key = $(this).data("key");
                var url = "<%=cpath%>/vcodeActivityHotArea/doHotAreaForAreaCodeDelete.do?areaCode="+key;
            	$.fn.confirm("确定要删除此区域及其热区吗？", function() {
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            // 修改
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityHotArea/showHotAreaCogEdit.do?hotAreaKey="+key;
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityHotArea/doHotAreaCogDelete.do?hotAreaKey="+key;
               	$.fn.confirm("确定要删除吗？", function() {
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            // 查看
            $("a.view").off();
            $("a.view").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityHotArea/showHotAreaCogView.do?hotAreaKey="+key;
                $("form").attr("action", url);
                $("form").submit(); 
                return false;
            });
            
            // 实现Collapse同时只展开一个的功能
//             $(".paneltitle").on("click", function(){
//                 var collapse = $(this).attr("aria-controls");
//                 $("[id^='collapseInner']").each(function(obj){
//                     if ($(this).attr("id") != collapse) {
//                         $(this).removeClass("in");
//                         $(this).addClass("collapse");
//                     }
//                 });
//             });
            
            //  默认第一个展开 
            // $("#collapseArea1").addClass("in");
            
        });
        
     	// 初始化界面
		function initPage() {
			// 回显查询条件
			var queryParamAry = "hotAreaName".split(",");
			$.each("${queryParam}".split(","), function(i, obj) {
				$("div.form-group").find(":input[name='" + queryParamAry[i] +"']:not(:hidden)").val(obj);
			});
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
            <li class="current"><a> 活动管理</a></li>
        	<li class="current"><a> 超级热区</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">超级热区查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addHotArea" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增热区
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/vcodeActivityHotArea/showHotAreaList.do">
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                <div class="row">
			            <div class="col-md-12">
		                    <div class="form-group little_distance search area">
                                <div class="search-item">
			                        <label class="control-label">热区名称：</label>
			                        <input name="hotAreaName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item" style="width: 485px !important;">
			                        <label class="control-label">所属区域：</label>
	                                <select name="provinceCode" class="zProvince form-control input-width-normal search"></select>
	                                <select name="cityCode" class="zCity form-control input-width-normal search"></select>
	                                <select name="countyCode" class="zDistrict form-control input-width-normal search"></select>
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
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <div class="panel-group" id="accordionArea">
                            <c:choose>
                            <c:when test="${fn:length(resultList) gt 0}">
	                            <c:forEach items="${resultList}" var="areaCodeCog" varStatus="idxArea">
							    <div class="panel panel-default">
							        <div class="panel-heading" data-toggle="collapse" data-parent="#accordionArea" href="#collapseArea${idxArea.count}" >
							           <span>${areaCodeCog.areaName}</span>
							           <div style="display: block; float: right;">
			                               <c:if test="${currentUser.roleKey ne '4'}">
			                                   <a class="btn btn-xs areaHotAreaAdd btn-orange" data-key="${areaCodeCog.areaCode}" ><i class="iconfont icon-xiugai"></i>&nbsp;添加热区</a>
			                                   <a class="btn btn-xs areaHotAreaDel btn-red" data-key="${areaCodeCog.areaCode}"><i class="iconfont icon-lajixiang"></i>&nbsp;删除区域</a>
			                               </c:if>
							           </div>
							        </div>
							        <div id="collapseArea${idxArea.count}" class="panel-collapse collapse">
							            <div class="panel-body" style="padding: 10px;">
					                        <table id="dataTable_data" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top">
					                            <thead>
					                                <tr>
					                                    <th style="width:5%;">序号</th>
					                                    <th style="width:80%;">热区名称</th>
					                                    <th style="width:15%;">操作</th>
					                                </tr>
					                            </thead>
					                            <tbody>
					                                <c:choose>
					                                <c:when test="${fn:length(areaCodeCog.hotAreaCogList) gt 0}">
				                                         <c:forEach items="${areaCodeCog.hotAreaCogList}" var="hotAreaCog" varStatus="idxHotArea">
					                                        <tr>
					                                            <td style="text-align:center; border-top-width: 0px;"> 
					                                                <span>${idxHotArea.count}</span>
					                                            </td>
					                                            <td style="text-align:left; border-top-width: 0px;"> 
	                                                                <span>${hotAreaCog.hotAreaName}</span>
	                                                            </td>
					                                            <c:if test="${currentUser.roleKey ne '4'}">
					                                                <td data-key="${hotAreaCog.hotAreaKey}" style="text-align:left; border-top-width: 0px">
					                                                    <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					                                                    <a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
					                                                </td>
					                                            </c:if>
	                                                            <c:if test="${currentUser.roleKey eq '4'}">
	                                                                <td data-key="${hotAreaCog.hotAreaKey}" style="text-align:left; border-top-width: 0px">
	                                                                    <a class="btn btn-xs view btn-blue"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;查 看</a>
	                                                                </td>
	                                                            </c:if>
					                                        </tr>
				                                         </c:forEach>
					                                </c:when>
					                                <c:otherwise>
					                                    <tr>
					                                        <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">3</c:when><c:otherwise>2</c:otherwise></c:choose>"><span>查无数据！</span></td>
					                                    </tr>
					                                </c:otherwise>
					                                </c:choose>
					                            </tbody>
					                        </table>
				                        </div>
						           </div>
						        </div>
						        </c:forEach>
					       </c:when>
	                       <c:otherwise>
                             <div class="panel panel-default">
                                <div class="panel-heading" style="text-align: center;">
                                   <span>暂无热区配置</span>
                                </div>
                             </div>
	                       </c:otherwise>
	                       </c:choose>
				       </div>
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
                <div style="padding-left: 20px; padding-top: 10px">${errorMsg}</div>
                <div class="modal-body">
                    <h6 class="add_success">添加成功</h6>
                    <h6 class="add_fail">添加失败</h6>
                    <h6 class="edit_success">编辑成功</h6>
                    <h6 class="edit_fail">编辑失败</h6>
                    <h6 class="import_success">${areaName}保存成功</h6>
                    <h6 class="import_fail">${areaName}保存失败</h6>
                    <h6 class="not_delete">请至少选择一条数据进行删除</h6>
                    <h6 class="is_delete">删除成功</h6>
                    <h6 class="xx_delete">删除失败</h6>
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
