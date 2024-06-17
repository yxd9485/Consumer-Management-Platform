<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
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
            //查询所有
            $("#queryAll").click(function(){
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            //查询2020通用版
            $("#queryA").click(function(){
                $("input[name='picGroup']").val("1");
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            $("#queryB").click(function(){
                $("input[name='picGroup']").val("2");
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            $("#queryC").click(function(){
                $("input[name='picGroup']").val("3");
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            $("#queryD").click(function(){
                $("input[name='picGroup']").val("4");
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            $("#queryE").click(function(){
                $("input[name='picGroup']").val("5");
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            $("#queryF").click(function(){
                $("input[name='picGroup']").val("6");
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            $("#queryG").click(function(){
                $("input[name='picGroup']").val("7");
                var url = "<%=cpath%>/picLib/picLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
        });
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
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form class="listForm" method="post" action="<%=cpath%>/picLib/picLibList.do">
                <input type="hidden" class="tableTotalCount" value="${showCount}" />
                <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                <input type="hidden" class="tableCurPage" value="${currentPage}" />
                <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                <input type="hidden" class="tableOrderType" value="${orderType}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="group" value="${group}"/>
            </form>
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,14,9,13,9,9,9,9,6,9" />
                            <input type="hidden" name="btnName" id="btnName" value="">
                            <tbody>
                            <!--gt大于 lt小于-->
                            <div id="checkboxDiv" >
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        ${idx.count % 5 eq 1 ? "<tr>" : "" }
                                        <td  width="10%"  style= "word-break:break-all" >
                                                <input name="itemCB" type="radio" value="${item.picUrl}" />
                                                <div name = "picUrl"><img src="https://img.vjifen.com/images/vma/${item.picUrl}" class="imgzoon" style="height: 80px;width: 170px;"></div>
                                                <div name = "picName" >${item.picName}</div>
                                                <div name = "picWidth" style="display: none;">${item.picWidth}</div>
                                                <div name = "picHeight" style="display: none;">${item.picHeight}</div>
                                                <div name = "picX" style="display: none;">${item.picX}</div>
                                                <div name = "picY" style="display: none;">${item.picY}</div>
                                        </td>
                                        ${idx.count % 5 eq 0 ?"</tr>" : "" }
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="13"><span>查无数据！</span></td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </div>
                            </tbody>
                        </table>
                        <table id="pagination"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
