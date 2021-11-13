package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * Stores a list of all the graded quizzes
 * <p>
 *
 * Graded Quiz Manager manages the list of quizzes, and provides methods for searching through them
 * @author Sean Lee
 * @see GradedQuizManager
 */
public class GradedQuiz { // should students be able to take quiz multiple times
    private Quiz quiz;
    private Student student;
    private String submissionTime;
    private int quizID;
    private int studentID;
    private HashMap<Integer, Integer> map = new HashMap<>();

//    public GradedQuiz(Quiz quiz, Student student) {
//        this.quiz = quiz;
//        this.student = student;
//    }

    public GradedQuiz(int quizID, int studentID) {
        this.quizID = quizID;
        this.studentID = studentID;
    }

    public GradedQuiz(int quizID, int studentID, HashMap<Integer, Integer> map, String submissionTime) {
        this.quizID = quizID;
        this.studentID = studentID;
        this.map = map;
        this.submissionTime = submissionTime;
    }

    public HashMap<Integer, Integer> getGradedQuizMap() {
        return this.map;
    }

    /**
     * Returns the ID of the quiz that got graded
     * @return Quiz ID
     */
    public int getQuizID() {
        return this.quizID;
    }

    /**
    * Returns the ID of the student that took the quiz
    * @return Student ID
     */
    public int getStudentID() {
        return this.studentID;
    }

    /**
     * Adds a question to the Hash Map of questions and answers
     * @param question
     * @param answer
     */
    public void addQuestion(Question question, Answer answer) {
        map.put(question.getId(), answer.getId());
    }

    /**
     * Returns string of ID of GradedQuiz
     * @return String of Graded Quiz ID
     */
    public String getID() {
        return String.format("G%d", quizID);
    }

    /**
     * Adds a question to the Hash Map of questions and answers
     * @param questionID
     * @param answerID
     */
    public void addQuestion(int questionID, int answerID) {
        map.put(questionID, answerID);
    }

    /**
     * Returns submission time of quiz
     * @return
     */
    public String getSubmissionTime() {
        return submissionTime;
    }

    /**
     * Sets the submission time of quiz
     * @param submissionTime
     */
    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }
}
