package com.dbt.platform.autocode.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;;

public class RandomDataUtil {
	
	public static String randomSimple="23456789ABCDEFGHJKLMNPQRSTUVWXYZ";//32位
	
	public static String randomHard="23456789abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";//56位
	//23456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz//54位
	
	public static String weichai="0123456789abcdefghjkmnpqrstuvwxyz";
	
//	public static String weichaiHB="0123456789abcdefghjkmnpqrstuvwxyz";
	
	/**
	 * 广西青啤生成码数
	 */
	@Deprecated
	public Set<String> getVcodeGXQP(String codeCount){
		int perNum = 0;
		String data = randomSimple;
		Set<String> set = new HashSet<String>();
		while(perNum < Integer.parseInt(codeCount)){
			String reStr = "";
			for (int i = 0; i < 8; i++) {
				reStr += RandomUtil.randomString(data, 1);
			}
			if (!set.contains(reStr)){
				set.add(reStr);//将生成的码数据放入set 过滤重复数据
				perNum++;
			}
		}
		return set;
	}
	
	
	/**
	 * 广西青啤生成码数(广西青啤|浙江青啤|立白)【8位码+31位取值范围】
	 */
	public Set<String> getVcodeGXQP(long codeCount,Set<String> setAll, Set<String> shareQrcodeLib){
	    long perNum = 0;
	    String data = randomSimple;
	    Set<String> set = Collections.synchronizedSet(new HashSet<String>());
	    while(perNum < codeCount){
	        String reStr = "";
	        for (int i = 0; i < 8; i++) {
	            reStr += RandomUtil.randomString(data, 1);
	        }
	        if ((!set.contains(reStr))&&(!setAll.contains(reStr))&&(!shareQrcodeLib.contains(reStr))){
	            set.add(reStr);//将生成的码数据放入set 过滤重复数据
	            perNum++;
	        }
	    }
	    return set;
	}
	
	/**
	 * 青啤生成码_串码【9位码+31位取值范围】
	 * @param codeCount
	 * @param setAll
	 * @return
	 */
	public Set<String> getVcodeCM(long codeCount,Set<String> setAll, Set<String> shareQrcodeLib){
		long perNum = 0;
		String data = randomSimple;
        Set<String> set = Collections.synchronizedSet(new HashSet<String>());
		while(perNum < codeCount){
			String reStr = "";
			for (int i = 0; i < 9; i++) {
				reStr += RandomUtil.randomString(data, 1);
			}
			if ((!set.contains(reStr))&&(!setAll.contains(reStr))&&(!shareQrcodeLib.contains(reStr))){
				set.add(reStr);//将生成的码数据放入set 过滤重复数据
				perNum++;
			}
		}
		return set;
	}
	
	/**
	 * 拉环青啤生成码【9位码+56位取值范围】
	 * @param codeCount
	 * @param setAll
	 * @return
	 */
	public Set<String> getVcodeLH(long codeCount,Set<String> setAll, Set<String> shareQrcodeLib){
		long perNum = 0;
		// String data = randomHard;
		String data = randomSimple; // 由于识别度的问题都切换为全大写
        Set<String> set = Collections.synchronizedSet(new HashSet<String>());
		while(perNum < codeCount){
			String reStr = "";
			for (int i = 0; i < 9; i++) {
				reStr += RandomUtil.randomString(data, 1);
			}
			if ((!set.contains(reStr))&&(!setAll.contains(reStr))&&(!shareQrcodeLib.contains(reStr))){
				set.add(reStr);//将生成的码数据放入set 过滤重复数据
				perNum++;
			}
		}
		return set;
	}
	
