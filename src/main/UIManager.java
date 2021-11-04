package main;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Queue;

import ui.InputMenu;
import ui.Menu;
import ui.MenuOption;
import ui.MenuQuizList;
import ui.MenuState;
import ui.OptionMenu;
import ui.OptionMenuYesNo;
import utils.ANSICodes;

public class UIManager implements Manager {

	private LearningManagementSystem lms;
	
	private Scanner scanner;
	private User currentUser;

	private Menu MENU_START;
	private Menu MENU_LOGIN;
	private Menu MENU_CREATE_USER;

	private Menu MENU_MAIN;
	private Menu MENU_QUIZ_LIST_INFO; // Quiz List for Show Quiz Info
	private Menu MENU_QUIZ_LIST_TAKE; // Quiz List for Take Quiz
	
	private Menu MENU_TEACHER;
	private Menu MENU_QUIZ_LIST_MODIFY; // Quiz List for Modify Quiz
	private Menu MENU_ADD_QUIZ;
	
	public UIManager(LearningManagementSystem lms) {
		this.lms = lms;
	}

	/**
	 * Initializes all the static {@link Menu}'s and all of their relevant headers, options, inputs, and callbacks.
	 * <p>
	 * Contains most of the relevant structure of the UI of the application.
	 * <p>
	 * Uses {@link InputMenu}, {@link OptionMenu}, and other menus to create the UI.
	 * <p>
	 * See {@link #run()} for the entry point to the UI.
	 * 
	 */
	@Override
	public void init() {
		this.scanner = new Scanner(System.in);
		
		MENU_START = (new OptionMenu(this))
			.addHeading("Welcome to the Learning Management System!")
			.addSubheading("Please select one of the following options:")
			.addOption((new MenuOption("Login")).onSelect(() -> {
				MENU_LOGIN.open();
				return MenuState.RESTART;
			}))
			.addOption((new MenuOption("Create User")).onSelect(() -> {
				MENU_CREATE_USER.open();
				return MenuState.RESTART;
			}))
			.addOption((new MenuOption("Exit")).onSelect(() -> {
				System.out.println("Okay! Bye!");
				return MenuState.CLOSE;
			}));
		
		MENU_CREATE_USER = (new InputMenu(this))
			.addHeading("Creating a new user")
			.addSubheading("Please answer the following questions")
			.addInput("What is your name?", "Name")
			.addInput("What username do you want?", "Username")
			.addInput("What password do you want?", "Password")
			.addInputWithOptions(
				"Will you be a teacher or a student?", 
				new String[] { "Teacher", "Student" },
				"User Type"
			)
			.addValidationRequest()
			.onInputFinish((Map<String, String> values) -> {
				String name = values.get("Name");
				String username = values.get("Username");
				String password = values.get("Password");
				String userType = values.get("User Type");
				User user;
				// TODO User - for creating a user
				// TODO UserManager - for adding the user
				// User user = ?;
				// lms.getUserManager().addUser(user);
				System.out.println("Succesfully created the user.");
				return MenuState.CLOSE;
			});
		
		MENU_LOGIN = (new InputMenu(this))
			.addHeading("Logging into the Learning Management System.")
			.addSubheading("Please enter your login details.")
			.addInput("Username: ", "Username")
			.addInput("Password: ", "Password")
			.onInputFinish((Map<String, String> values) -> {
				String username = values.get("Username");
				String password = values.get("Password");
				// TODO UserManager - Authenticate username password and determine if valid
				// lms.getUserManager().authenticateUser(username, password):
				boolean correctUsernamePassword = true;
				if(correctUsernamePassword) {
					// TODO UserManager get User from Username and Password
					// lms.getUserManager().getUser(username, password);
					User user = new User();
					this.setCurrentUser(user);
					System.out.println("You have successfully logged in!");
					MENU_MAIN.open();
					return MenuState.CLOSE;
				}
				
				OptionMenuYesNo tryAgainMenu = new OptionMenuYesNo(this);
				tryAgainMenu.addHeading("Invalid login credentials.");
				tryAgainMenu.addSubheading("Would you like to try again?");
				tryAgainMenu.open();
				boolean isYes = tryAgainMenu.getResult();
				if(isYes) {
					System.out.println("Okay.");
					return MenuState.RESTART;
				}
				
				System.out.println("Okay. Going back to the main menu.");
				return MenuState.CLOSE;
			});
		
		MENU_MAIN = (new OptionMenu(this))
			.addHeading("Main Menu")
			.addSubheading("Please select one of the following options:")
			.addOption((new MenuOption("Teacher Menu"))
				.addVisibilityCondition(() -> {
					return this.getCurrentUser().hasPermissions();
				})
				.onSelect(() -> {
					MENU_TEACHER.open();
					return MenuState.RESTART;
				}))
			.addOption((new MenuOption("List All Quizzes"))
				.onSelect(() -> {
					MENU_QUIZ_LIST_INFO.open();
					return MenuState.RESTART;
				}))
			.addOption((new MenuOption("Take Quiz"))
				.onSelect(() -> {
					MENU_QUIZ_LIST_TAKE.open();
					return MenuState.RESTART;
				}))
			.addOption((new MenuOption("Logout"))
				.onSelect(() -> {
					this.setCurrentUser(null);
					return MenuState.CLOSE;
				}));
		
		MENU_TEACHER = (new OptionMenu(this))
			.addHeading("Teacher Menu")
			.addOption((new MenuOption("List Quizzes"))
				.onSelect(() -> {
					MENU_QUIZ_LIST_INFO.open();
					return MenuState.RESTART;
				}))
			.addOption((new MenuOption("Add New Quiz"))
				.onSelect(() -> {
					MENU_ADD_QUIZ.open();
					return MenuState.RESTART;
				}))
			.addOption((new MenuOption("Modify Quizzes"))
				.onSelect(() -> {
					MENU_QUIZ_LIST_MODIFY.open();
					return MenuState.RESTART;
				}))
			.addOption((new MenuOption("Exit Teacher Menu"))
				.onSelect(() -> {
					return MenuState.CLOSE;
				}));
		
		MENU_QUIZ_LIST_INFO = (new MenuQuizList(lms,
			(Quiz q) -> {
				System.out.println(ANSICodes.BOLD + "\nQuiz Info" + ANSICodes.RESET);
				// TODO Quiz - get quiz info... maybe a toString method?
				System.out.println(q.toString());
				System.out.println("Press Enter to go back.");
				this.getScanner().nextLine();
				return MenuState.RESTART;
			}
		)).addHeading("List of All Quizzes");
		
		MENU_QUIZ_LIST_TAKE = (new MenuQuizList(lms,
			(Quiz q) -> {
				System.out.println(ANSICodes.BOLD + "\nQuiz Info" + ANSICodes.RESET);
				// TODO Quiz - get quiz info... maybe a toString method?
				System.out.println(q.toString());
				OptionMenuYesNo menuTakeQuizQuestion = new OptionMenuYesNo(this);
				menuTakeQuizQuestion.addHeading("Would you like to take this quiz");
				menuTakeQuizQuestion.open();
				if(menuTakeQuizQuestion.resultWasYes()) {
					getMenuTakeQuiz(q).open();
					return MenuState.CLOSE;
				}
				System.out.println("Okay. Going back");
				return MenuState.RESTART;
			}
		)).addHeading("List of Quizzes to Take");
		
		MENU_QUIZ_LIST_MODIFY = (new MenuQuizList(lms,
			(Quiz q) -> {
				return MenuState.CLOSE;
			}
		)).addHeading("List of Quizzes to Change");
		
		MENU_ADD_QUIZ = (new InputMenu(this))
			.addInput("What would you like the name of this quiz to be?", "Name")
			.onInputFinish((Map<String, String> results) -> {
				String name = results.get("Name");
				OptionMenu menu = getMenuAddQuiz(name);
				menu.open();
				return MenuState.CLOSE;
			});
		
	}

