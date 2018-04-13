package lab3.streams;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] argc) {
        int countElem = 15;
        int[] arr = new int[countElem];
        Random random = new Random();
        for (int i = 0; i < countElem; i++) {
            arr[i] = random.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));

        //Нахождение кол-ва элементов по условию: элементы > 40
        AtomicInteger count = new AtomicInteger(0);
        Arrays.stream(arr)
                .parallel()
                .filter(x -> x > 40)
                .forEach(x -> {
                    int oldValue, newValue;
                    do {
                        oldValue = count.get();
                        newValue = oldValue + 1;
                    } while (!count.compareAndSet(oldValue, newValue));
                });
        System.out.println("Колличество элементов, которые > 40: " + count);
        //Нахождение кол-ва элементов по условию: элементы > 40
      /*  long count = Arrays.stream(arr)
                .parallel()
                .filter(x -> x > 40)
                .count();
        System.out.println("Количество элементов в массиве, > 5: " + count + " элемент");
*/
        //Нахождение минимального элемента с индексом
        AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger minIndex = new AtomicInteger();
        IntStream.range(0, arr.length)
                .parallel()
                .forEachOrdered(x -> {
                    int oldIndex, newIndex, oldValue, newValue;
                    do {
                        if (arr[x] < min.get()) {
                            do {
                                oldValue = min.get();
                                newValue = arr[x];
                            } while (!min.compareAndSet(oldValue, newValue));
                            oldIndex = minIndex.get();
                            newIndex = x;
                        } else {
                            break;
                        }
                    } while (!minIndex.compareAndSet(oldIndex, newIndex));

                });
        System.out.println("Min значение: " + min);
        System.out.println("Индекс min значениея: " + minIndex);

        //Нахождение минимального элемента с индексом
        AtomicInteger max = new AtomicInteger(Integer.MIN_VALUE);
        AtomicInteger maxIndex = new AtomicInteger();
        IntStream.range(0, arr.length)
                .parallel()
                .forEach(x -> {
                    int oldIndex, newIndex, oldValue, newValue;
                    do {
                        if (arr[x] > max.get()) {
                            do {
                                //Получаем старое значение
                                oldValue = max.get();
                                newValue = arr[x];
                            } while (!max.compareAndSet(oldValue, newValue));
                            //получаем старое значение, которые ожидаем увидеть в переменной
                            oldIndex = maxIndex.get();
                            newIndex = x;
                        } else {
                            break;
                        }
                    } while (!maxIndex.compareAndSet(oldIndex, newIndex));
                });
        System.out.println("Max значение: " + max);
        System.out.println("Индекс max значениея: " + maxIndex);

        //Нахождение контрольной суммы с использованием xor
        AtomicInteger atomicCheckSum = new AtomicInteger();
        IntStream.range(0, arr.length)
                .parallel()
                .forEach(id -> {
                            int oldValue, newValue;
                            do {
                                oldValue = atomicCheckSum.get();
                                newValue = oldValue ^ arr[id];
                            } while (!atomicCheckSum.compareAndSet(oldValue, newValue));
                        }
                );
        System.out.println("Контрольная сумма = " + atomicCheckSum);
        System.out.println("Контрольная сумма в двоичной форме= " + Integer.toBinaryString(atomicCheckSum.get()));
    }
}
