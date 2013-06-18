package edu.rutgers.vietnguyen.webcrawler;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.regex.*;

public class Downloader implements Runnable {
	private List<String> downloadUrls = new ArrayList<String>();
	private BlockingQueue<String> queue;
	private String folderPath = "";
	
	public Downloader(BlockingQueue<String> queue, String folderPath )
	{
		this.queue = queue;
		this.folderPath = folderPath;
	}
	

	@Override
	public void run() {
		try
		{
			boolean done = false;
			while(!done)
			{
				String pdfFileName = queue.take();
				if(pdfFileName == URLReader.DUMMY)
				{
					queue.put(pdfFileName);
					done = true;
				}
				else 
					process(pdfFileName);
			}
		}
		catch(InterruptedException e)
		{
			
		}
		
	}
	
	public void setFolerPath(String name)
	{
		folderPath = name;
	}
	
	public void clearUrlList()
	{
		downloadUrls.clear();
	}
	
	public void addNewUrl(String name)
	{
		downloadUrls.add(name);
	}
	
	public void importUrlList(List<String> urlList)
	{
		downloadUrls.addAll(urlList);
	}
	
	public void process(String sCurrUrl)
	{
		try
		{
			URL url1 = new URL(sCurrUrl);
			URLConnection urlConn = url1.openConnection();
			
			//Checking whether the URL contains a PDF
			if(!urlConn.getContentType().equalsIgnoreCase("application/pdf"))
			{
				System.out.println(sCurrUrl + ": Sorry! Not a PDF file!");
			}
			else
			{

				//Download: get input stream from URL, output stream to file!
				String fileName = extractFileName(sCurrUrl);
				if(fileName == "")
				{
					System.out.println("Filename error! Couldn't extract file name: " + sCurrUrl);
				}
				InputStream in = new BufferedInputStream(urlConn.getInputStream());
				OutputStream out = new FileOutputStream(folderPath + "/" + fileName);
				
				int b;
				while((b = in.read())!=-1)
				{
					out.write(b);
				}
				in.close();
				out.close();
				System.out.println("Download " + extractFileName(sCurrUrl) + " completed!");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String extractFileName(String url)
	{
		Pattern p = Pattern.compile("/[^/]*.pdf");
		Matcher m = p.matcher(url);
		if(m.find())
		{
			return m.group().replaceFirst("/", "");
		}
		return "";
	}

}
