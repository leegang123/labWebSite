package scnu.able.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import scnu.able.myapp.dao.MemberChangeMapper;
import scnu.able.myapp.dao.MemberMapper;
import scnu.able.myapp.vo.member.Member;
import scnu.able.myapp.vo.member.OldMember;

import javax.servlet.http.HttpServletRequest;

// 전멤버 <-> 현멤버 전환 로직을 위해 만들어진 컨트롤러
@Controller
public class MemberChangeController {

    @Autowired
    private MemberChangeMapper memberChangeMapper;

    @Autowired
    private MemberMapper memberMapper;

    @RequestMapping("/memChange.do")
    public String memChange(HttpServletRequest request) {
        String tmpMemIdx = request.getParameter("memIdx");
        String tmpOldMemIdx = request.getParameter("oldMemIdx");

        if (tmpMemIdx == null || tmpMemIdx.length() == 0) {

            int oldMemIdx = Integer.parseInt(tmpOldMemIdx);

            memberMapper.memStatusUpdate(oldMemIdx);
        } else {
            int memIdx = Integer.parseInt(tmpMemIdx);


            memberMapper.memStatusDelete(memIdx);
        }

        return "redirect:/memAdmin.do";
    }
}
