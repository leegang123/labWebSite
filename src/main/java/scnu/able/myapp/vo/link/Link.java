package scnu.able.myapp.vo.link;

import lombok.Data;

@Data
public class Link {
    private int linkIdx;
    private String linkTitle;
    private String linkContent;
    private String linkWriter;
    private String linkIndate;
    private String linkFile;

    @Override
    public String
    toString() {
        return "Link{" +
                "linkIdx=" + linkIdx +
                ", linkTitle='" + linkTitle + '\'' +
                ", linkContent='" + linkContent + '\'' +
                ", linkWriter='" + linkWriter + '\'' +
                ", linkIndate='" + linkIndate + '\'' +
                ", linkFile='" + linkFile + '\'' +
                '}';
    }

    public Link() {
    }

    public int getLinkIdx() {
        return linkIdx;
    }

    public void setLinkIdx(int linkIdx) {
        this.linkIdx = linkIdx;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkContent() {
        return linkContent;
    }

    public void setLinkContent(String linkContent) {
        this.linkContent = linkContent;
    }

    public String getLinkWriter() {
        return linkWriter;
    }

    public void setLinkWriter(String linkWriter) {
        this.linkWriter = linkWriter;
    }

    public String getLinkIndate() {
        return linkIndate;
    }

    public void setLinkIndate(String linkIndate) {
        this.linkIndate = linkIndate;
    }

    public String getLinkFile() {
        return linkFile;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }

    public Link(int linkIdx, String linkTitle, String linkContent, String linkWriter, String linkIndate, String linkFile) {
        this.linkIdx = linkIdx;
        this.linkTitle = linkTitle;
        this.linkContent = linkContent;
        this.linkWriter = linkWriter;
        this.linkIndate = linkIndate;
        this.linkFile = linkFile;
    }
}
