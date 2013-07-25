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
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

import java.io.IOException;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class GoogleTasksAPI {
  /**
   * Tasks Builder 인스턴스를 가져온다.
   * @return
   * @throws IOException
   */
  private static Tasks getTasks() throws IOException {
    // Build the Plus object using the credentials
    Tasks tasks = new Tasks.Builder(
        Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, Utils.getCredential()).build();
    return tasks;
  }
  
  /**
   * 할일 목록을 가져온다.
   * @return
   * @throws IOException
   */
  public static Tasks.Tasklists.List getTasklistsList() throws IOException {
    return getTasks().tasklists().list();
  }
  
  /**
   * 할일을 가져온다.
   * @param tasklistId
   * @return
   * @throws IOException
   */
  public static Tasks.TasksOperations.List getTaskOperList(String tasklistId) throws IOException {
    return getTasks().tasks().list(tasklistId);
  }
  
  /**
   * 할일을 등록한다.
   * @param tasklistsId
   * @param content
   * @throws IOException
   */
  public static void insertTask(String tasklistsId, Task content) throws IOException {
    getTasks().tasks().insert(tasklistsId, content);
  }
  
  /**
   * 할일을 수정한다.
   * @param tasklistsId
   * @param taskId
   * @param content
   * @throws IOException
   */
  public static void updateTask(String tasklistsId, String tasksId, Task content) throws IOException {
    getTasks().tasks().patch(tasklistsId, tasksId, content);
  }
  
  /**
   * 할일을 삭제한다.
   * @param tasklist
   * @param task
   * @throws IOException
   */
  public static void deleteTask(String tasklistsId, String tasksId) throws IOException {
    getTasks().tasks().delete(tasklistsId, tasksId);
  }
}
