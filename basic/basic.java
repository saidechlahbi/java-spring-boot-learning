class object{
    private String full_name;
    private int date;
    public object (String full_name, int date)
    {
        this.full_name = full_name;
        this.date = date;
    }
    public void setFull_name(String full_name){
        this.full_name = full_name;
    }
    public void setDate (int date){
        this.date = date;
    }
    public String getFull_name(){
        return full_name;
    }
    public int getDate(){
        return date;
    }
}

public class basic {
    public static void main(String args[])
    {
        object ahmed = new object("ahmed soltan", 30);
        System.out.printf("name = %s, age = %d\n", ahmed.getFull_name(), ahmed.getDate());
        // System.out.printf("ahmed");
        // for (int i = 0; i< 10; i++)
        //     System.out.printf("hello world\n");
    }
}