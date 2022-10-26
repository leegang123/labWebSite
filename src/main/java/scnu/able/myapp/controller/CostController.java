package scnu.able.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scnu.able.myapp.dao.CostMapper;
import scnu.able.myapp.vo.cost.Cost;
import scnu.able.myapp.vo.cost.Shop;
import scnu.able.myapp.vo.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CostController {

    @Autowired
    private CostMapper costMapper;

    @RequestMapping("/shopList.do")
    public String shopList(Model model) {

        List<Shop> shopList = costMapper.shopList();
        model.addAttribute("shopList", shopList);
        return "cost/shopList";
    }

    @RequestMapping("/shopWriteForm.do")
    public String shopWriteForm() {return "cost/shopWriteForm";}

    @RequestMapping("/shopWrite.do")
    public String shopWrite(Shop shop, RedirectAttributes rttr) {
        costMapper.shopWrite(shop);
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "가게가 등록되었습니다.");

        return "redirect:/shopList.do";
    }

    @RequestMapping("/shopDelete.do")
    public String shopDelete(HttpServletRequest request, RedirectAttributes rttr) {
        int shopIdx = Integer.parseInt(request.getParameter("shopIdx"));
        costMapper.shopDelete(shopIdx);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "가게가 삭제되었습니다.");

        return "redirect:/shopList.do";
    }
    @RequestMapping("/costList.do")
    public String costList(HttpServletRequest request, Model model) {

        int shopIdx = Integer.parseInt(request.getParameter("shopIdx"));
        Shop shop = costMapper.getShopByShopIdx(shopIdx);

        // 맨 처음으로 가중 중요한 한 페이지에 몇개씩 보여줄 것인지, 밑의 인덱스 개수를 어떻게 할 것인지를 결정해줄 변수인 pageLength를 사용해줌
        int pageLength = 10;

        // 총 몇개의 페이지가 필요한지 계산하려면 리스트에 속해있는 전체 객체의 갯수를 알아야 하므로 전체 객체의 갯수를 totalListSize 라는 변수를 만들어 db에서 받아온 값을 저장
        int totalListSize = costMapper.costListNoLimit(shopIdx).size();

        // 총 몇개의 페이지가 필요한지 알기 위해(마지막 페이지를 알아야 하므로) totalPage 라는 변수에 총 페이지 갯수를 담음
        int totalPage = (totalListSize % pageLength) == 0 ? (totalListSize / pageLength) : (totalListSize / pageLength) + 1;
        if (totalListSize == 0) {
            totalPage = 1;
        }

        // 현재 페이지가 몇 페이지인지 알기 위하여 view단에서 page 객체를 받아옴(현재 페이지를 담을 변수는 cPage로 설정)
        int cPage;
        String tmpPage = request.getParameter("page");

        // 처음에 페이지를 호출하게 되면 page값이 null 상태가 되므로 null처리를 해줌
        if (tmpPage == null || tmpPage.length() == 0) {
            cPage = 1;
        }

        // 클라이언트에서 넘어오는 page 값이 엉뚱한 값이 파라미터로 넘어올 수 있기 때문에 try - catch 문으로 프로그램이 종료되는 것을 막아주도록 해야함(엉뚱한 값이 넘어올 경우 cPage = 1)
        try {
            cPage = Integer.parseInt(tmpPage);
        } catch (Exception e) {
            cPage = 1;
        }

        // db에서 페이징을 적용해서 데이터를 얻으려면 LIMIT의 두 인자중 첫번째 값으로 몇 번째 인덱스부터 가져올 것인지를 정해야 하므로 그 값을 startNum 이라는 변수로 정하고 거기에 담기로 함.
        int startNum = (cPage - 1) * pageLength;

        // 실제로 db에서 List<Cost>를 받아와서 변수에 저장
        List<Cost> costList = costMapper.costList(shopIdx, startNum);

        // 내가 한번에 보여줄 페이지의 개수가 10개이므로 pageLength를 10으로 정했으니, 한 블록은 10개로 정의하고, 그 블록마다의 첫 페이지와 마지막 페이지를 구해야 함.

        // 현재가 몇번째 블록인지를 구해주는 부분 (위의 totalPage와 비슷한 계산방식)
        int currentBlock = (cPage % pageLength) == 0 ? (cPage / pageLength) : (cPage / pageLength) + 1;

        // 현재 블록의 시작 페이지를 구해주는 부분(위의 startNum과 비슷한 로직)
        int startPage = (currentBlock - 1) * pageLength + 1;

        // 현재 블록의 마지막 페이지를 구해주는 부분 (startPage만 잘 구했다면 매우 쉽게 나온다)
        int endPage = startPage + pageLength - 1;
        if (endPage > totalPage) {
            endPage = totalPage;
        }


        model.addAttribute("cPage", cPage);
        model.addAttribute("totalListSize", totalListSize);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("shop", shop);
        model.addAttribute("costList", costList);

        return "cost/costList";
    }

    @RequestMapping("/costDepositForm.do")
    public String costDepositForm(HttpServletRequest request, Model model) {
        int shopIdx = Integer.parseInt(request.getParameter("shopIdx"));
        Shop shop = costMapper.getShopByShopIdx(shopIdx);
        model.addAttribute("shop", shop);
        return "cost/costDepositForm";
    }

    @RequestMapping("/costDeposit.do")
    public String costDeposit(HttpServletRequest request, RedirectAttributes rttr, HttpSession session) {

        int shopIdx = Integer.parseInt(request.getParameter("shopIdx"));
        int costBalance = Integer.parseInt(request.getParameter("costBalance"));

        Member tmpMember = (Member) session.getAttribute("mvo");
        String costWriter = tmpMember.getMemName();

        Cost cost = new Cost();
        cost.setCostWriter(costWriter);
        cost.setCostBalance(costBalance);
        cost.setShopIdx(shopIdx);

        costMapper.costDeposit(cost);

        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "입금이 완료되었습니다.");

        return "redirect:/costList.do?shopIdx="+cost.getShopIdx();
    }

    @RequestMapping("/costWithdrawForm.do")
    public String costWithdrawForm(HttpServletRequest request, Model model) {
        int shopIdx = Integer.parseInt(request.getParameter("shopIdx"));
        Shop shop = costMapper.getShopByShopIdx(shopIdx);
        model.addAttribute("shop", shop);
        return "cost/costWithdrawForm";
    }

    @RequestMapping("/costWithdraw.do")
    public String costWithdraw(HttpServletRequest request, RedirectAttributes rttr, HttpSession session) {
        int shopIdx = Integer.parseInt(request.getParameter("shopIdx"));
        String costTitle = request.getParameter("costTitle");
        int costBalance = Integer.parseInt(request.getParameter("costBalance"));

        Member tmpMember = (Member) session.getAttribute("mvo");

        Cost cost = new Cost();
        cost.setShopIdx(shopIdx);
        cost.setCostTitle(costTitle);
        cost.setCostBalance(costBalance);
        cost.setCostWriter(tmpMember.getMemName());

        costMapper.costWithdraw(cost);
        rttr.addFlashAttribute("msgType", "성공 메세지");
        rttr.addFlashAttribute("msg", "구매 등록이 완료되었습니다.");

        return "redirect:/costList.do?shopIdx="+cost.getShopIdx();
    }



    @RequestMapping("/costDelete.do")
    public String costDelete(HttpServletRequest request, RedirectAttributes rttr) {
        String[] costArr = request.getParameter("cost").split(",");

        String costType = costArr[0];
        int costIdx = Integer.parseInt(costArr[1]);
        Cost cost = costMapper.getCostByCostIdx(costIdx);

        if (costType.equals("deposit")) {
            costMapper.costDepositDelete(cost);
            rttr.addFlashAttribute("msgType", "성공 메세지");
            rttr.addFlashAttribute("msg", "입금내역이 삭제되었습니다.");
        } else {
            costMapper.costWithdrawDelete(cost);
            rttr.addFlashAttribute("msgType", "성공 메세지");
            rttr.addFlashAttribute("msg", "구매내역이 삭제되었습니다.");
        }

        return "redirect:/costList.do?shopIdx="+cost.getShopIdx();

    }
}
































