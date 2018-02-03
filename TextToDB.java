package edu.yonsei.test.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.mysql.jdbc.Connection;

import edu.yonsei.util.Sentence;
import edu.yonsei.util.Token;

public class TextToDB_Real {

	private static final int DEFAULT_BUFFER_SIZE = 0;

	// Read title of documents in folder
	public static void listFilesForFolder(final File folder) throws Exception {
		String url = "jdbc:mysql://localhost:3306/trend";
		String username = "root";
		String password = "1234";

		Properties properties = new Properties();
		properties.setProperty(username, "root");
		properties.setProperty(password, "1234");

		System.out.println("Connecting database...");

		try (Connection connection = (Connection) DriverManager.getConnection(url, username, password)) {
			System.out.println("Database connected!");

			String query = "INSERT INTO doc_table(doc_id, series_id, series_name_id, series_num_id,"
					+ "date_id, date_year_id, value)" + "values(?,?,?,?,?,?,?)";

			PreparedStatement pstmt = connection.prepareStatement(query);
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					listFilesForFolder(fileEntry);
				} else {
					System.out.println(fileEntry.getName());

					// Read directory file's name in folder
					String fileDir = fileEntry.getName();
					String[] parts = fileDir.split("-");
					String series_id = parts[2];
					String series_name_id = series_id.substring(0, 1);
					String series_num_id = series_id.substring(2);
					String date_id = parts[3];
					String date_year_id_test = date_id.substring(0, 4);
					int date_year_id = Integer.parseInt(date_year_id_test);

					pstmt.setString(1, fileDir);
					pstmt.setString(2, series_id);
					pstmt.setString(3, series_name_id);
					pstmt.setString(4, series_num_id);
					pstmt.setString(5, date_id);
					pstmt.setInt(6, date_year_id);

					// Read contents of file
					Scanner s = new Scanner(new FileReader("C:/trend/txt/" + fileDir));
					File file = new File("C:/trend/txt/" + fileDir);
					FileInputStream fis = null;
					FileInputStream fis_temp = null;

					// **********
					String text_front;
					String text_back;
					Sentence sent_front;
					Sentence sent_back;
					Sentence sent;

					// **********
					// String[] string_array = null;
					// Array sqlArray = null;
					// ArrayList<String> list = new ArrayList<String>();
					FileWriter writer = new FileWriter("data/corpus/Temp_Text.txt", true);
					BufferedWriter bufferedWriter = new BufferedWriter(writer);

					String file_temp = "data/corpus/Temp_Text.txt";
					Scanner s_temp = new Scanner(new FileReader(file_temp));

					// ******************
					/*
					 * Scanner s_temp_empty = new Scanner(new
					 * FileReader("C:/trend/txt/"+fileDir)); String
					 * cont_string_empty = null; FileWriter writer_empty = new
					 * FileWriter("data/corpus/Temp_Text.txt", true);
					 * BufferedWriter bufferedWriter_empty = new
					 * BufferedWriter(writer_empty);
					 * 
					 * 
					 * while (s_temp_empty.hasNext()){ cont_string_empty =
					 * s_temp_empty.nextLine();
					 * if(cont_string_empty.equals(" ")){
					 * cont_string_empty.replaceAll(" ", "");
					 * cont_string_empty.trim(); }
					 * if(!cont_string_empty.isEmpty()){
					 * bufferedWriter_empty.write(cont_string_empty); } }
					 * bufferedWriter_empty.flush();
					 * bufferedWriter_empty.close(); writer_empty.close();
					 * s_temp_empty.close(); //********************
					 */

					try {

						System.out.println("Scanner File Root:" + fileDir);
						fis = new FileInputStream(file);
						String cont_string = null;

						while (s.hasNext()) {
							// read text - line by line

							cont_string = s.nextLine();

							// if line has not empty (빈칸), implement the
							// preprocess.
							if (!cont_string.equals(" ")) {
								// cont_string.replaceAll(" ", "");
								// cont_string.trim();

								if (!cont_string.isEmpty()) {

									if (cont_string.length() > 180) {
										text_front = cont_string.substring(0, cont_string.length() / 2);
										text_back = cont_string.substring(cont_string.length() / 2,
												cont_string.length());

										text_front = text_front.replaceAll("\n", " ").replaceAll("[\\[\\]]", "")
												.replaceAll(",", "");
										text_back = text_back.replaceAll("\n", " ").replaceAll("[\\[\\]]", "")
												.replaceAll(",", "");

										sent_front = new Sentence(text_front);
										sent_back = new Sentence(text_back);

										sent_front.preprocess();
										sent_back.preprocess();

										for (Token token : sent_front) {
											bufferedWriter.write(token.getLemma() + " " + "\n");

										}

										for (Token token : sent_back) {
											bufferedWriter.write(token.getLemma() + " " + "\n");
										}
									} else {
										if (cont_string.equals(" ")) {
											// ***여기서 잡음 !
										} else {
											try {
												cont_string = cont_string.replaceAll("\n", " ")
														.replaceAll("[\\[\\]]", "").replaceAll(",", "");
												sent = new Sentence(cont_string);
												sent.preprocess();

												for (Token token : sent) {
													bufferedWriter.write(token.getLemma() + " " + "\n");
												}
											} catch (java.lang.IllegalStateException e) {
												cont_string = cont_string.replaceAll(" ", "empty");

											}
										}
									}
								}

							}
						}
						bufferedWriter.flush();
						bufferedWriter.close();
						writer.close();
						s.close();

						List<String> myList_temp = new ArrayList<String>();

						while (s_temp.hasNext()) {
							String text_temp = s_temp.nextLine();
							myList_temp.add(text_temp);
						}

						String replace_1 = myList_temp.toString();
						System.out.println("replace_2");

						String replace_2 = replace_1.replaceAll("\n", " ").replaceAll("[\\[\\]]", "").replaceAll(",",
								"");
						System.out.println(replace_2);
						pstmt.setString(7, replace_2);
						
						//delete the List = myList_temp
						//delete the string = text_front, text_back, cont_string, replace_1, replace_2
						//delete the sentence = sent_front, sent_back, sent
						myList_temp.clear();
						text_front = null;
						text_back = null;
						cont_string = null;
						replace_1 = null;
						replace_2 = null;
						sent_front = null;
						sent_back = null;
						sent = null;
						
						PrintWriter writer_delete = new PrintWriter(file_temp);
						writer_delete.print("");
						writer_delete.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					pstmt.execute();

				}
			} // iteration = <for> end

		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		System.out.println("Loading driver...");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
	}

	public static void main(String[] args) throws Exception {
		// Read title of documents in folder
		final File folder = new File("C:/trend/txt");
		listFilesForFolder(folder);
	}
}