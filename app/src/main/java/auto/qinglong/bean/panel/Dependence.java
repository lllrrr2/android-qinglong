package auto.qinglong.bean.panel;

/**
 * @author wsfsp4
 * @version 2023.07.08
 */
public class Dependence {
    public static final String TYPE_PYTHON = "python";
    public static final String TYPE_SHELL = "shell";
    public static final String TYPE_NODEJS = "nodejs";

    public static int STATUS_INSTALLING = 0;
    public static int STATUS_INSTALLED = 1;
    public static int STATUS_INSTALL_FAILURE = 2;
    public static int STATUS_UNINSTALLING = 3;
    public static int STATUS_UNINSTALL_FAILURE = 5;
    public static int STATUS_UNKOWN = -1;

    private Object key;
    private String title;
    private String status;
    private int statusCode;
    private String createTime;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}