package tudbut.ttcpinvites;

import de.tudbut.tools.FileRW;
import tudbut.parsing.ArgumentParser;
import tudbut.parsing.TCN;

import java.io.IOException;
import java.util.Map;

public class Main {
    
    public static void main(String[] args) throws IOException, TCN.TCNException {
        Map<String, String> arguments = ArgumentParser.parseDefault(args);
        
        String n = arguments.get("n");
        String d = arguments.get("d");
        TCN tcn = TCN.read(new FileRW("data.tcn").getContent().join("\n"));
        if(n != null) {
            String i = arguments.get("i");
    
            TCN iTCN = tcn.getSub(i);
            TCN nTCN = TCN.getEmpty();
            nTCN.set("inviter", i);
            nTCN.set("points", 0);
            tcn.set(n, nTCN);
    
            iTCN.set("points", iTCN.getFloat("points") + 1f);
            for (int j = 2; j <= 4 && iTCN.map.containsKey("inviter"); j *= 2) {
                iTCN = tcn.getSub(iTCN.getString("inviter"));
                iTCN.set("points", iTCN.getFloat("points") + 1f / j);
            }
        }
        if(d != null) {
            String i = arguments.get("i");
    
            TCN iTCN;
            if(!d.equals("true")) {
                TCN dTCN;
                dTCN = tcn.getSub(d);
                iTCN = tcn.getSub(dTCN.getString("inviter"));
            }
            else {
                iTCN = tcn.getSub(i);
            }
            tcn.set(d, null);
        
            iTCN.set("points", iTCN.getFloat("points") - 1f);
            for (int j = 2; j <= 4 && iTCN.map.containsKey("inviter"); j *= 2) {
                iTCN = tcn.getSub(iTCN.getString("inviter"));
                iTCN.set("points", iTCN.getFloat("points") - 1f / j);
            }
        }
    
        new FileRW("data.tcn").setContent(tcn.toString());
    }
}
