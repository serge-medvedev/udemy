package org.apache.flink.java;

/**
 * IpDataRecord
 */
public class IpDataRecord implements java.io.Serializable {
    private String userId;
    private String networkName;
    private Long userIp;
    private String userCountry;
    private String website;
    private Long timeBeforeNextClick;

    public IpDataRecord() {
    }

    public IpDataRecord(String userId, String networkName, Long userIp, String userCountry, String website, Long timeBeforeNextClick) {
        this.userId = userId;
        this.networkName = networkName;
        this.userIp = userIp;
        this.userCountry = userCountry;
        this.website = website;
        this.timeBeforeNextClick = timeBeforeNextClick;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public void setUserIp(Long userIp) {
        this.userIp = userIp;
    }
    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public void setTimeBeforeNextClick(Long timeBeforeNextClick) {
        this.timeBeforeNextClick = timeBeforeNextClick;
    }

    public String getUserId() {
        return this.userId;
    }
    public String getNetworkName() {
        return this.networkName;
    }
    public Long getUserIp() {
        return this.userIp;
    }
    public String getUserCountry() {
        return this.userCountry;
    }
    public String getWebsite() {
        return this.website;
    }
    public Long getTimeBeforeNextClick() {
        return this.timeBeforeNextClick;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(userId).append(",")
          .append(networkName).append(",")
          .append(userIp).append(",")
          .append(userCountry).append(",")
          .append(website).append(",")
          .append(timeBeforeNextClick);

        return sb.toString();
    }
}

