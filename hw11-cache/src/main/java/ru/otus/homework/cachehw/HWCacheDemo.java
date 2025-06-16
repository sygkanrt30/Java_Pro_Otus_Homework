package ru.otus.homework.cachehw;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HWCacheDemo {
    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();
        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        @SuppressWarnings("All")
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        cache.addListener(listener);
        cache.put("1", 1);

        log.info("getValue:{}", cache.get("1"));

        cache.remove("1");
        cache.removeListener(listener);
    }
}
