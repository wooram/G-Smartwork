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

package com.ginter.smartwork.admin.service;

import com.ginter.smartwork.common.googleapis.GoogleDirectoryAPI;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class AdminService {
  /**
   * 사용자 목록을 디렉토리 목록과 동기화 시킨다.
   * @throws IOException
   */
  public static void syncMemberList() throws IOException {
    String domain = UserServiceFactory.getUserService().getCurrentUser().getEmail().split("@")[1];
    Users users = GoogleDirectoryAPI.getUserList().setDomain(domain).execute();
    
    for(User user : users.getUsers()) {
      Entity e = new Entity("MEMBER", user.getPrimaryEmail());
      
      e.setProperty("domain", user.getPrimaryEmail().split("@")[1]);
      e.setProperty("primaryEmail", user.getPrimaryEmail());      
      e.setProperty("familyName", user.getName().getFamilyName());
      e.setProperty("givenName", user.getName().getGivenName());
      e.setProperty("fullName", user.getName().getFullName());
      
      if(user.getOrganizations() != null && user.getOrganizations().size() > 0) {
        for(int i=0; i < user.getOrganizations().size(); i++) {
          if(user.getOrganizations().get(i).getPrimary()) {
            e.setProperty("department", user.getOrganizations().get(i).getDepartment());
            e.setProperty("title", user.getOrganizations().get(i).getTitle());
          }
        }
      }
      
      String photo = "https://lh5.googleusercontent.com/-ey81_ql_4Bw/AAAAAAAAAAI/AAAAAAAAAAA/5ud-pzf-C8E/s49-c/photo.jpg";
      if(user.getThumbnailPhotoUrl() != null) {
        photo = "http://www.google.com" + user.getThumbnailPhotoUrl();
      }
      e.setProperty("photo", photo);
      
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(e);
    }
  }
}
