package pebble_game;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class PebbleGame {

    public static class pebbleGame {

        static int noPlayers = 0;

        static ArrayList<Integer> X = new ArrayList<Integer>();
        static ArrayList<Integer> Y = new ArrayList<Integer>();
        static ArrayList<Integer> Z = new ArrayList<Integer>();

        ArrayList<Integer> A = new ArrayList<Integer>();
        ArrayList<Integer> B = new ArrayList<Integer>();
        ArrayList<Integer> C = new ArrayList<Integer>();

        static void createBlackBags() {
            // initailse the scanner
            Scanner in = new Scanner(System.in); // need to close this

            while (noPlayers < 0) {

                System.out.println("Please enter the number of players: ");
                noPlayers = in.nextInt();

                if (noPlayers < 0) {
                    System.out.println("Please enter a positive number of players");
                }

                if (noPlayers == 0) {
                    System.out.println("The number of players can't be 0");
                }
            }
            
            //Maybe loop this, may need a list of [X,Y,Z] anyway, for the picking of random bags
            String fileName;
            System.out.println("Please enter the location of the first bag");
            fileName = in.nextLine();
            X = loadBlackBags(X, noPlayers, fileName);

            System.out.println("Please enter the location of the second bag");
            fileName = in.nextLine();
            Y = loadBlackBags(Y, noPlayers, fileName);

            System.out.println("Please enter the location of the third bag");
            fileName = in.nextLine();
            Z = loadBlackBags(Z, noPlayers, fileName);
        }


        //static void whiteBags() {

        //}

        //static void givePlayersPebbles(){
            //give player 10 pebbles from random bags
        //}

        /*
            Need checks for,
            If someone won
            If a black bag is full

        */
        

    }


    private static ArrayList<Integer> loadBlackBags(ArrayList<Integer> list, Integer noPlayers, String list2) { //works 
        Integer[] weights = getWeights(list2);
        for (int i = 0; i < (noPlayers * 11); i++) {
            list.add(weights[(getRandomNumberInRange(0,weights.length))]);
        }
        return list;
    }

    private static Integer[] getWeights(String fileName) { // needs checking
        ArrayList<Integer> weights = new ArrayList<>();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName)); //need to close  this
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                for (int i = 0; i< tokens.length; i++ ){
                    weights.add(Integer.parseInt(tokens[i].trim()));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        
        return weights.toArray(new Integer[0]);
    }

    private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
    }
    

    public class player extends Thread {
        ArrayList<Integer> playersPebbles = new ArrayList<Integer>();
        ArrayList<String> fromWhere = new ArrayList<String>();
        List<String> blackBags = Arrays.asList("X","Y","Z");

        void getPebbles(){
            String bag = blackBags.get(getRandomNumberInRange(0,blackBags.size()));
            if (bag == "X") {
                playersPebbles.add(pebbleGame.X.get(getRandomNumberInRange(0,pebbleGame.X.size()))); 
            } else if (bag == "Y") {
                playersPebbles.add(pebbleGame.Y.get(getRandomNumberInRange(0,pebbleGame.Y.size())));
            }else{
                playersPebbles.add(pebbleGame.Z.get(getRandomNumberInRange(0,pebbleGame.Z.size())));
            }
            //easy other if stateents
            //delete item from bag
            
            fromWhere.add(bag);
        }

        /*
        static void removePebbles(){

        }

        */
        

    }

    public void main(String[] args) throws Exception {
        System.out.println("Please enter the location of bag number 0 to load: ");
        // create threads here
        
    }

}