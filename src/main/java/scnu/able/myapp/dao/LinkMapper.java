package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.link.Link;
import scnu.able.myapp.vo.link.LinkRepl;

import java.util.List;

@Mapper
public interface LinkMapper {

    // db 로부터 리스트를 가져와서 리스트의 전체 개수를 알기 위한 목적인 메서드
    public List<Link> linkListNoLimit();

    // 실제로 페이징 기능을 적용해서 List<link>를 가져오기 위한 메서드
    public List<Link> linkList(int startNum);

    // 실제로 작성한 link 객체를 받아와서 db에 저장해주는 메서드
    public void linkWrite(Link link);

    // linkIdx를 통해서 db로부터 link 객체를 하나 얻어온다.
    public Link getLinkByLinkIdx(int linkIdx);

    // link 객체를 하나 받아서 db에 저장된 link 객체를 업데이트 해준다.
    public void linkUpdate(Link link);

    // link 객체를 하나 받아와서 삭제해주는 메서드
    public void linkDelete(Link link);

    // --------------------------------------- link 객체 관련 메서들( 밑에부터는 linkRepl(자유게시판 댓글 관련) 메서드들))

    // foreign key 역할인 linkIdx를 받아와서 그에 해당하는 linkRepl을 가져와주는 메서드
    public List<LinkRepl> linkReplList(int linkIdx);

    // linkRepl 객체를 받아와서 db에 저장해준다.
    public void linkReplWrite(LinkRepl linkRepl);
}
