package BDD;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:build/target/cucumber-report.html"},
    features = {"src/test/resources"}
)
public class BDDDummyTest {
}
