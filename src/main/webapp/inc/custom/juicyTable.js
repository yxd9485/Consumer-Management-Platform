/**
 * @title JuicyTable.js
 * @creator RoyFu
 * @version 1.0.0
 * @description Juicy table tools for "tree" use
 * @copyright Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.
 */
// loading jQuery for Base, if not then throws error
if(!jQuery) throw new Error("JuicyTable tools requires jQuery");
(function($){
	// 初始化控件
	$.fn.initJuicyTable = function(opts){
		// 定义默认值
		var defaultVals = {
			basePath : "",          // base path
			path : "",              // 访问路径
			tableDepth : 2,         // tree展开的最大层级
			currentNode : "",       // 
			haveSignal : true,      // 是否含有标识列
			requestParams : "",     // 数据值数组
			nodeKey : ""            // 数据值对应行的数据ID
		};
		var options = $.extend(defaultVals, opts);
		$(document.createElement('link')).attr({
			href: options.basePath + "/inc/custom/juicyTable.css",
			rel: "stylesheet",
			type: "text/css"
		}).appendTo('head');
		var $table = $(this);
		initTreeBase($table);
		initTreeNodes($table, options);
		if(options.haveSignal){
			initSymbol($table, options);
		}
	};
	// colorArray
	var nc_color = ['berry', 'juicy', 'drop', 'grass', 'wink', 'mashroom'];
	// 全部折起
	var initTreeBase = function(table) {
		var $table = table;
		$table.delegate("i[node]", "click", function(){
			var $this = $(this);
			$this.attr("class", "icon-folder-close");
			$table.find("tr[label='1']>td>i[class*=icon-caret]").attr("class", "icon-caret-right");
			$table.find("tbody>tr[label!='1']").remove();
		});
	};
	// 单行展开或折起
	var initTreeNodes = function(table, options) {
		var $table = table;
		$table.delegate("i[folder]", "click", function(e){
			var $node = $(this);
			var $treeBase = $table.find("i[node]");
			var currentClass = $node.attr("class");
			if(currentClass == "icon-caret-right"){
				$node.attr("class", "icon-caret-down");
				$treeBase.attr("class", "icon-folder-open");
				loadTreeNodeInfo($table, $node, options)
			} else {
				var nodeVal = $node.parents("td").data("key");
				$table.find("tbody>tr[rel='"+nodeVal+"']").remove();
				$node.attr("class", "icon-caret-right");
				var len = $("i.icon-caret-down").length;
				if(len == 0){
					$treeBase.attr("class", "icon-folder-close");
				}
			}
		});
	};
	// 展开行时获取数据
	var loadTreeNodeInfo = function(table, node, options) {
		var $table = table;
		var $node = node;
		var nodeVal = $node.parents("td").data("key");
		$.ajax({
			type: "POST",
			url: options.basePath + options.path,
			dataType : "json",
			data : {
				nodeName : nodeVal
			},
			success : function(result){
				if(result){
					var currentLayer = $node.parents("tr").attr("label");
					var tableDepth = options.tableDepth;
					var folderType = "icon-caret-right";
					var currentDepth = parseInt(currentLayer) + 1;
					if(tableDepth == currentDepth){
						folderType = "";
					}
					var requestParams = options.requestParams;
					var nodeKey = options.nodeKey;
					var haveSignal = options.haveSignal;
					var signal = "";
					if(haveSignal){
						signal = "<td><span class='blocks "+nc_color[currentLayer]+"'></span></td>"
					}
					var resultLen = result.length;
					var content = "";
					for(var i=0;i<resultLen;i++){
						var tableValues = "";
						for(var j=0;j<requestParams.length;j++){
							tableValues += "<td><span>"+result[i][requestParams[j]]+"</span></td>";
						}
						content += "<tr label='"+currentDepth+"' rel='"+nodeVal+"'>"
								 + "<td data-key='"+result[i][nodeKey]+"'><i folder='collapse' class='"+folderType+"'></i></td>"
								 + signal + tableValues + "</tr>";
					}
					$node.parents("tr").after(content);
				}
			}
		});
	};
	// 初始化标识列的操作
	var curSymbolNode = "";
	var initSymbol = function(table, options) {
		var $table = table;
		$table.delegate("span.blocks", "click", function(){
			var $block = $(this);
			var $tr = $block.parents("tr");
			var layer = $tr.attr("label");
			var curNode = $tr.find("td:first").data("key");
			if(curSymbolNode != curNode) {
				$("div.button-menu").show();
				$table.find("tr").removeClass("highlight-line");
				$block.parents("tr").addClass("highlight-line");
				$("div.button-menu").find(".btn-primary").attr("key", curNode).attr("layer", layer);
				$("div.button-menu").find(".btn-info").attr("key", curNode).attr("layer", layer);
				$("div.button-menu").find(".btn-danger").attr("key", curNode).attr("layer", layer);
				$("div.button-menu").find(".btn-warning").attr("key", curNode).attr("layer", layer);
			} else {
				var display = $("div.button-menu").css("display");
				if(display == 'none'){
					$("div.button-menu").show();
					$table.find("tr").removeClass("highlight-line");
					$block.parents("tr").addClass("highlight-line");
					$("div.button-menu").find(".btn-primary").attr("key", curNode).attr("layer", layer);
					$("div.button-menu").find(".btn-info").attr("key", curNode).attr("layer", layer);
					$("div.button-menu").find(".btn-danger").attr("key", curNode).attr("layer", layer);
					$("div.button-menu").find(".btn-warning").attr("key", curNode).attr("layer", layer);
				} else {
					$("div.button-menu").hide();
					$block.parents("tr").removeClass("highlight-line");
				}
			}
			curSymbolNode = curNode;
		});
	};
})(jQuery);
