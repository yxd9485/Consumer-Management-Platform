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
        var clickBtn = function(grp){
            $("input[name='picGroup']").val(grp);
            $("#queryBtn").click();
        }
        $(function(){

            // 新增
            $("#addItem").click(function(){
                var url = "<%=cpath%>/picLib/showPicLibAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            //查询所有
            $("#queryAll").click(function(){
                var url = "<%=cpath%>/picLib/showPicLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            //查询2020通用版
            $("#queryTY").click(function(){
                $("input[name='picGroup']").val("0");
                var url = "<%=cpath%>/picLib/showPicLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });
            $("#reset").click(function(){
                $("input[name='queryParam']").val("");
                var url = "<%=cpath%>/picLib/showPicLibList.do";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/picLib/showPicLibEdit.do?key="+key;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var picGroup = $(this).parents("td").data("picGroup");
                var url = "<%=cpath%>/picLib/doPicLibDel.do?key="+key+"&picGroup ="+picGroup;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            // 批量删除
            $("#delItem").click(function(){
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                })
                if (key == "") {
                    $.fn.alert("请选择要删除的素材!");
                } else {
                    var url = "<%=cpath%>/picLib/doPicLibDel.do?key=" + key;
                    $("form").attr("action", url);
                    $("form").submit();
                }
            });

            // 全选
            $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='itemCB']").prop("checked","checked");
                } else {
                    $("[name='itemCB']").prop("checked","");
                }
            });
            // 弹出批次调整窗口
            $("a.editGroup").off();
            $("a.editGroup").click(function(){
                $("#adjustBatchDialog .modal-title").text("移动分组");
                $("#adjustBatchDialog input[name='info']").val($(this).parents("td").data("key"));
                $("#adjustBatchDialog").modal("show");
            });
            // 弹出批次调整窗口
            $("#updateItem").on("click", function(){
                $("#adjustBatchDialog .modal-title").text("移动分组");
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                })
                if (key == "") {
                    $.fn.alert("请选择要移动的素材!");
                }else{
                    $("#adjustBatchDialog").modal("show");
                }
            });
            // 确定调整批次
            $("#adjustBatchDialog").delegate("#addBtn", "click", function(){
                var key = "";
                $("#adjustBatchDialog").modal("hide");
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                })
                    var tips = "确定要移动分组吗?" ;
                    $.fn.confirm(tips, function(){
                        // 提交表单
                        var url = "<%=cpath%>/picLib/doBatchPicLibUpdate.do";
                        var paramJson = {};
                        $("#adjustBatchDialog :input[name]").each(function(){
                            paramJson[$(this).attr("name")] = $(this).val();
                        });
                        paramJson.keyJson = key;
                        $.ajax({
                            type: "POST",
                            url: url,
                            data: paramJson,
                            dataType: "json",
                            async: false,
                            beforeSend:appendVjfSessionId,
                            success: function(data) {
                                $("#adjustBatchDialog #closeBtn").trigger("click");
                                $.fn.alert(data['errMsg'], function(){
                                    $("button.btn-primary").trigger("click");
                                });
                            }
                        });
                        $("form").attr("action", url);
                        $("form").submit();
                    });
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
            <div class="widget box">
                <div class="row">
                    <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">素材库</span></div>
                    <div class="col-md-10 text-right">
                        <!-- 用户key不为空 ne -->
                        <a id="addItem" class="btn btn-blue">
                            <i class="iconfont icon-shangchuan" style="font-size: 12px;"></i>&nbsp; 上传
                        </a>
                        <a id="delItem" class="btn btn-red">
                            <i class="iconfont icon-yichu" style="font-size: 12px;"></i>&nbsp; 删除
                        </a>
                        <a id="updateItem" class="btn btn-blue">
                            <i class="iconfont icon-peizhi" style="font-size: 12px;"></i>&nbsp; 移动
                        </a>
                    </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <!--查询url  -->
                    <form class="listForm" method="post"
                          action="<%=cpath%>/picLib/showPicLibList.do">
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
                                <!--搜索栏  -->
                                <div class="form-group little_distance search" style="line-height: 35px;">
                                    <div class="search-item" style="width: 500px;">
                                        <label class="control-label">按文件名搜索： </label>
                                        <input name="keyword" class="form-control input-width-larger keyword" autocomplete="off" maxlength="100" placeholder = "按文件名搜索"/>
                                    </div>
                                    <div class="search-item">
                                        <input name="picGroup" class="form-control input-width-larger" autocomplete="off" maxlength="30" style="opacity: 0;" disabled="disabled"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 text-center mart20">
                                        <button type="button" class="btn btn-primary btn-blue" id="queryBtn">查 询</button>
                                        <button type="button" class="btn btn-reset btn-radius3 marl20"  id= "reset">重 置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 text-center mart20">
                                <a id="queryAll" class="btn btn-white">
                                    <i class="iconfont" style="font-size: 12px;"></i>&nbsp; 全部素材
                                </a>
                                <a id="queryTY" class="btn btn-white">
                                    <i class="iconfont" style="font-size: 12px;"></i>&nbsp; 2020通用版
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                    <div class="col-md-10">
                        <span style="font-size: 14px; color: black; font-weight: bold; cursor:pointer; " onclick="clickBtn('1');" >LOGO(${logoCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('2');">SLOGAN(${sloganCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('3');">背景图(${backCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('4');">红包样式(${redCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('5');">品牌产品(${proCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('6');">提示图(${tipCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('7');">按钮样式(${btnCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('8');">扫码弹窗(${adUpCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('9');">首页轮播(${adHomeCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('10');">商城轮播(${adShopCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('11');">活动规则(${actRuleCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('12');">弹窗跳转图片(${adUpPicCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('13');">实物奖(${prizePicCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('14');">阶梯弹窗(${ladderUiCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('15');">终端实物奖(${vmtsPrizeUiCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('16');">活动专区(${actZoneCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('17');">N元乐购实物奖(${nylgPrizeUiCount})</span>&nbsp;&nbsp;&nbsp;
                        <span style="font-size: 14px; color: black; font-weight: bold;cursor:pointer; " onclick="clickBtn('18');">公告图(${noticePicCount})</span>&nbsp;&nbsp;&nbsp;
                    </div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,14,9,13,9,9,9,9,6,9" />
                            <thead>
                            <tr>
                                <th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
                                <th style="width:4%;">序号</th>
                                <th style="width:12%;">名称</th>
                                <th style="width:7%;">分组</th>
                                <th style="width:7%;">图片</th>
                                <th style="width:8%;" data-ordercol="create_time">创建时间</th>
                                <th style="width:10%;" >默认模板</th>
                                <th style="width:15%;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <!--gt大于 lt小于-->
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;">
                                                <input name="itemCB" type="checkbox" value="${item.infoKey}" />
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
                                            <td>
                                                <span>${item.picName}</span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.picGroup eq '1'}">LOGO</c:when>
                                                    <c:when test="${item.picGroup eq '2'}">SLOGAN</c:when>
                                                    <c:when test="${item.picGroup eq '3'}">背景图</c:when>
                                                    <c:when test="${item.picGroup eq '4'}">红包样式</c:when>
                                                    <c:when test="${item.picGroup eq '5'}">品牌产品</c:when>
                                                    <c:when test="${item.picGroup eq '6'}">提示图</c:when>
                                                    <c:when test="${item.picGroup eq '7'}">按钮样式</c:when>
                                                    <c:when test="${item.picGroup eq '8'}">扫码弹窗</c:when>
                                                    <c:when test="${item.picGroup eq '9'}">首页轮播</c:when>
                                                    <c:when test="${item.picGroup eq '10'}">商城轮播</c:when>
                                                    <c:when test="${item.picGroup eq '11'}">活动规则</c:when>
                                                    <c:when test="${item.picGroup eq '12'}">弹窗跳转图片</c:when>
                                                    <c:when test="${item.picGroup eq '13'}">实物奖</c:when>
                                                    <c:when test="${item.picGroup eq '14'}">阶梯弹窗</c:when>
                                                    <c:when test="${item.picGroup eq '15'}">终端实物奖</c:when>
                                                    <c:when test="${item.picGroup eq '16'}">活动专区</c:when>
                                                    <c:when test="${item.picGroup eq '17'}">N元乐购实物奖</c:when>
                                                    <c:when test="${item.picGroup eq '18'}">公告图</c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="https://img.vjifen.com/images/vma/${item.picUrl}" target="_blank">
                                                <span><img src="https://img.vjifen.com/images/vma/${item.picUrl}" style="height: 80px;width: 170px; float: left;"></span>
                                                </a>
                                            </td>
                                            <td>
                                                <span>${fn:substring(item.createTime, 0, 19)}</span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.picTemplate eq '0'}">2020通用版</c:when>
                                                </c:choose>
                                            </td>
                                            <td data-key="${item.infoKey}" style="text-align: center;">
                                                <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;编辑</a>
                                                <a class="btn btn-xs editGroup btn-orange"><i class="iconfont icon-peizhi"></i>&nbsp;移动分组</a>
                                                <a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="13"><span>查无数据！</span></td>
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
    <div class="modal fade" id="adjustBatchDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <h4 class="modal-title" id="myModalLabel">移动分组</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table" style="line-height: 30px;">
                        <input type="hidden" name="info" >
                        <tr>
                            <td class="ab_left">所属分组：<span class="required">*</span></td>
                            <td class="ab_main" colspan="3">
                                <div class="content firstRebate">
                                    <select name="picGroup" class="form-control input-width-larger search" autocomplete="off" >
                                        <option value="1">LOGO</option>
                                        <option value="2">SLOGAN</option>
                                        <option value="3">背景图</option>
                                        <option value="4">红包样式</option>
                                        <option value="5">品牌产品图</option>
                                        <option value="6">提示图</option>
                                        <option value="7">按钮样式</option>
                                        <option value="8">弹窗广告</option>
                                        <option value="9">首页轮播</option>
                                        <option value="10">商城轮播</option>
                                        <option value="11">活动规则</option>
                                        <option value="12">弹窗跳转图片</option>
                                        <option value="13">实物奖</option>
                                        <option value="14">阶梯弹窗</option>
                                        <option value="15">终端实物奖</option>
                                        <option value="16">活动专区</option>
                                    </select>
                                    <label class="validate_tips"></label>
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
