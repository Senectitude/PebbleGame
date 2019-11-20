package pebble_game;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;


public class PebbleGame {

    public static class pebbleGame {
    /*this class basically starts the game, initializes bags and loads them*/

        static int noPlayers;

        static ArrayList<Integer> X = new ArrayList<Integer>();
        static ArrayList<Integer> Y = new ArrayList<Integer>();
        static ArrayList<Integer> Z = new ArrayList<Integer>();

        static ArrayList<Integer> A = new ArrayList<Integer>();
        static ArrayList<Integer> B = new ArrayList<Integer>();
        static ArrayList<Integer> C = new ArrayList<Integer>();

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

        static void givePlayersPebbles() throws Exception {
            List<String> blackBags = Arrays.asList("X","Y","Z");
            ArrayList<Integer> bag;
            
            String blackBagName = blackBags.get(getRandomNumberInRange(0, blackBags.size()-1));
            if (blackBagName == "X") {
                bag = pebbleGame.X;
            } else if (blackBagName == "Y") {
                bag = pebbleGame.Y;
            } else if (blackBagName == "Z") {
                bag = pebbleGame.Z;
            } else {
                throw new Exception("Error");
            }
            for (int i=0; i < 10; i++){
                int location = getRandomNumberInRange(0, bag.size()-1);
                Integer item = bag.get(location);
                player.playersPebbles.add(item);
                bag.remove(location);
                player.fromWhere.add(blackBagName);
            }
        }
        
        static boolean checkPlayerWon() {
            Integer sum = 0;
            for (Integer i : player.playersPebbles){
                sum += i;
            }
            if (sum == 100 && player.playersPebbles.size() == 10){
                return true;
            }
            return false;
        }

        
    }


    private static ArrayList<Integer> loadBlackBags(ArrayList<Integer> list, Integer noPlayers, String list2) { //works 
        Integer[] weights = getWeights(list2);
        for (int i = 0; i < (noPlayers * 11); i++) {
            list.add(weights[(getRandomNumberInRange(0,weights.length-1))]);
        }
        return list;
    }

    private static Integer[] getWeights(String fileName) { //works
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

    private static int getRandomNumberInRange(int min, int max) {
        
		if (min > max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
        
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
    }
    

    public static class player extends Thread {
        static ArrayList<Integer> playersPebbles = new ArrayList<Integer>();
        static ArrayList<String> fromWhere = new ArrayList<String>();

        int location;
        Integer item;
        ArrayList<Integer> bag;
        String blackBagName;
        String emptyBagName = "";

        void getPebbles() throws Exception{ // check this works List<String> blackBags
            ArrayList<String> blackBags = new ArrayList<String>(Arrays.asList("X","Y","Z"));
            blackBagName = "";
            emptyBagName = "";

            System.out.println(blackBags);
            blackBagName = blackBags.get(getRandomNumberInRange(0, blackBags.size()-1));
            if (blackBagName == "X") {
                bag = pebbleGame.X;
            } else if (blackBagName == "Y") {
                bag = pebbleGame.Y;
            } else if (blackBagName == "Z") {
                bag = pebbleGame.Z;
            }
            
            if (bag.size() == 0) { // works
                System.out.println("Bag is empty " + blackBagName);
                blackBags.remove(blackBagName);
                System.out.println("New bags "+ blackBags);
                emptyBagName = blackBagName;
                blackBagName = blackBags.get(getRandomNumberInRange(0, 1));
                System.out.println("new bag is " + blackBagName);
                if (blackBagName == "X") {
                    bag = pebbleGame.X;
                } else if (blackBagName == "Y") {
                    bag = pebbleGame.Y;
                } else if (blackBagName == "Z") {
                    bag = pebbleGame.Z;
                }
            }
            
            System.out.println("empty " + emptyBagName);

            location = getRandomNumberInRange(0, bag.size()-1);
            System.out.println(bag.size());
            item = bag.get(location);//fine
            playersPebbles.add(item);
            bag.remove(location);

            fromWhere.add(blackBagName);
            System.out.println(blackBagName);

            if (emptyBagName.isEmpty() == true) {
                System.out.println("RUN");
                pebbleGame.whiteBags(emptyBagName);
            }
            //check the other bags
            if (pebbleGame.X.isEmpty() == true){
                pebbleGame.whiteBags("X");
            }else if (pebbleGame.Y.isEmpty() == true){
                pebbleGame.whiteBags("Y");
            }else if (pebbleGame.Z.isEmpty() == true){
                pebbleGame.whiteBags("Z");
            }
            
            System.out.println("Player " + Thread.currentThread().getId() + " has drawn a " + item + " from bag " + blackBagName);
            System.out.println("\n");
        }

        void removePebble() {
            location = getRandomNumberInRange(0, fromWhere.size()-1);
            item = playersPebbles.get(location);
            blackBagName = fromWhere.get(location);
            
            if (blackBagName == "X") {
                pebbleGame.A.add(item);
            } else if (blackBagName == "Y") {
                pebbleGame.B.add(item);
            } else if (blackBagName == "Z") {
                pebbleGame.C.add(item);
            }
            
            playersPebbles.remove(location);
            fromWhere.remove(location);
            
            System.out.println("Player " + Thread.currentThread().getId() + " Removed item: " + item + " from bag: " + blackBagName);

        }

        @Override
        public void run(){
            
            try{

                if (playersPebbles.isEmpty() == true){ //works
                    pebbleGame.givePlayersPebbles();
                }

                while (pebbleGame.checkPlayerWon() == false){
                    removePebble();
                    getPebbles(); //blackbags

                    System.out.println("A: " + pebbleGame.A);    
                    System.out.println("B: " + pebbleGame.B);
                    System.out.println("C: " + pebbleGame.C);

                    System.out.println("X: " + pebbleGame.X);
                    System.out.println("Y: " + pebbleGame.Y);
                    System.out.println("Z: " + pebbleGame.Z);

                }

                System.out.println("Player " + Thread.currentThread().getId() + " has won");
            
            } catch (Exception e) {
                System.out.println(e);
            }
            
        }

    }

    public static void main(String[] args) throws Exception {
        pebbleGame.loadGame(); // test with other stuff once we have the rest working

        for (int i=0; i < pebbleGame.noPlayers; i++){ //change to pebbleGame.noPlayers
            player object = new player();
            object.start();
            //object.join();  //method if it is called on any thread it will wait until the thread on which it is called terminates
        }
        
        
    }

}