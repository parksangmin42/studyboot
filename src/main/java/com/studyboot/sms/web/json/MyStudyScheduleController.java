package com.studyboot.sms.web.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.studyboot.sms.domain.Member;
import com.studyboot.sms.domain.Schedule;
import com.studyboot.sms.service.MemberService;
import com.studyboot.sms.service.MyStudyScheduleService;
import com.studyboot.sms.service.StudyMemberService;

@RestController("json/MyStudyScheduleController")
@RequestMapping("/json/mystudyschedule")
public class MyStudyScheduleController {

  @Autowired MyStudyScheduleService myStudyScheduleService;
  @Autowired StudyMemberService studyMemberService;
  @Autowired MemberService memberService;

  @PostMapping("add")
  public Object add(Schedule schedule, HttpSession session) {
    HashMap<String,Object> content = new HashMap<>();

    Member loginUser = (Member) session.getAttribute("loginUser"); // 로그인한 유저의 정보를 담는다.

    // loginUser.getNo() 로그인한 유저의 유저넘버를 schedule 객체에 담는다.
    schedule.setMemberNo(loginUser.getNo());

    // 리더 판단을 위한 코드
    HashMap<String,Object> studyAndUserNo = new HashMap<>();
    studyAndUserNo.put("loginUser", loginUser.getNo());
    studyAndUserNo.put("studyNo", schedule.getStudyNo());
    boolean leaderYesOrNo = studyMemberService.findStudyMemberLeader(studyAndUserNo);

    // 리더 유무 판단. 
    if (leaderYesOrNo == false) {

      content.put("status", "스터디 장만 일정을 등록 할 수 있습니다.");
      return content;
    }

    try {
      myStudyScheduleService.add(schedule);
      content.put("status", "등록이 완료 되었습니다.");
    } catch (Exception e) {
      content.put("status", "fail");
      content.put("message", e.getMessage());
    }

    return content;
  }

  @GetMapping("list")
  public Object list(HttpSession session, @RequestParam int no) {

    Member loginUser = (Member) session.getAttribute("loginUser"); // 로그인한 유저의 정보를 담는다.

    HashMap<String,Object> studyAndUserNo = new HashMap<>();
    studyAndUserNo.put("loginUser", loginUser.getNo());
    studyAndUserNo.put("studyNo", no);

    List<Object> schedule = myStudyScheduleService.list(no);

    // 배열 마지막방에 리더 유무 판단을 위해 boolean값을 넣는다.
    schedule.add(studyMemberService.findStudyMemberLeader(studyAndUserNo));

    System.out.println("컨트롤러 list: " + schedule);
    System.out.println("리더입니까?: " + schedule.get(schedule.size()-1));

    return schedule;
  }

  @GetMapping("allSchedules")
  public Object allSchedules(HttpSession session) {

    Member loginUser = (Member) session.getAttribute("loginUser");
    List<Schedule> schedule = null;

    if (loginUser != null) {
      schedule = myStudyScheduleService.allSchedules(loginUser.getNo());
    }
    return schedule;
  }

  @GetMapping("detail")
  public Object detail(int no) {

    Schedule schedule = myStudyScheduleService.get(no);

    return schedule;
  }

  @GetMapping("delete")
  public Object delete(int eventNo, int studyNo, HttpSession session) {

    HashMap<String,Object> content = new HashMap<>();

    HashMap<String,Object> studyAndUserNo = new HashMap<>();
    studyAndUserNo.put("loginUser", ((Member) session.getAttribute("loginUser")).getNo());
    studyAndUserNo.put("studyNo", studyNo);

    boolean leaderYesOrNo = studyMemberService.findStudyMemberLeader(studyAndUserNo);

    if (leaderYesOrNo == false) {

      content.put("status", "스터디 장만 일정을 삭제 할 수 있습니다.");
      return content;
    }

    try {
      if (myStudyScheduleService.delete(eventNo) == 0) 
        throw new RuntimeException("해당 번호의 게시물이 없습니다.");
      content.put("status", "success");

    } catch (Exception e) {
      content.put("status", "fail");
      content.put("message", e.getMessage());
    }
    return content;
  }

