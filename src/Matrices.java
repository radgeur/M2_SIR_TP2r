import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Matrices {
	private File datasFile = new File("log-reco.txt");
	private File usersFile = new File("Users.txt");
	private File themesFile = new File("Themes.txt");
	private File mutFile = new File("Mut.txt");
	private File mutBinaireFile = new File("Mut_binaire.txt");
	private File mutDistanceThemesFile = new File("Mtt.txt");

	/**
	 * create the Mut matrix
	 * @throws IOException
	 */
	public void mut() throws IOException {
		ArrayList<String> usersList = new ArrayList<String>();
		ArrayList<String> themesList = new ArrayList<String>();
		int mut[][];
		Scanner scan = new Scanner(datasFile);
		String currentLine;
		String splitLine[];
		
		//To can write into the files
		FileWriter usersFw = new FileWriter(usersFile);
		BufferedWriter usersOutput = new BufferedWriter(usersFw);
		FileWriter themesFw = new FileWriter(themesFile);
		BufferedWriter themesOutput = new BufferedWriter(themesFw);
		FileWriter mutFw = new FileWriter(mutFile);
		BufferedWriter mutOutput = new BufferedWriter(mutFw);
		
		//read the log and write into the files
		while(scan.hasNext()){
			currentLine = scan.next();
			splitLine = currentLine.split(";");
			if(!usersList.contains(splitLine[1])) {
				usersList.add(splitLine[1]);
				usersOutput.write(splitLine[1]);
				usersOutput.write("\n");
				usersOutput.flush();
			}
			
			if(!themesList.contains(splitLine[2])) {
				themesList.add(splitLine[2]);
				themesOutput.write(splitLine[2]);
				themesOutput.write("\n");
				themesOutput.flush();
			}
		}
	
		usersOutput.close();
		themesOutput.close();
		scan.close();
		
		scan = new Scanner(datasFile);
		mut = new int[usersList.size()][themesList.size()];
		
		//read the log again to can fill the mutex
		while(scan.hasNext()) {
			currentLine = scan.next();
			splitLine = currentLine.split(";");
			mut[usersList.indexOf(splitLine[1])][themesList.indexOf(splitLine[2])] = mut[usersList.indexOf(splitLine[1])][themesList.indexOf(splitLine[2])] + 1;
		}
		
		//write into the mutex file
		for (int i=0;i<usersList.size();i++) {
			for(int j=0; j<themesList.size();j++) {
				mutOutput.write(mut[i][j] + ";");
				mutOutput.flush();
			}
			mutOutput.write("\n");
			mutOutput.flush();
		}
		mutOutput.close();
		scan.close();
	}
	
	/**
	 * create the binary Mut matrix
	 * @throws IOException
	 */
	public void mut_binaire() throws IOException{
		Scanner scan = new Scanner(mutFile);
		String currentLine;
		String splitLine[];
		
		//To can write into the files
		FileWriter mutBinaireFw = new FileWriter(mutBinaireFile);
		BufferedWriter mutBinaireOutput = new BufferedWriter(mutBinaireFw);
		
		//read the log and write into the files
		while(scan.hasNext()){
			currentLine = scan.next();
			splitLine = currentLine.split(";");
			for(int i = 0; i<splitLine.length;i++) {
				if(Integer.parseInt(splitLine[i]) != 0)
					mutBinaireOutput.write("1;");
				else
					mutBinaireOutput.write("0;");
			}
			mutBinaireOutput.write("\n");
		}
	
		mutBinaireOutput.close();
		scan.close();
	}
	
	/**
	 * create the mtt in Mtt.txt
	 * @throws IOException
	 */
	public void mut_distance_themes() throws IOException {
		int matrix[][] = new int[9][5];
		Scanner scan = new Scanner(mutBinaireFile);
		String currentLine;
		String splitLine[];
		
		//To can write into the files
		FileWriter mttFw = new FileWriter(mutDistanceThemesFile);
		BufferedWriter mttOutput = new BufferedWriter(mttFw);
		
		//fill the matrix
		int user=0;
		while(scan.hasNext()){
			currentLine = scan.next();
			splitLine = currentLine.split(";");
			for(int j=0;j<matrix[0].length;j++){
				matrix[user][j] = Integer.parseInt(splitLine[j]);				
			}
			user++;
		}
		scan.close();
		
		//write into Mtt.txt
		for(int i=0;i<matrix[0].length;i++){
			for(int j=0;j<matrix[0].length;j++){
				float union = 0;
				float intersection = 0;
				for(int k=0;k<matrix.length;k++){
					if(matrix[k][i] == 1 && matrix[k][j]==1){
						union++;
						intersection++;
					}
					else if(matrix[k][i] == 1 || matrix[k][j]==1)
						union++;
				}
				if(union!=0)
					mttOutput.write(1-(intersection/union) + ";");
				else
					mttOutput.write(0 + ";");
			}
			mttOutput.write("\n");
		}
		mttOutput.close();
	}
	
	
}
