String.prototype.replaceAll = function(s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}

$(function(){
	
	// 分辨率
	var $tableColInput = $(":hidden[id^='screenWidth" + screen.width + "']");
	if ($tableColInput.val()) {
		var tableColAry = $tableColInput.val().split(",");
		$tableColInput.closest("table").find("thead:eq(0) > tr:eq(0) > th").each(function(i, obj){
			$(this).css("width", tableColAry[i] + "%");
		});
	}

	// 回显查询条件
	if ($("input[name='queryParam']").val()) {
		var item = '';
		var queryParamAry = $("input[name='queryParam']").val().split(",");
		$("div.search").find(":input[name]:not(:hidden)").each(function(i, obj){
			if(queryParamAry[i] != '' 
				&& $(this).attr("name") == "checkboxImg"
				&& queryParamAry[i].split(";").length > -1){
				changeData(queryParamAry[i]);
			}else{
				$(this).val(queryParamAry[i]).data("currval", queryParamAry[i]);
			}
		});
	}
    
    // 初始化排序
    $orderCol = $("[data-ordercol='" + $("input.tableOrderCol").val() + "']");
    $orderCol.data("ordertype", $("input.tableOrderType").val());
    if ($("input.tableOrderCol").val() == "") {
    	$("[data-orderdef]").data("ordertype","desc").append("<i class='iconfont icon-paixu-jiangxu' style='color:green; margin-left:2px;'></i>");
    } else {
        if ($("input.tableOrderType").val() == "asc") {
        	$orderCol.append("<i class='iconfont icon-paixu-shengxu' style='color:green; margin-left:2px;'></i>");
        } else {
        	$orderCol.append("<i class='iconfont icon-paixu-jiangxu' style='color:green; margin-left:2px;'></i>");
        }
    }
	
	// 排序
    $("[data-ordercol]").on("click", function(){
        $("input.tableOrderCol").val($(this).data("ordercol"));
        $("input.tableOrderType").val($(this).data("ordertype") == "desc" ? "asc" : "desc");
        
        // 跳转到首页
        doPaginationOrder();
    });
    
	//列表-全选按钮事件
	$("table.table").find("th.checkbox-column :checkbox").off();
	$("table.table").find("th.checkbox-column :checkbox").on("change", function(){
		var checked = $(this).prop("checked");
		var tbody = $(this).parents("table.table").children("tbody");
		tbody.find(".uniform").each(function(n, checkbox){
			var chkbox = $(this);
			chkbox.prop("checked", checked).trigger("change");
		});
	});
	
	//单个绑定控制全选按钮事件
	$("table.table tbody tr td :checkbox").off();
	$("table.table tbody tr td :checkbox").on("click", function(){
		var checkFlag = true;
		//
		$("table.table tbody tr td :checkbox").each(function(){
			var checked = $(this).prop("checked");
			if(!checked){
				checkFlag = false;
			}
		});
		
		var allchecked = $(this).parents("table.table").find("tr th.checkbox-column :checkbox");
		if(checkFlag){
			allchecked.prop("checked", true);
		} else {
			allchecked.prop("checked", false);
		}
	});
	
	//列表-查询按钮
	$("button.btn-primary").click(function(){
		var $form = $(this).parents(".listForm");
		var values = "";
		$form.find("div.form-group").find(":input[name]:not(:hidden)").each(function(){
			var putName=$(this).attr('name');
			if(putName!='checkboxValue'){
				var data=$(this).val();
				if(data){
					var dataNew=data.replaceAll(",",";");
					values += "," + dataNew;
				}else{
					values += "," + data;
				}
			}
		});
		values = values == "" ? "" : values.substr(1);
		$form.find("input[name=queryParam]").val(values);
		
//		//加入表头排序 2014-08-22 HaoQi start
//		var orderCol = $form.find("input.tableOrderCol").val();
//		var orderType = $form.find("input.tableOrderType").val();
//		if(orderCol && orderType && orderCol != "" && orderType != ""){
//			var startIndex = $form.find("input.tableStartIndex").val() ? $form.find("input.tableStartIndex").val() : 0;
//			var perPage = $form.find("input.tablePerPage").val() ? $form.find("input.tablePerPage").val() : 15;
//			var currentPage = $form.find("input.tableCurPage").val() ? $form.find("input.tableCurPage").val() : 0;
//			var pageParam = startIndex + "," + perPage + "," + currentPage + "," + orderCol + "," + orderType;
//			$form.find("input[name=pageParam]").val(pageParam);
//		} else {
			//让每次查询时，重置分页信息
			var pageParam = "0,15,1";
			$form.find("input[name=pageParam]").val(pageParam);
			$form.find("input[name=startIndex]").val(0);
			$form.find("input[name=pagePerCount]").val(15);
//		}
		
		//加入表头排序 2014-08-22 HaoQi end
		$form.submit();
	});
	
	//列表-重置按钮
	$("button.btn-reset").click(function(){
		var $form = $(this).parents(".listForm");
		$form.find("div.form-group").find(":input[name]:not(:hidden)").each(function(){
			if ($(this).is(":checkBox")) {
				$(this).removeAttr("checked");
				
			} else {
	            $(this).val("");
	            //是区域控件的，特殊处理一下
	            if($(this).hasClass("zProvince")){
	            	$(this).val("");
	            } else if($(this).hasClass("zCity")){
	            	$(this).html("<option value=''>--</option>");
	            } else if($(this).hasClass("zDistrict")){
	            	$(this).html("<option value=''>--</option>");
	            }
	            if($(this).hasClass("receiptPeriod")){
	            	$(this).find("option:eq(0)").prop("selected", true);
	            }
			}
        });
	});
	

	// 双击切换
	$("td.dblclick").dblclick(function(){
		var title = $(this).attr("title");
		if (title == "") return false;
		var conent = $(this).find("span").text();
		$(this).find("span").text(title);
		$(this).attr("title", conent);
		
		// 颜色区分
		var txtColor = $(this).css("color");
		var preColor = $(this).data("preColor");
		if (preColor == undefined) {
			$(this).css("color", "red");
			$(this).data("preColor", txtColor);
		} else {
            $(this).css("color", preColor);
            $(this).data("preColor", txtColor);
		}
	});
	
});