  @PostMapping("update")
  public Object update(Schedule schedule, HttpSession session) {
    HashMap<String,Object> content = new HashMap<>();

    Member loginUser = (Member) session.getAttribute("loginUser"); // 로그인한 유저의 정보를 담는다.

    // loginUser.getNo() 로그인한 유저의 유저넘버를 schedule 객체에 담는다.
    schedule.setMemberNo(loginUser.getNo());

    // 리더 판만을 위한 코드
    HashMap<String,Object> studyAndUserNo = new HashMap<>();
    studyAndUserNo.put("loginUser", loginUser.getNo());
    studyAndUserNo.put("studyNo", schedule.getStudyNo());
    boolean leaderYesOrNo = studyMemberService.findStudyMemberLeader(studyAndUserNo);

    // 리더 유무 판단. 
    if (leaderYesOrNo == false) {

      content.put("status", "스터디 장만 일정을 수정 할 수 있습니다.");
      return content;
    }

    try {
      if (myStudyScheduleService.update(schedule) == 0) 
        throw new RuntimeException("해당 번호의 게시물이 없습니다.");
      content.put("status", "수정이 완료 되었습니다.");

    } catch (Exception e) {
      content.put("status", "fail");
      content.put("message", e.getMessage());
    }
    return content;
  }

  @GetMapping("attend")
  public Object attend(String[] nickNames, int studyNo , int scheduleNo, HttpSession session) {

    HashMap<String,Object> content = new HashMap<>();

    Member loginUser = (Member) session.getAttribute("loginUser"); // 로그인한 유저의 정보를 담는다.
    HashMap<String,Object> studyAndUserNo = new HashMap<>();
    studyAndUserNo.put("loginUser", loginUser.getNo());
    studyAndUserNo.put("studyNo", studyNo);
    boolean leaderYesOrNo = studyMemberService.findStudyMemberLeader(studyAndUserNo);

    if (leaderYesOrNo == false) {

      content.put("status", "스터디 장만 출석 체크를 할 수 있습니다.");
      return content;
    }


    // 이미 출석한 경험이 있다면 리턴 한다.
    List<Schedule> attendTrueFalse = myStudyScheduleService.attendTrueFalse(scheduleNo); 

    if (attendTrueFalse.size() > 0) {
      content.put("status", "이미 출석체크를 하였습니다.");

      return content;
    }

    // 닉네임을 멤버넘버로 바꾸는 코드
    List memberNo =  memberService.findMemberNoByNickNameList(nickNames);

    HashMap<String,Object> attendMap = new HashMap<>();
    try {
      for(int i = 0; i < memberNo.size(); i++) { 
        //출석체크
        attendMap.put("scheduleNo", scheduleNo);
        attendMap.put("studyNo", studyNo);
        attendMap.put("memberNo", memberNo.get(i));
        myStudyScheduleService.attend(attendMap); // 출석체크!


        // 출석체크 후 출석률 업데이트!
        int allEventNumber = myStudyScheduleService.allEventCount(studyNo); // 모든 일정의 수

        HashMap<String,Object> studyNoMemberNo = new HashMap<>();
        studyNoMemberNo.put("studyNo", studyNo);
        studyNoMemberNo.put("memberNo", memberNo.get(i));

        // 해당 사용자가 출석한 수
        int studyAttendNumber = myStudyScheduleService.studyAttendCount(studyNoMemberNo); 

        // 출석률
        double attendPercent = (double) studyAttendNumber / allEventNumber * 100;
        
        // 출석률 소수점 한자리만 남기고 버림
        double attendPercentCut = (int)(attendPercent*10)/10.0; 

        Map<String, Object> attendPercentUpdateMap = new HashMap<>();

        System.out.println("모든일정 수: " + allEventNumber);
        System.out.println("출석한횟 수: " + studyAttendNumber);
        System.out.println("확률: " + attendPercent);
        System.out.println("확률자른거: " + attendPercentCut);

        attendPercentUpdateMap.put("percent", attendPercentCut);
        attendPercentUpdateMap.put("studyNo", studyNo);
        attendPercentUpdateMap.put("memberNo",  memberNo.get(i));

        studyMemberService.attendPercentUpdate(attendPercentUpdateMap); // 출석률을 업데이트 한다.

      }
      content.put("status", "출석 체크가 완료 되었습니다.");
    } catch (Exception e) {
      content.put("status", "fail");
      content.put("message", e.getMessage());
      System.out.println("출석체크 도중 오류! " + e.getMessage());
    }

    return content;
  }

  @GetMapping("attendTrueFalse") // 한 일정에 출석체크 한번만 하기 위한 메서드
  public Object attendTrueFalse(int scheduleNo) {

    HashMap<String,Object> content = new HashMap<>();

    List<Schedule> attendTrueFalse = myStudyScheduleService.attendTrueFalse(scheduleNo);

    content.put("attendNickName", attendTrueFalse);

    System.out.println(attendTrueFalse);

    if (attendTrueFalse.size() > 0) {

      content.put("attend", true);

      return content;
    } else {

      content.put("attend", false);
      return content;
    }
  }

}

