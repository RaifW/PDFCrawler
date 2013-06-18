package edu.rutgers.vietnguyen.webcrawler;


import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;
import java.io.IOException;
import java.util.concurrent.*;


public class URLReader implements Runnable {
	private BlockingQueue<String> pdfList;
	private String url;
	public static String DUMMY = "";
	
	public URLReader(BlockingQueue<String> queue, String url)
	{
		this.pdfList = queue;
		this.url = url;
	}
	
	@Override
	public void run() {
		try
		{
			readURL();
			pdfList.put(DUMMY); //put DUMMY at the end of the pdf list
		}
		catch(InterruptedException e)
		{
			
		}
	}
	
	public void readURL() throws InterruptedException
	{
		try
		{
			URL myURL = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(myURL.openStream()));
			String sLine;
			StringBuilder builder = new StringBuilder();
			Pattern htmltag = Pattern.compile("<a\\b[^>]*href=\"[^>]*>(.*?)</a>");
			Pattern link = Pattern.compile("href=\"[^\"]*\"");
			
			
			System.out.println("Read the web page...");
			
			while(null!=(sLine = br.readLine()))
			{
				builder.append(sLine);
			}
			
			Matcher tagmatch = htmltag.matcher(builder.toString());
			
			System.out.println("Beginining analysis the web page...");
			
			while(tagmatch.find())
			{
				Matcher matcher = link.matcher(tagmatch.group());
				matcher.find();
				String tmpLink = matcher.group().replace("href=\"","").replaceFirst("\"", "");
				if(valid(tmpLink) && tmpLink.matches(".*pdf"))
				{
					tmpLink = makeAbsolute(url,tmpLink);
					if(tmpLink != "")
					{
						pdfList.put(tmpLink);
						System.out.println("PDF found: " + tmpLink);
					}
				}
			}
			
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	/*
	 * Check if a string is a valid URL or not.
	 */
	private boolean valid(String s){
		if(s.matches("javascript:.*|mailto:.*")){
			return false;
		}
		return true;
	}
	
	/*
	 * Make absolute link, from the site url and the link address.
	 */
	private  String makeAbsolute(String url, String link)
	{
		if(link.matches("http://.*"))
		{
			return link;
		}
		if(link.matches("/.*") && url.matches(".*$[^/]"))
		{
			return url + "/" + link;
		}
		if(link.matches("[^/].*") && url.matches(".*[^/]"))
		{
			return url + "/" + link;
		}
		if(link.matches("[^/].*") && url.matches(".*[/]"))
		{
			return url + link;
		}
		if(link.matches("/.*") && url.matches(".*[/]"))
		{
			return url + link;
		}
		if(link.matches("/.*")&& url.matches(".*[^/]"))
		{
			return url + link;
		}
		return "";
				
	}


}
