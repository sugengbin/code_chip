package module.test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class MyStringUtils extends StringUtils {


	/**
	 * 将Object 转化为 String <br>
	 * o == null 	retrun StringUtils.EMPTY;
	 * o == ""      return StringUtils.EMPTY;
	 * o == " "     return StringUtils.EMPTY;
	 * o == "abc"   return "abc"; 
	 * o == " abc " return "abc";
	 * 
	 * @param o
	 * @return
	 */
	public static String objectToString(Object o) {
		String result = StringUtils.EMPTY;
		if (o != null) {
			result = StringUtils.trimToEmpty(o.toString());
		}
		return result;
	}

	/**
	 * 将String 转化为 long 			<br/>
	 *  s == "" 		return 0;	<br/> 
	 *  s == "  " 		return 0; 	<br/> 
	 *  s == "123" 		return 123; <br/> 
	 *  s == " 123 " 	return 123; <br/>
	 * 
	 * @param s
	 * @return
	 */
	public static long stringToLong(String s) {
		long result = 0;
		if (StringUtils.isNotBlank(s) || isNumeric(s)) {
			result =  Long.parseLong(StringUtils.trimToEmpty(s));
		}
		return result;
	}
	
	/**
	 * 检测String 是否为数字
	 * StringUtils.isNumeric不能检测负数、小数
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		boolean match = false;
		//正数: [0-9]*
		Pattern pattern = Pattern.compile("-?[0-9]+.*[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			match = true;
		}
		return match;
	}
	

	/**
	 * 
	 * 从一个字符串提取数字
	 * 
	 * @param s
	 * @return
	 */
	public static long extractNumber(String s) {
		if (StringUtils.isBlank(s)) {
			return 0;
		}
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
		String num = m.replaceAll(StringUtils.EMPTY).trim();
		return stringToLong(num);
	}

	/**
	 * 拼接相同的字符串
	 * 
	 * @param prefix
	 *            前缀
	 * @param ele
	 *            元素
	 * @param seperator
	 *            分隔字符串
	 * @param size
	 *            元素数量
	 * @param sufix
	 *            后缀
	 * @return
	 */
	public static String fillStrings(String prefix, String ele,
			String seperator, int size, String sufix) {
		if (size <= 0 || ele == null) {
			throw new IllegalArgumentException(
					"size must >0, element can't be null");
		}
		String[] eles = new String[size];
		Arrays.fill(eles, ele);
		return prefix + StringUtils.join(eles, seperator) + sufix;
	}

	/**
	 * 先trim，然后按照字节长度来截断尾部的字符串，因为不同编码字符的长度不一样，所以截断后字节长度是<=n
	 * 
	 * @param s
	 * @param n
	 * @return
	 */
	public static String trimAndCutTailByByte(String s, int n) {
		String ts = null;
		if (s != null) {
			ts = StringUtils.trim(s);
			byte[] utf8 = ts.getBytes();
			if (utf8.length > n && n > 0) {
				int n16 = 0;
				int advance = 1;
				int i = 0;
				while (i < n) {
					advance = 1;
					if ((utf8[i] & 0x80) == 0) {
						i += 1;
					} else if ((utf8[i] & 0xE0) == 0xC0) {
						i += 2;
					} else if ((utf8[i] & 0xF0) == 0xE0) {
						i += 3;
					} else {
						i += 4;
						advance = 2;
					}
					if (i <= n) {
						n16 += advance;
					}
				}
				ts = ts.substring(0, n16);
			} else if (n == 0) {
				ts = StringUtils.EMPTY;
			}
		}
		return ts;
	}

	/**
	 * 合并数组内元素
	 * 
	 * <pre>
	 * 例如：
	 * PCSSStringUtils.combineArr("#1(#2)",["Tom","Amy"],["Male","Female"]) -->["Tom(Male)","Amy(Female)"]
	 * 
	 * </pre>
	 * 
	 * @param pattern
	 *            模板，例如#1,#2
	 * @param defForNull
	 *            null的替换字符串
	 * @param arrs
	 *            数组
	 * @return
	 */
	public static String[] combineArrEle(String pattern, String defForNull,
			String[]... arrs) {
		String[] result = null;
		if (StringUtils.isNotEmpty(pattern)) {
			int minLength = Integer.MAX_VALUE;

			for (String[] arr : arrs) {
				minLength = Math.min(arr.length, minLength);
			}
			if (minLength != 0) {
				result = new String[minLength];
				int arrsLength = arrs.length;
				String replaceStr = null;
				for (int i = 0; i < minLength; i++) {
					result[i] = pattern;
					for (int j = 0; j < arrsLength; j++) {
						replaceStr = arrs[j][i];
						if (replaceStr == null) {
							replaceStr = defForNull;
						}
						result[i] = StringUtils.replace(result[i], "#"
								+ (j + 1), replaceStr);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 合并数组内元素，元素如果为null则替换为空字符串
	 * 
	 * <pre>
	 * 例如：
	 * combineArr("#1(#2)",["Tom","Amy"],["Male","Female"]) -->["Tom(Male)","Amy(Female)"]
	 * 
	 * </pre>
	 * 
	 * @param pattern
	 *            模板，例如#1,#2
	 * @param arrs
	 *            数组
	 * @return
	 */
	public static String[] combineArrEle(String pattern, String[]... arrs) {
		return combineArrEle(pattern, StringUtils.EMPTY, arrs);
	}

	/**
	 * 替换数组内的字符串
	 * 
	 * @param arr
	 * @param searchString
	 * @param replacement
	 * @return
	 */
	public static String[] replace(String[] arr, String searchString,
			String replacement) {
		String[] result = null;
		if (arr != null) {
			result = new String[arr.length];
			for (int i = 0; i < arr.length; i++) {
				result[i] = StringUtils.replace(arr[i], searchString,
						replacement);
			}
		}
		return result;
	}

	/**
	 * 将数组内的null转为空字符串
	 * 
	 * @param arr
	 * @return
	 */
	public static String[] stripToEmpty(String[] arr) {
		String[] result = null;
		if (arr != null) {
			result = new String[arr.length];
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] == null) {
					result[i] = StringUtils.EMPTY;
				} else {
					result[i] = arr[i];
				}
			}
		}
		return result;
	}

	/**
	 * 判断字符串是否存在数组中
	 * 
	 * @param val
	 * @param array
	 * @return
	 */
	public static boolean isStringInArray(String val, String[] array) {
		if (array == null || array.length == 0) {
			return false;
		}
		for (String str : array) {
			if (StringUtils.equals(val, str)) {
				return true;
			}
		}
		return false;
	}
}
