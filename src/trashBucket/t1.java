package trashBucket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class t1 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/accountInfo.txt"));
        if (reader.readLine() == null) {
            System.out.println("file is empty.");
        }
    }
}
