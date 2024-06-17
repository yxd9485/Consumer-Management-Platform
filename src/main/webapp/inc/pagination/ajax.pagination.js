var ajax_maxPage = 0;
var ajax_currentPage = 0;

var ajax_startIndex = 0;
var ajax_perPage = 0;
var ajax_totalCount = 0;

var ajax_defaultPerPage = 15;


jQuery.extend({
	
	/**
	 * 载入分页显示部分
	 * @param block   当前对应块标签
	 */
	loadPagination : function(block){
		var $pagination = $("#"+block+" #ajaxPagination");
		$pagination.empty();
		var ajax_pageContent = "<tr><td class='pageLeft'><label>每页显示</label>" +
							   "<input class='perPage' onkeydown='$.jumpPerPage($(this), event.which);' /><label>条</label>" +
							   "<label>总</label><span class='showTotal'></span><label>条</label>" +
							   "<label>共</label><span class='totalPage'></span><label>页</label></td><td class='pageRight'>" +
							   "<a class='firstPage' onclick='$.changePage($(this), event.which);'>首页</a>" +
							   "<a class='prevPage' onclick='$.changePage($(this), event.which);'>上一页</a>" +
							   "<input class='countings' onkeydown='$.jumpCountings($(this), event.which);' />" +
							   "<a class='nextPage' onclick='$.changePage($(this), event.which);'>下一页</a>" +
							   "<a class='lastPage' onclick='$.changePage($(this), event.which);'>末页</a></td></tr>";
		$pagination.append(ajax_pageContent);
	},
	
	/**
	 * 块级分页
	 * @param path        响应路径
	 * @param pageParam   分页参数
	 * @param queryParam  查询参数
	 * @param block   当前对应块标签
	 */
	createBlockPagination : function(path, pageParam, queryParam, block){
		$.ajax({
			type: "POST",
			url: path,
			asnyc: false,
			data: {pageParam:pageParam, queryParam:queryParam},
			dataType: "json",
			success: function(data){
				$.showPageResultInfo(data, block);
				$.changeImageHeight(block);
			}, error: function(){
			}
		});
	},
	
	changeImageHeight : function(block){
		// loadHeight();
	},
	
	/**
	 * 设置分页参数
	 * @param p_startCount    起始页码
	 * @param p_pagePerCount  每页显示条数
	 * @param p_currentPage   当前页数
	 * @param p_countAll      总条数
	 * @param block           当前对应块标签
	 */
	setPaginationInfo : function(p_startCount, p_pagePerCount, p_currentPage, p_countAll, block){
		
		var p_maxPage = Math.ceil(p_countAll / p_pagePerCount);
		
		$("#"+block+" #ajaxPagination .perPage").val(p_pagePerCount);
		$("#"+block+" #ajaxPagination .countings").val(p_currentPage);
		$("#"+block+" #ajaxPagination .showTotal").text(p_countAll);
		$("#"+block+" #ajaxPagination .totalPage").text(p_maxPage);
		
		ajax_maxPage = p_maxPage;
		ajax_currentPage = p_currentPage;
		ajax_startIndex = p_startCount;
		ajax_perPage = p_pagePerCount;
		ajax_totalCount = p_countAll;
		
		if(p_maxPage == 1 || p_maxPage == 0){
			$("#"+block+" #ajaxPagination .firstPage").addClass("page_disable");
			$("#"+block+" #ajaxPagination .prevPage").addClass("page_disable");
			$("#"+block+" #ajaxPagination .nextPage").addClass("page_disable");
			$("#"+block+" #ajaxPagination .lastPage").addClass("page_disable");
		} else {
			if(p_countAll != 0){
				if(p_currentPage == 1){
					$("#"+block+" #ajaxPagination .firstPage").addClass("page_disable");
					$("#"+block+" #ajaxPagination .prevPage").addClass("page_disable");
					$("#"+block+" #ajaxPagination .nextPage").removeClass("page_disable");
					$("#"+block+" #ajaxPagination .lastPage").removeClass("page_disable");
				} else if(p_currentPage == p_maxPage){
					$("#"+block+" #ajaxPagination .firstPage").removeClass("page_disable");
					$("#"+block+" #ajaxPagination .prevPage").removeClass("page_disable");
					$("#"+block+" #ajaxPagination .nextPage").addClass("page_disable");
					$("#"+block+" #ajaxPagination .lastPage").addClass("page_disable");
				}
			} else {
				$("#"+block+" #ajaxPagination .firstPage").removeClass("page_disable");
				$("#"+block+" #ajaxPagination .prevPage").removeClass("page_disable");
				$("#"+block+" #ajaxPagination .nextPage").removeClass("page_disable");
				$("#"+block+" #ajaxPagination .lastPage").removeClass("page_disable");
			}
		}
	},
	
	/**
	 * 展示数据
	 * @param result   JSON数据源
	 * @param block    当前对应块标签
	 */
	showPageResultInfo : function(result, block){
		$("#"+block+" div.detailTable:not(:hidden) table tbody").empty();

		var countAll = result.countAll;
		var currentPage = result.pageOrderInfo.currentPage == 0 ? 1 : result.pageOrderInfo.currentPage;
		var pagePerCount = result.pageOrderInfo.pagePerCount;
		var startCount = result.pageOrderInfo.startCount;
		
		var content = "";
		var len = result.resultList.length;
		
		var cols = $("#"+block+" div.detailTable:not(:hidden)").find("table.dataTable tr th").length;
		if(len > 0){
			for(var i = 0; i < len; i++){
				var index = i + 1;
				var id = result.resultList[i].orderKey ? result.resultList[i].orderKey : "";
				content += "<tr id='"+id+"'>";
				for(var j = 0; j < cols; j++){
					var name = $("#"+block+" div.detailTable:not(:hidden) table.dataTable tr th:eq("+j+")").attr("refCol");
					
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
		
		$("#"+block+" div.detailTable:not(:hidden) table.dataTable tbody").append(content);
		
		$.loadPagination(block);
		
		$.setPaginationInfo(startCount, pagePerCount, currentPage, countAll, block);
	},
	
	/**
	 * 分页操作栏
	 */
	changePage : function(pObject, event){
		var $this = pObject;
		
		if(!$this.is(".page_disable")){
			if($this.is(".nextPage")){
				ajax_startIndex = parseInt(ajax_perPage) + parseInt(ajax_startIndex);
				++ajax_currentPage;
				ajax_currentPage = ajax_currentPage > ajax_maxPage ? ajax_maxPage : ajax_currentPage;
			} else if($this.is(".lastPage")){
				ajax_startIndex = parseInt(ajax_perPage * (ajax_maxPage - ajax_currentPage)) + parseInt(ajax_startIndex);
				ajax_currentPage = ajax_maxPage;
			} else if($this.is(".prevPage")){
				ajax_startIndex = ajax_startIndex == 0 ? 0 : ajax_startIndex - ajax_perPage;
				ajax_currentPage = (ajax_currentPage - 1) > 1 ? (ajax_currentPage - 1) : 1;
			} else if($this.is(".firstPage")){
				ajax_startIndex = 0;
				ajax_currentPage = 1;
			}
			var pageVals = ajax_startIndex + "," + ajax_perPage + "," + ajax_currentPage;
			
			var block = $this.parents("#ajaxPagination").attr("ref");
			var path = $this.parents("#ajaxPagination").attr("path");
			
			var vConditions = "";
			
			$("#"+block).find("div.queryBlocks :input:not(:hidden)").each(function(event){
				vConditions += "," + $(this).val();
			});
			
			vConditions = vConditions == "" ? "" : vConditions.substr(1);
			
			var onHide = $this.parents("#ajaxPagination").attr("onHide");
			if(onHide){
				$("#"+block).find("div.queryBlocks :input:hidden").each(function(event){
					vConditions += "," + $(this).val();
				});
			}
			
			var queryVals = vConditions == "" ? "" : onHide ? vConditions.substr(1) : vConditions;
			
			$.createBlockPagination(path, pageVals, queryVals, block);
		}
	},
	
	/**
	 * 每页条数
	 */
	jumpPerPage : function(pObject, event){
		var $this = pObject;
		var val = $this.val() == "" ? 1 : ($this.val() == 0) ? 1 : $this.val();
		if(event == 13){
			var ajax_curPerPage = val;
			var change_currentPage = 0;
			var change_startIndex = 0;
			//先判断一下输入的这个数是不是比最大条数还要大
			if(parseInt(ajax_curPerPage) > parseInt(ajax_totalCount)){
				change_currentPage = 1;
				change_startIndex = 0;
			} else {
				change_currentPage = ajax_currentPage;
				change_startIndex = ajax_curPerPage * (change_currentPage - 1);
			}
			var pageVals = change_startIndex + "," + val + "," + change_currentPage;
			
			var block = $this.parents("#ajaxPagination").attr("ref");
			var path = $this.parents("#ajaxPagination").attr("path");
			
			var vConditions = "";
			$("#"+block).find("div.queryBlocks :input:not(:hidden)").each(function(event){
				vConditions += "," + $(this).val();
			});
			
			vConditions = vConditions == "" ? "" : vConditions.substr(1);
			
			var onHide = $this.parents("#ajaxPagination").attr("onHide");
			if(onHide){
				$("#"+block).find("div.queryBlocks :input:hidden").each(function(event){
					vConditions += "," + $(this).val();
				});
			}
			
			var queryVals = vConditions == "" ? "" : onHide ? vConditions.substr(1) : vConditions;
			
			$.createBlockPagination(path, pageVals, queryVals, block);
		}
	},
	
	/**
	 * 页码数
	 */
	jumpCountings : function(pObject, event){
		var $this = pObject;
		
		if(event == 13){
			var current = $this.val() > ajax_maxPage ? ajax_maxPage : $this.val();
			ajax_startIndex = ajax_perPage * (current - 1);
			if(parseInt(ajax_startIndex) < 0){
				ajax_startIndex = 0;
			}
			var pageVals = ajax_startIndex + "," + ajax_perPage + "," + current;
			
			var block = $this.parents("#ajaxPagination").attr("ref");
			var path = $this.parents("#ajaxPagination").attr("path");
			
			var vConditions = "";
			$("#"+block).find("div.queryBlocks :input:not(:hidden)").each(function(event){
				vConditions += "," + $(this).val();
			});
			
			vConditions = vConditions == "" ? "" : vConditions.substr(1);
			
			var onHide = $this.parents("#ajaxPagination").attr("onHide");
			if(onHide){
				$("#"+block).find("div.queryBlocks :input:hidden").each(function(event){
					vConditions += "," + $(this).val();
				});
			}
			
			var queryVals = vConditions == "" ? "" : onHide ? vConditions.substr(1) : vConditions;
			
			$.createBlockPagination(path, pageVals, queryVals, block);
		}
	}
});