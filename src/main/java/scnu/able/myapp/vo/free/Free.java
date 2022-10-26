package scnu.able.myapp.vo.free;

import lombok.Data;

@Data
public class Free {
    private int freeIdx;
    private String freeTitle;
    private String freeContent;
    private String freeWriter;
    private String freeIndate;
    private String freeFile;

    @Override
    public String toString() {
        return "Free{" +
                "freeIdx=" + freeIdx +
                ", freeTitle='" + freeTitle + '\'' +
                ", freeContent='" + freeContent + '\'' +
                ", freeWriter='" + freeWriter + '\'' +
                ", freeIndate='" + freeIndate + '\'' +
                ", freeFile='" + freeFile + '\'' +
                '}';
    }

    public Free() {
    }

    public Free(int freeIdx, String freeTitle, String freeContent, String freeWriter, String freeIndate, String freeFile) {
        this.freeIdx = freeIdx;
        this.freeTitle = freeTitle;
        this.freeContent = freeContent;
        this.freeWriter = freeWriter;
        this.freeIndate = freeIndate;
        this.freeFile = freeFile;
    }

    public int getFreeIdx() {
        return freeIdx;
    }

    public void setFreeIdx(int freeIdx) {
        this.freeIdx = freeIdx;
    }

    public String getFreeTitle() {
        return freeTitle;
    }

    public void setFreeTitle(String freeTitle) {
        this.freeTitle = freeTitle;
    }

    public String getFreeContent() {
        return freeContent;
    }

    public void setFreeContent(String freeContent) {
        this.freeContent = freeContent;
    }

    public String getFreeWriter() {
        return freeWriter;
    }

    public void setFreeWriter(String freeWriter) {
        this.freeWriter = freeWriter;
    }

    public String getFreeIndate() {
        return freeIndate;
    }

    public void setFreeIndate(String freeIndate) {
        this.freeIndate = freeIndate;
    }

    public String getFreeFile() {
        return freeFile;
    }

    public void setFreeFile(String freeFile) {
        this.freeFile = freeFile;
    }
}
