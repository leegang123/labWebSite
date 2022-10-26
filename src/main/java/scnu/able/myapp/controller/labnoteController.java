package scnu.able.myapp.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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
import scnu.able.myapp.dao.LabnoteMapper;
import scnu.able.myapp.dao.MemberMapper;
import scnu.able.myapp.vo.labnote.Labnote;
import scnu.able.myapp.vo.labnote.LabnoteRepl;
import scnu.able.myapp.vo.labnote.Subject;
import scnu.able.myapp.vo.member.Member;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class labnoteController {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private LabnoteMapper labnoteMapper;

    @Autowired
    private JavaMailSender emailSender;

    // 멤버의 idx를 받아와서 그 멤버에 해당하는 subjectList를 반환해준다.
    @RequestMapping("/subjectList.do")
    public String subjectList(HttpServletRequest request, Model model) {

        // 한 페이지당 몇 개의 게시물을 보이게 할 것이지 정해야 하므로 pageLength 변수를 하나 세팅해준다.
        int pageLength = 10;

        // 누구의 리스트를 띄우는지 알아야 하니까 memIdx값으로 Member객체를 하나 가져와서 view단에 뿌려준다.
        int memIdx = Integer.parseInt(request.getParameter("memIdx"));
        Member m = memberMapper.getMemberByMemIdx(memIdx);

        // 총 몇개의 페이지가 필요한지를 알기 위해서 객체가 몇개 있는지를 파악해야 하므로 totalListSize로 리스트 안에 들어있는 객체의 수를 파악한 후 변수에 10을 나누어(나머지가 없다면) 담아준다.
        int totalListSize = labnoteMapper.subjectListNoLimit(memIdx).size();

        // 총 몇개의 페이지가 필요한지 알기 위해(마지막 페이지를 알아야 하므로) totalPage라는 변수에 총 페이지 갯수를 담음.
        int totalPage = totalListSize % 10 == 0 ? totalListSize / 10 : totalListSize / 10 + 1;
        if (totalListSize == 0 ) {
            totalPage = 1;
        }

        // 현재 페이지가 몇 페이지인지 알기 위해서 client 로부터 page 객체를 받아온다.(현재 페이지를 담을 변수는 cPage로 설정)
        int cPage;
        String tmpPage = request.getParameter("page");

        // 처음에 List페이지에 접속하면 page값이 null이므로 null처리를 해준다.
        if (tmpPage == null || tmpPage.length() == 0) {
            cPage = 1;
        }

        // 클라이언트에서 파라미터로 넘어오는 값이 엉뚱한 값이 넘어올 수도 있기 때문에 try - catch 문으로 프로그램이 죽지 않도록 해준다.
        try {
            cPage = Integer.parseInt(tmpPage);
        } catch (Exception e) {
            cPage = 1;
        }

        // db에서 페이징을 적용해서 데이터를 얻어오려면 LIMIT의 두 인자 중 첫번째 값으로 몇 번째 인덱스부터 가져올 것인지를 정해야 하므로 그 값을 startNum으로 변수지정
        int startNum = (cPage - 1) * pageLength;
        // 넘어온 memIdx 값과 방금 설정한 startNum을 이용해 List<Subject>를 받아온다.
        List<Subject> subjectList = labnoteMapper.subjectList(memIdx, startNum);

        Member mvo = memberMapper.getMemberByMemIdx(memIdx);

        // 내가 한번에 보여줄 페이지의 개수가 10개 이므로 한 블록은 10개로 정의하고, 그 블록마다의 첫 페이지와 마지막 페이지를 구해야 한다.

        // 현재가 몇 번째 블록인지를 계산해주는 부분으로써 위의 totalPage를 구하는 로직과 비슷하다.
        int currentBlock = (cPage % pageLength) == 0 ? (cPage / pageLength) : (cPage / pageLength) + 1;
        // 현재 블록의 시작 페이지를 구하는 부분(위의 startNum고 비슷한 로직) startPage 변수에 담아준다.
        int startPage = (currentBlock - 1) * pageLength + 1;
        // startPage를 잘 구했다면 endPage는 그냥 startPage에서 pageLength를 더한 후 1을 빼주면 된다.
        int endPage = startPage + pageLength - 1;

        // 이 블록의 enPage가 totalPage 보다 커지면 안 되므로 조건문을 걸어준다.
        if (endPage > totalPage) {
            endPage = totalPage;
        }


        // 두 값다 맵핑해서 view로 쏴주기(여기서 mvo로 쏴주게 되면 session에서의 mvo와 이 값의 mvo를 구별하지 못하게 되서 큰 오류가 생긴다!!!!!!!!!)
        model.addAttribute("m", m);
        model.addAttribute("subjectList", subjectList);
        // 앞으로 view단으로 뿌려주는 데이터를 맨 마지막에 한번에 보냄으로써 어떤 데이터가 넘어가는지 알기 쉽게 하겠다.
        model.addAttribute("cPage", cPage);
        model.addAttribute("totalListSize", totalListSize);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("subjectList", subjectList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "labnote/subjectList";
    }

    // 받아온 memIdx를 다시 view로 넘겨서 view에서 히든값으로 subjectWrite.do로 전송할 예정
    // 다른 사람이 새 프로젝트를 생성하는 것을 막기 위해서 session으로부터 member객체 받아오기
    @RequestMapping("/subjectWriteForm.do")
    public String subjectWriteForm(HttpServletRequest request, Model model, HttpSession session, RedirectAttributes rttr) {

        int memIdx = Integer.parseInt(request.getParameter("memIdx"));

        Member mvo = (Member) session.getAttribute("mvo");
        if (mvo.getMemIdx() != memIdx) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "다른 사람의 랩노트에 프로젝트를 생성할 수 없습니다.");
            return "redirect:/subjectList.do?memIdx="+memIdx;
        }

        model.addAttribute("memIdx", memIdx);
        return "labnote/subjectWriteForm";
    }

    @RequestMapping("/subjectWrite.do")
    public String subjectWrite(int memIdx, Subject subject, RedirectAttributes rttr, HttpSession session) {

        Member mvo = (Member) session.getAttribute("mvo");
        if (mvo.getMemIdx() != memIdx) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "다른 사람의 랩노트에 프로젝트를 생성할 수 없습니다.");
            return "redirect:/subjectList.do?memIdx="+memIdx;
        }

        // 실제 db에 저장해줌
        labnoteMapper.subjectWrite(subject);
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "프로젝트가 생성되었습니다.");
        return "redirect:/subjectList.do?memIdx="+memIdx;

    }

    @RequestMapping("/subjectUpdateForm.do")
    public String subjectUpdate(HttpServletRequest request, RedirectAttributes rttr, HttpSession session, Model model) {

        // 일단 넘어오는 subjectIdx값을 받아주기
        int subjectIdx = Integer.parseInt(request.getParameter("subjectIdx"));
        // 넘어온 SubjectIdx를 이용해서 db로부터 Subject 객체 하나 받기
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(subjectIdx);
        // 본인이 아닌 경우 바로 리턴
        Member mvo = (Member) session.getAttribute("mvo");
        if (mvo.getMemIdx() != subject.getMemIdx()) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "다른 사람의 프로젝트를 수정할 수 없습니다.");
            return "redirect:/subjectList.do?memIdx="+subject.getMemIdx();
        }

        // 수정 폼으로 리턴해줘야 하기 때문에 model에 Subject 객체 하나 담아서 줌.
        model.addAttribute("subject", subject);

        return "labnote/subjectUpdateForm";
    }

    @RequestMapping("/subjectUpdate.do")
    public String subjectUpdate(Subject subject, RedirectAttributes rttr, HttpSession session) {

        // 그냥 여기서는 바로 수정만 해주기
        labnoteMapper.subjectUpdate(subject);
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "프로젝트가 수정되었습니다.");
        return "redirect:/subjectList.do?memIdx="+subject.getMemIdx();
    }

    @RequestMapping("/subjectDelete.do")
    public String subjectDelete(HttpServletRequest request, RedirectAttributes rttr, HttpSession session) {
        // 일단 넘어오는 subjectIdx값을 받아주기
        int subjectIdx = Integer.parseInt(request.getParameter("subjectIdx"));
        // 넘어온 SubjectIdx를 이용해서 db로부터 Subject 객체 하나 받기
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(subjectIdx);
        // 본인이 아닌 경우 바로 리턴
        Member mvo = (Member) session.getAttribute("mvo");
        if (mvo.getMemIdx() != subject.getMemIdx()) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "다른 사람의 프로젝트를 삭제할 수 없습니다.");
            return "redirect:/subjectList.do?memIdx="+subject.getMemIdx();
        }

        // db에 subjectIdx를 넘기면서 삭제해달라고 요청
        labnoteMapper.subjectDelete(subjectIdx);

        return "redirect:/subjectList.do?memIdx="+subject.getMemIdx();
    }

    // ------------------------------ 여기까지 Subject 관련된 CRUD ------------------------------------------------------------------------------------

    // SubjectIdx를 받아와서 그 SubjectIdx와 일치하는 labnote리스트를 db에서 가져와서 view에 넘겨줘야 하는데 labnoteList위에 SubjectDetail도 들어가야 하니까 Subject객체도
    // SubjectIdx를 통해 하나 받아와서 같이 넘겨줌
    // 결론 : Subject 객체, List<Labnote>를 넘겨줘야 함.
    @RequestMapping("/labnoteList.do")
    public String labnoteList(HttpServletRequest request, Model model) {

        // 일단 넘어온 subjectIdx 받아주기
        int subjectIdx = Integer.parseInt(request.getParameter("subjectIdx"));

        // --------------------- 페이징 기능 추가 --------------------------------------------------------------

        // 맨 처음으로 가장 중요한 한 페이지에 몇개씩 보여줄 것인지 밑의 인덱스 개수는 어떻게 할 것인지를 결정해줄 변수인 pageLength를 선언해줌
        int pageLength = 10;

        // 총 몇개의 페이지가 필요한지 계산하려면 리스트에 속해있는 전체 객체의 갯수를 알아야 하므로 전체 객체의 갯수를 totalListSize라는 변수를 만들어 db에서 받아온 값을 저장
        int totalListSize = labnoteMapper.labnoteListNoLimit(subjectIdx).size();

        // 총 몇개의 페이지가 필요한지 알기 위해(마지막 페이지를 알아야 하므로) totalPage라는 변수에 총 페이지 갯수를 담음
        int totalPage = (totalListSize % 10) == 0 ? (totalListSize / 10) : (totalListSize / 10) + 1;
        if (totalListSize == 0) {
            totalPage = 1;
        }

        // 현재 페이지가 몇 페이지인지 알기 위하여 client 로부터 page 객체를 받아옴(현재 페이지를 담을 변수는 cPage로 설정)
        int cPage;
        String tmpPage = request.getParameter("page");

        // 처음에 페이지를 호출하게 되면 page값이 null 상태가 되므로 null처리를 해준다.
        if (tmpPage == null || tmpPage.length() == 0) {
            cPage = 1;
        }

        // 클라이언트에서 넘어오는 page 값이 엉뚱한 값이 파라미터로 넘어올 수 있기 때문에 try - catch 문으로 프로그램이 종료되는 것을 막아주도록 해야함(엉뚱한 값이 들어올 경우 cPage는 1로 설정)
        try {
            cPage = Integer.parseInt(tmpPage);
        } catch (Exception e) {
            cPage = 1;
        }

        // db에서 페이징을 적용해서 데이터를 얻으려면 LIMIT의 두 인자중 첫번째 값으로 몇 번째 인덱스부터 가져올 것인지를 정해야 하므로 그 값을 startNum이라는 변수로 정하고 거기에 담기로 함.
        int startNum = (cPage - 1) * pageLength;

        // 실제로 db에서 List<Labnote>를 받아와서 변수에 저장
        List<Labnote> labnoteList = labnoteMapper.labnoteList(subjectIdx, startNum);

        // 내가 한번에 보여줄 페이지의 개수가 10개여서 pageLength를 10으로 정했으므로 한 블록은 10개로 정의하고, 그 블록마다의 첫 페이지와 마지막 페이지를 구해야 한다.

        // 현재가 몇번째 블록인지를 계산해주는 부분(위의 totalPage와 계산방식이 비슷하다)
        int currentBlock = (cPage % pageLength) == 0 ? (cPage / pageLength) : (cPage / pageLength) + 1;

        // 현재 블록의 시작 페이지를 구해주는 부분 (위의 startNum과 비슷한 로직이다.)
        int startPage = (currentBlock - 1) * pageLength + 1;

        // 현재 블록의 마지막 페이지를 구해주는 부분 (startPage만 잘 구했다면 매우 쉽게 나온다(조건문 처리만 해주면))
        int endPage = startPage + pageLength - 1;
        if (endPage > totalPage) {
            endPage = totalPage;
        }

        // SubjectIdx를 통해 db에서 Subject객체 하나 받아오기
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(subjectIdx);

        // 프로젝트 생성자를 알기 위해서 subjectIdx를 통해 알아온 subject객체의 memIdx를 통해 Member객체를 얻어옴
        Member m = memberMapper.getMemberByMemIdx(subject.getMemIdx());

        // 둘다 model에 넣어서 view단으로 쏴주기
        model.addAttribute("subject", subject);
        model.addAttribute("labnoteList", labnoteList);
        model.addAttribute("m", m);
        model.addAttribute("cPage", cPage);
        model.addAttribute("totalListSize", totalListSize);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "labnote/labnoteList";
    }

    // subjectIdx가 넘어오므로 그걸 그대로 다시 labnoteWrite.do로 넘겨주면 된다.
    // 어떤 프로젝트인지도 표시해주면 좋을것 같으므로 Subject 객체, Member 객체도 넘겨준다.
    @RequestMapping("/labnoteWriteForm.do")
    public String labnoteWriteForm(HttpServletRequest request, Model model) {
        int subjectIdx = Integer.parseInt(request.getParameter("subjectIdx"));
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(subjectIdx);
        Member m = memberMapper.getMemberByMemIdx(subject.getMemIdx());
        model.addAttribute("subjectIdx", subjectIdx);
        model.addAttribute("subject", subject);
        model.addAttribute("m", m);
        return "labnote/labnoteWriteForm";
    }

    // subjectIdx, labnoteTitle, labnoteContent, labnoteFile이 multipartRequest에 담겨서 넘어옴(먼저 꺼내야함!)
    // 일단 NoticeController를 참고해서 파일업로드를 구현해야 함! 그리고 labnoteWriter값이 null이기 때문에 세션으로부터 불러와야 함!
    @RequestMapping("/labnoteWrite.do")
    public String labnoteWrite(RedirectAttributes rttr, HttpSession session, MultipartHttpServletRequest multipartRequest, HttpServletRequest request)
    throws Exception{

        // 고유한 파일이름을 가지기 위해 랜덤함수 생성
        Random random = new Random();
        int tmpRandomIdx = random.nextInt();
        String randomIdx = String.valueOf(tmpRandomIdx);

        String UPLOAD_DIR = "labnote_upload";
        String uploadPath = request.getServletContext().getRealPath("")+ File.separator+UPLOAD_DIR;

        // 글제목, 내용, SubjectIdx가 파라미터로 넘어옴
        Labnote labnote = new Labnote();
        String labnoteTitle = multipartRequest.getParameter("labnoteTitle");
        String labnoteComment = multipartRequest.getParameter("labnoteComment");
        int subjectIdx = Integer.parseInt(multipartRequest.getParameter("subjectIdx"));
        labnote.setSubjectIdx(subjectIdx);
        labnote.setLabnoteTitle(labnoteTitle);
        labnote.setLabnoteComment(labnoteComment);

        // db에 저장할 때 작성자가 필요하므로 Session에 저장된 Member를 얻어옴.
        Member tmpMember = (Member) session.getAttribute("mvo");
        String labnoteWriter = tmpMember.getMemName();
        labnote.setLabnoteWriter(labnoteWriter);

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

        labnote.setLabnoteFile(fileNames.trim());

        // 공지사항을 작성했으니 db에 저장
        labnoteMapper.labnoteWrite(labnote);

        ArrayList<String> emailList = memberMapper.emailList();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kitaekim077@gmail.com");
        message.setTo((String[]) emailList.toArray(new String[emailList.size()]));
        message.setSubject(labnote.getLabnoteTitle());
        message.setText(labnote.getLabnoteComment());
        emailSender.send(message);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "랩노트가 작성되었습니다.");

        return "redirect:/labnoteList.do?subjectIdx="+subjectIdx;
    }

    // 랩노트 수정폼으로 리턴해주는 함수
    // 수정권한은 프로젝트 소유자와 랩노트 작성자만이 가지고 있다(둘이 일치하는 경우는 상관없음)
    @RequestMapping("/labnoteUpdateForm.do")
    public String labnoteUpdateForm(HttpServletRequest request, Model model, HttpSession session, RedirectAttributes rttr) {

        // 넘어온 labnoteIdx를 이용해서 Labnote객체를 반환해줌
        int labnoteIdx = Integer.parseInt(request.getParameter("labnoteIdx"));
        Labnote labnote = labnoteMapper.getLabnoteByLabnoteIdx(labnoteIdx);

        // 권한 확인을 위해 labnote의 labnoteIdx를 이용해서 Subject 객체도 받아와줌
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(labnote.getSubjectIdx());

        // 세션에서 받아온 멤버가 프로젝트 생성자 이거나 랩노트 작성자가 아니라면 권한이 없기 때문에 바로 리턴
        Member tmpMember = (Member) session.getAttribute("mvo");
        if (!(tmpMember.getMemIdx() == subject.getMemIdx() || tmpMember.getMemName().equals(labnote.getLabnoteWriter()))) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "랩노트 수정 권한이 없습니다.");
            return "redirect:/labnoteList.do?subjectIdx="+labnote.getSubjectIdx();
        }

        // 어떤 사람의 프로젝트인지 확인하기 위해서 Subject의 memIdx와 일치하는 Member 객체도 하나 받아와서 view단으로 넘겨준다.
        Member m = memberMapper.getMemberByMemIdx(subject.getMemIdx());

        // Subject -> subject, Labnote -> labnote 로 view단에 뿌려줌.
        model.addAttribute("subject", subject);
        model.addAttribute("labnote", labnote);
        model.addAttribute("m", m);

        return "labnote/labnoteUpdateForm";
    }

    // 실제로 랩노트를 수정해주는 함수(Labnote 객체의 subjectIdx, labnoteTitle, labnoteComment가 넘어온다)
    @RequestMapping("/labnoteUpdate.do")
    public String labnoteUpdate(Model model, HttpSession session, RedirectAttributes rttr, Labnote labnote) {


        // 넘어온 labnote객체의 subjectIdx를 이용하여 이 프로젝트의 subject.memIdx를 얻어옴(권한 확인하기 위해서)
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(labnote.getSubjectIdx());
        Member tmpMember = (Member) session.getAttribute("mvo");
        if (!(subject.getMemIdx() == tmpMember.getMemIdx() || labnote.getLabnoteWriter().equals(tmpMember.getMemName()))) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "랩노트 수정 권한이 없습니다.");
            return "redirect:/labnoteList.do?subjectIdx="+labnote.getSubjectIdx();
        }

        labnoteMapper.labnoteUpdate(labnote);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "랩노트 수정이 완료되었습니다.");

        return "redirect:/labnoteList.do?subjectIdx="+labnote.getSubjectIdx();
    }

    // 실제로 랩노트를 삭제해주는 부분
    @RequestMapping("/labnoteDelete.do")
    public String labnoteDelete(HttpServletRequest request, RedirectAttributes rttr, HttpSession session) {

        // 파라미터로 넘어오는 labnoteIdx를 이용해서 Labnote 객체를 얻어옴(권한 확인을 위해서 Session으로부터 Member tmpMember, Subject subject도 얻어옴)
        int labnoteIdx = Integer.parseInt(request.getParameter("labnoteIdx"));
        Labnote labnote = labnoteMapper.getLabnoteByLabnoteIdx(labnoteIdx);
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(labnote.getSubjectIdx());
        Member tmpMember = (Member) session.getAttribute("mvo");

        // 실제로 권한을 확인해주는 파트
        if (!(subject.getMemIdx() == tmpMember.getMemIdx() || labnote.getLabnoteWriter().equals(tmpMember.getMemName()))) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "랩노트 삭제 권한이 없습니다.");
            return "redirect:/labnoteList.do?subjectIdx="+labnote.getSubjectIdx();
        }

        labnoteMapper.labnoteDelete(labnoteIdx);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "랩노트 삭제가 완료되었습니다.");

        return "redirect:/labnoteList.do?subjectIdx="+labnote.getSubjectIdx();
    }




    // --------------------------   여기까지가 랩노트 CRUD -------------------------------------------------------------------------


    // 실제 labnote의 Primary Key인 labnoteIdx, 화면에서 보여주는 용도인 labnoteIndex가 넘어온다.
    @RequestMapping("/labnoteDetail.do")
    public String labnoteDetail(HttpServletRequest request, Model model, HttpSession session) {

        // 실제 Primary Key인 labnoteIdx를 먼저 받아준다.
        int labnoteIdx = Integer.parseInt(request.getParameter("labnoteIdx"));

        // ---------------- LabnoteController를 참고해서 조회수 증가 로직을 먼저 만들어줘야 한다!!!!!!!! --------------------------------------------------------

        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String s = attributeNames.nextElement();
            if (s.equals("visitCheckLabnoteIdx"+labnoteIdx)) {
                // labnoteIdx를 통해 Labnote 객체를 db로부터 얻어온다.
                Labnote labnote = labnoteMapper.getLabnoteByLabnoteIdx(labnoteIdx);

                // view 단에 넘겨줄 용도인 Subject subject도 db로부터 얻어온다.
                Subject subject = labnoteMapper.getSubjectBySubjectIdx(labnote.getSubjectIdx());

                // labnoteIdx를 이용해서 댓글 목록을 받아온다.
                List<LabnoteRepl> labnoteReplList = labnoteMapper.labnoteReplList(labnoteIdx);

                // 누구의 프로젝트인지 알기 위해서 Subject 객체의 memIdx를 통해 Member 객체를 얻어온다.
                Member m = memberMapper.getMemberByMemIdx(subject.getMemIdx());

                // 모두 모델에 담아서 view단으로 쏴준다.
                model.addAttribute("labnote", labnote);
                model.addAttribute("subject", subject);

                model.addAttribute("labnoteReplList", labnoteReplList);
                model.addAttribute("m", m);

                return "labnote/labnoteDetail";
            }
        }

        session.setAttribute("visitCheckLabnoteIdx"+labnoteIdx, true);


        // labnoteIdx를 통해 Labnote 객체를 db로부터 얻어온다.
        Labnote labnote = labnoteMapper.getLabnoteByLabnoteIdxCountPlus(labnoteIdx);

        // view 단에 넘겨줄 용도인 Subject subject도 db로부터 얻어온다.
        Subject subject = labnoteMapper.getSubjectBySubjectIdx(labnote.getSubjectIdx());


        // labnoteIdx를 이용해서 댓글 목록을 받아온다.
        List<LabnoteRepl> labnoteReplList = labnoteMapper.labnoteReplList(labnoteIdx);

        // 누구의 프로젝트인지 알기 위해서 Subject 객체의 memIdx를 통해 Member 객체를 얻어온다.
        Member m = memberMapper.getMemberByMemIdx(subject.getMemIdx());

        // 모두 모델에 담아서 view단으로 쏴준다.
        model.addAttribute("labnote", labnote);
        model.addAttribute("subject", subject);
        model.addAttribute("labnoteReplList", labnoteReplList);
        model.addAttribute("m", m);

        return "labnote/labnoteDetail";
    }

    @RequestMapping("/labnoteFileDownload.do")
    public void labnoteFileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 파라미터로 넘어오는 filename을 받아줌
        String filename = request.getParameter("filename");

        // 파일이 업로드된 디렉토리명을 적어줌.
        String UPLOAD_DIR = "labnote_upload";
        // 바로 위에서 선언한 UPLOAD_DIR을 이용해서 실제 파일이 있는곳을 가르켜주는 변수를 하나 생성
        String uploadPath = request.getServletContext().getRealPath("")+File.separator+UPLOAD_DIR;
        // 실제 파일 포인터
        File f = new File(uploadPath + "\\" + filename);

        filename = filename.replace("+", " ");

        // 파일을 다운로드 받을 수 있도록 클라이언트에게 알려주는 역할?
        response.setContentLength((int) f.length());
        response.setContentType("application/x-msdownload;charset=UTF-8");

        // 파일 이름이 중복되는 것을 막기 위해서 앞에 붙였던 숫자들을 제거
        String[] split = filename.split("`");
        List<String> noticeFile = new ArrayList<>(Arrays.asList(split));

        // 파일이름을 스플릿해서 noticeFile이라는 배열에 담았는데 인코딩을 해주지 않으면 다운로드 할 수 없으므로 인코딩을 해주는데, 앞에 붙은 숫자는 때버리고, 뒤의 파일이름만 인코딩해서 넘겨준다.
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(noticeFile.get(1), StandardCharsets.UTF_8) + ";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        // 파일을 실제로 다운로드 할 수 있게 FileInputStream에 넣어줌
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

    // 랩노트에 댓글 달기
    @RequestMapping("/labnoteReplWrite.do")
    public String labnoteReplWrite(HttpServletRequest request, HttpSession session, RedirectAttributes rttr) {

        // 일단 넘어오는 labnoteIdx, labnoteReplContent, labnoteIndex(다시 labnoteDetail페이지로 리턴해줘야함) 들을 받아줌
        int labnoteIndex = request.getIntHeader("labnoteIndex");
        int labnoteIdx = Integer.parseInt(request.getParameter("labnoteIdx"));
        String labnoteReplContent = request.getParameter("labnoteReplContent");

        // 넘어온 labnoteIdx를 통해 Labnote 객체를 db에서 불러오기
        Labnote labnote = labnoteMapper.getLabnoteByLabnoteIdx(labnoteIdx);

        // 일단 db에 저장할 LabnoteRepl 객체 생성
        LabnoteRepl labnoteRepl = new LabnoteRepl();
        labnoteRepl.setLabnoteReplContent(labnoteReplContent);
        labnoteRepl.setLabnoteIdx(labnote.getLabnoteIdx());
        labnoteRepl.setSubjectIdx(labnote.getSubjectIdx());

        // labnoteRepl 의 labnoteReplWriter를 지정해줘야 하므로 session에서 현재 로그인되어있는 멤버의 정보를 받아옴
        Member tmpMember = (Member) session.getAttribute("mvo");
        labnoteRepl.setLabnoteReplWriter(tmpMember.getMemName());

        // 이제 labnoteRepl에 필요한 값이 모두 들어갔으므로 db로 보내 저장해주기
        labnoteMapper.labnoteReplWrite(labnoteRepl);

        // 댓글 작성 성공 메세지를 띄워주고, 다시 그 전 페이지로 리턴해줌.
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "댓글이 작성되었습니다.");

        return "redirect:/labnoteDetail.do?labnoteIdx="+labnoteIdx+"&labnoteIndex="+labnoteIndex;
    }



    // ----------------------------------------------------------------------------------------------------------------------------------------------------

    // header.jsp 에서 '랩노트' 항목을 클릭하면 모든 랩노트들이 시간 순서대로 정렬되서 출력되도록 함
    @RequestMapping("/allLabnoteList.do")
    public String allLabnoteList(Model model) {

        // db로부터 전체 랩노트 리스트를 받아오기
        List<Labnote> allLabnoteList = labnoteMapper.allLabnoteList();
        model.addAttribute("labnoteList", allLabnoteList);

        return "labnote/allLabnoteList";
    }
}
