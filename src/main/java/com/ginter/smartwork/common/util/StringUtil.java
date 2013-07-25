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

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class StringUtil {
  /**
   * 문자열 null 처리
   * @param str
   * @return
   */
  public static String nvl(String str) {
    return nvl2(str, "");
  }
  
  /**
   * 문자열 null 처리
   * @param str
   * @param str2
   * @return
   */
  public static String nvl2(String str, String str2) {
    return str != null ? str : str2;
  }
}
