package scnu.able.myapp.vo.notice;

public class NoticeRepl {
    private int noticeIdx;
    private int noticeReplIdx;
    private String noticeReplContent;
    private String noticeReplWriter;
    private String noticeReplIndate;

    @Override
    public String toString() {
        return "NoticeRepl{" +
                "noticeIdx=" + noticeIdx +
                ", noticeReplIdx=" + noticeReplIdx +
                ", noticeReplContent='" + noticeReplContent + '\'' +
                ", noticeReplWriter='" + noticeReplWriter + '\'' +
                ", noticeReplIndate='" + noticeReplIndate + '\'' +
                '}';
    }

    public int getNoticeIdx() {
        return noticeIdx;
    }

    public void setNoticeIdx(int noticeIdx) {
        this.noticeIdx = noticeIdx;
    }

    public int getNoticeReplIdx() {
        return noticeReplIdx;
    }

    public void setNoticeReplIdx(int noticeReplIdx) {
        this.noticeReplIdx = noticeReplIdx;
    }

    public String getNoticeReplContent() {
        return noticeReplContent;
    }

    public void setNoticeReplContent(String noticeReplContent) {
        this.noticeReplContent = noticeReplContent;
    }

    public String getNoticeReplWriter() {
        return noticeReplWriter;
    }

    public void setNoticeReplWriter(String noticeReplWriter) {
        this.noticeReplWriter = noticeReplWriter;
    }

    public String getNoticeReplIndate() {
        return noticeReplIndate;
    }

    public void setNoticeReplIndate(String noticeReplIndate) {
        this.noticeReplIndate = noticeReplIndate;
    }

    public NoticeRepl() {
    }

    public NoticeRepl(int noticeIdx, int noticeReplIdx, String noticeReplContent, String noticeReplWriter, String noticeReplIndate) {
        this.noticeIdx = noticeIdx;
        this.noticeReplIdx = noticeReplIdx;
        this.noticeReplContent = noticeReplContent;
        this.noticeReplWriter = noticeReplWriter;
        this.noticeReplIndate = noticeReplIndate;
    }
}
