package testData;

class TestJava {
    private int testIntField = 10;

    protected int testProtectedIntField = 11;

    public int testPublicProtectedIntField = 12;

    static String testStringField = "sss";

    final String testFinalStringField = "teetet";


    public void testPublicMethod() {
        var anonymousClass = new FinalTestJava() {
            int sas2 = 11;

            @Override
            public void print() {
                System.out.println("print a");
            }
        };

        var anonymousClass2 = new testDerivedClass() {
            int sas = 11;

            @Override
            public void print() {
                System.out.println("print a");
            }
        };

        var lambda1 = ((String s) -> System.out.println(s));
        var lambda2 = ((String s) -> System.out.println(s));

    }

    private String testPrivateMethod(String arg) {
        return arg;
    }
}



public abstract class FinalTestJava {
    public void print();
}


interface InterfaceTest {

}