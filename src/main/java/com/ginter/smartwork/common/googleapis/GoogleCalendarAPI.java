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

package com.ginter.smartwork.common.googleapis;

import com.ginter.smartwork.common.Utils;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.CalendarList.List;

import java.io.IOException;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class GoogleCalendarAPI {
  /**
   * Tasks Builder 인스턴스를 가져온다.
   * @return
   * @throws IOException
   */
  private static Calendar getCalendar() throws IOException {
    // Build the Plus object using the credentials
    Calendar calendar = new Calendar.Builder(
        Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, Utils.getCredential()).build();
    return calendar;
  }
  
  /**
   * 달력 목록을 가져온다.
   * @return
   * @throws IOException
   */
  public static List getCaledarList() throws IOException {
    return getCalendar().calendarList().list();
  }
  
  public static com.google.api.services.calendar.Calendar.Events.List getEventsList(String calendarId) throws IOException {
    return getCalendar().events().list(calendarId);
  }
  
}
