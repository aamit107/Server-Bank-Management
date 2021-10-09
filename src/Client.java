import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable {
    BufferedReader reader;
    BufferedWriter writer;

    Client(Socket sc) {
        try {
            OutputStreamWriter o = new OutputStreamWriter(sc.getOutputStream());
            writer = new BufferedWriter(o);

            InputStreamReader isr = new InputStreamReader(sc.getInputStream());
            reader = new BufferedReader(isr);

        } catch (IOException e) {
            System.out.println("Exception at Client Constructor");
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                String data = reader.readLine();

                if (data.equals("addAccount")) {
//                    System.out.println("came here!! 1");

                    String name = reader.readLine();
                    String phoneNumber = reader.readLine();
                    String password = reader.readLine();

                    addAccount(new Account(name, phoneNumber, password));
                    System.out.println("account creation successful!!");
                }

                if (data.equals("deposit")) {
                    String phoneNumberFrom = reader.readLine();
                    String phoneNumberTo = reader.readLine();
                    double credit = Double.parseDouble(reader.readLine());

                    deposit(phoneNumberFrom, phoneNumberTo, credit);
                }

                if (data.equals("withdraw")) {
                    String phoneNumber = reader.readLine();
                    double credit = Double.parseDouble(reader.readLine());

                    withdraw(phoneNumber, credit);
                }

            } catch (Exception e) {
                System.out.println("Exception Occurred at Server Client");

                break;
            }
        }
    }

    private void withdraw(String phoneNumber, double credit) throws IOException, ClassNotFoundException {
        FileInputStream stream2 = new FileInputStream("src/accountInfo.txt");
        ObjectInputStream inputStream = new ObjectInputStream(stream2);

        ArrayList<Account> list = (ArrayList<Account>) inputStream.readObject();

        for (Account account : list) {
            if (account.phoneNumber.equals(phoneNumber)) {
                if (account.balance >= credit) {
                    account.withdrawal(credit);
                }
                break;
            }
        }
    }

    private void deposit(String phoneNumberFrom, String phoneNumberTo, double credit) throws IOException, ClassNotFoundException {
        FileInputStream stream2 = new FileInputStream("src/accountInfo.txt");
        ObjectInputStream inputStream = new ObjectInputStream(stream2);

        ArrayList<Account> list = (ArrayList<Account>) inputStream.readObject();

        for (Account account : list) {
//            for receiver to have track
            if (account.phoneNumber.equals(phoneNumberTo)) {
                account.deposit(phoneNumberFrom, phoneNumberTo, credit);
            }

//            for sender to have the track
            if (account.phoneNumber.equals(phoneNumberFrom)) {
                account.addStatement(phoneNumberFrom, phoneNumberTo, "deposit", credit);
            }
        }

    }

    void addAccount(Account account) throws IOException, ClassNotFoundException {
//        System.out.println("came here!! 111");

        ArrayList<Account> list;

        BufferedReader reader = new BufferedReader(new FileReader("src/accountInfo.txt"));
        if (reader.readLine() == null) {
            System.out.println("file is empty.");

            list = new ArrayList<>();
        } else {
            System.out.println("file not empty.");

            FileInputStream stream2 = new FileInputStream("src/accountInfo.txt");
            ObjectInputStream inputStream = new ObjectInputStream(stream2);

            list = (ArrayList<Account>) inputStream.readObject();
//            System.out.println("at line 127");
        }

        FileOutputStream stream = new FileOutputStream("src/accountInfo.txt");
//        System.out.println("at line 131");

        ObjectOutputStream outputStream = new ObjectOutputStream(stream);
//        System.out.println("at line 134");

        list.add(account);

        outputStream.writeObject(list);
        outputStream.close();

//        System.out.println("account added.");
        reader.close();
    }
}
