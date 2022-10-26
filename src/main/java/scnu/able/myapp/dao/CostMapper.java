package scnu.able.myapp.dao;

import org.apache.ibatis.annotations.Mapper;
import scnu.able.myapp.vo.cost.Cost;
import scnu.able.myapp.vo.cost.Shop;

import java.util.List;

@Mapper
public interface CostMapper {


    List<Shop> shopList();

    void shopWrite(Shop shop);

    void shopDelete(int shopIdx);

    Shop getShopByShopIdx(int shopIdx);

    List<Cost> costListNoLimit(int shopIdx);

    List<Cost> costList(int shopIdx, int startNum);

    void costDeposit(Cost cost);

    Cost getCostByCostIdx(int costIdx);

    void costDepositDelete(Cost cost);

    void costWithdraw(Cost cost);

    void costWithdrawDelete(Cost cost);
}
