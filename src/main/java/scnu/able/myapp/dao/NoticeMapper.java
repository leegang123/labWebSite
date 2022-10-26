package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import scnu.able.myapp.vo.notice.Notice;
import scnu.able.myapp.vo.notice.NoticeRepl;

import java.util.List;

@Mapper
public interface NoticeMapper {

    // 전체 공지사항 개수 가져오기
    public List<Notice> noticeListNoLimit();

    // 전체 공지사항 목록 가져오기(페이징을 위해 startNum을 파라미터로 추가해야함)
    public List<Notice> noticeList(int startNum);

    // 공지사항을 작성
    public void noticeWrite(Notice notice);

    // 공지사항 상세를 보여주기 위해서 공지사항 인덱스로 공지사항 객체를 하나 받아온다.
    public Notice getNoticeByIdx(int noticeIdx);

    // 공지사항 상세 페이지 에서 공지사항을 요청할 경우 공지사항의 조회수를 1씩 더해준다.
    Notice getNoticeByIdxCountPlus(int noticeIdx);

    // 공지사항을 수정
    public void noticeUpdate(Notice notice);

    // 공지사항 삭제
    public void noticeDelete(Notice notice);

    // ------------------------ 댓글 관련 -------------------------------------------------------

    // 공지사항의 인덱스를 이용해 공지사항댓글 리스트를 받아온다.
    public List<NoticeRepl> noticeReplList(int noticeIdx);

    // 공지사항 댓글 작성
    public void noticeReplWrite(NoticeRepl noticeRepl);


    NoticeRepl getNoticeReplByNoticeReplIdx(int idx);

    void noticeReplDelete(int idx);
}
