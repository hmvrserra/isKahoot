# PCD_Projeto
Repository for the Kahoot project for the course of Programação Concorrente e Distribuída

## Project Structure

```
src/ 
├── client/ 
│   ├── gui/ 
│   │   ├── IsKahootClient.java (Janela principal do cliente - JFrame) 
│   │   ├── QuestionPanel.java (JPanel para mostrar perguntas e respostas) 
│   │   ├── ScoreboardPanel.java (JPanel para mostrar o placar) 
│   │   └── WaitingPanel.java (JPanel para o ecrã de espera)
│   │ 
│   └── net/ 
│       └── ServerConnection.java (Gere a ligação de rede com o servidor) 
│   
├── server/ 
│   ├── game/
│   │   ├── GameState.java (Gere o estado de uma sala de jogo) 
│   │   ├── Quiz.java (Armazena as perguntas lidas do JSON) 
│   │   ├── Player.java (Classe de dados para um jogador) 
│   │   └── Team.java (Classe de dados para uma equipa) 
│   │
│   ├── net/ 
│   │   ├── Server.java (Classe principal do servidor, aceita ligações) 
│   │   └── ClientHandler.java (Thread que comunica com um cliente)
│   │
│   ├── tui/ 
│   │   └── ServerTUI.java (Interface de texto para o admin do servidor)
│   │    
│   └── ServerMain.java (Classe com o main() para arrancar o servidor)
│
├── common/
│   └── dto/
│       ├── QuestionDTO.java (Objeto para enviar uma pergunta pela rede)
│       ├── AnswerDTO.java (Objeto para enviar uma resposta pela rede)
│       └── ScoreboardDTO.java (Objeto para enviar o placar pela rede)
│ 
└── concurrency/
    ├── ModifiedSemaphore.java (Implementação do semáforo modificado)
    └── ModifiedBarrier.java (Implementação da barreira modificada)
```