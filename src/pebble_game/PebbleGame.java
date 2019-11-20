package pebble_game;

import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
            System.out.println("Welcome to the PebbleGame!!\nYou will be asked to enter the number of players\nand then for the location of three files in turn containing comma seperated integer values for the pebble weights.\nThe integer values must be strictly positive.\nThe game will then be simulated, and output written to files in this directory\n");
            // This will end when the number of players entered is greater than 0 
            while (noPlayers == 0) { 

                while(true){

                    System.out.println("Please enter the number of players: ");
                    noPlayers = in.nextInt();

                    if (noPlayers > 0) {

                        //System.out.println("Okay");
                        break;

                    } else if (noPlayers <= 0) {

                        System.out.println("Please enter a positive number of players");
                        break;

                    }
                }
            }
            
            /**
             * Here we ask for three files,
             * The first will be loaded into bag X
             * The second will be loaded into bag Y
             * The last is loaded into bag Z
             * 
             */
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
            
            // Closing the scanner
            in.close();
        }

        /**
         * Here we take an empty bag by name, and fill it with its corresponding white bag
         * 
         * @param emptyBagName the bag that needs to be filled
         * 
         */
        static void whiteBags(String emptyBagName) {

            ArrayList<Integer> sisterBag;
            ArrayList<Integer> emptyBag;

            //System.out.println("the empty bag is " + emptyBagName);

            //Here we define how the white bags and black bags relate to each other
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

            //Empty the white bag after the black bag has been filled
            sisterBag.clear();
        }
     
    }

    /**
     * We load the black bags by fetching the weights from the files provided,
     * then a random value from the weights file is picked and added to the black bag
     * Each black bag needs 11 x the number of players of pebbles at the start of the game
     * 
     * @param list black bag list that needs to be filled
     * @param noPlayers the number of players 
     * @param weightsFile the name of the file which contains the weights
     * @return the black bag list
     */
    public static ArrayList<Integer> loadBlackBags(ArrayList<Integer> list, Integer noPlayers, String weightsFile) { //works 
        
        Integer[] weights = getWeights(weightsFile);
        
        for (int i = 0; i < (noPlayers * 11); i++) {
        
            list.add(weights[rand.nextInt(weights.length)]);
        
        }
        
        return list;
    }
    /**
     * Loading the weights file and adding them to a list
     * 
     * @param fileName the name of the file which is to be loaded 
     * @return the weights from the file as a list
     * 
     */
    public static Integer[] getWeights(String fileName) { //works
        
        ArrayList<Integer> weights = new ArrayList<>();
        
        try{

            // Creates a new reader which looks through the file
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            
            // Ends when all elements in the file have been looked at
            while ((line = reader.readLine()) != null) {

                // Removing the ','
                String[] tokens = line.split(",");

                for (int i = 0; i< tokens.length; i++ ){

                    weights.add(Integer.parseInt(tokens[i].trim()));
                
                }
            }

            // Closing the reader
            reader.close();

        //Catching two exceptions
        } catch (FileNotFoundException e) {

            System.out.println(e);

        } catch (IOException e) {

            System.out.println(e);

        }
        
        return weights.toArray(new Integer[0]);

    }
    /**
     * Player class includes all the methods that the player needs during the game
     * This class also extends Thread, which allows multiple instances of the player,
     * i.e. creating multiple players during compilation
     * 
     */
    public static class Player extends Thread {
        
        /**
         * The player needs two arrays,
         * playersPebbles which stores the weights of the pebbles in the player's hand
         * and fromWhere, which tracks which bag the players pebble was taken from
         * 
        */
        ArrayList<Integer> playersPebbles = new ArrayList<Integer>();
        ArrayList<String> fromWhere = new ArrayList<String>();

        int location;
        Integer item;
        ArrayList<Integer> bag;
        String blackBagName;
        String emptyBagName = "";

        /**
         * This method is ran at the start by the pebbleGame class,
         * It gives each player 10 pebbles from a random bag
         * 
         */
        void givePlayersPebbles() throws Exception {

            List<String> blackBags = Arrays.asList("X","Y","Z");
            ArrayList<Integer> bag = null;
            String blackBagName;

            /**
             * Since we can't order how threads run, we need to check if the player already has pebbles
             * This is to stop one player having too many pebbles to start with
             * 
             */
            if (playersPebbles.size() == 10){

                return;

            } else {

                try{
                    // Choosing one of the three black bags
                    blackBagName = blackBags.get(rand.nextInt(3));
                    
                    if (blackBagName == "X") {

                        bag = pebbleGame.X;

                    } else if (blackBagName == "Y") {

                        bag = pebbleGame.Y;

                    } else if (blackBagName == "Z") {

                        bag = pebbleGame.Z;

                    } 
                    
                    // Gives the player 10 pebbles and adds which bag it came from
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
        /**
         * This method checks if the current player has won,
         * A player wins when they have 10 pebbles in their hand and,
         * the weights of each pebble add up to 100
         * 
         * @return boolean true/false - true if the player wins
         * 
         */
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

        /**
         * This method is to get one pebble from a random bag
         * It also makes sure the bag isn't empty before a pebble is drawn
         * 
         */
        void getPebbles() throws Exception{ 

            ArrayList<String> blackBags = new ArrayList<String>(Arrays.asList("X","Y","Z"));
            blackBagName = "";
            emptyBagName = "";

            //System.out.println(blackBags);
            blackBagName = blackBags.get(rand.nextInt(3));

            if (blackBagName == "X") {
                bag = pebbleGame.X;
            } else if (blackBagName == "Y") {
                bag = pebbleGame.Y;
            } else {
                bag = pebbleGame.Z;
            }
            
            location = rand.nextInt(bag.size());
            //System.out.println(bag.size());
            item = bag.get(location);
            playersPebbles.add(item);
            bag.remove(location);
            fromWhere.add(blackBagName);

            //System.out.println(blackBagName);

            if (bag.size() == 0) {
                pebbleGame.whiteBags(blackBagName);
            }
            
            //System.out.println("Player " + Thread.currentThread().getId() + " has drawn a " + item + " from bag " + blackBagName);
            //System.out.println("\n");

            // Writing to the players file
            String nameOfFile = String.format("Player%s_output.txt", (Thread.currentThread().getId()-11) );
            BufferedWriter writer = new BufferedWriter(new FileWriter(nameOfFile, true));
            writer.write("Player " + (Thread.currentThread().getId()-11) + " has drawn a " + item + " from bag " + blackBagName + "\n");
            writer.write("Player " + (Thread.currentThread().getId()-11) + " hand is " + playersPebbles + "\n");
            writer.close();

        }

        /**
         * This method removes a random pebble from a players hand The pebble is then
         * added to one of the white bags
         * 
         * @throws IOException
         */
        void removePebble() throws IOException {

            location = rand.nextInt(fromWhere.size());
            item = playersPebbles.get(location);
            blackBagName = fromWhere.get(location);
            String whiteBagName = "";
            
            if (blackBagName == "X") {
                pebbleGame.A.add(item);
                whiteBagName = "A";
            } else if (blackBagName == "Y") {
                pebbleGame.B.add(item);
                whiteBagName = "B";
            } else {
                pebbleGame.C.add(item);
                whiteBagName = "C"; 
            }
            
            playersPebbles.remove(location);
            fromWhere.remove(location);
            
            //System.out.println("Player " + Thread.currentThread().getId() + " Removed item: " + item + " to bag " + whiteBagName);
            
            String nameOfFile = String.format("Player%s_output.txt", (Thread.currentThread().getId()-11) );
            BufferedWriter writer = new BufferedWriter(new FileWriter(nameOfFile, true));
            writer.write("Player " + (Thread.currentThread().getId()-11) + " has discarded " + item + " to bag " + whiteBagName + "\n");
            writer.write("Player " + (Thread.currentThread().getId()-11) + " hand is " + playersPebbles + "\n");
            writer.close();

        }
        
        /**
         * This is the main method that each thread calls when it starts
         * It runs an instance of the game
         * 
         */
        @Override
        public void run(){
            
            try{
                
                // Here we give each player their pebbles if they haven't got any
                if (playersPebbles.isEmpty() == true){ 
                    givePlayersPebbles();
                    //System.out.println("Player "+ Thread.currentThread().getId() + " Hand is "+ playersPebbles);
                    String nameOfFile = String.format("Player%s_output.txt", (Thread.currentThread().getId()-11) );
                    BufferedWriter writer = new BufferedWriter(new FileWriter(nameOfFile));
                    writer.close();
                }

                // The game runs until a player has won
                while (checkPlayerWon() == false){

                    /**
                     * This locks the methods for the current thread
                     * When the thread has finished processing those methods, 
                     * the system lets the other threads know that they can start processing
                     * This ensures that only one thread runs at a time
                     * 
                     */
                    synchronized(lock) {
                        removePebble();
                        getPebbles();
                        lock.notifyAll();
                    }

                }

                //System.out.println("Hand: " + playersPebbles);
                //System.out.println("Player " + Thread.currentThread().getId() + " has won");
                String nameOfFile = String.format("Player%s_output.txt", (Thread.currentThread().getId()-11) );
                BufferedWriter writer = new BufferedWriter(new FileWriter(nameOfFile, true));
                writer.write("Player " + (Thread.currentThread().getId()-11) + " has won");
                writer.close();
                System.exit(0);
            
            } catch (Exception e) {

                System.out.println(e);

            }
            
        }

    }

    /**
     * The main method creates the threads and calls the run function in the player class
     * 
     * @param args
     * @throws Exception
     *
     */
    public static void main(String[] args) throws Exception {

        //First method called is loadGame in the pebbleGame class 
        pebbleGame.loadGame(); 

        for (int i=0; i < pebbleGame.noPlayers; i++){ 

            Player object = new Player(); // Creates a new player
            object.start();

        }        
    }

}