package news.command.impl;

import news.command.Command;
import news.utils.newsService.NewsService;
import org.apache.felix.scr.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
@Component
@Service(value = Command.class)
@Properties({
        @Property(name = "osgi.command.scope", value = "news"),
        @Property(name = "osgi.command.function", value = "stats")
})
public class StatsCommandImpl implements Command {
    @Reference(
            referenceInterface = NewsService.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            bind = "bindService",
            unbind = "unbindService"
    )
    private volatile List<NewsService> newsServices = new ArrayList<>();

    protected void bindService(NewsService service) {
        newsServices.add(service);
    }

    protected void unbindService(NewsService service) {
        newsServices.remove(service);
    }


    @Override
    public void stats(String[] urls) {
        if (urls == null || urls[0] == null) {
            System.out.println("Error: null source name");
            return;
        }
        if (urls.length > 1) {
            System.out.println("Error: incorrect number of source name");
            return;
        }
        if (newsServices.isEmpty()) {
            System.out.println("No sources available");
        } else {
            if (urls[0].equals("all")) {
                Map<String, Integer> tmp = new HashMap<>();
                for (NewsService service : newsServices) {
                    mergeAvailableMaps(service.getStat(), tmp);
                }
                printWords(tmp);
            } else {
                boolean flag = false;
                Map<String, Integer> tmp = new HashMap<>();
                for (NewsService service : newsServices) {
                    if (service.getName().equals(urls[0])) {
                        flag = true;
                        tmp = service.getStat();
                        break;
                    }
                }
                if (!flag) {
                    System.out.println("Source : \"" + urls[0] + "\" is not available");
                    return;
                }
                printWords(tmp);
            }
        }
    }

    private void mergeAvailableMaps(Map<String, Integer> stat, Map<String, Integer> tmp) {
        stat.forEach((k,v) -> tmp.merge(k, v, Integer::sum));
    }

    @Override
    public void stats() {
        System.out.println("Try: ");
        suggestAvailableServices();
    }

    private void suggestAvailableServices() {
        if (newsServices.isEmpty()) {
            System.out.println("No news service is registered");
            return;
        }
        for (NewsService service : newsServices) {
            System.out.println("news:stats " + service.getName());
        }
        System.out.println("news:stats all");
    }


    private void printWords(Map<String, Integer> map) {
        map.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10).forEach(el -> System.out.println(el.getKey()));
    }
}
