package auto.panel.bean.panel;

public class PanelAccount {
    private String address;
    private String username;
    private String password;
    private String token;
    private String code;

    private String version;

    public PanelAccount(String username, String password, String address, String token) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBaseUrl() {
        // BaseUrl
        StringBuilder sb = new StringBuilder();
        if (!address.startsWith("http")) {
            sb.append("http://");
        }
        sb.append(address);
        if (!address.endsWith("/")) {
            sb.append("/");
        }
        return sb.toString();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
