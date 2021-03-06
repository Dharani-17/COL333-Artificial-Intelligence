# Goal
The goal of this assignment is to find a solution to a real problem using ideas from sequential decision making under uncertainty (Markov Decision Processes).

# Scenario - The game of Blackjack

## Terminology

### Cards
A standard deck of playing cards is used, i.e., four suits (clubs, diamonds, spades, and hearts) and 13 different cards within each suit (the numbers 2 through 10, jack, queen, king, and ace). In this assignment, we will replace 10, jack, queen and king with a generic 'face card'. We will assume an infinite number of decks available in the pack.
### Card Values
The numbered cards (2 through 9) count as their numerical value. The generic face card (replacing 10, jack, queen, and king) counts as 10, and the ace may count as either 1 or 11 (whichever works in the player's favor).
### Hand Value
The value of a hand is the sum of the values of all cards in the hand. The values of the aces in a hand are such that they produce the highest value that is 21 or under (if possible). A hand where any ace is counted as 11 is called a soft hand. The suits of the cards do not matter in blackjack.

### Pair
The two card hand where both cards have the same value. (example, two aces, a pair of sixes, and for our assignment, a pair of face cards).

### Blackjack
It is a two-card hand where one card is an ace and the other card is any face card.

### Busted
A player is busted if the value of the hand has exceeded 21.

## Rules of play
There are some slight variations on the rules and procedure of blackjack. Below is the simplified procedure that we will use for this assignment. We will not be using insurance, surrender or dealer peeking, which are options in a standard casino game.
1. Each player places a bet on the hand.<br />
2. The dealer deals two cards to each player, including himself. The player's cards will be face-up. One of the dealers cards is face-up, but the other is face-down.<br />
3. The player must do one of the following:
   - H - Hit: the player receives one additional card (face up). A player can receive as many cards as he or she wants, but if the value of the player's hand exceeds 21, the player busts and loses the bet on this hand irrespective of dealer's hand.<br />
   - S - Stand: the player does not want any additional cards<br />
   - D - Double Down: before the player has received any additional cards, she may double-down. This means that the player doubles her bet on the hand and will receive exactly one additional card. The disadvantage of doubling-down is that the player cannot receive any more cards (beyond the one additional card); the advantage is that the player can use this option to increase the bet when conditions are favorable.<br />
   - P - sPlit: before the player has received any additional cards, if the original hand is a pair, then the player may split the hand. This means that instead of playing one hand the player will be playing two independent hands. She puts in the bet amount for the second hand. Two additional cards are drawn, one for each hand. The play goes on as if the player was playing two hands instead of one. If the drawn cards result in more pairs she is allowed to resplit. The player is allowed endless resplits in our version of the game.There is an exception associated with a pair of Aces, see below.<br />
4. Once the player stands, the dealer turns over his face-down card. The dealer then hits or stands according to the following deterministic policy: If the value of the hand is less than 17 (or soft 17), the dealer must hit. Otherwise, the dealer must stand. This means, that the dealer stands if his Cards are (A,2,4) because that makes a soft 17. If the dealer busts, then he loses the bets with the non-busted players.<br />

### Other Rules
1. Doubling is allowed after split. That means, after splitting the pair, the player is allowed to double down either or both her hands if she wishes to.<br />
2. Player can resplit as many times as she desires (whenever allowed).<br />
3. Splitting Aces- This is an exception to the rule. If the player gets a pair of aces that is a very strong hand. She can split this but she will only get one additional card per split hand, and she will not be allowed to resplit. Moreover, if the card is a face card, it will not be counted as blackjack, and will be treated as a regular 21.

## PayOffs (in this order)
1. If the player has a blackjack then she receives 2.5 times her bet (profit of 1.5). The only exception is if the dealer also got a blackjack, which is a push (player gets money back – no profit no loss).<br />
2. If the player busted, she lost her bet.<br />
3. If the dealer busted, he lost and the dealer pays the player double her bet, i.e., the player makes a profit equal to her bet.<br />
4. If the value of dealer's hand is greater than player's the player loses her bet.<br />
5. If the value of player's hand is greater than dealer's the player won and dealer pays double her bet.<br />
6. If the dealer has blackjack and the player has non-blackjack 21 the dealer wins.<br />
7. If the value of the two hands is equal, it is a push and the player gets back her bet money. That is, no profit no loss.<br />

# Problem Statement
In this assignment your task is to compute the policy for an optimal player. As usual the first step is to carefully think of the state space that you will need. Design a state transition function, and reward model to encode the dynamics of the play. Solve the game to compute the best play in each state. The best play is defined as the action (hit/stand/double/split) that maximizes the expected return. Make sure you double or split only in the states it is allowed. <br /><br />Assume that the player bets $1. Program for a BlackJack(p) game. Assume that the probability of getting a face card is p (an input to the program) and the probability of getting all other cards, 2-9 and Ace, is uniformly (1-p)/9. Note that p = 4/13 captures the standard Blackjack game.<br /><br />
After you solve the problem, the solution to BlackJack(4/13) should look very close to this. In the first column, representing your hand, a single integer represents the sum of the two cards, and indicates that they are not a pair and that neither is an ace. For the output of this assignment, you need to return only the first action that you will take. Thus your output need not distinguish between "D" and "DS".

# Output Format
The output format is very close to the policy in the link. The first value in a row is your hand – it goes from 5 to 19. Then we have special cases, first when one of the cards is an ace, A2 to A9 and finally pairs, 22 to 1010. After your hand there is a tab ‘\t’ and then there are 10 values for different open cards of the dealer (2 to 10 then ace). The letter will indicate your first action if you get this hand. [Here](https://github.com/pradyatiitd/COL333/blob/master/Blackjack/samplePolicy.txt) is a sample output and [here](https://github.com/pradyatiitd/COL333/blob/master/Blackjack/OptimumPolicy.txt) is the output for p=4/13 which captures the standard blackjack game <br /> <br />
As an example, the table above suggests that if you get a pair of 5s and the dealer got a 9 then you should double.
Use a tab as field separator between the "hand" field and the action fields. Use a space as field separator between the actions. Do not give a header or footer row, or any other extraneous output. Make sure you have the correct number of rows and colums. Note that there is no endline (‘\n’) after the last row.

# What is being provided
We provide an output checker, formatcheck.py, that tests whether your output is in the required format or not. To help students unfamiliar with shell scripting, we also provide a sample run.sh script that takes the input parameter from the command line and passes it to your own program (assuming your program is called BlackJackPlayer). Modify this file to replace “BlackJackPlayer” with the name of your executable.

# Important information
To familiarize yourself with the game, play it online for a few minutes. There are many online applets available like [this](http://www.hitorstand.net/game_m.html). The rules they follow may be slight variations of those used in the assignment, but you will get the general idea.
<br />
<br />
Please supply a compile.sh script and a run.sh script. We will run your code as follows: <br />
./compile.sh<br />
./run.sh 0.307<br />
<br />
The parameter in front of the ./run.sh will be the value p that represents the probability of the face card. When we call run.sh in the above format, it needs to save the policy to a file “Policy.txt” in the present working directory.
