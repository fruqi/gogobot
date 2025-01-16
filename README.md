# GOGOBOT!

## Intro

Gogobot is a CLI application that simulates a robot moving around a square tabletop, of dimension 5 x 5 units.<br>
The robot is free to roam and no other obstructions on the table surface.<br>
After you move the robot around, you can get the robot's current coordinate.

## Available commands

- `PLACE <x,y,NORTH|SOUTH|EAST|WEST>` - place the robot in x & y coordinate with face direction 
  - e.g. `PLACE 1,1,NORTH` will place the robot in x=1, y=1, facing NORTH.
- `MOVE` - move the robot one unit forward in a direction the robot is facing.
- `LEFT` - turn the face direction to the left.
- `RIGHT` - turn the face direction to the right.
- `REPORT` - announce the robot's current coordinate and its face direction.
- `EXIT` - exit the program. 

## Running The Application

### Java & Maven

If you have a Java 21 & Maven 3.9+ installed on your machine, <br>
you can easily run it locally with the following steps:

1. Clone this project into your local machine
2. In your terminal, go to the project directory
3. Then run `mvn clean package`
    - This will create a jar file (java binary file)
4. Once completed, go to `target/` folder
5. Then run `java -jar gogobot-<VERSION>.jar`

### Docker

If you have Docker installed on you machine, you can also run this application by following below steps:

1. Clone this project into your local machine
2. In your terminal, go to the project directory
3. Then run `docker build -t <your name or id>/gogobot:version .`
    - e.g. `docker build -t afaruqi/gogobot:latest .`
4. Run the following Docker command
    ```
    docker run -it <your image name> /bin/bash
    ```
5. Inside the Docker interactive terminal, you can run `gogobot` command

## Disclaimer

No actual robot was harmed during the process of building the program.<br>
Also, Chatgpt was used to in the process of creating `gogobot.sh` file.
