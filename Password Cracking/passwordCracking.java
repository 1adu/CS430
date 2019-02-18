
// Alymbek Damir Uulu 

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class passwordCracking {
	public static void main(String args[]) throws IOException {
		
		//Using Jcrypt
		
		//Timer
		long startTime;
		long endTime;

		//I read the file and store each line as a separate element in my ArrayList 
		//Here: userList for passwords, and wordList for dictionary words
		ArrayList<String> userList = new ArrayList<String>();
		ArrayList<String> wordList = new ArrayList<String>();
		Scanner file = new Scanner(new File("wordlist.txt"));
		while (file.hasNext()) {
			wordList.add(file.next().trim()); // trim() removes unnecessary spaces.
		}
		
		//User input for fileName
		System.out.print("Input filename: ");
		Scanner reader = new Scanner(System.in);				
		String fileName = reader.next();
		
		//Keep line in ArrayList
		Scanner fileUsers = new Scanner(new File(fileName));
		while (fileUsers.hasNextLine()) {
			userList.add(fileUsers.nextLine().trim()); // trim() removes unnecessary spaces.
		}
		
		//Start here
		startTime = System.currentTimeMillis();

		//Cracked passwords are stored here
		String crackedPasswords = "";

		//First main loop that checks if our UNIX users have used their usernames, last names as their passwords. 
		//I had to mangle some of them
		for (int i = userList.size() - 1; i >= 0; i--) {

			//This String array helps me to separate line so I can easily access user's first name, username and etc.
			String[] parts = userList.get(i).split(":");
			String username = parts[0];
			String userFirstName = parts[4].split(" ")[0];
			String userLastName = parts[4].split(" ")[1];

			//Here I save different manglings to variables so it is easier to access them.
			// user.LastName.ToggleCase1 ex. sTrInG
			String toggle = "";
			for (int j = 0; j < userLastName.length(); j++) {
				if (j % 2 == 0)
					toggle += Character.toLowerCase(userLastName.charAt(j));
				else
					toggle += Character.toUpperCase(userLastName.charAt(j));
			}
			// user.LastName.ToggleCase2 ex. StRiNg
			String toggle1 = "";
			for (int j = 0; j < userLastName.length(); j++) {
				if (j % 2 != 0)
					toggle1 += Character.toLowerCase(userLastName.charAt(j));
				else
					toggle1 += Character.toUpperCase(userLastName.charAt(j));
			}
			
			// user.LastName.nCapitalize
			String userLastNamenCap = "";
			userLastNamenCap = userLastName.substring(0, 1).toLowerCase() + userLastName.substring(1).toUpperCase();
			
			// user.LastName.AllLowerCase.Reverse
			String userLastNameAllLowerCaseReverse = "";
			userLastNameAllLowerCaseReverse = reverse(userLastName.substring(0).toLowerCase());
			/*When the password is cracked the user is removed from the userList and password is saved in crackedPassword variable*/
			//Check for username. 
			if (jcrypt.crypt(parts[1].substring(0, 2), username).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + username + "\n");
				userList.remove(i);
			}

			//Check for reflection of username.
			if (jcrypt.crypt(parts[1].substring(0, 2), reverse(username)).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i),  "password: " + reverse(username) + "\n");
				userList.remove(i);
			}

			//Check for user's last name.
			if (jcrypt.crypt(parts[1].substring(0, 2), userLastName).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + userLastName + "\n");
				userList.remove(i);
			}
			//Check for toggle case 
			if (jcrypt.crypt(parts[1].substring(0, 2), toggle).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + toggle + "\n");
				userList.remove(i);
			}
			//Check for toggle case2
			if (jcrypt.crypt(parts[1].substring(0, 2), toggle1).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + toggle1 + "\n");
				userList.remove(i);
			}
			//Check for lastName.nCAPITALIZE
			if (jcrypt.crypt(parts[1].substring(0, 2), userLastNamenCap).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + userLastNamenCap + "\n");
				userList.remove(i);
			}
			//Check for userLastName with all lower cases and reversed.
			if (jcrypt.crypt(parts[1].substring(0, 2), userLastNameAllLowerCaseReverse).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + userLastNameAllLowerCaseReverse + "\n");
				userList.remove(i);
			}
			//Check for userLastName plus first character of user's first name
			if (jcrypt.crypt(parts[1].substring(0, 2), userLastName + userFirstName.charAt(0)).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + userLastName + userFirstName.charAt(0) + "\n");
				userList.remove(i);
			}
			
			//Check for userLastName plus first character of user's last name
			if (jcrypt.crypt(parts[1].substring(0, 2), userLastName + userLastName.charAt(0)).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + userLastName + userFirstName.charAt(0) + "\n");
				userList.remove(i);
			}
			
			//Check for userFirstName plus first character of user's last name
			if (jcrypt.crypt(parts[1].substring(0, 2), userFirstName + userLastName.charAt(0)).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + userFirstName + userLastName.charAt(0) + "\n");
				userList.remove(i);
			}
			
			//Check for userFirstName plus first character of user's first name
			if (jcrypt.crypt(parts[1].substring(0, 2), userFirstName + userFirstName.charAt(0)).equals(parts[1])) {
				crackedPasswords += String.format("%-75s  %s", userList.get(i), "password: " + userFirstName + userLastName.charAt(0) + "\n");
				userList.remove(i);
			}

		}
		
		//Second loop Statement that check for users that have password from the dictionary
		for (int i = 0; i < wordList.size(); i++) {
			
			// Now we apply mangling techniques to words from the list
			// We take the -> Run userList loop and check if we can find the password
			// If the password found the user is removed from the list 
			// But the main loop doesn't stop. 
			
			// We apply toggling to the word and keep them in variables
			String toggle = "";
			for (int m = 0; m < wordList.get(i).length(); m++) {
				if (m % 2 == 0)
					toggle += Character.toLowerCase(wordList.get(i).charAt(m));
				else
					toggle += Character.toUpperCase(wordList.get(i).charAt(m));
			}
			String toggle1 = "";
			for (int m = 0; m < wordList.get(i).length(); m++) {
				if (m % 2 != 0)
					toggle1 += Character.toLowerCase(wordList.get(i).charAt(m));
				else
					toggle1 += Character.toUpperCase(wordList.get(i).charAt(m));
			}

			// UserList for loop
			for (int j = userList.size() - 1; j >= 0; j--) {
				String[] parts = userList.get(j).split(":");

				// Check for toggleCase1
				if (jcrypt.crypt(parts[1].substring(0, 2), toggle).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + toggle + "\n");
					userList.remove(j);	 
				}
				
				// Check for toggleCase2
				else if (jcrypt.crypt(parts[1].substring(0, 2), toggle1).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + toggle1 + "\n");
					userList.remove(j);	 
				}
				
				// Check if the plain is a password
				else if (jcrypt.crypt(parts[1].substring(0, 2), wordList.get(i)).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + wordList.get(i) + "\n");
					userList.remove(j);
				}

				// Check for the word with Reverse function
				else if (jcrypt.crypt(parts[1].substring(0, 2), reverse(wordList.get(i))).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + reverse(wordList.get(i)) + "\n");
					userList.remove(j);
				}

				// wordFromList.RemoveFirstChar
				else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(1))).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + (wordList.get(i).substring(1))
							+ "\n");
					userList.remove(j);
					 
				}
				// wordFromList.RemoveLastChar
				else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(0, wordList.get(i).length() - 1)))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i).substring(0, wordList.get(i).length() - 1)) + "\n");
					userList.remove(j);
					 
				}

				// wordFromList + wordFromList (same word)
				else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i) + "" + wordList.get(i))).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i) + "" + wordList.get(i)) + "\n");
					userList.remove(j);
					 
				}

				// wordFromList + wordFromList.Reflection (same word)
				else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i) + "" + reverse(wordList.get(i))))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i) + "" + reverse(wordList.get(i))) + "\n");
					userList.remove(j);
					 
				}

				// wordFromList.Reflection + wordFromList (same word)
				else if (jcrypt.crypt(parts[1].substring(0, 2), (reverse(wordList.get(i)) + "" + (wordList.get(i))))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (reverse(wordList.get(i)) + "" + (wordList.get(i))) + "\n");
					userList.remove(j);
					 
				}

				// wordFromList.AllLowerChars
				else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(0).toLowerCase()))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i).substring(0).toLowerCase()) + "\n");
					userList.remove(j);
					 
				}

				// wordFromList.AllCapitalChars
				else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(0).toUpperCase()))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i).substring(0).toUpperCase()) + "\n");
					userList.remove(j);
					 
				}

				// wordFromlist.nCapitalize
				else if (jcrypt.crypt(parts[1].substring(0, 2),
						(wordList.get(i).substring(0, 1).toLowerCase() + wordList.get(i).substring(1).toUpperCase()))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i).substring(0, 1).toLowerCase()
									+ wordList.get(i).substring(1).toUpperCase())
							+ "\n");
					userList.remove(j);
					 
				}
				// wordFromlist.nCapitalize2
				else if (jcrypt.crypt(parts[1].substring(0, 2),
						(wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1).toLowerCase()))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i).substring(0, 1).toUpperCase()
									+ wordList.get(i).substring(1).toLowerCase())
							+ "\n");
					userList.remove(j);
					 
				}

				// wordFromlist.FirstCharCapital
				else if (jcrypt
						.crypt(parts[1].substring(0, 2),
								(wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1)))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1)) + "\n");
					userList.remove(j);
					 
				}
				// wordFromlist.FirstCharCapital.LastChapCapital
				else if (jcrypt
						.crypt(parts[1].substring(0, 2),
								(wordList.get(i).substring(0, 1).toUpperCase() + 
										wordList.get(i).substring(1,wordList.get(i).length()-1) +wordList.get(i).substring(wordList.get(i).length()-1, wordList.get(i).length()).toUpperCase() ))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (wordList.get(i).substring(0, 1).toUpperCase() + 
									wordList.get(i).substring(1,wordList.get(i).length()-1) +wordList.get(i).substring(wordList.get(i).length()-1, wordList.get(i).length()).toUpperCase() )
							+ "\n");
					userList.remove(j);
					 
				}

				// wordFromList.FirstCharCapital.RemoveLastChar
				else if (jcrypt.crypt(parts[1].substring(0, 2),
						((wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1))
								.substring(0, wordList.get(i).length() - 1)))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ ((wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1))
									.substring(0, wordList.get(i).length() - 1))
							+ "\n");
					userList.remove(j);
					 
				}

				// wordFromList.RemoveFirstChar.RemoveLastChar
				else if (jcrypt.crypt(parts[1].substring(0, 2),wordList.get(i).substring(1, wordList.get(i).length()-1)).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ wordList.get(i).substring(1, wordList.get(i).length()-1)
							+ "\n");
					userList.remove(j);
					 
				}
				
				// wordFromList.RemoveFirstChar.LastCharCapital
				else if (jcrypt.crypt(parts[1].substring(0, 2),wordList.get(i).substring(1) + wordList.get(i).substring(wordList.get(i).length()-1, wordList.get(i).length()-1)).equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ wordList.get(i).substring(1) + wordList.get(i).substring(wordList.get(i).length()-1, wordList.get(i).length()-1)
							+ "\n");
					userList.remove(j);
					 
				}
				
				// wordFromList.CapitalizeFirstChar.Reverse
				else if (jcrypt
						.crypt(parts[1].substring(0, 2),
								(reverse(wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1))))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (reverse(wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1)))
							+ "\n");
					userList.remove(j);
					 
				}

				// wordFromList.CapitalizeFirstChar.Reverse.RemoveFirstChar
				else if (jcrypt.crypt(parts[1].substring(0, 2),
						(reverse(wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1))
								.substring(1)))
						.equals(parts[1])) {
					crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: "
							+ (reverse(wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1))
									.substring(1))
							+ "\n");
					userList.remove(j);
					 
				}
			}
		}
		
		// After finishing two loops we now go to the third one where we apply random characters
		for (int i = 0; i < wordList.size(); i++) {

			// We start from ASCII table #32 - #126
			for (int k = 32; k < 127; k++) {	

				// UserList for loop
				for (int j = userList.size() - 1; j >= 0; j--) {
					String[] parts = userList.get(j).split(":");

					// Check if user added a character at the begin 
					if (jcrypt.crypt(parts[1].substring(0, 2), (char)k + "" + wordList.get(i)).equals(parts[1])) {
						crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + (char)k + "" + wordList.get(i) + "\n");
						userList.remove(j); 
					}
					
					// Check if user added a character at the end
					else if (jcrypt.crypt(parts[1].substring(0, 2), wordList.get(i) + "" + (char)k ).equals(parts[1])) {
						crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + wordList.get(i) + "" + (char)k + "\n");
						userList.remove(j); 
					}
					
					// Check for wordFromList.nCAPITALIZE + random Char
					else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(0, 1).toLowerCase() + wordList.get(i).substring(1).toUpperCase()) + "" + (char)k ).equals(parts[1])) {
						crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + (wordList.get(i).substring(0, 1).toLowerCase() + wordList.get(i).substring(1).toUpperCase()) + "" + (char)k + "\n");
						userList.remove(j); 
					}
					
					// Check for wordFromList.Ncapitalize + random Char
					else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1).toLowerCase()) + "" + (char)k ).equals(parts[1])) {
						crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + (wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1).toLowerCase()) + "" + (char)k + "\n");
						userList.remove(j); 
					}
					
					//Check for wordFromList.RemoveLastChar + Random Character
					else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(0, wordList.get(i).length() - 1)) + "" + (char)k ).equals(parts[1])) {
						crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + (wordList.get(i).substring(0, wordList.get(i).length() - 1)) + "" + (char)k + "\n");
						userList.remove(j); 
					}
					
					
					//Commented out because it can be written with less complicated if statement.
					/*else if (jcrypt.crypt(parts[1].substring(0, 2), (wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1, wordList.get(i).length() - 1)) + "" + (char)k ).equals(parts[1])) {
						crackedPasswords += String.format("%-75s  %s", userList.get(j), "password: " + (wordList.get(i).substring(0, 1).toUpperCase() + wordList.get(i).substring(1, wordList.get(i).length() - 1)) + "" + (char)k + "\n");
						userList.remove(j); 
					} */
					
				}
			}
		}

		// Finish here...
		// I am pretty Sure that there are passwords with some random passwords
		endTime = System.currentTimeMillis();
		System.out.println(crackedPasswords + "\nCracked " + (20 - userList.size()) + " passwords in " + (endTime - startTime) * .001 + " seconds ");
		
		
	}
	
	
	
	//Reverse function that returns the reflection of the String variable
	public static String reverse(String a) {
		int j = a.length();
		char[] newWord = new char[j];
		for (int i = 0; i < a.length(); i++) {
			newWord[--j] = a.charAt(i);
		}
		return new String(newWord);
	}

}
