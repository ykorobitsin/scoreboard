package org.test;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreBoard {

    private final Map<String, Match> matches = new HashMap<>();

    /**
     * Starts a new match between the given teams.
     *
     * @param homeTeam home team.
     * @param awayTeam away team.
     * @throws IllegalArgumentException if a match between the same teams is already in progress.
     */
    public void startMatch(String homeTeam, String awayTeam) {
        if (StringUtils.isAnyBlank(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Team names cannot be null or empty.");
        }

        String key = generateKey(homeTeam, awayTeam);
        if (matches.containsKey(key)) {
            throw new IllegalArgumentException("Match between these teams is already in progress.");
        }

        matches.put(key, new Match(homeTeam, awayTeam));
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
        if (StringUtils.isAnyBlank(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Team names cannot be null or empty.");
        }

        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        String key = generateKey(homeTeam, awayTeam);
        Match match = matches.get(key);
        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }

        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
    }

    /**
     * Finishes the match between the given teams.
     *
     * @param homeTeam name of the home team.
     * @param awayTeam name of the away team.
     */
    public void finishMatch(String homeTeam, String awayTeam) {
        if (StringUtils.isAnyBlank(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Team names cannot be null or empty.");
        }

        String key = generateKey(homeTeam, awayTeam);
        matches.remove(key);
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
        return matches.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
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
