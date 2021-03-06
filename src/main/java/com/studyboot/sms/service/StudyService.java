package com.studyboot.sms.service;

import java.util.HashMap;
import java.util.List;
import com.studyboot.sms.domain.Study;

public interface StudyService {
  
  int add(Study study);
  Study get(int no);
  int update(Study study);
  int delete(int no);
  int size(String clsNo, String addressNo, double rateValue, String keyword, List<Integer> dayNoList);
  int size(List<String> clsNo, String addressNo, double rateValue, String keyword, List<Integer> dayNoList);
  List<Study> list(int pageNo, int pageSize, String clsNo, String addressNo, double rateValue, String keyword, List<Integer> dayNoList);
  List<Study> list(int pageNo, int pageSize, List<String> clsNo, String addressNo, double rateValue, String keyword, List<Integer> dayNoList);
  boolean checkFullCapacityByStudyNo(int studyNo);
  void updateAllStudyRecruitState();
  Study getStudyPhoto(int no);
  int insertPickedStudy(int userNo, int studyNo);
  int deletePickedStudy(int userNo, int studyNo);
  int prsnCount(int stdNo);
  int insertAppliedStudy(int no, int studyNo, String determination);
  int updateRate(int studyNo);
  HashMap<String, Integer> chartCount(double stdRate);
  int percentCount(double stdRate);
}