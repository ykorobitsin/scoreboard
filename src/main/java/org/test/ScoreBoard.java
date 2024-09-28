package org.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
public class ScoreBoard {

    private final Map<String, Match> matches = new ConcurrentHashMap<>();

    /**
     * Starts a new match between the given teams.
     *
     * @param homeTeam home team.
     * @param awayTeam away team.
     * @throws IllegalArgumentException if a match between the same teams is already in progress.
     */
    public void startMatch(String homeTeam, String awayTeam) {
        validateTeamNames(homeTeam, awayTeam);
        String key = generateKey(homeTeam, awayTeam);
        Match match = matches.putIfAbsent(key, new Match(homeTeam, awayTeam));
        if (nonNull(match)) {
            log.warn("Attempt to start a match between {} and {} that is already in progress.", homeTeam, awayTeam);
            throw new IllegalArgumentException("Match between these teams is already in progress.");
        }
        log.info("Started match between {} and {}", homeTeam, awayTeam);
    }

    /**
     * Updates the score for an existing match.
     *
     * @param homeTeam  home team name.
     * @param awayTeam  away team name.
     * @param homeScore updated score for the home team.
     * @param awayScore updated score for the away team.
     * @throws IllegalArgumentException if the match does not exist.
     */
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validateTeamNames(homeTeam, awayTeam);
        if (homeScore < 0 || awayScore < 0) {
            log.error("Attempt to update scores with negative values: homeScore={}, awayScore={}", homeScore, awayScore);
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        String key = generateKey(homeTeam, awayTeam);
        Match match = matches.get(key);
        if (match == null) {
            log.error("Attempt to update a non-existent match between {} and {}", homeTeam, awayTeam);
            throw new IllegalArgumentException("Match not found.");
        }

        match.updateScores(homeScore, awayScore);
        log.info("Updated scores for match between {} and {}: homeScore={}, awayScore={}", homeTeam, awayTeam, homeScore, awayScore);
    }

    /**
     * Finishes the match between the given teams.
     *
     * @param homeTeam name of the home team.
     * @param awayTeam name of the away team.
     */
    public void finishMatch(String homeTeam, String awayTeam) {
        validateTeamNames(homeTeam, awayTeam);
        String key = generateKey(homeTeam, awayTeam);
        Match removedMatch = matches.remove(key);
        if (isNull(removedMatch)) {
            log.warn("Attempt to finish a non-existent match between {} and {}", homeTeam, awayTeam);
            throw new IllegalArgumentException("Match between these teams does not exist.");
        }
    }

    /**
     * Gets the summary of all matches in progress ordered by total score.
     * Matches with the same total score are ordered by the most recently started match.
     *
     * @return list of matches in the required order.
     */
    public List<Match> getSummary() {
        Comparator<Match> comparator = Comparator.comparingInt(Match::getTotalScore)
                .thenComparing(Match::getStartTime).reversed();
        List<Match> summary = matches.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        log.info("Generated summary of all matches in progress. Total matches: {}", summary.size());
        return summary;
    }

    private void validateTeamNames(String homeTeam, String awayTeam) {
        if (StringUtils.isAnyBlank(homeTeam, awayTeam)) {
            log.error(
                    "Validation failed: team names cannot be null or empty. homeTeam='{}', awayTeam='{}'",
                    homeTeam, awayTeam);
            throw new IllegalArgumentException("Team names cannot be null or empty.");
        }

        if (homeTeam.equals(awayTeam)) {
            log.error(
                    "Validation failed: home team and away team cannot be the same. homeTeam='{}', awayTeam='{}'",
                    homeTeam, awayTeam);
            throw new IllegalArgumentException("Home team and away team cannot be the same.");
        }
    }

    /**
     * Generates a key for a match based on home and away teams.
     *
     * @param homeTeam home team name.
     * @param awayTeam away team name.
     */
    private String generateKey(String homeTeam, String awayTeam) {
        return homeTeam + " vs " + awayTeam;
    }
}