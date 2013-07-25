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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class CommonUtil {
  /**
   * key의 값(정수)을 기준으로 내림차순으로 정렬한다.
   * @param list
   * @param key
   */
  public static void sortAscMapList(List<Map<String, Object>> list, final String key) {
    Collections.sort(list, new Comparator<Map<String, Object>>() {
      @Override
      public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        if("java.lang.Integer".equals(o1.get(key).getClass().getName())) {
          if(((Integer)o2.get(key)).intValue() == ((Integer)o1.get(key)).intValue()) 
            return 0;
          else if(((Integer)o2.get(key)).intValue() < ((Integer)o1.get(key)).intValue())
            return 1;
          else 
            return -1;
        } else if("java.lang.Long".equals(o1.get(key).getClass().getName())) {
          if(((Long)o2.get(key)).longValue() == ((Long)o1.get(key)).longValue()) 
            return 0;
          else if(((Long)o2.get(key)).longValue() < ((Long)o1.get(key)).longValue())
            return 1;
          else 
            return -1;
        } else if("java.lang.String".equals(o1.get(key).getClass().getName())) {
          if(Long.parseLong((String)o2.get(key)) == Long.parseLong((String)o1.get(key))) 
            return 0;
          else if(Long.parseLong((String)o2.get(key)) < Long.parseLong((String)o1.get(key)))
            return 1;
          else 
            return -1;
        } else {
          return -2;
        }
      }
    });
  }
  
  /**
   * key의 값(정수)을 기준으로 오름차순으로 정렬한다.
   * @param list
   * @param key
   */
  public static void sortDescMapList(List<Map<String, Object>> list, final String key) {
    Collections.sort(list, new Comparator<Map<String, Object>>() {
      @Override
      public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        if("java.lang.Integer".equals(o1.get(key).getClass().getName())) {
          if(((Integer)o2.get(key)).intValue() == ((Integer)o1.get(key)).intValue()) 
            return 0;
          else if(((Integer)o2.get(key)).intValue() > ((Integer)o1.get(key)).intValue())
            return 1;
          else 
            return -1;
        } else if("java.lang.Long".equals(o1.get(key).getClass().getName())) {
          if(((Long)o2.get(key)).longValue() == ((Long)o1.get(key)).longValue()) 
            return 0;
          else if(((Long)o2.get(key)).longValue() > ((Long)o1.get(key)).longValue())
            return 1;
          else 
            return -1;
        } else if("java.lang.String".equals(o1.get(key).getClass().getName())) {
          if(Long.parseLong((String)o2.get(key)) == Long.parseLong((String)o1.get(key))) 
            return 0;
          else if(Long.parseLong((String)o2.get(key)) > Long.parseLong((String)o1.get(key)))
            return 1;
          else 
            return -1;
        } else {
          return -2;
        }
      }
    });
  }
  
  public static void startTimeStamp(Logger log, String label) {
    log.info("[START]["+label+"] " + new SimpleDateFormat("yyyy년 MM월 DD일 HH:mm:ss SSS").format(new Date(System.currentTimeMillis())));
  }
  
  public static void endTimeStamp(Logger log, String label) {
    log.info("[END]["+label+"] " + new SimpleDateFormat("yyyy년 MM월 DD일 HH:mm:ss SSS").format(new Date(System.currentTimeMillis())));
  }
}
