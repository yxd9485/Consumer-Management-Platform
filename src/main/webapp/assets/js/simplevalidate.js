function simplevalidate(control,tip){
	var value = control.val().trim();
	var $Msg = tip;
	
	$Msg.addClass("valid_fail_text");
	if(control.is(".required") && value == ""){
		//必填项
		var text = "请输入值";
		$Msg.html(text);
		$Msg.show();
		return false;
	} else if(control.is(".pass")&&!/^[a-z|A-Z]\w{5,15}$/.test(value)){
		//密码(6-16位以字母开头的字符)
        $Msg.html("密码为6到16位且字母开头");
        $Msg.show();
        return false;
	} else if(control.is(".passTwo")&&(this.value!=$("input[class*='pass']").val())){
		//密码双检
        $Msg.html("两次输入的密码不一致");
        $Msg.show();
        return false;
	} else if(control.is(".email") && !/.+@.+\.[a-zA-z]{2,4}$/.test(value)){
		//E-mail
        $Msg.html('请输入正确的E-Mail地址');
        $Msg.show();
        return false;
	} else if(control.is(".number") && !$.isNumeric(value)){
		//数字
		$Msg.html('请输入数字');
		$Msg.show();
		return false;
	} else if(control.is(".spchar") && !/^[-.@_a-zA-Z0-9\u4e00-\u9fa5]*$/.test(value)){
		//特殊字符判断
        $Msg.html('不能输入特殊字符');
        $Msg.show();
        return false;
	} else if(control.is(".tel") && !/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(value) ){
		//电话号码校验
        $Msg.html('请输入"区号-电话号码"格式的电话号码');
        $Msg.show();
        return false;
	} else if(control.is(".phone") && !isMobile(value) ){
		//手机号码校验
        $Msg.html('请输入11位手机号码');
        $Msg.show();
        return false;
	} else if(control.val() != "" && $.isNumeric(value) && control.is(".positive") && !/^[0-9]*[1-9][0-9]*$/.test(value)){
        $Msg.html("请输入正整数");
        $Msg.show();
        return false;
	} else if(control.val() != "" && $.isNumeric(value) && control.is(".minValue")){
		//最小数值
		var minVal = control.attr("minVal");
		if(control.val() - minVal < 0){
			$Msg.html("最小数值为"+minVal);
			$Msg.show();
			return false;
		} else {
			$Msg.html("");
			$Msg.removeClass("valid_fail_text");
			$Msg.hide();
			return true;
		}
		
	} else if(control.is(".constrant")){
		//唯一性校验
		var $hidden = control.next().next(".shidden");
		var $old = control.next().next().next();
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
        return true;
	}
	
}

function validateTip(msg){
    this.msg = msg;
}


function simplevalidate2(control,tip){
	var value = control.val().trim();
	
	if(control.is(".required") && value == ""){
		//必填项
		tip.msg = "请输入值";
		return false;
	} else if(control.is(".pass")&&!/^[a-z|A-Z]\w{5,15}$/.test(value)){
		//密码(6-16位以字母开头的字符)
        tip.msg="密码为6到16位且字母开头";
        return false;
	} else if(control.is(".passTwo")&&(this.value!=$("input[class*='pass']").val())){
		//密码双检
        tip.msg="两次输入的密码不一致";
        return false;
	} else if(control.is(".email") && !/.+@.+\.[a-zA-z]{2,4}$/.test(value)){
		//E-mail
        tip.msg='请输入正确的E-Mail地址';
        return false;
	} else if(control.is(".number") && !$.isNumeric(value)){
		//数字
		tip.msg='请输入数字';
		return false;
	} else if(control.is(".spchar") && !/^[-.@_a-zA-Z0-9\u4e00-\u9fa5]*$/.test(value)){
		//特殊字符判断
        tip.msg='不能输入特殊字符';
        return false;
	} else if(control.is(".tel") && !/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(value) ){
		//电话号码校验
        tip.msg='请输入"区号-电话号码"格式的电话号码';
        return false;
	} else if(control.is(".phone") && !isMobile(value) ){
		//手机号码校验
        tip.msg='请输入11位手机号码';
        return false;
	} else if(control.val() != "" && $.isNumeric(value) && control.is(".positive") && !/^[0-9]*[1-9][0-9]*$/.test(value)){
        tip.msg="请输入正整数";
        return false;
	} else if(control.val() != "" && $.isNumeric(value) && control.is(".minValue")){
		//最小数值
		var minVal = control.attr("minVal");
		if(control.val() - minVal < 0){
			tip.msg="最小数值为"+minVal;
			return false;
		} else {
			return true;
		}
		
	}else {
        return true;
	}
	
}

function transDate(timeStr){
	var date = new Date("2000-0-1");
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