package scnu.able.myapp.vo.cost;

import lombok.Data;

@Data
public class Shop {
    private int shopIdx;
    private String shopTitle;
    private String shopComment;
    private int shopBalance;

    @Override
    public String toString() {
        return "Shop{" +
                "shopIdx=" + shopIdx +
                ", shopTitle='" + shopTitle + '\'' +
                ", shopComment='" + shopComment + '\'' +
                ", shopBalance=" + shopBalance +
                '}';
    }

    public int getShopIdx() {
        return shopIdx;
    }

    public void setShopIdx(int shopIdx) {
        this.shopIdx = shopIdx;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getShopComment() {
        return shopComment;
    }

    public void setShopComment(String shopComment) {
        this.shopComment = shopComment;
    }

    public int getShopBalance() {
        return shopBalance;
    }

    public void setShopBalance(int shopBalance) {
        this.shopBalance = shopBalance;
    }

    public Shop() {
    }

    public Shop(int shopIdx, String shopTitle, String shopComment, int shopBalance) {
        this.shopIdx = shopIdx;
        this.shopTitle = shopTitle;
        this.shopComment = shopComment;
        this.shopBalance = shopBalance;
    }
}
