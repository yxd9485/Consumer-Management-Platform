<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String cpath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
	body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
	#allmap {width: 100%; height:550px; overflow: hidden;}
	#result {width:100%;font-size:12px;}
	dl,dt,dd,ul,li{
		margin:0;
		padding:0;
		list-style:none;
	}
	p{font-size:12px;}
	dt{
		font-size:14px;
		font-family:"微软雅黑";
		font-weight:bold;
		border-bottom:1px dotted #000;
		padding:5px 0 5px 5px;
		margin:5px 0;
	}
	dd{
		padding:5px 0 0 5px;
	}
	li{
		line-height:28px;
	}
	img {
	   	max-width: none !important;
	}
	</style>
	<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=DDeab79143f4badeab31428588d27aad"></script>
	<!--加载鼠标绘制工具-->
	<script type="text/javascript" src="https://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
	<link rel="stylesheet" href="https://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
	<!--加载检索信息窗口-->
	<script type="text/javascript" src="https://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
	<link rel="stylesheet" href="https://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
	<title>鼠标绘制工具</title>
	<jsp:include page="/inc/Main.jsp"></jsp:include>   
</head>
<body>
	<div id="allmap" style="overflow:hidden;zoom:1;position:relative;">	
		<div id="map" style="height:100%;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
	</div>
	<div id="result" style="visibility: hidden;">
		<input type="button" value="提交所选区域" onclick="allGps()"/>
		<input type="button" value="清除所有区域" onclick="clearAll()"/>
	</div>
	<script type="text/javascript">
	// 百度地图API功能
    var map = new BMap.Map('map',{enableMapClick:false});
	//定义地图中心点
    // var poi = new BMap.Point(116.392,39.918);
	//定义地图初始级别(数字越大越精细)
    // map.centerAndZoom(poi, 16);
	//定义中心城市
    map.centerAndZoom("${firstCity}",12);
	//启用滚轮缩放
    map.enableScrollWheelZoom(); 
    map.enableInertialDragging();
    map.enableContinuousZoom();
    
    // 地图加载完成设置最终城市定位
    map.addEventListener("tilesloaded", finalCityHandler);
    function finalCityHandler() {
        map.removeEventListener("tilesloaded", finalCityHandler);
        //定义中心城市
        if ("${secondCity}" != "") {
            map.centerAndZoom("${secondCity}",13);
        }
    }
    
    var local = new BMap.LocalSearch(map, {
		renderOptions:{map: map}
	});
	
     // 定义一个控件类,即function
	function ZoomControl(){
	  // 默认停靠位置和偏移量
	  this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
	  this.defaultOffset = new BMap.Size(80, 20);
	}

	// 通过JavaScript的prototype属性继承于BMap.Control
	ZoomControl.prototype = new BMap.Control();

	// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
	// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
	ZoomControl.prototype.initialize = function(map){
	  // 创建一个DOM元素
	  var div = document.createElement("div");
	  
	  var d = document.createElement("input");
      d.type = "text";
      d.setAttribute("style","width:120px;height:25px;");
      var f = document.createElement("input");
      f.type = "button";
      f.value = "搜索";
      
	  div.appendChild(d);
	  div.appendChild(f);
	  // 设置样式
	  // 绑定事件,点击一次放大两级
	  f.onclick = function(e){
		var val=d.value;
		if(val){
			local.search(val);
		}
	  }
	  // 添加DOM元素到地图中
	  map.getContainer().appendChild(div);
	  // 将DOM元素返回
	  return div;
	}
	// 创建控件
	var myZoomCtrl = new ZoomControl();
	// 添加到地图当中
	map.addControl(myZoomCtrl);
    
