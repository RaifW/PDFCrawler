package edu.rutgers.vietnguyen.webcrawler;

import java.util.Scanner;

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
			
			URLReader uRead = new URLReader();
			uRead.readURL(url);
			
			Downloader downloader = new Downloader();
			downloader.importUrlList(URLReader.pdfList);
			downloader.setFolerPath(folderPath);
			downloader.process();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

}
