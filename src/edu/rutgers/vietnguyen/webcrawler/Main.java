package edu.rutgers.vietnguyen.webcrawler;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			System.out.print("Please enter the URL you want to crawl: ");
			Scanner input = new Scanner(System.in);
			String url = input.nextLine();
			System.out.print("Folder path: ");
			String folderPath = input.nextLine();
		
			final int PDF_QUEUE_SIZE = 10;
			final int SEARCH_THREADS = 100;
			
			BlockingQueue<String> queue = new ArrayBlockingQueue<String>(PDF_QUEUE_SIZE);
			
			//run the url reader in one thread
			URLReader urlReaderTask = new URLReader(queue, url);
			new Thread(urlReaderTask).start();
			
			//execute multiple threads to download PDF
			ExecutorService pool = Executors.newCachedThreadPool();
			
			for(int i = 1; i <= SEARCH_THREADS; i ++ )
				new Thread(new Downloader(queue, folderPath)).start();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

}