//     //版权控件
//     var cr = new BMap.CopyrightControl({anchor: BMAP_ANCHOR_TOP_LEFT});   //设置版权控件位置
// 	map.addControl(cr); //添加版权控件
// 	var bs = map.getBounds();   //返回地图可视区域
// 	cr.addCopyright({id: 1, content: "<a href='http://www.vjifen.com/' target='_blank' style='font-size:15px'>技术支持:V积分</a>", bounds: bs});
	
	//搜索控件
	var size = new BMap.Size(10, 20);
    map.addControl(new BMap.CityListControl({
        anchor: BMAP_ANCHOR_TOP_LEFT,
        offset: size,
    }));
  
    //覆盖物列表
    var overlays = [];
    //--------------------------右键菜单方法---------------------------
    //删除动作方法
    var removeMarker = function(e,ee,marker){
		map.removeOverlay(marker);//地图删除覆盖物
		delPoint(overlays,marker);//覆盖物列表删除此覆盖物
	}
    //坐标检测方法
    var checkMarker = function(e,ee,marker){
    	var lon=marker.getPosition().lng;
    	var lat=marker.getPosition().lat;
    	checkPointIn(lon,lat);
	}
    //开启多边形编辑
    var beginEdit = function(e,ee,marker){
    	marker.enableEditing();
	}
    //关闭多边形编辑
    var endEdit = function(e,ee,marker){
    	if(marker.point instanceof Object){
    		var r = marker.getRadius(); 
	         if(r>1000*10){
	        	 $.fn.alert("半径不能超过10千米");
	        	 return;
	         }
    	}else{
    		var tops=marker.getPath().length;
        	if(tops>30){//多边形边数限制
        		$.fn.alert("多边形最大不能超过30条边!");
        		return;
        	}
    	}
    	
    	marker.disableEditing();
	}
    //todo--------添加标注
    var addLabel = function(e,ee,marker){
    	
	}
    
    // 更改中心城市
    function changeCenterCity(cityName) {
        map.centerAndZoom(cityName,13);
    }
    
    //右键菜单
    function getMenu(rectangle){
    	if ('${currentUser.roleKey}' == "1" || '${currentUser.roleKey}' == "2"){
    		var markerMenu=new BMap.ContextMenu();
    		markerMenu.addItem(new BMap.MenuItem('删除区域',removeMarker.bind(rectangle)));//为菜单绑定事件
    		markerMenu.addItem(new BMap.MenuItem('开始编辑',beginEdit.bind(rectangle)));//为菜单绑定事件
    		markerMenu.addItem(new BMap.MenuItem('结束编辑',endEdit.bind(rectangle)));//为菜单绑定事件
    		return markerMenu;
    	}else{
    		return false;
    	} 
    }
    //---------------------------覆盖物方法(覆盖物画完后调用)---------------------------
    //为覆盖物绑定方法
	var overlaycomplete = function(e){
		var p = e.drawingMode;//获取覆盖物的属性

        if(overlays.length==50){
        	   $.fn.alert("最多只能添加50个区域!");
               map.removeOverlay(e.overlay);
               return;
        }
		
		if(p =='marker'){//是否是点
			var markerMenu=new BMap.ContextMenu();//生成右键菜单
			markerMenu.addItem(new BMap.MenuItem('检测此点坐标',checkMarker.bind(e.overlay)));//为菜单绑定事件
			markerMenu.addItem(new BMap.MenuItem('删除',removeMarker.bind(e.overlay)));//为菜单绑定事件
			e.overlay.addContextMenu(markerMenu);//覆盖物绑定菜单
		}else if(p =='circle'){ // 圆
	         var r = e.overlay.getRadius(); 
	         if(r>1000*10){
	        	 $.fn.alert("半径不能超过10千米");
	        	 map.removeOverlay(e.overlay);
	        	 return;
	        	 
	         } else if(r<5){ // 半径小于5米认为图形无效
                 map.removeOverlay(e.overlay);
                 return;
	         }
			overlays.push(e.overlay);//覆盖物列表添加覆盖物
			//添加右键菜单
			e.overlay.addContextMenu(getMenu(e.overlay));//覆盖物绑定菜单
			
		} else if (p == 'rectangle') { // 矩形
			
			// 如果两点重合则视为无效
			if (JSON.stringify(e.overlay.getPath()[0]) == JSON.stringify(e.overlay.getPath()[1])) {
                map.removeOverlay(e.overlay);
                return;
			}
            overlays.push(e.overlay);//覆盖物列表添加覆盖物
            //添加右键菜单
            e.overlay.addContextMenu(getMenu(e.overlay));//覆盖物绑定菜单
            
		} else { // 多边形
			if(e.overlay.getPath().length>30){//多边形边数限制
				$.fn.alert("多边形最大不能超过30条边!");
	    		map.removeOverlay(e.overlay);
	    		return;
	    	} else if (e.overlay.getPath().length<3) {
                map.removeOverlay(e.overlay);
                return;
	    	}
			overlays.push(e.overlay);//覆盖物列表添加覆盖物
			//添加右键菜单
			e.overlay.addContextMenu(getMenu(e.overlay));//覆盖物绑定菜单
		}
    };
    
    //图形样式
    var styleOptions = {
        strokeColor:"red",    //边线颜色。
        //fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 3,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    }
    
    //实例化鼠标绘制工具
    var drawingManager = new BMapLib.DrawingManager(map, {
        isOpen: false, //是否开启绘制模式
        enableDrawingTool: "${currentUser.roleKey}" == "4" ? false : true, //是否显示工具栏
        drawingToolOptions: {
            anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
            offset: new BMap.Size(5, 5), //偏离值
          //显示哪些绘制工具(BMAP_DRAWING_MARKER:点,BMAP_DRAWING_CIRCLE:圆,BMAP_DRAWING_POLYLINE:折线,BMAP_DRAWING_POLYGON:多边形,BMAP_DRAWING_RECTANGLE:矩形)
            drawingModes:[BMAP_DRAWING_RECTANGLE,BMAP_DRAWING_POLYGON,BMAP_DRAWING_CIRCLE],
            drawingTypes:[BMAP_DRAWING_RECTANGLE,BMAP_DRAWING_POLYGON,BMAP_DRAWING_CIRCLE]
        }
    	//,
        //circleOptions: styleOptions, //圆的样式
       // polylineOptions: styleOptions, //线的样式
       // polygonOptions: styleOptions, //多边形的样式
       // rectangleOptions: styleOptions //矩形的样式
    });  
	 //添加鼠标绘制工具监听事件，用于获取绘制结果
    drawingManager.addEventListener('overlaycomplete', overlaycomplete);
	 
	 //清除所有覆盖物
    function clearAll() {
		for(var i = 0; i < overlays.length; i++){
            map.removeOverlay(overlays[i]);//循环清除
        }
        overlays.length = 0;
    }
	 
    //提交方法
    function allGps(){
    	if(overlays.length==0){
    		// alert("未选择任何区域");
    		return;
    	}
    	var points=[];//区域数组
    	for (var a = 0; a < overlays.length; a++) {
    		//var type=overlays[a].wQ;
    		//var type=Object.getPrototypeOf(overlays[a]).wQ;
    		//if(type=='Circle'){
    		if(overlays[a].point instanceof Object){
    			var str="circle:"+overlays[a].getCenter().lng+"_"+overlays[a].getCenter().lat+"_"+overlays[a].getRadius();
    		}else{
    			var ov=overlays[a].getPath();//单个覆盖物
        		var str="polygon:";
        		for (var t = 0; t < ov.length; t++) {
        			//循环添加各定点的横纵坐标
        			if(t==(ov.length-1)){
        				str+=ov[t].lng+"_"+ov[t].lat;
        			}else{
        				str+=ov[t].lng+"_"+ov[t].lat+";";
        			}
    			}
    		}
    		
			points.push(str);//将坐标系添加到区域数组
		}
    	return points;
    	// uploadPoint(points);//上传区域坐标系
    }
    
    //删除单个覆盖物
    function delPoint(arr, val){
    	for(var i=0; i<arr.length; i++) {
    	    if(arr[i] == val) {
    	      arr.splice(i, 1);
    	      break;
    	    }
    	}
    }
    
    //todo----------判断覆盖物是否有嵌套或重叠(如果有，则返回false)
    function checkGps(){
    	for (var a = 0; a < overlays.length; a++) {
    		var ov=overlays[a].getPath();
    		str="横坐标:"+ov[0].lng+",横坐标:"+ov[2].lng+",纵坐标:"+ov[2].lat+",纵坐标:"+ov[0].lat;
    		for(b = 0; b < overlays.length; b++){
    			var ov1=overlays[b].getPath();
    		}
		}
    }
    //上传区域坐标系
    function uploadPoint(points){
    	var url_="<%=cpath%>/mapTest/uploadPoint.do?pointString="+points;
		$.ajax({
			type: "POST",
			url: url_,
			beforeSend:appendVjfSessionId,
                    success: function(data){
					if(data=="success"){
						alert("提交成功");
					}	
			}
		});
    }
    //检测某个点是否在坐标系内
    function checkPointIn(lon,lat){
    	var url_="<%=cpath%>/mapTest/checkPoint.do?lon="+lon+"&lat="+lat;
		$.ajax({
			type: "POST",
			url: url_,
			beforeSend:appendVjfSessionId,
                    success: function(data){
					alert(data);
			}
		});
    }
    //接收后台传过来的区域列表，并画多边形
    var pointString='${hotAreaCog.coordinate}';
    if(pointString){
    	//var pointList=eval("(" + pointString + ")");//转换
    	var pointList=pointString.split(",");
    	if(pointList.length>0){//列表不为空
    		for (var a = 0; a < pointList.length; a++) {
    			var points=pointList[a];//单个区域
    			var rectangle;
    			if(points.indexOf('circle')>-1){
    				var circle=points.split(":")[1];
    				var p=circle.split("_");
    				var point=new BMap.Point(p[0], p[1]);
    				rectangle=new BMap.Circle(point,p[2],styleOptions);
    				
    			}else{
            		var point=points.split(":")[1].split(";");//坐标系
            		var markers=[];
            		for (var b = 0; b < point.length; b++) {
            			var gps=point[b].split("_");//单个坐标
            			markers.push(new BMap.Point(gps[0],gps[1]));//生成坐标点
            		}
            		rectangle = new BMap.Polygon(markers, styleOptions);  //创建矩形
    			}
    			//添加右键菜单
        		rectangle.addContextMenu(getMenu(rectangle));	
        		//地图添加矩形
          		 map.addOverlay(rectangle);
          		//覆盖物列表添加此覆盖物
          		overlays.push(rectangle);
        	}
    	}
    }    
</script>
</body>
</html>