package scnu.able.myapp.vo.labnote;

import lombok.Data;

@Data
public class Labnote {

    private int labnoteIdx;
    private int subjectIdx;
    private String labnoteTitle;
    private String labnoteWriter;
    private String labnoteComment;
    private String labnoteIndate;
    private int labnoteHit;
    private String labnoteFile;

    @Override
    public String toString() {
        return "Labnote{" +
                "labnoteIdx=" + labnoteIdx +
                ", subjectIdx=" + subjectIdx +
                ", labnoteTitle='" + labnoteTitle + '\'' +
                ", labnoteWriter='" + labnoteWriter + '\'' +
                ", labnoteComment='" + labnoteComment + '\'' +
                ", labnoteIndate='" + labnoteIndate + '\'' +
                ", labnoteHit=" + labnoteHit +
                ", labnoteFile='" + labnoteFile + '\'' +
                '}';
    }

    public Labnote() {
    }

    public Labnote(int labnoteIdx, int subjectIdx, String labnoteTitle, String labnoteWriter, String labnoteComment, String labnoteIndate, int labnoteHit, String labnoteFile) {
        this.labnoteIdx = labnoteIdx;
        this.subjectIdx = subjectIdx;
        this.labnoteTitle = labnoteTitle;
        this.labnoteWriter = labnoteWriter;
        this.labnoteComment = labnoteComment;
        this.labnoteIndate = labnoteIndate;
        this.labnoteHit = labnoteHit;
        this.labnoteFile = labnoteFile;
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

    public String getLabnoteTitle() {
        return labnoteTitle;
    }

    public void setLabnoteTitle(String labnoteTitle) {
        this.labnoteTitle = labnoteTitle;
    }

    public String getLabnoteWriter() {
        return labnoteWriter;
    }

    public void setLabnoteWriter(String labnoteWriter) {
        this.labnoteWriter = labnoteWriter;
    }

    public String getLabnoteComment() {
        return labnoteComment;
    }

    public void setLabnoteComment(String labnoteComment) {
        this.labnoteComment = labnoteComment;
    }

    public String getLabnoteIndate() {
        return labnoteIndate;
    }

    public void setLabnoteIndate(String labnoteIndate) {
        this.labnoteIndate = labnoteIndate;
    }

    public int getLabnoteHit() {
        return labnoteHit;
    }

    public void setLabnoteHit(int labnoteHit) {
        this.labnoteHit = labnoteHit;
    }

    public String getLabnoteFile() {
        return labnoteFile;
    }

    public void setLabnoteFile(String labnoteFile) {
        this.labnoteFile = labnoteFile;
    }
}
