
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

class Parser{
    public static String Func= "" ;
    public static String [] Args ;
    Parser(){}
    Parser(String Input){
        String FuncName = Input.substring(0,Input.indexOf(" "));
        String ArgsNames = Input.substring(Input.indexOf(" "));
        Args[0] = ArgsNames.substring(0,Input.indexOf(" "));
        if(!ArgsNames.equals(Args[0])){
            Args[1] = ArgsNames.substring(Input.indexOf(" "));
        }
    }
}
class Terminal{
    public static Parser parser ;
    public static Vector<String> VectorHistory = new Vector<>();
    public static Path MyPath = Paths.get("");
    Terminal(String Input){
        parser = new Parser(Input);
    }
    public static void cd(){
        if(parser.Args[0].isEmpty() || parser.Args[0].equals("~")){
            MyPath = MyPath.getRoot();
        } else {
            if(parser.Args[0].equals("..") && parser.Args[0].length() == 2){
                MyPath = MyPath.getParent();
            }
            else{
                if(parser.Args[0].contains("..")){
                    String P1 = parser.Args[0].substring(0,parser.Args[0].indexOf("..")-1);
                    String P2 = parser.Args[0].substring(parser.Args[0].indexOf("..")+2);
                    Path PP1 = Paths.get(P1);
                    if(PP1.isAbsolute()){
                        if(Files.exists(PP1)){
                            MyPath = PP1.getParent();
                            if(Files.exists(Paths.get(MyPath.toString()+P2))){
                                MyPath = Paths.get(MyPath.toString()+P2);
                            }
                            else{
                                System.out.println("Sorry This Path is not Available");
                            }
                        }else{
                            System.out.println("Sorry This Path is not Available");
                        }
                    }
                    else{
                        if(Files.exists(PP1)){
                            if(Files.exists(Paths.get(MyPath.toString()+PP1.toString()))){
                                MyPath = Paths.get(MyPath.toString()+PP1.toString()).getParent();
                            }else{
                                System.out.println("Sorry This Path is not Available");
                            }
                        }else{
                            System.out.println("Sorry This Path is not Available");
                        }
                    }
                }
                else{
                    Path X = Paths.get(parser.Args[0]);
                    if(X.isAbsolute()){
                        if(Files.exists(X)){
                            MyPath = X;
                        }
                        else{
                            System.out.println("Sorry This Path is not Available");
                        }
                    }
                    else{
                        if(Files.exists(Paths.get(MyPath.toString()+X))){
                            MyPath = Paths.get(MyPath.toString()+X);
                        }
                        else{
                            System.out.println("Sorry This Path is not Available");
                        }
                    }
                }
            }
        }
        System.out.println("The new Path Is: " + MyPath.toString());
    }
    public static void pwd(){
        System.out.println(MyPath.toString());
    }
    public static void echo(){
        System.out.println(parser.Args[1]);
    }
    public static void ls(){
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(MyPath)) {
            for (Path file : stream) {
                System.out.println(file.getFileName());
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
    public static void mkdir(){
        MyPath = Paths.get(MyPath.toString()+parser.Args[0]);
        File newfolder = new File(String.valueOf(MyPath));
        if(Files.exists(MyPath)){
            System.out.println("Sorry This Directory Already Added");
        }else{
            boolean Created = newfolder.mkdir();
            if(Created){
                System.out.println("Created Successfully");
            }else{
                System.out.println("Sorry This Directory Can't Added");
            }
        }
    }
    public static boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();
    }
    public static void rmdir(){
        MyPath = Paths.get(MyPath.toString()+"\\"+parser.Args[0]);
        File newfolder = new File(String.valueOf(MyPath));
        if(!Files.exists(MyPath)){
            System.out.println("Sorry This Directory Is't Finded");
        }else{
            boolean Deleted = deleteDirectory(newfolder);
            if(Deleted){
                System.out.println("Deleted Successfully");
            }else{
                System.out.println("Sorry This Directory Can't Deleted");
            }
        }
    }
    public static void touch(){
        File Obj = new File(MyPath.toString()+parser.Args[0]);
        try{
            if(Obj.createNewFile()){
                System.out.println("File Created Is: " + Obj.getName());
            }else{
                System.out.println("File Is Already Exists");
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("Commanded Ended");
        }
    }
    public static void cp(){
        String First = parser.Args[1];
        String Last = parser.Args[2];
        System.out.println("First Is:" + First + "   Last Is:" + Last);
        if(First.substring(First.indexOf(".")).trim().equals(Last.substring(Last.indexOf(".")).trim())){
            try {
                File ReadedFile = new File(First);
                Scanner Reader = new Scanner(ReadedFile);
                String ReturnedText = "";
                while (Reader.hasNextLine()){
                    ReturnedText += Reader.nextLine();
                }
                Reader.close();
                try{
                    FileWriter Writer = new FileWriter(Last);
                    Writer.write(ReturnedText);
                    Writer.close();
                    System.out.println("Text Copied Successfully.");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void cpr(){
        /*
        String First = args[1];
        String Last = args[2];
        File directory1 = new File(MyPath+"\\"+First);
        File directory2 = new File(MyPath+"\\"+Last);
        try {
            Files.copy(directory1, directory2, REPLACE_EXISTING, COPY_ATTRIBUTES);
            System.out.println("Data Copied Successfully");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
    }
    public static void rm(){
        File newfile = new File(MyPath.toString()+"\\"+parser.Args[0]);
        boolean Deleted = deleteDirectory(newfile);
        if(Deleted){
            System.out.println("Deleted Successfully");
        }else{
            System.out.println("Sorry This File Can't Deleted");
        }
    }
    public static void cat(){
        try {
            File ReadedFile = new File(parser.Args[0]);
            Scanner Reader = new Scanner(ReadedFile);
            String ReturnedText = "";
            while (Reader.hasNextLine()){
                ReturnedText += Reader.nextLine();
            }
            Reader.close();
            System.out.println(ReturnedText);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void wc(){}
    public static void insert(){
        try{
            FileWriter Obj1 = new FileWriter(MyPath.toString()+parser.Args[0]);
            Obj1.write(parser.Args[1]);
            Obj1.close();
            System.out.println("File Is Updated by Data");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("Command Ended");
        }
    }
    public static void update(){
        try{
            FileWriter Objx = new FileWriter(MyPath.toString()+parser.Args[0],true);
            Objx.write("\n"+parser.Args[1]);
            Objx.close();
            System.out.println("File Is Updated by Data");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("Commend Ended");
        }
    }
    public static void history(){
        for (String Commend : VectorHistory){
            System.out.println(Commend);
        }
    }
    public static void ChooseCommand(){
        switch (parser.Args[0]){
            case "echo": echo() ; break;
            case "cd": cd(); break;
            case "pwd": pwd(); break;
            case "ls": ls(); break;
            case "mkdir": mkdir(); break;
            case "rmdir": rmdir(); break;
            case "touch": touch(); break;
            case "cp": cp(); break;
            case "cp-r": cpr();break;
            case "rm": rm(); break;
            case "cat": cat();break;
            case "wc": wc();break;
            case ">": insert();break;
            case ">>": update();break;
            case "history": history();break;
        }
    }
}
public class Main {
    public static void main(String[] args) {
    }
}