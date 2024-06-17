if(!jQuery) throw new Error("jQuery required");

$(function(){
	// 绑定选择按钮，去后台取SKU数据，无值只显示，有值直接回显
	$("a.select-sku").off();
	$("a.select-sku").on("click", function(){
		var refTag = $(this).data("target");
		$("input[ref='"+refTag+"']").parents(".skuInfo").find(".validate_tips").text("");
		$("input[ref='"+refTag+"']").parents(".skuInfo").find(".validate_tips").removeClass("valid_fail_text");
		
		var url = $(this).data("url");
		var companyKey = $(this).data("company");
		// 获取类型
		var comboType = $(this).data("type") ? $(this).data("type") : "checkbox";
        var selVals = $("input[ref='"+refTag+"']").val();
		$.ajax({
			url : url,
            type : "POST",
            async : false,
            dataType : "json",
            data : {
            	companyKey : companyKey,
            	skuName : ""
            },
            async : false,
            success : function(result){
            	$("#showSku .modal-body").empty();
            	var content = "";
            	var len = result.length;
            	if(len > 0){
            		content = "<input type='text' class='sel-sku-input' data-url='"+url+"' data-company='"+companyKey+"' data-type='"+comboType+"' /><button class='sel-sku-search'><i class='icon-search'></i>搜索</button>";
            		content += "<table class='sel-sku-table'>";
            		var thead_info = "<thead><tr>"
            					   + "<th style='width:15%;'>模式</th>"
            					   + "<th style='width:85%;'>SKU名称</th>"
            					   + "</tr></thead>";
            		content += thead_info;
            		for(var i=0; i<len; i++){
            			content += "<tbody><tr>"
            					 + "<td style='width:15%;'><input type='checkbox' name='skuGroup' value="+result[i].skuKey+" /></td>"
            					 + "<td style='width:85%;'><span>"+result[i].skuName+"</span><span style='display:none;'>"+result[i].skuDesc+"</span></td>"
            					 + "</tr></tbody>";
        			}
            		if(comboType == "radio"){
            			content = content.replace(new RegExp("checkbox", "g"), "radio");
            		}
            		content += "</table>";
            	} else {
            		content = "<div class='nodata_tips'>没有相关SKU数据!</div>";
            	}
            	$("#showSku .modal-body").append(content);
            	// 回显
            	var selValArr = selVals.split(",");
            	$("#showSku :input[name='skuGroup']").each(function(){
            		for(var i=0; i<selValArr.length; i++){
            			if(selValArr[i] == $(this).val()){
            				$(this).prop("checked", true);
            			}
            		}
            	});
            },error:function(){
            	
            }
		});
		$("#showSku").modal("show");
	});
	// 搜索SKU
	$("#showSku").delegate(".sel-sku-search", "click", function(){
		var skuName = $("#showSku").find("input.sel-sku-input").val();
		var url = $("#showSku").find("input.sel-sku-input").data("url");
		var companyKey = $("#showSku").find("input.sel-sku-input").data("company");
		var comboType = $("#showSku").find("input.sel-sku-input").data("type");
		$.ajax({
			type: "POST",
			url: url,
			dataType : "json",
			data : {
				companyKey : companyKey,
            	skuName : skuName
			},
            success : function(result){
            	$("#showSku .modal-body").empty();
            	var content = "";
            	var len = result.length;
            	if(len > 0){
            		content = "<input type='text' class='sel-sku-input' data-url='"+url+"' data-company='"+companyKey+"' data-type='"+comboType+"' value='"+skuName+"' /><button class='sel-sku-search'><i class='icon-search'></i>搜索</button>";
            		content += "<table class='sel-sku-table'>";
            		var thead_info = "<thead><tr>"
            					   + "<th style='width:15%;'>模式</th>"
            					   + "<th style='width:85%;'>SKU名称</th>"
            					   + "</tr></thead>";
            		content += thead_info + "<tbody>";
            		for(var i=0; i<len; i++){
            			content += "<tr>"
            					 + "<td style='width:15%;'><input type='checkbox' name='skuGroup' value="+result[i].skuKey+" /></td>"
            					 + "<td style='width:85%;'><span>"+result[i].skuName+"</span><span style='display:none;'>"+result[i].skuDesc+"</span></td>"
            					 + "</tr>";
        			}
            		if(comboType == "radio"){
            			content = content.replace(new RegExp("checkbox", "g"), "radio");
            		}
            		content += "</tbody>";
            		content += "</table>";
            	} else {
            		content = "<div class='nodata_tips'>没有相关SKU数据!</div>";
            	}
            	$("#showSku .modal-body").append(content);
            }
		});
	});
	// 绑定回车事件
	$("#showSku").delegate("input.sel-sku-input", "keydown", function(event){
		if(event && event.keyCode == 13) {
			$("#showSku").find(".sel-sku-search").trigger("click");
		}
	});
	
	// 确定时给指定的input赋值
	$("button.selectSku").off();
	$("button.selectSku").on("click", function(){
		var target = $(this).data("target");
		var parentTag = $(this).data("parentTag");
		var vals = "";
		var texts = "";
		var descs = "";
		$(":input[name=skuGroup]:visible").each(function(){
			var $input = $(this);
			if($input.prop("checked")){
				vals += "," + $input.val();
				texts += "," + $input.parents("td").next().find("span:visible").text();
				descs += "," + $input.parents("td").next().find("span:hidden").text();
			}
		});
		vals = vals == "" ? "" : vals.substr(1);
		texts = texts == "" ? "" : texts.substr(1);
		descs = descs == "" ? "" : descs.substr(1);
		$("input[ref='"+target+"']").val(vals);
		$("input[ref='"+target+"']").next().val(texts);
		$(".show-sku-name").text(texts);
		$(".sku-desc").text(descs);
	});
});