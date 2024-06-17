$(function(){
	//企业名称事件
	$("#companyName").keydown(function(){
		//重置企业名称
		$("input[name='companyKey']").val("");
	}).blur(function(){
		var companyName = $(this).val();
		var companyKey = $("input[name='companyKey']");
		if(companyName != "" && companyKey.val() == ""){
			$.ajax({
				type: "POST",
				url: cpath + "/companyInform/findCompanyKeyByCompanyName.do",
				async: false,
				data: {
					companyName: encodeURI(companyName)
				},
				success: function(data) {
					if(data.length > 0){
						companyKey.val(data);
						getVcodeActivitys(data);
					}
				}
			});
		}
	}).autocomplete({
		minLength: 2,
		source: function( request, response ) {
			$.ajax({
				type: "POST",
				url: cpath + "/companyInform/autoCompleteCompanyInfo.do",
				dataType: "json",
				data: {
					companyName: encodeURI($("#companyName").val())
				},
				success: function(data) {
					if(data.length > 0){
						response( $.map( data, function( item ) {
							return {
								label: item.companyname,
								value: item.companyname,
								companykey: item.companykey
							};
						}));
					}
				}
			});
		},
		select: function( event, ui ) {
			//设置企业Key
			$(this).parent().find("input[name='companyKey']").val(ui.item.companykey);
			//调取该企业的返利活动 
			getVcodeActivitys(ui.item.companykey);
		},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		}
	});
	
	/* 给返利活动下拉框加change事件，用于自动带出开始、结束时间 */
	$("select[name='vcodeActivityKey']").change(function(){
		var option = $("select[name='vcodeActivityKey']").find("option:selected");
		var start = option.attr("startDate");
		var end   = option.attr("endDate");
		if(start && end && start != "" && end != ""){
			$("input[name='startTime']").val(start);
			$("input[name='endTime']").val(end);
		}
		//返利活动改变,则删除所有的奖品规则
		//$(".prize-edit").empty();
		//清空积分红包成本
		vjfCount();
	});
	
	
	/* 点击添加按钮 */
	$("a[flag='prizeAdd']").click(function(){
		var prizeHtml = '<tr>'
						+'<td style="width:4%;"><input autocomplete="off" class="rule-sel" name="check" type="checkbox" /><label class="rule-sel seq">1</label></td>'
						+'<td class="awards-name-td" style="width:12%;"><input autocomplete="off" name="ruleList[0].awardsName" class="awards-name rule-name form-control" type="text" maxlength="50" /></td>'
						+'<td class="awards-type-td" style="width:6%;">'+awardsTypeSelectHtml+'</td>'
						+'<td class="awards-cost-td" style="width:6%;"><input autocomplete="off" name="ruleList[0].awardsCost" class="awards-cost rule-cost form-control" type="text" maxlength="8" /></td>'
						+'<td class="awards-count-td" style="width:6%;"><input autocomplete="off" name="ruleList[0].awardsCount" class="awards-count rule-count form-control" type="text" maxlength="11" minvalue="0" /></td>'
						+'<td class="awards-percentage-td" style="width:6%;"><input autocomplete="off" name="ruleList[0].awardsPercentage" class="awards-percentage rule-percent form-control" type="text" value="100" maxlength="3" /></td>'
						+'<td class="self-priority-td" style="width:6%;"><input autocomplete="off" name="ruleList[0].selfPriority" class="self-priority rule-prority form-control" type="text" maxlength="2" /></td>'
						+'<td class="associated-percentage-td" style="width:6%;"></td>'
						+'<td class="if-associate-td" style="width:6%;"></td>'
						+'<td class="total-cost-td" style="width:6%;"></td>'
						+'<td class="valid-period-td" style="width:6%;"><input autocomplete="off" name="ruleList[0].validPeriod" class="valid-period rule-prority form-control" type="text" maxlength="3" /></td>'
						+'<td class="can-donate-td" style="width:6%;"><input autocomplete="off" name="ruleList[0].canDonate" value="1" class="can-donate rule-support" type="checkbox" /><label class="rule-support">支持</label></td>'
						+'<td class="awards-file-path-td" style="width:8%;"><input autocomplete="off" type="hidden" name="ruleList[0].awardsImagePath" class="awards-file-path rule-support" type="text" /><a class="btn btn-primary btn-xs btn-add-awardsfilepath"><i class="icon-plus"></i> 添加</a></td>'
						+'<td class="guiding-info-td" style="width:8%;"><input autocomplete="off" type="hidden" name="ruleList[0].guidingInfo" class="guiding-info rule-support" type="text" /><a class="btn btn-primary btn-xs btn-add-guiding-info"><i class="icon-plus"></i> 添加</a></td>'
						+'<td class="guide-name-td" style="width:8%;"><input autocomplete="off" type="hidden" name="ruleList[0].guideName" class="guide-name rule-support" type="text" /><a class="btn btn-primary btn-xs btn-add-guide-name"><i class="icon-plus"></i> 添加</a></td>'
						+'</tr>';
		$(".prize-edit").append(prizeHtml);
		//设置奖品数量
		var op = $("select[name='vcodeActivityKey']").find("option:selected");
		if(op){
			var prizeCount = $("select[name='vcodeActivityKey']").find("option:selected").attr("counts");
			$(".prize-edit tr:last").find("input.awards-count").val(prizeCount).keyup();
		}
		//重新把奖品序号排序
		reSeqPrize();
	});
	
	/* 点击删除按钮 */
	$("a[flag='prizeDel']").click(function(){
		var checked = $(".prize-edit").find("input[name='check']:checked");
		var delRuleKeys = "";
		if(checked.length > 0){
			var related = $("input.if-associate");
			if(related.length == 0){
				if(!window.confirm("确定要删除活动奖品吗？")){
					return false;
				}
			}else if(!window.confirm("该红包活动下的所有奖品关联会解除，是否确定要删除？")){
				return false;
			}
			checked.each(function(){
				var tr = $(this).parents("tr:first");
				if(tr.attr("rk") && tr.attr("rk") != ""){
					delRuleKeys += "," + tr.attr("rk");
				}
				tr.remove();
			});
			
			if(delRuleKeys.length > 0){
				delRuleKeys = delRuleKeys.substring(1);
			}
			
			var $delKeys = $("input[name='delRuleKeys']");
			if($delKeys.val()!=""){
				$delKeys.val($delKeys.val() + "," + delRuleKeys);
			}else{
				$delKeys.val(delRuleKeys);
			}
			//占比关联与关联占比全部删除
			$("td.associated-percentage-td, td.if-associate-td").empty();
			//放开奖品概率的readonly
			$(".awards-percentage").removeAttr("readonly");
			
			//重新把奖品序号排序
			reSeqPrize();
		}else{
			alert("请选择要删除的红包奖品");
		}
		
	});
	
	//奖品占比的options
	var assPercentageOptions = '<option value="10">10</option><option value="20">20</option><option value="30">30</option><option value="40">40</option><option value="50">50</option><option value="60">60</option><option value="70">70</option><option value="80">80</option><option value="90">90</option>';
	/* 点击关联占比按钮 */
	$("a[flag='prizeRelate']").click(function(){
		var flag = true;
		var group = "";
		$("input.rule-sel:checked").each(function(){
			var $tr = $(this).parents("tr");
			var awardsType = $tr.find("select.awards-type").val();
			if(awardsType != "3"){
				flag = false;
				return;
			} else {
				var col = $(this).next().text();
				group += "," + col;
			}
		});
		
		if(flag){
			group = $.trim(group) == "" ? "" : group.substr(1);
			var arr = group.split(",");
			if(arr.length > 1){
				$("input.rule-sel:checked").each(function(){
					var $tr = $(this).parents("tr");
					$tr.find(".awards-percentage").val("100").prop("readonly", true);
					$tr.find("td.if-associate-td").empty();
					var seq = $tr.find(".seq").text();
					$tr.find("td.if-associate-td").html('<input autocomplete="off" name="ruleList['+ (seq - 1) +'].ifAssociate" class="if-associate rule-cost form-control disabled" type="text" value="' + group + '" onfocus="$(this).blur();" />');
					$tr.find("td.associated-percentage-td").html('<select name="ruleList['+ (seq - 1) +'].associatedPercentage" class="associated-percentage rule-cost form-control" >' + assPercentageOptions + '</select>');
				});
				
				//处理占比关联
				dealAssociate(arr);
				
				alert("奖品序号" + group + "关联成功，请进行编辑");
			}else{
				alert("至少选择两个虚拟券才能关联哦");
			}
		} else {
			alert("只有虚拟券才能关联哦");
		}
		
		$("input.rule-sel:checked").prop("checked", false);
	});
	
	//奖品图片添加按钮
	$(".prize-edit").delegate("a.btn-add-awardsfilepath","click",function(){
		var $this = $(this);
		var key = $(this).parents("tr:first").find(".awards-file-path").attr("name");
		$("a[data-flag='rule-image-upload']").off();
		$("a[data-flag='rule-image-upload']").on("click", function (){
			openImageUploadify('logo', key);
		});
		
		$(".btn-pic-upload-sure").off();
		$(".btn-pic-upload-sure").on("click", function(){
			var filePath = $(".rule-file-path-input").val();
			if(filePath != ""){
				//回写图片路径
				$("input[name='"+key+"']").val(filePath);
				//清空图片路径
				$(".rule-file-path-input").val("");
				$(".rule-file-path-show-div").css("display", "none");
				//把添加按钮改为查看和修改按钮
				var td = $this.parent();
				$this.remove();
				td.append('<a class="btn btn-xs btn-view-awardsfilepath"><i class="icon-camera"></i> 查看</a>'
						 +'<a class="btn btn-info btn-xs btn-edit-awardsfilepath"><i class="icon-edit"></i> 修改</a>');
				alert("奖品图片添加成功");
			}
		});
		
		$("#picUploadModal").modal("show");
	});
	
	//红包引导语添加/修改
	$(".prize-edit").delegate(".btn-add-guiding-info, .btn-edit-guiding-info", "click", function(){
		var $btn = $(this);
		var $info = $btn.parents("tr:first").find(".guiding-info");
		$(".guiding-info-textarea").val($info.val());
		$(".btn-sure-guiding-info").off().on("click", function(){
			var text = $(".guiding-info-textarea").val();
			var $td = $btn.parent();
			if($.trim(text) == ""){
				//显示添加按钮
				$(this).attr("data-dismiss", "");
				alert("红包引导语不能为空");
			}else{
				//显示修改按钮
				$info.val(text);
				$(this).attr("data-dismiss", "modal");
				$btn.remove();
				$td.append('<a class="btn btn-info btn-xs btn-edit-guiding-info"><i class="icon-edit"></i> 修改</a>');
			}
			$(".guiding-info-textarea").val("");
		});
		$("#guidingInfoModal").modal("show");
	});
	
	//中奖名称添加/修改
	$(".prize-edit").delegate(".btn-add-guide-name, .btn-edit-guide-name", "click", function(){
		var $btn = $(this);
		var $info = $btn.parents("tr:first").find(".guide-name");
		$(".guide-name-textarea").val($info.val());
		$(".btn-sure-guide-name").off().on("click", function(){
			var text = $(".guide-name-textarea").val();
			var $td = $btn.parent();
			if($.trim(text) == ""){
				//显示添加按钮
				$(this).attr("data-dismiss", "");
				alert("中奖名称不能为空");
			}else{
				//显示修改按钮
				$info.val(text);
				$(this).attr("data-dismiss", "modal");
				$btn.remove();
				$td.append('<a class="btn btn-info btn-xs btn-edit-guide-name"><i class="icon-edit"></i> 修改</a>');
			}
			$(".guide-name-textarea").val("");
		});
		$("#guideNameModal").modal("show");
	});
	
	//奖品图片查看按钮
	$(".prize-edit").delegate(".btn-view-awardsfilepath", "click", function(){
		var filePath = $(this).parents("tr:first").find(".awards-file-path").val();
		if(filePath != ""){
			$(".show-awards-pic").attr("src", imageUrl + filePath);
			$("#showAwardsPicModal").modal("show");
			
			$(".btn-show-awards-pic-sure").off();
			$(".btn-show-awards-pic-sure").on("click", function(){
				$(".show-awards-pic").attr("src", "");
			});
		}
	});
	
	//奖品图片删除按钮
	$(".prize-edit").delegate(".btn-edit-awardsfilepath", "click", function(){
		var key = $(this).parents("tr:first").find(".awards-file-path").attr("name");
		$("a[data-flag='rule-image-upload']").off();
		$("a[data-flag='rule-image-upload']").on("click", function (){
			openImageUploadify('logo', key);
		});
		
		$(".btn-pic-upload-sure").off();
		$(".btn-pic-upload-sure").on("click", function(){
			var filePath = $(".rule-file-path-input").val();
			if(filePath != ""){
				//回写图片路径
				$("input[name='"+key+"']").val(filePath);
				//清空图片路径
				$(".rule-file-path-input").val("");
				$(".rule-file-path-show-div").css("display", "none");
				
				alert("奖品图片修改成功");
			}
		});
		
		$("#picUploadModal").modal("show");
	});
	
	
	/* 点击保存按钮 */
	$(".btn-save, .btn-publish").click(function(){
		var validateOK = true;
		//校验红包活动信息
		var validateInputs = $(".packs-table :input.validate");
		for(var i=0; i < validateInputs.length; i++){
			var input = $(validateInputs[i]);
			if($.trim(input.val()) == ""){
				validateOK = false;
				if(input.attr("name") == "companyKey"){
					alert(input.attr("tip") + "有误");
					input.parent().find("#companyName").focus();
				}else{
					alert(input.attr("tip") + "不能为空");
					input.focus();
				}
				break;
			}else if(input.attr("name") == "endTime"){
				var endTime = input.val();
				var startTime = $(validateInputs[i-1]).val();
				if(endTime < startTime){
					validateOK = false;
					alert("抢红包活动结束时间不能小于开始时间");
					break;
				}
			}else if(input.attr("name") == "linkExpireDay"){
				var intReg = /^[1-9]\d{0,2}$/;
				if(!intReg.test(input.val())){
					validateOK = false;
					alert("分享链接有效期输入错误，提示：请输入不为0的正整数");
					input.focus();
				}
			}
		}
		if(!validateOK){
			return false;
		}
		//校验奖品规则信息
		var $trs = $(".prize-edit >tr");
		if($trs.length == 0){
			alert("请至少添加一个红包奖品信息");
			return false;
		}
		for(var i=0; i < $trs.length; i++){
			var tr = $($trs[i]);
			
			var integerPattern = /^\d+$/;
			var floatPattern = /^\d+[.]*\d*$/;
			
			var awardsName = tr.find("input.awards-name");
			if($.trim(awardsName.val()) == ""){
				alert("奖品名称不能为空");
				awardsName.focus();
				return false;
			}else{
				//判断奖品名称是否有重复
				var flag = false;
				var val = awardsName.val();
				var curKey = tr.find(".rule-key");
				
				//当活动进行中则与数据库已存在数据做校验，否则只与页面做校验
				if(ifStart == "1"){
					for(var p in allRuleNameInDB){
						//如果此数据为已入库的数据，有awardsKey
						if(curKey.length > 0){
							//判断在allRuleNameInDB中，不同于当前key的名称；如果有相等则重复
							if(p != curKey.val() && allRuleNameInDB[p] == val){
								flag = true;
								break;
							};
						}else{
							//如果此数据为页面点击添加按钮生成的数据，无有awardsKey，则直接判断在allRuleNameInDB中是否有重复
							if(allRuleNameInDB[p] == val){
								flag = true;
								break;
							};
						};
					};
				}
				
				//与页面数据做校验
				var vArr = new Array();
				$(".awards-name").not(awardsName).each(function(){
					if($(this).val() != ""){
						vArr.push($(this).val());
					}
				});
				if($.inArray(val, vArr) != -1){
					flag = true;
				}
				
				if(flag){
					alert("奖品名称不能重复");
					awardsName.focus();
					return false;
				};
				
			}
			
			var awardsType = tr.find("select.awards-type");
			if($.trim(awardsType.val()) == ""){ alert("奖品类型不能为空"); awardsType.focus(); return false; }
			
			var awardsCost = tr.find("input.awards-cost");
			if(awardsCost.length>0 && $.trim(awardsCost.val()) == ""){
				alert("奖品成本不能为空");
				awardsCost.focus();
				return false;
			}else if(awardsCost.length>0 && $.trim(awardsCost.val()) != ""){
				var costArr = $.trim(awardsCost.val()).split(".");
				if(!floatPattern.test(awardsCost.val()) || costArr[0].length > 5 || (costArr.length==2 && costArr[1].length > 2)){
					alert("奖品成本输入错误，提示：整数最大5位、小数固定2位");
					awardsCost.focus();
					return false;
				}
			}
			
			var awardsCount = tr.find("input.awards-count");
			var awardsCatched = awardsCount.attr("minvalue");
			if($.trim(awardsCount.val()) == ""){
				alert("奖品数量不能为空");
				awardsCount.focus();
				return false;
			}else if(awardsCount.length > 0 && !integerPattern.test(unThousandth(awardsCount.val()))){
				alert("奖品数量输入错误");
				awardsCount.focus();
				return false;
			}
			
			if(awardsCatched != "" && integerPattern.test(unThousandth(awardsCount.val())) && parseInt(unThousandth(awardsCount.val())) < parseInt(awardsCatched)){
				alert("您输入的奖品数量要大于等于"+awardsCatched);
				awardsCount.focus();
				return false;
			}
			
			var awardsPercentage = tr.find("input.awards-percentage");
			if($.trim(awardsPercentage.val()) == ""){
				alert("奖品概率不能为空");
				awardsPercentage.focus();
				return false;
			}else if(awardsPercentage.length>0 && !integerPattern.test(awardsPercentage.val())){
				alert("奖品概率输入错误，提示：1-100之间的整数");
				awardsPercentage.focus();
				return false;
			}else if(awardsPercentage.val() > 100 || awardsPercentage.val() < 1){
				alert("奖品概率必须是1-100之间的整数");
				awardsPercentage.focus();
				return false;
			}
			
			var sp = tr.find("input.self-priority");
			if(sp && sp.val() != ""){
				var spVal = sp.val();
				if(spVal.length < 1 || spVal.length > 2 || spVal == 0 || !integerPattern.test(spVal)){
					alert("个人领奖优先级输入错误，提示：最多2位并且不为0的数字");
					sp.focus();
					return false;
				}
				
				var dup = false;
				$(".self-priority").not(sp).each(function(){
					if(spVal == $(this).val()){
						dup = true;
					}
				});
				var companyType = $("input.companyType").val();
				if(companyType != "1"){
					if(dup){
						alert("个人领奖优先级不能重复");
						sp.focus();
						return false;
					}
				};
			}
			
			var ap = tr.find("input.associated-percentage");
			if(ap.length>0 && $.trim(ap.val()) == ""){
				alert("奖品占比不能为空");
				ap.focus();
				return false;
			}else if(ap.length>0 && !floatPattern.test(ap.val())){
				alert("奖品占比输入错误");
				ap.focus();
				return false;
			}
			
			var totalCost = tr.find("input.total-cost");
			if(totalCost.length>0){
				var vals = unThousandth($.trim(totalCost.val())).split(".");
				if(vals[0].length > 14){
					alert("奖品总成本超过最大值，提示：整数最大14位。请修改【奖品成本】或【奖品数量】");
					awardsCost.focus();
					return false;
				};
			}
			
			var vp = tr.find("input.valid-period");
			if(vp.length>0 && $.trim(vp.val()) == ""){
				alert("奖品有效期不能为空"); vp.focus(); 
				return false; 
			}else if(vp.length>0 && !integerPattern.test(vp.val())){
				alert("奖品有效期输入错误，提示：1-3位不为0的数字"); 
				vp.focus();
				return false;
			}else if(vp.length>0 && (vp.val() < 1 || vp.val().length > 3) ){
				alert("奖品有效期为1-3位不为0的数字"); 
				vp.focus();
				return false;
			}

			var awardsFilePath = tr.find("input.awards-file-path");
			if(awardsFilePath.length > 0 && $.trim(awardsFilePath.val()) == ""){
				tr.find(".btn-add-awardsfilepath").removeClass("btn-primary").addClass("btn-danger");
				alert("奖品图片未上传");
				return false;
			}

			var gi = tr.find("input.guiding-info");
			if(gi && $.trim(gi.val()) == ""){
				tr.find(".btn-add-guiding-info").removeClass("btn-primary").addClass("btn-danger");
				alert("红包引导语不能为空");
				return false;
			}
			
			var gn = tr.find("input.guide-name");
			if(gn && $.trim(gn.val()) == ""){
				tr.find(".btn-add-guide-name").removeClass("btn-primary").addClass("btn-danger");
				alert("中奖名称不能为空");
				return false;
			}
		}
		
		//判断奖品占比是否为100
		var assArr = new Array();
		var associates = "";
		$(".if-associate").each(function(){
			var val = $(this).val();
			if(associates.indexOf(val) == -1){
				associates += val;
				assArr.push(val);
			};
		});
		if(assArr.length > 0){
			var flag = false;
			for(var i=0; i<assArr.length; i++){
				var ass = assArr[i];
				var total = 0;
				$("input[value='"+ass+"']").each(function(){
					total += parseInt($(this).parents("tr:first").find(".associated-percentage").val());
				});
				if(total != 100){
					alert("关联奖品" + ass + "的奖品占比之和必须是100%");
					flag = true;
					break;
				}
			};
			if(flag){
				return false;
			}
		};
		
		//提交表单
		$("input[name='activityStatus']").val($(this).attr("status"));
		$("#awards_form").submit();
	});
	
	//奖品类型change事件：1、实物与积分奖有成本，虚拟券无成本，控制成本输入框；
	//				2、实物奖有有效期，积分奖与券没有有效期，控制有效期输入框
	$(".prize-edit").delegate(".awards-type", "change", function(){
		var type = $(this).val();
		var tr = $(this).parents("tr:first");
		var seq = tr.find(".seq").text();
		if(type=="1"){
			var cost = tr.find(".awards-cost");
			if(cost.length == 0){
				tr.find(".awards-cost-td").html('<input autocomplete="off" name="ruleList['+ (seq - 1) +'].awardsCost" class="awards-cost rule-cost form-control" type="text" />');
				//处理占比关联
				var arr = tr.find(".seq").text().split(",");
				dealAssociate(arr);
				tr.find(".associated-percentage").remove();
			}
			
			tr.find(".valid-period-td").html('<input autocomplete="off" name="ruleList['+ (seq - 1) +'].validPeriod" maxlength="9" class="valid-period rule-prority form-control" type="text">');
			tr.find(".can-donate-td").html('<input autocomplete="off" name="ruleList['+ (seq - 1) +'].canDonate" value="1" class="can-donate rule-support" type="checkbox" /><label class="rule-support">支持</label>');
		}else if(type=="2"){
			var cost = tr.find(".awards-cost");
			if(cost.length == 0){
				tr.find(".awards-cost-td").html('<input autocomplete="off" name="ruleList['+ (seq - 1) +'].awardsCost" class="awards-cost rule-cost form-control" type="text" />');
				//处理占比关联
				var arr = tr.find(".seq").text().split(",");
				dealAssociate(arr);
				tr.find(".associated-percentage").remove();
			}
			tr.find(".valid-period-td").empty();
			tr.find(".can-donate-td").empty();
		}else if(type=="3"){
			//删除奖品成本输入框
			tr.find(".awards-cost").remove();
			//删除总成本的输入框
			tr.find(".total-cost-td").empty();
			//删除有效期的输入框
			tr.find(".valid-period-td").empty();
			//删除转送的选择框
			tr.find(".can-donate-td").empty();
		}
		
		//重新计算总积分红包成本
		vjfCount();
	});
	
	//输入奖品数量时自动加千分号
	$(".prize-edit").delegate(".awards-count", "keydown", function(event){
		// 0-9(小键盘:48-57; 大键盘:96-105; 删除8)
		if((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105) || event.keyCode == 8){
			return true;
		}else{
			return false;
		}
	}).delegate(".awards-count", "keyup", function(){
		var counts = $(this).val();
		$(this).val(thousandth(counts));
	});
	
	//计算总成本1：奖品数量失去焦点时
	$(".prize-edit").delegate(".awards-count", "blur", function(){
		var count = $(this).val();
		var tr = $(this).parents("tr:first");
		if(count != ""){
			var cost = tr.find(".awards-cost").val();
			if(cost && cost != ""){
				var tc = (parseFloat($.trim(cost)) * parseFloat(unThousandth($.trim(count)))).toFixed(2);
				var seq = tr.find(".seq").text();
				tr.find(".total-cost-td").html('<input autocomplete="off" name="ruleList['+ (seq - 1) +'].totalCostStr" class="total-cost rule-cost form-control disabled" type="text" value="' + thousandth(tc+"") + '" onfocus="$(this).blur();" />');
			}else{
				tr.find(".total-cost-td").empty();
			}
		}else{
			tr.find(".total-cost-td").empty();
		}
		//重新计算总积分红包成本
		vjfCount();
	});
	
	//计算总成本2：奖品成本失去焦点时(计算总积分红包成本)
	$(".prize-edit").delegate(".awards-cost", "blur", function(){
		var cost = $(this).val();
		var tr = $(this).parents("tr:first");
		if(cost != ""){
			var count = tr.find(".awards-count").val();
			if(count && count != ""){
				var tc = (parseFloat($.trim(cost)) * parseFloat(unThousandth($.trim(count)))).toFixed(2);
				var seq = tr.find(".seq").text();
				tr.find(".total-cost-td").html('<input autocomplete="off" name="ruleList['+ (seq - 1) +'].totalCostStr" class="total-cost rule-cost form-control disabled" type="text" value="' + thousandth(tc+"") + '" onfocus="$(this).blur();" />');
				//重新计算总积分红包成本
				vjfCount();
			}else{
				tr.find(".total-cost-td").empty();
			}
		}else{
			tr.find(".total-cost-td").empty();
		}
		//重新计算总积分红包成本
		vjfCount();
	});
	
	//把奖品成本与总成本换算为小数点后两位
	pointTwoAC_TC();
	
	//把奖品概率和奖品占比换算为不为0结尾的小数
	noPointZeroAWP_ASP();
	
	//把奖品总数、奖品数量加千分位
	thousandthAC_TC();
	
	//计算红包成本
	vjfCount();
	
});

