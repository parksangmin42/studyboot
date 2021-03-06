package com.studyboot.sms.service;

import java.util.List;
import java.util.Map;
import com.studyboot.sms.domain.Rate;
import com.studyboot.sms.domain.RateRequire;

public interface StudyRetireService {

  int evaluationAdd(Map<String, Object> evaluationMap);
  int rateRequire(Map<String, Object> rateRequireMap);
  List<RateRequire> retireTrueOrFalse(Map<String, Object> map);
  List<Rate> retireEvaluation(Map<String, Object> map);
  int evaluationMemberCount(Map<String, Object> map);
  int retireeEvaluationCount(Map<String, Object> retireeEcaluationCountMap);
  int rateRequireUpdate(Map<String, Object> map);
}
