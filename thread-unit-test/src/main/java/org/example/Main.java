package org.example;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }

        Ikea ikea = new Ikea();
        ikea.madeFrom();
        ikea.manyLegs();
        ikea.brand();
        ikea.type();

        ikea.setNickname(5);
        ikea.setNickname("bene");
        ikea.setNickname('C');
        ikea.setNickname(2.5);
        ikea.setNickname(2.5f);

    }
}

interface Chair {
    void brand();
    void type();
}

abstract class Table {
    abstract void madeFrom();
    void manyLegs(){
        System.out.println(4);
    }
}



class Ikea extends Table implements Chair {

    @Override
    void madeFrom() {
        System.out.println("Jati Wood");
    }

    @Override
    void manyLegs() {
        System.out.println("8");
    }

    @Override
    public void brand() {
        System.out.println(this.getClass());
    }

    @Override
    public void type() {
        System.out.println("Work Set");
    }

    public <T> void setNickname(T name){
        System.out.println(name.getClass());
        System.out.println(name);
    }

}

