var maxPage = 0;
var currentPage = 0;

var startIndex = 0;
var perPage = 0;
var totalCount = 0;

var defaultPerPage = 15;

$(function(){
	var extra = $("#pagination").data("extra");
	var pageContent = "<tr><td class='pageLeft'><label>每页显示</label><input class='perPage' /><label>条</label>" +
					  "<label>总</label><span class='showTotal'></span><label>条</label>" +
					  "<label>共</label><span class='totalPage'></span><label>页</label></td><td class='pageRight'>" +
					  "<a class='firstPage'>首页</a><a class='prevPage'>上一页</a><input class='countings' />" +
					  "<a class='nextPage'>下一页</a><a class='lastPage'>末页</a></td></tr>";
	
	if(extra == "open"){
		pageContent = "<tr><td class='pageLeft'><label></label></td><td class='pageRight'>" +
					  "<a class='prevPage'>上一页</a><a class='nextPage'>下一页</a></td></tr>";
	} else if(extra == "extra"){
		pageContent = "<tr><td class='pageLeft'>" +
					  "<label>总</label><span class='showTotal'></span><label>条</label>" +
					  "<label>共</label><span class='totalPage'></span><label>页</label></td><td class='pageRight'>" +
					  "<a class='prevPage'>上一页</a><a class='nextPage'>下一页</a></td></tr>";
	}
	
	$("#pagination").append(pageContent);
	
	startIndex = $("input.tableStartIndex").val() ? $("input.tableStartIndex").val() : 0;
	perPage = $("input.tablePerPage").val() ? $("input.tablePerPage").val() : 15;
	totalCount = $("input.tableTotalCount").val() ? $("input.tableTotalCount").val() : 0;
	currentPage = $("input.tableCurPage").val() ? $("input.tableCurPage").val() : 0;
	orderCol = $("input.tableOrderCol").val() ? $("input.tableOrderCol").val() : "";
	orderType = $("input.tableOrderType").val() ? $("input.tableOrderType").val() : "";
	
	$("#pagination .perPage").val(perPage);
	$("#pagination .showTotal").text(totalCount);
	
	doPagination(startIndex, perPage, totalCount);
	
	$("#pagination a:not(.page_disable)").off();
	$("#pagination a:not(.page_disable)").on("click", function(){
		orderCol = $("input.tableOrderCol").val() ? $("input.tableOrderCol").val() : "";
		orderType = $("input.tableOrderType").val() ? $("input.tableOrderType").val() : "";
		if($(this).is(".nextPage")){
			startIndex = parseInt(perPage) + parseInt(startIndex);
			++currentPage;
			currentPage = parseInt(currentPage) > parseInt(maxPage) ? maxPage : currentPage;
		} else if($(this).is(".lastPage")){
			startIndex = parseInt(perPage * (maxPage - currentPage)) + parseInt(startIndex);
			currentPage = maxPage;
		} else if($(this).is(".prevPage")){
			startIndex = startIndex == 0 ? 0 : startIndex - perPage;
			currentPage = (currentPage - 1) > 1 ? (currentPage - 1) : 1;
		} else if($(this).is(".firstPage")){
			startIndex = 0;
			currentPage = 1;
		}
		var vals = startIndex + "," + perPage + "," + currentPage + "," + orderCol + "," + orderType;
		$("input[name=pageParam]").val(vals);
		
		var values = "";
		$("div.form-group").find(":input[name]:not(:hidden)").each(function(){
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
		$("input[name=queryParam]").val(values);
		
		$("form.listForm").submit();
	});
	
	//每页条数跳起来
	$("#pagination .perPage").on("keydown", function(event){
		var val = $(this).val() == "" ? 1 : ($(this).val() == 0) ? 1 : $(this).val();
		if(event.keyCode == 13){
			var curPerPage = val;
			var chn_currentPage = 0;
			var chn_startIndex = 0;
			//先判断一下输入的这个数是不是比最大条数还要大
			if(parseInt(curPerPage) > parseInt(totalCount)){
				chn_currentPage = 1;
				chn_startIndex = 0;
			} else {
				chn_currentPage = currentPage;
				chn_startIndex = curPerPage * (currentPage - 1);
			}
			var vals = chn_startIndex + "," + val + "," + chn_currentPage + "," + orderCol + "," + orderType;
			$("input[name=pageParam]").val(vals);
			
			var values = "";
			$("div.form-group").find(":input[name]:not(:hidden)").each(function(){
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
			$("input[name=queryParam]").val(values);
			
			
			$("form.listForm").submit();
		}
	});
	
	//页数跳起来
	$("#pagination .countings").on("keydown", function(event){
		if(event.keyCode == 13){
			var current = $(this).val() > maxPage ? maxPage : $(this).val();
			startIndex = perPage * (current - 1);
			var vals = startIndex + "," + perPage + "," + current + "," + orderCol + "," + orderType;
			$("input[name=pageParam]").val(vals);
			
			var values = "";
			$("div.form-group").find(":input[name]:not(:hidden)").each(function(){
				values += "," + $(this).val();
			});
			values = values == "" ? "" : values.substr(1);
			$("input[name=queryParam]").val(values);
			
			$("form.listForm").submit();
		}
	});
	
});

function doPaginationOrder() {
	orderCol = $("input.tableOrderCol").val() ? $("input.tableOrderCol").val() : "";
	orderType = $("input.tableOrderType").val() ? $("input.tableOrderType").val() : "";
	
	var current = 1 > maxPage ? maxPage : 1;
	startIndex = perPage * (current - 1);
	var vals = startIndex + "," + perPage + "," + current + "," + orderCol + "," + orderType;
	$("input[name=pageParam]").val(vals);
	
	var values = "";
	$("div.form-group").find(":input[name]:not(:hidden)").each(function(){
		values += "," + $(this).val();
	});
	values = values == "" ? "" : values.substr(1);
	$("input[name=queryParam]").val(values);
	
	$("form.listForm").submit();
}

function doPagination(startIndex, perPage, totalCount) {
	maxPage = Math.ceil(totalCount / perPage);
	if(maxPage == 0 || isNaN(maxPage)) maxPage = 1;
	$("#pagination .totalPage").text(maxPage);
	$("input.countings").val(currentPage);
	if(maxPage == 1 || maxPage == 0){
		$("#pagination .firstPage").addClass("page_disable");
		$("#pagination .prevPage").addClass("page_disable");
		$("#pagination .nextPage").addClass("page_disable");
		$("#pagination .lastPage").addClass("page_disable");
	} else {
		if(totalCount != 0){
			if(currentPage == 1){
				$("#pagination .firstPage").addClass("page_disable");
				$("#pagination .prevPage").addClass("page_disable");
				$("#pagination .nextPage").removeClass("page_disable");
				$("#pagination .lastPage").removeClass("page_disable");
			} else if(currentPage == maxPage){
				$("#pagination .firstPage").removeClass("page_disable");
				$("#pagination .prevPage").removeClass("page_disable");
				$("#pagination .nextPage").addClass("page_disable");
				$("#pagination .lastPage").addClass("page_disable");
			}
		} else {
			$("#pagination .firstPage").removeClass("page_disable");
			$("#pagination .prevPage").removeClass("page_disable");
			$("#pagination .nextPage").removeClass("page_disable");
			$("#pagination .lastPage").removeClass("page_disable");
		}
	}
}