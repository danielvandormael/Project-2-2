package Logic;

public class TestMDFS {

    protected String mapDoc;
    protected Scenario scenario;

    public static void main(String[] args){
        // the mapscenario should be passed as a parameter
        String mapD="C:\\Users\\jiska\\OneDrive\\Documents\\Maastricht University\\Project 2-2 New\\src\\resources\\maps\\testmap.txt";
        TestMDFS game = new TestMDFS(mapD);
        //game.writeGameFile();

    }

    public TestMDFS(String scn){
        mapDoc=scn;
        scenario = new Scenario(mapDoc);
        MDFS mdfs = new MDFS(scenario);
        ExpGuard guard = new ExpGuard((int) scenario.baseSpeedGuard, 0, 5, new Map(scenario.mapWidth, scenario.mapHeight));
        guard.setXYDirection(2, 70, 1);

        int counter = 0;
        int maxCounter = scenario.mapWidth*scenario.mapHeight*5;
        //int maxCounter = 10000;
        boolean ready = false;

        while(!guard.getMap().isExplored() && counter < maxCounter && !ready){
            counter++;
            ready = mdfs.nextStep(guard);
            int x = guard.getX();
            int y = guard.getY();
            int nextSpeed = guard.getNextSpeed();
            int direction = guard.getNextDirection();
            switch (direction){
                case 0:
                    guard.setXYDirection(x, y+nextSpeed, direction);
                    break;
                case 1:
                    guard.setXYDirection(x+nextSpeed, y, direction);
                    break;
                case 2:
                    guard.setXYDirection(x, y-nextSpeed, direction);
                    break;
                case 3:
                    guard.setXYDirection(x-nextSpeed, y, direction);
                    break;

            }
            if (counter%20 == 0){
                System.out.println(guard.getMap().exportMap());
                System.out.println("---------------------------------------------------------------------");
            }
        }
        System.out.println(guard.getMap().exportMap());
        System.out.println("finished, counter : " + counter);
    }
}
