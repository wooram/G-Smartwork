/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ginter.smartwork.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class DateUtil {
  /**
   * 설정한 포맷으로 현재 날짜 가져오기
   * @param format
   * @return
   */
  public static String getCurrentDate() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    return sdf.format(new Date());
  }
  
  /**
   * 설정한 포맷으로 현재 날짜 가져오기
   * @param format
   * @return
   */
  public static String getCurrentDate(String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(new Date());
  }
  
  /**
   * yyyyMMddHHmmss 포맷으로 날짜 가져오기
   * @param date
   * @param format
   * @return
   */
  public static String getDate(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    return sdf.format(date);
  }
  
  /**
   * 설정한 포맷으로 날짜 가져오기
   * @param date
   * @param format
   * @return
   */
  public static String getDate(Date date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(date);
  }
  
  /**
   * yyyyMMddHHmmss 포맷으로 날짜 가져오기
   * @param date
   * @param format
   * @return
   */
  public static String getDate(long date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    return sdf.format(date);
  }
  
  /**
   * 설정한 포맷으로 날짜 가져오기
   * @param date
   * @param format
   * @return
   */
  public static String getDate(long date, String format) {
    return getDate(new Date(date), format);
  }
  
  /**
   * 이벤트시간을 기준으로 d-day 가져오기
   * @param eventTime
   * @return
   */
  public static int getDday(long eventTime) {
    long currentTime = System.currentTimeMillis();
    long dday = (eventTime / (60 * 60 * 24 * 1000) - currentTime / (60 * 60 * 24 * 1000));
    return (int) dday;
  }
  
  /**
   * 날짜에 해당하는 요일 가져오기
   * @param date
   * @return
   */
  public static String getDay(long date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String d = sdf.format(new Date(date));
    
    int year = Integer.parseInt(d.substring(0, 4));
    int month = Integer.parseInt(d.substring(4, 6));
    int day = Integer.parseInt(d.substring(6, 8));
    
    Calendar cal = Calendar.getInstance();
    cal.set(year, month -1, day);
    
    String returnStr = "";
    switch(cal.get(Calendar.DAY_OF_WEEK)) {
      case 1: returnStr = "일"; break; 
      case 2: returnStr = "월"; break; 
      case 3: returnStr = "화"; break; 
      case 4: returnStr = "수"; break; 
      case 5: returnStr = "목"; break; 
      case 6: returnStr = "금"; break; 
      case 7: returnStr = "토"; break; 
    }
    return returnStr;
  }
}
