package au.edu.anu.cecs.COMP6442GroupAssignment;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.AndExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.KeyExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.NonExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.Search.OrExp;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.TempMess.Parser;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.TempMess.Tokenizer;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Post;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Profile;

public class TempMessageTest {
    public static Profile currentUser;
    public static Profile friend;
    public static long lastTime;

    @BeforeClass
    public static void init() {
        currentUser = new Profile("u1", "qinyu.zhao@anu.edu.au",
                "Qinyu Zhao", "HI!!", true);
        friend = new Profile("u1", "xiangyu.hui@anu.edu.au",
                "Xiangyu Hui", "HI!!", true);
        lastTime = System.currentTimeMillis() - 23 * 60 * 60 * 1000;
    }

    @Test
    public void testNothing() {
        String temp = "Hi! How are you?";
        Tokenizer tokenizer = new Tokenizer(temp);
        Parser parser = new Parser(tokenizer, currentUser, friend, lastTime);

        assertEquals(parser.parse(), temp);
    }

    @Test
    public void testMyname() {
        String temp = "Hi! How are you? My name is %MY_USERNAME%";
        String answer = "Hi! How are you? My name is Qinyu Zhao";
        Tokenizer tokenizer = new Tokenizer(temp);
        Parser parser = new Parser(tokenizer, currentUser, friend, lastTime);

        assertEquals(parser.parse(), answer);
    }

    @Test
    public void testFriName() {
        String temp = "Hi! Are you %FRI_USERNAME%?";
        String answer = "Hi! Are you Xiangyu Hui?";
        Tokenizer tokenizer = new Tokenizer(temp);
        Parser parser = new Parser(tokenizer, currentUser, friend, lastTime);

        assertEquals(parser.parse(), answer);
    }

    @Test
    public void testMyEmailAddress() {
        String temp = "Hi! My email address is %MY_EMAILADDRESS%";
        String answer = "Hi! My email address is qinyu.zhao@anu.edu.au";
        Tokenizer tokenizer = new Tokenizer(temp);
        Parser parser = new Parser(tokenizer, currentUser, friend, lastTime);

        assertEquals(parser.parse(), answer);
    }

    @Test
    public void testLastMessTime() {
        String temp = "Hi! We haven't talked for %MESSTIME_TO_CURR%";
        String answer = "Hi! We haven't talked for 23 hour(s)";
        Tokenizer tokenizer = new Tokenizer(temp);
        Parser parser = new Parser(tokenizer, currentUser, friend, lastTime);

        assertEquals(parser.parse(), answer);
    }

    @Test
    public void testComplex() {
        String temp = "Hi %FRI_USERNAME%, I am not available now. Please send an email to me. My email address is %MY_EMAILADDRESS%. Cheers, %MY_USERNAME%.";
        String answer = "Hi Xiangyu Hui, I am not available now. Please send an email to me. My email address is qinyu.zhao@anu.edu.au. Cheers, Qinyu Zhao.";
        Tokenizer tokenizer = new Tokenizer(temp);
        Parser parser = new Parser(tokenizer, currentUser, friend, lastTime);

        assertEquals(parser.parse(), answer);
    }
}