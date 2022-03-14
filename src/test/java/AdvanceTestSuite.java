import app.AdvanceIntegrationTest;
import app.controllers.ManagerControllerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({AdvanceIntegrationTest.class, ManagerControllerTest.class})
@SuiteDisplayName("Advance Assembly Line Test Suite")
public class AdvanceTestSuite {
}
