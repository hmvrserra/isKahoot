# PCD_Projeto
Repository for the Kahoot project for the course of Programação Concorrente e Distribuída

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
