package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.free.Free;
import scnu.able.myapp.vo.free.FreeRepl;

import java.util.Collection;
import java.util.List;

@Mapper
public interface FreeMapper {

    // db 로부터 리스트를 가져와서 리스트의 전체 개수를 알기 위한 목적인 메서드
    public List<Free> freeListNoLimit();

    // 실제로 페이징 기능을 적용해서 List<Free>를 가져오기 위한 메서드
    public List<Free> freeList(int startNum);

    // 실제로 작성한 Free 객체를 받아와서 db에 저장해주는 메서드
    public void freeWrite(Free free);

    // freeIdx를 통해서 db로부터 Free 객체를 하나 얻어온다.
    public Free getFreeByFreeIdx(int freeIdx);

    // Free 객체를 하나 받아서 db에 저장된 Free 객체를 업데이트 해준다.
    public void freeUpdate(Free free);

    // Free 객체를 하나 받아와서 삭제해주는 메서드
    public void freeDelete(Free free);

    // --------------------------------------- Free 객체 관련 메서들( 밑에부터는 FreeRepl(자유게시판 댓글 관련) 메서드들))

    // foreign key 역할인 freeIdx를 받아와서 그에 해당하는 FreeRepl을 가져와주는 메서드
    public List<FreeRepl> freeReplList(int freeIdx);

    // FreeRepl 객체를 받아와서 db에 저장해준다.
    public void freeReplWrite(FreeRepl freeRepl);

    public void freeReplDelete(int idx);

    FreeRepl getFreeReplByFreeReplIdx(int idx);
}
