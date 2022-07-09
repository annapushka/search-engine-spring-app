import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class WebPageExtractor extends RecursiveAction {
    private Page page;

    public WebPageExtractor(Page parser) {
        this.page = parser;
    }

    @Override
    protected void compute() {
        DBConnection dbConnection = new DBConnection();

        try {
            page.getLinks();
            if (page.getChildrenList().isEmpty()) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<WebPageExtractor> tackList = new ArrayList<>();
        for (Page child : page.getChildrenList()) {
            if (child.getParentList().contains(child) || child.equals(page)) {
                continue;
            }

            WebPageExtractor task = new WebPageExtractor(child);
            task.fork();
            tackList.add(task);

            dbConnection.countWebPageData(child);
        }

        for (WebPageExtractor task : tackList) {
            task.join();
        }
    }
}
