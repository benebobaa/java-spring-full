import java.util.Arrays;

public class DataType {
    public static void main(String[] args) {
        String firstName = "Jhon";
        String lastName = "Doe";


        System.out.println(firstName + lastName);


        Bene bene = new Bene() {
            @Override
            void play() {
                System.out.println("Aku implement bene");;
            }
        };

        BeneInterface bene2 = () -> System.out.println("Bene Interface Play");

        bene.play();

        bene2.play();

        String[] arr = {"bene", "boba"};

        for (var value: arr){
            System.out.println(value);
        }

        System.out.println("======================");
        System.out.println("PRIMITIVE DATA TYPES\n");
        int x = 10;
        int y = x;
        System.out.print("Initially: ");
        System.out.println("x = " + x + ", y = " + y);

        // Here the change in the value of y
        // will not affect the value of x
        y = 30;

        System.out.print("After changing y to 30: ");
        System.out.println("x = " + x + ", y = " + y);
        System.out.println(
                "**Only value of y is affected here "
                        + "because of Primitive Data Type\n");

        System.out.println("REFERENCE DATA TYPES\n");
        int[] c = { 10, 20, 30, 40 };

        // Here complete reference of c is copied to d
        // and both point to same memory in Heap
        int[] d = c;

        System.out.println("Initially");
        System.out.println("Array c: "
                + Arrays.toString(c));
        System.out.println("Array d: "
                + Arrays.toString(d));

        // Modifying the value at
        // index 1 to 50 in array d
        System.out.println("\nModifying the value at "
                + "index 1 to 50 in array d\n");
        d[1] = 50;

        System.out.println("After modification");
        System.out.println("Array c: "
                + Arrays.toString(c));
        System.out.println("Array d: "
                + Arrays.toString(d));
        System.out.println(
                "**Here value of c[1] is also affected "
                        + "because of Reference Data Type\n");


        String coba = "Coba";

        coba = "Coba2";
        System.out.println(coba);

    }
}

abstract class Bene {
    void play(){
        System.out.println("Main");
    }
}

interface BeneInterface {
    void play();
}