/* 重新把奖品序号排序 */
function reSeqPrize(){
	var i = 0;
	$(".prize-edit .seq").each(function(){
		$(this).text(i+1);
		var tr = $(this).parents("tr:first");
		//奖品规则Key
		tr.find(".rule-key").attr("name", "ruleList["+i+"].ruleKey");
		//奖品名称
		tr.find(".awards-name").attr("name", "ruleList["+i+"].awardsName");
		//奖品类型
		tr.find(".awards-type").attr("name", "ruleList["+i+"].awardsType");
		//如果有奖品成本
		var ac = tr.find(".awards-cost");
		if(ac && ac.length > 0){tr.find(".awards-cost").attr("name", "ruleList["+i+"].awardsCost");}
		//奖品数量
		tr.find(".awards-count").attr("name", "ruleList["+i+"].awardsCountStr");
		//奖品概率
		tr.find(".awards-percentage").attr("name", "ruleList["+i+"].awardsPercentage");
		//个人领奖优先级
		tr.find(".self-priority").attr("name", "ruleList["+i+"].selfPriority");
		//如果有奖品占比，修改占比的name
		var ap = tr.find(".associated-percentage");
		if(ap && ap.length > 0){ ap.attr("name", "ruleList["+i+"].associatedPercentage"); }
		//如果有占比关联，修改name
		var ia = tr.find(".if-associate");
		if(ia && ia.length > 0){ ia.attr("name", "ruleList["+i+"].ifAssociate"); }
		//如果有总成本，修改name
		var tc = tr.find(".total-cost");
		if(tc && tc.length > 0){ tc.attr("name", "ruleList["+i+"].totalCostStr"); }
		//有效期
		var vp = tr.find(".valid-period");
		if(vp && vp.length > 0){tr.find(".valid-period").attr("name", "ruleList["+i+"].validPeriod");}
		//是否支持转送
		tr.find(".can-donate").attr("name", "ruleList["+i+"].canDonate");
		//图片
		tr.find(".awards-file-path").attr("name", "ruleList["+i+"].awardsImagePath");
		//引导语
		tr.find(".guiding-info").attr("name", "ruleList["+i+"].guidingInfo");
		//中奖名称
		tr.find(".guide-name").attr("name", "ruleList["+i+"].guideName");
		
		i++;
	});
}

