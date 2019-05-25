package ara.bc282.assignment1.zhong;

public class Eyeball implements Game {
    public GameMap currentMap;
    public Sprite sprite;

    public void start(int stage) {
        currentMap = new GameMap(stage);
        sprite = new Sprite(currentMap);
    }

    public void restart() {
        sprite = new Sprite(currentMap);
    }

    public boolean showSolution(int stage) {
        if (stage > 1) {
            System.out.println("We only provide solutions for first two stage.");
            return false;
        } else {
            int[][] solution = GameMap.solutionList[stage];
            start(stage);
            for(int[] step: solution) {
                sprite.walkTo(step[0], step[1]);
            }
            return true;
        }
    }
}
