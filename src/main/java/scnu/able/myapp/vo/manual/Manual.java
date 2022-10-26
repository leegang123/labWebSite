package scnu.able.myapp.vo.manual;

import lombok.Data;

@Data
public class Manual {
    private int manualIdx;
    private String manualTitle;
    private String manualContent;
    private String manualWriter;
    private String manualIndate;
    private String manualFile;

    public int getManualIdx() {
        return manualIdx;
    }

    public void setManualIdx(int manualIdx) {
        this.manualIdx = manualIdx;
    }

    public String getManualTitle() {
        return manualTitle;
    }

    public void setManualTitle(String manualTitle) {
        this.manualTitle = manualTitle;
    }

    public String getManualContent() {
        return manualContent;
    }

    public void setManualContent(String manualContent) {
        this.manualContent = manualContent;
    }

    public String getManualWriter() {
        return manualWriter;
    }

    public void setManualWriter(String manualWriter) {
        this.manualWriter = manualWriter;
    }

    public String getManualIndate() {
        return manualIndate;
    }

    public void setManualIndate(String manualIndate) {
        this.manualIndate = manualIndate;
    }

    public String getManualFile() {
        return manualFile;
    }

    public void setManualFile(String manualFile) {
        this.manualFile = manualFile;
    }

    public Manual() {
    }

    public Manual(int manualIdx, String manualTitle, String manualContent, String manualWriter, String manualIndate, String manualFile) {
        this.manualIdx = manualIdx;
        this.manualTitle = manualTitle;
        this.manualContent = manualContent;
        this.manualWriter = manualWriter;
        this.manualIndate = manualIndate;
        this.manualFile = manualFile;
    }
}
