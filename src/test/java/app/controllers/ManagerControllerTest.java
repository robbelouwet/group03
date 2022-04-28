package app.controllers;

public class ManagerControllerTest {
    /*
    private ManagerController mgrController;
    private IManagerView mgrView;

    @BeforeEach
    public void setup() {
        // change this to false to see the exception being thrown
        var mockedStore = mock(ManagerStore.class);
        var mockedAManager = mock(AssemblyManager.class);
        when(mockedAManager.advance(anyInt())).thenReturn(true);
        var mockedCOManager = mock(CarOrderManager.class);
        when(mockedCOManager.getCarModels()).thenReturn(new LinkedList<>());
        when(mockedStore.getAssemblyLineManager()).thenReturn(mockedAManager);
        when(mockedStore.getCarOrderManager()).thenReturn(mockedCOManager);

        var controllerStore = new ControllerStore(mockedStore);

        mgrView = new IManagerView() {
            @Override
            public void confirmMove(int timeSpent) {
                controllerStore.getManagerController().advanceAssemblyLine(timeSpent);
            }

            @Override
            public void showAdvanceOverview() {
                String action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
                while (!(action.equals("yes") || action.equals("no"))) {
                    ConsoleReader.getInstance().println("This is not a valid option.");
                    action = ConsoleReader.getInstance().ask("Advance Assembly Line? [yes] | [no]");
                }
                if (action.equals("yes")) {
                    int time = new Random().nextInt(100);
                    confirmMove(time);
                }
            }

            @Override
            public void showErrorMessage(String err) {
                throw new RuntimeException("Assembly line is blocked!");
            }

            @Override
            public void showSchedulingAlgorithms(List<String> algorithms, String selectedAlgorithm) {
                // TODO
            }

            @Override
            public void showPossibleOptionsForAlgorithm(List<Map<String, String>> options, String algorithm) {
                // TODO
            }

            @Override
            public void showAssemblyLineStatusAfterMove(Map<String, String> pendingOrders) {

            }
        };

        mgrController = controllerStore.getManagerController();
        mgrController.setUi(mgrView);
    }

    @Test
    void advanceAssemblyLine() {
        // if something goes wrong, the controller throws an exception,
        // so just executing and expecting no exception is sufficient
        mgrController.advanceAssemblyLine(60);
    }

     */
}