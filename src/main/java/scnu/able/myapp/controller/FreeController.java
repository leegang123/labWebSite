package scnu.able.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scnu.able.myapp.dao.FreeMapper;
import scnu.able.myapp.vo.free.Free;
import scnu.able.myapp.vo.free.FreeRepl;
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
public class FreeController {

    @Autowired
    private FreeMapper freeMapper;

    // 자유게시판 전체를 불러와서 freeList.jsp로 쏴줌(페이징 처리 연습해야 됨)
    @RequestMapping("/freeList.do")
    public String freeList(Model model, HttpServletRequest request) {

        // 일단 제일먼저 한 페이지당 줄의 개수, 한번에 선택가능한 페이지의 개수를 정의해서 pageLength라는 변수에 담아줘야 함
        int pageLength = 10;

        // 그 다음으로는 총 몇개의 페이지가 필요한 지 알기 위해 totalPage라는 변수에 전체 리스트의 갯수를 얻어와서 계산한 페이지의 개수를 넣어줌
        int totalListSize = freeMapper.freeListNoLimit().size();

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
        List<Free> freeList = freeMapper.freeList(startNum);

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
        model.addAttribute("freeList", freeList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "free/freeList";
    }

    // 자유게시판 작성폼으로 리턴
    @RequestMapping("/freeWriteForm.do")
    public String freeWriteForm() {return "free/freeWriteForm";}

    // 자유게시판 작성(파일업로드가 핵심!!!!)
    @RequestMapping("/freeWrite.do")
    public String freeWrite(MultipartHttpServletRequest multipartRequest, HttpServletRequest request, HttpSession session, Model model, RedirectAttributes rttr)
    throws Exception{

        // 고유한 파일이름을 가지기 위해 랜덤함수 생성
        Random random = new Random();
        int tmpRandomIdx = random.nextInt();
        String randomIdx = String.valueOf(tmpRandomIdx);

        // 파일을 업로드할 폴더의 이름 지정
        String UPLOAD_DIR = "free_upload";
        String uploadPath = request.getServletContext().getRealPath("")+ File.separator+UPLOAD_DIR;

        // 글제목, 내용이 파라미터로 넘어옴
        Free free = new Free();
        String freeTitle = multipartRequest.getParameter("freeTitle");
        String freeContent = multipartRequest.getParameter("freeContent");
        free.setFreeTitle(freeTitle);
        free.setFreeContent(freeContent);

        // db에 저장할 때 작성자가 필요하므로 Member 객체 하나 생성해서 작성자를 얻어옴.
        Member tmpMember = (Member) session.getAttribute("mvo");
        String freeWriter = tmpMember.getMemName();
        free.setFreeWriter(freeWriter);


        // 파일을 담고있는 파라미터 읽어오기
        Iterator<String> it = multipartRequest.getFileNames(); // 파일이름이 아니라 -> 파라미터 이름(file1, file2)
        String fileNames = ""; // 파일이 여러개 일 수 있으니 파일이름을 담아주는 문자열
        while (it.hasNext()) {
            String paramfName = it.next();
            MultipartFile mFile = multipartRequest.getFile(paramfName); // paramfName 변수에 파일 이름의 파라미터를 받아왔으니 그 파라미터 값을 통해 multipartRequest에 실제로 담겨있는 파일객체를 호출
            // 파일을 가리키는 포인터가 mFile 이었으므로 그 포인터를 통해 파일의 실제 이름을 가리키는 변수인 oName을 지정해 실제 이름을 저장해준다.
            String oName = mFile.getOriginalFilename();
            fileNames += randomIdx + "`" + oName + ",";
            // 파일을 업로드 할 경로를 확인?
            File file = new File(uploadPath + "\\" + paramfName); // file1
            if (mFile.getSize() != 0) {
                if (!file.exists()) {
                    if (file.getParentFile().mkdirs()) {
                        file.createNewFile();
                    }
                }
                mFile.transferTo(new File(uploadPath + "\\" + randomIdx + "`" + oName));
            }
        }

        free.setFreeFile(fileNames.trim());

        // 이제 Free 객체를 db에 저장해줌
        freeMapper.freeWrite(free);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "자유게시판에 글이 등록되었습니다.");

        return "redirect:/freeList.do";
    }

    // 자유게시판 수정폼으로 리턴(여기서부터 만들어야 됨!)
    @RequestMapping("/freeUpdateForm.do")
    public String freeUpdateForm(HttpServletRequest request, Model model) {

        // 먼저 freeIdx를 request 로 얻어옴.
        int freeIdx = Integer.parseInt(request.getParameter("freeIdx"));
        Free free = freeMapper.getFreeByFreeIdx(freeIdx);
        model.addAttribute("free", free);

        return "free/freeUpdateForm";
    }

