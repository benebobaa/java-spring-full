package test;

import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class ArrayTest {

    @Test
    public void testCreateArray(){
        Student[] intArr;

        intArr = new Student[2];

        intArr[0] = new Student(1, "bene");
        intArr[1] = new Student(2, "budi");
        intArr[2] = new Student(2, "budi");
//        for (var value: intArr){
//            System.out.println(value);
//        }
    }


    @Test
    public void testPassbyRef(){
        int[] arr = {1,2,3,4};

        System.out.println(Arrays.toString(arr));

        testPassArr(arr);

        System.out.println(Arrays.toString(arr));

        String a = "A";
        testPassObject(a);
        System.out.println(a);

        HashMap<String, String>  map = new HashMap<>();
        map.put("Z", "z");
        testPassHashMap(map);
        System.out.println(map.get("A"));
        Set<String> strings = map.keySet();
        System.out.println(strings.toString());
    }

    void testPassArr(int[] arr){
        arr[0] = 5;
    }

    void testPassObject(String value){
        value = "B";
    }

    void testPassHashMap(HashMap<String, String> value){
        value.put("A", "a");
        value.put("B", "a");
        value.put("C", "a");
    }
}

class Student {
    public int no;
    public String name;

    Student(int no, String name){
        this.no = no;
        this.name = name;

    }

    @Override
    public String toString() {
        return "Student{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }
}
