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

package com.ginter.smartwork.admin.controller;

import com.ginter.smartwork.admin.service.AdminService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
@Controller
public class AdminContorller {
  /**
   * 사용자 목록을 디렉토리 목록과 동기화 시킨다.
   * @throws IOException
   */
  @RequestMapping(value = "/admin/user/sync", method = RequestMethod.GET)
  public String syncMemberList() throws IOException {
    AdminService.syncMemberList();
    return "redirect:/main";
  } 
  
}
