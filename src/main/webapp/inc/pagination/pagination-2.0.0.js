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
	
	$.fn.initPagination = function(opts){
		
		var defaultVals = {
			path : "",                    // basePath for every page
			showPageLimit : false,        // if has the left pager input
			pageLimit : 15,               // shown lines for every page
			startIndex : 0,               // start index for every page
			maxCount : 0,                 // current page max lines
			queryParam : "",              // query parameters
			form : ""                     // request for the page form
		};
		var options = $.extend(defaultVals, opts);
		var form = options.form;
		var $block = $(this);
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
		
		$(document.createElement('link')).attr({
			href: options.path + "/inc/pagination/pagination.css",
			rel: "stylesheet",
			type: "text/css"
		}).appendTo('head');
		
		// jump to previous page
		$block.delegate("span.prev.def-normal", "click", function(){
			var currentPage = parseInt(options.startIndex) - parseInt(options.pageLimit);
			var queryFlag = true;
			if(parseInt(currentPage) < 0) {
				currentPage = defaultVals.startIndex;
				queryFlag = false;
			}
			if(queryFlag) {
				var form = options.pageForm;
				$("form[name='"+form+"']").find("input[name=startIndex]").val(currentPage);
				$("form[name='"+form+"']").find("input[name=pagePerCount]").val(options.pageLimit);
				$("form[name='"+form+"']").find("input[name=queryParam]").val(options.queryParam);
				$("form[name='"+form+"']").submit();
			} else {
				$(this).addClass("def-disabled");
			}
		});
		
		// jump to next page
		$block.delegate("span.next.def-normal", "click", function(){
			var currentPage = parseInt(options.startIndex) + parseInt(options.pageLimit);
			var queryFlag = true;
			var form = options.pageForm;
			
			if(parseInt(options.maxCount) < options.pageLimit) {
				currentPage = options.startIndex;
				queryFlag = false;
			}
			
			if(queryFlag) {
				var form = options.pageForm;
				$("form[name='"+form+"']").find("input[name=startIndex]").val(currentPage);
				$("form[name='"+form+"']").find("input[name=pagePerCount]").val(options.pageLimit);
				$("form[name='"+form+"']").find("input[name=queryParam]").val(options.queryParam);
				$("form[name='"+form+"']").submit();
			} else {
				$(this).addClass("def-disabled");
			}
		});
		
		// limits per page input
		$block.delegate("input.cpager", "keydown", function(event){
			if(event.keyCode == "13") {
				var pageLimit = $(this).val();
				var startIndex = 0;
				var form = options.pageForm;
				$("form[name='"+form+"']").find("input[name=startIndex]").val(startIndex);
				$("form[name='"+form+"']").find("input[name=pagePerCount]").val(pageLimit);
				$("form[name='"+form+"']").find("input[name=queryParam]").val(options.queryParam);
				$("form[name='"+form+"']").submit();
			}
		});
	};
	
})(jQuery);
