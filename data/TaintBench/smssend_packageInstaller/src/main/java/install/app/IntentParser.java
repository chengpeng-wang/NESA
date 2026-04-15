package install.app;

public class IntentParser {
    public String action = "";
    public String category = "";
    public String cmp = "";
    public String data = "";
    public String flag = "";

    /* access modifiers changed from: 0000 */
    public void parseString(String data) {
        try {
            String markerStart = "{";
            String markerEnd = "}";
            int startIndex = data.indexOf(markerStart);
            if (startIndex != -1) {
                startIndex += markerStart.length();
                int endIndex = data.indexOf(markerEnd, startIndex);
                if (endIndex != -1) {
                    String[] params = data.substring(startIndex, endIndex).trim().split(" ");
                    for (String param : params) {
                        String[] keyValue = param.split("=");
                        String key = keyValue[0];
                        String value = keyValue[1];
                        if (key.equals("act")) {
                            this.action = value;
                        } else if (key.equals("cat")) {
                            this.category = value;
                        } else if (key.equals("flg")) {
                            this.flag = value;
                        } else if (key.equals("dat")) {
                            this.data = value;
                        } else if (key.equals("cmp")) {
                            this.cmp = value;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("IntentParser Error: " + ex.getMessage());
        }
    }
}