/* 计算总积分红包成本 */
function vjfCount(){
	var vjf = 0;
	$(".awards-type").each(function(){
		var $this = $(this);
		if($this.val() == "2"){
			var vtr = $this.parents("tr:first");
			var vjfTc = vtr.find(".total-cost").val();
			if(vjfTc && vjfTc != ""){
				vjf += parseFloat(unThousandth(vjfTc));
			}
		}
	});
	$(".total-vjifen").text(vjf.toFixed(2) + "元(" + parseInt(Math.round(vjf*100)) + "积分)");
}

/* 处理占比关联，如果arr(当前选择的规则序列集合) */
function dealAssociate(arr){
	$("input.rule-sel:not(:checked)").each(function(){
		var $tr = $(this).parents("tr");
		var currentGroup = $tr.find(".if-associate").val();
		if(currentGroup && currentGroup != ""){
			var curArr = currentGroup.split(",");
			for(var i=0; i<curArr.length; i++){
				if($.inArray(curArr[i], arr) != -1){
					//删除占比关联
					$tr.find("td.if-associate-td").empty();
					//如果有奖品占比，则删除奖品占比输入框
					$tr.find(".associated-percentage").remove();
					//奖品概率可修改
					$tr.find(".awards-percentage").removeAttr("readonly");
				}
			}
		}
	});
}