	/**
	 * 随机获取6位字符【31位取值范围】
	 * @return
	 */
	public String getRandomSix(){
		String data = randomSimple;
		return RandomUtil.randomString(data, 6);
	}
	
	
	
	
	/**
	 * 通用生成码【建议使用】
	 * @param codeCount 生成码数量
	 * @param setAll 
	 * @param charSource 码字符源
	 * @param charlen 生成码长度
	 * @return
	 */
	public Set<String> getVcodeCommon(int codeCount,Set<String> setAll,String charSource,int charlen){
		int perNum = 0;
		String data = charSource;
		Set<String> set = new HashSet<String>();
		while(perNum < codeCount){
			String reStr = "";
			for (int i = 0; i < charlen; i++) {
				reStr += RandomUtil.randomString(data, 1);
			}
			if ((!set.contains(reStr))&&(!setAll.contains(reStr))){
				set.add(reStr);//将生成的码数据放入set 过滤重复数据
				perNum++;
			}
		}
		return set;
	}
	
//	public static void main(String[] args) {
//		//潍柴物流测试码
//		File f=new File("F:\\bak\\weichai\\201807030000\\test_wuliu_code.txt");
//		Set<String> setAll =new HashSet<String>();
//		setAll=new RandomData().getVcodeCommon(5000, setAll, weichai, 15);
//		Object[] array=setAll.toArray();
//		int setLen = array.length;
//		for (int i = 0; i < setLen; ++i) {
//			//vcodeKey=活动唯一标识码=三位字母
//			String source = "WV" + array[i];
//			BufferedWriteUtil.bufferedWrite(source,f);
//		}
//		//潍柴红包测试码
//		File f2=new File("F:\\bak\\weichai\\201807030000\\test_hongbao_code.txt");
//		File f3=new File("F:\\bak\\weichai\\201807030000\\test_hongbao_code_md5.txt");
//		Set<String> setAll2 =new HashSet<String>();
//		setAll2=new RandomData().getVcodeCommon(5000, setAll2, RandomData.weichai, 31);
//		Object[] array2=setAll2.toArray();
//		int setLen2 = array2.length;
//		MD5Util md5 = new MD5Util();
//		for (int i = 0; i < setLen2; ++i) {
//			//vcodeKey=活动唯一标识码=三位字母
//			String source = "V" + array2[i];
//			BufferedWriteUtil.bufferedWrite(source,f2);
//			BufferedWriteUtil.bufferedWrite(md5.getMD5ofStr(source).toLowerCase(),f3);
//		}
//	}
	
	/**
	 * 源码补充加密
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File f=new File("F:\\bak\\weichai\\20180622\\5000-1\\test_hongbao_code.txt");
		File f2=new File("F:\\bak\\weichai\\20180622\\5000-1\\test_hongbao_code_md5.txt");
		
		 FileWriter fw = new FileWriter(f2,true);
	        BufferedWriter bw = new BufferedWriter(fw);
	        
	        FileReader fr = new FileReader(f);
	        BufferedReader br = new BufferedReader(fr);
	        
	        String line=null;
//	        MD5Util md5 = new MD5Util();
	       // List<String> prefixList=new ArrayList<String>();
//	        while ((line = br.readLine()) != null) {
//	        	bw.write(md5.getMD5ofStr(line).toLowerCase());
//	        	bw.write("\r\n");
//	        }
	    	bw.flush();
	    	bw.close();
	    	br.close();
	}
	
	
//	public static void main(String[] args){
////		String[] data = {"2","3", "4", "5", "6", "7", "8", "9","a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l","m","n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y","z","A","B","C","D","E","F","G","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z"};
//		long start = System.currentTimeMillis();
//		String data = "23456789abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
//		int totals = 1000100;
//		int getTotals = 1000000;
//		int perTotal = 0;
//		int perNum = 0;
//		Set<String> set = new HashSet<String>();
//		while(perNum <= totals){
//			String reStr = "";
//			
//			for (int i = 0; i < 20; i++) {
//				reStr += RandomUtil.randomString(data, 1);
//			}
//			
////			BufferedWriteUtil.bufferedWrite(reStr,new File("G:/test.txt"));
//			if (!set.contains(reStr)){
//				set.add(reStr);//将生成的100W数据放入set 过滤重复数据
//				perNum++;
//			}
//		}
//		for (int t = 0; t < set.toArray().length; t++) {
//			if(perTotal == getTotals){
//				break;
//			}
//			BufferedWriteUtil.bufferedWrite(set.toArray()[t].toString(),new File("G:/test.txt"));
//			System.out.println(set.toArray()[t].toString());
//			perTotal++;
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("消耗"+(end-start));
//	}
}
