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

package com.ginter.smartwork.front.notice.contorller;

import com.ginter.smartwork.common.json.JsonView;
import com.ginter.smartwork.front.notice.service.NoticeService;
import com.google.appengine.api.datastore.EntityNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */

@Controller
public class NoticeController {
  @RequestMapping(value = "/notice", method = RequestMethod.GET)
  public ModelAndView list(HttpServletResponse response) {
    List<Map<String, Object>> resultList = NoticeService.list();
    return JsonView.Render(resultList, response);
  }
  
//  @RequestMapping(value = "/notice/{noticeId}", method = RequestMethod.GET)
  public ModelAndView get(@PathVariable String noticeId,  HttpServletResponse response) throws EntityNotFoundException {
    Map<String, Object> resultMap = NoticeService.get(noticeId);
    return JsonView.Render(resultMap, response);
  }
  
  @RequestMapping(value = "/notice", method = RequestMethod.POST)
  public String insert(HttpServletRequest request) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    
    HttpSession session = request.getSession();
    
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("title", title);
    params.put("contents", contents);
    params.put("writer", session.getAttribute("fullName"));
    
    NoticeService.insert(params);
    
    return "";
  }
  
  @RequestMapping(value = "/notice/{noticeId}", method = RequestMethod.PATCH)
  public String update(@PathVariable String noticeId,  HttpServletRequest request) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", noticeId);
    params.put("title", title);
    params.put("contents", contents);
    
    NoticeService.update(params);
    
    return "";
  }
  
  @RequestMapping(value = "/notice/{noticeId}", method = RequestMethod.DELETE)
  public String delete(@PathVariable String noticeId, HttpServletRequest request) {
    NoticeService.delete(noticeId);
    return "";
  }
}
