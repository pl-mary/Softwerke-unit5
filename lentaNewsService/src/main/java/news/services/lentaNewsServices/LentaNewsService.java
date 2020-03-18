package news.services.lentaNewsServices;

import news.utils.myHttpRequest.MyHttpRequest;
import news.utils.newsService.NewsService;
import news.utils.newsServiceAbstract.NewsServiceAbstract;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@Component()
@Service(value = NewsService.class)
public class LentaNewsService extends NewsServiceAbstract implements NewsService {
    private static final String link = "https://api.lenta.ru/lists/latest";

    public String getName() {
        return "lenta";
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
        Map<String, Integer> headLines = new HashMap<>();
        JSONObject obj = new JSONObject(data);
        JSONArray headlines = obj.getJSONArray("headlines");
        for (int i = 0; i < headlines.length(); i++) {
            JSONObject headline = headlines.getJSONObject(i);
            JSONObject info = headline.getJSONObject("info");
            String title = info.getString("title");
            String[] words = title.split(" ");
            for (String tmp : words) {
                String word = tmp.toLowerCase();
                if (isWord(word)) {
                    addToMap(headLines, word);
                }
            }
        }
        return headLines;
    }

    @Activate
    public void start(BundleContext context) {
        System.out.println("Lenta service started");
    }

    @Deactivate
    public void stop(BundleContext context) {
        System.out.println("Lenta service stopped");
    }
}
