package com.dbt.framework.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
/**
 * 区域坐标判断工具类
 * @author zhaohongtao
 *2017年8月24日
 */
public class GeoCheckUtil {
	/**
	 * 坐标是否在坐标系内
	 * @param lon经度
	 * @param lat纬度
	 * @param pointList区域坐标系 格式(可自定义):(123.12_34.334;123.12_34.334)
	 * @return
	 */
	public static boolean isPtIn(double lon,double lat,String pointList){
		//double[] d=GeoChange.gpsToBd(lon, lat);//将gps坐标转为百度坐标
		//Point2D.Double point = new Point2D.Double(d[0], d[1]);//创建点
		Point2D.Double point = new Point2D.Double(lon, lat);
	    List<Point2D.Double> pts = new ArrayList<Point2D.Double>();//创建多边形
	    String[] points=pointList.split(";");//循环写入多边形
	    for (int i = 0; i < points.length; i++) {
	    	String[] po=points[i].split("_");
	    	 pts.add(new Point2D.Double(Double.valueOf(po[0]), Double.valueOf(po[1])));
		}
	    //判断是否在多边形内
	   return IsPtInPoly(point,pts);
	}
	/**
	 * 判断坐标是否在圆形区域内
	 * @param lon
	 * @param lat
	 * @param pointList
	 * @return
	 */
	public static boolean isCircleIn(double lon,double lat,String pointList){
		//double[] d=GeoChange.gpsToBd(lon, lat);//将gps坐标转为百度坐标
		String lonC=pointList.split(":")[1].split("_")[0];
		String latC=pointList.split(":")[1].split("_")[1];
		String cd=pointList.split(":")[1].split("_")[2];
		double cdd=Double.valueOf(cd);
		//判断坐标到圆点的距离
		double length=GeoHashUtil.GetDistance(lon, lat, Double.valueOf(lonC), Double.valueOf(latC));
		if(length<=cdd){
			return true;
		}
		return false;
	}
	/**
	 * 判断点是否在多边形内
	 * @param point 检测点
	 * @param pts   多边形的顶点
	 * @return      点在多边形内返回true,否则返回false
	 */
	private static boolean IsPtInPoly(Point2D.Double point, List<Point2D.Double> pts){
	    
	    int N = pts.size();
	    boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
	    int intersectCount = 0;//cross points count of x 
	    double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
	    Point2D.Double p1, p2;//neighbour bound vertices
	    Point2D.Double p = point; //当前点
	    
	    p1 = pts.get(0);//left vertex        
	    for(int i = 1; i <= N; ++i){//check all rays            
	        if(p.equals(p1)){
	            return boundOrVertex;//p is an vertex
	        }
	        
	        p2 = pts.get(i % N);//right vertex            
	        if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests                
	            p1 = p2; 
	            continue;//next ray left point
	        }
	        
	        if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)
	            if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray                    
	                if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray
	                    return boundOrVertex;
	                }
	                
	                if(p1.y == p2.y){//ray is vertical                        
	                    if(p1.y == p.y){//overlies on a vertical ray
	                        return boundOrVertex;
	                    }else{//before ray
	                        ++intersectCount;
	                    } 
	                }else{//cross point on the left side                        
	                    double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y                        
	                    if(Math.abs(p.y - xinters) < precision){//overlies on a ray
	                        return boundOrVertex;
	                    }
	                    
	                    if(p.y < xinters){//before ray
	                        ++intersectCount;
	                    } 
	                }
	            }
	        }else{//special case when ray is crossing through the vertex                
	            if(p.x == p2.x && p.y <= p2.y){//p crossing over p2                    
	                Point2D.Double p3 = pts.get((i+1) % N); //next vertex                    
	                if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
	                    ++intersectCount;
	                }else{
	                    intersectCount += 2;
	                }
	            }
	        }            
	        p1 = p2;//next ray left point
	    }
	    
	    if(intersectCount % 2 == 0){//偶数在多边形外
	        return false;
	    } else { //奇数在多边形内
	        return true;
	    }
	}
	public static void main(String[] args) {
		Point2D.Double point = new Point2D.Double(116.404072, 39.916605);
	    
	    List<Point2D.Double> pts = new ArrayList<Point2D.Double>();
	    pts.add(new Point2D.Double(116.395, 39.910));
	    pts.add(new Point2D.Double(116.394, 39.914));
	    pts.add(new Point2D.Double(116.403, 39.920));
	    pts.add(new Point2D.Double(116.402, 39.914));
	    pts.add(new Point2D.Double(116.410, 39.913));
	    
	    if(IsPtInPoly(point, pts)){
	        System.out.println("点在多边形内");
	    }else{
	        System.out.println("点在多边形外");
	    }
	}
}
