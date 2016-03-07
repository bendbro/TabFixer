import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ben on 3/6/16.
 */
public class Main {
    public static void main(String[] args) {
        List<String> argList = new ArrayList<>();
        for(String arg : args) {
            argList.add(arg);
        }

        String tabs = "    ";
        int tabSettingIndex = argList.indexOf("-t");
        if(tabSettingIndex != -1) {
            tabs = argList.get(tabSettingIndex+1);
        }

        Set<String> positiveTabTriggers = new HashSet<>();
        int positiveTabTriggerIndex = argList.indexOf("-p");
        if(positiveTabTriggerIndex != -1) {
            String[] triggers = argList.get(positiveTabTriggerIndex+1).split(" ");
            for(String trigger : triggers) {
                positiveTabTriggers.add(trigger);
            }
        } else {
            positiveTabTriggers.add("{");
        }

        Set<String> negativeTabTriggers = new HashSet<>();
        int negativeTabTriggerIndex = argList.indexOf("-n");
        if(negativeTabTriggerIndex != -1) {
            String[] triggers = argList.get(negativeTabTriggerIndex+1).split(" ");
            for(String trigger : triggers) {
                negativeTabTriggers.add(trigger);
            }
        } else {
            negativeTabTriggers.add("}");
        }

        Scanner s = new Scanner(System.in);
        Pattern pattern = Pattern.compile("\\s*");
        int depth = 0;
        while(s.hasNext()) {
            String line = s.nextLine();

            List<List<String>> foundTriggers = new ArrayList<>(line.length());
            for(int i = 0; i < line.length(); i++) {
                foundTriggers.add(new ArrayList<String>());
            }

            for(String n : negativeTabTriggers) {
                int indexOf = line.indexOf(n);
                while(indexOf != -1) {
                    foundTriggers.get(indexOf).add(n);
                    indexOf = line.indexOf(n,indexOf+1);
                }
            }

            for(String p : positiveTabTriggers) {
                int indexOf = line.indexOf(p);
                while(indexOf != -1) {
                    foundTriggers.get(indexOf).add(p);
                    indexOf = line.indexOf(p, indexOf+1);
                }
            }

            int current = 0;
            int next = 0;
            boolean fresh = true;
            for(int i = 0; i < foundTriggers.size(); i++) {
                List<String> ti = foundTriggers.get(i);
                if(ti.size() > 0) {
                    if(negativeTabTriggers.contains(ti.get(0))) {
                        if(fresh) {
                            current = -1;
                            fresh = false;
                        }
                        next--;
                    } else {
                        fresh = false;
                        next++;
                    }
                }
            }
            String indent = "";
            for (int i = 0; i < depth + current; i++) {
                indent += tabs;
            }
            Matcher matcher = pattern.matcher(line);
            System.out.println(matcher.replaceFirst(indent));

            if(next > 0) {
                depth++;
            } else if(next < 0) {
                depth--;
            }
        }
    }
}
