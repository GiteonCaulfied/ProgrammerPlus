package au.edu.cecs.COMP6442GroupAssignment.Utils.Tree;

public class AVLTree<T extends Comparable<T>> extends BinarySearchTree<T> {
    /*
        As a result of inheritance by using 'extends BinarySearchTree<T>,
        all class fields within BinarySearchTree are also present here.
        So while not explicitly written here, this class has:
            - value
            - leftNode
            - rightNode
     */

    public AVLTree() {
        super();
    }

    public AVLTree(T value) {
        super(value);
        // Set left and right children to be of EmptyAVL as opposed to EmptyBST.
        this.leftNode = new EmptyAVL<>();
        this.rightNode = new EmptyAVL<>();
    }

    public AVLTree(T value, Tree<T> leftNode, Tree<T> rightNode) {
        super(value, leftNode, rightNode);
    }

    /**
     * @return balance factor of the current node.
     */
    public int getBalanceFactor() {
        /*
             Note:
             Calculating the balance factor and height each time they are needed is less efficient than
             simply storing the height and balance factor as fields within each tree node (as some
             implementations of the AVLTree do). However, although it is inefficient, it is easier to implement.
         */
        return leftNode.getHeight() - rightNode.getHeight();
    }

    @Override
    public AVLTree<T> insert(T element) {
        /*
            TODO: Write and or complete your insertion code here
            Note that what each method does is described in its superclass unless edited.
            E.g. what 'insert' does is described in Tree.java.
         */
        // Ensure input is not null.
        if (element == null)
            throw new IllegalArgumentException("Input cannot be null");

        if (value == null) return new AVLTree<T>(element);

        AVLTree<T> curr;
        if (element.compareTo(value) > 0) {
            // COMPLETE
            curr = new AVLTree<>(value, leftNode, rightNode.insert(element));
            int bfThis = curr.getBalanceFactor();
            if (bfThis > 1 || bfThis < -1) {
                int bfChild = ((AVLTree<T>) curr.rightNode).getBalanceFactor();
                if (bfThis * bfChild > 0) {
                    return curr.leftRotate();
                }
                else {
                    curr.rightNode = ((AVLTree<T>) curr.rightNode).rightRotate();
                    return curr.leftRotate();
                }
            }
            return curr;
        } else if (element.compareTo(value) < 0) {
            // COMPLETE
            curr = new AVLTree<>(value, leftNode.insert(element), rightNode);
            int bfThis = curr.getBalanceFactor();
            if (bfThis > 1 || bfThis < -1) {
                int bfChild = ((AVLTree<T>) curr.leftNode).getBalanceFactor();
                if (bfThis * bfChild > 0) {
                    return curr.rightRotate();
                }
                else {
                    curr.leftNode = ((AVLTree<T>) curr.leftNode).leftRotate();
                    return curr.rightRotate();
                }
            }
            return curr;
        }

        return new AVLTree<>(value, leftNode, rightNode);
        // Change to return something different
    }

    /**
     * Conducts a left rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> leftRotate() {
        /*
            TODO: Write and or complete this method so that you can conduct a left rotation on the current node.
            This can be quite difficult to get your head around. Try looking for visualisations
            of left rotate if you are confused.

            Note: if this is implemented correctly than the return MUST be an AVL tree. However, the
            rotation may move around EmptyAVL trees. So when moving trees, the type of the trees can
            be 'Tree<T>'. However, the return type should be of AVLTree<T>. To cast the return type into
            AVLTree<T> simply use: (AVLTree<T>).

            If you get an casting exception such as:
            'java.lang.ClassCastException: class AVLTree$EmptyAVL cannot be cast to class AVLTree
            (AVLTree$EmptyAVL and AVLTree are in unnamed module of loader 'app')'
            than something about your code is incorrect!
         */

        Tree<T> newParent = this.rightNode;
        Tree<T> newRightOfCurrent = newParent.leftNode;
        // COMPLETE
        Tree<T> newLeftOfParent = new AVLTree<T>(this.value, this.leftNode, newRightOfCurrent);
        return new AVLTree<>(newParent.value, newLeftOfParent, newParent.rightNode); // Change to return something different
    }

    /**
     * Conducts a right rotation on the current node.
     *
     * @return the new 'current' or 'top' node after rotation.
     */
    public AVLTree<T> rightRotate() {
        /*
            TODO: Write this method so that you can conduct a right rotation on the current node.
            This can be quite difficult to get your head around. Try looking for visualisations
            of right rotate if you are confused.

            Note: if this is implemented correctly than the return MUST be an AVL tree. However, the
            rotation may move around EmptyAVL trees. So when moving trees, the type of the trees can
            be 'Tree<T>'. However, the return type should be of AVLTree<T>. To cast the return type into
            AVLTree<T> simply use: (AVLTree<T>).

            If you get an casting exception such as:
            'java.lang.ClassCastException: class AVLTree$EmptyAVL cannot be cast to class AVLTree
            (AVLTree$EmptyAVL and AVLTree are in unnamed module of loader 'app')'
            than something about your code is incorrect!
         */

        Tree<T> newParent = this.leftNode;
        Tree<T> newLeftOfCurrent = newParent.rightNode;
        Tree<T> newRightOfParent = new AVLTree<T>(this.value, newLeftOfCurrent, this.rightNode);
        return new AVLTree<>(newParent.value, newParent.leftNode, newRightOfParent);
        // Change to return something different
    }

    /**
     * Note that this is not within a file of its own... WHY?
     * The answer is: this is just a design decision. 'insert' here will return something specific
     * to the parent class inheriting Tree from BinarySearchTree. In this case an AVL tree.
     */
    public static class EmptyAVL<T extends Comparable<T>> extends EmptyTree<T> {
        @Override
        public Tree<T> insert(T element) {
            // The creation of a new Tree, hence, return tree.
            return new AVLTree<T>(element);
        }
    }
}
