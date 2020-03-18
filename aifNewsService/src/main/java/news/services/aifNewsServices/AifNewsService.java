package news.services.aifNewsServices;

import news.utils.myHttpRequest.MyHttpRequest;
import news.utils.newsService.NewsService;
import news.utils.newsServiceAbstract.NewsServiceAbstract;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@Component
@Service(value = NewsService.class)
public class AifNewsService extends NewsServiceAbstract {
    private static final String link = "https://aif.ru/rss/news.php";

    public String getName() {
        return "aif";
    }

    public Map<String, Integer> getStat() {
        String data = null;
        try {
            MyHttpRequest http = new MyHttpRequest(link);
            data = http.execute();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL :" + link);
        } catch (IOException e) {
            System.out.println("Error while connection to " + link);
        }
        return parseStats(data);
    }

    @Override
    protected Map<String, Integer> parseStats(String data) {
        Map<String, Integer> answer = new HashMap<>();
        try {
            Document content = Jsoup.connect(link).get();
            Elements titles = content.select("title");
            for (Element element : titles) {
                String[] words = element.text().split("[\\p{Blank}\\p{Punct}]");
                for (String word : words) {
                    String tmp = word.toLowerCase();
                    if (isWord(tmp)) {
                        addToMap(answer, tmp);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Activate
    public void start(BundleContext context) {
        System.out.println("Aif service started");
    }

    @Deactivate
    public void stop(BundleContext context) {
        System.out.println("Aif service stopped");
    }
}
