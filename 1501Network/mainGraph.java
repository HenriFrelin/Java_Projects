/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;
/**
 *
 * @author Henri
 */
public class mainGraph {
    // private data for calculations below
    private double Latency;
    private boolean copperOnly = true;
    private int numV; // # of vertices 
    private Vertex[] vertexes; // add functionality 
    private ArrayList<Edge> MinTree; // add functionality 
    private ArrayList<Edge> edges;
    
    public mainGraph(String input) throws FileNotFoundException{
        MinTree = null;
        graph(input);
    }
    
    private void graph(String input) throws FileNotFoundException{
        
        Scanner s = new Scanner(new File(input));
        Scanner s1 = new Scanner(System.in);
        
        numV = s.nextInt();
        //this.graph = new NetworkAnalysis.Graph(v);
        System.out.println("Text File Info Added to Graph: ");
        vertexes = new Vertex[numV];
        for(int i = 0; i < vertexes.length; i++)
            vertexes[i] = new Vertex(i);
        
        edges = new ArrayList<Edge>();
        while(s.hasNext()){

            Vertex v0 = vertexes[s.nextInt()]; // startpoint 
            Vertex v1 = vertexes[s.nextInt()]; // endpoint
            String type = s.next(); // cable type 
            int band = s.nextInt(); // bandwidth
            int len = s.nextInt(); // length

            Edge e0 = new Edge(type, band, len, v0, v1);
            Edge e1 = new Edge(type, band, len, v1, v0);
            v0.getConn().add(e0);
            v1.getConn().add(e1);
            edges.add(e0);
            if(type.equals("copper")){
                copperOnly = false;
            }
            
            
            System.out.println(v0.ID() + " : " + v1.ID() + " : " + type + " : " + band + " : " + len);
          
        }
        
    }
    
    public void lowPath(int id0, int id1){
        if(id0 >= numV || id1 >= numV){
            System.out.println("Error, this vertex doesn't exist! Try again!");
            return;
        }
        Vertex v0 = vertexes[id0];
        Vertex v1 = vertexes[id1];
        
        Object[]shortPath = shortestPath(v0,v1,""+v0.ID(),0L,-1); 
        String p = (String)shortPath[0];
        String d = "";
        for(int i = 0; i < p.length(); i++){
            if(i<p.length()-1)
                d += p.charAt(i) + " --> ";
            else
                d += p.charAt(i);
        }
        p = d;
        double pSpeed = (double)shortPath[1];
        int pBand = (int)shortPath[2];
        System.out.println("\nShortest path: " + p + "\nLatency: " + pSpeed + "\nBandwidth: " + pBand);
    }
    
    private Object[] shortestPath(Vertex v0, Vertex v1, String p, double len, int band){  // null case???////////////
        if(v0 == v1)
            return new Object[] {p,len,band};
        
        LinkedList<Edge> edge0 = v0.getConn();
        double minL = -1.0;
        String minP = "";
        for(Edge e : edge0){
            Vertex eDest = e.getDest();
            if(p.contains("" + eDest.ID())) 
                continue;
            String nPath = p + eDest.ID();
            double nLen = 0.0;
            if(e.getType().equals("copper"))
                nLen = len + e.getSpeed();
            else if(e.getType().equals("optical"))
                nLen = len + e.getSpeed();
            else
                return null;
            
            int nBand = band;
            if(band == -1.0 || e.getBand() < band) 
                nBand = e.getBand();
            
            Object[] pathD = shortestPath(eDest, v1, nPath, nLen, nBand);
            if(pathD == null)
                continue;
            
            String ePath = (String)pathD[0];
            double pathL = (double)pathD[1];
            int pathB = (int)pathD[2];
            
            if(minL == -1 || pathL < minL){
                minL = pathL;
                minP = ePath;
                band = pathB;
            }
            else if(pathL == minL && pathB > band){
                minL = pathL;
                minP = ePath;
                band = pathB;
            }
        }
        if(minL > -1.0)
            return new Object[] {minP, minL, band};
        
        return null;
    }
    
    public void checkCopper(){
        if(copperOnly)
            System.out.println("Only copper wires in this graph: so it's copper-only connected!");
        else{
            boolean copperConn = true; 
            for(int j = 0; j < numV; j++){
                LinkedList<Edge> vEdges = vertexes[j].getConn();
                boolean isCopper = false;
                for(Edge e:vEdges){
                    if(e.getType().equals("copper")){
                        isCopper = true;
                        break;
                    }
                }
                if(!isCopper){
                    copperConn = false;
                    break;
                }
            }
            if(copperConn)
                System.out.println("Can be connected with only copper, but has fiber optic.");
            else
                System.out.println("Not copper-only and can't be connected with copper-only wires.");
        }
        
    }
    
    public void findMaxData(int id0, int id1){
        if(id0 >= numV || id1 >= numV){
            System.out.println("Error, this vertex doesn't exist! Try again!");
            return;
        }
        Vertex v0 = vertexes[id0];
        Vertex v1 = vertexes[id1];
        int max = maxData(v0, v1, ""+v0.ID(), -1);
        System.out.println("Max data between " + v0.ID() + " & " + v1.ID() + " is : " + max + "mbps");
    }
    
