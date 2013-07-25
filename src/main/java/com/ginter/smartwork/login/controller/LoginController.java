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

package com.ginter.smartwork.login.controller;

import com.ginter.smartwork.common.json.JsonView;
import com.ginter.smartwork.front.member.service.MemberService;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
@Controller
public class LoginController {
  Logger log = Logger.getLogger(LoginController.class.getName());
  
  @RequestMapping(value = "/login")
  public void loginProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, EntityNotFoundException {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if(user == null) {
      response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
    } else {
      response.sendRedirect("smartwork");
    }
  }
  
  @RequestMapping(value = "/logout")
  public void logoutProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if(user != null) {
      response.sendRedirect(userService.createLogoutURL("/login"));
    }
  }
  
  @RequestMapping(value = "/main")
  public String main(HttpServletRequest request, HttpServletResponse response) throws IOException, EntityNotFoundException {
    UserService userService = UserServiceFactory.getUserService();
    String primaryEmail = userService.getCurrentUser().getEmail();
    HttpSession session  = request.getSession();
    
    // 동기화 대상 여부 확인
    if(MemberService.list().size() == 0) {
      //AdminService.syncMemberList();    // 동기화
    }
    
    try {
      Map<String, Object> member = MemberService.get(primaryEmail);        
      
      String familyName = (String)member.get("familyName");
      String givenName = (String)member.get("givenName");
      String fullName = familyName + givenName;
      
      session.setAttribute("primaryEmail", primaryEmail);
      session.setAttribute("fullName", fullName);
      session.setAttribute("photo", member.get("photo"));
    } catch(EntityNotFoundException enfe) {
     log.info("등록된 사용자가 아닙니다. (= " + primaryEmail + ")");
     response.sendRedirect(userService.createLogoutURL("/login"));
     //return "redirect:/login";
    }
    return "main";
  }
  
  @RequestMapping(value = "/profile")
  public ModelAndView getProfile(HttpServletRequest request, HttpServletResponse response) throws EntityNotFoundException {
    String primaryEmail = UserServiceFactory.getUserService().getCurrentUser().getEmail();
    Map<String, Object> profile = MemberService.get(primaryEmail);
    return JsonView.Render(profile, response);
  }
}
