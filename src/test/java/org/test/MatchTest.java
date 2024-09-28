package org.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class MatchTest {

    @Test
    public void shouldCreateMatchWithValidTeams() {
        // Given
        String homeTeam = "Poland";
        String awayTeam = "Germany";

        // When
        Match match = new Match(homeTeam, awayTeam);

        // Then
        assertThat(match.getHomeTeam()).isEqualTo(homeTeam);
        assertThat(match.getAwayTeam()).isEqualTo(awayTeam);
        assertThat(match.getHomeScore()).isZero();
        assertThat(match.getAwayScore()).isZero();
        assertThat(match.getStartTime()).isNotNull();
        assertThat(match.getTotalScore()).isZero();
    }

    @Test
    public void shouldNotCreateMatchWithNullHomeTeam() {
        // Given
        String awayTeam = "Germany";

        // When
        Throwable thrown = catchThrowable(() -> {
            new Match(null, awayTeam);
        });

        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("homeTeam is marked non-null but is null");
    }

    @Test
    public void shouldNotCreateMatchWithNullAwayTeam() {
        // Given
        String homeTeam = "Poland";

        // When
        Throwable thrown = catchThrowable(() -> {
            new Match(homeTeam, null);
        });

        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("awayTeam is marked non-null but is null");
    }

    @Test
    public void shouldUpdateScoresWithValidValues() {
        // Given
        Match match = new Match("Poland", "Germany");

        // When
        match.updateScores(2, 3);

        // Then
        assertThat(match.getHomeScore()).isEqualTo(2);
        assertThat(match.getAwayScore()).isEqualTo(3);
        assertThat(match.getTotalScore()).isEqualTo(5);
    }

    @Test
    public void shouldUpdateScoresToZero() {
        // Given
        Match match = new Match("Poland", "Germany");
        match.updateScores(1, 1);

        // When
        match.updateScores(0, 0);

        // Then
        assertThat(match.getHomeScore()).isZero();
        assertThat(match.getAwayScore()).isZero();
        assertThat(match.getTotalScore()).isZero();
    }

    @Test
    public void shouldNotUpdateScoresWithNegativeHomeScore() {
        // Given
        Match match = new Match("Poland", "Germany");

        // When
        Throwable thrown = catchThrowable(() -> {
            match.updateScores(-1, 2);
        });

        // Then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scores cannot be negative.");
    }

    @Test
    public void shouldNotUpdateScoresWithNegativeAwayScore() {
        // Given
        Match match = new Match("Poland", "Germany");

        // When
        Throwable thrown = catchThrowable(() -> {
            match.updateScores(2, -1);
        });

        // Then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scores cannot be negative.");
    }

    @Test
    public void shouldNotUpdateScoresWithBothNegativeScores() {
        // Given
        Match match = new Match("Poland", "Germany");

        // When
        Throwable thrown = catchThrowable(() -> {
            match.updateScores(-1, -1);
        });

        // Then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Scores cannot be negative.");
    }

    @Test
    public void shouldSetStartTimeAtCreation() {
        // Given
        LocalDateTime beforeCreation = LocalDateTime.now();

        // When
        Match match = new Match("Poland", "Germany");

        // Then
        assertThat(match.getStartTime()).isAfterOrEqualTo(beforeCreation);
        assertThat(match.getStartTime()).isBeforeOrEqualTo(LocalDateTime.now());
    }

}