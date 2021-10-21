package au.edu.anu.cecs.COMP6442GroupAssignment;

import org.junit.Test;

import static org.junit.Assert.*;

import au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure.AVLTree;

public class AVLTreeTest {

    @Test
    public void SearchTest(){
        AVLTree<Integer> test = new AVLTree<>();
        AVLTree.AVLTreeNode node1 = test.createANode(1);
        AVLTree.AVLTreeNode node2 = test.createANode(2);
        AVLTree.AVLTreeNode node3 = test.createANode(3);

        test.setroot(node2);
        node2.setLeft(node1);
        node2.setRight(node3);
        assertEquals(test.search(3).getKey(), new Integer(3));

    }

    @Test
    public void InsertTest(){
        AVLTree<String> test = new AVLTree<>();
        test.insert("1");
        test.insert("2");
        test.insert("3");
        test.insert("4");
        test.insert("5");
        test.insert("6");
        assertEquals(test.search("2").getKey(), "2");
        assertEquals(test.search("4").getKey(), "4");

        assertTrue(Math.abs(test.getroot().getRight().getHeight()-test.getroot().getLeft().getHeight())<2);
    }

    @Test
    public void RemoveTest(){
        AVLTree<String> test = new AVLTree<>();
        test.insert("1");
        test.insert("2");
        test.insert("3");
        test.insert("4");
        test.insert("5");
        test.insert("6");
        assertEquals(test.search("4").getKey(), "4");
        test.delete("6");
        assertEquals(test.search("6"),null);
        test.delete("5");
        assertEquals(test.search("5"),null);
        assertTrue(Math.abs(test.getroot().getRight().getHeight()-test.getroot().getLeft().getHeight())<2);


    }





}
