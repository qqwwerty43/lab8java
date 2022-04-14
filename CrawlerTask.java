package com.company;

import java.net.*;
import java.util.Scanner;

public class CrawlerTask extends Thread {
    private URLPool pool;

    public CrawlerTask(URLDepthPair link) {
        pool = new URLPool();
        pool.addLink(link);
    }
    //переопределяем метод
    @Override
    public void run() {
        URLDepthPair link = pool.getLink();
        System.out.println(link.toString()); // вывод ссылки
        System.out.println(Thread.activeCount()); // вывод количества активных потоков
        Crawler.CountURLs++; // счетчик ссылок
        if(link.getDepth() == Crawler.getMaxDepth()) return; // проверка на ошибку. Если глубина = макс глубине, то выходим.

        findLinks(link);
    }

    private void findLinks(URLDepthPair link)
    {
        try {
            URL url = new URL(link.getURL());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Scanner scanner = new Scanner(connection.getInputStream());

            while (scanner.findWithinHorizon("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1", 0) != null) {
                String newURL = scanner.match().group(2);
                URLDepthPair newLink =  createNewLink(newURL, link);
                if (newLink == null) continue;
                CreateNewThread(newLink); // создаем поток
            }
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private URLDepthPair createNewLink(String newURL, URLDepthPair link){
        if (newURL.startsWith("/")) {
            newURL = link.getURL() + newURL;
        }
        else if (!newURL.startsWith("https")) return null;

        return new URLDepthPair(newURL, link.getDepth() + 1);
    }

    private void CreateNewThread(URLDepthPair link)  {
        CrawlerTask task = new CrawlerTask(link);
        task.start();
    }
}
