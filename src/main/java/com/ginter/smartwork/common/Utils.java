/*
 * Copyright (c) 2013 Google Inc.
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
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AppEngineCredentialStore;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.tasks.TasksScopes;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class Utils {
  public static Logger log = Logger.getLogger(Utils.class.getName()); 
  
  public static final String RETURN_URL_PATH = "/main";

  public static String SCOPE = DirectoryScopes.ADMIN_DIRECTORY_ORGUNIT 
      + " " + DirectoryScopes.ADMIN_DIRECTORY_USER
      + " " + TasksScopes.TASKS
      + " " + DriveScopes.DRIVE
      + " " + CalendarScopes.CALENDAR
      + " " + "https://mail.google.com/"
      + " " + PlusScopes.PLUS_ME;
  
  public static final UrlFetchTransport HTTP_TRANSPORT = new UrlFetchTransport();
  public static final JacksonFactory JSON_FACTORY = new JacksonFactory();
  
  private static GoogleClientSecrets clientSecrets = null;
  
  public static GoogleClientSecrets getClientSecrets() throws IOException {
    if (clientSecrets == null) {
      clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
          new InputStreamReader(Utils.class.getResourceAsStream("/client_secrets.json")));
      
//      Preconditions.checkArgument(!clientSecrets.getDetails().getClientId().startsWith("Enter ")
//          && !clientSecrets.getDetails().getClientSecret().startsWith("Enter "),
//          "Download client_secrets.json file from https://code.google.com/apis/console/?api=calendar "
//          + "into calendar-appengine-sample/src/main/resources/client_secrets.json");
    }
    return clientSecrets;
  }

  public static String getRedirectUri(HttpServletRequest req) {
    GenericUrl requestUrl = new GenericUrl(req.getRequestURL().toString());
    requestUrl.setRawPath("/oauth2callback");
    return requestUrl.build();
  }
  
  public static GoogleAuthorizationCodeFlow initializeFlow() throws IOException {
    Set<String> SCOPES = Collections.singleton(SCOPE);
    return new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, getClientSecrets(), SCOPES).setCredentialStore(
        new AppEngineCredentialStore())
          .setAccessType("offline")
          .setApprovalPrompt("force")
          .build();
  }
  
  public static Credential getCredential() throws IOException {
    AuthorizationCodeFlow authFlow = initializeFlow();
    String userId = UserServiceFactory.getUserService().getCurrentUser().getUserId();
    Credential credential = authFlow.loadCredential(userId);
    return credential;
  }
  
}
