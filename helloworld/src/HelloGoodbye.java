public class HelloGoodbye {
    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        System.out.printf("Hello %s and %s", args[0], args[1]);
        System.out.println();
        System.out.printf("Goodbye %s and %s", args[1], args[0]);
        System.out.println();
    }
}
