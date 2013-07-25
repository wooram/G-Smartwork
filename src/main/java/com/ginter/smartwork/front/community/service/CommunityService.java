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

package com.ginter.smartwork.front.community.service;

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
public class CommunityService {
  /**
   * 등록된 커뮤니티 목록을 보여준다.
   * @return
   */
  public static List<Map<String, Object>> list() {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1];
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Filter domainFilter = new FilterPredicate("domain", FilterOperator.EQUAL, domain);
    List<Entity> communityList = datastore.prepare(new Query("COMMUNITY").setFilter(domainFilter)).asList(FetchOptions.Builder.withDefaults());
    
    for(Entity e : communityList) {
      Map<String, Object> resultMap = new HashMap<String, Object>();
      resultMap.put("id", e.getKey().getId());
      resultMap.put("url", e.getProperty("url"));
      resultMap.put("name", e.getProperty("name"));
      resultMap.put("imageUrl", e.getProperty("imageUrl"));
      
      resultList.add(resultMap);
    }
    return resultList;
  }
  
  /**
   * 등록된 커뮤니티 목록을 보여준다.
   * @return
   */
  public static Map<String, Object> get(String id) throws EntityNotFoundException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Key communityKey = KeyFactory.createKey("COMMUNITY", Long.parseLong(id));
    Entity e = datastore.get(communityKey);

    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("url", e.getProperty("url"));
    resultMap.put("name", e.getProperty("name"));
    resultMap.put("imageUrl", e.getProperty("imageUrl"));
    
    return resultMap;  
  }
  
  /**
   * 커뮤니티를 등록한다.
   * @param params
   */
  public static void insert(Map<String, Object> params) {
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1];
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    try {
      String url = (String)params.get("url");
      String name = (String)params.get("name");
      String imageUrl = (String)params.get("imageUrl");
      
      Entity community = new Entity("COMMUNITY");
      community.setProperty("domain", domain);
      community.setProperty("url", url);
      community.setProperty("name", name);
      community.setProperty("imageUrl", imageUrl);
      
      datastore.put(community);
      
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
  
  /**
   * 커뮤니티를 수정한다.
   * @param params
   */
  public static void update(Map<String, Object> params) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    try {
      String id = (String)params.get("id");
      String url = (String)params.get("url");
      String name = (String)params.get("name");
      String imageUrl = (String)params.get("imageUrl");
      
      Entity community = new Entity("COMMUNITY", Long.parseLong(id));
      community.setProperty("url", url);
      community.setProperty("name", name);
      community.setProperty("imageUrl", imageUrl);
      
      datastore.put(community);
      
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
  
  /**
   * 커뮤니티를 삭제한다.
   * @param communityId
   */
  public static void delete(String communityId) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    
    try {
      Key communityKey = KeyFactory.createKey("COMMUNITY", Long.parseLong(communityId));
      datastore.delete(communityKey);
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
}
