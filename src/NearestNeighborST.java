/*************************************************************************
 *  Part of the Polinalysis Project
 *  Authors: Kush Patel, Josh Shin, Kevin Sun, Aravind Yeduvaka, Jon Zhang
 *
 *************************************************************************/
public class NearestNeighborST<Value> {
    
    private Node root;  //  top of our tree
    private int size;   //  number of points in the tree
    private Point2D closest; //  closest point for closest method
    public double distance; //  shortest distance for nearest method
    
    //  nodes of our trees to hold keys and values and left and right links
    //  and rectangles
    private class Node {
        private Point2D p;   // point of our node (key)
        private Value val;   // value of our node
        private Node left;   // left / bottom subrectangle
        private Node right;  // right/ top subrectangle
        private boolean dir; // false if it splits vertically, true if horizontal
        
        //  constructor to set point, value, and direction
        private Node(Point2D point, Value v, boolean direction) {
            p = point;
            val = v;
            dir = direction;
        }
    }
    
   // construct an empty symbol table of points 
   public NearestNeighborST() {
       size = 0;
       root = null;
   }
   
   // is the symbol table empty? 
   public boolean isEmpty() {
       return size == 0;
   }
   
   // number of points
   public int size() {
       return size;
   }
   
   // associate the value val with point p
   public void put(Point2D p, Value val) {
       if (p == null || val == null)
           throw new NullPointerException("Argument is null");
       //  call our recursive put method
       put(p, val, root, null);
   }
   
   //  recursive method to properly put our value in the right place
   private Node put(Point2D p, Value val, Node n, Node parent) {
       
       //  if we need to insert a new node
       if (n == null) {
           size++;
           // base case root
           if (parent == null) {
               Node node = new Node(p, val, false);
               root = node;
               return node;
           }
           
           Node node = new Node(p, val, !parent.dir);
           
           return node;
       }
       
       //  recursive comparisons
       
       //  equality case
       if (p.equals(n.p)) {
           n.val = val;
           return n;
       }
       
       double cmp;
       
       //  compare either x or y coordinates based off what level we are on
       if (n.dir)
           cmp = p.y() - n.p.y();
       else 
           cmp = p.x() - n.p.x();
       
       //  go down and set left or right links
       if (cmp < 0) {
           n.left = put(p, val, n.left, n);
           return n;
       }
       else {
           n.right = put(p, val, n.right, n);
           return n;
       }
   }
   
   // value associated with point p 
   public Value get(Point2D p) {
       if (p == null)
           throw new NullPointerException("Argument is null");
       return get(p, root);
   }
   
   // private method that gets the value of point p rooted at node n
   private Value get(Point2D p, Node n) {
       //  null case
       if (n == null)
           return null;
       
       double cmp;
       
       //  compare either x or y coordinates based off what level we are on
       if (n.dir)
           cmp = p.y() - n.p.y();
       else 
           cmp = p.x() - n.p.x();
       
       //  recursive calls
       if (cmp < 0) return get(p, n.left);
       else if (cmp > 0) return get(p, n.right);
       else 
           if (p.equals(n.p)) 
               return n.val;
       return get(p, n.right);
           
   }
   
   // does the symbol table contain point p? 
   public boolean contains(Point2D p) {
       if (p == null)
           throw new NullPointerException("Argument is null");
       return get(p) != null;
   }
   
   // all points in the symbol table 
   public Iterable<Point2D> points() {
       Queue<Node> levelq = new Queue<Node>();         //  used to process each level
       Queue<Point2D> storageq = new Queue<Point2D>(); //  stores the points in order
       
       if (root == null)
           return storageq;
       
       //  code from lecture slide
       levelq.enqueue(root);
       while (!levelq.isEmpty())
       {
        Node x = levelq.dequeue();
        if (x == null) continue;
        storageq.enqueue(x.p);
        levelq.enqueue(x.left);
        levelq.enqueue(x.right);
       }
       return storageq;
   }
   
   // a nearest neighbor to point p; null if the symbol table is empty 
   public Point2D nearest(Point2D p) {
       if (p == null)
           throw new NullPointerException("Argument is null");
       distance = Double.POSITIVE_INFINITY;
       closest = null;
       
       //  call our nearest neighbor recursive method
       nearest(p, root);
       return closest;
   }
   
   //  private recursive search method for nearest
   private void nearest(Point2D p, Node n) {
       //  null case
       if (n == null)
           return;
       
       if (p.distanceSquaredTo(n.p) < distance) {
           closest = n.p;
           distance = p.distanceSquaredTo(n.p);
       }
       
       // depending on split, we go a certain direction
       //  horizontal split
       if (n.dir) {
           
           //  search down then up
           if (p.y() < n.p.y()) {
               nearest(p, n.left);
               
               //  search right side if we don't prune
               if (distance >= (n.p.y() - p.y()) * (n.p.y() - p.y()))
                   nearest(p, n.right);
           }
           
           //  search up then down
           else {
               nearest(p, n.right);
               
               //  search left side if we don't prune
               if (distance >= (p.y() - n.p.y()) * (p.y() - n.p.y()))
                   nearest(p, n.left);
           }
       }
       //  vertical split
       else {
       //  search left then right
           if (p.x() < n.p.x()) {
               nearest(p, n.left);
               
               //  search right side if we don't prune
               if (distance >= (n.p.x() - p.x()) * (n.p.x() - p.x()))
                   nearest(p, n.right);
           }
           
           //  search right then left
           else {
               nearest(p, n.right);
               
               //  search left side if we don't prune
               if (distance >= (p.x() - n.p.x()) * (p.x() - n.p.x()))
                   nearest(p, n.left);
           }
       }
       
       return;
   }

}