package edu.rutgers.vietnguyen.webcrawler;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;
import java.io.IOException;

public class URLReader {
	public static List<String> pdfList = new ArrayList<String>();
	
	public void readURL(String sURL)
	{
		try
		{
			URL myURL = new URL(sURL);
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
					tmpLink = makeAbsolute(sURL,tmpLink);
					if(tmpLink != "")
					{
						pdfList.add(tmpLink);
					}
				}
			}
			
			System.out.println("Analysis done... Number of pdf link: " + pdfList.size());
			
		}
		catch(Exception ex)
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
