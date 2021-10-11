package trashBucket;


import java.io.*;
import java.util.ArrayList;

public class t1 {
    public static void main(String[] args) throws IOException {
        FileInputStream stream = new FileInputStream("src/accountInfo.txt");
        ObjectInputStream inputStream = new ObjectInputStream(stream);

    }
}
