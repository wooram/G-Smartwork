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

package com.ginter.smartwork.front.task.service;

import com.ginter.smartwork.common.googleapis.GoogleTasksAPI;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.model.Tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class TaskService {
  /**
   * 할일모음 목록을 가져온다.
   * @return
   * @throws IOException
   */
  public static List<Map<String, Object>> listsList() throws IOException {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    TaskLists tasklists = GoogleTasksAPI.getTasklistsList().execute();
    for(TaskList taskList : tasklists.getItems()) {
      Map<String, Object> hMap = new HashMap<String, Object>();
      hMap.put("groupId", taskList.getId());
      hMap.put("group", taskList.getTitle());
      resultList.add(hMap);
    }
    return resultList;
  }
  
  /**
   * 할일목록을 가져온다.
   * @param tasklistsId
   * @return
   * @throws IOException
   */
  public static List<Map<String, Object>> list(String tasklistsId) throws IOException {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    Tasks tasks = GoogleTasksAPI.getTaskOperList(tasklistsId).execute();
    for(Task task : tasks.getItems()) {
      Map<String, Object> hMap = new HashMap<String, Object>();
      hMap.put("taskId", task.getId());
      hMap.put("title", task.getTitle());
      if("completed".equals(task.getStatus())) {
        hMap.put("completed", true);
      } else {
        hMap.put("completed", false);
      }
      resultList.add(hMap);
    }
    return resultList;
  }
  
  public static List<Map<String, Object>> allList() throws IOException {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    TaskLists tasklists = GoogleTasksAPI.getTasklistsList().execute();
    
    for(TaskList taskList : tasklists.getItems()) {
      Tasks tasks = GoogleTasksAPI.getTaskOperList(taskList.getId()).execute();
      for(Task task : tasks.getItems()) {
        Map<String, Object> hMap = new HashMap<String, Object>();
        hMap.put("groupId", taskList.getId());
        hMap.put("group", taskList.getTitle());
        hMap.put("taskId", task.getId());
        hMap.put("title", task.getTitle());
        if("completed".equals(task.getStatus())) {
          hMap.put("completed", true);
        } else {
          hMap.put("completed", false);
        }
        
        resultList.add(hMap);
      }
    }
    return resultList;
  }
  
  /**
   * 할일을 등록한다.
   * @param tasklistsId
   * @throws IOException
   */
  public static void insert(String tasklistsId) throws IOException {
    GoogleTasksAPI.insertTask(tasklistsId, new Task());
  }

  
  /**
   * 할일을 수정한다.
   * @param tasklistsId
   * @param tasksId
   * @param params
   * @throws IOException
   */
  public static void update(String tasklistsId, String tasksId, Map<String, Object> params) throws IOException {
    String title = (String)params.get("title");     // 할일
    String completed = (String)params.get("completed");   // 할일 상태
    
    Task content = new Task();
    
    content.setTitle(title);
    if("true".equals(completed)) {
      completed = "completed";
    } else {
      completed = "needAction";
    }
    content.setStatus(completed);
    
    GoogleTasksAPI.updateTask(tasklistsId, tasksId, content);
  }
  
  /**
   * 할일을 삭제한다.
   * @param tasklistsId
   * @param tasksId
   * @throws IOException
   */
  public static void delete(String tasklistsId, String tasksId) throws IOException {
    GoogleTasksAPI.deleteTask(tasklistsId, tasksId);
  }
}
