package scnu.able.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scnu.able.myapp.dao.LinkMapper;
import scnu.able.myapp.vo.link.Link;
import scnu.able.myapp.vo.link.LinkRepl;
import scnu.able.myapp.vo.member.Member;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class LinkController {

    @Autowired
    private LinkMapper linkMapper;

    // 링크 전체를 불러와서 linkList.jsp로 쏴줌(페이징 처리 연습해야 됨)
    @RequestMapping("/linkList.do")
    public String linkList(Model model, HttpServletRequest request) {

        // 일단 제일먼저 한 페이지당 줄의 개수, 한번에 선택가능한 페이지의 개수를 정의해서 pageLength라는 변수에 담아줘야 함
        int pageLength = 10;

        // 그 다음으로는 총 몇개의 페이지가 필요한 지 알기 위해 totalPage라는 변수에 전체 리스트의 갯수를 얻어와서 계산한 페이지의 개수를 넣어줌
        int totalListSize = linkMapper.linkListNoLimit().size();

        // pageLength로 나누어 떨어지게 되면 딱 10으로 끊기므로 다음 페이지를 생성해줄 필요가 없지만 1이라도 나머지가 생기는 경우에는 다음 페이지를 만들어줘야 하므로 1을 더해준다.
        int totalPage = (totalListSize % pageLength) == 0 ? (totalListSize / pageLength) : (totalListSize / pageLength) + 1;
        // 리스트에 아무것도 담겨있지 않더라도 1페이지는 표시되도록 한다.
        if (totalListSize == 0) {
            totalPage = 1;
        }

        // 현재 페이지가 몇 페이지인지 알기 위해서 client 로부터 page 객체를 받아온다. (현재 페이지를 담을 변수는 cPage로 설정)
        int cPage;
        String tmpPage = request.getParameter("page");

        // 처음에 리스트를 호출하게 되면 page값이 null 상태가 되므로 null 처리를 해준다.
        if (tmpPage == null || tmpPage.length() == 0) {
            cPage = 1;
        }

        // 클라이언트에서 넘어오는 page 값이 엉뚱한 파라미터로 넘어올 수 있기 때문에 try - catch 문으로 프로그램이 종료되는 것을 막아주도록 한다.(엉뚱한 값이 들어온다면 cPage는 1로 설정)
        try {
            cPage = Integer.parseInt(tmpPage);
        } catch (Exception e) {
            cPage = 1;
        }

        // db에서 페이징을 적용해서 데이터를 얻으려면 SQL의 LIMIT 조건의 두 인자중 첫번재 값으로 몇 번째 인덱스부터 가져올 것인지를 정해야 하므로 그 값을 startNum 으로 함.
        int startNum = (cPage - 1) * pageLength;
        List<Link> linkList = linkMapper.linkList(startNum);

        // 나는 10개의 페이지를 묶어서 하나의 블록으로 만들어 줄 것이기 때문에 한 블록마다의 startPage와 endPage를 지정해서 변수에 담아 주어야 함.
        int currentBlock = (cPage % pageLength) == 0 ? (cPage / pageLength) : (cPage / pageLength) + 1;

        // // 현재 블록의 시작 페이지
        int startPage = (currentBlock - 1) * pageLength + 1;

        // // 현재 블록의 마지막 페이지
        int endPage = startPage + pageLength - 1;

        // // 이 블록의 endPage가 전체 페이지보다 커지면 안 되므로 조건문을 통해 해결해준다.
        if (endPage > totalPage) {
            endPage = totalPage;
        }

        // view단으로 뿌려주는 데이터들(항상 맨 마지막에 뿌려줘서 알아보기 쉽게 하도록 한다.)
        model.addAttribute("cPage", cPage);
        model.addAttribute("totalListSize", totalListSize);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("linkList", linkList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "link/linkList";
    }

    // 링크 작성폼으로 리턴
    @RequestMapping("/linkWriteForm.do")
    public String linkWriteForm() {return "link/linkWriteForm";}

    // 링크 작성(파일업로드가 핵심!!!!)
    @RequestMapping("/linkWrite.do")
    public String linkWrite(MultipartHttpServletRequest multipartRequest, HttpServletRequest request, HttpSession session, Model model, RedirectAttributes rttr)
    throws Exception{

        // 글제목, 내용이 파라미터로 넘어옴
        Link link = new Link();
        String linkTitle = multipartRequest.getParameter("linkTitle");
        String linkContent = multipartRequest.getParameter("linkContent");
        link.setLinkTitle(linkTitle);
        link.setLinkContent(linkContent);

        // db에 저장할 때 작성자가 필요하므로 Member 객체 하나 생성해서 작성자를 얻어옴.
        Member tmpMember = (Member) session.getAttribute("mvo");
        String linkWriter = tmpMember.getMemName();
        link.setLinkWriter(linkWriter);


        // 이제 Link 객체를 db에 저장해줌
        linkMapper.linkWrite(link);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "링크에 글이 등록되었습니다.");

        return "redirect:/linkList.do";
    }

    // 링크 삭제로직
    @RequestMapping("/linkDelete.do")
    public String linkDelete(HttpSession session, RedirectAttributes rttr, HttpServletRequest request) {

        int linkIdx = Integer.parseInt(request.getParameter("linkIdx"));
        Link link = linkMapper.getLinkByLinkIdx(linkIdx);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "링크 삭제에 성공했습니다.");

        linkMapper.linkDelete(link);

        return "redirect:/linkList.do";

    }
}






























