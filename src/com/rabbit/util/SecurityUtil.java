package com.rabbit.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SecurityUtil {
	//MD5 32位的加密
	public static String md5(String plainText){
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				return "";
			}
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
	}
	
	//sql注入关键字检测
	public static boolean sqlCheck (String param){
		if(param==null||!param.isEmpty())
			return false;
		String[] keys = {"select ",";","update ","union ","drop ","delete ","create ",
				"insert ","exec ","systemcolumn ","alter ","</script>"}; 
		for (String k : keys){
			if(param.toLowerCase().indexOf(k)!=-1){
				return true;
			}
		}
		return false;
	}
	
	public static String sqlFilter(String param){
		return (sqlCheck(param))?"":param;
	}

	// 生成随机密码
	public static String getPass(int passLenth) {
		StringBuffer buffer = new StringBuffer(
				"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < passLenth; i++) {
			// 生成指定范围类的随机数0—字符串长度(包括0、不包括字符串长度)
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

}
