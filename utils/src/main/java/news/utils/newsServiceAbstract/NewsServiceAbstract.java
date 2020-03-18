package news.utils.newsServiceAbstract;

import news.utils.newsService.NewsService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class NewsServiceAbstract implements NewsService {
    private static final Set<String> prepositions = new HashSet<String>() {
        {
            addAll(Arrays.asList("а", "без", "безо", "близ", "в", "во", "вместо", "вне",
                    "для", "до", "за", "и", "или", "из", "из-за", "из-под", "к", "ко", "кроме", "между", "меж", "на", "не",
                    "над", "надо", "о", "об", "обо", "от", "ото", "c", "перед", "передо", "пред", "предо", "по", "под",
                    "подо", "при", "про", "ради", "с", "сквозь", "со", "среди", "у", "через", "чрез", "что", "как"));
        }
    };

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    protected void addToMap(Map<String, Integer> map, String word) {
        map.putIfAbsent(word, 0);
        map.put(word, map.get(word) + 1);
    }

    protected abstract Map<String, Integer> parseStats(String data);

    protected boolean isWord(String a) {
        return !prepositions.contains(a) && !isNumber(a) && !a.isEmpty();
    }
}
