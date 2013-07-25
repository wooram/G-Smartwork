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

package com.ginter.smartwork.common.googleapis;

import com.ginter.smartwork.common.Utils;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.Directory.Users.Delete;
import com.google.api.services.admin.directory.Directory.Users.Insert;
import com.google.api.services.admin.directory.Directory.Users.Update;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.UserName;
import com.google.api.services.admin.directory.model.UserOrganization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class GoogleDirectoryAPI {
  /**
   * Directory Builder 인스턴스를 가져온다.
   * @return
   * @throws IOException
   */
  private static Directory getDirectory() throws IOException {
    // Build the Plus object using the credentials
    Directory directory = new Directory.Builder(
        Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, Utils.getCredential()).build();
    return directory;
  }
  
  /**
   * 조직리스트를 가져온다.
   * @param customId
   * @return
   * @throws IOException
   */
  public static Directory.Orgunits.List getOrgList(String customId) throws IOException {
    return getDirectory().orgunits().list(customId);
  }
  
  /**
   * 도메인에 있는 전체 사용자 리스트를 가져온다.
   * @return
   */
  public static Directory.Users.List getUserList() throws IOException {
    return getDirectory().users().list();
  }
  
  /**
   * 도메인에 사용자를 추가한다.
   * @return
   * @throws IOException
   */
  public static Insert insertUser(Map<String, Object> params) throws IOException {
    String primaryEmail = (String)params.get("primaryEmail");
    String password = (String)params.get("password");
    String familName = (String)params.get("familyName");
    String givenName = (String)params.get("givenName");
    String title = (String)params.get("title");
    String department = (String)params.get("department");
    
    User content = new User();
    
    content.setPrimaryEmail(primaryEmail);   // 이메일    
    content.setPassword(password);           // 비밀번호
    
    // 사용자 이름
    UserName userName = new UserName();
    userName.setFamilyName(familName);  // 성
    userName.setGivenName(givenName);   // 이름
    content.setName(userName);
    
    // 사용자조직정보 (부서, 직급)
    UserOrganization userOrg = new UserOrganization();
    List<UserOrganization> userOrgList = new ArrayList<UserOrganization>();
    userOrg.setDepartment(title);   // 부서
    userOrg.setTitle(department);   // 직급
    userOrgList.add(userOrg);
    content.setOrganizations(userOrgList);
    
    return getDirectory().users().insert(content);    
  }
  
  /**
   * 사용자 정보를 수정한다.
   * @param params
   * @return
   * @throws IOException
   */
  public static Update updateUser(Map<String, Object> params) throws IOException {
    String primaryEmail = (String)params.get("primaryEmail");
    String password = (String)params.get("password");
    String familName = (String)params.get("familyName");
    String givenName = (String)params.get("givenName");
    String title = (String)params.get("title");
    String department = (String)params.get("department");
    
    User content = new User();
    
    content.setPassword(password);           // 비밀번호
    
    // 사용자 이름
    UserName userName = new UserName();
    userName.setFamilyName(familName);  // 성
    userName.setGivenName(givenName);   // 이름
    content.setName(userName);
    
    // 사용자조직정보 (부서, 직급)
    UserOrganization userOrg = new UserOrganization();
    List<UserOrganization> userOrgList = new ArrayList<UserOrganization>();
    userOrg.setDepartment(title);   // 부서
    userOrg.setTitle(department);   // 직급
    userOrgList.add(userOrg);
    content.setOrganizations(userOrgList);
    
    return getDirectory().users().update(primaryEmail, content);
  }
  
  /**
   * 도메인에 사용자를 삭제한다.
   * @param primaryEmail
   * @throws IOException
   */
  public static Delete deleteUser(String primaryEmail) throws IOException {
    return getDirectory().users().delete(primaryEmail);
  }
}
