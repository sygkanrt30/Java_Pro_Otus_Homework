package ru.otus.homework.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.homework.listener.Listener;
import ru.otus.homework.model.Message;
import ru.otus.homework.processor.Processor;
import ru.otus.homework.processor.homework.ThrowEvenSecProcessor;
import ru.otus.homework.processor.homework.TimeProvider;

class ComplexProcessorTest {

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        // given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});

        // when
        var result = complexProcessor.handle(message);

        // then
        verify(processor1).process(message);
        verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        // given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        // when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        // then
        verify(processor1, times(1)).process(message);
        verify(processor2, never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        // given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {});

        complexProcessor.addListener(listener);

        // when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        // then
        verify(listener, times(1)).onUpdated(message);
    }

    @Test
    @DisplayName("Тестируем выброс исключения на четной секунде")
    void throwingExceptionProcessorTest() {
        // given
        var message = new Message.Builder(1L).field1("field1").build();

        Processor processor1 = new ThrowEvenSecProcessor(new TestTimeProvider(LocalTime.of(2, 23, 0)));
        var processors = List.of(processor1);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));
    }

    @Test
    @DisplayName("Тестируем не выброс исключения на нечетной секунде")
    void throwingExceptionProcessorTest2() {
        // given
        var message = new Message.Builder(1L).field1("field1").build();

        Processor processor1 = new ThrowEvenSecProcessor(new TestTimeProvider(LocalTime.of(2, 23, 1)));
        var processors = List.of(processor1);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        assertThat(complexProcessor.handle(message)).isEqualTo(message);
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }

    private record TestTimeProvider(LocalTime localTime) implements TimeProvider {
        @Override
        public LocalTime getTime() {
            return localTime;
        }
    }
}
