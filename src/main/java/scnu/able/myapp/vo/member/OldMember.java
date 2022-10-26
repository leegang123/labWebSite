package scnu.able.myapp.vo.member;

import lombok.Data;


@Data
public class OldMember {
    private int memIdx;
    private String memID;
    private String memPassword;
    private String memName;
    private int memAge;
    private String memEmail;
    private String memProfile;
    private String memAuth;

    public OldMember(int memIdx, String memID, String memPassword, String memName, int memAge, String memEmail, String memProfile, String memAuth) {
        this.memIdx = memIdx;
        this.memID = memID;
        this.memPassword = memPassword;
        this.memName = memName;
        this.memAge = memAge;
        this.memEmail = memEmail;
        this.memProfile = memProfile;
        this.memAuth = memAuth;
    }

    public OldMember() {
    }

    public OldMember(int memIdx, String memID, String memPassword, String memName, int memAge, String memEmail) {
        this.memIdx = memIdx;
        this.memID = memID;
        this.memPassword = memPassword;
        this.memName = memName;
        this.memAge = memAge;
        this.memEmail = memEmail;
    }

    public int getMemIdx() {
        return memIdx;
    }

    public void setMemIdx(int memIdx) {
        this.memIdx = memIdx;
    }

    public String getMemID() {
        return memID;
    }

    public void setMemID(String memID) {
        this.memID = memID;
    }

    public String getMemPassword()
    {


        return memPassword;
    }

    public void setMemPassword(String memPassword)
    {

        this.memPassword = memPassword;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public int getMemAge() {
        return memAge;
    }

    public void setMemAge(int memAge) {
        this.memAge = memAge;
    }

    public String getMemEmail() {
        return memEmail;
    }

    public void setMemEmail(String memEmail) {
        this.memEmail = memEmail;
    }

    public String getMemProfile() {
        return memProfile;
    }

    public void setMemProfile(String memProfile) {
        this.memProfile = memProfile;
    }

    public String getMemAuth() {
        return memAuth;
    }

    public void setMemAuth(String memAuth) {
        this.memAuth = memAuth;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memIdx=" + memIdx +
                ", memID='" + memID + '\'' +
                ", memPassword='" + memPassword + '\'' +
                ", memName='" + memName + '\'' +
                ", memAge=" + memAge +
                ", memEmail='" + memEmail + '\'' +
                ", memProfile='" + memProfile + '\'' +
                ", memAuth='" + memAuth + '\'' +
                '}';
    }
}
