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

	<script>
		$(function(){

			// 新增
			$("#addGoods").click(function(){
				var url = "<%=cpath%>/vpointsGoods/goodsAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

	     	// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vpointsGoods/getGoodsDetail.do?goodsId="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
         
			// 删除
          	$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vpointsGoods/delGoods.do?goodsId="+key;
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
            <li class="current"><a>商品管理</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">商品查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addGoods" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/vpointsGoods/getGoodsList.do">
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
                                        <c:if test="${projectServerName eq 'hebei'}">
                                            <option value="1">天津商城</option>
                                        </c:if>
                                        <c:if test="${projectServerName ne 'hebei' && !fn:startsWith(projectServerName, 'mengniu')}">
                                            <option value="1">畅享汇商城</option>
                                        </c:if>
                                        <c:if test="${fn:startsWith(projectServerName, 'mengniu')}">
                                            <option value="1">积分商城</option>
                                        </c:if>
                                        <option value="2">抽奖</option>
                                        <c:if test="${projectServerName eq 'hebei'}"><option value="6">畅享汇商城</option></c:if>
                                        <c:if test="${projectServerName eq 'henanpz'}"><option value="9">竞价活动</option></c:if>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">商品类型：</label>
                                    <select name="exchangeType" id="exchangeType" class="form-control input-width-larger search" >
                                        <option value="">请选择</option>
                                        <option value="1">实物</option>
                                        <option value="2">电子券</option>
                                        <option value="3">话费</option>
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
                                    <label class="control-label">商品名称：</label>
                                    <input name="goodsName" id="goodsName" class="form-control input-width-larger" autocomplete="off" type="text"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">兑换品牌：</label>
                                    <select name="brandId" class="form-control input-width-larger search" autocomplete="off" >
                                        <option value="">全部</option>
                                        <c:forEach items="${brandLst}" var="item">
                                            <option value="${item.brandId}">${item.brandName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">商品展示类型：</label>
                                    <select name="goodShowFlag" class="form-control input-width-larger search" autocomplete="off" >
                                        <option value="">全部</option>
                                        <option value="1">首页商品大图</option>
                                        <option value="2">首页商品小图</option>
                                        <option value="3">积分好礼</option>
                                        <option value="4">活动专区</option>
                                        <option value="5">爆款推荐</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">商品状态：</label>
                                    <select name="goodsStatus" id="goodsStatus" class="form-control input-width-larger search" >
                                    	<option value="">全部</option>
                                        <option value="0">正常</option>
                                        <option value="1">暂停</option>
                                        <option value="2">下架</option>
                                    </select>
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
                            <input type="hidden" id="screenWidth1366" value="5,5,9,8,8,8,8,8,8,8,8,8,8" />
                            <thead>
                            <tr>
                                <th style="width: 4%">序号</th>
                                <th style="width: 5%" data-ordercol="t.goods_url">图片</th>
                                <th style="width: 13%" data-ordercol="t.goods_name">商品名称</th>
                                <th style="width: 7%"data-ordercol="b.brand_name">品牌名称</th>
                                <th style="width: 7%"data-ordercol="t.exchange_channel">兑换渠道</th>
                                <th style="width: 6%"data-ordercol="a.category_name">一级分类</th>
                                <th style="width: 6%"data-ordercol="t.goods_vpoints">兑换积分</th>
                                <th style="width: 6%"data-ordercol="t.goods_remains">剩余个数</th>
                                <th style="width: 6%"data-ordercol="t.goods_exchange_num">兑换个数</th>
                                <th style="width: 6%"data-ordercol="t.goods_custom_sequence">自定义排序</th>
                                <th style="width: 9%"data-ordercol="t.goods_start_time">开始日期</th>
                                <th style="width: 9%"data-ordercol="t.goods_end_time">结束日期</th>
                                <th style="width: 10%">操作</th>
                            </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(goodsList) gt 0}">
                                    <c:forEach items="${goodsList}" var="goods" varStatus="status">
                                        <tr>
                                            <td class="text_center" style="vertical-align: middle;">${status.count}</td>
                                            <td style="width: 70px;height: 60px;">

		                                        <span><img onmouseover="imgBig('<%=imageServerUrl%>${fn:split(not empty(goods.goodsUrl) ? goods.goodsUrl : goods.goodsBigUrl,',')[0]}');"
                                                           onmouseleave="imgSmall();"
                                                           style="width: 50px;height: 50px;" alt="图片"
                                                           src="<%=imageServerUrl%>${fn:split(not empty(goods.goodsUrl) ? goods.goodsUrl : goods.goodsBigUrl,',')[0]}"/>
		                                        </span>
                                            </td>
                                            <td style="vertical-align: middle;"><span>${goods.goodsName}</span></td>
                                            <td style="vertical-align: middle;"><span>${goods.brandName}</span></td>
                                            <td style="vertical-align: middle;">
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(projectServerName, 'mengniu') && goods.exchangeChannel == '1'}">积分商城</c:when>
                                                    <c:when test="${projectServerName eq 'hebei' && goods.exchangeChannel == '1'}">天津商城</c:when>
                                                    <c:when test="${projectServerName ne 'hebei' && !fn:startsWith(projectServerName, 'mengniu') && goods.exchangeChannel == '1'}">畅享汇商城</c:when>
                                                    <c:when test="${goods.exchangeChannel == '2'}">扫码中出</c:when>
                                                    <c:when test="${projectServerName eq 'hebei' && goods.exchangeChannel == '6'}">畅享汇商城</c:when>
                                                    <c:when test="${goods.exchangeChannel == '9'}">竞价活动</c:when>
                                                    <c:otherwise>其他</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td style="vertical-align: middle;"><span>${goods.firstName}</span></td>
                                            <td style="vertical-align: middle;"><span>${goods.goodsVpoints}</span></td>
                                            <td style="vertical-align: middle;"><span>${goods.goodsRemains}</span></td>
                                            <td style="vertical-align: middle;"><span>${goods.goodsExchangeNum}</span></td>
                                            <td style="vertical-align: middle;"><span>${goods.goodsCustomSequence}</span></td>
                                            <td style="vertical-align: middle;"><span>${goods.goodsStartTime}</span></td>
                                            <td style="vertical-align: middle;"><span>${goods.goodsEndTime}</span></td>
                                            <td style="vertical-align: middle;text-align: center;" data-key="${goods.goodsId}">
                                                <a class="btn btn-xs edit btn-danger btn-orange" ><i class="iconfont icon-xiugai" style="font-size: 14px;"></i>  修 改</a>
                                            <a class="btn btn-xs del btn-danger btn-red" ><i class="iconfont icon-lajixiang" style="font-size: 14px;"></i>  删 除</a>

					        		</td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="14" style="text-align: left;">
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
