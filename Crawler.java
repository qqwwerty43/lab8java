package com.company;
import java.util.Scanner;

public class Crawler {
    private String URL;
    private static int maxDepth;
    public static int CountThreads;

    public static int WaitingThreads = 0;
    public static int CountURLs = 0;

    public static int getMaxDepth() { return maxDepth; }

    public Crawler(String URL, int maxDepth, int countThreads){
        this.URL = URL;
        Crawler.maxDepth = maxDepth;
        Crawler.CountThreads = countThreads;
    }

    public void run() {
        CrawlerTask task = new CrawlerTask(new URLDepthPair(URL,0));
        task.start();
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.print("URL: ");
        String url = in.next();
        System.out.print("Input a depth of search: ");
        String depth = in.next();
        Crawler crawler = new Crawler(url,Integer.parseInt(depth),10); // макс глубина =2, количество потоков = 10
        crawler.run();

        Runtime.getRuntime().addShutdownHook(new Thread(()->printResult()));
    }

    private static void printResult(){
        System.out.println();
        System.out.println("Всего ссылок: " + CountURLs);
    }
}
// http://htmlbook.ru/