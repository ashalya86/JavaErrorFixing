public class ConsecutiveLiteralAppendsExample {
    public void example(){
        StringBuilder buf = new StringBuilder();
    buf.append("Hi").append(" ").append("World");    // poor
    buf.append("Hello World");                          // good

    buf.append('h').append('e').append('l').append('l').append('o'); // poor
    buf.append("hello");                                             // good

    buf.append(1).append('m');  // poor
    buf.append("1m");           // good
    }
}