    private int maxData(Vertex v0, Vertex v1, String p, int band){
        if(v0 == v1){
            return band;
        }
        LinkedList<Edge> edges1 = v0.getConn();
        int i = -1;
        for(Edge e:edges1){
            Vertex eDest = e.getDest();
            if(p.contains(""+eDest.ID()))
                continue;
            int newBand = band;
            if(newBand == -1 || e.getBand() < newBand)
                newBand = e.getBand();
            String newP = p + eDest.ID();
            int pBand = maxData(eDest, v1, newP, newBand);
            if(pBand == -1)
                continue;
            if(pBand > i)
                i = pBand;
        }
        return i;
    }
    
    public void findMST(){
        if(MinTree == null){
            Latency = MST();
        }
        System.out.println("Minimum Average Latency Spanning Tree\n");
        for(Edge e:MinTree)
            System.out.println(" [ " + e.source().ID() + " , " + e.getDest().ID() + " ] ");
        System.out.println("Average Latency of MST is " + Latency);
    }
    
    private double MST(){
        int[] p = new int[numV];
        byte[] r = new byte[numV];
        for(int j = 0; j < numV; j++){
            p[j] = j;
            r[j] = 0;
        }
        Collections.sort(edges,(e1,e2) -> e1.compareTo(e2));
        MinTree = new ArrayList<Edge>();
        double time = 0.0;
        int v0 = 0;
        while(v0 != edges.size()-1 && MinTree.size() < numV - 1){
            Edge e = edges.get(v0);
            int i = e.source().ID();
            int j = e.getDest().ID();
            if(!connect(i,j,p)){
                edgeUnion(i,j,p,r);
                MinTree.add(e);
                time += e.getSpeed();
            }
            v0++;
        }
        return time;
    }
    
    private boolean connect(int i, int j, int[] p){
        return look(i, p) == look(j, p);
    }
    private int look(int i, int[] p){
        while(i != p[i]){
            p[i] = p[p[i]];
            i = p[i];
        }
        return i;
    }
    private void edgeUnion(int i, int j, int[] p, byte[] r){
        int rP = look(i, p);
        int rQ = look(j, p);
        if(rP == rQ)
            return;
        if(r[rP] < r[rQ])
            p[rP] = rQ;
        else if(r[rP] > r[rQ])
            p[rQ] = rP;
        else{
            p[rQ] = rP;
            r[rP]++;
        }    
    }
    
    public void vertFail(){
        for(int i = 0; i < numV-1; i++){
            for(int j = i + 1; j < numV; j++){
                Vertex s = null;
                Vertex f1 = vertexes[i];
                Vertex f2 = vertexes[j];
                boolean[] seen = new boolean [numV];
                seen[f1.ID()] = true;
                seen[f2.ID()] = true;
                if(i != 0)
                    s = vertexes[0];
                else{
                    if(j != numV-1)
                        s = vertexes[j+1];
                    else if(j-i != 1)
                        s = vertexes[j-1];
                    else{
                        System.out.println("Not connected when any two vertices fail!");
                        return;
                    }
                }
                connectVerts(s,f1,f2,seen);
                boolean conn = true;
                for(int z = 0; z < seen.length; z++){
                    if(seen[z] == false){
                        conn = false;
                        break;
                    }
                }
                if(!conn){
                    System.out.println("Not connected when any two vertices fail!");
                    return;        
                }
            }
        }
        System.out.println("Connected even when any two vertices fail!");
        return;
    }
    
    private void connectVerts(Vertex v0, Vertex v1, Vertex v2, boolean[] seen){
        if(seen[v0.ID()] == true)
            return;
        seen[v0.ID()] = true;
        LinkedList<Edge> edges0 = v0.getConn();
        for(Edge e:edges0){
            Vertex eDest = e.getDest();
            if(seen[eDest.ID()] == true)
                continue;
            connectVerts(eDest, v1, v2, seen);
        }
        return;
    }
    
    public class Vertex{
        private int i;
        private LinkedList<Edge> Conn;
        public Vertex(int temp){
            i = temp;
            Conn = new LinkedList<Edge>();
        }
        public int ID(){
            return i;
        }
        public LinkedList <Edge> getConn(){
            return Conn;
        }
  
    }
    public class Edge implements Comparable<Edge>{
        // private data for calculations below
        private int len;
        private int band;
        private String type;
        private int cSpeed = 230000000;
        private int fSpeed = 200000000;
        private double speed;
        private Vertex v0;
        private Vertex v1;
        
        public Edge(String t, int b, int l, Vertex vt0, Vertex vt1){
            v0 = vt0;
            v1 = vt1;
            len = l;
            band = b;
            type = t;
            if(type.equals("copper")){
                speed = ((double) 1/cSpeed)*len*Math.pow(10,9);
            }
            if(type.equals("optical")){
                speed = ((double) 1/fSpeed)*len*Math.pow(10,9);
            }
                    
        }
        public double getSpeed(){
                return speed;
        }
        public Vertex getDest(){
            return v1;
        }
        public String getType(){
            return type;
        }
        public int getBand(){
            return band;
        }
        public Vertex source(){
            return v0;
        }
        public int compareTo(Edge e){
            if(speed > e.getSpeed())
                return 1;
            else if(speed == e.getSpeed())
                return 0;
            else
                return -1;
        }
        
    }
}
