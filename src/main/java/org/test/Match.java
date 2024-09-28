package org.test;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Slf4j
public class Match {

    @NonNull
    @Getter
    private final String homeTeam;
    @NonNull
    @Getter
    private final String awayTeam;

    private final ReentrantLock lock = new ReentrantLock();

    @Getter
    private int homeScore = 0;
    @Getter
    private int awayScore = 0;

    @Getter
    private final LocalDateTime startTime = LocalDateTime.now();

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    public void updateScores(int newHomeScore, int newAwayScore) {
        if (newHomeScore < 0 || newAwayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        boolean lockAcquired = false;
        try {
            lockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
            this.homeScore = newHomeScore;
            this.awayScore = newAwayScore;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sth went wrong", e);
        } finally {
            if (lockAcquired) {
                lock.unlock();
            }
        }
    }

    @Override
    public String toString() {
        return homeTeam + " vs " + awayTeam + " [" + homeScore + " : " + awayScore + "]";
    }
}
