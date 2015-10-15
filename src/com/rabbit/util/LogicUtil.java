package com.rabbit.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.lang.RandomStringUtils;

import com.jfinal.kit.StrKit;

public class LogicUtil {
	//创建订单号 yyMMdd+随机数字
	public static String createOrderNo(int count){
		if(count<=6)return RandomStringUtils.randomNumeric(count);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
		return simpleDateFormat.format(new Date())+RandomStringUtils.randomNumeric(count-6);
	}
	
	//创建订单号 时间戳+随机数字
	public static String createSn(int count){
		String pre = Long.toString(System.currentTimeMillis()/(1000*8*3600));
		if(count<=pre.length())return RandomStringUtils.randomNumeric(count);
		return pre+RandomStringUtils.randomNumeric(count-pre.length());
	}
	
	//随机文件名 yyMMdd+随机字符
	public static String createRandomFileName() {
		Date dt = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(dt)+RandomStringUtils.randomAlphanumeric(12);
	}
	
	//过滤utf8mb4
	public static String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("utf-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            }
            else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            }
            else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }
        }
        buffer.flip();
        return new String(buffer.array(), "utf-8");
    }
	
	//根据百分比计算成功率
	public static boolean drawPercent(float rate) {
		if(rate>100f){
			return true;
		}
		Random random = new Random();
    	int rcout = random.nextInt(10000);
    	if(rcout<(rate*100)){
    		return true;
    	}
    	else return false;
	}
	
	//根据中间数组进行抽奖
	public static Object drawPan(Map<Float,Object> m) {
		Random random = new Random();
		float rcout = random.nextFloat()*100;
		float mk = 100f;
		for(Float k: m.keySet()){
			if(rcout<=k.floatValue()&&k.floatValue()<mk){
				mk = k.floatValue();
			}
		}
		if(mk<100f){
			return m.get(mk);
		}
		return null;
	}
	
	//获取汉语拼音的首字符
	public static String firstChar(String hanyu){
		String firstChar = "";
		if(StrKit.notBlank(hanyu)){
			String[] fchar = PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(0));
			if(fchar!=null&&fchar.length>0){
				firstChar = fchar[0].substring(0,1).toUpperCase();
			}
		}
		return firstChar;
	}
	
	//[1,2]转换为 (1,2) sql查询用
	public static String arrToInsqlStr(String[] strarr){
		StringBuilder strb = new StringBuilder("(");
		for(int i=0;i<strarr.length;i++){
			strb.append(strarr[i]).append(",");
		}
		if(strb.length()>0){
			String insqlStr = strb.substring(0, strb.length()-1);
			return insqlStr+")";
		}
		else return "";
	}
}
