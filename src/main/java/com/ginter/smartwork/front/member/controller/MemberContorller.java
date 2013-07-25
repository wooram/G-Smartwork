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

package com.ginter.smartwork.front.member.controller;

import com.ginter.smartwork.common.json.JsonView;
import com.ginter.smartwork.front.member.service.MemberService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.UserServiceFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
@Controller
public class MemberContorller {
  
  /**
   * 사용자 목록을 보여준다.
   * @param response
   * @return
   */
  @RequestMapping(value = "/member", method = RequestMethod.GET)
  public ModelAndView list(HttpServletResponse response) {
    List<Map<String, Object>> resultList = MemberService.list();
    return JsonView.Render(resultList, response);
  }
  
  /**
   * 사용자 목록을 보여준다.
   * @param response
   * @return
   */
  @RequestMapping(value = "/search/member", method = RequestMethod.GET)
  public ModelAndView search(HttpServletRequest request, HttpServletResponse response) {
    String searchWork = request.getParameter("searchWord");
    List<Map<String, Object>> resultList = MemberService.search(searchWork);
    return JsonView.Render(resultList, response);
  }
  
  /**
   * 특정 사용자 정보를 보여준다.
   * @param primaryEmail
   * @param response
   * @return
   * @throws EntityNotFoundException
   */
  @RequestMapping(value = "/member/{primaryEmail}", method = RequestMethod.GET)
  public ModelAndView get(@PathVariable String primaryEmail, HttpServletResponse response) throws EntityNotFoundException {
    return JsonView.Render(MemberService.get(primaryEmail), response) ;
  }
  
  @RequestMapping(value = "/member/register")
  public String regiterView() {
    return "adminMemberRegister";
  }
  
  /**
   * 사용자를 등록한다.
   * @param primaryEmail
   * @param response
   * @param request
   * @throws IOException
   */
  @RequestMapping(value = "/member/{primaryEmail}", method = RequestMethod.POST)  
  public void insert(@PathVariable String primaryEmail, HttpServletResponse response, HttpServletRequest request) throws IOException {
    String password = request.getParameter("password");
    String familyName = request.getParameter("familyName");
    String givenName = request.getParameter("givenName");
    String department = request.getParameter("department");
    String title = request.getParameter("title");
    
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1];
    domain = domain == "example.com" ? "g-interface.com"  : domain;
    
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("domain", domain);
    params.put("primaryEmail", primaryEmail);
    params.put("password", password);
    params.put("familyName", familyName);
    params.put("givenName", givenName);
    params.put("department", department);
    params.put("title", title);
    
    MemberService.insert(params);
  }
  
  /**
   * 사용자 정보를 수정한다.
   * @param primaryEmail
   * @param response
   * @param request
   * @throws IOException
   */
  @RequestMapping(value = "/member/{primaryEmail}", method = RequestMethod.POST)
  public void update(@PathVariable String primaryEmail, HttpServletResponse response, HttpServletRequest request) 
      throws IOException {
    String password = request.getParameter("password");
    String familyName = request.getParameter("familyName");
    String givenName = request.getParameter("givenName");
    String department = request.getParameter("department");
    String title = request.getParameter("title");
    
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("primaryEmail", primaryEmail);
    params.put("password", password);
    params.put("familyName", familyName);
    params.put("givenName", givenName);
    params.put("department", department);
    params.put("title", title);
    
    MemberService.update(params);
  }
  
  /**
   * 사용자를 삭제한다.
   * @param primaryEmail
   * @throws IOException
   */
  @RequestMapping(value = "/member/{primaryEmail}", method = RequestMethod.DELETE)
  public void delete(@PathVariable("primaryEmail") String primaryEmail) throws IOException {
    MemberService.delete(primaryEmail);    
  }
  
  /**
   * Google+정보와사용자정보를 동기화 한다.
   * @param primaryEmail
   * @throws IOException
   */
  @RequestMapping(value = "/member/me/sync")
  public void sync() throws IOException, EntityNotFoundException {
    MemberService.sync();
  }
  
  @RequestMapping(value = "/test/member/insert")
  public void testInsert() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    Entity user = new Entity("MEMBER", "hwr@inlab.co.kr");
    
    user.setProperty("domain", "inlab.co.kr");
    user.setProperty("primaryEmail", "hwr@inlab.co.kr");
    user.setProperty("familyName", "한");
    user.setProperty("givenName", "우람");
    user.setProperty("fullName", "한우람");
    user.setProperty("department", "");
    user.setProperty("title", "");
    
    datastore.put(user);
  }
}
