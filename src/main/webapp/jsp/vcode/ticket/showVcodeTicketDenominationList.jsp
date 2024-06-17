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
        var activityTicketLimit =  '';
        $(function(){
            var flag = "${flag}";
            if(flag != "") {
                $("div.modal-body ."+flag+"").show();
                $("div.modal-body :not(."+flag+")").hide();
                $("#myModal").modal("show");
            }

            $("#fileTr").css("display","none");

            $("select[name='ticketNo']").on("change",function(){
                checkOption($(this).val());
            });

            // 实现Collapse同时只展开一个的功能
            $(".paneltitle").on("click", function(){
                var collapse = $(this).attr("aria-controls");
                $("[id^='collapseInner']").each(function(obj){
                    if ($(this).attr("id") != collapse) {
                        $(this).removeClass("in");
                        $(this).addClass("collapse");
                    }
                });
            });

            // 新增
            $("#addTicketDenomination").click(function(){
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationEdit.do?infoKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/doTicketDenominationDel.do?infoKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });

            // 新增优惠券类型
            $("#addTicketCategory").click(function(){
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/showTicketCategoryAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 根据类型新增优惠券
            $("a.addTicketByCategory").click(function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationAdd.do?categoryKey1="+key;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑优惠券类型
            $("a.editCategory").off();
            $("a.editCategory").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/showTicketCategoryEdit.do?categoryKey1="+key;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 删除优惠券类型
            $("a.delCategory").off();
            $("a.delCategory").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/doTicketCategoryDel.do?categoryKey1="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });

            // 弹出导入面板
            $("a.import").off();
            $("a.import").on("click", function(){
                // 弹出对话框
                var key = $(this).parents("td").data("key");
                $("#qrcodeUploadDialog input.ticketInfoKey").val(key);
                $("#qrcodeUploadDialog").modal("show");
            });
            // 确定导入码包
            $("#qrcodeUploadDialog").delegate("#addBtn", "click", function(){

                $uploadFile = $("#qrcodeUploadDialog input.import-file");
                var files = $uploadFile.val();
                if(files == "") {
                    $.fn.alert("未选择任何文件，不能导入!");
                    return false;
                } else if(files.indexOf("csv") == -1) {
                    $.fn.alert("不是有效的csv文件");
                    return false;
                }

                // 弹出遮罩层
                $("#qrcodeFloatDiv").show();

                // 提交表单
                var url = "<%=cpath%>/vpsVcodeTicketDenomination/importFile.do";
                var formData = new FormData();

                formData.append("filePath", $uploadFile[0].files[0]);
                formData.append("infoKey", $("#qrcodeUploadDialog input.ticketInfoKey").val());
                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData,
                    dataType: "json",
                    async: true,
                    contentType : false,
                    processData: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        // 隐藏遮罩层
                        console.log(data['errMsg'])
                        $("#qrcodeFloatDiv").css("display","none");
                        $("#expressUploadDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });

        });

        function checkOption(ticketNo){
            if(activityTicketLimit !== ''){
                $("select[name='ticketLimit']").val("");
                $("#limitTr").css("display","none");
            }else{
                $("#limitTr").css("display","");
            }

            if(ticketNo.substring(1,2) == "U"
                || ticketNo.substring(1,2) == "Q"){
                $("#fileTr").css("display","none");
                $("#picTr").css("display","none");
                $("#noTr").css("display","none");
                $("#urlTr").css("display","");
            }else if(ticketNo.substring(1,2) == "P"){
                $("#fileTr").css("display","none");
                $("#urlTr").css("display","none")
                $("#noTr").css("display","none");
                $("#picTr").css("display","");
            }else if(ticketNo.substring(1,2) == "N"){
                $("#fileTr").css("display","none");
                $("#urlTr").css("display","none")
                $("#picTr").css("display","none");
                $("#noTr").css("display","");
            }else{
                $("#urlTr").css("display","none");
                $("#fileTr").css("display","");
            }

            // 青啤优惠券目前只支持活动期间只中一次
            /* if(ticketNo.substring(0,1) == "J"
                && $("#limitTr").css("display") != 'none'){
                $("select[name='ticketLimit']").val("2");
            } */
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
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">优惠券活动列表</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <div class="row">
                            <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动查询</span></div>
                            <div class="col-md-10 text-right">
                                <c:if test="${currentUser.roleKey ne '4'}">
                                    <a id="addTicketCategory" class="btn btn-blue">
                                        <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;
                                        新增优惠券类型
                                    </a>&nbsp;&nbsp;&nbsp;
                                    <a id="addTicketDenomination" class="btn btn-blue">
                                        <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;
                                        新增优惠券
                                    </a>
                                </c:if>
                            </div>
                        </div>
                        <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                        <div class="widget-content form-inline">
                            <form class="listForm" method="post"
                                  action="<%=cpath%>/vpsVcodeTicketDenomination/showTicketDenominationList.do">
                                <input type="hidden" class="tableTotalCount" value="${showCount}" />
                                <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                                <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                                <input type="hidden" class="tableCurPage" value="${currentPage}" />
                                <input type="hidden" name="queryParam" value="${queryParam}" />
                                <input type="hidden" name="pageParam" value="${pageParam}" />
                                <div class="row">
                                    <div class="col-md-12 ">
                                        <div class="form-group little_distance search" style="line-height: 35px;">
                                            <div class="search-item">
                                                <label class="control-label">类型名称：</label>
                                                <input name="ticketName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                            </div>
                                            <div class="search-item">
                                                <label class="control-label">优惠券类型：</label>
                                                <select class="form-control input-width-larger search" tag="validate" name="categoryKey">
                                                    <option style="padding: 20px;" value="">全部</option>
                                                    <c:forEach items="${ticketCategoryList }" var="item">
                                                        <option value="${item.categoryKey }">${item.categoryName }</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <%--<div class="search-item">
                                                <label class="control-label">有效时间：</label>
                                                <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                                       tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                                <label class="">-</label>
                                                <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                                       tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                            </div>--%>
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
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:10%;">优惠券类型</th>
                                <th style="width:10%;">优惠券编号</th>
                                <th style="width:10%;">创建时间</th>
                                <th style="width:12%;">小程序ID</th>
                                <th style="width:18%;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="ticketCategory" varStatus="idx">
                                        <tr style="cursor:pointer">
                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}">
                                                <span>${ticketCategory.categoryName}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}">
                                                <span>${ticketCategory.categoryType}</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${ticketCategory.createTime }</span>
                                            </td>
                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}"
                                                style="text-align:center;">
                                                <span>${ticketCategory.jumpId }</span>
                                            </td>
                                            <c:choose>
                                                <c:when test="${ticketCategory.categoryTypeNum ge 950}">
                                                    <td data-key="${ticketCategory.categoryKey}" style="text-align: center;">
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td data-key="${ticketCategory.categoryKey}" style="text-align: center;">
                                                        <a class="btn btn-xs editCategory btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                                        <%--<a class="btn btn-xs delCategory btn-orange"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>--%>
                                                        <a class="btn btn-xs addTicketByCategory btn-orange"><i class="iconfont icon-tianjiajiahaowubiankuang"></i>&nbsp;新增优惠券</a>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>


                                        <tr style="background-color: white;"><td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">8</c:when><c:otherwise>7</c:otherwise></c:choose>" style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
                                            <div id="collapseInnerArea${idx.count}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${idx.count}">
                                                <div class="panel-body" style="padding:5px;">

                                                    <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
                                                        <thead>
                                                        <tr class="success" style="background-color: #f0d2d0;">
                                                            <th style="width:5%;">序号</th>
                                                            <th style="width:8%;">优惠券编号</th>
                                                            <th style="width:10%;">优惠券名称</th>
                                                            <th style="width:10%;">优惠券投放时间</th>
                                                            <th style="width:8%;">中出方式</th>
                                                            <th style="width:8%;">券码数量</th>
                                                            <th style="width:8%;">使用数量</th>
                                                            <th style="width:8%;">导入文件</th>
                                                            <th style="width:8%;">是否启用</th>
                                                            <c:choose>
                                                                <c:when test="${ticketCategory.categoryTypeNum ge 950}">
                                                                    <th style="width:30%;">优惠券说明</th>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <th style="width:30%;">操作</th>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:choose>
                                                            <c:when test="${fn:length(ticketCategory.sysTicketInfoList) gt 0}">
                                                                <c:forEach items="${ticketCategory.sysTicketInfoList}" var="ticketInfo" varStatus="tacketIdx">
                                                                    <tr>
                                                                        <td style="text-align:center;">
                                                                            <span>${tacketIdx.count}</span>
                                                                        </td>
                                                                        <td style="text-align:center;">${ticketInfo.ticketNo}</td>
                                                                        <td style="text-align:center;">${ticketInfo.ticketName}</td>
                                                                        <td style="text-align:center;">${ticketInfo.releaseStartDate} - ${ticketInfo.releaseEndDate}</td>
                                                                        <td style="text-align:center;">
                                                                            <c:set var="isCode" value="0"></c:set>
                                                                            <c:choose>
                                                                                <c:when test="${ ticketInfo.ticketType eq '0'}">
                                                                                    链接方式
                                                                                </c:when>
                                                                                <c:when test="${ ticketInfo.ticketType eq '1'}">
                                                                                    券码方式
                                                                                    <c:set var="isCode" value="1"></c:set>
                                                                                </c:when>
                                                                                <c:when test="${ ticketInfo.ticketType eq '2'}">
                                                                                    图片方式
                                                                                    <c:set var="isCode" value="0"></c:set>
                                                                                </c:when>
                                                                                <c:when test="${ ticketInfo.ticketType eq '3'}">
                                                                                    动态券码
                                                                                    <c:set var="isCode" value="0"></c:set>
                                                                                </c:when>
                                                                                <c:when test="${ ticketInfo.ticketType eq '4'}">
                                                                                    活动编码
                                                                                    <c:set var="isCode" value="0"></c:set>
                                                                                </c:when>
                                                                                <c:otherwise>其他方式</c:otherwise>
                                                                            </c:choose>
                                                                        </td>
                                                                        <td style="text-align:center;">
                                                                        <c:choose>
                                                                            <c:when test="${ ticketInfo.ticketType eq '1'}">
                                                                                ${ticketInfo.ticketCount}
                                                                            </c:when>
                                                                            <c:otherwise>--</c:otherwise>
                                                                        </c:choose>
                                                                        </td>
                                                                        <td style="text-align:center;">${ticketInfo.ticketUseCount}</td>
                                                                        <td style="text-align:center;">
                                                                        <c:choose>
                                                                            <c:when test="${ ticketInfo.ticketType eq '1'}">
                                                                                ${ticketInfo.fileName}
                                                                            </c:when>
                                                                            <c:otherwise>--</c:otherwise>
                                                                        </c:choose>
                                                                        </td>
                                                                        <td style="text-align:center;">
                                                                            <c:set var="isCode" value="0"></c:set>
                                                                            <c:choose>
                                                                                <c:when test="${ ticketInfo.ticketStatus eq '0'}">
                                                                                    停用
                                                                                </c:when>
                                                                                <c:when test="${ ticketInfo.ticketStatus eq '1'}">
                                                                                    启用
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    停用
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </td>
                                                                        <c:choose>
                                                                            <c:when test="${ticketCategory.categoryTypeNum ge 950}">
                                                                                <td data-key="${ticketInfo.infoKey}" style="text-align: center;">
                                                                                     <c:out value="${ticketInfo.couponDesc}" escapeXml="false" />
                                                                                </td>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <td data-key="${ticketInfo.infoKey}" style="text-align: center;">
                                                                                    <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                                                                    <a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
                                                                                    <c:if test="${ticketInfo.ticketType eq '1'}">
                                                                                        <a class="btn btn-xs import btn-red"><i class="iconfont icon-daoru"></i>&nbsp;导 入</a>
                                                                                    </c:if>
                                                                                </td>
                                                                            </c:otherwise>
                                                                        </c:choose>
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
                                                </div>
                                            </div>
                                        </td></tr>
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


    <div id="qrcodeFloatDiv" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
    </div>

    <div class="modal fade" id="qrcodeUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">导入券码文件</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">选择券码文件：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="ticketInfoKey" tag="validate"
                                           class="form-control ticketInfoKey  input-width-larger" style="display: none"/>
                                    <input type="file" class="import-file" id="expressFile" name="filePath" single style="padding-bottom: 15px; margin-left: 10px;"/>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
