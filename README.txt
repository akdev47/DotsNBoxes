Welcome dear tester to our Dots And Boxes Project created by yours truly, Ali Kaan Uysal (S3157385)
and Bas Van Dijk (s3140008). In order for you to have the best possible testing experience, we have created
this README.txt file, so you can follow along while grading. Happy grading!

Our Dots And Boxes Application supports both offline local gameplay, and you can connect to a server
using a address name and port number. To run our program, run the DotsAndBoxesGUI class inside the
package "view" We first recommend that you test that the offline gameplay is working. You can choose
among 3 offline modes, "Player VS Player", "Player VS AI", and "AI vs AI".

Before you look over our classes, you should understand our board structure. For your and our own
simplicity, we have represented the board in the "BOARDLINES.png" file. We recommend that you
navigate to this image to have a clear understanding of our board structure.

To run our program, navigate to src > main > java > BoardDrawingGame > view > DotsAndBoxesGUI.
Run the DotsAndBoxesGUI class. All of our game modes work smoothly, and you can test them one by one.
We recommend that you run "AI vs AI" first, and see that the game logic works as expected.
The two AI's will play until a game over, in which you can then change the game mode. After that,
we recommend that you run "Player VS Player. You can test all the edge cases such as single box
detection in the sides and in the middle of the board, as well as double box detection.
Finally, we recommend you play the last offline mode "Player VS AI", and try to beat our AI program.

We then recommend you to connect to our own server. For this to work, you will have to run our
"Game Server" class inside the "Server" package. Navigate to ServerClient > Server > GameServer.
Choose a port number, and run the server. Then, you can go back to the GUI you had open.
You should choose the "Connect To Server" option and follow the steps on screen.
YOU CAN ADJUST THE DIFFICULTY OF THE AI HERE!

After the first client is connected, you can either connect your own client or run
the "GUI2" class in order to have two local clients you can keep track of. Connect the other client,
and then you can queue for a game. When the first game is over, you will be redirected to the set-up
game screen. Here, before you can queue for a game again, you will have to choose your player again,
but you can choose the same player.

Quick note: We also created a TUI for the AI player and human player, these classes
work; however since we already have a perfectly functioning GUI, these classes are not really needed,
but we decided to keep it in the project anyway.

After you connect to our own server, you can also connect to any other server and test our client
in other servers, such as the (practice) server the university has provided
in which our AI has been dominating to be perfectly honest.

We hope you had a good time testing and running our Dots And Boxes program, and thank you!