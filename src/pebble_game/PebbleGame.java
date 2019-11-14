package pebble_game;
import java.util.*;

public class PebbleGame {
    public static String dummyRange = "1,2,3,4,5,6,7,8,9,10";

    class Pebble {
    /*
    Pebble, when instantiated represents a pebble, holding its weight and the bag it is in
    */
        private String bagName;
        private String weight;

        public Pebble(String bagName, String weight){
            this.bagName = bagName;
            this.weight = weight;
        }
    }

    class Bag {
        
    }
    class BlackBag extends Bag {

    }
    class WhiteBag extends Bag {

    }

    static class Player {
        /*
        Player takes input nothing when instantiated, but holds the name of the player.
        This is just a demonstration of how we may deal with the user inputing the number of players playing.
        */
        private String name;

        public Player() {
            
        }

        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }

    }

    public static void main(String[] args) throws Exception {
        
        ArrayList<Player> players = new ArrayList<Player>();

        System.out.println("How many players are in this game?");
        Scanner scanner = new Scanner(System.in);
        int playerCount = scanner.nextInt();


        for(int i = 0; i < playerCount; i++)
        {
            System.out.print("What is Player " + (i + 1) + " name?\n");
            String playerName = scanner.next();
            Player plr = new Player();
            plr.setName(playerName);
            players.add(plr);
            
        }
        scanner.close();
        for (int j = 0; j < players.size(); j++){
            System.out.println(players.get(j).getName());
        }


    }
}