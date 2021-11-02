package au.edu.anu.cecs.COMP6442GroupAssignment.util.DataStructure;

/**
 * Dear instructors, the previous version AVLTree was wrongly replaced by a version on the Web for IO testing purposes,
 * Because the parameter and the public function names are not different, so it didn't raise my notice in time.
 * For the purpose of honesty, I think it is necessary for me to replace the previous version and give explanation.
 */

import java.util.ListResourceBundle;

/**
 * @Author
 */
public class AVLTree <T extends Comparable<T>>{
    private AVLTreeNode<T> root;

    public static class AVLTreeNode<T extends Comparable<T>> {
        T key;
        int height;
        AVLTreeNode<T> leftNode;
        AVLTreeNode<T> rightNode;

        public AVLTreeNode(T key, AVLTreeNode<T> leftNode, AVLTreeNode<T> rightNode) {
            this.key = key;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
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

        public AVLTreeNode<T> getLeftNode() {
            return leftNode;
        }

        public void setLeftNode(AVLTreeNode<T> leftNode) {
            this.leftNode = leftNode;
        }

        public AVLTreeNode<T> getRightNode() {
            return rightNode;
        }

        public void setRightNode(AVLTreeNode<T> rightNode) {
            this.rightNode = rightNode;
        }

        public AVLTreeNode searchRecur(T key) {
            if (this == null) {
                return null;
            }
            if (key.compareTo(this.key) < 0) {
                if (this.leftNode==null){
                    return null;
                }
                return this.leftNode.searchRecur(key);
            } else if (key.compareTo(this.key) > 0) {
                if (this.rightNode==null){
                    return null;
                }
                return this.rightNode.searchRecur(key);
            } else {
                return this;
            }
        }


        public AVLTreeNode findMaxRecur() {
            if (this.rightNode == null) {
                return this;
            } else {
                return this.rightNode.findMaxRecur();
            }
        }


        public int getHeightDa() {

            int leftNodeHeight = leftNode == null ? 1 : 1 + leftNode.getHeightDa();
            int rightNodeHeight = rightNode == null ? 1 : 1 + rightNode.getHeightDa();

            this.height =  Math.max(leftNodeHeight, rightNodeHeight);
            return this.height;
        }

        public int getBalance(){
            int leftNodeHeight = leftNode == null ? 1 : 1 + leftNode.getHeightDa();
            int rightNodeHeight = rightNode == null ? 1 : 1 + rightNode.getHeightDa();
            return (leftNodeHeight - rightNodeHeight);
        }

        public AVLTreeNode insert(AVLTreeNode node) {
            AVLTreeNode output = null;


            if (node.key.compareTo(this.key) < 0) {
                if (this.leftNode==null){
                    this.leftNode=node;
                    this.height=this.getHeightDa();
                    return this;
                }
                this.leftNode = this.leftNode.insert(node);
                if (Math.abs(this.getBalance()) >= 2) {
                    if (node.key.compareTo(this.leftNode.key) < 0) {
                        output = rightRotation(this);
                    } else {
                        output = lrRotation(this);
                        output.getHeightDa();
                    }

                }else {
                    output =this;
                }
            } else if (node.key.compareTo(this.key) > 0) {
                if (this.rightNode==null){
                    this.rightNode=node;
                    this.height=getHeightDa();
                    return this;
                }
                this.rightNode = this.rightNode.insert(node);
                if (Math.abs(this.getBalance()) >= 2) {
                    if (node.key.compareTo(this.rightNode.key) > 0) {
                        output = leftRotation(this);
                    } else {
                        output = rlRotation(this);
                        output.getHeightDa();
                    }
                }else {
                    output = this;
                }
            }

            output.height=output.getHeightDa();
            return output;
        }

        public AVLTreeNode<T> delete(AVLTreeNode<T> target) {
            AVLTreeNode output = null;
            if (target.key.compareTo(key)<0){
                this.leftNode=this.leftNode.delete(target);
                if (Math.abs(this.getBalance())>=2){
//                    AVLTreeNode out = this.rightNode;
                    if (this.getBalance()>0){
                        output=rlRotation(this);
                    }else {
                        output = leftRotation(this);
                    }
                }
            }else if (target.key.compareTo(key)>0){
                this.rightNode=this.rightNode.delete(target);
                if (Math.abs(this.getBalance())>=2){
                    if (getBalance()<0){
                        output = lrRotation(this);
                    }else {
                        output=rightRotation(this);
                    }
                }
            }else {
                if (this.leftNode !=null && this.rightNode != null){
                    if (getBalance()>0){
                        AVLTreeNode maxl = leftNode.findMaxRecur();
                        this.key = (T) maxl.key;
                        this.leftNode = this.leftNode.delete(maxl);
                        output = this;
                    }else {
                        AVLTreeNode maxr = rightNode.findMaxRecur();
                        this.key = (T) maxr.key;
                        this.rightNode = this.rightNode.delete(maxr);
                        output = this;
                    }
                }else {
                    output = (this.leftNode != null) ? this.leftNode : this.rightNode;
                }
            }
            if (output!=null){
                output.getHeightDa();
            }
            return output;
        }
    }



    public AVLTree(){};

    public AVLTreeNode<T> createANode (T key){
        return new AVLTreeNode<T>(key, null, null);
    }


    public AVLTreeNode search( T key){
        if (key==null) return null;
        return root.searchRecur(key);
    }

    public AVLTreeNode findMax(T key){
        return root.findMaxRecur();
    }


    public static AVLTreeNode rightRotation(AVLTreeNode node){
        AVLTreeNode newRoot = node.leftNode;
        AVLTreeNode newLeft = node.leftNode.rightNode;
        AVLTreeNode newRight = node;


        newRoot.rightNode=newRight;
        newRight.leftNode=newLeft;
//        newRoot.height = newRoot.getHeight();
//        newRight.height = newRight.getHeight();

        return newRoot;

    }

    public static AVLTreeNode leftRotation(AVLTreeNode node){
        AVLTreeNode newRoot = node.rightNode;
        AVLTreeNode newRight = node.rightNode.leftNode;
        AVLTreeNode newLeft = node;


        newRoot.leftNode=newLeft;
        newLeft.rightNode=newRight;
//        newRoot.height = newRoot.getHeight();
//        newLeft.height = newLeft.getHeight();
        return newRoot;

    }


    public static AVLTreeNode lrRotation (AVLTreeNode node){
        node.leftNode = leftRotation(node.leftNode);
        AVLTreeNode newNode = rightRotation(node);
        return newNode;

    }

    public static AVLTreeNode rlRotation(AVLTreeNode node){
        node.rightNode = rightRotation(node.rightNode);
        AVLTreeNode newNode = leftRotation(node);
        return newNode;

    }

    public AVLTreeNode<T> getroot() {
        return root;
    }

    public void setroot(AVLTreeNode<T> root) {
        this.root = root;
    }

    public void insert( T key){
        if (root==null){
            root = new AVLTreeNode<>(key,null,null);
        }else {

            AVLTreeNode node = new AVLTreeNode<>(key,null,null);
            root=root.insert(node);
        }
    }

    public boolean delete(T key) {
        AVLTreeNode<T> target;
        if (root==null){
            return false;
        }
        if ((target = search(key)) != null){
            root = root.delete(target);
            return true;
        }else {
            return false;
        }

    }


}


