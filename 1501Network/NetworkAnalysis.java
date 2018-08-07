import java.util.*;
import java.io.FileNotFoundException;

public class NetworkAnalysis {

    
      public static void main(String[] args) throws FileNotFoundException {
        mainGraph g = new mainGraph("network_data1.txt");
        Scanner s1 = new Scanner(System.in);
          
        while(true){
        System.out.println("------------------------------");
        System.out.println("Type '0' to find the Lowest Latency Path Between 2 Points \n     '1' to check Copper Connectivity \n     '2' to check the Max Data Transmission \n     '3' to check the Minimum Average Latency Spanning Tree \n     '4' to determine if graph will remain connected after any 2 vertices fail \n     '5' to EXIT ");
         
        int ans = s1.nextInt();
          System.out.println();
   
            if(ans == 0){
                //NetworkAnalysis adjacencyList = new NetworkAnalysis();
                //adjacencyList.createStaticGraph(file);
                
                System.out.println();
                System.out.println("Enter Source Node: ");
                int v0 = s1.nextInt();
                System.out.println("Enter Destination Node: ");
                int v1 = s1.nextInt();
                //adjacencyList.dijikstra(i0, i1);
                
                g.lowPath(v0, v1);             
            }    
            else if(ans == 1){ 
                g.checkCopper();
            }
            else if(ans == 2){
                System.out.println();
                System.out.println("Enter Source Node: ");
                int v0 = s1.nextInt();
                System.out.println("Enter Destination Node: ");
                int v1 = s1.nextInt();
                
                g.findMaxData(v0, v1);
            }
            else if(ans == 3){ 
                g.findMST();
            }
            else if(ans == 4){ 
                g.vertFail();
            }
            else if(ans == 5){ 
                System.exit(0);
            }
            
        }

    }

}
