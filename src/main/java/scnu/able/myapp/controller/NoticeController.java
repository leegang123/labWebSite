package scnu.able.myapp.controller;

import jdk.jfr.Frequency;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.javassist.bytecode.ByteArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scnu.able.myapp.dao.MemberMapper;
import scnu.able.myapp.dao.NoticeMapper;
import scnu.able.myapp.vo.member.Member;
import scnu.able.myapp.vo.notice.Notice;
import scnu.able.myapp.vo.notice.NoticeRepl;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
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
public class NoticeController {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private JavaMailSender emailSender;

    // 공지사항 전체를 불러와서 noticeList.jsp로 쏴줌
    @RequestMapping("/noticeList.do")
    public String noticeList(Model model, HttpServletRequest request) {


        // 총 몇개의 페이지가 필요한지를 알기 위해서 객체가 몇개 있는지를 파악해야 하므로 totalLiseSize로 객체를 파악후 totalPage라는 변수에 10을 나누어(나머지가 없다면) 담아줄 것이다.
        int totalListSize = noticeMapper.noticeListNoLimit().size();


        // 총 몇개의 페이지가 필요한지 알기 위해(마지막 페이지를 알아야 하므로) totalPage라는 변수에 총 페이지 갯수를 담음
        int totalPage = totalListSize % 10 == 0 ? totalListSize / 10 : (totalListSize / 10) + 1;
        if (totalListSize == 0) totalPage = 1;

        // 현재 페이지가 몇 페이지인지 알기 위해서 client 로부터 page 객체를 받아온다.(현재 페이지를 담을 변수는 cPage로 설정)
        int cPage;
        String tmpPage = request.getParameter("page");

        // 처음에 리스트를 호출하게 되면 page값이 null 상태가 되므로 null 처리를 해준다.
        if (tmpPage == null || tmpPage.length() == 0) {
            cPage = 1;
        }

        // 클라이언트에서 넘어오는 page 값이 엉뚱한 값이 파라미터로 넘어올 수 있기 때문에 try- catch 문으로 프로그램이 종료되는 것을 막아주도록 하자.(엉뚱한 값이 들어온다면 cPage = 1로 설정)
        try {
            cPage = Integer.parseInt(tmpPage);
        } catch (Exception e) {
            cPage = 1;
        }

        // db에서 페이징을 적용해서 데이터를 얻어오려면 LIMIT의 두 인자중 첫번째 값으로 몇 번째 인덱스부터 가져올 것인지를 정해야 하므로 그 값을 startNum으로 하겠다.
        int startNum = (cPage - 1) * 10;
        List<Notice> noticeList = noticeMapper.noticeList(startNum);

        // 내가 한번에 보여줄 페이지의 개수가 10개 이므로 한 블록은 10개로 정의하고, 그 블록마다의 첫 페이지와 마지막 페이지를 구해야 한다.

        // 현재가 몇번째 블록인지를 계산해주는 부분 위의 totalPage 구하는 부분과 비슷한 로직이다.
        int currentBlock = (cPage % 10) == 0 ? (cPage / 10) : (cPage / 10) + 1;

        // 현재 블록의 시작 페이지를 구해주는 부분 (위의 startNum과 비슷한 로직이다)
        int startPage = (currentBlock - 1) * 10 + 1;

        // startPage를 잘 구했다면 endPage는 매우 쉽다 그냥 startPage에서 10을 더한후 1을 빼주면 된다.
        int endPage = startPage + 10 - 1;

        // 이 블록의 endPage가 전체 페이지보다 커지면 안 되므로 조건문을 통해 해결해준다.
        if (endPage > totalPage) {
            endPage = totalPage;
        }

        // 앞으로 view단으로 뿌려주는 데이터를 맨 마지막에 한번에 보냄으로써 어떤 데이터가 넘어가는지 알기 쉽게 하겠다.
        model.addAttribute("cPage", cPage);
        model.addAttribute("totalListSize", totalListSize);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);