	private OptionMenu getMenuAddQuiz(String name) {
		Quiz quiz = new Quiz(/* TODO name */);
		OptionMenu menu = (new OptionMenu(this))
				.onHeadingPrint(() -> {
					// TODO quiz.getName();
					System.out.println(ANSICodes.BOLD + "\nCreating Quiz: '"+name+"'" + ANSICodes.RESET);
					System.out.println("Current Amount of Questions: "); // TODO quiz.getQuestionAmount();
				})
				.addOption((new MenuOption("Import Quiz From File"))
					.onSelect(() -> {
						InputMenu changeNameMenu = ((new InputMenu(this)))
							.addHeading("Importing the quiz from a file.")
							.addInput("What is the file path?", "FilePath")
							.onInputFinish((Map<String, String> results) -> {
								String filePath = results.get("FilePath");
								// TODO QuizFileManager -> .canImport(filePath)
								if(Boolean.getBoolean("true")) {
									// TODO QuizFileManager -> .import(filePath, quiz); -> use setters to mutate quiz
									System.out.println("Successfully imported the quiz.");
									return MenuState.CLOSE;
								}
								OptionMenuYesNo verifyMenu = new OptionMenuYesNo(this);
								verifyMenu.addHeading("Invalid file.");
								verifyMenu.addSubheading("Would you like to try again?");
								verifyMenu.open();
								boolean isYes = verifyMenu.getResult();
								if(isYes)
									return MenuState.RESTART;
								return MenuState.CLOSE;
							});
						changeNameMenu.open();
						return MenuState.RESTART;
					}))
				.addOption((new MenuOption("Export Quiz to File"))
						.onSelect(() -> {
							InputMenu changeNameMenu = ((new InputMenu(this)))
								.addHeading("Exporting the quiz to a file.")
								.addInput("What is the file path?", "FilePath")
								.onInputFinish((Map<String, String> results) -> {
									String filePath = results.get("FilePath");
									// TODO QuizFileManager.canExport(filePath)
									if(Boolean.getBoolean("true")) {
										// TODO QuizFileManager.export(filePath, quiz);
										System.out.println("Successfully exported the quiz.");
										return MenuState.CLOSE;
									}
									OptionMenuYesNo verifyMenu = new OptionMenuYesNo(this);
									verifyMenu.addHeading("Invalid file.");
									verifyMenu.addSubheading("Would you like to try again?");
									verifyMenu.open();
									boolean isYes = verifyMenu.getResult();
									if(isYes)
										return MenuState.RESTART;
									return MenuState.CLOSE;
								});
							changeNameMenu.open();
							return MenuState.RESTART;
						}))
				.addOption((new MenuOption("Change Name"))
					.onSelect(() -> {
						InputMenu changeNameMenu = ((new InputMenu(this)))
							.addHeading("Changing the quiz name.")
							.addInput("What would you like the new name of the quiz to be?", "NewName")
							.onInputFinish((Map<String, String> newNameMap) -> {
								String newName = newNameMap.get("NewName");
								// TODO quiz.setName(setName);
								System.out.println("Changed the quiz name to " + name);
								return MenuState.CLOSE;
							});
						changeNameMenu.open();
						return MenuState.RESTART;
					}))
				.addOption((new MenuOption("Add Question"))
						.onSelect(() -> {
							InputMenu addQuestionMenu = getAddQuestionMenu(quiz);
							addQuestionMenu.open();
							return MenuState.RESTART;
						}))
				.addOption((new MenuOption("Save Quiz"))
					.onSelect(() -> {
						// TODO Save Quiz in Quiz Manager
						return MenuState.CLOSE;
					}))
				.addOption((new MenuOption("Cancel without saving"))
					.onSelect(() -> {
						OptionMenuYesNo verifyMenu = new OptionMenuYesNo(this);
						verifyMenu.addHeading("Are you sure you want to cancel making this quiz?");
						verifyMenu.open();
						boolean isYes = verifyMenu.getResult();
						if(isYes)
							return MenuState.CLOSE;
						System.out.println("Okay. Going back to creating the quiz.");
						return MenuState.RESTART;
					}));
		return menu;
	}

