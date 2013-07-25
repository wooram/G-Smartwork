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

package com.ginter.smartwork.front.community.contorller;

import com.ginter.smartwork.common.json.JsonView;
import com.ginter.smartwork.front.community.service.CommunityService;
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

/**
 * @author silupke@google.com (Your Name Here)
 *
 */

@Controller
public class CommnuityController {
  @RequestMapping(value = "/community", method = RequestMethod.GET)
  public ModelAndView list(HttpServletResponse response) {
    List<Map<String, Object>> resultList = CommunityService.list();
    return JsonView.Render(resultList, response);
  }
  
  @RequestMapping(value = "/community/{communityId}", method = RequestMethod.GET)
  public ModelAndView get(@PathVariable String communityId,  HttpServletResponse response) throws EntityNotFoundException {
    Map<String, Object> resultMap = CommunityService.get(communityId);
    return JsonView.Render(resultMap, response);
  }
  
  @RequestMapping(value = "/community", method = RequestMethod.POST)
  public String insert(HttpServletRequest request) {
    String url = request.getParameter("url");
    String name = request.getParameter("name");
    String imageUrl = request.getParameter("imageUrl");
    
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("url", url);
    params.put("name", name);
    params.put("imageUrl", imageUrl);
    
    CommunityService.insert(params);
    
    return "";
  }
  
  @RequestMapping(value = "/community/{communityId}", method = RequestMethod.PATCH)
  public String update(@PathVariable String communityId,  HttpServletRequest request) {
    String url = request.getParameter("url");
    String name = request.getParameter("name");
    String imageUrl = request.getParameter("imageUrl");
    
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", communityId);
    params.put("url", url);
    params.put("name", name);
    params.put("imageUrl", imageUrl);
    
    CommunityService.update(params);
    
    return "";
  }
  
  @RequestMapping(value = "/community/{communityId}", method = RequestMethod.DELETE)
  public String delete(@PathVariable String communityId, HttpServletRequest request) {
    CommunityService.delete(communityId);
    return "";
  }
}
