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

package com.ginter.smartwork.common;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class SmartworkAuthorization extends AbstractAppEngineAuthorizationCodeServlet {
  private static final long serialVersionUID = 1L;
  
  /* (non-Javadoc)
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    resp.sendRedirect("/main");
  }

  /* (non-Javadoc)
   * @see com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet#initializeFlow()
   */
  @Override
  protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
    return Utils.initializeFlow();
  }

  /* (non-Javadoc)
   * @see com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet#getRedirectUri(javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
    return Utils.getRedirectUri(req);
  }

}
