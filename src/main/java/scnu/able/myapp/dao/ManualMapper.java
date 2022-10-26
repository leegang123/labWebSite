package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.manual.Manual;
import scnu.able.myapp.vo.manual.ManualRepl;

import java.util.List;

@Mapper
public interface ManualMapper {

    // db 로부터 리스트를 가져와서 리스트의 전체 개수를 알기 위한 목적인 메서드
    public List<Manual> manualListNoLimit();

    // 실제로 페이징 기능을 적용해서 List<manual>를 가져오기 위한 메서드
    public List<Manual> manualList(int startNum);

    // 실제로 작성한 manual 객체를 받아와서 db에 저장해주는 메서드
    public void manualWrite(Manual manual);

    // manualIdx를 통해서 db로부터 manual 객체를 하나 얻어온다.
    public Manual getManualByManualIdx(int manualIdx);

    // manual 객체를 하나 받아서 db에 저장된 manual 객체를 업데이트 해준다.
    public void manualUpdate(Manual manual);

    // manual 객체를 하나 받아와서 삭제해주는 메서드
    public void manualDelete(Manual manual);

    // --------------------------------------- manual 객체 관련 메서들( 밑에부터는 manualRepl(자유게시판 댓글 관련) 메서드들))

    // foreign key 역할인 manualIdx를 받아와서 그에 해당하는 manualRepl을 가져와주는 메서드
    public List<ManualRepl> manualReplList(int manualIdx);

    // manualRepl 객체를 받아와서 db에 저장해준다.
    public void manualReplWrite(ManualRepl manualRepl);

    ManualRepl getManualReplByManualReplIdx(int idx);

    void manualReplDelete(int idx);
}
