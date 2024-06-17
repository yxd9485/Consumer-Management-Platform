
$(function(){
	
	$("form[id$='_focus'] :input[tag=validate]").focus(function(){
		var $parent = $(this).parents(".form-group");
		var $prev = $parent.find("label.control-label");
		var $Msg = $parent.find(".help-block");
		var value = $(this).val();
		$Msg.addClass("valid_fail_text");
		if(($(this).is(".required") && value == "") || (value != "" && $(this).is(".nospace") && hasSpace($(this)))){
			//必填项
			var text = "" + ($prev.text().replace("：","").replace(":","").replace("*","")) + "不能为空";
			$Msg.html(text);
			$Msg.show();
		} else if($(this).is(".pass")&&!/^[a-z|A-Z]\w{5,15}$/.test(value)){
			//密码(6-16位以字母开头的字符)
            $Msg.html("密码为6到16位且字母开头");
            $Msg.show();
		} else if($(this).is(".passTwo")&&(this.value!=$("input[class*='pass']").val())){
			//密码双检
            $Msg.html("两次输入的密码不一致");
            $Msg.show();
		} else if($(this).is(".email") && !/.+@.+\.[a-zA-z]{2,4}$/.test(value)){
			//E-mail
            $Msg.html('请输入正确的E-Mail地址');
            $Msg.show();
		} else if($(this).is(".number") && !$.isNumeric(value)){
			//数字
			$Msg.html('请输入数字');
			$Msg.show();
		} else if($(this).val() != "" && $.isNumeric(value) && $(this).is(".money") && !/^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/.test(value)) {
			$Msg.html("请输入正确的金额");
            $Msg.show();
		} else if($(this).is(".spchar") && !/^[-.@_a-zA-Z0-9\u4e00-\u9fa5]*$/.test(value)){
			//特殊字符判断
            $Msg.html('不能输入特殊字符');
            $Msg.show();
		} else if($(this).is(".tel") && !/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(value) ){
			//电话号码校验
            $Msg.html('请输入"区号-电话号码"格式的电话号码');
            $Msg.show();
		} else if($(this).is(".phone") && !isMobile(value) ){
			//手机号码校验
            $Msg.html('请输入11位手机号码');
            $Msg.show();
		} else if($(this).val() != "" && $.isNumeric(value) && $(this).is(".positive") && !/^[0-9]*[1-9][0-9]*$/.test(value)){
            $Msg.html("请输入正整数");
            $Msg.show();
		} else if($(this).val() != "" && $.isNumeric(value) && $(this).is(".minValue")){
			//最小数值
			var minVal = $(this).attr("minVal");
			if($(this).val() - minVal < 0){
				$Msg.html("当前可用的最小数值为"+minVal);
				$Msg.show();
			} else {
				$Msg.html("");
				$Msg.removeClass("valid_fail_text");
				$Msg.hide();
			}
		} else if($(this).val() != "" && $(this).is(".preTime")) {
			var $next = $parent.find("input.sufTime");
			if($next.val() != ""){
				if(transDate($(this).val()) > transDate($next.val())){
					$Msg.html("前一个时间不能大于后一个");
					$Msg.show();
				} else {
					$Msg.html("");
					$Msg.removeClass("valid_fail_text");
					$Msg.hide();
				}
			}
		} else if($(this).val() != "" && $(this).is(".sufTime")) {
			var $prev = $parent.find("input.preTime");
			if($prev.val() != ""){
				if(transDate($(this).val()) < transDate($prev.val())){
					$Msg.html("前一个时间不能大于后一个");
					$Msg.show();
				} else {
					$Msg.html("");
					$Msg.removeClass("valid_fail_text");
					$Msg.hide();
				}
			}
		} else if($(this).is(".constrant")){
			//唯一性校验
			var $hidden = $(this).next().next(".shidden");
			var $old = $(this).next().next().next();
			if(value.trim() != "" && value.trim() != $old.val()){
				$.ajax({
					url:$hidden.val(),
					type:"POST",
					data:{name:value},
					dataType:"json",
		            async:false,
					success:function(data){
						if(data == "Y") {
							$Msg.html("名称已存在");
							$Msg.show();
						} else {
			                $Msg.html("");
			                $Msg.removeClass("valid_fail_text");
			                $Msg.hide();
						}
					}
				});
			} else {
	            $Msg.html("");
	            $Msg.removeClass("valid_fail_text");
	            $Msg.hide();
			}
		} else {
            $Msg.html("");
            $Msg.removeClass("valid_fail_text");
            $Msg.hide();
		}
		
	}).blur(function(){
		$(this).triggerHandler("focus");
	}).keyup(function(){
		$(this).triggerHandler("focus");
	}).change(function(){
		$(this).triggerHandler("focus");
	});
	
	//进入编辑或者增加页面，默认焦点位置
	$("button[type=submit][id$='_focus']").focus();
	
	//提交表单验证
	$("form[id$='_focus']").submit(function(){
		var b = true;
		$("input[type!=hidden]",this).each(function(){
			if($(this).parents().is(".has-error")){
				$(this).focus();
				b = false;
				return false;
			}
		});
		if($("div",this).is(".has-error")){
			b = false;
		}
		return b;
	});
	
	//重置按钮页面回复原来效果
	$("button[type=reset][id$='_focus']").click(function(){
		$("div[class*='form-group']").removeClass("has-error").removeClass("has-success");
		$("div[class*='help-block']").html("");
		$("button[type=submit][id$='_focus']").focus();
	});
    
});

function hasSpace(obj){
	var flag = false;
	var value = obj.val();
	value = value.replace(/(^\s*)|(\s*$)/g, '');
	if(value == ""){
		flag = true;
	}
	obj.val(value);
	return flag;
}

