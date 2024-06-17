<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
%>
	<script>
		$(function(){
            $.runtimeValidate($("#editContractYearDialog"));
            
            //弹出修改所属合同年份窗口
            $("#editContractYear").off();
            $("#editContractYear").on("click",function(){
                var key = "";
                var batchNum = 0;
                var batchName = "";
                var contractYear,contractChangeDate,nextContractYear;
                $("[name='itemCB']:checked").each(function() {
                	batchNum++;
                    key = $(this).val() + "," + key;
                    batchName = $(this).closest("tr").find("#batchName").text() + "</br>" + batchName;
                    contractYear = $(this).closest("tr").find("#contractYear").text();
                    contractChangeDate = $(this).closest("tr").find("#contractChangeDate").text();
                    nextContractYear = $(this).closest("tr").find("#nextContractYear").text();
                })
                if (key == "") {
                    $.fn.alert("请选择要修改的批次!");
                } else {
                    $("#editContractYearDialog :input[name]").val("");
                    $("#editContractYearDialog #batchNum").text(batchNum);
                    $("#editContractYearDialog input[name='batchKey']").val(key);
                    $("#editContractYearDialog input[name='batchName']").val(batchName);
                    if (batchNum == 1) {
                        $("#editContractYearDialog input[name='contractYear']").val(contractYear);
                        $("#editContractYearDialog input[name='contractChangeDate']").val(contractChangeDate);
                        $("#editContractYearDialog input[name='nextContractYear']").val(nextContractYear);
                    }
                    $("#editContractYearDialog").modal("show");
                }
            });
            
            // 确定调整批次
            $("#editContractYearDialog").delegate("#addBtn", "click", function(){
                // 输入元素校验
                var validateResult = $.submitValidate($("#editContractYearDialog"));
                if(!validateResult){
                    return false;
                }
                
                var tips = "您确定修改以下批次吗? </br>" + $("#editContractYearDialog input[name='batchName']").val();
                $.fn.confirm(tips, function(){
                    // 提交表单
                    var url = "<%=cpath%>/qrcodeBatchInfo/doContractYearAjax.do";
                    var paramJson = {};
                    $("#editContractYearDialog :input[name]").each(function(){
                        paramJson[$(this).attr("name")] = $(this).val();
                    });
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: paramJson,
                        dataType: "json",
                        async: false,
                        beforeSend:appendVjfSessionId,
                    success:  function(data) {
                            $("#editContractYearDialog #closeBtn").trigger("click");
                            $.fn.alert(data['errMsg'], function(){
                                $("button.btn-primary").trigger("click");
                            });
                        }
                    });
                });
            });
        });
	</script>
  
    <div class="modal fade" id="editContractYearDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">修改批次所属合同年份</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table" style="line-height: 30px;">
                        <tr>
                            <td width="30%" class="text-right"><label class="title">批次个数：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                  <input type="hidden" name="batchKey">
                                  <input type="hidden" name="batchName">
                                  <label id="batchNum"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="30%" class="text-right"><label class="title">所属合同年份：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="contractYear" tag="validate"
                                        class="form-control input-width-medium required number positive" autocomplete="off"  maxlength="4" />
                                    <label class="validate_tips" style="width:40%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="30%" class="text-right"><label class="title">所属合同年份切换日期：<span>*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="contractChangeDate" class="Wdate form-control input-width-medium"
                                                onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{\'%y-%M-%d\'}'})" autocomplete="off" tag="validate"/>
                                    <label class="validate_tips" style="width:40%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="30%" class="text-right"><label class="title">切换后所属合同年份：<span>*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="nextContractYear" tag="validate"
                                        class="form-control input-width-medium number positive" autocomplete="off"  maxlength="4" />
                                    <label class="validate_tips" style="width:40%"></label>
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
