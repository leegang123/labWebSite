package scnu.able.myapp.vo.cost;


import lombok.Data;

@Data
public class Cost {

    private int shopIdx;
    private int costIdx;
    private String costTitle;
    private int costBalance;
    private String costWriter;
    private String costIndate;

    @Override
    public String toString() {
        return "Cost{" +
                "shopIdx=" + shopIdx +
                ", costIdx=" + costIdx +
                ", costTitle='" + costTitle + '\'' +
                ", costBalance=" + costBalance +
                ", costWriter='" + costWriter + '\'' +
                ", costIndate='" + costIndate + '\'' +
                '}';
    }

    public int getShopIdx() {
        return shopIdx;
    }

    public void setShopIdx(int shopIdx) {
        this.shopIdx = shopIdx;
    }

    public int getCostIdx() {
        return costIdx;
    }

    public void setCostIdx(int costIdx) {
        this.costIdx = costIdx;
    }

    public String getCostTitle() {
        return costTitle;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public int getCostBalance() {
        return costBalance;
    }

    public void setCostBalance(int costBalance) {
        this.costBalance = costBalance;
    }

    public String getCostWriter() {
        return costWriter;
    }

    public void setCostWriter(String costWriter) {
        this.costWriter = costWriter;
    }

    public String getCostIndate() {
        return costIndate;
    }

    public void setCostIndate(String costIndate) {
        this.costIndate = costIndate;
    }

    public Cost() {
    }

    public Cost(int shopIdx, int costIdx, String costTitle, int costBalance, String costWriter, String costIndate) {
        this.shopIdx = shopIdx;
        this.costIdx = costIdx;
        this.costTitle = costTitle;
        this.costBalance = costBalance;
        this.costWriter = costWriter;
        this.costIndate = costIndate;
    }
}
