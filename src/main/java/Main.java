import org.test.ScoreBoard;

public class Main {
    public static void main(String[] args) {
        ScoreBoard scoreBoard = new ScoreBoard();

        scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.startMatch("Germany", "France");
        scoreBoard.startMatch("Uruguay", "Italy");
        scoreBoard.startMatch("Argentina", "Australia");

        scoreBoard.updateScore("Mexico", "Canada", 0, 5);
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);
        scoreBoard.updateScore("Germany", "France", 2, 2);
        scoreBoard.updateScore("Uruguay", "Italy", 6, 6);
        scoreBoard.updateScore("Argentina", "Australia", 3, 1);

        System.out.println(scoreBoard.getSummary());
    }
}
