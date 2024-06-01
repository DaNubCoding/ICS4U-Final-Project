import java.util.HashMap;

public class FeatureData extends HashMap<String, Object> {
    public FeatureData() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : keySet()) {
            sb.append(key);
            sb.append(",");
            sb.append(get(key));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
