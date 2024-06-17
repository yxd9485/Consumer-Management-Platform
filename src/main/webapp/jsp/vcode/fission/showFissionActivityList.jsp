<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
String pathPrefix = cpath + "/" + imagePathPrx;
String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
      <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
      <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script>
        $(function(){
            $.fn.confirms = function(content, callBack,btnCancelCallBack) {
                $("#modalConfirmDlg .modal-title").text("提示信息");
                $("#modalConfirmDlg .modal-body p").html(content);
                $("#modalConfirmDlg .modal-body p").css("display", "");
                $("#modalConfirmDlg #btnCancel").css("display", "");
                $("#modalConfirmDlg #btnSure").text("修改规则");
                $("#modalConfirmDlg #btnSure").unbind("click");
                $("#modalConfirmDlg #btnSure").on("click", function() {
                    $(this).unbind("click");
                    callBack();
                });
                $("#modalConfirmDlg #btnCancel").on('click', function () {
                    $(this).unbind("click");
                    btnCancelCallBack();
                });

                // 延迟100毫秒定位弹框的位置
                setTimeout(function(){
                    $("#modalConfirmDlg").modal("show");
                    var dialogH = $("#modalConfirmDlg").height();
                    if (dialogH > screen.height) {
                        if ($("#mainFrame", window.parent.parent.document).size() > 0) {
                            $("#modalConfirmDlg .modal-content").css("top", ($("#mainFrame", window.parent.parent.document).contents().scrollTop() + screen.height * 0.0) + "px");
                        }
                    }
                }, 100);
            }
            // 初始化模板状态显示样式
            $(".stateFlag").bootstrapSwitch({
                onSwitchChange:function(event,state) {
                    var key = $(this).parents("td").data("key");
                    $.ajax({
                        url : "${basePath}/activateRedEnvelopeRuleCog/checkRule.do",
                        data:{
                            "ruleKey":key
                        },
                        type : "POST",
                        dataType : "json",
                        async : false,
                        beforeSend: appendVjfSessionId,
                        success: function (data) {
                            if (data == "0") {
                                $.fn.confirms("中出规则未配置！请配置完成后修改!", function() {
                                    // $("a.rule").trigger("click");
                                    var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=7";
                                    $("form").attr("action", url);
                                    $("form").submit();
                                },function () {
                                    $("button.btn-primary").trigger("click");
                                });
                                return;
                            }
                            if(state==true){
                                $("input:hidden[name='stateFlag']").val("1");
                            }else{
                                $("input:hidden[name='stateFlag']").val("0");
                            }
                            updateStateFlag(key);
                        }
                    });
                }
            });
            // 新增
            $("#addActivity").click(function(){
                var url = "<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityEdit.do?activityKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 查看
            $("a.view").off();
            $("a.view").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityView.do?activityKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });
            // 规则
            $("a.rule").off();
            $("a.rule").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=7";
                $("form").attr("action", url);
                $("form").submit();
            });
            // 信息查看
            $("a.dataView").off();
            $("a.dataView").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/activatePrizeRecord/showQueryList.do?activityKey=" + key + "&activityType=7";
                $("form").attr("action", url);
                $("form").submit();
            });
        });
        
        function updateStateFlag(key) {
            $.ajax({
                url : "${basePath}/activateRedEnvelopeRuleCog/updateState.do",
                data:{
                    "activityKey":key,
                    "stateFlag": $("input:hidden[name='stateFlag']").val()
                },
                type : "POST",
                dataType : "json",
                async : true,
                beforeSend: appendVjfSessionId,
                success: function (data) {
                    $.fn.alert("状态修改成功", function () {
                        $("button.btn-primary").trigger("click")
                    })
                }
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
            <li class="current"><a> 分享裂变活动配置</a></li>
            <li class="current"><a title="">
                <c:if test="${tabsFlag eq '1'}">待上线</c:if>
                <c:if test="${tabsFlag eq '2'}">已上线</c:if>
                <c:if test="${tabsFlag eq '3'}">已下线</c:if>
                <c:if test="${tabsFlag eq '4'}">全部活动</c:if>
            </a></li>
        </ul>
    </div>

        <div class="row">
            <div class="col-md-12 tabbable">
                <a href="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=0"
                   class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">待上线</a>
                <a href="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1"
                   class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">已上线</a>
                <a href="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2"
                   class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">已下线</a>
                <a href="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3"
                   class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">全部活动</a>
            </div>
        </div>

    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">分享裂变活动配置查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addActivity" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增活动
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
                          action="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do">
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
                            <div class="form-group little_distance search"  style="line-height: 35px;">
                                <div class="search-item">
                                    <label class="control-label">活动编号查询：</label>
                                    <input name="ruleNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">活动名称：</label>
                                    <input name="ruleName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">启用状态：</label>
                                    <label>
                                        <select name="stateFlag" class="form-control input-width-larger search" autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">停用</option>
                                            <option value="1">启用</option>
                                        </select>
                                    </label>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">数据列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,5,9,8,8,8,8,8,8,8,8,8,8" />
                            <thead>
                            <tr>
                                <th style="width:4%;">序号</th>
                                <th style="width:8%;" data-ordercol="rule_no">活动编号查询</th>
                                <th style="width:8%;" data-ordercol="rule_name">活动名称</th>
                                <th style="width:10%;" data-ordercol="start_date">活动日期范围</th>
                                <th style="width:12%;" >分享活动</th>
                                <th style="width:12%;" >激活SKU</th>
                                <th style="width:6%;" >分享类型</th>
                                <th style="width:4%;" data-ordercol="state_flag">启用状态</th>
                                <th style="width:8%;" data-ordercol="create_time" data-orderdef>创建时间</th>
                                <th style="width:10%;">操作</th>
                            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="activity" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.ruleNo}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${activity.ruleName}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>从&nbsp;${activity.startDate}</span>
                                                <span>到&nbsp;${activity.endDate}</span>
                                            </td>
                                            <td style="text-align: left;">
                                                <c:forEach items="${fn:split(activity.shareVcodeActivityKey, ',')}" var="activityName" varStatus="idx">
                                                    <span title="">
                                                            ${idx.count}、${activityName}
                                                    </span>
                                                    <br/>
                                                </c:forEach>
                                            </td>
                                            <td style="text-align: left;">
                                                <c:forEach items="${fn:split(activity.activationSkuKeyList, ',')}" var="skuName" varStatus="idx">
                                                    <span title="">
                                                            ${idx.count}、${skuName}
                                                    </span>
                                                    <br/>
                                                </c:forEach>
                                            </td>
                                            <td style="text-align: center;">
                                                    <span title="">
                                                        <c:if test="${activity.shareType == 1}">二维码分享</c:if>
                                                        <c:if test="${activity.shareType == 0}">海报分享(现金红包)</c:if>
                                                        <c:if test="${activity.shareType == 3}">海报分享(待激活红包)</c:if>
                                                    </span>
                                            </td>
                                            <td  style="text-align: center;" data-key="${activity.activityKey}">
                                                    <input name="stateFlag" type="hidden" class="form-control" value="${activity.stateFlag}" />
                                                    <input type="checkbox"  class="form-control stateFlag" <c:if test="${activity.stateFlag eq '1'}">checked</c:if> data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                            </td>
                                            <td style="text-align:center;">
                                                    <%--												<span>${fn:substring(activity.createTime, 0, 19)}</span>--%>
                                                <span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
                                                                                            value="${activity.createTime}" /></span>
                                            </td>
                                            <td data-key="${activity.activityKey}" style="text-align: center;">
                                                <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                            <%--    <a class="btn btn-xs view btn-red"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;查 看</a>--%>
                                                <a class="btn btn-xs rule btn-brownness btn-blue"> <i class="iconfont icon-xiugai"></i>&nbsp;规 则 </a>
                                                <a class="btn btn-xs dataView btn-blue"> <i class="iconfont icon-xinxi"></i>&nbsp;中出数据 </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9"><span>查无数据！</span></td>
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
                    <h6 class="addFalse">保存失败</h6>
                    <h6 class="addSuccess">保存成功</h6>
                    <h6 class="deleteFalse">删除失败</h6>
                    <h6 class="deleteSuccess">删除成功</h6>
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
