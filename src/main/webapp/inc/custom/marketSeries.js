/**
 * @title market series tools
 * @creator RoyFu
 * @version 1.0.0
 * @description advanced pagination tools
 * @copyright Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.
 */
// loading jQuery for Base, if not then throws error
if(!jQuery) throw new Error("MarketSeries tools requires jQuery");
(function($){
	// 初始化控件
	$.fn.initMarketSeries = function(opts) {
		var defaultVals = {
			basePath : "",
			series : "",
			store : ""
		};
		var options = $.extend(defaultVals, opts);
		var $block = $(this);
		var $series = $block.find(options.series);
		var $store = $block.find(options.store);
		var curSeriesKey = $series.val();
		var curStoreKey = $store.val();
		if(curStoreKey != ""){
			loadSeries(options.basePath, $series, $store, curSeriesKey, curStoreKey);
		} else {
			loadSeries(options.basePath, $series, $store, curSeriesKey);
		}
	};
	// 获取超市系
	var loadSeries = function(basePath, targetSeries, targetStore, seriesKey, storeKey) {
		var $series = targetSeries;
		var $store = targetStore;
		$.ajax({
			type: "POST",
			url: basePath + "/sasMarket/loadAllSeries.do",
			async: false,
			dataType: "json",
			success: function(data){
				$series.empty();
				var defaultVals = "<option value=''>--</option>";
				if(data){
					var len = data.length;
					var options = defaultVals;
					for(var i=0;i<len;i++){
						if(seriesKey && seriesKey != "" && seriesKey == data[i].marketCode){
							options += "<option value='"+data[i].marketCode+"' selected='selected'>"+data[i].marketName+"</option>";
						} else {
							options += "<option value='"+data[i].marketCode+"'>"+data[i].marketName+"</option>";
						}
					}
					$series.append(options);
					loadStore(basePath, $series, $store, storeKey);
					$series.trigger("change");
				} else {
					$store.empty();
					$series.append(defaultVals);
					$store.append(defaultVals);
					alert("未配置任何的超市系！")
				}
			}
		});
	};
	// 读取门店
	var loadStore = function(basePath, triggerSeries, targetStore, storeKey) {
		var $target = triggerSeries;
		var $store = targetStore;
		$target.change(function(){
			var curId = $target.val();
			$.ajax({
				type: "POST",
				url: basePath + "/sasMarket/loadStoreBySeries.do",
				async: false,
				data: {nodeName : curId},
				dataType: "json",
				success: function(data){
					$store.empty();
					var defaultVals = "<option value=''>--</option>";
					if(data){
						var options = defaultVals;
						if(data != "noData"){
							var len = data.length;
							for(var i=0;i<len;i++){
								if(storeKey && storeKey != "" && storeKey == data[i].storeKey){
									options += "<option value='"+data[i].storeKey+"' selected='selected'>"+data[i].storeName+"</option>";
								} else {
									options += "<option value='"+data[i].storeKey+"'>"+data[i].storeName+"</option>";
								}
							}
						}
						$store.append(options);
					}
				}
			});
		});
	};
})(jQuery);