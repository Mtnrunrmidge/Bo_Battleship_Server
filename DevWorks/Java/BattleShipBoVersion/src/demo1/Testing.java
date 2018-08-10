package demo1;

import javax.xml.stream.events.Characters;

public class Testing {

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("F", 32));
        System.out.println(Character.getNumericValue('+'));
        System.out.println(Character.getNumericValue('-'));
        System.out.println(Character.getNumericValue('a'));
        for (int i = -1000; i <10000; i++) {
            System.out.print((char) i + "  ");
            if (i % 8 == 0) System.out.println();
        }
        System.out.println(Character.getNumericValue('我'));
    }
}
