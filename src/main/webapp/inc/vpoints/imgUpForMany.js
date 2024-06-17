var imgArr = [];
var numUp;
var delParent;
var imageServerUrl = "https://img.vjifen.com/images/vma/";
var imgSize=[];
$(function(){
	var checkStatus;
	var defaults = {
		fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
		fileSize         : 1024 * 1024 * 1                  // 上传文件的大小 1M
	};
	/*	if (customerDefaults != null) {
            defaults = customerDefaults;
        }*/
	/*点击图片的文本框*/
	$(".file").change(changeDo);
	function changeDo(){
		var imgSection = $(this).closest(".img-section");
		var filevalue = imgSection.find(".filevalue");
		var idFile = $(this).attr("id");
//		var file = document.getElementById(idFile);
//		var fileList = file.files; //获取的图片文件
		var imgContainer = $(this).parents(".z_photo"); //存放图片的父亲元素
		var fileList = $(this).closest(".z_file").find(".file").prop('files');
		var input = $(this).parent();//文本框的父亲元素
		//遍历得到的图片文件
		numUp = imgContainer.find(".up-section").length;
		var totalNum = numUp + fileList.length;  //总的数量
		var imgNum = imgSection.data("imgnum") ? imgSection.data("imgnum") : 4;
		if(fileList.length > imgNum || totalNum > imgNum ){
			alert("上传图片数目不可以超过" + imgNum + "个，请重新选择");  //一次选择上传超过4个 或者是已经上传和这次上传的到的总数也不可以超过4个
		}
		else if(numUp < imgNum){
			fileList = validateUp(fileList);
			if(!checkStatus){
				return;
			}
			var src=jQuery.parseJSON(uploadImg(this));
			$(".z_file").append("<input type='file' name='file' id='file' class='file' value='' accept='image/jpg,image/jpeg,image/png,image/bmp' multiple />");
			$(".file").change(changeDo);
			if(src["errMsg"]=="error"){
				alert("上传失败");
				return;
			}
			if (filevalue) {
				if (filevalue.val() != "") {
					filevalue.val(filevalue.val() + "," + src["path"]);
				} else {
					filevalue.val(src["path"]);
				}
			}
			var srcList=src["path"].split(",");
			var ipUrlList=src["ipUrl"].split(",");
			for(var i = 0;i<srcList.length;i++){
				var imgUrl = srcList[i];
				imgSrc.push(imgUrl);
				imgArr.push(imgUrl);
				var $section = $("<section id='"+getShortId(imgUrl)+"' class='up-section fl loading'>");
				imgContainer.append($section);
				var $span = $("<span class='up-span'>");
				$span.appendTo($section);

				var $img0 = $("<img class='close-upimg'>").on("click",function(event){
					event.preventDefault();
					event.stopPropagation();
					$(".works-mask").show();
//						$.fn.confirm("确定要删除此图片吗？", function() {
//							$(".wsdel-ok").click();
//						});
					delParent = $(this).parent();
				});
				$img0.attr("src",basePath+"/inc/vpoints/img/a7.png").appendTo($section);
				var $img = $("<img class='up-img up-opcity' style='width:auto; margin:auto;' >");
				$img.attr("src",ipUrlList[i]);
				$img.appendTo($section);
				var $p = $("<p class='img-name-p'>");
				$p.html(imgUrl).appendTo($section);
				var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
				$input.appendTo($section);
				var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
				$input2.appendTo($section);

			}
		}
		setTimeout(function(){
			$(".up-section").removeClass("loading");
			$(".up-img").removeClass("up-opcity");
		},450);
		numUp = imgContainer.find(".up-section").length;
		if(numUp >= imgNum){
			$(this).parent().hide();
		}
	}


	$(".z_photo").delegate(".close-upimg","click",function(){
		$(".works-mask").show();
//     	$.fn.confirm("确定要删除此图片吗？", function() {
//			$(".wsdel-ok").click();
//		});
		delParent = $(this).parent();
	});

	$(".wsdel-ok").click(function(){
		$(".works-mask").hide();

		var imgSection = $(delParent).closest(".img-section");
		var filevalue = imgSection.find(".filevalue");
		if (filevalue) {
			var imgUrl = delParent.find(".img-name-p").text();
			var fileImgUrl = filevalue.val();
			if (fileImgUrl) {
				if (fileImgUrl.indexOf(imgUrl) == 0) {
					fileImgUrl = fileImgUrl.replace(imgUrl, '');
				} else {
					fileImgUrl = fileImgUrl.replace("," + imgUrl, '');
				}
				if (fileImgUrl.indexOf(",") == 0) {
					fileImgUrl = fileImgUrl.substring(1, fileImgUrl.length);
				}
				filevalue.val(fileImgUrl);
			}
		}

		var numUp = delParent.siblings().length;
		var imgNum = imgSection.data("imgnum") ? imgSection.data("imgnum") : 4;
		if(numUp <= imgNum){
			delParent.parent().find(".z_file").show();
		}
		var delId=delParent.attr("id");
		imgSrc.removeByValue(delId);
		delParent.remove();
	});

	$(".wsdel-no").click(function(){
		$(".works-mask").hide();
	});

	function validateUp(files){
		checkStatus=true;
		var arrFiles = [];//替换的文件数组
		for(var i = 0, file; file = files[i]; i++){
			imgSize.push(file.size);
			//获取文件上传的后缀名
			var newStr = file.name.split("").reverse().join("");
			if(newStr.split(".")[0] != null){
				var type = newStr.split(".")[0].split("").reverse().join("");
				if(jQuery.inArray(type.toLowerCase(), defaults.fileType) > -1){
					// 类型符合，可以上传
					if (file.size >= defaults.fileSize) {
						alert('您这个"'+ file.name +'"文件大小过大,文件大小不能超过1M');
						checkStatus=false;
					} else {
						// 在这里需要判断当前所有文件中
						arrFiles.push(file);
					}
				}else{
					alert('您这个"'+ file.name +'"上传类型不符合');
					checkStatus=false;
				}
			}else{
				alert('您这个"'+ file.name +'"没有类型, 无法识别');
				checkStatus=false;
			}
		}
		return arrFiles;
	}

	function uploadImg(imgupfile){
		var result;
		var myform = document.createElement("form");
		myform.action = basePath+"/skuInfo/imgUploadUrl.do?vjfSessionId=" + $("#vjfSessionId").val();
		myform.method = "post";
		myform.enctype = "multipart/form-data";
		myform.style.display = "none";
		myform.appendChild(imgupfile);
		document.body.appendChild(myform);
		var form = $(myform);
		form.ajaxSubmit({
			async: false,
			success: function (data) {
				result=data;
				form.remove();
			}
		});
		return result;
	}
	Array.prototype.removeByValue = function(val) {
		for(var i=0; i<this.length; i++) {
			var shortId=getShortId(this[i]);
			if( shortId == val) {
				this.splice(i, 1);
				break;
			}
		}
	}
})
function showImg(src){
	if (src) {
		initImg($("#photoId"), src);
	} else {
		$(".filevalue").each(function(i, obj) {
			initImg($(this).closest(".img-box").find("#photoId"), $(this).val());
		});
	}
}
function initImg(imgContainer, src) {
	if(src == '') return false;

	var srcList=src.split(",");
	for(var i = 0;i<srcList.length;i++){
		var imgUrl = srcList[i];
		imgSrc.push(imgUrl);
		imgArr.push(imgUrl);
		var $section = $("<section id='"+getShortId(imgUrl)+"' class='up-section fl loading'>");
		imgContainer.append($section);
		var $span = $("<span class='up-span'>");
		$span.appendTo($section);

		var $img0 = $("<img class='close-upimg'>").on("click",function(event){
			event.preventDefault();
			event.stopPropagation();
			$(".works-mask").show();
//				$.fn.confirm("确定要删除此图片吗？", function() {
//					$(".wsdel-ok").click();
//				});
			delParent = $(this).parent();
			//delParent=$("#"+getShortId(imgUrl));
		});
		$img0.attr("src",basePath+"/inc/vpoints/img/a7.png").appendTo($section);
		var $img = $("<img class='up-img up-opcity' style='width:auto; margin:auto;' >");
		$img.attr("src",imageServerUrl+imgUrl);
		$img.appendTo($section);
		var $p = $("<p class='img-name-p'>");
		$p.html(imgUrl).appendTo($section);
		var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
		$input.appendTo($section);
		var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
		$input2.appendTo($section);
	}
	setTimeout(function(){
		$(".up-section").removeClass("loading");
		$(".up-img").removeClass("up-opcity");
	},450);
	numUp = imgContainer.find(".up-section").length;
	if(numUp >= 4){
		$(this).parent().hide();
	}
}
function getShortId(a){
	var b=a.lastIndexOf(".");
	var c =a.lastIndexOf("/");
	var d=a.substring(c+1,b);
	return d;
}