package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        // Путь к файлу с входными значениями
        final String input = "C:\\Users\\akmanova.elizaveta\\Desktop\\streams\\input.txt";
        // Путь к файлу, в который запишется результат
        final String output = "C:\\Users\\akmanova.elizaveta\\Desktop\\streams\\output.txt";
        // Пороговое значение
        final int threshold = 3;

        final String result = readFromFileAndExecute(input, threshold);
        writeIntoFile(output, result);
    }

    /**
     * Чтение из файла и "отбор" подходящих значений для output стрима
     * @param fileName Путь к файлу
     * @param threshold Пороговое значение
     * @return Итоговый результат
     */
    private static String readFromFileAndExecute(final String fileName, final int threshold) {
        final StringBuilder resultStringBuilder = new StringBuilder();
        // Создаём входящий стрим (канал), из которого формируем reader
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line;
            Integer previousValue = null;
            // Построчно считываем данные
            while ((line = br.readLine()) != null) {
                // Значение в текущей строке
                final int value = Integer.parseInt(line);
                // Проверка выполнения условия для записи в выходящий канад
                if (previousValue != null && checkCondition(value - previousValue, threshold)) {
                    resultStringBuilder.append(line).append("\n");
                }
                // Сетим предыдущее значение
                previousValue = value;
            }
        } catch (final IOException e) {
            System.out.println("Произошла ошибка при чтении из файла");
        }
        return resultStringBuilder.toString();
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
