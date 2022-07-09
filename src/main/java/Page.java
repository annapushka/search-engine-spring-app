import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Page {
    private List<WebPageData> parentList = new ArrayList<>();
    private List<Page> childrenList = new ArrayList<>();
    private final String url;

    public Page(String url) {
        this.url = url;
    }

    public List<Page> getChildrenList() {
        return childrenList;
    }

    public List<WebPageData> getParentList() {
        return parentList;
    }

    public void getLinks() throws IOException {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
            Thread.sleep(150);
            Elements links = doc.select("a");

            if (links.isEmpty()) {
                return;
            }
            links.stream().map((link) -> link.attr("abs:href")).forEachOrdered((this_url) -> {
                if (this_url.startsWith(url) && isCorrected(this_url) && !this_url.equals(url)) {
                    try {
                        URL cutUrl = new URL(this_url);
                        int code = getResponseCode(this_url);
                        WebPageData webPageData = new WebPageData(this_url,
                                cutUrl.getPath(),
                                code,
                                doc.html());
                        parentList.add(webPageData);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            parentList.forEach(parent -> {
                Page childPage = new Page(parent.getUrl());
                try {
                    Document docCh = Jsoup.connect(parent.getUrl()).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 " +
                                    "(KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                            .referrer("http://www.google.com")
                            .get();
                    Thread.sleep(150);
                    Elements chLinks = docCh.select("a");
                    if (chLinks.isEmpty()) {
                        return;
                    }
                    chLinks.stream().map((chLink) -> chLink.attr("abs:href")).forEachOrdered((this_chUrl) -> {
                        if (this_chUrl.startsWith(parent.getUrl()) && isCorrected(this_chUrl)
                                && !this_chUrl.equals(parent.getUrl())) {
                            try {
                                URL cutUrl = new URL(this_chUrl);
                                int code = getResponseCode(this_chUrl);
                                WebPageData webPageData = new WebPageData(this_chUrl,
                                        cutUrl.getPath(),
                                        code,
                                        docCh.html());
                                childPage.parentList.add(webPageData);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    childrenList.add(childPage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isCorrected (String url) {
        return (!url.isEmpty() && !url.contains("#")
                && !url.matches("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|pdf))$)"));
    }

    public static int getResponseCode(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection)url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }

}
