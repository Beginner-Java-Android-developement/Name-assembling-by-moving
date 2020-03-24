package com.golan.amit.nameassemble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class NamingHelper {

    public static final String[] NAMESTR = {
            "אריאל" ,
            "ליאור"
    };

    public static final int ARIEL = 0;
    public static final int LIOR = 1;

    private static final int MAGIC_NUMBER = 7;
    private int name_ptr;

    private int[] rnd_name;
    private int name_rnd_index;
    /**
     * Position handeling
     */
    private int name_counter;
    private Stack<Integer> sti, stu;



    /**
     * constructor
     */
    public NamingHelper() {
        this.name_ptr = ARIEL;
        rnd_name = new int[NAMESTR[name_ptr].length()];
        resetName_counter();
        sti = new Stack<>();
        stu = new Stack<>();
    }

    public String getNameCharByIndex(int ind) {
        if(ind < 0 || ind > rnd_name.length) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(NAMESTR[name_ptr].charAt(rnd_name[ind]));
        return sb.toString();
    }

    public String name_rnd_representation() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < rnd_name.length; i++) {
            sb.append(NAMESTR[name_ptr].charAt(rnd_name[i]));
            if(i < (rnd_name.length - 1)) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public String getCurr_name() {
        return NAMESTR[name_ptr];
    }

    /**
     * Random generator
     */
    public void generate_random_name() {
        List<Integer> computerNumberList = new ArrayList<>();
        for(int i = 0; i < NAMESTR[name_ptr].length(); i++) {
            computerNumberList.add(i);
        }
        Collections.shuffle(computerNumberList);
        for(int i = 0; i < NAMESTR[name_ptr].length(); i++) {
            rnd_name[i] = computerNumberList.get(i);
        }
    }

    public void generate_ordered_name() {
        List<Integer> computerNumberList = new ArrayList<>();
        for(int i = 0; i < NAMESTR[name_ptr].length(); i++) {
            computerNumberList.add(i);
        }
        for(int i = 0; i < NAMESTR[name_ptr].length(); i++) {
            rnd_name[i] = computerNumberList.get(i);
        }
    }

    public void generate_Name_rnd_index() {
        this.name_rnd_index = (int)(Math.random() * MAGIC_NUMBER + MAGIC_NUMBER);
    }

    public int[] getRnd_name() {
        return rnd_name;
    }

    /**
     * Getters & Setters
     */

    public int getName_rnd_index() {
        return name_rnd_index;
    }

    /**
     * Position handeling
     */

    private void resetName_counter() {
        this.name_counter = 0;
    }

    public int getName_counter() {
        return name_counter;
    }

    public void increaseName_counter() {
        this.name_counter++;
    }

    public void decreaseName_counter() {
        this.name_counter--;
    }

    /**
     * Stack handling
     * @return
     */

    public Stack<Integer> getSti() {
        return sti;
    }

    public Stack<Integer> getStu() {
        return stu;
    }

    public void setSti(Stack<Integer> sti) {
        this.sti = sti;
    }

    public void push_stack(Integer item) {
        this.sti.push(item);
    }

    public void push_stack_u(Integer item) {
        this.stu.push(item);
    }

    public Integer pop_stack() {
        if(sti.size() > 0)
            return this.sti.pop();
        else
            return -1;
    }

    public Integer pop_stack_u() {
        if(stu.size() > 0)
            return this.stu.pop();
        else
            return -1;
    }

    public int getName_ptr() {
        return name_ptr;
    }

    public void setName_ptr(int name_ptr) {
        this.name_ptr = name_ptr;
    }


}
