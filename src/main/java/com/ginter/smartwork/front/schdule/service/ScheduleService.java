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

package com.ginter.smartwork.front.schdule.service;

import com.ginter.smartwork.common.googleapis.GoogleCalendarAPI;
import com.ginter.smartwork.common.util.DateUtil;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class ScheduleService {
  /**
   * 달력 목록을 가져온다.
   * @return
   * @throws IOException
   */
  public static List<Map<String, Object>> caledarList() throws IOException {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    CalendarList calendarList = GoogleCalendarAPI.getCaledarList().execute();
    
    for(CalendarListEntry calendarListEntry : calendarList.getItems()) {
      Map<String, Object> hMap = new HashMap<String, Object>();
      
      hMap.put("id", calendarListEntry.getId());
      hMap.put("summary", calendarListEntry.getSummary());
      
      resultList.add(hMap);
    }
    
    return resultList;
  }
  
  /**
   * 일정 목록을 가져온다.
   * @return
   * @throws IOException
   */
  public static List<Map<String, Object>> list() throws IOException {
    final int MAX_RESULT = 30;  // 화면에 보여질 리스트 최대갯수
    
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    CalendarList calendarList = GoogleCalendarAPI.getCaledarList().execute();
    for(CalendarListEntry calendarListEntry : calendarList.getItems()) {      
      String calendarId = calendarListEntry.getId();
      
      Events events = GoogleCalendarAPI.getEventsList(calendarId).execute();
      for(Event event : events.getItems()) {
        Map<String, Object> hMap = new HashMap<String, Object>();
        
        hMap.put("summary", event.getSummary());
        hMap.put("creatorDisplayName", event.getCreator().getDisplayName());
        
        DateTime startDate = null;
        DateTime endDate = null;
        
        if(event.getStart().getDate() != null && event.getEnd().getDate() != null) {
          startDate = event.getStart().getDate(); 
          endDate = event.getEnd().getDate();
        } else {
          startDate = event.getStart().getDateTime(); 
          endDate = event.getEnd().getDateTime();
        }
        
        hMap.put("startDate", DateUtil.getDate(startDate.getValue()));
        hMap.put("endDate", DateUtil.getDate(endDate.getValue()));
        hMap.put("startDay", DateUtil.getDay(startDate.getValue()));
        hMap.put("endDay", DateUtil.getDay(endDate.getValue()));
        hMap.put("dday", DateUtil.getDday(startDate.getValue()));
        hMap.put("htmlLink", event.getHtmlLink());
        
        if(DateUtil.getDday(startDate.getValue()) < 0) {
          continue;
        }
        
        resultList.add(hMap);
      }
    }
    
    // 오름차순으로 정렬
    Collections.sort(resultList, new Comparator<Map<String, Object>>() {
      @Override
      public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        if(((Integer)o2.get("dday")).intValue() == ((Integer)o1.get("dday")).intValue()) 
          return 0;
        else if(((Integer)o2.get("dday")).intValue() < ((Integer)o1.get("dday")).intValue())
          return 1;
        else 
          return -1;
      }
    });
    
    if(resultList.size() > MAX_RESULT) {
      return resultList.subList(0, MAX_RESULT);
    }
    return resultList;
  }
}
