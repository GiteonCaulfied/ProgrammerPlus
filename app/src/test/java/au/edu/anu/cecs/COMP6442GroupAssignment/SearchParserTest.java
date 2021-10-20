package au.edu.anu.cecs.COMP6442GroupAssignment;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.AndExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.Exp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.KeyExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.NonExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.OrExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;

public class SearchParserTest {
    public static Post p1;
    public static Post p2;
    public static Post p3;
    public static Post p4;

    @BeforeClass
    public static void init(){

        p1 = new Post("1","1","001","1","1234","",new HashMap<>());
        p2 = new Post("2","2","002","2","3455","",new HashMap<>());
        p3 = new Post("3","3","003","3","3655","",new HashMap<>());
        p4 = new Post("4","4","004","4","3555","",new HashMap<>());
        System.out.println("1234");
    }

    @Test
    public void AndExpTest(){


        KeyExp term1 = new KeyExp("field","1",null);
        KeyExp term2 = new KeyExp("field","2",null);



        ArrayList<Post> l1 = new ArrayList<>();
        l1.add(p1);l1.add(p2);l1.add(p3);
        ArrayList<Post> l2 = new ArrayList<>();
        l2.add(p2);l2.add(p3);l2.add(p4);
        ArrayList<Post> test = new ArrayList<>();
        test.add(p2);test.add(p3);

        term1.setPostList(l1);
        term2.setPostList(l2);

        AndExp andExp = new AndExp(term1,term2);

        assertEquals(andExp.evaluate(),test);



    }

    @Test
    public void ORExpTest (){
        KeyExp term1 = new KeyExp("field","1",null);
        KeyExp term2 = new KeyExp("field","2",null);



        ArrayList<Post> l1 = new ArrayList<>();

        l1.add(p1);l1.add(p2);l1.add(p3);
        ArrayList<Post> l2 = new ArrayList<>();
        l2.add(p2);l2.add(p3);l2.add(p4);
        ArrayList<Post> test = new ArrayList<>();
        test.add(p1);test.add(p2);test.add(p3);test.add(p4);

        term1.setPostList(l1);
        term2.setPostList(l2);

        OrExp orExp = new OrExp(term1,term2);

        assertEquals(orExp.evaluate(),test);

    }
    @Test
    public void NonExpTest(){
        KeyExp term1 = new KeyExp("field","1",null);
        NonExp term2 = new NonExp();

        ArrayList<Post> l1 = new ArrayList<>();
        l1.add(p1);l1.add(p2);l1.add(p3);

        ArrayList<Post> test = new ArrayList<>();
        test.add(p1);test.add(p2);test.add(p3);

        term1.setPostList(l1);


        OrExp orExp = new OrExp(term1,term2);

        assertEquals(orExp.evaluate(),test);

        AndExp andExp = new AndExp(term1,term2);

        assertEquals(andExp.evaluate(),test);
    }
}
