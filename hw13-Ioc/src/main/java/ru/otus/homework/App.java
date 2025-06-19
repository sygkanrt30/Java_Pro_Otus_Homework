package ru.otus.homework;

import ru.otus.homework.appcontainer.AppComponentsContainerImpl;
import ru.otus.homework.appcontainer.api.AppComponentsContainer;
import ru.otus.homework.config.AppConfig1;
import ru.otus.homework.config.AppConfig2;
import ru.otus.homework.services.GameProcessor;

@SuppressWarnings({"squid:S125", "squid:S106"})
public class App {
    public static void main(String[] args) {
        // Опциональные варианты
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // Обязательный вариант
        // AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        // GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        // GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
