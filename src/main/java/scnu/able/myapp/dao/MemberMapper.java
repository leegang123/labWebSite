package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.member.Member;
import scnu.able.myapp.vo.member.OldMember;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MemberMapper {

    // -------------------  회원 가입, 로그인 페이지 메서드 -------------------------------------------------------------

    // memID로 멤버 가져오기
    public Member memIdCheck(String memID);

    // memName으로 멤버 가져오기
    public Member memNameCheck(String memName);

    // 실제 회원가입
    public int memJoin(Member m);

    // 로그인을 위한 멤버 하나 가져오기
    public Member memLogin(String memID);

    // 멤버 인덱스를 통해 멤버객체 하나 받아오기
    public Member getMemberByMemIdx(int memIdx);

    // -------------------------회원 수정 페이지 메서드  ----------------------------------------------------

    // 멤버 업데이트
    public int memUpdate(Member mvo);

    // 이름을 통해 멤버객체 하나 받아오기
    public Member nameGetMember(String memName);

    // 멤버 객체를 받아와서 프로필 사진 이름을 변경해주기
    public void memProfileUpdate(Member mvo);


    // ---------------- 관리자 페이지 메서드 ---------------------------------------------------------------------

    // 아직 승인을 받지 못한 회원 목록 가져오기
    public List<Member> getNoAuthList();

    // 승인을 받은 회원 목록 가져오기
    public List<Member> getAuthList();

    // 회원에게 '학생' 권한 주기
    public void memAuth(String memName);

    // 권한 없는 회원 삭제하기
    public void memNoAuthDelete(String memName);

    public ArrayList<String> emailList();

    public List<Member> getOldMemberList();


    Member getMemberByMemName(String memName);

    void memStatusUpdate(int oldMemIdx);

    void memStatusDelete(int memIdx);
}
