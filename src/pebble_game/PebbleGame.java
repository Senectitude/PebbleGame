package pebble_game;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
/**
 * This class includes nested classes for the player and the game
 * 
 */
public class PebbleGame {
    /**
     * Here we store two static variables,
     * rand stores a method to get a new random number inside a range
     * lock is used to make other threads wait until the current thread has finished
     */
    static Random rand = new Random();
    static Object lock = new Object();

    /*
        This class stores the methods that create the pebble game. 
        It also stores the bags that are needed thoughout the game
    */
    public static class pebbleGame {
        
        //Number of players
        static int noPlayers;

        //Black bag arrays
        static ArrayList<Integer> X = new ArrayList<Integer>();
        static ArrayList<Integer> Y = new ArrayList<Integer>();
        static ArrayList<Integer> Z = new ArrayList<Integer>();

        //White bag arrays
        static ArrayList<Integer> A = new ArrayList<Integer>();
        static ArrayList<Integer> B = new ArrayList<Integer>();
        static ArrayList<Integer> C = new ArrayList<Integer>();

        /**
         * Gives the black bags values and requests the number of players
         */
        static void loadGame() {

            // initailse the scanner
            Scanner in = new Scanner(System.in);

            while (noPlayers == 0) { 
                while(true){
                    System.out.println("Please enter the number of players: ");
                    noPlayers = in.nextInt();

                    if (noPlayers > 0) {
                        System.out.println("Okay");
                        break;
                    } else if (noPlayers <= 0) {
                        System.out.println("Please enter a positive number of players");
                        break;
                    }
                }
            }
            
            String fileName;
            System.out.println("Please enter the location of the first bag");
            fileName = in.next();
            X = loadBlackBags(X, noPlayers, fileName);
            
            System.out.println("Please enter the location of the second bag");
            fileName = in.next();
            Y = loadBlackBags(Y, noPlayers, fileName);

            System.out.println("Please enter the location of the third bag");
            fileName = in.next();
            Z = loadBlackBags(Z, noPlayers, fileName);
                
            in.close();
        }

        static void whiteBags(String emptyBagName) {
            ArrayList<Integer> sisterBag;
            ArrayList<Integer> emptyBag;
            System.out.println("the empty bag is " + emptyBagName);
            if (emptyBagName == "X"){
                sisterBag = pebbleGame.A;
                emptyBag = pebbleGame.X;
            } else if (emptyBagName == "Y"){
                sisterBag = pebbleGame.B;
                emptyBag = pebbleGame.Y;
            } else {
                sisterBag = pebbleGame.C;
                emptyBag = pebbleGame.Z;
            }

            for (int x : sisterBag){
                emptyBag.add(x);
            }
            sisterBag.clear();
        }
     
    }


    public static ArrayList<Integer> loadBlackBags(ArrayList<Integer> list, Integer noPlayers, String list2) { //works 
        Integer[] weights = getWeights(list2);
        for (int i = 0; i < (noPlayers * 11); i++) {
            list.add(weights[rand.nextInt(weights.length)]);
        }
        return list;
    }

    public static Integer[] getWeights(String fileName) { //works
        ArrayList<Integer> weights = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                for (int i = 0; i< tokens.length; i++ ){
                    weights.add(Integer.parseInt(tokens[i].trim()));
                }
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println(e);

        } catch (IOException e) {
            System.out.println(e);
        }
        
        return weights.toArray(new Integer[0]);
    }

    public static class Player extends Thread {
        ArrayList<Integer> playersPebbles = new ArrayList<Integer>();
        ArrayList<String> fromWhere = new ArrayList<String>();

        int location;
        Integer item;
        ArrayList<Integer> bag;
        String blackBagName;
        String emptyBagName = "";

        void givePlayersPebbles() throws Exception {
            List<String> blackBags = Arrays.asList("X","Y","Z");
            ArrayList<Integer> bag = null;
            String blackBagName;
            if (playersPebbles.size() == 10){
                return;
            }else{
                try{
                    blackBagName = blackBags.get(rand.nextInt(3));
                    if (blackBagName == "X") {
                        bag = pebbleGame.X;
                    } else if (blackBagName == "Y") {
                        bag = pebbleGame.Y;
                    } else if (blackBagName == "Z") {
                        bag = pebbleGame.Z;
                    } 

                    for (int i=0; i < 10; i++){
                        int location = rand.nextInt(bag.size());
                        Integer item = bag.get(location);
                        playersPebbles.add(item);
                        bag.remove(location);
                        fromWhere.add(blackBagName);
                    }
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }

        boolean checkPlayerWon() {
            Integer sum = 0;
            for (Integer i : playersPebbles){
                sum += i;
            }
            if (sum == 100 && playersPebbles.size() == 10){
                return true;
            }
            return false;
        }

        void getPebbles() throws Exception{ 

            ArrayList<String> blackBags = new ArrayList<String>(Arrays.asList("X","Y","Z"));
            blackBagName = "";
            emptyBagName = "";

            System.out.println(blackBags);
            blackBagName = blackBags.get(rand.nextInt(3));

            if (blackBagName == "X") {

                bag = pebbleGame.X;

            } else if (blackBagName == "Y") {

                bag = pebbleGame.Y;

            } else {

                bag = pebbleGame.Z;

            }
            
            location = rand.nextInt(bag.size());
            System.out.println(bag.size());
            item = bag.get(location);
            playersPebbles.add(item);
            bag.remove(location);

            fromWhere.add(blackBagName);
            System.out.println(blackBagName);

            if (bag.size() == 0) {
                pebbleGame.whiteBags(blackBagName);
            }
            
            System.out.println("Player " + Thread.currentThread().getId() + " has drawn a " + item + " from bag " + blackBagName);
            System.out.println("\n");

        }

        void removePebble() {

            location = rand.nextInt(fromWhere.size());
            item = playersPebbles.get(location);
            blackBagName = fromWhere.get(location);
            
            if (blackBagName == "X") {

                pebbleGame.A.add(item);

            } else if (blackBagName == "Y") {

                pebbleGame.B.add(item);

            } else {

                pebbleGame.C.add(item); 

            }
            
            playersPebbles.remove(location);
            fromWhere.remove(location);
            
            System.out.println("Player " + Thread.currentThread().getId() + " Removed item: " + item + " from bag: " + blackBagName);

        }

        @Override
        public void run(){
            
            try{

                if (playersPebbles.isEmpty() == true){ 
                    givePlayersPebbles();
                    System.out.println("Player "+ Thread.currentThread().getId() + " Hand is "+ playersPebbles);
                }

                while (checkPlayerWon() == false){

                    synchronized(lock) {
                        removePebble();
                        getPebbles();
                        lock.notifyAll();
                    }

                }

                System.out.println("Hand: " + playersPebbles);
                System.out.println("Player " + Thread.currentThread().getId() + " has won");
                System.exit(0);
            
            } catch (Exception e) {

                System.out.println(e);

            }
            
        }

    }

    public static void main(String[] args) throws Exception {

        pebbleGame.loadGame(); 

        for (int i=0; i < pebbleGame.noPlayers; i++){ 

            Player object = new Player();
            object.start();

        }
        
        
    }

}