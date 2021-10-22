package au.edu.anu.cecs.COMP6442GroupAssignment;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure.AVLTree;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.HateSpeechParser.Parser;
import au.edu.anu.cecs.COMP6442GroupAssignment.util.Parser.HateSpeechParser.Tokenizer;

public class HeatSpeechRemoveTest {
    static AVLTree<String> swearwords;

    @BeforeClass
    public static void init(){
        swearwords = new AVLTree<>();
        swearwords.insert("fuck");
        swearwords.insert("shit");
        swearwords.insert("asshole");
        swearwords.insert("ass");
    }
    @Test
    public void noMaskTest(){

        String withoutHeatSpeech1 = "I am really happy!";
        Parser parser1 = new Parser(new Tokenizer(swearwords,withoutHeatSpeech1));
        assertEquals(withoutHeatSpeech1,parser1.parse());
        String withoutHeatSpeech2= "Hey, how are you!";
        Parser parser2 = new Parser(new Tokenizer(swearwords,withoutHeatSpeech2));
        assertEquals(withoutHeatSpeech2,parser2.parse());
    }

    @Test
    public void MaskTest(){
        String HeatSpeech1 = "fuck you!";
        String result1 = "*** you!";
        Parser parser1 = new Parser(new Tokenizer(swearwords,HeatSpeech1));
        assertEquals(result1,parser1.parse());
        String HeatSpeech2= "Hey, asshole";
        String result2 = "Hey, ***";
        Parser parser2 = new Parser(new Tokenizer(swearwords,HeatSpeech2));
        assertEquals(result2,parser2.parse());
    }

    @Test public void LongSentenceTest(){
        String HeatSpeech1 = "Hey! you know what? I really wanna fuck you, you are just a piece of shit and I just think I am the best! ";
        String result1 = "Hey! you know what? I really wanna *** you, you are just a piece of *** and I just think I am the best! ";
        Parser parser1 = new Parser(new Tokenizer(swearwords,HeatSpeech1));
        assertEquals(result1,parser1.parse());
    }




}
