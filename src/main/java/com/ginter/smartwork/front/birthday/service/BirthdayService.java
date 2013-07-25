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

package com.ginter.smartwork.front.birthday.service;

import com.ginter.smartwork.common.util.DateUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.UserServiceFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class BirthdayService {
  public static List<Map<String, Object>> list() throws ParseException {
    
    String[] MONTH = new String[12];
    
    String currDate = new SimpleDateFormat("yyyyMM").format(System.currentTimeMillis());
    String currMonth = currDate.substring(4, 6);
    
    int currMonthInt = Integer.parseInt(currMonth);    
    
    // 현재달을 배열 첫번째 기준으로 해서 증가하여 할당시킨다.
    int monthInt = currMonthInt;
    for(int i=0; i < MONTH.length; i++) {      
      if(monthInt > 9) {
        MONTH[i] = String.valueOf(monthInt);
      } else {
        MONTH[i] = "0" + String.valueOf(monthInt);
      }
      monthInt++;
      if(monthInt > 12) {
        monthInt = 1;
      }
    }
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1]; 
    Filter domainFilter = new FilterPredicate("domain", FilterOperator.EQUAL, domain);
    List<Entity> memberList = datastore.prepare(new Query("MEMBER").setFilter(domainFilter)).asList(FetchOptions.Builder.withDefaults());
    
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    for(int i=0; i < MONTH.length; i++) {
      List<Map<String, Object>> persons = new ArrayList<Map<String, Object>>();
      Map<String, Object> resultMap = new HashMap<String, Object>();
      
      for(Entity member : memberList) {        
        String currYear = currDate.substring(0, 4);
        String birthday = (String)member.getProperty("birthday");
        String bMonth = "";    // 월
        String bDay = "";      // 일
        if(birthday != null && !"".equals(birthday)) {
          birthday = birthday.replaceAll("-", "");
          bMonth = birthday.substring(4, 6);
          bDay = birthday.substring(6, 8);
        }
         
        // 현재 찾고자 하는 월인지 확인
        if(MONTH[i].equals(bMonth)) {
          String year = currYear;
          
          // 생월이 현재월보다 작으면 내년연도를 넣어준다. (생일이 지난간 달이므로..)
          if(currMonthInt > Integer.parseInt(bMonth)) {
            int yearInt = Integer.parseInt(currYear) + 1;
            year = String.valueOf(yearInt); 
          }
          
          Map<String, Object> person = new HashMap<String, Object>();
          person.put("day", bDay);
          person.put("name", member.getProperty("fullName"));
          person.put("department", member.getProperty("department"));
          person.put("dday", DateUtil.getDday(new SimpleDateFormat("yyyyMMdd").parse(year + bMonth + bDay).getTime()));
          person.put("photo", member.getProperty("photo"));
           
          persons.add(person);
        }
        resultMap.put("persons", persons);
      }
      resultMap.put("month", MONTH[i]);
      
      List<Map<String, Object>> personList = (List<Map<String, Object>>)resultMap.get("persons");
      if(personList.size() > 0) {
        resultList.add(resultMap);
      }
    }
    return resultList;
  }
}
