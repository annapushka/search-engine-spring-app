public class WebPageData {

    private String url;
    private String path;
    private int code;
    private String content;

    public WebPageData(String url, String path, int code, String content) {
        this.url = url;
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "'" + path + "', " +
                "'" + code + "', " +
                "'" + content.replace("\n", "") + "'";
    }
}
