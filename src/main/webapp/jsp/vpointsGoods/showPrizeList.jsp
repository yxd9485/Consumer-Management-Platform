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
			$("#addGoods").click(function(){
				var url = "<%=cpath%>/vpointsGoods/goodsAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

	     	// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
			    var infoKey= $("input[name=prizeInfoKey]").val()
			//	var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vpointsExchange/getPrizeDetail.do?infoKey="+infoKey;
				$("form").attr("action", url);
				$("form").submit();
			});
       
			// 删除
          	$("a.del").off();
			$("a.del").on("click", function(){
                var goodsId= $("input[name=goodsIdKey]").val()
			/*	var key = $(this).parents("td").data("key");*/
				var url = "<%=cpath%>/vpointsGoods/delGoods.do?goodsId="+goodsId;
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
            
        });
        
        // 初始化界面
        function initPage() {
            
          

			// 回显查询条件
			var queryParamAry = "exchangeChannel,categoryParent,categoryType,goodsName,userKey,earnStartTime,earnEndTime".split(",");
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
            <li class="current"><a>实物奖查询</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">兑换查询</span></div>
	               <div class="col-md-10 text-right">
		           
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/vpointsExchange/getPrizeList.do">
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
                                    <label class="control-label">兑换渠道：</label>
                                    <select name="exchangeChannel" id="exchangeChannel" class="form-control input-width-larger search" >
                                        <option value="">请选择</option>
                                        <option value="1">商城兑换</option>
                                        <option value="2">商城抽奖</option>
                                    </select>
                                    </div>
                                    <div class="search-item">
                                    <label class="control-label">一级分类：</label>
                                    <select name="categoryParent" id="categoryParent" class="form-control input-width-larger search" >
                                        <option value="">请选择</option>
                                        <c:forEach items="${firstList}" var="item">
                                            <option value="${item.categoryType}">${item.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                    </div>
                                    <div class="search-item">
                                    <label class="control-label">二级分类：</label>
                                    <select name="categoryType" id="categoryType" class="form-control input-width-larger search">
                                        <option value="">请选择</option>
                                        <c:forEach items="${secondList}" var="item">
                                            <option value="${item.categoryType}">${item.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                    </div>
                                    <div class="search-item">
                                    <label class="control-label">商品名称：</label>
                                    <input name="goodsName" id="goodsName" class="form-control input-width-larger" autocomplete="off" type="text"/>
                                    </div>
                                    <div class="search-item">
                                    <label class="control-label">用户 Key：</label>
                                    <input name="userKey" id="userKey" class="form-control input-width-larger" autocomplete="off" type="text"/>
                                    </div>
                                    <div class="search-item">
                                    <label class="control-label">日期：</label>
                                    <input name="earnStartTime" id="earnStartTime" 
                                           class=" input-width-medium Wdate search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'earnEndTime\')}'})" autocomplete="off" />
                                    <label class="">-</label>
                                    <input name="earnEndTime" id="earnEndTime" 
                                           class="input-width-medium Wdate sufTime search-date" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'earnStartTime\')}'})" autocomplete="off" />
                                        
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">兑换列表</span></div>
                   
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                            <tr>
                                <th style="width: 5%" >序号</th>
                                <th style="width: 18%" data-ordercol="g.goods_name">商品名称</th>
                                <th style="width: 19%" data-ordercol="g.exchange_channel">兑换渠道</th>
                                <th style="width: 19%" data-ordercol="a.category_name">一级分类</th>
                                <th style="width: 19%" data-ordercol="c.category_name">二级分类</th>
                                <th style="width: 16%" data-ordercol="t.EARN_TIME" data-orderdef>日期</th>
                                <th style="width: 4%" >查看</th>
                            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(prizeList) gt 0}">
                                    <c:forEach items="${prizeList}" var="prize" varStatus="status">
                                        <tr>
                                            <td class="text_center" style="vertical-align: middle;">${status.count}</td>

                                            <td style="vertical-align: middle;"><span>${prize.goodsName}</span></td>
                                            <td style="vertical-align: middle;">
                                                <c:choose>
                                                    <c:when test="${prize.exchangeChannel == '1'}">商城兑换</c:when>
                                                    <c:when test="${prize.exchangeChannel == '2'}">商城抽奖</c:when>
                                                    <c:when test="${prize.exchangeChannel == '3'}">扫码中出</c:when>
                                                    <c:when test="${prize.exchangeChannel == '6'}">优品推荐</c:when>
                                                    <c:otherwise>其他</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="vertical-align: middle;"><span>${prize.firstName}</span></td>
                                            <td style="vertical-align: middle;"><span>${prize.secondName}</span></td>
                                            <td style="vertical-align: middle;"><span>${prize.earnTime}</span></td>
                                            <td style="vertical-align: middle;">
                                                <input type="hidden" name="prizeInfoKey" value="${prize.infoKey}" />
                                                <a class="btn btn-xs edit btn-danger btn-blue" href="#" ><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>  查 看</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9" style="text-align: left;">
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
