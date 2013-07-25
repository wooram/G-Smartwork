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

package com.ginter.smartwork.front.mail.service;

import com.ginter.smartwork.common.Utils;
import com.ginter.smartwork.common.util.CommonUtil;
import com.ginter.smartwork.common.util.DateUtil;
import com.ginter.smartwork.common.util.StringUtil;
import com.ginter.smartwork.front.mail.OAuth2Authenticator;
import com.google.api.client.auth.oauth2.Credential;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.code.com.sun.mail.imap.IMAPFolder.FetchProfileItem;
import com.google.code.com.sun.mail.imap.IMAPMessage;
import com.google.code.com.sun.mail.imap.IMAPStore;
import com.google.code.javax.mail.FetchProfile;
import com.google.code.javax.mail.Flags.Flag;
import com.google.code.javax.mail.Folder;
import com.google.code.javax.mail.Message;
import com.google.code.javax.mail.internet.InternetAddress;
import com.google.code.javax.mail.internet.MimeUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author silupke@google.com (Your Name Here)
 *
 */
public class EmailService {
  private final static Logger logger = Logger.getLogger(EmailService.class.getName());
  
  /**
   * ImapStore을 가져온다.
   * @param email
   * @return
   * @throws Exception
   */
  public static IMAPStore getImapStore(String email) throws Exception {
    Credential credential = Utils.getCredential();
    OAuth2Authenticator.initialize();    
    IMAPStore imapStore = OAuth2Authenticator.connectToImap("imap.gmail.com", 993, email, credential.getAccessToken(), true);
    return imapStore;
  }
  
  public static List<Map<String, Object>> list() throws Exception {
    final int MAX_RESULT = 30;
    
    String userEmail = UserServiceFactory.getUserService().getCurrentUser().getEmail();
    
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    
    // 메일
    IMAPStore store = getImapStore(userEmail);
    Folder folder = store.getFolder("inbox");
    folder.open(Folder.READ_ONLY);
    
//    Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
//    Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.), true), folder.getMessages(folder.getMessageCount() - 20, folder.getMessageCount()));
    
    FetchProfile fetchProfile = new FetchProfile();
    fetchProfile.add(FetchProfile.Item.ENVELOPE);   
    fetchProfile.add(FetchProfile.Item.FLAGS);
    fetchProfile.add(FetchProfileItem.X_GM_THRID);
//    fetchProfile.add(FetchProfile.Item.CONTENT_INFO);
    
    int start = folder.getMessageCount() - MAX_RESULT;  // start UID
    int end = folder.getMessageCount();                 // end UID
    
    if(start < 0) {
      start = 0;
    }
    
    Message[] messages = folder.getMessages(start, end);    // start UID ~ end UID 범위이 메일을 가져온다.
    folder.fetch(messages, fetchProfile);
    
    for(Message message : messages) {
      Map<String, Object> hMap = new HashMap<String, Object>();
      
      hMap.put("subject", StringUtil.nvl2(message.getSubject(), "(제목 없음)") );
      
      String fromEmail = ((InternetAddress)message.getFrom()[0]).getAddress();
      String fromPersonal = MimeUtility.decodeText(InternetAddress.toString(message.getFrom()));
      if(fromEmail.equals(userEmail)) {
        fromPersonal = "나 <" + fromEmail + ">";
      }
      
      // G-mail Thread ID 가져오기
      IMAPMessage imapMsg = (IMAPMessage)message;
      long thrId = imapMsg.getGoogleMessageThreadId();
      String thrIdHexStr = Long.toHexString(thrId);
      
      hMap.put("link", "https://mail.google.com/mail/u/0/?shva=1#inbox/" + thrIdHexStr);
      hMap.put("from", fromPersonal);
      hMap.put("receivedDate", DateUtil.getDate(message.getReceivedDate()));
      hMap.put("isRead", message.isSet(Flag.SEEN));
      
      resultList.add(hMap);
    }
    
    CommonUtil.sortDescMapList(resultList, "receivedDate");
    
    return resultList;
  }
}
  
  
