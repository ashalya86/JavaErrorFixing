public class UseStringBufferLengthExample {
    public void example(){
        StringBuffer sb = new StringBuffer();
        if (sb.toString().equals("")) {}        // inefficient
        if (sb.length() == 0) {}                // preferred
    }
    
}
