package org.test;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Data
public class Match {
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private LocalDateTime startTime;

    public Match(String homeTeam, String awayTeam) {
        if (StringUtils.isAnyBlank(homeTeam, awayTeam)) {
            throw new IllegalArgumentException("Team names cannot be null or empty.");
        }

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.startTime = LocalDateTime.now();
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }
}
