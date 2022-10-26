package scnu.able.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scnu.able.myapp.dao.MemberMapper;
import scnu.able.myapp.vo.member.Member;
import scnu.able.myapp.vo.member.OldMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Controller
public class MemberController {

    @Autowired
    private MemberMapper memberMapper;

    // -------------------------------- 회원 가입 --------------------------------------------------------------------------------

    // 회원가입 폼으로 이동
    @RequestMapping("/memJoinForm.do")
    public String memJoin() {
        return "member/memJoinForm";
    }

    // 아이디 중복체크
    @RequestMapping("/memIdCheck.do")
    public @ResponseBody int memIdCheck(@RequestParam("memID") String memID) {

        Member m = memberMapper.memIdCheck(memID);
        if (m != null) {
            return 0;
        }

        return 1;
    }

    // 이름 중복체크
    @RequestMapping("/memNameCheck.do")
    public @ResponseBody int memNameCheck(@RequestParam("memName") String memName) {

        System.out.println(memName);

        Member m = memberMapper.getMemberByMemName(memName);
        if (m != null) {
            return 0;
        }

        return 1;
    }

    // 회원 가입 유효성 확인 및 db저장
    @RequestMapping("/memJoin.do")
    public String memJoin(Member m, RedirectAttributes rttr, String memPassword1, String memPassword2) {

        System.out.println(m.toString());

        // 아이디와 이름 중복체크 안됬을 경우 다시 체크(tmpMember는 임시변수)
        Member tmpMember = memberMapper.memIdCheck(m.getMemID());
        if (tmpMember != null) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "아이디가 중복됩니다. 다시 확인하세요.");