	private InputMenu getAddQuestionMenu(Quiz quiz) {
		InputMenu questionMenu = (new InputMenu(this))
			.addInputWithOptions("What type of question do you want to add?",
				new String[] {"Multiple Choice", "True or False"},
				"Type"
			)
			.addInput("What is the question?", "Question")
			.onInputFinish((Map<String, String> questionInfo) -> {
				String type = questionInfo.get("Type");
				String questionString = questionInfo.get("Question");
				// TODO Question Create question object
				Question question = new Question();
				// TODO Question -> question.setType(type);
				// TODO Question -> question.setQuestionString(questionString);
				OptionMenu menu = (new OptionMenu(this))
					.addHeading("Adding a new question to the quiz") // TODO Quiz -> "to quiz: " + quiz.getName()
					.addSubheading("Current point value: 0") // TODO Question -> "value: " + question.getValue()
					.addOption((new MenuOption("Add Answer"))
						.onSelect(() -> {
							InputMenu inpMenu = (new InputMenu(this))
								.addInput("What is the answer?", "Answer")
								.addInputWithOptions("Is it correct?",
									new String[] {"Yes", "No"}, "Correct")
								.onInputFinish((Map<String, String> answerInfo) -> {
									String answer = answerInfo.get("Answer");
									boolean correct = answerInfo.get("Correct").equals("Yes");
									// TODO Question - question.addAnswer(answer, correct);
									return MenuState.CLOSE;
								});
							inpMenu.open();
							return MenuState.RESTART;
						}))
					.addOption((new MenuOption("Remove Answer"))
						.onSelect(() -> {
							OptionMenu answerList = (new OptionMenu(this));
							answerList.addHeading("Select an answer to remove.");
							for(Answer a: question.getAnswers()) {
								answerList.addOption((new MenuOption(a.getName()))
									.onSelect(() -> {
										question.removeAnswer(a);
										return MenuState.CLOSE;
									}));
							}
							answerList.addOption((new MenuOption("Cancel")
								.onSelect(() -> {
									return MenuState.CLOSE;
								})));
							answerList.open();
							return MenuState.RESTART;
						}))
					.addOption((new MenuOption("Set Point Value"))
						.onSelect(() -> {
							InputMenu inpMenu = (new InputMenu(this))
								.addIntInput("What is the point value of the question?", "Value")
								.onInputFinish((Map<String, String> answerInfo) -> {
									String value = answerInfo.get("Value");
									int i = Integer.parseInt(value);
									// TODO Question -> question.setPointValue(i);
									return MenuState.CLOSE;
								});
							return MenuState.RESTART;
						}));
				menu.open();
				return MenuState.CLOSE;
			});
		return questionMenu;
	}
	
