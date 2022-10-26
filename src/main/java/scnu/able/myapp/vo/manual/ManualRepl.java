package scnu.able.myapp.vo.manual;

import lombok.Data;

@Data
public class ManualRepl {

    private int manualIdx;
    private int manualReplIdx;
    private String manualReplContent;
    private String manualReplWriter;
    private String manualReplIndate;

    public int getManualIdx() {
        return manualIdx;
    }

    public void setManualIdx(int manualIdx) {
        this.manualIdx = manualIdx;
    }

    public int getManualReplIdx() {
        return manualReplIdx;
    }

    public void setManualReplIdx(int manualReplIdx) {
        this.manualReplIdx = manualReplIdx;
    }

    public String getManualReplContent() {
        return manualReplContent;
    }

    public void setManualReplContent(String manualReplContent) {
        this.manualReplContent = manualReplContent;
    }

    public String getManualReplWriter() {
        return manualReplWriter;
    }

    public void setManualReplWriter(String manualReplWriter) {
        this.manualReplWriter = manualReplWriter;
    }

    public String getManualReplIndate() {
        return manualReplIndate;
    }

    public void setManualReplIndate(String manualReplIndate) {
        this.manualReplIndate = manualReplIndate;
    }

    public ManualRepl() {
    }

    public ManualRepl(int manualIdx, int manualReplIdx, String manualReplContent, String manualReplWriter, String manualReplIndate) {
        this.manualIdx = manualIdx;
        this.manualReplIdx = manualReplIdx;
        this.manualReplContent = manualReplContent;
        this.manualReplWriter = manualReplWriter;
        this.manualReplIndate = manualReplIndate;
    }
}
