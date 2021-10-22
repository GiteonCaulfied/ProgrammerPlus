package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure;


public class AVLTree<T extends Comparable<T>> {
    private AVLTreeNode<T> root;

    public class AVLTreeNode<T extends Comparable<T>> {
        T key;
        int height;
        AVLTreeNode<T> leftNode;

        AVLTreeNode<T> rightNode;


        public AVLTreeNode(T key, AVLTreeNode<T> left, AVLTreeNode<T> right) {
            this.key = key;
            this.leftNode = left;
            this.rightNode = right;
            this.height = 0;
        }

        public T getKey() {
            return key;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public AVLTreeNode<T> getLeft() {
            return leftNode;
        }

        public void setLeft(AVLTreeNode<T> left) {
            this.leftNode = left;
        }

        public AVLTreeNode<T> getRight() {
            return rightNode;
        }

        public void setRight(AVLTreeNode<T> right) {
            this.rightNode = right;
        }
    }


    public AVLTree() {
        root = null;
    }

    /**
     * this function will return the height of the given node
     * @param tree
     * @return height
     */
    private int getHeight(AVLTreeNode<T> tree) {
        if (tree != null)
            return tree.height;

        return 0;
    }




    private int max(int a, int b) {
        return a > b ? a : b;
    }


    /**
     * search the target value form given node
     * @param x given node
     * @param key target value
     * @return return the node contains the target value
     */
    private AVLTreeNode<T> search(AVLTreeNode<T> x, T key) {
        if (x == null)
            return x;

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return search(x.leftNode, key);
        else if (cmp > 0)
            return search(x.rightNode, key);
        else
            return x;
    }

    /**
     * search the target form the root
     * @param key
     * @return
     */
    public AVLTreeNode<T> search(T key) {
        return search(root, key);
    }




    /**
     * find the max value
     * @param tree
     * @return
     */
    private AVLTreeNode<T> maximum(AVLTreeNode<T> tree) {
        if (tree == null)
            return null;

        while (tree.rightNode != null)
            tree = tree.rightNode;
        return tree;
    }




    private AVLTreeNode<T> leftLeftRotation(AVLTreeNode<T> k2) {
        AVLTreeNode<T> k1;

        k1 = k2.leftNode;
        k2.leftNode = k1.rightNode;
        k1.rightNode = k2;

        k2.height = max(getHeight(k2.leftNode), getHeight(k2.rightNode)) + 1;
        k1.height = max(getHeight(k1.leftNode), k2.height) + 1;

        return k1;
    }


    private AVLTreeNode<T> rightRightRotation(AVLTreeNode<T> k1) {
        AVLTreeNode<T> k2;

        k2 = k1.rightNode;
        k1.rightNode = k2.leftNode;
        k2.leftNode = k1;

        k1.height = max(getHeight(k1.leftNode), getHeight(k1.rightNode)) + 1;
        k2.height = max(getHeight(k2.rightNode), k1.height) + 1;

        return k2;
    }


    private AVLTreeNode<T> leftRightRotation(AVLTreeNode<T> k3) {
        k3.leftNode = rightRightRotation(k3.leftNode);

        return leftLeftRotation(k3);
    }

    private AVLTreeNode<T> rightLeftRotation(AVLTreeNode<T> k1) {
        k1.rightNode = leftLeftRotation(k1.rightNode);

        return rightRightRotation(k1);
    }

    public AVLTreeNode<T> createANode (T key){
        return new AVLTreeNode<T>(key, null, null);
    }

    /**
     * insert a new key to given tree
     * @param node
     * @param key
     * @return
     */
    private AVLTreeNode<T> insert(AVLTreeNode<T> node, T key) {
        if (node == null) {
            node = new AVLTreeNode<T>(key, null, null);
            if (node == null) {
                return null;
            }
        } else {


            if (key.compareTo(node.key) < 0) {
                node.leftNode = insert(node.leftNode, key);

                if (getHeight(node.leftNode) - getHeight(node.rightNode) == 2) {
                    if (key.compareTo(node.leftNode.key) < 0)
                        node = leftLeftRotation(node);
                    else
                        node = leftRightRotation(node);
                }
            } else if (key.compareTo(node.key)> 0) {
                node.rightNode = insert(node.rightNode, key);

                if (getHeight(node.rightNode) - getHeight(node.leftNode) == 2) {
                    if (key.compareTo(node.rightNode.key) > 0)
                        node = rightRightRotation(node);
                    else
                        node = rightLeftRotation(node);
                }
            } else {    // cmp==0

            }
        }

        node.height = max(getHeight(node.leftNode), getHeight(node.rightNode)) + 1;

        return node;
    }

    public AVLTreeNode<T> getroot() {
        return root;
    }

    public void setroot(AVLTreeNode<T> root) {
        this.root = root;
    }

    public void insert(T key) {
        root = insert(root, key);
    }

    /**
     * remove a key form the AVL tree
     * @param tree
     * @param target
     * @return
     */
    private AVLTreeNode<T> delete(AVLTreeNode<T> tree, AVLTreeNode<T> target) {

        if (tree == null || target == null)
            return null;


        if (target.key.compareTo(tree.key) < 0) {
            tree.leftNode = delete(tree.leftNode, target);

            if (getHeight(tree.rightNode) - getHeight(tree.leftNode) == 2) {
                AVLTreeNode<T> r = tree.rightNode;
                if (getHeight(r.leftNode) > getHeight(r.rightNode))
                    tree = rightLeftRotation(tree);
                else
                    tree = rightRightRotation(tree);
            }
        } else if (target.key.compareTo(tree.key) > 0) {
            tree.rightNode = delete(tree.rightNode, target);

            if (getHeight(tree.leftNode) - getHeight(tree.rightNode) == 2) {
                AVLTreeNode<T> l = tree.leftNode;
                if (getHeight(l.rightNode) > getHeight(l.leftNode))
                    tree = leftRightRotation(tree);
                else
                    tree = leftLeftRotation(tree);
            }
        } else {

            if ((tree.leftNode != null) && (tree.rightNode != null)) {
                if (getHeight(tree.leftNode) > getHeight(tree.rightNode)) {

                    AVLTreeNode<T> max = maximum(tree.leftNode);
                    tree.key = max.key;
                    tree.leftNode = delete(tree.leftNode, max);
                } else {

                    AVLTreeNode<T> min = maximum(tree.rightNode);
                    tree.key = min.key;
                    tree.rightNode = delete(tree.rightNode, min);
                }
            } else {
                AVLTreeNode<T> tmp = tree;
                tree = (tree.leftNode != null) ? tree.leftNode : tree.rightNode;
                tmp = null;
            }
        }

        return tree;
    }

    public boolean delete(T key) {
        AVLTreeNode<T> target;

        if ((target = search(root, key)) != null) {
            root = delete(root, target);
            return true;
        }else {
            return false;
        }
    }


}