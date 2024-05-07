public class UselessStringValueOfExample {    
    public String convert(int i) {
        String s = "Hello";
        String result = s + String.valueOf(i);   // case 1
        result = String.valueOf(i) + s;    // case 2
        result.append(String.valueOf(i)); // case 3
        return result;
    }
}
