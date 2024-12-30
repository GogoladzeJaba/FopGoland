The purpose of this project is to correctly interpret my pseudo Goland-like language and be able to do ten basic algorithms. 
to do this, theres 10 different java class files each interpreting and handling the algorithm they were designed to. there is a main file that lets you pick which algorithm you'd like to select 
afterwards, the users able to input the value of N (the main variable), after which that specific class file will assign the value provided by the user to the variable N that is part of the pseudo-go code inside of that .java file. 

HOW TO USE:
1)Run SimpleInterpreter.java
2)Select which algorithm you want to test from the list by inputting its corresponding number written next to it.
3)You'll be asked to Input values for a variable(s) depending on what you selected, mostly you'll just be asked to input value for variable N.
4)The algorithm will correctly interpret and calculate what its designed to, ex: Inputting 101 in IntPalendromy check will output PALENDROME.
5)SimpleInterpreter.java will run in loop again and ask to select the algorithm you want once more, to exit Simply type in 0 and hit enter.

The code was written entirely by me as a solo project with help of GPT4. Nothing is plagiarised or taken from another persons code.

Some key notes and concerns about my project:
* I could not for the life of me correctly write an interpreter that reads and understand perfect actual GoLand code. So instead i settled for a Pseudo-Go-like language I've come up with and thats what the codes actually interpreting. as far as im aware this is okay but im specifiying that here anyways.
* Each algorithm also has a different interpreter that suits only its specific needs. I understand this isnt efficient nor the smartest solution however i couldn't make it work another way even after days of trying. The code i wrote still acts as a real interpreter, it still parses correctly and it outputs correct results so I believe it still satisfies the main point of the project, it just split into different parts.
* Scanner only lets you set the values of the relevant variables. for trying custom code written in the goland-like syntax that interpretors understand you could edit the "String program = String.format" part of the code in each .java file, but the scanner itself doesn't support entering it line by line. and each file is only designed to handle what its made for so i didn't bother adding this.
