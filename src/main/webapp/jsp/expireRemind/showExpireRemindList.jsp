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
                alert(key);
                var url = "<%=cpath%>/skuInfo/doSkuInfoDelete.do?skuKey="+key;
                    $.fn.confirm("确认删除吗？", function(){
                        $("form").attr("action", url);
                        $("form").submit();
                    });
            });
            // 取消置顶  cancel
            $("a.cancelstickMsg").off();
            $("a.cancelstickMsg").on("click", function(){
                /*
                                var key = $(this).parents("a").data("key");*/
                var infoKey=$(this).parent().data("key");



                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/expireRemindAction/stickMsg.do",
                    async: false,
                    data: {"infoKey":infoKey,"topFlag":"0" },
                    dataType: "text",
                    beforeSend:appendVjfSessionId,
                    success:  function(result){

                    },
                });
                /*       $("form").attr("action", url);
                       $("form").submit();*/
                /*	$.fn.confirm("确认删除吗？", function(){
                        $("form").attr("action", url);
                        $("form").submit();
                    });*/
                var url = "<%=cpath%>/expireRemindAction/expireRemindListCondition.do"
                $("form").attr("action",url );
                $("form").submit();

            });
            // 置顶
            $("a.stickMsg").off();
            $("a.stickMsg").on("click", function(){
/*
                var key = $(this).parents("a").data("key");*/
               var infoKey=$(this).parent().data("key");



                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/expireRemindAction/stickMsg.do",
                    async: false,
                    data: {"infoKey":infoKey,"topFlag":"1" },
                    dataType: "text",
                    beforeSend:appendVjfSessionId,
                    success:  function(result){

                    },
                });
         /*       $("form").attr("action", url);
                $("form").submit();*/
            /*	$.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });*/
                var url = "<%=cpath%>/expireRemindAction/expireRemindListCondition.do"
                $("form").attr("action",url );
                $("form").submit();
            });
            // 全选
            $("input[name='allInfoKey']").click(function () {
                if (this.checked) {
                    $("input[name='infoKey']").attr("checked", "true");
                    $("input[name='infoKey']").prop("checked", true);
                } else {
                    $("input[name='infoKey']").removeAttr("checked");
                    $("input[name='infoKey']").prop("checked", false);
                }
            });

            //标记已读
            $("a.no_read").off();//sign_read
            $("a.no_read").on("click",function(){
                var key = "";
                $("[name='infoKey']:checked").each(function() {
                    key = $(this).val() + "," + key;
                   var fathcer= $(this).parent().parent().parent()
                    $(this).parent().parent().parent().css("font-weight","bold");
                })
                if (key == "") {
                    $.fn.alert("请选择需要被标记的提醒消息!");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "<%=cpath%>/expireRemindAction/updateStatusForRead.do",
                        async: false,
                        data: {"keys":key,"status":0 },
                        dataType: "text",
                        beforeSend:appendVjfSessionId,
                    success:  function(result){

                        },
                      });
                    $("input[name='infoKey']").removeAttr("checked");
                    $("input[name='infoKey']").prop("checked", false);
        /*   var url = " /expireRemindAction/updateStatusForRead.do?keys="+key;
                    $("form").attr("action", url);
                    $("form").submit();*/
                }
            });
            //标记未读
            $("a.sign_read").off();//no_read
            $("a.sign_read").on("click",function(){
                var key = "";
                $("[name='infoKey']:checked").each(function() {
                    key = $(this).val() + "," + key;
                    var fathcer= $(this).parent().parent().parent()

                    $(this).parent().parent().parent().css("font-weight","");
                })
                if (key == "") {
                    $.fn.alert("请选择需要被标记的提醒消息!");
                } else {
                    $.ajax({
                        type: "POST",
                        url: "<%=cpath%>/expireRemindAction/updateStatusForRead.do",
                        async: false,
                        data: {"keys":key,"status":1},
                        dataType: "text",
                        beforeSend:appendVjfSessionId,
                    success:  function(result){

                        },
                    });
                    $("input[name='infoKey']").removeAttr("checked");
                    $("input[name='infoKey']").prop("checked", false);
                    /*   var url = " /expireRemindAction/updateStatusForRead.do?keys="+key;
                                $("form").attr("action", url);
                                $("form").submit();*/
                }
            });
            // 排序
            $("a.ordercol").on("click", function(){
                $("form").attr("action", "<%=cpath%>/expireRemindAction/expireRemindListCondition.do");
                $("input.tableOrderCol").val($(this).data("ordercol"));
                $("input.tableOrderType").val($(this).data("ordertype") == "desc" ? "asc" : "desc");
                
                // 跳转到首页
                doPaginationOrder();
            });
            // 修改父页面提醒消息
            window.parent.$("#msgRemind").html("消息提醒:"+${showCount});
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
            var queryParamAry = "activityName,activityNo,msgType,activityType,activityStartDate,activityEndDate".split(",");
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
        function turnMenuSign(menuUrl,infoKey){
            $.ajax({
                type: "POST",
                url: "<%=cpath%>/expireRemindAction/updateStatusForRead.do",
                async: false,
                data: {"keys":infoKey,"status":1},
                dataType: "text",
                beforeSend:appendVjfSessionId,
                    success:  function(result){

                },
            });
            
            //  提醒消息菜单栏跳转 start
            // 扫码活动
            if("0"==menuUrl){
                window.parent.openMenu("vcodeActivityshowVcodeActivityListdo");
            }
            // 一码双奖
            if("6"==menuUrl){
                window.parent.openMenu("doubleprizeshowDoublePrizeListdo");
            }
            // 捆绑促销
            if("5"==menuUrl){
                window.parent.openMenu("promotionshowBindPromotionListdo");
            }
            // 万能签到
            if("4"==menuUrl){
                window.parent.openMenu("vcodeSigninshowVcodeSigninListdo");

            }
            //  提醒消息菜单栏跳转 end
        }
    </script>
    <style>
        table.table tr th {
            text-align: center;
        }
        table.table tr td {
            vertical-align: middle;
        }
        .marr20 {
            margin-right: 10px !important;
        }
        .input-width-large {
            width: 240px !important;
        }
    </style>
  </head>

  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a title="">到期提醒</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">查询条件</span></div>

                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/expireRemindAction/expireRemindListCondition.do">
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
                                <div class="form-group little_distance search" style="line-height:35px;">
                                    <div class="search-item">
                                    <label class="control-label">活动名称：</label>
                                    <input name="activityName" class="form-control input-width-large " autocomplete="off" maxlength="100"/>
                                    </div>
                                    <div class="search-item">
                                    <label class="control-label">活动编号：</label>
                                    <input name="activityNo" class="form-control input-width-large " autocomplete="off" maxlength="100"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">提醒类型：</label>
                                        <select name="msgType" class="form-control input-width-large search " autocomplete="off" >
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">规则到期</option>
                                            <option value="1">剩余钱数</option>
                                            <option value="2">剩余积分</option>
                                            <option value="3">红包个数</option>
                                            <option value="4">批次结束</option>

                                        </select>
                                    </div>
                                    <div class="search-item">
                                    <label class="control-label">活动类型：</label>
                                    <select name="activityType" class="form-control input-width-large search " autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option value="0">扫码活动</option>
                                        <option value="4">万能签到活动</option>
                                        <option value="5">捆绑升级活动</option>
                                        <option value="6">一码双奖活动</option>

                                    </select>
                                    </div>
                                  
                                    <div class="search-item">
                                    <label class="control-label">活动时间：</label>
                                    <input name="activityStartDate" id="activityStartDate" 
                                           class="form-control input-width-medium Wdate search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" autocomplete="off" />
                                    <label class="">-</label>
                                    <input name="activityEndDate" id="activityEndDate" 
                                           class="form-control input-width-medium Wdate sufTime search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" autocomplete="off" />
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
                   <div class="col-md-2" style="width:9%;"><span style="font-size: 15px; color: white; font-weight: 
                   bold;">提醒消息</span></div>

                    <div class="col-md-8 text-left" style="width: 70%;"></div>
                    <div class="col-md-2 text-right" style="width: 20%;">
                        <a id="signRead" class="btn sign_read btn-blue ordercol marr20" >标记已读 &nbsp;</a>
                        <a id="signNoRead" class="btn no_read btn-blue ordercol"  >标记未读 &nbsp;</a>
                    </div>
                 <%--  <div class="col-md-8 text-right" style="width: 67%;">
                        <a id="orderValidDay" class="btn btn-red ordercol marr20" data-ordercol="activity_no" data-ordertype="desc">活动编号 &nbsp;<i class="iconfont icon-paixu-jiangxu"></i></a>
                        <a id="orderCreateTime" class="btn btn-red ordercol" data-ordercol="create_time" data-ordertype="desc">创建时间 &nbsp;<i class="iconfont icon-paixu-jiangxu"></i></a>
                   </div>--%>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="4,7,10,22,14,12,12,10,10,20" />
                            <thead>

                                <tr>
                                    <th style="width:4%; text-align: center;"><input type="checkbox" name="allInfoKey" id="allInfoKey" /></th>
                                    <th style="width:7%; text-align: center;"data-ordercol="ACTIVITY_NO">活动编号</th>
                                    <th style="width:10%; text-align: center;"data-ordercol="ACTIVITY_TYPE">活动类型</th>
                                    <th style="width:22%; text-align: center;"data-ordercol="MSG_TYPE">活动名称</th>
                                    <th style="width:14%; text-align: center;"data-ordercol="CREATE_TIME">活动时间</th>
                                    <th style="width:12%; text-align: center;"data-ordercol="MSG_TYPE">提醒类型</th>
                                    <th style="width:12%; text-align: center;"data-ordercol="MSG_CONTENT">通知备注</th>
                                    <th style="width:10%; text-align: center;" data-ordercol="UPDATE_TIME"data-orderdef>收件时间</th>
                                    <c:if test="${currentUser.roleKey ne '4'}">
                                        <th style="width:20%;">操作</th>
                                    </c:if>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                <c:when test="${fn:length(msgList) gt 0}">
                                <c:forEach items="${msgList}" var="expireRemind" varStatus="idx">
                                <tr   style="<c:if test="${expireRemind.readFlag == 0}">font-weight:bold;</c:if>">
                                    <td style="text-align: center;"><span><input name="infoKey" type="checkbox" value="${expireRemind.infoKey}" ></span></td>
                                    <td style="text-align: center;"><span>${expireRemind.activityNo}</span></td>
                                    <td style="text-align: center;">
                                                            <span>
                                                                <c:if test="${expireRemind.activityType == 0}">扫码活动</c:if>
                                                                <c:if test="${expireRemind.activityType == 4}">万能签到活动</c:if>
                                                                <c:if test="${expireRemind.activityType == 5}">捆绑升级活动</c:if>
                                                                <c:if test="${expireRemind.activityType == 6}">一码双奖活动</c:if>
                                                            </span>
                                    </td>
                                    <td style="text-align: center;"><span>${expireRemind.activityName}</span></td>
                                    <td style="text-align: center;">
                                        <span data-ref="remoney" data-value="${expireRemind.activityStartDate}-${recharge.activityEndDate}">${expireRemind.activityStartDate}至${expireRemind.activityEndDate}</span>
                                    </td>

                                    <td style="text-align: center;">
                                                            <span>
                                                                <c:if test="${expireRemind.msgType == 0}">规则到期</c:if>
                                                                <c:if test="${expireRemind.msgType == 1}">剩余钱数</c:if>
                                                                <c:if test="${expireRemind.msgType == 2}">剩余积分</c:if>
                                                                <c:if test="${expireRemind.msgType == 3}">红包个数</c:if>
                                                                <c:if test="${expireRemind.msgType == 4}">批次结束</c:if>
                                                            </span>
                                    </td>
                                    <td style="text-align: center;"><span>${expireRemind.msgContent}</span></td>
                                    <td style="text-align: center;"><span>${expireRemind.updateTime}</span></td>
                                    <c:if test="${currentUser.roleKey ne '4'}">
                                    <td data-key="${expireRemind.infoKey}" style="text-align: center;">
                                        <c:if test="${expireRemind.msgType eq 0}">
                                            <c:if test="${expireRemind.activityType eq 0}">
                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                            </c:if>
                                            <c:if test="${expireRemind.activityType ne 0}">
                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                            </c:if>
                                         
                                    </c:if>
                                        <c:if test="${expireRemind.msgType eq 1}">
                                            <c:if test="${expireRemind.activityType eq 0}">
                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                            </c:if>
                                            <c:if test="${expireRemind.activityType ne 0}">
                                                <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                            </c:if>
                                            </c:if>
                                        <c:if test="${expireRemind.msgType eq 2}">
                                           <c:if test="${expireRemind.activityType eq 0}">
                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                           </c:if>
                                            <c:if test="${expireRemind.activityType ne 0}">
                                                <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${expireRemind.msgType eq 3}">
                                            <c:if test="${expireRemind.activityType eq 0}">
                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                            </c:if>
                                            <c:if test="${expireRemind.activityType ne 0}">
                                                <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${expireRemind.msgType eq 4}">
                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenuSign('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/qrcodeBatchInfo/showBatchForActivityUsedList.do?vjfSessionId=${vjfSessionId}&infoKey=${expireRemind.vcodeActivityKey}&queryParam=,,,,,,,,,4" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a></c:if>

                                        <c:if test="${expireRemind.topFlag == 0}">
                                        <a class="btn btn-xs stickMsg btn-orange" value="${expireRemind.infoKey}"><i class="iconfont icon-xiugai"></i>&nbsp;置顶</a>
                                        </c:if>
                                        <c:if test="${expireRemind.topFlag == 1}">
                                            <a class="btn btn-xs cancelstickMsg btn-orange" value="${expireRemind.infoKey}"><i class="iconfont icon-xiugai"></i>&nbsp;取消置顶</a>
                                        </c:if>
                                    </td>
                                    </c:if>

                                </tr>
                                </c:forEach>
                                </c:when>
                                <c:otherwise>
                                <tr>
                                    <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">9</c:when><c:otherwise>8</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
