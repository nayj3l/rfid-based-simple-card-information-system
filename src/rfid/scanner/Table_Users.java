package rfid.scanner;

public class Table_Users {
    
    String Card_Number, Name, Amount;

    public Table_Users(String Card_Number, String Name, String Amount) {
        this.Card_Number = Card_Number;
        this.Name = Name;
        this.Amount = Amount;
    }

    public String getCard_Number() {
        return Card_Number;
    }

    public void setCard_Number(String Card_Number) {
        this.Card_Number = Card_Number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }
    
}