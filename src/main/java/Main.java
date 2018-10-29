import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

	private static final Scanner scanner = new Scanner(System.in);
	final static String alphabet = "abcdefghijklmnopqrstuvwxyz";
    final static int N = alphabet.length();
    static char dice[][];
	public static void main(String[] args) {
	
	    dice = createRandomDice();
		List<ArrayList<String>> players = new ArrayList<ArrayList<String>>();
		System.out.println("DICE ");
		for(int i=0;i<dice.length;i++)
		{
			for(int j=0;j<dice[i].length;j++)
			{
				System.out.print(dice[i][j]+" ");
			}
			System.out.print("\n");

			System.out.println("_____________");
		}
		
		System.out.println(" Please enter number of players : ");
		int numberOfPlayers = scanner.nextInt();
		scanner.skip("(\r\n|[\n\r])?");
		
		for(int i=1;i<numberOfPlayers+1;i++)
		{
			System.out.println(" Please enter words found for the player "+ i + " (Use spaces between each word)");
			String[] results = scanner.nextLine().split(" ");
			ArrayList<String> player = new ArrayList<String>();
			player.addAll(Arrays.asList(results));
			ArrayList<String> lowered = (ArrayList<String>) player.stream().map(String::toLowerCase).collect(Collectors.toList());
			players.add(lowered);
		}
		
		if(numberOfPlayers==1)
			System.out.println("Total point :"+getScoreSinglePlayer(players.get(0)));
		else
		{
			List<Integer> results = getScoreMultiPlayer(players);
			for(int i=0;i<results.size();i++)
			{
				System.out.println(" Result for the player "+ i +" : " +results.get(i));
			}
		}
	}
	
	private static char[][] createRandomDice()
	{
		char[][] result = new char[5][5];
		Random r = new Random();

	    for (int i = 0; i < 25; i++) {
	         char t = alphabet.charAt(r.nextInt(alphabet.length()));
	         result[i%5][i/5]=t;
	    } 
	    return result;
	}

	public static List<ArrayList<Integer>> findCharPosition(char a)
	{
		List<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		for(int i=0;i<dice.length;i++)
		{
			for(int j=0;j<dice[i].length;j++)
			{
				if(a==dice[i][j])
				{
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.add(i);
					temp.add(j);
					result.add(temp);
				}
			}
		}
		return result;
	}
	
	public static boolean isAdjacent(char a, char b)
	{
		boolean result = false;
		List<ArrayList<Integer>> positionA = findCharPosition(a);
		List<ArrayList<Integer>> positionB = findCharPosition(b);
		
		for(int i=0;i<positionA.size();i++)
		{
			for(int j=0;j<positionB.size();j++)
			{
				if(
						positionA.get(i).get(0) + 1 == positionB.get(j).get(0) && positionA.get(i).get(1) == positionB.get(j).get(1) ||
						positionA.get(i).get(0) + 1 == positionB.get(j).get(0) && positionA.get(i).get(1) + 1 == positionB.get(j).get(1) ||
						positionA.get(i).get(0) + 1 == positionB.get(j).get(0) && positionA.get(i).get(1) - 1 == positionB.get(j).get(1) ||
						positionA.get(i).get(0) == positionB.get(j).get(0) && positionA.get(i).get(1) + 1 == positionB.get(j).get(1) ||
						positionA.get(i).get(0) == positionB.get(j).get(0) && positionA.get(i).get(1) - 1 == positionB.get(j).get(1) ||
						positionA.get(i).get(0) - 1 == positionB.get(j).get(0) && positionA.get(i).get(1) == positionB.get(j).get(1) ||
						positionA.get(i).get(0) - 1 == positionB.get(j).get(0) && positionA.get(i).get(1) + 1 == positionB.get(j).get(1) ||
						positionA.get(i).get(0) - 1 == positionB.get(j).get(0) && positionA.get(i).get(1) - 1 == positionB.get(j).get(1) 
				)
				{result = true; break;}
			}
		}
		return result;
	}
	
	public static int getScore(String word)
	{
		int result = 0;
		char[] chars = new char[word.length()]; 
		word.getChars(0, word.length(), chars, 0);
		
		boolean isValid = true;
		for(int i=0;i<chars.length-2;i++)
		{
			if(!isAdjacent(chars[i],chars[i+1]))
			{
				isValid=false;
				break;
			}
		}
		
		if(isValid)
		{
			if(chars.length==3)
				result ++;
			else if(chars.length>3 && chars.length<7)
				result += chars.length-3;
			else if(chars.length==7)
				result += 5;
			else if(chars.length>=8)
				result += 11;
		}
		
		return result;
	}
	
	public static int getScoreSinglePlayer(List<String> words)
	{
		int result = 0;
		for(String word: words)
		{
			if(isValidWord(word))
			result += getScore(word.toLowerCase());
		}
		return result;
	}
	
	public static List<Integer> getScoreMultiPlayer(List<ArrayList<String>> players)
	{
		List<Integer> resultList = new ArrayList<Integer>();		
		
		for(int i=0;i<players.size();i++)
		{
			List<String> player = players.get(i);
			int result = 0;
			for(String word: player)
			{
				if(isValidWord(word))
				{
					if(isUniqueWord(word.toLowerCase(),players,i))
						result++;
				}
				
			}
			resultList.add(result);
		}
		return resultList;
	}
	
	public static boolean isUniqueWord(String word, List<ArrayList<String>> players, int i)
	{
		boolean result = true;
		for(int j=i+1;j<players.size();j++)
		{
			List<String> player=players.get(j);
			if(player.contains(word))
				return false;
		}
		return result;
	}
	
	public static boolean isValidWord(String word)
	{
		boolean result=false;
		try(InputStream res = Main.class.getResourceAsStream("/wordlist-english.txt"))
		{
		    try(BufferedReader reader =
		        new BufferedReader(new InputStreamReader(res)))
		    {
		    	
		    String line = null;
		    
				while ((line = reader.readLine()) != null) {
				    if(word.equals(line))
				    {
				    		result=true;
				     	break;
				    }
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
				result=false;
			}
		}catch (IOException ex) {
				ex.printStackTrace();
				result=false;
		}
	 
	    return result;
	}
}