    // 실제 자유게시판 수정로직
    @RequestMapping("/freeUpdate.do")
    public String freeUpdate(Free free, HttpSession session, RedirectAttributes rttr) {

        // 자유게시판에 게시글을 등록한 사용자와 지금 수정을 요청하는 사용자의 이름이 다를 경우 수정이 안되도록 하기 위해 session으로부터 현재 로그인된 사용자 정보를 얻어옴.
        Member currentMember = (Member) session.getAttribute("mvo");
        if (!free.getFreeWriter().equals(currentMember.getMemName())) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다!");
            return "redirect:/freeDetail.do?freeIdx="+free.getFreeIdx();
        }

        freeMapper.freeUpdate(free);

        return "redirect:/freeDetail.do?freeIdx="+free.getFreeIdx();
    }

    // 자유게시판 삭제로직
    @RequestMapping("/freeDelete.do")
    public String freeDelete(HttpSession session, RedirectAttributes rttr, HttpServletRequest request) {

        int freeIdx = Integer.parseInt(request.getParameter("freeIdx"));
        Free free = freeMapper.getFreeByFreeIdx(freeIdx);

        // 자유게시판에 게시글을 등록한 사용자와 삭제하려는 사용자의 이름이 다른 경우 튕기게 만들기 위해 세션으로부터 멤버객체를 얻어옴.
        Member currentMember = (Member) session.getAttribute("mvo");
        if (!free.getFreeWriter().equals(currentMember.getMemName())) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다!");
            return "redirect:/freeDetail.do?freeIdx="+free.getFreeIdx();
        }

        freeMapper.freeDelete(free);

        return "redirect:/freeList.do";

    }

    // 자유게시판 자세히 보기
    @RequestMapping("/freeDetail.do")
    public String freeDetail(HttpServletRequest request, Model model, HttpSession session) {

        int freeIdx = Integer.parseInt(request.getParameter("freeIdx"));

        Free free = freeMapper.getFreeByFreeIdx(freeIdx);
        String[] split = free.getFreeFile().split(",");
        List<String> freeFile = new ArrayList<>(Arrays.asList(split));
        model.addAttribute("free", free);
        model.addAttribute("freeFile", freeFile);

        List<FreeRepl> freeReplList = freeMapper.freeReplList(freeIdx);
        model.addAttribute("freeReplList", freeReplList);

        return "free/freeDetail";
    }

    // 자유게시판 파일 다운로드
    @RequestMapping("/freeFileDownload.do")
    public void freeFileDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String filename = request.getParameter("filename");

        String UPLOAD_DIR = "free_upload";
        String uploadPath = request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIR;
        File f = new File(uploadPath + "\\" + filename);

        filename = filename.replace("+", " ");

        response.setContentLength((int) f.length());
        response.setContentType("application/x-msdownload;charset=UTF-8");

        // 파일 이름이 중복되는 것을 막기 우해 앞에 붙였던 숫자들을 제거
        String[] split = filename.split("`");
        List<String> freeFile = new ArrayList<>(Arrays.asList(split));

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(freeFile.get(1), StandardCharsets.UTF_8) + ";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        FileInputStream in = new FileInputStream(f);
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int count = in.read(buffer);
            if (count == -1) {
                break;
            }
            out.write(buffer, 0, count);
        }
        in.close();
        out.close();
    }

    // 자유게시판 댓글 작성(ajax 방식으로 구현)
    @RequestMapping("/freeReplWrite.do")
    public String freeReplWrite(HttpServletRequest request, HttpSession session, RedirectAttributes rttr) {

        Member currentMember = (Member) session.getAttribute("mvo");
        int freeIdx = Integer.parseInt(request.getParameter("freeIdx"));
        String freeReplContent = request.getParameter("freeReplContent");

        if (freeReplContent == null) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "댓글을 작성해주세요!");
            return "redirect:/freeDetail.do?freeIdx="+freeIdx;
        }

        FreeRepl freeRepl = new FreeRepl();
        freeRepl.setFreeReplWriter(currentMember.getMemName());
        freeRepl.setFreeReplContent(freeReplContent);
        freeRepl.setFreeIdx(freeIdx);
        freeMapper.freeReplWrite(freeRepl);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "댓글 달기에 성공했습니다.");

        return "redirect:/freeDetail.do?freeIdx="+freeIdx;
    }
}






























