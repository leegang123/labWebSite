package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.member.OldMember;

@Mapper
public interface MemberChangeMapper {


    public OldMember getOldMemberByOldMemIdx(int oldMemIdx);

    public void oldMemDelete(int oldMemIdx);

    public void memDelete(int memIdx);

    public void oldMemJoin(OldMember oldMember);
}