        return "notice/noticeList";
    }

    // 공지사항 작성폼
    @RequestMapping("/noticeWriteForm.do")
    public String noticeWriteForm() {return "notice/noticeWriteForm";}

    // 공지사항 작성
    @RequestMapping("/noticeWrite.do")
    public String noticeWrite(MultipartHttpServletRequest multipartRequest, HttpServletRequest request, HttpSession session, Model model, 
                              RedirectAttributes rttr) throws Exception {

        String noticeTop = multipartRequest.getParameter("noticeTop");

        // 고유한 파일이름을 가지기 위해 랜덤함수 생성
        Random random = new Random();
        int tmpRandomIdx = random.nextInt();
        String randomIdx = String.valueOf(tmpRandomIdx);

        String UPLOAD_DIR = "notice_upload";
        String uploadPath = request.getServletContext().getRealPath("")+ File.separator+UPLOAD_DIR;

        // 글제목, 내용이 파라미터로 넘어옴
        Notice notice = new Notice();
        String noticeTitle = multipartRequest.getParameter("noticeTitle");
        String noticeContent = multipartRequest.getParameter("noticeContent");
        notice.setNoticeContent(noticeContent);
        notice.setNoticeTitle(noticeTitle);
        notice.setNoticeTop(noticeTop);



        // db에 저장할 때 작성자가 필요하므로 Member 객체 하나 생성해서 작성자를 얻어옴
        Member tmpMember = (Member) session.getAttribute("mvo");
        String noticeWriter = tmpMember.getMemName();
        notice.setNoticeWriter(noticeWriter);


        // 파일을 담고있는 파라미터 읽어오기
        Iterator<String> it = multipartRequest.getFileNames();// 파일이름이 아니라 -> 파라미터 이름(file1, file2)
        String fileNames = ""; // 파일이 여러개 일수 있으니 파일이름을 담아주는 배열
        while (it.hasNext()) {
            String paramfName = it.next();
            MultipartFile mFile = multipartRequest.getFile(paramfName);// 실제 파일(이름, 타입, 크기...)
            String oName = mFile.getOriginalFilename(); // 실제 업로드된 파일 이름.
            fileNames += randomIdx+ "`"+oName + ",";
            // 파일을 업로드 할 경로를 확인?
            File file = new File(uploadPath + "\\" + paramfName); // file1

            if (mFile.getSize() != 0) {
                if (!file.exists()) {
                    if (file.getParentFile().mkdirs()) {
                        file.createNewFile(); // 임시로 파일을 생성
                    }
                }
                mFile.transferTo(new File(uploadPath + "\\" + randomIdx+ "`" +oName));
            }
        }

        notice.setNoticeFile(fileNames.trim());

        // 공지사항을 작성했으니 db에 저장
        noticeMapper.noticeWrite(notice);

        // 메일 작성 로직
        ArrayList<String> emailList = memberMapper.emailList();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kitaekim077@gmail.com");
        message.setTo((String[]) emailList.toArray(new String[emailList.size()]));
        message.setSubject(notice.getNoticeTitle());
        message.setText(notice.getNoticeContent());
        emailSender.send(message);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "공지사항이 작성되었습니다.");



       return "redirect:/noticeList.do";
    }

    // 수정을 하려면 원래 공지사항 객체의 모든 value를 가져와서 전송해야 하기 때문에 model에 공지사항 객체를 하나 담아서 전달.
    // 공지사항 글쓴이와 세션의 memName이 다를 경우 튕기는 것은 update.do에서 구현
    @RequestMapping("/noticeUpdateForm.do")
    public String noticeUpdateForm(HttpServletRequest request, Model model) {

        // 먼저 noticeIdx를 requets로 얻어옴.
        int noticeIdx = Integer.parseInt(request.getParameter("noticeIdx"));
        Notice notice = noticeMapper.getNoticeByIdx(noticeIdx);
        model.addAttribute("notice", notice);

        return "notice/noticeUpdateForm";
    }

    // 실제 공지사항 수정로직
    @RequestMapping("/noticeUpdate.do")
    public String noticeUpdate(Notice notice, HttpSession session, RedirectAttributes rttr) {

        // 공지사항을 작성한 작성자와 세션에 등록된 사용자의 이름이 다른 경우 튕기게 만들기 위해 세션으로부터 멤버객체를 얻어옴.
        Member currentMember = (Member) session.getAttribute("mvo");
        if (!notice.getNoticeWriter().equals(currentMember.getMemName())) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "수정 권한이 없습니다!");
            return "redirect:/noticeList.do";
        }

        // 실제로 공지사항을 수정해주는 파트
        noticeMapper.noticeUpdate(notice);

        return "redirect:/noticeDetail.do?noticeIdx="+notice.getNoticeIdx();
    }

    // 공지사항 삭제
    @RequestMapping("/noticeDelete.do")
    public String noticeDelete(HttpSession session, RedirectAttributes rttr, HttpServletRequest request) {

        int noticeIdx = Integer.parseInt(request.getParameter("noticeIdx"));
        Notice notice = noticeMapper.getNoticeByIdx(noticeIdx);

        // 공지사항을 작성한 작성자와 세션에 등록된 사용자의 이름이 다른 경우 튕기게 만들기 위해 세션으로부터 멤버객체를 얻어옴.
        Member currentMember = (Member) session.getAttribute("mvo");
        if (!notice.getNoticeWriter().equals(currentMember.getMemName())) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "삭제 권한이 없습니다!");
            return "redirect:/noticeList.do";
        }

        // 실제로 공지사항을 삭제해주는 파트
        noticeMapper.noticeDelete(notice);

        return "redirect:/noticeList.do";
    }


    // 공지사항 자세히 보기
    @RequestMapping("/noticeDetail.do")
    public String noticeDetail(HttpServletRequest request, Model model, HttpSession session) {
        int noticeIdx = Integer.parseInt(request.getParameter("noticeIdx"));

        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String s = attributeNames.nextElement();
            if (s.equals("visitCheckNoticeIdx"+noticeIdx)) {
                Notice notice = noticeMapper.getNoticeByIdx(noticeIdx);
                String[] split = notice.getNoticeFile().split(",");
                List<String> noticeFile = new ArrayList<>(Arrays.asList(split));
                model.addAttribute("notice", notice);
                model.addAttribute("noticeFile", noticeFile);

                List<NoticeRepl> noticeReplList = noticeMapper.noticeReplList(noticeIdx);
                model.addAttribute("noticeReplList", noticeReplList);

                return "notice/noticeDetail";
            }
        }
        // 조회수를 증가시킬 때 세션에서 그 게시물을 방문한적이 있는지 없는지를 고유한 키값으로 넣어서 저장하기
        session.setAttribute("visitCheckNoticeIdx"+noticeIdx, true);

        Notice notice = noticeMapper.getNoticeByIdxCountPlus(noticeIdx);
        String[] split = notice.getNoticeFile().split(",");
        List<String> noticeFile = new ArrayList<>(Arrays.asList(split));
        model.addAttribute("notice", notice);
        model.addAttribute("noticeFile", noticeFile);

        List<NoticeRepl> noticeReplList = noticeMapper.noticeReplList(noticeIdx);
        model.addAttribute("noticeReplList", noticeReplList);


        return "notice/noticeDetail";
    }

    // 공지사항 파일 다운로드
    @RequestMapping("/noticeFileDownload.do")
    public void noticeFileDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String filename = request.getParameter("filename");

        String UPLOAD_DIR = "notice_upload";
        String uploadPath = request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIR;
        File f = new File(uploadPath + "\\" + filename);

        filename = filename.replace("+", " ");

        response.setContentLength((int) f.length());
        response.setContentType("application/x-msdownload;charset=UTF-8");

        // 파일 이름이 중복되는 것을 막기 위한 앞에 붙는 숫자들 제거
        String[] split = filename.split("`");
        List<String> noticeFile = new ArrayList<>(Arrays.asList(split));

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(noticeFile.get(1), StandardCharsets.UTF_8) + ";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        FileInputStream in = new FileInputStream(f);
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[104];
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


    // 공지사항 댓글 작성(ajax 방식으로 구성)
    @RequestMapping("/noticeReplWrite.do")
    public String noticeReplWrite(HttpServletRequest request, HttpSession session, RedirectAttributes rttr) {

        Member currentMember = (Member) session.getAttribute("mvo");
        int noticeIdx = Integer.parseInt(request.getParameter("noticeIdx"));
        String noticeReplContent = request.getParameter("noticeReplContent");

        if (noticeReplContent == null) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "댓글을 작성해주세요!!");
            return "redirect:/noticeDetail.do?noticeIdx="+noticeIdx;
        }

        NoticeRepl noticeRepl = new NoticeRepl();
        noticeRepl.setNoticeReplWriter(currentMember.getMemName());
        noticeRepl.setNoticeReplContent(noticeReplContent);
        noticeRepl.setNoticeIdx(noticeIdx);
        noticeMapper.noticeReplWrite(noticeRepl);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "댓글 달기에 성공했습니다.");

        return "redirect:/noticeDetail.do?noticeIdx="+noticeIdx;
    }


}

































