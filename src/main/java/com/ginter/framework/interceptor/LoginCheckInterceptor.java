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

package com.ginter.framework.interceptor;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter{
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    User user = UserServiceFactory.getUserService().getCurrentUser();
    if(user == null && !"/login".equalsIgnoreCase(request.getRequestURI())) {
      response.sendRedirect("/login");
      return false;
    } 
    return true;
  }
  
}
