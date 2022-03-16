import app.AdvanceIntegrationTest;
import app.controllers.ManagerControllerTest;
import domain.assembly.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({AdvanceIntegrationTest.class, ManagerControllerTest.class,
                AssemblyLineTest.class, AssemblyTaskTest.class,
                WorkStationTest.class})
@SuiteDisplayName("Advance Assembly Line")
public class AdvanceTestSuite {
}
