package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.labnote.Labnote;
import scnu.able.myapp.vo.labnote.LabnoteRepl;
import scnu.able.myapp.vo.labnote.Subject;

import java.util.Collection;
import java.util.List;

@Mapper
public interface LabnoteMapper {

    // 페이징 처리를 해야 하므로 원래 있는 리스트를 가져오는 메서드는 Limit 조건을 추가해야 하므로 전체 개수를 얻어오는 메서드를 새로 만들어주어야 함.
    public List<Subject> subjectListNoLimit(int memIdx);

    // 멤버 인덱스를 이용해서 그 멤버에 해당하는 subject들을 리스트 형태로 묶어서 반환
    public List<Subject> subjectList(int memIdx, int startNum);

    // Subject 객체를 하나 받아와서 db에 저장해줌.
    public void subjectWrite(Subject subject);

    // Primary Key인 SubjectIdx를 이용해 Subject 객체를 하나 가져옴
    public Subject getSubjectBySubjectIdx(int subjectIdx);

    // Subject 객체를 하나 받아와서 수정해줌
    public void subjectUpdate(Subject subject);

    // SubjectIdx를 받아와서 Subject를 삭제해줌
    public void subjectDelete(int subjectIdx);

    // ---------------------- 여기까지 Subject 관련된 CRUD --------------------------------------------------------------------------

    // 원래의 List<Labnote>를 알아오는 메서드는 Limit 기능을 추가해야 하므로 전체 리스트의 갯수를 알아올 목적으로 List<Labnote>를 반환하는 새로운 메소드 생성
    public List<Labnote> labnoteListNoLimit(int subjectIdx);

    // subjectIdx를 통해서 List<Labnote>(랩노트 리스트)를 받아옴.
    public List<Labnote> labnoteList(int subjectIdx, int startNum);

    // 전체 랩노트 리스트를 가져와서 뿌려주는 allLabnoteList를 만들기 위해서 subjectIdx를 받지 않는 labnoteList 메서드를 하나 생성
    public List<Labnote> allLabnoteList();

    // Labnote 객체를 받아와서 db에 저장
    public void labnoteWrite(Labnote labnote);

    // LabnoteIdx를 받아서 Labnote 객체를 반환해줌
    public Labnote getLabnoteByLabnoteIdx(int labnoteIdx);

    // LabnoteIdx를 받아서 LabnoteHit를 1 더해준 후에 Labnote 객체를 반환해줌
    public Labnote getLabnoteByLabnoteIdxCountPlus(int labnoteIdx);

    // Labnote 객체를 하나 받아와서 db에 저장된 값을 업데이트 해 줌.
    public void labnoteUpdate(Labnote labnote);

    // labnoteIdx를 받아서 db에 저장된 Labnote 객체를 하나 삭제해줌
    public void labnoteDelete(int labnoteIdx);


    // ---------------------------- 여기까지 Labnote 관련된 CRUD ---------------------------------------------------------------------------------

    // labnoteIdx를 이용해 List<LabnoteRepl>을 가져온다.
    public List<LabnoteRepl> labnoteReplList(int labnoteIdx);

    // LabnoteRepl 객체를 하나 받아와서 db에 저장해주기
    public void labnoteReplWrite(LabnoteRepl labnoteRepl);

    LabnoteRepl getLabnoteReplByLabnoteReplIdx(int idx);

    void labnoteReplDelete(int idx);

    List<Labnote> allLabnoteListLimit();
}
