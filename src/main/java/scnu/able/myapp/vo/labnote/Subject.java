package scnu.able.myapp.vo.labnote;

import lombok.Data;

// Subject VO는 랩노트를 Subject별로 모을수 있도록 하기 위해서 만들어짐
@Data
public class Subject {


    private int subjectIdx;
    private int memIdx;
    private String subjectTitle;
    private String subjectComment;
    private String subjectIndate;

    @Override
    public String toString() {
        return "Subject{" +
                "subjectIdx=" + subjectIdx +
                ", memIdx=" + memIdx +
                ", subjectTitle='" + subjectTitle + '\'' +
                ", subjectComment='" + subjectComment + '\'' +
                ", subjectIndate='" + subjectIndate + '\'' +
                '}';
    }

    public int getSubjectIdx() {
        return subjectIdx;
    }

    public void setSubjectIdx(int subjectIdx) {
        this.subjectIdx = subjectIdx;
    }

    public int getMemIdx() {
        return memIdx;
    }

    public void setMemIdx(int memIdx) {
        this.memIdx = memIdx;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectComment() {
        return subjectComment;
    }

    public void setSubjectComment(String subjectComment) {
        this.subjectComment = subjectComment;
    }

    public String getSubjectIndate() {
        return subjectIndate;
    }

    public void setSubjectIndate(String subjectIndate) {
        this.subjectIndate = subjectIndate;
    }

    public Subject() {
    }

    public Subject(int subjectIdx, int memberIdx, String subjectTitle, String subjectComment, String subjectIndate) {
        this.subjectIdx = subjectIdx;
        this.memIdx = memberIdx;
        this.subjectTitle = subjectTitle;
        this.subjectComment = subjectComment;
        this.subjectIndate = subjectIndate;
    }
}
