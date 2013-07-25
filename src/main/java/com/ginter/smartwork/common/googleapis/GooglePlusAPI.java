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
import com.google.api.services.plus.Plus;

import java.io.IOException;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class GooglePlusAPI {
  private static Plus getPlus() throws IOException {
    // Build the Plus object using the credentials
    Plus plus = new Plus.Builder(
        Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, Utils.getCredential()).build();
    return plus;
  }
  
  public static Plus.People.Get getPeple() throws IOException {
    return getPlus().people().get("me");
  }
}
