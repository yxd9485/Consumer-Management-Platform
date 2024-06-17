
/**
 * 弹出选择SKU
 */
$(function(){
	//绑定选择按钮，去后台取SKU数据，无值只显示，有值直接回显
	$("a.selectSKU").off();
	$("a.selectSKU").on("click", function(){
		var url = $(this).attr("ref");
		var comboType = $(this).attr("cape") ? $(this).attr("cape") : "checkbox";
		var refTag = $(this).attr("tage");
        var selVals = $("input[name='"+refTag+"']").val();
		$.ajax({
			url:url,
            type:"POST",
            async: false,
            dataType:"json",
            async:false,
            success:function(result){
            	$("#showSKU .modal-body").empty();
            	var content = "";
            	var len = result.length;
            	if(len > 0){
            		content = "<table class='brand_table'>";
            		
            		for(var i=0; i<len; i++){
        				if(i == 0 || i % 3 == 0){
            				content += "<tr><td><input type='checkbox' class='wave_combo' name='brandgroup' value="+result[i].skuKey+" style='margin:5px;' />"
            						+ "<span class='wave_brands'>"+result[i].skuName+"</span><span flag='skudesc' style='display:none;'>"+result[i].skuDesc+"</span></td>";
            			} else if(i % 3 == 2) {
            				content += "<td><input type='checkbox' class='wave_combo' name='brandgroup' value="+result[i].skuKey+" style='margin:5px;' />"
            						+ "<span class='wave_brands'>"+result[i].skuName+"</span><span flag='skudesc' style='display:none;'>"+result[i].skuDesc+"</span></td></tr>";
            			} else {
            				content += "<td><input type='checkbox' class='wave_combo' name='brandgroup' value="+result[i].skuKey+" style='margin:5px;' />"
            						+ "<span class='wave_brands'>"+result[i].skuName+"</span><span flag='skudesc' style='display:none;'>"+result[i].skuDesc+"</span></td>";
            			}
        			}
            		
            		if(comboType == "radio"){
            			content = content.replace(new RegExp("checkbox", "g"), "radio");
            		}
            		
            		content += "</table>";
            	} else {
            		content = "<div class='nodata_tips'>没有相关SKU数据!</div>";
            	}
            	$("#showSKU .modal-body").append(content);
            	
            	//回显
            	var selValArr = selVals.split(",");
            	$("#showSKU :input[name='brandgroup']").each(function(){
            		for(var i=0; i<selValArr.length; i++){
            			if(selValArr[i] == $(this).val()){
            				$(this).prop("checked", true);
            			}
            		}
            	});
            },error:function(){
            	
            }
		});
		$("#showSKU").modal("show");
	});
	
	//确定时给指定的input赋值
	$("button.selectSKU").off();
	$("button.selectSKU").on("click", function(){
		var target = $(this).attr("target-tag");
		var vals = "";
		$(":input[name=brandgroup]:visible").each(function(){
			if($(this).prop("checked")){
				vals += "," + $(this).val();
			}
		});
		vals = vals == "" ? "" : vals.substr(1);
		$("input[name='"+target+"']").val(vals);
		//回显sku名称
		$("#brandKey_SKUName").text($("input[name='brandgroup']:checked").next().text());
		//回显sku详细信息
		$("textarea[name='SKUINFO']").val($("input[name='brandgroup']:checked").next().next().text()).trigger("change");
	});
});