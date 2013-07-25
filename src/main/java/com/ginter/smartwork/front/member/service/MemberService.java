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

package com.ginter.smartwork.front.member.service;

import com.ginter.smartwork.common.googleapis.GoogleDirectoryAPI;
import com.ginter.smartwork.common.googleapis.GooglePlusAPI;
import com.google.api.services.plus.model.Person;
import com.google.api.services.plus.model.Person.Organizations;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class MemberService {
  /**
   * 멤버 목록을 가져온다.
   * @return
   */
  public static List<Map<String, Object>> list() {
    List<Map<String, Object>> resultList = search("");
    return resultList;
  }
  
  /**
   * 멤버 검색 목록을 가져온다.
   * @return
   */
  public static List<Map<String, Object>> search(String searchWord) {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1];
    
    Filter defaultFilter = new FilterPredicate("domain", FilterOperator.EQUAL, domain);
    Filter emailFilter = new FilterPredicate("primaryEmail", FilterOperator.EQUAL, searchWord);
    Filter familyNameFilter = new FilterPredicate("familyName", FilterOperator.EQUAL, searchWord);
    Filter givenNameFilter = new FilterPredicate("givenName", FilterOperator.EQUAL, searchWord);
    Filter fullNameFilter = new FilterPredicate("fullName", FilterOperator.EQUAL, searchWord);
    Filter deptFilter = new FilterPredicate("department", FilterOperator.EQUAL, searchWord);
    Filter titleFilter = new FilterPredicate("title", FilterOperator.EQUAL, searchWord);
    
    Filter resultFilter = null;
    if("".equals(searchWord)) {
      resultFilter = defaultFilter;
    } else {
      Filter allFilter = CompositeFilterOperator.or(emailFilter, familyNameFilter, givenNameFilter, fullNameFilter, deptFilter, titleFilter);
      resultFilter = CompositeFilterOperator.and(defaultFilter, allFilter); 
    }
    
    List<Entity> userList = datastore.prepare(new Query("MEMBER").setFilter(resultFilter)).asList(FetchOptions.Builder.withDefaults());
    for(Entity e : userList) {
      
      HashMap<String, Object> hMap = new HashMap<String, Object>();
      hMap.put("primaryEmail", e.getProperty("primaryEmail"));
      hMap.put("familyName", e.getProperty("familyName"));
      hMap.put("givenName", e.getProperty("givenName"));
      hMap.put("fullName", e.getProperty("fullName"));
      hMap.put("department", e.getProperty("department"));
      hMap.put("title", e.getProperty("title"));
      hMap.put("photo", e.getProperty("photo"));
      
      resultList.add(hMap);
    }
    return resultList;
  }
  
  /**
   * 선택된 사용자 정보를 가져온다.
   * @param primaryEmail
   * @return
   * @throws EntityNotFoundException
   */
  public static Map<String, Object> get(String primaryEmail) throws EntityNotFoundException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Key memberKey = KeyFactory.createKey("MEMBER", primaryEmail);
    Entity e = datastore.get(memberKey);

    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("domain", e.getProperty("domain"));
    resultMap.put("primaryEmail", e.getProperty("primaryEmail"));
    resultMap.put("familyName", e.getProperty("familyName"));
    resultMap.put("givenName", e.getProperty("givenName"));
    resultMap.put("fullName", e.getProperty("fullName"));
    resultMap.put("department", e.getProperty("department"));
    resultMap.put("title", e.getProperty("title"));
    resultMap.put("photo", e.getProperty("photo"));
    
    return resultMap;    
  }
  
  /**
   * 멤버를 등록한다.
   * @param params
   */
  public static void insert(Map<String, Object> params) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    
    try {
    
      Entity user = new Entity("MEMBER", (String)params.get("primaryEmail"));
      
      user.setProperty("domain", params.get("domain"));
      user.setProperty("primaryEmail", params.get("primaryEmail"));
      user.setProperty("familyName", params.get("familyName"));
      user.setProperty("givenName", params.get("givenName"));
      user.setProperty("fullName", params.get("familyName") + (String)params.get("givenName"));
      user.setProperty("department", params.get("department"));
      user.setProperty("title", params.get("title"));
      
      datastore.put(user);
      
      GoogleDirectoryAPI.insertUser(params).execute();
      
      txn.commit();
      
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
    
  }
  
  /**
   * 멤버정보를 수정한다.
   * @param params
   */
  public static void update(Map<String, Object> params) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();

    try {
      Entity user = new Entity("MEMBER", (String)params.get("primaryEmail"));
      
      user.setProperty("domain", params.get("domain"));
      user.setProperty("primaryEmail", params.get("primaryEmail"));
      user.setProperty("familyName", params.get("familyName"));
      user.setProperty("givenName", params.get("givenName"));
      user.setProperty("fullName", params.get("familyName") + (String)params.get("givenName"));
      user.setProperty("department", params.get("department"));
      user.setProperty("title", params.get("title"));
      
      datastore.put(user);
      
      GoogleDirectoryAPI.updateUser(params);
      
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
  
  /**
   * 선택한 멤버를 삭제한다.
   * @param primaryEmail
   */
  public static void delete(String primaryEmail) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = datastore.beginTransaction();
    
    try {
      Key memberKey = KeyFactory.createKey("MEMBER", primaryEmail);
      datastore.delete(memberKey);
      GoogleDirectoryAPI.deleteUser(primaryEmail).execute();
      
      txn.commit();
    } finally {
      if(txn.isActive()) {
        txn.rollback();
      }
    }
  }
  
  /**
   * Google+에서 사용자 정보를 가져온다.
   * @return
   * @throws IOException
   */
  public static Map<String, Object> getMemberFromPlus() throws IOException {
    Person person = GooglePlusAPI.getPeple().execute();
    
    Map<String, Object> resultMap = new HashMap<String, Object>();
    
//    resultMap.put("familyName", person.getName().getFamilyName());
//    resultMap.put("givenName", person.getName().getGivenName());
    
    if(person.getOrganizations() != null && person.getOrganizations().size() > 0) {
      for(Organizations org : person.getOrganizations()) {
        if(org.getPrimary()) {
          resultMap.put("department", org.getDepartment());
          resultMap.put("title", org.getTitle());
          break;
        }
      }
    }
    resultMap.put("photo", person.getImage().getUrl());
    resultMap.put("birthday", person.getBirthday());
    
    return resultMap;
  }
  
  /**
   * Google+정보와사용자정보를 동기화 한다.
   * @param params
   */
  public static void sync() throws IOException, EntityNotFoundException {
    String primaryEmail = UserServiceFactory.getUserService().getCurrentUser().getEmail();
    
    Map<String, Object> member = getMemberFromPlus();
    
    String domain = primaryEmail.split("@")[1];
//    String familyName = (String)member.get("familyName");
//    String givenName = (String)member.get("givenName");
//    String fullName = familyName + givenName;
    String department = (String)member.get("department");
    String title = (String)member.get("title");
    String photo = (String)member.get("photo");
    String birthday = (String)member.get("birthday");
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key memberKey = KeyFactory.createKey("MEMBER", primaryEmail);
    Entity curMember = datastore.get(memberKey);
    
    Entity result = new Entity("MEMBER", primaryEmail);
    
    result.setProperty("domain", domain);
    result.setProperty("primaryEmail", primaryEmail);
    
    // 이름은 동기화 대상에서 제외 (저장되어있던 이름을 다시 저장..)
    result.setProperty("familyName", curMember.getProperty("familyName"));   
    result.setProperty("givenName", curMember.getProperty("givenName"));
    result.setProperty("fullName", curMember.getProperty("fullName"));
    
    result.setProperty("department", department);
    result.setProperty("title", title);
    result.setProperty("photo", photo);
    result.setProperty("birthday", birthday);
    
    datastore.put(result);
  }
}
