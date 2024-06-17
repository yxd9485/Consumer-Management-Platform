/**
 * @title pagination tools
 * @creator RoyFu
 * @version 2.0.0
 * @description advanced pagination tools
 * @copyright Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.
 */
// loading jQuery for Base, if not then throws error
if(!jQuery) throw new Error("Pagination tools requires jQuery");
(function($){
	$.fn.initBlockPagination = function(opts){
		
		var defaultVals = {
			path : "",                    // basePath for every page
			block : "",                   // response target on showing pagers block   
			showPageLimit : false,        // if has the left pager input
			pageLimit : 15,               // shown lines for every page
			startIndex : 0,               // start index for every page
			maxCount : 0,                 // current page max lines
			queryParam : ""               // query parameters
		};
		var options = $.extend(defaultVals, opts);
		var $block = $(this);
		$block.empty();
		var defaultPrev = options.startIndex == 0 ? "def-disabled" : "def-normal"; 
		var defaultNext = parseInt(options.maxCount) < parseInt(options.pageLimit)
							? "def-disabled" : "def-normal";
		var isShowLimit = options.showPageLimit;
		var limits = "";
		if(isShowLimit) {
			limits = "<label class='page-limit'>"
		 		   + "<span>每页显示</span><input class='cpager' value='"+options.pageLimit+"' /><span>条</span></label>";
		}
		var pageTable = "<tr>"
			 		  + "<td style='width: 65%;'>" + limits + "</td>"
			 		  + "<td style='width: 32%; text-align: right;'>"
			 		  + "<label class='page-pager'>"
			 		  + "<span class='prev " + defaultPrev + "'>上一页</span>"
			 		  + "<span class='next " + defaultNext + "'>下一页</span>"
			 		  + "</label>"
			 		  + "</td>"
			 		  + "</tr>";
		$block.append(pageTable);
		
		var drawData = function(result, block) {
			var pageInfo = result.pageInfo;
			options.startIndex = pageInfo.startNum;
			options.pageLimit = pageInfo.pagePerCount;
			options.maxCount = pageInfo.maxCount;
			var resultList = result.resultList;
			var len = resultList.length;
			var cols = $("div.tabbers > div[data-block='"+block+"']").find("table.dataTable tr th").length;
			var content = "";
			if(len > 0) {
				for(var i = 0; i < len; i++){
					var index = i + 1;
					var id = result.resultList[i].orderKey ? result.resultList[i].orderKey : "";
					content += "<tr id='"+id+"'>";
					for(var j = 0; j < cols; j++){
						var name = $("div.tabbers > div[data-block='"+block+"'] div.detailTable:not(:hidden) table.dataTable tr th:eq("+j+")").data("param");
						var paramType = $("div.tabbers > div[data-block='"+block+"'] div.detailTable:not(:hidden) table.dataTable tr th:eq("+j+")").data("model");
						if(name != "index"){
							if(name.indexOf(".") != -1){
								var nameArr = name.split(".");
								var val = "";
								if(result.resultList[i][nameArr[0]]){
									val = result.resultList[i][nameArr[0]][nameArr[1]] ? result.resultList[i][nameArr[0]][nameArr[1]] : "";
								}
								content += "<td><span>" + val + "</span></td>";
							} else if(name != "showOperation") {
								var val = result.resultList[i][name] ? result.resultList[i][name] : "";
								if(paramType){
									var regexStr = /(\d{1,3})(?=(\d{3})+(?:$|\.))/g;
									if(paramType == "i") {
										val = thousandth(val);
									} else if(paramType = "m") {
										val = "￥" + thousandth(val);
									}
								}
								content += "<td><span>" + val + "</span></td>";
							} else {
								var val = result.resultList[i][name] ? result.resultList[i][name] : "";
								content += "<td class='text_center'><span>" + val + "</span></td>";
							}
						} else {
							content += "<td><span>" + index + "</span></td>";
						}
					}
					
					content += "</tr>";
				}
			} else {
				content += "<tr><td colspan='" + cols + "'>查无数据！</td></tr>";
			}
			$("div.tabbers > div[data-block='"+block+"'] div.detailTable:not(:hidden) table.dataTable tbody").append(content);
			
			var defaultPrev = options.startIndex == 0 ? "def-disabled" : "def-normal"; 
			var defaultNext = options.maxCount < options.pageLimit ? "def-disabled" : "def-normal";
			$("span.prev").removeClass("def-disabled").removeClass("def-normal");
			$("span.next").removeClass("def-disabled").removeClass("def-normal");
			$("span.prev").addClass(defaultPrev);
			$("span.next").addClass(defaultNext);
		};
		
		// loading data
		var loadDataPerPage = function(path, queryParam, startIndex, pageLimit, block) {
			$.ajax({
				type: "POST",
				url: path,
				asnyc: false,
				data: {
					queryParam : queryParam,
					startIndex : startIndex,
					pagePerCount : pageLimit
				},
				dataType: "json",
				success: function(data){
					$("div.tabbers > div[data-block='"+block+"']").find("table.dataTable").find("tbody").empty();
					if(data) {
						drawData(data, block);
					}
				}, error: function(){
				}
			});
		};
		
		loadDataPerPage(options.path, options.queryParam, options.startIndex, options.pageLimit, options.block);
		
		// jump to previous page
		$block.delegate("span.prev.def-normal", "click", function(){
			var startIndex = parseInt(options.startIndex) - parseInt(options.pageLimit);
			var queryFlag = true;
			if(parseInt(startIndex) < 0) {
				startIndex = defaultVals.startIndex;
				queryFlag = false;
			}
			if(queryFlag) {
				var params = "";
				$("div[data-block='"+options.block+"'] > .queryBlock").find(":input:not(:hidden)").each(function(){
					params += "," + $(this).val();
				});
				params = params.length > 0 ? params.substr(1) : params;
				loadDataPerPage(options.path, params, startIndex, options.pageLimit, options.block);
			} else {
				$(this).addClass("def-disabled");
			}
		});
		
		// jump to next page
		$block.delegate("span.next.def-normal", "click", function(){
			var startIndex = parseInt(options.startIndex) + parseInt(options.pageLimit);
			var queryFlag = true;
			var form = options.pageForm;
			
			if(parseInt(options.maxCount) < options.pageLimit) {
				currentPage = options.startIndex;
				queryFlag = false;
			}
			
			if(queryFlag) {
				var params = "";
				$("div[data-block='"+options.block+"'] > .queryBlock").find(":input:not(:hidden)").each(function(){
					params += "," + $(this).val();
				});
				params = params.length > 0 ? params.substr(1) : params;
				loadDataPerPage(options.path, params, startIndex, options.pageLimit, options.block);
			} else {
				$(this).addClass("def-disabled");
			}
		});
		
		// limits per page input
		$block.delegate("input.cpager", "keydown", function(event){
			if(event.keyCode == "13") {
				var pageLimit = $(this).val();
				var startIndex = 0;
				var params = "";
				$("div[data-block='"+options.block+"'] > .queryBlock").find(":input:not(:hidden)").each(function(){
					params += "," + $(this).val();
				});
				params = params.length > 0 ? params.substr(1) : params;
				loadDataPerPage(options.path, params, startIndex, pageLimit, options.block);
			}
		});
		
	};
	
})(jQuery);