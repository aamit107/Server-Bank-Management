import java.io.Serializable;
import java.util.ArrayList;

class Statements implements Serializable{
    String from;
    String to;
    String type;
    double credit;

    public Statements(String from, String to, String type, double credit) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.credit = credit;
    }
}


public class Account implements Serializable {
    String name;
    String phoneNumber;
    String password;
    ArrayList<Statements> statements;

    double balance = 0.0;
    int numberOfTransaction = 0;

    public Account(String name, String phoneNumber, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.statements = new ArrayList<>();
    }

    void deposit(String from, String to, double credit) {
        balance += credit;
        addStatement(from, to, "deposit", credit);
    }

    void withdrawal(double credit) {
        balance -= credit;
        addStatement(null, null, "withdrawal", credit);
    }

    void addStatement(String from, String to, String type, double credit) {
        statements.add(new Statements(from, to, type, credit));
    }
}
