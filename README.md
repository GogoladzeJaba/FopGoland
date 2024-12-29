The purpose of this project is to correctly interpret my pseudo Goland-like language and be able to do ten basic algorithms. 
to do this, theres 10 different java class files each interpreting and handling the algorithm they were designed to. there is a main file that lets you pick which algorithm you'd like to select 
afterwards, the users able to input the value of N (the main variable), after which that specific class file will assign the value provided by the user to the variable N that is part of the pseudo-go code inside of that .java file. 

HOW TO USE:
1)Run SimpleInterpreter.java
2)Select which algorithm you want to test from the list by inputting its corresponding number written next to it.
3)You'll be asked to Input values for a variable(s) depending on what you selected, mostly you'll just be asked to input value for variable N.
4)The algorithm will correctly interpret and calculate what its designed to, ex: Inputting 101 in IntPalendromy check will output PALENDROME.
5)SimpleInterpreter.java will run once again and ask to select the algorithm you want once more, to exit Simply type in 0 and hit enter.

The code was written entirely by me with help of GPT4. Nothing is plagiarised or taken from another persons code.

Some key notes and concerns about my project:
* I could not for the life of me correctly write an interpreter that reads and understand perfect actual GoLand code. So instead i settled for a Pseudo-Go-like language I've come up with and thats what the codes actually interpreting.
* Each algorithm also has a different interpreter that suits only its specific needs. I understand this isnt efficient nor the smartest solution however i couldn't make it work another way even after days of trying.
* The code i wrote still acts as a real interpreter, it still parses correctly and it outputs correct results so I believe it still satisfies the main point of the project.
* You also can't currently copy and paste an entire pseudo-go-like language block in my algorithms and run it, it only supports setting the variable value from the scanner. however that actual code block is indeed sitting in the algorithms code. as far as i know we arent required to be able to handle manual input of that degree so i didn't bother because attempting to implement that cuz it kept breaking everything. If you really want to do that though you can go into the .java files themsleves of the algorithms and manually edit the String program = String.format at the bottom of each of them. but there isnt much point to it.
