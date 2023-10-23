package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    // Сюда записываем результат
    private static final StringBuilder resultStringBuilder = new StringBuilder();

    // Предыдущее значение
    // Используем volatile для синхронизации, чтобы брать значение напрямую из памяти в обход кэша jvm
    private static volatile int previousValue;

    // Наличие предыдущего значения
    // Используем volatile для синхронизации, чтобы брать значение напрямую из памяти в обход кэша jvm
    private static volatile boolean hasPreviousValue = false;

    public static void main(String[] args) throws IOException {
        // Путь к файлу с входными значениями
        final String input = "C:\\Users\\akmanova.elizaveta\\Desktop\\streams\\input.txt";
        // Путь к файлу, в который запишется результат
        final String output = "C:\\Users\\akmanova.elizaveta\\Desktop\\streams\\output.txt";
        // Пороговое значение
        final int threshold = 3;

        final BufferedReader bufferedReader = readFromFile(input);

        // Создаём 2 потока с использованием Runnable
        final Runnable thread1 = () -> {
            try {
                execute(bufferedReader, threshold);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        final Runnable thread2 = () -> {
            try {
                execute(bufferedReader, threshold);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        // Запускаем потоки
        thread1.run();
        thread2.run();

        // Записываем результат в файл
        writeIntoFile(output, resultStringBuilder.toString());
    }

    /**
     * Чтение из файла и "отбор" подходящих значений для output стрима
     * @param fileName Путь к файлу
     * @return Входящий stream
     */
    private static BufferedReader readFromFile(final String fileName) throws IOException {
        // Создаём входящий стрим (канал), из которого формируем reader
        return new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
    }

    /**
     * "отбор" подходящих значений для output стрима
     * @param br Входящий стрим
     * @param threshold Пороговое значение
     */
    private static void execute(final BufferedReader br, final int threshold) throws IOException {
        if (br == null) {
            return;
        }
        final String line;
        synchronized (Main.class) {
            line = br.readLine();
        }
        if (line == null) {
            return;
        }
        // Значение в текущей строке
        final int value = Integer.parseInt(line);
        // Проверка выполнения условия для записи в выходящий канал
        if (hasPreviousValue && checkCondition(value - previousValue, threshold)) {
            resultStringBuilder.append(line).append("\n");
        }
        // Сетим предыдущее значение
        previousValue = value;
        hasPreviousValue = true;
        execute(br, threshold);
    }

    /**
     * Проверка условия
     * @param difference Разница между текущим значением и предыдущим
     * @param threshold Пороговое значение, с которым сравниваем разницу
     * @return флаг выполнения условия
     */
    private static boolean checkCondition(int difference, int threshold) {
        // Условие написано не очень понятно, поэтому разницу между предыдущим значением и текущим с порогом сравниваю по модулю
        // Если тут важен знак, модуль нужно убрать
        return Math.abs(difference) > threshold;
    }

    /**
     * Запись данных в файл
     * @param fileName Путь к файлу
     * @param content Строка, которая запишется в файл
     */
    private static void writeIntoFile(final String fileName, final String content) {
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (final IOException e) {
            System.out.println("При записи в файл произошла ошибка");
        }
    }

}
