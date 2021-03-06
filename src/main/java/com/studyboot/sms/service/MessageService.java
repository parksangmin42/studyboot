package com.studyboot.sms.service;

import java.util.List;
import com.studyboot.sms.domain.Message;

public interface MessageService {
  List<Message> list(int pageNo, int pageSize, List<Integer> memberNos, int loginNo);
  List<Message> list2(int pageNo, int pageSize, List<Integer> memberNos, int loginNo);
  int add(Message message);
  Message get(int no);
  int delete(int no);
  int size(List<Integer> memberNos, int loginNo);
  int size2(List<Integer> memberNos, int loginNo);
}
