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

package com.ginter.smartwork.front.notice.service;

import com.ginter.smartwork.common.util.CommonUtil;
import com.ginter.smartwork.common.util.DateUtil;
import com.ginter.smartwork.common.util.StringUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class NoticeService {
  /**
   * 등록된 공지사항 목록을 보여준다.
   * @return
   */
  public static List<Map<String, Object>> list() {
    final int MAX_RESULT = 30;
    
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1];
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Filter domainFilter = new FilterPredicate("domain", FilterOperator.EQUAL, domain);
    List<Entity> noticeList = datastore.prepare(new Query("NOTICE").setFilter(domainFilter)).asList(FetchOptions.Builder.withDefaults());
    
    for(Entity e : noticeList) {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      resultMap.put("id", e.getKey().getId());
      resultMap.put("title", e.getProperty("title"));
      String contents = StringUtil.nvl((String)e.getProperty("contents"));
      contents = contents.replaceAll("\r\n", "<br />");
      
      resultMap.put("contents", contents);
      resultMap.put("createDate", e.getProperty("createDate"));
      resultMap.put("modifyDate", e.getProperty("modifyDate"));
      resultMap.put("writer", e.getProperty("writer"));
      
      resultList.add(resultMap);
    }
    
    CommonUtil.sortDescMapList(resultList, "modifyDate");
    if(resultList.size() > MAX_RESULT) {
      resultList.subList(0, MAX_RESULT);
    }
    
    return resultList;
  }
  
  /**
   * 공지사항 상세정보를 보여준다.
   * @return
   */
  public static Map<String, Object> get(String id) throws EntityNotFoundException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Key noticeKey = KeyFactory.createKey("NOTICE", Long.parseLong(id));
    Entity e = datastore.get(noticeKey);

    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("title", e.getProperty("title"));
    resultMap.put("contents", e.getProperty("contents"));
    resultMap.put("createDate", e.getProperty("createDate"));
    resultMap.put("modifyDate", e.getProperty("modifyDate"));
    resultMap.put("writer", e.getProperty("writer"));
    
    return resultMap;  
  }
  
  /**
   * 공지사항를 등록한다.
   * @param params
   */
  public static void insert(Map<String, Object> params) {
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1];
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    try {
      String title = (String)params.get("title");
      String contents = (String)params.get("contents");
      String writer = (String)params.get("writer");
      
      Entity notice = new Entity("NOTICE");
      notice.setProperty("domain", domain);
      notice.setProperty("title", title);
      notice.setProperty("contents", contents);
      notice.setProperty("createDate", DateUtil.getCurrentDate());
      notice.setProperty("modifyDate", DateUtil.getCurrentDate());
      notice.setProperty("writer", writer);
      
      datastore.put(notice);
      
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
  
  /**
   * 공지사항를 수정한다.
   * @param params
   */
  public static void update(Map<String, Object> params) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    try {
      String id = (String)params.get("id");
      String title = (String)params.get("title");
      String contents = (String)params.get("contents");
      
      Entity notice = new Entity("NOTICE", Long.parseLong(id));
      notice.setProperty("title", title);
      notice.setProperty("contents", contents);
      notice.setProperty("modifyDate", DateUtil.getCurrentDate());
      
      datastore.put(notice);
      
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
  
  /**
   * 공지사항를 삭제한다.
   * @param noticeId
   */
  public static void delete(String noticeId) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    
    try {
      Key noticeKey = KeyFactory.createKey("NOTICE", Long.parseLong(noticeId));
      datastore.delete(noticeKey);
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
}
