package scnu.able.myapp.vo.link;

import lombok.Data;

@Data
public class LinkRepl {

    private int linkIdx;
    private int linkReplIdx;
    private String linkReplContent;
    private String linkReplWriter;
    private String linkReplIndate;

    @Override
    public String toString() {
        return "LinkRepl{" +
                "linkIdx=" + linkIdx +
                ", linkReplIdx=" + linkReplIdx +
                ", linkReplContent='" + linkReplContent + '\'' +
                ", linkReplWriter='" + linkReplWriter + '\'' +
                ", linkReplIndate='" + linkReplIndate + '\'' +
                '}';
    }

    public int getLinkIdx() {
        return linkIdx;
    }

    public void setLinkIdx(int linkIdx) {
        this.linkIdx = linkIdx;
    }

    public int getLinkReplIdx() {
        return linkReplIdx;
    }

    public void setLinkReplIdx(int linkReplIdx) {
        this.linkReplIdx = linkReplIdx;
    }

    public String getLinkReplContent() {
        return linkReplContent;
    }

    public void setLinkReplContent(String linkReplContent) {
        this.linkReplContent = linkReplContent;
    }

    public String getLinkReplWriter() {
        return linkReplWriter;
    }

    public void setLinkReplWriter(String linkReplWriter) {
        this.linkReplWriter = linkReplWriter;
    }

    public String getLinkReplIndate() {
        return linkReplIndate;
    }

    public void setLinkReplIndate(String linkReplIndate) {
        this.linkReplIndate = linkReplIndate;
    }

    public LinkRepl() {
    }

    public LinkRepl(int linkIdx, int linkReplIdx, String linkReplContent, String linkReplWriter, String linkReplIndate) {
        this.linkIdx = linkIdx;
        this.linkReplIdx = linkReplIdx;
        this.linkReplContent = linkReplContent;
        this.linkReplWriter = linkReplWriter;
        this.linkReplIndate = linkReplIndate;
    }
}
