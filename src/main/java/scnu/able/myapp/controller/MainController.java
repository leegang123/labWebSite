package scnu.able.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scnu.able.myapp.dao.*;
import scnu.able.myapp.vo.free.Free;
import scnu.able.myapp.vo.free.FreeRepl;
import scnu.able.myapp.vo.labnote.Labnote;
import scnu.able.myapp.vo.labnote.LabnoteRepl;
import scnu.able.myapp.vo.manual.Manual;
import scnu.able.myapp.vo.manual.ManualRepl;
import scnu.able.myapp.vo.notice.Notice;
import scnu.able.myapp.vo.notice.NoticeRepl;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

@Controller
public class MainController {

    // searchMapper에 홈화면에 사용할 labnoteList 불러오는 기능과 NoticeList 불러오는 기능까지 추가하기
    @Autowired
    private CostMapper costMapper;

    @Autowired
    private FreeMapper freeMapper;

    @Autowired
    private LabnoteMapper labnoteMapper;

    @Autowired
    private LinkMapper linkMapper;

    @Autowired
    private ManualMapper manualMapper;

    @Autowired
    private MemberChangeMapper memberChangeMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private SearchMapper searchMapper;

    @RequestMapping("/")
    public String index(Model model) {

        List<Notice> noticeList = noticeMapper.noticeList(0);
        List<Labnote> labnoteList = labnoteMapper.allLabnoteListLimit();

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("labnoteList", labnoteList);

        return "index"; // WEB-INF/views/index.jsp
    }

    // 맨 마지막에 모든 게시판 구현완료 후 검색 기능을 만들어야 함!!!!!!!!!!!

    @RequestMapping("/allSearchList.do")
    public String getAllSearchList(HttpServletRequest request, Model model) {
        String searchContent = request.getParameter("searchContent");
        searchContent = "%" + searchContent + "%";
        List<Labnote> labnoteList = searchMapper.labnoteSearch(searchContent);
        List<Notice> noticeList = searchMapper.noticeSearch(searchContent);
        List<Free> freeList = searchMapper.freeSearch(searchContent);
        List<Manual> manualList = searchMapper.manualSearch(searchContent);

        model.addAttribute("labnoteList", labnoteList);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("freeList", freeList);
        model.addAttribute("manualList", manualList);

        return "common/allSearchList";
    }

    // 여기서 모든 댓글의 삭제가 이루어짐!!
    @RequestMapping("/replDelete.do")
    public String replDelete(HttpServletRequest request, RedirectAttributes rttr) {

        String[] repl = request.getParameter("repl").split(",");
        String type = repl[0];
        int idx = Integer.parseInt(repl[1]);

        switch (type) {
            case "free":
                FreeRepl freeRepl = freeMapper.getFreeReplByFreeReplIdx(idx);
                freeMapper.freeReplDelete(idx);
                rttr.addFlashAttribute("msgType", "성공 메세지");
                rttr.addFlashAttribute("msg", "댓글이 삭제되었습니다.");
                return "redirect:/freeDetail.do?freeIdx="+freeRepl.getFreeIdx();
            case "labnote":
                LabnoteRepl labnoteRepl = labnoteMapper.getLabnoteReplByLabnoteReplIdx(idx);
                labnoteMapper.labnoteReplDelete(idx);
                rttr.addFlashAttribute("msgType", "성공 메세지");
                rttr.addFlashAttribute("msg", "댓글이 삭제되었습니다.");
                return "redirect:/labnoteDetail.do?labnoteIdx="+labnoteRepl.getLabnoteIdx();
            case "manual":
                ManualRepl manualRepl = manualMapper.getManualReplByManualReplIdx(idx);
                manualMapper.manualReplDelete(idx);
                rttr.addFlashAttribute("msgType", "성공 메세지");
                rttr.addFlashAttribute("msg", "댓글이 삭제되었습니다.");
                return "redirect:/manualDetail.do?manualIdx="+manualRepl.getManualIdx();
            case "notice":
                NoticeRepl noticeRepl = noticeMapper.getNoticeReplByNoticeReplIdx(idx);
                noticeMapper.noticeReplDelete(idx);
                rttr.addFlashAttribute("msgType", "성공 메세지");
                rttr.addFlashAttribute("msg", "댓글이 삭제되었습니다.");
                return "redirect:/noticeDetail.do?noticeIdx="+noticeRepl.getNoticeIdx();
        }

        return "redirect:/";


    }


}
