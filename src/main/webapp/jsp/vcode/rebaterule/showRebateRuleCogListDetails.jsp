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
<div class="panel-group" id="accordionArea">
    <c:forEach items="${paramValues.resultList[0]}" var="ruleAreaCog" varStatus="idxArea">
        <span>${ruleAreaCog}</span>
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

                <c:if test="${param.currentUser.roleKey ne '4'}">
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
                            <c:if test="${param.projectName == '1'}"> <th style="width:6%;">扫码角色</th></c:if>
                            <!-- <th style="width:5%;">限制</br>有效</th> -->
                            <c:if test="${param.currentUser.roleKey ne '4'}">
                                <th style="width:6%;">操作</th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${fn:length(ruleAreaCog.rebateRuleCogLst) gt 0}">
                                <div class="panel-group" id="accordionRule${idxArea.count}" role="tablist" aria-multiselectable="true">
                                    <c:forEach items="${ruleAreaCog.rebateRuleCogLst}" var="ruleTypeCog" varStatus="idxType">
                                        <tr title="${ruleTypeCog.remarks}">
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
                                            <c:if test="${param.projectName == '1'}">
                                                <td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idxArea.count}" href="#collapseInnerArea${idxArea.count}Rule${idxType.count}" aria-expanded="false" aria-controls="collapseInnerArea${idxArea.count}Rule${idxType.count}" style="cursor: pointer;  border-top-width: 0px; line-height: 10px; position: relative;">
                                                    <c:forEach items="${param.roleInfoAll }" var="roleItem">
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
                                            <c:if test="${param.currentUser.roleKey ne '4'}">
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
                                                    <c:if test="${param.activityType ne '8'}">
                                                        <a class="btn btn-xs clone btn-red">克隆</a>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                        </tr>
                                        <tr style="background-color: white;"><td colspan="<c:choose><c:when test="${param.currentUser.roleKey ne '4'}">16</c:when><c:otherwise>15</c:otherwise></c:choose>" style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
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
                                                            <th style="width:10%; background: transparent;">津贴卡类型</th>
                                                            <th style="width:10%; background: transparent;">集卡类型</th>
                                                            <th style="width:6%; background: transparent;">总个数</th>
                                                            <th style="width:6%; background: transparent;">剩余数</th>
                                                            <th style="width:8%; background: transparent;">概率</th>
                                                            <th style="width:10%; background: transparent;">阶梯区间</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
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
				                                                                  <c:forEach items="${param.prizeTypeMap}" var="item">
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
                                                                                  <c:forEach items="${param.allowanceTypeMap}" var="item">
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
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="<c:choose><c:when test="${param.currentUser.roleKey ne '4'}">14</c:when><c:otherwise>13</c:otherwise></c:choose>"><span>查无数据！</span></td>
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