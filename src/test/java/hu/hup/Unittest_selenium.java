package hu.hup;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Unittest_selenium {
	
	@Test
	public void test1() {
		//System.setProperty("webdriver.chrome.driver", "C:/workspace/Webscraper/libs/chromedriver.exe");
		//WebDriver driver = new ChromeDriver();
		//driver.manage().window().maximize();
		WebDriver driver = new HtmlUnitDriver();
		String baseURL = "http://hup.hu";
		driver.get(baseURL);
		
		List<WebElement> usernameField = driver.findElements(By.cssSelector("#edit-name"));
		List<WebElement> passwordField = driver.findElements(By.cssSelector("#edit-pass"));

		assertFalse("User login field is missing",usernameField.size()>0);
		assertTrue("Password field is missing",passwordField.size()>0);
		
		/*
		 * Loop around first ten rows based on CSSselector, grab hrefs and collect them
		 * to array
		 */
		String webadressArray[] = new String[11];
		String hrefnode = new String();

		final int topicNumber = 11;
		
		for (int i = 1; i < topicNumber; i++) {

			hrefnode = driver.findElement(By.cssSelector("#block-block-16 > div > table > tbody > tr:nth-child(" + i
					+ ") > td:nth-child(2) > 				a")).getAttribute("href");
			// System.out.println(i + ". href: " + hrefnode); //for debugging

			// loading each into array
			webadressArray[i] = hrefnode;
		}
		Set<String> commenterArray = new TreeSet<>();
		Map<String, String> commenterNames = new HashMap<>();

		/*
		 * Loop around href array created in previous loop, click each, grab 1st 10
		 * commentauthors and collect them to array
		 */
		for (int i = 1; i < topicNumber; i++) {

			// open node in existing browserinstance
			driver.get(webadressArray[i]);

			List<WebElement> commenterList = driver.findElements(By.cssSelector("#comments > .comment > div.submitted"));

			int j = 0;
			for (WebElement commentNext : commenterList) {
				String commentHeader = commentNext.getText();

				String commentHeaderPattern = "(?:\\(\\s)([a-zA-Z0-9]{1,60})(?:\\s|.*)"; // ^[?( ][a-zA-Z0-9]{1,20}[?|]

				final Pattern pattern = Pattern.compile(commentHeaderPattern);
				Matcher commenterName = pattern.matcher(commentHeader);
				commenterName.find();

				if (j++ < 10) {
					commenterArray.add(commenterName.group(1).toLowerCase());
					commenterNames.put(commenterName.group(1).toLowerCase(), commenterName.group(1));
				}

			}
			
		} 
				
		for (String s : commenterArray) {
			System.out.println(commenterNames.get(s));
		}		
		
		driver.close();	
		assertTrue(commenterArray.size() > 9);
		assertTrue(commenterArray.size() < 101);
		assertTrue(!commenterArray.contains("trey"));
	}
		
}