//把奖品成本与总成本换算为小数点后两位
function pointTwoAC_TC(){
	$(".awards-cost,.total-cost").each(function(){
		var $this = $(this);
		var val = parseFloat(unThousandth($this.val())).toFixed(2);
		$this.val(val);
	});
}

//把奖品概率和奖品占比换算为不为0结尾的小数
function noPointZeroAWP_ASP(){
	$(".awards-percentage,.associated-percentage").each(function(){
		var $this = $(this);
		var $val = $this.val();
		if($val.length > 1){
			var endsWith = $val.substring($val.length-2);
			if(endsWith == ".0"){
				$this.val($val.substring(0, $val.length-2));
			}
		}
	});
}

//把奖品数量、奖品数量加千分位
function thousandthAC_TC(){
	$(".awards-count,.total-cost").each(function(){
		var $this = $(this);
		var val = thousandth($this.val());
		$this.val(val);
	});
}

/* 获取企业的返利活动，并显示 */
function getVcodeActivitys(companyKey){
	//调取该企业的返利活动 
	$.ajax({
		type: "GET",
		url: cpath + '/vcodeActivity/loadVcodeActivity.do',
		data: "companyKey="+companyKey,
		dataType: "json",
		async: false,
		success: function(data){
			var options = "";
			if(data && data.length > 0){
				for(var i=0; i < data.length; i++){
					options += "<option value='" + data[i].vcodeActivityKey + "' startdate='" + data[i].startDate + "' enddate='" + data[i].endDate + "' counts='" + data[i].vcodeAmounts + "'>" + data[i].vcodeActivityName + "</option>";
				}
			}
			if(options != ""){
				$("select[name='vcodeActivityKey']").html(options);
			}else{
				options += "<option value=''>--</option>";
				$("select[name='vcodeActivityKey']").html(options);
			}
			$("select[name='vcodeActivityKey']").change();
		}
	});
}
