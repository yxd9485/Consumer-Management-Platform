<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.dbt.framework.util.PropertiesUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
    String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>

<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>

    <script>
        $(function () {

            // 查看
            $("a.btnView").off();
            $("a.btnView").on("click", function () {
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/turntable/showMajorInfoView.do?infoKey=" + key;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 查询结果导出
            $(".export").on("click", function(){
                $("form").attr("action", "<%=cpath%>/turntable/exportTurnTableRecordList.do");
                $("form").submit();
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/turntable/showTurnTableRecordList.do");
            });
        });


        // 查询组织机构
        function checkDmRegionAry(dmRegion,level){
            var $departmentDiv = $(dmRegion).parent(".department");
            var departmentId = $(dmRegion).val();
            if(departmentId == '' || departmentId == null){
                if (level == '-1'){
                    $departmentDiv.find("select[name='depProvinceId']").html("<option value=''>全部省区</option>");
                    $departmentDiv.find("select[name='depMarketId']").html("<option value=''>全部市场</option>");
                    $departmentDiv.find("select[name='depCountyId']").html("<option value=''>全部区县</option>");
                } else if (level == '6'){
                    $departmentDiv.find("select[name='depMarketId']").html("<option value=''>全部市场</option>");
                    $departmentDiv.find("select[name='depCountyId']").html("<option value=''>全部区县</option>");
                } else if (level == '7'){
                    $departmentDiv.find("select[name='depCountyId']").html("<option value=''>全部区县</option>");
                }
                return;
            }

            $.ajax({
                type: "POST",
                url: "<%=cpath%>/mnDepartment/queryDepartmentInfoByPid.do",
                async: false,
                data: {"pid":departmentId,"level":level},
                dataType: "json",
                beforeSend:appendVjfSessionId,
                success:  function(result){
                    if (level == '-1'){
                        var content = "<option value=''>全部省区</option>";
                    } else if (level == '6'){
                        var content = "<option value=''>全部市场</option>";
                    } else if (level == '7'){
                        var content = "<option value=''>全部区县</option>";
                    }
                    if(result){
                        $.each(result, function(i, item) {
                            if (i >= 0 && item.mdgprovinceid != '' && item.mdgprovinceid != null) {
                                content += "<option value='"+item.id+"'>"+item.dep_name+"</option>";
                            }
                        });
                    }
                    if (level == '-1'){
                        $departmentDiv.find("select[name='depProvinceId']").html(content);
                        $departmentDiv.find("select[name='depMarketId']").html("<option value=''>全部市场</option>");
                        $departmentDiv.find("select[name='depCountyId']").html("<option value=''>全部区县</option>");
                    } else if (level == '6'){
                        $departmentDiv.find("select[name='depMarketId']").html(content);
                        $departmentDiv.find("select[name='depCountyId']").html("<option value=''>全部区县</option>");
                    } else if (level == '7'){
                        $departmentDiv.find("select[name='depCountyId']").html(content);
                    }
                },
                error: function(data){
                    $.fn.alert("组织机构回显错误!");
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
            <li class="current"><a title="">幸运转盘</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">中奖数据查询</span>
                    </div>
                    <div class="col-md-10 text-right">
                        <c:if test="${currentUser.roleKey ne '4'}">
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <hr style="height: 2px; margin: 10px 0px;"/>
                    </div>
                </div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="<%=cpath%>/turntable/showTurnTableRecordList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}"/>
                        <input type="hidden" class="tableStartIndex" value="${startIndex}"/>
                        <input type="hidden" class="tablePerPage" value="${countPerPage}"/>
                        <input type="hidden" class="tableCurPage" value="${currentPage}"/>
                        <input type="hidden" name="queryParam" value="${queryParam}"/>
                        <input type="hidden" name="pageParam" value="${pageParam}"/>
                        <input id="activityKey" type="hidden" name="activityKey" value="${activityKey}"/>
                        <div class="row">
                            <div class="col-md-12 ">
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">参与状态：</label>
                                        <select name="status" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="0">未中奖</option>
                                            <option value="1">已中奖</option>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">参与用户：</label>
                                        <input name="nickName" class="form-control input-width-larger"
                                               autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item" style="width: 600px !important">
                                        <label class="control-label">参与时间：</label>
                                        <input name="startDate" id="startDate"
                                               class="form-control input-width-medium Wdate search-date" style="width: 160px !important"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss ', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <label class="">-</label>
                                        <input name="endDate" id="endDate" style="width: 160px !important"
                                               class="form-control input-width-medium Wdate sufTime search-date"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">中奖信息：</label>
                                        <select name="prizeInfoKey" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">请选择</option>
                                            <c:if test="${vpsTurntablePrizeCogList != null}">
                                                <c:forEach items="${vpsTurntablePrizeCogList }" var="prize">
                                                    <option value="${prize.infoKey }" <c:if test="${prize.infoKey eq queryBean.prizeInfoKey}">selected</c:if>>${prize.turntablePrizeName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                    </div>
                                <c:if test="${currentUser.projectServerName eq 'mengniu'}">
                                    <div class="search-item department" style="width: 600px !important">
                                        <label class="control-label">组织架构：</label>
                                        <select name="depRegionId" class="zDmRegionAry form-control input-width-normal"
                                                onchange="checkDmRegionAry(this,-1)">
                                            <option value="">全部大区</option>
                                            <c:forEach items="${departmentList }" var="department">
                                                <option value="${department.id }" <c:if test="${department.id eq queryBean.depRegionId}">selected</c:if>>${department.dep_name }</option>
                                            </c:forEach>
                                        </select>
                                        <select name="depProvinceId"
                                                class="zDmProvinceAry form-control input-width-normal" onchange="checkDmRegionAry(this,6)">
                                            <option value="">全部省区</option>
                                            <c:if test="${depProvinceList != null}">
                                                <c:forEach items="${depProvinceList }" var="department">
                                                    <option value="${department.id }" <c:if test="${department.id eq queryBean.depRegionId}">selected</c:if>>${department.dep_name }</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <select name="depMarketId" class="zAgencyAry form-control input-width-normal" onchange="checkDmRegionAry(this,7)">
                                            <option value="">全部市场</option>
                                            <c:if test="${depMarketIdList != null}">
                                                <c:forEach items="${depMarketIdList }" var="department">
                                                    <option value="${department.id }" <c:if test="${department.id eq queryBean.depRegionId}">selected</c:if>>${department.dep_name }</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <select name="depCountyId" class="zAgencyAry form-control input-width-normal">
                                            <option value="">全部区县</option>
                                            <c:if test="${depCountyList != null}">
                                                <c:forEach items="${depCountyList }" var="department">
                                                    <option value="${department.id }" <c:if test="${department.id eq queryBean.depRegionId}">selected</c:if>>${department.dep_name }</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">所属经销商：</label>
                                        <input name="agencyName" class="form-control input-width-larger"
                                               autocomplete="off" maxlength="30"/>
                                    </div>
                                </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 text-center mart20">
                                <button type="button" class="btn btn-primary btn-blue">查 询</button>
                                <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
                                <button type="button" class="btn btn-reset export btn-red marl20">导 出</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">产品列表</span>
                    </div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width:5%;">序号</th>
                                <th style="width:7%;" >昵称</th>
                                <th style="width:7%;" >姓名</th>
                                <th style="width:9%;" >手机号</th>
                                <th style="width:10%;" >身份证号</th>
                                <c:if test="${currentUser.projectServerName eq 'mengniu'}">
                                    <th style="width:10%;">所属组织架构</th>
                                    <th style="width:10%;">所属经销商</th>
                                </c:if>
                                <th style="width:6%;">中奖信息</th>
                                <th style="width:6%;">中奖金额</th>
                                <th style="width:6%;">中奖积分</th>
                                <th style="width:9%;">奖项描述</th>
                                <th style="width:6%;">消耗积分</th>
                                <th style="width:12%;">抽奖位置</th>
                                <th style="width:9%;">中奖时间</th>
                                <c:if test="${currentUser.roleKey ne '4'}">
                                    <th style="width:10%;">操作</th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td>
                                                <span>${item.nickName}</span>
                                            </td>
                                            <td>
                                                <span>${item.realName}</span>
                                            </td>
                                            <td>
                                                <span>${item.phoneNumber}</span>
                                            </td>
                                            <td>
                                                <span>${item.idCard}</span>
                                            </td>
                                            <c:if test="${currentUser.projectServerName eq 'mengniu'}">
                                                <td>
                                                    <span>${item.depRegionName}-${item.depProvinceName}-${item.depMarketName}-${item.depCountyName}</span>
                                                </td>
                                                <td>
                                                    <span>${item.agencyName}</span>
                                                </td>
                                            </c:if>
                                            <td>
                                                <span>${item.turntablePrizeName}</span>
                                            </td>
                                            <td>
                                                <span>${item.earnMoney}</span>
                                            </td>
                                            <td>
                                                <span>${item.earnVpoints}</span>
                                            </td>
                                            <td>
                                                <span>${item.prizeDesc}</span>
                                            </td>
                                            <td>
                                                <span>${item.consumeVpoints}</span>
                                            </td>
                                            <td>
                                                <span>${item.province}-${item.city}-${item.county}-${item.address}</span>
                                            </td>
                                            <td>
                                                <span>${item.earnTime}</span>
                                            </td>
                                            <c:if test="${currentUser.roleKey ne '4'}">
                                                <td data-key="${item.prizeInfoKey}" style="text-align: center;">
                                                    <c:if test="${item.turntablePrizeType eq '2'}">
                                                        <a class="btn btn-xs btnView btn-orange"><i
                                                                class="iconfont icon-xiugai"></i>&nbsp发货信息</a>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">12</c:when><c:otherwise>10</c:otherwise></c:choose>">
                                            <span>查无数据！</span></td>
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