            return "redirect:/memJoinForm.do";
        }

        tmpMember = memberMapper.memNameCheck(m.getMemName());
        if (tmpMember != null) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "이름이 중복됩니다. 다시 확인하세요.");

            return "redirect:/memJoinForm.do";
        }


        // 누락된 값이 있는 경우 되돌려보내기
        if (m.getMemID() == null || m.getMemID().equals("") || m.getMemName() == null || m.getMemName().equals("") || m.getMemEmail() == null ||
                m.getMemEmail().equals("") || memPassword1 == null || memPassword1.equals("") || memPassword2 == null || memPassword2.equals("")) {
            rttr.addFlashAttribute("msgType", "누락 메세지");
            rttr.addFlashAttribute("msg", "모든 내용을 입력하세요");

            return "redirect:/memJoinForm.do";
        }

        // 암호화를 할 수 있는 클래스 임포트
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securePw = encoder.encode(memPassword1);


        m.setMemProfile(null);
        m.setMemPassword(securePw);


        // 모든 조건을 통과했으니 db연동해주기.
        int result = memberMapper.memJoin(m);
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "가입에 성공했습니다. 교수님께 승인을 요청하세요.");

        return "redirect:/memLoginForm.do";
    }


    // --------------------------------------  로그인 파트 시작 ------------------------------------------------------------------------
    //  유저가 로그인 시도를 하면 관리자 페이지에서 줄 수 있는 권한이 있는지 여부를 판단하여 없으면 /(root)로 튕기게 만듬.

    // 로그인 폼
    @RequestMapping("/memLoginForm.do")
    public String memLoginForm() {
        return "member/memLoginForm";
    }

    @RequestMapping("/memLogin.do")
    public String memLogin(Member m, RedirectAttributes rttr, HttpSession session) {

        // db로부터 입력된 아이디를 가진 유저가 있는지를 가져옴
        Member mvo = memberMapper.memLogin(m.getMemID());

        // Member가 존재하지 않을 경우 로그인 실패
        if (mvo == null) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "아이디가 존재하지 않습니다.");

            return "redirect:/memLoginForm.do";
        }

        // 패스워드 복호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Member가 존재하지만 비밀번호를 잘못 입력한 경우
        if (!encoder.matches(m.getMemPassword(), mvo.getMemPassword())) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "아이디와 비밀번호를 다시 확인하세요.");

            return "redirect:/memLoginForm.do";
        }

        // id와 비밀번호가 모두 맞지만 아직 승인을 받지 못한 경우
        if (mvo.getMemAuth() == null) {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "교수님께 승인을 부탁드리세요.");

            return "redirect:/memLoginForm.do";
        }

        // 로그인 성공
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "로그인에 성공했습니다.");
        session.setAttribute("mvo", mvo);

        return "redirect:/";
    }

    // -------------------------------------  로그아웃 및 회원정보 수정 ---------------------------------------------------------------


    // 로그아웃
    @RequestMapping("/memLogout.do")
    public String memLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    // 회원정보수정 폼 리다이렉트
    @RequestMapping("/memUpdateForm.do")
    public String memUpdateForm() {
        return "member/memUpdateForm";
    }


    // 회원정보수정
    @RequestMapping("/memUpdate.do")
    public String memUpdate(Member m, RedirectAttributes rttr, String memPassword1, String memPassword2, HttpSession session) {


        // 누락된 값이 있는 경우 되돌려보내기
        if (m.getMemID() == null || m.getMemID().equals("") || m.getMemName() == null || m.getMemName().equals("") || m.getMemEmail() == null ||
                m.getMemEmail().equals("") || memPassword1 == null || memPassword1.equals("") || memPassword2 == null || memPassword2.equals("")) {
            rttr.addFlashAttribute("msgType", "누락 메세지");
            rttr.addFlashAttribute("msg", "모든 내용을 입력하세요");

            return "redirect:/memUpdateForm.do";
        }

        // 패스워드 암호화를 위한 클래스 임포트
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securePw = encoder.encode(memPassword1);


        m.setMemPassword(securePw);
        int result = memberMapper.memUpdate(m);
        if (result == 1) {
            rttr.addFlashAttribute("msgType", "성공 메세지");
            rttr.addFlashAttribute("msg", "회원 정보 수정에 성공했습니다.");
            session.setAttribute("mvo", m);
            return "redirect:/";
        } else {
            rttr.addFlashAttribute("msgType", "실패 메세지");
            rttr.addFlashAttribute("msg", "회원수정 서버 오류.");
            return "redirect:/";
        }
    }

    // 멤버 프로필 사진 업데이트
    @RequestMapping("/memImageUpdate.do")
    public String memImageUpdate(HttpServletRequest request, RedirectAttributes rttr, HttpSession session, String memName, MultipartHttpServletRequest multipartRequest)
    throws IOException{

        // 클라이언트에서 잘 넘어오나 테스트
        System.out.println(memName);


        // 고유한 파일이름을 가지기 위해 랜덤함수 생성
        Random random = new Random();
        int tmpRandomIdx = random.nextInt();
        String randomIdx = String.valueOf(tmpRandomIdx);

        String UPLOAD_DIR = "member_profile";
        String uploadPath = request.getServletContext().getRealPath("") + File.separator+UPLOAD_DIR;

        // db에 저장할 때 작성자가 필요하므로 클라이언트에서 hidden값으로 넘어온 memName을 이용하여 Member객체 생성
        Member m = memberMapper.nameGetMember(memName);


        // 파일을 담고있는 파라미터 읽어오기
        Iterator<String> it = multipartRequest.getFileNames();
        String memberProfile = "";
        while (it.hasNext()) {
            String paramfName = it.next();
            MultipartFile mFile = multipartRequest.getFile(paramfName);
            String oName = mFile.getOriginalFilename();
            memberProfile += randomIdx + oName;
            File file = new File(uploadPath + "\\" + paramfName);
            if (mFile.getSize() != 0) {
                if (!file.exists()) {
                    if (file.getParentFile().mkdirs()) {
                        file.createNewFile();
                    }
                }
                mFile.transferTo(new File(uploadPath + "\\" + randomIdx + oName));
            }
        }
        m.setMemProfile(memberProfile);
        memberMapper.memProfileUpdate(m);
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "사진 변경이 완료되었습니다.");
        session.invalidate();

        return "redirect:/";
    }

    // ---------------------- 관리자 페이지(회원관리) -----------------------------------------------------------------------

    @RequestMapping("/memAdmin.do")
    public String memAdmin(Model model, RedirectAttributes rttr) {

        // 승인 대기중인 회원 모델에 담아주기
        List<Member> noAuthList = memberMapper.getNoAuthList();
        model.addAttribute("noAuthList", noAuthList);

        // 승인된 회원 모델에 담아주기
        List<Member> authList = memberMapper.getAuthList();
        model.addAttribute("authList", authList);

        List<Member> oldMemberList = memberMapper.getOldMemberList();
        model.addAttribute("oldMemberList", oldMemberList);

        return "member/memAdminForm";


    }

    // 회원에게 권한 부여(Ajax 이용)
    @RequestMapping("/memAuth.do") public @ResponseBody void memAuth(@RequestParam("memName") String memName) {memberMapper.memAuth(memName);}

    // 권한 없는 회원 삭제(Ajax 이용)
    @RequestMapping("/memNoAuthDelete.do") public @ResponseBody void memNoAuthDelete(@RequestParam("memName") String memName) {memberMapper.memNoAuthDelete(memName);}



    // 랩노트를 보여주기 위해서 멤버 리스트를 담아서 보내주기(Ajax 이용)
    @RequestMapping("/memberList.do")
    public @ResponseBody List<Member> memberList() {return memberMapper.getAuthList();}

    @RequestMapping("/oldMemberList.do")
    public @ResponseBody List<Member> oldMemberList() {return memberMapper.getOldMemberList();}

    @RequestMapping("/allMemberList.do")
    public String allMemberList(Model model, RedirectAttributes rttr) {


        // 승인된 회원 모델에 담아주기
        List<Member> authList = memberMapper.getAuthList();
        model.addAttribute("authList", authList);

        List<Member> oldMemberList = memberMapper.getOldMemberList();
        model.addAttribute("oldMemberList", oldMemberList);

        return "member/allMemberList";


    }




}
