<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>位置信息</title>
</head>
<body>
	<script type="text/javascript"
		src="http://api.map.baidu.com/api?v=2.0&ak=DDeab79143f4badeab31428588d27aad"></script>
	<div style="width: 300px; height: 200px;" id="allmap"></div>
	<script type="text/javascript">
										// 百度地图API功能
										var map = new BMap.Map("allmap");    // 创建Map实例
 										var lon=${lon};
 										var lat=${lat};
 										map.centerAndZoom(new BMap.Point(lon, lat), 15);  // 初始化地图,设置中心点坐标和地图级别
										map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
										//map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
										//map.centerAndZoom('${city}',15);
	 									map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
 										var point = new BMap.Point(lon,lat);
 										var marker = new BMap.Marker(point);
 										map.addOverlay(marker);
										var opts = {
												  width : 100,     // 信息窗口宽度
												  height: 50,     // 信息窗口高度
												  title : "扫码地点" , // 信息窗口标题
												  enableMessage:false,//设置允许信息窗发送短息
												  message:"ddd"
												}
										var didian="${address}";
										var infoWindow = new BMap.InfoWindow("地址："+didian, opts);  // 创建信息窗口对象
										marker.addEventListener("click", function(){
											map.openInfoWindow(infoWindow,point); //开启信息窗口
										});
									</script>
</body>
</html>