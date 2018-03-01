package com.blacksheep;

import java.util.List;

public class checkPlagiarism {

    public void check(List l1, List l2){

        double size = l1.size();
        double size2 = l2.size();
        double min,cnt = 0;
        double match;
        if(size <= size2)
            min = size;
        else
            min = size2;

        for(int i =0; i<min;i++)
        {
            if(l1.get(i).equals(l2.get(i)))
                cnt++;
        }

        match = (cnt/min)*100;

        System.out.println("The match percent is ");
        System.out.print(match);
    }
}
