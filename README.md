# Java Programming STEM Module

This STEM module teaches Java programming to students. Students implement a strategy to beat an opponent in a head-to-head, best of 10,000 Rock Paper Scissors game. The module ends with a bracket style tournament pitting each team's Java code against one another to decide the Rock Paper Scissors Champion!

## Installation

Download Student.jar and Instructor.jar.

## Usage

```bash
java -jar Student.jar
java -jar Instructor.jar
```

## 1. Coding Environment

![Coding Environment](https://github.com/kevinm422/RockPaperScissors-STEM-Module/raw/master/images/code_interface.png "Code")

### Helper Methods
```bash
1. Returns true or false depending on whether you won/tied/lost last game: wonLastGame() 
tiedLastGame() 
lostLastGame() 
2. Returns the value of your/their last play: 
myLastPlay() 
theirLastPlay() 
3. Returns value of ‘next’ hand in sequence: rock->paper->scissors->rock... cyclePlay() 
4. Returns the current game number (between 0 and 9,999) gameNumber() 
5. Return the value of your/their play at specified game number: getMyMove(<game_num>) 
getTheirMove(<game_num>) 
6. Return value of most thrown hand between 2 game numbers: myMostThrown(<game_num>, <game_num>) 
theirMostThrown(<game_num>, <game_num>) 
7. Return number of wins/ties/losses between 2 game numbers: winsBetween(<game_num>, <game_num>) 
tiesBetween(<game_num>, <game_num>) 
lossesBetween(<game_num>, <game_num>) 
8. Return win percentage between two game numbers: 
myWinPercentage(<game_num>, <game_num>) 
theirWinPercentage(<game_num>, <game_num>) 
```

## 2. Testing Interface
![Code Testing](https://github.com/kevinm422/RockPaperScissors-STEM-Module/raw/master/images/play.png "Test")

## 3. Tournament
![Code Testing](https://github.com/kevinm422/RockPaperScissors-STEM-Module/raw/master/images/instructor.png "Test")

## Acknowledgements

Uses RSyntaxTextArea for Java syntax highlighting.
https://github.com/bobbylight/RSyntaxTextArea

## License
[MIT](https://choosealicense.com/licenses/mit/)
2021
