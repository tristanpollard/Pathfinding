import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Created by Tristan on 2017-04-13.
 */
public class QuadTree {

    Rectangle tree;

    boolean hasObject = false;

    QuadTree topLeft, topRight, bottomLeft, bottomRight;


    public QuadTree(int topLeftX, int topLeftY, int w, int h){
        tree = new Rectangle(topLeftX, topLeftY, w, h);
    }

    public void draw(Graphics2D g){
        if (!hasObject) {
            g.drawRect(tree.point.x, tree.point.y, tree.w, tree.h);

            if (topLeft != null) {
                topLeft.draw(g);
            }
            if (topRight != null) {
                topRight.draw(g);
            }
            if (bottomRight != null) {
                bottomRight.draw(g);
            }
            if (bottomLeft != null) {
                bottomLeft.draw(g);
            }
        }
    }

    public boolean add(int x, int y){

        if (!validPoint(x,y)){
            return false;
        }

        if (tree.h == 1 && tree.w == 1){ //1 cm accuracy
            hasObject = true;
            return true;
        }

        if (topLeft == null){
            divideTree();
        }

        if (topLeft.add(x,y)){
            return true;
        }
        if (topRight.add(x,y)){
            return true;
        }
        if (bottomLeft.add(x,y)){
            return true;
        }
        if (bottomRight.add(x,y)){
            return true;
        }

        return false;
    }

    public void divideTree(){
        if (tree.h == 1){
            topLeft = new QuadTree(tree.point.x, tree.point.y,1,1);
            topRight = new QuadTree(tree.point.x + 1, tree.point.y,1,1);
        }else if(tree.w == 1){
            topLeft = new QuadTree(tree.point.x, tree.point.y, 1,1);
            topRight = new QuadTree(tree.point.x, tree.point.y + 1,1,1);
        }else{
            int halfWidth = tree.w/2;
            int otherHalfWidth = (int)Math.ceil((double)tree.w/2.0);

            int halfHeight = tree.h/2;
            int otherHalfHeight = (int)Math.ceil((double)tree.h/2.0);

            topLeft = new QuadTree(tree.point.x, tree.point.y, halfWidth, halfHeight);
            topRight = new QuadTree(tree.point.x + halfWidth, tree.point.y, otherHalfWidth, halfHeight);

            bottomLeft = new QuadTree(tree.point.x, tree.point.y + halfHeight, halfWidth, otherHalfHeight);
            bottomRight = new QuadTree(tree.point.x + halfWidth, tree.point.y + halfHeight, otherHalfWidth, otherHalfHeight);
        }
    }

     public Point getTreePoint(int x, int y){

         if (!validPoint(x,y)){
             return null;
         }

         if (topLeft == null){
             return tree.point;
         }

         Point ret;
         ret = topLeft.getTreePoint(x,y);
         if (ret != null){
             return ret;
         }
         ret = topRight.getTreePoint(x,y);
         if (ret != null){
             return ret;
         }
         ret = bottomLeft.getTreePoint(x,y);
         if (ret != null){
             return ret;
         }
         ret = bottomRight.getTreePoint(x,y);
         if (ret != null){
             return ret;
         }

         return ret;
     }

    public ArrayList<Rectangle> getNeighborNodes(int x, int y){

        ArrayList<Rectangle> neighbors = new ArrayList<>();

        Rectangle currentNode = getTreeNode(x, y);

        int nextNodeX = currentNode.point.x;
        int nextNodeY = currentNode.point.y - 1;
        Rectangle neighbor = getTreeNode(nextNodeX, nextNodeY);
        while (neighbor != null && nextNodeX < currentNode.point.x + currentNode.w){
            neighbors.add(neighbor);
            nextNodeX += neighbor.w;
            neighbor = getTreeNode(nextNodeX, nextNodeY);
        }

        nextNodeX = currentNode.point.x;
        nextNodeY = currentNode.point.y + currentNode.h;
        neighbor = getTreeNode(nextNodeX, nextNodeY);
        while (neighbor != null && nextNodeX < currentNode.point.x + currentNode.w){
            neighbors.add(neighbor);
            nextNodeX += neighbor.w;
            neighbor = getTreeNode(nextNodeX, nextNodeY);
        }

        nextNodeX = currentNode.point.x - 1;
        nextNodeY = currentNode.point.y;
        neighbor = getTreeNode(nextNodeX, nextNodeY);
        while (neighbor != null && nextNodeY < currentNode.point.y + currentNode.h){
            neighbors.add(neighbor);
            nextNodeY += neighbor.h;
            neighbor = getTreeNode(nextNodeX, nextNodeY);
        }


        nextNodeX = currentNode.point.x + currentNode.w;
        nextNodeY = currentNode.point.y;
        neighbor = getTreeNode(nextNodeX, nextNodeY);
        while (neighbor != null && nextNodeY < currentNode.point.y + currentNode.h){
            neighbors.add(neighbor);
            nextNodeY += neighbor.h;
            neighbor = getTreeNode(nextNodeX, nextNodeY);
        }

        return neighbors;

    }

    public Rectangle getTreeNode(int x, int y){

        if (!validPoint(x,y)){
            return null;
        }

        if (topLeft == null ){
            if (hasObject){
                return null;
            }
            return tree;
        }

        Rectangle ret = topLeft.getTreeNode(x,y);
        if (ret != null){
            return ret;
        }
        ret = topRight.getTreeNode(x,y);
        if (ret != null){
            return ret;
        }

        if (bottomLeft != null && bottomRight != null){
            ret = bottomLeft.getTreeNode(x,y);
            if (ret != null){
                return ret;
            }
            ret = bottomRight.getTreeNode(x,y);
            if (ret != null) {
                return ret;
            }
        }

        return ret;
    }

    public boolean validPoint(int x, int y){
        if (y < tree.point.y || y >= (tree.point.y + tree.h) || x < tree.point.x || x >= (tree.point.x + tree.w)){
            return false;
        }

        return true;
    }


}
