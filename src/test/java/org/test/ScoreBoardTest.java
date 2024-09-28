package org.test;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ScoreBoardTest {

    @Test
    public void shouldStartNewMatch() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startMatch("Poland", "Germany");

        // When
        List<Match> summary = scoreBoard.getSummary();

        // Then
        assertThat(summary).hasSize(1);

        Match match = summary.get(0);
        assertThat(match).isNotNull();
        assertThat(match.getHomeScore()).isZero();
        assertThat(match.getAwayScore()).isZero();
        assertThat(match.getHomeTeam()).isEqualTo("Poland");
        assertThat(match.getAwayTeam()).isEqualTo("Germany");
    }

    @Test
    public void shouldUpdateScore() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startMatch("Poland", "Germany");

        // When
        scoreBoard.updateScore("Poland", "Germany", 3, 2);

        // Then
        List<Match> summary = scoreBoard.getSummary();
        assertThat(summary).hasSize(1);

        Match match = summary.get(0);
        assertThat(match).isNotNull();
        assertThat(match.getHomeScore()).isEqualTo(3);
        assertThat(match.getAwayScore()).isEqualTo(2);
    }

    @Test
    public void shouldFinishMatch() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startMatch("Poland", "Germany");
        scoreBoard.finishMatch("Poland", "Germany");

        // When
        List<Match> summary = scoreBoard.getSummary();

        // Then
        assertThat(summary).isEmpty();
    }

    @Test
    public void shouldGetSummaryForSeveralMatches() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();

        scoreBoard.startMatch("Poland", "Germany");
        scoreBoard.updateScore("Poland", "Germany", 0, 3);

        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);

        scoreBoard.startMatch("Germany", "France");
        scoreBoard.updateScore("Germany", "France", 2, 2);

        scoreBoard.startMatch("Uruguay", "Italy");
        scoreBoard.updateScore("Uruguay", "Italy", 6, 6);

        scoreBoard.startMatch("Argentina", "Australia");
        scoreBoard.updateScore("Argentina", "Australia", 3, 1);

        // When
        List<Match> summary = scoreBoard.getSummary();

        // Then
        assertThat(summary).hasSize(5);
        assertThat(summary.get(0).getHomeTeam()).isEqualTo("Uruguay");
        assertThat(summary.get(0).getAwayTeam()).isEqualTo("Italy");

        assertThat(summary.get(1).getHomeTeam()).isEqualTo("Spain");
        assertThat(summary.get(1).getAwayTeam()).isEqualTo("Brazil");

        assertThat(summary.get(2).getHomeTeam()).isEqualTo("Argentina");
        assertThat(summary.get(2).getAwayTeam()).isEqualTo("Australia");

        assertThat(summary.get(3).getHomeTeam()).isEqualTo("Germany");
        assertThat(summary.get(3).getAwayTeam()).isEqualTo("France");

        assertThat(summary.get(4).getHomeTeam()).isEqualTo("Poland");
        assertThat(summary.get(4).getAwayTeam()).isEqualTo("Germany");
    }

    @Test
    public void shouldNotStartMatchWithInvalidTeams() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();

        // When
        Throwable thrown = catchThrowable(() -> {
            scoreBoard.startMatch("", "Germany");
        });

        // Then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Team names cannot be null or empty.");
    }

    @Test
    public void shouldNotUpdateScoreForNonExistentMatch() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();

        // When
        Throwable thrown = catchThrowable(() -> {
            scoreBoard.updateScore("Poland", "Germany", 1, 1);
        });

        // Then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Match not found.");
    }

    @Test
    public void shouldOrderMatchesByTotalScoreAndRecency() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();

        scoreBoard.startMatch("Poland", "Germany");
        scoreBoard.updateScore("Poland", "Germany", 2, 2);

        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore("Spain", "Brazil", 1, 3);

        scoreBoard.startMatch("Argentina", "Australia");
        scoreBoard.updateScore("Argentina", "Australia", 2, 2);

        // When
        List<Match> summary = scoreBoard.getSummary();

        // Then
        assertThat(summary).hasSize(3);

        assertThat(summary.get(0).getHomeTeam()).isEqualTo("Argentina");
        assertThat(summary.get(0).getAwayTeam()).isEqualTo("Australia");

        assertThat(summary.get(1).getHomeTeam()).isEqualTo("Spain");
        assertThat(summary.get(1).getAwayTeam()).isEqualTo("Brazil");

        assertThat(summary.get(2).getHomeTeam()).isEqualTo("Poland");
        assertThat(summary.get(2).getAwayTeam()).isEqualTo("Germany");
    }

    @Test
    public void shouldNotStartDuplicateMatch() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startMatch("Poland", "Germany");

        // When
        Throwable thrown = catchThrowable(() -> {
            scoreBoard.startMatch("Poland", "Germany");
        });

        // Then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Match between these teams is already in progress.");
    }

    @Test
    public void shouldReturnEmptySummaryWhenNoMatches() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();

        // When
        List<Match> summary = scoreBoard.getSummary();

        // Then
        assertThat(summary).isEmpty();
    }
}