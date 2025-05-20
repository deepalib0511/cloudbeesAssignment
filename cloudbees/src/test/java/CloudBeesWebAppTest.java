import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CloudBeesWebAppTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 20);

    }

    @Test
    public void testCloudBeesWebApplication() {
        // Step 1: Open the application
        driver.get("https://www.cloudbees.com/");
        driver.manage().window().maximize();
        WebElement closeCookies = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div/button[@aria-label='Close']")));
        closeCookies.click();

        // Step 2: Click the 'Products' link and verify it's visible
        WebElement productsLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Products')]")));
        Assert.assertTrue(productsLink.isDisplayed(), "Products link is not visible");
        productsLink.click();

        // Step 3: Click 'CloudBees CD/RO' and verify it's accessible
        WebElement cloudBeesCDRO = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'CloudBees CD/RO')]")));
        Assert.assertTrue(cloudBeesCDRO.isDisplayed(), "CloudBees CD/RO link is not visible");
        cloudBeesCDRO.click();

        // Step 4: Verify Cost Savings value
        WebElement costSavingsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//p[contains(text(), 'Cost Savings')]")));
        WebElement costSavingsValue = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Cost Savings')]/parent::div/div/span[1]")));
        Assert.assertTrue(costSavingsValue.getText().contains("$2m"), 
                "Cost savings value is not $2m, found: " + costSavingsValue.getText());

        // Step 5: Verify and click 'Auditors / Security'
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        WebElement auditorsSecurityLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div/span[contains(text(),'Auditors / Security')]")));
        Assert.assertTrue(auditorsSecurityLink.isDisplayed(), "Auditors/Security link is not visible");
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", auditorsSecurityLink);

        // Step 6: Verify Release Governance text
        WebElement releaseGovernanceHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath
                ("//p[contains(text(), 'Release Governance')]")));
        WebElement releaseGovernanceHeaderSiblingsP = driver.findElement(By.xpath
                ("//p[contains(text(), 'Release Governance')]/following-sibling::p"));
        WebElement releaseGovernanceHeaderSiblingsH4 = driver.findElement
                (By.xpath("//p[contains(text(), 'Release Governance')]/following-sibling::h4"));

        // Create a List to store multiple WebElements
        List<WebElement> releaseGovernanceHeaderSiblings = new ArrayList<>();
        releaseGovernanceHeaderSiblings.add(releaseGovernanceHeaderSiblingsP);
        releaseGovernanceHeaderSiblings.add(releaseGovernanceHeaderSiblingsH4);
        for (WebElement sibling : releaseGovernanceHeaderSiblings) {
            if (sibling.getText().contains("Generate single-click audit reports")) {
                System.out.println("Release Governance text is correct.");
            }
        }


        // Step 7: Click Resources and verify
        WebElement resourcesLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Resources')]")));
        Assert.assertTrue(resourcesLink.isDisplayed(), "Resources link is not visible");
        resourcesLink.click();

        // Step 8: Click Documentation and verify
        WebElement documentationLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@aria-label='Documentation']")));
        Assert.assertTrue(documentationLink.isDisplayed(), "Documentation link is not visible");
        documentationLink.click();

        // Step 9: Verify new tab
        String originalWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Set<String> windows = driver.getWindowHandles();
        Assert.assertEquals(windows.size(), 2, "New tab was not opened");


        // Switch to the new tab
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        // Step 10: Verify search functionality
        WebElement searchTextField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Search all CloudBees Resources']")));
        Assert.assertTrue(searchTextField.isDisplayed(), "Search text field is not visible");
        searchTextField.click();
        
        WebElement filterDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[@id='group-filter-dropdown']")));
        Assert.assertTrue(filterDropdown.isDisplayed(), "Redirection to new page didnt happen after navigating from Search All cloudbees Resources button");

        // Step 11: Search for Installation
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Search']")));
        searchField.click();
        searchField.sendKeys("Installation");
        searchField.sendKeys(Keys.RETURN);

        // Step 12: Verify pagination
        WebElement pagination = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//ul[@class='pagination pagination-sm justify-content-center flex-wrap']")));
        Assert.assertTrue(pagination.isDisplayed(), "Pagination is not visible");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}