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

package com.ginter.smartwork.front.task.controller;

import com.ginter.smartwork.common.json.JsonView;
import com.ginter.smartwork.front.task.service.TaskService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */

@Controller
public class TaskController {
  
  @RequestMapping(value = "/tasklists", method = RequestMethod.GET)
  public ModelAndView listsList(HttpServletResponse response) throws IOException {
    return JsonView.Render(TaskService.listsList(), response);
  }
  
  @RequestMapping(value = "/tasklists/{tasklistsId}/tasks", method = RequestMethod.GET)
  public ModelAndView list(@PathVariable String tasklistsId, HttpServletResponse response) throws IOException {
    return JsonView.Render(TaskService.list(tasklistsId), response);
  }
  
  @RequestMapping(value = "/tasklists/tasks", method = RequestMethod.GET)
  public ModelAndView allList(HttpServletResponse response) throws IOException {
    return JsonView.Render(TaskService.allList(), response);
  }
  
  @RequestMapping(value = "/tasklists/{tasklistsId}/tasks", method = RequestMethod.PUT)
  public void insert(@PathVariable String tasklistsId, @PathVariable String tasksId, HttpServletRequest request) throws IOException {
    TaskService.insert(tasklistsId);
  }
  
  @RequestMapping(value = "/tasklists/{tasklistsId}/tasks/{tasksId}", method = RequestMethod.PATCH)
  public void update(@PathVariable String tasklistsId, @PathVariable String tasksId, HttpServletRequest request) throws IOException {
    String title = request.getParameter("title");
    String completed = request.getParameter("completed");
    
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("title", title);
    params.put("completed", completed);
    
    TaskService.update(tasklistsId, tasksId, params);
  }
  
  @RequestMapping(value = "/tasklists/{tasklistsId}/tasks/{tasksId}", method = RequestMethod.DELETE)
  public void delete(@PathVariable String tasklistsId, @PathVariable String tasksId) throws IOException {
    TaskService.delete(tasklistsId, tasksId);
  }
}