function transDate(timeStr){
	var date = new Date();
	date.setFullYear(timeStr.substring(0,4));
	date.setMonth(timeStr.substring(5,7)-1);
	date.setDate(timeStr.substring(8,10));
	date.setHours(timeStr.substring(11,13));
	date.setMinutes(timeStr.substring(14,16));
	if(timeStr.length > 16){
		date.setSeconds(timeStr.substring(17,19));
	}
	return Date.parse(date) / 1000;
}

function overLength(obj){
	var length = obj.attr("lengthVal");
	var curLength = obj.val().length;
	if(curLength > length){
		return false;
	} else {
		return true;
	}
}

function isMobile(value){
    var flag = false;
    if(value.length == 11) {
        var reg = /^[1][358][0-9]\d{8}/;
        if(reg.test(value)) flag = true;
    }
    
    return flag;
}

jQuery.extend({
	blockValidate : function(blocks){
		var $block = blocks;
		$block.find(":input[tag=validate]").focus(function(){
			var $parent = $(this).parents(".form-group");
			var $prev = $parent.find("label.control-label");
			var $Msg = $parent.find(".help-block");
			var value = $(this).val();
			$Msg.addClass("valid_fail_text");
			if(($(this).is(".required") && value == "") || (value != "" && $(this).is(".nospace") && hasSpace($(this)))){
				//必填项
				var text = "请输入" + ($prev.text().replace("：","").replace(":","").replace("*",""));
				$Msg.html(text);
				$Msg.show();
			} else if($(this).is(".pass")&&!/^[a-z|A-Z]\w{5,15}$/.test(value)){
				//密码(6-16位以字母开头的字符)
	            $Msg.html("密码为6到16位且字母开头");
	            $Msg.show();
			} else if($(this).is(".passTwo")&&(this.value!=$("input[class*='pass']").val())){
				//密码双检
				$Msg.addClass("valid_fail_text");
	            $Msg.html("两次输入的密码不一致");
	            $Msg.show();
			} else if($(this).is(".email") && !/.+@.+\.[a-zA-z]{2,4}$/.test(value)){
				//E-mail
	            $Msg.html('请输入正确的E-Mail地址');
	            $Msg.show();
			} else if($(this).is(".number") && !$.isNumeric(value)){
				//数字
	            $Msg.html('请输入数字');
	            $Msg.show();
			} else if($(this).val() != "" && $.isNumeric(value) && $(this).is(".money") && /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/.test(value)) {
				$Msg.html("请输入正确的金额");
	            $Msg.show();
			} else if($(this).is(".spchar") && !/^[-.@_a-zA-Z0-9\u4e00-\u9fa5]*$/.test(value)){
				//特殊字符判断
	            $Msg.html('不能输入特殊字符');
	            $Msg.show();
			} else if($(this).is(".tel") && !/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(value) ){
				//电话号码校验
	            $Msg.html('请输入"区号-电话号码"格式的电话号码');
	            $Msg.show();
			} else if($(this).is(".phone") && !isMobile(value) ){
				//手机号码校验
	            $Msg.html('请输入11位手机号码');
	            $Msg.show();
			} else if($(this).val() != "" && $.isNumeric(value) && $(this).is(".maxValue")){
				//最大数值
				var maxVal = $(this).attr("maxVal");
				if($(this).val() > maxVal){
					$Msg.html("当前可用的最大数值为"+maxVal);
					$Msg.show();
				} else {
					$Msg.html("");
					$Msg.removeClass("valid_fail_text");
					$Msg.hide();
				}
			} else if($(this).val() != "" && $.isNumeric(value) && $(this).is(".minValue")){
				//最小数值
				var minVal = $(this).attr("minVal");
				if($(this).val() - minVal < 0){
					$Msg.html("当前可用的最小数值为"+minVal);
					$Msg.show();
				} else {
					$Msg.html("");
					$Msg.removeClass("valid_fail_text");
					$Msg.hide();
				}
			} else if($(this).val() != "" && $(this).is(".preTime")) {
				var $next = $parent.find("input.sufTime");
				if($next.val() != ""){
					if(transDate($(this).val()) > transDate($next.val())){
						$Msg.html("前一个时间不能大于后一个");
						$Msg.show();
					} else {
						$Msg.html("");
						$Msg.removeClass("valid_fail_text");
						$Msg.hide();
					}
				}
			} else if($(this).val() != "" && $(this).is(".sufTime")) {
				var $prev = $parent.find("input.preTime");
				if($prev.val() != ""){
					if(transDate($(this).val()) < transDate($prev.val())){
						$Msg.html("前一个时间不能大于后一个");
						$Msg.show();
					} else {
						$Msg.html("");
						$Msg.removeClass("valid_fail_text");
						$Msg.hide();
					}
				}
			} else if($(this).is(".constrant")){
				//唯一性校验
				var $hidden = $(this).next().next(".shidden");
				var $old = $(this).next().next().next();
				if(value != $old.val()){
					$.ajax({
						url:$hidden.val(),
						type:"POST",
						data:{name:value},
						dataType:"json",
			            async:false,
						success:function(data){
							if(data == "Y") {
								$Msg.html("名称已存在");
								$Msg.show();
							} else {
				                $Msg.html("");
				                $Msg.removeClass("valid_fail_text");
				                $Msg.hide();
							}
						}
					});
				} else {
		            $Msg.html("");
		            $Msg.removeClass("valid_fail_text");
		            $Msg.hide();
				}
			} else {
	            $Msg.html("");
	            $Msg.removeClass("valid_fail_text");
	            $Msg.hide();
			}
		}).blur(function(){
			$(this).triggerHandler("focus");
		}).keyup(function(){
			$(this).triggerHandler("focus");
		}).change(function(){
			$(this).triggerHandler("focus");
		});
	}
});
