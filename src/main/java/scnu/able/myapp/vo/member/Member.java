package scnu.able.myapp.vo.member;

import lombok.Data;


@Data
public class Member {
    private int memIdx;
    private String memID;
    private String memPassword;
    private String memName;
    private String memAge;
    private String memEmail;
    private String memProfile;
    private String memAuth;
    private String memPhone;
    private String memStatus;

    @Override
    public String toString() {
        return "Member{" +
                "memIdx=" + memIdx +
                ", memID='" + memID + '\'' +
                ", memPassword='" + memPassword + '\'' +
                ", memName='" + memName + '\'' +
                ", memAge='" + memAge + '\'' +
                ", memEmail='" + memEmail + '\'' +
                ", memProfile='" + memProfile + '\'' +
                ", memAuth='" + memAuth + '\'' +
                ", memPhone='" + memPhone + '\'' +
                ", memStatus='" + memStatus + '\'' +
                '}';
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

    public String getMemPassword() {
        return memPassword;
    }

    public void setMemPassword(String memPassword) {
        this.memPassword = memPassword;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getMemAge() {
        return memAge;
    }

    public void setMemAge(String memAge) {
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

    public String getMemPhone() {
        return memPhone;
    }

    public void setMemPhone(String memPhone) {
        this.memPhone = memPhone;
    }

    public String getMemStatus() {
        return memStatus;
    }

    public void setMemStatus(String memStatus) {
        this.memStatus = memStatus;
    }

    public Member() {
    }

    public Member(int memIdx, String memID, String memPassword, String memName, String memAge, String memEmail, String memProfile, String memAuth, String memPhone, String memStatus) {
        this.memIdx = memIdx;
        this.memID = memID;
        this.memPassword = memPassword;
        this.memName = memName;
        this.memAge = memAge;
        this.memEmail = memEmail;
        this.memProfile = memProfile;
        this.memAuth = memAuth;
        this.memPhone = memPhone;
        this.memStatus = memStatus;
    }
}
