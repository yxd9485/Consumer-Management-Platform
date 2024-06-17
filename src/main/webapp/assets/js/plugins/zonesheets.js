/**
 * 文件名： zonesheets.js<br>
 * 版权： Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述： 区域空间v2<br>
 * 创建人：付中岳<br>
 * 修改人： HaoQi<br>
 * 修改时间： 2014-07-28<br>
 * 修改内容： 修改<br>
 */
$.fn.extend({
	/** 省 */
	_province : "",
	/** 市 */
	_city : "",
	/** 县(区) */
	_district : "",
	/** 项目path */
	_basePath : "",
	/** 是否显示全部 */
	_isTopAll : "",
	/** 是否只显示项目所属省 */
	_isProjectArea : false,
	/** 是否显示全部（活动规则使用） */
	_isAllArea : false,
	/** 是否显示省外 */
	_isOutsideProvince : false,
	/** 是否包含全部地区 */
	_isIncludeAllArea : false,
	/** 最终一层的值 */
	_finalDistrict : "",
	/** input隐藏域 */
	_districtInput : "",
	
	/**
	 * @param basePath 项目contextpath
	 * @param isTopAll 是否需要(全部)的数据
	 * @param district 地区值
	 * @param isProjectArea 是否只显示项目所属省
	 * @param isAllArea 是否显示全部（活动规则使用）
	 * @param isOutsideProvince 是否显示省外
	 * @param isIncludeAllArea 是否包含全部地区
	 */
	initZone : function(basePath, isTopAll, district, isProjectArea, isAllArea, isOutsideProvince, isIncludeAllArea) {
		var $this = $(this);
		this._province = $this.find(".zProvince");
		this._city = $this.find(".zCity");
		this._district = $this.find(".zDistrict");
		this._basePath = basePath;
		this._isTopAll = isTopAll;
		this._isAllArea = isAllArea;
		this._isProjectArea = isProjectArea;
		this._isOutsideProvince = isOutsideProvince;
		this._isIncludeAllArea = isIncludeAllArea;
		this._finalDistrict = district;
		this._districtInput = this.find("input[flag='district']")
		
		//绑定省市的下拉框
		_bindZoneSelect(this._province, this._city, this._district, this._basePath, this._isTopAll, this._districtInput);
		
		if(district && district !== "" && district != '000000'){
			//不为空则显示数据层级，根据返回的数据id判断显示层级
			this._reshowDistrict();
		}else{
			//为空则默认显示省的全部
			this._showProvince();
			if(!this._isTopAll){
				this._province.trigger("change");
				this._city.trigger("change");
			}
		}
	},
	/**
	 * 默认显示省份
	 */
	_showProvince : function(){
		var _this = this;
		//初始化的时候直接去获取省级的数据，然后把值用JSON返回来回写到页面上去
		$.ajax({
			type: "POST",
			url: this._basePath + "/sysArea/findArea.do",
			async: false,
			data: {"id":0, "isProjectArea":_this._isProjectArea},
			dataType: "json",
            beforeSend:function() {
            	this.url += "?vjfSessionId=" + $("#vjfSessionId").val()
            },
			success: function(result){
				var resLen = result.length;
				var defaultVals = "<option value=''>--</option>";
				var content = _this._isTopAll ? defaultVals : "";
				content += _this._isAllArea ? "<option value='000000'>全部</option>" : "";
				if(!_this._isAllArea || _this._isIncludeAllArea){
					content += _this._isOutsideProvince ? "<option value='000001'>省外</option>" : "";
					for(var i=1; i<resLen; i++){
						content += "<option value='"+result[i].areaCode+"'>"+result[i].areaName+"</option>";
					}
				}
				_this._province.html(content);
				_this._city.append(defaultVals);
				_this._district.append(defaultVals);
				
				//默认值为第一个(不一定是全部)
				_this._districtInput.val(_this._province.val());
			},
			error: function(data){
				alert("未找到对应的区域信息!");
			}
		});
	},
	
	_reshowDistrict : function(){
		var _this = this;
		$.ajax({
			type: "POST",
			url: this._basePath + "/sysArea/findAreaByCurId.do",
			async: false,
			data: {"id":_this._finalDistrict, "isProjectArea":_this._isProjectArea},
			dataType: "json",
            beforeSend:function() {
            	this.url += "?vjfSessionId=" + $("#vjfSessionId").val()
            },
			success: function(result){
				var state = result.state;
				if(state == "0"){
					var curprovince = result.curprovince;
					var curcity = result.curcity;
					var curdistrict = result.curdistrict;
					var province = result.province;
					var city = result.city;
					var district = result.district;
					var defaultVals = "<option value=''>--</option>";
					if(province){
						var content =  _this._isTopAll ? defaultVals : "";
						var content = _this._isTopAll ? defaultVals : "";
						content += _this._isAllArea ? "<option value='000000'>全部</option>" : "";
						if(!_this._isAllArea || _this._isIncludeAllArea){
							content += _this._isOutsideProvince ? "<option value='000001'>省外</option>" : "";
						}
						for(var i=1; i<province.length; i++){
							content += "<option value='"+province[i].areaCode+"'>"+province[i].areaName+"</option>";
						}
						_this._province.html(content);
						if (curprovince) {
							_this._province.val(curprovince.areaCode);
						}
					}
					if(city){
						var content = defaultVals;
						for(var i=1; i<city.length; i++){
							content += "<option value='"+city[i].areaCode+"'>"+city[i].areaName+"</option>";
						}
						_this._city.html(content);
						if(curcity) {
							_this._city.val(curcity.areaCode);
						}
					}else{
						_this._city.html(defaultVals);
					}

					if(district){
						var content =  defaultVals;
						for(var i=1; i<district.length; i++){
							content += "<option value='"+district[i].areaCode+"'>"+district[i].areaName+"</option>";
						}
						_this._district.html(content);
						if(curdistrict) {
							_this._district.val(curdistrict.areaCode);
						}
					}else{
						_this._district.html(defaultVals);
					}
					
					//回显值到隐藏域
					_this._districtInput.val(_this._finalDistrict);
				}else{
					alert("地区回显错误!");
				}
			},
			error: function(data){
				alert("地区回显错误!");
			}
		});
	}
});

