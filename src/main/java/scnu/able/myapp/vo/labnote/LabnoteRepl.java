package scnu.able.myapp.vo.labnote;

import lombok.Data;

@Data
public class LabnoteRepl {

    private int labnoteReplIdx;
    private int labnoteIdx;
    private int subjectIdx;
    private String labnoteReplWriter;
    private String labnoteReplContent;
    private String labnoteReplIndate;

    public LabnoteRepl() {
    }

    public LabnoteRepl(int labnoteReplIdx, int labnoteIdx, int subjectIdx, String labnoteReplWriter, String labnoteReplContent, String labnoteReplIndate) {
        this.labnoteReplIdx = labnoteReplIdx;
        this.labnoteIdx = labnoteIdx;
        this.subjectIdx = subjectIdx;
        this.labnoteReplWriter = labnoteReplWriter;
        this.labnoteReplContent = labnoteReplContent;
        this.labnoteReplIndate = labnoteReplIndate;
    }

    @Override
    public String toString() {
        return "LabnoteRepl{" +
                "labnoteReplIdx=" + labnoteReplIdx +
                ", labnoteIdx=" + labnoteIdx +
                ", subjectIdx=" + subjectIdx +
                ", labnoteReplWriter='" + labnoteReplWriter + '\'' +
                ", labnoteReplContent='" + labnoteReplContent + '\'' +
                ", labnoteReplIndate='" + labnoteReplIndate + '\'' +
                '}';
    }

    public int getLabnoteReplIdx() {
        return labnoteReplIdx;
    }

    public void setLabnoteReplIdx(int labnoteReplIdx) {
        this.labnoteReplIdx = labnoteReplIdx;
    }

    public int getLabnoteIdx() {
        return labnoteIdx;
    }

    public void setLabnoteIdx(int labnoteIdx) {
        this.labnoteIdx = labnoteIdx;
    }

    public int getSubjectIdx() {
        return subjectIdx;
    }

    public void setSubjectIdx(int subjectIdx) {
        this.subjectIdx = subjectIdx;
    }

    public String getLabnoteReplWriter() {
        return labnoteReplWriter;
    }

    public void setLabnoteReplWriter(String labnoteReplWriter) {
        this.labnoteReplWriter = labnoteReplWriter;
    }

    public String getLabnoteReplContent() {
        return labnoteReplContent;
    }

    public void setLabnoteReplContent(String labnoteReplContent) {
        this.labnoteReplContent = labnoteReplContent;
    }

    public String getLabnoteReplIndate() {
        return labnoteReplIndate;
    }

    public void setLabnoteReplIndate(String labnoteReplIndate) {
        this.labnoteReplIndate = labnoteReplIndate;
    }
}
