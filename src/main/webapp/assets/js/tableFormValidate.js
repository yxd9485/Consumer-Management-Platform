/**
 * use for table class validate
 * create by RoyFu
 * 2014-03-13
 */
var tipsText = "";
jQuery.extend({
	
	validationContent : function (input, content, label, tips){
		var required = $.validRequired(input, label);
		var orginalPwd = $.validOrgPwd(input);
		var password = $.validPassword(input);
		var confirmPwd = $.validConfirmPassword(input);
		var number = $.validNumber(input);
		var cardNumber = $.validCardNum(input);
		var bankNumber = $.validBankNum(input);
		var positive = $.validPositive(input);
		var thousand = $.validThousand(input);
		var integer = $.validInteger(input);
		var preValue = $.validPreValue(input, content);
		var sufValue = $.validSufValue(input, content);
		var maxValue = $.validMaxValue(input);
		var minValue = $.validMinValue(input);
		var gtValue = $.validGtValue(input);
		var ltValue = $.validLtValue(input);
		var allRest = $.validAllWithRest(input, label);
		var restAll = $.validRestWithoutAll(input, label);
		var money = $.validMoney(input);
		var email = $.validEmail(input);
		var linkurl = $.validLink(input);
		var spchar = $.validSpecialChar(input);
		var phone = $.validPhone(input);
		var mobile = $.validMobile(input);
		var preTime = $.validPreTime(input, content);
		var sufTime = $.validSufTime(input, content);
		var pwdconstrant = $.pwdvalidConstrant(input, content);
		var constrant = $.validConstrant(input, content);
		var seqConstrant=$.seqvalidConstrant(input, content);
		var code = $.validCode(input);
		var illegalChar = $.validIllegalChar(input);
		var pageCompare = $.validPageConstrant(input);
		var fixation = $.validFixationLength(input);
		var varLength = $.validVarLength(input);
		var fixLength = $.validFixLength(input);
		var version = $.validVersion(input);
		
		// 验证控件的ID
		var validId = "";
		if (input.is("[id]")) {
			validId = input.attr("id")
		} else if (input.is("[name]")) {
			validId = input.attr("name");
		}

		if(required && number && orginalPwd && password && confirmPwd && pwdconstrant && version && cardNumber && bankNumber
				&& positive && integer && preValue && sufValue && maxValue && minValue && gtValue && ltValue
				&& allRest && restAll && thousand && money && email &&linkurl&& spchar && constrant && code && illegalChar
				&& phone && mobile && preTime && sufTime && seqConstrant && pageCompare && fixation && varLength && fixLength) {
			if (tips) {
				tips.removeClass("valid_fail_text");
				tips.html("");
				tips.hide();
				
				// 同组中还有未校验通过的
				if (tips.data("invalid")) {
					tips.data("invalid", tips.data("invalid").replace(validId + ",", ""));
					if (tips.data("invalid")) {
						validId = tips.data("invalid").split(",")[0];
						if (validId.indexOf('rangeVpoints') < 0) $(":input[name='" + validId +"'],#" + validId).trigger("focus");
					}
				}
			}
		} else {

			// 仅需要还原值的校验
			if (input.is("[data-oldval]")) {
				input.val(input.data("oldval"));
				input.trigger("input");
			} else {
				if (tips) {
					tips.addClass("valid_fail_text");
					tips.html(tipsText);
					tips.show();
					tipsText = "";
					
					// 验证未通过
					if (validId) {
						if (!tips.data("invalid")) {
							tips.data("invalid", validId + ",");
						} else if (tips.data("invalid").indexOf(validId) == -1){
							tips.data("invalid", tips.data("invalid") + validId + ",");
						}
					}
				}
			}
			
		}
	},
	
	/**
	 * div实时校验
	 * @param form 需要校验的区域 
	 */
	runtimeDivValidate : function(form){
		var $form = form;
		$form.find(":input[tag=validate]").focus(function(){
			var $input = $(this);
			var $content = $(this).parents(".form-group");
			var $label = $content.find("label.control-label");
			var $tips = $content.find(".help-block");
			
			$.validationContent($input, $content, $label, $tips);
		}).blur(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).keyup(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	},
	/**
	 * App-DIV实时校验
	 * @param form 需要校验的区域 
	 */
	runtimeSquDivseqValidate : function(form){
		var $form = form;
		$form.find(":input[tag=validate]").focus(function(){
			var $input = $(this);
			var $content = $(this).parents(".ab_main");
			var $label = $content.find("td.ab_left");
			var $tips = $content.find(".validate_tips");
			
			$.validationContent($input, $content, $label, $tips);
		}).blur(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).keyup(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	},
	runtimeDivisionValidate : function(form){
		var $form = form;
		$form.find(":input[tag=validate]").focus(function(){
			var $input = $(this);
			var $content = $(this).parents(".conv-form");
			var $label = $content.find("label.control-label");
			var $tips = $content.find(".div_control_validate_tips");
			
			$.validationContent($input, $content, $label, $tips);
		}).blur(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).keyup(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	},
	
	/**
	 * div实时校验
	 * @aram block 
	 */
	blockValidate :function(block){
		var $block = block;
		$block.find(":input[tag=validate]").focus(function(){
			var $input = $(this);
			var $content = $input.parents(".form-group");
			var $label = $content.find("label.control-label");
			var $tips = $content.find(".help-block");
			
			$.validationContent($input, $content, $label, $tips);
		}).blur(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).keyup(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	},
	
	dialogValidate : function(block){
		var $block = block;
		$block.find(":input[tag=validate]").focus(function(){
			var $input = $(this);
			var $content = $input.parents("div.content");
			var $label = $content.parent().prev().find(".mod-name");
			var $tips = $content.find(".validate-tags");
			
			$.validationContent($input, $content, $label, $tips);
		}).blur(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).keyup(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	},
	
	/**
	 * 实时校验
	 * @param form 需要校验的区域 
	 */
	runtimeValidate : function(form){
		var $form = form;
		$form.find(":input[tag=validate]").focus(function(){
			var $input = $(this);
			var $content = $input.closest("div.content");
			var $label = $content.closest("td").prev().find(".title");
			var $tips = $content.find(".validate_tips");
			
			$.validationContent($input, $content, $label, $tips);
		}).blur(function(){
			if ($(this).is(".money") && $(this).val()) {
				if ($.isNumeric($(this).val())) {
					$(this).val(Number($(this).val()).toFixed(2));
				} else {
					$(this).val("");
				}
			}
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
			if ($(this).is("[data-oldval]")) {
				$(this).data("oldval", $(this).val());
			}
		}).keyup(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	},
	
	/**
	 * 实时校验
	 * @param block 需要校验的区域 
	 */
	loadedValidate : function(block){
		var $block = block;
		$block.find(":input[tag=validate]").focus(function(){
			var $input = $(this);
			var $content = $(this).parents("div.p_content");
			var $label = $content.parent().prev().find(".p_title");
			var $tips = $content.find(".validate_tips");
			
			$.validationContent($input, $content, $label, $tips);
		}).blur(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).keyup(function(){
			if(!$(this).is(".Wdate")){
				$(this).triggerHandler("focus");
			}
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	},
	
	/**
	 * 提交时校验整个form
	 * @param form
	 */
	submitValidate : function(form){
		var $form = form;
		var flag = true;
		//默认返回第一个进行校验不通过的地方
		$form.find(":input:enabled[type!=hidden][tag=validate]").each(function(){
			var $this = $(this);
			if($this.is(".required") && $this.val().trim() == ""){
				if($this.is(".Wdate")){
					var $input = $(this);
					var $content = $input.parents("div.content");
					var $label = $content.parents("td").prev().find(".title");
					var $tips = $content.find(".validate_tips");
					$.validationContent($input, $content, $label, $tips);
					
				} else {
					$this.focus();
				}
				flag = false;
				return flag;
			}
		});
		
		if(!flag) flag = true;
		
		$form.find(".valid_fail_text").each(function(){
			$(this).prev(":input[tag=validate]").focus();
			flag = false;
			return flag;
		});
		
		return flag;
	},
	
	/**
	 * 必填项校验
	 * @param object 对应的input对象 
	 * @param textLabel 相关名称
	 */
	validRequired : function(object, textLabel){
		var flag = true;
		var value = object.val().trim();
		if( (object.is(".required") && value == "") || (value != "" && object.is(".nospace") && $.hasSpace(object)) ){
			flag = false;
			if(tipsText == "") tipsText = textLabel.text().replace("：","").replace(":","").replace("*","") + "不能为空";
		}
		return flag;
	},
	
	/**
	 * 是否包括空格
	 */
	hasSpace : function(object){
		var flag = false;
		var value = object.val();
		value = value.replace(/(^\s*)|(\s*$)/g, '');
		if(value == ""){
			flag = true;
		}
		object.val(value);
		return flag;
	},
	
	/**
	 * 数字校验
	 * @param object 对应的input对象
	 */
	validNumber : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".number") && !$.isNumeric(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入数字";
		}
		return flag;
	},
	
	/**
	 * 身份证校验
	 * @param object 对应的input对象
	 */
	validCardNum : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".cardnum") && !/[0-9]{17}[X0-9]$/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入18位身份证号";
		}
		return flag;
	},
	
	/**
	 * 银行卡校验
	 * @param object 对应的input对象
	 */
	validBankNum : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".banknum") && !/[0-9]{16,20}$/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入16或19位银行卡号";
		}
		return flag;
	},
	
	/**
	 * 正整数校验，不能为0
	 * @param object 对应的input对象
	 */
	validPositive : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".positive") && $.isNumeric(value) && !/^[1-9][0-9]*$/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入正整数";
		}
		return flag;
	},
	
	
	/**
	 * 正整数千分符校验，可为0
	 * @param object 对应的input对象
	 */
	validThousand : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".thousand")  && !/^[0-9,]*$/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入正整数";
		}
		return flag;
	},
	
	/**
	 * 正整数校验，可以为0
	 * @param object 对应的input对象
	 */
	validInteger : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".integer") && $.isNumeric(value) && (/^[+-]?[1-9]?[0-9]*\.[0-9]*$/.test(value) || /^(-|0)[0-9]+$/.test(value))){
			flag = false;
			if(tipsText == "") tipsText = "请输入正整数";
		}
		return flag;
	},
	
	/*
	 * 数值比较校验
	 */
	validPreValue : function(object, parent){
		var flag = true;
		var $next = parent.find("input.sufValue");
		var value = object.val();
		if(object.is(".preValue") && value != "" && $next.val() != "" && (Number(value) > Number($next.val()))){
			flag = false;
			if(tipsText == "") tipsText = "前一个数值不能大于后一个";
		}
		return flag;
	},
	
	/*
	 * 数值比较校验
	 */
	validSufValue : function(object, parent){
		var flag = true;
		var $prev = parent.find("input.preValue");
		var value = object.val();
		if(object.is(".sufValue") && value != "" && $prev.val() != "" && (Number(value) < Number($prev.val()))){
			flag = false;
			if(tipsText == "") tipsText = "前一个数值不能大于后一个";
		}
		return flag;
	},
	
	/**
	 * 最大数值校验
	 * @param object 对应的input对象
	 */
	validMaxValue : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".maxValue") && $.isNumeric(value) ){
			var maxVal = object.attr("maxVal");
			if(value - maxVal > 0){
				flag = false;
				if(tipsText == "") tipsText = "当前可用的最大数值为" + maxVal;
			}
		}
		return flag;
	},
	
	/**
	 * 最小数值校验
	 * @param object 对应的input对象
	 */
	validMinValue : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".minValue") && $.isNumeric(value) ){
			var minVal = object.attr("minVal");
			if(value - minVal < 0){
				flag = false;
				if(tipsText == "") tipsText = "当前可用的最小数值为" + minVal;
			}
		}
		return flag;
	},
	
	/**
	 * 大于等于校验
	 * @param object 对应的input对象
	 */
	validGtValue : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".gtValue") && $.isNumeric(value) ){
			var gtVal = object.attr("gtVal");
			if(value - gtVal >= 0){
				flag = false;
				if(tipsText == "") tipsText = "当前数值不能大于或等于" + gtVal;
			}
		}
		return flag;
	},
	
	/**
	 * 小于等于校验
	 * @param object 对应的input对象
	 */
	validLtValue : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".ltValue") && $.isNumeric(value) ){
			var ltVal = object.attr("ltVal");
			if(value - ltVal <= 0){
				flag = false;
				if(tipsText == "") tipsText = "当前数值不能小于或等于" + ltVal;
			}
		}
		return flag;
	},
	
	/**
	 * 数值比较校验A>B
	 * @param object 对应的input对象
	 */
	validAllWithRest : function(object, label){
		var flag = true;
		var value = object.val();
		var ref = object.attr("ref");
		var comparable = $("input.restAll[name="+ref+"]");
		var cVal = comparable.val();
		var labels = comparable.parents("td").prev().find("label");
		if( value != "" && cVal != "" && object.is(".allRest") && $.isNumeric(value) && $.isNumeric(cVal) ) {
			if(value - cVal < 0){
				flag = false;
				var textLabel = label.text().replace("：","").replace(":","").replace("*","");
				var comLabel = labels.text().replace("：","").replace(":","").replace("*","");
				if(tipsText == "") tipsText = textLabel + "不能小于" + comLabel;
			}
		}
		return flag;	
	},
	
	/**
	 * 数值比较校验A<B
	 * @param object 对应的input对象
	 */
	validRestWithoutAll : function(object, label){
		var flag = true;
		var value = object.val();
		var ref = object.attr("ref");
		var comparable = $("input.allRest[name="+ref+"]");
		var cVal = comparable.val();
		var labels = comparable.parents("td").prev().find("label");
		if( value != "" && cVal != "" && object.is(".restAll") && $.isNumeric(value) && $.isNumeric(cVal) ) {
			if(cVal - value < 0){
				flag = false;
				var textLabel = label.text().replace("：","").replace(":","").replace("*","");
				var comLabel = labels.text().replace("：","").replace(":","").replace("*","");
				if(tipsText == "") tipsText = comLabel + "不能小于" + textLabel;
			}
		}
		return flag;
	},
	
	/**
	 * 金额校验
	 * @param object 对应的input对象
	 */
	validMoney : function(object){
		var flag = true;
		var value = object.val();
		
		if( value != "" && object.is(".money")
				&& (!$.isNumeric(value) || (!object.is("[data-oldval]") && !/^(-?([1-9]\d{0,8})|0)(\.\d{2})$/.test(value)) 
										|| !/^(-?([1-9]\d{0,8})|0)(\.\d*)?$/.test(value))){
			flag = false;
			if(tipsText == "") tipsText = "请输入正确的金额#.##";
		}
		return flag;
	},
	
	/**
	 * 校验原密码
	 * @param object 对应的input对象
	 */
	validOrgPwd : function(object) {
		var flag = true;
		var value = object.val();
		var path = object.data("path");
		
		if(object.is(".originalPwd") && value != ""){
			$.ajax({
				url: path,
				type: "POST",
				data: {name:value},
				dataType: "json",
	            async: false,
				success: function(data){
					
					if(data == "N") {
						flag = false;
						if(tipsText == "") tipsText = "原密码输入错误";
					}
					return flag;
				}
			});
		}
		return flag;
	},
	
	/**
	 * 密码校验
	 * @param object 对应的input对象
	 */
	validPassword : function(object){
		var flag = true;
		var value = object.val();
		if(value != "" && object.is(".pass")){
			if(value.match(/([A-Z])+/) && value.match(/([a-z])+/) && value.match(/([0-9])+/) && value.match(/^[a-zA-Z0-9]{6,16}$/)){
				
			} else {
				flag = false;
				if(tipsText == "") tipsText = "密码为6到16位且必须同时包含大小写字母和数字";
			}
		}
		
		/*if( value != "" && object.is(".pass") && !/^[a-zA-Z0-9]{6,16}$/.test(value) && !value.match(/([A-Z])+/) ){
			flag = false;
			if(tipsText == "") tipsText = "密码为6到16位且需要包含大小写字母和数字";
		}*/
		return flag;
	},
	
	/**
	 * 两次输入的密码校验
	 * @param object 对应的input对象
	 */
	validConfirmPassword : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".passTwo") && value != $("input[class*='pass']").val() ){
			flag = false;
			if(tipsText == "") tipsText = "两次输入的密码不一致";
		}
		return flag;
	},
	
	/**
	 * E-Mail校验
	 * @param object 对应的input对象
	 */
	validEmail : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".email") && !/.+@.+\.[a-zA-z]{2,4}$/.test(value) ){//原来的
			flag = false;
			if(tipsText == "") tipsText = "请输入正确的E-Mail地址";
		}
		return flag;
	},
	
	/**
	 * 链接校验
	 * @param object 对应的input对象
	 */
	validLink: function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".linkurl") && !/^((https?|ftp|news):\/\/)?([a-z]([a-z0-9\-]*[\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\/[a-z0-9_\-\.~]+)*(\/([a-z0-9_\-\.]*)(\?[a-z0-9+_\-\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$/.test(value) ){//原来的
			flag = false;
			if(tipsText == "") tipsText = "请输入正确的链接地址";
		}
		return flag;
	},
	
	/**
	 * 特殊字符校验
	 * @param object 对应的input对象
	 */
	validSpecialChar : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".spchar") && !/^[-.@_a-zA-Z0-9\u4e00-\u9fa5]*$/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "不能输入特殊字符";
		}
		return flag;
	},
	
	/**
	 * 电话号码校验
	 * @param object 对应的input对象
	 */
	validPhone : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".tel") && !/^(([048]\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入 '区号-电话号码' 格式的电话号码";
		}
		return flag;
	},
	
	/**
	 * 手机号码校验
	 * @param object 对应的input对象
	 */
	validMobile : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".phone") && !$.isMobile(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入11位手机号码";
		}
		return flag;
	},
	
	
	/**
	 * 是否手机号码
	 */
	isMobile : function(value){
		var flag = false;
	    if(value.length == 11) {
	        var reg = /^[1][35789][0-9]\d{8}/;
	        if(reg.test(value)) flag = true;
	    }
	    return flag;
	},
	
	/**
	 * 时间比较校验
	 * @param object 对应的input对象
	 * @param parent 
	 */
	validPreTime : function(object, parent){
		var flag = true;
		var $next = parent.find("input.sufTime");
		var value = object.val();
		if(object.is(".preTime") && value != "" && $next.val() != "" && ($.transDate(value) > $.transDate($next.val()))){
			flag = false;
			if(tipsText == "") tipsText = "前一个时间不能大于后一个";
		}
		return flag;
	},
	
	/**
	 * 时间比较校验
	 * @param object 对应的input对象
	 * @param parent 
	 */
	validSufTime : function(object, parent){
		var flag = true;
		var $prev = parent.find("input.preTime");
		var value = object.val();
		if(object.is(".sufTime") && value != "" && $prev.val() != "" && ($.transDate(value) < $.transDate($prev.val()))){
			flag = false;
			if(tipsText == "") tipsText = "前一个时间不能大于后一个";
		}
		return flag;
	},
	
	/**
	 * 时间转换
	 */
	transDate : function(timeStr){
		var date = new Date("2000-0-1");
		if(timeStr.split("-").length == 1){
			timeStr = "2000-01-01 " + timeStr;
		}
		date.setFullYear(timeStr.substring(0,4));
		date.setMonth(timeStr.substring(5,7)-1);
		date.setDate(timeStr.substring(8,10));
		if(timeStr.length > 10){
			date.setHours(timeStr.substring(11,13));
			date.setMinutes(timeStr.substring(14,16));
			if(timeStr.length > 16){
				date.setSeconds(timeStr.substring(17,19));
			}
		}
		return Date.parse(date) / 1000;
	},
	
	/**
	 * 唯一性校验
	 * @param object 对应的input对象
	 * @param parent
	 */
	validConstrant : function(object, parent){
		var flag = true;
		var value = object.val();
		var $hidden = parent.find(".hidden_url");
		var $old = parent.find(".old_value");
		var info = object.attr("info");
		if(object.is(".constrant")){
			if(value != $old.val()){
				$.ajax({
					url:$hidden.val(),
					type:"POST",
					data:{name:value},
					dataType:"json",
		            async:false,
					success:function(data){
						if(data == "Y") {
							flag = false;
							if(tipsText == "") tipsText = "名称已存在";
							if(info) tipsText = info;
						}
						return flag;
					}
				});
			}
		}
		
		return flag;
	},
	/**
	 * 顺序号唯一校验
	 * @param object 对应的input对象
	 * @param parent
	 */
	seqvalidConstrant : function(object, parent){
		var flag = true;
		var value = object.val();
		var $hidden = parent.find(".hidden_url");
		var $old = parent.find(".old_value");
		if(object.is(".seqconstrant")){
			if(value != $old.val()){
				$.ajax({
					url:$hidden.val(),
					type:"POST",
					data:{name:value},
					dataType:"json",
		            async:false,
					success:function(data){
						if(data == "Y") {
							flag = false;
							if(tipsText == "") tipsText = "顺序号不能重复";
						}
						return flag;
					}
				});
			}
		}
		
		return flag;
	},
	
	/**
	 * 密码唯一校验
	 * @param object 对应的input对象
	 * @param parent
	 */
	pwdvalidConstrant : function(object, parent){
		var flag = true;
		var value = object.val();
		var $hidden = parent.find(".hidden_url");
		var $old = parent.find(".old_value");
		if(object.is(".pwdconstrant")){
			if(value != $old.val()){
				$.ajax({
					url:$hidden.val(),
					type:"POST",
					data:{name:value},
					dataType:"json",
		            async:false,
					success:function(data){
						
						if(data == "N") {
							flag = false;
							if(tipsText == "") tipsText = "原密码有误";
						}else{
							flag = true;
							if(tipsText == "") tipsText = "原密码输入正确";
						}
						return flag;
					}
				});
			}
		}
		
		return flag;
	},
	
	/**
	 * 编码校验
	 * @param object 对应的input对象
	 */
	validCode : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".code") && !/^[A-Z0-9]+$/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "请输入数字";
		}
		return flag;
	},
	
	/**
	 * 特殊字符校验
	 * @param object 对应的input对象
	 */
	validIllegalChar : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".illchar") && /[\':：;；*?？~`!@#$%^&+={}\<\>,，。\.]/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "不能输入特殊字符";
		}
		return flag;
	},
	
	/**
	 * 页面校验唯一性(同类匹配)
	 * @param object 对应的input对象
	 */
	validPageConstrant : function(object){
		var flag = true;
		var value = object.val();
		var index = object.attr("col");
		if( value != "" && object.is(".pageCompare")){
			$("input.pageCompare[col!="+index+"]").each(function(){
				if($(this).val() == value){
					flag = false;
					if(tipsText == "") tipsText = "名称已存在";
					return flag;
				}
			});
		}
		return flag;
	},
	
	/**
	 * 固定长度校验
	 * @param object 对应的input对象
	 */
	validFixationLength : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".fixation") && value.length < 13 ){
			flag = false;
			if(tipsText == "") tipsText = "长度为13位";
		}
		return flag;
	},
	
	/**
	 * 可变长度校验
	 * @param object 对应的input对象
	 */
	validVarLength : function(object){
		var flag = true;
		var value = object.val();
		var length = object.data("length");
		if( value != "" && object.is(".varlength") && value.length > length ){
			flag = false;
			if(tipsText == "") tipsText = "当前可输入的长度不能大于"+length+"位";
		}
		return flag;
	},
	
	/**
	 * 可变长度校验
	 * @param object 对应的input对象
	 */
	validFixLength : function(object){
		var flag = true;
		var value = object.val();
		var length = object.data("fixlength");
		if( value != "" && object.is(".fixlength") && value.length < parseInt(length) ){
			flag = false;
			if(tipsText == "") tipsText = "当前可输入的长度不能小于"+length+"位";
		}
		return flag;
	},
	
	/**
	 * 版本校验
	 * @param object 对应的input对象
	 */
	validVersion : function(object){
		var flag = true;
		var value = object.val();
		if( value != "" && object.is(".version") && !/^[1-9]\.\d\.\d/.test(value) ){
			flag = false;
			if(tipsText == "") tipsText = "版本号格式不正确，请按提示中的正确格式'1.0.0'进行填写";
		}
		return flag;
	}
});