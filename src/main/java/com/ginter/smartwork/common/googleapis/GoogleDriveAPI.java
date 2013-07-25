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
import com.google.api.services.drive.Drive;

import java.io.IOException;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class GoogleDriveAPI {
  /**
   * Drive Builder 인스턴스를 가져온다.
   * @return
   * @throws IOException
   */
  private static Drive getDrive() throws IOException {
    // Build the Plus object using the credentials
    Drive drive = new Drive.Builder(
        Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, Utils.getCredential()).build();
    return drive;
  }
  
  /**
   * 드라이브에 있는 전체폴더 및 파일을 가져온다.
   * @return
   * @throws IOException
   */
  public static Drive.Files.List getDriveFilesList() throws IOException {
    return getDrive().files().list();
  }
  
  public static Drive.Files.Get getDriveFile(String fileId) throws IOException {
    return getDrive().files().get(fileId);
  }
  
  public static Drive.Parents.List getDriveParentsList(String fileId) throws IOException {
    return getDrive().parents().list(fileId);
  }
}
