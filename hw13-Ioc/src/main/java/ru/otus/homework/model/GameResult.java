package ru.otus.homework.model;

public class GameResult {
    private static final String RESULT_PATTERN = "Уважаемый: %s. Всего было примеров: %d, отвечено верно: %d";
    private final Player player;
    private int total;
    private int rightAnswers;

    public GameResult(Player player) {
        this.player = player;
    }

    public void incrementRightAnswers(boolean mustIncremented) {
        total++;
        if (mustIncremented) {
            rightAnswers++;
        }
    }

    @Override
    public String toString() {
        return String.format(RESULT_PATTERN, player.name(), total, rightAnswers);
    }
}
