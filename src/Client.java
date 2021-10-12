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

                if (data.equals("fetchInfo")) {
                    String phone = reader.readLine();

                    sendInfo(phone);
                }

                if (data.equals("sendMoney")) {
                    String from = reader.readLine();
                    String to = reader.readLine();
                    String amount = reader.readLine();
                    double credit = Double.parseDouble(amount);

                    withdraw(from, credit);
                    deposit(from, to, credit);
                }

            } catch (Exception e) {
//                System.out.println("Exception Occurred at Server Client");
                System.out.println("client left");
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

        FileOutputStream stream = new FileOutputStream("src/accountInfo.txt");
        ObjectOutputStream outputStream = new ObjectOutputStream(stream);

        outputStream.writeObject(list);
        outputStream.close();
    }

    private void deposit(String phoneNumberFrom, String phoneNumberTo, double credit) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("src/accountInfo.txt");
        ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);

        ArrayList<Account> list = (ArrayList<Account>) inputStream.readObject();
        inputStream.close();

        for (Account account : list) {
//            for receiver to have track
            if (account.phoneNumber.equals(phoneNumberTo)) {
                account.deposit(phoneNumberFrom, phoneNumberTo, credit);
            }

//            for sender to have the track
            if (account.phoneNumber.equals(phoneNumberFrom)) {
                account.addStatement(phoneNumberFrom, phoneNumberTo, "deposit", credit);
                account.numberOfTransaction++;

//                add bonus to the account for every 5 transaction
                if (account.numberOfTransaction % 5 == 0) {
                    account.balance += 10;
                    account.addStatement(null, null, "bonus", 10);
                }
            }
        }

        FileOutputStream stream = new FileOutputStream("src/accountInfo.txt");
        ObjectOutputStream outputStream = new ObjectOutputStream(stream);

        outputStream.writeObject(list);
        outputStream.close();
    }

    void addAccount(Account account) throws IOException, ClassNotFoundException {
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
        }

        FileOutputStream stream = new FileOutputStream("src/accountInfo.txt");
        ObjectOutputStream outputStream = new ObjectOutputStream(stream);

        list.add(account);

        outputStream.writeObject(list);
        outputStream.close();

//        System.out.println("account added.");
        reader.close();
    }

    void sendInfo(String phone) throws IOException, ClassNotFoundException {
        FileInputStream stream = new FileInputStream("src/accountInfo.txt");
        ObjectInputStream inputStream = new ObjectInputStream(stream);

        ArrayList<Account> list;
        list = (ArrayList<Account>) inputStream.readObject();
        for (Account account : list) {
            if (account.phoneNumber.equals(phone)) {
                writer.write(account.name + "\n");
                writer.flush();

                writer.write(account.balance + "\n");
                writer.flush();

                writer.write(account.password + "\n");
                writer.flush();


                for (int j = 0; j < account.statements.size(); j++) {
                    String s1 = account.statements.get(j).from;
                    String s2 = account.statements.get(j).to;
                    String s3 = account.statements.get(j).type;
                    String s4 = account.statements.get(j).credit + "";

                    String s;

//                    if the statement type is withdrawal or bonus
                    if (s3.equals("withdrawal") || s3.equals("bonus")) {
                        s = s3 + " " + s4;
                    } else s = s3 + " From " + s1 + " to " + s2 + " credit " + s4;

                    writer.write(s + "\n");
                    writer.flush();
                }
                break;
            }
        }

        writer.write("exit\n");
        writer.flush();
    }

}