# is Kahoot
Repository for the Kahoot! project for the course of Concurrent and Distributed Programming

## Project Structure

```
src/
├── client/
│   ├── gui/
│   │   ├── GameOverPanel.java (JPanel for game over screen with animations)
│   │   ├── IsKahootClient.java (Main client window - JFrame, has test main method)
│   │   ├── QuestionPanel.java (JPanel for displaying questions and answers)
│   │   ├── ScoreboardPanel.java (JPanel for displaying the scoreboard)
│   │   └── WaitingPanel.java (JPanel for the waiting screen)
│   └── net/
│       └── ServerConnection.java (Manages network connection with server)
├── common/
│   ├── Answer.java (Answer data class)
│   ├── GameOverDTO.java (DTO for game over data transfer)
│   ├── LoginRequest.java (DTO for login request data)
│   ├── LoginResponse.java (DTO for login response data)
│   ├── QuestionDTO.java (DTO for sending questions over network)
│   └── ScoreboardDTO.java (DTO for sending scoreboard over network)
├── concurrency/
│   ├── Barrier.java (Barrier synchronization implementation)
│   └── CountDownLatch.java (CountDownLatch synchronization implementation)
└── server/
    ├── game/
    │   ├── GameState.java (Manages game room state)
    │   ├── Player.java (Player data class)
    │   ├── Question.java (Question data class)
    │   ├── Quiz.java (Stores questions read from JSON)
    │   ├── QuizFile.java (Quiz file handling utilities)
    │   ├── QuizLoader.java (Loads quizzes from JSON, has test main method)
    │   └── Team.java (Team data class)
    └── net/
        ├── ClientHandler.java (Thread that communicates with a client)
        └── Server.java (Main server class, accepts connections, has main method)
        
quizzes.json (JSON file containing quiz data)
```

## How to Run (Using IntelliJ IDEA)

### Prerequisites
- IntelliJ IDEA installed
- JDK configured in IntelliJ
- Gson library included in `lib/gson-2.13.2.jar`

### Step-by-Step Instructions

1. **Open the Project in IntelliJ IDEA**
   - Launch IntelliJ IDEA
   - Select `File > Open`
   - Navigate to the project directory (`PCD_Projeto`) and click `Open`

2. **Configure the Library**
   - Right-click the project root in the Project view
   - Select `Open Module Settings` (or press `F4`)
   - Go to `Libraries`
   - Click the `+` button and select `Java`
   - Navigate to `lib/gson-2.13.2.jar` and select it
   - Click `OK` to add the library

3. **Run the Server**
   - In the Project view, navigate to `src/server/net/Server.java`
   - Right-click `Server.java` and select `Run Server.main()`
   - The server will start and display a console
   - In the console, create a game room by typing:
     ```
     new <numTeams> <numPlayersPerTeam>
     ```
     Example: `new 2 3` (creates a room with 2 teams, 3 players per team)
   - Note the room code displayed (e.g., `QuizName_Room123`)

4. **Run the Client(s)**
   - In the Project view, navigate to `src/client/gui/IsKahootClient.java`
   - Right-click `IsKahootClient.java` and select `Run IsKahootClient.main()`
   - In the Run/Debug Configurations dialog that appears, go to `Program arguments` and enter:
     ```
     localhost 2025 <Room> <Team> <Username>
     ```
     Replace `<Room>` with the room code from step 3, `<Team>` with the team name, and `<Username>` with the player's name.
     Example: `localhost 2025 QuizName_Room123 Team1 Player1`
   - Click `Run`
   - Repeat for additional players, changing the team and username as needed

### Game Flow
1. Start the server and create a room as described above.
2. Launch client instances for each player, connecting to the created room.
3. Once all expected players have joined (based on the room configuration), the game will start automatically.
4. Players answer questions, view scoreboards between rounds, and see the final results.
5. The game ends when all questions are answered.

Note: The game supports multiple teams and players. Ensure the number of running clients matches the room configuration specified when creating the room.
