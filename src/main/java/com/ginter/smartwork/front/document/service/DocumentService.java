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

package com.ginter.smartwork.front.document.service;

import com.ginter.smartwork.common.googleapis.GoogleDriveAPI;
import com.ginter.smartwork.common.util.CommonUtil;
import com.ginter.smartwork.common.util.DateUtil;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */

public class DocumentService {
  private static Logger log  = Logger.getLogger(DocumentService.class.getName());
  
  private static final int MAX_RESULT = 30;
  
  /**
   * 문서파일을 가져온다.
   * @return
   * @throws Exception
   */
  public static List<Map<String, Object>> documentList() throws Exception {
    String query = "mimeType contains 'application/vnd.'"        
        + "and not mimeType contains 'application/vnd.google-apps.folder'";
    
    List<File> fileList = new ArrayList<File>();
    Files.List request = GoogleDriveAPI.getDriveFilesList();
    
    // default로 문서수정일 내림차순을 가져온다.
    do {
      FileList tempFileList = request.setQ(query).setMaxResults(1000).execute();
      fileList.addAll(tempFileList.getItems());
      request.setPageToken(tempFileList.getNextPageToken());
    } while (request.getPageToken() != null &&
             request.getPageToken().length() > 0);
    
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    for(File file : fileList) {
      if(file.getLabels().getTrashed()) {
        continue;
      }
      Map<String, Object> hMap = new HashMap<String, Object>();
      hMap.put("iconLink", file.getIconLink());
      hMap.put("alternateLink", file.getAlternateLink());
      hMap.put("title", file.getTitle());
      hMap.put("modifiedDate", DateUtil.getDate(file.getModifiedDate().getValue()));
      hMap.put("shared", file.getShared());
      hMap.put("starred", file.getLabels().getStarred());
      if(file.getOwnerNames().size() > 0) {
        hMap.put("ownerNames", file.getOwnerNames().get(0));
      } else {
        hMap.put("ownerNames", "");
      }
      if(file.getParents().size() > 0) {
        hMap.put("parentsId", file.getParents().get(0).getId());
      } else {
        hMap.put("parentsId", "");
      }
      if(file.getLastViewedByMeDate() != null) {
        hMap.put("lastViewedByMeDate", file.getLastViewedByMeDate().getValue());
      } else {
        hMap.put("lastViewedByMeDate", 0L);
      }
      resultList.add(hMap);
    }
    
    return resultList;
  }
  
  /**
   * 전체 폴더를 가져온다.
   * @return
   * @throws Exception
   */
  public static List<Map<String, Object>> folderList() throws Exception {
    String query = "mimeType = 'application/vnd.google-apps.folder'";
    
    List<File> fileList = new ArrayList<File>();
    Files.List request = GoogleDriveAPI.getDriveFilesList();
    
    do {        
      FileList tempFileList = request.setQ(query).setMaxResults(1000).execute();
      
      fileList.addAll(tempFileList.getItems());
      request.setPageToken(tempFileList.getNextPageToken());
    } while (request.getPageToken() != null &&
             request.getPageToken().length() > 0);
    
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    for(File file : fileList) {
      Map<String, Object> hMap = new HashMap<String, Object>();
      hMap.put("id", file.getId());
      hMap.put("iconLink", file.getIconLink());
      hMap.put("alternateLink", file.getAlternateLink());
      hMap.put("title", file.getTitle());
      hMap.put("modifiedDate", file.getModifiedDate());
      if(file.getOwnerNames().size() > 0) {
        hMap.put("ownerNames", file.getOwnerNames().get(0));
      } else {
        hMap.put("ownerNames", "");
      }
      hMap.put("shared", file.getShared());
      resultList.add(hMap);
    }
    
    return resultList;
  }
  
  /**
   * 최근 문서(사용자가 최근에 열어본 문서)를 가져온다.  
   * @return
   * @throws Exception
   */
  public static List<Map<String, Object>> recentList() throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

    List<Map<String, Object>> documentList = documentList();
    List<Map<String, Object>> folderList = folderList();
    
    if(documentList != null && documentList.size() > 0) {
      for(int i=0; i < documentList.size(); i++) {
        Map<String, Object> documentMap = documentList.get(i);
        
        documentMap.put("parentsTitle", "");
        
        if(folderList != null && folderList.size() > 0) {
          for(int j=0; j < folderList.size(); j++) {
            Map<String, Object> folderMap = folderList.get(j);
            
            if(documentMap.get("parentsId") != null) {
              if(documentMap.get("parentsId").equals(folderMap.get("id"))) {
                documentMap.put("parentsTitle", folderMap.get("title"));    // 폴더 이름
                break;
              } 
            }
          }
        }
        
        resultList.add(documentMap);
      }
      
      CommonUtil.sortDescMapList(resultList, "lastViewedByMeDate");
      if(resultList.size() > MAX_RESULT) {
        resultList.subList(0, MAX_RESULT);
      }
    }
    return resultList;
  }
  
  /**
   * 중요문서를 가져온다.
   * @return
   * @throws Exception
   */
  public static List<Map<String, Object>> importantList() throws Exception {
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

    List<Map<String, Object>> documentList = documentList();
    List<Map<String, Object>> folderList = folderList();
    
    if(documentList != null && documentList.size() > 0) {
      for(int i=0; i < documentList.size(); i++) {
        Map<String, Object> documentMap = documentList.get(i);
        if("false".equals(documentMap.get("starred").toString())) {
          continue;
        }
        documentMap.put("parentsTitle", "");
        
        if(folderList != null && folderList.size() > 0) {
          for(int j=0; j < folderList.size(); j++) {
            Map<String, Object> folderMap = folderList.get(j);
            
            if(documentMap.get("parentsId") != null) {
              if(documentMap.get("parentsId").equals(folderMap.get("id"))) {
                documentMap.put("parentsTitle", folderMap.get("title"));    // 폴더 이름
                break;
              } 
            }
          }
        }
        resultList.add(documentMap);
      }
      
      CommonUtil.sortAscMapList(resultList, "lastViewedByMeDate");
      if(resultList.size() > MAX_RESULT) {
        resultList.subList(0, MAX_RESULT);
      }
    }
    return resultList;
  }
}
