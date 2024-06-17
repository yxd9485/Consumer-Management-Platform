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
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    
    <script>
        $(function(){
            
            var flag = "${flag}";
            if(flag != "") {
                $("div.modal-body ."+flag+"").show();
                $("div.modal-body :not(."+flag+")").hide();
                $("#myModal").modal("show");
            }
            
            // 新增
            $("#addRule").click(function(){
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogAdd.do";
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 区域下新增规则
            $("a.areaRuleAdd").off();
            $("a.areaRuleAdd").on("click", function(){
                var areaCode = $(this).data("key");
                var departmentIds = $(this).data("id");
                var organizationIds = $(this).data("organization");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogAdd.do?areaCode="+areaCode;
                
                if('${serverName}' == 'mengniu'){
                	url += "&departmentIds="+departmentIds;
                }else if(${not empty(bigRegionList)}){
                	url += "&organizationIds="+organizationIds;
                }
                
                $("form").attr("action", url);
                $("form").submit(); 
                return false;
            });
            
            // 区域规则删除
            $("a.areaRuleDel").off();
            $("a.areaRuleDel").on("click", function(){
            	var areaCode = $(this).data("key");
                var departmentIds = $(this).data("id");
                var organizationIds = $(this).data("organization");
                var url = "<%=cpath%>/vcodeActivityRebateRule/doRebateRuleDelete.do?areaCode="+areaCode;

                if('${serverName}' == 'mengniu'){
                	url += "&departmentIds="+departmentIds;
                }else if(${not empty(bigRegionList)}){
                	url += "&organizationIds="+organizationIds;
                }
                $.fn.confirm("确定要删除此区域及其规则吗？",function(){
                    $("form").attr("action", url);
                    $("form").submit();
                    return false;
                	
                });
            });
            
         	// 删除活动下无效规则
            $("#delActivityRule").off();
            $("#delActivityRule").on("click", function(){
                var url = "<%=cpath%>/vcodeActivityRebateRule/doRebateRuleDelete.do";
                $.fn.confirm("确定要删除该活动下所有无效规则吗？",function(){
                    $("form").attr("action", url);
                    $("form").submit();
                    return false;
                	
                });
            });
            
            // 修改
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogEdit.do?rebateRuleKey="+key;
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
            	var key = $(this).parents("td").data("key");
            	var url = "<%=cpath%>/vcodeActivityRebateRule/doRebateRuleCogDelete.do?rebateRuleKey="+key;
                $.fn.confirm("确定要移除吗？",function(){
                    $("form").attr("action", url);
                    $("form").submit();	
                });
            }); 
                    
            // 克隆
            $("a.clone").off();
            $("a.clone").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleCogClone.do?rebateRuleKey="+key;
                $("form").attr("action", url);
                $("form").submit(); 
            });
            
            // 爆点红包
            $("a.erupt").off();
            $("a.erupt").on("click", function(){
            	var key = $(this).parents("td").data("key");
            	 var url = "<%=cpath%>/vcodeActivityRebateRule/queryEruptRedpacketList.do?rebateRuleKey="+key;
                 $("form").attr("action", url);
                 $("form").submit();
            });
            
            // 实现Collapse同时只展开一个的功能
            $(".paneltitle").on("click", function(){
                var collapse = $(this).attr("aria-controls");
                var rebateRuleKey = $(this).parents("tr").attr("rebateRuleKey");
                var dataFlag = $(this).parents("tr").attr("dataFlag");
                if(!dataFlag){
                    let tahtAddTri = $(this).parents("tr").next("tr").find(".addTri");
                    loadRule(rebateRuleKey,tahtAddTri)
                     $(this).parents("tr").attr("dataFlag", true);
                }
                console.log("rebateRuleKey=",rebateRuleKey)
                $("[id^='collapseInner']").each(function(obj){
                    if ($(this).attr("id") != collapse) {
                        $(this).removeClass("in");
                        $(this).addClass("collapse");
                    }
                });
            });
            
            //  默认第一个展开 
            // $("#collapseArea1").addClass("in");
            
        });
        function NumFormat(value) {
            if (!value) return '0.00'
            value = value.toFixed(2)
            var intPart = Math.trunc(value) // 获取整数部分
            var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,') // 将整数部分逢三一断
            var floatPart = '.00' // 预定义小数部分
            var value2Array = value.split('.')
            // =2表示数据有小数位
            if (value2Array.length === 2) {
                floatPart = value2Array[1].toString() // 拿到小数部分
                if (floatPart.length === 1) {
                    return intPartFormat + '.' + floatPart + '0'
                } else {
                    return intPartFormat + '.' + floatPart
                }
            } else {
                return intPartFormat + floatPart
            }
        }

        function scanTypeName(scanType) {
            return "0" == scanType ? "首扫" : "普通扫码";
        }

        var prizeTypeMap = {"prizeType":${prizeTypeJSON}};
        var allowanceType = {"allowanceType":${allowanceTypeJSON}};
        function getPrizeTypeMapValue(prizeType) {
           return prizeTypeMap.prizeType[prizeType]
        }
        function getAllowanceTypeValue(vpoint) {
            let pm = vpoint.allowanceMoney > 0 ? "-金额:" + vpoint.prizePayMoney : "";
            return vpoint.allowanceType ? allowanceType.allowanceType[vpoint.allowanceType]+pm : "";
        }
        function loadRule(rebateRuleKey,tahtAddTri){
            var url = "<%=cpath%>/vcodeActivityRebateRule/getVpointsCogById.do";
            $.ajax({
                type: "POST",
                url: url,
                data: {"rebateRuleKey":rebateRuleKey},
                dataType: "json",
                async: true,
                beforeSend:appendVjfSessionId,
                success: function(data) {
                   console.log(data)
                    let index=1;
                   if(data){
                       tahtAddTri.empty();
                       for (let i = 0; i < data.length; i++) {
                           var vpoint = data[i];
                           var prizeTypelux = vpoint.prizePayMoney > 0 ? "-支付" : "";
                           var prizeTypelex = vpoint.prizeDiscount > 0 ? "-优惠" : "";
                           var waitActivation = vpoint.isWaitActivation === "1" ? "待激活" : "";
                           //扫码待激活红包
                           var waitPrizeActivation = vpoint.isScanqrcodeWaitActivation === "1" ? "+待激活红包" : "";
                           var prizeType = waitActivation+getPrizeTypeMapValue(vpoint.prizeType)+prizeTypelux+prizeTypelex+waitPrizeActivation;
                           var randomType=vpoint.randomType=='0'?"随机":"固定"
                           var vpointsType = vpoint.randomType == '0' ? vpoint.minVpoints + "-" + vpoint.maxVpoints : vpoint.maxVpoints;
                           var moneyType = vpoint.randomType == '0' ? NumFormat(vpoint.minMoney) + "-" + NumFormat(vpoint.maxMoney) : NumFormat(vpoint.minMoney);
                           var waitPrizeMoney = vpoint.isScanqrcodeWaitActivation === "1" ? NumFormat(vpoint.waitActivationMinMoney) + "-" + NumFormat(vpoint.waitActivationMaxMoney) : "";
                           var prizePercentWarn = vpoint.prizePercentWarn  ? `<i class="iconfont icon-jinggao" style="color: red; font-weight: bold;" title="占比预警配置:` + vpoint.prizePercentWarn + `%" ></i>` : "";
                           var cardName = vpoint.cardName ? vpoint.cardName : "";

                           //只配了扫码待激活红包的特殊显示处理
                           if (prizeType === "现金红包+待激活红包" && (moneyType ==="0.00"||moneyType==="0.00-0.00")){
                               prizeType = "待激活红包";
                           }

                        var vpointsCogHtml=`<tr>
                                            <td style="text-align:center;">
                                                <span>`+index++ +`</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>`+scanTypeName(vpoint.scanType)+`</span>
                                            </td>
                                            <td>
                                                <span>`+prizeType+` </span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>`+randomType+`</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>`+vpointsType+`</span>
                                            </td>
                                            <td style="text-align:center;">
                                                 <span>`+moneyType+`</span>
                                            </td>
                                             <td style="text-align:center;">
                                                 <span>`+waitPrizeMoney+`</span>
                                            </td>
                                             <td>
                                                  <span>`+getAllowanceTypeValue(vpoint.allowanceType)+`</span>
                                            </td>
                                            <td>
                                                  <span>`+cardName+`</span>
                                            </td>

                                            <td>
                                                 <span>`+vpoint.cogAmounts+`</span>

                                            </td>
                                            <td>
                                                 <span>`+vpoint.restAmounts+`</span>
                                            </td>
                                            <td  style="text-align:center;">
                                                 <span>`+vpoint.rangePercent+`%</span>
                                                   `+prizePercentWarn+`
                                            </td>
                                            <td>
                                                 <span>`+vpoint.scanNum+`</span>
                                            </td>
                                        </tr>`
                           tahtAddTri.append(vpointsCogHtml);
                       }
                   }


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
  
	<form class="listForm" method="post">
	    <input type="hidden" name="vcodeActivityKey" value="${vcodeActivityKey}" />
        <input type="hidden" name="activityType" value="${activityType}" />
	</form>
    <div class="container" style="padding: 0px;">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
<!--             活动信息 -->
                <div class="widget-header">
                    <h4><i class="iconfont icon-xinxi"></i>基础活动信息</h4>
                </div>
                <div class="widget-content panel no-padding">
	                <table class="active_board_table">
                    <c:choose>
                        <c:when test="${activityType eq '0'}">
<!--                    扫码活动 -->
                        <tr>
                            <td class="ab_left"><label class="title">活动名称：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${activityCog.vcodeActivityName}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">SKU名称：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${activityCog.skuName}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">活动时间：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${activityCog.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${activityCog.endDate}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">可疑中出类型：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>
                                        <c:if test="${activityCog.doubtRebateType eq '0'}">积分红包</c:if>
                                        <c:if test="${activityCog.doubtRebateType eq '1'}">商城积分</c:if></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">活动状态：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>
                                        <c:if test="${activityCog.isBegin eq '0'}">待上线</c:if>
                                        <c:if test="${activityCog.isBegin eq '1'}">已上线</c:if>
                                        <c:if test="${activityCog.isBegin eq '2'}">已下线</c:if></span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">中出区间：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span style="color: red;">${activityCog.doubtRuleRangeMin}&nbsp;-&nbsp;${activityCog.doubtRuleRangeMax}</span>
                                    <span>
                                        <c:if test="${activityCog.doubtRebateType eq '0'}">元</c:if>
                                        <c:if test="${activityCog.doubtRebateType eq '1'}">积分</c:if></span>
                                </div>
                            </td>
                        </tr>
	                    </c:when>
                        <c:when test="${activityType eq '4'}">
<!--                    万能签到 -->
                        <tr>
                            <td class="ab_left"><label class="title">活动名称：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${signinCog.activityName}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">活动时间：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${signinCog.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${signinCog.endDate}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">签到周期：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <c:choose>
                                        <c:when test="${signinCog.periodType eq '0' }"><span>周签到</span></c:when>
                                        <c:when test="${signinCog.periodType eq '1' }"><span>月签到</span></c:when>
                                    </c:choose>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">SKU关系：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <c:choose>
                                        <c:when test="${signinCog.skuRelationType eq '0' }"><span>并且</span></c:when>
                                        <c:when test="${signinCog.skuRelationType eq '1' }"><span>或者</span></c:when>
                                        <c:when test="${signinCog.skuRelationType eq '2' }"><span>混合</span></c:when>
                                    </c:choose>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">周期红包上限：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${signinCog.prizeUpperLimit}个</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">扫码限制次数：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${signinCog.limitNum}次</span>
                                </div>
                            </td>
                        </tr>
	                    </c:when>
                        <c:when test="${activityType eq '5'}">
<!--                    捆绑促销 -->
                        <tr>
                            <td class="ab_left"><label class="title">活动名称：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${bindPromotionCog.activityName}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">活动时间：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${bindPromotionCog.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${bindPromotionCog.endDate}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">促销周期：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <c:choose>
                                        <c:when test="${bindPromotionCog.periodType eq '0' }"><span>周</span></c:when>
                                        <c:when test="${bindPromotionCog.periodType eq '1' }"><span>月</span></c:when>
                                        <c:when test="${bindPromotionCog.periodType eq '2' }"><span>天</span></c:when>
                                    </c:choose>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">促销SKU：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${bindPromotionCog.skuName}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">红包类型：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <c:choose>
                                        <c:when test="${bindPromotionCog.prizeGainType eq '0' }"><span>替换扫码红包</span></c:when>
                                        <c:when test="${bindPromotionCog.prizeGainType eq '1' }"><span>额外红包</span></c:when>
                                    </c:choose>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">周期红包上限：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${bindPromotionCog.prizeUpperLimit}个</span>
                                </div>
                            </td>
                        </tr>
	                    </c:when>
                        <c:when test="${activityType eq '6'}">
<!--                    一码双奖 -->
                        <tr>
                            <td class="ab_left"><label class="title">活动名称：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${doublePrizeCog.activityName}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">活动时间：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${doublePrizeCog.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${doublePrizeCog.endDate}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">每人中奖次数上限：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${doublePrizeCog.everyoneLimit}次</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">每天中奖次数上限：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${doublePrizeCog.everyDayLimit}次</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">需关注公众号：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <c:choose>
                                        <c:when test="${doublePrizeCog.subscribeStatus eq '0' }"><span>否</span></c:when>
                                        <c:when test="${doublePrizeCog.subscribeStatus eq '1' }"><span>是</span></c:when>
                                    </c:choose>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">兑奖截止日期：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${doublePrizeCog.prizeExpireDate}</span>
                                </div>
                            </td>
                        </tr>
	                    </c:when>
                        <c:when test="${activityType eq '7'}">
                        <!--                    万能签到 -->
                        <tr>
                            <td class="ab_left"><label class="title">活动名称：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${activityCog.vcodeActivityName}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">活动时间：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span>${activityCog.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${activityCog.endDate}</span>
                                </div>
                            </td>
                        </tr>
                        </c:when>
                        <c:when test="${activityType eq '8'}">
                            <!--                    万能签到 -->
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required"></span></label></td>
                                <td class="ab_main" style="width: 25% !important;">
                                    <div class="content">
                                        <span>${activityCog.vcodeActivityName}</span>
                                    </div>
                                </td>
                                <td class="ab_left"><label class="title">活动时间：<span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <span>${activityCog.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${activityCog.endDate}</span>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:when test="${activityType eq '13'}">
                            <!--                    天降红包 -->
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required"></span></label></td>
                                <td class="ab_main" style="width: 25% !important;">
                                    <div class="content">
                                        <span>${waitActivationRule.activityName}</span>
                                    </div>
                                </td>
                                <td class="ab_left"><label class="title">活动时间：<span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <span>${waitActivationRule.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${waitActivationRule.endDate}</span>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                    </c:choose>
	                </table>
                </div>
<!--            规则列表 -->
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <div class="row">
					        <div class="col-md-12 tabbable">
					            <a href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do?vjfSessionId=${vjfSessionId}&activityType=${activityType}&vcodeActivityKey=${activityCog.vcodeActivityKey}&tabsFlag=1" 
					                 class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">配置活动规则</a>
					            <a href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do?vjfSessionId=${vjfSessionId}&activityType=${activityType}&vcodeActivityKey=${activityCog.vcodeActivityKey}&tabsFlag=2" 
					                 class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">失效活动规则</a>
                                <c:if test="${currentUser.roleKey ne '4'}">
                                    <div style="float: right;">
                                        <a id="addRule" class="btn btn-blue"><i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;新 增</a>
                                        <a id="delActivityRule" class="btn btn-red">&nbsp;一键删除无效规则</a>
                                    </div>
                                </c:if>
					        </div>
					    </div>
					    <c:if test="${empty resultList}">
					       <div class="panel panel-default">
                                <div class="panel-heading" style="text-align: center;">
                                   <span>暂无活动规则配置</span>
                                </div>
                             </div>
					    </c:if>
                        <div class="panel-group" id="accordionArea">
                            <c:forEach items="${resultList}" var="ruleAreaCog" varStatus="idxArea">
						    <div class="panel panel-default">
						        <div class="panel-heading" data-toggle="collapse" data-parent="#accordionArea" href="#collapseArea${idxArea.count}" >
						           <span class="marr15">${ruleAreaCog.areaName}</span>
						           <c:if test="${ruleAreaCog.expireWarning}">
						              <i class="iconfont icon-riqishijian" style="color: red; font-weight: bold;" title="规则将要到期" ></i>
						           </c:if>
						           <c:if test="${ruleAreaCog.moneyWarning}">
                                      <i class="iconfont icon-money" style="color: red; font-weight: bold;" title="规则剩余钱数小于10%" ></i>
						           </c:if>
						           <c:if test="${ruleAreaCog.vpointsWarning}">
                                      <i class="iconfont icon-jifen" style="color: red; font-weight: bold;" title="规则剩余积分小于10%" ></i>
						           </c:if>
						           <c:if test="${ruleAreaCog.prizeNumWarning}">
                                      <i class="iconfont icon-yuliang" style="color: red; font-weight: bold;" title="规则剩余红包个数小于10%" ></i>
						           </c:if>
						           
                                   <c:if test="${currentUser.roleKey ne '4'}">
	                                   <div style="display: block; float: right;">
	                                       <a class="btn btn-xs areaRuleAdd btn-red" data-key="${ruleAreaCog.areaCode}" data-id="${ruleAreaCog.departmentIds}" data-organization="${ruleAreaCog.organizationIds}"><i class="iconfont icon-xiugai"></i>&nbsp;添加规则</a>
	                                       <c:if test="${ruleAreaCog.areaCode != '000000' or empty(ruleAreaCog.areaCode)}">
	                                           <a class="btn btn-xs areaRuleDel btn-brownness" data-key="${ruleAreaCog.areaCode}" data-id="${ruleAreaCog.departmentIds}"><i class="iconfont icon-lajixiang"></i>&nbsp;删除无效规则</a>
	                                       </c:if>
	                                   </div>
						           </c:if>
						        </div>
						        <div id="collapseArea${idxArea.count}" class="panel-collapse collapse">
						            <div class="panel-body" style="padding:5px;">
				                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart5"
				                               id="dataTable_data">
				                            <thead>
				                                <tr>
                                                    <th style="width:4%;">序号</th>
                                                    <th style="width:12%;">规则名称</th>
                                                    <th style="width:6%;">规则类型</th>
                                                    <th style="width:7%;">规则日期</th>
                                                    <th style="width:6%;">规则时间</th>
                                                    <th style="width:6%;">规则特性</th>
                                                    <th style="width:6%;">限制积分</th>
                                                    <th style="width:6%;">限制金额</th>
                                                    <th style="width:6%;">限制瓶数</th>
                                                    <th style="width:6%;">用户限次</th>
                                                    <th style="width:6%;">扫码积分</th>
                                                    <th style="width:6%;">扫码金额</th>
                                                    <th style="width:6%;">扫码瓶数</th>
                                                    <th style="width:6%;">单瓶金额</th>
                                                    <c:if test="${projectName == '1'}"> <th style="width:6%;">扫码角色</th></c:if>
				                                    <!-- <th style="width:5%;">限制</br>有效</th> -->
				                                    <c:if test="${currentUser.roleKey ne '4'}">
				                                    <th style="width:6%;">操作</th>
				                                    </c:if>
				                                </tr>
				                            </thead>
				                            <tbody>
				                                <c:choose>
				                                <c:when test="${fn:length(ruleAreaCog.rebateRuleCogLst) gt 0}">
				                                   <div class="panel-group" id="accordionRule${idxArea.count}" role="tablist" aria-multiselectable="true">
				                                      <c:forEach items="${ruleAreaCog.rebateRuleCogLst}" var="ruleTypeCog" varStatus="idxType">
                                                          <div class="cccccc">
				                                        <tr title="${ruleTypeCog.remarks}" rebateRuleKey="${ruleTypeCog.rebateRuleKey}">
				                                            <td style="text-align:center; border-top-width: 0px; 
				                                            	<c:choose>
						                                            <c:when test="${ruleTypeCog.ruleStatus == '1' and ruleTypeCog.isValid == '0'}">background-color: #ea918c;</c:when>
						                                            <c:when test="${ruleTypeCog.ruleStatus == '2' or ruleTypeCog.isValid == '1'}">background-color: #999999;</c:when>
						                                            <c:otherwise>background-color: #ecb010;</c:otherwise>
						                                        </c:choose>">
				                                                <span>${idxType.count}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px;">
				                                            	<span>${ruleTypeCog.rebateRuleName}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px">
				                                                <span>
				                                                   <c:choose>
				                                                       <c:when test="${ruleTypeCog.ruleType == '1'}">节假日</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '2'}">时间段</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '3'}">周几</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '4'}">每天</c:when>
				                                                       <c:when test="${ruleTypeCog.ruleType == '5'}">周签到</c:when>
				                                                   </c:choose>
				                                                </span>
				                                                <c:if test="${ruleTypeCog.validMsg != ''}">
				                                                   <br />
				                                                   <span style="color: red; font-weight: bold;">${ruleTypeCog.validMsg }</span>
				                                                </c:if>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; text-align: center; position: relative;">
				                                            	<c:choose>
				                                            		 <c:when test="${ruleTypeCog.ruleType == '3'}"><span>${ruleTypeCog.beginDate} - ${ruleTypeCog.endDate}</span></c:when>
				                                            		 <c:when test="${ruleTypeCog.ruleType == '1' or ruleTypeCog.ruleType == '2'}">
				                                            		 	<span>${ruleTypeCog.beginDate}</span>
					                                                  	<hr style="margin-bottom: 8px; margin-top: 9px; border-top: 0px solid white;">
					                                                  	<span>${ruleTypeCog.endDate}</span>
				                                            		 </c:when>
				                                            	</c:choose>
				                                            	<c:if test="${ruleTypeCog.expireWarning}">
                                                                    <i class="iconfont icon-riqishijian" style="color: red; font-weight:bold; position: absolute; right: 0px; top:32%;" title="规则将要到期" ></i>
			                                                    </c:if>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; text-align: center;">
				                                                  <span>${ruleTypeCog.beginTime}</span>
				                                                  <hr style="margin-bottom: 8px; margin-top: 9px; border-top: 0px solid white;">
				                                                  <span>${ruleTypeCog.endTime}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px">
			                            						<c:if test="${not empty(ruleTypeCog.hotAreaKey)}"><span title="热区">热 </span></c:if>                      
			                                                  	<c:if test="${not empty(ruleTypeCog.eruptRuleInfo)}"><span title="爆点">爆 </span></c:if>
                                                                <c:if test="${not empty(ruleTypeCog.appointRebateRuleKey)}"><span title="继承">继 </span></c:if>
                                                                <c:if test="${ruleTypeCog.redPacketRain eq '1'}"><span title="开启红包雨">雨 </span></c:if>
                                                                <c:if test="${ruleTypeCog.ruleNewUserLadder eq '1'}"><span title="规则新用户阶梯">新 </span></c:if>
                                                                <c:if test="${not empty(ruleTypeCog.specialLabel)}"><span title="优先级标签：${ruleTypeCog.specialLabel}">签 </span></c:if>
                                                                <c:if test="${not empty(ruleTypeCog.groupName)}"><span title="群组标签：${ruleTypeCog.groupName}">群 </span></c:if>
				                                            </td>
                                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px;">
                                                                <span><fmt:formatNumber value="${ruleTypeCog.restrictVpoints eq 0 ? '' : ruleTypeCog.restrictVpoints}" pattern="#,###" /></span>
                                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px;">
                                                                <span><fmt:formatNumber value="${ruleTypeCog.restrictMoney eq 0 ? '' : ruleTypeCog.restrictMoney}" pattern="#,##0.00#" /></span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px;">
                                                                <span>${ruleTypeCog.restrictBottle eq 0 ? '' : ruleTypeCog.restrictBottle}</span>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px;">
				                                            	<span>${ruleTypeCog.restrictCount eq 0 ? '' : ruleTypeCog.restrictCount}</span>
				                                            </td>
                                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
                                                                <c:choose>
                                                                    <c:when test="${ruleTypeCog.totalConsumeVpoints > 0}">
                                                                        <span title="当前规则已消耗积分">C：<fmt:formatNumber value="${ruleTypeCog.consumeVpoints eq 0 ? '' : ruleTypeCog.consumeVpoints}" pattern="#,###" /></span>
                                                                        <hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
                                                                        <span title="该限制已消耗总积分">T：<fmt:formatNumber value="${ruleTypeCog.totalConsumeVpoints eq 0 ? '' : ruleTypeCog.totalConsumeVpoints}" pattern="#,###" /></span>    
                                                                    </c:when>
                                                                    <c:otherwise><span title="当前规则已消耗积分"><fmt:formatNumber value="${ruleTypeCog.consumeVpoints eq 0 ? '' : ruleTypeCog.consumeVpoints}" pattern="#,###" /></span></c:otherwise>
                                                                </c:choose>
                                                                <c:if test="${ruleTypeCog.vpointsWarning}">
                                                                    <i class="iconfont icon-jifen" style="color: red; font-weight: bold; position: absolute; right: 0px; top:32%;" title="规则剩余积分小于10%" ></i>
                                                                </c:if>
                                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
				                                            	<c:choose>
				                                            		<c:when test="${ruleTypeCog.totalConsumeMoney > 0.00}">
				                                            			<span title="当前规则已消耗金额">C：<fmt:formatNumber value="${ruleTypeCog.consumeMoney eq 0 ? '' : ruleTypeCog.consumeMoney}" pattern="#,##0.00#" /></span>
					                                                    <hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
					                                                    <span title="该限制已消耗总金额">T：<fmt:formatNumber value="${ruleTypeCog.totalConsumeMoney eq 0 ? '' : ruleTypeCog.totalConsumeMoney}" pattern="#,##0.00#" /></span>	
				                                            		</c:when>
				                                            		<c:otherwise><span title="当前规则已消耗金额"><fmt:formatNumber value="${ruleTypeCog.consumeMoney eq 0 ? '' : ruleTypeCog.consumeMoney}" pattern="#,##0.00#" /></span></c:otherwise>
				                                            	</c:choose>
                                                                <c:if test="${ruleTypeCog.moneyWarning}">
                                                                   <i class="iconfont icon-money" style="color: red; font-weight: bold; position: absolute; right: 0px; top:32%;" title="规则剩余钱数小于10%" ></i>
                                                                </c:if>
				                                            </td>
				                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
				                                            	<c:choose>
				                                            		<c:when test="${ruleTypeCog.totalConsumeBottle > 0}">
				                                            			<span title="当前规则已消耗瓶数">C：${ruleTypeCog.consumeBottle eq 0 ? '' : ruleTypeCog.consumeBottle}</span>
			                                                  			<hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
			                                                  			<span title="该限制已消耗总瓶数">T：${ruleTypeCog.totalConsumeBottle eq 0 ? '' : ruleTypeCog.totalConsumeBottle}</span>
				                                            		</c:when>
				                                            		<c:otherwise><span title="当前规则已消耗瓶数">${ruleTypeCog.consumeBottle eq 0 ? '' : ruleTypeCog.consumeBottle}</span></c:otherwise>
				                                            	</c:choose>
                                                                <c:if test="${ruleTypeCog.prizeNumWarning}">
                                                                    <i class="iconfont icon-yuliang" style="color: red; font-weight: bold; position: absolute; right: 0px; top:32%;" title="规则剩余红包个数小于10%" ></i>
                                                                </c:if>
				                                            </td>
                                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
                                                                <c:choose>
                                                                    <c:when test="${ruleTypeCog.moneyDanping > 0.00}">
                                                                        <span title="实际单瓶金额">R：<fmt:formatNumber value="${ruleTypeCog.moneyDanpingReal eq 0 ? '' : ruleTypeCog.moneyDanpingReal}" pattern="#,##0.00#" /></span>    
                                                                        <hr style="margin-bottom: 5px; margin-top: 5px; border-top: 1px solid #C7BDBD;">
                                                                        <span title="规则限制单瓶">L：<fmt:formatNumber value="${ruleTypeCog.moneyDanping eq 0 ? '' : ruleTypeCog.moneyDanping}" pattern="#,##0.00#" /></span>
                                                                    </c:when>
                                                                    <c:otherwise><span title="实际单瓶金额"><fmt:formatNumber value="${ruleTypeCog.moneyDanpingReal eq 0 ? '' : ruleTypeCog.moneyDanpingReal}" pattern="#,##0.00#" /></span></c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <c:if test="${projectName == '1'}">
                                                            <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
                                                                <c:forEach items="${roleInfoAll }" var="roleItem">
                                                                    <c:forEach items="${fn:split(ruleTypeCog.allowScanRole, ',') }" var="role">
                                                                            <c:if test="${fn:split(roleItem, ':')[0] eq role}">
                                                                                <span>${fn:split(roleItem, ':')[1]}</span>
                                                                            </c:if>
                                                                    </c:forEach>
                                                                </c:forEach>
                                                            </td>
                                                            </c:if>
				                                            <%-- <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">
				                                                  <span>
				                                                    <c:choose>
				                                                        <c:when test="${ruleTypeCog.isValid == '0'}">是</c:when>
				                                                        <c:otherwise>否</c:otherwise>
				                                                    </c:choose>
				                                                  </span>
				                                            </td> --%>
				                                            <c:if test="${currentUser.roleKey ne '4'}">
				                                                <td data-key="${ruleTypeCog.rebateRuleKey}" style="text-align:left; border-top-width: 0px">
				                                                    <a class="btn btn-xs edit btn-red">修改</a>
				                                                    <c:if test="${ruleTypeCog.ruleType != '4' or ruleAreaCog.areaCode != '000000'}">
				                                                       <a class="btn btn-xs del btn-brownness">删除</a>
				                                                    </c:if>
				                                                    <c:if test="${ruleTypeCog.ruleType == '1' or ruleTypeCog.ruleType == '2'}">
				                                                    	<c:if test="${not empty(ruleTypeCog.eruptRuleInfo)}">
						                                                    <a class="btn btn-xs erupt btn-orange">爆点红包</a>
				                                                    	</c:if>
				                                                    </c:if>
                                                                    <c:if test="${activityType ne '8' && activityType ne '13'}">
                                                                        <a class="btn btn-xs clone btn-red">克隆</a>
                                                                    </c:if>
				                                                </td>
				                                            </c:if>
				                                        </tr>
				                                        <tr style="background-color: white;"><td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">16</c:when><c:otherwise>15</c:otherwise></c:choose>" style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
				                                         <div id="collapseInnerArea${idxArea.count}Rule${idxType.count}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${idxType.count}">
				                                           <div class="panel-body" style="padding:5px;">
				                                           
				                                               <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
				                                                <thead>
				                                                    <tr class="success" style="background-color: #f0d2d0;">
                                                                        <th style="width:5%; background: transparent;">序号</th>
                                                                        <th style="width:8%; background: transparent;">扫码类型</th>
                                                                        <th style="width:14%; background: transparent;">奖品类型</th>
                                                                        <th style="width:6%; background: transparent;">随机类型</th>
                                                                        <th style="width:8%; background: transparent;">配置积分</th>
                                                                        <th style="width:8%; background: transparent;">配置金额</th>
                                                                        <th style="width:8%; background: transparent;">待激活金额</th>
                                                                        <th style="width:10%; background: transparent;">津贴卡类型</th>
                                                                        <th style="width:10%; background: transparent;">集卡类型</th>
                                                                        <th style="width:6%; background: transparent;">总个数</th>
                                                                        <th style="width:6%; background: transparent;">剩余数</th>
                                                                        <th style="width:8%; background: transparent;">概率</th>
                                                                        <th style="width:10%; background: transparent;">阶梯区间</th>
				                                                    </tr>
				                                                </thead>
				                                                <tbody class="addTri">
				                                                <c:choose>
				                                                    <c:when test="${fn:length(ruleTypeCog.vpointsCogLst) gt 0}">
				                                                       <c:forEach items="${ruleTypeCog.vpointsCogLst}" var="vpoint" varStatus="idxV">
				                                                        <tr>
				                                                            <td style="text-align:center;">
				                                                                <span>${idxV.count}</span>
				                                                            </td>
				                                                            <td style="text-align:center;">
				                                                                <c:choose>
				                                                                    <c:when test="${vpoint.scanType eq '0'}"><span style="color: red;">首扫</span></c:when>
				                                                                    <c:when test="${vpoint.scanType eq '1'}"><span>普通扫码</span></c:when>
				                                                                </c:choose>
				                                                            </td>
				                                                            <td>
				                                                                  <span>
				                                                                  <c:forEach items="${prizeTypeMap}" var="item">
				                                                                    <c:if test="${vpoint.prizeType == item.key}">${item.value}<c:if test="${vpoint.prizePayMoney > 0}">-支付:${vpoint.prizePayMoney}</c:if><c:if test="${vpoint.prizeDiscount > 0}">-优惠:${vpoint.prizeDiscount}</c:if></c:if>
				                                                                  </c:forEach>
				                                                                  </span>
				                                                            </td>
                                                                            <td style="text-align:center;">
                                                                                <span>
                                                                                <c:choose>
                                                                                    <c:when test="${vpoint.randomType eq '0'}">随机</c:when>
                                                                                    <c:when test="${vpoint.randomType eq '1'}">固定</c:when>
                                                                                </c:choose>
                                                                                </span>
                                                                            </td>
				                                                            <td style="text-align:center;">
                                                                                <c:choose>
                                                                                    <c:when test="${vpoint.randomType eq '0'}">
                                                                                        <span>${vpoint.minVpoints}&nbsp;-&nbsp;${vpoint.maxVpoints}</span>
                                                                                    </c:when>
                                                                                    <c:when test="${vpoint.randomType eq '1'}">
                                                                                        <span>${vpoint.minVpoints}</span>
                                                                                    </c:when>
                                                                                </c:choose>
				                                                            </td>
				                                                            <td style="text-align:center;">
                                                                                <c:choose>
                                                                                    <c:when test="${vpoint.randomType eq '0'}">
                                                                                        <span>${vpoint.minMoney}&nbsp;-&nbsp;${vpoint.maxMoney}</span>
                                                                                    </c:when>
                                                                                    <c:when test="${vpoint.randomType eq '1'}">
                                                                                        <span>${vpoint.minMoney}</span>
                                                                                    </c:when>
                                                                                </c:choose>
				                                                            </td>
                                                                            <td>
                                                                                  <span>
                                                                                  <c:forEach items="${allowanceTypeMap}" var="item">
                                                                                    <c:if test="${vpoint.allowanceType eq item.key}">${item.value}<c:if test="${vpoint.allowanceMoney > 0}">-金额:${vpoint.prizePayMoney}</c:if></c:if>
                                                                                  </c:forEach>
                                                                                  </span>
                                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.cardName}</span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.cogAmounts}</span>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.restAmounts}</span>
				                                                            </td>
				                                                            <td style="text-align:center;">
				                                                                  <span>${vpoint.rangePercent}%</span>
				                                                                  <c:if test="${vpoint.prizePercentWarn gt 0}">
                                                                                        <i class="iconfont icon-jinggao" style="color: red; font-weight: bold;" title="占比预警配置:${vpoint.prizePercentWarn}%" ></i>
				                                                                  </c:if>
				                                                            </td>
				                                                            <td>
				                                                                  <span>${vpoint.scanNum}</span>
				                                                            </td>
				                                                        </tr>
				                                                       </c:forEach>
				                                                    </c:when>
				                                                    <c:otherwise>
				                                                        <tr>
				                                                            <td colspan="12"><span>查无数据！</span></td>
				                                                        </tr>
				                                                    </c:otherwise>
				                                                </c:choose>
				                                                </tbody>
				                                               </table>
				                                           </div>
				                                         </div>
				                                        </td></tr>
                                                          </div>
				                                       </c:forEach>
				                                    </div>
				                                </c:when>
				                                <c:otherwise>
				                                    <tr>
				                                        <td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">14</c:when><c:otherwise>13</c:otherwise></c:choose>"><span>查无数据！</span></td>
				                                    </tr>
				                                </c:otherwise>
				                                </c:choose>
				                            </tbody>
				                        </table>
			                        </div>
					           </div>
					        </div>
					        </c:forEach>
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
                    <h6 class="clone_success">${areaName}克隆成功</h6>
                    <h6 class="clone_fail">${areaName}克隆失败</h6>
                    <h6 class="import_success">${areaName}保存成功</h6>
                    <h6 class="import_fail">${areaName}保存失败</h6>
                    <h6 class="import_fail_lackFirstMoney">${areaName}地区规则编辑失败：导入金额缺少首扫区间金额</h6>
                    <h6 class="import_fail_lackFirstVpoints">${areaName}地区规则编辑失败：导入积分缺少首扫区间积分</h6>
                    <h6 class="import_fail_lackDoubtMoney">${areaName}地区规则编辑失败：导入金额缺少可疑区间金额</h6>
                    <h6 class="import_fail_lackDoubtVpoints">${areaName}地区规则编辑失败：导入积分缺少可疑区间积分</h6>
                    <h6 class="import_fail_repeatarea">${areaName}区域下的规则已存在</h6>
                    <h6 class="import_fail_scannum">${areaName}保存失败,不允许所有奖项配置项都配置阶梯区间</h6>
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
