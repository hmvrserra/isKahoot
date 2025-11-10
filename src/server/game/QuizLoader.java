package server.game;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

public class QuizLoader {

    public static QuizFile loadQuizzes() {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader("quizzes.json");
            QuizFile quizFile = gson.fromJson(reader, QuizFile.class);
            return quizFile;
        } catch (FileNotFoundException e) {
            System.out.println("ERRO: Ficheiro do quiz não encontrado");
            e.printStackTrace();
            return null;
        }
    }

    // --- Método de Teste ---
    // (Podes executar este main para testar apenas esta funcionalidade)
    public static void main(String[] args) {
        // 1. Cria um ficheiro "quizzes.json" na raiz do teu projeto
        //    com o conteúdo de exemplo do enunciado [cite: 104-156].

        System.out.println("A tentar carregar quizzes.json...");
        QuizFile quizData = loadQuizzes();

        if (quizData != null) {
            System.out.println("Ficheiro carregado com sucesso!");

            // Obtém o primeiro quiz
            Quiz pcdQuiz = quizData.getQuizzes().get(0);
            System.out.println("Nome do Quiz: " + pcdQuiz.getName());

            // Obtém a primeira pergunta desse quiz
            Question firstQuestion = pcdQuiz.getQuestions().get(0);
            System.out.println("Primeira Pergunta: " + firstQuestion.getQuestion());
            System.out.println("Opções: " + firstQuestion.getOptions());
        } else {
            System.out.println("Falha ao carregar o ficheiro.");
        }
    }
}
