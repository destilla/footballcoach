package CFBsimPack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold all the all-time league season records like passing/rushing yards, TDs, etc
 * Created by Achi Jones on 3/19/2016.
 */
public class LeagueRecords {

    public class Record {
        private int number;
        private String holder;
        private int year;

        public Record(int n, String h, int y) {
            number = n;
            holder = h;
            year = y;
        }

        public int getNumber() {
            return number;
        }

        public String getHolder() {
            return holder;
        }

        public int getYear() {
            return year;
        }
    }

    private HashMap<String, Record> records;

    public LeagueRecords(ArrayList<String> recordStrings) {
        records = new HashMap<String, Record>();
        String[] csv;
        for (String str : recordStrings) {
            csv = str.split(",");
            records.put(csv[0], new Record(Integer.parseInt(csv[1]), csv[2], Integer.parseInt(csv[3])));
        }
    }

    public LeagueRecords() {
        records = new HashMap<String, Record>();
        records.put("Team PPG", new Record(0, "XXX", 0));
        records.put("Team Opp PPG", new Record(500, "XXX", 0));
        records.put("Team YPG", new Record(0, "XXX", 0));
        records.put("Team Opp YPG", new Record(500, "XXX", 0));
        records.put("Team TO Diff", new Record(0, "XXX", 0));
        records.put("Pass Yards", new Record(0, "XXX", 0));
        records.put("Pass TDs", new Record(0, "XXX", 0));
        records.put("Interceptions", new Record(0, "XXX", 0));
        records.put("Comp Percent", new Record(0, "XXX", 0));
        records.put("Rush Yards", new Record(0, "XXX", 0));
        records.put("Rush TDs", new Record(0, "XXX", 0));
        records.put("Rush Fumbles", new Record(0, "XXX", 0));
        records.put("Rec Yards", new Record(0, "XXX", 0));
        records.put("Rec TDs", new Record(0, "XXX", 0));
        records.put("Catch Percent", new Record(0, "XXX", 0));
    }

    public void checkRecord(String record, int number, String holder, int year) {
        Record r = records.get(record);
        if (number > r.getNumber()) {
            records.remove(record);
            records.put(record, new Record(number, holder, year));
        }
    }

    public String getRecordsStr() {
        StringBuilder sb = new StringBuilder();
        Record r;
        for (Map.Entry<String, Record> record : records.entrySet()) {
            r = record.getValue();
            sb.append(record.getKey()+","+r.getNumber()+","+r.getHolder()+","+r.getYear());
            sb.append("\n");
        }
        return sb.toString();
    }



}
