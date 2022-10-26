package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.free.Free;
import scnu.able.myapp.vo.labnote.Labnote;
import scnu.able.myapp.vo.manual.Manual;
import scnu.able.myapp.vo.notice.Notice;

import java.util.List;

@Mapper
public interface SearchMapper {

    public List<Notice> noticeSearch(String query);
    public List<Free> freeSearch(String query);
    public List<Manual> manualSearch(String query);
    public List<Labnote> labnoteSearch(String query);

    public List<Notice> noticeList();
    public List<Labnote> labnoteList();
}
