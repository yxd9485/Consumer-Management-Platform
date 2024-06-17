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
            // 弹出批次信息
            $("#addGoods").off();
            $("#addGoods").on("click", function(){
                // 弹出对话框
                $('h4').html('电子券新增');
                $("#qrcodeUploadDialog input.import-file").val("");
                $(" #batchNameImport").val("");
                $(" #batchKeys").val("");
                $(" #batchCounts").val("");
                $("#qrcodeUploadDialog").modal("show");
                $("#addBtn").show(); 
                $("#editBtn").hide();
            });

            // 确定批次信息
            $("#qrcodeUploadDialog").delegate("#addBtn", "click", function(){

             /*   // 输入元素校验
                var validateResult = $.submitValidate($("#qrcodeUploadDialog"));
                if(!validateResult){
                    return false;
                }*/

                $uploadFile = $("#qrcodeUploadDialog input.import-file");
                var files = $uploadFile.val();
                if(files == "") {
                    $.fn.alert("未选择任何文件，不能导入!");
                    return false;
                } else if(files.indexOf("xlsx") == -1 && files.indexOf("xls") == -1  ) {
                    $.fn.alert("不是有效的Excel文件");
                    return false;
                }

                // 弹出遮罩层
                $("#qrcodeFloatDiv").show();

                // 提交表单
                var url = "<%=cpath%>/vpointsCoupon/addBatch.do";
                var formData = new FormData();
                console.log($("#qrcodeUploadDialog input[name='batchName']").val())
                console.log($uploadFile[0].files[0])
                formData.append("batchName", $("#qrcodeUploadDialog input[name='batchName']").val());
                formData.append("batchFile", $uploadFile[0].files[0]);

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
                        $("#qrcodeFloatDiv").css("display","none");
                        $("#expressUploadDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });
            
               
           // 新增
          /*  $("#addGoods").click(function(){
                var url = "<%=cpath%>/vpointsCoupon/batchAdd.do";
                $("form").attr("action", url);
                $("form").submit();
            });*/
            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
             /*   $("#addBatchDialog input[id='batchNameImport']").val("");
                $("#addBatchDialog input[name='batchKeys']").val("");
                $("#addBatchDialog input[name='batchCounts']").val("");*/
                $(" #batchNameImport").val("");
                $(" #batchKeys").val("");
                $(" #batchCounts").val("");
              var batchName;
                var batchKey= $("input[name=batchKey]").val();
                var formData = new FormData();
                formData.append("batchKey",batchKey)
                var url = "<%=cpath%>/vpointsCoupon/getBatchDetailMsg.do";
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
                        $(" #batchNameImport").val(data["batchName"]);
                      /*  $("#qrcodeUploadDialog input[id='batchNameImport']").val(data["batchName"]);*/
                        $("#qrcodeUploadDialog input[name='batchKeys']").val(data["batchKey"]);
                        $("#qrcodeUploadDialog input[name='batchCounts']").val(data["batchCount"]);
                        
                        $("#qrcodeUploadDialog").modal("show");
                        $("#qrcodeUploadDialog input.import-file").val("");
                        
                        $('h4').html('电子券修改');
                        $("#closeBtn").hide();
                        $("#addBtn").hide();
                        $("#editBtn").show();
                        
                    }
                });


                // 确定批次信息
                $("#qrcodeUploadDialog").delegate("#editBtn", "click", function(){

                    /*   // 输入元素校验
                       var validateResult = $.submitValidate($("#qrcodeUploadDialog"));
                       if(!validateResult){
                           return false;
                       }*/

                    $uploadFile = $("#qrcodeUploadDialog input.import-file");
                    var files = $uploadFile.val();
                  /*  if(files == "") {
                        $.fn.alert("未选择任何文件，不能导入!");
                        return false;
                    } else */if(files != ""&&files.indexOf("xlsx") == -1 && files.indexOf("xls") == -1  ) {
                        $.fn.alert("不是有效的Excel文件");
                        return false;
                    }

                    // 弹出遮罩层
                    $("#qrcodeFloatDiv").show();

                    // 提交表单
                    var url = "<%=cpath%>/vpointsCoupon/updateBatch.do";
                    var formData = new FormData();
                    formData.append("batchKey", $("#qrcodeUploadDialog input[name='batchKeys']").val());
                    formData.append("batchCount", $("#qrcodeUploadDialog input[name='batchCounts']").val());
                   /* formData.append("batchName", $("#qrcodeUploadDialog input[name='batchName']").val());*/
                    formData.append("batchName",  $("#batchNameImport").val());
                    console.log($uploadFile[0]);
                    console.log( $uploadFile[0].files[{name: "",type:"application/vnd.ms-excel" }]);
                    if(null!=$uploadFile[0].files[0]&&""!=$uploadFile[0].files[0]){formData.append("batchFile",$uploadFile[0].files[0])}
                    else{
                        let file = new File([''],'',{
                            type: 'application/octet-stream',
                        });
                        formData.append("batchFile", file);
                    }

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
                            $("#qrcodeFloatDiv").css("display","none");
                            $("#expressUploadDialog #closeBtn").trigger("click");
                            $.fn.alert(data['errMsg'], function(){
                                $("button.btn-primary").trigger("click");
                            });
                        }
                    });
                });
            });
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var batchKey= $("input[name=batchKey]").val()
            /*	var key = $(this).parents("td").data("key");*/
                var url = "<%=cpath%>/vpointsCoupon/delBatch.do?batchKey="+batchKey;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            $("#categoryParent").change(function(){
                var categoryParent=document.getElementById("categoryParent").value;
                if(categoryParent){
                    $.ajax({
                        url:'<%=cpath%>/vpointsGoods/getCategoryByParent.do?parentId='+categoryParent,
                        type: 'POST',
                        dataType:'json',
                        beforeSend:appendVjfSessionId,
                    success: function(data){
                            document.getElementById("categoryType").options.length = 0;
                            $("#categoryType").append("<option value=''>请选择</option>");
                            for (var a = 0; a <data.length; a++) {
                                var obj=data[a];
                                $("#categoryType").append("<option value='"+obj.categoryType+"'>"+obj.categoryName+"</option>");
                            }
                        },
                        error:function(data){
                            alert('fail');
                        }
                    });
                }else{
                    document.getElementById("categoryType").options.length = 0;
                    $("#categoryType").append("<option value=''>请选择</option>");
                }
            });
            $("#searchButton").click(function(){
                var categoryParent=document.getElementById("categoryParent").value;
                var categoryType=document.getElementById("categoryType").value;
                if(categoryType&&!categoryParent){
                    alert("选择二级分类前，先选择一级分类");
                    return;
                }
            });

            // 排序
            $("a.ordercol").on("click", function(){
                $("form").attr("action", "<%=cpath%>/vpointsCoupon/getBatchList.do");
                $("input.tableOrderCol").val($(this).data("ordercol"));
                $("input.tableOrderType").val($(this).data("ordertype") == "desc" ? "asc" : "desc");
                
                // 跳转到首页
                doPaginationOrder();
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
            var queryParamAry = "batchName".split(",");
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
            <li class="current"><a>积分商城</a></li>
            <li class="current"><a>电子券批次</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">电子券批次查询</span></div>
                   <div class="col-md-10 text-right">
                       <c:if test="${currentUser.roleKey ne '4'}">
                           <a id="addGoods" class="btn addGoods btn-blue" href="#">
                               <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
                           </a>
                       
                       </c:if>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post"
                        action="<%=cpath%>/vpointsCoupon/getBatchList.do">
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
                                    <label class="control-label">批次名称：</label>
                                    <input name="batchName" id="batchName" class="form-control input-width-larger" autocomplete="off" type="text"/>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">商品列表</span></div>
                 
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width: 5%">序号</th>
                                <th style="width: 25%" data-ordercol="t.batch_name">批次名称</th>
                                <th style="width: 35%" data-ordercol="t.batch_key">批次编码</th>
                                <th style="width: 10%" data-ordercol="t.batch_count">电子券数量</th>
                                <th style="width: 15%" data-ordercol="t.batch_table">批次表</th>
                                <th style="width: 5%">编辑</th>
                                <th style="width: 5%" >删除</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${fn:length(batchList) gt 0}">
                                    <c:forEach items="${batchList}" var="batch" varStatus="status">
                                        <tr>
                                            <td class="text_center" style="vertical-align: middle;">${status.count}</td>

                                            <td style="vertical-align: middle;"><span>${batch.batchName}</span></td>
                                            <td style="vertical-align: middle;"><span>${batch.batchKey}</span></td>
                                            <td style="vertical-align: middle;"><span>${batch.batchCount}</span></td>
                                            <td style="vertical-align: middle;"><span>${batch.batchTable}</span></td>
                                            <td style="vertical-align: middle;"><span>
                                                <input type="hidden" name="batchKey" value="${batch.batchKey}" />
                                            <a class="btn btn-xs edit btn-danger btn-orange" href="#" ><i class="iconfont icon-xiugai" style="font-size: 14px;"></i>  修 改</a>
                                        </span></td>
                                            <td style="vertical-align: middle;"><span>
                                            <a class="btn btn-xs del btn-danger btn-red" ><i class="iconfont icon-lajixiang" style="font-size: 14px;"></i>  删 除</a>
                                        </span></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8" style="text-align: left;">
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
        <div id="qrcodeFloatDiv" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
            <h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
        </div>
        <div class="modal fade" id="qrcodeUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content" style="top:20%;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">电子券新增</h4>
                    </div>
                    <div class="modal-body">
                        <table class="active_board_table">
                            <input type="hidden" name="batchKeys" id="batchKeys"/>
                            <input type="hidden" name="batchCounts" id="batchCounts"/>

                            <tr>
                                <td width="25%" class="text-right"><label class="title">批次名称：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                        <input name="batchName" id="batchNameImport" tag="validate"
                                               class="form-control required input-width-larger" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips" style="width:35%"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" class="text-right"><label class="title">电子券信息：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                        <input type="file"  class="import-file" id="batchFile" name="batchFile" single style="padding-bottom: 15px; margin-left: 10px;"/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="editBtn" class="btn btn-default do-add btn-orange" data-dismiss="">修 改</button>
                        <button type="button" id="addBtn" class="btn btn-default do-add btn-orange" data-dismiss="">确 认</button>
                        <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
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