	private OptionMenu getMenuTakeQuiz(Quiz quiz) {
		// Queue is used to go from first to last question in order.
		Queue<OptionMenu> questionsMenus = new LinkedList<OptionMenu>();
		ArrayList<Question> questions = quiz.getQuestions(); // TODO Quiz - quiz.getQuestions();
		// TODO GradedQuiz create new graded quiz object.
		for(Question question: questions) {
			OptionMenu menu = (new OptionMenu(this));
			for(Answer a: question.getAnswers()) {  // TODO Question - question.getAnswers();
				menu.addOption((new MenuOption(a.getName())) // TODO Answer - answer.getName();
					.onSelect(() -> {
						// TODO GradedQuiz gradedQuiz.addAnswer(question, answer);
						questionsMenus.poll().open();
						return MenuState.CLOSE;
					}));
			}
			questionsMenus.add(menu);
		}
		OptionMenu menu = (new OptionMenu(this));
		menu.addHeading("You have finished the quiz.");
		menu.addSubheading("Would you like to submit it?");
		menu.addOption((new MenuOption("Submit"))
			.onSelect(() -> {
				// TODO GradedQuizManager - add graded quiz.
				System.out.println("Successfully submitted the quiz.");
				return MenuState.CLOSE;
			}))
			.addOption((new MenuOption("Cancel"))
				.onSelect(() -> {
					OptionMenuYesNo exitMenu = new OptionMenuYesNo(this);
					exitMenu.addHeading("Are you sure you want to cancel submitting this quiz?");
					exitMenu.addSubheading("If you cancel now, all your answers will be gone.");
					exitMenu.open();
					if(exitMenu.resultWasYes()) {
						System.out.println("Going back to the main menu.");
						return MenuState.CLOSE;
					} else {
						return MenuState.RESTART;
					}
				}));
		questionsMenus.add(menu);
		return questionsMenus.poll();
	}

	@Override
	public void exit() {
		this.scanner.close();
	}
	
	
	/**
	 * Runs the UI Loop by opening the Start Menu.
	 * <p>
	 * Most (if not all) of the menus that are branched from here are initialized in {@link #init()}
	 * 
	 * @see LearningManagementSystem#run()
	 */
	public void run() {
		System.out.print(ANSICodes.CLEAR_SCREEN);
		System.out.print(ANSICodes.CURSOR_TO_HOME);
		
		MENU_START.open();
	}
	
	public Scanner getScanner() {
		return this.scanner;
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
