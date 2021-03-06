package com.dindong.dingdong.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dindong.dingdong.network.bean.entity.Address;

/**
 * Created by wcong on 2018/3/17. 文本格式化
 */

public class StringUtil {

  public static String format(String pattern, Object... arguments) {
    if (IsEmpty.string(pattern) || IsEmpty.object(arguments))
      return null;
    MessageFormat format = new MessageFormat(pattern);
    for (int i = 0; i < arguments.length; i++) {
      if (arguments[i] == null) {
        format.setFormat(i, null);
      }
    }
    return format.format(arguments);
  }

  public static String qty(BigDecimal qty) {
    return format("{0,number,0}", qty);
  }

  public static String amount(String amount) {
    return amount(new BigDecimal(amount));
  }

  public static String amount(BigDecimal amount) {
    return format("{0,number,0.00}", amount);
  }

  /**
   * 格式化数量，整数为##，小数则保留小数点后两位 #.##
   *
   * @param count
   * @return
   */
  public static String formatAmount(BigDecimal count) {
    String tempCount = StringUtil.amount(count);
    if (Integer.valueOf(tempCount.substring(tempCount.indexOf('.') + 1, tempCount.length())) == 0)
      return transQty(count, 1);
    else
      return transAmount(count);
  }

  public static String transQty(int count, int scale) {
    return transQty(new BigDecimal(count), scale);
  }

  public static String transQty(BigDecimal bigDecimal, int scale) {
    if (IsEmpty.object(bigDecimal))
      bigDecimal = BigDecimal.ZERO;
    String temp = "{0, number,0.###}";
    if (bigDecimal.compareTo(new BigDecimal(1000000000)) > 0) {
      return MessageFormat.format(temp,
          bigDecimal.divide(new BigDecimal(100000000), scale, RoundingMode.HALF_UP)) + "亿";
    } else if (bigDecimal.compareTo(new BigDecimal(100000)) > 0) {
      return MessageFormat.format(temp,
          bigDecimal.divide(new BigDecimal(10000), scale, RoundingMode.HALF_UP)) + "万";
    } else {
      return MessageFormat.format(temp, bigDecimal.setScale(scale, RoundingMode.HALF_UP));
    }
  }

  // 转化Amount 亿／万
  public static String transAmount(BigDecimal bigDecimal) {
    if (IsEmpty.object(bigDecimal))
      bigDecimal = BigDecimal.ZERO;
    String temp = "{0, number,0.00}";
    if (bigDecimal.compareTo(new BigDecimal(1000000000)) > 0) {
      return MessageFormat.format(temp,
          bigDecimal.divide(new BigDecimal(100000000), 2, RoundingMode.HALF_UP)) + "亿";
    } else if (bigDecimal.compareTo(new BigDecimal(100000)) > 0) {
      return MessageFormat.format(temp,
          bigDecimal.divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP)) + "万";
    } else {
      return MessageFormat.format(temp, bigDecimal.setScale(2, RoundingMode.HALF_UP));
    }
  }

  /**
   * 地址转字符串
   *
   * @param address
   * @return
   */
  public static String address2Str(Address address) {
    return address.getProvince().getText()
        + (address.getProvince().getText().equals(address.getCity().getText()) ? ""
            : address.getCity().getText())
        + address.getDistrict().getText() + address.getStreet();
  }

  /**
   * 城市格式化，去掉市用来UI显示
   * 
   * @param province
   * @return
   */
  public static String formatCity(String province) {
    return province.contains("市") ? province.substring(0, province.indexOf("市")) : province;
  }

  public static String formatRange(String range) {
    return new BigDecimal("range").compareTo(BigDecimal.valueOf(1000)) >= 0
        ? new BigDecimal("range").divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_UP) + "K"
        : qty(new BigDecimal("range"));
  }

  public static String formatRange(BigDecimal range) {
    if (range == null)
      return "";
    return range.compareTo(BigDecimal.valueOf(1000)) >= 0
        ? range.divide(BigDecimal.valueOf(1000), 1, RoundingMode.HALF_UP) + "k"
        : qty(range);
  }

  /**
   * 校验正确手机号
   * 
   * @param mobile
   * @return
   */
  public static boolean checkMobile(String mobile) {
    String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    if (mobile.length() != 11) {
      return false;
    } else {
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(mobile);
      return m.matches();
    }
  }
}