function _bindZoneSelect($province, $city, $district, $basePath, $isTopAll, $districtInput){
	_bindDistrictSelect($province, $city , $basePath, $isTopAll);
	_bindDistrictSelect($city, $district, $basePath, $isTopAll);
	
	function _bindDistrictSelect($target, $sub, $basePath, $isTopAll){
		$target.change(function(){
			$sub.empty();
			var curId = $(this).val();
			$.ajax({
				type: "POST",
				url: $basePath + "/sysArea/findArea.do",
				async: false,
				data: {id:curId},
				dataType: "json",
                beforeSend:function() {
                	this.url += "?vjfSessionId=" + $("#vjfSessionId").val()
                },
	    		success: function(result){
	    			var deLen = result.length;
	    			var defaultVals = "<option value=''>--</option>";
	    			var details = "";
	    			if(deLen){
	    				details = defaultVals;
	    				for(var i=1; i<deLen; i++){
							details += "<option value='"+result[i].areaCode+"'>"+result[i].areaName+"</option>";
	        			}
	    			} else {
	    				details = defaultVals;
	    			}
	    			
	    			$sub.append(details);
	    			
	    			//如果sub后一个还是select，则把后面的select置为默认值(针对city下拉框)
	    			if(!$isTopAll){
	    				$sub.trigger("change");
	    			}else{
	    				if($sub.next().is("select")){
		    				$sub.next().html(defaultVals);
		    			}
	    			}
	    			
	    			//始终显示最后一级有数据的值
	    			$target.parent().find("select").each(function(){
	    				var __this = $(this);
	    				if(__this.val() && __this.val() != ""){
	    					$districtInput.val(__this.val());
	    				}
	    			});
	    			
	    			//如果sub值为空，则显示target的值
	    			/*if($sub.val() && $sub.val() != ""){
	    				alert(1);
	    				alert($sub.val());
	    				$districtInput.val($sub.val());
	    			} else {
	    				alert(2);
	    				$districtInput.val($target.val());
	    			}*/
	    			
	    		},
	    		error: function(data){
	    			alert("未找到对应的区域信息!");
	    		}
			});
		});
	}
	
	$district.change(function(){
		$districtInput.val($(this).val());
	});
	
}
