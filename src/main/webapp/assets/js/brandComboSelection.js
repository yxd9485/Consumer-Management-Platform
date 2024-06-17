/**
 * use for table class validate
 * create by RoyFu
 * 2014-04-15
 */
jQuery.extend({
	
	/**
	 * 显示品类
	 * @param block 对应元素选择区
	 * @param brandID 品牌ID
	 * @param categoryID 品类ID
	 * @param SKU_ID SKU-ID
	 */
	showCategory : function(block, brand_ID, category_ID, SKU_ID){
		var $block = block;
		var category_url = $block.find(".category_url").val();
		$.ajax({
			type: "POST",
			url: category_url,
			async: false,
			data: {brandKey:brand_ID},
			dataType: "json",
			success: function(result){
				var $category = $block.find("select.proCategory");
				var $sku = $block.find("select.proSKU");
				if($category){
					$category.empty();
				}
				if($sku){
					$sku.empty();
				}
				var content = "";
				var resLen = result ? result.length : 0;
				var defaultVals = "";
				for(var i = 0; i < resLen; i++){
					if(category_ID && category_ID == result[i].categoryKey){
						content += "<option value='"+result[i].categoryKey+"' selected='selected'>"+result[i].categoryName+"</option>";
					} else {
						content += "<option value='"+result[i].categoryKey+"'>"+result[i].categoryName+"</option>";
					}
				}
				content = defaultVals + content;
				$category.append(content);
				
				$.showSKU($block, $category, category_ID, SKU_ID);
				$category.trigger("change");
			},
			error: function(){
			}
		});
	},
	
	findCategory : function(block, brand_ID, category_ID){
		var $block = block;
		var category_url = $block.find(".category_url").val();
		$.ajax({
			type: "POST",
			url: category_url,
			async: false,
			data: {brandKey:brand_ID},
			dataType: "json",
			success: function(result){
				var $category = $block.find("select.proCategory");
				if($category){
					$category.empty();
				}
				var content = "";
				var resLen = result ? result.length : 0;
				var defaultVals = "";
				for(var i = 0; i < resLen; i++){
					if(category_ID && category_ID == result[i].categoryKey){
						content += "<option value='"+result[i].categoryKey+"' selected='selected'>"+result[i].categoryName+"</option>";
					} else {
						content += "<option value='"+result[i].categoryKey+"'>"+result[i].categoryName+"</option>";
					}
				}
				content = defaultVals + content;
				$category.append(content);
			},
			error: function(){
			}
		});
	},
	
	showSKU : function(block, category, category_ID, SKU_ID){
		var $block = block;
		var sku_url = $block.find(".sku_url").val();
		$.ajax({
			type: "POST",
			url: sku_url,
			async: false,
			data: {categoryKey:category_ID},
			dataType: "json",
			success: function(result){
				var $sku = $block.find("select.proSKU");
				if($sku){
					$sku.empty();
				}
				var content = "";
				var resLen = result ? result.length : 0;
				var defaultVals = "";
				for(var i = 0; i < resLen; i++){
					if(SKU_ID && SKU_ID == result[i].skuKey){
						content += "<option value='"+result[i].skuKey+"' selected='selected'>"+result[i].skuName+"</option>";
					} else {
						content += "<option value='"+result[i].skuKey+"'>"+result[i].skuName+"</option>";
					}
				}
				content = defaultVals + content;
				$sku.append(content);
			},
			error: function(){
			}
		});
	},
	/**
	 * 显示积分码
	 * @param block 对应元素选择区
	 * @param brandID 品牌ID
	 * @param categoryID 品类ID
	 * @param SKU_ID SKU-ID
	 */
	showApprovelKey :function(block, category){
		var $block = block;
		var approvel_url = "";
		var SKU_ID = $block.find(".proBrand").val();
		var actionType = $block.find(".appEdit").val();
		if(actionType!=""){
			approvel_url = actionType;
		}else{
			approvel_url = $block.find(".approvel_url").val();
		}
		$.ajax({
			type: "POST",
			url: approvel_url,
			async: false,
			data: {brandkey:SKU_ID},
			dataType: "json",
			success: function(result){
				var $sku = $block.find("select.proSKU");
				if($sku){
					$sku.empty();
				}
				var content = "";
				var resLen = result.length;
				var defaultVals = "";
				for(var i = 0; i < resLen; i++){
					if(SKU_ID && SKU_ID == result[i].brandkey){
						content += "<option value='"+result[i].approvalKey+"' selected='selected'>"+result[i].title+"</option>";
					} else {
						content += "<option value='"+result[i].approvalKey+"'>"+result[i].title+"</option>";
					}
				}
				content = defaultVals + content;
				$sku.append(content);
			},
			error: function(){
			}
		});
	}
});