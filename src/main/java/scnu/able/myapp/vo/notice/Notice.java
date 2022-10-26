package scnu.able.myapp.vo.notice;

import lombok.Data;

import java.util.List;

@Data
public class Notice {

    private int noticeIdx;
    private String noticeTitle;
    private String noticeContent;
    private String noticeWriter;
    private String noticeIndate;
    private int noticeCount;
    private String noticeFile;
    private String noticeTop;

    @Override
    public String toString() {
        return "Notice{" +
                "noticeIdx=" + noticeIdx +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", noticeWriter='" + noticeWriter + '\'' +
                ", noticeIndate='" + noticeIndate + '\'' +
                ", noticeCount=" + noticeCount +
                ", noticeFile='" + noticeFile + '\'' +
                ", noticeTop='" + noticeTop + '\'' +
                '}';
    }

    public int getNoticeIdx() {
        return noticeIdx;
    }

    public void setNoticeIdx(int noticeIdx) {
        this.noticeIdx = noticeIdx;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeWriter() {
        return noticeWriter;
    }

    public void setNoticeWriter(String noticeWriter) {
        this.noticeWriter = noticeWriter;
    }

    public String getNoticeIndate() {
        return noticeIndate;
    }

    public void setNoticeIndate(String noticeIndate) {
        this.noticeIndate = noticeIndate;
    }

    public int getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(int noticeCount) {
        this.noticeCount = noticeCount;
    }

    public String getNoticeFile() {
        return noticeFile;
    }

    public void setNoticeFile(String noticeFile) {
        this.noticeFile = noticeFile;
    }

    public String getNoticeTop() {
        return noticeTop;
    }

    public void setNoticeTop(String noticeTop) {
        this.noticeTop = noticeTop;
    }

    public Notice() {
    }

    public Notice(int noticeIdx, String noticeTitle, String noticeContent, String noticeWriter, String noticeIndate, int noticeCount, String noticeFile, String noticeTop) {
        this.noticeIdx = noticeIdx;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeWriter = noticeWriter;
        this.noticeIndate = noticeIndate;
        this.noticeCount = noticeCount;
        this.noticeFile = noticeFile;
        this.noticeTop = noticeTop;
    }
}
