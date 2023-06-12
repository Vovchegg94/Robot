package ru.netology;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void main(String[] args) throws InterruptedException {

        String[] routes = new String[1000];
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < routes.length; i++) {
            routes[i] = generateRoute("RLRFR", 100);

        }

        for (String route : routes) {
            Runnable logic = () -> {
                int count = 0;

                synchronized (sizeToFreq) {
                    for (int i = 0; i < route.length(); i++) {
                        if (route.charAt(i) == 'R') {
                            count++;
                        }
                    }
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, (sizeToFreq.get(count)) + 1);
                    } else {
                        sizeToFreq.put(count, 1);

                    }
                }
            };
            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        Integer maxKey = sizeToFreq.keySet().stream()
                .max(Comparator.comparing(sizeToFreq::get))
                .orElse(null);
        System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + sizeToFreq.get(maxKey) + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> kv : sizeToFreq.entrySet()) {
            if (kv.getValue() != null && !kv.getKey().equals(maxKey)) {
                System.out.println("- " + kv.getKey() + " (" + kv.getValue() + " раз)");
            }
        }
    }
}