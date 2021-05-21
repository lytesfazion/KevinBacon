import java.io.FileReader;
import java.io.Reader;
import java.security.Key;
import java.util.Scanner;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

/**
 * Idea Behind my Assignment:
 * The goal was initially to create a hashmap where the keys are Actors and Values are other Actors that the key Actor has worked
 * with. I then ran into the issue of correctly passing through my hashmap of actors through bfs and was recommended, by a tutor
 * to try to give all of my actors IDs. So I tried to make another hashmap w the ids as keys, and values as the original hashmap
 * That is why I included a getKeyfromValue function. I wanted to get the ids and pass that thru bfs.
 *
 * I tell you all this just to get an idea of what is going on in the chaotic mess below that I claim as my code.
 */



class Graph<T> {



    // We use Hashmap to store the edges in the graph
    //The key is one actor and the value is a list of actors the key actor has worked with.
    public Map<T, List<T> > map = new HashMap<>();


    // This function adds a new vertex to the graph
    public void addVertex(T vertex)
    {
        //adds key and vertex
        map.put(vertex, new LinkedList<T>());
    }

    // This function adds the edge between source to destination
    public void addEdge(T source, T destination, boolean bidirectional) {

        if (!map.containsKey(source)){
            addVertex(source);
        }
//        if (!map.containsKey(destination)){
//            addVertex(destination);
//        }
        map.get(source).add(destination);
//        if (bidirectional == true) {
//            map.get(destination).add(source);
//        }
    }

//HashMap<Integer,IDHashmap>>
    public void printShortestPath( HashMap<Integer,String> adj, int s, int dest, int v)
    {
        // predecessor[i] array stores predecessor of
        // i and distance array stores distance of i from s
        int pred[] = new int[v];


        if (!BFS(adj, s, dest, v, pred)) {
            System.out.println("Given source and destination" + "are not connected");
            return;
        }

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<Integer>();
        int crawl = dest;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }
        // Print path
        System.out.println("Path is ::");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
        }
    }

    // a modified version of BFS
    private boolean BFS(HashMap<Integer,String> IntNameMap, int src, int dest, int v, int pred[])
    {

        Queue<Integer> queue = new LinkedList<Integer>();

        // boolean array visited[] which stores the information whether ith vertex is reached at least once in the Breadth first search
        boolean visited[] = new boolean[v];

        for (int i = 0; i < v; i++) {
            visited[i] = false;
            pred[i] = -1;
        }

        visited[src] = true;
        queue.add(src);

        // bfs Algorithm

        while (!queue.isEmpty()) {

            int u = queue.remove();
            System.out.println(u);
            for(T neighbor: map.get(u)){


                if (visited[(int) neighbor] == false) {
                    visited[(int) neighbor] = true;
                    //dist[neighbor] = dist[u] + 1;
                    pred[(int) neighbor] = u;

                    queue.add((Integer) neighbor);

                    // stopping condition (when we find
                    // our destination)
                    if (neighbor.equals(dest)){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}


public class movies {

    //Here I want to make the Key a number I can pass through the bfs and the value is the previous map
    public HashMap<Integer,Object> actorsIDmap = new HashMap<>();

    public HashMap<Integer,String> IDactorsmap = new HashMap<>();
    public HashMap<String,Integer> Map3 = new HashMap<>();


    public HashMap<Integer,LinkedList<Integer>> AdjList = new HashMap<>();
//    LinkedList<Integer> AdjacencyList = new LinkedList<>();


    public boolean validInputChecker(String username, Map cast){

        if (cast.containsKey(username)){
            return true;
        }else{
            return false;
        }

    }
    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static void main (String[] args) {

        movies function = new movies();
        Graph<String> graphfunction = new Graph();

        //IDHashmap idfunction = new IDHashmap();
        int ID = 0;

        try {
            Reader reader = new FileReader(args[0]);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            JSONParser jsonParser = new JSONParser();

            int movies = 0;

            for (CSVRecord csvRecord : csvParser) {
                if (movies >= 1) {
                    String title = csvRecord.get(1);
                    String castJSON = csvRecord.get(2);

                    // [] = array
                    // { } = "object" / "dictionary" / "hashtable" -- key"name": value

                    //prints title of the movie
                    System.out.println("Title: " + title);
                    Object object = jsonParser.parse(castJSON);
                    JSONArray jsonArray = (JSONArray)object;

                    HashSet<String> BrandNewMap = new HashSet<>();

                    //the goal is to add all actors as keys and make their values, other actors they've worked with
                    //i reps the current actor and j reps all other actors in the same movie

                    //loops thru the entire array of cast members
                    for (int i = 0; i < jsonArray.size(); i++) {

                        JSONObject jsonObject = (JSONObject)jsonArray.get(i); //grabs the item(actor) at index i
                        String actor = (String) jsonObject.get("name");

                        graphfunction.addVertex(actor);

                        System.out.println(" * " + jsonObject.get("name"));//prints actor name

                        //adds cast member at current index as Vertex specifically the key of the vertex
                        //graphfunction.addVertex(jsonObject.get("name"));

                        //This creates the connection between the actor and id by adding the pair into a hashmap
                        // a gives an actor(value) an id that i can use and adds the pair into

                        String parentName = (String) jsonObject.get("name");

                        //only add if the actorsmap doesnt contain the actor
//                        if (!function.Map3.containsKey(parentName)){
//
//                            function.IDactorsmap.put(ID,parentName);//puts in the actor as key and ID as value.
//                            graphfunction.addVertex(ID);
//                            function.Map3.put(parentName, ID++);//puts in the actor as key and ID as value.
//
//
//                        }

                        //also loops through all actors within this movie
                        for(int j = 0; j < jsonArray.size(); j++){

                            //this if statement makes sure the program doesn't also add the key as its own value
                            if (j != i){
                                JSONObject jsonObject2 = (JSONObject)jsonArray.get(j);
                                String child = (String) jsonObject2.get("name");
                                //graphfunction.addEdge(jsonObject.get("name"), jsonObject2.get("name"),true);

                                //adds the other actors as the edges
                                graphfunction.addEdge(parentName,child,true);

                            }

                        }

                    }
                }
                ++movies;
            }
            csvParser.close();
            System.out.println(graphfunction.map);
        }
        catch (Exception e) {
        // TODO Auto-generated catch block
        System.out.println("File " + args[0] + " is invalid or is in the wrong format.");
        }



        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter Actor 1: ");
        String actor1 = userInput.nextLine();

       //I only want to continue the program if the inputs are valid, so if actor1 is valid the user will be prompted
        //to input actor 2. if validInput returns false then this prints "no such actor"
        if (function.validInputChecker(actor1,graphfunction.map)){
            System.out.println("Enter Actor 2: ");
            String actor2 = userInput.nextLine();

            //Because I wanted to make the actors have ID's in order to pass them thru bfs easier I wanted to create a hashmap where the keys are
            // IDs and values are the key/value
            int KeyofActor1 = (int) getKeyFromValue(function.IDactorsmap,actor1);
            int KeyofActor2 = (int) getKeyFromValue(function.IDactorsmap,actor2);

            //check if actorinput matches getKey() if it does return the key in a variable and pass that variable thru the shortest path function

            if (function.validInputChecker(actor2,graphfunction.map)){
                System.out.println("Path between " + actor1 + "and " + actor2 + ":" );//insert function that returns path
                graphfunction.printShortestPath(function.IDactorsmap,KeyofActor1,KeyofActor2,100000);
            }else {
                System.out.println("No such actor");
            }
        }
        else {
            System.out.println("No such actor");
        }

    }


}

//Spit balling:

//for values in file, add vertex. if two actors have been in the same movie, add edge


//create an variable in the main repping the file. and pass it thru the funtion call
//shortestPath ( file, actor 1, actor 2)
//test case1: make sure the actors are valid

//figure out user input
//disect 2 shortest path algos- dikstra & Floyd

//store values in a graph and use dykstra's to find the shortest path'



// function to print the shortest distance and path
// between source vertex and destination vertex
