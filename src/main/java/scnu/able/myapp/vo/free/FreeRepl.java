package scnu.able.myapp.vo.free;

import lombok.Data;

@Data
public class FreeRepl {

    private int freeIdx;
    private int freeReplIdx;
    private String freeReplContent;
    private String freeReplWriter;
    private String freeReplIndate;

    @Override
    public String toString() {
        return "FreeRepl{" +
                "freeIdx=" + freeIdx +
                ", freeReplIdx=" + freeReplIdx +
                ", freeReplContent='" + freeReplContent + '\'' +
                ", freeReplWriter='" + freeReplWriter + '\'' +
                ", freeReplIndate='" + freeReplIndate + '\'' +
                '}';
    }

    public int getFreeIdx() {
        return freeIdx;
    }

    public void setFreeIdx(int freeIdx) {
        this.freeIdx = freeIdx;
    }

    public int getFreeReplIdx() {
        return freeReplIdx;
    }

    public void setFreeReplIdx(int freeReplIdx) {
        this.freeReplIdx = freeReplIdx;
    }

    public String getFreeReplContent() {
        return freeReplContent;
    }

    public void setFreeReplContent(String freeReplContent) {
        this.freeReplContent = freeReplContent;
    }

    public String getFreeReplWriter() {
        return freeReplWriter;
    }

    public void setFreeReplWriter(String freeReplWriter) {
        this.freeReplWriter = freeReplWriter;
    }

    public String getFreeReplIndate() {
        return freeReplIndate;
    }

    public void setFreeReplIndate(String freeReplIndate) {
        this.freeReplIndate = freeReplIndate;
    }

    public FreeRepl() {
    }

    public FreeRepl(int freeIdx, int freeReplIdx, String freeReplContent, String freeReplWriter, String freeReplIndate) {
        this.freeIdx = freeIdx;
        this.freeReplIdx = freeReplIdx;
        this.freeReplContent = freeReplContent;
        this.freeReplWriter = freeReplWriter;
        this.freeReplIndate = freeReplIndate;
    }
}
